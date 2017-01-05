package com.ly.web.yhd;

import java.math.BigDecimal;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ly.core.PagodaRandom;

import com.ly.utils.NumberUtils;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.command.ProductionInfo;
import com.ly.web.constant.Constant;
import com.ly.web.exception.PageNotLoadedException;
import com.ly.web.exception.SearchException;


/**
 * Created by yongliu on 10/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/09/2016 16:02
 */
public class SearchEngine extends YHDAbstractObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Integer clickedProductionCount = 0;

  /** 货比三(@compareProductionCount)家, this @compareProductionCount property value could be set on config file. */
  private Integer compareProductionCount;

  private final String CURRENT_PAGE_NUM_ID = "currentPageNum";

  private final String elementKeywordID = "keyword";

  private final String MAX_PRICE_ID = "searchPriceRangeMax";

  /**
   * <pre>
     The max search page num, after current page num greater than @maxSearchPageNum, will search by price,
     this @maxSearchPageNum property value could be set on config file.
   * </pre>
   */
  private Integer maxSearchPageNum;

  private final String MIN_PRICE_ID = "searchPriceRangeMin";

  private final String NEXT_PAGE_BUTTON_XPATH = "//div[@class='select_page_btn']/a[contains(@class, 'next')]";

  private final String OVER_SEA_CHECKBOX_XPATH = "//a[contains(text(), '1号海购')]";

  private Integer[] priceOffsets = null;

  private final String PRODUCTION_LIST_XPATH   = "//div[@class='mod_search_pro']";
  private final String PRODUCTION_SEARCH_XPATH = "//div[@class='mod_search_pro'][contains(@data-tcd, '%s')]";
  private final String PRODUCTION_NAME_XPATH   = PRODUCTION_SEARCH_XPATH + "//p[contains(@class, 'proName')]/a";
  private final String PRODUCTION_PRICE_XPATH  = PRODUCTION_SEARCH_XPATH
    + "//p[@class='proPrice']/em[contains(@class, 'num')]";

  private final String PRODUCTION_SEARCH_IN_THUMBNAIL_ICON_XPATH =
    "//div[@class='mod_search_pro']//div[contains(@class, 'proCrumb')]//b[@pmid='%s']";
  private final String PRODUCTION_STORE_NAME_XPATH               = PRODUCTION_SEARCH_XPATH
    + "//p[contains(@class,'storeName')]/a";

  private Boolean searchByPriceFiltered = Boolean.FALSE;

  private final String TOTAL_PAGE_NUM_ID = "pageCountPage";

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
   * @param  itemInfo   sku String
   * @param  isOversea  Boolean
   */
  public void findProduction(ItemInfoCommand itemInfo, Boolean isOversea) {
    String sku = itemInfo.getSku();

    if (logger.isDebugEnabled()) {
      logger.debug("Find the production with sku[" + sku + "]");
    }

    if (!webDriver.getCurrentUrl().contains("search")) {
      logger.error("Could not process find production with sku[" + sku + "], because no search results.");

      return;
    }

    // delay 5 seconds
    delay(3);

    checkNewUserPopUp();

    if (isOversea) {
      WebElement overseaCheckbox = ExpectedConditions.presenceOfElementLocated(By.xpath(OVER_SEA_CHECKBOX_XPATH)).apply(
          webDriver);

      if (overseaCheckbox != null) {
        overseaCheckbox.click();

        if (logger.isDebugEnabled()) {
          logger.debug("checked '1号海购' checkbox.");
        }

        delay(5);
      }
    }

    // find current page and total page
    try {
      Integer currentPageNum = getCurrentPageNum();
      Integer totalPageNum   = getTotalPageNum();

      ProductionInfo productionInfo = new ProductionInfo();
      productionInfo.setSku(sku);
      productionInfo.setProductionUrl(itemInfo.getUrl());
      productionInfo.setPrice(itemInfo.getPriceDecimal());

      searchProductionOnPage(productionInfo, currentPageNum, totalPageNum);

    } catch (NoSuchElementException e) {
      logger.error(e.getMessage(), e);
    }

  } // end method findProduction

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for compare production count.
   *
   * @return  Integer
   */
  public Integer getCompareProductionCount() {
    return compareProductionCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * search.
   *
   * @param   itemInfo   ItemInfoCommand
   * @param   orderInfo  OrderCommand
   *
   * @return  boolean
   *
   * @throws  PageNotLoadedException  exception
   * @throws  SearchException         exception
   */
  public boolean search(ItemInfoCommand itemInfo, OrderCommand orderInfo) throws PageNotLoadedException,
    SearchException {
    Boolean found     = Boolean.TRUE;
    String  keyword   = itemInfo.getKeyword();
    String  sku       = itemInfo.getSku();
    Boolean isOversea = orderInfo.getAllowOversea();

    if (logger.isDebugEnabled()) {
      logger.debug("Search production by keyword:" + keyword);
    }

    Assert.notNull(keyword);

    try {
      if (!webDriver.getCurrentUrl().equalsIgnoreCase(Constant.YHD_INDEX_PAGE_URL)) {
        navigateTo(Constant.YHD_INDEX_PAGE_URL);
      }

      // check the page is loaded.
      waitForById(elementKeywordID, null);

      // wait 5 seconds
      webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
      checkWelcomeShopping();
      checkNewUserPopUp();

      // find the search element
      WebElement searchElement = ExpectedConditions.presenceOfElementLocated(By.id(elementKeywordID)).apply(webDriver);

      if (searchElement != null) {
        searchElement.sendKeys(keyword);
        searchElement.sendKeys(Keys.ENTER); // press enter
      }

      findProduction(itemInfo, isOversea);

    } catch (TimeoutException ex) {
      found = Boolean.FALSE;
      logger.error("Page load timeout " + ex.getMessage());
      throw new PageNotLoadedException("Page load timeout" + ex.getMessage(), ex);
    } catch (Exception e) {
      found = Boolean.FALSE;
      logger.error(e.getMessage(), e);
      throw new SearchException("Search Exception " + e.getMessage(), e);
    } // end try-catch

    return found;
  } // end method search

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for compare production count.
   *
   * @param  compareProductionCount  Integer
   */
  public void setCompareProductionCount(Integer compareProductionCount) {
    this.compareProductionCount = compareProductionCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for max search page num.
   *
   * @param  maxSearchPageNum  Integer
   */
  public void setMaxSearchPageNum(Integer maxSearchPageNum) {
    this.maxSearchPageNum = maxSearchPageNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price offsets.
   *
   * @param  priceOffsets  Integer[]
   */
  public void setPriceOffsets(Integer[] priceOffsets) {
    this.priceOffsets = priceOffsets;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private void compareProductions(List<WebElement> productions) {
    if ((compareProductionCount != null) && (compareProductionCount > 0)
          && (clickedProductionCount < compareProductionCount)) {
      if (productions != null) {
        if (logger.isDebugEnabled()) {
          logger.debug("Will click " + compareProductionCount + " production pages to compare.");
        }

        int productionSize = productions.size();
        int count          = compareProductionCount;

        switchToSearchTab();

        if (productionSize == 1) {
          count = 1;
        } else if ((productionSize > 1) && (productionSize < compareProductionCount)) {
          count = productionSize;
        }

        PagodaRandom random = new PagodaRandom(productionSize, count);

        for (int i = 0; i < count; i++) {
          int        index      = random.nextInt();
          WebElement production = productions.get(index);

          if (production != null) {
            clickedProductionCount++;
            scrollToElementPosition(production);
            delay(2);
            production.click();
            delay(2);
          }
        }

        delay(2);
        switchToSearchTab();
      } // end if
    }   // end if
  }     // end method compareProductions

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

    delay(5);

    Set<String> tabs = webDriver.getWindowHandles();

    for (String tab : tabs) {
      String currentUrl = webDriver.switchTo().window(tab).getCurrentUrl();

      if (logger.isDebugEnabled()) {
        logger.debug("Current tab url: " + currentUrl);
      }

      if (currentUrl.contains("search")) {
        break;
      }
    }

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

      scrollToElementPosition(nextPageBtn);

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

  private void searchAndOpenProduction(List<WebElement> productions, String searchSku, ProductionInfo productionInfo,
    boolean allowCompareProduction) throws NoSuchElementException {
    String productionSearchXpath = String.format(PRODUCTION_SEARCH_XPATH, searchSku);

    if (logger.isDebugEnabled()) {
      logger.debug("Find the production sku[" + searchSku + "] with xpath: " + productionSearchXpath);
    }

    WebElement productionEle = webDriver.findElement(By.xpath(productionSearchXpath));

    // find the production info
    // such as price, merchantId, merchant name, merchant index url
    getProductionInfo(productionInfo);

    if (allowCompareProduction) {
      //// click other production url to compare.
      clickedProductionCount = 0;
      compareProductions(productions);
    }

    scrollToElementPosition(productionEle);

    if (logger.isDebugEnabled()) {
      logger.debug("The SKU[" + searchSku + "] info: " + productionInfo);
    }

    productionEle.click();
  } // end method searchAndOpenProduction

  //~ ------------------------------------------------------------------------------------------------------------------

  private Boolean searchProductionByPrice(BigDecimal price) {
    Boolean result   = Boolean.TRUE;
    Integer minPrice = null;
    Integer maxPrice = null;

    if (priceOffsets != null) {
      Integer[] prices = NumberUtils.getSearchPrices(price, priceOffsets[0], priceOffsets[1]);

      if (prices != null) {
        minPrice = prices[0];
        maxPrice = prices[1];
      }
    }

    logger.info("Will search production by price[" + minPrice + ", " + maxPrice + "]");

    if ((minPrice == null) || (maxPrice == null)) {
      return Boolean.FALSE;
    }

    logger.info("Ready search production by price: " + minPrice + ", " + maxPrice);

    try {
      WebElement minPriceEle = ExpectedConditions.presenceOfElementLocated(By.id(MIN_PRICE_ID)).apply(webDriver);

      if (minPriceEle != null) {
        scrollToElementPosition(minPriceEle);
        minPriceEle.clear();
        minPriceEle.sendKeys(minPrice.toString());
      }

      WebElement maxPriceEle = ExpectedConditions.presenceOfElementLocated(By.id(MAX_PRICE_ID)).apply(webDriver);

      if (maxPriceEle != null) {
        scrollToElementPosition(maxPriceEle);
        maxPriceEle.clear();
        maxPriceEle.sendKeys(maxPrice.toString());
      }


      // execute JS to click '确定' button
      executeJavaScript(
        "$('ul.boxCon').attr('style', 'display:block'); var btn = $('ul.boxCon>li.first>a.btn2'); if(btn){btn[0].click();}");

      if (logger.isDebugEnabled()) {
        logger.debug("Search by Price the 'OK' button was clicked.");
      }

      delay(3);

    } catch (Exception e) {
      result = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch


    return result;
  } // end method searchProductionByPrice

  //~ ------------------------------------------------------------------------------------------------------------------

  private void searchProductionOnPage(ProductionInfo productionInfo, Integer currentPageNum, Integer totalPageNum) {
    String pageInfo = currentPageNum + "/" + totalPageNum;
    String sku      = productionInfo.getSearchSku();

    logger.info(String.format("Ready find the sku[%s] on page: %s", sku, pageInfo));

    if (currentPageNum > totalPageNum) {
      logger.info(">>>>>>> Current page is the last page, not found the sku[" + sku
        + "], I guess may be this sku is new production and no sort in YHD");

      String ulr =
        ((productionInfo.getProductionUrl() != null) && StringUtils.hasText(productionInfo.getProductionUrl()))
        ? productionInfo.getProductionUrl() : getProductionUrl(sku);

      logger.info(">>>>>>>>>>>> Will buy this production by visit the url: " + ulr);

      webDriver.get(ulr);

      return;
    }

    // scroll to bottom
    // purples to load all of the items
    int positionY = 3600;

    for (int i = 0; i < 4; i++) {
      scrollOverflowY(positionY);

      if (i == 0) {
        delay(5);
      } else {
        delay(3);
      }

      positionY += 400;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Find production list by xpath: " + PRODUCTION_LIST_XPATH + " on page: " + pageInfo);
    }

    List<WebElement> productions = webDriver.findElements(By.xpath(PRODUCTION_LIST_XPATH));

    if (logger.isDebugEnabled()) {
      logger.debug("Found " + ((productions != null) ? productions.size() : 0) + " productions");
    }

    logger.info("Found the production with sku[" + sku + "] on page: " + pageInfo);

    // click @compareProductionCount productions in first page.
    compareProductions(productions);

    boolean allowCompareProduction = (currentPageNum > 1);

    try {
      searchAndOpenProduction(productions, sku, productionInfo, allowCompareProduction);

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

        // execute javascript to click the thumbnail, because some times the thumbnail icon was hide
        String thumbnailIconId = iconEle.getAttribute("id");
        String script          = String.format("$('#%s').click()", thumbnailIconId);
        executeJavaScript(script);

        searchAndOpenProduction(productions, refSKU, productionInfo, Boolean.FALSE);

        return;
      } catch (NoSuchElementException ex) {
        logger.warn("No thumbnail icons found.");
      } // end try-catch


      ///////////////////////Search production by price filter  [start]
      if (!searchByPriceFiltered && ((maxSearchPageNum > 0) && (currentPageNum >= maxSearchPageNum))) {
        logger.info("The production was not found in " + maxSearchPageNum + ", current page number: " + currentPageNum
          + " will find production by price filter.");

        Boolean searched = searchProductionByPrice(productionInfo.getPrice());
        searchByPriceFiltered = Boolean.TRUE;

        if (searched) {
          delay(5);
          currentPageNum = getCurrentPageNum();
          totalPageNum   = getTotalPageNum();

          searchProductionOnPage(productionInfo, currentPageNum, totalPageNum);
        }
      }
      ///////////////////////Search production by price filter  [end]
      else {
        logger.info("SKU[" + sku + "] not found on current page: " + pageInfo + ", will finding it on next page");

        if (gotoNextPage()) {
          searchProductionOnPage(productionInfo, ++currentPageNum, totalPageNum);
        }
      }


      return;
    } // end try-catch
  }   // end method searchProductionOnPage

  //~ ------------------------------------------------------------------------------------------------------------------

  private void switchToSearchTab() {
    Set<String> tabs = webDriver.getWindowHandles();

    for (String tab : tabs) {
      String currentUrl = webDriver.switchTo().window(tab).getCurrentUrl();

      if (logger.isDebugEnabled()) {
        logger.debug("Current tab url: " + currentUrl);
      }

      if (currentUrl.contains("search")) {
        if (logger.isDebugEnabled()) {
          logger.debug("Now current tab is search tab.");
        }

        break;
      }
    }
  }
} // end class SearchEngine
