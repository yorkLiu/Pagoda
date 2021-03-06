package com.ly.dao.hibernate;

import com.ly.dao.AccountDao;
import org.springframework.stereotype.Repository;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/21/2016 15:07
 */
@Repository("accountDao")
public class AccountDaoHibernate extends BaseDaoHibernate implements AccountDao { }
