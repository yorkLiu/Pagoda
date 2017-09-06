package com.ly.web.command;

import java.util.List;

import org.openqa.selenium.WebDriver;


/**
 * Created by yongliu on 8/23/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/23/2017 17:09
 */
public class ObserverDriver {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private List<OrderCommand> failedOrders = null;

  private OrderCommand orderInfo;

  private WebDriver webDriver;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ObserverDriver object.
   */
  public ObserverDriver() { }

  /**
   * Creates a new ObserverDriver object.
   *
   * @param  webDriver  WebDriver
   * @param  orderInfo  OrderCommand
   */
  public ObserverDriver(WebDriver webDriver, OrderCommand orderInfo) {
    this.webDriver = webDriver;
    this.orderInfo = orderInfo;
  } 
  
  public ObserverDriver(WebDriver webDriver, OrderCommand orderInfo, List<OrderCommand> failedOrders) {
    this.webDriver = webDriver;
    this.orderInfo = orderInfo;
    this.failedOrders = failedOrders;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#equals(java.lang.Object)
   */
  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    ObserverDriver that = (ObserverDriver) o;

    if ((webDriver != null) ? (!webDriver.equals(that.webDriver)) : (that.webDriver != null)) {
      return false;
    }

    return (orderInfo != null) ? orderInfo.equals(that.orderInfo) : (that.orderInfo == null);

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for failed orders.
   *
   * @return  List
   */
  public List<OrderCommand> getFailedOrders() {
    return failedOrders;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for order info.
   *
   * @return  OrderCommand
   */
  public OrderCommand getOrderInfo() {
    return orderInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for web driver.
   *
   * @return  WebDriver
   */
  public WebDriver getWebDriver() {
    return webDriver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (webDriver != null) ? webDriver.hashCode() : 0;
    result = (31 * result) + ((orderInfo != null) ? orderInfo.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for failed orders.
   *
   * @param  failedOrders  List
   */
  public void setFailedOrders(List<OrderCommand> failedOrders) {
    this.failedOrders = failedOrders;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order info.
   *
   * @param  orderInfo  OrderCommand
   */
  public void setOrderInfo(OrderCommand orderInfo) {
    this.orderInfo = orderInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for web driver.
   *
   * @param  webDriver  WebDriver
   */
  public void setWebDriver(WebDriver webDriver) {
    this.webDriver = webDriver;
  }
} // end class ObserverDriver
