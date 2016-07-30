package com.ly.web.view;

import java.io.UnsupportedEncodingException;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.support.ResourceBundleMessageSource;


/**
 * Created by yongliu on 7/28/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/28/2016 16:08
 */
public class PagodaResourceBundleMessageSource extends ResourceBundleMessageSource {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String ENCODING = "UTF-8"; // 注意属性文件使用GBK编码
  private static final String NULL     = "null";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** cache the encoding key value *. */
  Map<String, String> encodingCache = new ConcurrentHashMap<String, String>(200);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.context.support.MessageSourceSupport#createMessageFormat(java.lang.String, java.util.Locale)
   */
  @Override protected MessageFormat createMessageFormat(String msg, Locale locale) {
    if (logger.isDebugEnabled()) {
      logger.debug("Creating MessageFormat for pattern [" + msg
        + "] and locale '" + locale + "'");
    }

    msg = decodeString(msg, ENCODING);

    return new MessageFormat(((msg != null) ? msg : ""), locale);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * resolve no argus.
   *
   * @param   code    String
   * @param   locale  Locale
   *
   * @return  resolve no argus.
   */
  @Override protected String resolveCodeWithoutArguments(String code, Locale locale) {
    String message = super.resolveCodeWithoutArguments(code, locale);

    return decodeString(message, ENCODING);

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 转码.
   *
   * @param   message  $param.type$
   * @param   encode   $param.type$
   *
   * @return  转码.
   */
  private String decodeString(String message, String encode) {
    String encodMessage = encodingCache.get(message);

    if (encodMessage == null) {
      try {
        encodMessage = new String(message.getBytes("ISO8859-1"), encode);

        if (message != null) {
          encodingCache.put(message, encodMessage);
        } else {
          encodingCache.put(message, NULL);
          // log the code is not exist in properties
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }

    return encodMessage;
  }

} // end class PagodaResourceBundleMessageSource
