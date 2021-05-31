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
    <jsp:param name="currentSection" value="Register" />
  </jsp:include>

  <div style = "display: flex; margin-top:300px; justify-content: center; align-items:center;">
    <div class="greyContainer" style = "display: flex;">
      <img src="images/bg-planet.png">
      <div style="display: flex; justify-content: center; align-items:center; flex-direction: column; padding:100px 200px 100px 200px;">
        <h2>Register</h2>

        <form id = "register-form" action="/register" method="post" style="display: flex; justify-content: center; align-items:center; flex-direction: column;">
          <label for="fname">Email:</label>
          <input type="mail" id="mail" name="mail" value="example@gmail.com">
          <label for="fname">Username:</label>
          <input type="test" id="username" name="username">
          <label for="lname">Password:</label>
          <input type="password" id="pass" name="pass">
          <label for="lname">Confirm Password:</label>
          <input type="password" id="pass2" name="pass2">
          <input type="submit" value="Sign Up" class="roundButton">
        </form>
      </div>
    </div>
  </div>
</body>
</html>
