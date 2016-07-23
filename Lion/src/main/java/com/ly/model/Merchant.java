package com.ly.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ly.model.type.AppType;
import com.ly.model.type.StatusType;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/20/2016 15:19
 */

@Entity
@Table(name = "Merchant")
public class Merchant {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /**首选地址*/
  private String addressOption;

  @Enumerated(EnumType.STRING)
  private AppType appType;

  /** comment total count, default is all @totalCount. */
  private Integer commentCount;

  /** how many days delay for comment after 'confirm receipt', must gretter or equals @confirmReceiptDelay. */
  private Integer commentDelay;

  /** how many days delay to confirm receipt. */
  private Integer confirmReceiptDelay;

  /** 是否是团购. */
  private Boolean groupBuy;

  /** 团购 主页面(这个商品所在的团购页面). */
  private String groupBuyIndexUrl;

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id private Long id;

  private String indexUrl;

  /** 每单间隔时间 (minute). */
  private Integer intervalForOrder = 3;

  private String merchantId;

  private String merchantName;

  private String name;

  /** is overseas 是否是海购. */
  private Boolean overseas;

  /** SD Date. */
  @Temporal(TemporalType.TIMESTAMP)
  private Date scheduleDate;

  /** SD 时间 HH:mm (i.e: 18:00, 10:00), 采用24小时制 */
  private String scheduleTime;

  /**
   * <pre>
       In Progress
       cancelled
       completed.
   * </pre>
   */
  @Enumerated(EnumType.STRING)
  private StatusType status;

  /** SD count. */
  private Integer totalCount;

  /** 每个商品浏览时间 (minute). */
  private Integer viewingTimeForOrder = 1;

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

    Merchant merchant = (Merchant) o;

    if ((indexUrl != null) ? (!indexUrl.equals(merchant.indexUrl)) : (merchant.indexUrl != null)) {
      return false;
    }

    if ((name != null) ? (!name.equals(merchant.name)) : (merchant.name != null)) {
      return false;
    }

    if ((scheduleDate != null) ? (!scheduleDate.equals(merchant.scheduleDate)) : (merchant.scheduleDate != null)) {
      return false;
    }

    if ((totalCount != null) ? (!totalCount.equals(merchant.totalCount)) : (merchant.totalCount != null)) {
      return false;
    }

    if ((appType != null) ? (!appType.equals(merchant.appType)) : (merchant.appType != null)) {
      return false;
    }

    if ((commentCount != null) ? (!commentCount.equals(merchant.commentCount)) : (merchant.commentCount != null)) {
      return false;
    }

    if ((confirmReceiptDelay != null) ? (!confirmReceiptDelay.equals(merchant.confirmReceiptDelay))
                                      : (merchant.confirmReceiptDelay != null)) {
      return false;
    }

    if ((commentDelay != null) ? (!commentDelay.equals(merchant.commentDelay)) : (merchant.commentDelay != null)) {
      return false;
    }

