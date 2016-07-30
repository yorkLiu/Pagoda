package com.ly.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import com.ly.model.Account;

import com.ly.service.AccountService;

import com.ly.web.command.DWRJsonListResponseCommand;
import com.ly.web.command.configuration.AccountCommand;


/**
 * Created by yongliu on 7/27/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/27/2016 16:30
 */
@Controller
@RemoteProxy(name = "accountController")
public class AccountController extends BaseController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private AccountService accountService;

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * readAddress.
   *
   * @param   params   Map
   * @param   request  HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<AccountCommand> readAccounts(Map<String, String> params,
    HttpServletRequest request) {
    boolean              success = Boolean.TRUE;
    String               message = null;
    int                  total   = 0;
    List<AccountCommand> results = new ArrayList<>();

    try {
      List<Account> accounts = accountService.getAll(Account.class);
      total = accounts.size();

      if (logger.isDebugEnabled()) {
        logger.debug("Found " + total + " Accounts to list.");
      }

      for (Account account : accounts) {
        results.add(new AccountCommand(account));
      }


    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRJsonListResponseCommand<AccountCommand>(results, success, message, total);
  } // end method readAddress

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * updateAddress.
   *
   * @param   commands  List
   * @param   request   HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<AccountCommand> updateAccount(List<AccountCommand> commands,
    HttpServletRequest request) {
    boolean success = Boolean.TRUE;
    String  message = null;

    try {
      for (AccountCommand command : commands) {
        Account account = command.populate();

        accountService.save(account);
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRJsonListResponseCommand<AccountCommand>(commands, success, message);
  }

} // end class AccountController
