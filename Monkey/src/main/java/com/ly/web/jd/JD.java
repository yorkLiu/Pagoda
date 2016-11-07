package com.ly.web.jd;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ly.config.JDConfig;
import com.ly.file.FileWriter;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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

  /** TODO: DOCUMENT ME! */
  public String voiceFilePath = null;

  private CommentsInfo commentsInfo = null;

  private List<CommentsInfo> commentsInfoList = new LinkedList<>();

  private static final String applicationContext = "applicationContext-resources.xml";
  private static final String[] JD_Config = new String[]{"JDResources.xml"};
  
  @Autowired
  private JDConfig jdConfig;
  
  @Autowired
  private FileWriter fileWriter;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  @BeforeTest
  public void init(){
    ApplicationContext parentContext = new ClassPathXmlApplicationContext(applicationContext);
    ApplicationContext context = new ClassPathXmlApplicationContext(JD_Config, parentContext);
    context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
      AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);

    this.webDriverProperties = jdConfig;
    initProperties();
  }

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
   *
   * checkOrderNo.
   * Check the @commentsInfoList's orderNos is exists have commented.
   */
  @Test(priority = 2)
  public void checkOrderNo() {
    logger.info(">>>>>Start check the order number......");
    if (commentsInfoList != null && commentsInfoList.size() > 0) {
      List<String> commentedOrders = fileWriter.getTodayCommentedOrdersFromFile(Constant.JD_COMMENT_FILE_NAME_PREFIX);

      List<CommentsInfo> actualList = new LinkedList<>();
      if (commentedOrders != null && commentedOrders.size() > 0) {
        commentsInfoList.stream().forEach(info -> {
          if (!commentedOrders.contains(info.getOrderId())) {
            logger.info("The orderNo[" + info.getOrderId() + "] was commented, skip this order.");
            actualList.add(info);
          }
        });
        if (actualList.size() > 0) {
          logger.info("The excel file order count: " + commentsInfoList.size());
          logger.info("After check today commented order, actually should comment count is: " + actualList.size());
          commentsInfoList.clear();
          ;
          commentsInfoList.addAll(actualList);
          logger.info("Now commentsInfoList count: " + commentsInfoList.size());
        }
      }
    }
    logger.info(">>>>>End check the order number......");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * readComment.
   */
  @Test(priority = 3)
  public void readComment() {
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

      String indexInfo = "[" + index + "/" + total + "]";

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<Ready comment: " + indexInfo + ">>>>>>>");
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

        // if 只收不评 is  ('Y', 'Yes', 'True', '是')
        // then skip comment step.
        if (!commentsInfo.getDoNotComment()) {
          if (logger.isDebugEnabled()) {
            // 3. comments production(s)
            logger.debug("Ready comment " + indexInfo);
          }

          comments();
        } else {
          logger.info("Order#" + commentsInfo.getOrderId()
            + " doNotComment is 'TRUE' then will not comment this order, continue next order.");
        }
        
        // 4. logout current user
        logout();

        // write this orderNo to file.
        if(fileWriter != null){
          fileWriter.writeToFile(Constant.JD_COMMENT_FILE_NAME_PREFIX, commentsInfo.getOrderId());
        }

        // 5. check driver
        // random to delay seconds for next account
        // check the current browser is inputted valid code more than @MAX_INPUT_V_CODE_COUNT times
        // if chrome and firefox all inputted valid code more than @MAX_INPUT_V_CODE_COUNT times
        // then pause 10 minutes for next account and re-set drive to 'chrome'
        if (index >= total) {
          logger.info("Comment finished >>>>>>current/total[" + index + "/" + total + "]>>>>>>>");

          break;
        }

        checkDriver(vCodeCountMap);
      } // end if
    }   // end for

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
    initWebDriver(jdConfig.getDriverType());
  }

  @Override
  protected void initProperties() {
    super.initProperties();
    JDDataProvider.path = jdConfig.getFilesPath();
    Comment.EXCLUDE_TAGs = jdConfig.getExcludeTags();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for voice file path.
   *
   * @param  voiceFilePath  String
   */
  public void setVoiceFilePath(String voiceFilePath) {
    this.voiceFilePath = voiceFilePath;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void comments() {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Ready for 'Comment' order#" + commentsInfo.getOrderId() + " and comment content is: "
          + commentsInfo.getCommentsMap());
      }

      Comment comment = new Comment(driver);
      comment.comment(commentsInfo.getOrderId(), commentsInfo.getTagsCount(), commentsInfo.getCommentsMap());

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

      Login login = new Login(driver, Constant.JD_LOGIN_PAGE_URL, voiceFilePath);

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


  public void setJdConfig(JDConfig jdConfig) {
    this.jdConfig = jdConfig;
  }

  public void setFileWriter(FileWriter fileWriter) {
    this.fileWriter = fileWriter;
  }
} // end class JD
