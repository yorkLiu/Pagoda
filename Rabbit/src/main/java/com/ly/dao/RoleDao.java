package com.ly.dao;

import java.util.Set;


/**
 * Created by yongliu on 9/13/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/13/2016 14:47
 */
public interface RoleDao extends Dao {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * hasFeature.
   *
   * @param   roles    Set
   * @param   feature  String
   *
   * @return  boolean
   */
  boolean hasFeature(Set<String> roles, String feature);
}
