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

            if ($("#formId").html() == 1) {
                $("#title").html("Summary of Drugs Dispensed");
                $("#crumb").html("Pharmacy >> General Pharmacy >> Summary of Drugs Dispensed");
            } else {
                $("#title").html("");
            }
            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    if ($("#formId").html() == 1) {
                        url = "Dispensed_summary.action?reportingMonth=" + $("#reportingMonth").val() + "&reportingYear=" + $("#reportingYear").val();
                    }
                    window.open(url);
                }
                return false;
            });
            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Pharmacy_page");
                return true;
            });
        });

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            if ($("#reportingMonth").val().length == 0 || $("#reportingYear").val().length == 0) {
                $("#periodHelp1").html(" *");
                validate = false;
            } else {
                $("#periodHelp1").html("");
            }
            return validate;
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_report.jsp"/>
<!-- MAIN CONTENT -->
<form id="lamisform" theme="css_xhtml">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item"><a href="Visualizer_page">Report</a></li>
        <li class="breadcrumb-item active">Summary of Drugs Dispensed</li>
    </ol>
    <div class="row">
        <div class="col-md-10 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <label class="form-label">Month</label>
                                    <select name="reportingMonth" style="width: 100%" class="form-control select2"
                                            id="reportingMonth"/>
                                    <option></option>
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
                                    <label class="form-label">Year</label>
                                    <select name="reportingYear" style="width: 100%" class="form-control select2"
                                            id="reportingYear"/>
                                    <option></option>
                                    </select><span id="periodHelp1" style="color:red"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="pull-right">
                                <button id="ok_button" class="btn btn-info">Ok</button>
                                <button id="cancel_button" class="btn btn-default">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="formId" style="display: none">
    </div>
</form>

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
