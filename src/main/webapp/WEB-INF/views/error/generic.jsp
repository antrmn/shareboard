<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <head>
        <jsp:include page="../partials/head.jsp">
            <jsp:param name="currentPage" value="Errore" />
            <jsp:param name="styles" value="" />
            <jsp:param name="scripts" value="" />
        </jsp:include>
    </head>
</head>
<body>
<div id="error-body">
    <div id="error-pane">
        <div id="error-code">
            <h1>${pageContext.errorData.statusCode}</h1>
        </div>
        <a href="${pageContext.request.contextPath}/">Torna alla homepage</a>
    </div>
</div>
</body>
</html>
