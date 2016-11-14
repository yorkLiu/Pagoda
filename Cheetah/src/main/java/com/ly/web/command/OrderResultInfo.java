package com.ly.web.command;

/**
 * Created by yongliu on 11/1/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  11/01/2016 20:50
 */
public class OrderResultInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private AddressInfoCommand addressInfo;

  private String groupName;

  private String keyword;

  private String orderNo;

  private String password;

  private String paymentUrl;

  private String price;

  private String username;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new OrderResultInfo object.
   */
  public OrderResultInfo() { }

  /**
   * Creates a new OrderResultInfo object.
   *
   * @param  orderCommand  OrderCommand
   */
  public OrderResultInfo(OrderCommand orderCommand) {
    this.username    = orderCommand.getUsername();
    this.password    = orderCommand.getPassword();
    this.addressInfo = orderCommand.getAddressInfo();
    this.groupName   = orderCommand.getGroupName();
    this.price       = orderCommand.getOrderPrice();
    this.keyword     = orderCommand.popupKeyword();
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

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
   * getter method for group name.
   *
   * @return  String
   */
  public String getGroupName() {
    return groupName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for keyword.
   *
   * @return  String
   */
  public String getKeyword() {
    return keyword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for order no.
   *
   * @return  String
   */
  public String getOrderNo() {
    return orderNo;
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
   * getter method for payment url.
   *
   * @return  String
   */
  public String getPaymentUrl() {
    return paymentUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price.
   *
   * @return  String
   */
  public String getPrice() {
    return price;
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
   * setter method for group name.
   *
   * @param  groupName  String
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for keyword.
   *
   * @param  keyword  String
   */
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order no.
   *
   * @param  orderNo  String
   */
  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
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
   * setter method for payment url.
   *
   * @param  paymentUrl  String
   */
  public void setPaymentUrl(String paymentUrl) {
    this.paymentUrl = paymentUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price.
   *
   * @param  price  String
   */
  public void setPrice(String price) {
    this.price = price;
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
    final StringBuffer sb = new StringBuffer("OrderResultInfo{");
    sb.append("username='").append(username).append('\'');
    sb.append(", price='").append(price).append('\'');
    sb.append(", paymentUrl='").append(paymentUrl).append('\'');
    sb.append(", orderNo='").append(orderNo).append('\'');
    sb.append(", keyword='").append(keyword).append('\'');
    sb.append(", groupName='").append(groupName).append('\'');
    sb.append('}');

    return sb.toString();
  }
} // end class OrderResultInfo
