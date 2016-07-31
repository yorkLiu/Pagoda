package com.ly.web.lyd;

import java.io.File;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.CommentsInfo;
import com.ly.web.constant.Constant;
import com.ly.web.dp.YHDDataProvider;


/**
 * Created by yongliu on 7/12/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 14:08
 * @Steps    <pre>
     1. Login
     2. Open my order page
     3. Finding the orderId and click "Confirm Receipt"
     4. After finished step3, open comment page
     5. enter the comment content and select five star for all items then click "Submit"
     6. Logout
 * </pre>
 */
public class YHD extends SeleniumBaseObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  public static final String DRIVER_CHROME  = "chrome";

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

  /** TODO: DOCUMENT ME! */
  public static ConcurrentMap<String, Integer> vCodeCountMap = new ConcurrentHashMap<>(5);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private CommentsInfo commentsInfo = null;

  private List<CommentsInfo> commentsInfoList = new LinkedList<>();

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * comment.
   */
  @Test(priority = 2)
  public void comment() {
    int total = commentsInfoList.size();
    int index = 0;

    if (logger.isDebugEnabled()) {
      logger.debug("Comments total count: " + total);
    }

    for (CommentsInfo commentsInfo : commentsInfoList) {
      if ((commentsInfo.getUsername() == null) || !StringUtils.hasText(commentsInfo.getUsername())) {
        if (logger.isDebugEnabled()) {
          logger.debug("This record username is NULL, skip it.");
        }

        continue;
      }

      index++;

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<Ready comment: [" + index + "/" + total + "]>>>>>>>");
      }

      if (logger.isDebugEnabled()) {
        logger.debug(".......Start to check web drive is valid code shows more than 2 times.....");
        logger.debug("Current web drive: " + currentDriver + ", input the valid code time(s): "
          + vCodeCountMap.get(currentDriver));
      }

      if ((vCodeCountMap.get(currentDriver) != null) && (vCodeCountMap.get(currentDriver) >= 2)) {
        if (vCodeCountMap.get(DRIVER_FIREFOX) == null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Ready change drive from: '" + currentDriver + " To: '" + DRIVER_FIREFOX + "'");
          }

          setupDriver(DRIVER_FIREFOX);
        } else if (vCodeCountMap.get(DRIVER_SAFARI) == null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Ready change drive from: '" + currentDriver + " To: '" + DRIVER_SAFARI + "'");
          }

          setupDriver(DRIVER_SAFARI);
        }
      }

      if (logger.isDebugEnabled()) {
        logger.debug(".......End to check web drive is valid code shows more than 2 times.....");
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Ready comment: " + commentsInfo);
      }

      this.commentsInfo = commentsInfo;

      // 1. login
      login();

      // 2. confirm receipt
      confirmReceipt();

      // 3. comment production(s)
      comments();

      // 4. logout for current user
      logout();

      int seconds = new Random().nextInt(60);

      if (logger.isDebugEnabled()) {
        logger.debug("It will delay:" + seconds + " seconds to comment next record.");
      }

      delay(seconds);

    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Comment successfully, close the web driver.");
    }

    driver.close();

  } // end method comment

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * comments.
   */
// @Test(priority = 3)
  public void comments() {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Ready for 'Comment' order#" + commentsInfo.getOrderId() + " and comment content is: "
          + commentsInfo.getCommentsMap());
      }

      Comment comment = new Comment(driver);
      comment.comment(commentsInfo.getOrderId(), commentsInfo.getCommentsMap());

      if (logger.isDebugEnabled()) {
        logger.debug("'Comment' successfully for order#" + commentsInfo.getOrderId());
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * 1. Open my order page 2. confirm this orderId receipt
   */
// @Test(priority = 2)
  public void confirmReceipt() {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Ready for 'Confirm Receipt' order#" + commentsInfo.getOrderId());
      }

      ConfirmReceipt confirmReceipt = new ConfirmReceipt(driver);
      confirmReceipt.receipt(commentsInfo.getOrderId());

      if (logger.isDebugEnabled()) {
        logger.debug("'Confirm Receipt' successfully for order#" + commentsInfo.getOrderId());
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * initData.
   *
   * @param  commentsInfo  CommentsInfo
   */
  @Test(
    priority          = 1,
    dataProvider      = "dp-yhd-comment",
    dataProviderClass = YHDDataProvider.class
  )
  public void initData(CommentsInfo commentsInfo) {
    Assert.notNull(commentsInfo);
    commentsInfoList.add(commentsInfo);

    if (logger.isDebugEnabled()) {
      logger.debug("init comment data list size:" + commentsInfoList.size());
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * login.
   */
// @Test(priority = 1)
  public void login() {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Starting login....");
        logger.debug("Open Login Page:" + Constant.YHD_LOGIN_PAGE_URL);
      }

      Login login = new Login(driver, Constant.YHD_LOGIN_PAGE_URL);

      if (logger.isDebugEnabled()) {
        logger.debug("Login YHD with username: " + commentsInfo.getUsername() + "and password: XXXXX");
      }

      login.login(commentsInfo.getUsername(), commentsInfo.getPassword(), Boolean.TRUE);

      if (logger.isDebugEnabled()) {
        logger.debug("Login successfully for user:" + commentsInfo.getUsername());
      }


    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

  } // end method login

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * logout.
   */
// @Test(priority = 4)
  public void logout() {
    if (logger.isDebugEnabled()) {
      logger.debug("Ready logout for user#" + commentsInfo.getUsername());
    }

    Logout logout = new Logout(driver);
    logout.setCloseDriver(Boolean.FALSE);
    logout.doLogout();

    if (logger.isDebugEnabled()) {
      logger.debug("The user#" + commentsInfo.getUsername() + " Logged out.");
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setup.
   */
  @BeforeTest public void setup() {
    if (driver == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("Driver is NULL, then new chrome driver instance.");
      }

      currentDriver = DRIVER_CHROME;

      System.setProperty("webdriver.chrome.driver", "/Users/yongliu/Project/chromedriver/chromedriver");
      driver = new ChromeDriver();
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("Driver is not NULL, no need to new driver instance.");
      }
    }

  } // end method setup

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

      System.setProperty("webdriver.chrome.driver", "/Users/yongliu/Project/chromedriver/chromedriver");
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

      driver = new FirefoxDriver(new FirefoxBinary(new File("/Applications/Firefox.app/Contents/MacOS/firefox")),
          profile);
    } else if (DRIVER_SAFARI.equalsIgnoreCase(driverType)) {
      currentDriver = DRIVER_SAFARI;

      if (logger.isDebugEnabled()) {
        logger.debug("Init Safari Web Driver.....");
      }

      // safari path: /Applications/Safari.app/Contents/MacOS/safari
      driver = new SafariDriver();
    } else if (DRIVER_IE.equalsIgnoreCase(driverType)) {
      currentDriver = DRIVER_IE;

      if (logger.isDebugEnabled()) {
        logger.debug("Init Internet Explorer Web Driver.....");
      }

      driver = new InternetExplorerDriver();
    } // end if-else
  } // end method setupDriver

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


} // end class YHD
