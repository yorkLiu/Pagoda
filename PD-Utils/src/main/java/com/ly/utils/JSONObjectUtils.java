package com.ly.utils;


import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;


/**
 * Created by yongliu on 7/5/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/06/2016 10:47
 */
public class JSONObjectUtils {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * toJSONObject.
   *
   * @param   jsonStr  String
   *
   * @return  JSONObject
   */
  public static JSONObject toJSONObject(String jsonStr) {
    if ((jsonStr != null) && StringUtils.hasText(jsonStr)) {
      return JSONObject.fromObject(jsonStr);
    }

    return null;
  }

}
