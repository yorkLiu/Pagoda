package com.ly.utils;


import org.apache.log4j.Logger;

import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;


/**
 * Created by yongliu on 7/5/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/06/2016 10:47
 */
public class JSONObjectUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger log = Logger.getLogger(JSONObjectUtils.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for value.
   *
   * @param   jsonStr  String
   * @param   key      String
   *
   * @return  String
   */
  public static String getValue(String jsonStr, final String key) {
    if (log.isDebugEnabled()) {
      log.debug("Json string:" + jsonStr + " and KEY is: " + key);
    }

    JSONObject json = toJSONObject(jsonStr);

    if ((json != null) && (key != null)) {
      String value = json.has(key) ? json.get(key).toString() : null;

      if (log.isDebugEnabled()) {
        log.debug("json.get(key) value is: " + value);
      }

      return value;
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * toJSONObject.
   *
   * @param   jsonStr  String
   *
   * @return  JSONObject
   */
  public static JSONObject toJSONObject(String jsonStr) {
    if (log.isDebugEnabled()) {
      log.debug("Json string:" + jsonStr);
    }

    if ((jsonStr != null) && StringUtils.hasText(jsonStr)) {
      return JSONObject.fromObject(jsonStr);
    }

    return null;
  }

} // end class JSONObjectUtils
