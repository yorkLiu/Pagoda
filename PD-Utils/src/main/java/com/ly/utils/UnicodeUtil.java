package com.ly.utils;

/**
 * Created by yongliu on 7/5/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/05/2016 10:37
 */
public class UnicodeUtil {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final char[] hexChar = {
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
    'A',
    'B',
    'C',
    'D',
    'E',
    'F'
  };

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * main.
   *
   * @param  args  String[]
   */
  public static void main(String[] args) {
    String str = "你好, Hello, 世界, World!";

    String unicode = string2Unicode(str);
    System.out.println(unicode);

    System.out.println(unicode2String(unicode));

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * string2Unicode.
   *
   * @param   string  String
   *
   * @return  String
   */
  public static String string2Unicode(String string) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);

      if ((c >> 7) > 0) {
        sb.append("\\u");
        sb.append(hexChar[(c >> 12) & 0xF]); // append the hex character for the left-most 4-bits
        sb.append(hexChar[(c >> 8) & 0xF]);  // hex for the second group of 4-bits from the left
        sb.append(hexChar[(c >> 4) & 0xF]);  // hex for the third group
        sb.append(hexChar[c & 0xF]);         // hex for the last group, e.g., the right most 4-bits
      } else {
        sb.append(c);
      }
    }

    return sb.toString();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * unicode2String.
   *
   * @param   unicodeString  String
   *
   * @return  String
   */
  public static String unicode2String(String unicodeString) {
    StringBuilder sb  = new StringBuilder();
    String[]      hex = unicodeString.split("\\\\u");

    for (int i = 1; i < hex.length; i++) {
      String str     = hex[i];
      String tempStr = str;
      boolean flag    = tempStr.length() > 4;

      if (flag) {
        tempStr = tempStr.substring(0, 4);
        str     = str.substring(4);
      }

      int unicodeData = Integer.parseInt(tempStr, 16);
      sb.append((char) unicodeData);

      if (flag) {
        sb.append(str);
      }
    }

    return sb.toString();

  } // end method unicode2String


} // end class UnicodeUtil
