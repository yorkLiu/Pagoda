<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="webapp.name"/> </title>
    <jsp:useBean id="now" class="java.util.Date" />

    <!-- load ext js --->
    <%--<link href="<c:url value='/scripts/ext/resources/css/ext-all.css'/>" rel="stylesheet" type="text/css"/>--%>
    <link href="<c:url value='/scripts/ext/resources/ext-theme-neptune/ext-theme-neptune-all-debug.css'/>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/scripts/desktop/css/desktop.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/scripts/shared/icons.css'/>" />
    <link href="<c:url value='/scripts/pagoda/css/pagoda.css'/>" rel="stylesheet" type="text/css"/>
    
    <script type="text/javascript" src="<c:url value='/scripts/ext/ext-all-debug.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/ext/packages/ext-theme-neptune/ext-theme-neptune.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/jscripts/jscriptRes.js?t=${now.time}"/>"></script>

    <script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>

    <%--<script type="text/javascript" src='<c:url value="/dwr/interface/(userRoleController:accountController:addressController).js"/>'></script>--%>
    <script type="text/javascript" src='<c:url value="/dwr/interface/userRoleController.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/dwr/interface/accountController.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/dwr/interface/addressController.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/dwr/interface/commissionController.js"/>'></script>

    <script type="text/javascript">
      var basePath = '<c:url value="/"/>';
      Ext.Loader.setConfig({
            enabled: true,
            basePath: '<c:url value="/scripts/ext/src"/>',
            disableCaching: true
          }
      );

      Ext.Loader.setPath({
        'Ext.ux.desktop': '<c:url value="/scripts/desktop/js"/>',
        'Ext': '<c:url value="/scripts/ext/src"/>',
         Pagoda: '<c:url value="/scripts/pagoda"/>',
         Pago: '<c:url value="/scripts/ux/Pago"/>'
        
      });

      Ext.require('Pagoda.App');

      var pagoda;
      Ext.onReady(function () {
        pagoda = new Pagoda.App();
      });

    </script>

  </head>
<body>

</body>
</html>