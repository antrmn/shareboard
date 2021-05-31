<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="partials/head.jsp">
        <jsp:param name="currentPage" value="Home" />
    </jsp:include>
</head>
<body>
        <jsp:include page="partials/navbar.jsp">
            <jsp:param name="isLogged" value="true" />
            <jsp:param name="currentSection" value="Home" />
            <jsp:param name="userName" value="Testus" />
            <jsp:param name="userKarma" value="4316" />
        </jsp:include>

        <div id="body-container">
            <div id="left-container">
                <jsp:include page="partials/filter.jsp"/>
                <div id="post-container">
                    <jsp:include page="partials/post.jsp">
                        <jsp:param name="title" value="Doge best doggo" />
                        <jsp:param name="author" value="Testus" />
                        <jsp:param name="section" value="best" />
                        <jsp:param name="comments" value="69" />
                        <jsp:param name="voteState" value="0" />
                        <jsp:param name="votes" value="22" />
                        <jsp:param name="type" value="img" />
                    </jsp:include>
                    <c:forEach var = "i" begin = "1" end = "100">
                        <jsp:include page="partials/post.jsp">
                            <jsp:param name="title" value="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor" />
                            <jsp:param name="author" value="Testus" />
                            <jsp:param name="section" value="best" />
                            <jsp:param name="comments" value="${i}" />
                            <jsp:param name="voteState" value="0" />
                            <jsp:param name="votes" value="${i}" />
                            <jsp:param name="type" value="text" />
                        </jsp:include>
                    </c:forEach>
                </div>

                <jsp:include page="partials/pagination.jsp">
                    <jsp:param name="current" value="1" />
                    <jsp:param name="start" value="1" />
                    <jsp:param name="end" value="6" />
                </jsp:include>
            </div>

            <div id="right-container">
                <div class="greyContainer">
                    <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/bg-planet2.png'); height: 34px;
                            background-position-y: center;
                            background-position-x: center;"></div>
                    <h4>Welcome back Testus!</h4>
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
                <div class="greyContainer">
                    ShareBoard intende diventare un social media atto a promuovere lo scambio di idee e di informazioni tra persone che condividono gli stessi interessi. ShareBoard funge da “aggregatore di contenuti” mantenuto da utenti che collaborano condividendo post o interagendo con essi. L’obiettivo è quello di offrire un punto di riferimento per chiunque volesse condividere il proprio lavoro, una propria idea in rete o anche per interagire e stare in contatto con utenti con un certo interesse in comune
                    <br> <br>
                    Shareboard Inc © 2021. All rights reserved
                </div>
            </div>
        </div>
<%--    <jsp:include page="components/footer.jsp"/>--%>
</body>
</html>