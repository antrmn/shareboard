<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<div class="greyContainer grid-y-nw  align-center" style=" word-break: break-word;">
  <h3>About Community</h3>
  <h4 style=" word-break: break-word; text-align: center;"> ${empty param.description ? "Nessuna descrizione." : param.description}</h4>
  <h4> ${param.nFollowers} Membri</h4>
  <a class = "lightGreyButton roundButton" href = "${context}/newpost?section=${param.sectionId}" style = "margin-bottom: 15px;">Invia Contenuto</a>
</div>