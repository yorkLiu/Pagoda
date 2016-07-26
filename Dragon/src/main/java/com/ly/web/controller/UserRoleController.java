package com.ly.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Controller;

import com.ly.model.User;

import com.ly.service.UserService;

import com.ly.web.command.DWRBaseResponseCommand;
import com.ly.web.command.DWRJsonListResponseCommand;
import com.ly.web.command.userrole.UserCommand;


/**
 * Created by yongliu on 7/26/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/26/2016 11:52
 */
@Controller
@RemoteProxy(name = "userRoleController")
public class UserRoleController extends BaseController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private UserService userService;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * createUser.
   *
   * @param   commands  userCommand UserCommand
   * @param   request   HttpServletRequest
   *
   * @return  createUser.
   */
  @RemoteMethod public DWRJsonListResponseCommand<UserCommand> createUser(List<UserCommand> commands,
    HttpServletRequest request) {
    boolean success = Boolean.TRUE;

    try {
      for (UserCommand command : commands) {
        User user = new User();
        command.populate(user);

        userService.save(user);
      }


    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRJsonListResponseCommand<UserCommand>(commands, success, null);

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * listUsers.
   *
   * @param   params   Map
   * @param   request  HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<UserCommand> listUsers(Map<String, String> params,
    HttpServletRequest request) {
    List<UserCommand> results = new ArrayList<>();
    boolean           success = Boolean.TRUE;
    int               total   = 0;

    try {
      // todo get total
      // .....

      List<User> userList = userService.getAll(User.class);
      total = userList.size();

      for (User user : userList) {
        results.add(new UserCommand(user));
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }


    return new DWRJsonListResponseCommand<UserCommand>(results, success, null, total);
  }


} // end class UserRoleController
