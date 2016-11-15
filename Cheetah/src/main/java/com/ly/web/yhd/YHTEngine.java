package com.ly.web.yhd;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.util.Assert;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.command.ItemInfoCommand;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 11/14/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/14/2016 17:05
 */
public class YHTEngine extends YHDAbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String YHT_MENU_XPATH = "//div[@id='global_menu']//a[contains(text(), '1号团')]";

  private static final String YHT_CATEGORY_NAV_MENU_XPATH = "//div[contains(@class, 'navmenu')]";

  private static final String YHT_CATEGORY_MENU_XPATH = YHT_CATEGORY_NAV_MENU_XPATH
    + "/div[contains(@class, 'menu')]//li/a[contains(text(), '%s')]";

  private static final String YHT_ITEM_PAGE_LOADED_XPATH = "//ul[contains(@class, 'grouplist')]";

  private static final String YHT_PRODUCTION_XPATH = "//li[@gid='%s']";

  private static final String YHT_BUY_BUTTON_XPATH = "//a[@id='buyButton']";

  private static final String YHT_PRODUCTION_SKU_ID = "productMercantId";

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new YHTEngine object.
   *
   * @param  driver  WebDriver
   */
  public YHTEngine(WebDriver driver) {
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
  public boolean addToShoppingCar(ItemInfoCommand itemInfo) {
    boolean success          = Boolean.TRUE;
    String  groupBuyCategory = itemInfo.getGroupBuyCategory();
    String  grouponId        = itemInfo.getGrouponId();

    try {
      if (!webDriver.getCurrentUrl().equalsIgnoreCase(Constant.YHD_INDEX_PAGE_URL)) {
        navigateTo(Constant.YHD_INDEX_PAGE_URL);
      }

      // 1. navigate to "1号团" page
      // 1.1 navigate to associated category (i.e: 保健, 食品...)
      String  yhdTPageUrl     = navigateToYHTPage();
      String  categoryPageUrl = null;
      Boolean foundedItem     = Boolean.FALSE;

      if (yhdTPageUrl != null) {
        categoryPageUrl = navigateToYHTCategoryPage(yhdTPageUrl, groupBuyCategory);
      }

      // 2. find the sku
      if (categoryPageUrl != null) {
        foundedItem = findProductionBySku(categoryPageUrl, grouponId);
      }

      // 3. add it to shopping car
      if (foundedItem) {
        success = addItemToShoppingCar(itemInfo);
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage());
    } // end try-catch


    return success;
  } // end method addToShoppingCar

  //~ ------------------------------------------------------------------------------------------------------------------

  private Boolean addItemToShoppingCar(ItemInfoCommand itemInfo) {
    Boolean success   = Boolean.TRUE;
    String  grouponId = itemInfo.getGrouponId();

    try {
      if (!webDriver.getCurrentUrl().contains(grouponId)) {
        Set<String> tabs = webDriver.getWindowHandles();

        for (String tab : tabs) {
          String currentUrl = webDriver.switchTo().window(tab).getCurrentUrl();

          if (logger.isDebugEnabled()) {
            logger.debug("Current tab url: " + currentUrl);
          }

          if (currentUrl.contains(grouponId)) {
            break;
          }
        }
      }

      Boolean pageLoaded = waitForByXPath(YHT_BUY_BUTTON_XPATH, 10);

      if (pageLoaded) {
        WebElement buyButton  = webDriver.findElement(By.xpath(YHT_BUY_BUTTON_XPATH));
        WebElement skuField   = webDriver.findElement(By.id(YHT_PRODUCTION_SKU_ID));
        String     buttonText = buyButton.getText();
        String     sku        = skuField.getAttribute("value");

        if ("订阅开团提醒".equalsIgnoreCase(buttonText)) {
          success = Boolean.FALSE;
          logger.warn("This sku[" + grouponId + "] could not add it to shopping, because it has't open.");
        } else {
          // set the production real sku
          itemInfo.setSku(sku);

          // click 'buy' button
          buyButton.click();
          
          logger.debug("The 'Add to Shopping Car' button was clicked.");

          delay(2);
          
          success = (new WebDriverWait(this.webDriver, 20)).until(new ExpectedCondition<Boolean>() {
                @Override public Boolean apply(WebDriver d) {
                  return (d.getCurrentUrl().contains("cart"));
                }
              });
        }
      }

      return success;
    } catch (TimeoutException e) {
      refreshPage();
      delay(5);

      return addItemToShoppingCar(itemInfo);
    } catch (NoSuchElementException e) {
      success = Boolean.TRUE.FALSE;
      logger.error(e.getMessage());
    } // end try-catch

    return success;
  } // end method addItemToShoppingCar

  //~ ------------------------------------------------------------------------------------------------------------------

  private Boolean findProductionBySku(String categoryPageUrl, String sku) {
    Boolean founded = Boolean.TRUE;

    try {
      Boolean pagedLoaded = waitForByXPath(YHT_ITEM_PAGE_LOADED_XPATH, 10);

      if (pagedLoaded) {
        /// scroll the page for loaded all the productions
        int positionY = 4000;

        for (int i = 0; i < 6; i++) {
          positionY = positionY + (5000 * i);
          scrollOverflowY(positionY);
          delay(2);
        }

        delay(3);

        String productionXpath = String.format(YHT_PRODUCTION_XPATH, sku);

        if (logger.isDebugEnabled()) {
          logger.debug("Ready find element by xpath: " + productionXpath);
        }

        WebElement element = webDriver.findElement(By.xpath(productionXpath));

        scrollToElementPosition(element);

        delay(2);

        element.click();
      } // end if

    } catch (TimeoutException e) {
      refreshPage();
      delay(3);

      return findProductionBySku(categoryPageUrl, sku);
    } catch (NoSuchElementException e) {
      founded = Boolean.FALSE;

      logger.error("Not found the production by sku[" + sku + "]");
      logger.warn("Maybe this sku[" + sku + "] was out of or expired this group buying");

    } // end try-catch

    return founded;
  } // end method findProductionBySku

  //~ ------------------------------------------------------------------------------------------------------------------

  private String navigateToYHTCategoryPage(String yhtPageUrl, String category) {
    String url = null;

    try {
      if (!webDriver.getCurrentUrl().contains(Constant.YHT_PAGE_URL)) {
        navigateTo(yhtPageUrl);
      }

      Boolean founded = waitForByXPath(YHT_CATEGORY_NAV_MENU_XPATH, 10);

      if (founded) {
        String menuXpath = String.format(YHT_CATEGORY_MENU_XPATH, category);

        if (logger.isDebugEnabled()) {
          logger.debug("Ready find the category menu by xpath: " + menuXpath);
        }

        WebElement menuEl = webDriver.findElement(By.xpath(menuXpath));

        url = menuEl.getAttribute("href");

        scrollToElementPosition(menuEl);
        delay(2);
        menuEl.click();

        return url;
      }
    } catch (TimeoutException e) {
      refreshPage();
      delay(3);

      return navigateToYHTCategoryPage(yhtPageUrl, category);
    } catch (Exception e) {
      logger.error(e.getMessage());
    } // end try-catch

    return url;
  } // end method navigateToYHTCategoryPage

  //~ ------------------------------------------------------------------------------------------------------------------

  private String navigateToYHTPage() {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Will find the '1号团' by xpath: " + YHT_MENU_XPATH);
      }

      Boolean founded = waitForByXPath(YHT_MENU_XPATH, 10);


      if (founded) {
        WebElement yhtMenu    = webDriver.findElement(By.xpath(YHT_MENU_XPATH));
        String     yhtPageUrl = yhtMenu.getAttribute("href");

        if (logger.isDebugEnabled()) {
          logger.debug("The YHT page url is: " + yhtPageUrl + ", ready redirect to yht page");
        }

        navigateTo(yhtPageUrl);

        return yhtPageUrl;
      }
    } catch (TimeoutException e) {
      refreshPage();
      delay(3);

      return navigateToYHTPage();
    } catch (Exception e) {
      logger.error(e.getMessage());
    } // end try-catch

    return null;
  } // end method navigateToYHTPage


} // end class YHTEngine
