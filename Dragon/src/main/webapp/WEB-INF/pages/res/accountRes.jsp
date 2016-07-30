
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

var accountRes = {
  fields: {
    username: '<fmt:message key="account.field.username" />',
    password: '<fmt:message key="account.field.password" />',
    accountLevel: '<fmt:message key="account.field.accountLevel" />',
    categoryType: '<fmt:message key="account.field.categoryType" />',
    locked: '<fmt:message key="account.field.locked" />',
    disabledDescription: '<fmt:message key="account.field.disabledDescription" />'
  },

  title: {
    createAccount: '<fmt:message key="account.title.createAccount" />',
    updateAccount: '<fmt:message key="account.title.updateAccount" />',
    deleteAccount: '<fmt:message key="account.title.deleteAccount" />'
  },

  message:{
    deleteAccount: '<fmt:message key="account.message.deleteAccount" />'

  }
};
