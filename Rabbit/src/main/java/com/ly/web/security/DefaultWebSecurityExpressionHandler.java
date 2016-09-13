package com.ly.web.security;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;


/**
 * TODO: DOCUMENT ME!
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  09/13/2016 14:44
 */
public class DefaultWebSecurityExpressionHandler implements SecurityExpressionHandler<FilterInvocation> {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private ExpressionParser          expressionParser = new SpelExpressionParser();
  private RoleHierarchy             roleHierarchy;
  private WebSecurityExpressionRoot root;

  private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   authentication  DOCUMENT ME!
   * @param   fi              DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public EvaluationContext createEvaluationContext(
    Authentication authentication, FilterInvocation fi) {
    StandardEvaluationContext ctx = new StandardEvaluationContext();
    root.init(authentication, fi);
    root.setTrustResolver(trustResolver);
    root.setRoleHierarchy(roleHierarchy);
    ctx.setRootObject(root);

    return ctx;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  SecurityExpressionHandler#getExpressionParser()
   */
  @Override public ExpressionParser getExpressionParser() {
    return expressionParser;
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
   * @param  root  DOCUMENT ME!
   */
  public void setRoot(WebSecurityExpressionRoot root) {
    this.root = root;
  }
} // end class DefaultWebSecurityExpressionHandler
