package com.ly.web.jd;

import com.ly.exceptions.HistoryPhoneIncorrectException;
import com.ly.exceptions.NotReceiveMessageException;
import com.ly.exceptions.SendSmsFrequencyException;
import com.ly.exceptions.SendSmsOutOfMaxCountException;
import com.ly.exceptions.UnknownPhoneNumberException;
import com.ly.model.SMSReceiverInfo;
import com.ly.web.constant.Constant;
import com.ly.web.exceptions.AccountLockedException;
import com.ly.web.voice.VoicePlayer;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ly.web.base.AbstractObject;
import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 8/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/02/2016 15:55
 */
public class Login extends AbstractObject {
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /***
   * if the valid code shows playSounds = true, then will play the sound to attention user to enter valid code
   * else will not play voice.
   * default is TRUE.
   */
  private Boolean playVoice = Boolean.TRUE;
  
  private String voiceFilePath = null;

  private VoicePlayer voicePlayer = null;
  
  private static final String SWITCH_TO_ACCOUNT_LOGIN_XPATH="//a[contains(text(), '账户登录')]";

  /**
   * Creates a new Login object.
   */
  public Login() { }

  /**
   * Creates a new Login object.
   *
   * @param  webDriver  WebDriver
   * @param  url        String
   */
  public Login(WebDriver webDriver, String url) {
    this.webDriver = webDriver;
    this.url       = url;
  }

  public Login(WebDriver webDriver, String url, String voiceFilePath) {
    this.webDriver = webDriver;
    this.url       = url;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------
  
  public boolean checkPageCssLoaded(int index){
    if(index > 5){
      throw new TimeoutException("The Login page css was not loaded, login failed.");
    }
    
    try {
      WebElement vCodeDive = this.webDriver.findElement(By.id("o-authcode"));
      if(vCodeDive.isDisplayed()){
        String vcodepng = (String)executeJavaScript("return $('#JD_Verification1').attr('src')");
        if(vcodepng != null && StringUtils.hasText(vcodepng)){
          return Boolean.TRUE;
        }
        
        throw new TimeoutException("The Login page css was not loaded, login failed.");
      }
    }catch (Exception e){
      if(isCurrentPage(webDriver.getCurrentUrl(), "login")){
        refreshPage();
        delay(3);
        return checkPageCssLoaded(++index);
      }
    }
   
    return Boolean.FALSE;
  }
  

  /**
   * login.
   *
   * @param   userName  String
   * @param   password  String
   * @param   first     boolean
   *
   * @return  login.
   *
   * @throws  Exception  exception
   */
  public boolean login(String userName, String password, boolean first) throws Exception {
    boolean loginSuccess = Boolean.TRUE;

    try {
      if (first) {
        navigateTo(this.url);
      }

      if (logger.isDebugEnabled()) {
        logger.debug(">>>>>>>>>Begin Login JD>>>>>>>>>>>");
      }

      if (!webDriver.getCurrentUrl().contains("login")) {
        navigateTo(this.url);
      }
      
      delay(5);

      checkPageCssLoaded(0);

      if (logger.isDebugEnabled()) {
        logger.debug("Switch to account login.");
      }

      // switch to account login
      WebElement accountLogin = ExpectedConditions.presenceOfElementLocated(By.xpath(SWITCH_TO_ACCOUNT_LOGIN_XPATH))
        .apply(webDriver);

      if (accountLogin != null) {
        accountLogin.click();
      }
      
      (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
            @Override public Boolean apply(WebDriver d) {
              return null != d.findElement(By.id("loginname"));
            }
          });

      WebElement username = this.webDriver.findElement(By.id("loginname"));
      username.clear();
      username.sendKeys(userName);

      WebElement pwd = this.webDriver.findElement(By.id("nloginpwd"));
      pwd.clear();
      pwd.sendKeys(password);

      WebElement loginButton = this.webDriver.findElement(By.id("loginsubmit"));

      // validator code
      WebElement vCodeDive = this.webDriver.findElement(By.id("o-authcode"));
// WebElement vCodeInput = this.webDriver.findElement(By.id("authcode"));

      logger.info("vcode div is display:" + vCodeDive.isDisplayed());


      // click login button
      loginButton.click();

      delay(2);


      // need check the account is locked by jd
      // if locked, skip
      // ToDo


      // 如果browser 让用户连续几次输入"验证码", count ++
      // 如果不是连续输入,则忽略
      if (webDriver.getCurrentUrl().contains("login")) {
        logger.info("After click 'Submit' the vcode div isDisplay:" + vCodeDive.isDisplayed());

        if (vCodeDive.isDisplayed()) {
          logger.info("主人, 赶紧人工打码......");
          String  driverType = getDriverType();
          Integer count      = JD.vCodeCountMap.get(driverType);

          if (count == null) {
            count = 1;
          } else {
            count++;
          }

          JD.vCodeCountMap.put(driverType, count);

          // start play voice
          if(playVoice){
            if(voicePlayer == null){
              
              if(voiceFilePath != null && StringUtils.hasText(voiceFilePath)){
                voicePlayer = new VoicePlayer(voiceFilePath);
              } else {
                voicePlayer = new VoicePlayer();
              }
              
            }
            voicePlayer.playLoop();
          }
        } else {
          
          // if there is no vcode display, but after click the 'submit' button 30 seconds,  still stay the login page
          // then throw a timeout exception

          new WebDriverWait(this.webDriver, 30).until(new ExpectedCondition<Boolean>() {
            @Override public Boolean apply(WebDriver d) {
              return (!d.getCurrentUrl().contains("login"));
            }
          });
          
        }
      } else {
        // no need input valid code.
        String driverType = getDriverType();

        if ((driverType != null) && (JD.vCodeCountMap.get(driverType) != null)) {
          JD.vCodeCountMap.put(driverType, 0);
        }
      }

      loginSuccess = (new WebDriverWait(this.webDriver, 3000)).until(new ExpectedCondition<Boolean>() {
            @Override public Boolean apply(WebDriver d) {
              return (!d.getCurrentUrl().contains("login"));
            }
          });

      // stop to play voice
      if (loginSuccess && (voicePlayer != null)) {
        if (logger.isDebugEnabled()) {
          logger.debug("Stop to play the warn voice.");
        }

        voicePlayer.stopLoop();
      }

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<<<<Login JD Successfully with:[" + userName + "]>>>>>>>>>>>");
      }
    } catch (TimeoutException e) {
      logger.error(e.getMessage(), e);
      loginSuccess = Boolean.FALSE;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      loginSuccess = Boolean.FALSE;
    } // end try-catch

    // check the account locked or not
    checkAccountIsLocked(userName, Constant.JD_ACCOUNT_LOCKED_URL_PREFIX);

    return loginSuccess;
  } // end method login
  
  
  public String unlockAccount(SMSReceiverInfo smsReceiverInfo, String orderPhoneNum) throws SendSmsFrequencyException,
    NotReceiveMessageException, HistoryPhoneIncorrectException, UnknownPhoneNumberException, 
    SendSmsOutOfMaxCountException {
    return bindMobilePhoneToUnlockAccount(smsReceiverInfo, orderPhoneNum);
  }
} // end class Login
