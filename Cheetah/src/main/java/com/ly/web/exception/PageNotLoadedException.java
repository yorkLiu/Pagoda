package com.ly.web.exception;

/**
 * Created by yongliu on 12/29/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/29/2016 15:38
 */
public class PageNotLoadedException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 3723113997076799982L;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new PageNotLoadedException object.
   *
   * @param  message  String
   */
  public PageNotLoadedException(String message) {
    super(message);
  }

  /**
   * Creates a new PageNotLoadedException object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public PageNotLoadedException(String message, Throwable cause) {
    super(message, cause);
  }
} // end class PageNotLoadedException
