package com.ly.model;

import com.ly.model.base.AbstractUserInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/20/2016 15:16
 */
@Entity
@Table(name = "User")
public class User extends AbstractUserInfo {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Long id;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * deepCopy.
   *
   * @param  user  User
   */
  public void deepCopy(User user) {
    user.setUsername(this.username);
    user.setPassword(this.password);
    user.setFirstName(this.firstName);
    user.setLastName(this.lastName);
    user.setEmail(this.email);
    user.setLocked(this.locked);
    user.setChangePassword(this.changePassword);
    user.setPasswordHint(this.passwordHint);
    user.setTelephone(this.telephone);
    user.setTelephone2(this.telephone2);
    user.setStatus(this.status);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

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

    User user = (User) o;

    if ((username != null) ? (!username.equals(user.username)) : (user.username != null)) {
      return false;
    }

    if ((password != null) ? (!password.equals(user.password)) : (user.password != null)) {
      return false;
    }

    if ((disabled != null) ? (!disabled.equals(user.disabled)) : (user.disabled != null)) {
      return false;
    }

    if ((locked != null) ? (!locked.equals(user.locked)) : (user.locked != null)) {
      return false;
    }

    if ((firstName != null) ? (!firstName.equals(user.firstName)) : (user.firstName != null)) {
      return false;
    }

    if ((lastName != null) ? (!lastName.equals(user.lastName)) : (user.lastName != null)) {
      return false;
    }

    if ((email != null) ? (!email.equals(user.email)) : (user.email != null)) {
      return false;
    }

    if ((telephone != null) ? (!telephone.equals(user.telephone)) : (user.telephone != null)) {
      return false;
    }

    return !((telephone2 != null) ? (!telephone2.equals(user.telephone2)) : (user.telephone2 != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (username != null) ? username.hashCode() : 0;
    result = (31 * result) + ((password != null) ? password.hashCode() : 0);
    result = (31 * result) + ((disabled != null) ? disabled.hashCode() : 0);
    result = (31 * result) + ((locked != null) ? locked.hashCode() : 0);
    result = (31 * result) + ((firstName != null) ? firstName.hashCode() : 0);
    result = (31 * result) + ((lastName != null) ? lastName.hashCode() : 0);
    result = (31 * result) + ((email != null) ? email.hashCode() : 0);
    result = (31 * result) + ((telephone != null) ? telephone.hashCode() : 0);
    result = (31 * result) + ((telephone2 != null) ? telephone2.hashCode() : 0);

    return result;
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
   * setter method for first name.
   *
   * @param  firstName  String
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
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
} // end class User
