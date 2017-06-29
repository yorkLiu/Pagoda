package com.ly.repository;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.ly.domains.ProxyAudit;


/**
 * Created by yongliu on 6/26/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/26/2017 14:03
 */
@Repository public interface ProxyAuditRepository extends CrudRepository<ProxyAudit, Long> { }
