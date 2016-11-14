package com.ly.web.base;

import com.ly.config.WebDriverProperties;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Map;
import java.util.Random;


/**
 * Create by Yong Liu on 07/12/2016.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 15:06
 */
// @Listeners({ SauceOnDemandTestListener.class })
public class SeleniumBaseObject implements SauceOnDemandSessionIdProvider {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  public static final int MAX_INPUT_V_CODE_COUNT = 2;

  /** TODO: DOCUMENT ME! */
  public static final String DRIVER_CHROME = "chrome";

  /** TODO: DOCUMENT ME! */
  public static final String DRIVER_FIREFOX = "firefox";

  /** TODO: DOCUMENT ME! */
  public static final String DRIVER_SAFARI = "safari";

  /** TODO: DOCUMENT ME! */
  public static final String DRIVER_IE = "ie";

  /**
   * <pre>
   chrome
   firefox
   safari
   IE
   * </pre>
   */
  public static String currentDriver = "chrome";

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

  protected WebDriverProperties webDriverProperties;

  /*the value is webDriverProperties.warningVoiceFile*/
  public String voiceFilePath = null;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new SeleniumBaseObject object.
   */
  public SeleniumBaseObject() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  public void checkDriver(Map<String, Integer> vCodeCountMap){
    this.checkDriver(vCodeCountMap, webDriverProperties.getMaxDelaySecondsForNext());
  }

  /**
   * random to delay seconds for next account check the current browser is inputted valid code more than
   * MAX_INPUT_V_CODE_COUNT times if chrome and firefox all inputted valid code more than MAX_INPUT_V_CODE_COUNT times
   * then pause 10 minutes for next account and re-set drive to 'chrome'
   *
   * @param  vCodeCountMap           map
   * @param  maxDelaySecondsForNext  max delay seconds for next comment record.
   */
  public void checkDriver(Map<String, Integer> vCodeCountMap, int maxDelaySecondsForNext) {
    // all web driver input valid code gretter than @MAX_INPUT_V_CODE_COUNT
    if ((vCodeCountMap.get(DRIVER_CHROME) != null) && (vCodeCountMap.get(DRIVER_CHROME) >= MAX_INPUT_V_CODE_COUNT)
          && (vCodeCountMap.get(DRIVER_FIREFOX) != null)
          && (vCodeCountMap.get(DRIVER_FIREFOX) >= MAX_INPUT_V_CODE_COUNT)) {
      if (logger.isDebugEnabled()) {
        logger.debug("'Chrome' and 'FireFox' are all need input valid code, I think need pause comment.");
      }

      // need pause comment operation for 10 minutes
      delay(600);

      // after pause 10 minutes, clear the vCodeMap
      vCodeCountMap.clear();

      // re-set the driver to chrome.
      setupDriver(DRIVER_CHROME);

    } else {
      if (logger.isDebugEnabled()) {
        logger.debug(".......Start to check web drive is valid code shows more than 2 times.....");
        logger.debug("Current web drive: " + currentDriver + ", input the valid code time(s): "
          + vCodeCountMap.get(currentDriver));
      }

      if ((vCodeCountMap.get(currentDriver) != null) && (vCodeCountMap.get(currentDriver) >= MAX_INPUT_V_CODE_COUNT)) {
        if ((vCodeCountMap.get(DRIVER_FIREFOX) == null) || (vCodeCountMap.get(DRIVER_FIREFOX) <= 0)) {
          if (logger.isDebugEnabled()) {
            logger.debug("Ready change drive from: '" + currentDriver + " To: '" + DRIVER_FIREFOX + "'");
          }

          setupDriver(DRIVER_FIREFOX);
        } else if ((vCodeCountMap.get(DRIVER_CHROME) == null) || (vCodeCountMap.get(DRIVER_CHROME) <= 0)) {
          if (logger.isDebugEnabled()) {
            logger.debug("Ready change drive from: '" + currentDriver + " To: '" + DRIVER_CHROME + "'");
          }

          setupDriver(DRIVER_CHROME);
        }
      }

      if (logger.isDebugEnabled()) {
        logger.debug(".......End to check web drive is valid code shows more than 2 times.....");
      }

      int seconds = new Random().nextInt(maxDelaySecondsForNext);

      if (logger.isDebugEnabled()) {
        logger.debug("It will delay:" + seconds + " seconds to comment next record.");
      }

      delay(seconds);

    } // end if-else
  }   // end method checkDriver

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * setupDriver.
   *
   * @param  driverType  String
   */
  public void setupDriver(String driverType) {
    if (DRIVER_CHROME.equalsIgnoreCase(driverType)) {
      currentDriver = DRIVER_CHROME;

      if (logger.isDebugEnabled()) {
        logger.debug("Init Chrome Web Driver.....");
      }
      
      System.setProperty("webdriver.chrome.driver", webDriverProperties.getChromeDriverPath());
      driver = new ChromeDriver();

    } else if (DRIVER_FIREFOX.equalsIgnoreCase(driverType)) {
      currentDriver = DRIVER_FIREFOX;

      FirefoxProfile profile = new FirefoxProfile();
      profile.setAcceptUntrustedCertificates(true);
      profile.setAssumeUntrustedCertificateIssuer(true);
      profile.setEnableNativeEvents(true);
      profile.setAlwaysLoadNoFocusLib(true);

      if (logger.isDebugEnabled()) {
        logger.debug("Init Firefox Web Driver.....");
      }
      
      driver = new FirefoxDriver(new FirefoxBinary(new File(webDriverProperties.getFirefoxDriverPath())),
          profile);
    } else if (DRIVER_IE.equalsIgnoreCase(driverType)) {
      currentDriver = DRIVER_IE;

      if (logger.isDebugEnabled()) {
        logger.debug("Init Internet Explorer Web Driver.....");
      }

      System.setProperty("webdriver.ie.driver", webDriverProperties.getIeDriverPath());
      driver = new InternetExplorerDriver();
    } // end if-else
  }   // end method setupDriver

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * delay.
   *
   * @param  seconds  int
   */
  protected void delay(int seconds) {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug(">>>Starting delay " + seconds + " second(s).>>>");
      }

      Thread.sleep(seconds * 1000);

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<End delay " + seconds + " second(s).<<<<");
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  driverName  String
   */
  protected void initWebDriver(String driverName) {
    if ((driverName == null) || StringUtils.isEmpty(driverName)) {
      driverName = DRIVER_CHROME;
    }

    setupDriver(driverName);

    if (logger.isDebugEnabled()) {
      logger.debug("Maximum " + currentDriver);
    }

    driver.manage().deleteAllCookies();
    driver.manage().window().maximize();
  }
  
  protected void initProperties(){
    this.voiceFilePath = webDriverProperties.getWarningVoiceFile();
  }

  /**
   * setter method for voice file path.
   *
   * @param  voiceFilePath  String
   */
  public void setVoiceFilePath(String voiceFilePath) {
    this.voiceFilePath = voiceFilePath;
  }


} // end class SeleniumBaseObject
