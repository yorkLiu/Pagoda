package com.ly.web.jd;

import java.io.File;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.CommentsInfo;
import com.ly.web.constant.Constant;
import com.ly.web.dp.JDDataProvider;


/**
 * Created by yongliu on 8/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/02/2016 15:35
 * @Steps    <pre>
   1. Login
   2. Open my order page
   3. Finding the orderId and click "Confirm Receipt"
   4. After finished step3, open comment page
   5. enter the comment content and select five star for all items then click "Submit"
   6. Logout
 * </pre>
 */
public class JD extends SeleniumBaseObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  public static ConcurrentMap<String, Integer> vCodeCountMap = new ConcurrentHashMap<>(5);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private CommentsInfo commentsInfo = null;

  private List<CommentsInfo> commentsInfoList = new LinkedList<>();

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * initData.
   *
   * @param  commentsInfo  CommentsInfo
   */
  @Test(
    priority          = 1,
    dataProvider      = "dp-jd-comment",
    dataProviderClass = JDDataProvider.class
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
   * readComment.
   */
  @Test(priority = 2)
  public void readComment() {
// commentsInfo = new CommentsInfo();
// commentsInfo.setUsername("mqep07084");
// commentsInfo.setPassword("ylmr40056");
// commentsInfo.setOrderId("20752493660");
// Map<String, String> map = new HashMap(2);
// map.put("10431986857", "宝贝很好。手感非常好。刻度清晰是正品");
// commentsInfo.setCommentsMap(map);

// // 1. login
// boolean loginSuccess = login();
//
// if(loginSuccess){
// // 2. confirmReceipt
// confirmReceipt();
//
// // 3. comments production(s)
// comments();
//
// // 4. logout current user
// logout();
//
// }


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
        logger.debug("Ready comment: " + commentsInfo);
      }

      this.commentsInfo = commentsInfo;


      // 1. login
      boolean loginSuccess = login(!(index > 1));

      if (loginSuccess) {
        // 2. confirmReceipt
        confirmReceipt();

        // 3. comments production(s)
        comments();

        // 4. logout current user
        logout();

        // 5. check driver
        // random to delay seconds for next account
        // check the current browser is inputted valid code more than @MAX_INPUT_V_CODE_COUNT times
        // if chrome and firefox all inputted valid code more than @MAX_INPUT_V_CODE_COUNT times
        // then pause 10 minutes for next account and re-set drive to 'chrome'
        checkDriver(vCodeCountMap, 60);
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Comment successfully, close the web driver.");
    }

    driver.close();
  } // end method readComment

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setup.
   */
  @BeforeTest public void setup() {
    initWebDriver(DRIVER_CHROME);

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void comments() {
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

  private void confirmReceipt() {
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

  private boolean login(boolean isFirst) {
    boolean loginSuccess = Boolean.TRUE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Starting login....");
        logger.debug("Open Login Page:" + Constant.JD_LOGIN_PAGE_URL);
      }

      Login login = new Login(driver, Constant.JD_LOGIN_PAGE_URL);

      if (logger.isDebugEnabled()) {
        logger.debug("Login JD with username: " + commentsInfo.getUsername() + "and password: XXXXX");
      }

      loginSuccess = login.login(commentsInfo.getUsername(), commentsInfo.getPassword(), isFirst);

      if (loginSuccess) {
        if (logger.isDebugEnabled()) {
          logger.debug("Login successfully for user:" + commentsInfo.getUsername());
        }
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("Login failed for user:" + commentsInfo.getUsername());
        }
      }
    } catch (Exception e) {
      loginSuccess = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch

    return loginSuccess;
  } // end method login

  //~ ------------------------------------------------------------------------------------------------------------------

  private void logout() {
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

} // end class JD
