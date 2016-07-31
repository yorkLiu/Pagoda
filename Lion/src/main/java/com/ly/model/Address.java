package com.ly.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ly.model.base.CreatorObject;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/21/2016 11:51
 */

@Entity
@Table(name = "Address")
public class Address extends CreatorObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Column(
    length   = 255,
    nullable = false
  )
  private String address;

  @Column(
    length   = 20,
    nullable = true
  )
  private String city;

  @Column(
    length   = 20,
    nullable = true
  )
  private String county;

  @Column(
    length   = 50,
    nullable = false
  )
  private String fullName;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  @Column(
    length   = 20,
    nullable = true
  )
  private String identityCardNum;

  @Column(
    length   = 20,
    nullable = true
  )
  private String province;
  @Column(
    length   = 20,
    nullable = false
  )
  private String telephone;
  @Column(
    length   = 30,
    nullable = true
  )
  private String town;

  @Column(
    length   = 20,
    nullable = true
  )
  private String zipCode;

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

    Address that = (Address) o;

    if ((fullName != null) ? (!fullName.equals(that.fullName)) : (that.fullName != null)) {
      return false;
    }

    if ((address != null) ? (!address.equals(that.address)) : (that.address != null)) {
      return false;
    }

    if ((telephone != null) ? (!telephone.equals(that.telephone)) : (that.telephone != null)) {
      return false;
    }

    if ((zipCode != null) ? (!zipCode.equals(that.zipCode)) : (that.zipCode != null)) {
      return false;
    }

    if ((identityCardNum != null) ? (!identityCardNum.equals(that.identityCardNum)) : (that.identityCardNum != null)) {
      return false;
    }

    if ((province != null) ? (!province.equals(that.province)) : (that.province != null)) {
      return false;
    }

    if ((county != null) ? (!county.equals(that.county)) : (that.county != null)) {
      return false;
    }

    if ((city != null) ? (!city.equals(that.city)) : (that.city != null)) {
      return false;
    }

    return !((town != null) ? (!town.equals(that.town)) : (that.town != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * getter method for city.
   *
   * @return  String
   */
  public String getCity() {
    return city;
  }

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * getter method for county.
   *
   * @return  String
   */
  public String getCounty() {
    return county;
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
   * getter method for telephone.
   *
   * @return  String
   */
  public String getTelephone() {
    return telephone;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for town.
   *
   * @return  String
   */
  public String getTown() {
    return town;
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
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (fullName != null) ? fullName.hashCode() : 0;
    result = (31 * result) + ((address != null) ? address.hashCode() : 0);
    result = (31 * result) + ((telephone != null) ? telephone.hashCode() : 0);
    result = (31 * result) + ((zipCode != null) ? zipCode.hashCode() : 0);
    result = (31 * result) + ((identityCardNum != null) ? identityCardNum.hashCode() : 0);
    result = (31 * result) + ((province != null) ? province.hashCode() : 0);
    result = (31 * result) + ((county != null) ? county.hashCode() : 0);
    result = (31 * result) + ((city != null) ? city.hashCode() : 0);
    result = (31 * result) + ((town != null) ? town.hashCode() : 0);

    return result;
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
   * setter method for city.
   *
   * @param  city  String
   */
  public void setCity(String city) {
    this.city = city;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for county.
   *
   * @param  county  String
   */
  public void setCounty(String county) {
    this.county = county;
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
   * setter method for telephone.
   *
   * @param  telephone  String
   */
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for town.
   *
   * @param  town  String
   */
  public void setTown(String town) {
    this.town = town;
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
} // end class Address
