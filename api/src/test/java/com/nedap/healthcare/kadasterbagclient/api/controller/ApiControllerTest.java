package com.nedap.healthcare.kadasterbagclient.api.controller;

import static eu.execom.testutil.property.Property.changed;
import static eu.execom.testutil.property.Property.notNull;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nedap.healthcare.kadasterbagclient.api.AbstractWebTests;
import com.nedap.healthcare.kadasterbagclient.api.dao.AddressDao;
import com.nedap.healthcare.kadasterbagclient.api.model.Address;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.api.service.LocationService;
import com.nedap.healthcare.kadasterbagclient.service.ServiceImpl;

import eu.execom.testutil.property.Property;

/**
 * Testing api controller functionalities.
 * 
 * @author Dusko Vesin
 */
public class ApiControllerTest extends AbstractWebTests {

    private Unmarshaller um;

    @Autowired
    private AddressDao locationDao;

    @Before
    public void setJaxb() throws Exception {
        final JAXBContext jc = JAXBContext.newInstance(AddressDTO.class);
        // Create unmarshaller
        um = jc.createUnmarshaller();

        ServiceImpl.main(null);
    }

    @AfterClass
    public static void destroy() {
        ServiceImpl.destroy();
    }

    /**
     * Testing controller geocode.xml with valid data in case when requested object is not already precashed in local
     * DB.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws JAXBException
     * @throws Exception
     */
    @Test
    public void testGeoCodeXmlWithValidDataNotCatchedCode() throws JsonGenerationException, JsonMappingException,
            UnsupportedEncodingException, IOException, ServletException, JAXBException {

        // data preparing
        final String zipCode = "7513KC";
        final Integer houseNumber = 4;

        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.xml");
        mockRequest.setParameter("zipcode", zipCode);
        mockRequest.setParameter("number", houseNumber.toString());

        // calling method
        final String responseContent = processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());

        final AddressDTO locationDto = (AddressDTO) um.unmarshal(new StringReader(responseContent));
        Assert.assertNotNull(locationDto);

        final Address createdEntity = locationDao.findByCountryPostalCodeAndNumber(LocationService.NL_COUNTRY_CODE,
                zipCode, houseNumber);

        assertObject(createdEntity, notNull(Address.ID), notNull(Address.CREATION_DATE),
                changed(Address.COUNTRY_CODE, LocationService.NL_COUNTRY_CODE), changed(Address.NUMBER, houseNumber),
                Property.nulll(Address.NUMBER_POSTFIX), changed(Address.POSTAL_CODE, zipCode),
                notNull(Address.LATITUDE), notNull(Address.LONGITUDE), notNull(Address.VALID_FROM),
                Property.nulll(Address.VALID_TO), notNull(Address.CITY), notNull(Address.STREET));

