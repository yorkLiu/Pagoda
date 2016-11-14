package com.ly.web.yhd;

import com.ly.config.YHDOrderConfig;
import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.AddressInfoCommand;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.constant.Constant;
import com.ly.web.dp.YHDOrderDataProvider;
import com.ly.web.lyd.Login;
import com.ly.web.writer.OrderWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by yongliu on 10/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/09/2016 15:32
 */
public class NormalOrderNG extends SeleniumBaseObject {

  protected final Log logger = LogFactory.getLog(getClass());
  //~ Instance fields --------------------------------------------------------------------------------------------------
  
  private OrderWriter orderWriter;

  private OrderCommand orderCommand = null;
  private List<OrderCommand> orderCommandList = new LinkedList<>();

  private static final String applicationContext = "applicationConfig.xml";
  private static final String[] YHD_Configs = new String[]{"YHD-ApplicationConfig.xml"};

  @Autowired
  private YHDOrderConfig yhdOrderConfig;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  @BeforeTest
  public void init(){
    ApplicationContext parent = new ClassPathXmlApplicationContext(applicationContext);
    ApplicationContext context = new ClassPathXmlApplicationContext(YHD_Configs, parent);
    context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
      AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    
    this.webDriverProperties = yhdOrderConfig;
    initProperties();
  }
  
  /**
   * setup.
   */
  @BeforeTest public void setup() {
    initWebDriver(yhdOrderConfig.getDriverType());
  } // end method setup

  @Override
  protected void initProperties() {
    super.initProperties();

    YHDOrderDataProvider.normalOrderPath = yhdOrderConfig.getFilesPath();
  }


  @Test(
    priority          = 1,
    dataProvider      = "dp-yhd-normal-order",
    dataProviderClass = YHDOrderDataProvider.class
  )
  public void initData(OrderCommand orderInfo) {
    Assert.notNull(orderInfo);
    orderCommandList.add(orderInfo);
  }
  
  
  @Test(priority = 2)
  private void testOrder(){
//    orderCommand = new OrderCommand();
//    orderCommand.setUsername("13046883033");
//    orderCommand.setPassword("yyy555");
//
//    AddressInfoCommand addressInfo = new AddressInfoCommand();
//    addressInfo.setFullName("贺东");
////    addressInfo.setProvince("河南");
////    addressInfo.setCity("郑州");
////    addressInfo.setCountry("金水区");
////    addressInfo.setFullAddress("河南省郑州市新郑市未来路未来大厦");
//    
//    addressInfo.setProvince("四川");
//    addressInfo.setCity("成都市");
//    addressInfo.setCountry("金牛区");
//    
//    addressInfo.setFullAddress("四川省成都市金牛区 万福广场A1 2305");
//    
//    addressInfo.setTelephoneNum("13877435976");
//
//    ItemInfoCommand itemInfoCommand = new ItemInfoCommand();
//    itemInfoCommand.setSku("48479615");
//    itemInfoCommand.setKeyword("西拉美乐干红葡萄酒");
//
//    orderCommand.setAddressInfo(addressInfo);
//    orderCommand.addItem(itemInfoCommand);

    if (logger.isDebugEnabled()) {
      logger.debug("Total order list size:" + orderCommandList.size());
    }
    for (OrderCommand orderInfo : orderCommandList) {
      // 1. login
      // 2. search by keyword
      // 3. find the production by sku in search result
      // 4. browse the production
      // 5. add it to shopping car

      boolean loginSuccess = login(orderInfo);

      if(loginSuccess){

        boolean addedToShoppingCar = Boolean.FALSE;
        boolean canCheckoutOrder = Boolean.FALSE;
        boolean orderSubmitted   = Boolean.FALSE;

        for (ItemInfoCommand itemInfo : orderInfo.getItems()) {
          boolean founded = search(itemInfo);
          if(founded){
            // Add it to shopping car
            addedToShoppingCar = addToShoppingCar(itemInfo);
          }
        }


        if(addedToShoppingCar){
          logger.info("-----------------------------------------------------");
          logger.info("      Go to Shopping Car to confirm order            ");
          logger.info("-----------------------------------------------------");
          canCheckoutOrder = confirmOrder(orderInfo);
        }

        if(canCheckoutOrder){
          logger.info("-----------------------------------------------------");
          logger.info("            Go to Check out order                    ");
          logger.info("-----------------------------------------------------");
          orderSubmitted = checkoutOrder(orderInfo);
        }

        if(orderSubmitted){
          logger.info("-----------------------------------------------------");
          logger.info("            Order Submitted, Write Order Info          ");
          logger.info("-----------------------------------------------------");
          writeOrderInfo(orderInfo);
        }

      }
    } 
  }

  public void setOrderWriter(OrderWriter orderWriter) {
    this.orderWriter = orderWriter;
  }

  private boolean login(OrderCommand orderCommand) {
    boolean loginSuccess = Boolean.TRUE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Starting login....");
        logger.debug("Open Login Page:" + Constant.YHD_LOGIN_PAGE_URL_WITHOUT_MY_ORDER);
      }

      Login login = new Login(driver, Constant.YHD_LOGIN_PAGE_URL_WITHOUT_MY_ORDER, voiceFilePath);

      loginSuccess = login.login(orderCommand.getUsername(), orderCommand.getPassword(), true);

      if (logger.isDebugEnabled()) {
        logger.debug("Login successfully for user: " + orderCommand.getUsername());
      }

    } catch (Exception e) {
      loginSuccess = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return loginSuccess;
  }

  private boolean search(ItemInfoCommand itemInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>2. Search by keyword: " + itemInfo.getKeyword() + ">>>>>>>>>>>>");
    }

    SearchEngine searchEngine = new SearchEngine(driver);
    searchEngine.setCompareProductionCount(yhdOrderConfig.getCompareGoodsCount());

    return searchEngine.search(itemInfo);
  }

  private boolean addToShoppingCar(ItemInfoCommand itemInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>3. Ready add to shopping car >>>>>>>>>>>>");
    }

    AddProductionToShoppingCar shoppingCar = new AddProductionToShoppingCar(driver);
    shoppingCar.setBrowserTime(yhdOrderConfig.getBrowserTime());

    return shoppingCar.addToShoppingCar(itemInfo);
  }

  private boolean confirmOrder(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>4. Ready confirm order >>>>>>>>>>>");
    }

    ConfirmOrderInShoppingCar confirmOrder = new ConfirmOrderInShoppingCar(driver);

    return confirmOrder.confirmOrder(orderCommand.getItems());
  }

  private boolean checkoutOrder(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>5. Ready checkout order >>>>>>>>>>>");
    }

    CheckoutOrder checkoutOrder = new CheckoutOrder(driver);

    return checkoutOrder.checkout(orderCommand);
  }

  private boolean writeOrderInfo(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>6. Order was submitted, ready write order info >>>>>>>>>>>");
    }

    PaymentOrder paymentOrder = new PaymentOrder(driver);
    paymentOrder.setOrderWriter(orderWriter);

    return paymentOrder.writeOrderInfo(orderCommand);
  }

  public void setYhdOrderConfig(YHDOrderConfig yhdOrderConfig) {
    this.yhdOrderConfig = yhdOrderConfig;
  }
}
