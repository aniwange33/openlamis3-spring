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
    <link type="text/css" rel="stylesheet" href="assets/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/toastr.min.css"/>
    <link type="text/css" rel="stylesheet" href="assets/chart-asset/daterangepicker.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <!--<script type="text/javascript" src="js/lamis/dashboard-common.js"></script>-->
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/toastr.min.js"></script>
    <script type="text/javascript" src="js/json.min.js"></script>
    <!--        <script type="text/javascript" src="js/highcharts.js"></script>
            <script type="text/javascript" src="js/modules/exporting.js"></script>-->
    <script type="text/javascript" src="assets/highchart/highcharts.js"></script>
    <script type="text/javascript" src="assets/highchart/highcharts-3d.js"></script>
    <script type="text/javascript" src="assets/highchart/modules/exporting.js"></script>
    <script type="text/javascript" src="assets/chart-asset/moment.min.js"></script>
    <script type="text/javascript" src="assets/chart-asset/daterangepicker.js"></script>
    <script type="text/javascript" src="assets/chart-asset/moment.min.js"></script>
    <script type="text/javascript" src="assets/chart-asset/daterangepicker.js"></script>
    <script type="text/javascript" src="js/lamis/dashboard.js"></script>
    <script type="text/javascript" src="js/lamis/dashboard-month.js"></script>
    <style>
        .buttons button {
            clear: both;
            width: 82px;
            border: none;
            height: 30px;
            text-align: center;
            font-size: 11px;
            background-color: #0F9AF7;
            color: #FFF;
        }
    </style>
    <script type="text/JavaScript">


        (function (H) {
            H.wrap(H.Chart.prototype, 'showResetZoom', function (proceed) {
            });
        }(Highcharts));

        Highcharts.theme = {

            colors: ['#FF6600', '#2a5788', '#FFCC00', '#DDDF00', '#24CBE5', '#64E572',
                '#FF9655', '#FFF263', '#6AF9C4'],
            chart: {
                backgroundColor: {
                    linearGradient: [0, 0, 500, 500],
                    stops: [
                        [0, 'rgb(255, 255, 255)'],
                        [1, 'rgb(240, 240, 255)']
                    ]
                },
            },
            title: {
                style: {
                    color: '#000',
                    font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
                }
            },
            subtitle: {
                style: {
                    color: '#666666',
                    font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
                }
            },

            legend: {
                itemStyle: {
                    font: '9pt Trebuchet MS, Verdana, sans-serif',
                    color: 'black'
                },
                itemHoverStyle: {
                    color: 'gray'
                }
            }
        };

        // Apply the theme
        Highcharts.setOptions(Highcharts.theme);


        $(document).ready(function () {
            for (i = new Date().getFullYear(); i > 1900; i--) {
                $("#reportingYearBegin").append($("<option/>").val(i).html(i));
                $("#reportingYearEnd").append($("<option/>").val(i).html(i));
            }

            generate();

            $.ajax({
                url: "StateId_retrieve.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '0'>" + 'Select' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }); //end each
                    $("#stateId").html(options);
                }
            }); //end of ajax call

            $("#stateId").change(function (event) {
                $.ajax({
                    url: "LgaId_retrieve.action",
                    dataType: "json",
                    data: {stateId: $("#stateId").val()},
                    success: function (lgaMap) {
                        var options = "<option value = '0'>" + 'Select' + "</option>";
                        $.each(lgaMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }); //end each
                        $("#lgaId").html(options);
                    }
                }); //end of ajax call
            });

            $("#lgaId").change(function (event) {
                $.ajax({
                    url: "Facility_retrieve.action",
                    dataType: "json",
                    data: {stateId: $("#stateId").val(), lgaId: $("#lgaId").val()},
                    success: function (facilityMap) {
                        var options = "<option value = '0'>" + '' + "</option>";
                        $.each(facilityMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }) //end each
                        $("#id").html(options);
                    }
                }); //end of ajax call
            });

            toastr.options = {
                "closeButton": false,
                "debug": false,
                "newestOnTop": false,
                "progressBar": false,
                "positionClass": "toast-bottom-right",
                "preventDuplicates": false,
                "showDuration": "30000",
                "hideDuration": "1000",
                "timeOut": "30000",
                "extendedTimeOut": "3000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut",
            }

            var start = moment().subtract(6, 'days');
            var end = moment();

            var date_diff_indays = function (date1, date2) {
                dt1 = new Date(date1);
                dt2 = new Date(date2);
                return Math.floor((Date.UTC(dt2.getFullYear(), dt2.getMonth(), dt2.getDate()) - Date.UTC(dt1.getFullYear(), dt1.getMonth(), dt1.getDate())) / (1000 * 60 * 60 * 24));
            }

            function cb(start, end) {
                // $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
                $('#reportrange span').html(start.format('D/M/YYYY') + ' - ' + end.format('D/M/YYYY'));
                $('#reportingDateBegin').val(start.format('YYYY-M-D'));
                $('#reportingDateEnd').val(end.format('YYYY-M-D'));

                if (date_diff_indays(start, end) > 7) {
                    toastr.info("You cannot select more than a week", "Notifications");
                }

            }


            $('#reportrange').daterangepicker({
                // "singleDatePicker": true,
                "showISOWeekNumbers": true,
                startDate: start,
                endDate: end,
                ranges: {
                    'Today': [moment(), moment()],
                    'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                    'This Week': [moment().startOf('isoWeek'), moment().endOf('isoWeek')],
                    'Last 7 Days': [moment().subtract(6, 'days'), moment()]
                }
            }, cb);

            cb(start, end);

            $("#ok_button").bind("click", function () {
                event.preventDefault();
                if (date_diff_indays(start, end) > 7 && $("#reportingMonthBegin").val() == "") {
                    toastr.info("You cannot select more than a week", "Notifications");
                }
                if ($("#reportingMonthBegin").val() != 0 && $("#reportingYearBegin").val() != 0 &&
                    $("#reportingMonthEnd").val() != 0 && $("#reportingMonthEnd").val() != 0) {
                    generateByMonth();
                } else {
                    if (date_diff_indays(start, end) <= 7) {
                        generate();
                    }
                }
                return false;
            });

        });
    </script>

