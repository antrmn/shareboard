<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form class = "comment-form" method = "POST" action= "${pageContext.request.contextPath}/newcomment" onsubmit="return validateCommentForm(this)">
    <input type="hidden" name = "id" value = "${param.id}">
    <input type="hidden" name = "parent" value = "0">
    <textarea class="dark-textarea" name = "text" rows="5"></textarea>
    <br>
    <button class = roundButton style = "margin-top:10px; margin-bottom:10px;">Invia</button>
</form>