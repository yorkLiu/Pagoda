package com.ly.capture;


import org.apache.log4j.Logger;

import org.springframework.util.Assert;

import com.ly.lib.ChaoJiYing;

import com.ly.utils.JSONObjectUtils;

import net.sf.json.JSONObject;


/**
 * Created by yongliu on 7/5/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/05/2016 17:13
 */
public class CaptureUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String KEY_SCORE = "tifen";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private final Logger log = Logger.getLogger(getClass());

  private String password;

  private String username;

  //~ Methods ----------------------------------------------------------------------------------------------------------

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

  //~ ------------------------------------------------------------------------------------------------------------------

  private int getScore() {
    Assert.notNull(username);
    Assert.notNull(password);

    String results = ChaoJiYing.GetScore(username, password);

    JSONObject json = JSONObjectUtils.toJSONObject(results);

    if (json != null) {
      String scoreStr = (String) json.get(KEY_SCORE);

      if (log.isDebugEnabled()) {
        log.debug("UserName:" + username + ", you score is:" + scoreStr);
      }

      return Integer.parseInt(scoreStr);
    }

    if (log.isDebugEnabled()) {
      log.debug("UserName:" + username + ", you score is: 0");
    }

    return 0;
  }
} // end class CaptureUtils
