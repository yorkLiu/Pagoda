package com.ly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.ly.dao.UserDao;

import com.ly.model.User;

import com.ly.service.UserService;


/**
 * Created by yongliu on 7/23/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 16:57
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private UserDao userDao;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.UserService#getPasswordFailureLockCount()
   */
  @Override public Integer getPasswordFailureLockCount() {
    return userDao.getPasswordFailureLockCount();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.UserService#getUserByUsername(java.lang.String)
   */
  @Override public User getUserByUsername(String username) {
    return (User) userDao.loadUserByUsername(username);
  }
}
