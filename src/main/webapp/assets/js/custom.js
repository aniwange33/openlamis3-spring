var d = new Date();
$(document).ready(function () {
    $("title").html("LAMIS 3.0");
    
    setTimeout(function () {
        $("#preloader").hide();
        $(".wrapper").show();
    }, 2000);

    $(".select2").select2();

    //$.jgrid.defaults.width = 780;
    $.jgrid.defaults.responsive = true;
    $.jgrid.defaults.styleUI = 'Bootstrap4';
    $.jgrid.defaults.iconSet = "Octicons";

    $('[data-toggle="tooltip"]').tooltip();

    $("#copyright").html(d.getFullYear());
});
