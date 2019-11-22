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
    <script type="text/javascript" src="js/jquery.timer.js"></script>
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
                                processingStatusNotifier("Exporting " + status + " table, please wait...");
                            }
                        }
                    }
                });
            });
            timer.set({time: 30000, autostart: false});

            $("#export_button").bind("click", function (event) {
                timer.play();
                exportData();
                return false;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });
        });

        var url = "";
        var x = function wait() {
            window.open(url);
        }

        function exportData() {
            $("#messageBar").hide();
            $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            $("#export_button").attr("disabled", true);
            $.ajax({
                url: "Export_data.action",
                dataType: "json",
                data: {recordsAll: $("#recordsAll").prop("checked")},
                success: function (fileName) {
                    timer.stop();
                    $("#loader").html('');
                    $("#messageBar").html("Export Completed").slideDown('fast');
                    url = fileName;
                    window.setTimeout(x, 3000);
                    $("#export_button").attr("disabled", false);
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
    <li class="breadcrumb-item active">Export Data</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-10 mr-auto ml-auto">
            <div class="card">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <p><strong>Newly created and modified</strong> records since the last export operation will be
                        &nbsp;included in the export file</p>
                    <div class="row">
                        <div class="col-md-12">
                            <table>
                                <tr>
                                    <td><input name="recordsAll" type="checkbox" id="recordsAll"/> Export all
                                        transaction records to the export file<span style="margin-left: 15px"></span>
                        </div>
                        </td>
                        </table>
                    </div>
                </div>
                <div class="col-md-6 pull-right">
                    <button id="export_button" class="btn btn-info">Export</button>
                </div>
            </div>
        </div>
    </div>
    </div>
</form>
<div id="userGroup" style="display: none">
    <s:property value="#session.userGroup"/>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>