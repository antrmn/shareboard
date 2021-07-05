$(() => {
    postLoader.callbacks.before = () => $("#posts-delimiter").addClass("animated");
    postLoader.callbacks.always = () => $("#posts-delimiter").removeClass("animated");
})

$("#filter").on("filterchanged", function (){
    $("#post-container").empty();
    postLoader.params.onlyfollow = $("#filter #popular-button").hasClass("selected") ? "" : "on";
    postLoader.params.orderby = $("#filter #new-button").hasClass("selected") ? "newest" : postLoader.params.orderby;
    postLoader.params.orderby = $("#filter #top-button").hasClass("selected") ? "mostvoted" : postLoader.params.orderby;
    postLoader.params.page = 1;
    postLoader.fetch();
    postLoader.start($("#posts-delimiter").get(0));
})