package com.nedap.healthcare.kadasterbagclient.api.dao;

import org.junit.Test;

import com.nedap.healthcare.kadasterbagclient.api.AbstractSpringTest;
import com.nedap.healthcare.kadasterbagclient.api.model.AbstractPersistedEntity;

/**
 * Abstract DAO tester. Should be inherited by all test that test any DAO who extends {@link GenericDao}.
 * 
 * @author Dusko Vesin
 * @param <T>
 */
public abstract class AbstractDaoTransactionalTest<T extends AbstractPersistedEntity> extends AbstractSpringTest {

    /**
     * Tweak so we can create generic test.
     * 
     * @return entity class
     */
    public abstract Class<T> getEntityClass();

    /**
     * Testing DAO save and find by id methods with valid data.
     */
    @Test
    public void testGetByIdAndSave() {

        // data preparation
        final T createValidObject = createValidObject();
        getDao().save(createValidObject);

        takeSnapshot();

        // call method
        final T byId = getDao().findById(createValidObject.getId());

        // asserting
        assertObjects(createValidObject, byId);

    }

    /**
     * Testing find all method for DAO layer classes
     */
    @Test
    public abstract void testFindAll();

    /**
     * Testing DAO update method.
     */
    @Test
    public void testUpdate() {

        // data preparation
        final T createValidObject = createValidObject();
        getDao().save(createValidObject);

        T byId = getDao().findById(createValidObject.getId());
        assertObjects(createValidObject, byId);

        takeSnapshot();

        // call method
        getDao().update(createValidObject);

        // asserting
        byId = getDao().findById(createValidObject.getId());
        assertObjects(createValidObject, byId);
    }

    /**
     * Testing DAO saveOrUpdate method.
     */
    @Test
    public void testSaveOrUpdate() {

        // data preparation
        final T createValidObject = createValidObject();
        getDao().save(createValidObject);

        T byId = getDao().findById(createValidObject.getId());
        assertObjects(createValidObject, byId);

        takeSnapshot();

        // call method
        getDao().saveOrUpdate(createValidObject);

        // asserting
        byId = getDao().findById(createValidObject.getId());
        assertObjects(createValidObject, byId);
    };

    /**
     * Testing DAO delete method.
     */
    @Test
    public void testDelete() {

        // data preparation
        final T createValidObject = createValidObject();
        getDao().save(createValidObject);
        takeSnapshot();

        // call method
        getDao().delete(createValidObject);

        // asserting
        markEntityAsDeleted(createValidObject);

    }

    @Test
    public void testEquals() {

        // data preparation
        final T createValidObject1 = createValidObject();
        final T createValidObject2 = createValidObject();

        // asserting
        assertTrue(!createValidObject1.equals(null));
        assertTrue(!createValidObject1.equals(new Object()));
        assertTrue(createValidObject1.equals(createValidObject1));
        assertTrue(createValidObject1.equals(createValidObject2));
    }

    /**
     * Testing DAO deleteById method.
     */
    @Test
    public void deleteById() {

        // data preparation
        final T createValidObject = createValidObject();
        getDao().save(createValidObject);

        takeSnapshot();

        // call method
        getDao().deleteById(createValidObject.getId());

        // asserting
        markEntityAsDeleted(createValidObject);
    }

    /**
     * Get DAO for entity class.
     * 
     * @return GenericDao<T>
     */
    protected abstract GenericDao<T> getDao();

    /**
     * Create valid object.
     * 
     * @return T
     */
    public abstract T createValidObject();

}
