package com.ly.web.jd;

import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.ObserverDriver;
import com.ly.web.command.OrderCommand;
import com.ly.web.common.OrderObserver;
import com.ly.web.dp.JDOrderDataProvider;
import com.ly.web.exception.PageNotLoadedException;
import com.ly.web.exception.SearchException;
import com.ly.web.exceptions.OrderAddToShoppingCarFailedException;
import com.ly.web.exceptions.OrderCheckoutFailedException;
import com.ly.web.exceptions.OrderConfirmFailedException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yongliu on 8/15/17.
 */
public class JDOrderCase extends JDBaseOrderCase {
  
  private OrderObserver jdOrderObserver;
  private Thread observerThread=null;

  @Override protected void initProperties() {
    try {
      super.initProperties();
      this.useProxy = jdOrderConfig.getUseIpProxy();
      JDOrderDataProvider.jdOrderPath = jdOrderConfig.getFilesPath();
    }catch (Exception e){
      e.printStackTrace();
    }
  }


  @Test(
    priority          = 1,
    dataProvider      = "dp-jd-normal-order",
    dataProviderClass = JDOrderDataProvider.class
  )
  public void initData(OrderCommand orderInfo) {
    Assert.notNull(orderInfo);
    orderCommandList.add(orderInfo);
  }


  @Test(priority = 2)
  private void jdOrder() {
    String driverType = jdOrderConfig.getDriverType();
    int index = 0;
    List<OrderCommand> failedOrders = new LinkedList<>();

    Integer[] rangeIndexes = jdOrderConfig.getRanges();
     
    if(rangeIndexes != null){
      int startIndex = rangeIndexes[0];
      int endIndex = rangeIndexes[1] != null ? rangeIndexes[1]: orderCommandList.size();
      startIndex = Math.max(0, startIndex-1);
      endIndex = Math.min(endIndex, orderCommandList.size());
      
      logger.info("rangeIndexes: [" + startIndex + ", " + endIndex + "]");
      orderCommandList = orderCommandList.subList(startIndex, endIndex);
    }
    
    int total = orderCommandList.size();

    if (logger.isDebugEnabled()) {
      logger.debug("Total order list size:" + orderCommandList.size());
    }

    for (OrderCommand orderInfo : orderCommandList) {
      try {
        if ((orderInfo.getUsername() == null) || !StringUtils.hasText(orderInfo.getUsername())) {
          if (logger.isDebugEnabled()) {
            logger.debug("This record username is NULL, skip it.");
          }

          continue;
        }

        ////////////// get the ip proxy by province [start]
        // find the ip proxy by province
//        String ipProxy = null;
//        if (useProxy) {
//          ipProxy = pagodaProxyProcessor.getIpProxy(orderInfo.getProvince());
//        }

        ////////////// get the ip proxy by province [end]

        ///////////// init the web driver [start]
//        initWebDriver(driverType, ipProxy);
//        Assert.notNull(driver, "Driver could not be null.");
        ///////////// init the web driver [end]

        index++;

        String indexInfo = "[" + index + "/" + total + "]";

        logger.info(String.format(">>>>>>>>>>>>>>>>>>Ready order for %s>>>>>>>>>>>>>>>", indexInfo));

//        boolean loginSuccess = true;//login(orderInfo);
//        boolean loginSuccess = login(orderInfo);
        boolean loginSuccess = login(orderInfo, driverType);
        
        if (loginSuccess){
          boolean addedToShoppingCar = Boolean.FALSE;
          boolean canCheckoutOrder = Boolean.FALSE;
          boolean orderSubmitted = Boolean.FALSE;
          for (ItemInfoCommand itemInfo : orderInfo.getItems()) {
            boolean founded = search(orderInfo, itemInfo);
            
            if(founded){
              addedToShoppingCar = addToShoppingCar(itemInfo, orderInfo);
            }
          }
          
          if (addedToShoppingCar){
            logger.info("-----------------------------------------------------");
            logger.info("      Go to Shopping Car to confirm order            ");
            logger.info("-----------------------------------------------------");
            canCheckoutOrder = confirmOrder(orderInfo);
          }
          
          if (canCheckoutOrder){
            logger.info("-----------------------------------------------------");
            logger.info("            Go to Check out order                    ");
            logger.info("-----------------------------------------------------");
            orderSubmitted = checkoutOrder(orderInfo);
          }

          if (orderSubmitted) {
            logger.info("-----------------------------------------------------");
            logger.info("            Order Submitted, Write Order Info          ");
            logger.info("-----------------------------------------------------");
            writeOrderInfo(orderInfo);
            logger.info(String.format(">>>>>>>>>>>>>The order index %s successfully!", indexInfo));
          }

          //////////////// close the web driver
//          closeWebDriver();

          if(index < orderCommandList.size()){
            logger.info("Will delay " + jdOrderConfig.getMaxDelaySecondsForNext() + " seconds to start next order.");
            delay(jdOrderConfig.getMaxDelaySecondsForNext());
          }
          
        } // end if
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        logger.info("Added Order Info to failed list: " + orderInfo);
        failedOrders.add(orderInfo);

        // add this order to observer.
        addToObserver(orderInfo);
      }
    }

    if(!failedOrders.isEmpty()){
      logger.info("Ready process failed orders["+failedOrders.size()+"].....");
      orderCommandList.clear();
      orderCommandList.addAll(failedOrders);
      failedOrders.clear();
      jdOrder();
    }
    
    // stop the observer thread
    stopObserver();
  }
  
