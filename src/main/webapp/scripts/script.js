window.onclick = function(event) {
    console.log(event.target)

    if ($(event.target).has("#myDropdown").length) {
        console.log("NOPE");
        openSectionDropdown(false);
    }

    if ($(event.target).has("#profile-drowdown").length){
        console.log("NOPE2");
        openProfileDropdown(false);
    }
}

function openSectionDropdown(toggle) {
    if (!toggle){
        console.log("TESTEST2")
        let dropdowns = document.getElementsByClassName("dropdown-content");
        let i;
        for (i = 0; i < dropdowns.length; i++) {
            let openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    } else{
        openSectionDropdown(false);
        document.getElementById("myDropdown").classList.toggle("show");
        //document.getElementById("myDropdown").classList.add("show")
    }
}

function openProfileDropdown(toggle) {
    if (!toggle){
        console.log("TESTEST")
        let dropdowns = document.getElementsByClassName("dropdown-content");
        let i;
        for (i = 0; i < dropdowns.length; i++) {
            let openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    } else{
        openSectionDropdown(false);
        document.getElementById("profile-drowdown").classList.toggle("show");
        //document.getElementById("profile-drowdown").classList.add("show")
    }
}