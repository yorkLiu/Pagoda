package com.ly.web.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by yongliu on 10/9/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/09/2016 15:40
 */
public class OrderCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private AddressInfoCommand addressInfo;

  /* is over sea 海购*/
  private Boolean allowOversea;

  /** For distinguish this order belong which merchant, usually we using the file name as the groupName. */
  private String groupName;

  private Set<ItemInfoCommand> items = new HashSet<>();

  /**订单备注*/
  private String orderCommentText;

  private String       orderPrice;
  private List<String> pagesInfo = new ArrayList<>();

  private String password;
  private String storeName;

  private String usedIpProxy;

  private String username;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new OrderCommand object.
   */
  public OrderCommand() { }

  /**
   * Creates a new OrderCommand object.
   *
   * @param  initAddressInfo  boolean
   */
  public OrderCommand(boolean initAddressInfo) {
    if (initAddressInfo) {
      addressInfo = new AddressInfoCommand();
    }
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * addItem.
   *
   * @param  itemInfoCommand  ItemInfoCommand
   */
  public void addItem(ItemInfoCommand itemInfoCommand) {
    if (items != null) {
      items.add(itemInfoCommand);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addPageInfo.
   *
   * @param  pageInfo  String
   */
  public void addPageInfo(String pageInfo) {
    // pageInfo will be: keyword: pageNum
    this.pagesInfo.add(pageInfo);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addPageInfo.
   *
   * @param  keyword  String
   * @param  pageNum  String
   */
  public void addPageInfo(String keyword, String pageNum) {
    addPageInfo(String.format("%s:%s", keyword, pageNum));
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

    OrderCommand that = (OrderCommand) o;

    if ((items != null) ? (!items.equals(that.items)) : (that.items != null)) {
      return false;
    }

    if ((username != null) ? (!username.equals(that.username)) : (that.username != null)) {
      return false;
    }

    if ((usedIpProxy != null) ? (!usedIpProxy.equals(that.usedIpProxy)) : (that.usedIpProxy != null)) {
      return false;
    }

    return (storeName != null) ? storeName.equals(that.storeName) : (that.storeName == null);

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for address info.
   *
   * @return  AddressInfoCommand
   */
  public AddressInfoCommand getAddressInfo() {
    return addressInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for allow oversea.
   *
   * @return  Boolean
   */
  public Boolean getAllowOversea() {
    if (allowOversea == null) {
      return Boolean.FALSE;
    }

    return allowOversea;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for group name.
   *
   * @return  String
   */
  public String getGroupName() {
    return groupName;
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
   * getter method for order comment text.
   *
   * @return  String
   */
  public String getOrderCommentText() {
    return orderCommentText;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for order price.
   *
   * @return  String
   */
  public String getOrderPrice() {
    return orderPrice;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for pages info.
   *
   * @return  List
   */
  public List<String> getPagesInfo() {
    return pagesInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for password.
   *
   * @return  String
   */
  public String getPassword() {
    return password;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for province.
   *
   * @return  String
   */
  public String getProvince() {
    return (addressInfo != null) ? addressInfo.getProvince() : null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for store name.
   *
   * @return  String
   */
  public String getStoreName() {
    return storeName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for used ip proxy.
   *
   * @return  String
   */
  public String getUsedIpProxy() {
    return usedIpProxy;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for username.
   *
   * @return  String
   */
  public String getUsername() {
    return username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (items != null) ? items.hashCode() : 0;
    result = (31 * result) + ((username != null) ? username.hashCode() : 0);
    result = (31 * result) + ((usedIpProxy != null) ? usedIpProxy.hashCode() : 0);
    result = (31 * result) + ((storeName != null) ? storeName.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * popupKeyword.
   *
   * @return  String
   */
  public String popupKeyword() {
    return (items != null) ? items.iterator().next().getKeyword() : null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for address info.
   *
   * @param  addressInfo  AddressInfoCommand
   */
  public void setAddressInfo(AddressInfoCommand addressInfo) {
    this.addressInfo = addressInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for allow oversea.
   *
   * @param  allowOversea  Boolean
   */
  public void setAllowOversea(Boolean allowOversea) {
    this.allowOversea = allowOversea;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for group name.
   *
   * @param  groupName  String
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
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
   * setter method for order comment text.
   *
   * @param  orderCommentText  String
   */
  public void setOrderCommentText(String orderCommentText) {
    this.orderCommentText = orderCommentText;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order price.
   *
   * @param  orderPrice  String
   */
  public void setOrderPrice(String orderPrice) {
    this.orderPrice = orderPrice;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for pages info.
   *
   * @param  pagesInfo  List
   */
  public void setPagesInfo(List<String> pagesInfo) {
    this.pagesInfo = pagesInfo;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for password.
   *
   * @param  password  String
   */
  public void setPassword(String password) {
    this.password = password;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for store name.
   *
   * @param  storeName  String
   */
  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for used ip proxy.
   *
   * @param  usedIpProxy  String
   */
  public void setUsedIpProxy(String usedIpProxy) {
    this.usedIpProxy = usedIpProxy;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for username.
   *
   * @param  username  String
   */
  public void setUsername(String username) {
    this.username = username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("OrderCommand{");
    sb.append("username='").append(username).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append(", allowOversea=").append(allowOversea).append('\'');
    sb.append(", groupName=").append(groupName);
    sb.append(", orderCommentText=").append(orderCommentText);
    sb.append('}');

    return sb.toString();
  }
} // end class OrderCommand
