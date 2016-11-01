package com.ly.utils;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import org.apache.log4j.Logger;

import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 10/31/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/31/2016 21:21
 */
public class URLUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger logger = Logger.getLogger(URLUtils.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for params from url.
   *
   * @param   url  String
   *
   * @return  Map
   */
  public static Map<String, String> getParamsFromUrl(String url) {
    Map<String, String> paramMap = new HashedMap();
    logger.info("Resolve the url: " + url);

    if ((url != null) && StringUtils.hasText(url) && url.contains("?")) {
      String paramStr = url.split("\\?")[1];

      if (logger.isDebugEnabled()) {
        logger.debug("The URL's params string: " + paramStr);
      }

      if (StringUtils.hasText(paramStr)) {
        String[] params = paramStr.split("&");

        for (String param : params) {
          if (param.contains("=")) {
            String[] keyValues = param.split("=");

            if ((keyValues.length == 2) && (keyValues[0] != null) && StringUtils.hasText(keyValues[0])) {
              paramMap.put(keyValues[0], keyValues[1]);
            }
          }
        }
      }
    }

    return paramMap;
  } // end method getParamsFromUrl
} // end class URLUtils
