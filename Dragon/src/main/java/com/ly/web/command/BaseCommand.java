package com.ly.web.command;

import java.util.Date;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.model.User;


/**
 * Created by yongliu on 7/26/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/26/2016 11:18
 */
@DataTransferObject(converter = ObjectConverter.class)
public class BaseCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  @RemoteProperty protected Date createDate;

  /** TODO: DOCUMENT ME! */
  @RemoteProperty protected String creatorName;

  /** TODO: DOCUMENT ME! */
  @RemoteProperty protected Date lastUpdateDate;

  /** TODO: DOCUMENT ME! */
  @RemoteProperty protected String lastUpdaterName;

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
   * getter method for creator name.
   *
   * @return  String
   */
  public String getCreatorName() {
    return creatorName;
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
   * getter method for last updater name.
   *
   * @return  String
   */
  public String getLastUpdaterName() {
    return lastUpdaterName;
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
   * setter method for creator name.
   *
   * @param  creatorName  String
   */
  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
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
   * setter method for last updater name.
   *
   * @param  lastUpdaterName  String
   */
  public void setLastUpdaterName(String lastUpdaterName) {
    this.lastUpdaterName = lastUpdaterName;
  }
} // end class BaseCommand
