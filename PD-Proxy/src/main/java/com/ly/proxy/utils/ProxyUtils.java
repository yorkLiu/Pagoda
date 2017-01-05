package com.ly.proxy.utils;

import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.apache.log4j.Logger;

import org.springframework.util.StringUtils;

import com.ly.proxy.exceptions.InvalidIpProxyException;


/**
 * Created by yongliu on 12/30/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/30/2016 10:57
 */
public class ProxyUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger logger = Logger.getLogger(ProxyUtils.class);

  private static CloseableHttpClient client;

  static {
    client = HttpClientBuilder.create().build();
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * verifyIpProxy.
   *
   * @param   ipProxy           String
   * @param   verifyUrl         String
   * @param   verifyPassedText  String
   *
   * @return  Boolean
   *
   * @throws  Exception  exception
   */
  public static Boolean verifyIpProxy(String ipProxy, String verifyUrl, String verifyPassedText) throws Exception {
    Boolean result = Boolean.TRUE;

    if ((verifyUrl != null) && StringUtils.hasText(verifyUrl)) {
      logger.info("Verify the IP Proxy[" + ipProxy + "].... for visit: " + verifyUrl);

      String[] ipAndPort = ipProxy.split(":");
      String   ip        = ipAndPort[0];
      Integer  port      = new Integer(ipAndPort[1]);


      URL               url           = new URL(verifyUrl);
      InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
      Proxy             proxy         = new Proxy(Proxy.Type.HTTP, socketAddress); // http 代理
      long              startTime     = System.currentTimeMillis();
      URLConnection     conn          = url.openConnection(proxy);
      long              endTime       = System.currentTimeMillis();
      InputStream       in            = null;
      HttpURLConnection httpConn      = (HttpURLConnection) conn;

      httpConn.setConnectTimeout(30000);
      httpConn.setReadTimeout(30000);

      logger.info("Visit verify url response code:" + httpConn.getResponseCode());

      if ((httpConn.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED)
            || (httpConn.getResponseCode() == HttpURLConnection.HTTP_CREATED)
            || (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)) {
        in = httpConn.getInputStream();

      } else {
        in = httpConn.getErrorStream();
      }

      String responseHtml = IOUtils.toString(in, Charset.forName("UTF-8"));

      if (responseHtml.indexOf(verifyPassedText) > 0) {
        long readStreamTime = System.currentTimeMillis();
        logger.info("The IP[" + ipProxy + "] can be used, and the speed is: " + (endTime - startTime)
          + "ms. plus read stream spent: " + (readStreamTime - startTime) + "ms.");

        result = Boolean.TRUE;
      } else {
        result = Boolean.FALSE;
      }
    } // end if

    return result;
  } // end method verifyIpProxy

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * verifyIpProxyByHttpClient.
   *
   * @param   ipProxy           String
   * @param   verifyUrl         String
   * @param   verifyPassedText  String
   *
   * @return  Boolean
   *
   * @throws  InvalidIpProxyException  exception
   */
  public static Boolean verifyIpProxyByHttpClient(String ipProxy, String verifyUrl, String verifyPassedText)
    throws InvalidIpProxyException {
    return verifyIpProxyByHttpClient(ipProxy, verifyUrl, verifyPassedText, "HTTP");
  } // end method verifyIpProxyByHttpClient

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * verifyIpProxyByHttpClient.
   *
   * @param   ipProxy           String
   * @param   verifyUrl         String
   * @param   verifyPassedText  String
   * @param   protocol          String (HTTP, HTTPS)
   *
   * @return  Boolean
   *
   * @throws  InvalidIpProxyException  exception
   */
  public static Boolean verifyIpProxyByHttpClient(String ipProxy, String verifyUrl, String verifyPassedText,
    String protocol) throws InvalidIpProxyException {
    Boolean  result    = Boolean.FALSE;
    String[] ipAndPort = ipProxy.split(":");
    String   ip        = ipAndPort[0];
    Integer  port      = new Integer(ipAndPort[1]);
    protocol = ((protocol != null) && StringUtils.hasText(protocol)) ? protocol : "HTTP";

    if ((verifyUrl != null) && StringUtils.hasText(verifyUrl)) {
      HttpHost      proxy     = new HttpHost(ip, port, protocol);
      int           timeoutMi = 10000; // 10s
      RequestConfig config    = RequestConfig.custom().setProxy(proxy).setConnectionRequestTimeout(timeoutMi)
        .setConnectTimeout(timeoutMi).setSocketTimeout(timeoutMi).build();

      HttpGet httpGet = new HttpGet(verifyUrl);
      httpGet.setConfig(config);

      try {
        long                  startTime  = System.currentTimeMillis();
        CloseableHttpResponse response   = client.execute(httpGet);
        int                   statusCode = response.getStatusLine().getStatusCode();
        long                  endTime    = System.currentTimeMillis();
        logger.info("Visit verify url response code: " + statusCode);

        if ((statusCode == HttpURLConnection.HTTP_ACCEPTED)
              || (statusCode == HttpURLConnection.HTTP_CREATED)
              || (statusCode == HttpURLConnection.HTTP_OK)) {
          String html = EntityUtils.toString(response.getEntity(), "UTF-8");

          if (html.indexOf(verifyPassedText) > 0) {
            long readStreamTime = System.currentTimeMillis();
            logger.info("The IP[" + ipProxy + "] can be used, and the speed is: " + (endTime - startTime)
              + "ms. plus read stream spent: " + (readStreamTime - startTime) + "ms.");

            result = Boolean.TRUE;
          }
        }

        httpGet.releaseConnection();
      } catch (Exception e) {
        result = Boolean.FALSE;
        logger.error("The IP[" + ipProxy + "] was invalid.");
        throw new InvalidIpProxyException(ip, port, "The IP[" + ipProxy + "] was invalid.", e);
      } // end try-catch
    }   // end if

    return result;

  } // end method verifyIpProxyByHttpClient


} // end class ProxyUtils
