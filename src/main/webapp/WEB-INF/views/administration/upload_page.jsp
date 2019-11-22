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
    <script type="text/javascript" src="js/jquery.timer.js"></script>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/maintenance-common.js"></script>
    <script type="text/javascript" src="js/jquery.timer.js"></script>
    <script type="text/JavaScript">
        var timer = {};
        $(document).ready(function () {
            initialize();
            disableRecordsAll();
            timer = $.timer(function () {
                $.ajax({
                    url: "Processing_status.action",
                    dataType: "json",
                    success: function (status) {
                        if (status == "terminated") {
                            timer.stop();
                            $("#loader").html('');
                            $("#messageBar").html("Error occured while uploading data, please retry").slideDown('fast');
                        } else {
                            if (status == "completed") {
                                timer.stop();
                                $("#loader").html('');
                                $("#messageBar").html("Upload Completed").slideDown('fast');
                            } else {
                                processingStatusNotifier("Uploading " + status + " table, please wait...");
                            }
                        }
                    }
                });
            });
            timer.set({time: 60000, autostart: false});

            $("#dialog").dialog({
                title: "Server Access",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Ok", click: syncData},
                    {
                        text: "Close", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            $("#sync_button").click(function (event) {
                $("#messageBar").hide();
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    $("#dialog").dialog("open");
                    return false;
                }
            });

            $("#test_button").click(function (event) {
                $("#messageBar").hide();
                checkConnection();
                return false;
            });

            $("#close_button").click(function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });
        });

        function syncData() {
            if (validateSyncForm()) {
                $("#dialog").dialog("close");
                if ($("#userName").val() == "fhi360" && $("#password").val() == "admin@site") {
                    timer.play();
                    $("#messageBar").hide();
                    $("#sync_button").attr("disabled", true);
                    $("#test_button").attr("disabled", true);
                    $.ajax({
                        url: "Upload_data.action",
                        data: {recordsAll: $("#recordsAll").prop("checked")},
                        dataType: "json",
                        success: function (status) {
                            timer.stop();
                            $("#loader").html('');
                            $("#messageBar").html(status).slideDown('slow');
                            $("#sync_button").attr("disabled", false);
                            $("#test_button").attr("disabled", false);
                        }
                    });
                } else {
                    $("#messageBar").html("Access denied, please contact your Administrator").slideDown('slow');
                }

            }
        }

        function checkConnection() {
            $.ajax({
                url: "Internet_connection.action",
                dataType: "json",
                error: function (jgXHR, status) {
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

        function disableRecordsAll() {
            $.ajax({
                url: "Disable_recordsAll.action",
                dataType: "json",
                error: function (jgXHR, status) {
                },
                success: function (status) {
                    if (status == "disable") {
                        $("#recordsAll").attr("disabled", true);
                    } else {
                        $("#recordsAll").attr("disabled", false);
                    }
                }
            });
        }


    </script>
    <!--          <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
      <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>-->
    <script>
        $(function () {
            $("#dialog2").dialog();
        });
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item"><a href="/converter">Data Maintenance</a></li>
    <li class="breadcrumb-item active">Upload Data</li>
</ol>
<s:form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <p><strong>Newly created and modified</strong> records since the last upload operation will be
                        &nbsp;included in the upload to the server</p>
                    <div class="row">
                        <div class="col-10 form-check">
                            <label class="form-check-label">
                                <input name="recordsAll" type="checkbox" id="recordsAll" class="form-check-input"/>
                                <span class="form-check-sign"></span>Upload all transaction records to the server
                            </label>
                        </div>
                    </div>
                    <div id="dialog">
                        <div class="row">
                            <div class="col-6 form-group">
                                <label>User Name:</label>
                                <input name="userName" type="text" class="form-control" id="userName"/>
                                <span id="userHelp" style="color:red"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-6 form-group">
                                <label>Password:</label>
                                <input name="password" type="password" class="form-control" id="password"/>
                                <span id="passwordHelp" style="color:red"></span>
                            </div>
                        </div>
                    </div>
                    <div id="userGroup" style="display: none"></div>
                    <div class="pull-right">
                        <button id="test_button" class="btn">Test Connection</button>
                        <button id="sync_button" class="btn btn-info">Upload...</button>
                        <button id="close_button" class="btn btn-default">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</s:form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>

<!-- END MAIN CONTENT-->
<div id="user_group" style="display: none">Administrator</div>
<div id="userGroup" style="display: none"></div>
</body>
</html>


