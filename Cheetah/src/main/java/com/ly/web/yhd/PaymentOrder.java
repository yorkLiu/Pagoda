package com.ly.web.yhd;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ly.utils.URLUtils;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.command.OrderCommand;
import com.ly.web.constant.Constant;


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
   * writeOrderInfo.
   *
   * @param  orderInfo  OrderCommand
   */
  public boolean writeOrderInfo(OrderCommand orderInfo) {
    if (!webDriver.getCurrentUrl().contains(Constant.YHD_PAYMENT_ORDER_PAGE_URL_PREFIX)) {
      logger.warn("The payment page not loaded");
    }

    if (loadedPaymentPage()) {
      String paymentUrl = webDriver.getCurrentUrl();
      String orderNo    = getOrderNoFromUrl(paymentUrl);

      logger.info(paymentUrl + "\t\t\t|" +orderNo);
    }
    
    return Boolean.TRUE;

  }

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

  private Boolean loadedPaymentPage() {
    Boolean loaded = Boolean.FALSE;

    try {
      loaded = waitForById(PAYMENT_AMOUNT_FIELD_ID, 10);
    } catch (TimeoutException e) {
      refreshPage();

      delay(3);

      return loadedPaymentPage();
    }

    return loaded;
  }


} // end class PaymentOrder
