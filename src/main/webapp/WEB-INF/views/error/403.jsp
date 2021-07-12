<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <head>
        <jsp:include page="../partials/head.jsp">
            <jsp:param name="currentPage" value="Forbidden" />
            <jsp:param name="styles" value="" />
            <jsp:param name="scripts" value="" />
        </jsp:include>
    </head>
</head>
<body>
<div id="error-body">
    <div id="error-pane">
        <div id="error-code">
            <h1>403</h1>
        </div>
        <h2>Forbidden</h2>
        <p>Non disponi dei permessi necessari per accedere alla risorsa</p>
        <a href="${pageContext.request.contextPath}/">Torna alla homepage</a>
    </div>
</div>
</body>
</html>
