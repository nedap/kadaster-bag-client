package com.nedap.healthcare.kadasterbagclient.api.dao;

import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Repository;

import com.nedap.healthcare.kadasterbagclient.api.model.Address;

/**
 * Hibernate implementation of {@link AddressDao}.
 * 
 * @author Dusko Vesin
 */
@Repository
class LocationHibernateDao extends AbstractGenericHibernateDao<Address> implements AddressDao {

    @Override
    public Address findByCountryPostalCodeAndNumber(@NotBlank final String countryCode,
            @NotBlank final String postalCode, @NotBlank final Integer number) {

        return findByCriteriaUnique(Restrictions.eq(Address.COUNTRY_CODE, countryCode),
                Restrictions.eq(Address.POSTAL_CODE, postalCode), Restrictions.eq(Address.NUMBER, number));

    }

}
