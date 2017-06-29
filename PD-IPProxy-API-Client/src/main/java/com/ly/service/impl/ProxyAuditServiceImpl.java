package com.ly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.ly.repository.ProxyAuditRepository;

import com.ly.service.ProxyAuditService;


/**
 * Created by yongliu on 6/26/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/26/2017 14:06
 */
@Service @Transactional public class ProxyAuditServiceImpl implements ProxyAuditService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private ProxyAuditRepository proxyAuditRepository;
}
