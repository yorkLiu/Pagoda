package com.ly.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.util.StringUtils;

import com.ly.model.AppType;
import com.ly.model.Commission;
import com.ly.model.type.StatusType;

import com.ly.service.CommissionService;

import com.ly.web.command.BaseCommand;
import com.ly.web.command.DWRBaseResponseCommand;
import com.ly.web.command.DWRJsonListResponseCommand;
import com.ly.web.command.ParameterCommand;
import com.ly.web.command.configuration.AppTypeCommand;
import com.ly.web.command.configuration.CommissionCommand;


/**
 * Created by yongliu on 8/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/22/2016 15:43
 */

@Controller
@RemoteProxy(name = "commissionController")
public class CommissionController extends BaseController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private CommissionService commissionService;

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * checkUniqueName.
   *
   * @param   command  CommissionCommand
   * @param   request  HttpServletRequest
   *
   * @return  DWRBaseResponseCommand
   */
  @RemoteMethod public DWRBaseResponseCommand<CommissionCommand> checkUniqueName(CommissionCommand command,
    HttpServletRequest request) {
    boolean success = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug("Ready checking commission name....");
    }

    try {
      success = commissionService.checkNameUnique(command.getDescription(), command.getId());

      if (logger.isDebugEnabled()) {
        logger.debug("Found commission by {name:" + command.getDescription() + ", id:" + command.getId()
          + "}, result is:" + success);
      }

    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRBaseResponseCommand<CommissionCommand>(null, success);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * readAppTypes.
   *
   * @param   params   Map
   * @param   request  HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<AppTypeCommand> readAppTypes(Map<String, String> params,
    HttpServletRequest request) {
    boolean              success = Boolean.TRUE;
    String               message = null;
    int                  total   = 0;
    List<AppTypeCommand> results = new ArrayList<>();

    try {
      List<AppType> appTypes = commissionService.getAll(AppType.class);
      total = appTypes.size();

      if (logger.isDebugEnabled()) {
        logger.debug("Found " + total + " AppType to list.");
      }

//      for (AppType appType : appTypes) {
//        results.add(new AppTypeCommand(appType));
//      }
      
      appTypes.forEach((AppType appType) -> results.add(new AppTypeCommand(appType)));

    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRJsonListResponseCommand<AppTypeCommand>(results, success, message, total);
  } // end method readAppTypes

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * readCommissions.
   *
   * @param   params   Map
   * @param   request  HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<CommissionCommand> readCommissions(ParameterCommand params,
    HttpServletRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug("Load commissions....");
    }

    boolean                 success = Boolean.TRUE;
    String                  message = null;
    int                     total   = 0;
    List<CommissionCommand> results = new ArrayList<>();

    try {
      List<Commission> commissionList = commissionService.findCommissions(params.getQuery(), params.getStart(),
          params.getLimit());
      total = commissionService.countCommission(params.getQuery());

      if (logger.isDebugEnabled()) {
        logger.debug("Found " + total + " Commissions to list.");
      }

//      for (Commission commission : commissionList) {
//        results.add(new CommissionCommand(commission));
//      }
      
      commissionList.forEach((Commission commission) -> results.add(new CommissionCommand(commission)));
      

    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRJsonListResponseCommand<CommissionCommand>(results, success, message, total);

  } // end method readCommissions

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * updateAccount.
   *
   * @param   commands  List
   * @param   request   HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<CommissionCommand> updateAccount(List<CommissionCommand> commands,
    HttpServletRequest request) {
    boolean success = Boolean.TRUE;
    String  message = null;

    try {
      for (CommissionCommand command : commands) {
        AppType appType = null;

        if ((command.getAppTypeId() != null) && !command.getAppTypeId().equals(0)) {
          appType = (AppType) commissionService.get(AppType.class, command.getAppTypeId());
        }

        Commission commission = command.populate();
        commission.setAppType(appType);

        commissionService.save(commission);
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRJsonListResponseCommand<CommissionCommand>(commands, success, message);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * updateStatus.
   *
   * @param   command  CommissionCommand
   * @param   status   String
   * @param   request  HttpServletRequest
   *
   * @return  DWRBaseResponseCommand
   */
  @RemoteMethod public DWRBaseResponseCommand<CommissionCommand> updateStatus(CommissionCommand command, String status,
    HttpServletRequest request) {
    boolean success = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug("Ready for update commission status....");
    }

    try {
      if ((status != null) && !StringUtils.isEmpty(status)) {
        if (logger.isDebugEnabled()) {
          logger.debug("Update commission#" + command.getId() + " status to '" + status + "'");
        }

        commissionService.updateStatus(command.getId(), StatusType.convert(status.trim()));
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Update commission status successfully.");
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRBaseResponseCommand<CommissionCommand>(command, success);
  } // end method updateStatus


} // end class CommissionController
