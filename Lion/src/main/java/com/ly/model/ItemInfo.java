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


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/21/2016 11:57
 */

@Entity
@Table(name = "ItemInfo")
public class ItemInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** comment content for this item. */
  private String commentContent;

  /** when search by price using. */
  private BigDecimal endPrice;

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id private Long id;

  private String itemUrl;

  @Column(length = 100)
  private String keyword;

  private String name;

  /** production price. */
  private BigDecimal price;

  private String sku;

  /** when search by price using. */
  private BigDecimal startPrice;

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

    ItemInfo itemInfo = (ItemInfo) o;

    if ((commentContent != null) ? (!commentContent.equals(itemInfo.commentContent))
                                 : (itemInfo.commentContent != null)) {
      return false;
    }

    if ((endPrice != null) ? (!endPrice.equals(itemInfo.endPrice)) : (itemInfo.endPrice != null)) {
      return false;
    }

    if ((itemUrl != null) ? (!itemUrl.equals(itemInfo.itemUrl)) : (itemInfo.itemUrl != null)) {
      return false;
    }

    if ((keyword != null) ? (!keyword.equals(itemInfo.keyword)) : (itemInfo.keyword != null)) {
      return false;
    }

    if ((name != null) ? (!name.equals(itemInfo.name)) : (itemInfo.name != null)) {
      return false;
    }

    if ((price != null) ? (!price.equals(itemInfo.price)) : (itemInfo.price != null)) {
      return false;
    }

    if ((sku != null) ? (!sku.equals(itemInfo.sku)) : (itemInfo.sku != null)) {
      return false;
    }

    return !((startPrice != null) ? (!startPrice.equals(itemInfo.startPrice)) : (itemInfo.startPrice != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for comment content.
   *
   * @return  String
   */
  public String getCommentContent() {
    return commentContent;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for end price.
   *
   * @return  BigDecimal
   */
  public BigDecimal getEndPrice() {
    return endPrice;
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
   * getter method for item url.
   *
   * @return  String
   */
  public String getItemUrl() {
    return itemUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for keyword.
   *
   * @return  String
   */
  public String getKeyword() {
    return keyword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for name.
   *
   * @return  String
   */
  public String getName() {
    return name;
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
   * getter method for sku.
   *
   * @return  String
   */
  public String getSku() {
    return sku;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for start price.
   *
   * @return  BigDecimal
   */
  public BigDecimal getStartPrice() {
    return startPrice;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (commentContent != null) ? commentContent.hashCode() : 0;
    result = (31 * result) + ((endPrice != null) ? endPrice.hashCode() : 0);
    result = (31 * result) + ((itemUrl != null) ? itemUrl.hashCode() : 0);
    result = (31 * result) + ((keyword != null) ? keyword.hashCode() : 0);
    result = (31 * result) + ((name != null) ? name.hashCode() : 0);
    result = (31 * result) + ((price != null) ? price.hashCode() : 0);
    result = (31 * result) + ((sku != null) ? sku.hashCode() : 0);
    result = (31 * result) + ((startPrice != null) ? startPrice.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for comment content.
   *
   * @param  commentContent  String
   */
  public void setCommentContent(String commentContent) {
    this.commentContent = commentContent;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for end price.
   *
   * @param  endPrice  BigDecimal
   */
  public void setEndPrice(BigDecimal endPrice) {
    this.endPrice = endPrice;
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
   * setter method for item url.
   *
   * @param  itemUrl  String
   */
  public void setItemUrl(String itemUrl) {
    this.itemUrl = itemUrl;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for keyword.
   *
   * @param  keyword  String
   */
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for name.
   *
   * @param  name  String
   */
  public void setName(String name) {
    this.name = name;
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
   * setter method for sku.
   *
   * @param  sku  String
   */
  public void setSku(String sku) {
    this.sku = sku;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for start price.
   *
   * @param  startPrice  BigDecimal
   */
  public void setStartPrice(BigDecimal startPrice) {
    this.startPrice = startPrice;
  }
} // end class ItemInfo
