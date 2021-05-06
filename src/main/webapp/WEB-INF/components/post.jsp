<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<div class="post greyContainer interactable">
        <span id = vote-container>
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
            <h3 style = "display: block; margin-top: 5px; margin-bottom: 1px; margin-right:2px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor</h3>
            <div id="post-meta-container">
                <a href="/section" style = "font-size: 12px;font-weight: 400;line-height: 16px">s/test</a>
                <a href="/user" style = "font-size: 12px;font-weight: 400;line-height: 16px">Posted by: testus</a>
                <a href="/post" id = "post-comment-container" style = "display: block; font-size: 12px;font-weight: 400;line-height: 16px; width: 100px; margin-bottom: 5px;">
                    <i class="fas fa-comment-dots"></i>
                    69 comments
                </a>
            </div>
        </span>
<%--    </a>--%>
</div>