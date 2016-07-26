package com.ly.capture;


import org.apache.log4j.Logger;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ly.lib.ChaoJiYing;

import com.ly.utils.JSONObjectUtils;

import net.sf.json.JSONObject;


/**
 * Created by yongliu on 7/5/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/05/2016 17:13
 */
public class CaptureUtil {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String KEY_SCORE = "tifen";


  private static final String CAPTURE_KEY = "pic_str";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String appName;

  private final Logger log = Logger.getLogger(getClass());

  private String password;

  private String username;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for app name.
   *
   * @return  String
   */
  public String getAppName() {
    return appName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for capture.
   *
   * @param   path  String
   *
   * @return  String
   */
  public String getCapture(String path) {
    return getCapture(CaptureType.CAPTURE_TYPE_1104, path);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for capture.
   *
   * @param   captureType  CaptureType
   * @param   path         String
   *
   * @return  String capture
   */
  public String getCapture(CaptureType captureType, String path) {
    String capture = null;

    try {
      if ((path == null) || !StringUtils.hasText(path)) {
        log.error("Capture image path is 'NULL' or empty, return NULL.");

        return null;
      }

      if (log.isDebugEnabled()) {
        log.debug("Evaluate the capture on path: " + path);
      }

      String result = ChaoJiYing.PostPic(getUsername(), getPassword(), getAppName(), captureType.toString(), "0", "0",
          "0", path);

      if (log.isDebugEnabled()) {
        log.debug("result: " + result);
      }

      capture = JSONObjectUtils.getValue(result, CAPTURE_KEY);

      if (log.isDebugEnabled()) {
        log.debug("The capture is: " + capture);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } // end try-catch

    return capture;

  } // end method getCapture

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
   * setter method for app name.
   *
   * @param  appName  String
   */
  public void setAppName(String appName) {
    this.appName = appName;
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

} // end class CaptureUtil
