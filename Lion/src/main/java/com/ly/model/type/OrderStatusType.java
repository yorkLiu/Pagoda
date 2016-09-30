package com.ly.model.type;

import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/30/2016 11:21
 */
public enum OrderStatusType {
  //~ Enum constants ---------------------------------------------------------------------------------------------------

  INIT, PENDING, IN_PROGRESS, CANCELLED, COMPLETED, DELETED;

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * convert.
   *
   * @param   name  String
   *
   * @return  OrderStatusType
   */
  public static OrderStatusType convert(String name) {
    if ((name != null) && StringUtils.hasText(name)) {
      return valueOf(name.toUpperCase());
    }

    return null;
  }

}
