package com.ly.domains;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Created by yongliu on 6/26/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  06/26/2017 11:53
 */
@Entity
@Table(name = "proxyaudit")
public class ProxyAudit extends BaseProxyInfo implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -5735007311336106533L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  private Long proxyId;

  @Column(
    name   = "tokenId",
    length = 150
  )
  private String tokenId;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for create date.
   *
   * @return  Date
   */
  public Date getCreateDate() {
    return createDate;
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
   * getter method for proxy id.
   *
   * @return  Long
   */
  public Long getProxyId() {
    return proxyId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for token id.
   *
   * @return  String
   */
  public String getTokenId() {
    return tokenId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for create date.
   *
   * @param  createDate  Date
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
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
   * setter method for proxy id.
   *
   * @param  proxyId  Long
   */
  public void setProxyId(Long proxyId) {
    this.proxyId = proxyId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for token id.
   *
   * @param  tokenId  String
   */
  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }
} // end class ProxyAudit
