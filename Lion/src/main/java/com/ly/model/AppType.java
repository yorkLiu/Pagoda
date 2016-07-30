package com.ly.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ly.model.base.BaseObject;


/**
 * Created by yongliu on 7/28/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/28/2016 17:32
 * 
 * PC, WEI_Xin, Phone, PAD ...
 */
@Entity
@Table(name = "AppType")
public class AppType extends BaseObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Column(
    nullable = false,
    length   = 50
  )
  private String appName;

  private String description;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  //~ Methods ----------------------------------------------------------------------------------------------------------


  /**
   * getter method for app name.
   *
   * @return  String
   */
  public String getAppName() {
    return appName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for description.
   *
   * @return  String
   */
  public String getDescription() {
    return description;
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
   * setter method for app name.
   *
   * @param  appName  String
   */
  public void setAppName(String appName) {
    this.appName = appName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for description.
   *
   * @param  description  String
   */
  public void setDescription(String description) {
    this.description = description;
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
} // end class AppType
