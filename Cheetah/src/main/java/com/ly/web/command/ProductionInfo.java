package com.ly.web.command;

import java.math.BigDecimal;


/**
 * Created by yongliu on 10/10/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/10/2016 15:29
 */
public class ProductionInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String merchantId;

  private BigDecimal price;

  private String productionName;

  private String productionUrl;

  // default is @sku value
  // but some times will ref the parent sku (not search production sku)
  private String searchSku;

  // original sku, production sku
  private String sku;

  private String storeName;

  private String storeUrl;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for merchant id.
   *
   * @return  String
   */
  public String getMerchantId() {
    return merchantId;
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
   * getter method for production name.
   *
   * @return  String
   */
  public String getProductionName() {
    return productionName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for production url.
   *
   * @return  String
   */
  public String getProductionUrl() {
    return productionUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for search sku.
   *
   * @return  String
   */
  public String getSearchSku() {
    return searchSku;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for sku.
   *
   * @return  String
   */
  public String getSku() {
    return sku;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for store name.
   *
   * @return  String
   */
  public String getStoreName() {
    return storeName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for store url.
   *
   * @return  String
   */
  public String getStoreUrl() {
    return storeUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for merchant id.
   *
   * @param  merchantId  String
   */
  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
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
   * setter method for production name.
   *
   * @param  productionName  String
   */
  public void setProductionName(String productionName) {
    this.productionName = productionName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for production url.
   *
   * @param  productionUrl  String
   */
  public void setProductionUrl(String productionUrl) {
    this.productionUrl = productionUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for search sku.
   *
   * @param  searchSku  String
   */
  public void setSearchSku(String searchSku) {
    this.searchSku = searchSku;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for sku.
   *
   * @param  sku  String
   */
  public void setSku(String sku) {
    this.sku       = sku;
    this.searchSku = sku;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for store name.
   *
   * @param  storeName  String
   */
  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for store url.
   *
   * @param  storeUrl  String
   */
  public void setStoreUrl(String storeUrl) {
    this.storeUrl = storeUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("ProductionInfo{");
    sb.append("merchantId='").append(merchantId).append('\'');
    sb.append(", price=").append(price);
    sb.append(", productionName='").append(productionName).append('\'');
    sb.append(", productionUrl='").append(productionUrl).append('\'');
    sb.append(", searchSku='").append(searchSku).append('\'');
    sb.append(", sku='").append(sku).append('\'');
    sb.append(", storeName='").append(storeName).append('\'');
    sb.append(", storeUrl='").append(storeUrl).append('\'');
    sb.append('}');

    return sb.toString();
  }
} // end class ProductionInfo
