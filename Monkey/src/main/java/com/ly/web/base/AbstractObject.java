package com.ly.web.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.ly.model.SMSReceiverInfo;
import com.ly.suma.api.ApiService;
import com.ly.web.constant.Constant;
import com.ly.web.exceptions.AccountLockedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ly.web.lyd.YHD;
import com.ly.web.voice.VoicePlayer;


/**
 * Created by $authorName$ on 07/12/2016.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 15:03
 */
public abstract class AbstractObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  public WebDriver webDriver;

  /** DOCUMENT ME! */
  protected final Log logger = LogFactory.getLog(getClass());

  /** TODO: DOCUMENT ME! */
  protected String url;

  /** TODO: DOCUMENT ME! */
  protected String voiceFilePath = null;

  /** TODO: DOCUMENT ME! */
  protected VoicePlayer voicePlayer = null;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new AbstractObject object.
   */
  public AbstractObject() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for driver type.
   *
   * @return  String
   */
  public String getDriverType() {
    String driverType = SeleniumBaseObject.DRIVER_CHROME;

    if (webDriver instanceof ChromeDriver) {
      driverType = SeleniumBaseObject.DRIVER_CHROME;
    } else if (webDriver instanceof FirefoxDriver) {
      driverType = SeleniumBaseObject.DRIVER_FIREFOX;
    } else if (webDriver instanceof SafariDriver) {
      driverType = SeleniumBaseObject.DRIVER_SAFARI;
    } else if (webDriver instanceof InternetExplorerDriver) {
      driverType = SeleniumBaseObject.DRIVER_IE;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Find the driver type is: " + driverType);
    }

    return driverType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for url.
   *
   * @return  String
   */
  public String getUrl() {
    return url;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public WebDriver getWebDriver() {
    return webDriver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void logout() throws Exception { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for url.
   *
   * @param  url  String
   */
  public void setUrl(String url) {
    this.url = url;
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

  /**
   * DOCUMENT ME!
   *
   * @param  webDriver  DOCUMENT ME!
   */
  public void setWebDriver(WebDriver webDriver) {
    this.webDriver = webDriver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   handler  DOCUMENT ME!
   *
   * @return  dOCUMENT ME!
   */
  public WebDriver switchToWindow(String handler) {
    if (StringUtils.isNotEmpty(handler)) {
      return webDriver.switchTo().window(handler);
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  targetPartUrl  DOCUMENT ME!
   */
  public void switchWindow(String targetPartUrl) {
    for (String handle : this.webDriver.getWindowHandles()) {
      this.webDriver.switchTo().window(handle);

      if (this.webDriver.getCurrentUrl().contains(targetPartUrl)) {
        if (logger.isDebugEnabled()) {
          logger.debug("switchWindow:" + this.webDriver.getCurrentUrl());
        }

        break;
      }
    }

  }
  
  public void closeTabsExcept(String url){
    String activeTab = null;
    for (String tab : this.webDriver.getWindowHandles()) {
      WebDriver currentTab = webDriver.switchTo().window(tab);
      String currentUrl = currentTab.getCurrentUrl();
      
      if (!currentUrl.contains(url)) {
        if (logger.isDebugEnabled()) {
          logger.debug("Close tab: " + currentUrl);
        }
        currentTab.close();
      } else {
        activeTab = tab;
      }
    }
    
    activeTab = activeTab != null? activeTab : this.webDriver.getWindowHandles().iterator().next();
    if(activeTab != null){
      webDriver.switchTo().window(activeTab);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addStyleForElement.
   *
   * @param  element   WebElement
   * @param  cssStyle  String
   */
  protected void addStyleForElement(WebElement element, String cssStyle) {
    if ((null != webDriver) && (null != element)) {
      JavascriptExecutor jsEngine   = (JavascriptExecutor) webDriver;
      String             baseScript = "arguments[0].setAttribute('style', '%s')";
      String             script     = String.format(baseScript, cssStyle);
      jsEngine.executeScript(script, element);

      // wait 5 seconds
      webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
  }

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

// Thread.sleep(seconds * 1000);
      TimeUnit.SECONDS.sleep(seconds);

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<End delay " + seconds + " second(s).<<<<");
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * executeJavaScript.
   *
   * @param   script  String
   *
   * @return  Object
   */
  protected Object executeJavaScript(String script) {
    JavascriptExecutor jsEngine = (JavascriptExecutor) webDriver;

    return jsEngine.executeScript(script);
  } 
  
  protected Object executeJavaScript(String script, WebElement element) {
    JavascriptExecutor jsEngine = (JavascriptExecutor) webDriver;

    return jsEngine.executeScript(script, element);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   seleniumpath  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected WebElement findInputElementBySeleniumPath(String seleniumpath) {
    return this.webDriver.findElement(By.xpath("//input[@seleniumpath='" + seleniumpath + "']"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * hideElement.
   *
   * @param  element  WebElement
   */
  protected void hideElement(WebElement element) {
    if (logger.isDebugEnabled()) {
      logger.debug("Hide element: " + element.getTagName() + " " + element.getAttribute("class"));
    }

    addStyleForElement(element, "display:none;");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   url  DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  protected void navigateTo(String url) throws Exception {
    try {
      webDriver.navigate().to(url);

      if (logger.isDebugEnabled()) {
        logger.debug("Url navigateTo:" + url);
      }
    } catch (Exception e) {
      logger.error(e);
      throw e;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * playVoice.
   */
  protected void playVoice() {
    if (voicePlayer == null) {
      if ((voiceFilePath != null) && org.springframework.util.StringUtils.hasText(voiceFilePath)) {
        voicePlayer = new VoicePlayer(voiceFilePath);
      } else {
        voicePlayer = new VoicePlayer();
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("The voice was played");
    }

    voicePlayer.playLoop();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * refreshPage.
   */
  protected void refreshPage() {
    String script = "window.location.reload();";
    executeJavaScript(script);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * scrollOverflowY.
   *
   * @param  positionY  int
   */
  protected void scrollOverflowY(int positionY) {
    if (logger.isDebugEnabled()) {
      logger.debug("Scroll overflowY to: " + positionY);
    }

    executeJavaScript("scrollTo(0, " + positionY + ")");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * scrollToElementPosition.
   *
   * @param  element  WebElement
   */
  protected void scrollToElementPosition(WebElement element) {
    if (element != null) {
      int positionY = element.getLocation().getY();

      if (logger.isDebugEnabled()) {
        logger.debug("Will scroll to overflowY:" + positionY);
      }

      executeJavaScript("scrollTo(0, " + (positionY - 200) + ")");
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * stopVoice.
   */
  protected void stopVoice() {
    if (null != voicePlayer) {
      if (logger.isDebugEnabled()) {
        logger.debug("The voice was stop playing");
      }

      voicePlayer.stopLoop();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * waitForById.
   *
   * @param   elementId  String
   *
   * @return  Boolean
   */
  protected Boolean waitForById(final String elementId) {
    return (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return d.findElement(By.id(elementId)) != null;
          }
        });
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * waitForById.
   *
   * @param   elementId  String
   * @param   seconds    Integer
   *
   * @return  Boolean
   */
  protected Boolean waitForById(final String elementId, Integer seconds) {
    seconds = (seconds != null) ? seconds : 10;

    return (new WebDriverWait(this.webDriver, seconds)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return d.findElement(By.id(elementId)) != null;
          }
        });
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * waitForByXPath.
   *
   * @param   xpath  String
   *
   * @return  Boolean
   */
  protected Boolean waitForByXPath(final String xpath) {
    return (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return d.findElement(By.xpath(xpath)) != null;
          }
        });
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * waitForByXPath.
   *
   * @param   xpath    String
   * @param   seconds  Integer
   *
   * @return  Boolean
   */
  protected Boolean waitForByXPath(final String xpath, Integer seconds) {
    seconds = (seconds != null) ? seconds : 10;

    return (new WebDriverWait(this.webDriver, seconds)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return d.findElement(By.xpath(xpath)) != null;
          }
        });
  }

  protected void checkAccountIsLocked(String userName, String accountLockUrlPrefix) throws AccountLockedException {
    // verify after login, is the url redirect to account locked page.
    // account locked page: https://safe.jd.com/dangerousVerify/index.action?username=bN0UGt113 ...
    String currentUrl = this.webDriver.getCurrentUrl();
    logger.debug("After login success, the current url is: " +currentUrl);

    if(org.springframework.util.StringUtils.hasText(currentUrl) && currentUrl.startsWith(accountLockUrlPrefix)){
      logger.error("The username[" + userName + "] was locked.");
      throw new AccountLockedException(userName, " The username[" + userName + "] was locked.");
    }
  }
  
  protected String bindMobilePhoneToUnlockAccount(SMSReceiverInfo smsReceiverInfo, String orderPhoneNumber){
    String bindPhoneNum = null;
    if(this.webDriver.getCurrentUrl().contains(Constant.JD_ACCOUNT_LOCKED_URL_PREFIX)){
      WebElement historyMobileEl = null;
      try {
        historyMobileEl = this.webDriver.findElement(By.id("historyMobile"));

        WebElement bindMobilePhoneEl = this.webDriver.findElement(By.id("mobile"));
//        WebElement bindMobilePhoneBtn = this.webDriver.findElement(By.id("sendMobileCode"));
        WebElement codeEl = this.webDriver.findElement(By.id("code"));
        
        historyMobileEl.clear();
        historyMobileEl.sendKeys(orderPhoneNumber);


        if(!smsReceiverInfo.isLogin()){
          String token = ApiService.login(smsReceiverInfo.getUsername(), smsReceiverInfo.getPassword());
          if(token != null && org.springframework.util.StringUtils.hasText(token)){
            smsReceiverInfo.setToken(token);
          } else {
            logger.error("SMS Platform login failed....");
            return null;
          }
        }
        
        // get user infomation
        String userInfo = ApiService.getUserInfo(smsReceiverInfo.getUsername(), smsReceiverInfo.getToken());
        logger.info("SMS Platform user info: " + userInfo);
        String[] infos = userInfo.split(";");
        String score = infos[1];
        Integer balance = new Integer(infos[2]);
        String maxGetCount = infos[3];

        System.out.println("****************************INFO****************************");
        System.out.println(String.format("积分: %s, 余额: %s, 可同时获取号码数: %s", score, (balance/100.00), maxGetCount));
        System.out.println("************************************************************");
        
        String phoneNumber = getPhoneNumberFromPlatForm(smsReceiverInfo.getPid(), smsReceiverInfo, 0);
        bindMobilePhoneEl.clear();
        bindMobilePhoneEl.sendKeys(phoneNumber);
        
        
        // get the message from the phone number.
        String message = getVCodeFromPhoneNum(smsReceiverInfo, phoneNumber, 0);
        String vCodeFromSms = ApiService.getVCodeFromSms(message);
        codeEl.clear();
        codeEl.sendKeys(vCodeFromSms);
        
        String submitScriptTpl = "function func2(){ var retVal; $.ajaxSetup({async: false}); $.get('https://safe.jd.com/dangerousVerify/bindMobile.action?code=$code$&k=$k&historyMobile=$historyMobile$&t=$t&eid=$eid&fp=$fp'.replace('$k', $('#sendMobileCode').attr('href').match(/'(.*)'/)[1]).replace('$t',new Date().getTime()).replace('$eid', $('#eid').val()).replace('$fp', $('#fp').val()),"
          + "function(data){"
          + "retVal =  data ? data.resultCode+'|' + data.resultMessage : 'ERROR';"
          + "}, 'json'); return retVal;} return func2();";
        
        
        String submitScript =submitScriptTpl.replace("$code$", vCodeFromSms).replace("$historyMobile$", orderPhoneNumber);

        String resultCode = (String)executeJavaScript(submitScript);
        
        logger.info("---resultCode:" + resultCode);
        if(resultCode != null && resultCode.startsWith("0")){
          bindPhoneNum = phoneNumber;
          logger.info("The Phone number["+phoneNumber+"] has bind successfully.");
        }
        
      }catch(NoSuchElementException e){
        logger.error("This account has bind the phone");
      }
    }
    
    return bindPhoneNum;
  }
  
  protected String getVCodeFromPhoneNum(SMSReceiverInfo smsReceiverInfo, String phoneNumber, int index){
    String retMsg = null;
    String message = ApiService.getVcodeAndReleaseMobile(smsReceiverInfo.getUsername(), smsReceiverInfo.getToken(), phoneNumber, smsReceiverInfo.getAuthorID());
    if(message.startsWith(phoneNumber)){
      logger.info("SMS was received, message: " + message);
      retMsg = message.split("\\|")[1];
    } else if(message.contains("not_receive")) {
      if(index<=15){
        logger.warn("SMS receive error, will wait 2 seconds and try again.");
        delay(2);
        return getVCodeFromPhoneNum(smsReceiverInfo, phoneNumber, ++index);
      } else {
        logger.warn("The phone [" + phoneNumber + "] was not received message.");
      }
    }
    
    return retMsg;
  }
  
  
  protected String getPhoneNumberFromPlatForm(String pid, SMSReceiverInfo smsReceiverInfo, int index){
    String verifyPhoneScript="function getOne(){var retVal; $.ajaxSetup({async: false}); $.get('https://safe.jd.com/dangerousVerify/getCode.action?mobile=%2B0086$phone$&k=$k&t=$t'.replace('$k', ($('#sendMobileCode').attr('href')).match(/'(.*)'/)[1]).replace('$t', new Date().getTime()), function(data){var result = data.resultCode+'|'; result= data.resultCode == '0'? result+data.retryNum : result+data.resultMessage; retVal = result;}, 'json'); return retVal;} return getOne();";
    String[] phoneNumbers = ApiService.getMobileNums(pid, smsReceiverInfo.getUsername(), smsReceiverInfo.getToken(), 5);
    List<String> ignorePhones = new ArrayList<>();

    if(phoneNumbers != null){
      String foundedPhoneNum = null;
      for (String phoneNumber : phoneNumbers) {
        // verify is the phone number has been binded 
        String script = verifyPhoneScript.replace("$phone$", phoneNumber);
        String retInfo = (String)executeJavaScript(script);
        String resultCode = retInfo.split("\\|")[0];
        if("0".equalsIgnoreCase(resultCode)){
          foundedPhoneNum =  phoneNumber;
        } else {
          logger.error(retInfo);
          ignorePhones.add(phoneNumber);
        }
      }
      
      if(ignorePhones.size() > 0){
        String mobiles = org.springframework.util.StringUtils.arrayToDelimitedString(ignorePhones.toArray(), ",");
        ApiService.addIgnore(mobiles, smsReceiverInfo.getUsername(), smsReceiverInfo.getToken(), smsReceiverInfo.getPid());
      }
      
      if(foundedPhoneNum != null && org.springframework.util.StringUtils.hasText(foundedPhoneNum)){
        return foundedPhoneNum;
      }
    }
    
//    if(index <= 6){
//      delay(2);
//      return getPhoneNumberFromPlatForm(pid, smsReceiverInfo, ++index);
//    }
//    
//    return null;

    delay(2);
    return getPhoneNumberFromPlatForm(pid, smsReceiverInfo, ++index);
  }
  
  
  protected String getSKUUrl(String prefix, String sku){
    return String.format(prefix, sku);
  }
  
  
  protected void browseProduction(String sku, Integer browseTime) {
    if ((browseTime != null) && (browseTime > 0)) {
      if (logger.isDebugEnabled()) {
        logger.debug("Will browse this sku[" + sku + "] " + browseTime + " seconds.");
      }


      int        firstTime = 1000;
      final long stayTime  = browseTime * 1000;
      Timer timer     = new Timer();

      timer.scheduleAtFixedRate(new TimerTask() {
        private long    startTime;
        private boolean cancelFlag = Boolean.FALSE;
        private int     startPosition   = 1000;
        private int     count      = 0;
        private int[]   positionOffsets = new int[]{ 500, 800, 1500, 1700, 3400, 800};

        @Override public void run() {
          if (startTime <= 0) {
            startTime = this.scheduledExecutionTime();
          }
          cancelFlag = (System.currentTimeMillis() - startTime) >= stayTime;

          if (count < 6) {
            startPosition += positionOffsets[count];
            scrollOverflowY(startPosition);
          } else {
            cancelFlag = Boolean.TRUE;
          }

          count++;

          if (cancelFlag) {
            scrollOverflowY(0);
            timer.cancel();
            timer.purge();

            if (logger.isDebugEnabled()) {
              logger.debug("The timer is cancelled.");
            }
          }
        } // end method run
      }, firstTime, 5000);

      delay(browseTime + 1);

      if (logger.isDebugEnabled()) {
        logger.debug("Browse the production page " + browseTime + " seconds, Done!");
      }

    } // end if
  }   // end method browseProduction


  protected boolean isCurrentPage(String currentWebDriveUrl, String currentPageUrl){
    return currentWebDriveUrl.contains(currentPageUrl);
  }
  
  protected void closeJDPopupWindow(){
    executeJavaScript("$($('.ui-dialog-close')[0]).click()");
  }

} // end class AbstractObject
