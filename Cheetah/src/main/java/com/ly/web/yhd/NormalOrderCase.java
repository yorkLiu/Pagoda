package com.ly.web.yhd;

import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.dp.YHDOrderDataProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;


/**
 * Created by yongliu on 10/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/09/2016 15:32
 */
public class NormalOrderCase extends BaseOrderCase {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  @Override protected void initProperties() {
    super.initProperties();
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

  private boolean addToShoppingCar(ItemInfoCommand itemInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>3. Ready add to shopping car >>>>>>>>>>>>");
    }

    AddProductionToShoppingCar shoppingCar = new AddProductionToShoppingCar(driver);
    shoppingCar.setBrowserTime(yhdOrderConfig.getBrowserTime());

    return shoppingCar.addToShoppingCar(itemInfo);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean search(ItemInfoCommand itemInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>2. Search by keyword: " + itemInfo.getKeyword() + ">>>>>>>>>>>>");
    }

    SearchEngine searchEngine = new SearchEngine(driver);
    searchEngine.setCompareProductionCount(yhdOrderConfig.getCompareGoodsCount());

    return searchEngine.search(itemInfo);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  @Test(priority = 2)
  private void testOrder() {
    
    int total = orderCommandList.size();
    int index = 0;
    if (logger.isDebugEnabled()) {
      logger.debug("Total order list size:" + orderCommandList.size());
    }
    
    for (OrderCommand orderInfo : orderCommandList) {
      if ((orderInfo.getUsername() == null) || !StringUtils.hasText(orderInfo.getUsername())) {
        if (logger.isDebugEnabled()) {
          logger.debug("This record username is NULL, skip it.");
        }

        continue;
      }

      index++;

      String indexInfo = "[" + index + "/" + total + "]";
      
      logger.info(String.format(">>>>>>>>>>>>>>>>>>Ready order for %s>>>>>>>>>>>>>>>", indexInfo));
      
      
      // 1. login
      // 2. search by keyword
      // 3. find the production by sku in search result
      // 4. browse the production
      // 5. add it to shopping car

      boolean loginSuccess = login(orderInfo);

      if (loginSuccess) {
        boolean addedToShoppingCar = Boolean.FALSE;
        boolean canCheckoutOrder   = Boolean.FALSE;
        boolean orderSubmitted     = Boolean.FALSE;

        for (ItemInfoCommand itemInfo : orderInfo.getItems()) {
          boolean founded = search(itemInfo);

          if (founded) {
            // Add it to shopping car
            addedToShoppingCar = addToShoppingCar(itemInfo);
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

      } // end if
    }   // end for
  }     // end method testOrder
} // end class NormalOrderCase
