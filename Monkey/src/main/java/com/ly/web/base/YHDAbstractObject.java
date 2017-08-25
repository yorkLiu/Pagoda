package com.ly.web.base;

import org.apache.xpath.operations.Bool;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import com.ly.web.constant.Constant;
import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 7/12/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 17:12
 */
public abstract class YHDAbstractObject extends AbstractObject {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  private static final int YHT_SKU_PAGE=1;
  private static final int NORMAL_SKU_PAGE=2;
  private static final int SHOPPING_CAR_PAGE=3;
  

  /**
   * checkWelcomeShopping.
   */
  public void checkWelcomeShopping() {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Check is there popped up welcome to shopping window.");
      }

      WebElement startShoppingBtn = webDriver.findElement(By.id("startShopping"));

      if (logger.isDebugEnabled()) {
        logger.debug("Welcome to shopping window popped up.");
        logger.debug("Ready click the 'Start Shopping' button.");
      }

      startShoppingBtn.click();

      if (logger.isDebugEnabled()) {
        logger.debug("The welcome shopping window was closed.");
      }

      Thread.sleep(1000);

    } catch (NoSuchElementException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("No pop-up welcome to shopping in YHD window");
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } // end try-catch

  } // end method checkWelcomeShopping
  
  /**
   * checkNewUserPopUp.
   */
  public void checkNewUserPopUp() {
    try {
      WebElement closeNewUserBtn = webDriver.findElement(By.xpath(
            "//div[@id='newUserPopup']//a[contains(@class, 'close_btn')]"));

      if (closeNewUserBtn != null) {
        closeNewUserBtn.click();
      }

      if (logger.isDebugEnabled()) {
        logger.debug("The new user adv pop-up window was closed.");
      }
    } catch (NoSuchElementException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("This user is not a new user, no pop-up window.");
      }
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for user id.
   *
   * @return  String
   */
  public String getUserId() {
    String userId = null;

    // in myOrder page
    try {
      WebElement ele = webDriver.findElement(By.id("myorderUserId"));

      if (ele != null) {
        userId = ele.getAttribute("value");

        if (logger.isDebugEnabled()) {
          logger.debug("UserId is: " + userId);
        }
      }
    } catch (NoSuchElementException e) {
      logger.error(e.getMessage());
    }

    return userId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for write comment url.
   *
   * @param   orderId  String
   * @param   userId   String
   *
   * @return  String
   */
  public String getWriteCommentUrl(String orderId, String userId) {
    Assert.notNull(orderId);
    Assert.notNull(userId);

    if (logger.isDebugEnabled()) {
      logger.debug("Order id#" + orderId + " and userInfoId#" + userId);
    }

    String writeCommentUrl = String.format(Constant.YHD_ORDER_WRITE_COMMENT_URL, orderId, userId);

    if (logger.isDebugEnabled()) {
      logger.debug("The comment page url is:" + writeCommentUrl);
    }

    return writeCommentUrl;
  }

  /**
   * Generate YHD item url with sku parameter.
   * @param sku
   * @return
   */
  protected String getProductionUrl(String sku){
    if(sku != null && StringUtils.hasText(sku)){
      return Constant.NONE_SKU_KEY_PREFIX + sku;
    }
    return null;
  }
  
  
  /**
   * closePopUpWinByXpath.
   *
   * @param   xpath  String
   *
   * @return  boolean
   */
  protected boolean closePopUpWinByXpath(String xpath) {
    Boolean closed = Boolean.TRUE;
    logger.debug("..........Ready close pop up window......");
    try{

      logger.debug("Find pop up window by xpath: " + xpath);

      Boolean foundedPopUpWin = waitForByXPath(xpath, 10);
      if(foundedPopUpWin){
        WebElement closeBtn = ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)).apply(webDriver);  
        
        logger.debug("Ready fire click close button");
        closeBtn.click();
      }
    }catch (NoSuchElementException e){
      closed = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } catch (TimeoutException e){
      closed = Boolean.FALSE;
      logger.error("The pop up window does not popped up");
      logger.error(e.getMessage(), e);
    }
    return closed;
  }

  
  
  protected Boolean loginYHDinEmbedLoginPage(String username, String password, int pageId){
    Boolean success = Boolean.TRUE;

    try {
      String     loginFormDivId  = "mod_login_pop_wrap";
      WebElement loginFormDivEle = this.webDriver.findElement(By.id(loginFormDivId));

      if ((loginFormDivEle != null) && loginFormDivEle.isDisplayed()) {
        this.webDriver.switchTo().frame(this.webDriver.findElement(By.id("loginIframe")));

        WebElement usernameEle = ExpectedConditions.presenceOfElementLocated(By.id("un")).apply(webDriver);

        if (usernameEle != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Enter username:" + username);
          }

          usernameEle.clear();
          usernameEle.sendKeys(username);
        }

        WebElement passwordEle = ExpectedConditions.presenceOfElementLocated(By.id("pwd")).apply(webDriver);

        if (passwordEle != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Enter password:XXXXX");
          }

          passwordEle.clear();
          passwordEle.sendKeys(password);
        }

        WebElement loginBtnEle = ExpectedConditions.presenceOfElementLocated(By.id("login_button")).apply(webDriver);

        if (loginBtnEle != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Click 'Login' button");
          }

          loginBtnEle.click();
        }

        delay(2);

        try {
          // validator code
          WebElement vCodeDive = this.webDriver.findElement(By.id("vcd_item"));
          logger.info("vcode div is display:" + vCodeDive.isDisplayed());

          if (vCodeDive.isDisplayed()) {
            logger.info("主人, 赶紧人工打码......");

            // play the warning voice
            playVoice();
          }
        }catch (NoSuchElementException e){
          logger.info("Not show the valid code. No need input.");
        } catch (Exception e){
          logger.error(e.getMessage());
        }
        
        
        switch (pageId){
          case YHT_SKU_PAGE:{
            success = (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
              @Override public Boolean apply(WebDriver d) {
                return (d.getCurrentUrl().contains("cart"));
              }
            });
            break;
          }
          case SHOPPING_CAR_PAGE:{
            success = (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
              @Override public Boolean apply(WebDriver d) {
                return !(d.getCurrentUrl().contains("cart"));
              }
            });
            break;
          }

          // default contains NORMAL_SKU_PAGE
          default:{
            success = (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
              @Override public Boolean apply(WebDriver d) {
                Boolean isLoginPageDisplayed = Boolean.TRUE;
                try{
                  isLoginPageDisplayed = d.findElement(By.id(loginFormDivId)).isDisplayed();
                }catch (NoSuchElementException e){
                  isLoginPageDisplayed = Boolean.FALSE;
                }
                return !isLoginPageDisplayed;
              }
            });
          }
        }

        if (success) {
          // stop play the warning voice
          stopVoice();
        }
      } // end if

    } catch (NoSuchElementException e) {
      success = Boolean.TRUE;
      logger.error(e.getMessage());
      logger.info("The User:" + username + " was logged, no need login.");
    } // end try-catch
    
    
    return success;
  }

  /**
   * loginYHDWithPopupForm.
   *
   * @param   username  String
   * @param   password  String
   *
   * @return  Boolean
   */
  protected Boolean loginYHDWithPopupForm(String username, String password) {
    return loginYHDinEmbedLoginPage(username, password, YHT_SKU_PAGE);
  } // end method loginYHDWithPopupForm

  protected Boolean loginYHDWithPopupFormInShoppingCar(String username, String password) {
    return loginYHDinEmbedLoginPage(username, password, SHOPPING_CAR_PAGE);
  } // end method loginYHDWithPopupForm

  protected Boolean loginYHDWithPopupFormInNormalPage(String username, String password) {
    return loginYHDinEmbedLoginPage(username, password, NORMAL_SKU_PAGE);
  }

} // end class YHDAbstractObject
