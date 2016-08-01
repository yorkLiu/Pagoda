package com.ly.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ly.model.base.CreatorObject;


/**
 * Created by yongliu on 7/31/16.
 *
 * @author       <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version      07/31/2016 16:35
 * @Description  拥金管理
 */

@Entity
@Table(name = "Commission")
public class Commission extends CreatorObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @JoinColumn(
    name     = "appTypeId",
    nullable = false
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private AppType appType;

  @Column(length = 255)
  private String description;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  @Column(
    name     = "price",
    nullable = false,
    precision = 19,
    scale     = 2
  )
  private BigDecimal price;

  /** 在@price 的成交价格, 如果大于了@priceForDeal1, 则应加价. */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDeal1;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDeal2;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDeal3;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDeal4;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDeal5;

  /** 在@price 的成交价格, 如果大于了@priceForDeal1, 则应加价@priceForDealOffset1. */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDealOffset1;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDealOffset2;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDealOffset3;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDealOffset4;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForDealOffset5;

  /** 每增加一个商品, 拥金增加多少(base price offset). */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForOneMoreOffset;

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

    Commission that = (Commission) o;

    if ((description != null) ? (!description.equals(that.description)) : (that.description != null)) {
      return false;
    }

    if ((price != null) ? (!price.equals(that.price)) : (that.price != null)) {
      return false;
    }

    if ((priceForOneMoreOffset != null) ? (!priceForOneMoreOffset.equals(that.priceForOneMoreOffset))
                                        : (that.priceForOneMoreOffset != null)) {
      return false;
    }

    if ((priceForDeal1 != null) ? (!priceForDeal1.equals(that.priceForDeal1)) : (that.priceForDeal1 != null)) {
      return false;
    }

    if ((priceForDealOffset1 != null) ? (!priceForDealOffset1.equals(that.priceForDealOffset1))
                                      : (that.priceForDealOffset1 != null)) {
      return false;
    }

    if ((priceForDeal2 != null) ? (!priceForDeal2.equals(that.priceForDeal2)) : (that.priceForDeal2 != null)) {
      return false;
    }

    if ((priceForDealOffset2 != null) ? (!priceForDealOffset2.equals(that.priceForDealOffset2))
                                      : (that.priceForDealOffset2 != null)) {
      return false;
    }

    if ((priceForDeal3 != null) ? (!priceForDeal3.equals(that.priceForDeal3)) : (that.priceForDeal3 != null)) {
      return false;
    }

    if ((priceForDealOffset3 != null) ? (!priceForDealOffset3.equals(that.priceForDealOffset3))
                                      : (that.priceForDealOffset3 != null)) {
      return false;
    }

    if ((priceForDeal4 != null) ? (!priceForDeal4.equals(that.priceForDeal4)) : (that.priceForDeal4 != null)) {
      return false;
    }

    if ((priceForDealOffset4 != null) ? (!priceForDealOffset4.equals(that.priceForDealOffset4))
                                      : (that.priceForDealOffset4 != null)) {
      return false;
    }

    if ((priceForDeal5 != null) ? (!priceForDeal5.equals(that.priceForDeal5)) : (that.priceForDeal5 != null)) {
      return false;
    }

    if ((priceForDealOffset5 != null) ? (!priceForDealOffset5.equals(that.priceForDealOffset5))
                                      : (that.priceForDealOffset5 != null)) {
      return false;
    }

    return !((appType != null) ? (!appType.equals(that.appType)) : (that.appType != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for app type.
   *
   * @return  AppType
   */
  public AppType getAppType() {
    return appType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for description.
   *
   * @return  String
   */
  public String getDescription() {
    return description;
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
   * getter method for price.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPrice() {
    return price;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal1.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDeal1() {
    return priceForDeal1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal2.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDeal2() {
    return priceForDeal2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal3.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDeal3() {
    return priceForDeal3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal4.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDeal4() {
    return priceForDeal4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal5.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDeal5() {
    return priceForDeal5;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal offset1.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDealOffset1() {
    return priceForDealOffset1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal offset2.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDealOffset2() {
    return priceForDealOffset2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal offset3.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDealOffset3() {
    return priceForDealOffset3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal offset4.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDealOffset4() {
    return priceForDealOffset4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for deal offset5.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForDealOffset5() {
    return priceForDealOffset5;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price for one more offset.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceForOneMoreOffset() {
    return priceForOneMoreOffset;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (description != null) ? description.hashCode() : 0;
    result = (31 * result) + ((price != null) ? price.hashCode() : 0);
    result = (31 * result) + ((priceForOneMoreOffset != null) ? priceForOneMoreOffset.hashCode() : 0);
    result = (31 * result) + ((priceForDeal1 != null) ? priceForDeal1.hashCode() : 0);
    result = (31 * result) + ((priceForDealOffset1 != null) ? priceForDealOffset1.hashCode() : 0);
    result = (31 * result) + ((priceForDeal2 != null) ? priceForDeal2.hashCode() : 0);
    result = (31 * result) + ((priceForDealOffset2 != null) ? priceForDealOffset2.hashCode() : 0);
    result = (31 * result) + ((priceForDeal3 != null) ? priceForDeal3.hashCode() : 0);
    result = (31 * result) + ((priceForDealOffset3 != null) ? priceForDealOffset3.hashCode() : 0);
    result = (31 * result) + ((priceForDeal4 != null) ? priceForDeal4.hashCode() : 0);
    result = (31 * result) + ((priceForDealOffset4 != null) ? priceForDealOffset4.hashCode() : 0);
    result = (31 * result) + ((priceForDeal5 != null) ? priceForDeal5.hashCode() : 0);
    result = (31 * result) + ((priceForDealOffset5 != null) ? priceForDealOffset5.hashCode() : 0);
    result = (31 * result) + ((appType != null) ? appType.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for app type.
   *
   * @param  appType  AppType
   */
  public void setAppType(AppType appType) {
    this.appType = appType;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for description.
   *
   * @param  description  String
   */
  public void setDescription(String description) {
    this.description = description;
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
   * setter method for price.
   *
   * @param  price  BigDecimal
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal1.
   *
   * @param  priceForDeal1  BigDecimal
   */
  public void setPriceForDeal1(BigDecimal priceForDeal1) {
    this.priceForDeal1 = priceForDeal1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal2.
   *
   * @param  priceForDeal2  BigDecimal
   */
  public void setPriceForDeal2(BigDecimal priceForDeal2) {
    this.priceForDeal2 = priceForDeal2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal3.
   *
   * @param  priceForDeal3  BigDecimal
   */
  public void setPriceForDeal3(BigDecimal priceForDeal3) {
    this.priceForDeal3 = priceForDeal3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal4.
   *
   * @param  priceForDeal4  BigDecimal
   */
  public void setPriceForDeal4(BigDecimal priceForDeal4) {
    this.priceForDeal4 = priceForDeal4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal5.
   *
   * @param  priceForDeal5  BigDecimal
   */
  public void setPriceForDeal5(BigDecimal priceForDeal5) {
    this.priceForDeal5 = priceForDeal5;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal offset1.
   *
   * @param  priceForDealOffset1  BigDecimal
   */
  public void setPriceForDealOffset1(BigDecimal priceForDealOffset1) {
    this.priceForDealOffset1 = priceForDealOffset1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal offset2.
   *
   * @param  priceForDealOffset2  BigDecimal
   */
  public void setPriceForDealOffset2(BigDecimal priceForDealOffset2) {
    this.priceForDealOffset2 = priceForDealOffset2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal offset3.
   *
   * @param  priceForDealOffset3  BigDecimal
   */
  public void setPriceForDealOffset3(BigDecimal priceForDealOffset3) {
    this.priceForDealOffset3 = priceForDealOffset3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal offset4.
   *
   * @param  priceForDealOffset4  BigDecimal
   */
  public void setPriceForDealOffset4(BigDecimal priceForDealOffset4) {
    this.priceForDealOffset4 = priceForDealOffset4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for deal offset5.
   *
   * @param  priceForDealOffset5  BigDecimal
   */
  public void setPriceForDealOffset5(BigDecimal priceForDealOffset5) {
    this.priceForDealOffset5 = priceForDealOffset5;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for price for one more offset.
   *
   * @param  priceForOneMoreOffset  BigDecimal
   */
  public void setPriceForOneMoreOffset(BigDecimal priceForOneMoreOffset) {
    this.priceForOneMoreOffset = priceForOneMoreOffset;
  }
} // end class Commission
