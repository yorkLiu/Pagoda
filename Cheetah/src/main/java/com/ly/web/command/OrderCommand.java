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

  private Set<ItemInfoCommand> items = new HashSet<>();

  private String password;

  private String username;

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
   * getter method for items.
   *
   * @return  Set
   */
  public Set<ItemInfoCommand> getItems() {
    return items;
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
   * setter method for items.
   *
   * @param  items  Set
   */
  public void setItems(Set<ItemInfoCommand> items) {
    this.items = items;
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
} // end class OrderCommand