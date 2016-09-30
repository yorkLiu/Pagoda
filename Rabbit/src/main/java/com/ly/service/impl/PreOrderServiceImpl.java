package com.ly.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import com.ly.dao.PreOrderDao;

import com.ly.model.Merchant;
import com.ly.model.PreOrderInfo;

import com.ly.service.PreOrderService;


/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 15:01
 */
@Service("preOrderService")
public class PreOrderServiceImpl extends BaseServiceImpl implements PreOrderService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private PreOrderDao preOrderDao;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.PreOrderService#countPreOrder(java.lang.Long, java.lang.String)
   */
  @Override public int countPreOrder(Long merchantId, String queryText) {
    return preOrderDao.countPreOrder(merchantId, queryText);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.PreOrderService#readPreOrders(java.lang.Long, java.lang.String, java.lang.Integer,
   *       java.lang.Integer)
   */
  @Override public List<PreOrderInfo> readPreOrders(Long merchantId, String queryText, Integer start, Integer limit) {
    return preOrderDao.readPreOrders(merchantId, queryText, start, limit);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.PreOrderService#saveOrUpdatePreOrder(com.ly.model.PreOrderInfo)
   */
  @Override public PreOrderInfo saveOrUpdatePreOrder(PreOrderInfo preOrderInfo) {
    if ((preOrderInfo.getPreOrderNo() == null) || StringUtils.isEmpty(preOrderInfo.getPreOrderNo())) {
      String preOrderNo = generatePreOrderNo();
      logger.info("Generate preOrderNo#" + preOrderNo + " for PreOrderInfo:" + preOrderInfo);
      preOrderInfo.setPreOrderNo(preOrderNo);
    }

    preOrderInfo = (PreOrderInfo) save(preOrderInfo);

    return preOrderInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String generatePreOrderNo() {
    String preOrderNo = getLast12UUID();
    Long   count      = preOrderDao.countPreOrderNo(preOrderNo);

    if ((count != null) && (count.intValue() > 0)) {
      // this caseId was exists, re-generate caseId
      return generatePreOrderNo();
    }

    return preOrderNo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getLast12UUID() {
    String   retUUID = null;
    String[] uuIds   = UUID.randomUUID().toString().split("-");
    retUUID = uuIds[4];

    return (retUUID != null) ? retUUID.toUpperCase() : retUUID;
  }

} // end class PreOrderServiceImpl
