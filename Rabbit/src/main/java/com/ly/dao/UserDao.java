package com.ly.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ly.model.User;


/**
 * Created by yongliu on 7/23/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 14:30
 */
public interface UserDao extends Dao {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for password failure lock count.
   *
   * @return  Integer
   */
  Integer getPasswordFailureLockCount();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Gets users information based on login name.
   *
   * @param   username  the user's username
   *
   * @return  userDetails populated userDetails object
   *
   * @throws  UsernameNotFoundException  DOCUMENT ME!
   */
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
