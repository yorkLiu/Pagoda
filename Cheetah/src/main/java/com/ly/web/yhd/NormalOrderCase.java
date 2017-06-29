package com.ly.web.yhd;

import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.dp.YHDOrderDataProvider;
import com.ly.web.exception.PageNotLoadedException;
import com.ly.web.exception.SearchException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by yongliu on 10/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/09/2016 15:32
 */
public class NormalOrderCase extends YHDBaseOrderCase {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  @Override protected void initProperties() {
    super.initProperties();
    this.useProxy = yhdOrderConfig.getUseIpProxy();
    YHDOrderDataProvider.normalOrderPath = yhdOrderConfig.getFilesPath();
  }

  /**
   * initData.
   *
   * @param  orderInfo  OrderCommand
   */
  @Test(
    priority          = 1,
    dataProvider      = "dp-yhd-normal-order",
    dataProviderClass = YHDOrderDataProvider.class
  )
  public void initData(OrderCommand orderInfo) {
    Assert.notNull(orderInfo);
    orderCommandList.add(orderInfo);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean addToShoppingCar(ItemInfoCommand itemInfo, OrderCommand orderInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>3. Ready add to shopping car >>>>>>>>>>>>");
    }

    AddProductionToShoppingCar shoppingCar = new AddProductionToShoppingCar(driver);
    shoppingCar.setBrowserTime(yhdOrderConfig.getBrowserTime());

    return shoppingCar.addToShoppingCar(itemInfo, orderInfo.getUsername(), orderInfo.getPassword());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean search(OrderCommand orderInfo, ItemInfoCommand itemInfo) throws PageNotLoadedException, SearchException {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>2. Search by keyword: " + itemInfo.getKeyword() + ">>>>>>>>>>>>");
    }

    SearchEngine searchEngine = new SearchEngine(driver);
    searchEngine.setCompareProductionCount(yhdOrderConfig.getCompareGoodsCount());
    searchEngine.setMaxSearchPageNum(yhdOrderConfig.getMaxSearchPageNum());
    searchEngine.setPriceOffsets(yhdOrderConfig.getPriceOffsets());

    return searchEngine.search(itemInfo, orderInfo);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  @Test(priority = 2)
  private void yhdOrder() {
    String driverType = yhdOrderConfig.getDriverType();
    int total = orderCommandList.size();
    int index = 0;
    List<OrderCommand> failedOrders = new LinkedList<>();
    
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
        String ipProxy = null;
        if(useProxy){
//          ipProxy = proxyProcessor.getIpProxy(orderInfo.getProvince());
          ipProxy = pagodaProxyProcessor.getIpProxy(orderInfo.getProvince());
        }
        ////////////// get the ip proxy by province [end]


        ///////////// init the web driver [start]
        initWebDriver(driverType, ipProxy);
        Assert.notNull(driver, "Driver could not be null.");
        ///////////// init the web driver [end]

        index++;

        String indexInfo = "[" + index + "/" + total + "]";

        logger.info(String.format(">>>>>>>>>>>>>>>>>>Ready order for %s>>>>>>>>>>>>>>>", indexInfo));


        // 1. login
        // 2. search by keyword
        // 3. find the production by sku in search result
        // 4. browse the production
        // 5. add it to shopping car

        boolean loginSuccess = true;//login(orderInfo);

        if (loginSuccess) {
          boolean addedToShoppingCar = Boolean.FALSE;
          boolean canCheckoutOrder = Boolean.FALSE;
          boolean orderSubmitted = Boolean.FALSE;

          for (ItemInfoCommand itemInfo : orderInfo.getItems()) {
            boolean founded = search(orderInfo, itemInfo);

            if (founded) {
              // Add it to shopping car
              addedToShoppingCar = addToShoppingCar(itemInfo, orderInfo);
            }
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
          
        } // end if
      }catch (Exception e){
        logger.error(e.getMessage(), e);
        logger.info("Added Order Info to failed list: " + orderInfo);
        failedOrders.add(orderInfo);
      }
      
    }   // end for

    if(!failedOrders.isEmpty()){
      logger.info("Ready process failed orders["+failedOrders.size()+"].....");
      orderCommandList.clear();
      orderCommandList.addAll(failedOrders);
      failedOrders.clear();
      yhdOrder();
    }
  }     // end method testOrder
} // end class NormalOrderCase
