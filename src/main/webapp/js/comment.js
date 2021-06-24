$( document ).ready(function() {
    console.log( "commentjs ready!" );
    loadComments();
});

function createComment(author, votes, vote, content, id, isEven, isSpecial){
    let style = getComputedStyle(document.body);
    let color = "#242323" //make this a css var

    let upvoteClass = "";
    if (vote === 1){
        upvoteClass = "upvote-icon-active"
    }
    let downvoteClass = "";
    if (vote === -1){
        downvoteClass = "downvote-icon-active";
    }
    if (isEven){
        color = style.getPropertyValue('--shareboard-container-1');
    }
    let comment = `
              <div id = "${id}" class = "grid-x-nw" style = "width: 100%; align-items: start; margin-top:10px; background-color: ${color}; border-radius: 4px; border: solid 1px #313132; ">
                  <div class = "vote-container">
                    <input type = "hidden" name = "id" value = ${id}>
                    <i class="fas fa-chevron-up voteIcon upvoteIcon interactable ${upvoteClass}" onclick = "toggleVote(this, 'upvote', 'comment')"></i>
                    <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">
                        ${votes}
                    </div>
                    <i class="fas fa-chevron-down voteIcon downvoteIcon interactable ${downvoteClass}" onclick = "toggleVote(this, 'downvote', 'comment')"></i>
                  </div>
                  <div class = "grid-y-nw" style="flex-grow:1; align-items: start;padding-bottom: 10px; padding-right: 10px;">
                      <div style = "flex-basis: 100%">
                          <a class = grey-text>posted by ${author}</a>
                      </div>
                      <div>
                          <p class = white-text>
                            ${content}
                          </p>
                      </div>
                      <div id = "reply-button" class = "grey-text" onclick="toggleTextArea(this)">
                          <input type = "hidden" name = "commentId" value = ${id}>
                          <i class="fas fa-comment-dots"></i>
                          <span>Reply</span>
                      </div>
                  </div>
              </div>
    `;

    return comment;
}

function createEmpty(){
    let el = `
    <span>
        Nessun Commento    
    </span>
    `;

    return el;
}

function addChilds(comments, key, depth){
    if (comments[key]){
        depth++;
        for(let comment of comments[key]){
            // console.log(key + "  " + depth + " " + comment.text);
            // console.log(depth%2===0);
            $(createComment(comment.author.username, comment.votes,comment.vote, comment.text, comment.id, depth%2===0)).insertAfter(`#${key} > .grid-y-nw > #reply-button`);
            addChilds(comments, comment.id, depth);
        }
    }
    // console.log("END:" + depth)
    depth--;
}


function loadComments(){

    $.post(window.location.origin+"/shareboard/loadComments",
        {
            postId: getUrlParameter('id')
        },
        function(data, status){
            // console.log(status);
            // console.log(data);

            if (data == null){
                $("#comments-container").append(createEmpty());
            }
            let comments = JSON.parse(data);
            console.log(comments);
            console.log(comments[0]);
            let i = 1;

            let baseId = 0;
            if (comments[baseId]){
                for(let comment of comments[baseId]){
                    $("#comments-container").append(createComment(comment.author.username, comment.votes,comment.vote, comment.text, comment.id, false));
                    addChilds(comments, comment.id, 1);
                }
            }
        });
}

function toggleTextArea(el){
    let form = `<form class = "comment-form" method = "POST" action= "./newcomment">
                    <input type = "hidden" name = "id" value = ${getPostId()}>
                    <input type = "hidden" name = "parent" value = ${$(el).find('input').val()}>
                    <textarea name = "text" rows="5"></textarea>
                    <br>
                    <button class = roundButton>Invia</button>
                </form>`;

    if($(el).hasClass('has-form')){
        $(el).parent().find('.comment-form').remove();
    }else{
        $(form).insertAfter(el);
    }

    $(el).toggleClass('has-form');
}

function validateCommentForm(el){
    console.log($(el).find("textarea").val().length)
    if ($(el).find("textarea").val().length == 0 || $(el).children("textarea").val().length > 1000){
        return false;
    }

    return true;
}

function getPostId(){
    return $("#post-data").find('input').val();
}