package com.ly.exceptions;

/**
 * Created by yongliu on 9/27/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/27/2017 12:54
 */
public class SendSmsOutOfMaxCountException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -7469750309179364440L;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new SendSmsOutOfMaxCountException object.
   */
  public SendSmsOutOfMaxCountException() {
    super();
  }

  /**
   * Creates a new SendSmsOutOfMaxCountException object.
   *
   * @param  message  String
   */
  public SendSmsOutOfMaxCountException(String message) {
    super(message);
  }

  /**
   * Creates a new SendSmsOutOfMaxCountException object.
   *
   * @param  cause  Throwable
   */
  public SendSmsOutOfMaxCountException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new SendSmsOutOfMaxCountException object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public SendSmsOutOfMaxCountException(String message, Throwable cause) {
    super(message, cause);
  }
} // end class SendSmsOutOfMaxCountException
