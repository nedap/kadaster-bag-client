package com.nedap.healthcare.kadasterbagclient.api.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nedap.healthcare.kadasterbagclient.api.AbstractWebTests;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.service.ServiceImpl;

/**
 * Testing api controller functionalities.
 * 
 * @author Dusko Vesin
 */
public class ApiControllerTest extends AbstractWebTests {

    private Unmarshaller um;

    final String zipCode = "7513KC";
    final Integer houseNumber = 4;
    final String extension = "a4";

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

        Assert.assertEquals("52.20", locationDto.getLatitude().substring(0, 5));
        Assert.assertEquals("4.39", locationDto.getLongitude().substring(0, 4));
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
        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.xml");
        mockRequest.setParameter("zipcode", "postalCode3");

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
        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.xml");
        mockRequest.setParameter("number", "3");

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
        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.xml");
        mockRequest.setParameter("zipcode", "postalCode");
        mockRequest.setParameter("number", "44");

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

        Assert.assertEquals("52.20", locationDto.getLatitude().substring(0, 5));
        Assert.assertEquals("4.39", locationDto.getLongitude().substring(0, 4));
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
        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.json");
        mockRequest.setParameter("zipcode", "postalCode3");

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
        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.json");
        mockRequest.setParameter("number", "3");

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
        mockRequest.setMethod(RequestMethod.GET.name());
        mockRequest.setRequestURI("/api/address.json");
        mockRequest.setParameter("zipcode", "postalCode");
        mockRequest.setParameter("number", "44");

        // calling method
        processRequest(null, null, mockRequest, mockResponse);

        // asserting
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), mockResponse.getStatus());
    }

}
