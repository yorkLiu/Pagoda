package com.ly.web.command.merchant;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.model.PreOrderInfo;
import com.ly.model.User;
import com.ly.model.type.OrderStatusType;

import com.ly.web.command.BaseCommand;


/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 15:21
 */
@DataTransferObject(converter = ObjectConverter.class)
public class PreOrderCommand extends BaseCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @RemoteProperty private Long appTypeId;

  @RemoteProperty private String appTypeName;

  @RemoteProperty private Long id;

  @RemoteProperty private Set<ItemInfoCommand> items = new HashSet<>();

  @RemoteProperty private Long merchantId;

  @RemoteProperty private String preOrderNo;

  @RemoteProperty private Integer priority;

  @RemoteProperty private String status;

  @RemoteProperty private Integer totalCount;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new PreOrderCommand object.
   */
  public PreOrderCommand() { }

  /**
   * Creates a new PreOrderCommand object.
   *
   * @param  preOrder  PreOrderInfo
   */
  public PreOrderCommand(PreOrderInfo preOrder) {
    this.id          = preOrder.getId();
    this.merchantId  = preOrder.getMerchant().getId();
    this.preOrderNo  = preOrder.getPreOrderNo();
    this.totalCount  = preOrder.getTotalCount();
    this.appTypeName = preOrder.getAppType().getDescription();
    this.appTypeId   = preOrder.getAppType().getId();
    this.status      = preOrder.getStatus().toString();
    this.priority    = preOrder.getPriority();
    this.creatorName = (preOrder.getCreator() != null) ? preOrder.getCreator().getFullName() : null;
    this.createDate  = preOrder.getCreateDate();

    preOrder.getItems().stream().forEach(itemInfo -> { items.add(new ItemInfoCommand(itemInfo)); });

  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for app type id.
   *
   * @return  Long
   */
  public Long getAppTypeId() {
    return appTypeId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for app type name.
   *
   * @return  String
   */
  public String getAppTypeName() {
    return appTypeName;
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
  public Set<ItemInfoCommand> getItems() {
    return items;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * getter method for pre order no.
   *
   * @return  String
   */
  public String getPreOrderNo() {
    return preOrderNo;
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
   * populate.
   *
   * @param   user  User
   *
   * @return  PreOrderInfo
   */
  public PreOrderInfo populate(User user) {
    PreOrderInfo preOrderInfo = new PreOrderInfo();

    if ((this.getId() != null) && (this.getId().longValue() > 0)) {
      preOrderInfo.setId(this.id);
      preOrderInfo.setLastUpdater(user);
      preOrderInfo.setLastUpdateDate(new Date());
      preOrderInfo.setStatus(OrderStatusType.valueOf(this.status));
    } else {
      preOrderInfo.setCreateDate(new Date());
      preOrderInfo.setCreator(user);
      preOrderInfo.setStatus(OrderStatusType.INIT);
    }

    preOrderInfo.setPreOrderNo(this.preOrderNo);
    preOrderInfo.setTotalCount(this.totalCount);

    items.stream().forEach(item -> { preOrderInfo.addItemInfo(item.popup(user)); });

    return preOrderInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for app type id.
   *
   * @param  appTypeId  Long
   */
  public void setAppTypeId(Long appTypeId) {
    this.appTypeId = appTypeId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for app type name.
   *
   * @param  appTypeName  String
   */
  public void setAppTypeName(String appTypeName) {
    this.appTypeName = appTypeName;
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
  public void setItems(Set<ItemInfoCommand> items) {
    this.items = items;
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
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("PreOrderCommand{");
    sb.append("id=").append(id);
    sb.append(", preOrderNo='").append(preOrderNo).append('\'');
    sb.append(", MerchantId='").append(merchantId).append('\'');
    sb.append(", AppType='").append(appTypeName).append('\'');
    sb.append(", priority='").append(priority).append('\'');
    sb.append(", totalCount=").append(totalCount);
    sb.append('}');

    return sb.toString();
  }
} // end class PreOrderCommand
