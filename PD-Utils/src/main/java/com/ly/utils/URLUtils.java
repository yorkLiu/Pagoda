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

  public static final String P_YHD_URL_PREFIX          = "http://item.yhd.com/item/";
  public static final String P_YHT_URL_PREFIX = "http://t.yhd.com/detail/";
  public static final String P_JD_URL_PREFIX           = "http://item.jd.com/";
  public static final String P_JD_URL_HTTPS_PREFIX     = "https://item.jd.com/";

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
  
  /**
   * getter method for sku from url.
   *
   * @param   skuOrUrl  String
   *
   * @return  String
   */
  public static String getSkuFromUrl(String skuOrUrl) {
      String sku = null;

      if (logger.isDebugEnabled()) {
        logger.debug("Ready analysis SKU form: " + skuOrUrl);
      }

      if ((skuOrUrl != null) && StringUtils.hasText(skuOrUrl)) {
        boolean isUrl = Boolean.FALSE;
        String prefix = null;
        if(skuOrUrl.contains(P_YHD_URL_PREFIX)){
          isUrl = Boolean.TRUE;
          prefix = P_YHD_URL_PREFIX;
        } else if(skuOrUrl.contains(P_YHT_URL_PREFIX)){
          isUrl = Boolean.TRUE;
          prefix = P_YHT_URL_PREFIX;
        } else if(skuOrUrl.contains(P_JD_URL_PREFIX)){
          isUrl = Boolean.TRUE;
          prefix = P_JD_URL_PREFIX;
        } else if(skuOrUrl.contains(P_JD_URL_HTTPS_PREFIX)){
          isUrl = Boolean.TRUE;
          prefix = P_JD_URL_HTTPS_PREFIX;
        }

        if(isUrl && prefix != null){
          if (logger.isDebugEnabled()) {
            logger.debug(skuOrUrl + " is a url, will get sku NO. from this url.");
          }

          String urlWithoutParams = skuOrUrl.split("\\?")[0].split("\\#")[0];

          if (logger.isDebugEnabled()) {
            logger.debug("Analysis the url without parameters is: " + urlWithoutParams);
          }

          sku = urlWithoutParams.replace(prefix, "").replace(".html", "").trim();

          if (logger.isDebugEnabled()) {
            logger.debug("Got the SKU: " + sku + " from the url: " + skuOrUrl);
          }

        } else {
          if (logger.isDebugEnabled()) {
            logger.debug(skuOrUrl + " is not a url, it is a actual sku.");
          }

          sku = skuOrUrl;

          if (logger.isDebugEnabled()) {
            logger.debug("So the sku is: " + sku);
          }
        } // end if-else
      }   // end if

      if (logger.isDebugEnabled()) {
        logger.debug("Return sku[" + sku + "]");
      }

      return sku;
    }
} // end class URLUtils
