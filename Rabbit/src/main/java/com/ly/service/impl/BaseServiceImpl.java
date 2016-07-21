package com.ly.service.impl;

import java.io.Serializable;

import java.util.List;

import com.ly.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ly.dao.Dao;
import org.springframework.stereotype.Service;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/21/2016 14:59
 */

@Service("baseService")
public class BaseServiceImpl implements BaseService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  @Autowired
  @Qualifier("baseDao")
  protected Dao dao;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#commit()
   */
  @Override public void commit() {
    dao.commit();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#flush()
   */
  @Override public void flush() {
    dao.flush();

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#get(java.lang.Class, java.io.Serializable)
   */
  @Override public Object get(Class clazz, Serializable id) {
    return dao.get(clazz, id);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#getAll(java.lang.Class)
   */
  @Override public List getAll(Class clazz) {
    return dao.getAll(clazz);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#getObject(java.lang.Class, java.io.Serializable)
   */
  @Override public Object getObject(Class clazz, Serializable id) {
    return dao.getObject(clazz, id);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#getObjects(java.lang.Class)
   */
  @Override public List getObjects(Class clazz) {
    return dao.getObjects(clazz);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#remove(java.lang.Class, java.io.Serializable)
   */
  @Override public void remove(Class clazz, Serializable id) {
    dao.remove(clazz, id);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#removeObject(java.lang.Class, java.io.Serializable)
   */
  @Override public void removeObject(Class clazz, Serializable id) {
    dao.removeObject(clazz, id);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#save(java.lang.Object)
   */
  @Override public Object save(Object o) {
    return dao.save(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#saveObject(java.lang.Object)
   */
  @Override public void saveObject(Object o) {
    dao.saveObject(o);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  BaseService#setDao(com.ly.dao.Dao)
   */
  @Override public void setDao(Dao dao) {
    this.dao = dao;
  }
} // end class BaseService
