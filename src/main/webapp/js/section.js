$(() => {
    postLoader.callbacks.before = () => $("#posts-delimiter").addClass("animated");
    postLoader.callbacks.always = () => $("#posts-delimiter").removeClass("animated");
    postLoader.callbacks.empty = () => $("#post-container").append(createEmptyElement("fas fa-frown","Nessun Post"))
});

$("#filter").on("filterchanged", function (){
    $("#post-container").empty();
    postLoader.params.section = $("#post-container").attr("section");
    postLoader.params.orderby = $("#filter #new-button").hasClass("selected") ? "newest" : postLoader.params.orderby;
    postLoader.params.orderby = $("#filter #top-button").hasClass("selected") ? "mostvoted" : postLoader.params.orderby;
    postLoader.params.page = 1;
    postLoader.fetch();
    postLoader.start($("#posts-delimiter").get(0));
})