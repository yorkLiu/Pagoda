package com.ly.exceptions;

/**
 * Created by yongliu on 9/27/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/27/2017 10:30
 */
public class HistoryPhoneIncorrectException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 8773804862378692256L;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new HistoryPhoneIncorrectException object.
   */
  public HistoryPhoneIncorrectException() {
    super();
  }

  /**
   * Creates a new HistoryPhoneIncorrectException object.
   *
   * @param  message  String
   */
  public HistoryPhoneIncorrectException(String message) {
    super(message);
  }

  /**
   * Creates a new HistoryPhoneIncorrectException object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public HistoryPhoneIncorrectException(String message, Throwable cause) {
    super(message, cause);
  }
} // end class HistoryPhoneIncorrectException
