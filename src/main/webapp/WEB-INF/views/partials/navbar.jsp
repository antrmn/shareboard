<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<navbar>
    <div id="left">
        <span id="nav-logo" >
            <a href = "${context}/home" style="display:inline-flex">
                <i class="fas fa-share-alt-square" style = "color: #ff4500; font-size: 25px;"></i>
                <h3>Shareboard</h3>
            </a>
        </span>
        <span id="nav-crt-sctn" class="interactable" onclick="toggleDropdown(true, 'section-dropdown')">
            <i class="fas fa-map-marker-alt" style = "color:#0079D3"></i>
            <span>
                <span>${param.currentSection}</span>
                <i class="fas fa-sort-down" ></i>
            </span>
            <div id="section-dropdown" class="dropdown-content greyContainer">
                <div style = "padding: 12px 16px; color: #77797a; font-size: 10px; font-weight: 500; line-height: 16px;text-transform: uppercase; ">Home Feeds</div>
                <a href="#home">Home</a>
                <a href="#home">Popular</a>
                <a href="#about">About</a>
                <div style = "padding: 12px 16px; color: #77797a; font-size: 10px; font-weight: 500; line-height: 16px;text-transform: uppercase; ">Sections</div>
                <c:forEach var = "i" begin = "1" end = "100">
                    <div style="margin-bottom:15px;">
                        <a href="#contact" style="display: inline; margin-bottom:10px;">Section</a>
                        <c:choose>
                            <c:when test="${param.isLogged eq 'true'}">
                                <i class="fas fa-star" style="float:right; margin-right: 10px; color:blue" onclick="toggleFavorite(this)"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="far fa-star" style="float:right; margin-right: 10px;" onclick="toggleFavorite(this)"></i>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>
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
                <span id = "profile-container" class = "interactable" href = "${pageContext.request.contextPath}/profile/?id=${requestScope.user_id}" onclick="toggleDropdown(true, 'profile-dropdown')" >
                    <i id = "nav-profile-photo" class="fas fa-user-circle"></i>
                    <div id="nav-profile-data" >
                        <p style="display: block; margin-bottom:0px; ">${param.userName}</p>
                        <div style="display: block; font-size: 12px; margin-top:0px;">
                            <i class="far fa-arrow-alt-circle-up" style="color: orangered; display: inline; margin-top:0px;"></i>
                            <p style="display: inline-block; font-size: 11px; margin-top:0px;">${param.userKarma} Karma</p>
                        </div>
                    </div>
                    <i class="fas fa-sort-down" style="display: inline-block;"></i>
                    <div id="profile-dropdown" class="dropdown-content greyContainer">
                        <a href="${context}/profile">
                            <i class="fas fa-address-card"></i>
                            Profile
                        </a>

<%--                     if is admin check--%>
                        <a href="${context}/admin">
                            <i class="fas fa-user-shield"></i>
                            Pannello Admin
                        </a>
                        <a href="${context}/logout">
                            <i class="fas fa-sign-out-alt"></i>
                            Log Out
                        </a>
                    </div>
                </span>
            </c:when>
            <c:otherwise>
                <div id = "button-container">
                    <a href= "${context}/login" class = "roundButton darkGreyButton">Log In</a>
                    <a href= "${context}/register" class = "roundButton lightGreyButton">Sign Up</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</navbar>