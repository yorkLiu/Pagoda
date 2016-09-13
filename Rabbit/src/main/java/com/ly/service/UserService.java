package com.ly.service;

import com.ly.model.User;


/**
 * Created by yongliu on 7/23/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 14:29
 */
public interface UserService extends BaseService {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for password failure lock count.
   *
   * @return  Integer
   */
  Integer getPasswordFailureLockCount();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for user by username.
   *
   * @param   username  String
   *
   * @return  User
   */
  User getUserByUsername(String username);
}
