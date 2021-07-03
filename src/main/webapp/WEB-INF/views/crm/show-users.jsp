<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="currentPage" value="Admin" />
        <jsp:param name="styles" value="admin" />
        <jsp:param name="scripts" value="admin" />
    </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/navbar">
    <jsp:param name="currentSection" value="Admin" />
</jsp:include>

<div class = "grid-y justify-center align-center" style = "margin-top: 150px;">
    <jsp:include page="../partials/admin-sidebar.jsp">
        <jsp:param name="currentSection" value="dashboard" />
    </jsp:include>
    <table>
        <tr>
            <th>Id</th>
            <th>Nome</th>
            <th>Azioni</th>
        </tr>
        <c:forEach items="${requestScope.users}" var="user">
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>
                    <a href = "${context}/admin/toggleadmin?userId=${user.id}">
                        <i class="fas fa-edit"></i>
                    </a>
                    <a href = "${context}/admin/addban?userId=${user.id}">
                        <i class="fas fa-ban"></i>
                    </a>
                    <a href = "${context}/admin/deletuser?userId=${user.id}">
                        <i class="fas fa-minus-circle"></i>
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<%--</div>--%>
</body>
</html>
