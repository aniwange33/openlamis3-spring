<%-- 
    Document   : Data Export
    Created on : Aug 15, 2012, 6:53:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/maintenance-common.js"></script>

    <script type="text/JavaScript">
        var timer = {};
        $(document).ready(function () {
            initialize();
            timer = $.timer(function () {
                $.ajax({
                    url: "/processing",
                    dataType: "json",
                    success: function (status) {
                        if (status === "check1") $("#check1").removeAttr("hidden");
                        if (status === "check2") {
                            $("#check1").removeAttr("hidden");
                            $("#check2").removeAttr("hidden");
                        }
                        if (status === "check3") {
                            $("#check1").removeAttr("hidden");
                            $("#check2").removeAttr("hidden");
                            $("#check3").removeAttr("hidden");
                        }
                        if (status === "check4") {
                            $("#check1").removeAttr("hidden");
                            $("#check2").removeAttr("hidden");
                            $("#check3").removeAttr("hidden");
                            $("#check4").removeAttr("hidden");
                        }
                    }
                });
            });
            timer.set({time: 30000, autostart: false});

            $("#ok_button").bind("click", function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                timer.play();
                cleanup();
                return false;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });
        });

        function cleanup() {
            $("#messageBar").hide();
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: "Cleanup_data.action",
                dataType: "json",
                success: function (status) {
                    timer.stop();
                    $("#loader").html('');
                    $("#check1").removeAttr("hidden");
                    $("#check2").removeAttr("hidden");
                    $("#check3").removeAttr("hidden");
                    $("#check4").removeAttr("hidden");
                    $("#check5").removeAttr("hidden");
                    $("#messageBar").html("Database Cleanup Completed").slideDown('fast');
                    $("#ok_button").attr("disabled", false);
                }
            });
        }
    </script>
    <style>
        p {
            color: #000000;
            font-size: 1.2em;
        }
    </style>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/">Home</a></li>
    <li class="breadcrumb-item"><a href="/export">Data Maintenance</a></li>
    <li class="breadcrumb-item active">Cleanup Database Records</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-10 mr-auto ml-auto">
            <div class="card">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar"></div>
                    <div class="row">
                        <div class="col-md-12 text-darken-3">
                            <p><i class="fa fa-check"></i>Cleaning clinic records and updating date of last clinic in
                                patient table</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 text-darken-3">
                            <p><i class="fa fa-check"></i>Updating date of last ARV refill in patient table</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 text-darken-3">
                            <p><i class="fa fa-check"></i> Updating date of last lab investigation in patient table</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 text-darken-3">
                            <p><i class="fa fa-check"></i>Determining due date for viral load test</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 text-darken-3">
                            <p><i class="fa fa-check"></i>Updating patient current status</p>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="ok_button" class="btn btn-info">Cleanup</button>
                        <button id="close_button" class="btn btn-default">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
