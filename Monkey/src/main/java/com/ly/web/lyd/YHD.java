package com.ly.web.lyd;

import java.io.File;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.ly.config.WebDriverProperties;
import com.ly.config.YHDConfig;
import com.ly.file.FileWriter;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
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
  public static ConcurrentMap<String, Integer> vCodeCountMap = new ConcurrentHashMap<>(5);

  //~ Instance fields --------------------------------------------------------------------------------------------------
  
  private static final String applicationContext = "applicationContext-resources.xml";

  private CommentsInfo commentsInfo = null;

  private List<CommentsInfo> commentsInfoList = new LinkedList<>();

  private static final String[] YHD_Config = new String[]{"YHDResources.xml"};
  
  @Autowired
  private YHDConfig yhdConfig;
  
  @Autowired
  private FileWriter fileWriter;

  //~ Methods ----------------------------------------------------------------------------------------------------------
  @BeforeTest
  public void init(){
    ApplicationContext parentContext = new ClassPathXmlApplicationContext(applicationContext);
    ApplicationContext context = new ClassPathXmlApplicationContext(YHD_Config, parentContext);
    context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
      AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);

    this.webDriverProperties = yhdConfig;
    initProperties();
  }

  /**
   * comment.
   */
  @Test(priority = 3)
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
        logger.debug("Ready comment: " + commentsInfo);
      }

      this.commentsInfo = commentsInfo;

      // 1. login
      boolean loginSuccess = login(!(index > 1));

      if (loginSuccess) {
        // 2. confirm receipt
        confirmReceipt();

        // 3. comment production(s)
        comments();

        // 4. logout for current user
        logout();

        // write this orderNo to file.
        if(fileWriter != null){
          fileWriter.writeToFile(Constant.YHD_COMMENT_FILE_NAME_PREFIX, commentsInfo.getOrderId());
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
        checkDriver();
      }
    } // end for

    if (logger.isDebugEnabled()) {
      logger.debug("Comment successfully, close the web driver.");
    }

    driver.close();

  } // end method comment

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

  /**
   *
   * checkOrderNo.
   * Check the @commentsInfoList's orderNos is exists have commented.
   */
  @Test(priority = 2)
  public void checkOrderNo() { 
    
    logger.info(">>>>>Start check the order number......");
    if(commentsInfoList != null && commentsInfoList.size() > 0){
      List<String> commentedOrders = fileWriter.getTodayCommentedOrdersFromFile(Constant.YHD_COMMENT_FILE_NAME_PREFIX);

      List<CommentsInfo> actualList = new LinkedList<>();
      if (commentedOrders != null && commentedOrders.size() > 0) {
        commentsInfoList.stream().forEach(info -> {
          if(!commentedOrders.contains(info.getOrderId())){
            logger.info("The orderNo[" + info.getOrderId() + "] was commented, skip this order.");
            actualList.add(info);
          }
        });
        if(actualList.size() > 0){
          logger.info("The excel file order count: " + commentsInfoList.size());
          logger.info("After check today commented order, actually should comment count is: " + actualList.size());
          commentsInfoList.clear();;
          commentsInfoList.addAll(actualList);
          logger.info("Now commentsInfoList count: " + commentsInfoList.size());
        }
      }
    }
    logger.info(">>>>>End check the order number......");
  }
  

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setup.
   */
  @BeforeTest public void setup() {
    initWebDriver(DRIVER_CHROME);

  } // end method setup

  @Override
  protected void initProperties() {
    super.initProperties();
    YHDDataProvider.path = yhdConfig.getFilesPath();
  }

  //~ ------------------------------------------------------------------------------------------------------------------


  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * random to delay seconds for next account check the current browser is inputted valid code more than
   * MAX_INPUT_V_CODE_COUNT times if chrome and firefox all inputted valid code more than MAX_INPUT_V_CODE_COUNT times
   * then pause 10 minutes for next account and re-set drive to 'chrome'
   */
  private void checkDriver() {
    // all web driver input valid code gretter than @MAX_INPUT_V_CODE_COUNT
    checkDriver(vCodeCountMap);
  } // end method checkDriver

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * comments.
   */
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

  /**
   * 1. Open my order page 2. confirm this orderId receipt
   */
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

  /**
   * login.
   *
   * @param   isFirst  boolean
   *
   * @return  login.
   */
  private Boolean login(boolean isFirst) {
    boolean loginSuccess = Boolean.TRUE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Starting login....");
        logger.debug("Open Login Page:" + Constant.YHD_LOGIN_PAGE_URL);
      }

      Login login = new Login(driver, Constant.YHD_LOGIN_PAGE_URL, voiceFilePath);

      if (logger.isDebugEnabled()) {
        logger.debug("Login YHD with username: " + commentsInfo.getUsername() + "and password: XXXXX");
      }

      loginSuccess = login.login(commentsInfo.getUsername(), commentsInfo.getPassword(), isFirst);

      if (logger.isDebugEnabled()) {
        logger.debug("Login successfully for user:" + commentsInfo.getUsername());
      }


    } catch (Exception e) {
      loginSuccess = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return loginSuccess;
  } // end method login

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * logout.
   */
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

  public void setYhdConfig(YHDConfig yhdConfig) {
    this.yhdConfig = yhdConfig;
  }

  public void setFileWriter(FileWriter fileWriter) {
    this.fileWriter = fileWriter;
  }
} // end class YHD
