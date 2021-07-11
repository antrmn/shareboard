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

function toggleFollow(e){
    let addFollow = false
    let sectionId = $(e).attr("data-section-id")

    if(!$(e).hasClass('follow-button-isfollowing')){
        addFollow = true;
    }
    $(e).toggleClass('follow-button-isfollowing')
    toggleFollowAjax(sectionId, addFollow)

    let elements = $(`.follow-button[data-section-id = "${sectionId}"]`);

    for (let element of elements){
        updateAllFollowButtons(element, addFollow)
    }

}

function toggleFollowAjax(id, addFollow){
    let url = window.location.origin+"/shareboard/unfollow";
    if(addFollow){
        url = window.location.origin+"/shareboard/follow"
    }

    $.post(url,
        {
            section: id,
        });
}

function updateAllFollowButtons(e, addFollow){
    if($(e).hasClass('fa-star')){
        toggleFollowStar(e, addFollow)
    } else if($(e).hasClass('follow-roundbutton')){
        toggleFollowButton(e, addFollow)
    }
}

$('.fa-star').on('click', function(e) {
    e.stopPropagation();
});

function toggleFollowStar(e, addFollow){
    //se la sezione non è nei preferiti
    if(!addFollow){
        e.classList.remove("fas");
        e.classList.add("far");
        e.classList.remove("favorite-star");
    }else{
        e.classList.remove("far");
        e.classList.add("fas");
        e.classList.add("favorite-star");
    }
}

function toggleFollowButton(e, addFollow){

    if(addFollow){
        $(e).removeClass("lightGreyButton")
        $(e).addClass('darkGreyButton')
        $(e).text("Joined");
    }else{
        $(e).removeClass("darkGreyButton")
        $(e).addClass('lightGreyButton')
        $(e).text("Join");
    }
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

function createEmptyElement(icon, text){
    let emptyElement = `
    <div class = "grid-x-nw justify-center align-center">
        <i class="${icon}" style = " color: rgb(215, 218, 220); font-size: 35px; margin-left: 15px;"></i>
        <h2 style = " color: rgb(215, 218, 220); font-size: 30px;">
            ${text}
        </h2>
    </div>
`
    return emptyElement;
}