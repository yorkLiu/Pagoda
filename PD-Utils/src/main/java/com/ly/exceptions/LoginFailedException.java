package com.ly.exceptions;

/**
 * Created by yongliu on 8/18/17.
 */
public class LoginFailedException extends RuntimeException {


  private static final long serialVersionUID = 6875674268257322284L;

  public LoginFailedException() {
    super();
  }

  public LoginFailedException(String message) {
    super(message);
  }
  
  public LoginFailedException(Throwable e){
    super(e);
  }

  public LoginFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
