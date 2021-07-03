<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="currentPage" value="Admin" />
    <jsp:param name="styles" value="admin" />
    <jsp:param name="scripts" value="admin" />
  </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/navbar">
  <jsp:param name="currentSection" value="Admin" />
</jsp:include>

<div class = "grid-y justify-center align-center" style = "margin-top: 150px;">
  <jsp:include page="../partials/admin-sidebar.jsp">
    <jsp:param name="currentSection" value="dashboard" />
  </jsp:include>
  <button class="lightGreyButton roundButton" onclick="openModal();">Aggiungi Ban</button>
  <table>
    <tr>
      <th>Id</th>
      <th>Sezione</th>
      <th>Data Inizio</th>
      <th>Data Fine</th>
      <th>Azioni</th>
    </tr>
    <c:forEach items="${requestScope.bans}" var="ban">
      <tr>
        <td>${ban.id}</td>
        <td>${ban.section.name}</td>
        <td>${ban.startTime}</td>
        <td>${ban.endTime}</td>
        <td>
          <a href = "${context}/admin/deletuser?userId=${ban.id}">
            <i class="fas fa-minus-circle"></i>
          </a>
        </td>
      </tr>
    </c:forEach>
  </table>
  <div id="myModal" class="modal">

    <!-- Modal content -->
    <div class="modal-content">
      <div class="modal-header">
        <span class="close" onclick="closeModal();">&times;</span>
        <h2>Aggiungi Ban</h2>
      </div>
      <div class="modal-body">
        <form method = "get" action = "${context}/admin/addban">
          <label for="start-date" style = "display: inline">Data Inizio:</label>
          <input type = "date" name="'start-date" id = "start-date">
          <label for="end-date" style = "display: inline">Data Fine:</label>
          <input type = "date" name="'end-date" id = "end-date">
          <label for="section-select" style = "display: inline">Scegli sezione:</label>
          <select name="section" id="section-select">
            <option value="all">Tutte</option>
            <c:forEach items="${applicationScope.sections}" var="section">
              <option value="${section.value.id}">${section.value.name}</option>
            </c:forEach>
          </select>
          <button class = "lightGreyButton roundButton">Aggiungi</button>
        </form>
      </div>
    </div>

  </div>
</div>
</body>
</html>