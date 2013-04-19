package com.nedap.healthcare.kadasterbagclient.api.service;

import junit.framework.Assert;
import net.opengis.gml.DirectPositionType;
import net.opengis.gml.PointType;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO.Antwoord;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO.Antwoord.Producten;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.ApplicatieException;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOAdres;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOAdres.Vraag;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_producten_apd.v20090901.ADOProduct;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.APD;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.NUMPostcodeAdres;
import nl.kadaster.schemas.imbag.apd.v20090901.GerelateerdeAdressen;
import nl.kadaster.schemas.imbag.apd.v20090901.Nummeraanduiding;
import nl.kadaster.schemas.imbag.apd.v20090901.OpenbareRuimte;
import nl.kadaster.schemas.imbag.apd.v20090901.Verblijfsobject;
import nl.kadaster.schemas.imbag.apd.v20090901.Woonplaats;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.PuntOfVlak;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.Tijdvakgeldigheid;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.AbstractSpringTest;
import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.exception.UnExistingLocation;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.api.util.BasselCoordinates;
import com.nedap.healthcare.kadasterbagclient.api.util.CoordinatesConverterUtil;
import com.nedap.healthcare.kadasterbagclient.api.util.RDCoordinates;
import com.nedap.healthcare.kadasterbagclient.service.ServiceImpl;

/**
 * Test for {@link LocationServiceImpl}.
 * 
 * @author Dusko Vesin
 */
public class LocationServiceTest extends AbstractSpringTest {

    @Autowired
    private LocationServiceHelper locationService;
    
    final String postalCode = "7513KC";
    final int number = 4;
    final String extension = "a4";

    /*
     * This setup is used in case there is no access to actual Kadaster service, so mocked one should be used instead.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        ServiceImpl.main(null);

    }

    @AfterClass
    public static void destroy() {
        ServiceImpl.destroy();
    }

    /**
     * Testing method {@link LocationServiceHelper#convertAndSave(KadasterLocationDTO)} .
     */
    @Test
    public void testConvertAndSave() {

        // data preparation
        final AntwoordberichtAPDADO kadasterLocation = new AntwoordberichtAPDADO();

        final Tijdvakgeldigheid tij1 = new Tijdvakgeldigheid();
        tij1.setBegindatumTijdvakGeldigheid("20070502120000");
        tij1.setEinddatumTijdvakGeldigheid("22991231120000");

        final Verblijfsobject convertObject = new Verblijfsobject();
        final GerelateerdeAdressen adresses = new GerelateerdeAdressen();
        final Nummeraanduiding mainAddress = new Nummeraanduiding();
        final OpenbareRuimte publicSpace = new OpenbareRuimte();
        final Woonplaats city = new Woonplaats();
        city.setWoonplaatsNaam("city");
        publicSpace.setGerelateerdeWoonplaats(city);
        publicSpace.setOpenbareRuimteNaam("street");
        mainAddress.setGerelateerdeOpenbareRuimte(publicSpace);
        mainAddress.setTijdvakgeldigheid(tij1);
        mainAddress.setHuisnummer(1);
        mainAddress.setHuisnummertoevoeging("a1");
        mainAddress.setPostcode("postcode1");
        adresses.setHoofdadres(mainAddress);
        convertObject.setGerelateerdeAdressen(adresses);

        // geo positioning data
        final DirectPositionType dpt = new DirectPositionType();
        dpt.getValue().add(87232.211);// value for x
        dpt.getValue().add(469408.512);// value for y
        final PointType pointType = new PointType();
        pointType.setPos(dpt);
        final PuntOfVlak punt = new PuntOfVlak();
        punt.setPoint(pointType);
        convertObject.setVerblijfsobjectGeometrie(punt);

        final ADOProduct adop1 = new ADOProduct();
        adop1.setVerblijfsobject(convertObject);
        final Producten p1 = new Producten();
        p1.getADOProduct().add(adop1);

        final Antwoord an1 = new Antwoord();
        an1.setProducten(p1);

        kadasterLocation.setAntwoord(an1);

        // call method
        final AddressDTO location = locationService.convertToDto(kadasterLocation.getAntwoord().getProducten()
                .getADOProduct().get(0).getVerblijfsobject());

        final Verblijfsobject object = kadasterLocation.getAntwoord().getProducten().getADOProduct().get(0)
                .getVerblijfsobject();

        // asserting
        final RDCoordinates rdc = new RDCoordinates(object.getVerblijfsobjectGeometrie().getPoint().getPos().getValue()
                .get(0), object.getVerblijfsobjectGeometrie().getPoint().getPos().getValue().get(1));
        final BasselCoordinates bassel = CoordinatesConverterUtil.transformRijksdriehoeksmetingToBassel(rdc);

        Assert.assertEquals(LocationService.NL_COUNTRY_CODE, location.getCountryCode());
        Assert.assertEquals(bassel.getF().toString(), location.getLatitude());
        Assert.assertEquals(bassel.getA().toString(), location.getLongitude());
    }

    /**
     * Testing {@link LocationServiceHelper#getLocation)} without pre cashed location .
     * 
     * @throws FaildCommunicationWithServer
     * @throws UnExistingLocation
     * @throws ApplicatieException
     */
    @Test
    public void testGetLocationWithoutPreCashedLocation() throws UnExistingLocation, FaildCommunicationWithServer,
            ApplicatieException {

        // call method
        final AddressDTO locationDto = locationService.getAddress(postalCode, number, extension);

        Assert.assertEquals("52.20", locationDto.getLatitude().substring(0, 5));
        Assert.assertEquals("4.39", locationDto.getLongitude().substring(0, 4));
    }

    /**
     * Testing {@link LocationServiceHelper#getLocation)} without pre cashed location .
     * 
     * @throws FaildCommunicationWithServer
     * @throws UnExistingLocation
     * @throws ApplicatieException
     */
    @Test
    public void testGetLocationWithoutPreCashedLocationUnExistingParameters() throws FaildCommunicationWithServer,
            ApplicatieException {

        // call method
        try {
            final AddressDTO locationDto = locationService.getAddress("postcode0", 0, extension);

            if (locationDto != null) {
                Assert.assertTrue(false);
            }
        } catch (final UnExistingLocation e) {
        	Assert.assertTrue(true);
        }
    }

    /**
     * Test {@link LocationServiceImpl#getLocatioFromKadaster}
     * 
     * @throws UnExistingLocation
     * @throws ApplicatieException
     */
    @Test
    public void testGetLocatioFromKadaster() throws UnExistingLocation, ApplicatieException {

        // call method
        final AntwoordberichtAPDADO kadasterLocation = locationService.getWSClient()
                .zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(
                        wrapZipCodeAndHouseNumberToVraagberichtAPDADOAdres(postalCode, number, extension));

        Assert.assertNotNull(kadasterLocation);

    }

    private VraagberichtAPDADOAdres wrapZipCodeAndHouseNumberToVraagberichtAPDADOAdres(final String zipCode,
            final Integer houseNumber, final String extension) {
        final APD apd = new APD();
        apd.setGegVarActueel(true);

        final NUMPostcodeAdres numPostcodeAdres = new NUMPostcodeAdres();
        numPostcodeAdres.setHuisnummer(houseNumber);
        numPostcodeAdres.setPostcode(zipCode);
        numPostcodeAdres.setHuisnummertoevoeging(extension);

        final Vraag vraag = new Vraag();
        vraag.setNUMPostcodeAdres(numPostcodeAdres);
        vraag.setAPD(apd);

        final VraagberichtAPDADOAdres result = new VraagberichtAPDADOAdres();
        result.setVraag(vraag);

        return result;
    }
}
