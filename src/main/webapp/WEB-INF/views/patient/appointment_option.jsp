<%-- 
    Document   : Facility
    Created on : Feb 8, 2012, 1:15:46 PM
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

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    var reportType = $("input[name=reportType]:checked").val();

                    if (reportType == "1" || reportType == "2" || reportType == "3") {
                        url = "Appointment_list.action?reportType=" + reportType + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();
                    } else {
                        if (reportType == "4") {
                            url = "Visit_refill_list.action?reportType=" + reportType + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();
                        } else {
                            if (reportType == "5") {
                                url = "Visit_clinic_list.action?reportType=" + reportType + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();
                            } else {
                                url = "Defaulter_list.action?reportType=" + reportType + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();
                            }
                        }
                    }
                    window.open(url);
                }
                return false;
            });
            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Home_page");
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
<jsp:include page="/WEB-INF/views/template/nav_report.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="Visualizer_page">Report</a></li>
        <li class="breadcrumb-item active" aria-current="page">Clinic & Refill Appointments/Visits</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <div class="row">
            <div class="col-md-10 ml-auto mr-auto">
                <div class="card">
                    <div class="card-body">
                        <div id="messageBar"></div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="card-title">Clinic & Refill Appointments/Visits</div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="form-check form-check-radio">
                                    <label class="form-check-label">
                                        <input type="radio" name="reportType" value="1" class="form-check-input"
                                               checked/>
                                        <span class="form-check-sign"></span>List of clinic appointment
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="form-check form-check-radio">
                                    <label class="form-check-label">
                                        <input type="radio" name="reportType" value="2" class="form-check-input"/>
                                        <span class="form-check-sign"></span> LList of refill visits
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="form-check form-check-radio">
                                    <label class="form-check-label">
                                        <input type="radio" name="reportType" value="3" class="form-check-input"/>
                                        <span class="form-check-sign"></span> List of clinic visits
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="form-check form-check-radio">
                                    <label class="form-check-label">
                                        <input type="radio" name="reportType" value="4" class="form-check-input"/>
                                        <span class="form-check-sign"></span> List of missed refill appointment
                                        (defaulters)
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="form-check form-check-radio">
                                    <label class="form-check-label">
                                        <input type="radio" name="reportType" value="5" class="form-check-input"/>
                                        <span class="form-check-sign"></span> List of missed clinic appointment
                                        (defaulters)
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <div class="form-check form-check-radio">
                                    <label class="form-check-label">
                                        <input type="radio" name="reportType" value="6" class="form-check-input"/>
                                        <span class="form-check-sign"></span> List of missed tracking appointment (based
                                        on agreed date of return)
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 form-group">
                                <label class="form-label">From</label>
                                <input name="date1" type="text" class="form-control" id="date1"/><input
                                    name="reportingDateBegin" type="hidden" id="reportingDateBegin"/><span
                                    id="dateHelp1" style="color:red"></span>
                            </div>
                            <div class="col-md-4 form-group">
                                <label class="form-label">To</label>
                                <input name="date2" type="text" class="form-control" id="date2"/><input
                                    name="reportingDateEnd" type="hidden" id="reportingDateEnd"/><span id="dateHelp2"
                                                                                                       style="color:red"></span>
                            </div>
                        </div>
                        <div class="col-md-6 pull-right">
                            <button id="ok_button" class="btn btn-info">Ok</button>
                            <button id="cancel_button" class="btn btn-default">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>

</body>
</html>
