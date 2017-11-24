package com.ly.web.jd;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.springframework.util.Assert;

import com.ly.web.base.AbstractObject;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 8/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/02/2016 16:31* Exist Confirm Receipt Step Code:
 *
 *           <pre>
     1. R_11001: if not found the 'Confirm Receipt' button throw NoSuchElementException
 * </pre>
 */
public class ConfirmReceipt extends AbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String CONFIRM_RECEIPT_BUTTON_XPATH  = "//a[contains(@class, 'order-confirm')][@_oid='%s']";
  private static final String ORDER_CONFIRM_OK_BUTTON_XPATH = "//a[contains(@class, 'confirm-good')][@_oid='%s']";
  private static final String JD_HELPER_XPATH               = "//div[@class='ui-slidebar']";

  //~ Constructors -----------------------------------------------------------------------------------------------------


  /**
   * Creates a new ConfirmReceipt object.
   */
  public ConfirmReceipt() { }

  /**
   * Creates a new ConfirmReceipt object.
   *
   * @param  driver  WebDriver
   */
  public ConfirmReceipt(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * receipt.
   *
   * @param   orderId  String
   *
   * @throws  Exception  exception
   */
  public void receipt(String orderId, Boolean refreshPage) throws Exception {
    Assert.notNull(orderId);

    if (logger.isDebugEnabled()) {
      logger.debug("Confirm receipt the order#" + orderId);
    }


    if (!webDriver.getCurrentUrl().contains("order.jd.com/center/list.action")) {
      navigateTo(Constant.JD_MY_ORDER_URL);
    }

    // wait 10 seconds
    webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


    // ------- start hide the JD Online Helper ----------------------------------------
    try {
      WebElement jdHelperEle = webDriver.findElement(By.xpath(JD_HELPER_XPATH));
      hideElement(jdHelperEle);
    } catch (NoSuchElementException e) {
      logger.warn("Not found JD Online Helper element");
    }
    // ------- end hide the JD Online Helper ------------------------------------------

    // delay 3 seconds
    delay(4);

    String confirmReceiptXpath  = String.format(CONFIRM_RECEIPT_BUTTON_XPATH, orderId.trim());
    String confirmOKButtonXpath = String.format(ORDER_CONFIRM_OK_BUTTON_XPATH, orderId.trim());

    WebElement confirmReceiptBtn = null;

    try {
      confirmReceiptBtn = ExpectedConditions.presenceOfElementLocated(By.xpath(confirmReceiptXpath)).apply(webDriver);
    } catch (NoSuchElementException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Not found 'Confirm Receipt' button, this order#" + orderId + " has receipted.");
        logger.debug("Exit 'Confirm Receipt' step, exit code: R_11001");
      }

      return;
    }

    // wait 5 seconds
    webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    if (logger.isDebugEnabled()) {
      logger.debug("Found order confirm receipt element xpath#'" + confirmReceiptXpath + "'");
    }

    ExpectedConditions.elementToBeClickable(confirmReceiptBtn);

    if (logger.isDebugEnabled()) {
      logger.debug("Try to click confirm receipt button....");
    }

    delay(2);
    
    confirmReceiptBtn.click();

    if (logger.isDebugEnabled()) {
      logger.debug("'Confirm receipt' button was clicked, and popped up a confirm message window.");
    }


    // click 'OK' button after confirm message window pop-up

    if (logger.isDebugEnabled()) {
      logger.debug("Finding confirm receipt [OK] button by xpath:" + confirmOKButtonXpath);
    }

    waitForByXPath(confirmOKButtonXpath);


    if (logger.isDebugEnabled()) {
      logger.debug("Starting click 'Confirm' button in pop-up message window");
    }

    WebElement okBtn = webDriver.findElement(By.xpath(confirmOKButtonXpath));

    // wait 3 seconds
    webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

    if (logger.isDebugEnabled()) {
      logger.debug("Try to click the 'OK' to confirm the this order has receipt");
    }

    ExpectedConditions.elementToBeClickable(okBtn);
    
    delay(3);

    okBtn.click();

    if (logger.isDebugEnabled()) {
      logger.debug("Clicked the 'OK' button in pop-up message window");
      logger.debug("The Order[" + orderId + "] was receipt. You can comments it.");
    }
    
    // delay 10 secs
    delay(3);
    
    if(refreshPage != null && refreshPage){
      refreshPage();
    }
    
  } // end method receipt

} // end class ConfirmReceipt
