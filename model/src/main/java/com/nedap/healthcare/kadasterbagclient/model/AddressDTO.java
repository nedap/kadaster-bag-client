package com.nedap.healthcare.kadasterbagclient.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddressDTO {

    /** ISO 3166-1 Alpha-2 country code. */
    private String countryCode;
    public final static String COUNTRY_CODE = "countryCode";

    /** String of numbers and/or digits */
    private String postalCode;
    public final static String POSTAL_CODE = "postalCode";

    /** City */
    private String city;
    public final static String CITY = "city";

    /** Street */
    private String street;
    public final static String STREET = "street";

    /** House number */
    private Integer number;
    public final static String NUMBER = "number";

    /** House number postfix */
    private String numberPostfix;
    public final static String NUMBER_POSTFIX = "numberPostfix";

    /** GPS latitude */
    private String latitude;
    public final static String LATITUDE = "latitude";

    /** GPS longitude */
    private String longitude;
    public final static String LONGITUDE = "longitude";

    /** The encoding is valid from this date */
    private String validFrom;
    public final static String VALID_FROM = "validFrom";

    /** The encoding is valid to this date */
    private String validTo;
    public final static String VALID_TO = "validTo";

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getNumber() {
        return number;
    }

    public String getNumberPostfix() {
        return numberPostfix;
    }

    public void setNumberPostfix(final String numberPostfix) {
        this.numberPostfix = numberPostfix;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(final String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(final String longitude) {
        this.longitude = longitude;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(final String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(final String validTo) {
        this.validTo = validTo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
