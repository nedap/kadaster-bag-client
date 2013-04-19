package com.nedap.healthcare.kadasterbagclient.api.service;

import javax.validation.constraints.NotNull;
import javax.xml.ws.WebServiceException;

import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.ApplicatieException;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.IBagVsRaadplegenDatumADOV20090901;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOAdres;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.APD;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.NUMPostcodeAdres;
import nl.kadaster.schemas.imbag.apd.v20090901.Verblijfsobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.exception.UnExistingLocation;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.api.util.BasselCoordinates;
import com.nedap.healthcare.kadasterbagclient.api.util.CoordinatesConverterUtil;
import com.nedap.healthcare.kadasterbagclient.api.util.DateTimeUtil;
import com.nedap.healthcare.kadasterbagclient.api.util.RDCoordinates;

/**
 * {@link LocationServiceHelper} implementation.
 * 
 * @author Dusko Vesin
 */
@Service
class LocationServiceImpl implements LocationServiceHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private IBagVsRaadplegenDatumADOV20090901 clientService;

    @Override
    public AddressDTO getAddress(@NotNull final String zipCode, @NotNull final Integer houseNumber,
            final String extension) throws UnExistingLocation, FaildCommunicationWithServer, ApplicatieException {

        LOGGER.debug(String.format("Find location in Netherlands for zipCode: %s, houseNumber: %s and extension: %s ",
                zipCode, houseNumber, extension));
        
        AntwoordberichtAPDADO result = null;
        Verblijfsobject object = null;
        try {
            result = clientService
                    .zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(wrapZipCodeAndHouseNumberToVraagberichtAPDADOAdres(
                            zipCode, houseNumber, extension));

            // if response is null or without kadaster address - throw not found exception
            if (result != null && result.getAntwoord().getProducten().getADOProduct().size() > 0) {
                object = result.getAntwoord().getProducten().getADOProduct().get(0).getVerblijfsobject();
            } else {
                LOGGER.info("Response doesn't contain any addresses");
                throw new UnExistingLocation(zipCode, houseNumber);
            }

            // log if more than one kadaster address is returned from service
            if (result.getAntwoord().getProducten().getADOProduct().size() > 1) {
                LOGGER.info("More than one address was returned, found "
                        + result.getAntwoord().getProducten().getADOProduct().size());
            }
        } catch (final ApplicatieException e) {
            throw new UnExistingLocation(zipCode, houseNumber);
        } catch (final WebServiceException ex) {
            LOGGER.error("Web Service communication error : " + ex.toString());
            if (ex.getMessage().startsWith(ApplicatieException.class.getName())) {
                throw new UnExistingLocation(zipCode, houseNumber);
            }
            throw new FaildCommunicationWithServer();
        }
        LOGGER.info("service result " + result);
            
        return convertToDto(object);
    }

    @Override
    public AddressDTO convertToDto(@NotNull final Verblijfsobject kadasterLocation) {
        final AddressDTO locationDTO = new AddressDTO();

        LOGGER.debug("Entire object for location : " + kadasterLocation.toString());
        final RDCoordinates rdc = new RDCoordinates(kadasterLocation.getVerblijfsobjectGeometrie().getPoint().getPos()
                .getValue().get(0), kadasterLocation.getVerblijfsobjectGeometrie().getPoint().getPos().getValue()
                .get(1));
        final BasselCoordinates bassel = CoordinatesConverterUtil.transformRijksdriehoeksmetingToBassel(rdc);
        
        locationDTO.setCountryCode(LocationService.NL_COUNTRY_CODE);
        locationDTO.setLatitude(bassel.getF().toString());
        locationDTO.setLongitude(bassel.getA().toString());
        locationDTO.setNumber(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getHuisnummer());
        locationDTO.setNumberPostfix(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getHuisnummertoevoeging());
        locationDTO.setPostalCode(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getPostcode());
        locationDTO.setValidFrom(DateTimeUtil.parse(kadasterLocation.getGerelateerdeAdressen().getHoofdadres()
                .getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid()));
        
        final String endDate = kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getTijdvakgeldigheid()
                .getEinddatumTijdvakGeldigheid();
        if (endDate != null) {
        	locationDTO.setValidTo(DateTimeUtil.parse(endDate));
        }
        
        locationDTO.setCity(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getGerelateerdeOpenbareRuimte()
                .getGerelateerdeWoonplaats().getWoonplaatsNaam());
        locationDTO.setStreet(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getGerelateerdeOpenbareRuimte()
                .getOpenbareRuimteNaam());

        return locationDTO;
    }

    /**
     * Function wraps passed parameters into {@link VraagberichtAPDADOAdres} object.
     * 
     * @param zipCode
     * @param houseNumber
     * @return
     */
    private VraagberichtAPDADOAdres wrapZipCodeAndHouseNumberToVraagberichtAPDADOAdres(final String zipCode,
            final Integer houseNumber, final String extension) {
        final APD apd = new APD();
        apd.setGegVarActueel(true);

        final NUMPostcodeAdres numPostcodeAdres = new NUMPostcodeAdres();
        numPostcodeAdres.setHuisnummer(houseNumber);
        numPostcodeAdres.setPostcode(zipCode);
        numPostcodeAdres.setHuisnummertoevoeging(extension);

        final VraagberichtAPDADOAdres.Vraag vraag = new VraagberichtAPDADOAdres.Vraag();
        vraag.setNUMPostcodeAdres(numPostcodeAdres);
        vraag.setAPD(apd);

        final VraagberichtAPDADOAdres result = new VraagberichtAPDADOAdres();
        result.setVraag(vraag);

        return result;
    }

    @Override
    public IBagVsRaadplegenDatumADOV20090901 getWSClient() {
        return clientService;
    }
}
