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
                    <jsp:include page="components/post.jsp"/>
                    <jsp:include page="components/post.jsp"/>
                    <jsp:include page="components/post.jsp"/>
                    <jsp:include page="components/post.jsp"/>
                </div>
            </div>

            <div id="right-container">
                <div class="greyContainer">
                    <p>
                        Shareboard è un sito bello, non puoi dire che è brutto
                    </p>
                </div>
                <div class="greyContainer">lorem</div>
<%--                <jsp:include page="components/post.jsp"/>--%>
<%--                <jsp:include page="components/post.jsp"/>--%>
            </div>
        </div>
<%--    <jsp:include page="components/footer.jsp"/>--%>
</body>
</html>