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
                    url: "Processing_status.action",
                    dataType: "json",
                    success: function (status) {
                        if (status == "terminated") {
                            timer.stop();
                            $("#loader").html('');
                            $("#messageBar").html("Error occured while downloading updates, please retry").slideDown('fast');
                        } else {
                            if (status == "completed") {
                                timer.stop();
                                $("#loader").html('');
                                $("#messageBar").html("Download completed, updates will be applied when LAMIS restarts").slideDown('fast');
                            } else {
                                processingStatusNotifier("Downloading " + status + " file, please wait...");
                            }
                        }
                    }
                });
            });
            timer.set({time: 60000, autostart: false});

            $("#updates_button").bind("click", function (event) {
                $("#messageBar").hide();
                checkUpdates();
                return false;
            });

            $("#test_button").bind("click", function (event) {
                $("#messageBar").hide();
                checkConnection();
                return false;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });
        });

        function checkUpdates() {
            timer.play();
            $("#messageBar").hide();
            $("#updates_button").attr("disabled", true);
            $("#test_button").attr("disabled", true);
            $.ajax({
                url: "Check_updates.action",
                dataType: "json",
                success: function (status) {
                    timer.stop();
                    $("#loader").html('');
                    $("#messageBar").html(status).slideDown('slow');
                    $("#updates_button").attr("disabled", false);
                    $("#test_button").attr("disabled", false);
                }
            });
        }

        function checkConnection() {
            $.ajax({
                url: "Internet_connection.action",
                dataType: "json",
                error: function (jgXHR, status) {
                    alert(status);
                },
                success: function (status) {
                    if (status == 1) {
                        $("#messageBar").html("Connection successfuly established with the LAMIS server").slideDown('slow');
                    } else {
                        $("#messageBar").html("No connection established with the LAMIS server, please connect to the internet before proceeding").slideDown('slow');
                    }
                }
            });
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item"><a href="/export">Data Maintenance</a></li>
    <li class="breadcrumb-item active">Check & Download Updates</li>
</ol>
<s:form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar"></div>
                    <div class="row">
                        <div class="col-md-8">
                            <p>Available updates will be downloaded and applied when LAMIS is restarted</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 pull-right">
                            <button id="test_button" class="btn btn-info">Test Connection</button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="updates_button" class="btn btn-info">Updates</button>
                        <button id="close_button" class="btn btn-default">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</s:form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>

</html>