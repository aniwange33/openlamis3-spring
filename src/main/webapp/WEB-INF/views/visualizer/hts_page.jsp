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
    <title>LAMIS 3.0</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!--<link type="text/css" rel="stylesheet" href="assets/highchart/css/highcharts.css" />-->
    <link type="text/css" rel="stylesheet" href="assets/chart-asset/daterangepicker.css"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/javascript" src="assets/highchart/highcharts.js"></script>
    <script type="text/javascript" src="assets/highchart/highcharts-3d.js"></script>
    <script type="text/javascript" src="assets/highchart/modules/exporting.js"></script>
    <script type="text/javascript" src="assets/chart-asset/moment.min.js"></script>
    <script type="text/javascript" src="assets/chart-asset/daterangepicker.js"></script>
    <script type="text/javascript" src="assets/chart-asset/hts-chart.js"></script>
    <script type="text/javascript" src="assets/chart-asset/hts-chart-month.js"></script>
    <style>
        body {
            background: #e8e8e8;
        }

        i.fa {
            color: #FFFFFF;
        }

        .form-control {
            height: calc(1.5em + 0.5rem + 1px);
            line-height: inherit;
        }

        .border-5 {
            border-width: 5px !important;
        }


        .bg-flat-color-1 {
            background: #5c6bc0;
        }

        .bg-flat-color-2 {
            background: #FF6600;
        }
    </style>
</head>

<body>
<!-- Navbar -->
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>

