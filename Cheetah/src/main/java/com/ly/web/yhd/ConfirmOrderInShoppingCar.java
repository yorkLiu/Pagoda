package com.ly.web.yhd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 10/14/16.
 *
 * @author      <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version     10/14/2016 14:23
 * @descripton  Shopping Car
 */
public class ConfirmOrderInShoppingCar extends YHDAbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String SHOPPING_CAR_ITEM_LIST_XPATH            =
    "//div[@id='itemList']//ul[contains(@class, 'cart3_list')]";
  private static final String SHOPPING_CAR_CHECK_ALL_BTN_ID           = "all_checked";
  private static final String SHOPPING_CAR_ITEM_CHECK_BOX_XPATH       =
    "//li/a[@name='cart2Checkbox'][contains(@value, '%s')]";
  private static final String SHOPPING_CAR_ITEM_BUY_COUNT_FIELD_XPATH =
    "//li/div/input[@name='itemNumBox'][contains(@cartitemvoid, '%s')]";
  public static final String SHOPPING_CAR_SUBMIT_BTN_XPATH           =
    "//a[@data-tpa='GO_TO_CHECKOUT'][contains(text(), '去结算')]";

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ConfirmOrderInShoppingCar object.
   */
  public ConfirmOrderInShoppingCar() { }

  /**
   * Creates a new ConfirmOrderInShoppingCar object.
   *
   * @param  webDriver  WebDriver
   */
  public ConfirmOrderInShoppingCar(WebDriver webDriver) {
    Assert.notNull(webDriver);
    this.webDriver = webDriver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * confirmOrder.
   *
   * @param   itemSet  Set
   *
   * @return  confirmOrder.
   */
  public boolean confirmOrder(Set<ItemInfoCommand> itemSet, String username, String password) {
    boolean success = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug(".......confirm order.......");
    }

    if (!webDriver.getCurrentUrl().contains(Constant.YHD_SHOPPING_CAR_URL)) {
      webDriver.get(Constant.YHD_SHOPPING_CAR_URL);
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


    return success;
  } // end method confirmOrder

  //~ ------------------------------------------------------------------------------------------------------------------

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
      return getItemsInShoppingCar();
    }

    return items;
  } // end method getItemsInShoppingCar

  //~ ------------------------------------------------------------------------------------------------------------------

  private Boolean preSelectAndSubmitItem(List<WebElement> shoppingCarItems, Set<ItemInfoCommand> itemSet, String username, String password) {
    Boolean success = Boolean.TRUE;

    try {
      List<WebElement> selectedItem = new ArrayList<>(itemSet.size());

      if (logger.isDebugEnabled()) {
        // 1. un-check "select all"
        logger.debug("Read find 'Check All' check box by ID: " + SHOPPING_CAR_CHECK_ALL_BTN_ID);
      }

      WebElement checkAllEle = ExpectedConditions.presenceOfElementLocated(By.id(SHOPPING_CAR_CHECK_ALL_BTN_ID)).apply(
          webDriver);

      if (checkAllEle != null) {
        String isChecked = checkAllEle.getAttribute("data-checked");

        if ("yes".equalsIgnoreCase(isChecked)) {
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
          String     itemCheckFlag = checkBoxEle.getAttribute("data-checked");

          if ("no".equalsIgnoreCase(itemCheckFlag)) {
            scrollToElementPosition(checkBoxEle);
            delay(2);
            checkBoxEle.click();

            if (logger.isDebugEnabled()) {
              logger.debug("The sku[" + sku + "] was checked");
            }
          }

          WebElement parentEle = checkBoxEle.findElement(By.xpath("../.."));
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

  //~ ------------------------------------------------------------------------------------------------------------------

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
      success = submitBtnClick(username, password);

    } catch (Exception e) {
      success = Boolean.FALSE;
    } // end try-catch

    return success;
  } // end method submitOrder
  
  private boolean submitBtnClick(String username, String password){
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
        loginYHDWithPopupFormInShoppingCar(username, password);

//        // check is there any other error message box popped up.
//
//        (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
//          @Override public Boolean apply(WebDriver d) {
//            return (d.getCurrentUrl().contains("cart"));
//          }
//        });

        stopVoice();
      }

      success = (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return (!d.getCurrentUrl().contains("cart"));
        }
      });
    } catch (TimeoutException ex){
      return submitBtnClick(username, password);
    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }
    
    return success;
  }

} // end class ConfirmOrderInShoppingCar
