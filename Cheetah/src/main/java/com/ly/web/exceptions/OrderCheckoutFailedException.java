package com.ly.web.exceptions;

/**
 * Created by yongliu on 8/25/17.
 */
public class OrderCheckoutFailedException extends RuntimeException {
  private static final long serialVersionUID = 7192428415565326560L;

  public OrderCheckoutFailedException() {
    super();
  }

  public OrderCheckoutFailedException(String message) {
    super(message);
  }

  public OrderCheckoutFailedException(Throwable e){
    super(e);
  }

  public OrderCheckoutFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
