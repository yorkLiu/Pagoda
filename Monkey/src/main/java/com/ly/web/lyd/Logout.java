package com.ly.web.lyd;

import org.openqa.selenium.WebDriver;

import org.springframework.util.Assert;

import com.ly.web.base.YHDAbstractObject;
import com.ly.web.constant.Constant;


/**
 * Created by yongliu on 7/13/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/13/2016 17:06
 */
public class Logout extends YHDAbstractObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Boolean closeDriver = Boolean.FALSE;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new Logout object.
   */
  public Logout() { }

  /**
   * Creates a new Logout object.
   *
   * @param  driver  WebDriver
   */
  public Logout(WebDriver driver) {
    Assert.notNull(driver);
    this.webDriver = driver;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * doLogout.
   */
  public void doLogout() {
    try {
      String logoutUrl = Constant.YHD_LOGOUT_URL;

      if (logger.isDebugEnabled()) {
        logger.debug("Navigate to logout url:" + logoutUrl);
      }

      navigateTo(logoutUrl);

      if (logger.isDebugEnabled()) {
        logger.debug("Logout successfully.");
        logger.debug("Does close driver after logout: " + getCloseDriver());
      }

      if (getCloseDriver()) {
        webDriver.close();

        if (logger.isDebugEnabled()) {
          logger.debug("Driver was closed.");
        }
      }
      
      navigateTo(Constant.YHD_LOGIN_PAGE_URL);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } // end try-catch


  } // end method doLogout

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for close driver.
   *
   * @return  Boolean
   */
  public Boolean getCloseDriver() {
    if (closeDriver == null) {
      return Boolean.FALSE;
    }

    return closeDriver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for close driver.
   *
   * @param  closeDriver  Boolean
   */
  public void setCloseDriver(Boolean closeDriver) {
    this.closeDriver = closeDriver;
  }
} // end class Logout
