package com.ly.model;

import com.ly.suma.api.MyControl;


/**
 * Created by yongliu on 9/22/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2017 12:45
 */
public class SMSReceiverInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String password;

  private String token;

  private String username;
  
  private String pid;
  
  private String authorID;

  //~ Methods ----------------------------------------------------------------------------------------------------------

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
   * getter method for token.
   *
   * @return  String
   */
  public String getToken() {
    return token;
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
   * getter method for login.
   *
   * @return  Boolean
   */
  public Boolean isLogin() {
    if (MyControl.isNull(username) || MyControl.isNull(password) || MyControl.isNull(token)) {
      return false;
    } else {
      return true;
    }
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
   * setter method for token.
   *
   * @param  token  String
   */
  public void setToken(String token) {
    this.token = token;
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

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getAuthorID() {
    return authorID;
  }

  public void setAuthorID(String authorID) {
    this.authorID = authorID;
  }
} // end class SMSReceiverInfo
