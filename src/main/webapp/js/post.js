$( document ).ready(function() {
    console.log( "ready!" );
    loadPosts();
});


function createPost(data){
    return `<div class="post greyContainer interactable">
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
        <span id = "post-media-container">` +
        // if(data.type === "TEXT"){
        //     `<i class="fas fa-comment post-generic-holder" ></i>`
        // } else{
        //     ` <img class=" post-generic-holder post-image-holder" src=${data.content}>`
        // }
        `</span>
        <span >
            <h3 style = "display: block; margin-top: 5px; margin-bottom: 1px; margin-right:2px;">${data.title}</h3>
            <div id="post-meta-container">
                <a href="/section" style = "font-size: 12px;font-weight: 400;line-height: 16px">s/${data.section.name}</a>
                <a href="/user" style = "font-size: 12px;font-weight: 400;line-height: 16px">Posted by: ${data.author.username}</a>
                <a href="/post" id = "post-comment-container" style = "display: block; font-size: 12px;font-weight: 400;line-height: 16px; width: 120px; margin-bottom: 5px;">
                    <i class="fas fa-comment-dots"></i>
                    ${data.nComments} comments
                </a>
            </div>
        </span>
</div>`
}


function loadPosts(section, limit, offset){
    //chiamata ajax
    $.post("loadPosts",
        {
            section: "Desc",
        },
        function(data, status){
            //alert("Data: " + data + "\nStatus: " + status);
            let test = JSON.parse(data);
            console.log(test);

            for(let post of test){
                console.log(post);
                $('#post-container').append(createPost(post));
            }
        });

}