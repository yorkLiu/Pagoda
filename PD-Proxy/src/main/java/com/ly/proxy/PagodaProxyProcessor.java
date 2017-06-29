package com.ly.proxy;

import com.ly.proxy.utils.ProxyUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Created by yongliu on 6/27/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/27/2017 11:26
 */
public class PagodaProxyProcessor extends AbstractProxyProcessor implements InitializingBean {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Logger logger = Logger.getLogger(getClass());
  
  private RestTemplate restTemplate;
  

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * @see  org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    if(restTemplate == null){
      restTemplate = new RestTemplate();
    }
    logger.info("ipProxyServiceUrl:" + ipProxyServiceUrl);
    logger.info("ipProxyRetryServiceUrl:" + ipProxyRetryServiceUrl);
    logger.info("verifyPassedText:" + verifyPassedText);
    logger.info("verifyUrl:" + verifyUrl);
    logger.info("allowGetIpAddress:" + allowGetIpAddress);
    Assert.notNull(ipProxyServiceUrl);
    Assert.notNull(ipProxyRetryServiceUrl);
    Assert.notNull(restTemplate);
//    Assert.notNull(verifyPassedText);
//    Assert.notNull(verifyUrl);
    
  }

  public static void main(String[] args) {
    PagodaProxyProcessor pagodaProxyProcessor = new PagodaProxyProcessor();
    String apiUrl = "http://localhost:8901/ip?tid=test1234&num=1&operator=2&filter=on";
    pagodaProxyProcessor.setIpProxyServiceUrl(apiUrl);
    pagodaProxyProcessor.setIpProxyRetryServiceUrl(apiUrl);
    pagodaProxyProcessor.setVerifyUrl("https://passport.yhd.com/passport/login_input.do");
    pagodaProxyProcessor.setVerifyPassedText("1号店");
    pagodaProxyProcessor.setAllowVerifyIpAddress(true);
    pagodaProxyProcessor.getIpProxy("上海");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.proxy.AbstractProxyProcessor#getIpProxy(java.lang.String)
   */
  @Override public String getIpProxy(String province) {
    try {
      logger.info("Ready to found IP Proxy for province: " + province);

      String serviceUrl = null;

      if ((null != province) && StringUtils.hasText(province)) {
        String areaParam = "&area=" + new String(province.getBytes("UTF-8"), "UTF-8");
        serviceUrl = this.ipProxyServiceUrl + areaParam;
      }

      return getIpFromProxyServer(serviceUrl);

    }catch (Exception e){
      logger.error(e.getMessage(), e);
    }
    
    
    return null;
  }
  
  public String getIpFromProxyServer(String ipProxyServerUrl){
    String  ipProxy = null;
    Boolean founded = Boolean.FALSE;
    logger.info("Visit IP Proxy Service URL: " + ipProxyServerUrl);
    String responseBody = getRestTemplate().getForEntity(ipProxyServerUrl, String.class).getBody();
    
    if(responseBody != null && !responseBody.contains("NONE")){
      JSONArray jsonArray = JSONArray.fromObject(responseBody);
      logger.info("Found " + jsonArray.size() + " IP Proxy.");
      int idx = 0;
      for (Object item : jsonArray) {
        idx ++;
        JSONObject json = JSONObject.fromObject(item);
        ipProxy =json.get("ip").toString();
        String area =json.get("area").toString();
        logger.info("IP:" + ipProxy + ", area: " + area);
        logger.info("this.getAllowVerifyIpAddress():" + this.getAllowVerifyIpAddress());
        
        if(this.getAllowVerifyIpAddress()){
          try{
            Boolean flag = ProxyUtils.verifyIpProxyByHttpClient(ipProxy, this.verifyUrl, this.verifyPassedText);
            logger.info("Using Proxy[" + ipProxy + "] visit ["+this.verifyUrl+"] return " + flag);
            if(flag){
              founded = Boolean.TRUE;
              return ipProxy;
            } else {
              if(idx >= jsonArray.size()){
                logger.info("The IP Proxy could not visit the url ["+this.verifyUrl+"], retry find a new ip proxy.");
                return getIpFromProxyServer(ipProxyServerUrl);
              }
            }
          }catch (Exception e){
            founded = Boolean.FALSE;
            logger.error(e.getMessage());
          }
        } else {
          founded = Boolean.TRUE;
        }
      }
    } else {
      // for @ipProxyServerUrl not found the ip proxy.
      return getIpFromProxyServer(this.ipProxyRetryServiceUrl);
    }
    
    if(!founded){
      return getIpFromProxyServer(ipProxyServerUrl);
    }
    return ipProxy;
  }

  public RestTemplate getRestTemplate() {
    if(null == restTemplate){
      restTemplate = new RestTemplate();
    }
    return restTemplate;
  }
}
