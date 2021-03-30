<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<!DOCTYPE html>
<html>
<head><title>All posts</title>
</head>
<body>
<div>
    <jsp:useBean id="posts" scope="request" type="java.util.List"/>
    <c:if test="${empty posts}">Nessun post.</c:if>
    <c:forEach items="${posts}" var="post">
        <div style="border: 1px solid black">
            <strong>${post.title}<br></strong>
                ${post.text}<br>
        </div>
        <br>
    </c:forEach>
</div>
</body>
</html>