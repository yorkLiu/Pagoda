package com.ly.service;

import java.util.List;

import com.ly.model.PreOrderInfo;


/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 15:01
 */
public interface PreOrderService extends BaseService {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * countPreOrder.
   *
   * @param   merchantId  Long
   * @param   queryText   String
   *
   * @return  int
   */
  int countPreOrder(Long merchantId, String queryText);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * readPreOrders.
   *
   * @param   merchantId  Long
   * @param   queryText   String
   * @param   start       Integer
   * @param   limit       Integer
   *
   * @return  List
   */
  List<PreOrderInfo> readPreOrders(Long merchantId, String queryText, Integer start, Integer limit);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * saveOrUpdatePreOrder.
   *
   * @param   preOrderInfo  PreOrderInfo
   *
   * @return  PreOrderInfo
   */
  PreOrderInfo saveOrUpdatePreOrder(PreOrderInfo preOrderInfo);
} // end interface PreOrderService
