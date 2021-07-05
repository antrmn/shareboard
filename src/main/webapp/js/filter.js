$(() =>{
    $("#filter a#popular-button").click(function(){
        $(this).toggleClass("selected");
        $("#filter").trigger("filterchanged");
        }
    );
    $("#filter a#top-button, #filter a#new-button").click(function(){
        if($(this).hasClass("selected")){
            /* no op */
        } else {
            $("#filter a#top-button, #filter a#new-button").not(this).removeClass("selected");
            $(this).addClass("selected");
            $("#filter").trigger("filterchanged");
        }
    });

    //Comportamento di default. TODO: controllare qui cookie?
    $("#filter a#popular-button").addClass("selected");
    $("#filter a#top-button").addClass("selected");
    $("#filter").trigger("filterchanged");
})