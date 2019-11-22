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
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    -->
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
                <!--url: "StateId_retrieve.action",-->
                url: "StateId_retrieve_custom.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#stateId").html(options);
                }
            }); //end of ajax call

            $("#stateId").change(function (event) {
                $("#messageBar").slideUp('fast');
                $("#ok_button").attr("disabled", false);

            });

            $("#date1").mask("99/99/9999");
            $("#date1").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    convertTrackerData(14);
                }
                return false;
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Event_page");
                return true;
            });

        });

        var url = "";
        var x = function wait() {
            window.open(url);
        }

        function convertTrackerData(recordType) {
            $("#messageBar").hide();
            $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: "Converter_dispatch.action",
                dataType: "json",
                data: {
                    recordType: recordType,
                    reportingDateBegin: $("#reportingDateBegin").val(),
                    stateId: $("#stateId").val()
                },
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

            // check for valid input is entered
            if ($("#date1").val().length == 0) {
                $("#dateHelp1").html(" *");
                validate = false;
            } else {
                $("#date1").datepicker("option", "altField", "#reportingDateBegin");
                $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
                $("#dateHelp1").html("");
            }
            return validate;
        }

    </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>
<!-- MAIN CONTENT -->
<div class="mt-5"></div>
<div class="content mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/converter">Events Monitor</a></li>
        <li class="breadcrumb-item active">Retention Tracker</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <div class="row">
            <div class="col-md-8 ml-auto mr-auto">
                <div class="card">
                    <div id="loader"></div>
                    <div id="messageBar"></div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Reporting Period:</label>
                                    <input name="date1" type="text" class="form-control" id="date1"/><input
                                        name="reportingDateBegin" type="hidden" id="reportingDateBegin"/><span
                                        id="dateHelp1" style="color:red"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Select state:</label>
                                    <select name="stateId" style="width: 100%;" class="form-control select2"
                                            id="stateId"></select><span id="stateHelp" class="errorspan"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="pull-right">
                                <button id="ok_button" type="submit" class="btn btn-info">Ok</button>
                                <button id="cancel_button" class="btn btn-default">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
