<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="loggedUser" value="${requestScope.loggedUser}" />
<navbar>
    <div id="left">

        <span id="nav-logo" >
            <a href = "${context}/home" style="display:inline-flex">
                <i class="fas fa-share-alt-square" style = "color: #ff4500; font-size: 25px;"></i>
                <h3>Shareboard</h3>
            </a>
        </span>
        <span id="nav-crt-sctn" class="interactable" onclick="toggleDropdown('toggle', 'section-dropdown')">
            <i class="fas fa-map-marker-alt" style = "color:#0079D3"></i>
            <span>
                <span>${param.currentSection}</span>
                <i class="fas fa-sort-down" ></i>
            </span>
            <div id="section-dropdown" class="dropdown-content greyContainer">
                <div style = "padding: 12px 16px; color: #77797a; font-size: 10px; font-weight: 500; line-height: 16px;text-transform: uppercase; ">Home Feeds</div>
                    <a class = "section-element" href="${context}/home">Home</a>
                    <a class = "section-element" href="${context}/home?order=popular">Popular</a>
                    <a class = "section-element" href="${context}/home?order=new">New</a>
                <div style = "padding: 12px 16px; color: #77797a; font-size: 10px; font-weight: 500; line-height: 16px;text-transform: uppercase; ">Sections</div>

                <div id = "section-container">
                    <c:forEach items="${applicationScope.sections}" var="section">
                        <div class = "section-element" style=" padding: 12px 0px 12px 0px;">
                            <a href="${context}/s?section=${section.value.name}" style="display: inline;">${section.value.name}</a>
                            <input type = "hidden" name = "sectionId" value = "${section.value.id}">
                            <c:set var="contains" value="false" />
                            <c:forEach var="item" items="${follows}">
                                <c:if test="${item eq section.value.id}">
                                    <c:set var="contains" value="true" />
                                </c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${contains eq true}">
                                    <i class="fas fa-star star favorite-star" onclick="toggleFollowStar(this)"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="far fa-star star" onclick="toggleFollowStar(this)"></i>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </span>

        <div id="nav-search" >
            <form action="${pageContext.request.contextPath}/search">
                 <button type="submit" class="fabutton"><i id = "search-icon" class="fas fa-search"></i></button>
                <input type="text" placeholder="Search" name="content">
            </form>
        </div>
    </div>
    <div id="nav-profile">
        <c:choose>
            <c:when test="${not empty loggedUser}">
                <a href = "${context}/newpost"style="margin-right: 20px;"><i class="fas fa-edit"></i></a>
                <span id = "profile-container" class = "interactable" href = "${context}/profile/?id=${loggedUser.id}" onclick="toggleDropdown('toggle', 'profile-dropdown')" >
                    <i id = "nav-profile-photo" class="fas fa-user-circle"></i>
                    <div id="nav-profile-data" >
                        <p style="display: block; margin-bottom:0px; ">${loggedUser.username}</p>
                        <div style="display: block; font-size: 12px; margin-top:0px;">
                            <i class="far fa-arrow-alt-circle-up" style="color: orangered; display: inline; margin-top:0px;"></i>
                        </div>
                    </div>
                    <i class="fas fa-sort-down" style="display: inline-block;"></i>
                    <div id="profile-dropdown" class="dropdown-content greyContainer">
                        <a href="${context}/user?name=${loggedUser.username}">
                            <i class="fas fa-address-card"></i>
                            Profile
                        </a>
                        <a href="${context}/edituser?id=${loggedUser.id}">
                            <i class="fas fa-sliders-h"></i>
                            Edit profile
                        </a>

                    <c:if test="${loggedUser.admin.booleanValue() == true}">
                        <a href="${context}/admin">
                            <i class="fas fa-user-shield"></i>
                            Pannello Admin
                        </a>
                    </c:if>

                        <a href="${context}/logout">
                            <i class="fas fa-sign-out-alt"></i>
                            Log Out
                        </a>
                    </div>
                </span>
            </c:when>
            <c:otherwise>
                <div id = "button-container">
                    <a id = "login-button" href= "${context}/login" class = "roundButton darkGreyButton">Log In</a>
                    <a id = "register-button" href= "${context}/register" class = "roundButton lightGreyButton">Sign Up</a>
                </div>

                <span id = "profile-container" class = "interactable hide" onclick="toggleDropdown('toggle', 'right-dropdown')" >
                    <i class="fas fa-user-circle nav-right-dropdown"></i>
                    <div id="right-dropdown" class="dropdown-content greyContainer">
                        <a href="${context}/login">
                           <i class="fas fa-sign-in-alt"></i>
                            Login
                        </a>

                        <a href="${context}/register">
                            <i class="fas fa-user-plus"></i>
                            Register
                        </a>
                    </div>
                </span>
            </c:otherwise>
        </c:choose>
        <div id="container-switcher">☰</div>
    </div>
</navbar>