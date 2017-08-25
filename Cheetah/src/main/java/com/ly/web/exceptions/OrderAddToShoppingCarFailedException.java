package com.ly.web.exceptions;

/**
 * Created by yongliu on 8/25/17.
 */
public class OrderAddToShoppingCarFailedException extends RuntimeException {
  private static final long serialVersionUID = -4123884977860111420L;


  public OrderAddToShoppingCarFailedException() {
    super();
  }

  public OrderAddToShoppingCarFailedException(String message) {
    super(message);
  }

  public OrderAddToShoppingCarFailedException(Throwable e){
    super(e);
  }

  public OrderAddToShoppingCarFailedException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
