package com.ly.proxy.exceptions;

/**
 * Created by yongliu on 1/3/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  01/03/2017 09:58
 */
public class InvalidIpProxyException extends RuntimeException {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -7414512255251409421L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String ip;

  private Integer port;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new InvalidIpProxyException object.
   */
  public InvalidIpProxyException() {
    super();
  }

  /**
   * Creates a new InvalidIpProxyException object.
   *
   * @param  message  String
   */
  public InvalidIpProxyException(String message) {
    super(message);
  }

  /**
   * Creates a new InvalidIpProxyException object.
   *
   * @param  message  String
   * @param  cause    Throwable
   */
  public InvalidIpProxyException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new InvalidIpProxyException object.
   *
   * @param  ip       String
   * @param  port     String
   * @param  message  String
   */
  public InvalidIpProxyException(String ip, Integer port, String message) {
    super(message);
    this.ip   = ip;
    this.port = port;
  }

  /**
   * Creates a new InvalidIpProxyException object.
   *
   * @param  ip       String
   * @param  port     String
   * @param  message  String
   * @param  cause    Throwable
   */
  public InvalidIpProxyException(String ip, Integer port, String message, Throwable cause) {
    super(message, cause);
    this.ip   = ip;
    this.port = port;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

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


} // end class InvalidIpProxyException
