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
        <div>
            <ul>
                <li>${post.title}</li>
                <li>${post.text}</li>
            </ul>
        </div>
    </c:forEach>
</div>
</body>
</html>