<%@ taglib prefix="sbfn" uri="/WEB-INF/tlds/tagUtils.tld" %>
<%--@elvariable id="comment" type="model.comment.Comment"--%>
<%-- Quello sopra è un commento di IntelliJ che permette di ignorare l'errore "cannot resolve variable" e
     fornisce l'auto-complete anche se l'oggetto non è presente (Ancora) in nessuno scope --%>

<div id = "${comment.id}" class = "grid-x-nw" style = "width: 100%; align-items: start; margin-top:10px; background-color: #242323; border-radius: 4px; border: solid 1px #313132; ">
    <div class = "vote-container">
        <input type = "hidden" name = "id" value = !ID>
        <i class="fas fa-chevron-up voteIcon upvoteIcon interactable ${comment.vote == 1 ? "upvote-icon-active" : ""}" onclick = "toggleVote(this, 'upvote', 'comment')"></i>
        <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">
            ${comment.votes}
        </div>
        <i class="fas fa-chevron-down voteIcon downvoteIcon interactable ${comment.vote == -1 ? "downvote-icon-active" : ""}" onclick = "toggleVote(this, 'downvote', 'comment')"></i>
    </div>
    <div class = "grid-y-nw" style="flex-grow:1; align-items: start;padding-bottom: 10px; padding-right: 10px;">
        <div style = "flex-basis: 100%">
            <a class = "grey-text" href="${pageContext.request.contextPath}/u/${comment.author.username}">posted by ${comment.author.username}</a>
            <a href="javascript:void(0)" class="grey-text" title="${sbfn:getDate(comment.creationDate)}" >${sbfn:printTimeSince(comment.creationDate)} fa</a>
        </div>
        <div>
            <p class = "white-text comment-text">
                ${comment.text}
            </p>
        </div>
        <div>
            <c:if test="${not empty requestScope.loggedUser}">
             <span id = "reply-button" class = "grey-text" onclick="toggleTextArea(this)">
                <input type = "hidden" name = "commentId" value = ${comment.id}>
                <i class="fas fa-comment-dots"></i>
                <span>Reply</span>
             </span>
            </c:if>
            <c:if test="${not empty requestScope.loggedUser
                        and (comment.author.id == requestScope.loggedUser.id or requestScope.loggedUser.admin.equals(true))}">
                <span id = "edit-button" class = "grey-text" onclick="toggleTextArea(this)">Edit</span>
                <span class="grey-text"><a href="${pageContext.request.contextPath}/deletecomment?id=${comment.id}" id="delete-button" onclick="return confirm('Cancellare il commento?')">Delete</a></span>
            </c:if>
        </div>
        <form class = "comment-form reply-form" method = "POST" action= "${pageContext.request.contextPath}/newcomment" hidden>
            <input type = "hidden" name = "id" value = ${comment.post.id}>
            <input type = "hidden" name = "parent" value = ${comment.id}>
            <textarea name = "text" rows="5" placeholder="Scrivi una risposta..."></textarea>
            <br>
            <button class = roundButton>Rispondi</button>
        </form>
        <form class = "comment-form edit-form" method = "POST" action= "${pageContext.request.contextPath}/editcomment" hidden>
            <input type = "hidden" name = "id" value = ${comment.id}>
            <textarea name = "text" rows="5">${comment.text}</textarea>
            <br>
            <button class = roundButton>Modifica</button>
        </form>

        ${childComments}
    </div>
</div>