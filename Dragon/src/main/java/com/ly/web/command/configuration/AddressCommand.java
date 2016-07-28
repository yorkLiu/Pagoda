package com.ly.web.command.configuration;

import java.util.Date;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.model.Address;

import com.ly.web.command.BaseCommand;


/**
 * Created by yongliu on 7/27/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/27/2016 10:38
 */

@DataTransferObject(converter = ObjectConverter.class)
public class AddressCommand extends BaseCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @RemoteProperty private String address;

  @RemoteProperty private String area;

  @RemoteProperty private String city;

  @RemoteProperty private String fullName;

  @RemoteProperty private Long id;

  @RemoteProperty private String identityCardNum;

  @RemoteProperty private String province;

  @RemoteProperty private String state;

  @RemoteProperty private String telephone;

  @RemoteProperty private String zipCode;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new AddressCommand object.
   */
  public AddressCommand() { }

  /**
   * Creates a new AddressCommand object.
   *
   * @param  address  Address
   */
  public AddressCommand(Address address) {
    this.id              = address.getId();
    this.address         = address.getAddress();
    this.fullName        = address.getFullName();
    this.province        = address.getProvince();
    this.state           = address.getState();
    this.city            = address.getCity();
    this.area            = address.getArea();
    this.identityCardNum = address.getIdentityCardNum();
    this.telephone       = address.getTelephone();
    this.zipCode         = address.getZipCode();
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for address.
   *
   * @return  String
   */
  public String getAddress() {
    return address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for area.
   *
   * @return  String
   */
  public String getArea() {
    return area;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for city.
   *
   * @return  String
   */
  public String getCity() {
    return city;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for full name.
   *
   * @return  String
   */
  public String getFullName() {
    return fullName;
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
   * getter method for identity card num.
   *
   * @return  String
   */
  public String getIdentityCardNum() {
    return identityCardNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for province.
   *
   * @return  String
   */
  public String getProvince() {
    return province;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for state.
   *
   * @return  String
   */
  public String getState() {
    return state;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for telephone.
   *
   * @return  String
   */
  public String getTelephone() {
    return telephone;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for zip code.
   *
   * @return  String
   */
  public String getZipCode() {
    return zipCode;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * populate.
   *
   * @return  Address
   */
  public Address populate() {
    Address address = new Address();

    if (null != this.getId()) {
      address.setId(this.getId());
      address.setLastUpdateDate(new Date());
    } else {
      address.setId(null);
    }

    address.setAddress(this.getAddress());
    address.setFullName(this.getFullName());
    address.setProvince(this.getProvince());
    address.setState(this.getState());
    address.setCity(this.getCity());
    address.setArea(this.getArea());
    address.setTelephone(this.getTelephone());
    address.setZipCode(this.getZipCode());
    address.setIdentityCardNum(this.getIdentityCardNum());

    return address;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for address.
   *
   * @param  address  String
   */
  public void setAddress(String address) {
    this.address = address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for area.
   *
   * @param  area  String
   */
  public void setArea(String area) {
    this.area = area;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for city.
   *
   * @param  city  String
   */
  public void setCity(String city) {
    this.city = city;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for full name.
   *
   * @param  fullName  String
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
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
   * setter method for identity card num.
   *
   * @param  identityCardNum  String
   */
  public void setIdentityCardNum(String identityCardNum) {
    this.identityCardNum = identityCardNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for province.
   *
   * @param  province  String
   */
  public void setProvince(String province) {
    this.province = province;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for state.
   *
   * @param  state  String
   */
  public void setState(String state) {
    this.state = state;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for telephone.
   *
   * @param  telephone  String
   */
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for zip code.
   *
   * @param  zipCode  String
   */
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }
} // end class AddressCommand
