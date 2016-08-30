package com.ly.dao.hibernate;

import com.ly.dao.CommissionDao;
import org.springframework.stereotype.Repository;

/**
 * Created by yongliu on 8/22/16.
 */
@Repository("commissionDao")
public class CommissionDaoHibernate extends BaseDaoHibernate implements CommissionDao {
}
