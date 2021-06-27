<%@ page import="model.user.User" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="partials/head.jsp">
        <jsp:param name="currentPage" value="Home" />
        <jsp:param name="scripts" value="home,post" />
    </jsp:include>
</head>
<body>
        <jsp:include page="/WEB-INF/navbar">
            <jsp:param name="currentSection" value="Home" />
            <jsp:param name="userName" value="${empty requestScope.loggedUser ? 'unlogged' : requestScope.loggedUser.username}" />
            <jsp:param name="userKarma" value="4316" />
        </jsp:include>

        <div id="body-container">
            <div id="left-container">
                <jsp:include page="partials/filter.jsp"/>
                <div id="post-container"></div>
            </div>
            <div id="right-container">
                <div class="greyContainer">
                    <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/bg-planet2.png'); height: 34px;
                            background-position-y: center;
                            background-position-x: center;"></div>
                    <c:choose>
                        <c:when test="${empty requestScope.loggedUser}">
                            <h4>Welcome on Shareboard!</h4>
                        </c:when>
                        <c:otherwise>
                            <h4>Welcome back ${requestScope.loggedUser.username}</h4>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="greyContainer">
                    <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/banner-background.png');height: 80px;">
                        <h2 class="card-img-text">Top Sections</h2>
                    </div>
                    <c:forEach var = "i" begin = "1" end = "5">
                        <div id = "top-sections-container" style = "display:flex; flex-direction: row; justify-content: space-between; align-content: center">
                            <i class="fas fa-globe-europe" style = "color:#0079D3; font-size: 25px"></i>
                            <p>TEXT</p>
                            <button type="submit" class = "roundButton lightGreyButton">Join</button>
                        </div>
                    </c:forEach>
                </div>
                <div class="greyContainer">
                    <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/bg-orange.png');">
                        <h2 class="card-img-text">Trending Sections</h2>
                    </div>
                    <c:forEach var = "i" begin = "1" end = "5">
                        <div id = "trending-sections-container" style = "display:flex; flex-direction: row; align-content: center; justify-content: space-between">
                            <span>
                                <i class="fas fa-caret-up" style = "display:inline; color:green;"></i>
                                <p style = "display:inline;">TEXT</p>
                            </span>
                        </div>
                    </c:forEach>
                </div>
                <jsp:include page="partials/footer.jsp"/>
            </div>
        </div>
</body>
</html>