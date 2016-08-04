package com.ly.web.jd;

import com.ly.web.base.SeleniumBaseObject;
import com.ly.web.command.CommentsInfo;
import com.ly.web.constant.Constant;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.util.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


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

  public static ConcurrentMap<String, Integer> vCodeCountMap = new ConcurrentHashMap<>(5);
  

  private CommentsInfo commentsInfo = null;

  private List<CommentsInfo> commentsInfoList = new LinkedList<>();
  
  @Test(priority = 1)
  public void readComment(){
    
    commentsInfo = new CommentsInfo();
    commentsInfo.setUsername("ndot84639");
    commentsInfo.setPassword("waxq59872");
    commentsInfo.setOrderId("20660669231");
    Map<String, String> map = new HashMap(2);
    map.put("1067519959", "很好,不错");
    commentsInfo.setCommentsMap(map);
    
    // 1. login
    boolean loginSuccess = login();
    
    if(loginSuccess){
      // 2. confirmReceipt
      confirmReceipt();
      
      // 3. comments production(s)
      
    }
    
   
    
    
    
    
    
  }

//  public void initData(CommentsInfo commentsInfo) {
//    Assert.notNull(commentsInfo);
//    commentsInfoList.add(commentsInfo);
//
//    if (logger.isDebugEnabled()) {
//      logger.debug("init comment data list size:" + commentsInfoList.size());
//    }
//  }

  private boolean login() {
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

      loginSuccess = login.login(commentsInfo.getUsername(), commentsInfo.getPassword(), Boolean.TRUE);

      if(loginSuccess) {
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
    }

    return loginSuccess;
  }

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

  @BeforeTest
  public void setup() {
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
//    
//    if(driver == null){
//
//      System.setProperty("webdriver.safari.driver", "/Applications/Safari.app/Contents/MacOS/safari");
//      
//      driver = new SafariDriver();
//    }

  }
  
}
