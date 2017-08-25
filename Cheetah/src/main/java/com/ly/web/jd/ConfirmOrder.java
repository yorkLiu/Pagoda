package com.ly.web.jd;

import com.ly.web.command.ItemInfoCommand;
import com.ly.web.constant.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import com.ly.web.base.AbstractObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by yongliu on 8/16/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/16/2017 15:05
 */
public class ConfirmOrder extends AbstractObject {
  //~ Constructors -----------------------------------------------------------------------------------------------------
  
  private final int SHOPPING_CAR_PAGE = 2;

  
  private final String SHOPPING_CAR_ITEM_LIST_XPATH = "//div[contains(@class, 'item-item')]";
  private final String SHOPPING_CAR_ITEM_BUY_COUNT_FIELD_XPATH="//div[@id='product_%s']/div/div[contains(@class, 'p-quantity')]/div[contains(@class, 'quantity-form')]/input";
  private final String SHOPPING_CAR_SUBMIT_BTN_XPATH="//a[contains(@class, 'submit-btn')][contains(text(), '去结算')]";
  private final String SHOPPING_CAR_CHECK_ALL_BTN_XPATH="//div[contains(@class, 'cart-thead')]/div[contains(@class, 't-checkbox')]/div/input";
  private final String SHOPPING_CAR_ITEM_CHECK_BOX_XPATH="//div[@id='product_%s']/div/div[contains(@class, 'p-checkbox')]/div[contains(@class, 'cart-checkbox')]/input";
  

  /**
   * Creates a new ConfirmOrder object.
   */
  public ConfirmOrder() { }


  /**
   * Creates a new ConfirmOrder object.
   *
   * @param  driver  WebDriver
   */
  public ConfirmOrder(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }

  public boolean confirmOrder(Set<ItemInfoCommand> itemSet, String username, String password) {
    boolean success = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug(".......confirm order.......");
    }

    if (!webDriver.getCurrentUrl().contains(Constant.JD_SHOPPING_CAR_URL)) {
      webDriver.get(Constant.JD_SHOPPING_CAR_URL);
    }

    delay(5);

    List<WebElement> shoppingCarItems = getItemsInShoppingCar();

    if (shoppingCarItems != null) {
      if (shoppingCarItems.size() == itemSet.size()) {
        success = submitOrder(shoppingCarItems, itemSet,username, password);
      } else {
        success = preSelectAndSubmitItem(shoppingCarItems, itemSet, username, password);
      }
    } else {
      logger.warn(
        ">>>>>>>>>Could not submit this order, because there is not any one production item in shopping car>>>>>>>>");
    }
    
    // check if the url is redirect to confirm page, return true flag.
    if (!success && !isCurrentPage(webDriver.getCurrentUrl(), Constant.JD_SHOPPING_CAR_URL)){
      success = Boolean.TRUE;
    }

