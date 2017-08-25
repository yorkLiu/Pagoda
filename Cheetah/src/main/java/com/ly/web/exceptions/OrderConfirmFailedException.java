package com.ly.web.exceptions;

/**
 * Created by yongliu on 8/25/17.
 */
public class OrderConfirmFailedException extends RuntimeException {
  private static final long serialVersionUID = -6288845003208162384L;

  public OrderConfirmFailedException() {
    super();
  }

  public OrderConfirmFailedException(String message) {
    super(message);
  }

  public OrderConfirmFailedException(Throwable e){
    super(e);
  }

  public OrderConfirmFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
