package com.ly.web.jd;

import com.ly.web.base.AbstractObject;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.constant.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yongliu on 8/16/17.
 */
public class ShoppingCar extends AbstractObject {
  private Integer browserTime;


  private final String ADD_TO_SHOPPING_CAR_BUTTON_ID = "InitCartUrl";
  private final String GO_TO_SHOPPING_CAR_BUTTON_ID = "GotoShoppingCart";
  
  /**
   * Creates a new AddProductionToShoppingCar object.
   */
  public ShoppingCar() { }

  /**
   * Creates a new AddProductionToShoppingCar object.
   *
   * @param  driver  WebDriver
   */
  public ShoppingCar(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }


  public boolean addToShoppingCar(ItemInfoCommand itemInfo, String username, String password) {
    Boolean added = Boolean.TRUE;
    Boolean foundSkuTag = Boolean.FALSE;
    String sku = itemInfo.getSku();

    ///////////////////////////config start////////////////
    Boolean allowAttentionProduction = itemInfo.getAttentionItem();
    Boolean allowAttentionStore      = itemInfo.getAttentionStore();
    ///////////////////////////config end//////////////////

    delay(6);

    try {
      foundSkuTag = switchToCorrectTab(sku);

      if (!foundSkuTag) {
        added = Boolean.FALSE;
        logger.error("In Opened tab(s) not found the url contains sku[" + sku + "]");

        return added;
      }
      
      // check the page has loaded
      checkPageLoaded(sku);

      // browse the production page.
      browseProduction(sku, this.getBrowserTime());

      // 1. attention production
      if (allowAttentionProduction) {
//        attentionProduction(sku, username, password);
      }

      // 2. attention store
      if (allowAttentionStore) {
//        attentionStore(sku);
      }

      // 3. add it to shopping car
      addProductionToShoppingCar(sku);
      
      
    } catch (Exception e) {
      added = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }
    
    
    return added;
  }


  private Boolean switchToCorrectTab(String sku){
    Boolean foundSkuTag = Boolean.FALSE;
    Set<String> tabs = webDriver.getWindowHandles();

    for (String tab : tabs) {
      String currentUrl = webDriver.switchTo().window(tab).getCurrentUrl();

      if (logger.isDebugEnabled()) {
        logger.debug("Current tab url: " + currentUrl);
      }

      if (currentUrl.contains(sku)) {
        foundSkuTag = Boolean.TRUE;

        break;
      }
    }
    return foundSkuTag;
  }
  
  private Boolean checkPageLoaded(String sku){
    try{
      
      waitForById(ADD_TO_SHOPPING_CAR_BUTTON_ID,20);
      
      return Boolean.TRUE;
    }catch (Exception e){
      logger.error(e.getMessage());
      if(!isCurrentPage(webDriver.getCurrentUrl(), sku)){
        switchToCorrectTab(sku);
      }

      refreshPage();
      return checkPageLoaded(sku);
    }
  }


  private void addProductionToShoppingCar(String sku) {
    if(!isCurrentPage(webDriver.getCurrentUrl(), sku)){
      switchToCorrectTab(sku);
    }
    
    if (logger.isDebugEnabled()) {
      logger.debug("........Ready add sku[" + sku + "] to shopping car.......");
    }

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Find add to shopping car button by ID: " + ADD_TO_SHOPPING_CAR_BUTTON_ID);
      }

      WebElement addToShoppingCarBtn = ExpectedConditions.presenceOfElementLocated(By.id(ADD_TO_SHOPPING_CAR_BUTTON_ID))
        .apply(webDriver);
      

      scrollToElementPosition(addToShoppingCarBtn);

      delay(2);

      if (logger.isDebugEnabled()) {
        logger.debug("Ready to fire click addShopping car button.");
      }
      
      try {
        closeJDPopupWindow();
        addToShoppingCarBtn.click();
      }catch (WebDriverException we){
        logger.error(we.getMessage(), we);
        logger.info("Will refresh current page and do it again.");
        refreshPage();
        delay(3);
        addProductionToShoppingCar(sku);
      }
      
      Boolean pickedUp =(new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
            return d.getCurrentUrl().contains("addToCart");    
        }
      });

      if (logger.isDebugEnabled()) {
        logger.debug("The sku[" + sku + "] added to shopping car successfully, please visit my shopping car to view");
      }

    } catch (NoSuchElementException e) {
      logger.error("Add sku[" + sku + "] to shopping car failed.");

      logger.error(e.getMessage(), e);
      throw e;
    } // end try-catch
  } // end method addProductionToShoppingCar
  
  public void setBrowserTime(Integer browserTime) {
    this.browserTime = browserTime;
  }

  public Integer getBrowserTime() {
    return browserTime;
  }
}
