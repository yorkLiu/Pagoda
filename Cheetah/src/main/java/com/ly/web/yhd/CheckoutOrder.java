package com.ly.web.yhd;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.command.AddressInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.constant.Constant;

import java.util.List;


/**
 * Created by yongliu on 10/31/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/31/2016 16:00
 */
public class CheckoutOrder extends YHDAbstractObject {
  
  private static final String CHECKOUT_ORDER_ADDRESS_LIST_XPATH="//div[@id='addressList']/ul/li"; 
  
  private static final String CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_XPATH="//div[contains(@class, 'popGeneral')]";
  private static final String CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_CHANGE_CITY_BUTTON_XPATH=CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_XPATH + "//span[contains(text(), '切换站点')]";
  
  private static final String CHECKOUT_ORDER_SUBMIT_ORDER_BUTTON_XPATH="//div[contains(@class, 'mallsettelment')]//button[contains(text(), '提交订单')]";
  
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new CheckoutOrder object.
   *
   * @param  webDriver  WebDriver
   */
  public CheckoutOrder(WebDriver webDriver) {
    Assert.notNull(webDriver);
    this.webDriver = webDriver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * checkout.
   *
   * @param   orderInfo  OrderCommand
   *
   * @return  boolean
   */
  public boolean checkout(OrderCommand orderInfo) {
    boolean            success     = Boolean.TRUE;
    AddressInfoCommand addressInfo = orderInfo.getAddressInfo();

    try {
      if (logger.isDebugEnabled()) {
        logger.debug(".......Checkout order.......");
      }

      if (!webDriver.getCurrentUrl().contains(Constant.YHD_CHECKOUT_ORDER_URL)) {
        webDriver.get(Constant.YHD_CHECKOUT_ORDER_URL);
      }

      delay(5);

      // check the address info is exists or not, if exists select it from address info list
      // if not exist then create a new address for this order.
      boolean hasSelected = selectFromAddressInfoList(addressInfo);

      if (!hasSelected) {
        if (logger.isDebugEnabled()) {
          logger.debug("Will create a new address for this order, new address info: " + addressInfo);

          // create a new address
        }
      }

      // check if there pop-up window
      checkChangeProvincePopupWindow();

      delay(5);
      // click 'submit order' button
      success = submitOrder(orderInfo);

      logger.debug("Order has been submitted successfully.");
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);

    } // end try-catch

    return success;
  } // end method checkout

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean selectFromAddressInfoList(AddressInfoCommand addressInfo) {
    boolean selected = Boolean.FALSE;

    try {
      List<WebElement> addressEles = webDriver.findElements(By.xpath(CHECKOUT_ORDER_ADDRESS_LIST_XPATH));

      if ((addressEles != null) && (addressEles.size() > 0)) {
        for (WebElement addressEle : addressEles) {
          String addressInfoText = addressEle.getText();

          if (logger.isDebugEnabled()) {
            logger.debug("Exists address info: " + addressInfoText);
          }

          if (addressInfoText.contains(addressInfo.getFullName()) && addressInfoText.contains(addressInfo.getProvince())
                && addressInfoText.contains(addressInfo.getCity())
                && addressInfoText.contains(addressInfo.getCountry())) {
            scrollToElementPosition(addressEle);
            delay(2);

            addressEle.click();

            selected = true;
          }
        }
      }
    } catch (Exception e) {
      selected = Boolean.FALSE;
      logger.error("No exists address info for: " + addressInfo);
    } // end try-catch

    return selected;
  } // end method selectFromAddressInfoList

  private void checkChangeProvincePopupWindow() {
    if (logger.isDebugEnabled()) {
      logger.debug("Check is there change province pop-up window.");
    }

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Find pop up window by xpath: " + CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_XPATH);
      }

      WebElement popupWindow = webDriver.findElement(By.xpath(CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_XPATH));

      if (popupWindow != null) {
        if (logger.isDebugEnabled()) {
          logger.debug("Change province pop-up window popped up. Ready to click 'Change Province'");

          logger.debug("Find 'Change Province' button by xpath:"
            + CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_CHANGE_CITY_BUTTON_XPATH);
        }

        WebElement changeButton = webDriver.findElement(By.xpath(
              CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_CHANGE_CITY_BUTTON_XPATH));

        // if clicked the 'change province' button,
        // it will redirect to 'shopping car' page
        // need click 'submit' button in 'shopping car' page again.
        changeButton.click();

        delay(5);

        // check the current url is in 'shopping car' page
        checkNavigateToConfirmOrderPage();

      }

    } catch (NoSuchElementException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("No Pop up window popped up.");
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } // end try-catch
  } // end method checkChangeProvincePopupWindow

  private void checkNavigateToConfirmOrderPage() {
    if (logger.isDebugEnabled()) {
      logger.debug("Now the current url is: " + webDriver.getCurrentUrl());
    }

    if (webDriver.getCurrentUrl().contains(Constant.YHD_SHOPPING_CAR_URL)) {
      submitBtnClickInShoppingCarPage();
    }
  }

  /**
   * submitBtnClick.
   *
   * @return  boolean
   */
  private boolean submitBtnClickInShoppingCarPage() {
    Boolean success = Boolean.FALSE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Ready to click '去结算' button");
      }

      WebElement submitBtnEle = ExpectedConditions.presenceOfElementLocated(By.xpath(
            ConfirmOrderInShoppingCar.SHOPPING_CAR_SUBMIT_BTN_XPATH)).apply(webDriver);
      scrollToElementPosition(submitBtnEle);
      delay(2);

      submitBtnEle.click();

      success = (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
            @Override public Boolean apply(WebDriver d) {
              return (!d.getCurrentUrl().contains("cart"));
            }
          });
    } catch (TimeoutException ex) {
      return submitBtnClickInShoppingCarPage();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return success;
  } // end method submitBtnClick
  
  private boolean submitOrder(OrderCommand orderInfo) {
    boolean submitted = Boolean.FALSE;

    try {
      // todo this order is overseas buy
      // ...
      
      if (logger.isDebugEnabled()) {
        logger.debug("Find the 'Submit Order' by xpath: "
          + CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_CHANGE_CITY_BUTTON_XPATH);
      }

      WebElement submitOrderBtnEle = ExpectedConditions.presenceOfElementLocated(By.xpath(
            CHECKOUT_ORDER_SUBMIT_ORDER_BUTTON_XPATH)).apply(webDriver);

      scrollToElementPosition(submitOrderBtnEle);

      submitOrderBtnEle.click();

      submitted = Boolean.TRUE;

      delay(3);
    } catch (Exception e) {
      submitted = Boolean.FALSE;
    }

    return submitted;
  } // end method submitOrder
  

} // end class CheckoutOrder
