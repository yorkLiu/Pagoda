package com.ly.proxy;

import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.apache.log4j.Logger;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.Assert;

import com.ly.proxy.pojo.ProxyIPInfo;


/**
 * Created by yongliu on 12/29/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/30/2016 10:29 The data from http://www.xicidaili.com
 */
public class IPProxyCrawler implements InitializingBean {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger log = Logger.getLogger(IPProxyCrawler.class);

  private static final int    IP_COLUMN             = 1;
  private static final int    PORT_COLUMN           = 2;
  private static final int    PROVINCE_COLUMN       = 3;
  private static final int    ANONYMOUS_TYPE_COLUMN = 4;
  private static final int    TYPE_COLUMN           = 5;
  private static final int    DELAY_COLUMN          = 6;
  private static final String CHARSET_UTF_8         = "UTF-8";

  /** HTTP Proxy. */
  protected static final String HTTP_PROTOCOL_BASE_URL = "http://www.xicidaili.com/wt/";

  /** HTTPS Proxy. */
  protected static final String HTTPS_PROTOCOL_BASE_URL = "http://www.xicidaili.com/wn/";

  /**高匿Proxy*/
  protected static final String ANONYMOUS_BASE_URL = "http://www.xicidaili.com/nn/";

  /**普通Proxy*/
  protected static final String NON_ANONYMOUS_BASE_URL = "http://www.xicidaili.com/nt/";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  protected String  baseUrl       = ANONYMOUS_BASE_URL;
  protected Integer fetchPageSize = 1;

  private CloseableHttpClient httpClient = null;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new IPProxyCrawler object.
   */
  public IPProxyCrawler() {
    httpClient = HttpClientBuilder.create().build();
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * main.
   *
   * @param  args  String[]
   */
  public static void main(String[] args) {
    try {
      IPProxyCrawler    ipProxyCrawler  = new IPProxyCrawler();
      Set<ProxyIPInfo> proxyIPInfoList = ipProxyCrawler.getIpProxy(10);

      System.out.println(proxyIPInfoList);
      log.info("Total:" + proxyIPInfoList.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    Assert.notNull(baseUrl, "BaseUrl could not be null.");
    Assert.notNull(fetchPageSize, "FetchPageSize could not be null.");
    if(httpClient == null){
      httpClient = HttpClientBuilder.create().build();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for ip proxy.
   *
   * @return  List
   */
  public Set<ProxyIPInfo> getIpProxy() {
    Assert.notNull(fetchPageSize);

    return getIpProxy(fetchPageSize);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for base url.
   *
   * @param  baseUrl  String
   */
  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for fetch page size.
   *
   * @param  fetchPageSize  Integer
   */
  public void setFetchPageSize(Integer fetchPageSize) {
    this.fetchPageSize = fetchPageSize;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private static void setHeader(HttpGet httpGet) {
    if (httpGet != null) {
      httpGet.setHeader("Content-Type", "text/html; charset=utf-8");
      httpGet.setHeader("Accept", "text/html");
      httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
      httpGet.setHeader("Host", "www.xicidaili.com");
      httpGet.setHeader("Origin", "www.xicidaili.com");
      httpGet.setHeader("Referer", "www.xicidaili.com");
      httpGet.setHeader("User-Agent",
        "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19");
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String fetchHtml(String url, String charset) throws IOException {
    String  html    = null;
    HttpGet httpGet = new HttpGet(url);
    setHeader(httpGet);

    CloseableHttpResponse response = httpClient.execute(httpGet);

    if (log.isDebugEnabled()) {
      log.debug("Status:" + response.getStatusLine().getStatusCode());
    }

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      html = EntityUtils.toString(response.getEntity(), charset);
    }

    httpGet.releaseConnection();

    return html;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * fetchIpProxy.
   *
   * @param   url  String
   *
   * @return  List
   */
  private List<ProxyIPInfo> fetchIpProxy(String url) {
    List<ProxyIPInfo> proxyIPInfoList = new ArrayList<>();

    if (log.isDebugEnabled()) {
      log.debug("Process fetch ip proxy from visit url: " + url);
    }

    try {
      String   html     = fetchHtml(url, CHARSET_UTF_8);
      Document document = Jsoup.parse(html);
      Elements elements = document.select("table>tbody>tr");

      if (log.isDebugEnabled()) {
        log.debug("Found " + elements.size());
      }

      for (Element element : elements) {
        int      columnIdx = 0;
        Elements columns   = element.select("td");

        if ((columns != null) && (columns.size() > 0)) {
          ProxyIPInfo proxyIPInfo = new ProxyIPInfo();

          for (Element column : columns) {
            String text = column.text();

            switch (columnIdx) {
              case IP_COLUMN: {
                proxyIPInfo.setIp(text);

                break;
              }

              case PORT_COLUMN: {
                proxyIPInfo.setPort(text);

                break;
              }

              case PROVINCE_COLUMN: {
                proxyIPInfo.setProvince(text);

                break;
              }

              case ANONYMOUS_TYPE_COLUMN: {
                proxyIPInfo.setAnonymousType(text);

                break;
              }

              case TYPE_COLUMN: {
                proxyIPInfo.setType(text);

                break;
              }

              case DELAY_COLUMN: {
                String delay = column.select("div").attr("title");
                proxyIPInfo.setDelay(delay);

                break;
              }
            } // end switch

            columnIdx++;
          } // end for

          proxyIPInfoList.add(proxyIPInfo);
        } // end if
      }   // end for
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }     // end try-catch

    return proxyIPInfoList;
  } // end method fetchIpProxy

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for ip proxy.
   *
   * @param   fetchPageSize  int
   *
   * @return  List
   */
  protected Set<ProxyIPInfo> getIpProxy(int fetchPageSize) {
    Set<ProxyIPInfo> ipProxies = new LinkedHashSet<>();
    fetchPageSize = (fetchPageSize > 0) ? fetchPageSize : 1;

    if (log.isDebugEnabled()) {
      log.debug("Will fetch IP from total page: " + fetchPageSize);
    }

    for (int i = 1; i <= fetchPageSize; i++) {
      String url = baseUrl + i;
      ipProxies.addAll(fetchIpProxy(url));
    }

    return ipProxies;
  }
} // end class IPProxyCrawler
