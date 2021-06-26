<div id = "${comment.id}" class = "grid-x-nw" style = "width: 100%; align-items: start; margin-top:10px; background-color: #242323; border-radius: 4px; border: solid 1px #313132; ">
    <div class = "vote-container">
        <input type = "hidden" name = "id" value = !ID>
        <i class="fas fa-chevron-up voteIcon upvoteIcon interactable" onclick = "toggleVote(this, 'upvote', 'comment')"></i>
        <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">
            !VOTI
        </div>
        <i class="fas fa-chevron-down voteIcon downvoteIcon interactable" onclick = "toggleVote(this, 'downvote', 'comment')"></i>
    </div>
    <div class = "grid-y-nw" style="flex-grow:1; align-items: start;padding-bottom: 10px; padding-right: 10px;">
        <div style = "flex-basis: 100%">
            <a class = grey-text>posted by ${comment.author.username}</a>
        </div>
        <div>
            <p class = white-text>
                ${comment.text}
            </p>
        </div>
        <div id = "reply-button" class = "grey-text" onclick="toggleTextArea(this)">
            <input type = "hidden" name = "commentId" value = ${comment.id}>
            <i class="fas fa-comment-dots"></i>
            <span>Reply</span>
        </div>
        ${childComments}
    </div>
</div>