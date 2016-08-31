package com.ly.dao.hibernate;

import java.util.List;

import org.hibernate.query.Query;

import org.springframework.stereotype.Repository;

import com.ly.dao.CommissionDao;

import com.ly.model.Commission;


/**
 * Created by yongliu on 8/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/31/2016 14:34
 */
@Repository("commissionDao")
public class CommissionDaoHibernate extends BaseDaoHibernate implements CommissionDao {
  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * @see  com.ly.dao.CommissionDao#checkNameUnique(java.lang.String, java.lang.Long)
   */
  @Override public boolean checkNameUnique(String commissionName, Long commissionId) {
    StringBuilder hql = new StringBuilder(
        "SELECT COUNT(c.id) FROM Commission c WHERE c.status != 'DELETED' and c.description=:commissionName ");

    if (commissionId != null) {
      hql.append("and c.id !=:commissionId ");
    }

    Query query = getSession().createQuery(hql.toString()).setParameter("commissionName", commissionName);

    if (commissionId != null) {
      query.setParameter("commissionId", commissionId);
    }

    Long count = (Long) query.uniqueResult();

    return !((count != null) && (count.longValue() > 0L));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.CommissionDao#countCommission(java.lang.String)
   */
  @Override public int countCommission(String queryText) {
    String hql = "SELECT COUNT(c.id) FROM Commission c WHERE c.status <> 'DELETED' ";

    Long count = (Long) getSession().createQuery(hql).uniqueResult();

    return (count != null) ? count.intValue() : 0;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.CommissionDao#findCommissionByAppType(java.lang.Long)
   */
  @Override public List<Commission> findCommissionByAppType(Long appTypeId) {
    Query query = getSession().createQuery(
        "from Commission c where c.appType.id=:appTypeId and c.status = 'PUBLISHED' ");
    query.setParameter("appTypeId", appTypeId);

    return query.list();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.CommissionDao#findCommissions(java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @Override public List<Commission> findCommissions(String queryText, Integer start, Integer limit) {
    StringBuilder sb = new StringBuilder();

    sb.append("FROM Commission WHERE status !='DELETED' ");
    sb.append("ORDER BY CASE status WHEN 'PUBLISHED' THEN 1 WHEN 'ENABLE' THEN 2 WHEN 'DISABLED' THEN 3 WHEN 'RETIRED' THEN 4 END ");

    Query query = getSession().createQuery(sb.toString());

    if ((start != null) && (start.intValue() > 0)) {
      query.setFirstResult(start);
      query.setMaxResults(limit);
    }

    return query.list();
  }
} // end class CommissionDaoHibernate
