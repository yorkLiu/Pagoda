package com.ly.web.base;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ly.web.lyd.YHD;


/**
 * Created by $authorName$ on 07/12/2016.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 15:03
 */
public abstract class AbstractObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  public WebDriver webDriver;

  /** DOCUMENT ME! */
  protected final Log logger = LogFactory.getLog(getClass());

  /** TODO: DOCUMENT ME! */
  protected String url;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new AbstractObject object.
   */
  public AbstractObject() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for driver type.
   *
   * @return  String
   */
  public String getDriverType() {
    String driverType = SeleniumBaseObject.DRIVER_CHROME;

    if (webDriver instanceof ChromeDriver) {
      driverType = SeleniumBaseObject.DRIVER_CHROME;
    } else if (webDriver instanceof FirefoxDriver) {
      driverType = SeleniumBaseObject.DRIVER_FIREFOX;
    } else if (webDriver instanceof SafariDriver) {
      driverType = SeleniumBaseObject.DRIVER_SAFARI;
    } else if (webDriver instanceof InternetExplorerDriver) {
      driverType = SeleniumBaseObject.DRIVER_IE;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Find the driver type is: " + driverType);
    }

    return driverType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for url.
   *
   * @return  String
   */
  public String getUrl() {
    return url;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public WebDriver getWebDriver() {
    return webDriver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void logout() throws Exception { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for url.
   *
   * @param  url  String
   */
  public void setUrl(String url) {
    this.url = url;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  webDriver  DOCUMENT ME!
   */
  public void setWebDriver(WebDriver webDriver) {
    this.webDriver = webDriver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   handler  DOCUMENT ME!
   *
   * @return  dOCUMENT ME!
   */
  public WebDriver switchToWindow(String handler) {
    if (StringUtils.isNotEmpty(handler)) {
      return webDriver.switchTo().window(handler);
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  targetPartUrl  DOCUMENT ME!
   */
  public void switchWindow(String targetPartUrl) {
    for (String handle : this.webDriver.getWindowHandles()) {
      if (!handle.equalsIgnoreCase(this.webDriver.getWindowHandle())) {
        this.webDriver.switchTo().window(handle);

        if (this.webDriver.getCurrentUrl().contains(targetPartUrl)) {
          if (logger.isDebugEnabled()) {
            logger.debug("switchWindow:" + this.webDriver.getCurrentUrl());
          }

          break;
        }
      }
    }

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addStyleForElement.
   *
   * @param  element   WebElement
   * @param  cssStyle  String
   */
  protected void addStyleForElement(WebElement element, String cssStyle) {
    if ((null != webDriver) && (null != element)) {
      JavascriptExecutor jsEngine   = (JavascriptExecutor) webDriver;
      String             baseScript = "arguments[0].setAttribute('style', '%s')";
      String             script     = String.format(baseScript, cssStyle);
      jsEngine.executeScript(script, element);

      // wait 5 seconds
      webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * delay.
   *
   * @param  seconds  int
   */
  protected void delay(int seconds) {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug(">>>Starting delay " + seconds + " second(s).>>>");
      }

      Thread.sleep(seconds * 1000);

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<End delay " + seconds + " second(s).<<<<");
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   seleniumpath  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected WebElement findInputElementBySeleniumPath(String seleniumpath) {
    return this.webDriver.findElement(By.xpath("//input[@seleniumpath='" + seleniumpath + "']"));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * hideElement.
   *
   * @param  element  WebElement
   */
  protected void hideElement(WebElement element) {
    if (logger.isDebugEnabled()) {
      logger.debug("Hide element: " + element.getTagName() + " " + element.getAttribute("class"));
    }

    addStyleForElement(element, "display:none;");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   url  DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  protected void navigateTo(String url) throws Exception {
    try {
      webDriver.navigate().to(url);

      if (logger.isDebugEnabled()) {
        logger.debug("Url navigateTo:" + url);
      }
    } catch (Exception e) {
      logger.error(e);
      throw e;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * waitForById.
   *
   * @param   elementId  String
   *
   * @return  Boolean
   */
  protected Boolean waitForById(final String elementId) {
    return (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return d.findElement(By.id(elementId)) != null;
          }
        });
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * waitForByXPath.
   *
   * @param   xpath  String
   *
   * @return  Boolean
   */
  protected Boolean waitForByXPath(final String xpath) {
    return (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return d.findElement(By.xpath(xpath)) != null;
          }
        });
  }

} // end class AbstractObject
