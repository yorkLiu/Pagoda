package com.ly.service;

import com.ly.domains.Proxy;

import java.util.List;

/**
 * Created by yongliu on 6/26/17.
 */
public interface ProxyService {
  
  public List<Proxy> getIp(String tokenId, Integer num, Integer types, String area, Boolean isFilterOut);
}
