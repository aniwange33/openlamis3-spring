<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
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
    <script type="text/javascript" src="assets/chart-asset/slide-chart.js"></script>
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

        .bg-flat-color-0 {
            background: #42a5f5;
        }

        .bg-flat-color-1 {
            background: #5c6bc0;
        }

        .bg-flat-color-2 {
            background: #FF6600;
        }

        .carousel {
            box-sizing: content-box;
        }
    </style>
</head>

<body>
<div class="content col-12 mr-auto ml-auto">
    <s:form theme="css_xhtml">
        <div class="row">
            <div class="col-12">
                <input type="hidden" id="ipId" value="1"/>
                <input type="hidden" id="stateId" value="3"/>
                <input type="hidden" id="lgaId" value="0"/>
                <input type="hidden" id="facilityId" value="0"/>
                <input type="hidden" name="reportingDateBegin" id="reportingDateBegin">
                <input type="hidden" name="reportingDateEnd" id="reportingDateEnd">
                <input type="hidden" name="reportingMonthBegin" id="reportingMonthBegin">
                <input type="hidden" name="reportingMonthEnd" id="reportingMonthEnd">
                <input type="hidden" name="reportingYearBegin" id="reportingYearBegin">
                <input type="hidden" name="reportingYearEnd" id="reportingYearEnd">
            </div>
        </div>
    </s:form>
    <!-- Content Row -->
    <div class="row">
        <div class="col-md-12">
            <h2 id="caption" class="text-center" style="color: #FF6600; font-weight: 600"></h2>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div id="chart-carousel" class="carousel slide carousel-multi-item" data-ride="carousel">

                <!--Indicators-->
                <ol class="carousel-indicators">
                    <li data-target="#chart-carousel" data-slide-to="0" class="active"></li>
                    <li data-target="#chart-carousel" data-slide-to="1"></li>
                    <li data-target="#chart-carousel" data-slide-to="2"></li>
                </ol>
                <!--/.Indicators-->

                <!--Slides-->
                <div class="carousel-inner" role="listbox">
                    <!--First slide-->
                    <div class="carousel-item active">
                        <div class="row">
                            <div class="col-md-12">
                                <h3 class="text-center" style="color: #FF6600; font-weight: bold">Daily Report</h3>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card bg-info">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Tested</div>
                                                <div id="totalTested" class="h5 mb-0 font-weight-bold text-gray-800">0
                                                </div>
                                            </div>
                                            <div class="col-auto">
                                                <i class="fa fa-users fa-2x text-gray-light"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Total Positive -->
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card bg-flat-color-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Positive
                                                </div>
                                                <div class="row no-gutters align-items-center">
                                                    <div class="col-auto">
                                                        <div id="totalPositive"
                                                             class="h5 mb-0 mr-3 font-weight-bold text-gray-800">0
                                                        </div>
                                                    </div>
                                                    <div class="col h5 mb-0 mr-3 font-weight-bold text-gray-800"
                                                         id="positive">
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
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card bg-success">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Initiated on
                                                    ART
                                                </div>
                                                <div class="row no-gutters align-items-center">
                                                    <div class="col-auto">
                                                        <div id="totalInitiated"
                                                             class="h5 mb-0 mr-3 font-weight-bold text-gray-800">0
                                                        </div>
                                                    </div>
                                                    <div class="col h5 mb-0 mr-3 font-weight-bold text-gray-800"
                                                         id="initiated">
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
                    </div>

                    <!--Second slide-->
                    <div class="carousel-item">
                        <div class="row">
                            <div class="col-md-12">
                                <h3 class="text-center" style="color: #FF6600;  font-weight: 600">Weekly Report</h3>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card  rounded bg-info">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Tested</div>
                                                <div id="totalTested2" class="h5 mb-0 font-weight-bold text-gray-800">
                                                    0
                                                </div>
                                            </div>
                                            <div class="col-auto">
                                                <i class="fa fa-users fa-2x text-gray-light"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Total Positive -->
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card  bg-flat-color-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Positive
                                                </div>
                                                <div class="row no-gutters align-items-center">
                                                    <div class="col-auto">
                                                        <div id="totalPositive2"
                                                             class="h5 mb-0 mr-3 font-weight-bold text-gray-800">0
                                                        </div>
                                                    </div>
                                                    <div class="col h5 mb-0 mr-3 font-weight-bold text-gray-800"
                                                         id="positive2">
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
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card  bg-success">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Initiated on
                                                    ART
                                                </div>
                                                <div class="row no-gutters align-items-center">
                                                    <div class="col-auto">
                                                        <div id="totalInitiated2"
                                                             class="h5 mb-0 mr-3 font-weight-bold text-gray-800">0
                                                        </div>
                                                    </div>
                                                    <div class="col h5 mb-0 mr-3 font-weight-bold text-gray-800"
                                                         id="initiated2">
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
                                            <div id="container12"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Proportion of VL results with viral suppression -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container22"></div>
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
                                            <div id="container32"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Proportion of VL results with viral suppression -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container42"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!--Third slide-->
                    <div class="carousel-item">
                        <div class="row">
                            <div class="col-md-12">
                                <h3 class="text-center" style="color: #FF6600;  font-weight: 600">Monthly Report</h3>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card bg-info">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Tested</div>
                                                <div id="totalTested3" class="h5 mb-0 font-weight-bold text-gray-800">
                                                    0
                                                </div>
                                            </div>
                                            <div class="col-auto">
                                                <i class="fa fa-users fa-2x text-gray-light"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Total Positive -->
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card  bg-flat-color-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Positive
                                                </div>
                                                <div class="row no-gutters align-items-center">
                                                    <div class="col-auto">
                                                        <div id="totalPositive3"
                                                             class="h5 mb-0 mr-3 font-weight-bold text-gray-800">0
                                                        </div>
                                                    </div>
                                                    <div class="col h5 mb-0 mr-3 font-weight-bold text-gray-800"
                                                         id="positive3">
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
                            <div class="col-md-4 col-lg-4 mb-2">
                                <div class="card  bg-success">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-white mb-1">Total Initiated on
                                                    ART
                                                </div>
                                                <div class="row no-gutters align-items-center">
                                                    <div class="col-auto">
                                                        <div id="totalInitiated3"
                                                             class="h5 mb-0 mr-3 font-weight-bold text-gray-800">0
                                                        </div>
                                                    </div>
                                                    <div class="col h5 mb-0 mr-3 font-weight-bold text-gray-800"
                                                         id="initiated3">
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
                                            <div id="container13"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Proportion of VL results with viral suppression -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container23"></div>
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
                                            <div id="container33"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Proportion of VL results with viral suppression -->
                            <div class="col-md-6">
                                <div class="card card-chart">
                                    <div class="card-body">
                                        <div class="chart-area">
                                            <div id="container43"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- END CONTENT-->
