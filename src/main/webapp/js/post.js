let offset = 0;
$( document ).ready(function() {
    console.log( "ready!" );
    offset = 0;
    loadPosts();
});


function createPost(data){

    let post = `<div class="post greyContainer interactable">
        <span id = vote-container>
            <button onclick="doUpvote();" class="voteButton interactable" >
                 <i class="fas fa-chevron-up voteIcon upvoteIcon" ></i>
            </button>
            <div style="text-align: center">
                <div style="font-size: 12px;font-weight: 700; line-height: 16px;">${data.votes}</div>
            </div>
            <button class="voteButton interactable" onclick="doDownvote();">
                <i class="fas fa-chevron-down voteIcon downvoteIcon"></i>
            </button>
        </span>
        <span id = "post-media-container">`;
        if(data.type === "TEXT"){
            post += `<i class="fas fa-comment post-generic-holder" ></i>`;
        } else{
            post += `<img class=" post-generic-holder post-image-holder" src=${data.content}>`;
        }
        post += `</span>
        <span >
            <h3 style = "display: block; margin-top: 5px; margin-bottom: 1px; margin-right:2px;">${data.title}</h3>
            <div id="post-meta-container">
                <a href="s/${data.section.name}" style = "font-size: 12px;font-weight: 400;line-height: 16px">s/${data.section.name}</a>
                <a href="shareboard/user/${data.author.username}" style = "font-size: 12px;font-weight: 400;line-height: 16px">Posted by: ${data.author.username}</a>
                <a href="s/${data.section.name}/${data.id}" id = "post-comment-container" style = "display: block; font-size: 12px;font-weight: 400;line-height: 16px; width: 120px; margin-bottom: 5px;">
                    <i class="fas fa-comment-dots"></i>
                    ${data.nComments} comments
                </a>
            </div>
        </span>
        </div>`

    return post;

}


function loadPosts(section){
    // let section = window.location.pathname
    $.post(window.location.origin+"/shareboard/loadPosts",
        {
            section: window.location.pathname,
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