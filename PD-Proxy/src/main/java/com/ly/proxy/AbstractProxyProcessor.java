package com.ly.proxy;

import com.ly.utils.ExcelUtils;
import org.apache.log4j.Logger;


/**
 * Created by yongliu on 6/27/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/28/2017 17:22
 */
public abstract class AbstractProxyProcessor {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  protected Boolean allowGetIpAddress    = Boolean.FALSE;

  /** TODO: DOCUMENT ME! */
  protected Boolean allowVerifyIpAddress = Boolean.FALSE;
  private String _allowVerifyIpAddress;

  /** TODO: DOCUMENT ME! */
  protected String ipProxyRetryServiceUrl;

  /** TODO: DOCUMENT ME! */
  protected String ipProxyServiceUrl;

  /** TODO: DOCUMENT ME! */
  protected Logger logger = Logger.getLogger(getClass());


  /** TODO: DOCUMENT ME! */
  protected String verifyPassedText;

  /** TODO: DOCUMENT ME! */
  protected String verifyUrl;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for ip proxy.
   *
   * @param   province  String
   *
   * @return  String
   */
  public abstract String getIpProxy(String province);

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * getter method for allow get ip address.
   *
   * @return  Boolean
   */
  public Boolean getAllowGetIpAddress() {
    return allowGetIpAddress;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for allow verify ip address.
   *
   * @return  Boolean
   */
  public Boolean getAllowVerifyIpAddress() {
    if (null == allowVerifyIpAddress) {
      return Boolean.FALSE;
    }

    return allowVerifyIpAddress;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for ip proxy retry service url.
   *
   * @return  String
   */
  public String getIpProxyRetryServiceUrl() {
    return ipProxyRetryServiceUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for ip proxy service url.
   *
   * @return  String
   */
  public String getIpProxyServiceUrl() {
    return ipProxyServiceUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for verify passed text.
   *
   * @return  String
   */
  public String getVerifyPassedText() {
    return verifyPassedText;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for verify url.
   *
   * @return  String
   */
  public String getVerifyUrl() {
    return verifyUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for allow get ip address.
   *
   * @param  allowGetIpAddress  Boolean
   */
  public void setAllowGetIpAddress(Boolean allowGetIpAddress) {
    this.allowGetIpAddress = allowGetIpAddress;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for allow verify ip address.
   *
   * @param  allowVerifyIpAddress  Boolean
   */
  public void setAllowVerifyIpAddress(Boolean allowVerifyIpAddress) {
    this.allowVerifyIpAddress = allowVerifyIpAddress;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for ip proxy retry service url.
   *
   * @param  ipProxyRetryServiceUrl  String
   */
  public void setIpProxyRetryServiceUrl(String ipProxyRetryServiceUrl) {
    this.ipProxyRetryServiceUrl = ipProxyRetryServiceUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for ip proxy service url.
   *
   * @param  ipProxyServiceUrl  String
   */
  public void setIpProxyServiceUrl(String ipProxyServiceUrl) {
    this.ipProxyServiceUrl = ipProxyServiceUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for verify passed text.
   *
   * @param  verifyPassedText  String
   */
  public void setVerifyPassedText(String verifyPassedText) {
    this.verifyPassedText = verifyPassedText;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for verify url.
   *
   * @param  verifyUrl  String
   */
  public void setVerifyUrl(String verifyUrl) {
    this.verifyUrl = verifyUrl;
  }

  public String get_allowVerifyIpAddress() {
    return _allowVerifyIpAddress;
  }

  public void set_allowVerifyIpAddress(String _allowVerifyIpAddress) {
    this._allowVerifyIpAddress = _allowVerifyIpAddress;
    this.setAllowVerifyIpAddress(ExcelUtils.getBooleanValue(_allowVerifyIpAddress));
  }
} // end class AbstractProxyProcessor
