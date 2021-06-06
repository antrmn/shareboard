$( document ).ready(function() {
    let order = getUrlParameter('orderBy');
    let section = getUrlParameter('section');
    initParams(section, order);
});
