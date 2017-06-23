package com.ly.web.lyd;

import com.ly.config.YHDConfig;
import com.ly.file.FileWriter;
import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.CommentsInfo;
import com.ly.web.constant.Constant;
import com.ly.web.dp.YHDDataProvider;
import com.ly.web.exceptions.AccountLockedException;
import com.ly.web.utils.PagodaOrderSortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


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
  @Test(priority = 4)
  public void comment() {
    int total = commentsInfoList.size();
    int index = 0;

    if (logger.isDebugEnabled()) {
      logger.debug("Comments total count: " + total);
    }

    for (CommentsInfo commentsInfo : commentsInfoList) {
      long startTime = System.currentTimeMillis();
      if ((commentsInfo.getUsername() == null) || !StringUtils.hasText(commentsInfo.getUsername())) {
        if (logger.isDebugEnabled()) {
          logger.debug("This record username is NULL, skip it.");
        }

        continue;
      }

      index++;

      String indexInfo = "[" + index + "/" + total + "]";
      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<Ready comment: "+indexInfo+">>>>>>>");
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

        // if 只收不评 is  ('Y', 'Yes', 'True', '是')
        // then skip comment step.
        if (!commentsInfo.getDoNotComment()) {
          if (logger.isDebugEnabled()) {
            logger.debug("Ready comment " + indexInfo);
          }
          
          // 3. comment production(s)
          comments();
        } else {
          logger.info("Order#" + commentsInfo.getOrderId()
            + " doNotComment is 'TRUE' then will not comment this order, continue next order.");
        }

        // 4. logout for current user
        logout();

        // write this orderNo to file.
        if(fileWriter != null){
          fileWriter.writeToFile(Constant.YHD_COMMENT_FILE_NAME_PREFIX, commentsInfo.getOrderId());
        }
        
        // print info
        long   endTime = System.currentTimeMillis();
        String infoMsg = String.format("%s -- [%s] -- [%s/%s] -- Spent: %s ms. Commented Successfully.", commentsInfo.getOrderId(),
            commentsInfo.getUsername(), index, total, (endTime - startTime));
        logger.info(infoMsg);

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

  @Test(priority = 2)
  public void sortCommentInfoList(){
    List<CommentsInfo> sortedCommentsInfoList = PagodaOrderSortUtils.sort(commentsInfoList);
    if(sortedCommentsInfoList != null && !sortedCommentsInfoList.isEmpty()){
      String sepreter = "\t";
      logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Compare Before >>>>> After >>>>>>>>>>>>>>>>>>>>");
      System.out.println("No.#" + sepreter + "Order No.#" + sepreter + "User Name" + sepreter + "Order No.#" + sepreter + "User Name" + sepreter + "Not Comment");
      for (int i = 0; i < sortedCommentsInfoList.size(); i++) {
        CommentsInfo before = commentsInfoList.get(i);
        CommentsInfo after = sortedCommentsInfoList.get(i);
        System.out.println(i + "." + sepreter + before.getOrderId() + sepreter + before.getSku() + " >>>" +sepreter + after.getOrderId() + sepreter + after.getSku() + sepreter + after.getDoNotCommentStr());
      }
      
      logger.info("Total count before/after:" + commentsInfoList.size() + "/" + sortedCommentsInfoList.size());
      commentsInfoList.clear();
      commentsInfoList.addAll(sortedCommentsInfoList);
    }
  }
  
  
  /**
   *
   * checkOrderNo.
   * Check the @commentsInfoList's orderNos is exists have commented.
   */
  @Test(priority = 3)
  public void checkOrderNo() { 
    
    logger.info(">>>>>Start check the order number......");
    try {
      if (commentsInfoList != null && commentsInfoList.size() > 0) {
        List<String> commentedOrders = fileWriter.getTodayCommentedOrdersFromFile(Constant.YHD_COMMENT_FILE_NAME_PREFIX);

        List<CommentsInfo> actualList = new LinkedList<>();
        if (commentedOrders != null && commentedOrders.size() > 0) {
          commentsInfoList.stream().forEach(info -> {
            if (!commentedOrders.contains(info.getOrderId())) {
              actualList.add(info);
            } else {
              logger.info("The orderNo[" + info.getOrderId() + "] was commented, skip this order.");
            }
          });
          commentsInfoList.clear();
          if (actualList.size() > 0) {
            logger.info("The excel file order count: " + commentsInfoList.size());
            logger.info("After check today commented order, actually should comment count is: " + actualList.size());
            commentsInfoList.addAll(actualList);
            logger.info("Now commentsInfoList count: " + commentsInfoList.size());
          }
        }
      }
    }catch (Exception e){
      logger.error(e.getMessage());
    }
    logger.info(">>>>>End check the order number......");
  }
  

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setup.
   */
  @BeforeTest public void setup() {
    initWebDriver(yhdConfig.getDriverType());

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
    String username = commentsInfo.getUsername();
    String pwd = commentsInfo.getPassword();

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Starting login....");
        logger.debug("Open Login Page:" + Constant.YHD_LOGIN_PAGE_URL);
      }

      Login login = new Login(driver, Constant.YHD_LOGIN_PAGE_URL, voiceFilePath);

      if (logger.isDebugEnabled()) {
        logger.debug("Login YHD with username: " + username + "and password: XXXXX");
      }

      loginSuccess = login.login(username, pwd, isFirst);

      if (logger.isDebugEnabled()) {
        logger.debug("Login successfully for user:" + username);
      }


    } catch (AccountLockedException e){
      loginSuccess = Boolean.FALSE;
      // write this orderNo to file.
      if(fileWriter != null){
        String content = StringUtils.arrayToDelimitedString(new String[]{username, pwd, commentsInfo.getOrderId()}, "/");
        fileWriter.writeToFile(Constant.YHD_ACCOUNT_LOCKED_FILE_NAME_PREFIX, content);
      }
      logger.error(e.getMessage(), e);
    }catch (Exception e) {
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
