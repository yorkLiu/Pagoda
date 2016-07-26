package com.ly.service;

import com.ly.dao.Dao;

import java.io.Serializable;

import java.util.List;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/21/2016 14:46
 */
public interface BaseService {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   */
  void commit();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method to flush the session.
   */
  void flush();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * get.
   *
   * @param   clazz  Class
   * @param   id     Serializable
   *
   * @return  Object
   */
  Object get(Class clazz, Serializable id);

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
   * Generic method to get an object based on class and identifier.
   *
   * @param   clazz  model class to lookup
   * @param   id     the identifier (primary key) of the class
   *
   * @return  a populated object
   * 
   * @see org.springframework.orm.ObjectRetrievalFailureException   
   */
  @SuppressWarnings("unchecked")
  Object getObject(Class clazz, Serializable id);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method used to get a all objects of a particular type.
   *
   * @param   clazz  the type of objects
   *
   * @return  List of populated objects
   */
  @SuppressWarnings("unchecked")
  List getObjects(Class clazz);

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
   * Generic method to delete an object based on class and id.
   *
   * @param  clazz  model class to lookup
   * @param  id     the identifier of the class
   */
  @SuppressWarnings("unchecked")
  void removeObject(Class clazz, Serializable id);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * save.
   *
   * @param   o  Object
   *
   * @return  save.
   */
  Object save(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Generic method to save an object.
   *
   * @param  o  the object to save
   */
  void saveObject(Object o);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Expose the setDao method for testing purposes.
   *
   * @param  dao  DOCUMENT ME!
   */
  void setDao(Dao dao);

} // end interface BaseService
