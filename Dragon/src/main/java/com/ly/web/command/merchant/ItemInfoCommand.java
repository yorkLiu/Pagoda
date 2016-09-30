package com.ly.web.command.merchant;

import java.math.BigDecimal;

import java.util.Date;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.model.ItemInfo;
import com.ly.model.User;


/**
 * Created by yongliu on 9/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/22/2016 15:18
 */
@DataTransferObject(converter = ObjectConverter.class)
public class ItemInfoCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @RemoteProperty private String commentContent;

  @RemoteProperty private Integer count;

  @RemoteProperty private BigDecimal endPrice;

  @RemoteProperty private Long id;

  @RemoteProperty private String itemUrl;

  @RemoteProperty private String keyword;

  @RemoteProperty private String name;

  @RemoteProperty private BigDecimal price;

  @RemoteProperty private Integer priority;

  @RemoteProperty private String sku;

  @RemoteProperty private BigDecimal startPrice;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ItemInfoCommand object.
   */
  public ItemInfoCommand() { }

  /**
   * Creates a new ItemInfoCommand object.
   *
   * @param  itemInfo  ItemInfo
   */
  public ItemInfoCommand(ItemInfo itemInfo) {
    this.id             = itemInfo.getId();
    this.keyword        = itemInfo.getKeyword();
    this.itemUrl        = itemInfo.getItemUrl();
    this.sku            = itemInfo.getSku();
    this.name           = itemInfo.getName();
    this.price          = itemInfo.getPrice();
    this.count          = itemInfo.getCount();
    this.startPrice     = itemInfo.getStartPrice();
    this.endPrice       = itemInfo.getEndPrice();
    this.priority       = itemInfo.getPriority();
    this.commentContent = itemInfo.getCommentContent();
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

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
   * getter method for count.
   *
   * @return  Integer
   */
  public Integer getCount() {
    return count;
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
   * getter method for priority.
   *
   * @return  Integer
   */
  public Integer getPriority() {
    return priority;
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
   * popup.
   *
   * @param   user  User
   *
   * @return  ItemInfo
   */
  public ItemInfo popup(User user) {
    ItemInfo itemInfo = new ItemInfo();

    if ((this.getId() != null) && (this.getId().longValue() > 0)) {
      itemInfo.setId(this.getId());
      itemInfo.setLastUpdateDate(new Date());
      itemInfo.setLastUpdater(user);
    } else {
      itemInfo.setCreateDate(new Date());
      itemInfo.setCreator(user);
    }

    itemInfo.setKeyword(this.keyword);
    itemInfo.setItemUrl(this.itemUrl);
    itemInfo.setSku(this.getSku());
    itemInfo.setName(this.name);
    itemInfo.setPrice(this.getPrice());
    itemInfo.setCount(this.count);
    itemInfo.setStartPrice(this.getStartPrice());
    itemInfo.setEndPrice(this.getEndPrice());
    itemInfo.setPriority(this.priority);
    itemInfo.setCommentContent(this.commentContent);

    return itemInfo;
  } // end method popup

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
   * setter method for count.
   *
   * @param  count  Integer
   */
  public void setCount(Integer count) {
    this.count = count;
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
   * setter method for priority.
   *
   * @param  priority  Integer
   */
  public void setPriority(Integer priority) {
    this.priority = priority;
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
} // end class ItemInfoCommand
