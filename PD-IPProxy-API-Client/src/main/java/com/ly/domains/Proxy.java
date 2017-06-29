package com.ly.domains;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yongliu on 6/23/17.
 */
@Entity
@Table(name = "proxys")
public class Proxy extends BaseProxyInfo implements Serializable {
  private static final long serialVersionUID = -7421522812628105952L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100)
  private String country;  
  
  @Column(name = "updatetime")
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date updateTime;
  
  @Column(precision = 5, scale = 2)
  private BigDecimal speed;
  
  private Integer score;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public Integer getTypes() {
    return types;
  }

  public void setTypes(Integer types) {
    this.types = types;
  }

  public Integer getProtocol() {
    return protocol;
  }

  public void setProtocol(Integer protocol) {
    this.protocol = protocol;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public BigDecimal getSpeed() {
    return speed;
  }

  public void setSpeed(BigDecimal speed) {
    this.speed = speed;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Proxy proxys = (Proxy) o;

    if (ip != null ? !ip.equals(proxys.ip) : proxys.ip != null) return false;
    if (port != null ? !port.equals(proxys.port) : proxys.port != null) return false;
    if (types != null ? !types.equals(proxys.types) : proxys.types != null) return false;
    if (protocol != null ? !protocol.equals(proxys.protocol) : proxys.protocol != null) return false;
    if (country != null ? !country.equals(proxys.country) : proxys.country != null) return false;
    if (area != null ? !area.equals(proxys.area) : proxys.area != null) return false;
    if (updateTime != null ? !updateTime.equals(proxys.updateTime) : proxys.updateTime != null) return false;
    if (speed != null ? !speed.equals(proxys.speed) : proxys.speed != null) return false;
    return score != null ? score.equals(proxys.score) : proxys.score == null;

  }

  @Override
  public int hashCode() {
    int result = ip != null ? ip.hashCode() : 0;
    result = 31 * result + (port != null ? port.hashCode() : 0);
    result = 31 * result + (types != null ? types.hashCode() : 0);
    result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    result = 31 * result + (area != null ? area.hashCode() : 0);
    result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
    result = 31 * result + (speed != null ? speed.hashCode() : 0);
    result = 31 * result + (score != null ? score.hashCode() : 0);
    return result;
  }
}
