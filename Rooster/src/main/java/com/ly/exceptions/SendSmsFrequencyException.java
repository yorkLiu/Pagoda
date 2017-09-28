package com.ly.exceptions;

/**
 * Created by yongliu on 9/25/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/25/2017 18:30
 */
public class SendSmsFrequencyException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -6477670213680705436L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Long timeStamp;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new SendSmsFrequencyException object.
   */
  public SendSmsFrequencyException() {
    super();
  }

  /**
   * Creates a new SendSmsFrequencyException object.
   *
   * @param  message  String
   */
  public SendSmsFrequencyException(String message) {
    super(message);
  }

  /**
   * Creates a new SendSmsFrequencyException object.
   *
   * @param  timeStamp  Long
   * @param  message    String
   */
  public SendSmsFrequencyException(Long timeStamp, String message) {
    super(message);
    this.timeStamp = timeStamp;
  }

  /**
   * Creates a new SendSmsFrequencyException object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public SendSmsFrequencyException(String message, Throwable cause) {
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
} // end class SendSmsFrequencyException
