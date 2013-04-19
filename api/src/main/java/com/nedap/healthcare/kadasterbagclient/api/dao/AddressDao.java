package com.nedap.healthcare.kadasterbagclient.api.dao;

import com.nedap.healthcare.kadasterbagclient.api.model.Address;

/**
 * DAO interface for accessing {@link Address} objects.
 * 
 * @author Dusko Vesin
 */
public interface AddressDao extends GenericDao<Address> {

    /**
     * Find unique {@link Address} entity that match to provided parameters.
     * 
     * @param countryCode
     *            country code
     * @param postalCode
     *            postal code
     * @param number
     *            house number
     * @param extension
     *            house number extension
     * @return {@link Address}
     */
    Address findByCountryPostalCodeAndNumber(String countryCode, String postalCode, Integer number, String extension);

}
