package com.ly.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.ly.dao.AddressDao;


/**
 * Created by yongliu on 7/27/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/27/2016 10:45
 */
@Repository("addressDao")
public class AddressDaoHibernate extends BaseDaoHibernate implements AddressDao { }
