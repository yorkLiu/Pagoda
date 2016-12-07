package com.ly.web.yhd;

import java.util.LinkedList;
import java.util.List;

import com.ly.proxy.ProxyProcessor;
import com.ly.web.constant.Constant;
import com.ly.web.dp.YHDOrderDataProvider;
import com.ly.web.lyd.Login;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.testng.annotations.BeforeTest;

import com.ly.config.YHDOrderConfig;

import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.OrderCommand;
import com.ly.web.writer.OrderWriter;


/**
 * Created by yongliu on 11/14/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/14/2016 15:11
 */
public class YHDBaseOrderCase extends SeleniumBaseObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /**  DOCUMENT ME! */
  protected static final String   applicationContext = "applicationConfig.xml";

  /** DOCUMENT ME! */
  protected static final String[] YHD_Configs = new String[] { "YHD-ApplicationConfig.xml" };

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected final Log logger = LogFactory.getLog(getClass());

  /** DOCUMENT ME! */
  protected List<OrderCommand> orderCommandList = new LinkedList<>();

  /** DOCUMENT ME! */
  protected OrderWriter orderWriter;

  @Autowired protected YHDOrderConfig yhdOrderConfig;

  @Autowired  protected ProxyProcessor proxyProcessor;

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
   * @see  com.ly.web.base.SeleniumBaseObject#initProperties()
   */
  @Override protected void initProperties() {
    super.initProperties();
  }

//  @BeforeTest public void setup() {
//    initWebDriver(yhdOrderConfig.getDriverType());
//  } // end method setup


  /**
   * login.
   *
   * @param   orderCommand  OrderCommand
   *
   * @return  boolean
   */
  protected boolean login(OrderCommand orderCommand) {
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

  protected boolean confirmOrder(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>4. Ready confirm order >>>>>>>>>>>");
    }

    ConfirmOrderInShoppingCar confirmOrder = new ConfirmOrderInShoppingCar(driver);

    return confirmOrder.confirmOrder(orderCommand.getItems());
  }

  protected boolean checkoutOrder(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>5. Ready checkout order >>>>>>>>>>>");
    }

    CheckoutOrder checkoutOrder = new CheckoutOrder(driver);

    return checkoutOrder.checkout(orderCommand);
  }

  protected boolean writeOrderInfo(OrderCommand orderCommand) {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>>6. Order was submitted, ready write order info >>>>>>>>>>>");
    }

    PaymentOrder paymentOrder = new PaymentOrder(driver);
    paymentOrder.setOrderWriter(orderWriter);

    return paymentOrder.writeOrderInfo(orderCommand);
  }


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
   * getter method for yhd order config.
   *
   * @return  YHDOrderConfig
   */
  public YHDOrderConfig getYhdOrderConfig() {
    return yhdOrderConfig;
  }

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

  /**
   * setter method for yhd order config.
   *
   * @param  yhdOrderConfig  YHDOrderConfig
   */
  public void setYhdOrderConfig(YHDOrderConfig yhdOrderConfig) {
    this.yhdOrderConfig = yhdOrderConfig;
  }

  public void setProxyProcessor(ProxyProcessor proxyProcessor) {
    this.proxyProcessor = proxyProcessor;
  }
} // end class YHDBaseOrderCase
