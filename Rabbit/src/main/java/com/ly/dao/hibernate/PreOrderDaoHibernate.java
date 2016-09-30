package com.ly.dao.hibernate;

import java.util.List;

import org.hibernate.query.Query;

import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;

import com.ly.dao.PreOrderDao;

import com.ly.model.PreOrderInfo;


/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 15:03
 */
@Repository("preOrderDao")
@Transactional public class PreOrderDaoHibernate extends BaseDaoHibernate implements PreOrderDao {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.PreOrderDao#countPreOrder(java.lang.Long, java.lang.String)
   */
  @Override public int countPreOrder(Long merchantId, String queryText) {
    StringBuilder hql = new StringBuilder(
        "SELECT COUNT(id) from PreOrderInfo p WHERE p.merchant.id=:merchantId AND p.status != 'deleted' ");

    if ((queryText != null) && StringUtils.hasText(queryText)) {
      hql.append("AND ").append(queryText);
    }

    Long count = (Long) getSession().createQuery(hql.toString()).setParameter("merchantId", merchantId).uniqueResult();

    return (count != null) ? count.intValue() : 0;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.PreOrderDao#countPreOrderNo(java.lang.String)
   */
  @Override public Long countPreOrderNo(String preOrderNo) {
    return (Long) getSession().createQuery("SELECT COUNT(p.id) FROM PreOrderInfo p WHERE p.preOrderNo=:preOrderNo ").setParameter(
        "preOrderNo", preOrderNo).uniqueResult();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.dao.PreOrderDao#readPreOrders(java.lang.Long, java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @Override public List<PreOrderInfo> readPreOrders(Long merchantId, String queryText, Integer start, Integer limit) {
    StringBuilder hql = new StringBuilder(
        "FROM PreOrderInfo p WHERE p.merchant.id=:merchantId AND p.status != 'deleted' ");

    if ((queryText != null) && StringUtils.hasText(queryText)) {
      hql.append("AND ").append(queryText);
    }

    hql.append(" ORDER BY p.priority ASC ");
//    hql.append(" ORDER BY p.lastUpdateDate DESC ");

    Query query = getSession().createQuery(hql.toString()).setParameter("merchantId", merchantId);

    if ((limit != null) && (limit.intValue() > 1)) {
      query.setFirstResult(start).setMaxResults(limit);
    }

    return query.list();
  }
} // end class PreOrderDaoHibernate
