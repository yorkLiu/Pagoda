package com.ly.web.security;

import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import org.springframework.util.StringUtils;

import com.ly.model.User;

import com.ly.service.UserService;


/**
 * Created by yongliu on 9/12/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 14:25
 */
public class WebAuthenticationFailureLoggerHandler extends SimpleUrlAuthenticationFailureHandler
  implements AuthenticationFailureHandler {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log log = LogFactory.getLog(WebAuthenticationFailureLoggerHandler.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Map<String, String> failureLoginUrl          = new HashMap<String, String>();
  private Long                maxAccountLockTime       = 60L;
  private Integer             maxLoginFailures         = 5;
  private Integer             passwordFailureLockCount;

  private Integer retryCount;

  @Autowired private UserService userService;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for failure login url.
   *
   * @return  Map
   */
  public Map<String, String> getFailureLoginUrl() {
    return failureLoginUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for max account lock time.
   *
   * @return  Long
   */
  public Long getMaxAccountLockTime() {
    return maxAccountLockTime;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for max login failures.
   *
   * @return  Integer
   */
  public Integer getMaxLoginFailures() {
    return maxLoginFailures;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for password failure lock count.
   *
   * @return  Integer
   */
  public Integer getPasswordFailureLockCount() {
    return passwordFailureLockCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for retry count.
   *
   * @return  Integer
   */
  public Integer getRetryCount() {
    return retryCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest,
   *       javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
   */
  @Override public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException exception) throws IOException, ServletException {
    // Override login failure redirect page

    String url = request.getRequestURL().toString();

    for (Map.Entry<String, String> e : failureLoginUrl.entrySet()) {
      String key = e.getKey();

      if (url.contains(key)) {
        String redirect = e.getValue() + "?error=true";
        setDefaultFailureUrl(redirect);

        break;
      }
    }

// Authentication auth = null;
//
// if (exception instanceof UsernameNotFoundException) {
// auth = ((UsernameNotFoundException) exception).getAuthentication();
// }

    String principal = "";

// if (auth != null) {
// principal = (String) auth.getPrincipal();
// }
//
    String credentials = "";
//
// if ((auth != null) && (auth.getCredentials() != null)) {
// credentials = auth.getCredentials().toString();
// }

    if (passwordFailureLockCount == null) {
      passwordFailureLockCount = userService.getPasswordFailureLockCount();

      if ((passwordFailureLockCount != null) && (passwordFailureLockCount.intValue() != 0)) {
        retryCount = passwordFailureLockCount;
      } else {
        passwordFailureLockCount = 0;
      }
    }

    logUserLoginFailure(principal, credentials, exception);

    super.onAuthenticationFailure(request, response, exception);
  } // end method onAuthenticationFailure

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for failure login url.
   *
   * @param  failureLoginUrl  Map
   */
  public void setFailureLoginUrl(Map<String, String> failureLoginUrl) {
    this.failureLoginUrl = failureLoginUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for max account lock time.
   *
   * @param  maxAccountLockTime  Long
   */
  public void setMaxAccountLockTime(Long maxAccountLockTime) {
    this.maxAccountLockTime = maxAccountLockTime;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for max login failures.
   *
   * @param  maxLoginFailures  Integer
   */
  public void setMaxLoginFailures(Integer maxLoginFailures) {
    this.maxLoginFailures = maxLoginFailures;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for password failure lock count.
   *
   * @param  passwordFailureLockCount  Integer
   */
  public void setPasswordFailureLockCount(Integer passwordFailureLockCount) {
    this.passwordFailureLockCount = passwordFailureLockCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for retry count.
   *
   * @param  retryCount  Integer
   */
  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void logUserLoginFailure(String principal, String credentials, AuthenticationException exception) {
    // Trigger logging as long as there is principal
    if (StringUtils.hasText(principal)) {
      logger.info("User '" + principal + "' login fail.");

      // TODO save failure infomation to webactive table
// WebActivity webActivity = webLoggingService.createAgentWebActivity(
// principal, WebActivityUtil.ACTION_LOG_PREFIX
// + WebActivityUtil.LOGIN_FAILURE);
// webActivity.setData1(principal);
// webActivity.setData2(credentials);
//
// String message = exception.getMessage();
//
// if ((exception != null) && StringUtils.hasText(message)) {
// int len = (message.length() > 149) ? 149 : message.length();
// webActivity.setData5(message.substring(0, len));
// }
//
// webActivityManager.saveWebActivity(webActivity);

      try {
        User user = userService.getUserByUsername(principal);

        if (user != null) {
          Date now = new Date();
          user.setRetryCount(retryCount);
          user.addLoginFailureCount();
          user.setSecondToLastLoginFailureDate(user.getLastLoginFailureDate());
          user.setLastLoginFailureDate(now);
          userService.saveObject(user);
        }
      } catch (Exception e) {
        log.error(e);
      }
    } // end if
  }   // end method logUserLoginFailure
} // end class WebAuthenticationFailureLoggerHandler
