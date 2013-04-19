package com.nedap.healthcare.kadasterbagclient.api.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.AbstractSpringNoExternalServiceTest;
import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.service.ServiceImpl;

public class LocationServiceMissingWebServiceTest extends AbstractSpringNoExternalServiceTest {

    @Autowired
    private LocationServiceHelper locationService;
    
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
        final String extension = "a3";

        ServiceImpl.destroy();

        // call method
        AddressDTO locationDto = null;
        try {
            locationDto = locationService.getAddress(postalCode, number, extension);
            Assert.assertTrue(false);
        } catch (final FaildCommunicationWithServer ex) {
        	Assert.assertTrue(true);
        }

        ServiceImpl.main(null);

        locationDto = locationService.getAddress(postalCode, number, extension);

        Assert.assertEquals(LocationService.NL_COUNTRY_CODE, locationDto.getCountryCode());
    }
}
