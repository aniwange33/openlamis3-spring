<%-- 
    Document   : treatment_tracker_page
    Created on : Dec 5, 2018, 5:20:30 PM
    Author     : user10
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css"/>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/javascript" src="js/jquery.timer.js"></script>
    <script type="text/JavaScript">
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $("body").bind('ajaxStart', function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            });

            $("body").bind('ajaxStop', function (event) {
                $("#loader").html('');
            });
            $("#messageBar").hide();

            $.ajax({
                url: "State_retrieve.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }); //end each
                    $("#state").html(options);
                }
            }); //end of ajax call

            $.ajax({

                url: "Report_period_retrieve.action",
                dataType: "json",
                data: {reportType: "WR"},

                success: function (periodMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(periodMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    });
                    $("#period").html(options);
                }
            }); //end of ajax call

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    convertTrackerData(20);
                }
                return false;
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Event_page");
                return true;
            });

        });

        $("#state").change(function (event) {
            console.log('State', event);
            $.ajax({
                url: "Facility_retrieve.action",
                dataType: "json",
                data: {state: $("#state").val()},
                success: function (facilityMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(facilityMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }); //end each
                    $("#facility").html(options);
                }
            }); //end of ajax call
        });

        var url = "";
        var x = function wait() {
            window.open(url);
        };

        function convertTrackerData(recordType) {
            $("#messageBar").hide();
            $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: "Converter_dispatch.action",
                dataType: "json",
                data: {recordType: recordType},
                beforeSend: function () {
                    console.log("About to generate report...");
                },
                success: function (fileName) {
                    $("#messageBar").html("Report generated successfully!").slideDown('slow');
                    $("#ok_button").attr("disabled", false);
                    console.log(fileName);
                    url = fileName;
                    window.setTimeout(x, 3000);
                },
                error: function (e) {
                    alert("There was an error in report generation!");
                    console.log("Error: " + JSON.stringify(e));
                    $("#ok_button").attr("disabled", false);
                }
            });
        }

        function validateForm() {
            var validate = true;
            return validate;
        }

    </script>
</head>
<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">

        <jsp:include page="/WEB-INF/views/template/nav_event.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/monitor.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Administration >> Events Monitor >> DHIS Aggregate Report</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">DHIS Aggregate Report</td>
                    </tr>
                </table>
                <div id="loader"></div>
                <div id="messageBar"></div>
                <p></p>

                <div style="margin-right: 10px;">
                    <table width="99%" border="0" class="space" cellpadding="3">
                        <tr>
                            <td width="15%">
                                <input type="radio" name="reportType" value="DR" disabled="true"/>
                                <label>Daily Report</label>
                            </td>
                            <td width="15%">
                                <input type="radio" name="reportType" value="WR" checked disabled="true">
                                <label>Weekly Report</label>
                            </td>
                            <td width="15%">
                                <input type="radio" name="reportType" value="MR" disabled="true"/>
                                <label>Monthly Report</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="15%"><label>State:</label></td>
                            <td width="85%">
                                <select name="state" style="width: 130px;" class="inputboxes" id="state">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td width="15%"><label>Facility:</label></td>
                            <td width="85%">
                                <select name="facility" style="width: 130px;" class="inputboxes" id="facility">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td width="15%"><label>Reporting Period:</label></td>
                            <td width="85%">
                                <select name="period" style="width: 200px;" class="inputboxes" id="period">
                                </select><span id="periodHelp" class="errorspan"></span>
                            </td>
                        </tr>
                    </table>
                </div>
                <p></p>
                <div id="buttons" style="width: 200px">
                    <button id="ok_button">Export</button> &nbsp;<button id="cancel_button">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>

