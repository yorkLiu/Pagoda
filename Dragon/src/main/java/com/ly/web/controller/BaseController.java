package com.ly.web.controller;

import com.ly.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;



@Controller public class BaseController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected final transient Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private ApplicationContext context;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Get message from resource bundle for default locale.
   *
   * @param   key  DOCUMENT ME!
   *
   * @return  get message from resource bundle for default locale
   */
  public String getMessage(String key) {
    return context.getMessage(key, null, Locale.getDefault());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   key   DOCUMENT ME!
   * @param   args  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getMessage(String key, Object[] args) {
    return context.getMessage(key, args, Locale.getDefault());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get message from resource bundle for locale.
   *
   * @param   key     DOCUMENT ME!
   * @param   locale  DOCUMENT ME!
   *
   * @return  get message from resource bundle for locale
   */
  public String getMessage(String key, Locale locale) {
    return context.getMessage(key, null, locale);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   key     DOCUMENT ME!
   * @param   args    DOCUMENT ME!
   * @param   locale  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getMessage(String key, Object[] args, Locale locale) {
    return context.getMessage(key, args, locale);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   <T>        DOCUMENT ME!
   * @param   params     DOCUMENT ME!
   * @param   paramName  DOCUMENT ME!
   * @param   cls        DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public <T> T getParamValue(Map<String, String> params, String paramName, Class<T> cls) {
    Object value    = null;
    String strValue = params.get(paramName);

    if (StringUtils.hasText(strValue)) {
      if (cls == String.class) {
        value = strValue;
      } else if (cls == Integer.class) {
        value = Integer.valueOf(strValue);
      } else if (cls == Long.class) {
        value = Long.valueOf(strValue);
      } else if (cls == Double.class) {
        value = Double.valueOf(strValue);
      } else if (cls == Float.class) {
        value = Float.valueOf(strValue);
      } else if (cls == BigDecimal.class) {
        value = new BigDecimal(strValue);
      } else if (cls == Boolean.class) {
        if (strValue.equalsIgnoreCase("True")
              || strValue.equalsIgnoreCase("Yes")
              || strValue.equalsIgnoreCase("T")
              || strValue.equalsIgnoreCase("Y")) {
          value = Boolean.TRUE;
        } else {
          value = Boolean.FALSE;
        }
      } else if (cls == Date.class) {
        value = DateUtil.toDate(strValue);
      } // end if-else
    }   // end if

    return (T) value;
  } // end method getParamValue

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  request  DOCUMENT ME!
   * @param  binder   DOCUMENT ME!
   */
  @InitBinder public void initBinder(HttpServletRequest request, WebDataBinder binder) {
    binder.registerCustomEditor(Integer.class, null,
      new CustomNumberEditor(Integer.class, null, true));
    binder.registerCustomEditor(Long.class, null,
      new CustomNumberEditor(Long.class, null, true));
    binder.registerCustomEditor(byte[].class,
      new ByteArrayMultipartFileEditor());

    SimpleDateFormat dateFormat = new SimpleDateFormat(getMessage(
          "date.format", request.getLocale()));
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, null,
      new CustomDateEditor(dateFormat, true));
  } // end method initBinder

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   object  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected Object getNullableObject(Object object) {
    if (object == null) {
      return "";
    }

    return object;
  }
} // end class BaseController
