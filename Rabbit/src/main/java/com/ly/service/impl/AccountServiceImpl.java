package com.ly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.ly.dao.AccountDao;

import com.ly.service.AccountService;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/21/2016 15:10
 */
@Service("accountService")
public class AccountServiceImpl extends BaseServiceImpl implements AccountService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private AccountDao accountDao;

}
