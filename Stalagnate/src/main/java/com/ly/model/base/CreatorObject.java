package com.ly.model.base;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.ly.model.User;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/20/2016 14:47
 */


@MappedSuperclass public abstract class CreatorObject extends BaseObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** Create date . */
  @JoinColumn(
    name       = "creatorId",
    insertable = true,
    updatable  = false
  )
  @ManyToOne(fetch = FetchType.EAGER)
  protected User creator;

  /** Last Updater . */
  @JoinColumn(
    name       = "lastUpdaterId",
    insertable = true,
    updatable  = true
  )
  @ManyToOne(fetch = FetchType.EAGER)
  protected User lastUpdater;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for creator.
   *
   * @return  User
   */
  public User getCreator() {
    return creator;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for last updater.
   *
   * @return  User
   */
  public User getLastUpdater() {
    return lastUpdater;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for creator.
   *
   * @param  creator  User
   */
  public void setCreator(User creator) {
    this.creator = creator;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for last updater.
   *
   * @param  lastUpdater  User
   */
  public void setLastUpdater(User lastUpdater) {
    this.lastUpdater = lastUpdater;
  }
} // end class CreatorObject
