package com.ly.proxy.pojo;

import org.springframework.util.StringUtils;


/**
 * Created by yongliu on 12/29/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  12/30/2016 10:08
 */
public class ProxyIPInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** 透明, 匿名, 高匿. */
  private String anonymousType;

  private String delay;

  private Boolean hasUsed = Boolean.FALSE;

  private Boolean invalid = Boolean.FALSE;

  private String ip;

  private String port;

  private String province;

  /** the value parse from @delay. */
  private Float speed;

  /**
   * TODO: DOCUMENT ME!
   *
   * @type  Http or Https
   */
  private String type;

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

    ProxyIPInfo that = (ProxyIPInfo) o;

    if ((ip != null) ? (!ip.equals(that.ip)) : (that.ip != null)) {
      return false;
    }

    if ((port != null) ? (!port.equals(that.port)) : (that.port != null)) {
      return false;
    }

    if ((province != null) ? (!province.equals(that.province)) : (that.province != null)) {
      return false;
    }

    if ((anonymousType != null) ? (!anonymousType.equals(that.anonymousType)) : (that.anonymousType != null)) {
      return false;
    }

    if ((type != null) ? (!type.equals(that.type)) : (that.type != null)) {
      return false;
    }

    return (delay != null) ? delay.equals(that.delay) : (that.delay == null);

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for anonymous type.
   *
   * @return  String
   */
  public String getAnonymousType() {
    return anonymousType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for delay.
   *
   * @return  String
   */
  public String getDelay() {
    return delay;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * get full IP address.
   *
   * @return  ip:port
   */
  public String getFullIpAddress() {
    return ip + ":" + port;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for has used.
   *
   * @return  Boolean
   */
  public Boolean getHasUsed() {
    return hasUsed;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for invalid.
   *
   * @return  Boolean
   */
  public Boolean getInvalid() {
    return invalid;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for ip.
   *
   * @return  String
   */
  public String getIp() {
    return ip;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for port.
   *
   * @return  String
   */
  public String getPort() {
    return port;
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
   * getter method for speed.
   *
   * @return  Float
   */
  public Float getSpeed() {
    return speed;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for type.
   *
   * @return  String
   */
  public String getType() {
    return type;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (ip != null) ? ip.hashCode() : 0;
    result = (31 * result) + ((port != null) ? port.hashCode() : 0);
    result = (31 * result) + ((province != null) ? province.hashCode() : 0);
    result = (31 * result) + ((anonymousType != null) ? anonymousType.hashCode() : 0);
    result = (31 * result) + ((type != null) ? type.hashCode() : 0);
    result = (31 * result) + ((delay != null) ? delay.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for anonymous type.
   *
   * @param  anonymousType  String
   */
  public void setAnonymousType(String anonymousType) {
    this.anonymousType = anonymousType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for delay.
   *
   * @param  delay  String
   */
  public void setDelay(String delay) {
    if ((delay != null) && StringUtils.hasText(delay)) {
      delay = delay.replace("秒", "");

      try {
        setSpeed(Float.parseFloat(delay.trim()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }

    this.delay = delay;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for has used.
   *
   * @param  hasUsed  Boolean
   */
  public void setHasUsed(Boolean hasUsed) {
    this.hasUsed = hasUsed;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for invalid.
   *
   * @param  invalid  Boolean
   */
  public void setInvalid(Boolean invalid) {
    this.invalid = invalid;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for ip.
   *
   * @param  ip  String
   */
  public void setIp(String ip) {
    this.ip = ip;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for port.
   *
   * @param  port  String
   */
  public void setPort(String port) {
    this.port = port;
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
   * setter method for speed.
   *
   * @param  speed  Float
   */
  public void setSpeed(Float speed) {
    this.speed = speed;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for type.
   *
   * @param  type  String
   */
  public void setType(String type) {
    this.type = type;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("ProxyIPInfo{");
    sb.append("ip='").append(ip).append('\'');
    sb.append(", port='").append(port).append('\'');
    sb.append(", province='").append(province).append('\'');
    sb.append(", anonymousType='").append(anonymousType).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append(", delay='").append(delay).append('\'');
    sb.append('}');

    return sb.toString();
  }
} // end class ProxyIPInfo
