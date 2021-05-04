<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="components/head.jsp"/>
</head>
<body>
    <div id = "test">
        <jsp:include page="components/navbar.jsp">
            <jsp:param name="isLogged" value="false" />
            <jsp:param name="currentSection" value="Home" />
            <jsp:param name="userName" value="Testus" />
            <jsp:param name="userKarma" value="4316" />
        </jsp:include>

        <div id="container">
            <jsp:include page="components/leftsidebar.jsp"/>
            <jsp:include page="components/postcontainer.jsp"/>
        </div>
    </div>
</body>
</html>