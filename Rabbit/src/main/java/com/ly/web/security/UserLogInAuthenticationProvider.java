package com.ly.web.security;


import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ly.dao.UserDao;

import com.ly.model.User;

import com.ly.web.security.util.LoginAuthenticationTokens;
import com.ly.web.security.util.LoginUser;


/**
 * Created by wenchaoyong on 16/3/8.
 *
 * @author   <a href="mailto:wenchao.yong@ozstrategy.com">Wenchao Yong</a>
 * @version  03/08/2016 13:02
 */
public class UserLogInAuthenticationProvider extends DaoAuthenticationProvider implements AuthenticationProvider,
  MessageSourceAware {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static Log log = LogFactory.getLog(UserLogInAuthenticationProvider.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  protected UserCache userCache = new NullUserCache();

  /** DOCUMENT ME! */
  private MessageSourceAccessor messages           = SpringSecurityMessageSource.getAccessor();
  private BCryptPasswordEncoder passwordEncoder;
  @Autowired private UserDao    userDao;
  private UserDetailsChecker    userDetailsChecker = new AccountStatusUserDetailsChecker();

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  AuthenticationProvider#authenticate(Authentication)
   */
  @Override public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    LoginAuthenticationTokens tokens    = (LoginAuthenticationTokens) authentication;
    String                    username  = tokens.getName();
    LoginUser                 loginUser = (LoginUser) this.userCache.getUserFromCache(username);
    User                      user      = (User) userDao.loadUserByUsername(username);

    if (loginUser == null) {
      if ((tokens.getPrincipal() == null) || "".equals(tokens.getPrincipal().toString())) {
        throw new UsernameNotFoundException("username is empty");
      }

      if ((authentication.getCredentials() == null) || "".equals(tokens.getCredentials().toString())) {
        throw new UsernameNotFoundException("password is empty");
      }

      String presentedPassword = tokens.getCredentials().toString();

      // Get user information
      UserDetails userDetails = getUserDetailsService().loadUserByUsername(username);


      Object  principalToReturn = user;
      boolean passwordMatched   = false;

      // check the password with new password encoder (SHA-256)
      try {
        passwordMatched = passwordEncoder.matches(presentedPassword, userDetails.getPassword());
      } catch (IllegalArgumentException e) {
        log.error(e);
      }

      // check the password with old password encoder (SHA-1)
      if ((!passwordMatched)) {
        passwordMatched = true;

        // chang old password encoder(SHA-1) to new password encoder(SHA-256)
        user.setPassword(passwordEncoder.encode(presentedPassword));
        userDao.saveObject(user);
      }

      if (passwordMatched) {
        userDetailsChecker.check(user);

        // Authenticate successfully
        return this.createSuccessAuthentication(principalToReturn, tokens, user);
      } else {
        throw new BadCredentialsException("Bad credentials");
      }

    } // end if

    return null;
  } // end method authenticate

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for user cache.
   *
   * @return  UserCache
   */
  @Override public UserCache getUserCache() {
    return userCache;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for user dao.
   *
   * @return  UserDao
   */
  public UserDao getUserDao() {
    return userDao;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  MessageSourceAware#setMessageSource(MessageSource)
   */
  @Override public void setMessageSource(MessageSource messageSource) {
    this.messages = new MessageSourceAccessor(messageSource);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for password encoder.
   *
   * @param  passwordEncoder  org.springframework.security.crypto.password.PasswordEncoder
   */
  public void setPasswordEncoder(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for user cache.
   *
   * @param  userCache  UserCache
   */
  @Override public void setUserCache(UserCache userCache) {
    this.userCache = userCache;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for user dao.
   *
   * @param  userDao  UserDao
   */
  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  AuthenticationProvider#supports(Class)
   */
  @Override public boolean supports(Class<?> authentication) {
    return (LoginAuthenticationTokens.class.isAssignableFrom(
          authentication));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * createSuccessAuthentication.
   *
   * @param   principal       Object
   * @param   authentication  LoginAuthenticationTokens
   * @param   user            UserDetails
   *
   * @return  Authentication
   */
  private Authentication createSuccessAuthentication(Object principal, LoginAuthenticationTokens authentication,
    UserDetails user) {
    LoginAuthenticationTokens result = new LoginAuthenticationTokens(
        principal, authentication.getToken2(),
        authentication.getToken1(), authentication.getToken2(), (Collection<GrantedAuthority>) user.getAuthorities());
    result.setDetails(authentication.getDetails());

    return result;
  }

} // end class UserLogInAuthenticationProvider
