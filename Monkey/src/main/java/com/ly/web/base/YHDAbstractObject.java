package com.ly.web.base;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
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

  protected void refreshPage(){
    String             script = "window.location.reload();";
    executeJavaScript(script);
  }

} // end class YHDAbstractObject
