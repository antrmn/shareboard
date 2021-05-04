<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<navbar>
    <div id="left">
        <span id="nav-logo" >
            <a href = "${context}/home" style="display:inline-flex">
                <i class="fas fa-share-alt-square" style = "color: #0079D3;"></i>
                <h4>Shareboard</h4>
            </a>
        </span>
        <span id="nav-crt-sctn" style="display:inline-flex">
            <i class="fas fa-map-marker-alt"></i>
            <c:out value="${param.currentSection}" />
        </span>

        <div id="nav-search" >
            <form action="/search">
                 <i class="fas fa-search test"></i>
                <input type="text" placeholder="Search" name="search">
            </form>
        </div>
    </div>
    <div id="nav-profile">
        <c:choose>
            <c:when test="${param.isLogged eq 'true'}">
                <a id = "profile-container" href = "${pageContext.request.contextPath}/profile/?id=${requestScope.user_id}">
                    <i class="fas fa-user-circle"></i>
                    <c:out value="${param.userName}" />
                    <i class="fas fa-certificate" style = "color: orangered;"></i>
                    <c:out value="${param.userKarma}" />
                </a>
            </c:when>
            <c:otherwise>
                <div id = "button-container">
                    <button type="submit" class = "darkGreyButton">Log In</button>
                    <button type="submit" class = "lightGreyButton">Sign Up</button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</navbar>