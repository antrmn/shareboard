<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Questo partial jsp accetta 2 parametri forniti dall'utente:
     1. view ('popular' o 'following') [ha effetto solo nella home]
     2. sort ('new' o 'top')
--%>

<c:set var="isViewSet" value="${param.view == 'popular' || param.view == 'following'}"/>
<c:set var="isSortSet" value="${param.sort == 'top' || param.sort == 'new'}"/>

<div id="filter" class="greyContainer grid-x-nw" ${isViewSet ? "filterset" : ""} ${isSortSet ? "orderbyset" : ""} style = "height:50px;">
  <c:if test="${param.isHome == 'true'}">
    <a id="popular-button" href="javascript:void(0)" class="${param.view == 'popular' ? 'selected' : ''}"><i class="fas fa-rocket" style = "display:unset;"></i>All</a>
  </c:if>
  <a id="top-button" href="javascript:void(0)" class="${param.sort == 'top' ? 'selected' : ''}"><i class="fas fa-burn" style = "display:unset;"></i>Top</a>
  <a id="new-button" href="javascript:void(0)" class="${param.sort == 'new' ? 'selected' : ''}"><i class="fas fa-certificate" style = "display:unset;"></i>New</a>
  <i class="fas fa-ellipsis-h" id = "filter-icon" style = "margin-left: auto;" onclick="toggleDropdown('toggle', 'filter-dropdown')">
    <div id="filter-dropdown" class="dropdown-content greyContainer">
      <a id="set-default-button" href="javascript:void(0)">
        Set Default
      </a>
    </div>
  </i>
</div>