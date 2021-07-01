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

    <jsp:include page="../partials/admin-sidebar.jsp">
        <jsp:param name="currentSection" value="dashboard" />
    </jsp:include>

    <div class = "grid-x justify-center align-center" style = "margin-top:300px;">
        <div class="greyContainer">
            <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/bg-orange.png');">
            </div>
            <h1>Utenti Registrati</h1>
        </div>

        <div class="greyContainer">
            <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/bg-orange.png');">
            </div>
            <h1>N. Sezioni</h1>
        </div>

        <div class="greyContainer">
            <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/bg-orange.png');">
            </div>
            <h1>N. Posts</h1>
        </div>
    </div>
</body>
</html>
