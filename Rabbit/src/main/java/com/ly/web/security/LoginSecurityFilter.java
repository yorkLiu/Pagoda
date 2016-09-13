package com.ly.web.security;

import java.io.IOException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import org.springframework.util.StringUtils;

import com.ly.web.security.util.LoginAuthenticationTokens;


/**
 * TODO: DOCUMENT ME!
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/13/2016 10:38
 */
public class LoginSecurityFilter extends AbstractAuthenticationProcessingFilter {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log log = LogFactory.getLog(LoginSecurityFilter.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Boolean enableLoginTokensValidation = Boolean.TRUE;

  private SimpleUrlAuthenticationFailureHandler failureHandler        = new SimpleUrlAuthenticationFailureHandler();
  private Map<String, String>                   loginTokens           = new HashMap<String, String>();
  private String                                loginURL              = null;
  private boolean                               populateFailureTokens = false;
  private RememberMeServices                    rememberMeServices    = null;
  private Boolean                               requestEncrypted      = Boolean.FALSE;
  private AuthenticationSuccessHandler          successHandler        =
    new SavedRequestAwareAuthenticationSuccessHandler();

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new LoginSecurityFilter object.
   *
   * @param  login  DOCUMENT ME!
   */
  public LoginSecurityFilter(String login) {
    super(login);
    setLoginURL(login);
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  AbstractAuthenticationProcessingFilter#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() {
    if (rememberMeServices == null) {
      rememberMeServices = new NullRememberMeServices();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest,
   *       javax.servlet.http.HttpServletResponse)
   */
  @Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException, IOException, ServletException {
    Authentication user = null;

    if (enableLoginTokensValidation) {
      validateLoginTokens(request);
    }

    if ((SecurityContextHolder.getContext().getAuthentication() != null)
          && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
      try {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);

        if (session != null) {
          Enumeration attributeNames = session.getAttributeNames();

          while (attributeNames.hasMoreElements()) {
            String sAttribute = attributeNames.nextElement().toString();

            if (!"SPRING_SECURITY_CONTEXT".equalsIgnoreCase(sAttribute)) {
              session.removeAttribute(sAttribute);
            }
          }
        }
      } catch (Exception e) {
        log.error("Exception while clearing the session or context");
      }
    }

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      LoginAuthenticationTokens tokens = new LoginAuthenticationTokens();

      if (requestEncrypted) {
        Map<String, String> decryptQueryMap  = (Map<String, String>) request.getAttribute("decryptMap");
        String              validSessionId   = request.getSession().getId();
        String              requestSessionId = decryptQueryMap.get("validSessionId");
        log.info("validSessionId.." + validSessionId);
        log.info("requestSessionId.." + requestSessionId);

        if (!validSessionId.equals(requestSessionId)) {
          log.error("sessions are invalid....");
          throw new UsernameNotFoundException(messages.getMessage(
              "AbstractUserDetailsAuthenticationProvider.badCredentials", "security.user.notFound"));
        }

        populateTokensFromMap(decryptQueryMap, tokens);

      } else {
        populateTokens(request, tokens);

      }

      if ((((tokens.getToken1() != null) && StringUtils.hasText(tokens.getToken1().toString()))
              && ((tokens.getToken2() != null) && StringUtils.hasText(tokens.getToken2().toString())))) {
        user = getAuthenticationManager().authenticate(tokens);


        if (user != null) {
          log.info("Successfully Authenticated in object----->" + user.getName());

          // Store to SecurityContextHolder
          SecurityContextHolder.getContext().setAuthentication(user);
        }
      } else {
        throw new UsernameNotFoundException(messages.getMessage(
            "AbstractUserDetailsAuthenticationProvider.badCredentials", "security.user.notFound"));
      }
    } else {
      user = SecurityContextHolder.getContext().getAuthentication();
    } // end if-else

    return user;
  } // end method attemptAuthentication

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  request  DOCUMENT ME!
   */
  public void clearSessionTokens(HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    if (loginTokens != null) {
// session.removeAttribute(SPRING_SECURITY_LAST_EXCEPTION_KEY);
      session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
      session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION_MESSAGE");

      Set<String>      keys = loginTokens.keySet();
      Iterator<String> itr  = keys.iterator();

      while (itr.hasNext()) {
        String key = itr.next();

        if ("token1".equals(key) || "token2".equals(key)) {
          session.removeAttribute(loginTokens.get(key));
        } // end if-else
      }   // end while
    }

  } // end method clearSessionTokens

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Boolean getEnableLoginTokensValidation() {
    return enableLoginTokensValidation;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public AuthenticationFailureHandler getFailureHandler() {
    return failureHandler;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Map<String, String> getLoginTokens() {
    return loginTokens;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getLoginURL() {
    return loginURL;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   errList  list DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public List getOrderedErrorList(List errList) {
    List newErrorList = new LinkedList();

    if ((loginTokens != null) && (errList.size() > 0)) {
      String[] loginTokensArr = {
        loginTokens.get("token1"),
        loginTokens.get("token2")
      };

      for (int i = 0; i < loginTokens.size(); i++) {
        String myToken = loginTokensArr[i];

        for (int j = 0; j < errList.size(); j++) {
          if (myToken.equalsIgnoreCase((String) errList.get(j))) {
            newErrorList.add(myToken);

            break;
          }
        }
      }
    } // end if

    return newErrorList;
  } // end method getOrderedErrorList

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Boolean getRequestEncrypted() {
    if (null == requestEncrypted) {
      return Boolean.FALSE;
    }

    return requestEncrypted;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public boolean isPopulateFailureTokens() {
    return populateFailureTokens;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  tokens   failed tokens DOCUMENT ME!
   * @param  session  DOCUMENT ME!
   */
  public void populateRequest(LoginAuthenticationTokens tokens, HttpSession session) {
    if (loginTokens != null) {
      Set<String>      keys = loginTokens.keySet();
      Iterator<String> itr  = keys.iterator();

      while (itr.hasNext()) {
        String key = itr.next();

        if ("token1".equals(key)) {
          setRequestValue(session, loginTokens.get(key), tokens.getToken1());
        } else if ("token2".equals(key)) {
          setRequestValue(session, loginTokens.get(key), tokens.getToken2());
        } // end if-else
      }   // end while
    }     // end if

  } // end method populateRequest

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for enable login tokens validation.
   *
   * @param  enableLoginTokensValidation  Boolean
   */
  public void setEnableLoginTokensValidation(Boolean enableLoginTokensValidation) {
    this.enableLoginTokensValidation = enableLoginTokensValidation;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for login tokens.
   *
   * @param  loginTokens  Map
   */
  public void setLoginTokens(Map<String, String> loginTokens) {
    this.loginTokens = loginTokens;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for login URL.
   *
   * @param  loginURL  String
   */
  public void setLoginURL(String loginURL) {
    this.loginURL = loginURL;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for populate failure tokens.
   *
   * @param  populateFailureTokens  boolean
   */
  public void setPopulateFailureTokens(boolean populateFailureTokens) {
    this.populateFailureTokens = populateFailureTokens;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for request encrypted.
   *
   * @param  requestEncrypted  Boolean
   */
  public void setRequestEncrypted(Boolean requestEncrypted) {
    this.requestEncrypted = requestEncrypted;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   request  DOCUMENT ME!
   *
   * @throws  BadCredentialsException  DOCUMENT ME!
   */
  public void validateLoginTokens(HttpServletRequest request) {
    if (loginTokens != null) {
      Boolean     emptyTokes = false;
      HttpSession session    = request.getSession();
      List        errorList  = new LinkedList();
      clearSessionTokens(request);

      for (Map.Entry<String, String> entry : loginTokens.entrySet()) {
        String requestParam = entry.getValue();
        String paramValue   = request.getParameter(requestParam);

        if (StringUtils.hasText(paramValue)) {
          session.setAttribute(requestParam, paramValue);
        } else {
          errorList.add(requestParam);
          emptyTokes = true;
        }
      } // end for

      if (emptyTokes) {
        errorList = getOrderedErrorList(errorList);
        session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION_MESSAGE", errorList);
        throw new BadCredentialsException("MissingCredentials", new Throwable("MissingCredentials"));
      }
    } // end if
  }   // end method validateLoginTokens

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * successfulAuthentication.
   *
   * @throws  IOException       exception
   * @throws  ServletException  exception
   */
  protected void successfulAuthentication() throws IOException, ServletException { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest,
   *       javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain,
   *       org.springframework.security.core.Authentication)
   */
  @Override protected void successfulAuthentication(HttpServletRequest request,
    HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    if (log.isDebugEnabled()) {
      log.debug("Authentication success. Updating SecurityContextHolder to contain: "
        + authResult);
    }

    SecurityContextHolder.getContext().setAuthentication(authResult);

    rememberMeServices.loginSuccess(request, response, authResult);

    // Fire event
    if (eventPublisher != null) {
      eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
          authResult, getClass()));
    }

    clearSession(request);

    // TODO
// if (WebActivityChannel.AGENCY.equals(webLoggingService.getWebActivityChannel())) {
// // populating the sessionInfoContext even before the filter as we have login activity associated..
// SimpleSessionInfoContext si = new SimpleSessionInfoContext();
// si.setBrowser(SessionInfoContextHolder.getBrowser());
// si.setSessionInfo(request, WebActivityChannel.AGENCY, browserManager, sessionManager,
// System.currentTimeMillis());
// SessionInfoContextHolder.setSessionInfoContext(si);
//
// }

    successHandler.onAuthenticationSuccess(request, response, authResult);
  } // end method successfulAuthentication

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  AbstractAuthenticationProcessingFilter#unsuccessfulAuthentication(HttpServletRequest, HttpServletResponse,
   *       AuthenticationException)
   */
  @Override protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();

    String failedReason = failed.getMessage();

    if (log.isDebugEnabled()) {
      log.debug("Authentication request failed & failedReason is: " + failedReason);
      log.debug("Updated SecurityContextHolder to contain null Authentication");
      log.debug("Delegating to authentication failure handler" + failureHandler);

    }

    HttpSession session = request.getSession(false);

    if (session != null) {
      session = request.getSession();
      session.setAttribute(WebAttributes.WEB_INVOCATION_PRIVILEGE_EVALUATOR_ATTRIBUTE, failed);
    }

    if (populateFailureTokens) {
      if (!"MultipleSessions".equalsIgnoreCase(failedReason) && !"MissingCredentials".equalsIgnoreCase(failedReason)) {
        if (log.isDebugEnabled()) {
          log.debug("populating authentication failed tokens for : ");
        }

        if (failed instanceof UsernameNotFoundException) {
          UsernameNotFoundException exception = (UsernameNotFoundException) failed;
// populateRequest((LoginAuthenticationTokens) exception.getAuthentication(), session);
        }
      }
    }

    rememberMeServices.loginFail(request, response);
    
    /*
    1)16 digit account number page is shown only if there is a duplicate user for the 4 digit account number
    credential's entered on the website.
    2) Below we are checking the "musr".equals(request.getParameter("errorCode")) because if the credentials entered on
    the 16 digit account number page is wrong we need to redirect it to the same 16 digit account number page rather
    than showing the default 4 digit number screen.
     */
    String errorCode = request.getParameter("errorCode");

    if (!"DUPLICATEUSER2".equals(failedReason)
      && ("DUPLICATEUSER".equals(failedReason) || "musr".equals(errorCode))) {
      failureHandler.setDefaultFailureUrl(loginURL + "?error=true&errorCode=musr");
      populateRequestForDuplicateUsers(request, failed);
    } else if (((!"DUPLICATEUSER3".equals(failedReason) && ("DUPLICATEUSER2".equals(failedReason)))
      || "musr2".equals(errorCode))) {
      failureHandler.setDefaultFailureUrl(loginURL + "?error=true&errorCode=musr2");
      populateRequestForDuplicateUsers(request, failed);
    } else {
      failureHandler.setDefaultFailureUrl(loginURL + "?error=true");
    }

    if (!"DUPLICATEUSER".equals(failedReason) && !"DUPLICATEUSER2".equals(failedReason)
      && !"MissingCredentials".equalsIgnoreCase(failedReason)) {
      // login failure count for this session
// WebUtil.increaseSessionLoginErrorCount(request);
    }
    
    
    failureHandler.onAuthenticationFailure(request, response, failed);
  } // end method unsuccessfulAuthentication

  //~ ------------------------------------------------------------------------------------------------------------------

  private void clearSession(HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    if (session != null) {
      // Remove session login error count
      session.removeAttribute("loginErrorCount");
      clearSessionTokens(request);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getRequestValue(Map<String, String> decryptQueryMap, String key) {
    return decryptQueryMap.get(loginTokens.get(key));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getRequestValue(HttpServletRequest request, String key) {
    return request.getParameter(loginTokens.get(key));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void populateRequestForDuplicateUsers(HttpServletRequest request, AuthenticationException failed) {
    if (failed instanceof UsernameNotFoundException) {
      UsernameNotFoundException exception = (UsernameNotFoundException) failed;
// LoginAuthenticationTokens authWebSite = (LoginAuthenticationTokens) exception.getAuthentication();
// populateRequest(authWebSite, request.getSession(false));
    }

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void populateTokens(HttpServletRequest request, LoginAuthenticationTokens tokens) {
    if (loginTokens != null) {
      Set<String>      keys = loginTokens.keySet();
      Iterator<String> itr  = keys.iterator();

      while (itr.hasNext()) {
        String key = itr.next();

        if ("token1".equals(key)) {
          tokens.setToken1(getRequestValue(request, key));
        } else if ("token2".equals(key)) {
          tokens.setToken2(getRequestValue(request, key));
        }
      } // end while

      if ((request.getParameter("errorCode") != null) && "musr".equals(request.getParameter("errorCode"))) {
        tokens.setErrorMsg(true);
      }

      if ((request.getParameter("errorCode") != null) && "musr2".equals(request.getParameter("errorCode"))) {
        tokens.setSecondaryErrorMsg(true);
      }
    } // end if
  }   // end method populateTokens

  //~ ------------------------------------------------------------------------------------------------------------------

  private void populateTokensFromMap(Map<String, String> requestMap, LoginAuthenticationTokens tokens) {
    if (loginTokens != null) {
      Set<String>      keys = loginTokens.keySet();
      Iterator<String> itr  = keys.iterator();

      while (itr.hasNext()) {
        String key = itr.next();

        if ("token1".equals(key)) {
          tokens.setToken1(getRequestValue(requestMap, key));
        } else if ("token2".equals(key)) {
          tokens.setToken2(getRequestValue(requestMap, key));
        }
      } // end while

      if ((requestMap.get("errorCode") != null) && "musr".equals(requestMap.get("errorCode"))) {
        tokens.setErrorMsg(true);
      }

      if ((requestMap.get("errorCode") != null) && "musr2".equals(requestMap.get("errorCode"))) {
        tokens.setSecondaryErrorMsg(true);
      }
    } // end if
  }   // end method populateTokensFromMap

  //~ ------------------------------------------------------------------------------------------------------------------

  private void setRequestValue(HttpSession session, String key, Object value) {
    session.setAttribute(key, value);
  }
} // end class LoginSecurityFilter
