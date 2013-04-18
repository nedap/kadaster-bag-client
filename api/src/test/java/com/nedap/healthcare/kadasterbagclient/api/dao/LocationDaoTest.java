package com.nedap.healthcare.kadasterbagclient.api.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.model.Address;
import com.nedap.healthcare.kadasterbagclient.api.util.DateTimeUtil;

/**
 * Testing {@link LocationHibernateDao}.
 * 
 * @author Dusko Vesin
 */
public class LocationDaoTest extends AbstractDaoTransactionalTest<Address> {

    @Autowired
    private AddressDao locationDao;

    Address location1;
    Address location2;
    Address location3;
    final String countryCode = "conutryCode1";
    final String postalCode = "postalCode1";
    final String extension = "a1";
    final int number = 1;

    private static final Comparator<Address> ADDRESS_COMPARATOR = new Comparator<Address>() {
        // This is where the sorting happens.
        @Override
        public int compare(final Address o1, final Address o2) {
            return o1.getNumber() - o2.getNumber();
        }
    };

    @Override
    protected GenericDao<Address> getDao() {
        return locationDao;
    }

    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }

    @Override
    public void testFindAll() {

        // prepare data
        setup();
        takeSnapshot();

        // method
        final List<Address> findAll = locationDao.findAll();

        Collections.sort(findAll, ADDRESS_COMPARATOR);

        assertObjects(findAll, location1, location2, location3);

    }

    public void setup() {
        location1 = createUniqueAddress("1");
        locationDao.save(location1);
        location2 = createUniqueAddress("2");
        locationDao.save(location2);
        location3 = createUniqueAddress("3");
        locationDao.save(location3);
    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber} .
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberSuccess() {

        setup();
        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, postalCode, number,
                extension);

        // asserting
        assertObjects(location, location1);

    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber} .
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberSuccessWithoutExtension() {

        setup();
        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, postalCode, number, null);

        // asserting
        assertObjects(location, location1);

    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because of wrong
     * value for countryCode parameter.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForCountryCode() {

        setup();
        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber("conutryCode2", postalCode, number,
                extension);

        // asserting
        assertNull(location);
    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because of wrong
     * value for postalCode parameter.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForPostalCode() {

        setup();
        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, "postalCode2", number,
                extension);

        // asserting
        assertNull(location);
    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because of wrong
     * value for number parameter.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForNumber() {

        setup();
        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, postalCode, 0, extension);

        // asserting
        assertNull(location);
    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because of wrong
     * value for number parameter.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForExtension() {

        setup();
        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, postalCode, number, "b");

        // asserting
        assertNull(location);
    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because null
     * parameters are passed through.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForNullParameters() {

        setup();
        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(null, null, null, null);

        // asserting
        assertNull(location);
    }

    @Override
    public Address createValidObject() {
        return createUniqueAddress("");
    }

    /**
     * Testing methods used for access to database outside the regular functions (with custom queries)
     */
    @Test
    public void testHibernateCriteriaMethods() {

        // data preparation
        setup();
        final String[] params = {Address.POSTAL_CODE, Address.NUMBER};
        final Map<String, String> criterias = new HashMap<String, String>();
        criterias.put(Address.CITY, "city");

        takeSnapshot();

        // call method
        final List<Address> list1 = locationDao.findByExample(location1, params);
        final List<Address> list2 = locationDao.findByCriteria(criterias);
        final List<Address> list3 = locationDao.findAll();

        // asserting
        assertNotNull(list1);
        assertNotNull(list2);
        assertNotNull(list3);
    }

    private Address createUniqueAddress(final String unique) {
        final Address address = new Address();
        address.setCountryCode("conutryCode" + unique);
        address.setLatitude("latitude" + unique);
        address.setLongitude("longitude" + unique);
        if (unique.length() > 0) {
            address.setNumber(Integer.valueOf(unique));
            address.setNumberPostfix("a" + unique);
        } else {
            address.setNumber(0);
            address.setNumberPostfix("a");
        }
        address.setPostalCode("postalCode" + unique);
        address.setStreet("street");
        address.setCity("city");
        final DateTime validFrom = DateTimeUtil.parse("20120512120000");
        final DateTime validTo = DateTimeUtil.parse("20120912120000");
        address.setValidFrom(validFrom);
        address.setValidTo(validTo);
        address.setCreationDate(DateTimeUtil.parse("20121102092100"));
        return address;
    }

    // @After
    // public void after() {
    // locationDao.delete(location1);
    // locationDao.delete(location2);
    // locationDao.delete(location3);
    // markEntityAsDeleted(location1);
    // markEntityAsDeleted(location2);
    // markEntityAsDeleted(location3);
    // }
}
