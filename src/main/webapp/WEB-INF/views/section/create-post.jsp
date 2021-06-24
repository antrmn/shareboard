<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="currentPage" value="" />
    </jsp:include>
</head>
<body>
<jsp:include page="/navbar">
    <jsp:param name="isLogged" value="false" />
    <jsp:param name="currentSection" value="Create Post" />
</jsp:include>

<div style = "display: flex; margin-top:300px; justify-content: center; align-items:center;">
    <div class="greyContainer" style = "display: flex;">
        <img src="images/bg-planet.png">
        <div style="display: flex; justify-content: center; align-items:center; flex-direction: column; padding:100px 200px 100px 200px;">
            <h2>Create Post</h2>
            <ul>
                <c:if test = "${not empty requestScope.errors}">
                    <c:forEach items="${requestScope.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </c:if>
            </ul>
            <form id = "create-post-form" action="${pageContext.request.contextPath}/newpost" method="post" style="display: flex; justify-content: center; align-items:center; flex-direction: column;">
                <label for="section-select">Choose a section:</label>

                <select name="sections" id="section-select">
                    <c:forEach items="${applicationScope.sections}" var="section">
                        <option value="${section.value.id}">${section.value.name}</option>
                    </c:forEach>
                </select>
                <label for="title">Title:</label>
                <input type="text" id="title" name="title">
                <input type="radio" id="text-type" name="text" value="Text"
                       checked>
                <label for="img">img</label>
                <input type="radio" id="img-type" name="img" value="Image">
                <label for="text">text</label>
                <label for="content">Text:</label>
                <input type="text" id="content" name="content">

                <input type="submit" value="Send" class="roundButton">
            </form>
        </div>
    </div>
</div>
</body>
</html>
