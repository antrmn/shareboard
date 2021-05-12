<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="components/head.jsp">
        <jsp:param name="currentPage" value="Home" />
    </jsp:include>
</head>
<body>
        <jsp:include page="components/navbar.jsp">
            <jsp:param name="isLogged" value="true" />
            <jsp:param name="currentSection" value="Home" />
            <jsp:param name="userName" value="Testus" />
            <jsp:param name="userKarma" value="4316" />
        </jsp:include>

        <div id="body-container">
            <div id="left-container">
                <jsp:include page="components/filter.jsp"/>
                <div id="post-container">
                    <jsp:include page="components/post.jsp">
                        <jsp:param name="title" value="Doge best doggo" />
                        <jsp:param name="author" value="Testus" />
                        <jsp:param name="section" value="best" />
                        <jsp:param name="comments" value="69" />
                        <jsp:param name="voteState" value="0" />
                        <jsp:param name="votes" value="22" />
                        <jsp:param name="type" value="img" />
                    </jsp:include>
                    <c:forEach var = "i" begin = "1" end = "100">
                        <jsp:include page="components/post.jsp">
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

                <jsp:include page="components/pagination.jsp">
                    <jsp:param name="current" value="1" />
                    <jsp:param name="start" value="1" />
                    <jsp:param name="end" value="6" />
                </jsp:include>
            </div>

            <div id="right-container">
                <div class="greyContainer" style = "margin-bottom: 20px">
                    <p>
                        ShareBoard intende diventare un social media atto a promuovere lo scambio di idee e di informazioni tra persone che condividono gli stessi interessi. ShareBoard funge da “aggregatore di contenuti” mantenuto da utenti che collaborano condividendo post o interagendo con essi. L’obiettivo è quello di offrire un punto di riferimento per chiunque volesse condividere il proprio lavoro, una propria idea in rete o anche per interagire e stare in contatto con utenti con un certo interesse in comune.
                    </p>
                </div>
                <div class="greyContainer" style = "margin-bottom: 20px">
                    <h4>Top Sections</h4>
                    <c:forEach var = "i" begin = "1" end = "5">
                        <div id = "top-sections-container" style = "display:flex; flex-direction: row; justify-content: center; align-content: center">
                            <i class="fas fa-globe-europe"></i>
                            <p>TEXT</p>
                            <button type="submit" class = "roundButton lightGreyButton">Join</button>
                        </div>
                    </c:forEach>
                </div>
                <div class="greyContainer" style = "margin-bottom: 20px">
                    <h4>Trending Sections</h4>
                    <c:forEach var = "i" begin = "1" end = "5">
                        <div id = "trending-sections-container" style = "display:flex; flex-direction: row; justify-content: center; align-content: center">
                            <i class="fas fa-globe-europe"></i>
                            <p>TEXT</p>
                            <button type="submit" class = "roundButton lightGreyButton">Join</button>
                        </div>
                    </c:forEach>
                </div>
<%--                <div class="greyContainer" onclick=""  >--%>
<%--                    Sponsored Section--%>
<%--                </div>--%>
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