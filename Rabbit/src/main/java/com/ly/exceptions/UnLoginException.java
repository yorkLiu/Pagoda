package com.ly.exceptions;

/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 11:03
 */
public class UnLoginException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 4384418070530973754L;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new UnLoginException object.
   */
  public UnLoginException() {
    super();
  }

  /**
   * Creates a new UnLoginException object.
   *
   * @param  msg  String
   */
  public UnLoginException(String msg) {
    super(msg);
  }

  /**
   * Creates a new UnLoginException object.
   *
   * @param  msg  String
   * @param  t    Throwable
   */
  public UnLoginException(String msg, Throwable t) {
    super(msg, t);
  }
} // end class UnLoginException
