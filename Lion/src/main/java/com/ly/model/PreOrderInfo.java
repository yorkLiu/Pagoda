package com.ly.model;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ly.model.base.CreatorObject;
import com.ly.model.type.OrderStatusType;


/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 14:55
 */
@Entity
@Table(
  name    = "PreOrderInfo",
  indexes = {
    @Index(
      name = "IDX_preOrderNo",
      columnList = "preOrderNo"
    )
  }
)
public class PreOrderInfo extends CreatorObject implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 2402711613672307247L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  @JoinColumn(
    name     = "appTypeId",
    nullable = false
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private AppType appType;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  @Cascade(value = CascadeType.ALL)
  @JoinTable(
    name               = "PreOrderItemInfo",
    joinColumns        = {
      @JoinColumn(
        name           = "preOrderInfoId",
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

  @JoinColumn(
    name     = "merchantId",
    nullable = false
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private Merchant merchant;


  /** 12 length UUID. */
  @Column(
    nullable = false,
    length   = 50
  )
  private String preOrderNo;

  private Integer priority;

  @Enumerated(value = EnumType.STRING)
  private OrderStatusType status;

  @Column(nullable = false)
  private Integer totalCount;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * addItemInfo.
   *
   * @param  itemInfo  ItemInfo
   */
  public void addItemInfo(ItemInfo itemInfo) {
    if (itemInfo != null) {
      items.add(itemInfo);
    }
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

    PreOrderInfo that = (PreOrderInfo) o;

    if ((preOrderNo != null) ? (!preOrderNo.equals(that.preOrderNo)) : (that.preOrderNo != null)) {
      return false;
    }

    if ((status != null) ? (!status.equals(that.status)) : (that.status != null)) {
      return false;
    }

    if ((totalCount != null) ? (!totalCount.equals(that.totalCount)) : (that.totalCount != null)) {
      return false;
    }

    if ((appType != null) ? (!appType.equals(that.appType)) : (that.appType != null)) {
      return false;
    }

    return !((appType != null) ? (!items.equals(that.items)) : (that.items != null));

  } // end method equals

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
  public Merchant getMerchant() {
    return merchant;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for pre order no.
   *
   * @return  String
   */
  public String getPreOrderNo() {
    return preOrderNo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for priority.
   *
   * @return  Integer
   */
  public Integer getPriority() {
    return priority;
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
   * getter method for total count.
   *
   * @return  Integer
   */
  public Integer getTotalCount() {
    return totalCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (preOrderNo != null) ? preOrderNo.hashCode() : 0;
    result = (31 * result) + ((totalCount != null) ? totalCount.hashCode() : 0);
    result = (31 * result) + ((status != null) ? status.hashCode() : 0);
    result = (31 * result) + ((appType != null) ? appType.hashCode() : 0);
    result = (31 * result) + ((items != null) ? items.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * removeItemInfo.
   *
   * @param  itemInfo  ItemInfo
   */
  public void removeItemInfo(ItemInfo itemInfo) {
    if (itemInfo != null) {
      items.remove(itemInfo);
    }
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
  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for pre order no.
   *
   * @param  preOrderNo  String
   */
  public void setPreOrderNo(String preOrderNo) {
    this.preOrderNo = preOrderNo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for priority.
   *
   * @param  priority  Integer
   */
  public void setPriority(Integer priority) {
    this.priority = priority;
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
   * setter method for total count.
   *
   * @param  totalCount  Integer
   */
  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.model.base.BaseObject#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("PreOrderInfo{");
    sb.append("appType=").append(appType);
    sb.append(", preOrderNo='").append(preOrderNo).append('\'');
    sb.append(", status=").append(status);
    sb.append(", priority=").append(priority);
    sb.append(", totalCount=").append(totalCount);
    sb.append('}');

    return sb.toString();
  }
} // end class PreOrderInfo
