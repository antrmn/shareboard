<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<!DOCTYPE html>
<html>
<head><title>${post.title}</title>
</head>
<body>
<div>
    <jsp:useBean id="post" scope="request" type="java.util.List"/>
    <div style="border: 1px solid black">
        <small>${post.author.username}         ${post.category.name} <br></small>
        <strong>${post.title}<br></strong>
        ${post.text}<br>
        <small>${post.voti} voti, ${post.n_comments} commenti</small>
    </div>
    <br>

    <h3>Commenti</h3> <br>
    <c:forEach items="${post.comments}" var="comment">
        <div style="border: 1px solid black">
            <small>${comment.author.username}         ${comment.creation_date} <br></small>
                ${comment.text}<br>
            <small>${comment.votes} voti</small>
        </div>
        <br>
    </c:forEach>
</div>
</body>
</html>