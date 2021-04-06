<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<jsp:useBean id="post" scope="request" type="model.Post"/>
<!DOCTYPE html>
<html>
<head><title>${post.title}</title>
</head>
<body>
<div>
    <div style="border: 1px solid black">
        <small>${post.author.username}         ${post.category.name} <br></small>
        <strong>${post.title}<br></strong>
        ${post.text}<br>
        <small>${post.voti} voti, ${post.n_comments} commenti</small>
    </div>
    <br>

    <h3>Commenti</h3>
    <c:forEach items="${post.comments}" var="comment">
        <div style="border: 1px solid black">
            <small>${comment.author.username}         ${comment.creationDate} <br></small>
                ${comment.text}<br>
            <small>${comment.votes} voti</small>
        </div>
        <br>
    </c:forEach>
</div>
</body>
</html>