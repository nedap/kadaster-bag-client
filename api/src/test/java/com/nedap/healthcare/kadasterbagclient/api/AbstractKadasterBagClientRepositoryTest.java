package com.nedap.healthcare.kadasterbagclient.api;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.dao.AddressDao;
import com.nedap.healthcare.kadasterbagclient.api.model.AbstractPersistedEntity;
import com.nedap.healthcare.kadasterbagclient.api.model.Address;
import com.nedap.healthcare.kadasterbagclient.api.model.AddressDTO;

import eu.execom.testutil.AbstractExecomRepositoryAssert;

/**
 * {@link AbstractExecomRepositoryAssert} implementation with specific adjustments for Kadaster Bag Client project.
 * 
 * @author Dusko Vesin
 */
public abstract class AbstractKadasterBagClientRepositoryTest extends
        AbstractExecomRepositoryAssert<AbstractPersistedEntity, Long> {

    public static final String TEXT_CONTEXT = "/META-INF/test-context.xml";

    @Autowired
    private AddressDao locationDao;

    @Override
    protected void initEntityList(final List<Class<?>> entityTypes) {
        entityTypes.add(Address.class);
    }

    @After
    public final void checkTestValidity() {
        assertDbState();
    }

    @Override
    protected void initComplexTypes(final List<Class<?>> complexTypes) {
        complexTypes.add(AddressDTO.class);
    }

    @Override
    protected void initIgnoredTypes(final List<Class<?>> ignoredTypes) {
        // no ignored types
    }

    @Override
    protected List<?> findAll(final Class<?> entityClass) {
        if (entityClass == Address.class) {
            return locationDao.findAll();
        }
        throw new IllegalStateException("Unsupported entity class " + entityClass);
    }

    @Override
    protected AbstractPersistedEntity findById(final Class<?> entityClass, final Long id) {
        if (entityClass == Address.class) {
            return locationDao.findById(id);
        }
        throw new IllegalStateException("Unsupported entity class " + entityClass);
    }

    @Override
    protected <T> void customAssertEquals(final T expected, final T actual) {
        if (actual instanceof DateTime) {
            assertEquals(((DateTime) expected).getMillis(), ((DateTime) actual).getMillis());
        } else {
            assertEquals(expected, actual);
        }
    }

}
