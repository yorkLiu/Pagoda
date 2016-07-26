package com.ly.service.impl;

import com.ly.dao.UserDao;
import com.ly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yongliu on 7/23/16.
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {
  
  @Autowired
  private UserDao userDao;
  
}
