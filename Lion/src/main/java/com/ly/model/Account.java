package com.ly.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.ly.model.base.CreatorObject;
import com.ly.model.type.CategoryType;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/20/2016 15:06
 */

@Entity
@Table(name = "Account")
public class Account extends CreatorObject {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Integer accountLevel;

  @Enumerated(EnumType.STRING)
  private CategoryType categoryType;

  private Boolean disabled = Boolean.FALSE;

  @Column(
    length   = 500,
    nullable = true
  )
  private String disabledDescription;

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id private Long id;

  private Boolean locked;

  @Column(
    length   = 255,
    nullable = false
  )
  private String password;

  @Column(
    length   = 100,
    nullable = false
  )
  private String username;

  //~ Methods ----------------------------------------------------------------------------------------------------------

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

    Account account = (Account) o;

    if ((username != null) ? (!username.equals(account.username)) : (account.username != null)) {
      return false;
    }

    if ((password != null) ? (!password.equals(account.password)) : (account.password != null)) {
      return false;
    }

    if (categoryType != account.categoryType) {
      return false;
    }

    if ((disabled != null) ? (!disabled.equals(account.disabled)) : (account.disabled != null)) {
      return false;
    }

    if ((disabledDescription != null) ? (!disabledDescription.equals(account.disabledDescription))
                                      : (account.disabledDescription != null)) {
      return false;
    }

    if ((accountLevel != null) ? (!accountLevel.equals(account.accountLevel)) : (account.accountLevel != null)) {
      return false;
    }

    return !((locked != null) ? (!locked.equals(account.locked)) : (account.locked != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * getter method for category type.
   *
   * @return  CategoryType
   */
  public CategoryType getCategoryType() {
    return categoryType;
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
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (username != null) ? username.hashCode() : 0;
    result = (31 * result) + ((password != null) ? password.hashCode() : 0);
    result = (31 * result) + ((categoryType != null) ? categoryType.hashCode() : 0);
    result = (31 * result) + ((disabled != null) ? disabled.hashCode() : 0);
    result = (31 * result) + ((disabledDescription != null) ? disabledDescription.hashCode() : 0);
    result = (31 * result) + ((accountLevel != null) ? accountLevel.hashCode() : 0);
    result = (31 * result) + ((locked != null) ? locked.hashCode() : 0);

    return result;
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
   * setter method for category type.
   *
   * @param  categoryType  CategoryType
   */
  public void setCategoryType(CategoryType categoryType) {
    this.categoryType = categoryType;
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


} // end class Account
