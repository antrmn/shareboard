<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<navbar>
    <div id="left">
        <span id="nav-logo">
                <i class="fas fa-share-alt-square" style = "color: #0079D3;"></i>
                <h4>Shareboard</h4>
        </span>
        <span id="nav-crt-sctn">
            <i class="fas fa-map-marker-alt"></i>
            <c:out value="${param.currentSection}" />
        </span>

        <div id="nav-search">
            <form action="/search">
                 <i class="fas fa-search test"></i>
                <input type="text" placeholder="Search" name="search">
                <%--            <button type="submit"><i class="fa fa-search"></i></button>--%>
            </form>
        </div>
    </div>
    <div id="nav-profile">
        <c:choose>
            <c:when test="${param.isLogged eq 'true'}">
                <c:out value="${param.userName}" />
                <c:out value="${param.userKarma}" />
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