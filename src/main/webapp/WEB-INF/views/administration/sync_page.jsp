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
                            $("#messageBar").html("Error occured while exporting data, please retry").slideDown('fast');
                        } else {
                            if (status == "completed") {
                                timer.stop();
                                $("#loader").html('');
                                $("#messageBar").html("Export Completed").slideDown('fast');
                            } else {
                                processingStatusNotifier("Downloading & syncing " + status + " table, please wait...");
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

            $("#patient").change(function (event) {
                buttonState();
            });
            $("#drug").change(function (event) {
                buttonState();
            });
            $("#labtest").change(function (event) {
                buttonState();
            });
            $("#facility").change(function (event) {
                buttonState();
            });

            $("#sync_button").bind("click", function (event) {
                $("#messageBar").hide();
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    $("#dialog").dialog("open");
                    return false;
                }
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

        function buttonState() {
            if ($("#patient").attr("checked")) {
                $("#sync_button").attr("disabled", false);
            } else {
                if ($("#drug").attr("checked")) {
                    $("#sync_button").attr("disabled", false);
                } else {
                    if ($("#labtest").attr("checked")) {
                        $("#sync_button").attr("disabled", false);
                    } else {
                        if ($("#facility").attr("checked")) {
                            $("#sync_button").attr("disabled", false);
                        } else {
                            $("#sync_button").attr("disabled", true);
                        }
                    }
                }
            }
        }

        function syncData() {
            var url = "Sync_data.action?sync";
            if ($("#patient").attr("checked")) url += "&patient";
            if ($("#drug").attr("checked")) url += "&drug"
            if ($("#labtest").attr("checked")) url += "&labtest"
            if ($("#facility").attr("checked")) url += "&facility"

            if (validateSyncForm()) {
                $("#dialog").dialog("close");
                if ($("#userName").val() == "fhi360" && $("#password").val() == "admin@site") {
                    timer.play();
                    $("#messageBar").hide();
                    $("#sync_button").attr("disabled", true);
                    $("#test_button").attr("disabled", true);
                    $.ajax({
                        url: url,
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
    <li class="breadcrumb-item active">Download & Synchronize Data</li>
</ol>
<s:form id="lamisform" theme="css_xhtml">
    <div id="loader"></div>
    <div id="messageBar"></div>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <table class="table table-stripped">
                        <tr>
                            <td colspan="2"><input name="patient" type="checkbox" value="0"
                                                   id="patient"/><label>Download and sync patient data from the
                                server</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="5%"></td>
                            <td width="80%">
                                <p><label><strong>Note:</strong> Your data in the server repository will be returned
                                    back to your system this might take several minutes. Do this operation if
                                    &nbsp;you want data from the server</label></p>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2"><input name="drug" type="checkbox" value="0" id="drug"
                                                   disabled="disabled"/><label>Download and sync all drug &amp; regimen
                                registered
                                on the server</label></td>
                        </tr>
                        <tr>
                            <td width="5%"></td>
                            <td width="60%"></td>
                        </tr>
                        <tr>
                            <td colspan="2"><input name="labtest" type="checkbox" value="0" id="labtest"
                                                   disabled="disabled"/><label>Download and sync all laboratory test
                                registered on
                                the server</label></td>
                        </tr>
                        <tr>
                            <td width="5%"></td>
                            <td width="60%"></td>
                        </tr>
                        <tr>
                            <td colspan="2"><input name="facility" type="checkbox" value="0" id="facility"
                                                   disabled="disabled"/><label>Download and sync all facility registered
                                on the
                                server</label></td>
                        </tr>
                        <tr>
                            <td width="5%"></td>
                            <td width="60%"></td>
                        </tr>
                        <tr>
                            <td colspan="3">&nbsp;</td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <button id="test_button" class="btn btn-info">Test Connection</button>
                            </td>
                        </tr>
                    </table>
                    <div id="dialog">
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>User Name:</label>
                                <input name="userName" type="text" class="form-control" id="userName"/>
                                <span id="userHelp" style="color:red"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Password</label>
                                <input name="password" type="password" class="form-control" id="password"/>
                                <span id="passwordHelp" style="color:red"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="pull-right">
                                    <button id="sync_button" class="btn btn-info" disabled="disabled">Sync...</button>
                                    <button class="btn btn-default" id="close_button">Cancel</button>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div id="userGroup" style="display: none">

                    </div>
                </div>
            </div>
        </div>
    </div>
</s:form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>

</html>