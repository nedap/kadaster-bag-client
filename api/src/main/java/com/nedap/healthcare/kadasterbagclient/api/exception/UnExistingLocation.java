package com.nedap.healthcare.kadasterbagclient.api.exception;

/**
 * Exception that should be thrown when some parts of system cant find matcing lcoation.
 * 
 * @author Dusko Vesin
 */
public class UnExistingLocation extends Exception {

    private static final long serialVersionUID = -412822262717735877L;
    private final String zipCode;
    private final Integer houseNumber;

    /**
     * Default constructor.
     * 
     * @param zipCode
     *            zip code
     * @param houseNumber
     *            house number.
     */
    public UnExistingLocation(final String zipCode, final Integer houseNumber) {
        super();
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

}
