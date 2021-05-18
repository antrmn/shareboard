<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<div class="pagination">
  <a href="#">&laquo;</a>
  <c:forEach var = "i" begin = "${param.start}" end = "${param.end}">
    <c:choose>
      <c:when test="${param.start eq i}">
        <a href="#" class="active">${i}</a>
      </c:when>
      <c:otherwise>
        <a href="#">${i}</a>
      </c:otherwise>
    </c:choose>
  </c:forEach>
  <a href="#">&raquo;</a>
</div>
