package com.ly.model.base;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.ly.model.type.OrderStatusType;
import org.hibernate.annotations.Type;

import com.ly.model.Address;
import com.ly.model.AppType;
import com.ly.model.Merchant;
import com.ly.model.type.PaymentType;


/**
 * Created by yongliu on 7/23/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/23/2016 15:45
 */
@MappedSuperclass public abstract class AbstractOrderInfo extends CreatorObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  @JoinColumn(
    name       = "addressId",
    insertable = true,
    updatable  = true
  )
  @ManyToOne(fetch = FetchType.LAZY)
  protected Address address;

  /** How much amount for this order. */
  @Column(
    precision = 19,
    scale     = 2
  )
  protected BigDecimal commission;

  /** TODO: DOCUMENT ME! */
  @JoinColumn(
    name     = "merchantId",
    nullable = false
  )
  @ManyToOne protected Merchant merchant;

  /** TODO: DOCUMENT ME! */
  protected String orderNum;

  /** TODO: DOCUMENT ME! */
  protected String paymentMethod;

  /** TODO: DOCUMENT ME! */
  @Enumerated(EnumType.STRING)
  protected PaymentType paymentType;

  /** TODO: DOCUMENT ME! */
  protected String paymentUrl;

  /** TODO: DOCUMENT ME! */
  @Type(type = "yes_no")
  protected Boolean readyForComment;

  /** TODO: DOCUMENT ME! */
  @Enumerated(EnumType.STRING)
  protected OrderStatusType status;

  /** This order total amount. */
  @Column(
    precision = 19,
    scale     = 2
  )
  protected BigDecimal totalAmount;


  @JoinColumn(name = "appTypeId")
  @ManyToOne(fetch = FetchType.LAZY)
  private AppType appType;
  
  private Integer priority;
  
  /**关注商品*/
  private Boolean attentionGoods;

  /**关注店铺*/
  private Boolean attentionMerchant;

  //~ Methods ----------------------------------------------------------------------------------------------------------

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

    AbstractOrderInfo that = (AbstractOrderInfo) o;

    if ((address != null) ? (!address.equals(that.address)) : (that.address != null)) {
      return false;
    }

    if ((commission != null) ? (!commission.equals(that.commission)) : (that.commission != null)) {
      return false;
    }

    if ((merchant != null) ? (!merchant.equals(that.merchant)) : (that.merchant != null)) {
      return false;
    }

    if ((orderNum != null) ? (!orderNum.equals(that.orderNum)) : (that.orderNum != null)) {
      return false;
    }

    if ((paymentMethod != null) ? (!paymentMethod.equals(that.paymentMethod)) : (that.paymentMethod != null)) {
      return false;
    }

    if (paymentType != that.paymentType) {
      return false;
    }

    if ((paymentUrl != null) ? (!paymentUrl.equals(that.paymentUrl)) : (that.paymentUrl != null)) {
      return false;
    }

    if ((appType != null) ? (!appType.equals(that.appType)) : (that.appType != null)) {
      return false;
    }
    if ((priority != null) ? (!priority.equals(that.priority)) : (that.priority != null)) {
      return false;
    }

    if (status != that.status) {
      return false;
    }

    return !((totalAmount != null) ? (!totalAmount.equals(that.totalAmount)) : (that.totalAmount != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * getter method for app type.
   *
   * @return  AppType
   */
  public AppType getAppType() {
    return appType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission.
   *
   * @return  BigDecimal
   */
  public BigDecimal getCommission() {
    return commission;
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
   * getter method for order num.
   *
   * @return  String
   */
  public String getOrderNum() {
    return orderNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for payment method.
   *
   * @return  String
   */
  public String getPaymentMethod() {
    return paymentMethod;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for payment type.
   *
   * @return  PaymentType
   */
  public PaymentType getPaymentType() {
    return paymentType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for payment url.
   *
   * @return  String
   */
  public String getPaymentUrl() {
    return paymentUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for ready for comment.
   *
   * @return  Boolean
   */
  public Boolean getReadyForComment() {
    if (null == readyForComment) {
      return Boolean.FALSE;
    }

    return readyForComment;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for status.
   *
   * @return  OrderStatusType
   */
  public OrderStatusType getStatus() {
    return status;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for total amount.
   *
   * @return  BigDecimal
   */
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (address != null) ? address.hashCode() : 0;
    result = (31 * result) + ((commission != null) ? commission.hashCode() : 0);
    result = (31 * result) + ((merchant != null) ? merchant.hashCode() : 0);
    result = (31 * result) + ((orderNum != null) ? orderNum.hashCode() : 0);
    result = (31 * result) + ((paymentMethod != null) ? paymentMethod.hashCode() : 0);
    result = (31 * result) + ((paymentType != null) ? paymentType.hashCode() : 0);
    result = (31 * result) + ((paymentUrl != null) ? paymentUrl.hashCode() : 0);
    result = (31 * result) + ((status != null) ? status.hashCode() : 0);
    result = (31 * result) + ((totalAmount != null) ? totalAmount.hashCode() : 0);
    result = (31 * result) + ((appType != null) ? appType.hashCode() : 0);
    result = (31 * result) + ((priority != null) ? priority.hashCode() : 0);

    return result;
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
   * setter method for app type.
   *
   * @param  appType  AppType
   */
  public void setAppType(AppType appType) {
    this.appType = appType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission.
   *
   * @param  commission  BigDecimal
   */
  public void setCommission(BigDecimal commission) {
    this.commission = commission;
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
   * setter method for order num.
   *
   * @param  orderNum  String
   */
  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for payment method.
   *
   * @param  paymentMethod  String
   */
  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for payment type.
   *
   * @param  paymentType  PaymentType
   */
  public void setPaymentType(PaymentType paymentType) {
    this.paymentType = paymentType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for payment url.
   *
   * @param  paymentUrl  String
   */
  public void setPaymentUrl(String paymentUrl) {
    this.paymentUrl = paymentUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for ready for comment.
   *
   * @param  readyForComment  Boolean
   */
  public void setReadyForComment(Boolean readyForComment) {
    this.readyForComment = readyForComment;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for status.
   *
   * @param  status  OrderStatusType
   */
  public void setStatus(OrderStatusType status) {
    this.status = status;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for total amount.
   *
   * @param  totalAmount  BigDecimal
   */
  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public Boolean getAttentionGoods() {
    if (attentionGoods == null) {
      return Boolean.FALSE;
    }
    return attentionGoods;
  }

  public void setAttentionGoods(Boolean attentionGoods) {
    this.attentionGoods = attentionGoods;
  }

  public Boolean getAttentionMerchant() {
    if(attentionMerchant == null){
      return Boolean.FALSE;
    }
    return attentionMerchant;
  }

  public void setAttentionMerchant(Boolean attentionMerchant) {
    this.attentionMerchant = attentionMerchant;
  }
} // end class AbstractOrderInfo
