package com.ly.exceptions;

/**
 * Created by yongliu on 9/27/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/27/2017 10:47
 */
public class UnknownPhoneNumberException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -7523320200004306548L;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new UnknownPhoneNumberException object.
   */
  public UnknownPhoneNumberException() {
    super();
  }

  /**
   * Creates a new UnknownPhoneNumberException object.
   *
   * @param  message  String
   */
  public UnknownPhoneNumberException(String message) {
    super(message);
  }

  /**
   * Creates a new UnknownPhoneNumberException object.
   *
   * @param  cause  Throwable
   */
  public UnknownPhoneNumberException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new UnknownPhoneNumberException object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public UnknownPhoneNumberException(String message, Throwable cause) {
    super(message, cause);
  }
} // end class UnknownPhoneNumberException
