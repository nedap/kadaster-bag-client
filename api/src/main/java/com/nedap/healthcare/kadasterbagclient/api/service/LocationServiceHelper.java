package com.nedap.healthcare.kadasterbagclient.api.service;

import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;

import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.IBagVsRaadplegenDatumADOV20090901;
import nl.kadaster.schemas.imbag.apd.v20090901.Verblijfsobject;

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
    AddressDTO convertToDto(Verblijfsobject kadasterLocation);

    /**
     * Get web service client to make calls directly to the web service.
     * 
     * @return {@link AntwoordberichtAPDADO} answer from service.
     */
    IBagVsRaadplegenDatumADOV20090901 getWSClient();

}
