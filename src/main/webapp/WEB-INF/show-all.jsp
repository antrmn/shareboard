<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!DOCTYPE html>
<html>
<head><name>All posts</name>
</head>
<body>
<div>
    <c:set var="context" value="${pageContext.request.contextPath}" />
    <c:if test="${empty posts}">Nessun post.</c:if>
    <c:forEach items="${posts}" var="post">
        <div style="border: 1px solid #000000">
            <small>${post.author.username}         ${post.section.name}  ${post.stringCreationDate}<br></small>
            <a href="${context}/post?p=${post.id}"><strong>${post.title}<br></strong></a>
                ${post.content}<br>
            <small>${post.votes} voti, ${post.nComments} commenti, Voto: ${post.vote}</small>
        </div>
        <br>
    </c:forEach>
</div>
</body>
</html>