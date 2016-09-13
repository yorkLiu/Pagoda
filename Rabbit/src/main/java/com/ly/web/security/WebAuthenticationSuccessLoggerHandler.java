package com.ly.web.security;

import java.io.IOException;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.ly.model.User;

import com.ly.utils.Constants;


/**
 * Created by yongliu on 9/12/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 14:23
 */
public class WebAuthenticationSuccessLoggerHandler extends SavedRequestAwareAuthenticationSuccessHandler {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  private static final Log log = LogFactory.getLog(WebAuthenticationSuccessLoggerHandler.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String defaultTargetUrlTpl;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for default target url tpl.
   *
   * @return  String
   */
  public String getDefaultTargetUrlTpl() {
    return defaultTargetUrlTpl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest,
   *       javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
   */
  @Override public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) throws ServletException, IOException {
    super.onAuthenticationSuccess(request, response, authentication);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for default target url tpl.
   *
   * @param  defaultTargetUrlTpl  String
   */
  public void setDefaultTargetUrlTpl(String defaultTargetUrlTpl) {
    this.defaultTargetUrlTpl = defaultTargetUrlTpl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler#handle(javax.servlet.http.HttpServletRequest,
   *       javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
   */
  @Override protected void handle(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) throws IOException, ServletException {
    User user = (User) authentication.getPrincipal();
    logUserLoginSuccess(user);

    HttpSession session = request.getSession();
    session.setAttribute(Constants.USER_SESSION_IDENTIFY, session.getId());


    super.handle(request, response, authentication);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void logUserLoginSuccess(User user) {
    try {
      if (user != null) {
        String agentName = user.getUsername();
        String userId    = user.getId().toString();

        logger.info("User '" + agentName + "' login success.");

        // TODO save the web activity

// WebActivity webActivity = webLoggingService.createAgentWebActivity(
// agentName, WebActivityUtil.ACTION_LOG_PREFIX
// + WebActivityUtil.LOGIN_SUCCESS);
// webActivity.setData1(agentName);
// webActivity.setData5(userId);
// webActivityManager.saveWebActivity(webActivity);
// user.resetLoginFailureCount();
//
// Date now = new Date();
// user.setSecondToLastLoginDate(user.getLastLoginDate());
// user.setLastLoginDate(now);
// userManager.saveUser(user);
      }
    } catch (Exception e) {
      log.error(e);
    } // end try-catch
  }   // end method logUserLoginSuccess
} // end class WebAuthenticationSuccessLoggerHandler
