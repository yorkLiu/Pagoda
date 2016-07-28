package com.ly.service.impl;

import com.ly.dao.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.service.AddressService;


/**
 * Created by yongliu on 7/27/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/27/2016 10:46
 */
@Service("addressService")
public class AddressServiceImpl extends BaseServiceImpl implements AddressService { 
  
  @Autowired
  private AddressDao addressDao;
  
}
