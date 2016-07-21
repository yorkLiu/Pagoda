package com.ly.model.history;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ly.model.Address;
import com.ly.model.Merchant;
import com.ly.model.OrderInfo;
import com.ly.model.base.BaseObject;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/21/2016 11:44
 */
@Entity
@Table(name = "AddressHistory")
public class AddressHistory extends BaseObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @JoinColumn(
    name     = "addressid",
    nullable = false
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private Address address;

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id private Long id;

  @JoinColumn(
    name     = "merchantId",
    nullable = false
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private Merchant merchant;

  @JoinColumn(name = "orderInfoId")
  @ManyToOne(fetch = FetchType.LAZY)
  private OrderInfo orderInfo;

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * getter method for address.
   *
   * @return  Address
   */
  public Address getAddress() {
    return address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * getter method for id.
   *
   * @return  Long
   */
  public Long getId() {
    return id;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for merchant.
   *
   * @return  Merchant
   */
  public Merchant getMerchant() {
    return merchant;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for order info.
   *
   * @return  OrderInfo
   */
  public OrderInfo getOrderInfo() {
    return orderInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for address.
   *
   * @param  address  Address
   */
  public void setAddress(Address address) {
    this.address = address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for id.
   *
   * @param  id  Long
   */
  public void setId(Long id) {
    this.id = id;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for merchant.
   *
   * @param  merchant  Merchant
   */
  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order info.
   *
   * @param  orderInfo  OrderInfo
   */
  public void setOrderInfo(OrderInfo orderInfo) {
    this.orderInfo = orderInfo;
  }
} // end class AddressHistory
