package com.ly.sort.util;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


/**
 * Created by yongliu on 8/17/15.
 */
public class Utils {

  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
  private static final Charset GB2312_CHARSET = Charset.forName("GB2312");

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param astring DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public static String convertUTF8(String astring) {
    if (astring != null) {

      byte[] bytes = astring.getBytes(UTF8_CHARSET);

      return new String(bytes, UTF8_CHARSET);
    }

    return astring;
  }

  /**
   * DOCUMENT ME!
   *
   * @param astring DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public static String convertGB2312(String astring) {

    if (astring != null) {

      byte[] bytes = astring.getBytes();

      return new String(bytes, GB2312_CHARSET);
    }

    return astring;
  }

  /**
   * DOCUMENT ME!
   *
   * @param in DOCUMENT ME!
   * @param charset DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public static InputStream getStringTOInputStream(String in, Charset charset)
    throws Exception {

    ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes(charset));

    return is;
  }
  
  public static String getStringFormInputStream(InputStream in, Charset charset){
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
      StringBuffer sbf = new StringBuffer();
      String line = null;
      while ((line = br.readLine()) != null) {
        sbf.append(line);
      }

      br.close();
      
      return sbf.toString();
    }catch (Exception e){
      e.printStackTrace();
    }
    
    return null;
  }

  public static String getContentCharSet(final HttpEntity entity)
    throws ParseException {

    if (entity == null) {
      throw new IllegalArgumentException("HTTP entity may not be null");
    }
    String charset = null;
    if (entity.getContentType() != null) {
      HeaderElement values[] = entity.getContentType().getElements();
      if (values.length > 0) {
        NameValuePair param = values[0].getParameterByName("charset" );
        if (param != null) {
          charset = param.getValue();
        }
      }
    }

    if(StringUtils.isEmpty(charset)){
      charset = "UTF-8";
    }
    return charset;
  }
}
