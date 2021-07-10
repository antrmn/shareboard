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
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp">
        <jsp:param name="currentSection" value="Admin" />
    </jsp:include>

    <jsp:include page="../partials/admin-sidebar.jsp">
        <jsp:param name="currentSection" value="dashboard" />
    </jsp:include>

    <div class = "grid-x justify-center align-center" style = "margin-top: 150px;">
        <div class="dashboard-card">
            <div style = "padding-left: 30px;">
                <div style = "display:inline-block;">
                    <h1>${requestScope.count}</h1>
                    <h3>Utenti Registrati</h3>
                </div>
                <i class="fas fa-user-plus" style = "font-size: 80px; display:inline; color: #4295b5; float: right"></i>
            </div>
        </div>

        <div class="dashboard-card">
            <div style = "padding-left: 30px;">
                <div style = "display:inline-block;">
                    <h1>7</h1>
                    <h3>Visite</h3>
                </div>
                <i class="fas fa-user-plus" style = "font-size: 80px; display:inline; color: #4295b5; float: right"></i>
            </div>
        </div>
        <div class="dashboard-card">
            <div id="piechart"></div>
        </div>
        <div id="curve_chart" style="width: 900px; height: 500px"></div>
    </div>
</body>
</html>
