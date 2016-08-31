package com.ly.web.command.configuration;

import java.math.BigDecimal;

import java.util.Date;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.model.AppType;
import com.ly.model.Commission;

import com.ly.web.command.BaseCommand;


/**
 * Created by yongliu on 8/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/22/2016 15:28
 */

@DataTransferObject(converter = ObjectConverter.class)
public class CommissionCommand extends BaseCommand {
  
  public static final int COMMISSION_TYPE_AMOUNT=1;
  public static final int COMMISSION_TYPE_PERCENTAGE=2;
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @RemoteProperty private Long appTypeId;

  @RemoteProperty private String appTypeName;

  @RemoteProperty private String description;

  @RemoteProperty private Long id;

  @RemoteProperty private String status;

  /** 如果大于了@dealPrice1, 则应加价. */
  @RemoteProperty private BigDecimal dealPrice1;

  @RemoteProperty private BigDecimal dealPrice2;

  @RemoteProperty private BigDecimal dealPrice3;

  @RemoteProperty private BigDecimal dealPrice4;

  @RemoteProperty private BigDecimal dealPrice5;

  @RemoteProperty private BigDecimal commissionAmount1;
  @RemoteProperty private Integer commissionPercentage1;
  
  @RemoteProperty private BigDecimal commissionAmount2;
  @RemoteProperty private Integer commissionPercentage2;
  
  @RemoteProperty private BigDecimal commissionAmount3;
  @RemoteProperty private Integer commissionPercentage3;
  
  @RemoteProperty private BigDecimal commissionAmount4;
  @RemoteProperty private Integer commissionPercentage4;
  
  @RemoteProperty private BigDecimal commissionAmount5;
  @RemoteProperty private Integer commissionPercentage5;

  /** 每增加一个商品, 拥金增加多少(base price offset). */
  @RemoteProperty private BigDecimal priceForOneMoreOffset;
  
  @RemoteProperty private Boolean allowCommission1;
  @RemoteProperty private Boolean allowCommission2;
  @RemoteProperty private Boolean allowCommission3;
  @RemoteProperty private Boolean allowCommission4;
  @RemoteProperty private Boolean allowCommission5;
  @RemoteProperty private Integer allowCommissionType1;
  @RemoteProperty private Integer allowCommissionType2;
  @RemoteProperty private Integer allowCommissionType3;
  @RemoteProperty private Integer allowCommissionType4;
  @RemoteProperty private Integer allowCommissionType5;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new CommissionCommand object.
   */
  public CommissionCommand() { }

