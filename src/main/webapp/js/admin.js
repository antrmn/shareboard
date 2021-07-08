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
            console.log(data)
            $('#error-list').empty();
            for(let error of data){
                $('#error-list').append(` <li>${error}</li>`)
            }
        } else{
            window.location.reload(false);
        }
    });

});

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);


function drawChart() {

    var data = google.visualization.arrayToDataTable([
        ['Section', 'Posts'],
        ['Work',     11],
        ['Eat',      2],
        ['Commute',  2],
        ['Watch TV', 2],
        ['Sleep',    7]
    ]);

    var options = {
        title: 'Post per sezione',
        legendTextStyle: { color: '#FFF' },
        titleTextStyle: { color: '#FFF' },
        backgroundColor: { fill:'transparent' },
        height: 500,
        width: 500
    };

    var chart = new google.visualization.PieChart(document.getElementById('piechart'));

    chart.draw(data, options);
}
