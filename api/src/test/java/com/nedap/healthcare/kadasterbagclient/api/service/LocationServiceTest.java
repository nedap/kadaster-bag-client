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

import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.AbstractSpringTest;
import com.nedap.healthcare.kadasterbagclient.api.dao.AddressDao;
import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.exception.UnExistingLocation;
import com.nedap.healthcare.kadasterbagclient.api.model.Address;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.api.util.BasselCoordinates;
import com.nedap.healthcare.kadasterbagclient.api.util.CoordinatesConverterUtil;
import com.nedap.healthcare.kadasterbagclient.api.util.DateTimeUtil;
import com.nedap.healthcare.kadasterbagclient.api.util.RDCoordinates;
import com.nedap.healthcare.kadasterbagclient.service.ServiceImpl;

import eu.execom.testutil.property.Property;

/**
 * Test for {@link LocationServiceImpl}.
 * 
 * @author Dusko Vesin
 */
public class LocationServiceTest extends AbstractSpringTest {

    @Autowired
    private LocationServiceHelper locationService;
    @Autowired
    private AddressDao locationDao;

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

        // final Nummeraanduiding numm1 = new Nummeraanduiding();
        // numm1.setTijdvakgeldigheid(tij1);
        // numm1.setHuisnummer(1);
        // numm1.setHuisnummertoevoeging("a1");
        // numm1.setPostcode("postcode1");
        // final Woonplaats woonplaats = new Woonplaats();
        // woonplaats.setWoonplaatsNaam("city");
        // final OpenbareRuimte openabreRuimte = new OpenbareRuimte();
        // openabreRuimte.setOpenbareRuimteNaam("street");

        // =====
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
        // =====

        // numm1.setGerelateerdeWoonplaats(woonplaats);
        // numm1.setGerelateerdeOpenbareRuimte(openabreRuimte);
        // final GerelateerdeAdressen ga1 = new GerelateerdeAdressen();
        // ga1.setHoofdadres(numm1);

        // geo positioning data
        final DirectPositionType dpt = new DirectPositionType();
        dpt.getValue().add(87232.211);// value for x
        dpt.getValue().add(469408.512);// value for y
        final PointType pointType = new PointType();
        pointType.setPos(dpt);
        final PuntOfVlak punt = new PuntOfVlak();
        punt.setPoint(pointType);
        convertObject.setVerblijfsobjectGeometrie(punt);

        // final Verblijfsobject ver1 = new Verblijfsobject();
        // ver1.setGerelateerdeAdressen(ga1);
        // ver1.setVerblijfsobjectGeometrie(punt);
        final ADOProduct adop1 = new ADOProduct();
        // adop1.setVerblijfsobject(ver1);
        adop1.setVerblijfsobject(convertObject);
        final Producten p1 = new Producten();
        p1.getADOProduct().add(adop1);

        final Antwoord an1 = new Antwoord();
        an1.setProducten(p1);

        kadasterLocation.setAntwoord(an1);

        takeSnapshot();

        // call method
        final Address location = locationService.convertAndSave(kadasterLocation);

        final Verblijfsobject object = kadasterLocation.getAntwoord().getProducten().getADOProduct().get(0)
                .getVerblijfsobject();

        // asserting
        final RDCoordinates rdc = new RDCoordinates(object.getVerblijfsobjectGeometrie().getPoint().getPos().getValue()
                .get(0), object.getVerblijfsobjectGeometrie().getPoint().getPos().getValue().get(1));
        final BasselCoordinates bassel = CoordinatesConverterUtil.transformRijksdriehoeksmetingToBassel(rdc);

