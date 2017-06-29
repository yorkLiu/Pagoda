package com.ly.service.impl;

import com.ly.domains.Proxy;
import com.ly.domains.ProxyAudit;
import com.ly.repository.ProxyAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.ly.repository.ProxyRepository;

import com.ly.service.ProxyService;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by yongliu on 6/26/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/26/2017 14:05
 */
@Service @Transactional public class ProxyServiceImpl implements ProxyService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private ProxyRepository proxyRepository;
  @Autowired private ProxyAuditRepository proxyAuditRepository;

  @Override
  public List<Proxy> getIp(String tokenId, Integer num, Integer types, String area,  Boolean isFilterOut) {
    types = types != null && types.intValue() > -1 ? types: null;
    num = num != null && num.intValue() > 0 ? num : 5;
    PageRequest pagging = new PageRequest(1, num);
    List<Proxy> results = null;
    
    if(types != null && area != null && StringUtils.hasText(area)){
      results = proxyRepository.getIp(tokenId, types, area, pagging);
    } else if(StringUtils.hasText(area) && types == null) {
      results = proxyRepository.getIp(tokenId, area, pagging);
    } else if(types == null && !StringUtils.hasText(area)){
      results =  proxyRepository.getIp(tokenId, pagging);
    }
    
    if(isFilterOut && results != null && !results.isEmpty()){
      Set<ProxyAudit> auditSet = new HashSet<>();
      for (Proxy proxy : results) {
        ProxyAudit proxyAudit = new ProxyAudit();
        proxyAudit.setProxyId(proxy.getId());
        proxyAudit.setTokenId(tokenId);
        proxyAudit.setArea(proxy.getArea());
        proxyAudit.setIp(proxy.getIp());
        proxyAudit.setPort(proxy.getPort());
        proxyAudit.setProtocol(proxy.getProtocol());
        proxyAudit.setTypes(proxy.getTypes());
        proxyAudit.setCreateDate(new Date());
        auditSet.add((proxyAudit));
      }
  
      if(!auditSet.isEmpty()){
        proxyAuditRepository.save(auditSet);
      }
    }

    return results; 
  }
  
}
