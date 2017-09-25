package com.ly.config;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.StringUtils;

import com.ly.utils.ExcelUtils;


/**
 * Created by yongliu on 11/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/02/2016 16:57
 */
public abstract class WebDriverProperties implements InitializingBean {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  public static final String DRIVER_CHROME = "chrome";

  /** TODO: DOCUMENT ME! */
  public static final String DRIVER_FIREFOX = "firefox";

  /** TODO: DOCUMENT ME! */
  public static final String DRIVER_IE = "ie";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected Logger logger = Logger.getLogger(getClass());

  private String chromeDriverPath;

  private String filesPath;
  private String firefoxDriverPath;
  private String ieDriverPath;

  /** only using when comment. */
  private Integer maxDelaySecondsForNext;

  /**
   * The flag of turn on or off the unlock account automatic. By default is 'FALSE' <code>Value: TRUE, Y, FALSE,
   * N</code>
   */
  private String unlockAccountAutomatic;

  /** when the login page show need input valid code, play voice file to warning. */
  private String warningVoiceFile;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    logger.info("chromeDriverPath: " + chromeDriverPath);
    logger.info("firefoxDriverPath: " + firefoxDriverPath);
    logger.info("ieDriverPath: " + ieDriverPath);
    logger.info("filesPath: " + filesPath);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for chrome driver path.
   *
   * @return  String
   */
  public String getChromeDriverPath() {
    return chromeDriverPath;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for driver type.
   *
   * @return  String
   */
  public String getDriverType() {
    if ((getChromeDriverPath() != null) && StringUtils.hasText(getChromeDriverPath())) {
      return DRIVER_CHROME;
    } else if ((getFirefoxDriverPath() != null) && StringUtils.hasText(getFirefoxDriverPath())) {
      return DRIVER_FIREFOX;
    } else if ((getIeDriverPath() != null) && StringUtils.hasText(getIeDriverPath())) {
      return DRIVER_IE;
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for files path.
   *
   * @return  String
   */
  public String getFilesPath() {
    return filesPath;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for firefox driver path.
   *
   * @return  String
   */
  public String getFirefoxDriverPath() {
    return firefoxDriverPath;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for ie driver path.
   *
   * @return  String
   */
  public String getIeDriverPath() {
    return ieDriverPath;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for max delay seconds for next.
   *
   * @return  Integer
   */
  public Integer getMaxDelaySecondsForNext() {
    if (null == maxDelaySecondsForNext) {
      return 60;
    }

    return maxDelaySecondsForNext;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for unlock account automatic.
   *
   * @return  Boolean
   */
  public Boolean getUnlockAccountAutomatic() {
    if (null == unlockAccountAutomatic) {
      return Boolean.FALSE;
    }

    return ExcelUtils.getBooleanValue(unlockAccountAutomatic);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for warning voice file.
   *
   * @return  String
   */
  public String getWarningVoiceFile() {
    return warningVoiceFile;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for chrome driver path.
   *
   * @param  chromeDriverPath  String
   */
  public void setChromeDriverPath(String chromeDriverPath) {
    if ((chromeDriverPath != null) && StringUtils.hasText(chromeDriverPath)) {
      this.chromeDriverPath = StringUtils.cleanPath(chromeDriverPath);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for files path.
   *
   * @param  filesPath  String
   */
  public void setFilesPath(String filesPath) {
    if ((filesPath != null) && StringUtils.hasText(filesPath)) {
      this.filesPath = StringUtils.cleanPath(filesPath);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for firefox driver path.
   *
   * @param  firefoxDriverPath  String
   */
  public void setFirefoxDriverPath(String firefoxDriverPath) {
    if ((firefoxDriverPath != null) && StringUtils.hasText(firefoxDriverPath)) {
      this.firefoxDriverPath = StringUtils.cleanPath(firefoxDriverPath);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for ie driver path.
   *
   * @param  ieDriverPath  String
   */
  public void setIeDriverPath(String ieDriverPath) {
    if ((ieDriverPath != null) && StringUtils.hasText(ieDriverPath)) {
      this.ieDriverPath = StringUtils.cleanPath(ieDriverPath);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for max delay seconds for next.
   *
   * @param  maxDelaySecondsForNext  Integer
   */
  public void setMaxDelaySecondsForNext(Integer maxDelaySecondsForNext) {
    this.maxDelaySecondsForNext = maxDelaySecondsForNext;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for unlock account automatic.
   *
   * @param  unlockAccountAutomatic  String
   */
  public void setUnlockAccountAutomatic(String unlockAccountAutomatic) {
    this.unlockAccountAutomatic = unlockAccountAutomatic;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for warning voice file.
   *
   * @param  warningVoiceFile  String
   */
  public void setWarningVoiceFile(String warningVoiceFile) {
    this.warningVoiceFile = warningVoiceFile;
  }
} // end class WebDriverProperties
