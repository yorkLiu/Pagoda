package com.ly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.ly.dao.CommissionDao;

import com.ly.service.CommissionService;


/**
 * Created by yongliu on 8/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/22/2016 15:36
 */
@Service("commissionService")
public class CommissionServiceImpl extends BaseServiceImpl implements CommissionService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private CommissionDao commissionDao;
}
