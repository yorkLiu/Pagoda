package com.ly.service;

import java.util.List;

import com.ly.model.Commission;
import com.ly.model.type.StatusType;


/**
 * Created by yongliu on 8/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/31/2016 11:21
 */
public interface CommissionService extends BaseService {
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
   * @return  int
   */
  int countCommission(String queryText);

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

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * updateStatus.
   *
   * @param  commissionId  Long
   * @param  status        StatusType
   */
  void updateStatus(Long commissionId, final StatusType status);
} // end interface CommissionService
