<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--var userRoleRes = {--%>

  <%--titles: {--%>
    <%--createUser: '<fmt:message key="userRole.title.createUser" />',--%>
    <%--updateUser: '<fmt:message key="userRole.title.updateUser" />',--%>
    <%--deleteUser: '<fmt:message key="userRole.title.deleteUser" />',--%>
    <%--disableUser: '<fmt:message key="userRole.title.disableUser" />',--%>
    <%--enableUser: '<fmt:message key="userRole.title.enableUser" />'--%>
  <%--},--%>
  <%----%>
  <%--fields: {--%>
    <%--username: '<fmt:message key="userRole.field.username" />',--%>
    <%--password: '<fmt:message key="userRole.field.password" />',--%>
    <%--confirmPassword: '<fmt:message key="userRole.field.confirmPassword" />',--%>
    <%--passwordHint: '<fmt:message key="userRole.field.passwordHint" />',--%>
    <%--firstName: '<fmt:message key="userRole.field.firstName" />',--%>
    <%--lastName: '<fmt:message key="userRole.field.lastName" />',--%>
    <%--fullName: '<fmt:message key="userRole.field.fullName" />',--%>
    <%--email: '<fmt:message key="userRole.field.email" />',--%>
    <%--telephone: '<fmt:message key="userRole.field.telephone" />',--%>
    <%--telephone2: '<fmt:message key="userRole.field.telephone2" />',--%>
    <%--status: '<fmt:message key="userRole.field.status" />',--%>
    <%--enabled: '<fmt:message key="userRole.field.enabled" />',--%>
    <%--locked: '<fmt:message key="userRole.field.locked" />',--%>
    <%--disabled: '<fmt:message key="userRole.field.disabled" />'--%>
  <%--},--%>
  <%----%>
  <%--error: {--%>
    <%--newPasswordNotMatch: '<fmt:message key="userRole.error.newPasswordNotMatch" />'--%>
  <%--}--%>
<%--};--%>


var userRoleRes = {

  titles: {
    createUser: '创建新用户',
    updateUser: '更新用户',
    deleteUser: '删除用户',
    disableUser: '标记用户可用',
    enableUser: '标记用户不可用'
  },

  fields: {
    username: '用户名',
    password: '密码',
    confirmPassword: '确认密码',
    passwordHint: '密码提示',
    firstName: '姓',
    lastName: '名',
    fullName: '姓名',
    email: '电子邮件地址',
    telephone: '手机号码',
    telephone2: '备用手机号码',
    status: '密码提示',
    enabled: '状态',
    locked: '可用?',
    disabled: '不可用?'
  },

  error: {
    newPasswordNotMatch: '密码不匹配'
  }
};