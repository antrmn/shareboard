// window.onclick = function(event) {
//     // console.log(event.target)
//
//     //console.log($(event.target).parents().has("#section-dropdown"))
//     // console.log($(event.target).parents("#section-dropdown"))
//     // if (!$(event.target).is("#section-dropdown") && $(event.target).parents("#section-dropdown").length === 0) {
//     //     console.log("NOPE");
//     //     toggleDropdown("close", "section-dropdown")
//     // }
//     //
//     // if (!$(event.target).is("#profile-dropdown") && $(event.target).parents("#profile-drowdown").length === 0){
//     //     console.log("NOPE2");
//     //     toggleDropdown("close", "profile-drowdown")
//     // }
// }

function toggleDropdown(action, id){
    // console.log(document.getElementById(id).classList.contains("show"))
    if (action === "close"){
        document.getElementById(id).classList.remove("show");
    } else if(action === "toggle"){
        document.getElementById(id).classList.toggle("show");
    }
}

$('.fa-star').on('click', function(e) {
    e.stopPropagation();
});

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
    let isAddingVote = true
    if(currentVotes === "Vote")
        currentVotes = 0;
    currentVotes = parseInt(currentVotes);
    // console.log(currentVotes);

    if(actiontype === "upvote"){
        let upvoteElement = el;
        let downvoteElement = $(el).parent().find(".downvoteIcon")

        if($(downvoteElement).hasClass('downvote-icon-active')){
            $(downvoteElement).toggleClass('downvote-icon-active')
            modifier = 2;
        } else{
            if($(upvoteElement).hasClass('upvote-icon-active')){
                isAddingVote = false;
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
                isAddingVote = false;
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
    console.log($(el).siblings("input").val())
    let _id = $(el).siblings("input").val();
    if (isAddingVote){
        $.post(window.location.origin+"/shareboard/vote",
            {
                id: _id,
                vote: actiontype,
                type: elementType
            });
    } else {
        $.post(window.location.origin+"/shareboard/unvote",
            {
                id: _id,
                type: elementType
            });
    }
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



/* Responsive left/right container */

$(() => {
    let toggleView = ()=>{
        if(!window.matchMedia('(max-width: 767px)').matches)
            return;
        $("#left-container").toggleClass("selected-container");
        $("#right-container").toggleClass("selected-container");
        let x = 200;
        $(".selected-container").children().hide().each(function() {$(this).fadeIn(x); x+=200});
    }

    if ($("#left-container, #right-container").length<2){
        $("#container-switcher").remove(); //Senza i due container il tasto è inutile e va rimosso
    } else{
        $("#container-switcher").click(toggleView);
    }

})