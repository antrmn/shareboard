<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<jsp:include page="../partials/head.jsp">
  <jsp:param name="currentPage" value="${post.title}" />
  <jsp:param name="styles" value="section,comment" />
  <jsp:param name="scripts" value="comment" />
</jsp:include>
<body>
<jsp:include page="../partials/navbar.jsp">
  <jsp:param name="isLogged" value="true" />
  <jsp:param name="currentSection" value="${section.name}" />
  <jsp:param name="userName" value="Testus" />
  <jsp:param name="userKarma" value="4316" />
</jsp:include>

<div id="body-container" class = "justify-center align-center">
  <div id="post-container" class = "grid-x-nw" style = "flex-basis: 1280px;">
      <div id = left-container class = "greyContainer">
          <div id = "post-data" style = "flex-grow: 1; width: 100%;">
              <div class = "grid-x-nw" style = "align-items: start; margin-top:10px;">
                  <div class = "vote-container">
                          <i class="fas fa-chevron-up voteIcon upvoteIcon interactable" onclick="toggleVote(this, 'upvote', 'post')"></i>
                          <c:choose>
                              <c:when test="${post.vote ne 0}">
                                  <c:set var="vote" value="${post.vote}" />
                              </c:when>
                              <c:otherwise>
                                  <c:set var="vote" value="Vote" />
                              </c:otherwise>
                          </c:choose>
                          <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">${vote}</div>
                          <i class="fas fa-chevron-down voteIcon downvoteIcon interactable" onclick="toggleVote(this, 'downvote', 'post')"></i>
                  </div>
                  <div class = "grid-y-nw" style="flex-grow:1; align-items: start; margin-right: 40px;">
                      <div style = "flex-basis: 100%">
                          <a class = white-text>s/${post.section.name}</a>
                          <a class = grey-text>posted by ${post.author.username}</a>
                      </div>
                      <div class = white-text>${post.title}</div>

                      <div>
                          <p class = white-text style = "border: solid 1px gray; border-radius: 4px; padding: 7px; word-break: break-word;">
                              ${post.content}
                          </p>
                      </div>
                      <div class = "grey-text" >
                          <i class="fas fa-comment-dots"></i>
                          ${post.nComments}
                      </div>

                      <form  method = "POST" action= "./add-comment" style = "width:100%; border-bottom: solid 1px; border-radius:1px; border-color: gray;">
                          <textarea rows="5" style = "color: #fff; resize: vertical; width:100%; border-radius: 4px; background-color: var(--shareboard-container-2); border-color: var(--shareboard-container-1); border: solid 1px;"></textarea>
                          <br>
                          <button class = roundButton>Invia</button>
                      </form>
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