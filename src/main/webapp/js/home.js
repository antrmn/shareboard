function bottomPageReached(){
    //TODO: vedi come funziona sta condizione qui sotto. Si può migliorare. (se riduco lo zoom di pagina non funge)
    return window.innerHeight + window.pageYOffset >= document.body.offsetHeight;
}

function setInfiniteScrolling(params){
    setInfiniteScrolling.requestSent = false; //l'equivalente di una variabile statica in Java
    setInfiniteScrolling.postsFinished = false;

    params.page = 2;
    $(window).scroll(() => {
        if(bottomPageReached() && setInfiniteScrolling.requestSent === false && setInfiniteScrolling.postsFinished === false){
            setInfiniteScrolling.requestSent = true;
            $.post(window.location.origin+ "/shareboard/loadposts", params)
                .done((data) => {$("#post-container").append(data);
                                 params.page++;
                                 if(!data || data.trim() === ""){
                                     console.log("è finito");
                                     setInfiniteScrolling.postsFinished = true;
                                 }})
                .fail($.noop)
                .always(() => setInfiniteScrolling.requestSent = false);
        }
    })
}

$(() => {
    $.post(window.location.origin+ "/shareboard/loadposts", {page:1})
        .done((data) => $("#post-container").append(data));
    setInfiniteScrolling({page: 1});
});