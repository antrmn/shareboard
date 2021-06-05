<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<div class="greyContainer grid-y-nw  align-center">
  <h3>About Community</h3>
  <h5> ${params.description}</h5>
  <h5> ${params.nFollowers} membri</h5>
  <a class = "lightGreyButton roundButton">Invia Contenuto</a>
</div>