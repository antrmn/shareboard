<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<div class="greyContainer grid-y-nw  align-center">
  <h3>About Community</h3>
  <h4> ${param.description}</h4>
  <h4> ${param.nFollowers} Membri</h4>
  <a class = "lightGreyButton roundButton" href = "${param.link}">Invia Contenuto</a>
</div>