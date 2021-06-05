<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<jsp:include page="../partials/head.jsp">
  <jsp:param name="currentPage" value="${post.title}" />
  <jsp:param name="styles" value="section" />
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
          <div id = "post-data" style = "flex-grow: 1">
              <div class = "grid-x-nw" style = "align-items: start; margin-top:10px;">
                  <div id = vote-container style = "width: 40px; ">
                      <button onclick="doUpvote();" class="voteButton interactable" >
                          <i class="fas fa-chevron-up voteIcon upvoteIcon" ></i>
                      </button>
                      <div style="text-align: center">
                          <c:choose>
                              <c:when test="${post.votes > 0}">
                                  <c:set var="vote" value="${post.votes}" />
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
                  </div>
                  <div class = "grid-y-nw" style="flex-grow:1; align-items: start; margin-right: 40px;">
                      <div style = "flex-basis: 100%">
                          <a class = white-text>s/test</a>
                          <a class = grey-text>posted by somedude</a>
                      </div>
                      <div class = white-text>Best Title</div>

                      <div>
                          <p class = white-text style = "border: solid 1px gray; border-radius: 4px; padding: 7px;">
                              Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum
                          </p>
                      </div>
                      <div class = "grey-text" >
                          <i class="fas fa-comment-dots"></i>
                          ${post.nComments} 10 comments
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
          <jsp:include page="../partials/rules.jsp"/>
          <jsp:include page="../partials/footer.jsp"/>
      </div>
  </div>
</div>
</body>
</html>
