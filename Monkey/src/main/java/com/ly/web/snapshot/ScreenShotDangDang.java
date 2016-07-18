package com.ly.web.snapshot;

import java.awt.image.BufferedImage;

import java.io.File;

import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.util.Assert;

import com.ly.web.base.AbstractObject;
import com.ly.web.constant.Constant;
import com.ly.web.utils.ScreenshotUtils;


/**
 * Created by yongliu on 7/8/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/08/2016 13:52
 * 
 * @issues: chrome getLocation issue
 *          URL: https://bugs.chromium.org/p/chromedriver/issues/detail?id=744
 */
public class ScreenShotDangDang extends AbstractObject {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String LOGIN_PAGE_URL = "https://login.dangdang.com/signin.aspx";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String imageBasePath = null;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ScreenShotDangDang object.
   *
   * @param  webDriver      WebDriver
   * @param  imageBasePath  String
   */
  public ScreenShotDangDang(WebDriver webDriver, String imageBasePath) {
    Assert.notNull(webDriver);
    Assert.notNull(imageBasePath);
    this.webDriver     = webDriver;
    this.imageBasePath = imageBasePath;

  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * login.
   */
  public void login() {
    try {
      webDriver.manage().window().maximize();
      navigateTo(LOGIN_PAGE_URL);
      webDriver.manage().window().maximize();
      webDriver.manage().window().maximize();
      webDriver.manage().window().maximize();

      ///wait the info element display then process
      String flag = (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<String>() {
            @Override public String apply(WebDriver d) {
              WebElement ele = null;

              try {
                ele = webDriver.findElement(By.xpath("//*[@id='txtUsername']"));
              } catch (NoSuchElementException e) {
                return Constant.NOT_FOUND;
              }

              return Constant.OK;
            }
          });

      Thread.sleep(3000);
      WebElement J_loginMaskClose              = this.webDriver.findElement(By.xpath("//a[@id='J_loginMaskClose']"));
      
      if(J_loginMaskClose != null){
        J_loginMaskClose.click();
      }
      
      WebElement usernameFld              = this.webDriver.findElement(By.xpath("//input[@id='txtUsername']"));
      WebElement passwordFld              = this.webDriver.findElement(By.xpath("//input[@id='txtPassword']"));
      WebElement validCodeDiv             = this.webDriver.findElement(By.xpath("//div[@id='inputcode']"));
      Thread.sleep(3000);
      WebElement validCodeImg             = this.webDriver.findElement(By.xpath("//img[@id='imgVcode']"));
      String     validCodeDivDisplayValue = validCodeDiv.getCssValue("display");

      if (logger.isDebugEnabled()) {
        logger.debug("validCodeDivDisplayValue:" + validCodeDivDisplayValue);
      }

      // the address of vcode
      String vCodeSrc = validCodeImg.getAttribute("src");

      if (logger.isDebugEnabled()) {
        logger.debug("The valid code src:" + vCodeSrc);
        logger.debug("The valid code position: " + validCodeImg.getLocation().toString());
      }

      usernameFld.sendKeys("Tester0001");
      passwordFld.sendKeys("XXX000");

      String        descFullName = imageBasePath + new Date().getTime() + ".png";
      ScreenshotUtils.snapshot2(webDriver, descFullName);
      if (logger.isDebugEnabled()) {
        logger.debug("Take full screen snapshot to: " + descFullName + " successfully.");
      }
      

      BufferedImage img      = ScreenshotUtils.createElementImage(webDriver, validCodeImg);
      String        descName = imageBasePath + new Date().getTime() + ".png";

      if (logger.isDebugEnabled()) {
        logger.debug("Save valid code image to: " + descName);
      }

      ImageIO.write(img, "png", new File(descName));

      if (logger.isDebugEnabled()) {
        logger.debug("Saved valid code image to: " + descName + " successfully.");
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } catch (Throwable e) {
      logger.error(e.getMessage(), e);
    } // end try-catch


  } // end method login


  public static void main(String[] args) {
    try {
//    System.setProperty("webdriver.chrome.driver", "/Users/yongliu/Project/AutoMessage/JDMessage/chromedriver");
      System.setProperty("webdriver.chrome.driver", "/Users/yongliu/Project/chromedriver/chromedriver");
      DesiredCapabilities capability = DesiredCapabilities.chrome();
      capability.setJavascriptEnabled(true);
    WebDriver driver =new ChromeDriver(capability);
//    driver.manage().window().maximize();
    driver.manage().window().setSize(new Dimension(1440, 810));


//    FirefoxProfile profile = new FirefoxProfile();
//    profile.setAcceptUntrustedCertificates(true);
//    profile.setAssumeUntrustedCertificateIssuer(true);
//    profile.setEnableNativeEvents(true);
//    profile.setAlwaysLoadNoFocusLib(true);

//    WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("/Applications/Firefox.app/Contents/MacOS/firefox")), profile);

      String basePath = "/Users/yongliu/Downloads/temp/";


      ScreenShotDangDang screenShotDangDang = new ScreenShotDangDang(driver, basePath);
      screenShotDangDang.login();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

} // end class ScreenShotDangDang
