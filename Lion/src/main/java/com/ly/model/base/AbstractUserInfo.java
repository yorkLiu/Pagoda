package com.ly.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;


/**
 * Created by yongliu on 7/21/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/21/2016 14:10
 */
@MappedSuperclass public abstract class AbstractUserInfo extends CreatorObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** TODO: DOCUMENT ME! */
  @Type(type = "yes_no")
  protected Boolean changePassword;

  /** TODO: DOCUMENT ME! */
  @Column(
    name     = "credentialsExpired",
    nullable = false
  )
  @Type(type = "yes_no")
  protected Boolean credentialsExpired = Boolean.FALSE;

  /** TODO: DOCUMENT ME! */
  @Type(type = "yes_no")
  protected Boolean disabled = Boolean.FALSE;

  /** TODO: DOCUMENT ME! */
  protected String email;

  /** TODO: DOCUMENT ME! */
  @Type(type = "yes_no")
  protected Boolean enable;

  /** TODO: DOCUMENT ME! */
  @Column(nullable = false)
  @Type(type = "yes_no")
  protected Boolean expired = Boolean.FALSE;

  /** TODO: DOCUMENT ME! */
  protected String firstName;

  /** TODO: DOCUMENT ME! */
  @Transient protected String fullName;


  /** DOCUMENT ME! */
  @Column(name = "lastLoginFailureDate")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastLoginFailureDate;

  /** TODO: DOCUMENT ME! */
  protected String lastName;

  /** TODO: DOCUMENT ME! */
  @Type(type = "yes_no")
  protected Boolean locked = Boolean.FALSE;

  /** TODO: DOCUMENT ME! */
  @Column(nullable = false)
  protected String password;

  /** TODO: DOCUMENT ME! */
  @Column protected Integer passwordFailureCount = 0;

  /** TODO: DOCUMENT ME! */
  protected String passwordHint;

  /** TODO: DOCUMENT ME! */
  @Transient protected Integer retryCount = 6;

  /** second to last web login date for this accoun. */
  @Column(name = "secondToLastLoginDate")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date secondToLastLoginDate;

  /** DOCUMENT ME! */
  @Column(name = "secondToLastLoginFailureDate")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date secondToLastLoginFailureDate;

  @Column(name = "loginFailureCount")
  protected Integer loginFailureCount = 0;

  /**
   * <pre>
       Deleted
   * </pre>
   */
  @Column(length = 20)
  protected String status;

  /** TODO: DOCUMENT ME! */
  protected String telephone;

  /** TODO: DOCUMENT ME! */
  protected String telephone2;

  /** TODO: DOCUMENT ME! */
  @Column(
    length   = 100,
    nullable = false,
    unique   = true
  )
  protected String username;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for change password.
   *
   * @return  Boolean
   */
  public Boolean getChangePassword() {
    return changePassword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for credentials expired.
   *
   * @return  Boolean
   */
  public Boolean getCredentialsExpired() {
    if (null == credentialsExpired) {
      return Boolean.FALSE;
    }

    return credentialsExpired;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for disabled.
   *
   * @return  Boolean
   */
  public Boolean getDisabled() {
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
   * getter method for enable.
   *
   * @return  Boolean
   */
  public Boolean getEnable() {
    if (null == enable) {
      return Boolean.TRUE;
    }

    return enable;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for expired.
   *
   * @return  Boolean
   */
  public Boolean getExpired() {
    if (null == expired) {
      return Boolean.FALSE;
    }

    return expired;
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
    return firstName + " " + lastName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for last login failure date.
   *
   * @return  Date
   */
  public Date getLastLoginFailureDate() {
    return lastLoginFailureDate;
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
   * getter method for password failure count.
   *
   * @return  Integer
   */
  public Integer getPasswordFailureCount() {
    return passwordFailureCount;
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
   * getter method for retry count.
   *
   * @return  Integer
   */
  public Integer getRetryCount() {
    return retryCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for second to last login date.
   *
   * @return  Date
   */
  public Date getSecondToLastLoginDate() {
    return secondToLastLoginDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for second to last login failure date.
   *
   * @return  Date
   */
  public Date getSecondToLastLoginFailureDate() {
    return secondToLastLoginFailureDate;
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
   * setter method for change password.
   *
   * @param  changePassword  Boolean
   */
  public void setChangePassword(Boolean changePassword) {
    this.changePassword = changePassword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for credentials expired.
   *
   * @param  credentialsExpired  Boolean
   */
  public void setCredentialsExpired(Boolean credentialsExpired) {
    this.credentialsExpired = credentialsExpired;
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
   * setter method for enable.
   *
   * @param  enable  Boolean
   */
  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for expired.
   *
   * @param  expired  Boolean
   */
  public void setExpired(Boolean expired) {
    this.expired = expired;
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
   * setter method for last login failure date.
   *
   * @param  lastLoginFailureDate  Date
   */
  public void setLastLoginFailureDate(Date lastLoginFailureDate) {
    this.lastLoginFailureDate = lastLoginFailureDate;
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
   * setter method for password failure count.
   *
   * @param  passwordFailureCount  Integer
   */
  public void setPasswordFailureCount(Integer passwordFailureCount) {
    this.passwordFailureCount = passwordFailureCount;
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
   * setter method for retry count.
   *
   * @param  retryCount  Integer
   */
  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for second to last login date.
   *
   * @param  secondToLastLoginDate  Date
   */
  public void setSecondToLastLoginDate(Date secondToLastLoginDate) {
    this.secondToLastLoginDate = secondToLastLoginDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for second to last login failure date.
   *
   * @param  secondToLastLoginFailureDate  Date
   */
  public void setSecondToLastLoginFailureDate(Date secondToLastLoginFailureDate) {
    this.secondToLastLoginFailureDate = secondToLastLoginFailureDate;
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
} // end class AbstractUserInfo
