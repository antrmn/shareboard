$( document ).ready(function() {
    console.log( "ready!" );
    loadComments();
});

function createComment(author, votes, content, id, isEven, isSpecial){
    let style = getComputedStyle(document.body);
    let color = "#242323" //make this a css var

    if (isEven){
        color = style.getPropertyValue('--shareboard-container-1');
    }
    let comment = `
              <div id = "${id}" class = "grid-x-nw" style = "width: 100%; align-items: start; margin-top:10px; background-color: ${color}; border-radius: 4px; border: solid 1px #313132; ">
                  <div class = "vote-container">
                    <i class="fas fa-chevron-up voteIcon upvoteIcon interactable" onclick = "toggleVote(this, 'upvote', 'comment')"></i>
                    <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">
                        ${votes}
                    </div>
                    <i class="fas fa-chevron-down voteIcon downvoteIcon interactable" onclick = "toggleVote(this, 'downvote', 'comment')"></i>
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
        let backup = depth
        for(let comment of comments[key]){
            depth++;
            console.log(depth + " " + comment.text);
            console.log(depth%2===0);
            $(createComment(comment.author.username, comment.vote, comment.text, comment.id, depth%2===0)).insertAfter(`#${key} #reply-button`)
            addChilds(comments, comment.id, depth);
        }
    }
    console.log("END:" + depth)
    depth=-2;
}

function loadComments(){

    $.post(window.location.origin+"/shareboard/loadComments",
        {
            postId: getUrlParameter('post')
        },
        function(data, status){
            // console.log(status);
            // console.log(data);

            if (data == null){
                $("#comments-container").append(createEmpty());
            }
            let comments = JSON.parse(data);
            console.log(comments);
            let i = 1;

            let baseId = 0;
            if (comments[baseId]){
                for(let comment of comments[baseId]){
                    $("#comments-container").append(createComment(comment.author.username, comment.vote, comment.text, comment.id, false));
                    addChilds(comments, comment.id, 1);
                }
            }
        });
}

function addComment(parent, content){

}

function toggleTextArea(el){
    let test = `<div id = "comment-form" style = "width: 100%;">
                    <textarea rows="5" style = "color: #fff; resize: vertical; width:100%; border-radius: 4px; background-color: var(--shareboard-container-2); border-color: var(--shareboard-container-1); border: solid 1px;"></textarea>
                    <br>
                    <button class = roundButton>Invia</button>
                </div>`;

    if($(el).hasClass('has-form')){
        // $(el).parent().remove('#comment-form')
        $(el).parent().find('#comment-form').remove();
    }else{
        $(test).insertAfter(el);
    }

    $(el).toggleClass('has-form');
}