<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="partials/head.jsp">
    <jsp:param name="currentPage" value="${section.name}" />
  </jsp:include>
</head>
<body>
<jsp:include page="partials/navbar.jsp">
  <jsp:param name="isLogged" value="true" />
  <jsp:param name="currentSection" value="${section.name}" />
  <jsp:param name="userName" value="Testus" />
  <jsp:param name="userKarma" value="4316" />
</jsp:include>

<div id="body-container">
  <div id="left-container">
    <jsp:include page="partials/filter.jsp"/>
    <div id="post-container">

    </div>
  </div>

  <div id="right-container">
    <div class="greyContainer">
      ${section.name}
      ${section.description}
    </div>

    <div class="greyContainer">
      ShareBoard intende diventare un social media atto a promuovere lo scambio di idee e di informazioni tra persone che condividono gli stessi interessi. ShareBoard funge da “aggregatore di contenuti” mantenuto da utenti che collaborano condividendo post o interagendo con essi. L’obiettivo è quello di offrire un punto di riferimento per chiunque volesse condividere il proprio lavoro, una propria idea in rete o anche per interagire e stare in contatto con utenti con un certo interesse in comune
      <br> <br>
      Shareboard Inc © 2021. All rights reserved
    </div>
  </div>
</div>
</body>
</html>