package com.ly.web.jd;

import java.util.Map;

import com.ly.web.utils.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.util.Assert;

import com.ly.utils.URLUtils;

import com.ly.web.base.AbstractObject;
import com.ly.web.command.OrderCategory;
import com.ly.web.command.OrderCommand;
import com.ly.web.command.OrderResultInfo;
import com.ly.web.constant.Constant;
import com.ly.web.writer.OrderWriter;


/**
 * Created by yongliu on 8/17/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/17/2017 16:30
 */
public class JDPaymentOrder extends AbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String ORDER_NO_KEY = "orderId";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private OrderWriter orderWriter;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new JDPaymentOrder object.
   */
  public JDPaymentOrder() { }

  /**
   * Creates a new JDPaymentOrder object.
   *
   * @param  webDriver  WebDriver
   */
  public JDPaymentOrder(WebDriver webDriver) {
    Assert.notNull(webDriver);
    this.webDriver = webDriver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * getter method for order writer.
   *
   * @return  OrderWriter
   */
  public OrderWriter getOrderWriter() {
    return orderWriter;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order writer.
   *
   * @param  orderWriter  OrderWriter
   */
  public void setOrderWriter(OrderWriter orderWriter) {
    this.orderWriter = orderWriter;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * writeOrderInfo.
   *
   * @param   orderInfo  OrderCommand
   *
   * @return  boolean
   */
  public boolean writeOrderInfo(OrderCommand orderInfo) {
    if (!webDriver.getCurrentUrl().contains(Constant.JD_PAYMENT_ORDER_PAGE_URL_PREFIX)) {
      logger.warn("The payment page not loaded, the current tab url is: " + webDriver.getCurrentUrl());
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("The page page was loaded and the url is: " + webDriver.getCurrentUrl());
      }
    }

    try {
      Boolean paymentPageLoaded = (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
            @Override public Boolean apply(WebDriver d) {
              return (d.getCurrentUrl().contains(Constant.JD_PAYMENT_ORDER_PAGE_URL_PREFIX));
            }
          });

      if (paymentPageLoaded) {
        String paymentUrl = webDriver.getCurrentUrl();
        String orderNo    = Utils.getOrderNoFromUrl(paymentUrl);

        OrderResultInfo orderResultInfo = new OrderResultInfo(orderInfo);
        orderResultInfo.setPaymentUrl(paymentUrl);
        orderResultInfo.setOrderNo(orderNo);

        if (logger.isDebugEnabled()) {
          logger.debug(orderResultInfo);
        }
        
        Utils.writeJDOrderInfoToFile(orderWriter, orderResultInfo);
//        orderWriter.writeOrderInfo2(OrderCategory.JD, orderResultInfo, Boolean.TRUE);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return Boolean.TRUE;
  } // end method writeOrderInfo
} // end class JDPaymentOrder
