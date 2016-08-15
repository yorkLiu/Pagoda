package com.ly.web.jd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ly.web.base.AbstractObject;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 8/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/03/2016 13:12
 *
 *           <p>Exist Comment Step Code:</p>
 *
 *           <pre>
   1. C_10001: if comment button text is '晒单'
   2. C_11001: if not found the comment button throw NoSuchElementException
 * </pre>
 */
public class Comment extends AbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String ORDER_TR_ID_PREFIX            = "track%s";
  private static final String SHOW_COMMENTS_BUTTON_XPATH    = "//td[@id='operate%s']/div[@class='operate']/a[contains(text(), '评价')]";
  private static final String PAGE_LOADING_XPATH            = "//div[@class='f-textarea']/textarea";
  private static final String WRITE_COMMENTS_TEXTAREA_XPATH = "//div[contains(@class, 'product-%s')]//div[@class='f-textarea']/textarea";
  private static final String PRODUCT_TAG_XPATH             = "//div[contains(@class, 'product-%s')]//a[@class='tag-item']";
  private static final String TAG_SELECT_XPATH              = PRODUCT_TAG_XPATH + "[@data-id='%s']";
  private static final String SUBMIT_BTN_XPATH              = "//a[@class='btn-submit'][contains(text(), '提交')]";

  private static final String POP_UP_WIN_CLOSE_BUTTON_XPATH = "//a[contains(@class, 'comment-good-cancel')][contains(text(), '关闭')]";

  private static final String[] EXCLUDE_TAGs = { "一般", "家用", "家人", "自定义", "还可以" };

  private static List EXCLUDE_TAG_LIST = null;

  static {
    EXCLUDE_TAG_LIST = Arrays.asList(EXCLUDE_TAGs);
  }

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
    Assert.notNull(driver);
    this.webDriver = driver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * comment.
   *
   * @param   orderId     String
   * @param   commentMap  Map
   *
   * @throws  Exception  exception
   */
  public void comment(String orderId, Map<String, String> commentMap) throws Exception {
    Assert.notNull(orderId);

    String writeCommentUrl      = getCommentUrl(orderId);
    String myOrderUrl           = Constant.JD_MY_ORDER_URL;
    String showCommentsBtnXpath = String.format(SHOW_COMMENTS_BUTTON_XPATH, orderId);

    if (logger.isDebugEnabled()) {
      logger.debug("Confirm receipt the order#" + orderId);
    }

    if (!webDriver.getCurrentUrl().contains(myOrderUrl)) {
      navigateTo(myOrderUrl);
    }

    // wait 10 seconds
    webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    
    
    // If the 确认评价 pop-up message window poped-up
    // that's means this order not comment
    Boolean confirmCommentWinPoppedUp = Boolean.FALSE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Try to find the 'Confirm comments' pop-up window popped up or not");
      }

      WebElement closePopUpBtn = webDriver.findElement(By.xpath(POP_UP_WIN_CLOSE_BUTTON_XPATH));

      if (closePopUpBtn != null) {
        confirmCommentWinPoppedUp = Boolean.TRUE;

        if (logger.isDebugEnabled()) {
          logger.debug("Found the 'Confirm comments' pop-up window, will click 'close' ");
        }

        closePopUpBtn.click();
      }

    } catch (NoSuchElementException e) {
      confirmCommentWinPoppedUp = Boolean.FALSE;
      logger.warn("Not found the 'Confirm comments' pop-up window.");
    }

    if (!confirmCommentWinPoppedUp) {
      // found is this order has 'commented'
      // if shows '评价', then means this order has not commented.
      // else means this order has commented, will return.
      try {
        if (logger.isDebugEnabled()) {
          logger.debug("Try to find is this order has commented or not.....");
        }

        webDriver.findElement(By.xpath(showCommentsBtnXpath));


        if (logger.isDebugEnabled()) {
          logger.debug("Order[" + orderId + "] not comment yet, ready comment it .....");
        }
      } catch (NoSuchElementException e) {
        if (logger.isDebugEnabled()) {
          logger.debug("Order[" + orderId + "] has not commented. No need comment again. Skip it.");
        }

        return;
      }
    }

    // if the write comment url is null or empty, it's a invalid url
    if ((writeCommentUrl == null) || !StringUtils.hasText(writeCommentUrl)) {
      if (logger.isDebugEnabled()) {
        logger.debug(
          "The write comment url is null or empty, please contact the author(mailTo: pagodasupport@sina.com) to fixed this issue.");
      }

      return;
    }


    if (logger.isDebugEnabled()) {
      logger.debug("Redirect to write comment page: " + writeCommentUrl);
    }
    
    
    delay(10);
    // go to write comment page
    navigateTo(writeCommentUrl);

    // wait 10 seconds
    //webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    delay(10);

    loadWriteCommentPage();

    if (logger.isDebugEnabled()) {
      logger.debug("Excluded tags: " + EXCLUDE_TAGs);
    }

    for (String sku : commentMap.keySet()) {
      // 1. five star comments
      // I think this can be move to outside for sentence.
      fiveStar(sku);

      // 2. find tags for per product exclude '一般, 家用, 自定义'
      // and random select 0-3 tag(s)
      selectTheTags(sku);

      // 4. input the comments for per production
      writeComment(sku, commentMap);

    }

    // delay 10 seconds then submit
    delay(10);
    // 5. submit
    submit(orderId);

  } // end method comment
  
  private boolean loadWriteCommentPage(){
    try {
      logger.debug("Reload the write comment page....");
      // if 10 seconds not loaded, then refresh page
      Boolean founded = waitForByXPath(PAGE_LOADING_XPATH, 10);
      if(founded){
        logger.debug("Reload the write comment page found the element, do not reload yet. Return");
      }
      return founded;
    } catch (TimeoutException e) {
      // refresh page
      refreshPage();

      return loadWriteCommentPage();
      // wait 10 seconds
      //webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean fiveStar(String sku) {
    boolean result = Boolean.TRUE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Five star comments for production sku[" + sku + "]....");
      }
      
      delay(5);

      String script = "var arr = $('.star.star5'); for(var i = 0; i< arr.length;i++){var span = arr[i]; span.click()}";
      executeJavaScript(script);

      if (logger.isDebugEnabled()) {
        logger.debug("Five star clicked successfully!");
      }

    } catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Five star clicked failed!");
      }

      result = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }
    return result;
  } // end method fiveStar

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * String.
   *
   * @param         orderId  $param.type$
   *
   * @return        String.
   *
   * @Description:  <pre>
       find the tr attr oty
        i.e: oty='22, 4, 70'
        oty[0] _otype     in comment url is 'ot'
        oty[1] _paytype   in comment url is 'payid'
        oty[2] _shipType  in comment url is 'shipmentid'
   * </pre>
   */
  private String getCommentUrl(String orderId) {
    String     url       = null;
    String     orderTrId = String.format(ORDER_TR_ID_PREFIX, orderId);
    WebElement trEle     = webDriver.findElement(By.id(orderTrId));

    if (trEle != null) {
      String oty = trEle.getAttribute("oty");

      if ((oty != null) && StringUtils.hasText(oty)) {
        String[] otys = oty.trim().split(",");

        if (otys.length >= 3) {
          url = String.format(Constant.JD_ORDER_WRITE_COMMENT_URL, orderId, otys[0], otys[1], otys[2]);
        }
      } else {
        logger.error("Oh, JD has changed the code, not found the attribute 'oty'");
      }
    }

    return url;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String[] getTagData(String sku) {
    if (logger.isDebugEnabled()) {
      logger.debug("Get all tags for SKU[" + sku + "]");
    }

    String           productTagXpath = String.format(PRODUCT_TAG_XPATH, sku);
    List<String>     tagData         = new ArrayList<>();
    List<WebElement> tagElements     = webDriver.findElements(By.xpath(productTagXpath));

    logger.debug("Find tags by xpath: " + productTagXpath);
    logger.debug("From the xpath found [" + tagElements.size() + "]");
    
    for (WebElement tagElement : tagElements) {
      String tag     = tagElement.getAttribute("data-id");
      String tagText = tagElement.getText();

      if ((tagText != null) && StringUtils.hasText(tagText)
            && EXCLUDE_TAG_LIST.contains(tagText)) {
        if (logger.isDebugEnabled()) {
          logger.debug("This tag: " + tagText + " was exclude, skip this tag.");
        }

        continue;
      }

      if ((tag != null) && StringUtils.hasText(tag)) {
        tagData.add(tag);
      }
    }

    if (tagData.size() > 0) {
      String[] a = new String[tagData.size()];

      return tagData.toArray(a);
    }

    return null;
  } // end method getTagData

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean selectTheTags(String sku) {
    boolean success = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug("Ready for select tag(s) for SKU[" + sku + "] with random.");
    }
    
    delay(5);

    try {
      String[] tags = getTagData(sku);
      
      if(tags == null || tags.length == 0){
        // re-find the tags again
        tags = getTagData(sku);
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Found can be used tags count:" + (tags != null ? tags.length : "NULL"));
      }

      if (tags != null) {
        int          size            = 1;
        Set<String> readySelectTags = new HashSet<>(5);

        if (tags.length >= 5) {
          size = new Random().nextInt(5);
        } else if (tags.length >= 3) {
          size = new Random().nextInt(3);
        }
        
        // fixed if random size is zero
        // no tag select
        if (size == 0) {
          if (logger.isDebugEnabled()) {
            logger.debug("Random size is 0, set default size to 1");
          }

          size = 1;
        }
        
        if (logger.isDebugEnabled()) {
          logger.debug("Random tag seed [size=" + size + "]");
        }

        for (int i = 0; i < size; i++) {
          String value = tags[new Random().nextInt(tags.length)];

          if ((value != null) && StringUtils.hasText(value)) {
            readySelectTags.add(value);
          }
        }

        if (logger.isDebugEnabled()) {
          logger.debug("Will select " + readySelectTags.size() + " tag(s) for this SKU[" + sku + "]");
        }

        if (!readySelectTags.isEmpty()) {
          for (String dataId : readySelectTags) {
            String     selectTagXpath = String.format(TAG_SELECT_XPATH, sku, dataId);
            WebElement ele            = webDriver.findElement(By.xpath(selectTagXpath));

            if (ele != null) {
              if (logger.isDebugEnabled()) {
                logger.debug("Selected the tag: [" + ele.getText() + "]");
              }

              delay(3);
              
              ele.click();
            }
          }
        }

      } // end if
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch

    return success;
  } // end method selectTheTags

  //~ ------------------------------------------------------------------------------------------------------------------

  private void writeComment(String sku, Map<String, String> commentMap) {
    if (logger.isDebugEnabled()) {
      logger.debug("Write comment for production[" + sku + "]");
    }

    if ((sku != null) && StringUtils.hasText(sku)) {
      String writeCommentTextArea = String.format(WRITE_COMMENTS_TEXTAREA_XPATH, sku);

      try {
        WebElement textArea = ExpectedConditions.presenceOfElementLocated(By.xpath(writeCommentTextArea)).apply(
            webDriver);

        // wait 5 seconds
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        String text = commentMap.get(sku);

        if (logger.isDebugEnabled()) {
          logger.debug("SKU[" + sku + "] comment is:[" + text + "]");
        }

        textArea.clear();
        textArea.sendKeys(text);
      } catch (NoSuchElementException e) {
        logger.error(e.getMessage(), e);
      }

    } else {
      logger.warn("No sku or sku is empty.");
    } // end if-else
  }   // end method writeComment
  
  
  private void submit(String orderId){
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Ready for submit comment for orderId#" + orderId);
      }

      WebElement submitBtn = ExpectedConditions.elementToBeClickable(By.xpath(SUBMIT_BTN_XPATH)).apply(
        webDriver);
      delay(10);
      submitBtn.click();

      delay(5);
      
      (new WebDriverWait(this.webDriver, 3000)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return (d.getCurrentUrl().contains(Constant.JD_COMMENT_SUCCESS_URL));
        }
      });
      
      if (logger.isDebugEnabled()) {
        logger.debug("Submit comment for orderId#" + orderId + " successfully!");
      }
    } catch (NoSuchElementException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Submit comment for orderId#" + orderId + " failed");
      }

      logger.error(e.getMessage(), e);
    }
  }

} // end class Comment
