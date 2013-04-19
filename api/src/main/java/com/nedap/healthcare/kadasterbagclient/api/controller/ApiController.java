package com.nedap.healthcare.kadasterbagclient.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.exception.UnExistingLocation;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;
import com.nedap.healthcare.kadasterbagclient.api.service.LocationService;

import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.ApplicatieException;

/**
 * API controller, publish REST services for accessing geo coding data.
 * 
 * @author Dusko Vesin
 */
@Controller
@RequestMapping("/api")
public class ApiController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private LocationService locationService;

    /**
     * REST service for requesting geo coding information in XML format according to requested parameters.
     * 
     * @param zipCode
     *            zip code
     * @param number
     *            house number
     * @param extension
     *            house number extension
     * @return matching {@link AddressDTO} object
     * @throws UnExistingLocation
     *             is thrown when no match is found
     * @throws FaildCommunicationWithServer
     *             when server experience issue with kadaster web service communication
     * @throws ApplicatieException
     */
    @RequestMapping(method = RequestMethod.GET, value = "address.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    AddressDTO addressXml(@RequestParam(value = "zipcode", required = true) final String zipCode,
            @RequestParam(value = "number", required = true) final Integer number,
            @RequestParam(value = "homeNumberExtension", required = false) final String extension)
            throws UnExistingLocation, FaildCommunicationWithServer, ApplicatieException {

        LOGGER.debug(String.format("Received xml request to location with %s, %s and %s", zipCode, number, extension));

        return locationService.getAddress(zipCode, number, extension);

    }

    /**
     * REST service for requesting address information in JSON format according to requested parameters.
     * 
     * @param zipCode
     *            zip code
     * @param number
     *            house number
     * @param extension
     *            house number extension
     * @return matching {@link AddressDTO} object
     * @throws UnExistingLocation
     *             is thrown when no match is found
     * @throws FaildCommunicationWithServer
     *             when server experience issue with kadaster web service communication
     * @throws ApplicatieException
     */
    @RequestMapping(method = RequestMethod.GET, value = "address.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    AddressDTO addressJson(@RequestParam(value = "zipcode", required = true) final String zipCode,
            @RequestParam(value = "number", required = true) final Integer number,
            @RequestParam(value = "homeNumberExtension", required = false) final String extension)
            throws UnExistingLocation, FaildCommunicationWithServer, ApplicatieException {

        LOGGER.debug(String.format("Received xml request to location with %s, %s and %s", zipCode, number, extension));

        return locationService.getAddress(zipCode, number, extension);
    }

}
