package com.ly.web.yhd;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import com.ly.utils.URLUtils;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.command.OrderCategory;
import com.ly.web.command.OrderCommand;
import com.ly.web.command.OrderResultInfo;
import com.ly.web.constant.Constant;
import com.ly.web.writer.OrderWriter;


/**
 * Created by yongliu on 10/31/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/31/2016 21:41
 */
public class PaymentOrder extends YHDAbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String ORDER_NO_KEY = "orderCode";

  private static final String PAYMENT_AMOUNT_FIELD_ID = "payableAmount";

  private static final String PAYMENT_INFO_XPATH = "//div[contains(@class, 'pay_list')]//span[contains(@class, 'txt')]";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private OrderWriter orderWriter;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new PaymentOrder object.
   *
   * @param  webDriver  WebDriver
   */
  public PaymentOrder(WebDriver webDriver) {
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
   * @return  writeOrderInfo.
   */
  public boolean writeOrderInfo(OrderCommand orderInfo) {
    if (!webDriver.getCurrentUrl().contains(Constant.YHD_PAYMENT_ORDER_PAGE_URL_PREFIX)) {
      logger.warn("The payment page not loaded, the current tab url is: " + webDriver.getCurrentUrl());
    } else {
      logger.debug("The page page was loaded and the url is: " + webDriver.getCurrentUrl());
    }

    try {

      Boolean paymentPageLoaded = (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return (d.getCurrentUrl().contains(Constant.YHD_PAYMENT_ORDER_PAGE_URL_PREFIX));
        }
      });
      
//      if (loadedPaymentPage()) {
      if (paymentPageLoaded) {
        String paymentUrl = webDriver.getCurrentUrl();
        String orderNo    = getOrderNoFromUrl(paymentUrl);

        OrderResultInfo orderResultInfo = new OrderResultInfo(orderInfo);
        orderResultInfo.setPaymentUrl(paymentUrl);
        orderResultInfo.setOrderNo(orderNo);

        if (logger.isDebugEnabled()) {
          logger.debug(orderResultInfo);
        }

        orderWriter.writeOrderInfo(OrderCategory.YHD, orderResultInfo, Boolean.TRUE);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return Boolean.TRUE;
  } // end method writeOrderInfo

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getOrderNoFromUrl(String paymentUrl) {
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

  private String getPrice() {
    String price = null;

    try {
      WebElement paymentAmountEle = ExpectedConditions.presenceOfElementLocated(By.id(PAYMENT_AMOUNT_FIELD_ID)).apply(
          webDriver);
      price = paymentAmountEle.getAttribute("value");
    } catch (NoSuchElementException e) {
      logger.error(e.getMessage(), e);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("The order price is: " + price);
    }

    return price;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private Boolean loadedPaymentPage() {
    try {
      return waitForByXPath(PAYMENT_INFO_XPATH, 10);
    } catch (TimeoutException e) {
      refreshPage();

      delay(3);

      return loadedPaymentPage();
    }
  }
} // end class PaymentOrder
