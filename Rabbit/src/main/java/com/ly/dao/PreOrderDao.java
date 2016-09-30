package com.ly.dao;

import java.util.List;

import com.ly.model.PreOrderInfo;


/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 15:02
 */
public interface PreOrderDao extends Dao {
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
   * countPreOrderNo.
   *
   * @param   preOrderNo  String
   *
   * @return  Long
   */
  Long countPreOrderNo(final String preOrderNo);

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
} // end interface PreOrderDao
