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
                $("#title").html("PMTCT Monthly Summary");
                $("#crumb").html("Clinic >> Ante-Natal Clinic >> PMTCT Monthly Summary");
            } else {
            }
            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    if ($("#formId").html() == 1) {
                        url = "Pmtct_summary.action?reportingMonth=" + $("#reportingMonth").val() + "&reportingYear=" + $("#reportingYear").val();
                    } else {
                    }
                    window.open(url);
                }
                return false;
            });
            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Pmtct_page");
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
<jsp:include page="/WEB-INF/views/template/nav_pmtct.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item"><a href="Anc_search">PMTCT</a></li>
    <li class="breadcrumb-item active">Monthly Report</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Month</label>
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <select name="reportingMonth" style="width: 100%;" class="form-control select2"
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
                            </div>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Year</label>
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <select name="reportingYear" style="width: 100%;" class="form-control select2"
                                            id="reportingYear"/>
                                    <option></option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <span id="periodHelp1" class="errorspan"></span>
                        <span id="fm"></span>
                    </div>

                    <div id="formId" style="display: none">

                    </div>
                    <div class="col-md-6 pull-right">
                        <button id="ok_button" class="btn btn-info">Ok</button>
                        <button id="cancel_button" class="btn btn-default">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<div id="userGroup" style="display: none">

</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>

</html>