package com.ly.web.command.userrole;

import java.util.Date;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.BasicObjectConverter;

import com.ly.model.User;

import com.ly.web.command.BaseCommand;


/**
 * Created by yongliu on 7/26/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/26/2016 11:42
 */
@DataTransferObject(converter = BasicObjectConverter.class)
public class UserCommand extends BaseCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @RemoteProperty private Boolean changePassword;

  @RemoteProperty private Boolean disabled;

  @RemoteProperty private String email;

  @RemoteProperty private Boolean enabled;

  @RemoteProperty private String firstName;

  @RemoteProperty private String fullName;

  @RemoteProperty private Long id;

  @RemoteProperty private String lastName;

  @RemoteProperty private Boolean locked;

  @RemoteProperty private String password;

  @RemoteProperty private String passwordHint;

  @RemoteProperty private String status;

  @RemoteProperty private String telephone;

  @RemoteProperty private String telephone2;

  @RemoteProperty private String username;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new UserCommand object.
   */
  public UserCommand() { }

  /**
   * Creates a new UserCommand object.
   *
   * @param  user  User
   */
  public UserCommand(User user) {
    this.id             = user.getId();
    this.username       = user.getUsername();
    this.changePassword = user.getChangePassword();
    this.disabled       = user.getDisabled();
    this.email          = user.getEmail();
    this.firstName      = user.getFirstName();
    this.lastName       = user.getLastName();
    this.fullName       = user.getFirstName() + " " + user.getLastName();
    this.locked         = user.getLocked();
    this.passwordHint   = user.getPasswordHint();
    this.status         = user.getStatus();
    this.telephone      = user.getTelephone();
    this.telephone2     = user.getTelephone2();
    this.enabled        = user.getEnable();
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for change password.
   *
   * @return  Boolean
   */
  public Boolean getChangePassword() {
    if (changePassword == null) {
      return Boolean.FALSE;
    }

    return changePassword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for disabled.
   *
   * @return  Boolean
   */
  public Boolean getDisabled() {
    if (disabled == null) {
      return Boolean.FALSE;
    }

    return disabled;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for email.
   *
   * @return  String
   */
  public String getEmail() {
    return email;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for enabled.
   *
   * @return  Boolean
   */
  public Boolean getEnabled() {
    if (null == enabled) {
      return Boolean.TRUE;
    }

    return enabled;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for first name.
   *
   * @return  String
   */
  public String getFirstName() {
    return firstName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for full name.
   *
   * @return  String
   */
  public String getFullName() {
    return fullName;
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
   * getter method for last name.
   *
   * @return  String
   */
  public String getLastName() {
    return lastName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for locked.
   *
   * @return  Boolean
   */
  public Boolean getLocked() {
    if (locked == null) {
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
   * getter method for password hint.
   *
   * @return  String
   */
  public String getPasswordHint() {
    return passwordHint;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for status.
   *
   * @return  String
   */
  public String getStatus() {
    return status;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for telephone.
   *
   * @return  String
   */
  public String getTelephone() {
    return telephone;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for telephone2.
   *
   * @return  String
   */
  public String getTelephone2() {
    return telephone2;
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
   * @param  user  User
   */
  public void populate(User user) {
    if (this.getId() != null) {
      user.setId(this.getId());
      user.setLastUpdateDate(new Date());
    } else {
      user.setCreateDate(new Date());
    }

    user.setUsername(this.getUsername());
    user.setPassword(this.getPassword());
    user.setEmail(this.getEmail());
    user.setFirstName(this.getFirstName());
    user.setLastName(this.getLastName());
    user.setPasswordHint(this.getPasswordHint());
    user.setTelephone(this.getTelephone());
    user.setTelephone2(this.getTelephone2());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for change password.
   *
   * @param  changePassword  Boolean
   */
  public void setChangePassword(Boolean changePassword) {
    this.changePassword = changePassword;
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
   * setter method for email.
   *
   * @param  email  String
   */
  public void setEmail(String email) {
    this.email = email;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for enabled.
   *
   * @param  enabled  Boolean
   */
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for first name.
   *
   * @param  firstName  String
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for full name.
   *
   * @param  fullName  String
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
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
   * setter method for last name.
   *
   * @param  lastName  String
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
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
   * setter method for password hint.
   *
   * @param  passwordHint  String
   */
  public void setPasswordHint(String passwordHint) {
    this.passwordHint = passwordHint;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for status.
   *
   * @param  status  String
   */
  public void setStatus(String status) {
    this.status = status;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for telephone.
   *
   * @param  telephone  String
   */
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for telephone2.
   *
   * @param  telephone2  String
   */
  public void setTelephone2(String telephone2) {
    this.telephone2 = telephone2;
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
} // end class UserCommand
