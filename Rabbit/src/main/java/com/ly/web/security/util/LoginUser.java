package com.ly.web.security.util;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Component;

import org.springframework.util.Assert;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
//@Component("loginUser")
public class LoginUser extends User {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -4280420432982763425L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private boolean               accountNonExpired;
  private boolean               accountNonLocked;
  private Set<GrantedAuthority> authorities;
  private boolean               credentialsNonExpired;

  private boolean enabled;
  private String  password;
  private String  token3;

  private String token4;
  private String userLogon;
  private String username;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new LoginUser object.
   *
   * @param  username               DOCUMENT ME!
   * @param  password               DOCUMENT ME!
   * @param  token3                 DOCUMENT ME!
   * @param  token4                 DOCUMENT ME!
   * @param  enabled                DOCUMENT ME!
   * @param  accountNonExpired      DOCUMENT ME!
   * @param  credentialsNonExpired  DOCUMENT ME!
   * @param  accountNonLocked       DOCUMENT ME!
   * @param  authorities            DOCUMENT ME!
   * @param  userLogon              DOCUMENT ME!
   */
  public LoginUser(String username, String password, String token3, String token4, boolean enabled,
    boolean accountNonExpired,
    boolean credentialsNonExpired, boolean accountNonLocked, GrantedAuthority[] authorities, String userLogon) {
    this(username, password, token3, token4, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
      (authorities == null) ? null : Arrays.asList(authorities), userLogon);
  }

  /**
   * Creates a new LoginUser object.
   *
   * @param   username               DOCUMENT ME!
   * @param   password               DOCUMENT ME!
   * @param   token3                 DOCUMENT ME!
   * @param   token4                 DOCUMENT ME!
   * @param   enabled                DOCUMENT ME!
   * @param   accountNonExpired      DOCUMENT ME!
   * @param   credentialsNonExpired  DOCUMENT ME!
   * @param   accountNonLocked       DOCUMENT ME!
   * @param   authorities            DOCUMENT ME!
   * @param   userLogon              DOCUMENT ME!
   *
   * @throws  IllegalArgumentException  DOCUMENT ME!
   */
  public LoginUser(String username, String password, String token3, String token4, boolean enabled,
    boolean accountNonExpired,
    boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities,
    String userLogon) {
    super(username, password, enabled, accountNonExpired,
      credentialsNonExpired, accountNonLocked, authorities);

    if (((username == null) || "".equals(username)) || (password == null)) {
      throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
    }

    this.username              = username;
    this.password              = password;
    this.enabled               = enabled;
    this.accountNonExpired     = accountNonExpired;
    this.credentialsNonExpired = credentialsNonExpired;
    this.accountNonLocked      = accountNonLocked;
    this.authorities           = Collections.unmodifiableSet(sortAuthorities(authorities));
    this.userLogon             = userLogon;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  Object#equals(Object)
   */
  @Override public boolean equals(Object rhs) {
    if (!(rhs instanceof User) || (rhs == null)) {
      return false;
    }

    LoginUser user = (LoginUser) rhs;

    if (!authorities.equals(user.authorities)) {
      return false;
    }

    // We rely on constructor to guarantee non-null username and password
    return (this.getPassword().equals(user.getPassword()) && this.getUsername().equals(user.getUsername())
        && this.getUserLogon().equals(user.getUserLogon())
        && (this.isAccountNonExpired() == user.isAccountNonExpired())
        && (this.isAccountNonLocked() == user.isAccountNonLocked())
        && (this.isCredentialsNonExpired() == user.isCredentialsNonExpired())
        && (this.isEnabled() == user.isEnabled()));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#getAuthorities()
   */
  @Override public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#getPassword()
   */
  @Override public String getPassword() {
    return password;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getToken3() {
    return token3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getToken4() {
    return token4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getUserLogon() {
    return userLogon;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#getUsername()
   */
  @Override public String getUsername() {
    return username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  Object#hashCode()
   */
  @Override public int hashCode() {
    int code = 31;

    for (GrantedAuthority authority : getAuthorities()) {
      code = code * (authority.hashCode());
    }

    if (this.getPassword() != null) {
      code = code * (this.getPassword().hashCode());
    }

    if (this.getUsername() != null) {
      code = code * (this.getUsername().hashCode());
    }

    if (this.getUserLogon() != null) {
      code = code * (this.getUserLogon().hashCode());
    }

    if (this.isAccountNonExpired()) {
      code = code * -2;
    }

    if (this.isAccountNonLocked()) {
      code = code * -3;
    }

    if (this.isCredentialsNonExpired()) {
      code = code * -5;
    }

    if (this.isEnabled()) {
      code = code * -7;
    }

    return code;
  } // end method hashCode

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
   */
  @Override public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
   */
  @Override public boolean isAccountNonLocked() {
    return this.accountNonLocked;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
   */
  @Override public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.userdetails.UserDetails#isEnabled()
   */
  @Override public boolean isEnabled() {
    return enabled;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  enabled  DOCUMENT ME!
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  password  DOCUMENT ME!
   */
  public void setPassword(String password) {
    this.password = password;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  token3  DOCUMENT ME!
   */
  public void setToken3(String token3) {
    this.token3 = token3;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  token4  DOCUMENT ME!
   */
  public void setToken4(String token4) {
    this.token4 = token4;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  userLogon  DOCUMENT ME!
   */
  public void setUserLogon(String userLogon) {
    this.userLogon = userLogon;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  username  DOCUMENT ME!
   */
  public void setUsername(String username) {
    this.username = username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  Object#toString()
   */
  @Override public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString()).append(": ");
    sb.append("Username: ").append(this.username).append("; ");
    sb.append("Password: [PROTECTED]; ");
    sb.append("UserLogon: ").append(this.userLogon).append("; ");
    sb.append("Enabled: ").append(this.enabled).append("; ");
    sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
    sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
    sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

    if (!authorities.isEmpty()) {
      sb.append("Granted Authorities: ");

      boolean first = true;

      for (GrantedAuthority auth : authorities) {
        if (!first) {
          sb.append(",");
        }

        first = false;

        sb.append(auth);
      }
    } else {
      sb.append("Not granted any authorities");
    }

    return sb.toString();
  } // end method toString

  //~ ------------------------------------------------------------------------------------------------------------------

  private static SortedSet<GrantedAuthority> sortAuthorities(Collection<GrantedAuthority> authorities) {
    Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");

    // Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and SEC-717)
    SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<GrantedAuthority>(new AuthorityComparator());

    for (GrantedAuthority grantedAuthority : authorities) {
      Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
      sortedAuthorities.add(grantedAuthority);
    }

    return sortedAuthorities;
  }

  //~ Inner Classes ----------------------------------------------------------------------------------------------------

  private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = -741004248885463987L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    @Override public int compare(GrantedAuthority g1, GrantedAuthority g2) {
      // Neither should ever be null as each entry is checked before adding it to the set.
      // If the authority is null, it is a custom authority and should precede others.
      if (g2.getAuthority() == null) {
        return -1;
      }

      if (g1.getAuthority() == null) {
        return 1;
      }

      return g1.getAuthority().compareTo(g2.getAuthority());
    }
  }
} // end class LoginUser
