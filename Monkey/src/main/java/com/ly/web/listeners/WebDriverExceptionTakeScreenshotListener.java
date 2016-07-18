package com.ly.web.listeners;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Calendar;

import org.apache.commons.codec.binary.Base64;

import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.ScreenshotException;
import org.openqa.selenium.support.events.WebDriverEventListener;


/**
 * Created by yongliu on 7/12/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/12/2016 11:32
 */
public class WebDriverExceptionTakeScreenshotListener implements WebDriverEventListener {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  Logger logger = Logger.getLogger(getClass());

  private String baseDir = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#afterChangeValueOf(org.openqa.selenium.WebElement, org.openqa.selenium.WebDriver)
   */
  @Override public void afterChangeValueOf(WebElement webElement, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#afterClickOn(org.openqa.selenium.WebElement, org.openqa.selenium.WebDriver)
   */
  @Override public void afterClickOn(WebElement webElement, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#afterFindBy(org.openqa.selenium.By,org.openqa.selenium.WebElement,
   *       org.openqa.selenium.WebDriver)
   */
  @Override public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#afterNavigateBack(org.openqa.selenium.WebDriver)
   */
  @Override public void afterNavigateBack(WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#afterNavigateForward(org.openqa.selenium.WebDriver)
   */
  @Override public void afterNavigateForward(WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#afterNavigateRefresh(org.openqa.selenium.WebDriver)
   */
  @Override public void afterNavigateRefresh(WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#afterNavigateTo(java.lang.String, org.openqa.selenium.WebDriver)
   */
  @Override public void afterNavigateTo(String s, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#afterScript(java.lang.String, org.openqa.selenium.WebDriver)
   */
  @Override public void afterScript(String s, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#beforeChangeValueOf(org.openqa.selenium.WebElement, org.openqa.selenium.WebDriver)
   */
  @Override public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#beforeClickOn(org.openqa.selenium.WebElement, org.openqa.selenium.WebDriver)
   */
  @Override public void beforeClickOn(WebElement webElement, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#beforeFindBy(org.openqa.selenium.By,org.openqa.selenium.WebElement,
   *       org.openqa.selenium.WebDriver)
   */
  @Override public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#beforeNavigateBack(org.openqa.selenium.WebDriver)
   */
  @Override public void beforeNavigateBack(WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#beforeNavigateForward(org.openqa.selenium.WebDriver)
   */
  @Override public void beforeNavigateForward(WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#beforeNavigateRefresh(org.openqa.selenium.WebDriver)
   */
  @Override public void beforeNavigateRefresh(WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#beforeNavigateTo(java.lang.String, org.openqa.selenium.WebDriver)
   */
  @Override public void beforeNavigateTo(String s, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#beforeScript(java.lang.String, org.openqa.selenium.WebDriver)
   */
  @Override public void beforeScript(String s, WebDriver webDriver) { }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for base dir.
   *
   * @return  String
   */
  public String getBaseDir() {
    if (baseDir == null) {
      return "";
    }

    return baseDir;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.openqa.selenium.support.events.WebDriverEventListener#onException(java.lang.Throwable, org.openqa.selenium.WebDriver)
   */
  @Override public void onException(Throwable throwable, WebDriver webDriver) {
    if (logger.isDebugEnabled()) {
      logger.debug(throwable.getMessage());
    }

    String filename = generateRandomFilename(throwable);

    try {
      String screenshot = extractScreenShot(throwable);

      if (logger.isDebugEnabled()) {
        logger.debug("Starting catch the screenshot......");
      }

      if (screenshot != null) {
        byte[]           btDataFile = Base64.decodeBase64(screenshot.getBytes());
        File             of         = new File(filename);
        FileOutputStream osf        = new FileOutputStream(of);
        osf.write(btDataFile);
        osf.flush();
        osf.close();

        if (logger.isDebugEnabled()) {
          logger.debug("Catched the screenshot and saved on path: " + filename);
        }
      }

    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  } // end method onException

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for base dir.
   *
   * @param  baseDir  String
   */
  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String extractScreenShot(Throwable ex) {
    Throwable cause = ex.getCause();

    if (cause instanceof ScreenshotException) {
      return ((ScreenshotException) cause).getBase64EncodedScreenshot();
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Not the ScreenshotException, so can not take screen shot: " + cause.getMessage());
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String generateRandomFilename(Throwable ex) {
    Calendar c        = Calendar.getInstance();
    String   filename = ex.getMessage();
    int      i        = filename.indexOf('\n');
    filename = filename.substring(0, i).replaceAll("\\s", "_").replaceAll(":", "")
      + ".png";
    filename = "" + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH)
      + "-" + c.get(Calendar.DAY_OF_MONTH) + "-"
      + c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE)
      + "-" + c.get(Calendar.SECOND) + "-" + filename;

    if (logger.isDebugEnabled()) {
      logger.debug("Generated the file name without path is: " + filename);
      logger.debug("The base dir is: " + getBaseDir());
      logger.debug("So the full path is: " + getBaseDir() + filename);
    }


    return getBaseDir() + filename;
  }
} // end class WebDriverExceptionTakeScreenshotListener
