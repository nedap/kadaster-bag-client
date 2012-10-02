package com.nedap.healthcare.kadasterbagclient.api.service;

import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.IBagVsRaadplegenDatumADOV20090901;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.AbstractSpringTest;
import com.nedap.healthcare.kadasterbagclient.api.dao.AddressDao;
import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.model.Address;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.service.ServiceImpl;

import eu.execom.testutil.property.Property;

public class LocationServiceMissingWebServiceTest extends AbstractSpringTest {

    @Autowired
    private LocationServiceHelper locationService;
    @Autowired
    private AddressDao locationDao;

    /**
     * Testing {@link LocationServiceHelper#getLocation)} without pre cashed location when web service is missing .
     * 
     * @throws Exception
     */
    @Test
    public void testGetLocationWithoutPreCashedLocationMissingWebServiceEndpoint() throws Exception {

        // data preparation
        final String postalCode = "postcode3";
        final int number = 3;

        System.out.println("treba da unistim");
        ServiceImpl.destroy();
        
        takeSnapshot();

        // call method
        System.out.println("treba da udjem");
        AddressDTO locationDto = null;
        try {
        	System.out.println("usao");
            locationDto = locationService.getAddress(postalCode, number);
            assertTrue(false);
        } catch (FaildCommunicationWithServer ex) {
            assertTrue(true);
            System.out.println("puko");
        }
        System.out.println("izasao");

        ServiceImpl.main(null);

        locationDto = locationService.getAddress(postalCode, number);

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
}
