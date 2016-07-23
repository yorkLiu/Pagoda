package com.ly.dao.hibernate;

import com.ly.dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * Created by yongliu on 7/23/16.
 */
@Repository("userDao")
public class UserDaoHibernate extends BaseDao implements UserDao {
}
