package com.ly.dao;

import java.util.List;

import com.ly.model.Commission;


/**
 * Created by yongliu on 8/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/31/2016 14:34
 */
public interface CommissionDao extends Dao {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * checkNameUnique.
   *
   * @param   commissionName  String
   * @param   commissionId    Long
   *
   * @return  boolean
   */
  boolean checkNameUnique(final String commissionName, final Long commissionId);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * countCommission.
   *
   * @param   queryText  String
   *
   * @return  Integer
   */
  int countCommission(String queryText);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * findCommissionByAppType.
   *
   * @param   appTypeId  Long
   *
   * @return  List
   */
  List<Commission> findCommissionByAppType(final Long appTypeId);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * findCommissions.
   *
   * @param   queryText  String
   * @param   start      Integer
   * @param   limit      Integer
   *
   * @return  List
   */
  List<Commission> findCommissions(String queryText, Integer start, Integer limit);
} // end interface CommissionDao
