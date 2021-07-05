/*
    Vecchio metodo per vedere se il fondo pagina è stato raggiunto
    function bottomPageReached(){
        return window.innerHeight + window.pageYOffset >= document.body.offsetHeight;
    }
*/


let postLoader = {
    lock: false,
    params: {page: 1},
    target: null,
    observer: new IntersectionObserver(()=>{
        if (postLoader.lock === false)
            postLoader.fetch()
    }, {rootMargin: '0px', threshold: 0.2}),
    callbacks: {
        before: $.noop,
        success: (data) => $("#post-container").append(data),
        error: $.noop,
        empty: $.noop,
        always: $.noop
    },
    fetch: function(callbacks = postLoader.callbacks){
        postLoader.lock = true;
        Object.assign(callbacks, postLoader.callbacks, callbacks);
        if (typeof postLoader.lastRequest === "object" && typeof postLoader.lastRequest.abort === "function")
            postLoader.lastRequest.abort();
        if(!Number.isInteger(postLoader.params.page))
            postLoader.params.page = 1;
        callbacks.before();

        //setTimeout impostato solo per far vedere l'animazione di caricamento.
        setTimeout(()=>{
            postLoader.lastRequest = $.get(window.location.origin+ "/shareboard/loadposts", postLoader.params)
                .done((data) => {
                    if(!data || data.trim() === ""){
                        callbacks.empty();
                        postLoader.stop();
                    } else {
                        callbacks.success(data);
                        postLoader.params.page += 1;
                        //forza ricontrollo
                        postLoader.lock = false;
                        postLoader.observer.unobserve(postLoader.target);
                        postLoader.observer.observe(postLoader.target);
                    }
                })
                .fail(() => {
                    callbacks.error();
                    postLoader.stop();
                })
                .always(() => {
                    callbacks.always();
                    postLoader.lock = false;
                });
        }, 800);

    },
    start: function(el) {
        postLoader.target = el;
        postLoader.observer.observe(postLoader.target);
    },
    stop: function() {
        postLoader.observer.disconnect();
        postLoader.lock = false;
    }
}

