<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
  <title></title>
  <link href="<c:url value='/favicon.ico'/>" rel="icon" type="image/x-icon"/>
  <link href="<c:url value='/favicon.ico'/>" rel="shortcut icon" type="image/x-icon"/>
  <script type="text/javascript">
    window.history.forward(1);
    if (window.opener) {
      window.opener.location.href = window.location.href;
    }
    if (window.parent && window.parent != window) {
      window.parent.location.href = window.location.href;
    }
  </script>
  <jwr:style src="/bundles/login.css"/>
</head>

<body>
<div id="content">
  <div id="login" class="login-panel">
    <div class="header pagodao" style="background-image: url('<c:url value="/scripts/login/resources/images/bg-loginbox-pagoda.jpg"/>')">
    </div>
    <div class="bg-loginform" style="background-image: url('<c:url value="/scripts/login/resources/images/bg-loginbox-main.jpg"/>')">
      <form id="loginForm" action="<c:url value="/security/check"/>" method="post">
        <c:if test="${not empty param.error}">
          <div class="valid-user">用户名与密码不匹配</div>
        </c:if>
        <input class="username" style="background-image: url('<c:url value="/scripts/login/resources/images/icon-user.png"/>')"
               name="j_username" type="text" placeholder="用户名" autocomplete="off" autofocus/>
        <input class="password" style="background-image: url('<c:url value="/scripts/login/resources/images/icon-password.png"/>')"
               name="j_password" type="password" placeholder="密码" autocomplete="off"/>

        <input class="btn-login" style="background-image: url('<c:url value="/scripts/login/resources/images/login-btn.jpg"/>')"
               type="submit" value="登陆"/>
      </form>
    </div>
    <div class="loginbox-footer" style="background-image: url('<c:url value="/scripts/login/resources/images/bg-loginbox-footer.jpg"/>')"></div>
  </div>
  <div class="shadow-loginbox" style="background-image: url('<c:url value="/scripts/login/resources/images/bg-loginbox-shadow.png"/>')"></div>
</div>
</body>
</html>