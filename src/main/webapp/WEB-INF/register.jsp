<%--
  Created by IntelliJ IDEA.
  User: Zane
  Date: 12/05/2021
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="components/head.jsp">
    <jsp:param name="currentPage" value="Register" />
  </jsp:include>
</head>
<body>
  <jsp:include page="components/navbar.jsp">
    <jsp:param name="isLogged" value="false" />
    <jsp:param name="currentSection" value="Register" />
  </jsp:include>

  <div class="greyContainer">

</div>
</body>
</html>
