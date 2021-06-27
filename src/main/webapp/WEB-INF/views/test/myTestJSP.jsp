<%@ taglib prefix="sb" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 26/06/2021
  Time: 16:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/views/partials/head.jsp">
        <jsp:param name="currentPage" value="loool" />
        <jsp:param name="scripts" value="home,post,normalize,section,style" />
    </jsp:include>
    <title>Title</title>
</head>
<body>
    <sb:printComments comments="${requestScope.comments}" idParent="${1}">
    <jsp:attribute name="commentFragment">
        <%@ include file="/WEB-INF/views/partials/comment.jsp" %>
    </jsp:attribute>
    </sb:printComments>
</body>
</html>
