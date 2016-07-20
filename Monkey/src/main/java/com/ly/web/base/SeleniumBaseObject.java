package com.ly.web.base;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import org.testng.annotations.Listeners;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;

import com.saucelabs.testng.SauceOnDemandTestListener;


/**
 * Create by Yong Liu on 07/12/2016.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/12/2016 15:06
 */
// @Listeners({ SauceOnDemandTestListener.class })
public class SeleniumBaseObject implements SauceOnDemandSessionIdProvider {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected WebDriver driver;

  /** DOCUMENT ME! */
  protected final Log logger = LogFactory.getLog(getClass());

  /** DOCUMENT ME! */
// protected SysConfig sysConfig = null;

  /** ThreadLocal variable which contains the Sauce Job Id. */
  private ThreadLocal<String> sessionId = new ThreadLocal<String>();


  /**
   * ThreadLocal variable which contains the {@link WebDriver} instance which is used to perform browser interactions
   * with.
   */
  private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new SeleniumBaseObject object.
   */
  public SeleniumBaseObject() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for driver.
   *
   * @return  WebDriver
   */
  public WebDriver getDriver() {
    return driver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * The Sauce Job id for the current thread.
   *
   * @return  the Sauce Job id for the current thread
   */
  @Override public String getSessionId() {
    return sessionId.get();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
// public SysConfig getSysConfig() {
// return sysConfig;
// }

  /**
   * The {@link WebDriver} for the current thread.
   *
   * @return  the {@link WebDriver} for the current thread
   */
  public WebDriver getWebDriver() {
    if (logger.isDebugEnabled()) {
      logger.debug("WebDriver" + webDriver.get());
    }

    return webDriver.get();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for driver.
   *
   * @param  driver  WebDriver
   */
  public void setDriver(WebDriver driver) {
    this.driver = driver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  dOCUMENT ME!
   */
// public void setSysConfig(SysConfig sysConfig) {
// this.sysConfig = sysConfig;
// }

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected WebDriver createLocalFireFoxDriver() {
// String webDriverPath = sysConfig.getWebDriverPath();
//
// if (webDriverPath != null && StringUtils.isNotEmpty(webDriverPath)) {
//
// logger.info("webDriverPath is chrome:"  + (webDriverPath.toLowerCase()).contains("chrome"));
// if ((webDriverPath.toLowerCase()).contains("chrome")) {
// try {
// // System.setProperty("webdriver.chrome.driver", "/Users/yongliu/Project/chromedriver/chromedriver");
// System.setProperty("webdriver.chrome.driver", sysConfig.getWebDriverPath());
//
// return new ChromeDriver();
// }catch (Exception e){
// e.printStackTrace();
// }
// } else if ((webDriverPath.toLowerCase()).contains("firefox")) {
// FirefoxProfile profile = new FirefoxProfile();
// profile.setAcceptUntrustedCertificates(true);
// profile.setAssumeUntrustedCertificateIssuer(true);
// profile.setEnableNativeEvents(true);
// profile.setAlwaysLoadNoFocusLib(true);
//
// WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File(sysConfig.getWebDriverPath())), profile);
//
// return driver;
// }
// }

    return null;
  } // end method createLocalFireFoxDriver

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   */
  protected void initLocalWebDriver() {
    driver = createLocalFireFoxDriver();

    driver.manage().window().maximize();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   userName  DOCUMENT ME!
   * @param   password  DOCUMENT ME!
   * @param   first     DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  protected void loginLocalSys(String userName, String password, Boolean first) throws Exception {
// if(Consent.SHOPPING_CHANNEL_SUNING.equalsIgnoreCase(sysConfig.getShoppingChannel())){
// logger.info("Ready login Suning.....");
// LoginSuning login = new LoginSuning(driver, Consent.SUNING_LOGIN_URL);
// login.login(userName, password, first);
// } else if(Consent.SHOPPING_CHANNEL_YHD.equalsIgnoreCase(sysConfig.getShoppingChannel())){
// logger.info("Ready login YHD");
// LoginHttp login = new LoginHttp(driver, Consent.YHD_LOGIN_URL);
// login.login(userName, password, first);
// } else {
// logger.info("Ready login YHD");
// LoginHttp login = new LoginHttp(driver, Consent.YHD_LOGIN_URL);
// login.login(userName, password, first);
// }

  } // end method loginLocalSys

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  message         DOCUMENT ME!
   * @param  sendMessageUrl  DOCUMENT ME!
   * @param  startShopId     start DOCUMENT ME!
   * @param  totalCount      DOCUMENT ME!
   */
  protected void sendMessageToShop(String message, String sendMessageUrl, String startShopId, Integer totalCount) {
// String      endShopId   = String.valueOf(new Integer(startShopId) + totalCount);
    logger.info("Title: " + driver.getTitle());

// SendMessage sendMessage = new SendMessage(driver, message, sendMessageUrl, startShopId, totalCount);
// sendMessage.sendMessage();

// if(Consent.SHOPPING_CHANNEL_SUNING.equalsIgnoreCase(sysConfig.getShoppingChannel())){
// SendMessageToSuning sendMessage = new SendMessageToSuning(driver, message, sysConfig.getProductionListUrl(),
// totalCount);
// sendMessage.sendMessage();
// } else if(Consent.SHOPPING_CHANNEL_YHD.equalsIgnoreCase(sysConfig.getShoppingChannel())){
// SendMessageByCategory sendMessage = new SendMessageByCategory(driver, message, sysConfig.getProductionListUrl(),
// totalCount);
// sendMessage.sendMessage();
// }else {
// SendMessageByCategory sendMessage = new SendMessageByCategory(driver, message, sysConfig.getProductionListUrl(),
// totalCount);
// sendMessage.sendMessage();
// }

  }
} // end class SeleniumBaseObject
