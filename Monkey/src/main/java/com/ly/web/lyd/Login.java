package com.ly.web.lyd;

import com.ly.web.constant.Constant;
import com.ly.web.voice.VoicePlayer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ly.web.base.AbstractObject;
import org.springframework.util.StringUtils;


/**
 * Created with IntelliJ IDEA. User: yongliu Date: 8/20/14 Time: 12:21 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class Login extends AbstractObject {
  
  public static final String MY_ORDER_MENU_XPATH="//li[@id='glHdMyYhd']//a[@data-ref='YHD_TOP_order']";

  /***
   * if the valid code shows playSounds = true, then will play the sound to attention user to enter valid code
   * else will not play voice.
   * default is TRUE.
   */
  private Boolean allowPlayVoice = Boolean.TRUE;
  
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new Login object.
   */
  public Login() { }

  /**
   * Creates a new Login object.
   *
   * @param  webDriver  DOCUMENT ME!
   * @param  url        DOCUMENT ME!
   */
  public Login(WebDriver webDriver, String url) {
    this.webDriver = webDriver;
    this.url       = url;
  }

  public Login(WebDriver webDriver, String url, String voiceFilePath) {
    this.webDriver = webDriver;
    this.url       = url;
    this.voiceFilePath = voiceFilePath;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   userName  DOCUMENT ME!
   * @param   password  DOCUMENT ME!
   * @param   first     DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public Boolean login(String userName, String password, boolean first) throws Exception {
    Boolean loginSuccessfully = Boolean.TRUE;
    try{
      
//      String loginUrl = getLoginUrFromIndexPage();
      
      if (first) {
        this.webDriver.get(this.url);
      }

      if (logger.isDebugEnabled()) {
        logger.debug(">>>>>>>>>Begin Login YHD>>>>>>>>>>>");
      }

      if (!webDriver.getCurrentUrl().contains("login")) {
        navigateTo(this.url);
      }

      (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return null != d.findElement(By.id("un"));
        }
      });

      WebElement username = this.webDriver.findElement(By.id("un"));
      username.clear();
      username.sendKeys(userName);

      WebElement pwd = this.webDriver.findElement(By.id("pwd"));
      pwd.clear();
      pwd.sendKeys(password);

      WebElement loginButton = this.webDriver.findElement(By.id("login_button"));

      // validator code
      WebElement vCodeDive = this.webDriver.findElement(By.id("vcd_div"));

      logger.info("vcode div is display:" + vCodeDive.isDisplayed());

      // click login button
      loginButton.click();

      delay(2);

      // 如果browser 让用户连续几次输入"验证码", count ++
      // 如果不是连续输入,则忽略
      if (webDriver.getCurrentUrl().contains("login")) {
        logger.info("After click 'Submit' the vcode div isDisplay:" + vCodeDive.isDisplayed());

        if (vCodeDive.isDisplayed()) {
          logger.info("主人, 赶紧人工打码......");
          String  driverType = getDriverType();
          Integer count      = YHD.vCodeCountMap.get(driverType);

          if (count == null) {
            count = 1;
          } else {
            count++;
          }

          YHD.vCodeCountMap.put(driverType, count);

          // start play voice
          if(allowPlayVoice){
           playVoice();
          }
        }
      } else {
        // no need input valid code.
        String driverType = getDriverType();

        if ((driverType != null) && (YHD.vCodeCountMap.get(driverType) != null)) {
          YHD.vCodeCountMap.put(driverType, 0);
        }
      }

      loginSuccessfully = (new WebDriverWait(this.webDriver, 3000)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return (!d.getCurrentUrl().contains("login"));
        }
      });

      // stop to play voice
      if (loginSuccessfully && (voicePlayer != null)) {
        if (logger.isDebugEnabled()) {
          logger.debug("Stop to play the warn voice.");
        }

        stopVoice();
      }

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<<<<Login YHD Successfully with:[" + userName + "]>>>>>>>>>>>");
      }
    }catch (Exception e){
      loginSuccessfully = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    // check the account locked or not
    // ToDO change JD_ACCOUNT_LOCKED_URL_PREFIX this url
    checkAccountIsLocked(userName, Constant.JD_ACCOUNT_LOCKED_URL_PREFIX);
    
    return loginSuccessfully;
  } // end method login

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   */
  @Override public void logout() {
    webDriver.quit();
  }
  
  
  public String getLoginUrFromIndexPage(){
    String loginUrlFromClickMyOrder = null;

    try{
      if(!webDriver.getCurrentUrl().equalsIgnoreCase(Constant.YHD_INDEX_PAGE_URL)){
        navigateTo(Constant.YHD_INDEX_PAGE_URL);  
      }

      logger.debug("Ready found 'My Order Menu' element by xpath: " + MY_ORDER_MENU_XPATH);
      
      WebElement myOrderMenuEle = ExpectedConditions.presenceOfElementLocated(By.xpath(MY_ORDER_MENU_XPATH)).apply(
        webDriver);
      
      if(myOrderMenuEle != null){
        String href = myOrderMenuEle.getAttribute("href");
        
        logger.debug("Login Url:" + href);

        loginUrlFromClickMyOrder = href;
        
      }
    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }
    
    return loginUrlFromClickMyOrder;
  } 
} // end class Login
