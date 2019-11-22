<%-- 
    Document   : Patient
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 2.6</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!--        <link type="text/css" rel="stylesheet" href="css/lamis.css" />-->
    <!--        <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />-->

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <!--        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
            <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>       -->
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/javascript" src="assets/highchart/highcharts.js"></script>
    <script type="text/javascript" src="assets/highchart/modules/exporting.js"></script>
    <script type="text/JavaScript">
        $(document).ready(function () {
            resetPage();
            reports();

            $("#ok_button").click(function (event) {
                if (validateForm()) {
                    $("#ok_button").attr("disabled", true);
                    event.preventDefault();
                    $("#loader").html('<img id="loader_image" src="assets/img/loading.gif" />');

                    $.getJSON("http://localhost:4040/Performance_chart.action",
                        {
                            reportingMonthBegin: $('#reportingMonthBegin').val(),
                            reportingYearBegin: $('#reportingYearBegin').val(),
                            reportingMonthEnd: $('#reportingMonthEnd').val(),
                            reportingYearEnd: $('#reportingYearEnd').val(),
                            indicatorId: $('#indicatorId').val()
                        }, function (json) {
                            console.log(json);
                            $("#loader").html('');
                            $("#container").css({minWidth: '400px', height: '400px', margin: '0 auto'});
                            console.log(json);
                            setChart(json);
                            $("#ok_button").attr("disabled", false);
                        });

                }
                return false;
            });

            $("#cancel_button").click(function (event) {
                $("#lamisform").attr("action", "Dashboard_page");
                return true;
            });
        });

        function setChart(json) {
            $('#container').highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: json.title
                },
                subtitle: {
                    text: json.subtitle
                },
                xAxis: {
                    categories: json.categories
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: json.titleForYAxis
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: json.series
            });
        }

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            if ($("#indicatorId").val() == 0) {
                $("#indicatorHelp").html(" *");
                validate = false;
            } else {
                $("#indicatorHelp").html("");
            }
            if ($("#reportingMonthBegin").val().length == 0) {
                $("#periodHelpBegin").html(" *");
                validate = false;
            } else {
                $("#periodHelpBegin").html("");
            }
            if ($("#reportingYearBegin").val().length == 0) {
                $("#periodHelpBegin").html(" *");
                validate = false;
            } else {
                $("#periodHelpBegin").html("");
            }
            if ($("#reportingMonthEnd").val().length == 0) {
                $("#periodHelpEnd").html(" *");
                validate = false;
            } else {
                $("#periodHelpEnd").html("");
            }
            if ($("#reportingYearEnd").val().length == 0) {
                $("#periodHelpEnd").html(" *");
                validate = false;
            } else {
                $("#periodHelpEnd").html("");
            }

            return validate;
        }
    </script>
</head>

