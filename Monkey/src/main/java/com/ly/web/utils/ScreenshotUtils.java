package com.ly.web.utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;


/**
 * Created by yongliu on 7/8/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/08/2016 13:23
 * @visit    http://m635674608.iteye.com/blog/2082242
 */
public class ScreenshotUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  public static final Logger log = Logger.getLogger(ScreenshotUtils.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * createElementImage.
   *
   * @param   driver      WebDriver
   * @param   webElement  WebElement
   *
   * @return  BufferedImage
   *
   * @throws  IOException  exception
   */
  public static BufferedImage createElementImage(WebDriver driver, WebElement webElement) throws IOException {
    // 获得webElement的位置和大小。
//    Point     location = webElement.getLocation();
    Point     location = webElement.getLocation();
    log.info("location:" + location);
    Dimension size     = webElement.getSize();
    
    // 创建全屏截图。
    BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(takeScreenshot(driver)));


    // 截取webElement所在位置的子图。
    BufferedImage croppedImage = originalImage.getSubimage(
        location.getX(),
        location.getY(),
        size.getWidth(),
        size.getHeight());

    return croppedImage;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * snapshot
   * @param driverName
   * @param filename
   */
  public static void snapshot(TakesScreenshot driverName, String filename) {
    // this method will take screen shot ,require two parameters ,one is driver name, another is file name
    File scrFile = driverName.getScreenshotAs(OutputType.FILE);

    // Now you can do whatever you need to do with it, for example copy somewhere
    try {
      if (log.isDebugEnabled()) {
        log.debug("save snapshot path is:" + filename);
      }

      FileUtils.copyFile(scrFile, new File(filename));

      if (log.isDebugEnabled()) {
        log.debug("Saved image to path:" + filename + " successfully.");
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      log.error("Can't save screenshot");
      e.printStackTrace();
    } finally {
      log.error("screen shot finished");
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * snapshot2.
   *
   * @param  driverName  WebDriver
   * @param  filename    String
   */
  public static void snapshot2(WebDriver driverName, String filename) {
    // this method will take screen shot ,require two parameters ,one is driver name, another is file name
    // File scrFile = drivername.getScreenshotAs(OutputType.FILE);
    // Now you can do whatever you need to do with it, for example copy somewhere
    try {
      WebDriver augmentedDriver = new Augmenter().augment(driverName);
      File      screenshot      = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);

      if (log.isDebugEnabled()) {
        log.debug("Saved image to path:" + filename);
      }

      File file = new File(filename);
      FileUtils.copyFile(screenshot, file);

      if (log.isDebugEnabled()) {
        log.debug("Saved image to path:" + filename + " successfully.");
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      log.error("Can't save screenshot");
      e.printStackTrace();
    } finally {
      log.error("screen shot finished");
    }
  } // end method snapshot2

  //~ ------------------------------------------------------------------------------------------------------------------

  private static byte[] takeScreenshot(WebDriver driver) throws IOException {
    WebDriver augmentedDriver = new Augmenter().augment(driver);

    return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
      // TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
      // return takesScreenshot.getScreenshotAs(OutputType.BYTES);
  }


} // end class ScreenshotUtils
