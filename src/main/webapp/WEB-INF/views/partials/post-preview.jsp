<%@ page import="model.post.Post" %>
<%@ page import="util.InstantFormatter" %><%--@elvariable id="post" type="model.post.Post"--%>
<%-- Quello sopra è un commento di IntelliJ che permette di ignorare l'errore "cannot resolve variable" e
     fornisce l'auto-complete anche se l'oggetto non è presente (Ancora) in nessuno scope --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="post greyContainer interactable">
        <span class = "vote-container">
            <input type = "hidden" name = "id" value = ${post.id}>
            <i class="fas fa-chevron-up voteIcon upvoteIcon interactable ${post.vote == 1 ? "upvote-icon-active" : ""}" onclick = "toggleVote(this, 'upvote', 'post')"></i>
            <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">${post.votes}</div>
            <i class="fas fa-chevron-down voteIcon downvoteIcon interactable ${post.vote == -1 ? "downvote-icon-active" : ""}" onclick = "toggleVote(this, 'downvote', 'post')"></i>
        </span>
    <span id = "post-media-container">
           <a href = "${pageContext.request.contextPath}/post?id=${post.id}"}>
               <c:choose>
                   <c:when test="${post.type == 'TEXT'}">
                       <i class="fas fa-comment post-generic-holder" ></i>
                   </c:when>
                   <c:otherwise>
                       <img class=" post-generic-holder post-image-holder" src=${applicationScope.picsLocation}/${post.content}>
                   </c:otherwise>
               </c:choose>
           </a>
    </span>
    <span>
        <a href = "${pageContext.request.contextPath}/post?id=${post.id}">
            <h3 style = "display: block; margin-top: 5px; margin-bottom: 1px; margin-right:2px;">${post.title}</h3>
            <div id="post-meta-container">
                <a href="${pageContext.request.contextPath}/s?section=${post.section.name}" style="font-size:12px;font-weight:400;line-height:16px">s/${post.section.name}</a>
                <a href="${pageContext.request.contextPath}/user?id=${post.author.username}" style = "font-size: 12px;font-weight: 400;line-height: 16px">Posted by: ${post.author.username}</a>
                <a href="" title="${post.stringCreationDate}"  style = "font-size: 12px;font-weight: 400;line-height: 16px"><%= InstantFormatter.printRelative(((Post)pageContext.getAttribute("post")).getCreationDate()) %></a>
                <a href="${pageContext.request.contextPath}/post?id=${post.id}#comments-container" id="post-comment-container" style = "display: block; font-size: 12px;font-weight: 400;line-height: 16px; width: 120px; margin-bottom: 5px;">
                    <i class="fas fa-comment-dots"></i>
                    ${post.nComments} comments
                </a>
            </div>
        </a>
    </span>
</div>