<body>
<!-- Navbar -->
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>
<div class="mt-5"></div>
<div class="content col-12 mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item"><a href="Dashboard_page">Visualizer</a></li>
        <li class="breadcrumb-item active">Performance Chart</li>
    </ol>
    <div class="row">
        <div class="col-12">
            <div class="card">
                <!-- <div class="card-header bg-info"> -->
                <!-- <h5 class="card-title text-white">Performance Chart</h5> -->
                <div class="card-body">
                    <s:form id="lamisform" theme="css_xhtml">
                        <div class="row">
                            <div class="form-group col-10">
                                <label>Indicator:</label>
                                <select name="indicatorId" style="width: 100%;" class="form-control"
                                        id="indicatorId">
                                    <option value="0">Select</option>
                                    <option value="1">Proportion of clinic visits during the reporting
                                        period that had a documentation of TB status
                                    </option>
                                    <option value="2">Proportion of clinic visits during the reporting
                                        period that had a documentation of functional status
                                    </option>
                                    <option value="3">Proportion of clinic visits during the reporting
                                        period that had a documentation of weight
                                    </option>
                                    <option value="4">Proportion of clinic visits during the of the
                                        reporting month that had documentation of OI status in ART
                                        patients
                                    </option>
                                    <option value="5">Proportion of clinic visits during the of the
                                        reporting month that had documentation of OI status in non-ART
                                        patients
                                    </option>
                                    <option value="6">Proportion of clinic visits of current ART
                                        patients within the reporting period that had a documentation
                                        of ADR status
                                    </option>
                                    <option value="7">Proportion of pharmacy visits of current ART
                                        patients within the reporting period that had a documentation
                                        of ADR status
                                    </option>
                                    <option value="8">Proportion of current ART patients who had at
                                        least one documented clinical visit in the last 6 months
                                    </option>
                                    <option value="9">Proportion of current ART patients with at least
                                        one CD4 count test done in the last 6 months
                                    </option>
                                    <option value="10">Proportion of current ART patients at least at
                                        one time a minimum set of standard haematology tests done in
                                        the last 6 months
                                    </option>
                                    <option value="11">Proportion of current ART patients with at least
                                        at one time a minimum set of standard chemistry tests done in
                                        the last 6 months
                                    </option>
                                    <option value="12">Proportion of current ART patients who had a
                                        clinical staging done at the last clinical visit prior to the
                                        reporting date
                                    </option>
                                    <option value="13">Proportion of current ART patients reporting an
                                        ADR with severity grade 3 or 4 in the reporting month
                                    </option>
                                    <option value="14">Proportion of current ART patients who picked up
                                        their drugs within 7 days of the appointment day within the
                                        last 3 months
                                    </option>
                                    <option value="15">Proportion of current ART patients who have not
                                        had a refill 3 months after the last refill who have had their
                                        status correctly updated
                                    </option>
                                    <option value="16">Proportion of patients newly initiated on ART <= 5
                                        years old with documented eligibility criteria
                                    </option>
                                    <option
                                            value="17">Proportion of patients newly initiated
                                        on ART > 5 years old with documented eligibility criteria
                                    </option>
                                    <option value="18">Proportion of patients newly initiated on ART
                                        who had at least one clinical staging done prior to ART
                                        commencement
                                    </option>
                                    <option value="19">Proportion of patients newly initiated on ART
                                        with at least one CD4 count test done before ART commencement
                                    </option>
                                    <option value="100">Proportion of patients newly initiated on ART
                                        with at least at one time a minimum set of standard chemistry
                                        tests done before ART commencement
                                    </option>
                                    <option value="21">Proportion of patients newly initiated on ART
                                        with at least at one time a minimum set of standard haematology
                                        tests done before ART commencement
                                    </option>
                                    <option value="22">Proportion of HIV positive patients <= 5 years
                                        initiating cotrimoxazole prophylaxis in the last 6 months
                                    </option>
                                    <option value="23">Proportion of HIV positive patients > 5
                                        years initiating cotrimoxazole prophylaxis in the last 6
                                        months
                                    </option>
                                </select><span id="indicatorHelp" style="color:red"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-10">
                                <label>Reporting Period:</label>
                                <div class="row">
                                    <div class="col-3">
                                        <select name="reportingMonthBegin" style="width: 100%;" class="form-control"
                                                id="reportingMonthBegin"/>
                                        <option value="0">Select</option>
                                        <option value="1">January</option>
                                        <option value="2">February</option>
                                        <option value="3">March</option>
                                        <option value="4">April</option>
                                        <option value="5">May</option>
                                        <option value="6">June</option>
                                        <option value="7">July</option>
                                        <option value="8">August</option>
                                        <option value="9">September</option>
                                        <option value="10">October</option>
                                        <option value="11">November</option>
                                        <option value="12">December</option>
                                        </select>
                                    </div>
                                    <div class="col-2">
                                        <select name="reportingYearBegin" style="width: 100%;" class="form-control"
                                                id="reportingYearBegin">
                                        </select>
                                        <span id="periodHelpBegin" style="color:red"></span>
                                    </div>
                                    <div class="col-1">
                                        <label>to</label>
                                    </div>
                                    <div class="col-3">
                                        <select name="reportingMonthEnd" style="width: 100%;" class="form-control"
                                                id="reportingMonthEnd"/>
                                        <option value="0">Select</option>
                                        <option value="1">January</option>
                                        <option value="2">February</option>
                                        <option value="3">March</option>
                                        <option value="4">April</option>
                                        <option value="5">May</option>
                                        <option value="6">June</option>
                                        <option value="7">July</option>
                                        <option value="8">August</option>
                                        <option value="9">September</option>
                                        <option value="10">October</option>
                                        <option value="11">November</option>
                                        <option value="12">December</option>
                                        </select>
                                    </div>
                                    <div class="col-3">
                                        <select name="reportingYearEnd" style="width: 100%;" class="form-control"
                                                id="reportingYearEnd"/>
                                        </select>
                                        <span id="periodHelpEnd" style="color:red"></span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-10">
                                <div class="">
                                    <button id="ok_button" class="btn btn-info">Generate</button>
                                    <button id="cancel_button" class="btn btn-dark">Close</button>
                                </div>
                            </div>
                        </div>
                    </s:form>
                    <div class="row">
                        <div class="col-10">
                            <div id="loader"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <div class="card card-chart">
                <div class="card-header">
                    <h5 class="card-category">Performance Chart</h5>
                </div>
                <div class="card-body">
                    <div class="chart-area">
                        <div id="container"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<footer class="footer mt-0 mb-0 pt-0 pb-0">
    <nav>
        <ul>
            <li>
                <a href="#">
                    Lamis
                </a>
            </li>
            <li>
                <a href="#">
                    About Us
                </a>
            </li>
        </ul>
    </nav>
    <div class="copyright" id="copyright">
        &copy; 2018
    </div>
</footer>
<!-- END FOOTER -->
</div>
<!-- END MAIN MENU -->
</div>
<!-- END WRAPPER -->
</body>

</html>