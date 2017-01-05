package com.ly.web.yhd;

import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.dp.YHDOrderDataProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yongliu on 11/14/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/14/2016 15:07
 * @Desc      YHD Tuan
 */
public class YHTOrderCase extends YHDBaseOrderCase {

  /**
   * @see  YHDBaseOrderCase#initProperties()
   */
  @Override protected void initProperties() {
    super.initProperties();
    this.useProxy= yhdOrderConfig.getUseIpProxy();
    YHDOrderDataProvider.yhtOrderPath = yhdOrderConfig.getFilesPath();
  }

  @Test(
    priority          = 1,
    dataProvider      = "dp-yhd-t-order",
    dataProviderClass = YHDOrderDataProvider.class
  )
  public void initData(OrderCommand orderInfo) {
    Assert.notNull(orderInfo);
    orderCommandList.add(orderInfo);
  }
  
  @Test(priority = 2)
  private void yhdTDoOrder() {
    String driverType = yhdOrderConfig.getDriverType();
    int total = orderCommandList.size();
    int index = 0;
    List<OrderCommand> failedOrders = new LinkedList<>(); 

    if (logger.isDebugEnabled()) {
      logger.debug("Total order list size:" + orderCommandList.size());
    }

//    Map<String, List<OrderCommand>> provinceOrderMap = orderCommandList.stream().collect(Collectors.groupingBy(OrderCommand::getProvince));
//
//    System.out.println(provinceOrderMap);

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
        String ipProxy = null;
        if(useProxy){
          ipProxy = proxyProcessor.getIpProxy(orderInfo.getProvince());
        }
        
        ////////////// get the ip proxy by province [end]

        ///////////// init the web driver [start]
        initWebDriver(driverType, ipProxy);
        Assert.notNull(driver, "Driver could not be null.");
        ///////////// init the web driver [end]

        index++;

        String indexInfo = "[" + index + "/" + total + "]";

        logger.info(String.format(">>>>>>>>>>>>>>>>>>Ready order for %s>>>>>>>>>>>>>>>", indexInfo));

        boolean loginSuccess = true;//login(orderInfo);

        if (loginSuccess) {
          boolean addedToShoppingCar = Boolean.FALSE;
          boolean canCheckoutOrder = Boolean.FALSE;
          boolean orderSubmitted = Boolean.FALSE;

          for (ItemInfoCommand itemInfo : orderInfo.getItems()) {
            addedToShoppingCar = addToShoppingCar(itemInfo, orderInfo.getUsername(), orderInfo.getPassword());
          }

          if (addedToShoppingCar) {
            logger.info("-----------------------------------------------------");
            logger.info("      Go to Shopping Car to confirm order            ");
            logger.info("-----------------------------------------------------");
            canCheckoutOrder = confirmOrder(orderInfo);
          }

          if (canCheckoutOrder) {
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
          closeWebDriver();

          logger.info("Will delay " + yhdOrderConfig.getMaxDelaySecondsForNext() + " seconds to start next order.");
          delay(yhdOrderConfig.getMaxDelaySecondsForNext());

        }
      }catch (Exception e){
        logger.error(e.getMessage(), e);
        logger.info("Added Order Info to failed list: " + orderInfo);
        failedOrders.add(orderInfo);
      }
    }
    
    
    if(!failedOrders.isEmpty()){
      logger.info("Ready process failed order.....");
      orderCommandList.clear();
      orderCommandList.addAll(failedOrders);
      failedOrders.clear();
      yhdTDoOrder();
    }
  } // end method yhdTDoOrder
  
  private boolean addToShoppingCar(ItemInfoCommand itemInfo, String username, String password){
    YHTEngine yhtEngine = new YHTEngine(driver);
    yhtEngine.setVoiceFilePath(yhdOrderConfig.getWarningVoiceFile());
    return yhtEngine.addToShoppingCar(itemInfo, username, password);
  }

} // end class YHTOrderCase
