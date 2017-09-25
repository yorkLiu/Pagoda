package com.ly.web.jd;

import com.ly.core.PagodaRandom;
import com.ly.utils.NumberUtils;
import com.ly.web.base.AbstractObject;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.command.OrderCommand;
import com.ly.web.command.ProductionInfo;
import com.ly.web.constant.Constant;
import com.ly.web.exception.PageNotLoadedException;
import com.ly.web.exception.SearchException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by yongliu on 8/15/17.
 */
public class JDSearch extends AbstractObject {
  
  private Integer clickedProductionCount;

  /** 货比三(@compareProductionCount)家, this @compareProductionCount property value could be set on config file. */
  private Integer compareProductionCount;

  private Boolean searchByPriceFiltered = Boolean.FALSE;

  /**
   * <pre>
   The max search page num, after current page num greater than @maxSearchPageNum, will search by price,
   this @maxSearchPageNum property value could be set on config file.
   * </pre>
   */
  private Integer maxSearchPageNum;

  private Integer[] priceOffsets = null;
  
  private final String elementKeywordID = "key";
  
  private final String SEARCH_BY_PRICE_XPATH="//div[@id='J_selectorPrice']/div[contains(@class, 'f-price-edit')]/a[contains(@class, 'J-price-confirm')]";
  
  
  
  private final String PRODUCTION_LIST_XPATH = "//div[@id='J_goodsList']/ul/li";
  private final String PRODUCTION_SEARCH_XPATH= PRODUCTION_LIST_XPATH+"[@data-sku='%s']";
  private final String PRODUCTION_MAIN_IMAGE_XPATH = PRODUCTION_LIST_XPATH+"[@data-sku='%s']/div/div[contains(@class, 'p-img')]/a";
  
  private final String PRODUCTION_SEARCH_IN_THUMBNAIL_ICON_XPATH=PRODUCTION_LIST_XPATH + "/div/div[contains(@class, 'p-scroll')]/div/ul/li/a/img[@data-sku='%s']";
  
  private final String NEXT_PAGE_BUTTON_XPATH="//div[@id='J_topPage']/a[contains(@class, 'fp-next')]";


  public JDSearch(){}

  public JDSearch(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }


