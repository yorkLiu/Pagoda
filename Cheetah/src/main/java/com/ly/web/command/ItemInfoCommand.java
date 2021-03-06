package com.ly.web.command;

import java.math.BigDecimal;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.ly.utils.URLUtils;


/**
 * Created by yongliu on 10/31/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/31/2016 10:39
 */
public class ItemInfoCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Boolean attentionItem;

  private Boolean attentionStore;

  private Integer count;

  /**
   * The category of group buying.
   *
   * <pre>
   i.e: 保健, 食品 ...
   * </pre>
   */
  private String groupBuyCategory;

  /** only for group buying. */
  private String grouponId;

  private String itemName;

  private String keyword;

  private String price;

  private String sku;

  private String url;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ItemInfoCommand object.
   */
  public ItemInfoCommand() { }

  /**
   * Creates a new ItemInfoCommand object.
   *
   * @param  url            String
   * @param  name           String
   * @param  price          String
   * @param  keyword        String
   * @param  count          Integer
   * @param  attentionItem  Boolean
   */
  public ItemInfoCommand(String url, String name, String price, String keyword, Integer count, Boolean attentionItem) {
    this.url           = url;
    this.itemName      = name;
    this.price         = price;
    this.keyword       = keyword;
    this.count         = count;
    this.attentionItem = attentionItem;
  }

  /**
   * Creates a new ItemInfoCommand object.
   *
   * @param  url            String
   * @param  sku            String
   * @param  name           String
   * @param  price          String
   * @param  keyword        String
   * @param  count          Integer
   * @param  attentionItem  Boolean
   */
  public ItemInfoCommand(String url, String sku, String name, String price, String keyword, Integer count,
    Boolean attentionItem) {
    this(url, name, price, keyword, count, attentionItem);
    this.sku = sku;
  }

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

    ItemInfoCommand that = (ItemInfoCommand) o;

    if ((sku != null) ? (!sku.equals(that.sku)) : (that.sku != null)) {
      return false;
    }

    if ((keyword != null) ? (!keyword.equals(that.keyword)) : (that.keyword != null)) {
      return false;
    }

    if ((count != null) ? (!count.equals(that.count)) : (that.count != null)) {
      return false;
    }

    if ((attentionItem != null) ? (!attentionItem.equals(that.attentionItem)) : (that.attentionItem != null)) {
      return false;
    }

    return !((attentionStore != null) ? (!attentionStore.equals(that.attentionStore)) : (that.attentionStore != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for attention item.
   *
   * @return  Boolean
   */
  public Boolean getAttentionItem() {
    if (attentionItem == null) {
      return Boolean.FALSE;
    }

    return attentionItem;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for attention store.
   *
   * @return  Boolean
   */
  public Boolean getAttentionStore() {
    if (attentionStore == null) {
      return Boolean.FALSE;
    }

    return attentionStore;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for count.
   *
   * @return  Integer
   */
  public Integer getCount() {
    if (count == null) {
      return Integer.parseInt("1");
    }

    return count;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for group buy category.
   *
   * @return  String
   */
  public String getGroupBuyCategory() {
    return groupBuyCategory;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for groupon id.
   *
   * @return  String
   */
  public String getGrouponId() {
    return grouponId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for item name.
   *
   * @return  String
   */
  public String getItemName() {
    return itemName;
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
   * getter method for price.
   *
   * @return  String
   */
  public String getPrice() {
    return price;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for price decimal.
   *
   * @return  BigDecimal
   */
  public BigDecimal getPriceDecimal() {
    if ((price != null) && StringUtils.hasText(price)) {
      return new BigDecimal(price.trim());

    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for sku.
   *
   * @return  String
   */
  public String getSku() {
    if ((url != null) && StringUtils.hasText(url) && ((sku == null) || !StringUtils.hasText(sku))) {
      this.sku = URLUtils.getSkuFromUrl(url);
    }

    return sku;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for url.
   *
   * @return  String
   */
  public String getUrl() {
    return url;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (sku != null) ? sku.hashCode() : 0;
    result = (31 * result) + ((keyword != null) ? keyword.hashCode() : 0);
    result = (31 * result) + ((count != null) ? count.hashCode() : 0);
    result = (31 * result) + ((attentionItem != null) ? attentionItem.hashCode() : 0);
    result = (31 * result) + ((attentionStore != null) ? attentionStore.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for attention item.
   *
   * @param  attentionItem  Boolean
   */
  public void setAttentionItem(Boolean attentionItem) {
    this.attentionItem = attentionItem;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for attention store.
   *
   * @param  attentionStore  Boolean
   */
  public void setAttentionStore(Boolean attentionStore) {
    this.attentionStore = attentionStore;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for count.
   *
   * @param  count  Integer
   */
  public void setCount(Integer count) {
    this.count = count;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for group buy category.
   *
   * @param  groupBuyCategory  String
   */
  public void setGroupBuyCategory(String groupBuyCategory) {
    this.groupBuyCategory = groupBuyCategory;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for groupon id.
   *
   * @param  grouponId  String
   */
  public void setGrouponId(String grouponId) {
    this.grouponId = grouponId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for item name.
   *
   * @param  itemName  String
   */
  public void setItemName(String itemName) {
    this.itemName = itemName;
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
   * setter method for price.
   *
   * @param  price  String
   */
  public void setPrice(String price) {
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
   * setter method for url.
   *
   * @param  url  String
   */
  public void setUrl(String url) {
    this.url = url;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("ItemInfoCommand{");
    sb.append("attentionItem=").append(attentionItem);
    sb.append(", sku='").append(sku).append('\'');
    sb.append(", url='").append(url).append('\'');
    sb.append(", keyword='").append(keyword).append('\'');
    sb.append(", count=").append(count);
    sb.append(", price=").append(price);
    sb.append(", attentionItem=").append(attentionItem);
    sb.append('}');

    return sb.toString();
  }
} // end class ItemInfoCommand
