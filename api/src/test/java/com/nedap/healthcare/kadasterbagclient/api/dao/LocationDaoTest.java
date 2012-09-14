package com.nedap.healthcare.kadasterbagclient.api.dao;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.model.Address;

/**
 * Testing {@link LocationHibernateDao}.
 * 
 * @author Dusko Vesin
 */
public class LocationDaoTest extends AbstractDaoTransactionalTest<Address> {

    @Autowired
    private AddressDao locationDao;

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
        // init
        final Address location1 = createUniqueAddress("1");
        locationDao.save(location1);

        final Address location2 = createUniqueAddress("2");
        locationDao.save(location2);

        final Address location3 = createUniqueAddress("3");
        locationDao.save(location3);

        // method
        takeSnapshot();
        final List<Address> findAll = locationDao.findAll();
        assertObjects(findAll, location1, location2, location3);

    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber} .
     */
    @Test
    public void testFindByCountryPostalCodeAndNumber() {

        // data preparation
        final Address location1 = createUniqueAddress("1");
        locationDao.save(location1);
        final Address location2 = createUniqueAddress("2");
        locationDao.save(location2);
        final Address location3 = createUniqueAddress("3");
        locationDao.save(location3);
        final String countryCode = "conutryCode2";
        final String postalCode = "postalCode2";
        final int number = 2;

        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, postalCode, number);

        // asserting
        assertObjects(location, location2);

    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because of wrong
     * value for countryCode parameter.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForCountryCode() {

        // data preparation
        final Address location1 = createUniqueAddress("1");
        locationDao.save(location1);
        final Address location2 = createUniqueAddress("2");
        locationDao.save(location2);
        final Address location3 = createUniqueAddress("3");
        locationDao.save(location3);
        final String countryCode = "conutryCode4";
        final String postalCode = "postalCode2";
        final int number = 2;

        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, postalCode, number);

        // asserting
        assertNull(location);
    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because of wrong
     * value for postalCode parameter.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForPostalCode() {

        // data preparation
        final Address location1 = createUniqueAddress("1");
        locationDao.save(location1);
        final Address location2 = createUniqueAddress("2");
        locationDao.save(location2);
        final Address location3 = createUniqueAddress("3");
        locationDao.save(location3);
        final String countryCode = "conutryCode1";
        final String postalCode = "postalCode2";
        final int number = 1;

        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, postalCode, number);

        // asserting
        assertNull(location);
    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because of wrong
     * value for number parameter.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForNumber() {

        // data preparation
        final Address location1 = createUniqueAddress("1");
        locationDao.save(location1);
        final Address location2 = createUniqueAddress("2");
        locationDao.save(location2);
        final Address location3 = createUniqueAddress("3");
        locationDao.save(location3);
        final String countryCode = "conutryCode1";
        final String postalCode = "postalCode1";
        final int number = 0;

        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(countryCode, postalCode, number);

        // asserting
        assertNull(location);
    }

    /**
     * Testing method {@link AddressDao#findByCountryPostalCodeAndNumber}. Method should return null because null
     * parameters are passed through.
     */
    @Test
    public void testFindByCountryPostalCodeAndNumberFailForNullParameters() {

        // data preparation
        final Address location1 = createUniqueAddress("1");
        locationDao.save(location1);
        final Address location2 = createUniqueAddress("2");
        locationDao.save(location2);
        final Address location3 = createUniqueAddress("3");
        locationDao.save(location3);

        takeSnapshot();

        // call method
        final Address location = locationDao.findByCountryPostalCodeAndNumber(null, null, null);

        // asserting
        assertNull(location);
    }

    @Override
    public Address createValidObject() {
        return createUniqueAddress("");
    }

    private Address createUniqueAddress(final String unique) {
        final Address address = new Address();
        address.setCountryCode("conutryCode" + unique);
        address.setLatitude("latitude" + unique);
        address.setLongitude("longitude" + unique);
        if (unique.length() > 0) {
            address.setNumber(Integer.valueOf(unique));
        } else {
            address.setNumber(0);
        }
        address.setPostalCode("postalCode" + unique);
        address.setStreet("street");
        address.setCity("city");
        address.setValidFrom("20120512000000");
        address.setValidTo("20120912000000");
        address.setCreationDate(new DateTime());
        return address;
    }
}
