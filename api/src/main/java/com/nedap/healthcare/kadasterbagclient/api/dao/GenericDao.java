package com.nedap.healthcare.kadasterbagclient.api.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;

import com.nedap.healthcare.kadasterbagclient.api.model.AbstractPersistedEntity;

/**
 * Generic data access object that provides commonly used methods for a single entity.
 * 
 * @author Dusko Vesin
 * @param <T>
 *            type of persisted entity this DAO will handle
 */
public interface GenericDao<T extends AbstractPersistedEntity> {

    /**
     * Find entity by ID.
     * 
     * @param id
     *            Long
     * @return T
     */
    T findById(Long id);

    /**
     * Retrieve all entities.
     * 
     * @return List<T>
     */
    List<T> findAll();

    /**
     * Find entities by given {@link Criteria}.
     * 
     * @param criterias
     *            Map<?,?>
     * @return List<T>
     */
    List<T> findByCriteria(Map<?, ?> criterias);

    /**
     * Find entities by given example entity.
     * 
     * @param exampleInstance
     *            T
     * @param excludeProperty
     *            String[]
     * @return List<T>
     */
    List<T> findByExample(T exampleInstance, String[] excludeProperty);

    /**
     * Save entity to database.
     * 
     * @param entity
     *            T
     */
    void save(T entity);

    /**
     * Update entity.
     * 
     * @param entity
     *            T
     */
    void update(T entity);

    /**
     * Save entity to database or update it if already exists in database.
     * 
     * @param entity
     *            T
     */
    void saveOrUpdate(T entity);

    /**
     * Delete entity.
     * 
     * @param entity
     *            T
     */
    void delete(T entity);

    /**
     * Delete entity with given ID.
     * 
     * @param id
     *            Long
     */
    void deleteById(Long id);

    /**
     * Add example criteria with given example entity to specified criteria.
     * 
     * @param example
     *            T
     * @param criteria
     *            Criteria
     */
    void addExampleCriteria(T example, Criteria criteria);

    /**
     * Add example criteria with given example entity and excluded properties to specified criteria.
     * 
     * @param example
     *            T
     * @param criteria
     *            Criteria
     * @param excludeProperty
     *            propertys that should be excluded from example object
     */
    void addExampleCriteria(T example, Criteria criteria, String[] excludeProperty);

    /**
     * Merge entity.
     * 
     * @param entity
     *            T
     * @return T
     */
    T merge(T entity);

}