        assertObject(
                location,
                Property.notNull(Address.ID),
                Property.notNull(Address.CREATION_DATE),
                Property.changed(Address.COUNTRY_CODE, LocationService.NL_COUNTRY_CODE),
                Property.changed(Address.LATITUDE, bassel.getF().toString()),
                Property.changed(Address.LONGITUDE, bassel.getA().toString()),
                Property.changed(Address.NUMBER, object.getGerelateerdeAdressen().getHoofdadres().getHuisnummer()),
                Property.changed(Address.NUMBER_POSTFIX, object.getGerelateerdeAdressen().getHoofdadres()
                        .getHuisnummertoevoeging()),
                Property.changed(Address.POSTAL_CODE, object.getGerelateerdeAdressen().getHoofdadres().getPostcode()),
                Property.changed(
                        Address.VALID_FROM,
                        DateTimeUtil.parse(object.getGerelateerdeAdressen().getHoofdadres().getTijdvakgeldigheid()
                                .getBegindatumTijdvakGeldigheid())),
                Property.changed(
                        Address.VALID_TO,
                        DateTimeUtil.parse(object.getGerelateerdeAdressen().getHoofdadres().getTijdvakgeldigheid()
                                .getEinddatumTijdvakGeldigheid())),
                Property.changed(AddressDTO.CITY, object.getGerelateerdeAdressen().getHoofdadres()
                        .getGerelateerdeOpenbareRuimte().getGerelateerdeWoonplaats().getWoonplaatsNaam()),
                Property.changed(AddressDTO.STREET, object.getGerelateerdeAdressen().getHoofdadres()
                        .getGerelateerdeOpenbareRuimte().getOpenbareRuimteNaam()));

