package com.ly.web.yhd;

import java.util.List;

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


/**
 * Created by yongliu on 10/31/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/31/2016 16:00
 */
public class CheckoutOrder extends YHDAbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String CHECKOUT_ORDER_ADDRESS_LIST_XPATH = "//div[@id='addressList']/ul/li";

  private static final String CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_XPATH                    =
    "//div[contains(@class, 'popGeneral')]";
  private static final String CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_CHANGE_CITY_BUTTON_XPATH =
    CHECKOUT_ORDER_SELECTED_ADDRESS_POP_UP_WINDOW_XPATH + "//span[contains(text(), '切换站点')]";

  private static final String CHECKOUT_ORDER_SUBMIT_ORDER_BUTTON_XPATH =
    "//div[contains(@class, 'mallsettelment')]//button[contains(text(), '提交订单')]";

  private static final String CHECKOUT_ADDRESS_FORM_ID = "addressForm";

  private static final String CHECKOUT_ADDRESS_FROM_FIELD_RECEIVER_NAME_ID  = "receiverName";
  private static final String CHECKOUT_ADDRESS_FROM_FIELD_DETAIL_ADDRESS_ID = "detailAddress";
  private static final String CHECKOUT_ADDRESS_FROM_FIELD_MOBILE_PHONE_ID   = "mobile";
  private static final String CHECKOUT_ADDRESS_FROM_FIELD_PROVINCE_ID       = "province";
  private static final String CHECKOUT_ADDRESS_FROM_FIELD_CITY_ID           = "city";
  private static final String CHECKOUT_ADDRESS_FROM_FIELD_COUNTRY_ID        = "county";
  private static final String CHECKOUT_ADDRESS_FROM_DROP_DOWN_XPATH         =
    "//select[@id='%s']/option[contains(text(), '%s')]";
  private static final String CHECKOUT_ADDRESS_FORM_SAVE_BTN_ID             = "saveEditAddress";

  private static final String CHECKOUT_OVERSEA_REAL_NAME_ID                  = "realName";
  private static final String CHECKOUT_OVERSEA_IDENTITY_CARD_NUM_ID          = "idCard";
  private static final String CHECKOUT_OVERSEA_EDIT_INFO_BTN_ID              = "userAuthModify";
  private static final String CHECKOUT_OVERSEA_SAVE_REAL_NAME_AUTH_BTN_XPATH =
    "//div[@data-tpa='SAVE_MODIFY_REAL_NAME_AUTH'][contains(@class, 'submit')]/span[contains(text(), '保存实名信息')]";
  private static final String CHECKOUT_OVERSEA_AGREE_PROTOCOL_XPATH          =
    "//div[@id='statisticsUI']/div[contains(@class, 'mod_1mallxieyi')]/i[contains(@class, 'cb')]";