    return status == merchant.status;

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for address option.
   *
   * @return  String
   */
  public String getAddressOption() {
    return addressOption;
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
   * getter method for comment count.
   *
   * @return  Integer
   */
  public Integer getCommentCount() {
    return commentCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for comment delay.
   *
   * @return  Integer
   */
  public Integer getCommentDelay() {
    return commentDelay;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for confirm receipt delay.
   *
   * @return  Integer
   */
  public Integer getConfirmReceiptDelay() {
    return confirmReceiptDelay;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for group buy.
   *
   * @return  Boolean
   */
  public Boolean getGroupBuy() {
    return groupBuy;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for group buy index url.
   *
   * @return  String
   */
  public String getGroupBuyIndexUrl() {
    return groupBuyIndexUrl;
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
   * getter method for index url.
   *
   * @return  String
   */
  public String getIndexUrl() {
    return indexUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for interval for order.
   *
   * @return  Integer
   */
  public Integer getIntervalForOrder() {
    return intervalForOrder;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for merchant id.
   *
   * @return  String
   */
  public String getMerchantId() {
    return merchantId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for merchant name.
   *
   * @return  String
   */
  public String getMerchantName() {
    return merchantName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for name.
   *
   * @return  String
   */
  public String getName() {
    return name;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for overseas.
   *
   * @return  Boolean
   */
  public Boolean getOverseas() {
    if (overseas == null) {
      return Boolean.FALSE;
    }

    return overseas;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for schedule date.
   *
   * @return  Date
   */
  public Date getScheduleDate() {
    return scheduleDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for schedule time.
   *
   * @return  String
   */
  public String getScheduleTime() {
    return scheduleTime;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for status.
   *
   * @return  StatusType
   */
  public StatusType getStatus() {
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
   * getter method for viewing time for order.
   *
   * @return  Integer
   */
  public Integer getViewingTimeForOrder() {
    return viewingTimeForOrder;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (indexUrl != null) ? indexUrl.hashCode() : 0;
    result = (31 * result) + ((name != null) ? name.hashCode() : 0);
    result = (31 * result) + ((scheduleDate != null) ? scheduleDate.hashCode() : 0);
    result = (31 * result) + ((totalCount != null) ? totalCount.hashCode() : 0);
    result = (31 * result) + ((status != null) ? status.hashCode() : 0);
    result = (31 * result) + ((appType != null) ? appType.hashCode() : 0);
    result = (31 * result) + ((commentCount != null) ? commentCount.hashCode() : 0);
    result = (31 * result) + ((confirmReceiptDelay != null) ? confirmReceiptDelay.hashCode() : 0);
    result = (31 * result) + ((commentDelay != null) ? commentDelay.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for address option.
   *
   * @param  addressOption  String
   */
  public void setAddressOption(String addressOption) {
    this.addressOption = addressOption;
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
   * setter method for comment count.
   *
   * @param  commentCount  Integer
   */
  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for comment delay.
   *
   * @param  commentDelay  Integer
   */
  public void setCommentDelay(Integer commentDelay) {
    this.commentDelay = commentDelay;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for confirm receipt delay.
   *
   * @param  confirmReceiptDelay  Integer
   */
  public void setConfirmReceiptDelay(Integer confirmReceiptDelay) {
    this.confirmReceiptDelay = confirmReceiptDelay;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for group buy.
   *
   * @param  groupBuy  Boolean
   */
  public void setGroupBuy(Boolean groupBuy) {
    this.groupBuy = groupBuy;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for group buy index url.
   *
   * @param  groupBuyIndexUrl  String
   */
  public void setGroupBuyIndexUrl(String groupBuyIndexUrl) {
    this.groupBuyIndexUrl = groupBuyIndexUrl;
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
   * setter method for index url.
   *
   * @param  indexUrl  String
   */
  public void setIndexUrl(String indexUrl) {
    this.indexUrl = indexUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for interval for order.
   *
   * @param  intervalForOrder  Integer
   */
  public void setIntervalForOrder(Integer intervalForOrder) {
    this.intervalForOrder = intervalForOrder;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for merchant id.
   *
   * @param  merchantId  String
   */
  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for merchant name.
   *
   * @param  merchantName  String
   */
  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for name.
   *
   * @param  name  String
   */
  public void setName(String name) {
    this.name = name;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for overseas.
   *
   * @param  overseas  Boolean
   */
  public void setOverseas(Boolean overseas) {
    this.overseas = overseas;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for schedule date.
   *
   * @param  scheduleDate  Date
   */
  public void setScheduleDate(Date scheduleDate) {
    this.scheduleDate = scheduleDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for schedule time.
   *
   * @param  scheduleTime  String
   */
  public void setScheduleTime(String scheduleTime) {
    this.scheduleTime = scheduleTime;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for status.
   *
   * @param  status  StatusType
   */
  public void setStatus(StatusType status) {
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
   * setter method for viewing time for order.
   *
   * @param  viewingTimeForOrder  Integer
   */
  public void setViewingTimeForOrder(Integer viewingTimeForOrder) {
    this.viewingTimeForOrder = viewingTimeForOrder;
  }
} // end class Merchant
