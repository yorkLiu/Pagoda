package com.ly.dao.hibernate;

import com.ly.dao.UserDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yongliu on 7/23/16.
 */
@Repository("userDao")
public class UserDaoHibernate extends BaseDaoHibernate implements UserDao {
}
