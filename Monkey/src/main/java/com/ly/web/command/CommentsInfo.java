package com.ly.web.command;

import java.io.Serializable;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;


/**
 * Created by yongliu on 7/12/16.
 *
 * @author   <a href="pagodasupport@sina.com">Yong Liu</a>
 * @version  07/12/2016 14:40
 */
public class CommentsInfo implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 4038225625321253332L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /**
   * key: sku value: comments content
   *
   * @i.e  key: 123423423423 value: It's so good
   */
  private Map<String, String> commentsMap = new HashedMap(10);

  private Boolean doNotComment = Boolean.FALSE;

  private String orderId;

  private String password;

  /**
   * TODO: DOCUMENT ME!
   *
   * @tagsCount  means must select @tagsCount tag(s) default is null. if this property has value, means for every sku,
   *             must select @tagsCount tag(s) if it is null, means random select tags from 1~2.
   */
  private Integer tagsCount;

  private String username;

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

    CommentsInfo that = (CommentsInfo) o;

    if ((commentsMap != null) ? (!commentsMap.equals(that.commentsMap)) : (that.commentsMap != null)) {
      return false;
    }

    if ((orderId != null) ? (!orderId.equals(that.orderId)) : (that.orderId != null)) {
      return false;
    }

    if ((password != null) ? (!password.equals(that.password)) : (that.password != null)) {
      return false;
    }

    return !((username != null) ? (!username.equals(that.username)) : (that.username != null));

  } // end method equals

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
    if (null == doNotComment) {
      return Boolean.FALSE;
    }

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
   * getter method for password.
   *
   * @return  String
   */
  public String getPassword() {
    return password;
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
   * getter method for username.
   *
   * @return  String
   */
  public String getUsername() {
    return username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (commentsMap != null) ? commentsMap.hashCode() : 0;
    result = (31 * result) + ((orderId != null) ? orderId.hashCode() : 0);
    result = (31 * result) + ((password != null) ? password.hashCode() : 0);
    result = (31 * result) + ((username != null) ? username.hashCode() : 0);

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
   * setter method for password.
   *
   * @param  password  String
   */
  public void setPassword(String password) {
    this.password = password;
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

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for username.
   *
   * @param  username  String
   */
  public void setUsername(String username) {
    this.username = username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final StringBuffer sb = new StringBuffer("CommentsInfo{");
    sb.append("commentsMap=").append(commentsMap);
    sb.append(", orderId='").append(orderId).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append(", username='").append(username).append('\'');
    sb.append('}');

    return sb.toString();
  }
} // end class CommentsInfo
