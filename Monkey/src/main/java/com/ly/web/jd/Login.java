package com.ly.web.jd;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ly.web.base.AbstractObject;


/**
 * Created by yongliu on 8/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/02/2016 15:55
 */
public class Login extends AbstractObject {
  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new Login object.
   */
  public Login() { }

  /**
   * Creates a new Login object.
   *
   * @param  webDriver  WebDriver
   * @param  url        String
   */
  public Login(WebDriver webDriver, String url) {
    this.webDriver = webDriver;
    this.url       = url;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * login.
   *
   * @param   userName  String
   * @param   password  String
   * @param   first     boolean
   *
   * @return  login.
   *
   * @throws  Exception  exception
   */
  public boolean login(String userName, String password, boolean first) throws Exception {
    boolean loginSuccess = Boolean.TRUE;

    try {
      if (first) {
        navigateTo(this.url);
      }

      if (logger.isDebugEnabled()) {
        logger.debug(">>>>>>>>>Begin Login JD>>>>>>>>>>>");
      }

      if (!webDriver.getCurrentUrl().contains("login")) {
        navigateTo(this.url);
      }

      loginSuccess = (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return null != d.findElement(By.id("loginname"));
          }
        });

      WebElement username = this.webDriver.findElement(By.id("loginname"));
      username.clear();
      username.sendKeys(userName);

      WebElement pwd = this.webDriver.findElement(By.id("nloginpwd"));
      pwd.clear();
      pwd.sendKeys(password);

      WebElement loginButton = this.webDriver.findElement(By.id("loginsubmit"));

      // validator code
      WebElement vCodeDive = this.webDriver.findElement(By.id("o-authcode"));
// WebElement vCodeInput = this.webDriver.findElement(By.id("authcode"));

      logger.info("vcode div is display:" + vCodeDive.isDisplayed());


      // click login button
      loginButton.click();

      delay(2);
      
      
      // need check the account is locked by jd
      // if locked, skip
      // ToDo
      

      // 如果browser 让用户连续几次输入"验证码", count ++
      // 如果不是连续输入,则忽略
      if (webDriver.getCurrentUrl().contains("login")) {
        logger.info("After click 'Submit' the vcode div isDisplay:" + vCodeDive.isDisplayed());

        if (vCodeDive.isDisplayed()) {
          String  driverType = getDriverType();
          Integer count      = JD.vCodeCountMap.get(driverType);

          if (count == null) {
            count = 1;
          } else {
            count++;
          }

          JD.vCodeCountMap.put(driverType, count);
        }
      } else {
        // no need input valid code.
        String driverType = getDriverType();

        if ((driverType != null) && (JD.vCodeCountMap.get(driverType) != null)) {
          JD.vCodeCountMap.put(driverType, 0);
        }
      }
      
      loginSuccess = (new WebDriverWait(this.webDriver, 3000)).until(new ExpectedCondition<Boolean>() {
          @Override public Boolean apply(WebDriver d) {
            return (!d.getCurrentUrl().contains("login"));
          }
        });

      if (logger.isDebugEnabled()) {
        logger.debug("<<<<<<<<<Login JD Successfully with:[" + userName + "]>>>>>>>>>>>");
      }
    } catch (TimeoutException e){
      logger.error(e.getMessage(), e);
      loginSuccess = Boolean.FALSE;
    }catch (Exception e) {
      logger.error(e.getMessage(), e);
      loginSuccess = Boolean.FALSE;
    } // end try-catch
    return loginSuccess;
  } // end method login


} // end class Login
