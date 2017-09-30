package com.ly.web.exceptions;

/**
 * Created by yongliu on 9/30/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/30/2017 10:49
 */
public class CommentFailedWithoutBindPhone extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 5578301636384938101L;

  //~ Constructors -----------------------------------------------------------------------------------------------------


  /**
   * Creates a new CommentFailedWithoutBindPhone object.
   */
  public CommentFailedWithoutBindPhone() {
    super();
  }

  /**
   * Creates a new CommentFailedWithoutBindPhone object.
   *
   * @param  message  String
   */
  public CommentFailedWithoutBindPhone(String message) {
    super(message);
  }

  /**
   * Creates a new CommentFailedWithoutBindPhone object.
   *
   * @param  cause  Throwable
   */
  public CommentFailedWithoutBindPhone(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new CommentFailedWithoutBindPhone object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public CommentFailedWithoutBindPhone(String message, Throwable cause) {
    super(message, cause);
  }
} // end class CommentFailedWithoutBindPhone
