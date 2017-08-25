package com.ly.web.utils;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ly.utils.URLUtils;

import com.ly.web.command.OrderCategory;
import com.ly.web.command.OrderCommand;
import com.ly.web.command.OrderResultInfo;
import com.ly.web.writer.OrderWriter;


/**
 * Created by yongliu on 8/24/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/24/2017 13:08
 */
public class Utils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  private static final Log logger = LogFactory.getLog(Utils.class);

  public static final String ORDER_NO_KEY = "orderId";

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for order no from url.
   *
   * @param   paymentUrl  String
   *
   * @return  String
   */
  public static String getOrderNoFromUrl(String paymentUrl) {
    String              orderNo = null;
    Map<String, String> paraMap = URLUtils.getParamsFromUrl(paymentUrl);

    if ((paraMap != null) && paraMap.containsKey(ORDER_NO_KEY)) {
      orderNo = paraMap.get(ORDER_NO_KEY);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("The OrderNo is: " + orderNo);
    }

    return orderNo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * writeJDOrderInfoToFile.
   *
   * @param  orderWriter      OrderWriter
   * @param  orderResultInfo  OrderResultInfo
   */
  public static synchronized void writeJDOrderInfoToFile(OrderWriter orderWriter, OrderResultInfo orderResultInfo) {
    if(orderWriter != null){
      logger.info("-----------------------------------------------------");
      logger.info("           <Write Order Info to File>                ");
      logger.info("-----------------------------------------------------");
      orderWriter.writeOrderInfo2(OrderCategory.JD, orderResultInfo, Boolean.TRUE);
    } else {
      logger.error("Could not save orderInfo to file, orderWriter is NULL.");
    }
  }

} // end class Utils
