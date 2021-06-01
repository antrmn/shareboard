<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="partials/head.jsp">
        <jsp:param name="currentPage" value="Admin" />
    </jsp:include>
</head>
<body>
    <jsp:include page="partials/navbar.jsp">
        <jsp:param name="isLogged" value="true" />
        <jsp:param name="currentSection" value="Admin" />
        <jsp:param name="userName" value="Testus" />
        <jsp:param name="userKarma" value="4316" />
    </jsp:include>

    <div style = "display: flex; margin-top:300px; justify-content: center; align-items:center;">
        <div class="greyContainer" style = "display: flex;flex-direction: column">
            <a href="${context}/admin/create-section">Crea Sezione</a>
            <a>Gestione Utenti</a>
        </div>
    </div>
</body>
</html>
