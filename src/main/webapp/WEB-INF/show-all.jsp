<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!DOCTYPE html>
<html>
<head><title>All posts</title>
</head>
<body>
<div>
    <jsp:useBean id="posts" scope="request" type="java.util.List"/>
    <c:set var="context" value="${pageContext.request.contextPath}" />
    <c:if test="${empty posts}">Nessun post.</c:if>
    <c:forEach items="${posts}" var="post">
        <div style="border: 1px solid #000000">
            <small>${post.author.username}         ${post.category.name} <br></small>
            <a href="${context}/post?p=${post.id}"><strong>${post.title}<br></strong></a>
                ${post.text}<br>
            <small>${post.voti} voti, ${post.n_comments} commenti</small>
        </div>
        <br>
    </c:forEach>
</div>
</body>
</html>