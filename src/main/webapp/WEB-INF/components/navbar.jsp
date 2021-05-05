<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<navbar>
    <div id="left">
        <span id="nav-logo" >
            <a href = "${context}/home" style="display:inline-flex">
                <i class="fas fa-share-alt-square" style = "color: #0079D3; font-size: 25px;"></i>
                <h3>Shareboard</h3>
            </a>
        </span>
        <span id="nav-crt-sctn" style="display:inline-flex" onclick="openSectionDrodown()">
            <i class="fas fa-map-marker-alt"></i>
            <span>${param.currentSection}</span>
            <i class="fas fa-sort-down" ></i>
        </span>

        <div id="nav-search" >
            <form action="/search">
                 <i id = "search-icon"class="fas fa-search"></i>
                <input type="text" placeholder="Search" name="search">
            </form>
        </div>
    </div>
    <div id="nav-profile">
        <c:choose>
            <c:when test="${param.isLogged eq 'true'}">
                <a href = "/create"style="margin-right: 20px;"><i class="fas fa-edit"></i></a>
                <a id = "profile-container" href = "${pageContext.request.contextPath}/profile/?id=${requestScope.user_id}"  >
                    <i id = "nav-profile-photo" class="fas fa-user-circle"></i>
                    <div id="nav-profile-data" >
                        <p style="display: block; margin-bottom:0px; ">${param.userName}</p>
                        <div style="display: block; font-size: 12px; margin-top:0px;">
                            <i class="far fa-arrow-alt-circle-up" style="color: orangered; display: inline; margin-top:0px;"></i>
                            <p style="display: inline-block; font-size: 11px; margin-top:0px;">${param.userKarma} Karma</p>
                        </div>
                    </div>
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