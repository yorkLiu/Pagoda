package com.ly.proxy;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import java.nio.charset.Charset;

import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.http.ResponseEntity;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import org.springframework.web.client.RestTemplate;

import com.ly.proxy.utils.ProxyUtils;


/**
 * Created by yongliu on 12/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/02/2016 17:37
 */
public class ProxyProcessor implements InitializingBean {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final int RESPONSE_OK_STATUS = 200;

  private static CloseableHttpClient client;

  static {
    client = HttpClientBuilder.create().build();
  }

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Boolean allowGetIpAddress = Boolean.FALSE;

  private String ipProxyRetryServiceUrl;
  private String ipProxyServiceUrl;

  private Logger logger           = Logger.getLogger(getClass());
  private String verifyPassedText;
  private String verifyUrl;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * main.
   *
   * @param  args  String[]
   */
  public static void main(String[] args) {
    try {
      ProxyProcessor proxyProcessor = new ProxyProcessor();
      proxyProcessor.setAllowGetIpAddress(true);
      proxyProcessor.setIpProxyRetryServiceUrl("asd");
      proxyProcessor.setVerifyUrl("http://www.yhd.com");
      proxyProcessor.setVerifyPassedText("1号店");
// proxyProcessor.verifyIpProxy("202.96.63.172:80");
      ProxyUtils.verifyIpProxy("202.96.63.172:80", proxyProcessor.verifyUrl, proxyProcessor.verifyPassedText);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

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
      logger.info("Ready to found IP Proxy for province: " + province);

      String serviceUrl = null;

      if ((null != province) && StringUtils.hasText(province)) {
        String areaParam = "&area=" + new String(province.getBytes("UTF-8"), "UTF-8");
        serviceUrl = this.ipProxyServiceUrl + areaParam;
      }

      String ip = getIpFromProxyServer(serviceUrl);

      if (allowGetIpAddress) {
        String ipOnly = ip.split(":")[0];
        logger.info("Will get ip[" + ipOnly + "] address.");

        String address = IPAddressGetter.getIpAddressInfo(ipOnly);
        logger.info("The IP[" + ip + "] address is: " + address);
      }

// Boolean flag = ProxyUtils.verifyIpProxy(ip, this.verifyUrl, this.verifyPassedText);
      Boolean flag = ProxyUtils.verifyIpProxyByHttpClient(ip, this.verifyUrl, this.verifyPassedText);

      if (flag) {
        logger.info("The IP Proxy[" + ip + "] was valid");

        return ip;
      } else {
        logger.info("The IP Proxy[" + ip + "] was invalid, will retry to get ip");

        return getIpProxy(province);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());

      try {
        Thread.sleep(5000);
        logger.info("Sleep 5 seconds.");
      } catch (Exception ex) { }

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

  private String getIpFromProxyServer(String ipProxyServerUrl) throws IOException {
    String  ipProxy = null;
    Boolean founded = Boolean.FALSE;
    logger.info("Visit IP Proxy Service URL: " + ipProxyServerUrl);
    Assert.notNull(client, "HttpClient could not be null.");

    HttpGet               httpGet      = new HttpGet(ipProxyServerUrl);
    CloseableHttpResponse response     = client.execute(httpGet);
    String                responseBody = EntityUtils.toString(response.getEntity());

    if ((response.getStatusLine().getStatusCode() == RESPONSE_OK_STATUS) && !responseBody.contains("ERROR")) {
      ipProxy = responseBody;
      logger.info("Get the ip Proxy:" + ipProxy);
      founded = Boolean.TRUE;
    }

    httpGet.releaseConnection();

    if (founded) {
      return ipProxy;
    } else {
      // not found the ip for @ipProxyServerUrl
      // retry find the ip by @this.ipProxyRetryServiceUrl
      logger.info("Retry find ip....");

      return getIpFromProxyServer(this.ipProxyRetryServiceUrl);
    }

// RestTemplate           restTemplate = new RestTemplate();
// ResponseEntity<String> result       = restTemplate.getForEntity(ipProxyServerUrl, String.class);
// String responseBody = result.getBody();
// logger.info("IP Proxy Server url:" + ipProxyServerUrl);
// logger.info("Status code:" + result.getStatusCodeValue());
// logger.info("Body text:" + responseBody);
//
// if ((result.getStatusCodeValue() == RESPONSE_OK_STATUS) && !responseBody.contains("ERROR")) {
// ipProxy = result.getBody();
// logger.info("Get the ip Proxy:" + ipProxy);
//
// return ipProxy;
// } else {
// // not found the ip for @ipProxyServerUrl
// // retry find the ip by @this.ipProxyRetryServiceUrl
// logger.info("Retry find ip....");
//
// return getIpFromProxyServer(this.ipProxyRetryServiceUrl);
// }
  } // end method getIpFromProxyServer
} // end class ProxyProcessor
