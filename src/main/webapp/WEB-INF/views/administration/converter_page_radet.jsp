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
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/JavaScript">
        var facilityIds = [];
        $(document).ready(function () {
            initialize();
            reports();

            for (i = new Date().getFullYear(); i > 1900; i--) {
                $("#cohortYearBegin").append($("<option/>").val(i).html(i));
                $("#cohortYearEnd").append($("<option/>").val(i).html(i));
                $("#reportingYear").append($("<option/>").val(i).html(i));
            }

            $("body").bind('ajaxStart', function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            });

            $("body").bind('ajaxStop', function (event) {
                $("#loader").html('');
            });
            $("#messageBar").hide();


            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    if ($("#cohortYearEnd").val() - $("#cohortYearBegin").val() < 0) {
                        var message = "Cohort end year cannot be eailer than cohort beginning year";
                        $("#messageBar").html(message).slideDown('slow');
                    } else {
                        convertData();
                    }
                }
                return false;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Conversion_page");
                return true;
            });

        });

        var url = "";
        var x = function wait() {
            window.open(url);
        }

        function convertData() {
            $("#messageBar").hide();
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: "Converter_dispatch_radet.action",
                dataType: "json",
                data: {
                    cohortMonthBegin: $("#cohortMonthBegin").val(),
                    cohortYearBegin: $("#cohortYearBegin").val(),
                    cohortMonthEnd: $("#cohortMonthEnd").val(),
                    cohortYearEnd: $("#cohortYearEnd").val(),
                    reportingMonth: $("#reportingMonth").val(),
                    reportingYear: $("#reportingYear").val()
                },
                success: function (fileName) {
                    $("#messageBar").html("Analysis Completed").slideDown('fast');
                    url = fileName;
                    window.setTimeout(x, 3000);
                    $("#ok_button").attr("disabled", false);   //$("#ok_button").removeAttr("disabled");
                }
            });
        }

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            if ($("#cohortMonthBegin").val().length == 0 || $("#cohortYearBegin").val().length == 0) {
                $("#periodHelp1").html(" *");
                validate = false;
            } else {
                $("#periodHelp1").html("");
            }
            if ($("#cohortMonthEnd").val().length == 0 || $("#cohortYearEnd").val().length == 0) {
                $("#periodHelp2").html(" *");
                validate = false;
            } else {
                $("#periodHelp2").html("");
            }
            if ($("#reportingMonth").val().length == 0 || $("#reportingYear").val().length == 0) {
                $("#periodHelp3").html(" *");
                validate = false;
            } else {
                $("#periodHelp3").html("");
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
        <li class="breadcrumb-item"><a href="/converter">Data Conversion</a></li>
        <li class="breadcrumb-item active">Generate RADET File</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <div class="row">
            <div class="col-md-10 ml-auto mr-auto">
                <div class="card">
                    <div class="card-body">
                        <div id="loader"></div>
                        <div id="messageBar"></div>

                        <div class="row">
                            <div class="col-10">
                                <div class="form-group">
                                    <label>Cohorts of ART:</label></td>
                                    <div class="row">
                                        <div class="col-3">
                                            <select name="cohortMonthBegin" style="width: 100%;"
                                                    class="form-control select2" id="cohortMonthBegin"/>
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
                                        <div class="col-3">
                                            <select name="cohortYearBegin" style="width: 100%;"
                                                    class="form-control select2" id="cohortYearBegin"/>
                                            <option value=""></option>
                                            </select><span id="periodHelp1" style="color:red"></span>
                                        </div>

                                        <div class="col-1">to</div>
                                        <div class="col-3">
                                            <select name="cohortMonthEnd" style="width: 100%;"
                                                    class="form-control select2" id="cohortMonthEnd"/>
                                            <option value=""></option>
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
                                        <div class="col-2">
                                            <select name="cohortYearEnd" style="width: 100%;"
                                                    class="form-control select2" id="cohortYearEnd"/>
                                            <option value=""></option>
                                            </select><span id="periodHelp2" style="color:red"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-10">
                                <div class="form-group">
                                    <label>Reporting Period:</label></td>
                                    <div class="row">
                                        <div class="col-6">
                                            <select name="reportingMonth" style="width: 100%;"
                                                    class="form-control select2" id="reportingMonth"/>
                                            <option value=""></option>
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
                                        <div class="col-6">
                                            <select name="reportingYear" style="width: 100%;"
                                                    class="form-control select2" id="reportingYear"/>
                                            <option value=""></option>
                                            </select><span id="periodHelp3" style="color:red"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-10 ml-3">
                                <p><strong>Cohorts of ART are defined by the month/year they are started on ART</strong>
                                </p>
                                <p><strong>Reporting period is the period treatment outcome for the cohorts will be
                                    evaluated</strong></p>
                            </div>
                        </div>
                        <div class="pull-right">
                            <button class="btn btn-info" id="ok_button">Generate</button>
                            <button class="btn btn-default" id="close_button">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <!-- END MAIN CONTENT-->
    <div id="userGroup" style="display: none"></div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