  private void addToObserver(OrderCommand orderInfo){
    try {
      logger.info("Starting observer order......");
      if (jdOrderObserver == null) {
        jdOrderObserver = new OrderObserver();
        jdOrderObserver.setOrderWriter(orderWriter);
      }

      jdOrderObserver.addObserverOrder(new ObserverDriver(driver, orderInfo));

      if (observerThread == null) {
        observerThread = new Thread(jdOrderObserver);
      }

      if (!observerThread.isAlive()) {
        observerThread.start();
      }
    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }
    
  }
  
  private void stopObserver(){
    if(observerThread != null){
      observerThread.interrupt();
    }
  }
  
  private boolean login(OrderCommand orderInfo, String driverType){
    boolean loginSuccess = Boolean.FALSE;
    String ipProxy = null;
    try{
      ////////////// get the ip proxy by province [start]
      // find the ip proxy by province
//      String ipProxy = null;
      if (useProxy) {
        ipProxy = pagodaProxyProcessor.getIpProxy(orderInfo.getProvince());
      }

      ////////////// get the ip proxy by province [end]

      ///////////// init the web driver [start]
      initWebDriver(driverType, ipProxy);
      Assert.notNull(driver, "Driver could not be null.");
      ///////////// init the web driver [end]

      loginSuccess = login(orderInfo);
      
      orderInfo.setUsedIpProxy(ipProxy);
      
    }catch (Exception e){
      logger.warn("The ip Proxy["+ipProxy+"] was too slowly, change a new ip proxy.");
      closeWebDriver();
      return login(orderInfo, driverType);
    }
    
    return loginSuccess;
  }

  private boolean search(OrderCommand orderInfo, ItemInfoCommand itemInfo) throws PageNotLoadedException, SearchException {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>2. Search by keyword: " + itemInfo.getKeyword() + ">>>>>>>>>>>>");
    }

    JDSearch searchEngine = new JDSearch(driver);
    searchEngine.setCompareProductionCount(jdOrderConfig.getCompareGoodsCount());
    searchEngine.setMaxSearchPageNum(jdOrderConfig.getMaxSearchPageNum());
    searchEngine.setPriceOffsets(jdOrderConfig.getPriceOffsets());

    return searchEngine.search(itemInfo, orderInfo);
  }

  private boolean addToShoppingCar(ItemInfoCommand itemInfo, OrderCommand orderInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>3. Ready add to shopping car >>>>>>>>>>>>");
    }

    ShoppingCar shoppingCar = new ShoppingCar(driver);
    shoppingCar.setBrowserTime(jdOrderConfig.getBrowserTime());

    Boolean addedShoppingCar = shoppingCar.addToShoppingCar(itemInfo, orderInfo.getUsername(), orderInfo.getPassword());
    if(!addedShoppingCar){
      throw new OrderAddToShoppingCarFailedException("Order add to shopping car failed exception [" + orderInfo.getUsername() + "]");
    }
    
    return addedShoppingCar;
  }


  protected boolean confirmOrder(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>4. Ready confirm order >>>>>>>>>>>");
    }

    ConfirmOrder confirmOrder = new ConfirmOrder(driver);

    Boolean confirmed = confirmOrder.confirmOrder(orderCommand.getItems(), orderCommand.getUsername(), orderCommand.getPassword());
    if(!confirmed){
      throw new OrderConfirmFailedException("Order confirm failed exception [" + orderCommand.getUsername() + "]");
    }
    
    return confirmed;
  }

  protected boolean checkoutOrder(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>5. Ready checkout order >>>>>>>>>>>");
    }

    JDCheckoutOrder checkoutOrder = new JDCheckoutOrder(driver);

    Boolean checkedOut = checkoutOrder.checkout(orderCommand);
    if(!checkedOut){
      throw new OrderCheckoutFailedException("Order check out failed exception [" + orderCommand.getUsername() + "]");
    }
    return checkedOut;
  }

  protected boolean writeOrderInfo(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>6. Order was submitted, ready write order info >>>>>>>>>>>");
    }

    JDPaymentOrder paymentOrder = new JDPaymentOrder(driver);
    paymentOrder.setOrderWriter(orderWriter);

    return paymentOrder.writeOrderInfo(orderCommand);
  }
  
}