<div class="card-footer" style="background-color:#666666;">
    <footer class="footer mt-0 mb-0 pt-0 pb-0 text-white text-center">
        <div class="row">
            <div class="col-md-1">
                <a href="#" class="text-white"></a>
            </div>
            <div class="col-md-8">
                <div id="footerMessage">LAMIS</div>
            </div>
        </div>
    </footer>
    <!-- END FOOTER -->
</div>
<!-- END CARD FOOTER -->
</div>
<!-- END MAIN MENU -->
</div>
<!-- END WRAPPER -->

<script>

    $(document).ready(function () {
        var counter = 0;
        var index = 39;
        var initValue = 0;
        let lga = [
            {id: "39", name: "Abak"}, {id: "40", name: "Eastern Obolo"}, {id: "41", name: "Eket"}, {
                id: "42",
                name: "Esit Eket"
            }, {id: "43", name: "Essien Udim"},
            {id: "44", name: "Etim Ekpo"}, {id: "45", name: "Etinan"}, {id: "46", name: "Ibeno"}, {
                id: "47",
                name: "Ibesikpo Asutan"
            },
            {id: "48", name: "Ibiono-Ibom"}, {id: "49", name: "Ika"}, {id: "50", name: "Ikono"}, {
                id: "51",
                name: "Ikot Abasi"
            }, {id: "52", name: "Ikot Ekpene"},
            {id: "53", name: "Ini"}, {id: "54", name: "Itu"}, {id: "55", name: "Mbo"}, {
                id: "56",
                name: "Mkpat-Enin"
            }, {id: "57", name: "Nsit-Atai"}, {id: "58", name: "Nsit-Ibom"},
            {id: "59", name: "Nsit-Ubium"}, {id: "60", name: "Obot Akara"}, {id: "61", name: "Okobo"}, {
                id: "62",
                name: "Onna"
            }, {id: "63", name: "Oron"}, {id: "64", name: "Oruk Anam"},
            {id: "65", name: "Udung-Uko"}, {id: "66", name: "Ukanafun"}, {id: "67", name: "Uruan"}, {
                id: "68",
                name: "Urue-Offong/Oruko"
            }, {id: "69", name: "Uyo"}
        ];

        $("#caption").html("Akwa Ibom");

        $('.carousel').carousel({
            interval: 60000
        });

        var flag = 0;
        setInterval(function () {

            if (flag == 0) {
                $("#caption").html(lga[counter].name + " LGA");
                $("#lgaId").val(index);
                generate();

            } else {
                counter = -1;
                index = 38;
                flag = 0;
                $("#caption").html(" Akwa Ibom State");
                $("#lgaId").val(flag);
                generate();

            }

            if (counter < 30) {
                counter++;
                index++;
            } else {
                flag = 1;
                counter = -1;
                index = 0;
            }

        }, 180000);

        generate();

    });

</script>

</body>

</html>
