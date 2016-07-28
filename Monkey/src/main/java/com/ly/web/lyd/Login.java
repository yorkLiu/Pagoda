package com.ly.web.lyd;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ly.web.base.AbstractObject;


/**
 * Created with IntelliJ IDEA. User: yongliu Date: 8/20/14 Time: 12:21 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
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
   * @param  webDriver  DOCUMENT ME!
   * @param  url        DOCUMENT ME!
   */
  public Login(WebDriver webDriver, String url) {
    this.webDriver = webDriver;
    this.url       = url;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   userName  DOCUMENT ME!
   * @param   password  DOCUMENT ME!
   * @param   first     DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void login(String userName, String password, boolean first) throws Exception {
    if (first) {
      this.webDriver.get(this.url);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("begin login-----");
    }

    if (!webDriver.getCurrentUrl().contains("login")) {
      navigateTo(this.url);
    }

    (new WebDriverWait(this.webDriver, 30)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return null != d.findElement(By.id("un"));
        }
      });

    WebElement username = this.webDriver.findElement(By.id("un"));
    username.clear();
    username.sendKeys(userName);

    WebElement pwd = this.webDriver.findElement(By.id("pwd"));
    pwd.clear();
    pwd.sendKeys(password);

    WebElement loginButton = this.webDriver.findElement(By.id("login_button"));
    
    // validator code
    WebElement vCodeDive = this.webDriver.findElement(By.id("vcd_div"));

//    WebElement loginError = this.webDriver.findElement(By.id("error_tips"));
    
    
    logger.info("vcode div is css value:" + vCodeDive.getCssValue("display"));
    

    // click login button
    loginButton.click();

    logger.info("After click 'Submit' the vcode div is css value:" + vCodeDive.getCssValue("display"));

// Thread.sleep(1000);
//
// if (loginError.getText().contains("账号和密码不匹配，请重新输入")) {
// throw new Exception("账号和密码不匹配，请重新输入");
// }

    (new WebDriverWait(this.webDriver, 3000)).until(new ExpectedCondition<Boolean>() {
        @Override public Boolean apply(WebDriver d) {
          return (!d.getCurrentUrl().contains("login"));
        }
      });

    if (logger.isDebugEnabled()) {
      logger.debug("login success with ---" + userName);
    }
  } // end method login

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   */
  @Override public void logout() {
    webDriver.quit();
  }

} // end class Login
