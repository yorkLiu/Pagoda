package com.ly.capture;


import com.ly.lib.ChaoJiYing;
import com.sun.tools.javac.util.Assert;
import org.apache.log4j.Logger;


/**
 * Created by yongliu on 7/5/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/05/2016 17:13
 */
public class CaptureUtils {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private final Logger log = Logger.getLogger(getClass());

  private String password;
  
  private String username;
  
  private static final String KEY_SCORE="tifen";
  
  //~ Methods ----------------------------------------------------------------------------------------------------------
  
  private int getScore(){
    Assert.checkNonNull(username);
    Assert.checkNonNull(password);
    String results = ChaoJiYing.GetScore(username, password);
    
    
    return 0;
  }


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
} // end class CaptureUtils
