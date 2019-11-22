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
    <title>LAMIS 3.0</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/javascript" src="assets/chart-asset/moment.min.js"></script>
    <script type="text/javascript" src="assets/highchart/highcharts.js"></script>
    <script type="text/javascript" src="assets/highchart/highcharts-3d.js"></script>
    <script type="text/javascript" src="assets/highchart/modules/exporting.js"></script>
    <script type="text/javascript" src="assets/js/dashboard_tv.js"></script>
    <style>
        i.fa {
            font-size: 24px;
            color: #FFFFFF;
        }

        .card-category {
            font-size: 15px;
            color: #000;
            font-weight: bold;
        }

        #container {
            height: 500px;
            min-width: 310px;
            max-width: 800px;
            margin: 0 auto;
        }

        .loading {
            margin-top: 10em;
            text-align: center;
            color: gray;
        }
    </style>
</head>

<body>
<div class="content col-12 mr-auto ml-auto">
    <div class="row">
        <div class="col-12">
            <input type="hidden" id="ipId" value="1"/>
            <input type="hidden" id="stateId" value="0"/>
            <input type="hidden" id="lgaId" value="0"/>
            <input type="hidden" id="facilityId" value="0"/>
            <input type="hidden" name="reportingDateBegin" id="reportingDateBegin">
            <input type="hidden" name="reportingDateEnd" id="reportingDateEnd">
            <h2 id="caption" class="text-center" style="color: #FF6600; font-weight: bold"></h2>
        </div>
    </div>

    <!-- Summary Section -->
    <div class="row">
        <div class="col-md-3">
            <div class="row">
                <div class="col-md-12 mt-0 mb-0">
                    <div class="card card-stats bg-info">
                        <div class="card-body ">
                            <div class="statistics statistics-horizontal">
                                <div class="info info-horizontal">
                                    <div class="row">
                                        <div class="col-md-12 text-right">
                                            <h6 class="card-category">Cummulative Enrollment</h6>
                                        </div>
                                        <div class="col-8">
                                            <div class="card card-pricing">
                                                <h6 class="info-title text-center"
                                                    style="color: #FF6600; font-weight: bold">1st 90% <br/>Diagnosed
                                                </h6>
                                            </div>
                                        </div>
                                        <div class="col-4 text-right mt-0 mb-0">
                                            <h6 class="info-title mt-0 mb-1"><i class="fa fa-female"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="femaleEnrolled"></span></h6>
                                            <h6 class="info-title mt-0 mb-1"><i class="fa fa-male"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="maleEnrolled"></span></h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 mt-0 mb-0">
                    <div class="card card-stats" style="background-color: #ABAFA6;">
                        <div class="card-body ">
                            <div class="statistics statistics-horizontal">
                                <div class="info info-horizontal">
                                    <div class="row">
                                        <div class="col-md-12 text-right">
                                            <h6 class="card-category">Current On ART</h6>
                                        </div>
                                        <div class="col-8">
                                            <div class="card">
                                                <h6 class="info-title text-center"
                                                    style="color: #FF6600; font-weight: bold">2nd 90% <br/>Treatment
                                                </h6>
                                            </div>
                                        </div>
                                        <div class="col-4 text-right mt-0 mb-0">
                                            <h6 class="info-title mt-0 mb-1"><i class="fa fa-female"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="femaleCurrent"></span></h6>
                                            <h6 class="info-title mt-0 mb-1"><i class="fa fa-male"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="maleCurrent"></span></h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 mt-0 mb-0">
                    <div class="card card-stats" style="background-color: #99CC33;">
                        <div class="card-body ">
                            <div class="statistics statistics-horizontal">
                                <div class="info info-horizontal">
                                    <div class="row">
                                        <div class="col-md-12 text-right">
                                            <h6 class="card-category">Viral Load Suppression</h6>
                                        </div>
                                        <div class="col-8">
                                            <div class="card">
                                                <h6 class="info-title text-center"
                                                    style="color: #FF6600; font-weight: bold">3rd 90% <br/>Virally
                                                    Suppressed</h6>
                                            </div>
                                        </div>
                                        <div class="col-4 text-right">
                                            <h6 class="info-title mt-0 mb-1"><i class="fa fa-female"></i> <span
                                                    style="color: #fff; font-weight: bold; font-family: Arial, Helvetica, sans-serif; font-size: 15px;"
                                                    id="femaleViralLoad"></span></h6>
                                            <h6 class="info-title mt-0 mb-1"><i class="fa fa-male"></i> <span
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
        </div>
        <!-- ./Summary -->
        <div class="col-md-9">
            <!--Carousel Wrapper-->
            <div id="chart-carousel" class="carousel slide carousel-multi-item" data-ride="carousel">

                <!--Indicators-->
                <ol class="carousel-indicators">
                    <li data-target="#chart-carousel" data-slide-to="0" class="active"></li>
                    <li data-target="#chart-carousel" data-slide-to="1"></li>
                    <li data-target="#chart-carousel" data-slide-to="2"></li>
                    <li data-target="#chart-carousel" data-slide-to="3"></li>
                    <li data-target="#chart-carousel" data-slide-to="4"></li>
                    <li data-target="#chart-carousel" data-slide-to="5"></li>
                    <li data-target="#chart-carousel" data-slide-to="6"></li>
                    <li data-target="#chart-carousel" data-slide-to="7"></li>
                </ol>
                <!--/.Indicators-->

                <!--Slides-->
                <div class="carousel-inner" role="listbox">

                    <!--First slide-->
                    <div class="carousel-item active">
                        <div class="row">
                            <!-- Newly enrolled -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container6"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Number of patient’s enrolled vs no started ART -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container8"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--/.First slide-->

                    <!--Second slide-->
                    <div class="carousel-item">
                        <div class="row">
                            <!-- Cumulative enrollment vs. Current in care -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container5"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Current on ART (disaggregated by gender) -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container7"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--/.Second slide-->

                    <!--Third slide-->
                    <div class="carousel-item">
                        <div class="row">
                            <!-- No of patients eligible for viral load vs Viral load test done -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container1"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Viral suppression rate -->
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

                    </div>
                    <!--/.Third slide-->

                    <!-- Fourth slide-->
                    <div class="carousel-item">
                        <div class="row">
                            <!-- Proportion of unsuppressed patient who had EAC within 1 month (vertical orientation is preferable) -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container3"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Clients with documented TB screening -->
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

                    </div>
                    <!--/.Fourth slide-->

                    <!--Fifth slide-->
                    <div class="carousel-item">
                        <div class="row">
                            <!-- No of missed appointment within the month (TX_ML) -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container9"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Proportion of defaulters that returned to care within 1 month. -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container10"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                    <!--/.Fifth slide-->

                    <!--Six slide-->
                    <div class="carousel-item">
                        <div class="row">
                            <!-- Number of clients on TLD (disaggregated by age & sex) -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container12"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- TLD transition rate -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container11"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                    <!--/.Six slide-->

                    <!--Seven slide-->
                    <div class="carousel-item">
                        <div class="row">
                            <!-- Number of current patients on DMOC -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container13"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- DMOC type -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container14"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                    <!--/.Seven slide-->

                </div>
                <!--/.Slides-->

                <!--Controls-->
                <a class="carousel-control-prev" href="#chart-carousel" role="button" data-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="carousel-control-next" href="#chart-carousel" role="button" data-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                </a>
                <!--/.Controls-->
            </div>
            <!--/.Carousel Wrapper-->
        </div>
        <div class="col-md-12">
            <h6 class="card-title text-center" style="color: #ff6600; font-weight: bold">LAMIS 3.0</h6>
        </div>
    </div>
    <div class="col-md-6 mr-auto ml-auto">
        <marquee direction="right"><strong>FHI360 Health Informatics Team</strong></marquee>
    </div>
    <div id="user_group1" style="display: none"></div>