        assertObject(locationDto, changed(AddressDTO.COUNTRY_CODE, createdEntity.getCountryCode()),
                changed(AddressDTO.NUMBER, createdEntity.getNumber()),
                changed(AddressDTO.NUMBER_POSTFIX, createdEntity.getNumberPostfix()),
                changed(AddressDTO.POSTAL_CODE, createdEntity.getPostalCode()),
                changed(AddressDTO.LATITUDE, createdEntity.getLatitude()),
                changed(AddressDTO.LONGITUDE, createdEntity.getLongitude()),
                changed(AddressDTO.VALID_FROM, createdEntity.getValidFrom()),
                changed(AddressDTO.VALID_TO, createdEntity.getValidTo()),
                changed(AddressDTO.CITY, createdEntity.getCity()),
                changed(AddressDTO.STREET, createdEntity.getStreet()));
    }

    /**
     * Testing controller geocode.xml with valid data in case when requested object is not already precashed in local
     * DB.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws JAXBException
     * @throws Exception
     */
    @Test
    public void testGeoCodeXmlWithValidDataNotCatchedCodeMissingHouseNumber() throws JsonGenerationException,
            JsonMappingException, UnsupportedEncodingException, IOException, ServletException, JAXBException {

        // data preparing
        final String zipCode = "postalCode3";

        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.xml");
        mockRequest.setParameter("zipcode", zipCode);

        // calling method
        processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
    }

    /**
     * Testing controller geocode.xml with valid data in case when requested object is not already precashed in local
     * DB.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws JAXBException
     * @throws Exception
     */
    @Test
    public void testGeoCodeXmlWithValidDataNotCatchedCodeMissingZipCode() throws JsonGenerationException,
            JsonMappingException, UnsupportedEncodingException, IOException, ServletException, JAXBException {

        // data preparing
        final String houseNumber = "3";

        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.xml");
        mockRequest.setParameter("number", houseNumber);

        // calling method
        processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
    }

    /**
     * Testing controller geocode.xml with valid data in case when requested object is not already precashed in local
     * DB.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws JAXBException
     * @throws Exception
     */
    @Test
    public void testGeoCodeXmlWithValidDataNotCatchedCodeUnExistingLocation() throws JsonGenerationException,
            JsonMappingException, UnsupportedEncodingException, IOException, ServletException, JAXBException {

        // data preparing
        final String zipCode = "postalCode";
        final String houseNumber = "44";

        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.xml");
        mockRequest.setParameter("zipcode", zipCode);
        mockRequest.setParameter("number", houseNumber);

        // calling method
        processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
    }

    /**
     * Testing controller geocode.json with valid data in case when requested object is not already precashed in local
     * DB.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws Exception
     */
    @Test
    public void testGeoCodeXmlJsonValidDataNotCatchedCode() throws JsonGenerationException, JsonMappingException,
            UnsupportedEncodingException, IOException, ServletException {

        // data preparing
        final String zipCode = "7513KC";
        final Integer houseNumber = 4;

        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.json");
        mockRequest.setParameter("zipcode", zipCode);
        mockRequest.setParameter("number", houseNumber.toString());

        // calling method
        final String responseContent = processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());

        final AddressDTO locationDto = mapper.readValue(responseContent, AddressDTO.class);
        Assert.assertNotNull(locationDto);

        final Address createdEntity = locationDao.findByCountryPostalCodeAndNumber(LocationService.NL_COUNTRY_CODE,
                zipCode, houseNumber);

        assertObject(createdEntity, notNull(Address.ID),
                changed(Address.COUNTRY_CODE, LocationService.NL_COUNTRY_CODE), changed(Address.NUMBER, houseNumber),
                Property.nulll(Address.NUMBER_POSTFIX), changed(Address.POSTAL_CODE, zipCode),
                notNull(Address.CREATION_DATE), notNull(Address.LATITUDE), notNull(Address.LONGITUDE),
                notNull(Address.VALID_FROM), Property.nulll(Address.VALID_TO), notNull(Address.CITY),
                notNull(Address.STREET));

        assertObject(locationDto, changed(AddressDTO.COUNTRY_CODE, createdEntity.getCountryCode()),
                changed(AddressDTO.NUMBER, createdEntity.getNumber()),
                changed(AddressDTO.NUMBER_POSTFIX, createdEntity.getNumberPostfix()),
                changed(AddressDTO.POSTAL_CODE, createdEntity.getPostalCode()),
                changed(AddressDTO.LATITUDE, createdEntity.getLatitude()),
                changed(AddressDTO.LONGITUDE, createdEntity.getLongitude()),
                changed(AddressDTO.VALID_FROM, createdEntity.getValidFrom()),
                changed(AddressDTO.VALID_TO, createdEntity.getValidTo()),
                changed(AddressDTO.CITY, createdEntity.getCity()),
                changed(AddressDTO.STREET, createdEntity.getStreet()));
    }

    /**
     * Testing controller geocode.json with valid data in case when requested object is not already precashed in local
     * DB.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws Exception
     */
    @Test
    public void testGeoCodeXmlJsonValidDataNotCatchedCodeMissingHouseNumber() throws JsonGenerationException,
            JsonMappingException, UnsupportedEncodingException, IOException, ServletException {

        // data preparing
        final String zipCode = "postalCode3";

        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.json");
        mockRequest.setParameter("zipcode", zipCode);

        // calling method
        processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
    }

    /**
     * Testing controller geocode.json with valid data in case when requested object is not already precashed in local
     * DB.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws Exception
     */
    @Test
    public void testGeoCodeXmlJsonValidDataNotCatchedCodeMissingZipCode() throws JsonGenerationException,
            JsonMappingException, UnsupportedEncodingException, IOException, ServletException {

        // data preparing
        final String houseNumber = "3";

        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.json");
        mockRequest.setParameter("number", houseNumber);

        // calling method
        processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());
    }

    /**
     * Testing controller geocode.json with valid data in case when requested object is not already precashed in local
     * DB.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws Exception
     */
    @Test
    public void testGeoCodeXmlJsonValidDataNotCatchedCodeUnExistingLocation() throws JsonGenerationException,
            JsonMappingException, UnsupportedEncodingException, IOException, ServletException {

        // data preparing
        final String zipCode = "postalCode";
        final String houseNumber = "44";

        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.json");
        mockRequest.setParameter("zipcode", zipCode);
        mockRequest.setParameter("number", houseNumber);

        // calling method
        processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
    }

}
