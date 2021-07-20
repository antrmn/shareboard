function openNav() {
    document.getElementById("mySidenav").style.width = "300px";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

function toggleAdminStatus(e, id){
    console.log(e.checked)
    $.get('./toggleAdmin',
        {
            isAdmin: e.checked,
            userId: id
        },
        function (data) {
        //add notification
    });
}

function openModal(){
    document.getElementById("myModal").style.display = "block";
}

function closeModal(){
    document.getElementById("myModal").style.display = "none";
}

window.addEventListener("click", function(event) {
    if (event.target == document.getElementById("myModal")) {
        closeModal();
    }
});


$('#ban-form').submit(function(e) {
    e.preventDefault();


    $.post('./addban', $('#ban-form').serialize(), function (data) {
        if(data!=""){
            //console.log(data)
            $('#error-list').empty();
            for(let error of data){
                $('#error-list').append(` <li>${error}</li>`)
            }
        } else{
            window.location.reload(false);
        }
    });

});