  public boolean search(ItemInfoCommand itemInfo, OrderCommand orderInfo, int index) throws PageNotLoadedException,
    SearchException {
    Boolean found     = Boolean.TRUE;
    String  keyword   = itemInfo.getKeyword();
    String  sku       = itemInfo.getSku();

    if (logger.isDebugEnabled()) {
      logger.debug("Search production by keyword:" + keyword);
    }

    Assert.notNull(keyword);

    try {
      Boolean needRedirectToIndexPage = Boolean.FALSE;
      
      if (index == 0 && !webDriver.getCurrentUrl().equalsIgnoreCase(Constant.JD_INDEX_PAGE_URL)) {
        needRedirectToIndexPage = Boolean.TRUE;
      } else if(index > 0){
        try{
          waitForById(elementKeywordID, null);

          closeTabsExcept(webDriver.getCurrentUrl());
          delay(2);
          
        }catch (TimeoutException teo){
          needRedirectToIndexPage = Boolean.TRUE;
        }
      }
      
      if(needRedirectToIndexPage){
        navigateTo(Constant.JD_INDEX_PAGE_URL);
        // check the page is loaded.
        waitForById(elementKeywordID, null);
      }

      // find the search element
//      WebElement searchElement = ExpectedConditions.presenceOfElementLocated(By.id(elementKeywordID)).apply(webDriver);
//
//      if (searchElement != null) {
//        searchElement.sendKeys(keyword);
//        searchElement.sendKeys(Keys.ENTER); // press enter
//      }
      
      if(waitForSearchAfterClickSearchBtn(keyword, 0)){
        findProduction(itemInfo, orderInfo);
      }
      
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

  }
  
  private Boolean waitForSearchAfterClickSearchBtn(String keyword, int index){
    try {
      WebElement searchElement = ExpectedConditions.presenceOfElementLocated(By.id(elementKeywordID)).apply(webDriver);

      if (searchElement != null) {
        searchElement.sendKeys(keyword);
        searchElement.sendKeys(Keys.ENTER); // press enter
        
        delay(3);
        if(!webDriver.getCurrentUrl().contains("search")){
          throw new RuntimeException("The URL was not change, will refresh current page to try again.");
        }
      }
    }catch (Exception e){
      refreshPage();
      if (index <= 3){
        return waitForSearchAfterClickSearchBtn(keyword, ++index);  
      }
    }
    
    return Boolean.TRUE;
  }
  
  private Integer getTotalPageNumber(){
    Integer total = new Integer(executeJavaScript("return SEARCH.adv_param.page_count").toString());
    return total;
  }
  
  private Integer getCurrentPageNumber(){
    Integer page = new Integer(executeJavaScript("return SEARCH.adv_param.page").toString());
    return new Double(Math.ceil(page / 2.0)).intValue();
  }


  public Boolean findProduction(ItemInfoCommand itemInfo, OrderCommand orderInfo) throws Exception {
    String sku = itemInfo.getSku();

    waitForById("J_goodsList", 30);


    // find current page and total page
    Integer currentPageNum = getCurrentPageNumber();
    Integer totalPageNum   = getTotalPageNumber();

    ProductionInfo productionInfo = new ProductionInfo();
    productionInfo.setSku(sku);
    productionInfo.setProductionUrl(itemInfo.getUrl());
    productionInfo.setPrice(itemInfo.getPriceDecimal());

    Boolean result = searchProductionOnPage(productionInfo, currentPageNum, totalPageNum);
    if(result){
      orderInfo.setStoreName(productionInfo.getStoreName());
      orderInfo.addPageInfo(itemInfo.getKeyword(), productionInfo.getPageNum());
    }
    
    return result;
  }

  private Boolean searchProductionOnPage(ProductionInfo productionInfo, Integer currentPageNum, Integer totalPageNum) {
    String pageInfo = currentPageNum + "/" + totalPageNum;
    String sku = productionInfo.getSearchSku();

    logger.info(String.format("Ready find the sku[%s] on page: %s", sku, pageInfo));

    if (currentPageNum > totalPageNum) {
      logger.info(">>>>>>> Current page is the last page, not found the sku[" + sku
        + "], I guess may be this sku is new production and no sort in YHD");

      String ulr =
        ((productionInfo.getProductionUrl() != null) && StringUtils.hasText(productionInfo.getProductionUrl()))
          ? productionInfo.getProductionUrl() : getSKUUrl(Constant.JD_ITEM_URL_PREFIX, sku);

      logger.info(">>>>>>>>>>>> Will buy this production by visit the url: " + ulr);

      webDriver.get(ulr);

      productionInfo.setPageNum(ProductionInfo.DIRECT_PAGE);

      return Boolean.TRUE;
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

      positionY += 800;
    }
    
    if (logger.isDebugEnabled()) {
      logger.debug("Find production list by xpath: " + PRODUCTION_LIST_XPATH + " on page: " + pageInfo);
    }

    List<WebElement> productions = webDriver.findElements(By.xpath(PRODUCTION_LIST_XPATH));

    if (logger.isDebugEnabled()) {
      logger.debug("Found " + ((productions != null) ? productions.size() : 0) + " productions");
    }

    logger.info("Find the production with sku[" + sku + "] on page: " + pageInfo);

    boolean allowCompareProduction = (currentPageNum >= 1);

    try {
      searchAndOpenProduction(productions, sku, productionInfo, allowCompareProduction);

      return Boolean.TRUE;
    } catch (NoSuchElementException e) {
      // search the sku from the thumbnail icon
      try {

        String productionSearchINThumbnailIconXpath = String.format(PRODUCTION_SEARCH_IN_THUMBNAIL_ICON_XPATH, sku);

        if (logger.isDebugEnabled()) {
          logger.debug("Try to find thumbnail icon by xpath: " + productionSearchINThumbnailIconXpath);
        }

        WebElement iconEle = webDriver.findElement(By.xpath(productionSearchINThumbnailIconXpath));

        if (iconEle != null){
          scrollToElementPosition(iconEle);
          WebElement iconParentUlEle = (WebElement)executeJavaScript("return arguments[0].parentNode.parentNode.parentNode", iconEle);
          WebElement parentLiEle = (WebElement)executeJavaScript("return arguments[0].parentNode.parentNode.parentNode.parentNode", iconParentUlEle);

          WebElement prevBtn = parentLiEle.findElement(By.xpath("div/div[contains(@class, 'p-scroll')]/span[contains(@class, 'ps-prev')]"));
          WebElement nextBtn = parentLiEle.findElement(By.xpath("div/div[contains(@class, 'p-scroll')]/span[contains(@class, 'ps-nex')]"));
          
          if (nextBtn.isEnabled()){
            closeJDPopupWindow();
            nextBtn.click();
          } else if(prevBtn.isEnabled()){
            closeJDPopupWindow();
            prevBtn.click();
          }
          delay(2);
          
          if(iconParentUlEle != null){
            String position = iconParentUlEle.getCssValue("position");
            String width = iconParentUlEle.getCssValue("width");
            List<WebElement> lis = iconParentUlEle.findElements(By.xpath("li"));
            int idx = 0;
            for (WebElement li : lis) {
              try {
                WebElement el = li.findElement(By.xpath(String.format("a/img[@data-sku='%s']", sku)));
                if (el != null) {
                  break;
                }
              } catch (Exception eex){}
              idx++;
            }
            
            int leftPosition = 34 * idx;

            String style = String.format("position: %s; width: %s; left: %spx", position, width, (leftPosition > 0 ? -leftPosition: leftPosition));
            executeJavaScript(String.format("arguments[0].setAttribute('style', '%s')", style), iconParentUlEle);
          }


          WebElement clickEle = ExpectedConditions.elementToBeClickable(iconEle).apply(webDriver);
          if (clickEle != null){
            delay(1);
            closeJDPopupWindow();
            clickEle.click();
          }

          String parentSku = parentLiEle.getAttribute("data-sku");
          productionInfo.setSearchSku(parentSku);

          searchAndOpenProduction(productions, parentSku, productionInfo, allowCompareProduction);
          return Boolean.TRUE;
        }
      }catch (NoSuchElementException ex){
        logger.warn("No thumbnail icons found.");
      }
    }

    ///////////////////////Search production by price filter  [start]
    if (!searchByPriceFiltered && ((maxSearchPageNum > 0) && (currentPageNum >= maxSearchPageNum))) {
      logger.info("The production was not found in " + maxSearchPageNum + "pages, current page number: " + currentPageNum
        + " will find production by price filter.");

      Boolean searched = searchProductionByPrice(productionInfo.getPrice());
      searchByPriceFiltered = Boolean.TRUE;

      if (searched) {
        delay(5);
        currentPageNum = getCurrentPageNumber();
        totalPageNum   = getTotalPageNumber();

        return searchProductionOnPage(productionInfo, currentPageNum, totalPageNum);
      }
    }
    ///////////////////////Search production by price filter  [end]
    else {
      logger.info("SKU[" + sku + "] not found on current page: " + pageInfo + ", will finding it on next page");

      if (gotoNextPage()) {
        return searchProductionOnPage(productionInfo, ++currentPageNum, totalPageNum);
      }
    }
    
    return Boolean.FALSE;
  }


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
    
    
    String mainImageXpath=String.format(PRODUCTION_MAIN_IMAGE_XPATH,searchSku);
    WebElement element = ExpectedConditions.elementToBeClickable(By.xpath(mainImageXpath)).apply(webDriver);


    closeJDPopupWindow();
    element.click();
  } // end method searchAndOpenProduction


