package com.ly.model;

import java.math.BigDecimal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ly.model.base.AbstractOrderInfo;
import com.ly.model.type.PaymentType;
import com.ly.model.type.StatusType;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/21/2016 11:39
 */
@Entity
@Table(name = "OrderInfo")
public class OrderInfo extends AbstractOrderInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  @Cascade(value = CascadeType.ALL)
  @JoinTable(
    name               = "OrderItemInfo",
    joinColumns        = {
      @JoinColumn(
        name           = "orderInfoId",
        insertable     = true,
        nullable       = false
      )
    },
    inverseJoinColumns = {
      @JoinColumn(
        name           = "itemInfoId",
        insertable     = true,
        nullable       = false
      )
    }
  )
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<ItemInfo> items = new HashSet<>();

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * deepCopy.
   *
   * @param  that  OrderInfo
   */
  public void deepCopy(OrderInfo that) {
    that.setAddress(this.address);
    that.setCommission(this.commission);
    that.setMerchant(this.merchant);
    that.setOrderNum(this.orderNum);
    that.setPaymentMethod(this.paymentMethod);
    that.setPaymentType(this.paymentType);
    that.setPaymentUrl(this.paymentUrl);
    that.setStatus(this.status);
    that.setTotalAmount(this.getTotalAmount());
    that.setAppType(this.getAppType());
    that.setPriority(this.getPriority());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#equals(java.lang.Object)
   */
  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    OrderInfo orderInfo = (OrderInfo) o;

    if ((commission != null) ? (!commission.equals(orderInfo.commission)) : (orderInfo.commission != null)) {
      return false;
    }

    if ((merchant != null) ? (!merchant.equals(orderInfo.merchant)) : (orderInfo.merchant != null)) {
      return false;
    }

    if ((orderNum != null) ? (!orderNum.equals(orderInfo.orderNum)) : (orderInfo.orderNum != null)) {
      return false;
    }

    if ((paymentMethod != null) ? (!paymentMethod.equals(orderInfo.paymentMethod))
                                : (orderInfo.paymentMethod != null)) {
      return false;
    }

    if (paymentType != orderInfo.paymentType) {
      return false;
    }

    if ((paymentUrl != null) ? (!paymentUrl.equals(orderInfo.paymentUrl)) : (orderInfo.paymentUrl != null)) {
      return false;
    }

    if (status != orderInfo.status) {
      return false;
    }

    return !((totalAmount != null) ? (!totalAmount.equals(orderInfo.totalAmount)) : (orderInfo.totalAmount != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for address.
   *
   * @return  Address
   */
  @Override public Address getAddress() {
    return address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission.
   *
   * @return  BigDecimal
   */
  @Override public BigDecimal getCommission() {
    return commission;
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
   * getter method for items.
   *
   * @return  Set
   */
  public Set<ItemInfo> getItems() {
    return items;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for merchant.
   *
   * @return  Merchant
   */
  @Override public Merchant getMerchant() {
    return merchant;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for order num.
   *
   * @return  String
   */
  @Override public String getOrderNum() {
    return orderNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for payment method.
   *
   * @return  String
   */
  @Override public String getPaymentMethod() {
    return paymentMethod;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for payment type.
   *
   * @return  PaymentType
   */
  @Override public PaymentType getPaymentType() {
    return paymentType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for payment url.
   *
   * @return  String
   */
  @Override public String getPaymentUrl() {
    return paymentUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for status.
   *
   * @return  StatusType
   */
  @Override public StatusType getStatus() {
    return status;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for total amount.
   *
   * @return  BigDecimal
   */
  @Override public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (commission != null) ? commission.hashCode() : 0;
    result = (31 * result) + ((merchant != null) ? merchant.hashCode() : 0);
    result = (31 * result) + ((orderNum != null) ? orderNum.hashCode() : 0);
    result = (31 * result) + ((paymentMethod != null) ? paymentMethod.hashCode() : 0);
    result = (31 * result) + ((paymentType != null) ? paymentType.hashCode() : 0);
    result = (31 * result) + ((paymentUrl != null) ? paymentUrl.hashCode() : 0);
    result = (31 * result) + ((status != null) ? status.hashCode() : 0);
    result = (31 * result) + ((totalAmount != null) ? totalAmount.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for address.
   *
   * @param  address  Address
   */
  @Override public void setAddress(Address address) {
    this.address = address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission.
   *
   * @param  commission  BigDecimal
   */
  @Override public void setCommission(BigDecimal commission) {
    this.commission = commission;
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
   * setter method for items.
   *
   * @param  items  Set
   */
  public void setItems(Set<ItemInfo> items) {
    this.items = items;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for merchant.
   *
   * @param  merchant  Merchant
   */
  @Override public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order num.
   *
   * @param  orderNum  String
   */
  @Override public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for payment method.
   *
   * @param  paymentMethod  String
   */
  @Override public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for payment type.
   *
   * @param  paymentType  PaymentType
   */
  @Override public void setPaymentType(PaymentType paymentType) {
    this.paymentType = paymentType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for payment url.
   *
   * @param  paymentUrl  String
   */
  @Override public void setPaymentUrl(String paymentUrl) {
    this.paymentUrl = paymentUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for status.
   *
   * @param  status  StatusType
   */
  @Override public void setStatus(StatusType status) {
    this.status = status;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for total amount.
   *
   * @param  totalAmount  BigDecimal
   */
  @Override public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }
} // end class OrderInfo
