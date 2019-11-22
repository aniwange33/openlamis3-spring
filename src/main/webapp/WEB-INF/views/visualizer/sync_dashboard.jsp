<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>LAMIS 2.6</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <link type="text/css" rel="stylesheet" href="js/highchart/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/toastr.min.css"/>
    <link type="text/css" rel="stylesheet" href="js/highchart/daterangepicker.css"/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="js/highchart/font-awesome/css/font-awesome.min.css">

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/highchart/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/toastr.min.js"></script>
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/javascript" src="js/highchart/highcharts.js"></script>
    <script type="text/javascript" src="js/highchart/highcharts-3d.js"></script>
    <script type="text/javascript" src="js/highchart/modules/exporting.js"></script>
    <script type="text/javascript" src="js/highchart/moment.min.js"></script>
    <script type="text/javascript" src="js/highchart/daterangepicker.js"></script>
    <script type="text/javascript" src="js/highchart/moment.min.js"></script>
    <script type="text/javascript" src="js/highchart/daterangepicker.js"></script>
    <script type="text/javascript" src="js/lamis/sync-chart-common.js"></script>
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
    <!-- Content Row -->
    <div class="row">
        <div class="col-md-12">
            <h2 id="caption" class="text-center" style="color: #FF6600; font-weight: 600"></h2>
        </div>
    </div>
    <!-- Chart section -->
    <div class="row">
        <!-- Proportion of VL results with viral suppression -->
        <div class="col-md-12">
            <div class="card card-chart">
                <div class="card-body">
                    <div class="chart-area">
                        <div id="container1"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

<!-- END CARD FOOTER -->
<div class="card-footer" style="background-color:#666666;">
    <div class="row">
        <div class="col-md-12 mr-auto ml-auto">
            <div style="text-align: center; height: 25px; color: #CD853F; font-weight: bold;font-family: Arial, Helvetica, sans-serif; font-size: 17px;">
                LAMIS
            </div>
        </div>
    </div>
    <!-- END FOOTER -->
</div>
<script>
    $(document).ready(function () {
        generate();

        setInterval(generate, 1800000);
    });

</script>

</body>

</html>
