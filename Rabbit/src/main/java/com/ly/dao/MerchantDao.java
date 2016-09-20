package com.ly.dao;

import java.util.List;

import com.ly.model.Merchant;


/**
 * Created by yongliu on 9/18/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/18/2016 14:16
 */
public interface MerchantDao extends Dao {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * countMerchant.
   *
   * @param   queryText  String
   *
   * @return  Integer
   */
  Integer countMerchant(String queryText);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * readMerchants.
   *
   * @param   queryText  String
   * @param   start      Integer
   * @param   limit      Integer
   *
   * @return  List
   */
  List<Merchant> readMerchants(String queryText, Integer start, Integer limit);

  boolean checkNameUnique(String name, Long merchantId);
} // end interface MerchantDao
