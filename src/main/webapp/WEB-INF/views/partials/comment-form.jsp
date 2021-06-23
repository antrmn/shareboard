<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form class = "comment-form" method = "POST" action= "./newcomment" onsubmit="return validateCommentForm(this)">
    <input type="hidden" name = "id" value = "${param.id}">
    <input type="hidden" name = "parent" value = "0">
    <textarea name = "text" rows="5"></textarea>
    <br>
    <button class = roundButton>Invia</button>
</form>