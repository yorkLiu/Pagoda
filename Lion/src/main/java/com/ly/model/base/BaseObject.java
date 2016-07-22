package com.ly.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/20/2016 14:47
 */
@MappedSuperclass public abstract class BaseObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** Create date. */
  @Column(
    name      = "createDate",
    nullable  = false,
    updatable = false
  )
  @Temporal(TemporalType.TIMESTAMP)
  protected Date createDate;

  /** Update date. */
  @Column(name = "lastUpdateDate")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastUpdateDate;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new BaseObject object.
   */
  public BaseObject() {
    this.createDate     = new Date();
    this.lastUpdateDate = createDate;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for create date.
   *
   * @return  Date
   */
  public Date getCreateDate() {
    return createDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for last update date.
   *
   * @return  Date
   */
  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for create date.
   *
   * @param  createDate  Date
   */
  public void setCreateDate(Date createDate) {
    if (createDate == null) {
      this.createDate = new Date();
    } else {
      this.createDate = createDate;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for last update date.
   *
   * @param  lastUpdateDate  Date
   */
  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#toString()
   */
  @Override public String toString() {
    final String TAB = "    ";

    StringBuilder retValue = new StringBuilder();

    retValue.append("BaseObject ( ").append("createDate = ").append(
      this.createDate).append(TAB).append("updateDate = ").append(
      this.lastUpdateDate).append(TAB).append(" )");

    return retValue.toString();
  }
} // end class BaseObject