    return success;
  } // end method confirmOrder


  private List<WebElement> getItemsInShoppingCar() {
    List<WebElement> items = null;

    if (logger.isDebugEnabled()) {
      logger.debug("Count how many production items in shopping car");
    }

    try {
      Boolean founded = waitForByXPath(SHOPPING_CAR_ITEM_LIST_XPATH, 10);

      if (founded) {
        items = webDriver.findElements(By.xpath(SHOPPING_CAR_ITEM_LIST_XPATH));

        if (logger.isDebugEnabled()) {
          logger.debug("Found " + items.size() + " item(s)");
        }

      }

    } catch (TimeoutException e) {

      if (isCurrentPage(webDriver.getCurrentUrl(), Constant.JD_SHOPPING_CAR_URL)) {
        return getItemsInShoppingCar(); 
      }
    }

    return items;
  } // end method getItemsInShoppingCar



  private Boolean submitOrder(List<WebElement> checkedItems, Set<ItemInfoCommand> itemSet, String  username, String password) {
    Boolean success = Boolean.TRUE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("There are " + checkedItems.size()
          + " item(s) were checked in shopping car, and expected checked size is: " + itemSet.size());
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Checking the sku by number....");
      }

      for (ItemInfoCommand itemInfoCommand : itemSet) {
        String sku   = itemInfoCommand.getSku();
        String count = itemInfoCommand.getCount().toString();

        if (logger.isDebugEnabled()) {
          logger.debug("Checking sku[" + sku + "] buy number");
        }

        String xpath = String.format(SHOPPING_CAR_ITEM_BUY_COUNT_FIELD_XPATH, sku);

        try {
          WebElement numInputEle   = webDriver.findElement(By.xpath(xpath));
          String     numberVal     = numInputEle.getAttribute("value");
          String     numberFieldId = numInputEle.getAttribute("id");

          if (!numberVal.equalsIgnoreCase(count)) {
            if (logger.isDebugEnabled()) {
              logger.debug("The sku[" + sku + "] number is:" + numberVal + ", should be: " + count
                + ", correct the number to: " + count);
            }

            String script = String.format("$('#%s').val('')", numberFieldId);
            executeJavaScript(script);

            scrollToElementPosition(numInputEle);
            delay(2);
            numInputEle.sendKeys(count);
            numInputEle.sendKeys(Keys.ENTER);

            if (logger.isDebugEnabled()) {
              logger.debug("Correct sku[" + sku + "] number from: " + numberVal + " to: " + count + " successfully.");
            }

            (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
              @Override public Boolean apply(WebDriver d) {
                return !d.findElement(By.xpath(SHOPPING_CAR_SUBMIT_BTN_XPATH)).getAttribute("class").contains("cart3_btn_dis");
              }
            });
          }
        } catch (NoSuchElementException e) {
          continue;
        } // end try-catch
      } // end for

      // click 'submit'
      success = submitBtnClick(username, password, 0);

    } catch (Exception e) {
      success = Boolean.FALSE;
    } // end try-catch

    return success;
  } // end method submitOrder


  private boolean submitBtnClick(String username, String password, int index){
    Boolean success = Boolean.FALSE;
    try{
      if (logger.isDebugEnabled()) {
        logger.debug("Ready to click '去结算' button");
      }

      delay(3);

      WebElement submitBtnEle = ExpectedConditions.elementToBeClickable(By.xpath(SHOPPING_CAR_SUBMIT_BTN_XPATH))
        .apply(webDriver);
      if(submitBtnEle != null){
        scrollToElementPosition(submitBtnEle);
        delay(3);

        submitBtnEle.click();

        delay(3);

        // check is there should be login first
        loginJDinEmbedLoginPage(username, password, SHOPPING_CAR_PAGE);

        stopVoice();
      }

      success = (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return (!d.getCurrentUrl().contains("cart"));
        }
      });
    } catch (TimeoutException ex){
      if (index > 2){
        logger.info("-----refresh page....");
        refreshPage();
        delay(4);
      }
      return submitBtnClick(username, password, ++index);
    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }

    return success;
  }

  private Boolean preSelectAndSubmitItem(List<WebElement> shoppingCarItems, Set<ItemInfoCommand> itemSet, String username, String password) {
    Boolean success = Boolean.TRUE;

    try {
      List<WebElement> selectedItem = new ArrayList<>(itemSet.size());

      if (logger.isDebugEnabled()) {
        // 1. un-check "select all"
        logger.debug("Read find 'Check All' check box by ID: " + SHOPPING_CAR_CHECK_ALL_BTN_XPATH);
      }

      WebElement checkAllEle = ExpectedConditions.presenceOfElementLocated(By.xpath(SHOPPING_CAR_CHECK_ALL_BTN_XPATH)).apply(
        webDriver);

      if (checkAllEle != null) {
        Boolean isChecked = (Boolean) executeJavaScript("return arguments[0].checked", checkAllEle);
//        String isChecked = checkAllEle.getAttribute("data-checked");

        if (isChecked) {
          scrollToElementPosition(checkAllEle);
          delay(2);
          checkAllEle.click();

          if (logger.isDebugEnabled()) {
            logger.debug("The 'Check All' check box was unchecked.");
          }

          delay(2);
        } else {
          if (logger.isDebugEnabled()) {
            logger.debug("The 'Check All' was unchecked, not need click");
          }
        }
      }

      // 2. check the items which in @itemSet
      for (ItemInfoCommand itemInfoCommand : itemSet) {
        String sku = itemInfoCommand.getSku();

        if (logger.isDebugEnabled()) {
          logger.debug("Ready select sku[" + sku + "]");
        }

        String xpath = String.format(SHOPPING_CAR_ITEM_CHECK_BOX_XPATH, sku);

        if (logger.isDebugEnabled()) {
          logger.debug("Find sku[" + sku + "] check box by xpath: " + xpath);
        }

        try {
          WebElement checkBoxEle   = webDriver.findElement(By.xpath(xpath));
          Boolean itemCheckFlag = (Boolean) executeJavaScript("return arguments[0].checked", checkBoxEle);

          if (!itemCheckFlag) {
            scrollToElementPosition(checkBoxEle);
            delay(2);
            checkBoxEle.click();

            if (logger.isDebugEnabled()) {
              logger.debug("The sku[" + sku + "] was checked");
            }
          }

          WebElement parentEle = checkBoxEle.findElement(By.xpath("../../../.."));
          selectedItem.add(parentEle);

        } catch (NoSuchElementException e) {
          logger.error(e.getMessage(), e);

          continue;
        }
      } // end for

      if (selectedItem.size() > 0) {
        success = submitOrder(selectedItem, itemSet, username, password);
      } else {
        logger.warn(">>>>>Not check any items in shopping car page>>>>>>");
      }

    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch

    return success;
  } // end method preSelectAndSubmitItem


  protected Boolean loginJDinEmbedLoginPage(String username, String password, int pageId){
    Boolean success = Boolean.TRUE;

    try {
      String     loginFormDivId  = "loginDialogBody";
      WebElement loginFormDivEle = this.webDriver.findElement(By.id(loginFormDivId));

      if ((loginFormDivEle != null) && loginFormDivEle.isDisplayed()) {

        
        
        this.webDriver.switchTo().frame(this.webDriver.findElement(By.id("dialogIframe")));

        // switch to account login
        WebElement accountLogin = ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), '账户登录')]"))
          .apply(webDriver);

        WebElement usernameEle = ExpectedConditions.presenceOfElementLocated(By.id("loginname")).apply(webDriver);

        if (usernameEle != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Enter username:" + username);
          }

          usernameEle.clear();
          usernameEle.sendKeys(username);
        }

        WebElement passwordEle = ExpectedConditions.presenceOfElementLocated(By.id("nloginpwd")).apply(webDriver);

        if (passwordEle != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Enter password:XXXXX");
          }

          passwordEle.clear();
          passwordEle.sendKeys(password);
        }

        WebElement loginBtnEle = ExpectedConditions.presenceOfElementLocated(By.id("loginsubmit")).apply(webDriver);

        if (loginBtnEle != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Click 'Login' button");
          }

          loginBtnEle.click();
        }

        delay(2);

        try {
          // validator code
          WebElement vCodeDive = this.webDriver.findElement(By.id("o-authcode"));
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
      logger.info("The User:" + username + " was logged, no need login.");
    } // end try-catch


    return success;
  }


}
