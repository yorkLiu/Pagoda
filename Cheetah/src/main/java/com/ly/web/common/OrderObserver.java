package com.ly.web.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.WebDriver;

import com.ly.web.command.ObserverDriver;
import com.ly.web.command.OrderCommand;
import com.ly.web.command.OrderResultInfo;
import com.ly.web.utils.Utils;
import com.ly.web.writer.OrderWriter;


/**
 * Created by yongliu on 8/23/17.
 *
 * @author       <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version      08/24/2017 13:16
 * @Description  Observer the the webDriver which was give up by selenium
 */

public class OrderObserver implements Runnable {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private final Log logger = LogFactory.getLog(getClass());

  private List<ObserverDriver> observerDrivers = new ArrayList<>();

  private OrderWriter orderWriter;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * addObserverOrder.
   *
   * @param  observerDriver  ObserverDriver
   */
  public void addObserverOrder(ObserverDriver observerDriver) {
    observerDrivers.add(observerDriver);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * @see  java.lang.Runnable#run()
   */
  @Override public void run() {
    while (observerDrivers.size() > 0) {
      logger.info("observerDrivers.size() in while: " + observerDrivers.size());

      if (observerDrivers.size() > 0) {
        for (ObserverDriver observerDriver : observerDrivers) {
          WebDriver webDriver = observerDriver.getWebDriver();

          try {
            if ((webDriver != null) && !webDriver.toString().contains("null") && (webDriver.getCurrentUrl() != null)) {
              checkIsOnPaymentPage(observerDriver);

            } else {
              removeObserver(observerDriver);
            }
          } catch (Exception e) {
            removeObserver(observerDriver);
          }
        }
      } else {
        break;
      }

      if (observerDrivers.size() > 0) {
        try {
          // wait 10 seconds
          Thread.sleep(1000 * 10);
        } catch (Exception e) { }
      }
    } // end while

    logger.info("Stop current thread: " + Thread.currentThread().getName());
    Thread.currentThread().interrupt();
  } // end method run

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

  private void checkIsOnPaymentPage(ObserverDriver observerDriver) throws Exception {
    WebDriver    webDriver = observerDriver.getWebDriver();
    OrderCommand orderInfo = observerDriver.getOrderInfo();

    try {
      String currentPageUrl = webDriver.getCurrentUrl();
      logger.info("Observer - currentPageUrl: " + currentPageUrl);

      if (currentPageUrl.contains(Utils.ORDER_NO_KEY)) {
        String          orderNo         = Utils.getOrderNoFromUrl(currentPageUrl);
        OrderResultInfo orderResultInfo = new OrderResultInfo(orderInfo);
        orderResultInfo.setPaymentUrl(currentPageUrl);
        orderResultInfo.setOrderNo(orderNo);

        logger.info(
          "Observer - Found the orderNo not save to file (because this webdriver was out scope), will save it.");

        if (logger.isDebugEnabled()) {
          logger.debug(orderResultInfo);
        }

        Utils.writeJDOrderInfoToFile(getOrderWriter(), orderResultInfo);

        if (observerDriver.getFailedOrders() != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Remove observer order from failed orders: [" + orderInfo.getUsername() + " - "
              + orderInfo.getStoreName() + "]");
          }

          observerDriver.getFailedOrders().remove(orderInfo);
        }

        removeObserver(observerDriver);
      } // end if
    } catch (Exception e) {
      throw e;
    } // end try-catch
  }   // end method checkIsOnPaymentPage

  //~ ------------------------------------------------------------------------------------------------------------------

  private void removeObserver(ObserverDriver observerDriver) {
    if (observerDriver != null) {
      observerDrivers.remove(observerDriver);
      logger.info("Removed one observer.... ");
    }
  }
} // end class OrderObserver
