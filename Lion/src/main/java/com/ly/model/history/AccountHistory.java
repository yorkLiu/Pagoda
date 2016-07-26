package com.ly.model.history;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ly.model.Account;
import com.ly.model.ItemInfo;
import com.ly.model.OrderInfo;
import com.ly.model.base.BaseObject;
import com.ly.model.base.CreatorObject;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/21/2016 11:07
 */

@Entity
@Table(name = "AccountHistory")
public class AccountHistory extends BaseObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @JoinColumn(
    name       = "accountId",
    insertable = true,
    updatable  = true,
    nullable   = false
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private Account account;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  @JoinColumn(
    name       = "itemInfoId",
    nullable   = false,
    insertable = true,
    updatable  = true
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private ItemInfo itemInfo;

  @JoinColumn(name = "orderInfoId")
  @ManyToOne(fetch = FetchType.LAZY)
  private OrderInfo orderInfo;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for account.
   *
   * @return  Account
   */
  public Account getAccount() {
    return account;
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
   * getter method for item info.
   *
   * @return  ItemInfo
   */
  public ItemInfo getItemInfo() {
    return itemInfo;
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
   * setter method for account.
   *
   * @param  account  Account
   */
  public void setAccount(Account account) {
    this.account = account;
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
   * setter method for item info.
   *
   * @param  itemInfo  ItemInfo
   */
  public void setItemInfo(ItemInfo itemInfo) {
    this.itemInfo = itemInfo;
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
} // end class AccountHistory
