<%-- 
    Document   : Patient
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 2.3</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!--<link type="text/css" rel="stylesheet" href="assets/highchart/css/highcharts.css" />-->
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="assets/chart-asset/daterangepicker.css"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/javascript" src="js/highchart/highcharts.js"></script>
    <script type="text/javascript" src="js/highchart/highcharts-3d.js"></script>
    <script type="text/javascript" src="js/highchart/modules/exporting.js"></script>
    <script type="text/javascript" src="js/highchart/moment.min.js"></script>
    <script type="text/javascript" src="js/highchart/daterangepicker.js"></script>
    <script type="text/javascript" src="js/lamis/dashboard.js"></script>
    <script type="text/javascript" src="js/lamis/dashboard-month.js"></script>
    <style>
        .highcharts-container {
            width: 100%;
            height: 100%;
        }

        i.fa {
            font-size: 36px;
            color: #FFFFFF;
        }

        .card-title {
            font-size: 15px;
            color: #fffff;
            font-weight: bold;
        }

        .ninety {
            background-color: #FF6600;
            color: #FFFFFF;
        }

        .bgInfo {
            background-color: rgb(42, 87, 136);
        }

        .bgGrey {
            background-color: #001aa3 \9;
        }

        .bg-flat-color-1 {
            background: #5c6bc0;
        }

        .bg-flat-color-2 {
            background: #FF6600;
        }

        .form-control {
            font-size: 16px;
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

            $('.datepicker').datepicker({
                format: 'mm/dd/yyyy'
            });

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

            var start = moment().subtract(6, 'days');
            var end = moment();

//            var date_diff_indays = function(date1, date2) {
//            dt1 = new Date(date1);
//            dt2 = new Date(date2);
//            return Math.floor((Date.UTC(dt2.getFullYear(), dt2.getMonth(), dt2.getDate()) - Date.UTC(dt1.getFullYear(), dt1.getMonth(), dt1.getDate()) ) /(1000 * 60 * 60 * 24));
//            }

            function cb(start, end) {
                // $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
                //    $('#reportrange span').html(start.format('D/M/YYYY') + ' - ' + end.format('D/M/YYYY'));
                $('#reportingDateBegin').val(start.format('YYYY-M-D'));
                $('#reportingDateEnd').val(end.format('YYYY-M-D'));
                $('#reportingLabel').val(start.format('MM/DD/YYYY'));
                $('#reportingLabel2').val(end.format('MM/DD/YYYY'));

            }


//            $('#reportrange').daterangepicker({
//            // "singleDatePicker": true,
//            "showISOWeekNumbers": true,
//            startDate: start,
//            endDate: end,
//            ranges: {
//            'Today': [moment(), moment()],
//            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
//            'This Week': [moment().startOf('isoWeek'), moment().endOf('isoWeek')],
//            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
//            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//            'This Month': [moment().startOf('month'), moment().endOf('month')],
//            }
//            }, cb);

            cb(start, end);

            $("#ok_button").bind("click", function () {
                event.preventDefault();
                startDate = formatDate($('#reportingLabel').val());
                endDate = formatDate($('#reportingLabel2').val());

                console.log('Date: ' + startDate);
                console.log('end Date: ' + endDate);

                $('#reportingDateBegin').val(startDate);
                $('#reportingDateEnd').val(endDate);
                $("#reportLabel").html("Reporting Period: " + $('#reportingLabel').val() + " to " + $('#reportingLabel2').val());
                generate();
                return false;
            });

            function formatDate(dateInput) {
                var date_convert = new Date(dateInput);
                var dy = date_convert.getDate();
                var mth = date_convert.getMonth() + 1;
                var yr = date_convert.getFullYear();
                str_date = yr + '-' + mth + '-' + dy;
                return str_date;
            }

            $("#reportLabel").html("Reporting Period: " + $('#reportingLabel').val() + " to " + $('#reportingLabel2').val());
            generate();

        });
    </script>