//  private static final String CHECKOUT_ORDER_PRICE_XPATH = "//div[contains(@class, 'totalCount')]/i/b";
  private static final String CHECKOUT_ORDER_PRICE_XPATH = "//div[@id='statisticsUI']//strong/b";

  private static final String CHECKOUT_CREATE_NEW_ADDRESS_BUTTON_XPATH =
    "//div[@id='addAddressOperatBt']/span[contains(text(), '使用新地址')]";

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
          createNewAddress(addressInfo);
        }
      }

      // check if there pop-up window
      checkChangeProvincePopupWindow();

      delay(5);

      // is this order is oversea
      // then will input name, identityCardNo and check the box.
      if (orderInfo.getAllowOversea()) {
        inputOverseaFields(addressInfo);

        delay(5);
      }

      // click 'submit order' button
      success = submitOrder(orderInfo);

      if (logger.isDebugEnabled()) {
        logger.debug("Order has been submitted successfully.");
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);

    } // end try-catch

    return success;
  } // end method checkout

  //~ ------------------------------------------------------------------------------------------------------------------

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
  }   // end method checkChangeProvincePopupWindow

  //~ ------------------------------------------------------------------------------------------------------------------

  private void checkNavigateToConfirmOrderPage() {
    if (logger.isDebugEnabled()) {
      logger.debug("Now the current url is: " + webDriver.getCurrentUrl());
    }

    if (webDriver.getCurrentUrl().contains(Constant.YHD_SHOPPING_CAR_URL)) {
      submitBtnClickInShoppingCarPage();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void createNewAddress(AddressInfoCommand addressInfo) {
    Boolean addressFormShows = Boolean.FALSE;

    try {
      WebElement addressFormEle = webDriver.findElement(By.id(CHECKOUT_ADDRESS_FORM_ID));

      if (addressFormEle.isDisplayed()) {
        addressFormShows = Boolean.TRUE;

        // input address info.
        inputAddressInfo(addressInfo);
      }
    } catch (NoSuchElementException e) {
      addressFormShows = Boolean.FALSE;
      logger.error("The address form does not display automatic.");
    }

    if (!addressFormShows) {
      // if address form does not display automatic
      // the will let it display thought click 'Use new Address' button.

      try {
        WebElement newAddressBtnEle = webDriver.findElement(By.xpath(CHECKOUT_CREATE_NEW_ADDRESS_BUTTON_XPATH));
        newAddressBtnEle.click();
        delay(3);

        // input address info.
        inputAddressInfo(addressInfo);

      } catch (NoSuchElementException ex) {
        logger.error("No found 'Create new Address button' with xpath: " + CHECKOUT_CREATE_NEW_ADDRESS_BUTTON_XPATH);
      }

    }
  } // end method createNewAddress

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getOrderPrice() {
    String orderPrice = null;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Find order price element by xpath: " + CHECKOUT_ORDER_PRICE_XPATH);
      }

      WebElement priceEle = webDriver.findElement(By.xpath(CHECKOUT_ORDER_PRICE_XPATH));

      orderPrice = priceEle.getText();

      if (logger.isDebugEnabled()) {
        logger.debug("The Order price is: " + orderPrice);
      }

    } catch (NoSuchElementException e) {
      logger.error(e.getMessage());
    }

    return orderPrice;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void inputAddressInfo(AddressInfoCommand addressInfo) {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Will create the address info: " + addressInfo);
      }

      // receiver name
      inputValue(CHECKOUT_ADDRESS_FROM_FIELD_RECEIVER_NAME_ID, addressInfo.getFullName());

      // detail address
      inputValue(CHECKOUT_ADDRESS_FROM_FIELD_DETAIL_ADDRESS_ID, addressInfo.getFullAddress());

      // mobile phone
      inputValue(CHECKOUT_ADDRESS_FROM_FIELD_MOBILE_PHONE_ID, addressInfo.getTelephoneNum());

      // province
      inputSelectOptionValue(CHECKOUT_ADDRESS_FROM_FIELD_PROVINCE_ID, addressInfo.getProvince());
      delay(2);

      // city
      inputSelectOptionValue(CHECKOUT_ADDRESS_FROM_FIELD_CITY_ID, addressInfo.getCity());
      delay(2);

      // county
      inputSelectOptionValue(CHECKOUT_ADDRESS_FROM_FIELD_COUNTRY_ID, addressInfo.getCountry());
      delay(2);

      // save address button
      WebElement saveAddressBtnEl = ExpectedConditions.elementToBeClickable(By.id(CHECKOUT_ADDRESS_FORM_SAVE_BTN_ID))
        .apply(webDriver);

      if (saveAddressBtnEl != null) {
        saveAddressBtnEl.click();
        delay(3);
      }

    } catch (Exception e) {
      logger.error(e.getMessage());
    } // end try-catch

  } // end method inputAddressInfo

  //~ ------------------------------------------------------------------------------------------------------------------

  private void inputOverseaFields(AddressInfoCommand addressInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug("Ready input oversea fields (realName, identityCardNum) and check the checkbox ......");
    }

    try {
      WebElement editBtn = webDriver.findElement(By.id(CHECKOUT_OVERSEA_EDIT_INFO_BTN_ID));

      if (editBtn != null) {
        scrollToElementPosition(editBtn);

        delay(2);

        editBtn.click();
      }

      delay(2);
    } catch (NoSuchElementException e) {
      logger.error("No real name info, please input a new.");
    }

    try {
      WebElement saveBtn = ExpectedConditions.elementToBeClickable(By.xpath(
            CHECKOUT_OVERSEA_SAVE_REAL_NAME_AUTH_BTN_XPATH)).apply(webDriver);

      scrollToElementPosition(saveBtn);

      delay(2);

      inputValue(CHECKOUT_OVERSEA_REAL_NAME_ID, addressInfo.getFullName());
      inputValue(CHECKOUT_OVERSEA_IDENTITY_CARD_NUM_ID, addressInfo.getIdentityCardNum());

      if (saveBtn != null) {
        saveBtn.click();

        if (logger.isDebugEnabled()) {
          logger.debug("Save real name button was clicked.");
        }
      }

      WebElement agreeCB = ExpectedConditions.elementToBeClickable(By.xpath(CHECKOUT_OVERSEA_AGREE_PROTOCOL_XPATH))
        .apply(webDriver);

      if (agreeCB != null) {
        scrollToElementPosition(agreeCB);

        delay(2);

        boolean isChecked = agreeCB.getAttribute("class").contains("select");

        if (!isChecked) {
          agreeCB.click();

          if (logger.isDebugEnabled()) {
            logger.debug("The agree YHD oversea checkbox was checked.");
          }
        }
      }

    } catch (NoSuchElementException e) {
      logger.error(e.getMessage());
    } // end try-catch

    if (logger.isDebugEnabled()) {
      logger.debug("Input oversea fields and check the checkbox, Done!");
    }
  } // end method inputOverseaFields

  //~ ------------------------------------------------------------------------------------------------------------------

  private void inputSelectOptionValue(String elementId, String value) {
    String     xpath    = String.format(CHECKOUT_ADDRESS_FROM_DROP_DOWN_XPATH, elementId, value);
    WebElement optionEl = webDriver.findElement(By.xpath(xpath));

    if (optionEl != null) {
      optionEl.click();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void inputValue(String elementId, String value) {
    WebElement element = ExpectedConditions.presenceOfElementLocated(By.id(elementId)).apply(webDriver);

    if ((element != null) && element.isDisplayed()) {
      element.clear();
      element.sendKeys(value);
    }
  }

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

  //~ ------------------------------------------------------------------------------------------------------------------

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

      if (submitBtnEle != null) {
        submitBtnEle.click();
      }

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
  } // end method submitBtnClickInShoppingCarPage

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean submitOrder(OrderCommand orderInfo) {
    boolean submitted = Boolean.FALSE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Find the 'Submit Order' by xpath: " + CHECKOUT_ORDER_SUBMIT_ORDER_BUTTON_XPATH);
      }

      WebElement submitOrderBtnEle = ExpectedConditions.elementToBeClickable(By.xpath(
            CHECKOUT_ORDER_SUBMIT_ORDER_BUTTON_XPATH)).apply(webDriver);

      scrollToElementPosition(submitOrderBtnEle);

      if (submitOrderBtnEle != null) {
        delay(2);
        
        // get order price and set it to orderInfo.
        orderInfo.setOrderPrice(getOrderPrice());
        
        delay(2);
        
        submitOrderBtnEle.click();

        if (logger.isDebugEnabled()) {
          logger.debug("The button '提交订单' was clicked.");
        }
      }

      submitted = (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
            @Override public Boolean apply(WebDriver d) {
              return (!d.getCurrentUrl().contains("cashier"));
            }
          });

    } catch (Exception e) {
      submitted = Boolean.FALSE;
      logger.error(e.getMessage());
    } // end try-catch

    return submitted;
  } // end method submitOrder


} // end class CheckoutOrder
