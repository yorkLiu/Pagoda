package com.ly.exceptions;

/**
 * Created by yongliu on 9/27/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/27/2017 09:45
 */
public class NotReceiveMessageException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -5637440119508291820L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Long timeStamp;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new NotReceiveMessageException object.
   */
  public NotReceiveMessageException() {
    super();
  }

  /**
   * Creates a new NotReceiveMessageException object.
   *
   * @param  message  String
   */
  public NotReceiveMessageException(String message) {
    super(message);
  }

  /**
   * Creates a new NotReceiveMessageException object.
   *
   * @param  timeStamp  Long
   * @param  message    String
   */
  public NotReceiveMessageException(Long timeStamp, String message) {
    super(message);
    this.timeStamp = timeStamp;
  }


  /**
   * Creates a new NotReceiveMessageException object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public NotReceiveMessageException(String message, Throwable cause) {
    super(message, cause);
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for time stamp.
   *
   * @return  Long
   */
  public Long getTimeStamp() {
    return timeStamp;
  }
} // end class NotReceiveMessageException
