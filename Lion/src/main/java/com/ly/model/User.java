package com.ly.model;

import com.ly.model.base.AbstractUserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Collection;


/**
 * Created by yongliu on 7/20/16.
 *
 * @author   <a href="mailto:pagodasupport@sina.com">Yong Liu</a>
 * @version  07/20/2016 15:16
 */
@Entity
@Table(name = "User")
public class User extends AbstractUserInfo 
//  implements Serializable, UserDetails 
{
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @GeneratedValue(strategy = GenerationType.IDENTITY)
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


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }



//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//    return null;
//  }
//
//  @Override
//  public boolean isAccountNonExpired() {
//    return !getExpired();
//  }
//
//  @Override
//  public boolean isAccountNonLocked() {
//    return !getLocked();
//  }
//
//  @Override
//  public boolean isCredentialsNonExpired() {
//    return !getCredentialsExpired();
//  }
//
//  @Override
//  public boolean isEnabled() {
//    return getEnable();
//  }
} // end class User
