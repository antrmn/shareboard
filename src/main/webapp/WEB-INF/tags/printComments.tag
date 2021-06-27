<%@ tag body-content="scriptless" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sb"%>
<%@ attribute name="comments" type="java.util.Map" required="true"%>
<%@ attribute name="idParent" type="java.lang.Integer" required="true" %>
<%@ attribute name="commentFragment" fragment="true"%>
<%@ variable name-given="comment" variable-class="model.comment.Comment" scope="NESTED" %>
<%@ variable name-given="childComments" variable-class="java.lang.String" scope="NESTED"%>

<c:forEach items="${comments[idParent]}" var="comment">
    <c:set var="childComments">
        <sb:printComments comments="${comments}" idParent="${comment.id}">
            <jsp:attribute name="commentFragment">
                <%@ include file="/WEB-INF/views/partials/comment.jsp" %>
            </jsp:attribute>
        </sb:printComments>
    </c:set>
    <jsp:invoke fragment="commentFragment"/>
</c:forEach>