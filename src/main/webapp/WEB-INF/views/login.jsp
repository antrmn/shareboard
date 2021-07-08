<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="partials/head.jsp">
    <jsp:param name="currentPage" value="Login" />
  </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/navbar">
  <jsp:param name="currentSection" value="Login" />
</jsp:include>

<div class = "auth-body-container">
  <div class="greyContainer auth-container" >
    <img id = "user-auth-image" src="images/bg-planet.png">
    <div style="display: flex; justify-content: center; align-items:center; flex-direction: column; flex-grow:1;">
      <h2>Login</h2>
      <ul>
        <c:if test = "${not empty requestScope.errors}">
          <c:forEach items="${requestScope.errors}" var="error">
            <li>${error}</li>
          </c:forEach>
        </c:if>
      </ul>
      <form id = "login-form" action="${pageContext.request.contextPath}/login" method="post" style="display: flex; justify-content: center; align-items:center; flex-direction: column;">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="${fn:trim(fn:escapeXml(param.username))}">
        <label for="pass">Password:</label>
        <input type="password" id="pass" name="pass">
        <input type="submit" value="Log In" class="roundButton">
      </form>
    </div>
  </div>
</div>
</body>
</html>
