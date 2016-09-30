package com.ly.web.command.merchant;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.web.command.ParameterCommand;


/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 15:39
 */
@DataTransferObject(converter = ObjectConverter.class)
public class PreOrderParameterCommand extends ParameterCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  @RemoteProperty protected Long merchantId;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new PreOrderParameterCommand object.
   */
  public PreOrderParameterCommand() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for merchant id.
   *
   * @return  Long
   */
  public Long getMerchantId() {
    return merchantId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for merchant id.
   *
   * @param  merchantId  Long
   */
  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }
} // end class PreOrderParameterCommand
