package com.ly.utils;

import java.util.concurrent.TimeUnit;


/**
 * Created by yongliu on 11/4/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/04/2016 09:46
 */
public class TimeUtils {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * toTime.
   *
   * @param   milliseconds  long
   *
   * @return  String
   */
  public static String toTime(long milliseconds) {
    if (milliseconds > 0) {
      String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
          TimeUnit.MILLISECONDS.toMinutes(milliseconds)
          - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
          TimeUnit.MILLISECONDS.toSeconds(milliseconds)
          - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

      return hms;
    }

    return "00:00:00";
  }

} // end class TimeUtils
