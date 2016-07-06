package com.ly.capture;

import java.io.Serializable;


/**
 * Created by yongliu on 7/6/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/06/2016 14:02
 * @URL http://www.chaojiying.com/price.html
 */
public enum CaptureType {
  //~ Enum constants ---------------------------------------------------------------------------------------------------

  /**4位英文数字*/
  CAPTURE_TYPE_1104("1104"),

  /**1~4位英文数字*/
  CAPTURE_TYPE_1004("1004"),

  /**1~5位英文数字*/
  CAPTURE_TYPE_1005("1005"),

  /**1~6位英文数字*/
  CAPTURE_TYPE_1006("1006"),

  /**1~7位英文数字*/
  CAPTURE_TYPE_1007("1007"),

  /**1~8位英文数字*/
  CAPTURE_TYPE_1008("1008"),

  /**1~9位英文数字*/
  CAPTURE_TYPE_1009("1009"),

  /**1~10位英文数字*/
  CAPTURE_TYPE_1010("1010"),

  /**1~11位英文数字*/
  CAPTURE_TYPE_1011("1011"),

  /**1~12位英文数字*/
  CAPTURE_TYPE_1012("1012");

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String strValue;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Define enum.
   */
  private CaptureType() {
    this.strValue = this.name();
  }

  /**
   * Define enum with string value.
   *
   * @param  strValue  $param.type$
   */
  private CaptureType(String strValue) {
    this.strValue = strValue;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Get the string value for the enum.
   *
   * @return  enum string value
   */
  @Override public String toString() {
    return strValue;
  }
}
