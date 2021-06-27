<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="../partials/head.jsp">
  <jsp:param name="currentPage" value="${post.title}" />
  <jsp:param name="styles" value="section,comment" />
  <jsp:param name="scripts" value="comment" />
</jsp:include>
<body>
<jsp:include page="/WEB-INF/navbar">
  <jsp:param name="isLogged" value="true" />
  <jsp:param name="currentSection" value="${post.section.name}" />
  <jsp:param name="userName" value="Testus" />
  <jsp:param name="userKarma" value="4316" />
</jsp:include>

<div id="body-container" class = "justify-center align-center">
  <div id="post-container" class = "grid-x-nw" style = "flex-basis: 1280px;">
      <div id = left-container class = "greyContainer">
          <div id = "post-data" style = "flex-grow: 1; width: 100%;">
              <div class = "grid-x-nw" style = "align-items: start; margin-top:10px;">
                  <div class = "vote-container">
                          <input type = "hidden" name = "id" value = ${post.id}>
                          <c:choose>
                              <c:when test="${post.vote == 1}">
                                  <i class="fas fa-chevron-up voteIcon upvoteIcon interactable upvote-icon-active" onclick="toggleVote(this, 'upvote', 'post')"></i>
                              </c:when>
                              <c:otherwise>
                                  <i class="fas fa-chevron-up voteIcon upvoteIcon interactable" onclick="toggleVote(this, 'upvote', 'post')"></i>
                              </c:otherwise>
                          </c:choose>
                          <c:choose>
                              <c:when test="${post.votes != 0}">
                                  <c:set var="vote" value="${post.votes}" />
                              </c:when>
                              <c:otherwise>
                                  <c:set var="vote" value="Vote" />
                              </c:otherwise>
                          </c:choose>
                            <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">${vote}</div>
                          <c:choose>
                              <c:when test="${post.vote == -1}">
                                  <i class="fas fa-chevron-down voteIcon downvoteIcon interactable downvote-icon-active" onclick="toggleVote(this, 'downvote', 'post')"></i>
                              </c:when>
                              <c:otherwise>
                                  <i class="fas fa-chevron-down voteIcon downvoteIcon interactable" onclick="toggleVote(this, 'downvote', 'post')"></i>
                              </c:otherwise>
                          </c:choose>
                  </div>
                  <div class = "grid-y-nw" style="flex-grow:1; align-items: start; margin-right: 40px;">
                      <div style = "flex-basis: 100%">
                          <a href="/shareboard/s?section=${post.section.name}" class = white-text>s/${post.section.name}</a>
                          <a href="/shareboard/user?id=${post.author.id}" class = grey-text>posted by ${post.author.username}</a>
                      </div>
                      <div class = white-text>
                          ${post.title}
                      </div>

                      <div>
                          <c:choose>
                              <c:when test="${post.type == 'IMG'}">
                                 <img src= "${applicationScope.picsLocation}/${post.content}">
                              </c:when>
                              <c:when test="${post.type == 'TEXT'}">
                                  <p class = white-text style = "border: solid 1px gray; border-radius: 4px; padding: 7px; word-break: break-word;">
                                          ${post.content}
                                  </p>
                              </c:when>
                          </c:choose>
                      </div>
                      <div class = "grey-text" >
                          <i class="fas fa-comment-dots"></i>
                          ${post.nComments}
                      </div>
                        <jsp:include page="../partials/comment-form.jsp">
                            <jsp:param name="id" value="${post.id}"/>
                        </jsp:include>
                      <div class ="grid-x-nw" style = "width: 100%">
                          <div id = "comments-container" style = "margin:8px; width: 100%">
                          </div>
                      </div>
                  </div>
              </div>
          </div>
      </div>
      <div id="right-container">
          <jsp:include page="../partials/section-info.jsp">
              <jsp:param name="description" value="${section.description}" />
              <jsp:param name="nFollowers" value="${section.nFollowers}" />
              <jsp:param name="link" value="test" />
          </jsp:include>
          <jsp:include page="../partials/rules.jsp"/>
          <jsp:include page="../partials/footer.jsp"/>
      </div>
  </div>
</div>
</body>
</html>