</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>
    <div id="mainPanel">
        <div class="mt-5"></div>
        <div class="content col-12 mr-auto ml-auto">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/dashboard">Visualizer</a></li>
                <li class="breadcrumb-item active">Treatment Dashboard</li>
            </ol>
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <form id="lamisform" theme="css_xhtml">
                                <div class="row">
                                    <div class="col-md-3 form-group">
                                        <label>IP</label>
                                        <select name="ipId" id="ipId" class="form-control select2" style="width: 100%;">
                                            <option value="0">FHI360</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3 form-group">
                                        <label>State</label>
                                        <select name="stateId" id="stateId" class="form-control select2"
                                                style="width: 100%;">
                                            <option value="0">Select</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3 form-group">
                                        <label>LGA</label>
                                        <select name="lgaId" id="lgaId" class="form-control select2"
                                                style="width: 100%;">
                                            <option value="0">Select</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3 form-group">
                                        <label>Facility</label>
                                        <select name="facilityId" id="facilityId" class="form-control select2"
                                                style="width: 100%;">
                                            <option value="0">Select</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3 form-group">
                                        <label>Date From</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text">
                                                    <i class="fa fa-calendar" style="font-size:16px; color: #ccc;"></i>
                                                </div>
                                            </div>
                                            <input type="text" name="reportingLabel" id="reportingLabel"
                                                   class="form-control datepicker">
                                        </div>
                                    </div>
                                    <div class="col-md-3 form-group">
                                        <label>Date To</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text">
                                                    <i class="fa fa-calendar" style="font-size:16px; color: #ccc;"></i>
                                                </div>
                                            </div>
                                            <input type="text" name="reportingLabel2" id="reportingLabel2"
                                                   class="form-control datepicker">
                                        </div>
                                    </div>
                                    <div class="col-md-4 form-group">
                                        <br/>
                                        <button id="ok_button" type="button" class="btn btn-sm btn-info">Generate
                                        </button>
                                        <input type="hidden" name="reportingDateBegin" id="reportingDateBegin"
                                               class="form-control datepicker">
                                        <input type="hidden" name="reportingDateEnd" id="reportingDateEnd"
                                               class="form-control">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <div id="loader"></div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 mr-auto ml-auto">
                    <h3 id="reportLabel" style="text-align:center;"></h3>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <!-- Summary Section -->
                    <div class="row">
                        <div class="col-md-4">
                            <div class="card card-stats bgInfo">
                                <div class="card-body ">
                                    <div class="statistics statistics-horizontal">
                                        <div class="info info-horizontal">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <h6 style="color: #fff; font-weight: bold">Cumulative
                                                        Enrollment</h6>
                                                </div>
                                                <div class="col-5">
                                                    <h2 class="btn btn-lg btn-round ninety"><strong>1ST 90%</strong>
                                                    </h2>
                                                </div>
                                                <div class="col-7 text-right">
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
                                                <div class="col-md-12">
                                                    <h6 style="color: #fff; font-weight: bold">Cumulative Current On
                                                        ART</h6>
                                                </div>
                                                <div class="col-5">
                                                    <h2 class="btn btn-lg btn-round ninety"><strong>2ND 90%</strong>
                                                    </h2>
                                                </div>
                                                <div class="col-7 text-right">
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
                                                <div class="col-md-12">
                                                    <h6 style="color: #fff; font-weight: bold">Viral Load
                                                        Suppression</h6>
                                                </div>
                                                <div class="col-5">
                                                    <h2 class="btn btn-lg btn-round ninety"><strong>3RD 90%</strong>
                                                    </h2>
                                                </div>
                                                <div class="col-7 text-right">
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
                    <!-- Chart section -->
                    <div class="row">
                        <!-- Number eligible for Viral Load vs Test done -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container1"></div>
                                    </div>
                                </div>

                            </div>
                        </div>

                        <!-- Proportion of VL results with viral suppression -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container2"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Proportion of unspressed patients who attended EAC within 1 month of VL result -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container3"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Clinic visit with documented TB status -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container4"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Ever enrolled vs Currently in Care -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container5"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Newly enrolled (disaggregated by sex) -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container6"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Current on ART (disaggregated by sex) -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container7"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Number of those enrolled vs started on ART -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container8"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Number of missed appointment for ART refill  -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container9"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Proportion of defaulters that returned to care within 1 month -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container10"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Number of Current Patients Transitioned to TLD -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container11"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--Number on TLD Age & Sex-->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container12"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Number of Current Patients On DMOC  -->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container13"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Number on DMOC Age & Sex-->
                        <div class="col-md-12">
                            <div class="card card-chart">
                                <div class="card-body" style="height: 20rem;">
                                    <div class="chart-area">
                                        <div id="container14"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="footer">
            <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
        </div>
</body>
</html>

