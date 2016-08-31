package com.ly.web.command.configuration;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.model.AppType;

import com.ly.web.command.BaseCommand;


/**
 * Created by yongliu on 8/22/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/22/2016 17:18
 */

@DataTransferObject(converter = ObjectConverter.class)
public class AppTypeCommand extends BaseCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @RemoteProperty private String appName;

  @RemoteProperty private String description;

  @RemoteProperty private Long id;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new AppTypeCommand object.
   */
  public AppTypeCommand() { }

  /**
   * Creates a new AppTypeCommand object.
   *
   * @param  appType  AppType
   */
  public AppTypeCommand(AppType appType) {
    this.id          = appType.getId();
    this.appName     = appType.getAppName();
    this.description = appType.getDescription();
  }

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
} // end class AppTypeCommand
