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

            if ($("#formId").html() == 3) {
                $("#title").html("Cohort Analysis");
                $("#crumb").html("Home >> Cohort Analysis");
            } else {
                $("#title").html("Service Quality Indicator");
                if ($("#formId").html() == 1)
                    $("#crumb").html("Home >> Service Quality Indicators (National)");
                else if ($("#formId").html() == 2)
                    $("#crumb").html("Home >> Service Quality Indicators (PEPFAR)");
            }
            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    if ($("#formId").html() == 1) {
                        url = "Quality_indicator.action?reportingMonthBegin=" + $("#reportingMonthBegin").val() + "&reportingYearBegin=" + $("#reportingYearBegin").val() + "&reportingMonthEnd=" + $("#reportingMonthEnd").val() + "&reportingYearEnd=" + $("#reportingYearEnd").val();
                    } else {
                        if ($("#formId").html() == 2) {
                            url = "Service_summary.action?reportingMonthBegin=" + $("#reportingMonthBegin").val() + "&reportingYearBegin=" + $("#reportingYearBegin").val() + "&reportingMonthEnd=" + $("#reportingMonthEnd").val() + "&reportingYearEnd=" + $("#reportingYearEnd").val();
                        } else {
                            url = "Cohort_analysis.action?reportingMonthBegin=" + $("#reportingMonthBegin").val() + "&reportingYearBegin=" + $("#reportingYearBegin").val() + "&reportingMonthEnd=" + $("#reportingMonthEnd").val() + "&reportingYearEnd=" + $("#reportingYearEnd").val();
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
            if ($("#reportingMonthBegin").val().length == 0 || $("#reportingYearBegin").val().length == 0) {
                $("#periodHelp1").html(" *");
                validate = false;
            } else {
                $("#periodHelp1").html("");
            }
            if ($("#reportingMonthEnd").val().length == 0 || $("#reportingYearEnd").val().length == 0) {
                $("#periodHelp2").html(" *");
                validate = false;
            } else {
                $("#periodHelp2").html("");
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
        <li class="breadcrumb-item"><a href="/Visualizer_page">Report</a></li>
        <li class="breadcrumb-item active" aria-current="page">ART Monthly Summary</li>
    </ol>
</nav>
<form id="lamisform">
    <div class="row">
        <div class="col-md-10 ml-auto mr-auto">
            <div class="card">

                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar"></div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <label>Monthly</label>
                                    <select name="reportingMonth" style="width: 100%;" class="form-control"
                                            id="reportingMonth"/>
                                    <option>Select</option>
                                    <option>January</option>
                                    <option>February</option>
                                    <option>March</option>
                                    <option>April</option>
                                    <option>May</option>
                                    <option>June</option>
                                    <option>July</option>
                                    <option>August</option>
                                    <option>September</option>
                                    <option>October</option>
                                    <option>November</option>
                                    <option>December</option>
                                    </select>
                                </div>
                                <div class="col-md-6 form-group">
                                    <label>Year</label>
                                    <select name="reportingYear" style="width: 100%;" class="form-control"
                                            id="reportingYear"/>
                                    <option>Select</option>
                                    </select>
                                </div>
                            </div>
                            <span id="periodHelp1" class="errorspan"></span>
                            <span id="fm"></span>
                        </div>
                    </div>
                    <div id="formId" style="display: none"></div>
                    <div id="buttons" style="width: 200px">
                        <button id="ok_button">Ok</button> &nbsp;<button id="cancel_button">Close</button>
                        <button id="cancel_button" class="btn btn-default">Close</button>
                    </div>
                    <div id="formId" style="display: none"></div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
