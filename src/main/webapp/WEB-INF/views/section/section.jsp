<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="currentPage" value="${section.name}" />
    <jsp:param name="styles" value="section" />
    <jsp:param name="scripts" value="section,post,postloader,filter" />
  </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
  <jsp:param name="isLogged" value="true" />
  <jsp:param name="currentSection" value="${section.name}" />
  <jsp:param name="userName" value="Testus" />
  <jsp:param name="userKarma" value="4316" />
</jsp:include>

<div class = "grid-y-nw" style = "height:324px; left:0; right:0; position:relative; top:30px;">
  <div id = "header-image" style ='background: url("https://styles.redditmedia.com/t5_2zf9m/styles/bannerBackgroundImage_h8gepdvfwqb61.png?width=4000&s=ff692337d4b54b575ec3b23d32037b63d919f096") no-repeat scroll center center / cover;'>
    <a href="${pageContext.request.contextPath}" style = "height:inherit; width:100%; display: inline-block;"></a>
  </div>
  <div id="header-container" class = "grid-x-nw" >
    <span>
      <c:choose>
        <c:when test="${not empty section.picture}">
          <img id="header-icon" src= "${applicationScope.picsLocation}/${section.picture}" >
        </c:when>
        <c:otherwise>
          <img id = "header-icon" src="${pageContext.request.contextPath}/images/default-logo.png">
        </c:otherwise>
      </c:choose>
    </span>
    <span class = "grid-y">
      <h2>${section.name}</h2>
      <h4>s/${section.name}</h4>
    </span>
    <span>
      <button class = "darkGreyButton roundButton">Joined</button>
    </span>
  </div>
</div>

<div id="body-container">
  <div id="left-container" class="selected-container">
    <jsp:include page="../partials/filter.jsp">
      <jsp:param name="isHome" value="false"/>
    </jsp:include>
    <div id="post-container" section="${section.name}">
        <%-- vedi section.js --%>
    </div>
    <div id="posts-delimiter"></div>
  </div>

  <div id="right-container">
    <jsp:include page="../partials/section-info.jsp">
      <jsp:param name="description" value="${section.description}" />
      <jsp:param name="nFollowers" value="${section.nFollowersTotal}" />
      <jsp:param name="sectionId" value="${section.id}" />
    </jsp:include>
    <jsp:include page="../partials/rules.jsp"/>
    <jsp:include page="../partials/footer.jsp"/>
  </div>
</div>
</body>
</html>