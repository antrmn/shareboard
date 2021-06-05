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
                  <div id = vote-container style = "width: 40px; ">
                      <button onclick="doUpvote();" class="voteButton interactable" >
                          <i class="fas fa-chevron-up voteIcon upvoteIcon" ></i>
                      </button>
                      <div style="text-align: center">
                          <div style="font-size: 12px;font-weight: 700; line-height: 16px;">${votes}</div>
                      </div>
                      <button class="voteButton interactable" onclick="doDownvote();">
                          <i class="fas fa-chevron-down voteIcon downvoteIcon"></i>
                      </button>
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
                      <div id = "reply-button" class = "grey-text" >
                          <i class="fas fa-comment-dots"></i>
                          <span onclick="toggleTextArea()">Reply</span>
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

let emptyResponse = false;
function loadComments(){
    if (emptyResponse){
        $("#comments-container").append(createEmpty());
    } else{
        $("#comments-container").append(createComment("test", 10, "BLABlach", "com1", false));
        $(createComment("test", 10, "BLABlach", "com2", true)).insertAfter("#comments-container #com1 #reply-button")
        $(createComment("GoodGuy", 100, "testest", "com3", false)).insertAfter("#comments-container #com2 #reply-button")
    }
}

function toggleTextArea(){
    console.log("sup");
}