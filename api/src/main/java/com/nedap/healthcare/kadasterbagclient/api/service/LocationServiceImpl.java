package com.nedap.healthcare.kadasterbagclient.api.service;

import javax.validation.constraints.NotNull;
import javax.xml.ws.WebServiceException;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nedap.healthcare.kadasterbagclient.api.dao.AddressDao;
import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.exception.UnExistingLocation;
import com.nedap.healthcare.kadasterbagclient.api.model.Address;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.api.util.BasselCoordinates;
import com.nedap.healthcare.kadasterbagclient.api.util.CoordinatesConverterUtil;
import com.nedap.healthcare.kadasterbagclient.api.util.DateTimeUtil;
import com.nedap.healthcare.kadasterbagclient.api.util.RDCoordinates;

import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.ApplicatieException;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.IBagVsRaadplegenDatumADOV20090901;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOAdres;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.APD;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.NUMPostcodeAdres;
import nl.kadaster.schemas.imbag.apd.v20090901.Verblijfsobject;

/**
 * {@link LocationServiceHelper} implementation.
 * 
 * @author Dusko Vesin
 */
@Service
@Transactional
class LocationServiceImpl implements LocationServiceHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);

    /**
     * Max valid time for location beans fetched from configuration file.
     */
    private @Value("${max.validation.period.sec}")
    Integer maxValidPeriod;

    @Autowired
    private AddressDao locationDao;

    @Autowired
    private IBagVsRaadplegenDatumADOV20090901 clientService;

    @Override
    public AddressDTO getAddress(@NotBlank final String zipCode, @NotBlank final Integer houseNumber,
            final String extension) throws UnExistingLocation, FaildCommunicationWithServer, ApplicatieException {

        LOGGER.debug(String.format("Find location in Netherlands for zipCode: %s, houseNumber: %s and extension: %s ",
                zipCode, houseNumber, extension));
        Address location = locationDao.findByCountryPostalCodeAndNumber(NL_COUNTRY_CODE, zipCode, houseNumber,
                extension);

        if (location != null && isExpired(location)) {
            locationDao.delete(location);
            location = null;
        }

        if (location == null) {
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
            location = convertAndSave(object);
        }

        return convertToDto(location);
    }

    @Override
    public boolean isExpired(@NotNull final Address location) {
        final Long creationTime = location.getCreationDate().getMillis();
        final Long currentTime = new DateTime().getMillis();

        final Long difference = currentTime - creationTime;
        return difference > maxValidPeriod.longValue() * 1000L;
    }

    @Override
    public AddressDTO convertToDto(@NotNull final Address location) {
        final AddressDTO locationDTO = new AddressDTO();

        locationDTO.setCountryCode(location.getCountryCode());
        locationDTO.setLatitude(location.getLatitude());
        locationDTO.setLongitude(location.getLongitude());
        locationDTO.setNumber(location.getNumber());
        locationDTO.setNumberPostfix(location.getNumberPostfix());
        locationDTO.setPostalCode(location.getPostalCode());
        locationDTO.setValidFrom(location.getValidFrom());
        locationDTO.setValidTo(location.getValidTo());
        locationDTO.setCity(location.getCity());
        locationDTO.setStreet(location.getStreet());

        return locationDTO;
    }

    @Override
    public Address convertAndSave(@NotNull final Verblijfsobject kadasterLocation) {

        final Address location = new Address();

        LOGGER.debug("Entire object for location : " + kadasterLocation.toString());
        final RDCoordinates rdc = new RDCoordinates(kadasterLocation.getVerblijfsobjectGeometrie().getPoint().getPos()
                .getValue().get(0), kadasterLocation.getVerblijfsobjectGeometrie().getPoint().getPos().getValue()
                .get(1));
        final BasselCoordinates bassel = CoordinatesConverterUtil.transformRijksdriehoeksmetingToBassel(rdc);

        location.setCountryCode(LocationService.NL_COUNTRY_CODE);
        location.setCreationDate(new DateTime());

        location.setLatitude(bassel.getF().toString());
        location.setLongitude(bassel.getA().toString());

        location.setValidFrom(DateTimeUtil.parse(kadasterLocation.getGerelateerdeAdressen().getHoofdadres()
                .getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid()));
        final String endDate = kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getTijdvakgeldigheid()
                .getEinddatumTijdvakGeldigheid();
        if (endDate != null) {
            location.setValidTo(DateTimeUtil.parse(endDate));
        }
        location.setNumber(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getHuisnummer());
        location.setNumberPostfix(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getHuisnummertoevoeging());
        location.setPostalCode(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getPostcode());
        location.setCity(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getGerelateerdeOpenbareRuimte()
                .getGerelateerdeWoonplaats().getWoonplaatsNaam());
        location.setStreet(kadasterLocation.getGerelateerdeAdressen().getHoofdadres().getGerelateerdeOpenbareRuimte()
                .getOpenbareRuimteNaam());

        locationDao.save(location);

        return location;
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
    public Integer getMaxValidPeriod() {
        return maxValidPeriod;
    }

    @Override
    public IBagVsRaadplegenDatumADOV20090901 getWSClient() {
        return clientService;
    }
}
