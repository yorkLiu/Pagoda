package com.ly.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.ly.dao.CommissionDao;

import com.ly.model.Commission;
import com.ly.model.type.StatusType;

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

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * @see  com.ly.service.CommissionService#checkNameUnique(java.lang.String, java.lang.Long)
   */
  @Override public boolean checkNameUnique(String commissionName, Long commissionId) {
    return commissionDao.checkNameUnique(commissionName, commissionId);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.CommissionService#countCommission(java.lang.String)
   */
  @Override public int countCommission(String queryText) {
    return commissionDao.countCommission(queryText);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.CommissionService#findCommissions(java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  @Override public List<Commission> findCommissions(String queryText, Integer start, Integer limit) {
    return commissionDao.findCommissions(queryText, start, limit);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.service.CommissionService#updateStatus(java.lang.Long, com.ly.model.type.StatusType)
   */
  @Override public void updateStatus(Long commissionId, StatusType status) {
    Commission commission = (Commission) get(Commission.class, commissionId);

    if (logger.isDebugEnabled()) {
      logger.debug("Update Commission#" + commissionId + " status from '" + commission.getStatus() + "' to '" + status
        + "'");
    }

    // check this commission.appType is exists published
    // if exists then 'retired' others
    // then set the current record to 'published'
    if (StatusType.PUBLISHED.equals(status) && (commission.getAppType() != null)) {
      retireCommissionByAppType(commission.getAppType().getId());
    }

    commission.setStatus(status);

    commissionDao.saveOrUpdate(commission);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void retireCommissionByAppType(Long appTypeId) {
    List<Commission> commissions = commissionDao.findCommissionByAppType(appTypeId);

    if (logger.isDebugEnabled()) {
      logger.debug("Will retired " + ((commissions != null) ? commissions.size() : "0") + " Commission(s) by appType#"
        + appTypeId);
    }

    if ((commissions != null) && (commissions.size() > 0)) {
      for (Commission commission : commissions) {
        commission.setStatus(StatusType.RETIRED);
        commission.setLastUpdateDate(new Date());
        saveObject(commission);
      }
    }
  }

} // end class CommissionServiceImpl
