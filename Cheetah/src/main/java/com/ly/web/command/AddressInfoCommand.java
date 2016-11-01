package com.ly.web.command;

/**
 * Created by yongliu on 10/31/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/31/2016 16:05
 */
public class AddressInfoCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String city;

  private String country;

  private String fullName;

  /** only for by ocean. */
  private String identityCardNum;

  private String province;

  private String telephoneNum;

  //~ Methods ----------------------------------------------------------------------------------------------------------


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
   * getter method for country.
   *
   * @return  String
   */
  public String getCountry() {
    return country;
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
   * getter method for telephone num.
   *
   * @return  String
   */
  public String getTelephoneNum() {
    return telephoneNum;
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
   * setter method for country.
   *
   * @param  country  String
   */
  public void setCountry(String country) {
    this.country = country;
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
   * setter method for telephone num.
   *
   * @param  telephoneNum  String
   */
  public void setTelephoneNum(String telephoneNum) {
    this.telephoneNum = telephoneNum;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("AddressInfoCommand{");
    sb.append("city='").append(city).append('\'');
    sb.append(", fullName='").append(fullName).append('\'');
    sb.append(", province='").append(province).append('\'');
    sb.append(", country='").append(country).append('\'');
    sb.append(", telephoneNum='").append(telephoneNum).append('\'');
    sb.append(", identityCardNum='").append(identityCardNum).append('\'');
    sb.append('}');

    return sb.toString();
  }
} // end class AddressInfoCommand
