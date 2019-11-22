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
                $("#title").html("ART Monthly Summary");
                $("#crumb").html("Home >> ART Monthly Summary");
            } else {
                $("#title").html("Performance Indicator");
                $("#crumb").html("Home >> Performance Indicators");
            }
            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    if ($("#formId").html() == 1) {
                        url = "Art_summary.action?reportingMonth=" + $("#reportingMonth").val() + "&reportingYear=" + $("#reportingYear").val();
                    } else {
                        if ($("#formId").html() == 2) {
                            url = "Performance_indicator.action?reportingMonth=" + $("#reportingMonth").val() + "&reportingYear=" + $("#reportingYear").val();
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
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="/Visualizer_page">Report</a></li>
        <li class="breadcrumb-item active" aria-current="page">ART Monthly Summary</li>
    </ol>
</nav>
<form id="lamisform" theme="css_xhtml">
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

                    <div id="formId" style="display: none">
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
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