  private void getProductionInfo(ProductionInfo productionInfo) {
    try {
      String productionSearchXpath = String.format(PRODUCTION_SEARCH_XPATH, productionInfo.getSearchSku());

      WebElement e = webDriver.findElement(By.xpath(productionSearchXpath + "/div/div[contains(@class, 'p-price')]/strong/i"));
      String price = e.getText();
      e = webDriver.findElement(By.xpath(productionSearchXpath + "/div/div[contains(@class, 'p-name')]/a/em"));
      String productName = e.getText();
      e = webDriver.findElement(By.xpath(productionSearchXpath + "/div/div[contains(@class, 'p-shop')]/span/a"));
      String shopName = e.getAttribute("title");
      String shopUrl = e.getAttribute("href");
      
      if(!productionInfo.getSku().equalsIgnoreCase(productionInfo.getSearchSku())){
        String parentSuffix=null;
        String newSuffix=null;
        WebElement parentEl = webDriver.findElement(By.xpath(String.format(PRODUCTION_SEARCH_IN_THUMBNAIL_ICON_XPATH, productionInfo.getSearchSku())));
        if(parentEl != null){
          WebElement el = (WebElement)executeJavaScript("return arguments[0].parentNode", parentEl);
          parentSuffix = el.getAttribute("title");
        }
        WebElement current = webDriver.findElement(By.xpath(String.format(PRODUCTION_SEARCH_IN_THUMBNAIL_ICON_XPATH, productionInfo.getSku())));
        if(current != null){
          WebElement el = (WebElement)executeJavaScript("return arguments[0].parentNode", current);
          newSuffix = el.getAttribute("title");
        }
        
        if(parentSuffix != null && newSuffix != null){
          productName = productName.replace(parentSuffix, newSuffix);
        }
        
      }

      productionInfo.setStoreName(shopName);
      productionInfo.setStoreUrl(shopUrl);

      productionInfo.setPrice(new BigDecimal(price));
      productionInfo.setProductionName(productName);
      productionInfo.setProductionUrl(getSKUUrl(Constant.JD_ITEM_URL_PREFIX, productionInfo.getSku()));
      productionInfo.setPageNum(getCurrentPageNumber().toString());
      
    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }
    
  }
  
  

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
            closeJDPopupWindow();
            production.click();
            delay(2);
          }
        }

        delay(2);
        switchToSearchTab();
      } // end if
    }   // end if
  }     // end method compareProductions


  public void setCompareProductionCount(Integer compareProductionCount) {
    this.compareProductionCount = compareProductionCount;
  }

  public void setMaxSearchPageNum(Integer maxSearchPageNum) {
    this.maxSearchPageNum = maxSearchPageNum;
  }

  public void setPriceOffsets(Integer[] priceOffsets) {
    this.priceOffsets = priceOffsets;
  }


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

      if (logger.isDebugEnabled()) {
        logger.debug("Go to next page");
      }

      scrollToElementPosition(nextPageBtn);

      delay(2);

      closeJDPopupWindow();
      nextPageBtn.click();

      
      ///// TODO
      delay(10);

    } catch (NoSuchElementException e) {
      flag = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return flag;
  }

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

      WebElement element = webDriver.findElement(By.xpath(SEARCH_BY_PRICE_XPATH));
      String baseSearchUrl = element.getAttribute("data-url");
      
      if (baseSearchUrl != null){
        baseSearchUrl = baseSearchUrl.replace("min", minPrice.toString()).replace("max", maxPrice.toString());
        
        logger.info("Will load url: " + baseSearchUrl);
        executeJavaScript(String.format("window.location.href='%s'", baseSearchUrl));
      }

      //// TODO
      delay(3);

    } catch (Exception e) {
      result = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch


    return result;
  } // end method searchProductionByPrice
}
