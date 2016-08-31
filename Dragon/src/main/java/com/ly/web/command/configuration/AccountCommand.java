package com.ly.web.command.configuration;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;

import com.ly.model.Account;
import com.ly.model.AppType;
import com.ly.model.type.CategoryType;

import com.ly.web.command.BaseCommand;


/**
 * Created by yongliu on 7/27/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/27/2016 10:23
 */
@DataTransferObject(converter = ObjectConverter.class)
public class AccountCommand extends BaseCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @RemoteProperty private Integer accountLevel;

  @RemoteProperty private Long appTypeId;

  @RemoteProperty private String appTypeName;

  @RemoteProperty private Boolean disabled;

  @RemoteProperty private String disabledDescription;

  @RemoteProperty private Long id;

  @RemoteProperty private Boolean locked;

  @RemoteProperty private String password;

  @RemoteProperty private String username;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new AccountCommand object.
   */
  public AccountCommand() { }


  /**
   * Creates a new AccountCommand object.
   *
   * @param  account  Account
   */
  public AccountCommand(Account account) {
    this.id                  = account.getId();
    this.username            = account.getUsername();
    this.password            = account.getPassword();
    this.accountLevel        = account.getAccountLevel();
    this.disabled            = account.getDisabled();
    this.locked              = account.getLocked();
    this.disabledDescription = account.getDisabledDescription();
    this.appTypeId           = account.getAppType().getId();
    this.appTypeName         = account.getAppType().getAppName();
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for account level.
   *
   * @return  Integer
   */
  public Integer getAccountLevel() {
    return accountLevel;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for app type id.
   *
   * @return  Long
   */
  public Long getAppTypeId() {
    return appTypeId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for app type name.
   *
   * @return  String
   */
  public String getAppTypeName() {
    return appTypeName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for disabled.
   *
   * @return  Boolean
   */
  public Boolean getDisabled() {
    if (null == disabled) {
      return Boolean.FALSE;
    }

    return disabled;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for disabled description.
   *
   * @return  String
   */
  public String getDisabledDescription() {
    return disabledDescription;
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
   * getter method for locked.
   *
   * @return  Boolean
   */
  public Boolean getLocked() {
    if (null == locked) {
      return Boolean.FALSE;
    }

    return locked;
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
   * getter method for username.
   *
   * @return  String
   */
  public String getUsername() {
    return username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * populate.
   *
   * @return  Account
   */
  public Account populate() {
    Account account = new Account();

    if (null != getId()) {
      account.setId(this.getId());
      account.setDisabled(this.getDisabled());
    } else {
      account.setId(null);
      account.setDisabled(Boolean.FALSE);
    }

    account.setUsername(this.getUsername());
    account.setPassword(this.getPassword());
    account.setLocked(this.getLocked());

    if (this.getAppTypeId() != null) {
      AppType appType = new AppType();
      appType.setId(this.getAppTypeId());

      account.setAppType(appType);
    }

    return account;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for account level.
   *
   * @param  accountLevel  Integer
   */
  public void setAccountLevel(Integer accountLevel) {
    this.accountLevel = accountLevel;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for app type id.
   *
   * @param  appTypeId  Long
   */
  public void setAppTypeId(Long appTypeId) {
    this.appTypeId = appTypeId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for app type name.
   *
   * @param  appTypeName  String
   */
  public void setAppTypeName(String appTypeName) {
    this.appTypeName = appTypeName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for disabled.
   *
   * @param  disabled  Boolean
   */
  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for disabled description.
   *
   * @param  disabledDescription  String
   */
  public void setDisabledDescription(String disabledDescription) {
    this.disabledDescription = disabledDescription;
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
   * setter method for locked.
   *
   * @param  locked  Boolean
   */
  public void setLocked(Boolean locked) {
    this.locked = locked;
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
   * setter method for username.
   *
   * @param  username  String
   */
  public void setUsername(String username) {
    this.username = username;
  }
} // end class AccountCommand
