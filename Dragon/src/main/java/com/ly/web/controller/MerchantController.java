package com.ly.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import com.ly.model.Merchant;
import com.ly.model.User;

import com.ly.service.MerchantService;
import com.ly.service.UserService;

import com.ly.web.command.DWRBaseResponseCommand;
import com.ly.web.command.DWRJsonListResponseCommand;
import com.ly.web.command.ParameterCommand;
import com.ly.web.command.merchant.MerchantCommand;


/**
 * Created by yongliu on 9/18/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/18/2016 14:13
 */

@Controller
@RemoteProxy(name = "merchantController")
public class MerchantController extends BaseController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private MerchantService merchantService;

  @Autowired private UserService userService;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * checkUniqueName.
   *
   * @param   command  MerchantCommand
   * @param   request  HttpServletRequest
   *
   * @return  DWRBaseResponseCommand
   */
  @RemoteMethod public DWRBaseResponseCommand<MerchantCommand> checkUniqueName(MerchantCommand command,
    HttpServletRequest request) {
    boolean success = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug("Ready checking merchant name....");
    }

    try {
      success = merchantService.checkNameUnique(command.getName(), command.getId());

      if (logger.isDebugEnabled()) {
        logger.debug("Found merchant by {name:" + command.getName() + ", id:" + command.getId()
          + "}, result is:" + success);
      }

    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRBaseResponseCommand<MerchantCommand>(null, success);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * readMerchant.
   *
   * @param   params   ParameterCommand
   * @param   request  HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<MerchantCommand> readMerchant(ParameterCommand params,
    HttpServletRequest request) {
    List<MerchantCommand> results = new ArrayList<>();
    boolean               success = Boolean.TRUE;
    int                   total   = 0;

    try {
      String queryText = params.getQuery();

      total = merchantService.countMerchant(queryText);

      if (logger.isDebugEnabled()) {
        logger.debug("Found " + total + " Merchant(s)");
      }

      if (total > 0) {
        List<Merchant> merchants = merchantService.readMerchants(queryText, params.getStart(), params.getLimit());

        if (logger.isDebugEnabled()) {
          logger.debug("Showing [" + params.getStart() + ", " + params.getLimit() + "] and found " + merchants.size()
            + " merchant(s).");
        }

        merchants.stream().forEach(merchant -> { results.add(new MerchantCommand(merchant)); });
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }


    return new DWRJsonListResponseCommand<MerchantCommand>(results, success, null, total);
  } // end method readMerchant

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * updateMerchant.
   *
   * @param   commands  List
   * @param   request   HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<MerchantCommand> updateMerchant(List<MerchantCommand> commands,
    HttpServletRequest request) {
    boolean success  = Boolean.TRUE;
    String  username = request.getRemoteUser();

    try {
      User user = userService.getUserByUsername(username);

      commands.stream().forEach(command -> {
        Merchant merchant = command.populate(user);

        if (command.getFromCopy()) {
          if (logger.isDebugEnabled()) {
            logger.debug("Copy Merchant info from merchantId#" + command.getCopyFromId());
          }

          merchant.setId(null);
          merchant.setCopyFromId(command.getCopyFromId());
        } else {
          if (command.getId() != null) {
            if (logger.isDebugEnabled()) {
              logger.debug("Update Merchant " + command + " with user#" + username);
            }
          } else {
            if (logger.isDebugEnabled()) {
              logger.debug("Create Merchant " + command + " with user#" + username);
            }
          }
        }

        merchantService.save(merchant);

      });

    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch

    return new DWRJsonListResponseCommand<MerchantCommand>(commands, success, null);

  } // end method updateMerchant


} // end class MerchantController
