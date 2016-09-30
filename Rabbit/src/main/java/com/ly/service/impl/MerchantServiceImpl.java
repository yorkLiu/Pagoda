package com.ly.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.ly.dao.MerchantDao;

import com.ly.model.Merchant;
import com.ly.model.User;
import com.ly.model.type.OrderStatusType;

import com.ly.service.MerchantService;


/**
 * Created by yongliu on 9/18/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 11:11
 */
@Service("merchantService")
public class MerchantServiceImpl extends BaseServiceImpl implements MerchantService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private MerchantDao merchantDao;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.MerchantService#checkNameUnique(java.lang.String, java.lang.Long)
   */
  @Override public boolean checkNameUnique(String name, Long merchantId) {
    return merchantDao.checkNameUnique(name, merchantId);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.MerchantService#countMerchant(java.lang.String)
   */
  @Override public Integer countMerchant(String queryText) {
    return merchantDao.countMerchant(queryText);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.MerchantService#readMerchants(java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @Override public List<Merchant> readMerchants(String queryText, Integer start, Integer limit) {
    return merchantDao.readMerchants(queryText, start, limit);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.MerchantService#removeMerchant(java.lang.Long, com.ly.model.User)
   */
  @Override public void removeMerchant(Long merchantId, User user) {
    if (merchantId != null) {
      Merchant merchant = (Merchant) get(Merchant.class, merchantId);

      if (merchant != null) {
        merchant.setStatus(OrderStatusType.DELETED);
        merchant.setLastUpdater(user);
        merchant.setLastUpdateDate(new Date());

        // ToDO set the orders to 'deleted' status

        saveObject(merchant);
      }
    }
  }
} // end class MerchantServiceImpl
