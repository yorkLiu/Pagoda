package com.ly.proxy.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by yongliu on 6/27/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagodaIPInfo {
  
  private String ip;
  
  private String area;


  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }
}
