package com.ly.web.lyd;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ly.utils.UnicodeUtil;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 7/12/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 16:43 Exist Comment Step Code:
 *
 *           <pre>
     1. C_10001: if comment button text is '追加评论'
     2. C_11001: if not found the comment button throw NoSuchElementException
 * </pre>
 */
public class Comment extends YHDAbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String COMMENT_BUTTON_PREFIX = "comment_";

  private static final String COMMENT_DIV_SELECTOR = "//div[@soid='%s']";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String userId = null;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new Comment object.
   */
  public Comment() { }

  /**
   * Creates a new Comment object.
   *
   * @param  driver  WebDriver
   */
  public Comment(WebDriver driver) {
    this.webDriver = driver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * comment.
   *
   * @param   orderId     String
   * @param   commentMap  HashMap
   *
   * @throws  Exception  exception
   */
  public void comment(String orderId, Map<String, String> commentMap) throws Exception {
    String myOrderUrl = Constant.YHD_MY_ORDER_URL;
    Assert.notNull(orderId);

    if (logger.isDebugEnabled()) {
      logger.debug("Confirm receipt the order#" + orderId);
    }

    if (!webDriver.getCurrentUrl().contains("myOrder")) {
      navigateTo(myOrderUrl);
    }

    // wait 5 seconds
    webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    checkWelcomeShopping();

    String commentBtnId = COMMENT_BUTTON_PREFIX + orderId.trim();

    WebElement commentBtnEle = null;

    try {
      commentBtnEle = ExpectedConditions.presenceOfElementLocated(By.id(commentBtnId)).apply(webDriver);

      if (commentBtnEle != null) {
        String text = commentBtnEle.getText();

        if (logger.isDebugEnabled()) {
          logger.debug("comment button text:" + text);
        }

        if (UnicodeUtil.string2Unicode("追加评论").equals(UnicodeUtil.string2Unicode(text))) {
          if (logger.isDebugEnabled()) {
            logger.debug("This order#" + orderId + " has commented.");
            logger.debug("Exit 'comment' step, exit code: C_10001");
          }

          return;
        }
      }

      userId = getUserId();

// waitForById(commentBtnId);
    } catch (NoSuchElementException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("This order#" + orderId + " has commented.");
        logger.debug("Exit 'comment' step, exit code: C_11001");
      }

      return;
    } // end try-catch

    if (logger.isDebugEnabled()) {
      logger.debug("Finding comment button element id#'" + commentBtnId + "'");
    }

// WebElement commentBtnEle = webDriver.findElement(By.id(commentBtnId));

    if (logger.isDebugEnabled()) {
      logger.debug("Found the comment button and starting click it....");
    }

    WebElement commentBtn  = commentBtnEle.findElement(By.tagName("a"));
    String     commentHref = commentBtn.getAttribute("href");
// commentBtn.click();

    if (logger.isDebugEnabled()) {
      logger.debug("Comment button was clicked and the page will direct to: " + commentHref);
    }

    // go to the write comment page
    gotoCommentPage(commentHref, orderId, userId, commentMap);


  } // end method comment

  //~ ------------------------------------------------------------------------------------------------------------------

  private WebElement getCommentElement(String orderId) {
    return this.webDriver.findElement(getCommentSelector(orderId));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private By getCommentSelector(String orderId) {
    String commentXpath = String.format(COMMENT_DIV_SELECTOR, orderId);

    if (logger.isDebugEnabled()) {
      logger.debug("Find comment div by xpath:" + commentXpath);
    }

    return By.xpath(commentXpath);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void gotoCommentPage(String url, String orderId, String userId, Map<String, String> commentMap)
    throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Ready to process write comment url....");
    }

    if (((url == null) || !StringUtils.hasText(url))
          || (!url.contains("orderProductExperience"))) {
      url = getWriteCommentUrl(orderId, userId);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Go to the url: " + url + ", from: " + this.webDriver.getCurrentUrl());
    }

    navigateTo(url);

    // wait 5 seconds
    webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    try {
      WebElement commentEle = ExpectedConditions.presenceOfElementLocated(getCommentSelector(orderId)).apply(webDriver);

      writeComment(commentEle, orderId, userId, commentMap);


    } catch (NoSuchElementException e) {
      logger.error(e.getMessage(), e);
    } // end try-catch

  } // end method gotoCommentPage

  //~ ------------------------------------------------------------------------------------------------------------------

  private void writeComment(WebElement commentElement, String orderId, String userId, Map<String, String> commentMap) {
    String displayCssValue = (commentElement != null) ? commentElement.getCssValue("display") : null;

    if (logger.isDebugEnabled()) {
      logger.debug("The Comment div css style 'display' is: " + displayCssValue);
    }

    if (((displayCssValue != null) && "none".equalsIgnoreCase(displayCssValue))) {
      if (logger.isDebugEnabled()) {
        logger.debug("The comment dive was hidden (display: none)");
      }

      return;
    }

    String sku            = commentElement.getAttribute("pminfoid");
    String commentContent = commentMap.get(sku);

    if (logger.isDebugEnabled()) {
      logger.debug("SKU[" + sku + "] and comment is[" + commentContent + "]");
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Ready comment sku[" + sku + "]....");
    }

    WebElement commentBox = commentElement.findElement(By.id("productExperienceContent"));
    WebElement submit     = commentElement.findElement(By.id("btn_submit"));
    commentBox.clear();
    commentBox.sendKeys(commentContent);

    if (logger.isDebugEnabled()) {
      logger.debug("Ready for submit comment for orderId#" + orderId);
    }

    // wait 5 seconds
    delay(5);
    webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    
    // submit the comment
    submit.click();

    if (logger.isDebugEnabled()) {
      logger.debug("Submit successfully.");
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Comment sku[" + sku + "] with comment[" + commentContent + "] successfully.");
    }

    // wait 5 seconds
    delay(5);

    WebElement ele = getCommentElement(orderId);

    if (logger.isDebugEnabled()) {
      logger.debug("The Comment div isDisplayed: " + ele.isDisplayed());
    }

    // check the comment div is display
    // if displayed, that means there are another product not commented.
    // then execute write comment method again
    // until the comment div css display='none' (isDisplayed = false)
    if (ele.isDisplayed()) {
      writeComment(ele, orderId, sku, commentMap);
    }

  } // end method writeComment

} // end class Comment
