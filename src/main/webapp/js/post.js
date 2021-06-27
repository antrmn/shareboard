let offset = 0;
let section;
let order;
// $( document ).ready(function() {
//     console.log( "ready!" );
//     offset = 0;
//     loadPosts();
// });

function initParams(_section, _order){
    section = _section;
    order= _order;
    console.log( "postjs ready!" );
    console.log( section );
    offset = 0;
   // loadPosts();
}

function createPost(data){

    console.log(data.section.name)
    let sectionLink = `${window.location.origin}/shareboard/s?section=${data.section.name}`;
    let postLink = `${window.location.origin}/shareboard/post?id=${data.id}`;

    let upvoteClass = "";
    if (data.vote === 1){
        upvoteClass = "upvote-icon-active"
    }
    let downvoteClass = "";
    if (data.vote === -1){
        downvoteClass = "downvote-icon-active";
    }
    let post = `<div class="post greyContainer interactable">
        <span class = "vote-container">
            <input type = "hidden" name = "id" value = ${data.id}>
            <i class="fas fa-chevron-up voteIcon upvoteIcon interactable ${upvoteClass}" onclick = "toggleVote(this, 'upvote', 'post')"></i>
            <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">${data.votes}</div>
            <i class="fas fa-chevron-down voteIcon downvoteIcon interactable ${downvoteClass}" onclick = "toggleVote(this, 'downvote', 'post')"></i>
        </span>
        <span id = "post-media-container"> 
           <a href = ${postLink}>`;
        if(data.type === "TEXT"){
            post += `<i class="fas fa-comment post-generic-holder" ></i>`;
        } else{
            post += `<img class=" post-generic-holder post-image-holder" src=${data.content}>`;
        }
        post += `</a></span>
        <span >
            <a href = ${postLink}>
                <h3 style = "display: block; margin-top: 5px; margin-bottom: 1px; margin-right:2px;">${data.title}</h3>
                <div id="post-meta-container">
                    <a href=${sectionLink} style = "font-size: 12px;font-weight: 400;line-height: 16px">s/${data.section.name}</a>
                    <a href="user?id=${data.author.username}" style = "font-size: 12px;font-weight: 400;line-height: 16px">Posted by: ${data.author.username}</a>
                    <a href=${postLink} id = "post-comment-container" style = "display: block; font-size: 12px;font-weight: 400;line-height: 16px; width: 120px; margin-bottom: 5px;">
                        <i class="fas fa-comment-dots"></i>
                        ${data.nComments} comments
                    </a>
                </div>
            </a>
        </span>
        </div>`

    return post;

}


function loadPosts(){
    console.log(section);
    $.post(window.location.origin+"/shareboard/loadPosts",
        {
            section: section,
            order: order,
            offset: offset
        },
        function(data, status){
            // console.log(status);
            // console.log(data);
            let posts = JSON.parse(data);
            // console.log(posts);
            let stuff = '';
            for(let post of posts){
                console.log(post);
                stuff += createPost(post);
                //$('#post-container').append(createPost(post));
            }
            $('#post-container').append(stuff);
            offset += 50;
        });

    // $.post(window.location.origin+"/shareboard/loadPosts",
    //     {
    //         section: window.location.pathname,
    //         offset: offset
    //     },
    //     function(data, status){
    //         $('#post-container').html(data);
    //     });


}