/*TODO:
- Messaggio "la ricerca non ha prodotto risultati"
- Messaggio "post finiti" (facoltativo)
- Messaggio "errore, riprova"
- Messaggio "cerca qualcosa"
 */

function isSearchFormBlank() {
    return $("#search-form [name=content]").val().trim() === "" &&
        !$("#search-form [name=onlyfollow]").is(":checked") &&
        $("#search-form [name=section]").val().trim() === "" &&
        $("#search-form [name=author]").val().trim() === "" &&
        $("#search-form [name=postedafter]").val().trim() === "" &&
        $("#search-form [name=postedbefore]").val().trim() === ""
}

$("#search-form").submit(function() {
    if(!isSearchFormBlank()) {
        $("#post-container").empty();
        $(this).serializeArray().reduce((object, param) => {
            object[param.name] = param.value;
            return object
        }, postLoader.params);
        postLoader.params.page = 1;
        postLoader.fetch();
        postLoader.start();
    }
});

$(() => {
    if(!isSearchFormBlank())
        $("#search-form").submit();
    else
        //TODO: messaggio a schermo del tipo "cerca qualcosa"
        ;
});