package com.ly.web.command;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;


/**
 * Created by yongliu on 10/24/17.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  10/24/2017 17:20
 */
public class OrderInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /**
   * key: sku value: comments content
   *
   * @i.e  key: 123423423423 value: It's so good
   */
  private Map<String, String> commentsMap = null;

  private Boolean doNotComment = Boolean.FALSE;

  private String orderId;

  /**
   * TODO: DOCUMENT ME!
   *
   * @tagsCount  means must select @tagsCount tag(s) default is null. if this property has value, means for every sku,
   *             must select @tagsCount tag(s) if it is null, means random select tags from 1~2.
   */
  private Integer tagsCount;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new OrderInfo object.
   */
  public OrderInfo() { }

  /**
   * Creates a new OrderInfo object.
   *
   * @param  commentsInfo  CommentsInfo
   */
  public OrderInfo(CommentsInfo commentsInfo) {
    this.orderId      = commentsInfo.getOrderId();
    this.commentsMap  = commentsInfo.getCommentsMap();
    this.doNotComment = commentsInfo.getDoNotComment();
    this.tagsCount    = commentsInfo.getTagsCount();
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

    OrderInfo orderInfo = (OrderInfo) o;

    if ((orderId != null) ? (!orderId.equals(orderInfo.orderId)) : (orderInfo.orderId != null)) {
      return false;
    }

    if ((commentsMap != null) ? (!commentsMap.equals(orderInfo.commentsMap)) : (orderInfo.commentsMap != null)) {
      return false;
    }

    return (doNotComment != null) ? doNotComment.equals(orderInfo.doNotComment) : (orderInfo.doNotComment == null);

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for comments map.
   *
   * @return  Map
   */
  public Map<String, String> getCommentsMap() {
    return commentsMap;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for do not comment.
   *
   * @return  Boolean
   */
  public Boolean getDoNotComment() {
    return doNotComment;
  }

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * getter method for order id.
   *
   * @return  String
   */
  public String getOrderId() {
    return orderId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for tags count.
   *
   * @return  Integer
   */
  public Integer getTagsCount() {
    return tagsCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (orderId != null) ? orderId.hashCode() : 0;
    result = (31 * result) + ((commentsMap != null) ? commentsMap.hashCode() : 0);
    result = (31 * result) + ((doNotComment != null) ? doNotComment.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for comments map.
   *
   * @param  commentsMap  Map
   */
  public void setCommentsMap(Map<String, String> commentsMap) {
    this.commentsMap = commentsMap;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for do not comment.
   *
   * @param  doNotComment  Boolean
   */
  public void setDoNotComment(Boolean doNotComment) {
    this.doNotComment = doNotComment;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for order id.
   *
   * @param  orderId  String
   */
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for tags count.
   *
   * @param  tagsCount  Integer
   */
  public void setTagsCount(Integer tagsCount) {
    this.tagsCount = tagsCount;
  }
} // end class OrderInfo
