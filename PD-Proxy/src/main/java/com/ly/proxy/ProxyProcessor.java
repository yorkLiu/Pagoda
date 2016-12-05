package com.ly.proxy;

import java.io.InputStream;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.http.ResponseEntity;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import org.springframework.web.client.RestTemplate;


/**
 * Created by yongliu on 12/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/02/2016 17:37
 */
public class ProxyProcessor implements InitializingBean {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final int RESPONSE_OK_STATUS = 200;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Boolean allowGetIpAddress = Boolean.FALSE;

  private String ipProxyRetryServiceUrl;
  private String ipProxyServiceUrl;

  private Logger logger           = Logger.getLogger(getClass());
  private String verifyPassedText;
  private String verifyUrl;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    logger.info("ipProxyServiceUrl:" + ipProxyServiceUrl);
    logger.info("ipProxyRetryServiceUrl:" + ipProxyRetryServiceUrl);
    logger.info("verifyPassedText:" + verifyPassedText);
    logger.info("verifyUrl:" + verifyUrl);
    logger.info("allowGetIpAddress:" + allowGetIpAddress);
    Assert.notNull(ipProxyServiceUrl);
    Assert.notNull(ipProxyRetryServiceUrl);
    Assert.notNull(verifyPassedText);
    Assert.notNull(verifyUrl);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get ip proxy without province, will get the ip proxy by dynamic.
   *
   * @return  get ip proxy without province, will get the ip proxy by dynamic.
   */
  public String getIpProxy() {
    return getIpProxy(null);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Get the ip proxy by parameter @province, if not found a ip for @province, will find the ip by
   * &#064;ipProxyRetryServiceUrl.
   *
   * @param   province  String
   *
   * @return  String
   */
  public String getIpProxy(String province) {
    try {
      if ((null != province) && StringUtils.hasText(province)) {
        String areaParam = "&area=" + new String(province.getBytes("UTF-8"), "UTF-8");
        this.ipProxyServiceUrl += areaParam;
      }

      String ip = getIpFromProxyServer(this.ipProxyServiceUrl);

      Boolean flag = verifyIpProxy(ip);

      if (flag) {
        logger.info("The IP Proxy[" + ip + "] was valid");

        if (allowGetIpAddress) {
          String ipOnly = ip.split(":")[0];
          logger.info("Will get ip[" + ipOnly + "] address.");

          String address = IPAddressGetter.getIpAddressInfo(ipOnly);
          logger.info("The IP[" + ip + "] address is: " + address);
        }

        return ip;
      } else {
        logger.info("The IP Proxy[" + ip + "] was invalid, will retry to get ip");

        return getIpProxy(province);
      }
    } catch (Exception e) {
      return getIpProxy(province);
    } // end try-catch
  }   // end method getIpProxy

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

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getIpFromProxyServer(String ipProxyServerUrl) {
    String                 ipProxy      = null;
    RestTemplate           restTemplate = new RestTemplate();
    ResponseEntity<String> result       = restTemplate.getForEntity(ipProxyServerUrl, String.class);

    String responseBody = result.getBody();
    logger.info("IP Proxy Server url:" + ipProxyServerUrl);
    logger.info("Status code:" + result.getStatusCodeValue());
    logger.info("Body text:" + responseBody);

    if ((result.getStatusCodeValue() == RESPONSE_OK_STATUS) && !responseBody.contains("ERROR")) {
      ipProxy = result.getBody();
      logger.info("Get the ip Proxy:" + ipProxy);

      return ipProxy;
    } else {
      // not found the ip for @ipProxyServerUrl
      // retry find the ip by @this.ipProxyRetryServiceUrl
      logger.info("Retry find ip....");

      return getIpFromProxyServer(this.ipProxyRetryServiceUrl);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private Boolean verifyIpProxy(String ipProxy) throws Exception {
    logger.info("Verify the IP Proxy[" + ipProxy + "].... for visit: " + verifyUrl);

    String[] ipAndPort = ipProxy.split(":");
    String   ip        = ipAndPort[0];
    Integer  port      = new Integer(ipAndPort[1]);


    URL               url           = new URL(this.verifyUrl);
    InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
    Proxy             proxy         = new Proxy(Proxy.Type.HTTP, socketAddress); // http 代理
    long              startTime     = System.currentTimeMillis();
    URLConnection     conn          = url.openConnection(proxy);
    long              endTime       = System.currentTimeMillis();

    InputStream in           = conn.getInputStream();
    String      responseHtml = IOUtils.toString(in, Charset.forName("UTF-8"));

    if (responseHtml.indexOf(verifyPassedText) > 0) {
      long readStreamTime = System.currentTimeMillis();
      logger.info("The IP[" + ipProxy + "] can be used, and the speed is: " + (endTime - startTime)
        + "ms. plus read stream spent: " + (readStreamTime - startTime) + "ms.");

      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  } // end method verifyIpProxy
} // end class ProxyProcessor
