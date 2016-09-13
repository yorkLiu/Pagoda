package com.ly.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Repository;

import com.ly.dao.UserDao;

import com.ly.model.User;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by yongliu on 7/23/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 14:42
 */
@Repository("userDao")
@Transactional
public class UserDaoHibernate extends BaseDaoHibernate implements UserDao, UserDetailsService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  private final Log log = LogFactory.getLog(getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  public Integer getPasswordFailureLockCount() {
    Integer           passwordFailureLockCount = null;
//    ApplicationConfig ac                       = null;
//
//    try {
//      ac = this.get(ApplicationConfig.class, "passwordFailureLockCount");
//    } catch (Exception e) {
//      log.warn("passwordFailureLockCount is not configured");
//    }
//
//    if ((ac != null) && (ac.getFeatureValue() != null)) {
//      passwordFailureLockCount = Integer.parseInt(ac.getFeatureValue().toString());
//    }

    return passwordFailureLockCount;
  }

  /**
   * @see  com.ly.dao.UserDao#loadUserByUsername(java.lang.String)
   */
  @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<User> users = getSession().createQuery("from User where username=?").setParameter(0, username).list();

    if ((users == null) || users.isEmpty()) {
      if (log.isDebugEnabled()) {
        log.debug("Could not found the User by username#" + username);
      }

      throw new UsernameNotFoundException("Could not found the User by username#" + username);

    } else {
      if (log.isDebugEnabled()) {
        log.debug("User loaded: " + StringEscapeUtils.escapeXml(username));
      }

      User user = users.get(0);

      if (!user.getLocked() && !user.getExpired() && user.isEnabled()) {
        return user;
      }
    }

    return null;
  }
} // end class UserDaoHibernate
