// window.onclick = function(event) {
//     console.log(event.target)
//     if (!event.target.matches('#nav-crt-sctn')) {
//         console.log("NOPE")
//         var dropdowns = document.getElementsByClassName("dropdown-content");
//         var i;
//         for (i = 0; i < dropdowns.length; i++) {
//             var openDropdown = dropdowns[i];
//             if (openDropdown.classList.contains('show')) {
//                 openDropdown.classList.remove('show');
//             }
//         }
//     }
// }

let toggle = false;
function openSectionDropown() {
    console.log("TESTEST")
    if (toggle){
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    } else{
        document.getElementById("myDropdown").classList.toggle("show");
    }
}