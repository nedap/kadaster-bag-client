package com.nedap.healthcare.kadasterbagclient.api.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Address entity is used to pre cache data collected from kadaster web service.
 * 
 * @author Dusko Vesin
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {Address.COUNTRY_CODE, Address.POSTAL_CODE, Address.NUMBER})})
public class Address extends AbstractPersistedEntity {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1308795024262635690L;

    /** ISO 3166-1 Alpha-2 country code. */
    @Column(nullable = false)
    private String countryCode;
    public static final String COUNTRY_CODE = "countryCode";

    /** String of numbers and/or digits. */
    @Column(nullable = false)
    private String postalCode;
    public static final String POSTAL_CODE = "postalCode";

    /** City. */
    @Column(nullable = false)
    private String city;
    public static final String CITY = "city";

    /** Street. */
    @Column(nullable = false)
    private String street;
    public static final String STREET = "street";

    /** House number. */
    @Column(nullable = false)
    private Integer number;
    public static final String NUMBER = "number";

    /** GPS latitude. */
    @Column(nullable = false)
    private String latitude;
    public static final String LATITUDE = "latitude";

    /** GPS longitude. */
    @Column(nullable = false)
    private String longitude;
    public static final String LONGITUDE = "longitude";

    /** The encoding is valid from this date. */
    @Column(nullable = false)
    private String validFrom;
    public static final String VALID_FROM = "validFrom";

    /** The encoding is valid to this date. */
    @Column(nullable = false)
    private String validTo;
    public static final String VALID_TO = "validTo";

    /** The creation date. */
    @Column(nullable = false)
    private Calendar creationDate;
    public static final String CREATION_DATE = "creationDate";

    /**
     * Default constructor.
     */
    public Address() {
        super();
    }

    /**
     * Instantiates a new location.
     * 
     * @param countryCode
     *            the country code
     * @param postalCode
     *            the postal code
     * @param number
     *            the number
     * @param latitude
     *            the latitude
     * @param longitude
     *            the longitude
     * @param validFrom
     *            the valid from
     * @param validTo
     *            the valid to
     * @param creationDate
     *            the creation date
     */
    public Address(final String countryCode, final String postalCode, final Integer number, final String latitude,
            final String longitude, final String validFrom, final String validTo, final Calendar creationDate) {
        super();
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.creationDate = creationDate;
    }

    /**
     * Gets the country code.
     * 
     * @return the country code
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the country code.
     * 
     * @param countryCode
     *            the new country code
     */
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Gets the postal code.
     * 
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code.
     * 
     * @param postalCode
     *            the new postal code
     */
    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the number.
     * 
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Sets the number.
     * 
     * @param number
     *            the new number
     */
    public void setNumber(final Integer number) {
        this.number = number;
    }

    /**
     * Gets the latitude.
     * 
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     * 
     * @param latitude
     *            the new latitude
     */
    public void setLatitude(final String latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     * 
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     * 
     * @param longitude
     *            the new longitude
     */
    public void setLongitude(final String longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the valid from.
     * 
     * @return the valid from
     */
    public String getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the valid from.
     * 
     * @param validFrom
     *            the new valid from
     */
    public void setValidFrom(final String validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * Gets the valid to.
     * 
     * @return the valid to
     */
    public String getValidTo() {
        return validTo;
    }

    /**
     * Sets the valid to.
     * 
     * @param validTo
     *            the new valid to
     */
    public void setValidTo(final String validTo) {
        this.validTo = validTo;
    }

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     */
    public Calendar getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     * 
     * @param creationDate
     *            the new creation date
     */
    public void setCreationDate(final Calendar creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the city.
     * 
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     * 
     * @param city
     *            the new city
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Gets the street.
     * 
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street.
     * 
     * @param street
     *            the new street
     */
    public void setStreet(final String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address [countryCode=" + countryCode + ", postalCode=" + postalCode + ", city=" + city + ", street="
                + street + ", number=" + number + ", latitude=" + latitude + ", longitude=" + longitude
                + ", validFrom=" + validFrom + ", validTo=" + validTo + ", creationDate=" + creationDate + "]";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Address other = (Address) obj;
        if (city == null) {
            if (other.city != null) {
                return false;
            }
        } else if (!city.equals(other.city)) {
            return false;
        }
        if (countryCode == null) {
            if (other.countryCode != null) {
                return false;
            }
        } else if (!countryCode.equals(other.countryCode)) {
            return false;
        }
        if (creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        if (latitude == null) {
            if (other.latitude != null) {
                return false;
            }
        } else if (!latitude.equals(other.latitude)) {
            return false;
        }
        if (longitude == null) {
            if (other.longitude != null) {
                return false;
            }
        } else if (!longitude.equals(other.longitude)) {
            return false;
        }
        if (number == null) {
            if (other.number != null) {
                return false;
            }
        } else if (!number.equals(other.number)) {
            return false;
        }
        if (postalCode == null) {
            if (other.postalCode != null) {
                return false;
            }
        } else if (!postalCode.equals(other.postalCode)) {
            return false;
        }
        if (street == null) {
            if (other.street != null) {
                return false;
            }
        } else if (!street.equals(other.street)) {
            return false;
        }
        if (validFrom == null) {
            if (other.validFrom != null) {
                return false;
            }
        } else if (!validFrom.equals(other.validFrom)) {
            return false;
        }
        if (validTo == null) {
            if (other.validTo != null) {
                return false;
            }
        } else if (!validTo.equals(other.validTo)) {
            return false;
        }
        return true;
    }

}
