<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--var globalRes = {--%>

  <%--fields: {--%>
    <%--creteDate: '<fmt:message key="global.field.createDate" />',--%>
    <%--lastUpdateDate: '<fmt:message key="global.field.lastUpdateDate" />',--%>
    <%--creator: '<fmt:message key="global.field.creator" />',--%>
    <%--lastUpdater: '<fmt:message key="global.field.lastUpdater" />'--%>
  <%--},--%>
  <%----%>
  <%--buttons: {--%>
    <%--save: '<fmt:message key="global.button.save" />',--%>
    <%--cancel: '<fmt:message key="global.button.cancel" />',--%>
    <%--create: '<fmt:message key="global.button.create" />',--%>
    <%--update: '<fmt:message key="global.button.update" />',--%>
    <%--delete: '<fmt:message key="global.button.delete" />',--%>
    <%--enable: '<fmt:message key="global.button.enable" />',--%>
    <%--disable: '<fmt:message key="global.button.disable" />'--%>
  <%--},--%>
  <%----%>
  <%--title:{--%>
    <%--confirmClose: '<fmt:message key="global.title.confirmClose" />',--%>
  <%--},--%>
  <%----%>
  <%--message: {--%>
    <%--confirmClose: '<fmt:message key="global.message.confirmClose" />',--%>
  <%--},--%>
  <%----%>
  <%--exception: {--%>
    <%--title: '<fmt:message key="global.exception.title" />',--%>
    <%--message: '<fmt:message key="global.exception.message" />'--%>
  <%--}--%>
<%--};--%>

var accessRes  =[]

var globalRes = {

  fields: {
    creteDate: '创建时间',
    lastUpdateDate: '最后更新时间',
    creator: '创建人',
    lastUpdater: '最后更新人'
  },

  buttons: {
    save: '保存',
    cancel: '取消',
    create: '新建',
    update: '更新',
    delete: '删除',
    enable: '标记为可用',
    disable: '标记为不可用',
    publish: '发布',
    unpublish: '撤销'
  },

  title:{
    confirmClose: '关闭当前窗口',
    markReadOny: '{0}<span style="color:red">[只读]</span>'
  },
  
  message: {
    confirmClose: '你确定要关闭当前窗口吗?'
  },

  exception: {
    title: '远程异常',
    message: '出错了,请联系管理员.'
  }
};
