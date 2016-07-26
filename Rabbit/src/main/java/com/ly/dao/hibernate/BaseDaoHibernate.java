package com.ly.dao.hibernate;

import java.io.Serializable;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.query.Query;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;

import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import com.ly.dao.Dao;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/21/2016 15:04
 */

@Repository("baseDao")
public class BaseDaoHibernate implements Dao {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private SessionFactory sessionFactory;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#clear()
   */
  @Override public void clear() {
    getSession().clear();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#commit()
   */
  @Override public void commit() {
    getSession().flush();
    getSession().clear();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#delete(java.lang.Object)
   */
  @Override
  @SuppressWarnings("unchecked")
  public void delete(Object o) {
    getSession().delete(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#deleteAll(java.util.Collection)
   */
  @Override public void deleteAll(final Collection entities) throws DataAccessException {
    for (Object entity : entities) {
      getSession().delete(entity);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#deleteObject(java.lang.Object)
   */
  @Override
  @SuppressWarnings("unchecked")
  public void deleteObject(Object o) {
    getSession().delete(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#evictObject(java.lang.Object)
   */
  @Override public void evictObject(Object o) {
    getSession().evict(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#find(java.lang.String)
   */
  @Override public List find(String queryString) throws DataAccessException {
    return find(queryString, (Object[]) null);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#find(java.lang.String, java.lang.Object)
   */
  @Override public List find(String queryString, Object value) throws DataAccessException {
    return find(queryString, new Object[] { value });
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#find(java.lang.String, java.lang.Object[])
   */
  @Override public List find(final String queryString, final Object... values) {
    Query queryObject = getSession().createQuery(queryString);

    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        queryObject.setParameter(i, values[i]);
      }
    }

    return queryObject.list();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#flush()
   */
  @Override public void flush() {
    getSession().flush();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#get(java.lang.Class, java.io.Serializable)
   */
  @Override public <T> T get(Class<T> clazz, Serializable id) {
    T o = getSession().get(clazz, id);


    return o;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#getAll(java.lang.Class)
   */
  @Override public List getAll(Class clazz) {
    Criteria criteria = getSession().createCriteria(clazz);

    return criteria.list();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#getObject(java.lang.Class, java.io.Serializable)
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object getObject(Class clazz, Serializable id) {
    Object o = getSession().get(clazz, id);

    return o;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#getObjects(java.lang.Class)
   */
  @Override
  @SuppressWarnings("unchecked")
  public List getObjects(Class clazz) {
    Criteria criteria = getSession().createCriteria(clazz);

    return criteria.list();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for session.
   *
   * @return  Session
   */
  public Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#load(java.lang.Class, java.io.Serializable)
   */
  @Override public <T> T load(Class<T> obj, Serializable id) {
    return getSession().load(obj, id);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#load(java.lang.Class, java.io.Serializable, org.hibernate.LockMode)
   */
  @Override public <T> T load(Class<T> entityClass, Serializable id, LockMode lockMode) {
    return getSession().load(entityClass, id, lockMode);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#lock(java.lang.Object, org.hibernate.LockMode)
   */
  @Override public void lock(Object entity, LockMode lockMode) {
    getSession().lock(entity, lockMode);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#merge(java.lang.Object)
   */
  @Override public Object merge(Object o) {
    return getSession().merge(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#mergeObject(java.lang.Object)
   */
  @Override public void mergeObject(Object o) {
    getSession().merge(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#remove(java.lang.Class, java.io.Serializable)
   */
  @Override public void remove(Class clazz, Serializable id) {
    getSession().delete(get(clazz, id));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#removeObject(java.lang.Object)
   */
  @Override public void removeObject(Object obj) {
    getSession().delete(obj);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#removeObject(java.lang.Class, java.io.Serializable)
   */
  @Override
  @SuppressWarnings("unchecked")
  public void removeObject(Class clazz, Serializable id) {
    getSession().delete(getObject(clazz, id));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#save(java.lang.Object)
   */
  @Override public Object save(Object o) {
    return getSession().merge(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#saveObject(java.lang.Object)
   */
  @Override public void saveObject(Object o) {
    getSession().saveOrUpdate(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#saveOrUpdate(java.lang.Object)
   */
  @Override public void saveOrUpdate(Object o) {
    getSession().saveOrUpdate(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#saveOrUpdateAll(java.util.Collection)
   */
  @Override public void saveOrUpdateAll(final Collection entities) throws DataAccessException {
    for (Object entity : entities) {
      getSession().saveOrUpdate(entity);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for session factory.
   *
   * @param  sessionFactory  SessionFactory
   */
  @Autowired public final void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.Dao#update(java.lang.Object)
   */
  @Override public void update(Object obj) {
    getSession().update(obj);
  }

} // end class BaseDaoHibernate
