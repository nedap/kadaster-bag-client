package com.nedap.healthcare.kadasterbagclient.api.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.nedap.healthcare.kadasterbagclient.api.model.AbstractPersistedEntity;

/**
 * This class provides default implementations for the {@link GenericDao} methods.
 * 
 * @author Dusko Vesin
 * @param <T>
 *            type of the persisted entity this DAO will handle
 */
public abstract class AbstractGenericHibernateDao<T extends AbstractPersistedEntity> implements GenericDao<T> {

    /**
     * Path separator for hibernate aliases. Value: {@value DOT}
     */
    protected static final String DOT = ".";
    /**
     * Field PERCENT. (value is ""%"") Value: {@value PERCENT}
     */
    protected static final String PERCENT = "%";

    private final Class<T> persistentClass;

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Default constructor.
     */
    @SuppressWarnings("unchecked")
    public AbstractGenericHibernateDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    /**
     * Get persistence class.
     * 
     * @return Class
     */
    protected Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findById(final Long id) {
        return (T) getCurrentSession().get(getPersistentClass(), id);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#save(T)
     * @param entity
     *            T
     */
    @Override
    public void save(final T entity) {
        getCurrentSession().save(entity);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#update(T)
     * @param entity
     *            T
     */
    @Override
    public void update(final T entity) {
        getCurrentSession().update(entity);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#saveOrUpdate(T)
     * @param entity
     *            T
     */
    @Override
    public void saveOrUpdate(final T entity) {
        getCurrentSession().saveOrUpdate(merge(entity));
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#merge(T)
     * @param entity
     *            T
     * @return T
     */
    @SuppressWarnings("unchecked")
    @Override
    public T merge(final T entity) {
        return (T) getCurrentSession().merge(entity);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#delete(T)
     * @param entity
     *            T
     */
    @Override
    public void delete(final T entity) {
        getCurrentSession().delete(entity);
        getCurrentSession().flush();
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#deleteById(Long)
     * @param id
     *            Long
     */
    @Override
    public void deleteById(final Long id) {
        getCurrentSession().delete(findById(id));
        getCurrentSession().flush();
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#testFindAll()
     * @return List<T>
     */
    @Override
    public List<T> findAll() {
        return findByCriteria();
    }

    /**
     * Use this inside subclasses as a convenience method.
     * 
     * @param criterion
     *            Criterion[]
     * @return List
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(final Criterion... criterion) {
        final Criteria crit = getCurrentSession().createCriteria(getPersistentClass());
        for (final Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    /**
     * Use this inside subclasses as a convenience method.
     * 
     * @param criterion
     *            Criterion[]
     * @return T
     */
    @SuppressWarnings("unchecked")
    protected T findByCriteriaUnique(final Criterion... criterion) {
        final Criteria crit = getCurrentSession().createCriteria(getPersistentClass());
        for (final Criterion c : criterion) {
            crit.add(c);
        }
        return (T) crit.uniqueResult();
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#findByCriteria(Map)
     * @param criterias
     *            Map
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByCriteria(final Map<?, ?> criterias) {
        final Criteria criteria = getCurrentSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.allEq(criterias));
        return criteria.list();
    }


    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#findByExample(T, String[])
     * @param exampleInstance
     *            T
     * @param excludeProperty
     *            String[]
     * @return List<T>
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByExample(final T exampleInstance, final String[] excludeProperty) {
        final Criteria crit = getCurrentSession().createCriteria(getPersistentClass());
        final Example example = exampleCriteria(exampleInstance, excludeProperty);
        crit.add(example);
        return crit.list();
    }

    /**
     * Example criteria will be generated from example instance with possibility of fine tuning using excluded
     * properties.
     * 
     * @param exampleInstance
     *            T
     * @param excludeProperty
     *            String[]
     * @return Example
     */
    private Example exampleCriteria(final T exampleInstance, final String[] excludeProperty) {
        final Example example = Example.create(exampleInstance).excludeZeroes().enableLike(MatchMode.ANYWHERE)
                .ignoreCase();
        for (final String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        return example;
    }

    @Override
    public void addExampleCriteria(final T example, final Criteria criteria) {
        addExampleCriteria(example, criteria, new String[] {});
    }

    @Override
    public void addExampleCriteria(final T example, final Criteria criteria, final String[] excludeProperty) {
        if (example != null) {
            criteria.add(exampleCriteria(example, excludeProperty));
        }
    }

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
