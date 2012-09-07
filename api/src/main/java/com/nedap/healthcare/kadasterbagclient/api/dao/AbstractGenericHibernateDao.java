package com.nedap.healthcare.kadasterbagclient.api.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.nedap.healthcare.kadasterbagclient.api.model.AbstractPersistedEntity;

/**
 * This class provides default implementations for the {@link GenericDao} methods.
 * 
 * @author Dusko Vesin
 * @param <T>
 *            type of the persisted entity this DAO will handle
 */
public abstract class AbstractGenericHibernateDao<T extends AbstractPersistedEntity> extends HibernateDaoSupport
        implements GenericDao<T> {

    /**
     * Path separator for hibernate aliases. Value: {@value DOT}
     */
    protected static final String DOT = ".";
    /**
     * Field PERCENT. (value is ""%"") Value: {@value PERCENT}
     */
    protected static final String PERCENT = "%";

    private final Class<T> persistentClass;

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

    /**
     * Setting session factory from spring.
     * 
     * @param sessionFactory
     *            active session factory
     */
    @Autowired
    public void setDaoSessionFactory(final SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findById(final Long id) {
        return (T) getSession().get(getPersistentClass(), id);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#save(T)
     * @param entity
     *            T
     */
    @Override
    public void save(final T entity) {
        getSession().save(entity);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#update(T)
     * @param entity
     *            T
     */
    @Override
    public void update(final T entity) {
        getSession().update(entity);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#saveOrUpdate(T)
     * @param entity
     *            T
     */
    @Override
    public void saveOrUpdate(final T entity) {
        getSession().saveOrUpdate(merge(entity));
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
        return (T) getSession().merge(entity);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#delete(T)
     * @param entity
     *            T
     */
    @Override
    public void delete(final T entity) {
        getSession().delete(entity);
    }

    /**
     * @see nl.deepbluesoftware.greentimes.backend.dao.GenericDao#deleteById(Long)
     * @param id
     *            Long
     */
    @Override
    public void deleteById(final Long id) {
        getSession().delete(findById(id));
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
        final Criteria crit = getSession().createCriteria(getPersistentClass());
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
        final Criteria crit = getSession().createCriteria(getPersistentClass());
        for (final Criterion c : criterion) {
            crit.add(c);
        }
        return (T) crit.uniqueResult();
    }

    /**
     * Use this inside subclasses as a convenience method.
     * 
     * @param criterion
     *            Criterion[]
     * @return T
     */
    @SuppressWarnings("unchecked")
    protected T findUniqueByCriteria(final Criterion... criterion) {
        final Criteria crit = getSession().createCriteria(getPersistentClass());
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
        final Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.allEq(criterias));
        return criteria.list();
    }

    /**
     * @param query
     *            String
     * @param namedParams
     *            String[]
     * @param params
     *            Object[]
     * @return int
     */
    protected int executeQuery(final String query, final String[] namedParams, final Object[] params) {
        final Query q = getSession().createQuery(query);

        if (namedParams != null) {
            for (int i = 0; i < namedParams.length; i++) {
                q.setParameter(namedParams[i], params[i]);
            }
        }

        return q.executeUpdate();
    }

    /**
     * This method will execute an HQL query without named parameters and parameters and return the number of affected
     * entities.
     * 
     * @param query
     *            String
     * @return int
     */
    protected int executeQuery(final String query) {
        return executeQuery(query, null, null);
    }

    /**
     * This method will execute a Named HQL query and return the number of affected entities.
     * 
     * @param namedQuery
     *            String
     * @param namedParams
     *            String[]
     * @param params
     *            Object[]
     * @return int
     */
    protected int executeNamedQuery(final String namedQuery, final String[] namedParams, final Object[] params) {
        final Query q = getSession().getNamedQuery(namedQuery);

        if (namedParams != null) {
            for (int i = 0; i < namedParams.length; i++) {
                q.setParameter(namedParams[i], params[i]);
            }
        }

        return q.executeUpdate();
    }

    /**
     * This method will execute a Named HQL query without named parameters and parameters and return the number of
     * affected entities.
     * 
     * @param namedQuery
     *            String
     * @return int
     */
    protected int executeNamedQuery(final String namedQuery) {
        return executeNamedQuery(namedQuery, null, null);
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
        final Criteria crit = getSession().createCriteria(getPersistentClass());
        final Example example = exampleCriteria(exampleInstance, excludeProperty);
        crit.add(example);
        return crit.list();
    }

    /**
     * TODO - write method description exampleCriteria.
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

    /**
     * Surrounds parameter String likeParameter with {@value #PERCENT}.
     * 
     * @param likeParameter
     *            String
     * @return String
     */
    protected String surroundWithPercent(final String likeParameter) {
        return PERCENT + likeParameter + PERCENT;
    }

}
