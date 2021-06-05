<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>

<c:forEach items="${posts}" var="post">
  <jsp:include page="../partials/post-preview.jsp">
    <jsp:param name="title" value="${post.title}" />
    <jsp:param name="author" value="${post.title}" />
    <jsp:param name="section" value="${post.title}" />
  </jsp:include>
</c:forEach>