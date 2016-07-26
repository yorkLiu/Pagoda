package com.ly.model.audit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ly.model.OrderInfo;
import com.ly.model.base.AbstractOrderInfo;


/**
 * Created by yongliu on 7/23/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/23/2016 15:45
 */
@Entity
@Table(name = "OrderInfoAudit")
public class OrderInfoAudit extends AbstractOrderInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id private Long id;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new OrderInfoAudit object.
   */
  public OrderInfoAudit() { }

  /**
   * Creates a new OrderInfoAudit object.
   *
   * @param  orderInfo  OrderInfo
   */
  public OrderInfoAudit(OrderInfo orderInfo) {
    this.setAddress(orderInfo.getAddress());
    this.setCommission(orderInfo.getCommission());
    this.setMerchant(orderInfo.getMerchant());
    this.setOrderNum(orderInfo.getOrderNum());
    this.setPaymentMethod(orderInfo.getPaymentMethod());
    this.setPaymentType(orderInfo.getPaymentType());
    this.setPaymentUrl(orderInfo.getPaymentUrl());
    this.setStatus(orderInfo.getStatus());
    this.setTotalAmount(orderInfo.getTotalAmount());


  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

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
   * setter method for id.
   *
   * @param  id  Long
   */
  public void setId(Long id) {
    this.id = id;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.model.base.BaseObject#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("OrderInfoAudit{");
    sb.append("orderNum:").append(orderNum).append(", status:").append(status);
    sb.append('}');

    return sb.toString();
  }


} // end class OrderInfoAudit
