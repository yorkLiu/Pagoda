package com.ly.web.jd;

import com.ly.web.base.AbstractObject;
import com.ly.web.command.AddressInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.constant.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by yongliu on 8/16/17.
 */
public class JDCheckoutOrder extends AbstractObject {
  
  
  private final String SUBMIT_ORDER_BTN_XPATH="//button[contains(@class,'checkout-submit')][contains(text(), '提交订单')]";
  
  private final String CHECKOUT_ORDER_ADDRESS_LIST_XPATH = "//ul[@id='consignee-list']/li";
  
  private final String CREATE_NEW_ADDRESS_BTN_XPATH="//a[contains(@class, 'J_consignee_global')][contains(text(), '新增收货地址')]";
  
  private final String AREA_SELECTOR_XPATH="//div[contains(@class, 'ui-area-content')]/div[@data-index='%s']/ul/li/a[contains(text(), '%s')]";

  private final String RECEIVER_NAME_ID="consignee_name";
  private final String RECEIVER_ADDR_ID="consignee_address";
  private final String RECEIVER_MOBILE_ID="consignee_mobile";
  
  private final String SAVE_CONTACT_BTN_XPATH="//a[contains(@class, 'btn-1)][contains(text(), '保存')]";
  
  private final String CHECKOUT_ORDER_PRICE_ID="sumPayPriceId";
  
  private final String SUBMIT_ORDER_BTN_ID="order-submit";
  
  private final String SELECT_AREA_SCRIPT="function selectArea(){ var atags = $('div.ui-area-content > div[data-index=\"%s\"] > ul > li > a'); for(var i = 0; i < atags.length; i++){ var a = atags[i]; if (a){ text = $(a).text(); if(text.indexOf(\"%s\") > -1) { a.click(); return text; } } } return 'NA';} return selectArea();";
  

  /**
   * Creates a new ConfirmOrder object.
   */
  public JDCheckoutOrder() { }


  /**
   * Creates a new ConfirmOrder object.
   *
   * @param  driver  WebDriver
   */
  public JDCheckoutOrder(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }


  public boolean checkout(OrderCommand orderInfo) {
    boolean success = Boolean.TRUE;
    AddressInfoCommand addressInfo = orderInfo.getAddressInfo();


    try {
      if (logger.isDebugEnabled()) {
        logger.debug(".......Checkout order.......");
      }

      if (!webDriver.getCurrentUrl().contains(Constant.JD_CHECKOUT_ORDER_URL)) {
        webDriver.get(Constant.JD_CHECKOUT_ORDER_URL);
      }
      
      waitForByXPath(SUBMIT_ORDER_BTN_XPATH, 20);

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
      
      delay(2);
      
      if(StringUtils.hasText(orderInfo.getOrderCommentText())){
        WebElement comment = ExpectedConditions.presenceOfElementLocated(By.id("remarkText")).apply(webDriver);
        if(comment != null){
          scrollToElementPosition(comment);
          comment.clear();
          comment.sendKeys(orderInfo.getOrderCommentText());
        }
      }
      
      success = submitOrder(orderInfo);
      
      if (!success && !isCurrentPage(webDriver.getCurrentUrl(), Constant.JD_CHECKOUT_ORDER_URL)){
        success = Boolean.TRUE;
      }
      
      
    }catch (Exception e){
      logger.error(e.getMessage(),e);
      if(isCurrentPage(webDriver.getCurrentUrl(), Constant.JD_CHECKOUT_ORDER_URL)) {
        refreshPage();
        return checkout(orderInfo);
      }
    }
    
    return success;
  }

  private boolean selectFromAddressInfoList(AddressInfoCommand addressInfo) {
    boolean selected = Boolean.FALSE;

    try {

      WebElement considerAllAddresEle = ExpectedConditions.elementToBeClickable(By.id("consigneeItemAllClick")).apply(webDriver);
      if(considerAllAddresEle != null && considerAllAddresEle.isDisplayed()){
        considerAllAddresEle.click();
      }
      
      List<WebElement> addressEles =  ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CHECKOUT_ORDER_ADDRESS_LIST_XPATH)).apply(webDriver);

