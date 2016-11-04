package com.ly.web.yhd;

import java.math.BigDecimal;

import java.security.acl.LastOwnerException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.ProductionInfo;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 10/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/09/2016 16:02
 */
public class SearchEngine extends YHDAbstractObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private final String CURRENT_PAGE_NUM_ID = "currentPageNum";

  private final String elementKeywordID = "keyword";

  private final String NEXT_PAGE_BUTTON_XPATH = "//div[@class='select_page_btn']/a[contains(@class, 'next')]";

  private final String PRODUCTION_LIST_XPATH   = "//div[@class='mod_search_pro']";
  private final String PRODUCTION_SEARCH_XPATH = "//div[@class='mod_search_pro'][contains(@data-tcd, '%s')]";
  private final String PRODUCTION_NAME_XPATH   = PRODUCTION_SEARCH_XPATH + "//p[contains(@class, 'proName')]/a";
  private final String PRODUCTION_PRICE_XPATH  = PRODUCTION_SEARCH_XPATH
    + "//p[@class='proPrice']/em[contains(@class, 'num')]";

  private final String PRODUCTION_SEARCH_IN_THUMBNAIL_ICON_XPATH =
    "//div[@class='mod_search_pro']//div[contains(@class, 'proCrumb')]//b[@pmid='%s']";
  private final String PRODUCTION_STORE_NAME_XPATH               = PRODUCTION_SEARCH_XPATH
    + "//p[contains(@class,'storeName')]/a";
  private final String TOTAL_PAGE_NUM_ID                         = "pageCountPage";

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new SearchEngine object.
   */
  public SearchEngine() { }

  /**
   * Creates a new SearchEngine object.
   *
   * @param  driver  WebDriver
   */
  public SearchEngine(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * findProduction.
   *
   * @param  sku  String
   */
  public void findProduction(String sku) {
    if (logger.isDebugEnabled()) {
      logger.debug("Find the production with sku[" + sku + "]");
    }

    if (!webDriver.getCurrentUrl().contains("search")) {
      logger.error("Could not process find production with sku[" + sku + "], because no search results.");

      return;
    }

    // delay 5 seconds
    delay(5);

    // find current page and total page
    try {
      Integer currentPageNum = getCurrentPageNum();
      Integer totalPageNum   = getTotalPageNum();

      ProductionInfo productionInfo = new ProductionInfo();
      productionInfo.setSku(sku);

      searchProductionOnPage(productionInfo, currentPageNum, totalPageNum);

    } catch (NoSuchElementException e) {
      logger.error(e.getMessage(), e);
    }

  } // end method findProduction

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * search.
   *
   * @param   itemInfo  ItemInfoCommand
   *
   * @return  boolean
   */
  public boolean search(ItemInfoCommand itemInfo) {
    Boolean found   = Boolean.TRUE;
    String  keyword = itemInfo.getKeyword();
    String  sku     = itemInfo.getSku();

    if (logger.isDebugEnabled()) {
      logger.debug("Search production by keyword:" + keyword);
    }

    Assert.notNull(keyword);

    try {
      if (!webDriver.getCurrentUrl().equalsIgnoreCase(Constant.YHD_INDEX_PAGE_URL)) {
        navigateTo(Constant.YHD_INDEX_PAGE_URL);
      }

      // wait 5 seconds
      webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
      checkWelcomeShopping();


      // find the search element
      WebElement searchElement = ExpectedConditions.presenceOfElementLocated(By.id(elementKeywordID)).apply(webDriver);

      if (searchElement != null) {
        searchElement.sendKeys(keyword);
        searchElement.sendKeys(Keys.ENTER); // press enter
      }

      findProduction(sku);

    } catch (Exception e) {
      found = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return found;
  } // end method search

  //~ ------------------------------------------------------------------------------------------------------------------

  private Integer getCurrentPageNum() {
    Integer currentPageNum = 1;

    if (logger.isDebugEnabled()) {
      logger.debug("..... Getting current page number......");
    }

    try {
      WebElement currentPageEle = webDriver.findElement(By.id(CURRENT_PAGE_NUM_ID));
      currentPageNum = new Integer(currentPageEle.getAttribute("value"));

      if (logger.isDebugEnabled()) {
        logger.debug("..... Current page number: " + currentPageNum);
      }

      return currentPageNum;
    } catch (NoSuchElementException e) {
      logger.error(e.getMessage());
    }

    return currentPageNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * price, production name and store info.
   *
   * @param  productionInfo  $param.type$
   */
  private void getProductionInfo(ProductionInfo productionInfo) {
    getProductionPrice(productionInfo);
    getProductionName(productionInfo);
    getStoreInfo(productionInfo);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void getProductionName(ProductionInfo productionInfo) {
    String sku = productionInfo.getSearchSku();

    if (logger.isDebugEnabled()) {
      logger.debug("......Getting sku[" + sku + "] name....");
    }

    try {
      String productionNameXpath = String.format(PRODUCTION_NAME_XPATH, sku);

      if (logger.isDebugEnabled()) {
        logger.debug("Find production name element by xpath: " + productionNameXpath);
      }

      WebElement productionNameEle = webDriver.findElement(By.xpath(productionNameXpath));
      String     productionUrl     = productionNameEle.getAttribute("href");
      String     productionName    = productionNameEle.getText();

      logger.info(String.format("The SKU[%s]'s info {url: %s, name: %s}", sku, productionUrl, productionName));

      productionInfo.setProductionName(productionName);
      productionInfo.setProductionUrl(productionUrl);

    } catch (NoSuchElementException e) {
      logger.error(e.getMessage(), e);
    }
  } // end method getProductionName

  //~ ------------------------------------------------------------------------------------------------------------------

  private void getProductionPrice(ProductionInfo productionInfo) {
    String sku = productionInfo.getSearchSku();

    if (logger.isDebugEnabled()) {
      logger.debug("...... Getting sku[" + sku + "] price");
    }


    String productionPriceXpath = String.format(PRODUCTION_PRICE_XPATH, sku);

    if (logger.isDebugEnabled()) {
      logger.debug("Find the price for sku[" + sku + "] by xpath: " + productionPriceXpath);
    }

    try {
      WebElement priceEle = webDriver.findElement(By.xpath(productionPriceXpath));

      BigDecimal price = new BigDecimal(priceEle.getAttribute("yhdprice"));

      if (logger.isDebugEnabled()) {
        logger.debug("SKU[" + sku + "] price is: " + price);
      }

      productionInfo.setPrice(price);

    } catch (NoSuchElementException e) {
      logger.error(e.getMessage(), e);
    }
  } // end method getProductionPrice

  //~ ------------------------------------------------------------------------------------------------------------------

  private void getStoreInfo(ProductionInfo productionInfo) {
    String sku = productionInfo.getSearchSku();

    if (logger.isDebugEnabled()) {
      logger.debug("..... Getting SKU[" + sku + "] store info.....");
    }

    try {
      String productionStoreInfoXpath = String.format(PRODUCTION_STORE_NAME_XPATH, sku);

      if (logger.isDebugEnabled()) {
        logger.debug("Find store info element by xpath: " + productionStoreInfoXpath);
      }

      WebElement storeInfoEle = webDriver.findElement(By.xpath(productionStoreInfoXpath));
      String     storeUrl     = storeInfoEle.getAttribute("href");
      String     merchantId   = storeInfoEle.getAttribute("merchantid");
      String     storeName    = storeInfoEle.getText();

      logger.info(String.format("The SKU[%s]'s store info: {storeName: %s, storeUrl: %s, merchantId: %s}", sku,
          storeName, storeUrl, merchantId));

      productionInfo.setStoreName(storeName);
      productionInfo.setMerchantId(merchantId);
      productionInfo.setStoreUrl(storeUrl);

    } catch (NoSuchElementException e) {
      logger.error(e.getMessage(), e);
    }
  } // end method getStoreInfo

  //~ ------------------------------------------------------------------------------------------------------------------

  private Integer getTotalPageNum() {
    Integer totalPageNum = 1;

    if (logger.isDebugEnabled()) {
      logger.debug("..... Getting total page number......");
    }

    try {
      WebElement totalPageEle = webDriver.findElement(By.id(TOTAL_PAGE_NUM_ID));

      totalPageNum = new Integer(totalPageEle.getAttribute("value"));

      if (logger.isDebugEnabled()) {
        logger.debug("..... Total page number: " + totalPageNum);
      }

      return totalPageNum;
    } catch (NoSuchElementException e) {
      logger.error(e.getMessage());
    }

    return totalPageNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private boolean gotoNextPage() {
    Boolean flag = Boolean.TRUE;

    if (logger.isDebugEnabled()) {
      logger.debug("Find the next page button by xpath: " + NEXT_PAGE_BUTTON_XPATH);
    }

    try {
      WebElement nextPageBtn = webDriver.findElement(By.xpath(NEXT_PAGE_BUTTON_XPATH));

      String url  = nextPageBtn.getAttribute("url");
      String href = nextPageBtn.getAttribute("href");

      if (logger.isDebugEnabled()) {
        logger.debug(">>>> the Url is: " + href);
        logger.debug(">>>> The next page url is: " + url + href);
      }

      scrollOverflowY(100);

      delay(3);

      nextPageBtn.click();

      delay(10);

    } catch (NoSuchElementException e) {
      flag = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return flag;
  } // end method gotoNextPage

  //~ ------------------------------------------------------------------------------------------------------------------

  private void searchAndOpenProduction(String searchSku, ProductionInfo productionInfo) throws NoSuchElementException {
    String productionSearchXpath = String.format(PRODUCTION_SEARCH_XPATH, searchSku);

    if (logger.isDebugEnabled()) {
      logger.debug("Find the production sku[" + searchSku + "] with xpath: " + productionSearchXpath);
    }

    WebElement productionEle = webDriver.findElement(By.xpath(productionSearchXpath));

    // find the production info
    // such as price, merchantId, merchant name, merchant index url
    getProductionInfo(productionInfo);

    scrollToElementPosition(productionEle);

    if (logger.isDebugEnabled()) {
      logger.debug("The SKU[" + searchSku + "] info: " + productionInfo);
    }

    productionEle.click();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void searchProductionOnPage(ProductionInfo productionInfo, Integer currentPageNum, Integer totalPageNum) {
    String pageInfo = currentPageNum + "/" + totalPageNum;
    String sku      = productionInfo.getSearchSku();

    logger.info(String.format("Ready find the sku[%s] on page: %s", sku, pageInfo));

    if (currentPageNum > totalPageNum) {
      logger.info(">>>>>>> Current page is the last page, not found the sku[" + sku
        + "], I guess may be this sku is new production and no sort in YHD");
      logger.info(">>>>>>>>>>>> Will buy this production by visit the url: " + getProductionUrl(sku));

      // todo
      return;
    }

    // scroll to bottom
    // purples to load all of the items
    scrollOverflowY(3600);

    delay(10);

    scrollOverflowY(3900);

    if (logger.isDebugEnabled()) {
      logger.debug("Find production list by xpath: " + PRODUCTION_LIST_XPATH + " on page: " + pageInfo);
    }

    List<WebElement> productions = webDriver.findElements(By.xpath(PRODUCTION_LIST_XPATH));

    if (logger.isDebugEnabled()) {
      logger.debug("Found " + ((productions != null) ? productions.size() : 0) + " productions");
    }

    logger.info("Found the production with sku[" + sku + "] on page: " + pageInfo);

    try {
      searchAndOpenProduction(sku, productionInfo);

      return;
    } catch (NoSuchElementException e) {
      try {
        String productionSearchINThumbnailIconXpath = String.format(PRODUCTION_SEARCH_IN_THUMBNAIL_ICON_XPATH, sku);

        if (logger.isDebugEnabled()) {
          logger.debug("Try to find thumbnail icon by xpath: " + productionSearchINThumbnailIconXpath);
        }

        WebElement iconEle   = webDriver.findElement(By.xpath(productionSearchINThumbnailIconXpath));
        String     thumbSKU  = iconEle.getAttribute("pmid");
        String     thumbName = iconEle.getAttribute("originaltitle");
        String     refSKU    = iconEle.getAttribute("defpmid");

        if (logger.isDebugEnabled()) {
          logger.debug(String.format("Found the sku[%s] in thumbnail icon, {thumbSKU:%s, thumbName: %s, refSKU: %s}",
              sku, thumbSKU, thumbName, refSKU));
        }

        if ((refSKU != null) && StringUtils.hasText(refSKU)) {
          productionInfo.setSearchSku(refSKU);
        }

        scrollToElementPosition(iconEle);
        iconEle.click();

        delay(3);
        searchAndOpenProduction(refSKU, productionInfo);

        return;
      } catch (NoSuchElementException ex) {
        logger.warn("No thumbnail icons found.");
      } // end try-catch

      logger.info("SKU[" + sku + "] not found on current page: " + pageInfo + ", will finding it on next page");

      if (gotoNextPage()) {
        searchProductionOnPage(productionInfo, ++currentPageNum, totalPageNum);
      }

      return;
    } // end try-catch
  } // end method searchProductionOnPage

} // end class SearchEngine