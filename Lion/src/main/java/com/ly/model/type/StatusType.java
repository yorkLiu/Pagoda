package com.ly.model.type;

/**
 * Created by yongliu on 8/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/31/2016 11:23
 */
public enum StatusType {
  //~ Enum constants ---------------------------------------------------------------------------------------------------

  ENABLE, DISABLED, DELETED, LOCKED, PUBLISHED, RETIRED;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * convert.
   *
   * @param   statusType  String
   *
   * @return  StatusType
   */
  public static StatusType convert(String statusType) {
    if ((statusType == null) || statusType.trim().isEmpty()) {
      return null;
    }

    return valueOf(statusType);
  }

}
