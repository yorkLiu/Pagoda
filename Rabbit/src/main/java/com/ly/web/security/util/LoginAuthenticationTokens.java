package com.ly.web.security.util;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


/**
 * Created by yongliu on 9/12/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 17:14
 */
public class LoginAuthenticationTokens extends AbstractAuthenticationToken {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 858306053132859384L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Object credentials = null;


  private boolean errorMsg;
  private Object  principal         = null;
  private boolean secondaryErrorMsg;
  private Object  token1;
  private Object  token2;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new LoginAuthenticationTokens object.
   */
  public LoginAuthenticationTokens() {
    super(null);
    setAuthenticated(false);
  }

  /**
   * Creates a new LoginAuthenticationTokens object.
   *
   * @param  token1  Object
   * @param  token2  Object
   */
  public LoginAuthenticationTokens(Object token1, Object token2) {
    super(null);
    this.token1      = token1;
    this.token2      = token2;
    this.principal   = token1;
    this.credentials = token2;
    setAuthenticated(false);
  }

  /**
   * Creates a new LoginAuthenticationTokens object.
   *
   * @param  principal    Object
   * @param  credentials  Object
   * @param  token1       Object
   * @param  token2       Object
   * @param  authorities  Collection
   */
  public LoginAuthenticationTokens(Object principal, Object credentials, Object token1, Object token2,
    Collection<GrantedAuthority> authorities) {
    super(authorities);
    this.principal   = principal;
    this.credentials = credentials;
    this.token1      = token1;
    this.token2      = token2;
    this.setAuthenticated(true);
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.Authentication#getCredentials()
   */
  @Override public Object getCredentials() {
    return this.credentials;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.Authentication#getPrincipal()
   */
  @Override public Object getPrincipal() {
    return this.principal;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for token1.
   *
   * @return  Object
   */
  public Object getToken1() {
    return token1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for token2.
   *
   * @return  Object
   */
  public Object getToken2() {
    return token2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for error msg.
   *
   * @return  boolean
   */
  public boolean isErrorMsg() {
    return errorMsg;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for secondary error msg.
   *
   * @return  boolean
   */
  public boolean isSecondaryErrorMsg() {
    return secondaryErrorMsg;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for credentials.
   *
   * @param  credentials  Object
   */
  public void setCredentials(Object credentials) {
    this.credentials = credentials;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for error msg.
   *
   * @param  errorMsg  boolean
   */
  public void setErrorMsg(boolean errorMsg) {
    this.errorMsg = errorMsg;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for principal.
   *
   * @param  principal  Object
   */
  public void setPrincipal(Object principal) {
    this.principal = principal;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for secondary error msg.
   *
   * @param  secondaryErrorMsg  boolean
   */
  public void setSecondaryErrorMsg(boolean secondaryErrorMsg) {
    this.secondaryErrorMsg = secondaryErrorMsg;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for token1.
   *
   * @param  token1  Object
   */
  public void setToken1(Object token1) {
    this.token1    = token1;
    this.principal = token1;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for token2.
   *
   * @param  token2  Object
   */
  public void setToken2(Object token2) {
    this.token2      = token2;
    this.credentials = token2;
  }
} // end class LoginAuthenticationTokens
