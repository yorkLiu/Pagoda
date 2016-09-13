package com.ly.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ly.model.base.BaseObject;


/**
 * Created by yongliu on 9/12/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/12/2016 16:49
 */
@Entity
@Table(name = "RoleFeature")
public class RoleFeature extends BaseObject implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -5055107147139849045L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  @JoinColumn(
    name     = "featureId",
    nullable = false,
    insertable = true,
    updatable = true
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private Feature feature;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id private Long id;

  @JoinColumn(
    name     = "roleId",
    nullable = false,
    insertable = true,
    updatable = true
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private Role role;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for feature.
   *
   * @return  Feature
   */
  public Feature getFeature() {
    return feature;
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
   * getter method for role.
   *
   * @return  Role
   */
  public Role getRole() {
    return role;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for feature.
   *
   * @param  feature  Feature
   */
  public void setFeature(Feature feature) {
    this.feature = feature;
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
   * setter method for role.
   *
   * @param  role  Role
   */
  public void setRole(Role role) {
    this.role = role;
  }
} // end class RoleFeature
