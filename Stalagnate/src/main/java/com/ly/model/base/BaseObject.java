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
@MappedSuperclass
public abstract class BaseObject {
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
    this.createDate = createDate;
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
} // end class BaseObject
