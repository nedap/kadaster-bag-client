package com.nedap.healthcare.kadasterbagclient.api.service;

import java.util.Calendar;

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

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.AbstractSpringTest;
import com.nedap.healthcare.kadasterbagclient.api.dao.AddressDao;
import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.exception.UnExistingLocation;
import com.nedap.healthcare.kadasterbagclient.api.model.Address;
import com.nedap.healthcare.kadasterbagclient.api.util.BasselCoordinates;
import com.nedap.healthcare.kadasterbagclient.api.util.CoordinatesConverterUtil;
import com.nedap.healthcare.kadasterbagclient.api.util.RDCoordinates;
import com.nedap.healthcare.kadasterbagclient.model.AddressDTO;
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

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        ServiceImpl.main(null);

    }

    /**
     * Testing method {@link LocationServiceHelper#convertAndSave(KadasterLocationDTO)} .
     */
    @Test
    public void testConvertAndSave() {

        // data preparation
        final AntwoordberichtAPDADO kadasterLocation = new AntwoordberichtAPDADO();

        Tijdvakgeldigheid tij1 = new Tijdvakgeldigheid();
        tij1.setBegindatumTijdvakGeldigheid("20070502000000");
        tij1.setEinddatumTijdvakGeldigheid("22991231000000");

        Nummeraanduiding numm1 = new Nummeraanduiding();
        numm1.setTijdvakgeldigheid(tij1);
        numm1.setHuisnummer(1);
        numm1.setHuisnummertoevoeging("a1");
        numm1.setPostcode("postcode1");
        Woonplaats woonplaats = new Woonplaats();
        woonplaats.setWoonplaatsNaam("city");
        OpenbareRuimte openabreRuimte = new OpenbareRuimte();
        openabreRuimte.setOpenbareRuimteNaam("street");
        numm1.setGerelateerdeWoonplaats(woonplaats);
        numm1.setGerelateerdeOpenbareRuimte(openabreRuimte);
        GerelateerdeAdressen ga1 = new GerelateerdeAdressen();
        ga1.setHoofdadres(numm1);

        // geo positioning data
        DirectPositionType dpt = new DirectPositionType();
        dpt.getValue().add(87232.211);// value for x
        dpt.getValue().add(469408.512);// value for y
        PointType pointType = new PointType();
        pointType.setPos(dpt);
        PuntOfVlak punt = new PuntOfVlak();
        punt.setPoint(pointType);

        Verblijfsobject ver1 = new Verblijfsobject();
        ver1.setGerelateerdeAdressen(ga1);
        ver1.setVerblijfsobjectGeometrie(punt);
        ADOProduct adop1 = new ADOProduct();
        adop1.setVerblijfsobject(ver1);
        Producten p1 = new Producten();
        p1.getADOProduct().add(adop1);

        Antwoord an1 = new Antwoord();
        an1.setProducten(p1);

        kadasterLocation.setAntwoord(an1);

        takeSnapshot();

        // call method
        final Address location = locationService.convertAndSave(kadasterLocation);

        Verblijfsobject object = kadasterLocation.getAntwoord().getProducten().getADOProduct().get(0)
                .getVerblijfsobject();

        // asserting
        RDCoordinates rdc = new RDCoordinates(object.getVerblijfsobjectGeometrie().getPoint().getPos().getValue()
                .get(0), object.getVerblijfsobjectGeometrie().getPoint().getPos().getValue().get(1));
        BasselCoordinates bassel = CoordinatesConverterUtil.transformRijksdriehoeksmetingToBassel(rdc);

        assertObject(
                location,
                Property.notNull(Address.ID),
                Property.notNull(Address.CREATION_DATE),
                Property.changed(Address.COUNTRY_CODE, LocationService.NL_COUNTRY_CODE),
                Property.changed(Address.LATITUDE, bassel.getA().toString()),
                Property.changed(Address.LONGITUDE, bassel.getF().toString()),
                Property.changed(Address.NUMBER, object.getGerelateerdeAdressen().getHoofdadres().getHuisnummer()),
                Property.changed(Address.NUMBER_POSTFIX, object.getGerelateerdeAdressen().getHoofdadres()
                        .getHuisnummertoevoeging()),
                Property.changed(Address.POSTAL_CODE, object.getGerelateerdeAdressen().getHoofdadres().getPostcode()),
                Property.changed(Address.VALID_FROM, object.getGerelateerdeAdressen().getHoofdadres()
                        .getTijdvakgeldigheid().getBegindatumTijdvakGeldigheid()),
                Property.changed(Address.VALID_TO, object.getGerelateerdeAdressen().getHoofdadres()
                        .getTijdvakgeldigheid().getEinddatumTijdvakGeldigheid()),
                Property.changed(AddressDTO.CITY, object.getGerelateerdeAdressen().getHoofdadres()
                        .getGerelateerdeWoonplaats().getWoonplaatsNaam()),
                Property.changed(AddressDTO.STREET, object.getGerelateerdeAdressen().getHoofdadres()
                        .getGerelateerdeOpenbareRuimte().getOpenbareRuimteNaam()));
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
        kadasterLocation.setValidFrom("20120512000000");
        kadasterLocation.setValidTo("20121012000000");
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
        final String postalCode = "postcode3";
        final int number = 3;

        takeSnapshot();

        // call method
        final AddressDTO locationDto = locationService.getAddress(postalCode, number);

        // asserting
        final Address createdEntity = locationDao.findByCountryPostalCodeAndNumber(LocationService.NL_COUNTRY_CODE,
                postalCode, number);

        assertObject(createdEntity, Property.notNull(Address.ID), Property.notNull(Address.CREATION_DATE),
                Property.changed(Address.COUNTRY_CODE, LocationService.NL_COUNTRY_CODE),
                Property.changed(Address.NUMBER, number), Property.notNull(Address.NUMBER_POSTFIX),
                Property.changed(Address.POSTAL_CODE, postalCode), Property.notNull(Address.LATITUDE),
                Property.notNull(Address.LONGITUDE), Property.notNull(Address.VALID_FROM),
                Property.notNull(Address.VALID_TO), Property.notNull(Address.CITY), Property.notNull(Address.STREET));

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
        final String postalCode = "postcode3";
        final int number = 3;
        final Calendar creationDate = Calendar.getInstance();
        creationDate.add(Calendar.SECOND, -locationService.getMaxValidPeriod() + 5);

        final Address location = new Address();
        location.setCountryCode(LocationServiceHelper.NL_COUNTRY_CODE);
        location.setNumber(number);
        location.setNumberPostfix("c");
        location.setPostalCode(postalCode);
        location.setLatitude("latitude");
        location.setLongitude("longitude");
        location.setValidFrom("20120512000000");
        location.setValidTo("20120912000000");
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
        final String postalCode = "postcode3";
        final int number = 3;
        final Calendar creationDate = Calendar.getInstance();
        creationDate.add(Calendar.SECOND, -locationService.getMaxValidPeriod() - 5);

        final Address location = new Address();
        location.setCountryCode(LocationServiceHelper.NL_COUNTRY_CODE);
        location.setNumber(number);
        location.setNumberPostfix("c");
        location.setPostalCode(postalCode);
        location.setLatitude("latitude");
        location.setLongitude("longitude");
        location.setValidFrom("20120512000000");
        location.setValidTo("20120912000000");
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
                Property.changed(Address.NUMBER, number), Property.notNull(Address.NUMBER_POSTFIX),
                Property.changed(Address.POSTAL_CODE, postalCode), Property.notNull(Address.LATITUDE),
                Property.notNull(Address.LONGITUDE), Property.notNull(Address.VALID_FROM),
                Property.notNull(Address.VALID_TO), Property.notNull(Address.CITY), Property.notNull(Address.STREET));

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
        } catch (UnExistingLocation e) {
            assertTrue(true);
        }
    }

    /**
     * Test {@link LocationServiceHelper#isExpired(Address)} with location with expired date.
     */
    @Test
    public void testIsExpiredWithExpiredLocation() {

        // data preparation
        final Calendar creationDate = Calendar.getInstance();
        creationDate.add(Calendar.SECOND, -locationService.getMaxValidPeriod() - 5);

        final Address location = new Address();
        location.setCountryCode("countryCode");
        location.setLatitude("latitude");
        location.setLongitude("longitude");
        location.setNumber(3);
        location.setNumberPostfix("a");
        location.setPostalCode("postalCode");
        location.setValidFrom("20120512000000");
        location.setValidTo("20120912000000");
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
        final Calendar creationDate = Calendar.getInstance();
        creationDate.add(Calendar.SECOND, -locationService.getMaxValidPeriod() + 5);

        final Address location = new Address();
        location.setCountryCode("countryCode");
        location.setLatitude("latitude");
        location.setLongitude("longitude");
        location.setNumber(3);
        location.setNumberPostfix("c");
        location.setPostalCode("postalCode");
        location.setValidFrom("20120512000000");
        location.setValidTo("20120912000000");
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
        // ServiceImpl endpoint = new ServiceImpl ();
        // JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        // svrFactory.setServiceClass(IBagVsRaadplegenDatumADOV20090901.class);
        // svrFactory.setAddress("http://localhost:8080/clientService");
        // svrFactory.setServiceBean(endpoint);
        // svrFactory.create();

        // data preparation
        final String zipCode = "postcode3";
        final int houseNumber = 3;

        takeSnapshot();

        // call method
        final AntwoordberichtAPDADO kadasterLocation = locationService.getWSClient()
                .zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(
                        wrapZipCodeAndHouseNumberToVraagberichtAPDADOAdres(zipCode, houseNumber));

        assertNotNull(kadasterLocation);

        // asserting
        // assertObject(kadasterLocation, changed(KadasterLocationDTO.COUNTRY_CODE, LocationService.NL_COUNTRY_CODE),
        // changed(KadasterLocationDTO.NUMBER, houseNumber), changed(KadasterLocationDTO.POSTAL_CODE, zipCode),
        // notNull(KadasterLocationDTO.LATITUDE), notNull(KadasterLocationDTO.LONGITUDE),
        // notNull(KadasterLocationDTO.VALID_FROM), notNull(KadasterLocationDTO.VALID_TO));
    }

    private VraagberichtAPDADOAdres wrapZipCodeAndHouseNumberToVraagberichtAPDADOAdres(String zipCode,
            Integer houseNumber) {
        APD apd = new APD();
        apd.setGegVarActueel(true);

        NUMPostcodeAdres numPostcodeAdres = new NUMPostcodeAdres();
        numPostcodeAdres.setHuisnummer(houseNumber);
        numPostcodeAdres.setPostcode(zipCode);

        Vraag vraag = new Vraag();
        vraag.setNUMPostcodeAdres(numPostcodeAdres);
        vraag.setAPD(apd);

        VraagberichtAPDADOAdres result = new VraagberichtAPDADOAdres();
        result.setVraag(vraag);

        return result;
    }
}
