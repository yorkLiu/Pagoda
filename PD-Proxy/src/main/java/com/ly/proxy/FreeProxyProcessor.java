package com.ly.proxy;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ly.proxy.exceptions.InvalidIpProxyException;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ly.proxy.pojo.ProtocolType;
import com.ly.proxy.pojo.ProxyIPInfo;
import com.ly.proxy.utils.ProxyUtils;


/**
 * Created by yongliu on 12/30/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/30/2016 14:10
 */
public class FreeProxyProcessor extends IPProxyCrawler implements InitializingBean {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Logger logger = Logger.getLogger(FreeProxyProcessor.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** HTTP or HTTPS if protocol is null or empty, default is HTTP. */
  protected String protocol;

  /** DOCUMENT ME! */
  protected String verifyPassedText;

  /** DOCUMENT ME! */
  protected String verifyUrl;

  private Set<ProxyIPInfo> ipProxyPool = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * main.
   *
   * @param  args  String[]
   */
  public static void main(String[] args) {
    FreeProxyProcessor freeProxyProcessor = new FreeProxyProcessor();
    freeProxyProcessor.setFetchPageSize(5);
    freeProxyProcessor.setVerifyUrl("http://www.yhd.com");
    freeProxyProcessor.setVerifyPassedText("1号店");
    freeProxyProcessor.setBaseUrl("http://www.xicidaili.com/nn/");

    freeProxyProcessor.getIpProxy("四川");
    
    while (true){
      
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.proxy.IPProxyCrawler#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();

    // ----------------------init base url start---------------------------------
    if (protocol != null) {
      if (ProtocolType.HTTP.toString().equalsIgnoreCase(protocol)) {
        setBaseUrl(HTTP_PROTOCOL_BASE_URL);
      } else if (ProtocolType.HTTPS.toString().equalsIgnoreCase(protocol)) {
        setBaseUrl(HTTPS_PROTOCOL_BASE_URL);
      } else {
        setBaseUrl(ANONYMOUS_BASE_URL);
      }
    } else {
      setBaseUrl(ANONYMOUS_BASE_URL);
    }
    // ----------------------init base url end-----------------------------------

    Assert.notNull(baseUrl);
  }

  //~ ------------------------------------------------------------------------------------------------------------------
  
  private void fetchFirstPageIPProxyAutomatic(){
    Timer timer = new Timer();
    long delay = TimeUnit.SECONDS.toMillis(30);
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        logger.info("Fetch IP Proxy from automatic before - " + ipProxyPool.size());
        ipProxyPool.addAll(getIpProxy(1));
        sortProxyPool();
        logger.info("Fetch IP Proxy from automatic after - " + ipProxyPool.size());
      }
    }, delay, delay);
  }

  /**
   * getter method for ip proxy.
   *
   * @param   province  String
   *
   * @return  String
   */
  public String getIpProxy(String province) {
    if (ipProxyPool == null) {
      ipProxyPool = getIpProxy();
      sortProxyPool();
      fetchFirstPageIPProxyAutomatic();
    }

    searchByProvince(province);


    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for protocol.
   *
   * @param  protocol  String
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
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

  private ProxyIPInfo getValidIPProxy(Set<ProxyIPInfo> proxies) {
    ProxyIPInfo validProxy = null;

    if ((proxies != null) && (proxies.size() > 0)) {
      for (ProxyIPInfo proxyIPInfo : proxies) {
        String ipProxy = proxyIPInfo.getFullIpAddress();

        try {
          logger.info("Ready valid IP[" + ipProxy + "] " + proxyIPInfo.getProvince());

//          Boolean isValid = ProxyUtils.verifyIpProxy(ipProxy, this.verifyUrl, this.verifyPassedText);
          Boolean isValid = ProxyUtils.verifyIpProxyByHttpClient(ipProxy, this.verifyUrl, this.verifyPassedText);

          if (isValid) {
            logger.info("IP[" + ipProxy + "] " + proxyIPInfo.getProvince() + " - " + proxyIPInfo.getDelay() + "s");
            proxyIPInfo.setHasUsed(Boolean.TRUE);
            validProxy = proxyIPInfo;

            break;
          }
        } catch (Exception e) {
          // mark this proxy is invalid
          proxyIPInfo.setInvalid(Boolean.TRUE);
          logger.error("IP[" + ipProxy + "] was invalid. " + e.getMessage());
        }
      }
    }

    return validProxy;
  } // end method getValidIPProxy
  

  //~ ------------------------------------------------------------------------------------------------------------------

  private String searchByProvince(String province) {
    Set<ProxyIPInfo> proxyIPInfoSet = null;
    ProxyIPInfo       proxyIPInfo     = null;
    String            ipProxy         = null;


    if ((province != null) && StringUtils.hasText(province)
          && (ipProxyPool != null) && (ipProxyPool.size() > 0)) {
      proxyIPInfoSet = ipProxyPool.stream().filter(p -> !p.getInvalid() && p.getProvince().contains(province)).collect(Collectors
          .toSet());
    }

    if ((proxyIPInfoSet != null) && (proxyIPInfoSet.size() > 0)) {
      proxyIPInfo = getValidIPProxy(proxyIPInfoSet);
    }

    if (proxyIPInfo == null) {
      // not found province or province is null or empty
      proxyIPInfo = getValidIPProxy(ipProxyPool);
    }

    if (proxyIPInfo != null) {
      ipProxy = proxyIPInfo.getFullIpAddress();
    }

    return ipProxy;
  } // end method searchByProvince

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Sort the @ipProxyPool list.
   */
  private void sortProxyPool() {
    if (ipProxyPool != null) {
      ipProxyPool = ipProxyPool.stream().sorted((p1, p2) -> p1.getSpeed().compareTo(p2.getSpeed())).collect(Collectors
          .toSet());
    }
  }
  
  private void removeIPProxyFromPool(ProxyIPInfo proxyIPInfo){
    if(ipProxyPool != null && ipProxyPool.size() > 0 && proxyIPInfo != null){
      ipProxyPool.remove(proxyIPInfo);
    }
  }
} // end class FreeProxyProcessor
