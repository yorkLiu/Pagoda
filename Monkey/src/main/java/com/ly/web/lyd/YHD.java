package com.ly.web.lyd;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;

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

  private List<CommentsInfo> commentsInfoList = new LinkedList<>();

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * comment.
   */
  @Test(priority = 2)
  public void comment() {
    if (logger.isDebugEnabled()) {
      logger.debug("Comments total count: " + commentsInfoList.size());
    }

    for (CommentsInfo commentsInfo : commentsInfoList) {
      
      if ((commentsInfo.getUsername() == null) || !StringUtils.hasText(commentsInfo.getUsername())) {
        if (logger.isDebugEnabled()) {
          logger.debug("This record username is NULL, skip it.");
        }

        continue;
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

    }
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

      System.setProperty("webdriver.chrome.driver", "/Users/yongliu/Project/chromedriver/chromedriver");
      driver = new ChromeDriver();
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("Driver is not NULL, no need to new driver instance.");
      }
    }

  } // end method setup


} // end class YHD
