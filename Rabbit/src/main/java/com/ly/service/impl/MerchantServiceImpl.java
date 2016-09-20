package com.ly.service.impl;

import com.ly.dao.MerchantDao;
import com.ly.model.Merchant;
import com.ly.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yongliu on 9/18/16.
 */
@Service("merchantService")
public class MerchantServiceImpl extends BaseServiceImpl implements MerchantService {
  
  @Autowired private MerchantDao merchantDao;
  
  @Override
  public Integer countMerchant(String queryText) {
    return merchantDao.countMerchant(queryText);
  }

  @Override
  public List<Merchant> readMerchants(String queryText, Integer start, Integer limit) {
    return merchantDao.readMerchants(queryText, start, limit);
  }

  @Override
  public boolean checkNameUnique(String name, Long merchantId) {
    return merchantDao.checkNameUnique(name, merchantId);
  }
}
