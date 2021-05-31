<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="partials/head.jsp">
    <jsp:param name="currentPage" value="" />
  </jsp:include>
</head>
<body>
<jsp:include page="partials/navbar.jsp">
  <jsp:param name="isLogged" value="false" />
  <jsp:param name="currentSection" value="Login" />
</jsp:include>

<div style = "display: flex; margin-top:300px; justify-content: center; align-items:center;">
  <div class="greyContainer" style = "display: flex;">
    <img src="images/bg-planet.png">
    <div style="display: flex; justify-content: center; align-items:center; flex-direction: column; padding:100px 200px 100px 200px;">
      <h2>Login</h2>

      <form id = "register-form" action="/register" method="post" style="display: flex; justify-content: center; align-items:center; flex-direction: column;">
        <label for="fname">Email/Username:</label>
        <input type="mail" id="text" name="mail">
        <label for="lname">Password:</label>
        <input type="password" id="pass" name="pass">
        <input type="submit" value="Log In" class="roundButton">
      </form>
    </div>
  </div>
</div>
</body>
</html>
