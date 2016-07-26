package com.ly.web.snapshot;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

// import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

// import com.chonseng.webmon.net.HtmlTools;
// import com.cyyun.imgapp.util.URLDownloader;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;


/**
 * Created by yongliu on 7/8/16.
 *
 * @visit    http://m635674608.iteye.com/blog/2082242
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/08/2016 10:38
 */
public class TakeSnapshotUtils {
  
  public static final Logger log = Logger.getLogger(TakeSnapshotUtils.class);
  
  public static final String IMAGE_BASE_PATH="/Users/yongliu/Downloads/temp/";
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
    Point     location = webElement.getLocation();
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
   * login.
   *
   * @param   driver  WebDriver
   *
   * @throws  Exception  exception
   */
  public static void login(WebDriver driver) throws Exception {
    // 新浪微博自己发出的评论地址
    // http://weibo.com/comment/outbox?wvr=1
    String login = "https://login.sina.com.cn/signup/signin.php?entry=homepage";
    driver.navigate().to(login);
    Thread.sleep(2000);

    WebElement keyWord = driver.findElement(By.xpath("//input[@name='username']"));
    keyWord.clear();
    keyWord.sendKeys("nbr987654321@163.com");
    keyWord = driver.findElement(By.xpath("//input[@name='password']"));
    keyWord.clear();
    keyWord.sendKeys("XXX");

    try {
      keyWord = driver.findElement(By.xpath("//img[@id='check_img']"));

      if (keyWord != null) {
        String src = keyWord.getAttribute("src");
        System.out.println("验证码地址 =====" + src);

        if ((src != null) && !"".equals(src)) {
          BufferedImage inputbig = createElementImage(driver, keyWord);
          ImageIO.write(inputbig, "png", new File(IMAGE_BASE_PATH+"testasdasd.png"));

          String key = "";

          // 验证码输入框
          keyWord = driver.findElement(By.xpath("//input[@id='door']"));
          keyWord.clear();
          keyWord.sendKeys(key);
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    keyWord = driver.findElement(By.xpath("//input[@class='btn_submit_m']"));
    keyWord.click();
    Thread.sleep(2000);

    try {
      keyWord = driver.findElement(By.xpath("//img[@id='check_img']"));

      if (keyWord != null) {
        String src = keyWord.getAttribute("src");
        System.out.println("验证码地址 =====" + src);

        if ((src != null) && !"".equals(src)) {
          snapshot2(driver, "open_baidu_sinaweibo1111111111.png");

          BufferedImage inputbig = createElementImage(driver, keyWord);
          ImageIO.write(inputbig, "png", new File(IMAGE_BASE_PATH + "testasdasd.png"));

          String key = "";

          // 验证码输入框
          keyWord = driver.findElement(By.xpath("//input[@id='door']"));
          keyWord.clear();
          keyWord.sendKeys(key);

          // 在提交
          keyWord = driver.findElement(By.xpath("//input[@class='btn_submit_m']"));
          keyWord.click();
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    } // end try-catch

    Thread.sleep(2000);
  } // end method login

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * main.
   *
   * @param   args  String[]
   *
   * @throws  Exception  exception
   */
  public static void main(String[] args) throws Exception {
    String serverUrl = "http://192.168.1.107:4444/wd/hub";
    String URL       = "http://weibo.com/comment/outbox?wvr=1"; // http://coral.qq.com/1008591939

    // System.setProperty("webdriver.chrome.driver", "d:\\ie\\chromedriver.exe");
    // WebDriver driver = new ChromeDriver();
    DesiredCapabilities capability = DesiredCapabilities.firefox();
    capability.setJavascriptEnabled(true);

//    WebDriver driver = new RemoteWebDriver(new URL(serverUrl), capability);
//    WebDriver driver = new RemoteWebDriver(capability);


//    FirefoxProfile profile = new FirefoxProfile();
//    profile.setAcceptUntrustedCertificates(true);
//    profile.setAssumeUntrustedCertificateIssuer(true);
//    profile.setEnableNativeEvents(true);
//    profile.setAlwaysLoadNoFocusLib(true);
//
//    WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("/Users/yongliu/Project/chromedriver/chromedriver")), profile);

    // System.setProperty("webdriver.chrome.driver", "/Users/yongliu/Project/chromedriver/chromedriver");
    System.setProperty("webdriver.chrome.driver", "/Users/yongliu/Project/chromedriver/chromedriver");

    WebDriver driver =new ChromeDriver();
    driver.manage().window().maximize();
    
    // 如果3s内还定位不到则抛出异常
//    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
//
//    // 页面加载超时时间设置为5s
//    // driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
//    // driver.manage.script_timeout = 3 #seconds
//    driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
    login(driver);
    snapshot2(driver, "open_baidu_sinaweibo1.png");

    // 跳转新页面
    // driver.navigate().to(URL);
    driver.get(URL);

    // max size the browser
    driver.manage().window().maximize();

    /*
           Navigation navigation = driver.navigate();
            navigation.to(URL);*/
    Thread.sleep(2000);

    // snapshot((TakesScreenshot)driver,"open_baidu.png");
    snapshot2(driver, "open_baidu_sinaweibo.png");


    driver.quit();


  } // end method main

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * snapshot.
   *
   * @param  drivername  TakesScreenshot
   * @param  filename    String
   */
  public static void snapshot(TakesScreenshot drivername, String filename) {
    // this method will take screen shot ,require two parameters ,one is driver name, another is file name


    File scrFile = drivername.getScreenshotAs(OutputType.FILE);

    // Now you can do whatever you need to do with it, for example copy somewhere
    try {
      log.info("save snapshot path is:"+ IMAGE_BASE_PATH + filename);
      FileUtils.copyFile(scrFile, new File(IMAGE_BASE_PATH + filename));
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
   * @param  drivername  WebDriver
   * @param  filename    String
   */
  public static void snapshot2(WebDriver drivername, String filename) {
    // this method will take screen shot ,require two parameters ,one is driver name, another is file name


    // File scrFile = drivername.getScreenshotAs(OutputType.FILE);
    // Now you can do whatever you need to do with it, for example copy somewhere
    try {
      WebDriver augmentedDriver = new Augmenter().augment(drivername);
      File      screenshot      = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
      log.info("Saved image to path:" + IMAGE_BASE_PATH + filename);
      File      file            = new File(IMAGE_BASE_PATH + filename);
      FileUtils.copyFile(screenshot, file);
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
   * takeScreenshot.
   *
   * @param   driver  WebDriver
   *
   * @return  byte[]
   *
   * @throws  IOException  exception
   */
  public static byte[] takeScreenshot(WebDriver driver) throws IOException {
    WebDriver augmentedDriver = new Augmenter().augment(driver);

    return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
      // TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
      // return takesScreenshot.getScreenshotAs(OutputType.BYTES);
  }

} // end class TakeSnapshotUtils
