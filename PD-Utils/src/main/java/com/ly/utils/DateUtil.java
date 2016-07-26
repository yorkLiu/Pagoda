package com.ly.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 7/26/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/26/2016 11:46
 */
public class DateUtil {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** default date formats. */
  private static List<String> dateFormats = Arrays.asList(
      "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss",
      "MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy'T'HH:mm:ss", "yyyy-MM-dd",
      "MM/dd/yyyy");

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * toDate.
   *
   * @param   dataString  String
   *
   * @return  Date
   */
  public static Date toDate(String dataString) {
    if (StringUtils.hasText(dataString)) {
      for (String dateFormat : dateFormats) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        try {
          Date date = sdf.parse(trim(dataString));

          return date;
        } catch (ParseException e) {
          // not match for this pattern, try next one
        }
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * trim.
   *
   * @param   input  String
   *
   * @return  String
   */
  public static String trim(String input) {
    if (input != null) {
      String output = input.trim();
      output = output.replaceAll("^\"*\'*\"*|\"*\'*\"*$", "");

      return output;
    }

    return null;
  }

} // end class DateUtil