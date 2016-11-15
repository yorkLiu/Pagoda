package com.ly.web.yhd;

import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.dp.YHDOrderDataProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

/**
 * Created by yongliu on 11/14/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/14/2016 15:07
 * @Desc      YHD Tuan
 */
public class YHTOrderCase extends BaseOrderCase {



  /**
   * @see  com.ly.web.yhd.BaseOrderCase#initProperties()
   */
  @Override protected void initProperties() {
    super.initProperties();
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

      boolean loginSuccess = login(orderInfo);

      if (loginSuccess) {
        boolean addedToShoppingCar = Boolean.FALSE;
        boolean canCheckoutOrder = Boolean.FALSE;
        boolean orderSubmitted = Boolean.FALSE;

        for (ItemInfoCommand itemInfo : orderInfo.getItems()) {
          addedToShoppingCar = addToShoppingCar(itemInfo);
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
      }
    }
  } // end method yhdTDoOrder
  
  private boolean addToShoppingCar(ItemInfoCommand itemInfo){
    YHTEngine yhtEngine = new YHTEngine(driver);
    return yhtEngine.addToShoppingCar(itemInfo);
  }
  
} // end class YHTOrderCase