      if ((addressEles != null) && (addressEles.size() > 0)) {
        for (WebElement addressEle : addressEles) {

          WebElement clickableEle = addressEle.findElement(By.xpath("div[contains(@class, 'consignee-item')]"));
          WebElement addressDetailEle = addressEle.findElement(By.xpath("div[contains(@class, 'addr-detail')]"));
          
          
          String addressInfoText = addressDetailEle.getText();

          if (logger.isDebugEnabled()) {
            logger.debug("Exists address info: " + addressInfoText);
          }

          if (addressInfoText.contains(addressInfo.getFullName()) && addressInfoText.contains(addressInfo.getProvince())
            && addressInfoText.contains(addressInfo.getCity())
            && addressInfoText.contains(addressInfo.getCountry())) {
            scrollToElementPosition(addressEle);
            delay(2);

            clickableEle.click();

            selected = true;
          }
        }
      }
    } catch (Exception e) {
      selected = Boolean.FALSE;
      logger.error("No exists address info for: " + addressInfo);
    } // end try-catch

    return selected;
  }


  private void createNewAddress(AddressInfoCommand addressInfo) {
    Boolean addressFormShows = Boolean.FALSE;

    WebElement createAddress = ExpectedConditions.elementToBeClickable(By.xpath(CREATE_NEW_ADDRESS_BTN_XPATH)).apply(webDriver);
    
    if (createAddress == null){
      return;
    }

    createAddress.click();

   (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
      @Override public Boolean apply(WebDriver d) {
        return d.findElement(By.id("dialogIframe")) != null;
      }
    });
    

    WebElement iframe = ExpectedConditions.presenceOfElementLocated(By.id("dialogIframe")).apply(webDriver);
    if(iframe != null && iframe.isDisplayed()){
      this.webDriver.switchTo().frame(this.webDriver.findElement(By.id("dialogIframe")));
      // wait for the dialog window pop up
      waitForById("consignee_name", 20);
      
      // select the area
      WebElement area = webDriver.findElement(By.id("jd_area"));
      area.click();

      String selProvince = selectArea(String.format(AREA_SELECTOR_XPATH, "0", addressInfo.getJDProvince()), 0, addressInfo.getJDProvince());
      
      String selCity = selectArea(String.format(AREA_SELECTOR_XPATH, "1", addressInfo.getCity()), 1, addressInfo.getCity());
      String selCountry = selectArea(String.format(AREA_SELECTOR_XPATH, "2", addressInfo.getCountry()), 2, addressInfo.getCountry());
      String selTown = selectArea(String.format(AREA_SELECTOR_XPATH, "3", addressInfo.getTwon()), 3, addressInfo.getTwon());

      System.out.println(String.format("Selected: %s %s %s %s", selProvince, selCity, selCountry, selTown));
      
      inputValue(RECEIVER_NAME_ID, addressInfo.getFullName());
      inputValue(RECEIVER_ADDR_ID, addressInfo.getFullAddress());
      inputValue(RECEIVER_MOBILE_ID, addressInfo.getTelephoneNum());

      WebElement saveContact = webDriver.findElement(By.xpath(SAVE_CONTACT_BTN_XPATH));
      saveContact.click();
    }

  }

  private void inputValue(String elementId, String value) {
    WebElement element = ExpectedConditions.presenceOfElementLocated(By.id(elementId)).apply(webDriver);

    if ((element != null) && element.isDisplayed()) {
      element.clear();
      element.sendKeys(value);
    }
  }
  
  private String selectArea(String xpath, int index, String text){
    WebElement element = ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)).apply(webDriver);
//    WebElement element = webDriver.findElement(By.xpath(xpath));
    if(element != null){
      //element.click();
      
      String script = String.format(SELECT_AREA_SCRIPT, index, text);
      System.out.println("Script\n" + script);
      String ret = (String)executeJavaScript(script);
      return ret;
    }
    return null;
  }

  private String getOrderPrice() {
    String orderPrice = null;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Find order price element by id: " + CHECKOUT_ORDER_PRICE_ID);
      }

      WebElement priceEle = webDriver.findElement(By.id(CHECKOUT_ORDER_PRICE_ID));

      orderPrice = priceEle.getText();

      if (logger.isDebugEnabled()) {
        logger.debug("The Order price is: " + orderPrice);
      }

    } catch (NoSuchElementException e) {
      logger.error(e.getMessage());
    }

    return orderPrice;

  }

  private boolean submitOrder(OrderCommand orderInfo) {
    boolean submitted = Boolean.FALSE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Find the 'Submit Order' by id: " + SUBMIT_ORDER_BTN_ID);
      }

      WebElement submitOrderBtnEle = ExpectedConditions.elementToBeClickable(By.id(SUBMIT_ORDER_BTN_ID)).apply(webDriver);

      if (submitOrderBtnEle != null) {
        // get order price and set it to orderInfo.
        orderInfo.setOrderPrice(getOrderPrice());

        delay(2);

        scrollToElementPosition(submitOrderBtnEle);

        delay(2);

        submitOrderBtnEle.click();

        if (logger.isDebugEnabled()) {
          logger.debug("The button '提交订单' was clicked.");
        }
      }

      submitted = (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return (d.getCurrentUrl().contains("cashier"));
        }
      });

    } catch (Exception e) {
      submitted = Boolean.FALSE;
      logger.error(e.getMessage());
    } // end try-catch

    return submitted;
  } // end method submitOrder

}
