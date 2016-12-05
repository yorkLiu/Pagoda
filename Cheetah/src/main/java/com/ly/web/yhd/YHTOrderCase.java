package com.ly.web.yhd;

import com.ly.proxy.ProxyProcessor;
import com.ly.web.command.AddressInfoCommand;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.dp.YHDOrderDataProvider;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yongliu on 11/14/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/14/2016 15:07
 * @Desc      YHD Tuan
 */
public class YHTOrderCase extends BaseOrderCase {


  private ProxyProcessor proxyProcessor;

  /**
   * @see  com.ly.web.yhd.BaseOrderCase#initProperties()
   */
  @Override protected void initProperties() {
    super.initProperties();
    this.useProxy=Boolean.TRUE;
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

    if (logger.isDebugEnabled()) {
      logger.debug("Total order list size:" + orderCommandList.size());
    }

//    Map<String, List<OrderCommand>> provinceOrderMap = orderCommandList.stream().collect(Collectors.groupingBy(OrderCommand::getProvince));
//
//    System.out.println(provinceOrderMap);

    for (OrderCommand orderInfo : orderCommandList) {
      if ((orderInfo.getUsername() == null) || !StringUtils.hasText(orderInfo.getUsername())) {
        if (logger.isDebugEnabled()) {
          logger.debug("This record username is NULL, skip it.");
        }

        continue;
      }
      
      ////////////// get the ip proxy by province [start]
      // find the ip proxy by province
      String ipProxy = proxyProcessor.getIpProxy(orderInfo.getAddressInfo().getProvince());
      ////////////// get the ip proxy by province [end]
      
      ///////////// init the web driver [start]
      initWebDriver(driverType, ipProxy);
      Assert.notNull(driver);
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
    }
  } // end method yhdTDoOrder
  
  private boolean addToShoppingCar(ItemInfoCommand itemInfo, String username, String password){
    YHTEngine yhtEngine = new YHTEngine(driver);
    yhtEngine.setVoiceFilePath(yhdOrderConfig.getWarningVoiceFile());
    return yhtEngine.addToShoppingCar(itemInfo, username, password);
  }


  public void setProxyProcessor(ProxyProcessor proxyProcessor) {
    this.proxyProcessor = proxyProcessor;
  }
} // end class YHTOrderCase
