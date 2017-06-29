package com.ly.domains;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * Created by yongliu on 6/26/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/26/2017 11:47
 */
@MappedSuperclass public class BaseProxyInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Column(length = 100)
  protected String area;

  @Column(length = 16)
  protected String ip;

  protected Integer port;

  protected Integer protocol;

  protected Integer types;

  //~ Methods ----------------------------------------------------------------------------------------------------------

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
   * @return  Integer
   */
  public Integer getPort() {
    return port;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for protocol.
   *
   * @return  Integer
   */
  public Integer getProtocol() {
    return protocol;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for types.
   *
   * @return  Integer
   */
  public Integer getTypes() {
    return types;
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
   * @param  port  Integer
   */
  public void setPort(Integer port) {
    this.port = port;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for protocol.
   *
   * @param  protocol  Integer
   */
  public void setProtocol(Integer protocol) {
    this.protocol = protocol;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for types.
   *
   * @param  types  Integer
   */
  public void setTypes(Integer types) {
    this.types = types;
  }
} // end class BaseProxyInfo
