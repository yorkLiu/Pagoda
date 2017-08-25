package com.ly.web.yhd;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.command.ItemInfoCommand;


/**
 * Created by yongliu on 10/12/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/12/2016 15:23
 * @Description Found the production and add it to shopping car
 */
public class AddProductionToShoppingCar extends YHDAbstractObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private final String ADD_TO_SHOPPING_CAR_BUTTON_ID               = "addCart";
  private final String ADD_TO_SHOPPING_CAR_POPUP_WIN_SUCCESS_XPATH =
    "//div[@id='addCartPopWin']//p[contains(@class, 'hd_pop_tips')][contains(text(), '成功')]";

  private final String ATTENTION_PRODUCTION_ID = "addFavorite";

  private final String ATTENTION_PRODUCTION_POPUP_WIN_CLOSE_BTN_XPAHT =
    "//div[@class='popup_favorite']//button[@class='btn_close'][@title='关闭']";
  private final String ATTENTION_STORE_POPUP_WIN_CLOSE_BTN_XPATH      =
    "//div[@class='aptab']//a[contains(text(), '关闭')]";
  private final String ATTENTION_STORE_XPATH                          =
    "//div[@id='sellershopid']/a[contains(text(), '收藏店铺')]";

  
  private Integer browserTime;
  //~ Constructors -----------------------------------------------------------------------------------------------------
  

  /**
   * Creates a new AddProductionToShoppingCar object.
   */
  public AddProductionToShoppingCar() { }

  /**
   * Creates a new AddProductionToShoppingCar object.
   *
   * @param  driver  WebDriver
   */
  public AddProductionToShoppingCar(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * addToShoppingCar.
   *
   * @param   itemInfo  ItemInfoCommand
   *
   * @return  boolean
   */
  public boolean addToShoppingCar(ItemInfoCommand itemInfo, String username, String password) {
    Boolean added       = Boolean.TRUE;
    Boolean foundSkuTag = Boolean.FALSE;
    String  sku         = itemInfo.getSku();

    ///////////////////////////config start////////////////
    Boolean allowAttentionProduction = itemInfo.getAttentionItem();
    Boolean allowAttentionStore      = itemInfo.getAttentionStore();
    ///////////////////////////config end//////////////////

    delay(6);
    
    try {
      Set<String> tabs = webDriver.getWindowHandles();
      
      foundSkuTag = switchToCorrectTab(sku);

      if (!foundSkuTag) {
        added = Boolean.FALSE;
        logger.error("In Opened " + tabs.size() + " tab(s) not found the url contains sku[" + sku + "]");

        return added;
      }
      
      // browse the production page.
      browseProduction(sku, this.getBrowserTime());
      

      // 1. attention production
      if (allowAttentionProduction) {
        attentionProduction(sku, username, password);
      }

      // 2. attention store
      if (allowAttentionStore) {
        attentionStore(sku);
      }

      // 3. add it to shopping car
      addProductionToShoppingCar(sku);


    } catch (Exception e) {
      added = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    } // end try-catch

    return added;
  } // end method addToShoppingCar
  
  //~ ------------------------------------------------------------------------------------------------------------------
  
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

  private void addProductionToShoppingCar(String sku) {
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

      addToShoppingCarBtn.click();

      delay(3);


      // check the add to shopping car is successfully.
      Boolean poppedUp = waitForByXPath(ADD_TO_SHOPPING_CAR_POPUP_WIN_SUCCESS_XPATH, 10);

      if (poppedUp) {
        if (logger.isDebugEnabled()) {
          logger.debug("The sku[" + sku + "] added to shopping car successfully, please visit my shopping car to view");
        }
      } else {
        // TODO add shopping car failed.
      }

    } catch (NoSuchElementException e) {
      logger.error("Add sku[" + sku + "] to shopping car failed.");

      logger.error(e.getMessage(), e);
    } // end try-catch
  } // end method addProductionToShoppingCar

  //~ ------------------------------------------------------------------------------------------------------------------

  private void attentionProduction(String sku, String username, String password) {
    if (logger.isDebugEnabled()) {
      logger.debug("......Ready attention the production.....");
    }

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Find attention production button by ID: " + ATTENTION_PRODUCTION_ID);
      }

      WebElement attentionProductionBtn = ExpectedConditions.presenceOfElementLocated(By.id(ATTENTION_PRODUCTION_ID))
        .apply(webDriver);

      scrollToElementPosition(attentionProductionBtn);

      delay(2);

      if (logger.isDebugEnabled()) {
        logger.debug("Ready to fire click attention production button.");
      }

      attentionProductionBtn.click();

      delay(3);
      
      //check login
      loginYHDWithPopupFormInNormalPage(username, password);
      
      delay(3);

      // close the pop-up wind
      closePopUpWinByXpath(ATTENTION_PRODUCTION_POPUP_WIN_CLOSE_BTN_XPAHT);

    } catch (NoSuchElementException e) {
      logger.error("Attention sku[" + sku + "] Failed");

      logger.error(e.getMessage(), e);
    } // end try-catch
  } // end method attentionProduction

  //~ ------------------------------------------------------------------------------------------------------------------

  private void attentionStore(String sku) {
    if (logger.isDebugEnabled()) {
      logger.debug("......Ready attention the store.........");
    }

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Find attention store button by xpath: " + ATTENTION_STORE_XPATH);
      }

      WebElement attentionStoreBtn = ExpectedConditions.presenceOfElementLocated(By.xpath(ATTENTION_STORE_XPATH)).apply(
          webDriver);

      scrollToElementPosition(attentionStoreBtn);

      delay(2);

      if (logger.isDebugEnabled()) {
        logger.debug("Ready to fire click attention production button.");
      }

      attentionStoreBtn.click();

      delay(3);

      // close the pop-up wind
      closePopUpWinByXpath(ATTENTION_STORE_POPUP_WIN_CLOSE_BTN_XPATH);


    } catch (NoSuchElementException e) {
      logger.error("Attention sku[" + sku + "]'s store Failed");

      logger.error(e.getMessage(), e);
    } // end try-catch
  } // end method attentionStore

  public Integer getBrowserTime() {
    return browserTime;
  }

  public void setBrowserTime(Integer browserTime) {
    this.browserTime = browserTime;
  }
} // end class AddProductionToShoppingCar
