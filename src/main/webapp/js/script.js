window.onclick = function(event) {
    // console.log(event.target)

    // if (!$(event.target).has("#myDropdown").length) {
    //     console.log("NOPE");
    //     toggleDropdown(false, "myDropdown")
    // }
    //
    // if (!$(event.target).has("#profile-drowdown").length){
    //     console.log("NOPE2");
    //     toggleDropdown(false, "profile-drowdown")
    // }
}

function toggleDropdown(state, id){
    console.log(document.getElementById(id).classList.contains("show"))
    document.getElementById(id).classList.toggle("show");
}

function doUpvote(){console.log('test')}

function toggleFavorite(e){
    //se la sezione è nei preferiti
    if(e.classList.contains("fas")){
        e.classList.remove("fas");
        e.classList.add("far");
        e.style.color = "white";
    }else{
        e.classList.remove("far");
        e.classList.add("fas");
        e.style.color = "blue";
    }

    //send ajax request
}

function toggleVote(el, elementType, actiontype){
    let currentVotes = $(el).parent().find(".vote-count").text();
    let modifier = 0;

    if(currentVotes === "Vote")
        currentVotes = 0;
    currentVotes = parseInt(currentVotes);
    console.log(currentVotes);

    if(elementType === "upvote"){
        let upvoteElement = el;
        let downvoteElement = $(el).parent().find(".downvoteIcon")

        if($(downvoteElement).hasClass('downvote-icon-active')){
            $(downvoteElement).toggleClass('downvote-icon-active')
            modifier = 2;
        } else{
            if($(upvoteElement).hasClass('upvote-icon-active')){
                modifier = -1;
            } else {
                modifier = 1;
            }
        }
        $(upvoteElement).toggleClass('upvote-icon-active')
    } else if (elementType === "downvote"){
        let upvoteElement = $(el).parent().find(".upvoteIcon");
        let downvoteElement = el;

        if($(upvoteElement).hasClass('upvote-icon-active')){
            $(upvoteElement).toggleClass('upvote-icon-active')
            modifier = -2;
        } else{
            if($(downvoteElement).hasClass('downvote-icon-active')){
                modifier = 1;
            } else {
                modifier = -1;
            }
        }
        $(downvoteElement).toggleClass('downvote-icon-active')
    } else {
        console.log("ALERT ERROR IN TOGGLEVOTE")
    }

    $(el).parent().find(".vote-count").text(currentVotes + modifier);

    //send ajax request
}