  /**
   * Creates a new CommissionCommand object.
   *
   * @param  commission  Commission
   */
  public CommissionCommand(Commission commission) {
    this.id                    = commission.getId();
    this.description           = commission.getDescription();
    this.appTypeId             = commission.getAppType().getId();
    this.appTypeName           = commission.getAppType().getDescription();
    this.priceForOneMoreOffset = commission.getPriceForOneMoreOffset();
    this.status = commission.getStatus().toString();
    
    this.dealPrice1 = commission.getDealPrice1();
    this.dealPrice2 = commission.getDealPrice2();
    this.dealPrice3 = commission.getDealPrice3();
    this.dealPrice4 = commission.getDealPrice4();
    this.dealPrice5 = commission.getDealPrice5();
    
    // set allow commission checkbox checked or unchecked
    this.allowCommission1 = commission.getDealPrice1() != null;
    this.allowCommission2 = commission.getDealPrice2() != null;
    this.allowCommission3 = commission.getDealPrice3() != null;
    this.allowCommission4 = commission.getDealPrice4() != null;
    this.allowCommission5 = commission.getDealPrice5() != null;
    
    if(commission.getDealPrice1() != null){
      if(commission.getCommissionAmount1() != null){
        this.commissionAmount1 = commission.getCommissionAmount1();
        this.allowCommissionType1 = COMMISSION_TYPE_AMOUNT;
      } else if(commission.getCommissionPercentage1() != null){
        this.commissionPercentage1 = commission.getCommissionPercentage1();
        this.allowCommissionType1 = COMMISSION_TYPE_PERCENTAGE;
      }
    }  
    
    if(commission.getDealPrice2() != null){
      if(commission.getCommissionAmount2() != null){
        this.commissionAmount2 = commission.getCommissionAmount2();
        this.allowCommissionType2 = COMMISSION_TYPE_AMOUNT;
      } else if(commission.getCommissionPercentage2() != null){
        this.commissionPercentage2 = commission.getCommissionPercentage2();
        this.allowCommissionType2 = COMMISSION_TYPE_PERCENTAGE;
      }
    }
    
    if(commission.getDealPrice3() != null){
      if(commission.getCommissionAmount3() != null){
        this.commissionAmount3 = commission.getCommissionAmount3();
        this.allowCommissionType3 = COMMISSION_TYPE_AMOUNT;
      } else if(commission.getCommissionPercentage3() != null){
        this.commissionPercentage3 = commission.getCommissionPercentage3();
        this.allowCommissionType3 = COMMISSION_TYPE_PERCENTAGE;
      }
    }
    
    if(commission.getDealPrice4() != null){
      if(commission.getCommissionAmount4() != null){
        this.commissionAmount4 = commission.getCommissionAmount3();
        this.allowCommissionType4 = COMMISSION_TYPE_AMOUNT;
      } else if(commission.getCommissionPercentage4() != null){
        this.commissionPercentage4 = commission.getCommissionPercentage4();
        this.allowCommissionType4 = COMMISSION_TYPE_PERCENTAGE;
      }
    }
    
    if(commission.getDealPrice5() != null){
      if(commission.getCommissionAmount4() != null){
        this.commissionAmount5 = commission.getCommissionAmount5();
        this.allowCommissionType5 = COMMISSION_TYPE_AMOUNT;
      } else if(commission.getCommissionPercentage5() != null){
        this.commissionPercentage5 = commission.getCommissionPercentage5();
        this.allowCommissionType5 = COMMISSION_TYPE_PERCENTAGE;
      }
    }
  }

 
  /**
   * populate.
   *
   * @return  Commission
   */
  public Commission populate() {
    Commission commission = new Commission();

    if ((this.getId() != null) && !this.getId().equals(0)) {
      commission.setId(this.getId());
      commission.setLastUpdateDate(new Date());
    }

    commission.setDescription(this.getDescription());
    commission.setPriceForOneMoreOffset(this.getPriceForOneMoreOffset());
    
    if(this.getAllowCommission1()){
      commission.setDealPrice1(this.getDealPrice1());
      commission.setCommissionAmount1(this.getCommissionAmount1());
      commission.setCommissionPercentage1(this.getCommissionPercentage1());
    }
    
    if(this.getAllowCommission2()){
      commission.setDealPrice2(this.getDealPrice2());
      commission.setCommissionAmount2(this.getCommissionAmount2());
      commission.setCommissionPercentage2(this.getCommissionPercentage2());
    }
    
    if(this.getAllowCommission3()){
      commission.setDealPrice3(this.getDealPrice3());
      commission.setCommissionAmount3(this.getCommissionAmount3());
      commission.setCommissionPercentage3(this.getCommissionPercentage3());
    }
    
    if(this.getAllowCommission4()){
      commission.setDealPrice4(this.getDealPrice4());
      commission.setCommissionAmount4(this.getCommissionAmount4());
      commission.setCommissionPercentage4(this.getCommissionPercentage4());
    }
    
    if(this.getAllowCommission5()){
      commission.setDealPrice5(this.getDealPrice5());
      commission.setCommissionAmount5(this.getCommissionAmount5());
      commission.setCommissionPercentage5(this.getCommissionPercentage5());
    }
    
    return commission;
  } // end method populate

  public Long getAppTypeId() {
    return appTypeId;
  }

  public void setAppTypeId(Long appTypeId) {
    this.appTypeId = appTypeId;
  }

  public String getAppTypeName() {
    return appTypeName;
  }

  public void setAppTypeName(String appTypeName) {
    this.appTypeName = appTypeName;
  }

  public BigDecimal getCommissionAmount1() {
    return commissionAmount1;
  }

  public void setCommissionAmount1(BigDecimal commissionAmount1) {
    this.commissionAmount1 = commissionAmount1;
  }

  public BigDecimal getCommissionAmount2() {
    return commissionAmount2;
  }

  public void setCommissionAmount2(BigDecimal commissionAmount2) {
    this.commissionAmount2 = commissionAmount2;
  }

  public BigDecimal getCommissionAmount3() {
    return commissionAmount3;
  }

  public void setCommissionAmount3(BigDecimal commissionAmount3) {
    this.commissionAmount3 = commissionAmount3;
  }

  public BigDecimal getCommissionAmount4() {
    return commissionAmount4;
  }

  public void setCommissionAmount4(BigDecimal commissionAmount4) {
    this.commissionAmount4 = commissionAmount4;
  }

  public BigDecimal getCommissionAmount5() {
    return commissionAmount5;
  }

  public void setCommissionAmount5(BigDecimal commissionAmount5) {
    this.commissionAmount5 = commissionAmount5;
  }

  public Integer getCommissionPercentage1() {
    return commissionPercentage1;
  }

