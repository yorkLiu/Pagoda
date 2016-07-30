package com.ly.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import com.ly.model.Address;

import com.ly.service.AddressService;

import com.ly.web.command.DWRJsonListResponseCommand;
import com.ly.web.command.configuration.AccountCommand;
import com.ly.web.command.configuration.AddressCommand;


/**
 * Created by yongliu on 7/27/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/27/2016 16:38
 */
@Controller
@RemoteProxy(name = "addressController")
public class AddressController extends BaseController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private AddressService addressService;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * readAddresses.
   *
   * @param   params   Map
   * @param   request  HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<AddressCommand> readAddresses(Map<String, String> params,
    HttpServletRequest request) {
    boolean              success = Boolean.TRUE;
    String               message = null;
    int                  total   = 0;
    List<AddressCommand> results = new ArrayList<>();

    try {
      List<Address> addresses = addressService.getAll(Address.class);
      total = addresses.size();

      if (logger.isDebugEnabled()) {
        logger.debug("Found " + total + " Addresses to list.");
      }

      for (Address address : addresses) {
        results.add(new AddressCommand(address));
      }


    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRJsonListResponseCommand<AddressCommand>(results, success, message, total);


  } // end method readAddresses

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * updateAddress.
   *
   * @param   commands  List
   * @param   request   HttpServletRequest
   *
   * @return  DWRJsonListResponseCommand
   */
  @RemoteMethod public DWRJsonListResponseCommand<AddressCommand> updateAddress(List<AddressCommand> commands,
    HttpServletRequest request) {
    boolean success = Boolean.TRUE;
    String  message = null;

    try {
      for (AddressCommand command : commands) {
        Address account = command.populate();
        addressService.save(account);
      }
    } catch (Exception e) {
      success = Boolean.FALSE;
      logger.error(e.getMessage(), e);
    }

    return new DWRJsonListResponseCommand<AddressCommand>(commands, success, message);
  }

} // end class AddressController