</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">
        <jsp:include page="/WEB-INF/views/template/nav_visualizer.jsp"/>
        <div id="rightPanelScroll">
            <table width="100%">
                <tbody>
                <tr>
                    <td>IP</td>
                    <td>
                        <select name="ipId" id="ipId" class="inputboxes" style="width: 100%;">
                            <option value="1">FHI360</option>
                        </select>
                    </td>
                    <td>State</td>
                    <td>
                        <select name="stateId" id="stateId" class="inputboxes" style="width: 100%;">
                            <option value="0">Select</option>
                        </select>
                    </td>
                    <td>
                        LGA
                    </td>
                    <td>
                        <select name="lgaId" id="lgaId" class="inputboxes" style="width: 100%;">
                            <option value="0">Select</option>
                        </select>
                    </td>
                    <td>
                        Facility
                    </td>
                    <td>
                        <select name="facilityId" id="facilityId" class="inputboxes"
                                style="width: 100%;">
                            <option value="0">Select</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">Reporting Period</td>
                </tr>
                <tr>
                    <td>Month</td>
                    <td>
                        <select name="reportingMonthBegin" style="width: 100%;" class="inputboxes"
                                id="reportingMonthBegin">
                            <option value="0">Select Month</option>
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
                    </td>
                    <td>Year</td>
                    <td>
                        <select name="reportingYearBegin" style="width: 100%;" class="inputboxes"
                                id="reportingYearBegin">
                            <option value="0">Select Year</option>
                        </select><span id="periodHelp1" style="color:red"></span>
                    </td>
                    <td>Month</td>
                    <td>
                        <select name="reportingMonthEnd" style="width: 100%;" class="inputboxes" id="reportingMonthEnd">
                            <option value="0">Select Month</option>
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
                    </td>
                    <td>Year</td>
                    <td>
                        <select name="reportingYearEnd" style="width: 100%;" class="inputboxes" id="reportingYearEnd">
                            <option value="0">Select Year</option>
                        </select><span id="periodHelp2" style="color:red"></span>
                    </td>
                </tr>
                <tr>
                    <td>Date Range</td>
                    <td>
                        <div id="reportrange" class="form-control"
                             style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc; width: 100%">
                            <i class="fa fa-calendar-o"></i>&nbsp;
                            <span></span> <i class="fa fa-caret-square-down"></i>
                        </div>
                    </td>
                    <td colspan="4">
                        <input type="hidden" name="reportingDateBegin" id="reportingDateBegin" class="form-control">
                        <input type="hidden" name="reportingDateEnd" id="reportingDateEnd" class="form-control">
                        <button id="ok_button" class="buttons">Generate</button>
                    </td>
                </tr>
                </tbody>
            </table>
            <hr></hr>
            <div id="loader"></div>
            <!-- Summary Section -->
            <div class="row">
                <div class="col-md-4">
                    <div class="card card-stats bg-info">
                        <div class="card-body ">
                            <div class="statistics statistics-horizontal">
                                <div class="info info-horizontal">
                                    <div class="row">
                                        <div class="col-5">
                                            <h2 class="btn btn-lg btn-primary btn-round"><strong>1ST 90%</strong></h2>
                                        </div>
                                        <div class="col-7 text-right">
                                            <h6 class="card-category">Cumulative Enrollment</h6>
                                            <h6 class="info-title"><i class="fa fa-female"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="femaleEnrolled"></span></h6>
                                            <h6 class="info-title"><i class="fa fa-male"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="maleEnrolled"></span></h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-stats" style="background-color: #ABAFA6;">
                        <div class="card-body ">
                            <div class="statistics statistics-horizontal">
                                <div class="info info-horizontal">
                                    <div class="row">
                                        <div class="col-4">
                                            <h2 class="btn btn-lg btn-primary btn-round"><strong>2ND 90%</strong></h2>
                                        </div>
                                        <div class="col-8 text-right">
                                            <h6 class="card-category">Cumulative Current On ART</h6>
                                            <h6 class="info-title"><i class="fa fa-female"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="femaleCurrent"></span></h6>
                                            <h6 class="info-title"><i class="fa fa-male"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="maleCurrent"></span></h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-stats" style="background-color: #99CC33;">
                        <div class="card-body ">
                            <div class="statistics statistics-horizontal">
                                <div class="info info-horizontal">
                                    <div class="row">
                                        <div class="col-5">
                                            <h2 class="btn btn-lg btn-primary btn-round"><strong>3RD 90%</strong></h2>
                                        </div>
                                        <div class="col-7 text-right">
                                            <h6 class="card-category">Viral Load Suppression</h6>
                                            <h6 class="info-title"><i class="fa fa-female"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="femaleViralLoad"></span></h6>
                                            <h6 class="info-title"><i class="fa fa-male"></i><span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="maleViralLoad"></span></h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="chart1">
                <div id="container1"></div>
            </div>
            <div id="chart2">
                <div id="container2"></div>
            </div>
            <div id="chart3">
                <div id="container3"></div>
            </div>
            <div id="chart4">
                <div id="container4"></div>
            </div>
            <div id="chart5">
                <div id="container5"></div>
            </div>
            <div id="chart6">
                <div id="container6"></div>
            </div>
            <div id="chart7">
                <div id="container7"></div>
            </div>
            <div id="chart8">
                <div id="container8"></div>
            </div>
            <div id="chart7">
                <div id="container9"></div>
            </div>
            <div id="chart8">
                <div id="container10"></div>
            </div>
            <div id="chart7">
                <div id="container11"></div>
            </div>
            <div id="chart8">
                <div id="container12"></div>
            </div>
            <div id="chart7">
                <div id="container13"></div>
            </div>
            <div id="chart8">
                <div id="container14"></div>
            </div>
        </div>
    </div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>