<div class="mt-5"></div>
<div class="content col-12 mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Dashboard_page">Visualizer</a></li>
        <li class="breadcrumb-item active">HTS Chart</li>
    </ol>
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <s:form id="lamisform" theme="css_xhtml">
                        <div class="row">
                            <div class="col-md-3 form-group">
                                <label>IP</label>
                                <select name="ipId" id="ipId" class="form-control select2" style="width: 100%;">
                                    <option value="0">FHI360</option>
                                    <!--                                            <option value="1">Global Fund</option>
                                                                                <option value="2">Heartland Alliance</option>-->
                                </select>
                            </div>
                            <div class="col-md-3 form-group">
                                <label>State</label>
                                <select name="stateId" id="stateId" class="form-control select2" style="width: 100%;">
                                    <option value="0">Select</option>
                                </select>
                            </div>
                            <div class="col-md-3 form-group">
                                <label>LGA</label>
                                <select name="lgaId" id="lgaId" class="form-control select2" style="width: 100%;">
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
                        <p>Chart Period</p>

                        <div class="row">
                            <div class="col-md-2">
                                <label>Month</label>
                                <select name="reportingMonthBegin" class="form-control" id="reportingMonthBegin"/>
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
                            </div>
                            <div class="col-md-2">
                                <label>Year</label>
                                <select name="reportingYearBegin" style="width: 100%;" class="form-control"
                                        id="reportingYearBegin"/>
                                <option value="0">Select Year</option>
                                </select><span id="periodHelp1" style="color:red"></span>
                            </div>

                            <div class="col-md-1"><br/><br/>to</div>
                            <div class="col-md-2">
                                <label>Month</label>
                                <select name="reportingMonthEnd" style="width: 100%;" class="form-control"
                                        id="reportingMonthEnd"/>
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
                            </div>
                            <div class="col-md-2">
                                <label>Year</label>
                                <select name="reportingYearEnd" style="width: 100%;" class="form-control"
                                        id="reportingYearEnd"/>
                                <option value="0">Select Year</option>
                                </select><span id="periodHelp2" style="color:red"></span>
                            </div>
                            <div class="col-md-3 form-group">
                                <label>Date </label>
                                <div id="reportrange" class="form-control"
                                     style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc; width: 100%">
                                    <i class="fa fa-calendar-o"></i>&nbsp;
                                    <span></span> <i class="fa fa-caret-square-down"></i>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 form-group">
                                <br/>
                                <input type="hidden" name="reportingDateBegin" id="reportingDateBegin"
                                       class="form-control">
                                <input type="hidden" name="reportingDateEnd" id="reportingDateEnd" class="form-control">
                                <button id="ok_button" type="button" class="btn btn-sm btn-info">Generate</button>
                            </div>
                        </div>
                    </s:form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <span id="loader"></span>
        </div>
    </div>
    <!-- Summary Section -->

    <div class="row">
        <div class="col-xl-4 col-md-4 mb-2">
            <div class="card border-left border-5 border-secondary bg-info">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-white mb-1">Total Tested</div>
                            <div id="totalTested" class="h5 mb-0 font-weight-bold text-gray-800">0</div>
                        </div>
                        <div class="col-auto">
                            <i class="fa fa-users fa-2x text-gray-light"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Total Positive -->
        <div class="col-xl-4 col-md-4 mb-2">
            <div class="card border-left border-5 border-secondary bg-flat-color-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-white mb-1">Total Positive</div>
                            <div class="row no-gutters align-items-center">
                                <div class="col-auto">
                                    <div id="totalPositive" class="h5 mb-0 mr-3 font-weight-bold text-gray-800">0</div>
                                </div>
                                <div class="col" id="positive">
                                </div>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="fa fa-user-plus fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Total Enrolled on ART -->
        <div class="col-xl-4 col-md-4 mb-2">
            <div class="card border-left border-5 border-secondary bg-success">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-white mb-1">Total Initiated on ART</div>
                            <div class="row no-gutters align-items-center">
                                <div class="col-auto">
                                    <div id="totalInitiated" class="h5 mb-0 mr-3 font-weight-bold text-gray-800">0</div>
                                </div>
                                <div class="col" id="initiated">
                                </div>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="fa fa-user-circle-o fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Chart section -->
    <div class="row">
        <!-- Proportion of VL results with viral suppression -->
        <div class="col-md-6">
            <div class="card card-chart">
                <div class="card-body">
                    <div class="chart-area">
                        <div id="container1"></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Proportion of VL results with viral suppression -->
        <div class="col-md-6">
            <div class="card card-chart">
                <div class="card-body">
                    <div class="chart-area">
                        <div id="container2"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <!-- Proportion of VL results with viral suppression -->
        <div class="col-md-6">
            <div class="card card-chart">
                <div class="card-body">
                    <div class="chart-area">
                        <div id="container3"></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Proportion of VL results with viral suppression -->
        <div class="col-md-6">
            <div class="card card-chart">
                <div class="card-body">
                    <div class="chart-area">
                        <div id="container4"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="card card-chart">
                <div class="card-body">
                    <div class="chart-area">
                        <div id="container5"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card card-chart shadow">
                <div class="card-body">
                    <div class="chart-area">
                        <div id="container6"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="user_group1" style="display: none"><s:property value="#session.userGroup"/></div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
    <script type="text/JavaScript">

        for (i = new Date().getFullYear(); i > 1900; i--) {
            $("#reportingYearBegin").append($("<option/>").val(i).html(i));
            $("#reportingYearEnd").append($("<option/>").val(i).html(i));
        }


        $(document).ready(function () {

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
                    'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                    //   'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                    //   'This Month': [moment().startOf('month'), moment().endOf('month')],
                    //   'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                }
            }, cb);

            cb(start, end);

            $("#ok_button").bind("click", function () {
                event.preventDefault();
                //\\if (date_diff_indays(start, end) > 7){
                // toastr.info("You cannot select more than a week", "Notifications");
                // }
                if ($("#reportingMonthBegin").val() != 0 && $("#reportingYearBegin").val() != 0 &&
                    $("#reportingMonthEnd").val() != 0 && $("#reportingMonthEnd").val() != 0) {
                    generateByMonth();
                } else {
                    generate();
                }

                return false;
            });

            generate();

        });


    </script>

</body>
</html>

