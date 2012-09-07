package com.nedap.healthcare.kadasterbagclient.api.service;

import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.IBagVsRaadplegenDatumADOV20090901;

import com.nedap.healthcare.kadasterbagclient.api.model.Address;
import com.nedap.healthcare.kadasterbagclient.model.AddressDTO;

/**
 * {@link LocationService} extension with additional methods that are used inside inside implementation. This interface
 * is protected and its only purpose is to allow better testing of its methods.
 * 
 * @author Dusko Vesin
 */
interface LocationServiceHelper extends LocationService {

    /**
     * Convert {@link Address} to {@link AddressDTO}.
     * 
     * @param location
     *            to be converted
     * @return new {@link AddressDTO}.
     */
    AddressDTO convertToDto(Address location);

    /**
     * Convert {@link AntwoordberichtAPDADO} object into location object, set creation time and save it into DB.
     * 
     * @param kadasterLocation
     *            to be processed
     * @return newly created {@link Address} object.
     */
    Address convertAndSave(AntwoordberichtAPDADO kadasterLocation);

    /**
     * Check is difference between locations creation date and current date greater than allowed.
     * 
     * @param location
     *            to be checked
     * @return <code>true</code> if location is valid, else if is expired return
     */
    boolean isExpired(Address location);

    /**
     * Get max validation period for location in local storage.
     * 
     * @return value in seconds.
     */
    Integer getMaxValidPeriod();

    /**
     * Get web service client to make calls directly to the web service.
     * 
     * @return {@link AntwoordberichtAPDADO} answer from service.
     */
    IBagVsRaadplegenDatumADOV20090901 getWSClient();

}
