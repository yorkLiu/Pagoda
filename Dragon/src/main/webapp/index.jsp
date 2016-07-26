<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatNumber value="${now.time}" var="sn"/>
<%--<c:redirect url="/html/desktop?xsf=SnaPadoPws${sn}"/>--%>
<c:redirect url="/html/desktop?pgd=SnaPadoPws${now.time}"/>