</body>
<script type="text/javascript">
    var counter = 1;
    var initValue = 0;
    var ipId = 1;
    let states = [{state: "Abia"}, {state: "Adamawa"}, {state: "Anambra"}, {state: "Akwa Ibom"}, {state: "Bauchi"}, {state: "Bayelsa"}, {state: "Benue"}, {state: "Borno"}, {state: "Cross River"}, {state: "Delta"}, {state: "Ebonyi"}, {state: "Enugu"}, {state: "Edo"}, {state: "Ekiti"}, {state: "FCT"}, {state: "Gombe"}, {state: "Imo"}, {state: "Jigawa"}, {state: "Kaduna"}, {state: "Kano"}, {state: "Katsina"}, {state: "Kebbi"}, {state: "Kogi"}, {state: "Kwara"}, {state: "Lagos"}, {state: "Nasarawa"}, {state: "Niger"}, {state: "Ogun"}, {state: "Ondo"}, {state: "Osun"}, {state: "Oyo"}, {state: "Plateau"}, {state: "Rivers"}, {state: "Sokoto"}, {state: "Taraba"}, {state: "Yobe"}, {state: "Zamfara"}];

    setInterval(function () {
        $("#caption").html(states[counter].state + " State");
        $("#ipId").val(ipId);
        $("#stateId").val(counter);
        $("#lgaId").val(initValue);
        $("#id").val(initValue);

        generate();
        if (counter < 3)
            counter++;
        else
            counter = 0;
    }, 10000);
</script>
<script type="text/JavaScript">
    $(function () {
        var start = moment().subtract(6, 'days');
        var end = moment();
        $('#reportingDateBegin').val(start.format('YYYY-M-D'));
        $('#reportingDateEnd').val(end.format('YYYY-M-D'));

        console.log("Start date: " + $('#reportingDateBegin').val());
        console.log("End date: " + $('#reportingDateEnd').val());

        $('.carousel').carousel({
            interval: 5000
        });
        // $("#caption").html("<img src='assets/img/fhi360_logo.PNG' alt='FHI360 -'/> " + states[0].state + " State");
        $("#caption").html(states[0].state + " State");
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

    });

    $(document).ready(function () {
        generate();

        $.ajax({
            url: "StateId_retrieve.action",
            dataType: "json",
            success: function (stateMap) {
                var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
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
                    var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
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
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(facilityMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#id").html(options);
                }
            }); //end of ajax call
        });

        $("#ok_button").bind("click", function () {
            generate();
            return false;
        });

    });

</script>

</html>