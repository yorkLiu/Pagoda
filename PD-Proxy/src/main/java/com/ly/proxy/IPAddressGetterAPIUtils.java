package com.ly.proxy;

import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONObject;


/**
 * Created by yongliu on 12/2/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/02/2016 15:19
 */
public class IPAddressGetterAPIUtils {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static Logger logger = Logger.getLogger(IPAddressGetterAPIUtils.class);

  /** DOCUMENT ME! */
  public static final int STATUS_OK = 200;

  /** DOCUMENT ME! */
  public static final String IP_ADDRESS_SINA_URL =
    "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=%s";

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for ip address info.
   *
   * @param   ip  String
   *
   * @return  String
   */
  public static String getIpAddressInfo(String ip) {
    String ip2Addr = null;

    RestTemplate restTemplate = new RestTemplate();

    String url = String.format(IP_ADDRESS_SINA_URL, ip);

    ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

    if (result.getStatusCodeValue() == STATUS_OK) {
      JSONObject          json     = JSONObject.fromObject(result);
      Map<String, String> map      = (Map) json.get("body");
      String              country  = map.get("country");
      String              province = map.get("province");
      String              city     = map.get("city");

      ip2Addr = country + " " + province + " " + city;
      logger.info("IP:" + ip + ":" + ip2Addr);
    }

    return ip2Addr;
  }


} // end class IPAddressGetterAPIUtils
