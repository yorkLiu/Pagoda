package com.ly.model;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ly.model.base.AbstractUserInfo;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/20/2016 15:16
 */
@Entity
@Table(name = "User")
public class User extends AbstractUserInfo implements Serializable, UserDetails {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -2549173191097161611L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.REMOVE })
  @JoinTable(
    name               = "UserRole",
    joinColumns        = {
      @JoinColumn(
        name           = "userId",
        nullable       = false,
        updatable      = false
      )
    },
    inverseJoinColumns = {
      @JoinColumn(
        name           = "roleId",
        nullable       = false,
        updatable      = false
      )
    }
  )
  @ManyToMany(fetch = FetchType.EAGER)
  protected Set<Role> roles = new HashSet<Role>();

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * addLoginFailureCount.
   */
  public void addLoginFailureCount() {
    if (this.loginFailureCount == null) {
      this.loginFailureCount = 1;
    } else {
      this.loginFailureCount++;
    }

    if (loginFailureCount >= retryCount) {
      this.setLocked(Boolean.TRUE);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * addRole.
   *
   * @param  role  Role
   */
  public void addRole(Role role) {
    if (role != null) {
      this.roles.add(role);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

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
   * @see  org.springframework.security.core.userdetails.UserDetails#getAuthorities()
   */
  @Override @Transient public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(roles.toArray(new GrantedAuthority[0]));
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
   * getter method for roles.
   *
   * @return  Set
   */
  public Set<Role> getRoles() {
    return roles;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
   */
  @Override public boolean isAccountNonExpired() {
    return !getExpired();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
   */
  @Override public boolean isAccountNonLocked() {
    return !getLocked();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
   */
  @Override public boolean isCredentialsNonExpired() {
    return !getCredentialsExpired();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#isEnabled()
   */
  @Override public boolean isEnabled() {
    return getEnable();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * removeRole.
   *
   * @param  role  Role
   */
  public void removeRole(Role role) {
    if ((role != null) && (this.roles.size() > 0)) {
      this.roles.remove(role);
    }
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
   * setter method for roles.
   *
   * @param  roles  Set
   */
  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
} // end class User
