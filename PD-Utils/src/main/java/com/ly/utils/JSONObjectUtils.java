package com.ly.utils;


import org.springframework.util.StringUtils;

/**
 * Created by yongliu on 7/5/16.
 */
public class JSONObjectUtils {

  public static JSONObject toJSONObject(String jsonStr) {
    if ((jsonStr != null) && StringUtils.hasText(jsonStr)) {
      return JSONObject.fromObject(jsonStr);
    }

    return null;
  }
  
}
