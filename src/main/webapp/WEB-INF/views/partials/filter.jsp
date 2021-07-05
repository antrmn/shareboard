<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="filter" class="greyContainer">
  <c:if test="${param.isHome == 'true'}">
    <a id="popular-button" href="javascript:void(0)"><i class="fas fa-rocket"></i>Popular</a>
  </c:if>
  <a id="top-button" href="javascript:void(0)"><i class="fas fa-burn"></i>Top</a>
  <a id="new-button" href="javascript:void(0)"><i class="fas fa-certificate"></i>New</a>
  <i class="fas fa-ellipsis-h" id = "filter-icon" style = "float:right;" onclick="toggleDropdown('toggle', 'filter-dropdown')">
    <div id="filter-dropdown" class="dropdown-content greyContainer">
      <a href="${pageContext.request.contextPath}/">
        Set Default
      </a>
    </div>
  </i>
</div>