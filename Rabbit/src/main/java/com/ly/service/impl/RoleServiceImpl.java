package com.ly.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.ly.dao.RoleDao;

import com.ly.service.RoleService;


/**
 * Created by yongliu on 9/13/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/13/2016 14:49
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private RoleDao roleDao;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.RoleService#hasFeature(java.util.Set, java.lang.String)
   */
  @Override public boolean hasFeature(Set<String> roles, String featureName) {
    return roleDao.hasFeature(roles, featureName);
  }
}
