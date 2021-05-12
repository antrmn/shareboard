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
    if(document.getElementById(id).classList.contains("show") && state == false){
        console.log("NOPE3");
        document.getElementById(id).classList.toggle("show");
        // let dropdowns = document.getElementsByClassName("dropdown-content");
        // for (let i = 0; i < dropdowns.length; i++) {
        //     let openDropdown = dropdowns[i];
        //     if (openDropdown.classList.contains('show')) {
        //         openDropdown.classList.remove('show');
        //     }
        // }
    }

    if(state){
        document.getElementById(id).classList.toggle("show");
    }
}

// function openSectionDropdown(toggle) {
//     if (!toggle){
//         console.log("TESTEST2")
//         let dropdowns = document.getElementsByClassName("dropdown-content");
//         let i;
//         for (i = 0; i < dropdowns.length; i++) {
//             let openDropdown = dropdowns[i];
//             if (openDropdown.classList.contains('show')) {
//                 openDropdown.classList.remove('show');
//             }
//         }
//     } else{
//         openSectionDropdown(false);
//         document.getElementById("myDropdown").classList.toggle("show");
//         //document.getElementById("myDropdown").classList.add("show")
//     }
// }
//
// function openProfileDropdown(toggle) {
//     if (!toggle){
//         console.log("TESTEST")
//         let dropdowns = document.getElementsByClassName("dropdown-content");
//         let i;
//         for (i = 0; i < dropdowns.length; i++) {
//             let openDropdown = dropdowns[i];
//             if (openDropdown.classList.contains('show')) {
//                 openDropdown.classList.remove('show');
//             }
//         }
//     } else{
//         openSectionDropdown(false);
//         document.getElementById("profile-drowdown").classList.toggle("show");
//         //document.getElementById("profile-drowdown").classList.add("show")
//     }
// }