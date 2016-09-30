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

import com.ly.exceptions.UnLoginException;

import com.ly.model.AppType;
import com.ly.model.Merchant;
import com.ly.model.PreOrderInfo;
import com.ly.model.User;
import com.ly.model.type.OrderStatusType;

import com.ly.service.MerchantService;
import com.ly.service.PreOrderService;
import com.ly.service.UserService;

import com.ly.web.command.DWRBaseResponseCommand;
import com.ly.web.command.DWRJsonListResponseCommand;
import com.ly.web.command.ParameterCommand;
import com.ly.web.command.merchant.MerchantCommand;
import com.ly.web.command.merchant.PreOrderCommand;
import com.ly.web.command.merchant.PreOrderParameterCommand;


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

  @Autowired private PreOrderService preOrderService;

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
   * readPreOrder.
   *
   * @param   params   PreOrderParameterCommand
   * @param   request  HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<PreOrderCommand> readPreOrder(PreOrderParameterCommand params,
    HttpServletRequest request) {
    List<PreOrderCommand> results = new ArrayList<>();
    boolean               success = Boolean.TRUE;
    int                   total   = 0;

    logger.info("Request readPreOrder......");

    try {
      String queryText = params.getQuery();

      total = preOrderService.countPreOrder(params.getMerchantId(), queryText);

      if (logger.isDebugEnabled()) {
        logger.debug("Found " + total + " PreOrder(s) with merchantId#" + params.getMerchantId());
      }

      if (total > 0) {
        List<PreOrderInfo> preOrderInfoList = preOrderService.readPreOrders(params.getMerchantId(), queryText,
            params.getStart(), params.getLimit());

        if (logger.isDebugEnabled()) {
          logger.debug("Showing [" + params.getStart() + ", " + params.getLimit() + "] and found "
            + preOrderInfoList.size()
            + " preOrder(s).");
        }

        preOrderInfoList.stream().forEach(preOrder -> { results.add(new PreOrderCommand(preOrder)); });
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch


    return new DWRJsonListResponseCommand<PreOrderCommand>(results, success, null, total);
  } // end method readPreOrder

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * checkUniqueName.
   *
   * @param   commands  MerchantCommand
   * @param   request   HttpServletRequest
   *
   * @return  DWRBaseResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<MerchantCommand> removeMerchant(List<MerchantCommand> commands,
    HttpServletRequest request) {
    Boolean success = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug("Start remove Merchant....");
    }

    try {
      String username = request.getRemoteUser();

      commands.forEach(command -> {
        if ((username == null) || !StringUtils.hasText(username)) {
          throw new UnLoginException(
            request.getRemoteAddr() + " not login to remove merchant: " + command
            + ", this was illegal. Undo remove merchant");
        }

        if (logger.isDebugEnabled()) {
          logger.debug("Remove Merchant: " + command + " by user: " + username);
        }

        User user = userService.getUserByUsername(username);

        if ((command != null) && (command.getId() != null) && (user != null)) {
          merchantService.removeMerchant(command.getId(), user);

          if (logger.isDebugEnabled()) {
            logger.debug("Remove Merchant successfully.");
          }
        }
      });

    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch

    return new DWRJsonListResponseCommand<MerchantCommand>(null, success, "");
  } // end method removeMerchant

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

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * updatePreOrder.
   *
   * @param   commands  List
   * @param   request   HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<PreOrderCommand> updatePreOrder(List<PreOrderCommand> commands,
    HttpServletRequest request) {
    boolean success  = Boolean.TRUE;
    String  username = request.getRemoteUser();

    try {
      User user = userService.getUserByUsername(username);

      commands.stream().forEach(command -> {
        PreOrderInfo preOrderInfo = command.populate(user);

        if (logger.isDebugEnabled()) {
          logger.debug("Found AppType by appTypeId#" + command.getAppTypeId());
        }

        if (command.getAppTypeId() != null) {
          AppType appType = (AppType) preOrderService.get(AppType.class, command.getAppTypeId());
          preOrderInfo.setAppType(appType);
        }

        if (logger.isDebugEnabled()) {
          logger.debug("Found Merchant by merchantId#" + command.getMerchantId());
        }

        if (command.getMerchantId() != null) {
          Merchant merchant = (Merchant) merchantService.get(Merchant.class, command.getMerchantId());

          if (merchant != null) {
            preOrderInfo.setMerchant(merchant);
          }
        }

        if (command.getId() != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Update PreOrder " + command + " with user#" + username);
          }
        } else {
          if (logger.isDebugEnabled()) {
            logger.debug("Create PreOrder " + command + " with user#" + username);
          }
        }

        preOrderService.saveOrUpdatePreOrder(preOrderInfo);

      });

    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch

    return new DWRJsonListResponseCommand<PreOrderCommand>(commands, success, null);

  } // end method updatePreOrder


} // end class MerchantController
