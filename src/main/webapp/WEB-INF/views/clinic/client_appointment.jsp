<%-- 
    Document   : client_appointment
    Created on : Oct 27, 2017, 11:55:31 AM
    Author     : user10
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!--<link type="text/css" rel="stylesheet" href="css/lamis.css" />
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />-->
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/JavaScript">
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $("#date1").mask("99/99/9999");
            $("#date1").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $("#date2").mask("99/99/9999");
            $("#date2").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $.ajax({
                url: "Casemanager_retrieve.action",
                dataType: "json",
                success: function (caseManagerMap) {
                    var options = "<option value = '" + '0' + "'>" + '' + "</option>";
                    $.each(caseManagerMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#casemanagerId").html(options);
                }
            }); //end of ajax call

            $("#casemanagerId").bind("change", function (event) {
                var casemanagerId = $("#casemanagerId").val();
                if (casemanagerId != "0") {
                    $("#ok_button").prop("disabled", false);
                } else if (casemanagerId == "0") {
                    $("#ok_button").prop("disabled", true);
                }
            });

            $("#ok_button").bind("click", function (event) {
                var casemanagerId = $("#casemanagerId").val();
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    url = "";
                    url += "casemanagerId=" + casemanagerId + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();
                    url = "Client_appointment_report.action?" + url;
                    window.open(url);
                }
                return false;
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Casemanagement_page");
                return true;
            });

        });

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
            if ($("#date2").val().length == 0) {
                $("#dateHelp2").html(" *");
                validate = false;
            } else {
                $("#date2").datepicker("option", "altField", "#reportingDateEnd");
                $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
                $("#dateHelp2").html("");
            }
            return validate;
        }

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="/Casemanagement_page">Reports</a></li>
        <li class="breadcrumb-item active" aria-current="page">Client Appointment List</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-10 ml-auto mr-auto">
        <div class="card">
            <div class="card-header bg-info">
                <class
                ="card-title text-white"></class="card-title><h5></h5>
            </div>
            <form id="lamisform" theme="css_xhtml">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar"></div>
                    <p></p>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Facility Case Manager:</label>
                                <select name="casemanagerId" class="form-control" id="casemanagerId">
                                    <option value='0'></option>
                                </select><span id="caseManagerHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">Appointment Period From:</label>
                                <input name="date1" type="text" class="form-control" id="date1"/><input
                                    name="reportingDateBegin" type="hidden" id="reportingDateBegin"/><span
                                    id="dateHelp1" style="color:red"></span>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">To:</label><input name="date2" type="text"
                                                                            class="form-control" id="date2"/><input
                                    name="reportingDateEnd" type="hidden" id="reportingDateEnd"/><span id="dateHelp2"
                                                                                                       style="color:red"></span>
                            </div>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="ok_button" type="submit" class="btn btn-fill btn-info" disabled>Ok</button>
                        <button id="cancel_button type=" reset
                        " class="btn btn-fill btn-default">Close</button>
                    </div>
                    <div id="user_group" style="display: none">Case management</div>
            </form>
        </div>
    </div>
</div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
