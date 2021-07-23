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
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
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
      <form class = "grid-y-nw align-center justify-center" id = "login-form" action="${pageContext.request.contextPath}/login" method="post">
        <label for="username">Username:</label>
        <input class = "auth-input-field" type="text" id="username" name="username" value="${fn:trim(fn:escapeXml(param.username))}" required maxlength="30" data-ttp-message = "Richiesto, Max: 30 char" onfocus="openTooltip(this);" onblur="closeTooltip(this);">
        <label for="pass">Password:</label>
        <input class = "auth-input-field" type="password" id="pass" name="pass" minlength="3" maxlength="255" required onfocus="openTooltip(this);" onblur="closeTooltip(this);"  data-ttp-message = "Min: 3 char, Max: 30 char">
        <input type="submit" value="Log In" class="roundButton" style = "margin-top:10px;">

        <span class = "auth-alternative-text">Non hai un account? <a href="./register" class="auth-alternative-link">Registrati</a></span>
        <div id="tooltip"></div>
      </form>
    </div>
  </div>
</div>
</body>
</html>
