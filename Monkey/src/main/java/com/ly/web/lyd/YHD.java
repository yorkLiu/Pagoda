package com.ly.web.lyd;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.CommentsInfo;
import com.ly.web.constant.Constant;
import com.ly.web.listeners.WebDriverExceptionTakeScreenshotListener;


/**
 * Created by yongliu on 7/12/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
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
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private CommentsInfo commentsInfo = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * comments.
   */
  @Test(priority = 3)
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
  @Test(priority = 2)
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
   */
  @Test(priority = 0)
  public void initData() {
    
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * login.
   */
  @Test(priority = 1)
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
  @Test(priority = 4)
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
    WebDriverEventListener eventListener = new WebDriverExceptionTakeScreenshotListener();
    driver = new EventFiringWebDriver(new FirefoxDriver());


// DesiredCapabilities capability = null;
// capability = DesiredCapabilities.firefox();
// WebDriverExceptionTakeScreenshotListener eventListener = new WebDriverExceptionTakeScreenshotListener();
// eventListener.setBaseDir("/Users/yongliu/Download/temp/");
// WebDriver driver = new EventFiringWebDriver(new RemoteWebDriver(new URL(
// remote_driver_url), capability)).register(eventListener);

    
    
  }


} // end class YHD
