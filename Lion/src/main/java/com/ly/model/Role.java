package com.ly.model;

import java.io.Serializable;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.util.StringUtils;

import com.ly.model.base.CreatorObject;


/**
 * Created by yongliu on 9/12/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 16:42
 */
@Entity
@Table(name = "Role")
public class Role extends CreatorObject implements Serializable, GrantedAuthority {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 2097140083063047919L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Column(length = 255)
  private String description;

  @Column(
    length   = 255,
    nullable = false
  )
  private String displayName;

  @Type(type = "yes_no")
  private Boolean enable;

  @Cascade({ CascadeType.ALL, CascadeType.REMOVE })
  @OneToMany(
    fetch    = FetchType.LAZY,
    mappedBy = "role"
  )
  private Set<RoleFeature> features = new LinkedHashSet<RoleFeature>();
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long         id;

  @Column(
    length   = 255,
    nullable = false
  )
  private String name;

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

    Role role = (Role) o;

    if ((description != null) ? (!description.equals(role.description)) : (role.description != null)) {
      return false;
    }

    if ((displayName != null) ? (!displayName.equals(role.displayName)) : (role.displayName != null)) {
      return false;
    }

    if ((enable != null) ? (!enable.equals(role.enable)) : (role.enable != null)) {
      return false;
    }

    return !((name != null) ? (!name.equals(role.name)) : (role.name != null));

  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.security.core.GrantedAuthority#getAuthority()
   */
  @Override public String getAuthority() {
    return getName();
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
   * getter method for display name.
   *
   * @return  String
   */
  public String getDisplayName() {
    return displayName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for enable.
   *
   * @return  Boolean
   */
  public Boolean getEnable() {
    return enable;
  }

  //~ ------------------------------------------------------------------------------------------------------------------


  /**
   * getter method for features.
   *
   * @return  Set
   */
  public Set<RoleFeature> getFeatures() {
    return features;
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
   * getter method for name.
   *
   * @return  String
   */
  public String getName() {
    return name;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * hasFeature.
   *
   * @param   featureName  String
   *
   * @return  boolean
   */
  public boolean hasFeature(String featureName) {
    if ((features == null) || !StringUtils.hasText(featureName)
          || (features.size() <= 0)) {
      return false;
    }

    for (RoleFeature feature : features) {
      if (featureName.equalsIgnoreCase(
              feature.getFeature().getFeatureName())) {
        return true;
      }
    }

    return false;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    int result = (description != null) ? description.hashCode() : 0;
    result = (31 * result) + ((displayName != null) ? displayName.hashCode() : 0);
    result = (31 * result) + ((enable != null) ? enable.hashCode() : 0);
    result = (31 * result) + ((name != null) ? name.hashCode() : 0);

    return result;
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
   * setter method for display name.
   *
   * @param  displayName  String
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
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
   * setter method for features.
   *
   * @param  features  Set
   */
  public void setFeatures(Set<RoleFeature> features) {
    this.features = features;
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
   * setter method for name.
   *
   * @param  name  String
   */
  public void setName(String name) {
    this.name = name;
  }
} // end class Role