  public void setCommissionPercentage1(Integer commissionPercentage1) {
    this.commissionPercentage1 = commissionPercentage1;
  }

  public Integer getCommissionPercentage2() {
    return commissionPercentage2;
  }

  public void setCommissionPercentage2(Integer commissionPercentage2) {
    this.commissionPercentage2 = commissionPercentage2;
  }

  public Integer getCommissionPercentage3() {
    return commissionPercentage3;
  }

  public void setCommissionPercentage3(Integer commissionPercentage3) {
    this.commissionPercentage3 = commissionPercentage3;
  }

  public Integer getCommissionPercentage4() {
    return commissionPercentage4;
  }

  public void setCommissionPercentage4(Integer commissionPercentage4) {
    this.commissionPercentage4 = commissionPercentage4;
  }

  public Integer getCommissionPercentage5() {
    return commissionPercentage5;
  }

  public void setCommissionPercentage5(Integer commissionPercentage5) {
    this.commissionPercentage5 = commissionPercentage5;
  }

  public BigDecimal getDealPrice1() {
    return dealPrice1;
  }

  public void setDealPrice1(BigDecimal dealPrice1) {
    this.dealPrice1 = dealPrice1;
  }

  public BigDecimal getDealPrice2() {
    return dealPrice2;
  }

  public void setDealPrice2(BigDecimal dealPrice2) {
    this.dealPrice2 = dealPrice2;
  }

  public BigDecimal getDealPrice3() {
    return dealPrice3;
  }

  public void setDealPrice3(BigDecimal dealPrice3) {
    this.dealPrice3 = dealPrice3;
  }

  public BigDecimal getDealPrice4() {
    return dealPrice4;
  }

  public void setDealPrice4(BigDecimal dealPrice4) {
    this.dealPrice4 = dealPrice4;
  }

  public BigDecimal getDealPrice5() {
    return dealPrice5;
  }

  public void setDealPrice5(BigDecimal dealPrice5) {
    this.dealPrice5 = dealPrice5;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public BigDecimal getPriceForOneMoreOffset() {
    return priceForOneMoreOffset;
  }

  public void setPriceForOneMoreOffset(BigDecimal priceForOneMoreOffset) {
    this.priceForOneMoreOffset = priceForOneMoreOffset;
  }


  public Boolean getAllowCommission1() {
    if(allowCommission1 == null){
      return Boolean.FALSE;
    }
    return allowCommission1;
  }

  public void setAllowCommission1(Boolean allowCommission1) {
    this.allowCommission1 = allowCommission1;
  }

  public Boolean getAllowCommission2() {
    if(allowCommission2 == null){
      return Boolean.FALSE;
    }
    return allowCommission2;
  }

  public void setAllowCommission2(Boolean allowCommission2) {
    this.allowCommission2 = allowCommission2;
  }

  public Boolean getAllowCommission3() {
    if(allowCommission3 == null){
      return Boolean.FALSE;
    }
    return allowCommission3;
  }

  public void setAllowCommission3(Boolean allowCommission3) {
    this.allowCommission3 = allowCommission3;
  }

  public Boolean getAllowCommission4() {
    if(allowCommission4 == null){
      return Boolean.FALSE;
    }
    return allowCommission4;
  }

  public void setAllowCommission4(Boolean allowCommission4) {
    this.allowCommission4 = allowCommission4;
  }

  public Boolean getAllowCommission5() {
    if(allowCommission5 == null){
      return Boolean.FALSE;
    }
    return allowCommission5;
  }

  public void setAllowCommission5(Boolean allowCommission5) {
    this.allowCommission5 = allowCommission5;
  }

  public Integer getAllowCommissionType1() {
    return allowCommissionType1;
  }

  public void setAllowCommissionType1(Integer allowCommissionType1) {
    this.allowCommissionType1 = allowCommissionType1;
  }

  public Integer getAllowCommissionType2() {
    return allowCommissionType2;
  }

  public void setAllowCommissionType2(Integer allowCommissionType2) {
    this.allowCommissionType2 = allowCommissionType2;
  }

  public Integer getAllowCommissionType3() {
    return allowCommissionType3;
  }

  public void setAllowCommissionType3(Integer allowCommissionType3) {
    this.allowCommissionType3 = allowCommissionType3;
  }

  public Integer getAllowCommissionType4() {
    return allowCommissionType4;
  }

  public void setAllowCommissionType4(Integer allowCommissionType4) {
    this.allowCommissionType4 = allowCommissionType4;
  }

  public Integer getAllowCommissionType5() {
    return allowCommissionType5;
  }

  public void setAllowCommissionType5(Integer allowCommissionType5) {
    this.allowCommissionType5 = allowCommissionType5;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
} // end class CommissionCommand
