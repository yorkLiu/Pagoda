package com.ly.web.lyd;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.util.Assert;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 7/12/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 15:16
 * Exist Confirm Receipt Step Code:
 * <pre>
 *   1. R_11001: if not found the 'Confirm Receipt' button throw NoSuchElementException
 * </pre>
 */
public class ConfirmReceipt extends YHDAbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String CONFIRM_RECEIPT_BUTTON_PREFIX = "orderConfirm_";
  private static final String ORDER_CONFIRM_OK_BUTTON_XPATH = "//a[@data-tpa='PC_ORDER_CONFIRM_OK']";

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
  public void receipt(String orderId) throws Exception {
// String myOrderUrl = getMyOrderUrl();
    String myOrderUrl = Constant.YHD_MY_ORDER_URL;
    Assert.notNull(orderId);

    if (logger.isDebugEnabled()) {
      logger.debug("Confirm receipt the order#" + orderId);
    }

    if (!webDriver.getCurrentUrl().contains("toOrderList")) {
      navigateTo(myOrderUrl);
    }

    // wait 5 seconds
    webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    checkWelcomeShopping();


    // wait 5 seconds
    webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    
    executeJavaScript(String.format("confirmReceived(%s)", orderId));
    
    
//    String confirmReceiptId = CONFIRM_RECEIPT_BUTTON_PREFIX + orderId.trim();
//
//    try {
//       ExpectedConditions.presenceOfElementLocated(By.id(confirmReceiptId)).apply(webDriver);
////      waitForById(confirmReceiptId);
//    } catch (NoSuchElementException e) {
//      if (logger.isDebugEnabled()) {
//        logger.debug("Not found 'Confirm Receipt' button, this order#" + orderId + " has receipted.");
//        logger.debug("Exit 'Confirm Receipt' step, exit code: R_11001");
//      }
//      return;
//    }
//
//    if (logger.isDebugEnabled()) {
//      logger.debug("Finding order confirm receipt element id#'" + confirmReceiptId + "'");
//    }
//
//    checkWelcomeShopping();
//    // wait 3 seconds
//    webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
//    
//    WebElement confirmReceiptBtnEle = webDriver.findElement(By.id(confirmReceiptId));
//
//    if (logger.isDebugEnabled()) {
//      logger.debug("Found order confirm receipt element id#'" + confirmReceiptId + "'");
//    }
//
//    WebElement confirmReceiptBtn = confirmReceiptBtnEle.findElement(By.tagName("a"));
//
//    ExpectedConditions.elementToBeClickable(confirmReceiptBtn);
//
//    if (logger.isDebugEnabled()) {
//      logger.debug("Try to click confirm receipt button....");
//    }
//
//    
//    confirmReceiptBtn.click();
//
//    if (logger.isDebugEnabled()) {
//      logger.debug("'Confirm receipt' button was clicked, and popped up a confirm message window.");
//    }
//
//    if (logger.isDebugEnabled()) {
//      logger.debug("Finding confirm receipt [OK] button by xpath:" + ORDER_CONFIRM_OK_BUTTON_XPATH);
//    }
//
//    waitForByXPath(ORDER_CONFIRM_OK_BUTTON_XPATH);
//
//    if (logger.isDebugEnabled()) {
//      logger.debug("Starting click 'Confirm' button in pop-up message window");
//    }
//
//    WebElement okBtn = webDriver.findElement(By.xpath(ORDER_CONFIRM_OK_BUTTON_XPATH));
//    
//    logger.debug("Try to click the 'OK' to confirm the this order has receipt");
//    ExpectedConditions.elementToBeClickable(okBtn);
//    
//    okBtn.click();
//
//    if (logger.isDebugEnabled()) {
//      logger.debug("Clicked the 'OK' button in pop-up message window");
//      logger.debug("The Order[" + orderId + "] was receipt. You can comments it.");
//    }
  } // end method receipt

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getMyOrderUrl() {
    String myOrderUrl = Constant.YHD_MY_ORDER_URL;

    try {
      navigateTo(Constant.YHD_INDEX_PAGE_URL);

      (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return d.findElement(By.xpath("//a[@data-ref='YHD_TOP_order']")) != null;
          }
        });

      WebElement myOrderEle = this.webDriver.findElement(By.xpath("//a[@data-ref='YHD_TOP_order']"));

      if (myOrderEle != null) {
        myOrderUrl = myOrderEle.getAttribute("href");

        if (logger.isDebugEnabled()) {
          logger.debug("My Order url: " + myOrderUrl);
        }
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return myOrderUrl;

  } // end method getMyOrderUrl


} // end class ConfirmReceipt
