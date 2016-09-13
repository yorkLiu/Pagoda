package com.ly.web.security;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.FilterInvocation;

import org.springframework.util.StringUtils;

import com.ly.service.RoleService;


/**
 * TODO: DOCUMENT ME!
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/13/2016 14:42
 */
public class WebSecurityExpressionRoot {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** Allows "denyAll" expression. */
  public final boolean denyAll = false;

  /** Allows "permitAll" expression. */
  public final boolean permitAll = true;

  // private FilterInvocation filterInvocation;
  /** Allows direct access to the request object. */
  public HttpServletRequest request;
  private Authentication    authentication;
  private RoleHierarchy     roleHierarchy;
  private Set<String>       roles;

  @Autowired private RoleService roleService;

  private AuthenticationTrustResolver trustResolver;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new WebSecurityExpressionRoot object.
   */
  public WebSecurityExpressionRoot() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean denyAll() {
    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final Authentication getAuthentication() {
    return authentication;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Object getPrincipal() {
    return authentication.getPrincipal();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   features  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean hasAnyFeature(String... features) {
    Set<String> roleSet = getAuthoritySet();

    for (String feature : features) {
      if (roleService.hasFeature(roleSet, feature)) {
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   roles  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean hasAnyRole(String... roles) {
    Set<String> roleSet = getAuthoritySet();

    for (String role : roles) {
      if (roleSet.contains(role)) {
        return true;
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   feature  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean hasFeature(String feature) {
    return roleService.hasFeature(getAuthoritySet(), feature);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Takes a specific IP address or a range using the IP/Netmask (e.g. 192.168.1.0/24 or 202.24.0.0/14).
   *
   * @param   ipAddress  the address or range of addresses from which the request must come.
   *
   * @return  true if the IP address of the current request is in the required range.
   *
   * @throws  IllegalArgumentException  DOCUMENT ME!
   */
  public boolean hasIpAddress(String ipAddress) {
    int nMaskBits = 0;

    if (ipAddress.indexOf('/') > 0) {
      String[] addressAndMask = StringUtils.split(ipAddress, "/");
      ipAddress = addressAndMask[0];
      nMaskBits = Integer.parseInt(addressAndMask[1]);
    }

    InetAddress requiredAddress = parseAddress(ipAddress);
    InetAddress remoteAddress   = parseAddress(request.getRemoteAddr());

    if (!requiredAddress.getClass().equals(remoteAddress.getClass())) {
      throw new IllegalArgumentException(
        "IP Address in expression must be the same type as "
        + "version returned by request");
    }

    if (nMaskBits == 0) {
      return remoteAddress.equals(requiredAddress);
    }

    byte[] remAddr = remoteAddress.getAddress();
    byte[] reqAddr = requiredAddress.getAddress();

    int    oddBits    = nMaskBits % 8;
    int    nMaskBytes = (nMaskBits / 8) + ((oddBits == 0) ? 0 : 1);
    byte[] mask       = new byte[nMaskBytes];

    Arrays.fill(mask, 0, (oddBits == 0) ? mask.length : (mask.length - 1), (byte) 0xFF);

    if (oddBits != 0) {
      int finalByte = (1 << oddBits) - 1;
      finalByte             <<= 8 - oddBits;
      mask[mask.length - 1] = (byte) finalByte;
    }

    // System.out.println("Mask is " + new sun.misc.HexDumpEncoder().encode(mask));

    for (int i = 0; i < mask.length; i++) {
      if ((remAddr[i] & mask[i]) != (reqAddr[i] & mask[i])) {
        return false;
      }
    }

    return true;
  } // end method hasIpAddress

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   role  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean hasRole(String role) {
    return getAuthoritySet().contains(role);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   a   DOCUMENT ME!
   * @param   fi  DOCUMENT ME!
   *
   * @throws  IllegalArgumentException  DOCUMENT ME!
   */
  public void init(Authentication a, FilterInvocation fi) {
    if (a == null) {
      throw new IllegalArgumentException(
        "Authentication object cannot be null");
    }

    this.authentication = a;

    this.request = fi.getRequest();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean isAnonymous() {
    return trustResolver.isAnonymous(authentication);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean isAuthenticated() {
    return !isAnonymous();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean isFullyAuthenticated() {
    return !trustResolver.isAnonymous(authentication)
      && !trustResolver.isRememberMe(authentication);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean isRememberMe() {
    return trustResolver.isRememberMe(authentication);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final boolean permitAll() {
    return true;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  roleHierarchy  DOCUMENT ME!
   */
  public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
    this.roleHierarchy = roleHierarchy;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  roleService  DOCUMENT ME!
   */
  public void setRoleManager(RoleService roleService) {
    this.roleService = roleService;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  trustResolver  DOCUMENT ME!
   */
  public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
    this.trustResolver = trustResolver;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private Set<String> getAuthoritySet() {
// if (roles == null) {
    roles = new HashSet<String>();

    Collection<GrantedAuthority> userAuthorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

    if (roleHierarchy != null) {
      userAuthorities = (Collection<GrantedAuthority>) roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
    }

    roles = AuthorityUtils.authorityListToSet(userAuthorities);
// }

    return roles;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private InetAddress parseAddress(String address) {
    try {
      return InetAddress.getByName(address);
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException("Failed to parse address"
        + address, e);
    }
  }


} // end class WebSecurityExpressionRoot
