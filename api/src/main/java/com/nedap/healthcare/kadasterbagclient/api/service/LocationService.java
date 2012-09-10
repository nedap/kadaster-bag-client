package com.nedap.healthcare.kadasterbagclient.api.service;

import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.ApplicatieException;

import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.exception.UnExistingLocation;
import com.nedap.healthcare.kadasterbagclient.model.AddressDTO;

/**
 * Service that describes set of methods for accessing to location informations.
 * 
 * @author Dusko Vesin
 */
public interface LocationService {

    /**
     * NL country code.
     */
    // TODO - verify that code is OK.
    String NL_COUNTRY_CODE = "NL";

    /**
     * Get matching location from local cache storage, and if doesnt exist or is expired try to fetch it from kadaster
     * web service.
     * 
     * @param zipCode
     *            zip code
     * @param houseNumber
     *            house number
     * @return matching {@link AddressDTO}
     * @throws UnExistingLocation
     *             is thrown if no location can be found.
     * @throws FaildCommunicationWithServer
     *             is thrown if trouble in communication with Kadaster web service is experienced
     * @throws ApplicatieException 
     */
    AddressDTO getAddress(String zipCode, Integer houseNumber) throws UnExistingLocation, FaildCommunicationWithServer, ApplicatieException;

}
