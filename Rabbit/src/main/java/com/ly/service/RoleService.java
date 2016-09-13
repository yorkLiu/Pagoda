package com.ly.service;

import java.util.Set;


/**
 * Created by yongliu on 9/13/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/13/2016 14:45
 */
public interface RoleService extends BaseService {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * hasFeature.
   *
   * @param   roles        Set
   * @param   featureName  String
   *
   * @return  boolean
   */
  boolean hasFeature(Set<String> roles, String featureName);
}
