package com.ly.web.command;

import java.util.HashSet;
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

  private String orderPrice;

  private String password;

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
   * getter method for order price.
   *
   * @return  String
   */
  public String getOrderPrice() {
    return orderPrice;
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
   * getter method for username.
   *
   * @return  String
   */
  public String getUsername() {
    return username;
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
   * setter method for order price.
   *
   * @param  orderPrice  String
   */
  public void setOrderPrice(String orderPrice) {
    this.orderPrice = orderPrice;
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
   * setter method for username.
   *
   * @param  username  String
   */
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String popupKeyword(){
    return items != null? items.iterator().next().getKeyword() : null;
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
    sb.append('}');

    return sb.toString();
  }
} // end class OrderCommand
