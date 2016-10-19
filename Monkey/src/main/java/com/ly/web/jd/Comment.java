package com.ly.web.jd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.map.HashedMap;

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

  private static final String ORDER_TR_ID_PREFIX                   = "track%s";
  private static final String SHOW_COMMENTS_BUTTON_XPATH           =
    "//td[@id='operate%s']/div[@class='operate']/a[contains(text(), '评价')]";
  private static final String PAGE_LOADING_XPATH                   = "//div[@class='f-textarea']/textarea";
  private static final String WRITE_COMMENTS_TEXTAREA_XPATH        =
    "//div[contains(@class, 'product-%s')]//div[@class='f-textarea']/textarea";
  private static final String PRODUCT_TAG_XPATH                    =
    "//div[contains(@class, 'product-%s')]//a[@class='tag-item']";
  private static final String PRODUCT_TAG_WITHOUT_SKU_XPATH        =
    "//div[contains(@class, 'J-mjyx')]//a[@class='tag-item']";
  private static final String PRODUCT_TAG_SELECT_WITHOUT_SKU_XPATH = PRODUCT_TAG_WITHOUT_SKU_XPATH + "[@data-id='%s']";
  private static final String TAG_SELECT_XPATH                     = PRODUCT_TAG_XPATH + "[@data-id='%s']";
  private static final String SUBMIT_BTN_XPATH                     = "//a[@class='btn-submit'][contains(text(), '提交')]";

  private static final String POP_UP_WIN_CLOSE_BUTTON_XPATH =
    "//a[contains(@class, 'comment-good-cancel')][contains(text(), '关闭')]";

  private static final String[] EXCLUDE_TAGs = { "一般", "家用", "家人", "自定义", "还可以" };

  private static Integer maxSelectTagCount = 2;

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
    // webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    delay(10);

    loadWriteCommentPage();
    
    // the original comment map
    Map<String, String> originalCommentMap = new HashedMap();
    originalCommentMap.putAll(commentMap);

    if (logger.isDebugEnabled()) {
      logger.debug("Excluded tags: " + EXCLUDE_TAGs);
    }

    // if commentMap any one key start with 'NONE-SKU-'
    // then means this order will ignore sku comment
    if (findCommentMapContainsNoneSku(commentMap)) {
      if (logger.isDebugEnabled()) {
        logger.debug("Ignore SKU comments: TRUE");
      }

      // find SKU from front-end (comment page)
// checkSku(orderId, commentMap);
    }

    // find SKU from front-end (comment page)
    List<String> itemsOnFrontEnd = checkSku(orderId, commentMap);

    if (logger.isDebugEnabled()) {
      logger.debug("The comment map is: " + commentMap);
    }

    if ((commentMap != null) && (commentMap.size() > 0)) {
      // 1. five star comments
      // I think this can be move to outside for sentence.
      // if front-end need comment production size > the excel file wrote, 
      // then the gift production will not comment (comment part of productions)
      fiveStar(orderId, originalCommentMap, (itemsOnFrontEnd.size() > originalCommentMap.size()));

      for (String sku : commentMap.keySet()) {
        // 2. find tags for per product exclude '一般, 家用, 自定义'
        // and random select 0-3 tag(s)
        selectTheTags(orderId, sku);

        // 4. input the comments for per production
        writeComment(sku, commentMap);
      }
    }

    // delay 10 seconds then submit
    delay(10);

    // 5. submit
    submit(orderId);

  } // end method comment

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * find SKU from front-end (comment page).
   *
   * @param  orderId     $param.type$
   * @param  commentMap  $param.type$
   *                     
   * @return the sku founded in front-end                     
   */
  private List<String> checkSku(String orderId, Map<String, String> commentMap) {
    List<String> skuList = getSkuFromFrontEnd(orderId);

    if (logger.isDebugEnabled()) {
      logger.debug("The sku result found in front-end: " + skuList.toString());
    }

    int                 idx     = 0;
    Map<String, String> tempMap = new HashedMap();

    for (String key : commentMap.keySet()) {
      if (key.startsWith(Constant.NONE_SKU_KEY_PREFIX)) {
        tempMap.put(skuList.get(idx), commentMap.get(key));
      } else if (!skuList.contains(key)) {
        tempMap.put(skuList.get(idx), commentMap.get(key));
      } else {
        tempMap.put(key, commentMap.get(key));
      }

      idx++;
    }

    commentMap.clear();
    commentMap.putAll(tempMap);
    
    return skuList;
  } // end method checkSku

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean checkTagInExclusiveTags(String tagText) {
    boolean result = Boolean.FALSE;

    if ((tagText != null) && !StringUtils.isEmpty(tagText)) {
      for (String exclude_tag : EXCLUDE_TAGs) {
        if (tagText.contains(exclude_tag)) {
          return Boolean.TRUE;
        }
      }
    }

    return result;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean findCommentMapContainsNoneSku(Map<String, String> commentMap) {
    if (commentMap != null) {
      return commentMap.keySet().stream().anyMatch(k -> k.startsWith(Constant.NONE_SKU_KEY_PREFIX));
    }

    return Boolean.FALSE;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean fiveStar(String orderId, Map<String, String> originalCommentMap, boolean partOfComments) {
    boolean result = Boolean.TRUE;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Five star comments for production order[" + orderId + "]....");
      }

      delay(5);
      
      if (!partOfComments) {
        // comment all productions
        if (logger.isDebugEnabled()) {
          logger.debug("This order has no gift or ignore the gift production(s) comment.");
        }

        String script =
          "var arr = $('.star.star5'); for(var i = 0; i< arr.length;i++){if(i !=4 && i!=5 && i !=6){var span = arr[i]; span.click();} }";
        executeJavaScript(script);
      } else {
        if (logger.isDebugEnabled()) {
          // part of comments
          logger.debug("A part of comments (not all productions need comments)");
        }

        // 1. five star the global part
        String script1 =
          "var arr = $('.star.star5'); for(var i = 0; i< arr.length;i++){if(i<5){var span = arr[i]; span.click();} }";
        executeJavaScript(script1);

        // 2. five star the associated production only (not all)
        for (String sku : originalCommentMap.keySet()) {
          if (logger.isDebugEnabled()) {
            logger.debug("Five star the sku[" + sku + "]");
          }

          String script2 =
            "var arr = $('.star.star5'); for(var i = 0; i< arr.length;i++){ if(i>6){var span = arr[i]; var parentDiv = $(span).parents('.product-%s'); if(parentDiv && parentDiv.length>0){span.click();}} }";
          script2 = String.format(script2, sku);
          executeJavaScript(script2);
        }
      } // end if-else

      if (logger.isDebugEnabled()) {
        logger.debug("Five star clicked successfully! and exclude ('安装服务态度', '安装服务及时性', '出示收费标准') ");
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

  private List<String> getSkuFromFrontEnd(String orderId) {
    if (logger.isDebugEnabled()) {
      logger.debug("Find sku from Front-end for orderId#" + orderId);
    }

    StringBuilder script = new StringBuilder();
    script.append("var products = []; var arrys = $('textarea'); ");
    script.append("for(var i = 0;i< arrys.length;i++){");
    script.append(
      "var item = arrys[i]; var className = $(item).closest('.f-item').attr('class'); var strStartIdx = className.indexOf('product-');");
    script.append(
      "if(strStartIdx > 0){ var cls = className.substr(strStartIdx); var sku = cls.replace('product-', ''); products.push(sku);}}");
    script.append("return products;");

    return (List) executeJavaScript(script.toString());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String[] getTagData(String sku) {
    if (logger.isDebugEnabled()) {
      logger.debug("Get all tags for SKU[" + sku + "]");
    }

    String           productTagXpath = String.format(PRODUCT_TAG_XPATH, sku);
    List<String>     tagData         = new ArrayList<>();
    List<WebElement> tagElements     = null;

    try {
      // tagElements = webDriver.findElements(By.xpath(productTagXpath));
      tagElements = ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(productTagXpath)).apply(
          webDriver);
    } catch (NoSuchElementException e) {
      logger.warn("Not found element by xpath: [" + productTagXpath + "], may be the SKU not match.");

      productTagXpath = PRODUCT_TAG_WITHOUT_SKU_XPATH;

      if (logger.isDebugEnabled()) {
        logger.debug("Try to find tag elements by xpath: " + productTagXpath);
      }

      tagElements = ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(productTagXpath)).apply(
          webDriver);
    }

    if (tagElements == null) {
      logger.warn("Not found element by xpath: [" + productTagXpath + "], may be the SKU not match.");
      productTagXpath = PRODUCT_TAG_WITHOUT_SKU_XPATH;

      if (logger.isDebugEnabled()) {
        logger.debug("Try to find tag elements by xpath: " + productTagXpath);
      }

      tagElements = ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(productTagXpath)).apply(
          webDriver);
    }


    if (logger.isDebugEnabled()) {
      logger.debug("Find tags by xpath: " + productTagXpath);

      if (tagElements != null) {
        if (logger.isDebugEnabled()) {
          logger.debug("From the xpath found [" + tagElements.size() + "] tags");
        }
      }
    }

    if ((tagElements != null) && !tagElements.isEmpty()) {
      for (WebElement tagElement : tagElements) {
        String tag     = tagElement.getAttribute("data-id");
        String tagText = tagElement.getText();

        if ((tagText != null) && StringUtils.hasText(tagText)
              && checkTagInExclusiveTags(tagText)) {
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
    } // end if

    return null;
  } // end method getTagData

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean loadWriteCommentPage() {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Reload the write comment page....");
      }

      // if 10 seconds not loaded, then refresh page
      Boolean founded = waitForByXPath(PAGE_LOADING_XPATH, 10);

      if (founded) {
        if (logger.isDebugEnabled()) {
          logger.debug("Reload the write comment page found the element, do not reload yet. Return");
        }
      }

      return founded;
    } catch (TimeoutException e) {
      // refresh page
      refreshPage();

      return loadWriteCommentPage();
        // wait 10 seconds
        // webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
  } // end method loadWriteCommentPage

  //~ ------------------------------------------------------------------------------------------------------------------

  private void selectTagOnFrontEnd(String orderId, String sku, String tagDataId) {
    if ((tagDataId == null) || !StringUtils.hasText(tagDataId)) {
      logger.error("The tag id is NULL or empty for orderId#" + orderId + " and SKU: " + sku);

      return;
    }

    try {
      String script =
        "var arrays = $('.product-%s').find('a.tag-item'); for(var i = 0;i<arrays.length;i++){var a = arrays[i]; var dataAttr = $(a).attr('data-id'); if(dataAttr == '%s'){$(a).click()}}";
      script = String.format(script, sku, tagDataId);
      executeJavaScript(script);

      if (logger.isDebugEnabled()) {
        logger.debug("Selected the tag[data-id=" + tagDataId + "] for sku:" + sku + " and orderId#" + orderId
          + " was successfully.");
      }


    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean selectTheTags(String orderId, String sku) {
    boolean success = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug("Ready for select tag(s) for SKU[" + sku + "] with random and orderId#" + orderId);
    }

    delay(5);

    try {
      String[] tags = getTagData(sku);

      if ((tags == null) || (tags.length == 0)) {
        // re-find the tags again
        tags = getTagData(sku);
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Found can be used tags count:" + ((tags != null) ? tags.length : "NULL"));
      }

      if ((tags != null) && (tags.length > 0)) {
        int         size            = 1;
        Set<String> readySelectTags = new HashSet<>(5);

// if (tags.length >= 5) {
// size = new Random().nextInt(5);
// } else if (tags.length >= 3) {
// size = new Random().nextInt(3);
// }

        if (tags.length >= maxSelectTagCount) {
          size = new Random().nextInt(maxSelectTagCount);
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
            WebElement ele            = null;

            try {
              ele = ExpectedConditions.presenceOfElementLocated(By.xpath(selectTagXpath)).apply(
                  webDriver);

              if (logger.isDebugEnabled()) {
                logger.debug("Found tag data-Id: " + dataId + " by xpath: " + selectTagXpath);
              }
            } catch (NoSuchElementException e) {
              logger.warn("Nof found tag by xpath: " + selectTagXpath);

              selectTagXpath = String.format(PRODUCT_TAG_SELECT_WITHOUT_SKU_XPATH, dataId);

              if (logger.isDebugEnabled()) {
                logger.debug("Try to find tag data-Id " + dataId + " by xpath: " + selectTagXpath);
              }

              ele = ExpectedConditions.presenceOfElementLocated(By.xpath(selectTagXpath)).apply(
                  webDriver);
            }

            if (ele == null) {
              logger.warn("Nof found tag by xpath: " + selectTagXpath);

              selectTagXpath = String.format(PRODUCT_TAG_SELECT_WITHOUT_SKU_XPATH, dataId);

              if (logger.isDebugEnabled()) {
                logger.debug("Try to find tag data-Id " + dataId + " by xpath: " + selectTagXpath);
              }

              ele = ExpectedConditions.presenceOfElementLocated(By.xpath(selectTagXpath)).apply(
                  webDriver);
            }


            if (ele != null) {
              if (logger.isDebugEnabled()) {
                logger.debug("Selected the tag: [" + ele.getText() + "]");
              }

              delay(3);

              try {
                ele.click();
              } catch (Exception e) {
                logger.warn(e.getMessage());


                if (logger.isDebugEnabled()) {
                  logger.debug("Will execute the 'Select the tag on front-end page'");
                }

                selectTagOnFrontEnd(orderId, sku, dataId);
              }
            }
          } // end for
        }   // end if

      } // end if
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }   // end try-catch

    return success;
  } // end method selectTheTags

  //~ ------------------------------------------------------------------------------------------------------------------

  private void submit(String orderId) {
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
            return (d.getCurrentUrl().contains(Constant.JD_COMMENT_SUCCESS_URL) || d.getCurrentUrl().contains("partFinish"));
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
    } // end try-catch
  }   // end method submit

  //~ ------------------------------------------------------------------------------------------------------------------

  private void writeComment(String sku, Map<String, String> commentMap) {
    if (logger.isDebugEnabled()) {
      logger.debug("Write comment for production[" + sku + "]");
    }

    if ((sku != null) && StringUtils.hasText(sku)) {
      String     writeCommentTextArea = String.format(WRITE_COMMENTS_TEXTAREA_XPATH, sku);
      WebElement textArea             = null;

      try {
        textArea = ExpectedConditions.presenceOfElementLocated(By.xpath(writeCommentTextArea)).apply(
            webDriver);
      } catch (NoSuchElementException e) {
        logger.warn("Not found the text area element by xpath: " + writeCommentTextArea);

        if (logger.isDebugEnabled()) {
          logger.debug("Try to find the text area element by xpath: " + PAGE_LOADING_XPATH);
        }

        textArea = ExpectedConditions.presenceOfElementLocated(By.xpath(PAGE_LOADING_XPATH)).apply(
            webDriver);
      }

      if (textArea != null) {
        // wait 5 seconds
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        String text = commentMap.get(sku);

        if (logger.isDebugEnabled()) {
          logger.debug("SKU[" + sku + "] comment is:[" + text + "]");
        }

        textArea.clear();
        textArea.sendKeys(text);
      }

    } else {
      logger.warn("No sku or sku is empty.");
    } // end if-else
  }   // end method writeComment

} // end class Comment
