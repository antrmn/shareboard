<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="partials/head.jsp">
        <jsp:param name="currentPage" value="Admin" />
    </jsp:include>
</head>
<body>

    <jsp:include page="partials/navbar.jsp">
        <jsp:param name="isLogged" value="true" />
        <jsp:param name="currentSection" value="Admin" />
        <jsp:param name="userName" value="Testus" />
        <jsp:param name="userKarma" value="4316" />
    </jsp:include>



    <div style = "display: flex; margin-top:300px; justify-content: center; align-items:center;">
        <div class="greyContainer" style = "display: flex;">
            <img src="../images/bg-planet.png">
            <div style="display: flex; justify-content: center; align-items:center; flex-direction: column; padding:100px 200px 100px 200px;">
                <h2>Crea Sezione</h2>

                <form id = "section-form" action="${context}/admin/create-section" method="post" style="display: flex; justify-content: center; align-items:center; flex-direction: column;">
                    <label for="fname">Nome:</label>
                    <input type="text" id="sectionName" name="sectionName">
                    <label for="fname">Descrizione:</label>
                    <input type="text" id="sectionDescr" name="sectionDescr">
                    <label for="lname">Icona:</label>
                    <input type="file" id="sectionIcon" name="sectionIcon">
                    <label for="lname">Banner:</label>
                    <input type ="file" id="sectionBanner" name="sectionBanner">
                    <button class = "roundButton darkGreyButton">Crea</button>
                </form>
            </div>
        </div>
    </div>

</body>
</html>
