package com.ly.web.command;

import org.directwebremoting.annotations.DataTransferObject;

import org.directwebremoting.convert.ObjectConverter;


/**
 * Created by yongliu on 7/26/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/26/2016 12:57
 */
@DataTransferObject(converter = ObjectConverter.class)
public class DWRBaseResponseCommand<T> {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  protected T data;

  /** TODO: DOCUMENT ME! */
  protected String message;

  /** TODO: DOCUMENT ME! */
  protected boolean success;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new DWRBaseResponseCommand object.
   */
  public DWRBaseResponseCommand() { }

  /**
   * Creates a new DWRBaseResponseCommand object.
   *
   * @param  data  T
   */
  public DWRBaseResponseCommand(T data) {
    this(data, Boolean.TRUE);
  }

  /**
   * Creates a new DWRBaseResponseCommand object.
   *
   * @param  data     T
   * @param  success  boolean
   */
  public DWRBaseResponseCommand(T data, boolean success) {
    this(data, success, null);
  }

  /**
   * Creates a new DWRBaseResponseCommand object.
   *
   * @param  data     T
   * @param  success  boolean
   * @param  message  String
   */
  public DWRBaseResponseCommand(T data, boolean success, String message) {
    this.data    = data;
    this.success = success;
    this.message = message;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for data.
   *
   * @return  T
   */
  public T getData() {
    return data;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for message.
   *
   * @return  String
   */
  public String getMessage() {
    return message;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for success.
   *
   * @return  boolean
   */
  public boolean isSuccess() {
    return success;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for data.
   *
   * @param  data  T
   */
  public void setData(T data) {
    this.data = data;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for message.
   *
   * @param  message  String
   */
  public void setMessage(String message) {
    this.message = message;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for success.
   *
   * @param  success  boolean
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }
} // end class DWRBaseResponseCommand
