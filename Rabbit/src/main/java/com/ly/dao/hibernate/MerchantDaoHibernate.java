package com.ly.dao.hibernate;

import java.util.List;

import org.hibernate.query.Query;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;

import com.ly.dao.Dao;
import com.ly.dao.MerchantDao;

import com.ly.model.Merchant;


/**
 * Created by yongliu on 9/18/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/18/2016 14:12
 */
@Service("merchantDao")
@Transactional public class MerchantDaoHibernate extends BaseDaoHibernate implements MerchantDao {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.MerchantDao#checkNameUnique(java.lang.String, java.lang.Long)
   */
  @Override public boolean checkNameUnique(String name, Long merchantId) {
    StringBuilder hql = new StringBuilder(
        "SELECT COUNT(m.id) FROM Merchant m WHERE (m.status != 'DELETED' AND m.status !='CANCELLED') AND m.name=:name ");

    if (merchantId != null) {
      hql.append("AND m.id !=:merchantId ");
    }

    Query query = getSession().createQuery(hql.toString()).setParameter("name", name);

    if (merchantId != null) {
      query.setParameter("merchantId", merchantId);
    }

    Long count = (Long) query.uniqueResult();

    return !((count != null) && (count.longValue() > 0L));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.MerchantDao#countMerchant(java.lang.String)
   */
  @Override public Integer countMerchant(String queryText) {
    StringBuilder hql = new StringBuilder("SELECT COUNT(id) from Merchant WHERE status != 'deleted' ");

    if ((queryText != null) && StringUtils.hasText(queryText)) {
      hql.append("AND ").append(queryText);
    }

    Long count = (Long) getSession().createQuery(hql.toString()).uniqueResult();

    return (count != null) ? count.intValue() : 0;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.MerchantDao#readMerchants(java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @Override public List<Merchant> readMerchants(String queryText, Integer start, Integer limit) {
    StringBuilder hql = new StringBuilder("FROM Merchant m WHERE m.status != 'deleted' ");

    if ((queryText != null) && StringUtils.hasText(queryText)) {
      hql.append("AND ").append(queryText);
    }

    hql.append(" ORDER BY m.lastUpdateDate DESC ");

    Query query = getSession().createQuery(hql.toString());

    if ((limit != null) && (limit.intValue() > 1)) {
      query.setFirstResult(start).setMaxResults(limit);
    }

    return query.list();
  }
} // end class MerchantDaoHibernate
