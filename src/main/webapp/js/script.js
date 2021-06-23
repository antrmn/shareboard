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

function toggleFollowStar(e){
    let addFollow = false;
    //se la sezione è nei preferiti
    if(e.classList.contains("fas")){
        e.classList.remove("fas");
        e.classList.add("far");
        e.classList.remove("favorite-star");
    }else{
        e.classList.remove("far");
        e.classList.add("fas");
        e.classList.add("favorite-star");
        addFollow = true
    }

    let id = $(e).parent().find("input").val();
    console.log(id);
    //send ajax request
    toggleFollow(id, addFollow)
}

function toggleFollow(id, addFollow){
    let url = window.location.origin+"/shareboard/unfollow";
    if(addFollow){
        url = window.location.origin+"/shareboard/follow"
    }

    $.post(url,
        {
            section: id,
        });
}

function toggleVote(el, actiontype, elementType){
    let currentVotes = $(el).parent().find(".vote-count").text();
    let modifier = 0;

    if(currentVotes === "Vote")
        currentVotes = 0;
    currentVotes = parseInt(currentVotes);
    console.log(currentVotes);

    if(actiontype === "upvote"){
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
    } else if (actiontype === "downvote"){
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

    let serverAction = 1;

    if (actiontype === "downvote")
        serverAction = -1;

    $.post(window.location.origin+"/shareboard/handlevote",
        {
            id: $(el).find("input").val(),
            action: serverAction,
            type: elementType
        });
}

let getUrlParameter = function getUrlParameter(sParam) {
    let sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return typeof sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
    return false;
};