        // assertObject(
        // locationFromDAO,
        // changed(AddressDTO.COUNTRY_CODE, location.getCountryCode()),
        // changed(AddressDTO.NUMBER, location.getNumber()),
        // changed(AddressDTO.NUMBER_POSTFIX, location.getNumberPostfix()),
        // changed(AddressDTO.POSTAL_CODE, location.getPostalCode()),
        // changed(AddressDTO.LATITUDE, location.getLatitude()),
        // changed(AddressDTO.LONGITUDE, location.getLongitude()),
        // changed(AddressDTO.VALID_FROM, location.getValidFrom()),
        // changed(AddressDTO.VALID_TO, location.getValidTo()),
        // changed(AddressDTO.CITY, location.getCity()),
        // changed(AddressDTO.STREET, location.getStreet()));
    }

    /**
     * Testing {@link LocationServiceHelper#convertToDto(Address)} .
     */
    @Test
    public void testConvertToDto() {

        // data preparation
        final Address kadasterLocation = new Address();
        kadasterLocation.setCountryCode("countryCode");
        kadasterLocation.setLatitude("latitude");
        kadasterLocation.setLongitude("longitude");
        kadasterLocation.setNumber(3);
        kadasterLocation.setNumberPostfix("c");
        kadasterLocation.setPostalCode("postalCode");
        kadasterLocation.setValidFrom(DateTimeUtil.parse("20120512120000"));
        kadasterLocation.setValidTo(DateTimeUtil.parse("20121012120000"));
        kadasterLocation.setCity("city");
        kadasterLocation.setStreet("street");

        takeSnapshot();

        // call method
        final AddressDTO locationDto = locationService.convertToDto(kadasterLocation);

        // asserting
        assertObject(locationDto, Property.changed(AddressDTO.COUNTRY_CODE, kadasterLocation.getCountryCode()),
                Property.changed(AddressDTO.LATITUDE, kadasterLocation.getLatitude()),
                Property.changed(AddressDTO.LONGITUDE, kadasterLocation.getLongitude()),
                Property.changed(AddressDTO.NUMBER, kadasterLocation.getNumber()),
                Property.changed(AddressDTO.NUMBER_POSTFIX, kadasterLocation.getNumberPostfix()),
                Property.changed(AddressDTO.POSTAL_CODE, kadasterLocation.getPostalCode()),
                Property.changed(AddressDTO.VALID_FROM, kadasterLocation.getValidFrom()),
                Property.changed(AddressDTO.VALID_TO, kadasterLocation.getValidTo()),
                Property.changed(AddressDTO.CITY, kadasterLocation.getCity()),
                Property.changed(AddressDTO.STREET, kadasterLocation.getStreet()));
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

        // data preparation
        final String postalCode = "7513KC";
        final int number = 4;

        takeSnapshot();

        // call method
        final AddressDTO locationDto = locationService.getAddress(postalCode, number);

        // asserting
        final Address createdEntity = locationDao.findByCountryPostalCodeAndNumber(LocationService.NL_COUNTRY_CODE,
                postalCode, number);

        assertObject(createdEntity, Property.notNull(Address.ID), Property.notNull(Address.CREATION_DATE),
                Property.changed(Address.COUNTRY_CODE, LocationService.NL_COUNTRY_CODE),
                Property.changed(Address.NUMBER, number), Property.nulll(Address.NUMBER_POSTFIX),
                Property.changed(Address.POSTAL_CODE, postalCode), Property.notNull(Address.LATITUDE),
                Property.notNull(Address.LONGITUDE), Property.notNull(Address.VALID_FROM),
                Property.nulll(Address.VALID_TO), Property.notNull(Address.CITY), Property.notNull(Address.STREET));

        assertObject(locationDto, Property.changed(AddressDTO.COUNTRY_CODE, createdEntity.getCountryCode()),
                Property.changed(AddressDTO.NUMBER, createdEntity.getNumber()),
                Property.changed(AddressDTO.NUMBER_POSTFIX, createdEntity.getNumberPostfix()),
                Property.changed(AddressDTO.POSTAL_CODE, createdEntity.getPostalCode()),
                Property.changed(AddressDTO.LATITUDE, createdEntity.getLatitude()),
                Property.changed(AddressDTO.LONGITUDE, createdEntity.getLongitude()),
                Property.changed(AddressDTO.VALID_FROM, createdEntity.getValidFrom()),
                Property.changed(AddressDTO.VALID_TO, createdEntity.getValidTo()),
                Property.changed(AddressDTO.CITY, createdEntity.getCity()),
                Property.changed(AddressDTO.STREET, createdEntity.getStreet()));

        Assert.assertEquals("52.20", createdEntity.getLatitude().substring(0, 5));
        Assert.assertEquals("4.39", createdEntity.getLongitude().substring(0, 4));
    }

    /**
     * Testing {@link LocationServiceHelper#getLocation)} with existing not expired location .
     * 
     * @throws FaildCommunicationWithServer
     * @throws UnExistingLocation
     * @throws ApplicatieException
     */
    @Test
    public void testGetLocationWithPreCashedNotExpiredLocation() throws UnExistingLocation,
            FaildCommunicationWithServer, ApplicatieException {

        // data preparation
        final String postalCode = "7513KC";
        final int number = 4;
        final DateTime creationDate = new DateTime().plusSeconds(-locationService.getMaxValidPeriod() + 5);

        final Address location = new Address();
        location.setCountryCode(LocationServiceHelper.NL_COUNTRY_CODE);
        location.setNumber(number);
        location.setNumberPostfix("c");
        location.setPostalCode(postalCode);
        location.setLatitude("latitude");
        location.setLongitude("longitude");
        location.setValidFrom(DateTimeUtil.parse("20120512120000"));
        location.setValidTo(DateTimeUtil.parse("20120912120000"));
        location.setCreationDate(creationDate);
        location.setCity("city");
        location.setStreet("street");

        locationDao.save(location);

        takeSnapshot();

        // call method
        final AddressDTO locationDto = locationService.getAddress(postalCode, number);

        // asserting
        assertObject(locationDto, Property.changed(AddressDTO.COUNTRY_CODE, location.getCountryCode()),
                Property.changed(AddressDTO.NUMBER, location.getNumber()),
                Property.changed(AddressDTO.NUMBER_POSTFIX, location.getNumberPostfix()),
                Property.changed(AddressDTO.POSTAL_CODE, location.getPostalCode()),
                Property.changed(AddressDTO.LATITUDE, location.getLatitude()),
                Property.changed(AddressDTO.LONGITUDE, location.getLongitude()),
                Property.changed(AddressDTO.VALID_FROM, location.getValidFrom()),
                Property.changed(AddressDTO.VALID_TO, location.getValidTo()),
                Property.changed(AddressDTO.CITY, location.getCity()),
                Property.changed(AddressDTO.STREET, location.getStreet()));
    }

    /**
     * Testing {@link LocationServiceHelper#getLocation)} with existing expired location .
     * 
     * @throws FaildCommunicationWithServer
     * @throws UnExistingLocation
     * @throws ApplicatieException
     */
    @Test
    public void testGetLocationWithPreCashedExpiredLocation() throws UnExistingLocation, FaildCommunicationWithServer,
            ApplicatieException {

        // data preparation
        final String postalCode = "7513KC";
        final int number = 4;
        final DateTime creationDate = new DateTime().plusSeconds(-locationService.getMaxValidPeriod() - 5);

        final Address location = new Address();
        location.setCountryCode(LocationServiceHelper.NL_COUNTRY_CODE);
        location.setNumber(number);
        location.setNumberPostfix("c");
        location.setPostalCode(postalCode);
        location.setLatitude("latitude");
        location.setLongitude("longitude");
        location.setValidFrom(DateTimeUtil.parse("20120512120000"));
        location.setValidTo(DateTimeUtil.parse("20120912120000"));
        location.setCreationDate(creationDate);
        location.setCity("city");
        location.setStreet("street");

        locationDao.save(location);

        takeSnapshot();

        // call method
        final AddressDTO locationDto = locationService.getAddress(postalCode, number);

        // asserting
        final Address createdEntity = locationDao.findByCountryPostalCodeAndNumber(LocationService.NL_COUNTRY_CODE,
                postalCode, number);

        markEntityAsDeleted(location);

        assertObject(createdEntity, Property.notNull(Address.ID), Property.notNull(Address.CREATION_DATE),
                Property.changed(Address.COUNTRY_CODE, LocationService.NL_COUNTRY_CODE),
                Property.changed(Address.NUMBER, number), Property.nulll(Address.NUMBER_POSTFIX),
                Property.changed(Address.POSTAL_CODE, postalCode), Property.notNull(Address.LATITUDE),
                Property.notNull(Address.LONGITUDE), Property.notNull(Address.VALID_FROM),
                Property.nulll(Address.VALID_TO), Property.notNull(Address.CITY), Property.notNull(Address.STREET));

        assertObject(locationDto, Property.changed(AddressDTO.COUNTRY_CODE, createdEntity.getCountryCode()),
                Property.changed(AddressDTO.NUMBER, createdEntity.getNumber()),
                Property.changed(AddressDTO.NUMBER_POSTFIX, createdEntity.getNumberPostfix()),
                Property.changed(AddressDTO.POSTAL_CODE, createdEntity.getPostalCode()),
                Property.changed(AddressDTO.LATITUDE, createdEntity.getLatitude()),
                Property.changed(AddressDTO.LONGITUDE, createdEntity.getLongitude()),
                Property.changed(AddressDTO.VALID_FROM, createdEntity.getValidFrom()),
                Property.changed(AddressDTO.VALID_TO, createdEntity.getValidTo()),
                Property.changed(AddressDTO.CITY, createdEntity.getCity()),
                Property.changed(AddressDTO.STREET, createdEntity.getStreet()));
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

        // data preparation
        final String postalCode = "postcode0";
        final int number = 0;

        takeSnapshot();

        // call method
        try {
            final AddressDTO locationDto = locationService.getAddress(postalCode, number);

            if (locationDto != null) {
                assertTrue(false);
            }
        } catch (final UnExistingLocation e) {
            assertTrue(true);
        }
    }

    /**
     * Test {@link LocationServiceHelper#isExpired(Address)} with location with expired date.
     */
    @Test
    public void testIsExpiredWithExpiredLocation() {

        // data preparation
        final DateTime creationDate = new DateTime().plusSeconds(-locationService.getMaxValidPeriod() - 5);

        final Address location = new Address();
        location.setCountryCode("countryCode");
        location.setLatitude("latitude");
        location.setLongitude("longitude");
        location.setNumber(3);
        location.setNumberPostfix("a");
        location.setPostalCode("postalCode");
        location.setValidFrom(DateTimeUtil.parse("20120512120000"));
        location.setValidTo(DateTimeUtil.parse("20120912120000"));
        location.setCreationDate(creationDate);
        location.setCity("city");
        location.setStreet("street");

        // call method
        final boolean isExpired = locationService.isExpired(location);

        // asserting
        assertTrue("Should be expired", isExpired);

    }

    /**
     * Test {@link LocationServiceHelper#isExpired(Address)} with location with expired date.
     */
    @Test
    public void testIsExpiredWithNonExpiredLocation() {

        // data preparation
        final DateTime creationDate = new DateTime().plusSeconds(-locationService.getMaxValidPeriod() + 5);

        final Address location = new Address();
        location.setCountryCode("countryCode");
        location.setLatitude("latitude");
        location.setLongitude("longitude");
        location.setNumber(3);
        location.setNumberPostfix("c");
        location.setPostalCode("postalCode");
        location.setValidFrom(DateTimeUtil.parse("20120512120000"));
        location.setValidTo(DateTimeUtil.parse("20120912120000"));
        location.setCreationDate(creationDate);
        location.setCity("city");
        location.setStreet("street");

        // call method
        final boolean isExpired = locationService.isExpired(location);

        // asserting
        assertFalse("Should not be expired", isExpired);

    }

    /**
     * Test {@link LocationServiceImpl#getLocatioFromKadaster}
     * 
     * @throws UnExistingLocation
     * @throws ApplicatieException
     */
    @Test
    public void testGetLocatioFromKadaster() throws UnExistingLocation, ApplicatieException {

        // data preparation
        final String zipCode = "7513KC";
        final int houseNumber = 4;

        takeSnapshot();

        // call method
        final AntwoordberichtAPDADO kadasterLocation = locationService.getWSClient()
                .zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(
                        wrapZipCodeAndHouseNumberToVraagberichtAPDADOAdres(zipCode, houseNumber));

        assertNotNull(kadasterLocation);

        // asserting
        // assertObject(kadasterLocation,
        // changed(KadasterLocationDTO.COUNTRY_CODE,
        // LocationService.NL_COUNTRY_CODE),
        // changed(KadasterLocationDTO.NUMBER, houseNumber),
        // changed(KadasterLocationDTO.POSTAL_CODE, zipCode),
        // notNull(KadasterLocationDTO.LATITUDE),
        // notNull(KadasterLocationDTO.LONGITUDE),
        // notNull(KadasterLocationDTO.VALID_FROM),
        // notNull(KadasterLocationDTO.VALID_TO));
    }

    private VraagberichtAPDADOAdres wrapZipCodeAndHouseNumberToVraagberichtAPDADOAdres(final String zipCode,
            final Integer houseNumber) {
        final APD apd = new APD();
        apd.setGegVarActueel(true);

        final NUMPostcodeAdres numPostcodeAdres = new NUMPostcodeAdres();
        numPostcodeAdres.setHuisnummer(houseNumber);
        numPostcodeAdres.setPostcode(zipCode);

        final Vraag vraag = new Vraag();
        vraag.setNUMPostcodeAdres(numPostcodeAdres);
        vraag.setAPD(apd);

        final VraagberichtAPDADOAdres result = new VraagberichtAPDADOAdres();
        result.setVraag(vraag);

        return result;
    }
}
