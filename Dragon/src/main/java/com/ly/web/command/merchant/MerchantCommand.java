package com.ly.web.command.merchant;

import java.util.Date;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.model.Merchant;
import com.ly.model.User;
import com.ly.model.type.OrderStatusType;

import com.ly.utils.DateUtil;

import com.ly.web.command.BaseCommand;


/**
 * Created by yongliu on 9/14/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/14/2016 16:31
 */
@DataTransferObject(converter = ObjectConverter.class)
public class MerchantCommand extends BaseCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @RemoteProperty private String addressOption;

  @RemoteProperty private Integer commentCount;

  @RemoteProperty private Integer commentDelay;

  @RemoteProperty private Integer confirmReceiptDelay;

  @RemoteProperty private Long copyFromId;

  @RemoteProperty private Boolean flashBuy;

  @RemoteProperty private String  flashBuyIndexUrl;
  @RemoteProperty private Boolean fromCopy;

  @RemoteProperty private Boolean groupBuy;

  @RemoteProperty private String groupBuyIndexUrl;

  @RemoteProperty private Long id;

  @RemoteProperty private String indexUrl;

  @RemoteProperty private Integer intervalForOrder;
  @RemoteProperty private String  merchantName;
  @RemoteProperty private String  merchantNo;
  @RemoteProperty private String  name;

  @RemoteProperty private Boolean overseas;

  @RemoteProperty private Boolean quickOrder;

  @RemoteProperty private Date scheduleDate;

  @RemoteProperty private Date scheduleDateTime;

  @RemoteProperty private String scheduleTime;

  @RemoteProperty private String status;

  @RemoteProperty private Integer totalCount;

  @RemoteProperty private Integer viewingTimeForOrder;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new MerchantCommand object.
   */
  public MerchantCommand() { }

  /**
   * Creates a new MerchantCommand object.
   *
   * @param  merchant  Merchant
   */
  public MerchantCommand(Merchant merchant) {
    this.id                  = merchant.getId();
    this.name                = merchant.getName();
    this.merchantNo          = merchant.getMerchantNo();
    this.merchantName        = merchant.getMerchantName();
    this.addressOption       = merchant.getAddressOption();
    this.totalCount          = merchant.getTotalCount();
    this.commentCount        = merchant.getCommentCount();
    this.commentDelay        = merchant.getCommentDelay();
    this.confirmReceiptDelay = merchant.getConfirmReceiptDelay();
    this.scheduleDate        = merchant.getScheduleDate();
    this.scheduleTime        = merchant.getScheduleTime();
    this.flashBuy            = merchant.getFlashBuy();
    this.flashBuyIndexUrl    = merchant.getFlashBuyIndexUrl();
    this.groupBuy            = merchant.getGroupBuy();
    this.groupBuyIndexUrl    = merchant.getGroupBuyIndexUrl();
    this.indexUrl            = merchant.getIndexUrl();
    this.intervalForOrder    = merchant.getIntervalForOrder();
    this.overseas            = merchant.getOverseas();
    this.quickOrder          = merchant.getQuickOrder();
    this.status              = merchant.getStatus().toString();
    this.viewingTimeForOrder = merchant.getViewingTimeForOrder();


    if ((this.scheduleDate != null) && (scheduleTime != null)) {
      scheduleDateTime = DateUtil.toDate(scheduleDate + " " + scheduleTime + ":00");
    }

    this.creatorName = (merchant.getCreator() != null) ? merchant.getCreator().getFullName() : null;
    this.createDate  = merchant.getCreateDate();

  } // end ctor MerchantCommand

  //~ Methods ----------------------------------------------------------------------------------------------------------

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
   * getter method for copy from id.
   *
   * @return  Long
   */
  public Long getCopyFromId() {
    return copyFromId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for flash buy.
   *
   * @return  Boolean
   */
  public Boolean getFlashBuy() {
    return flashBuy;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for flash buy index url.
   *
   * @return  String
   */
  public String getFlashBuyIndexUrl() {
    return flashBuyIndexUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for from copy.
   *
   * @return  Boolean
   */
  public Boolean getFromCopy() {
    if (null == fromCopy) {
      return Boolean.FALSE;
    }

    return fromCopy;
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
   * getter method for merchant name.
   *
   * @return  String
   */
  public String getMerchantName() {
    return merchantName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for merchant no.
   *
   * @return  String
   */
  public String getMerchantNo() {
    return merchantNo;
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
    return overseas;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for quick order.
   *
   * @return  Boolean
   */
  public Boolean getQuickOrder() {
    return quickOrder;
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
   * getter method for schedule date time.
   *
   * @return  Date
   */
  public Date getScheduleDateTime() {
    return scheduleDateTime;
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
   * @return  String
   */
  public String getStatus() {
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
   * populate.
   *
   * @param   user  User
   *
   * @return  Merchant
   */
  public Merchant populate(User user) {
    Merchant merchant = new Merchant();

    if ((getId() != null) && (getId().intValue() > 0)) {
      merchant.setId(this.getId());
      merchant.setLastUpdateDate(new Date());
      merchant.setLastUpdater(user);
      merchant.setStatus(OrderStatusType.convert(this.status));
    } else {
      merchant.setStatus(OrderStatusType.INIT);
      merchant.setCreator(user);
    }

    merchant.setName(this.getName());
    merchant.setMerchantNo(this.getMerchantNo());
    merchant.setMerchantName(this.getMerchantName());
    merchant.setAddressOption(this.getAddressOption());
    merchant.setTotalCount(this.getTotalCount());
    merchant.setCommentCount(this.getCommentCount());
    merchant.setCommentDelay(this.getCommentDelay());
    merchant.setConfirmReceiptDelay(this.getConfirmReceiptDelay());
    merchant.setScheduleDate(this.getScheduleDate());
    merchant.setScheduleTime(this.getScheduleTime());
    merchant.setFlashBuy(this.getFlashBuy());
    merchant.setFlashBuyIndexUrl(this.getFlashBuyIndexUrl());
    merchant.setGroupBuy(this.getGroupBuy());
    merchant.setGroupBuyIndexUrl(this.getGroupBuyIndexUrl());
    merchant.setIndexUrl(this.getIndexUrl());
    merchant.setIntervalForOrder(this.getIntervalForOrder());
    merchant.setOverseas(this.getOverseas());
    merchant.setQuickOrder(this.getQuickOrder());
    merchant.setViewingTimeForOrder(this.getViewingTimeForOrder());

    return merchant;
  } // end method populate

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
   * setter method for copy from id.
   *
   * @param  copyFromId  Long
   */
  public void setCopyFromId(Long copyFromId) {
    this.copyFromId = copyFromId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for flash buy.
   *
   * @param  flashBuy  Boolean
   */
  public void setFlashBuy(Boolean flashBuy) {
    this.flashBuy = flashBuy;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for flash buy index url.
   *
   * @param  flashBuyIndexUrl  String
   */
  public void setFlashBuyIndexUrl(String flashBuyIndexUrl) {
    this.flashBuyIndexUrl = flashBuyIndexUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for from copy.
   *
   * @param  fromCopy  Boolean
   */
  public void setFromCopy(Boolean fromCopy) {
    this.fromCopy = fromCopy;
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
   * setter method for merchant name.
   *
   * @param  merchantName  String
   */
  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for merchant no.
   *
   * @param  merchantNo  String
   */
  public void setMerchantNo(String merchantNo) {
    this.merchantNo = merchantNo;
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
   * setter method for quick order.
   *
   * @param  quickOrder  Boolean
   */
  public void setQuickOrder(Boolean quickOrder) {
    this.quickOrder = quickOrder;
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
   * setter method for schedule date time.
   *
   * @param  scheduleDateTime  Date
   */
  public void setScheduleDateTime(Date scheduleDateTime) {
    this.scheduleDateTime = scheduleDateTime;
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
   * @param  status  String
   */
  public void setStatus(String status) {
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

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("MerchantCommand{");
    sb.append("name='").append(name).append('\'');
    sb.append(", merchantNo='").append(merchantNo).append('\'');
    sb.append(", merchantName='").append(merchantName).append('\'');
    sb.append(", intervalForOrder=").append(intervalForOrder);
    sb.append(", status='").append(status).append('\'');
    sb.append(", totalCount=").append(totalCount);
    sb.append(", indexUrl='").append(indexUrl).append('\'');
    sb.append('}');

    return sb.toString();
  }
} // end class MerchantCommand
