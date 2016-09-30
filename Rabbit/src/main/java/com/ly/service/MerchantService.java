package com.ly.service;

import java.util.List;

import com.ly.model.Merchant;
import com.ly.model.User;


/**
 * Created by yongliu on 9/18/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/18/2016 14:15
 */
public interface MerchantService extends BaseService {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * checkNameUnique.
   *
   * @param   name        String
   * @param   merchantId  Long
   *
   * @return  boolean
   */
  boolean checkNameUnique(String name, Long merchantId);

  //~ ------------------------------------------------------------------------------------------------------------------

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

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * removeMerchant.
   *
   * @param  merchantId  Long
   * @param  user        User
   */
  void removeMerchant(Long merchantId, User user);
} // end interface MerchantService
