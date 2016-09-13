package com.ly.dao.hibernate;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ly.dao.RoleDao;


/**
 * Created by yongliu on 9/13/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/13/2016 14:48
 */
@Repository("roleDao")
public class RoleDaoHibernate extends BaseDaoHibernate implements RoleDao {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.RoleDao#hasFeature(java.util.Set, java.lang.String)
   */
  @Override public boolean hasFeature(Set<String> roles, String featureName) {
    if ((roles != null) && (roles.size() > 0)) {
      List list = getSession().createQuery(
          "select roleFeature from RoleFeature roleFeature, Role role, Feature feature where roleFeature.role = role "
          + "and roleFeature.feature = feature and feature.featureName = :featureName and role.name in (:roleNames)")
        .setParameter("featureName", featureName).setParameterList("roleNames", roles).list();

      if ((list != null) && (list.size() > 0)) {
        return true;
      }
    }

    return false;
  }
}
