<%--DEPRECATO--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<div class="post greyContainer interactable">
        <span class = "vote-container">
            <button onclick="doUpvote();" class="voteButton interactable" >
                 <i class="fas fa-chevron-up voteIcon upvoteIcon" ></i>
            </button>
            <div style="text-align: center">
                <c:choose>
                    <c:when test="${param.votes > 0}">
                        <c:set var="vote" value="${param.votes}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="vote" value="Vote" />
                    </c:otherwise>
                </c:choose>
                <div style="font-size: 12px;font-weight: 700; line-height: 16px;">${vote}</div>
            </div>
            <button class="voteButton interactable" onclick="doDownvote();">
                <i class="fas fa-chevron-down voteIcon downvoteIcon"></i>
            </button>
        </span>

        <span id = "post-media-container">
            <c:choose>
                <c:when test="${param.type eq 'img'}">
                    <img class=" post-generic-holder post-image-holder" src="https://dogecoin.org/static/11cf6c18151cbb22c6a25d704ae7b313/dd8fa/doge-main.jpg">
                </c:when>
                <c:otherwise>
                    <i class="fas fa-comment post-generic-holder" ></i>
                </c:otherwise>
            </c:choose>
        </span>
        <span >
            <h3 style = "display: block; margin-top: 5px; margin-bottom: 1px; margin-right:2px;">${param.title}</h3>
            <div id="post-meta-container">
                <a href="/section" style = "font-size: 12px;font-weight: 400;line-height: 16px">s/${param.section}</a>
                <a href="/user" style = "font-size: 12px;font-weight: 400;line-height: 16px">Posted by: ${param.author}</a>
                <a href="/post" id = "post-comment-container" style = "display: block; font-size: 12px;font-weight: 400;line-height: 16px; width: 120px; margin-bottom: 5px;">
                    <i class="fas fa-comment-dots"></i>
                    ${param.comments} comments
                </a>
            </div>
        </span>
</div>

<%--DEPRECATO--%>
