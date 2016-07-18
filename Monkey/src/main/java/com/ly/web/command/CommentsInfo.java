package com.ly.web.command;

import java.io.Serializable;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;


/**
 * Created by yongliu on 7/12/16.
 *
 * @author   <a href="pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 14:40
 */
public class CommentsInfo implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 4038225625321253332L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /**
   * key: sku
   * value: comments content
   * @i.e 
   *    key: 123423423423
   *    value: It's so good
   */
  private Map<String, String> commentsMap = new HashedMap(10);

  private String orderId;

  private String password;

  private String username;

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * getter method for comments map.
   *
   * @return  Map
   */
  public Map<String, String> getCommentsMap() {
    return commentsMap;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for order id.
   *
   * @return  String
   */
  public String getOrderId() {
    return orderId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for password.
   *
   * @return  String
   */
  public String getPassword() {
    return password;
  }

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * getter method for username.
   *
   * @return  String
   */
  public String getUsername() {
    return username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for comments map.
   *
   * @param  commentsMap  Map
   */
  public void setCommentsMap(Map<String, String> commentsMap) {
    this.commentsMap = commentsMap;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order id.
   *
   * @param  orderId  String
   */
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for password.
   *
   * @param  password  String
   */
  public void setPassword(String password) {
    this.password = password;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for username.
   *
   * @param  username  String
   */
  public void setUsername(String username) {
    this.username = username;
  }
} // end class CommentsInfo
