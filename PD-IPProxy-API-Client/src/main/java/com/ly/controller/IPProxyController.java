package com.ly.controller;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ly.domains.Proxy;

import com.ly.service.ProxyService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * Created by yongliu on 6/26/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/26/2017 15:17
 */
@RestController public class IPProxyController {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String NO_RESULT = "NONE";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Logger logger = Logger.getLogger(IPProxyController.class);

  @Autowired private ProxyService proxyService;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * String.
   *
   * @param   tid       tokenId
   * @param   num       how many ip(s) you want to get once time, default is "5"
   * @param   operator  0: 透明, 1: 匿名, 2: 高匿, default is "0"
   * @param   area      省份名
   * @param   filter    on: the IP will only return once time for tokenId, off: not store the loaded ip for tokenId
   *
   * @return  Object (JSONArray or String).
   */
  @GetMapping("/ip")
  public Object getIp(@RequestParam(required = true) String tid,
    @RequestParam(required = false) Integer num,
    @RequestParam(required = false) Integer operator,
    @RequestParam(required = false) String area,
    @RequestParam(required = false) String filter) {
    
    num      = getDefaultValue(num, 5);
    operator = getDefaultValue(operator, 0);
    area     = getDefaultValue(area, null);

    Boolean isFilterOut = getFiltered(filter);

    logger.info("tokenID:" + tid + " getNumber:" + num + " Operator:" + operator + " Area:" + area + ", isFilterOut: "
      + isFilterOut);

    List<Proxy> results    = proxyService.getIp(tid, num, operator, area, isFilterOut);
    int         resultSize = ((results != null) && !results.isEmpty()) ? results.size() : 0;

    logger.info("Found " + resultSize + " IP Proxy for tokenID: " + tid + ", area: " + area);

    if ((results == null) || results.isEmpty()) {
      return NO_RESULT;
    }

    JSONArray array = new JSONArray();

    if ((results != null) && (resultSize > 0)) {
      for (Proxy result : results) {
        array.element(new JSONObject().element("ip", result.getIp() + ":" + result.getPort()).element("area",
            result.getArea()));
      }
    }

    return array;
  } // end method getIp

  //~ ------------------------------------------------------------------------------------------------------------------

  private Integer getDefaultValue(Integer intValue, Integer defaultValue) {
    return (intValue != null) ? intValue : defaultValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String getDefaultValue(String strValue, String defaultValue) {
    return ((strValue != null) && StringUtils.hasText(strValue)) ? strValue : defaultValue;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private Boolean getFiltered(String filter) {
    if ((filter != null) && StringUtils.hasText(filter) && filter.equalsIgnoreCase("on")) {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

} // end class IPProxyController
