package com.ly.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ly.model.base.CreatorObject;
import com.ly.model.type.StatusType;


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

  @Cascade({ CascadeType.ALL, CascadeType.SAVE_UPDATE })
  @JoinColumn(
    name     = "appTypeId",
    nullable = false
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private AppType appType;

  /** associate @dealPrice1. */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal commissionAmount1;
  
  /**
   * 一个订单中每增加一个商品, 加多少钱
   */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal priceForOneMoreOffset;

  /** associate @dealPrice2. */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal commissionAmount2;

  /** associate @dealPrice3. */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal commissionAmount3;

  /** associate @dealPrice4. */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal commissionAmount4;

  /** associate @dealPrice5. */
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal commissionAmount5;

  /** 1 ~ 100 associate @dealPrice1. */
  @Column(length = 3)
  private Integer commissionPercentage1;

  /** 1 ~ 100 associate @dealPrice2. */
  @Column(length = 3)
  private Integer commissionPercentage2;

  /** 1 ~ 100 associate @dealPrice3. */
  @Column(length = 3)
  private Integer commissionPercentage3;

  /** 1 ~ 100 associate @dealPrice4. */
  @Column(length = 3)
  private Integer commissionPercentage4;

  /** 1 ~ 100 associate @dealPrice5. */
  @Column(length = 3)
  private Integer commissionPercentage5;


  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal dealPrice1;

  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal dealPrice2;
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal dealPrice3;
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal dealPrice4;
  @Column(
    precision = 19,
    scale     = 2
  )
  private BigDecimal dealPrice5;

  @Column(length = 255)
  private String description;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  @Column(length = 10)
  @Enumerated(EnumType.STRING)
  private StatusType status = StatusType.ENABLE;

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

    if ((appType != null) ? (!appType.equals(that.appType)) : (that.appType != null)) {
      return false;
    }

    if ((description != null) ? (!description.equals(that.description)) : (that.description != null)) {
      return false;
    }

    if ((dealPrice1 != null) ? (!dealPrice1.equals(that.dealPrice1)) : (that.dealPrice1 != null)) {
      return false;
    }

    if ((dealPrice2 != null) ? (!dealPrice2.equals(that.dealPrice2)) : (that.dealPrice2 != null)) {
      return false;
    }

    if ((dealPrice3 != null) ? (!dealPrice3.equals(that.dealPrice3)) : (that.dealPrice3 != null)) {
      return false;
    }

    if ((dealPrice4 != null) ? (!dealPrice4.equals(that.dealPrice4)) : (that.dealPrice4 != null)) {
      return false;
    }

    if ((dealPrice5 != null) ? (!dealPrice5.equals(that.dealPrice5)) : (that.dealPrice5 != null)) {
      return false;
    }

    if ((commissionAmount1 != null) ? (!commissionAmount1.equals(that.commissionAmount1))
                                    : (that.commissionAmount1 != null)) {
      return false;
    }

    if ((commissionPercentage1 != null) ? (!commissionPercentage1.equals(that.commissionPercentage1))
                                        : (that.commissionPercentage1 != null)) {
      return false;
    }

    if ((commissionAmount2 != null) ? (!commissionAmount2.equals(that.commissionAmount2))
                                    : (that.commissionAmount2 != null)) {
      return false;
    }

    if ((commissionPercentage2 != null) ? (!commissionPercentage2.equals(that.commissionPercentage2))
                                        : (that.commissionPercentage2 != null)) {
      return false;
    }

    if ((commissionAmount3 != null) ? (!commissionAmount3.equals(that.commissionAmount3))
                                    : (that.commissionAmount3 != null)) {
      return false;
    }

    if ((commissionPercentage3 != null) ? (!commissionPercentage3.equals(that.commissionPercentage3))
                                        : (that.commissionPercentage3 != null)) {
      return false;
    }

    if ((commissionAmount4 != null) ? (!commissionAmount4.equals(that.commissionAmount4))
                                    : (that.commissionAmount4 != null)) {
      return false;
    }

    if ((commissionPercentage4 != null) ? (!commissionPercentage4.equals(that.commissionPercentage4))
                                        : (that.commissionPercentage4 != null)) {
      return false;
    }

    if ((commissionAmount5 != null) ? (!commissionAmount5.equals(that.commissionAmount5))
                                    : (that.commissionAmount5 != null)) {
      return false;
    }

    if ((commissionPercentage5 != null) ? (!commissionPercentage5.equals(that.commissionPercentage5))
                                        : (that.commissionPercentage5 != null)) {
      return false;
    }
    if ((priceForOneMoreOffset != null) ? (!priceForOneMoreOffset.equals(that.priceForOneMoreOffset))
                                        : (that.priceForOneMoreOffset != null)) {
      return false;
    }

    return status == that.status;

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
   * getter method for commission amount1.
   *
   * @return  BigDecimal
   */
  public BigDecimal getCommissionAmount1() {
    return commissionAmount1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission amount2.
   *
   * @return  BigDecimal
   */
  public BigDecimal getCommissionAmount2() {
    return commissionAmount2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission amount3.
   *
   * @return  BigDecimal
   */
  public BigDecimal getCommissionAmount3() {
    return commissionAmount3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission amount4.
   *
   * @return  BigDecimal
   */
  public BigDecimal getCommissionAmount4() {
    return commissionAmount4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission amount5.
   *
   * @return  BigDecimal
   */
  public BigDecimal getCommissionAmount5() {
    return commissionAmount5;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission percentage1.
   *
   * @return  Integer
   */
  public Integer getCommissionPercentage1() {
    return commissionPercentage1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission percentage2.
   *
   * @return  Integer
   */
  public Integer getCommissionPercentage2() {
    return commissionPercentage2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission percentage3.
   *
   * @return  Integer
   */
  public Integer getCommissionPercentage3() {
    return commissionPercentage3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission percentage4.
   *
   * @return  Integer
   */
  public Integer getCommissionPercentage4() {
    return commissionPercentage4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for commission percentage5.
   *
   * @return  Integer
   */
  public Integer getCommissionPercentage5() {
    return commissionPercentage5;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for deal price1.
   *
   * @return  BigDecimal
   */
  public BigDecimal getDealPrice1() {
    return dealPrice1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for deal price2.
   *
   * @return  BigDecimal
   */
  public BigDecimal getDealPrice2() {
    return dealPrice2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for deal price3.
   *
   * @return  BigDecimal
   */
  public BigDecimal getDealPrice3() {
    return dealPrice3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for deal price4.
   *
   * @return  BigDecimal
   */
  public BigDecimal getDealPrice4() {
    return dealPrice4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for deal price5.
   *
   * @return  BigDecimal
   */
  public BigDecimal getDealPrice5() {
    return dealPrice5;
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
   * getter method for status.
   *
   * @return  StatusType
   */
  public StatusType getStatus() {
    return status;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (appType != null) ? appType.hashCode() : 0;
    result = (31 * result) + ((description != null) ? description.hashCode() : 0);
    result = (31 * result) + ((dealPrice1 != null) ? dealPrice1.hashCode() : 0);
    result = (31 * result) + ((dealPrice2 != null) ? dealPrice2.hashCode() : 0);
    result = (31 * result) + ((dealPrice3 != null) ? dealPrice3.hashCode() : 0);
    result = (31 * result) + ((dealPrice4 != null) ? dealPrice4.hashCode() : 0);
    result = (31 * result) + ((dealPrice5 != null) ? dealPrice5.hashCode() : 0);
    result = (31 * result) + ((commissionAmount1 != null) ? commissionAmount1.hashCode() : 0);
    result = (31 * result) + ((commissionPercentage1 != null) ? commissionPercentage1.hashCode() : 0);
    result = (31 * result) + ((commissionAmount2 != null) ? commissionAmount2.hashCode() : 0);
    result = (31 * result) + ((commissionPercentage2 != null) ? commissionPercentage2.hashCode() : 0);
    result = (31 * result) + ((commissionAmount3 != null) ? commissionAmount3.hashCode() : 0);
    result = (31 * result) + ((commissionPercentage3 != null) ? commissionPercentage3.hashCode() : 0);
    result = (31 * result) + ((commissionAmount4 != null) ? commissionAmount4.hashCode() : 0);
    result = (31 * result) + ((commissionPercentage4 != null) ? commissionPercentage4.hashCode() : 0);
    result = (31 * result) + ((commissionAmount5 != null) ? commissionAmount5.hashCode() : 0);
    result = (31 * result) + ((commissionPercentage5 != null) ? commissionPercentage5.hashCode() : 0);
    result = (31 * result) + ((status != null) ? status.hashCode() : 0);
    result = (31 * result) + ((priceForOneMoreOffset != null) ? priceForOneMoreOffset.hashCode() : 0);

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
   * setter method for commission amount1.
   *
   * @param  commissionAmount1  BigDecimal
   */
  public void setCommissionAmount1(BigDecimal commissionAmount1) {
    this.commissionAmount1 = commissionAmount1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission amount2.
   *
   * @param  commissionAmount2  BigDecimal
   */
  public void setCommissionAmount2(BigDecimal commissionAmount2) {
    this.commissionAmount2 = commissionAmount2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission amount3.
   *
   * @param  commissionAmount3  BigDecimal
   */
  public void setCommissionAmount3(BigDecimal commissionAmount3) {
    this.commissionAmount3 = commissionAmount3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission amount4.
   *
   * @param  commissionAmount4  BigDecimal
   */
  public void setCommissionAmount4(BigDecimal commissionAmount4) {
    this.commissionAmount4 = commissionAmount4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission amount5.
   *
   * @param  commissionAmount5  BigDecimal
   */
  public void setCommissionAmount5(BigDecimal commissionAmount5) {
    this.commissionAmount5 = commissionAmount5;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission percentage1.
   *
   * @param  commissionPercentage1  Integer
   */
  public void setCommissionPercentage1(Integer commissionPercentage1) {
    this.commissionPercentage1 = commissionPercentage1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission percentage2.
   *
   * @param  commissionPercentage2  Integer
   */
  public void setCommissionPercentage2(Integer commissionPercentage2) {
    this.commissionPercentage2 = commissionPercentage2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission percentage3.
   *
   * @param  commissionPercentage3  Integer
   */
  public void setCommissionPercentage3(Integer commissionPercentage3) {
    this.commissionPercentage3 = commissionPercentage3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission percentage4.
   *
   * @param  commissionPercentage4  Integer
   */
  public void setCommissionPercentage4(Integer commissionPercentage4) {
    this.commissionPercentage4 = commissionPercentage4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for commission percentage5.
   *
   * @param  commissionPercentage5  Integer
   */
  public void setCommissionPercentage5(Integer commissionPercentage5) {
    this.commissionPercentage5 = commissionPercentage5;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for deal price1.
   *
   * @param  dealPrice1  BigDecimal
   */
  public void setDealPrice1(BigDecimal dealPrice1) {
    this.dealPrice1 = dealPrice1;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for deal price2.
   *
   * @param  dealPrice2  BigDecimal
   */
  public void setDealPrice2(BigDecimal dealPrice2) {
    this.dealPrice2 = dealPrice2;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for deal price3.
   *
   * @param  dealPrice3  BigDecimal
   */
  public void setDealPrice3(BigDecimal dealPrice3) {
    this.dealPrice3 = dealPrice3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for deal price4.
   *
   * @param  dealPrice4  BigDecimal
   */
  public void setDealPrice4(BigDecimal dealPrice4) {
    this.dealPrice4 = dealPrice4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for deal price5.
   *
   * @param  dealPrice5  BigDecimal
   */
  public void setDealPrice5(BigDecimal dealPrice5) {
    this.dealPrice5 = dealPrice5;
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
   * setter method for status.
   *
   * @param  status  StatusType
   */
  public void setStatus(StatusType status) {
    this.status = status;
  }

  public BigDecimal getPriceForOneMoreOffset() {
    return priceForOneMoreOffset;
  }

  public void setPriceForOneMoreOffset(BigDecimal priceForOneMoreOffset) {
    this.priceForOneMoreOffset = priceForOneMoreOffset;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  com.ly.model.base.BaseObject#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("Commission{");
    sb.append("appType=").append(appType);
    sb.append(", description='").append(description).append('\'');
    sb.append(", id=").append(id);
    sb.append(", dealPrice1=").append(dealPrice1);
    sb.append(", dealPrice2=").append(dealPrice2);
    sb.append(", dealPrice3=").append(dealPrice3);
    sb.append(", dealPrice4=").append(dealPrice4);
    sb.append(", dealPrice5=").append(dealPrice5);
    sb.append(", commissionAmount1=").append(commissionAmount1);
    sb.append(", commissionPercentage1=").append(commissionPercentage1);
    sb.append(", commissionAmount2=").append(commissionAmount2);
    sb.append(", commissionPercentage2=").append(commissionPercentage2);
    sb.append(", commissionAmount3=").append(commissionAmount3);
    sb.append(", commissionPercentage3=").append(commissionPercentage3);
    sb.append(", commissionAmount4=").append(commissionAmount4);
    sb.append(", commissionPercentage4=").append(commissionPercentage4);
    sb.append(", commissionAmount5=").append(commissionAmount5);
    sb.append(", commissionPercentage5=").append(commissionPercentage5);
    sb.append(", priceForOneMoreOffset=").append(priceForOneMoreOffset);
    sb.append(", status=").append(status);
    sb.append('}');

    return sb.toString();
  } // end method toString
} // end class Commission
