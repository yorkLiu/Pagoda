package com.ly.web.yhd;

import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.AddressInfoCommand;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.constant.Constant;
import com.ly.web.lyd.Login;
import com.ly.web.writer.OrderWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


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

  private static final String applicationContext = "applicationContext-resources.xml";



  //~ Methods ----------------------------------------------------------------------------------------------------------

  @BeforeTest
  public void init(){
    ApplicationContext context = new ClassPathXmlApplicationContext(applicationContext);
    context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
      AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
  }
  

  /**
   * setup.
   */
  @BeforeTest public void setup() {
    initProperties();
    initWebDriver(DRIVER_CHROME);
  } // end method setup
  
  
  @Test(priority = 1)
  private void testOrder(){
    orderCommand = new OrderCommand();
    orderCommand.setUsername("13046883033");
    orderCommand.setPassword("yyy555");

    AddressInfoCommand addressInfo = new AddressInfoCommand();
    addressInfo.setFullName("贺雨馨");
    addressInfo.setProvince("河南");
    addressInfo.setCity("郑州");
    addressInfo.setCountry("金水区");
    addressInfo.setTelephoneNum("13877435976");

    ItemInfoCommand itemInfoCommand = new ItemInfoCommand();
    itemInfoCommand.setSku("67559639");
    itemInfoCommand.setKeyword("红酒");

    orderCommand.setAddressInfo(addressInfo);
    orderCommand.addItem(itemInfoCommand);
    
//    orderCommand.setKeyword("睡衣女秋");
//    orderCommand.setSku("54741283");
    
    
    // 1. login
    // 2. search by keyword
    // 3. find the production by sku in search result
    // 4. browse the production
    // 5. add it to shopping car
    boolean loginSuccess = login();
    if(loginSuccess){

      boolean addedToShoppingCar = Boolean.FALSE; 
      boolean canCheckoutOrder = Boolean.FALSE;
      boolean orderSubmitted   = Boolean.FALSE;

      for (ItemInfoCommand itemInfo : orderCommand.getItems()) {
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
        canCheckoutOrder = confirmOrder();
      }
      
      if(canCheckoutOrder){
        logger.info("-----------------------------------------------------");
        logger.info("            Go to Check out order                    ");
        logger.info("-----------------------------------------------------");
        orderSubmitted = checkoutOrder();
      }
      
      if(orderSubmitted){
        logger.info("-----------------------------------------------------");
        logger.info("            Order Submitted, Get Order Info          ");
        logger.info("-----------------------------------------------------");
        writeOrderInfo();
      }
      
    }
  }

  public void setOrderWriter(OrderWriter orderWriter) {
    this.orderWriter = orderWriter;
  }

  private boolean login() {
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

    return searchEngine.search(itemInfo);
  }

  private boolean addToShoppingCar(ItemInfoCommand itemInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>>>3. Ready add to shopping car >>>>>>>>>>>>");
    }

    AddProductionToShoppingCar shoppingCar = new AddProductionToShoppingCar(driver);

    return shoppingCar.addToShoppingCar(itemInfo);
  }

  private boolean confirmOrder() {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>4. Ready confirm order >>>>>>>>>>>");
    }

    ConfirmOrderInShoppingCar confirmOrder = new ConfirmOrderInShoppingCar(driver);

    return confirmOrder.confirmOrder(orderCommand.getItems());
  }

  private boolean checkoutOrder() {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>5. Ready checkout order >>>>>>>>>>>");
    }

    CheckoutOrder checkoutOrder = new CheckoutOrder(driver);

    return checkoutOrder.checkout(orderCommand);
  }

  private boolean writeOrderInfo() {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>6. Order was submitted, ready write order info >>>>>>>>>>>");
    }

    PaymentOrder paymentOrder = new PaymentOrder(driver);
    paymentOrder.setOrderWriter(orderWriter);

    return paymentOrder.writeOrderInfo(orderCommand);
  }

  public void setVoiceFilePath(String voiceFilePath) {
    this.voiceFilePath = voiceFilePath;
  }
}
