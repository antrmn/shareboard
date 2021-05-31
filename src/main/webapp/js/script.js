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

    console.log(id)
    console.log(document.getElementById(id).classList.contains("show"))
    document.getElementById(id).classList.toggle("show");
    // if(document.getElementById(e.id).classList.contains("show") && state == false){
    //     console.log("NOPE3");
    //     document.getElementById(e.id).classList.toggle("show");
    //     let dropdowns = document.getElementsByClassName("dropdown-content");
    //     for (let i = 0; i < dropdowns.length; i++) {
    //         let openDropdown = dropdowns[i];
    //         if (openDropdown.classList.contains('show')) {
    //             openDropdown.classList.remove('show');
    //         }
    //     }
    // }

    // if(state){
    //     document.getElementById(id).classList.toggle("show");
    // }
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