package com.ly.dao;

import org.hibernate.LockMode;
import org.springframework.dao.DataAccessException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by yongliu on 7/21/16.
 */
public interface Dao {

  void clear();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   */
  void commit();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * delete.
   *
   * @param  o  Object
   */
  @SuppressWarnings("unchecked")
  void delete(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * deleteAll.
   *
   * @param   entities  Collection
   *
   * @throws  DataAccessException  exception
   */
  void deleteAll(Collection entities) throws DataAccessException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method to delete an object.
   *
   * @param  o  the object to delete
   */
  void deleteObject(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method to evict an object from the current session.
   *
   * @param  o  the object to save
   */
  void evictObject(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * find.
   *
   * @param   queryString  String
   *
   * @return  List
   *
   * @throws  DataAccessException  exception
   */
  List find(String queryString) throws DataAccessException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * find.
   *
   * @param   queryString  String
   * @param   value        Object
   *
   * @return  List
   *
   * @throws  DataAccessException  exception
   */
  List find(String queryString, Object value) throws DataAccessException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * find.
   *
   * @param   queryString  String
   * @param   values       Object
   *
   * @return  List
   */
  List find(String queryString, Object... values);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * find Object by criteria.
   */
  @SuppressWarnings("unchecked")
// List findByCriteria(DetachedCriteria criteria);

  /**
   * Generic method to flush current session.
   */
  void flush();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * get.
   *
   * @param   <T>    Serializable
   * @param   clazz  Class
   * @param   id     Serializable
   *
   * @return  T
   */
  <T> T get(Class<T> clazz, Serializable id);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for all.
   *
   * @param   clazz  Class
   *
   * @return  List
   */
  List getAll(Class clazz);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method to get an object based on class and identifier. An ObjectRetrievalFailureException Runtime Exception
   * is thrown if nothing is found.
   *
   * @param   clazz  model class to lookup
   * @param   id     the identifier (primary key) of the class
   *
   * @return  a populated object
   *
   * @see     org.springframework.orm.ObjectRetrievalFailureException
   */
  @SuppressWarnings("unchecked")
  Object getObject(Class clazz, Serializable id);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method used to get all objects of a particular type. This is the same as lookup up all rows in a table.
   *
   * @param   clazz  the type of objects (a.k.a. while table) to get data from
   *
   * @return  List of populated objects
   */
  @SuppressWarnings("unchecked")
  List getObjects(Class clazz);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * load.
   *
   * @param   <T>  Serializable
   * @param   obj  Class
   * @param   id   Serializable
   *
   * @return  T
   */
  <T> T load(Class<T> obj, Serializable id);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * load.
   *
   * @param   <T>          LockMode
   * @param   entityClass  Class
   * @param   id           Serializable
   * @param   lockMode     LockMode
   *
   * @return  T
   */
  <T> T load(Class<T> entityClass, Serializable id, LockMode lockMode);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * lock.
   *
   * @param  entity    Object
   * @param  lockMode  LockMode
   */
  void lock(Object entity, LockMode lockMode);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * merge.
   *
   * @param   o  Object
   *
   * @return  Object
   */
  Object merge(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method to merge an object - handles both update and insert.
   *
   * @param  o  the object to merge
   */
  void mergeObject(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * remove.
   *
   * @param  clazz  Class
   * @param  id     Serializable
   */
  void remove(Class clazz, Serializable id);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * removeObject.
   *
   * @param  obj  Object
   */
  void removeObject(Object obj);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method to delete an object based on class and id.
   *
   * @param  clazz  model class to lookup
   * @param  id     the identifier (primary key) of the class
   */
  @SuppressWarnings("unchecked")
  void removeObject(Class clazz, Serializable id);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * save.
   *
   * @param   o  Object
   *
   * @return  Object
   */
  Object save(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method to save an object - handles both update and insert.
   *
   * @param  o  the object to save
   */
  void saveObject(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * saveOrUpdate.
   *
   * @param  o  Object
   */
  void saveOrUpdate(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * saveOrUpdateAll.
   *
   * @param   entities  Collection
   *
   * @throws  DataAccessException  exception
   */
  void saveOrUpdateAll(Collection entities) throws DataAccessException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * update.
   *
   * @param  obj  Object
   */
  void update(Object obj);
}
