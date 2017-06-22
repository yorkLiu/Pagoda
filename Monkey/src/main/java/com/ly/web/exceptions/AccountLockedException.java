package com.ly.web.exceptions;

/**
 * Created by yongliu on 6/22/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/22/2017 14:07
 */
public class AccountLockedException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -2192572162585049129L;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new AccountLockedException object.
   */
  public AccountLockedException() {
    super();
  }

  /**
   * Creates a new AccountLockedException object.
   *
   * @param  message  String
   */
  public AccountLockedException(String message) {
    super(message);
  }

  /**
   * Creates a new AccountLockedException object.
   *
   * @param  cause  Throwable
   */
  public AccountLockedException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new AccountLockedException object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public AccountLockedException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new AccountLockedException object.
   *
   * @param  username  String
   * @param  message   String
   */
  public AccountLockedException(String username, String message) {
    super("The username [" + username + "] was locked" + message);
  }
} // end class AccountLockedException
