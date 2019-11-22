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
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/javascript" src="assets/highchart/highcharts.js"></script>
    <script type="text/javascript" src="assets/highchart/modules/exporting.js"></script>
    <script type="text/JavaScript">
        var facilityIds = [];
        $(document).ready(function () {

            //initialize();
            reports();

            $.ajax({
                url: "Sync_summary.action",
                dataType: "json",
                success: function (obj) {
                    console.log(obj);
                    $("#facilitySyncing").html(obj.syncMap["facilitySyncing"]);
                    $("#facilityExpected").html(obj.syncMap["facilityExpected"]);
                }
            }); //end of ajax call

            $("#grid").jqGrid({
                url: "Sync_analyzer.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["State", "Facility", "Last Sync", "", "", ""],
                colModel: [
                    {name: "state", index: "state", width: "320"},
                    {name: "facility", index: "facility", width: "560"},
                    {name: "dateLastSync", index: "dateLastSync", width: "220", align: 'center'},
                    {name: "status", index: "status", width: "50"},
                    {name: "sync", index: "sync", width: "0", hidden: true},
                    {name: "facilityId", index: "facilityId", width: "100", hidden: true},
                ],
                rowNum: -1,
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 180,
                loadtext: "Analyzing data, this may take some minutes...",
                jsonReader: {
                    root: "syncList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false
                },
                loadComplete: function () {
                    var gridIds = $("#grid").getDataIDs();
                    for (i = 0; i < gridIds.length; i++) {
                        var data = $("#grid").getRowData(gridIds[i]);
                        if (data.sync == "0") {
                            $("#grid").jqGrid('setCell', i + 1, 'status', '', {background: '#ff9933'});
                        } else {
                            $("#grid").jqGrid('setCell', i + 1, 'status', '', {background: '#ccff33'});
                        }
                    }
                    $("#loader").html('');
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    $("#id").val(data.facilityId);
                    $("#loader").html('<img id="loader_image" src="assets/img/loading.gif" />');
                    getAudit();
                    getChart();

                }
            }); //end of jqGrid                 
        });

        function getAudit() {

            $.getJSON("Sync_audit.action", {facilityId: $('#id').val()}, function (auditList) {
                console.log(auditList);
                $("#newRecPatient").html(auditList[0].newRecPatient);
                $("#newRecClinic").html(auditList[0].newRecClinic);
                $("#newRecPharm").html(auditList[0].newRecPharm);
                $("#newRecLab").html(auditList[0].newRecLab);
                $("#newEnrolled").html(auditList[0].newEnrolled);
                $("#everEnrolled").html(auditList[0].everEnrolled);
                $("#currentCare").html(auditList[0].currentCare);
                $("#currentART").html(auditList[0].currentART);
            });

        }

        function getChart() {
            $.getJSON("SyncPatient_chart.action", {facilityId: $('#id').val()}, function (json) {
                setChart1(json);
            });
            $.getJSON("SyncClinic_chart.action", {facilityId: $('#id').val()}, function (json) {
                setChart2(json);
            });
            $.getJSON("SyncPharm_chart.action", {facilityId: $('#id').val()}, function (json) {
                setChart3(json);
            });
            $.getJSON("SyncLab_chart.action", {facilityId: $('#id').val()}, function (json) {
                setChart4(json);
                $('#loader').html('');
            });
        }

        function setChart1(json) {
            $('#container1').highcharts({
                chart: {
                    type: 'line'
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
                colors: ['#FF0000'],
                tooltip: {
                    headerFormat: '<span style="font-size:9px">{point.key}</span><table>',
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

        function setChart2(json) {
            $('#container2').highcharts({
                chart: {
                    type: 'line'
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
                colors: ['#3399FF'],
                tooltip: {
                    headerFormat: '<span style="font-size:9px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        colorByPoint: true,
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: json.series
            });
        }

        function setChart3(json) {
            $('#container3').highcharts({
                chart: {
                    type: 'line'
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
                colors: ['#FFCC00'],
                tooltip: {
                    headerFormat: '<span style="font-size:9px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        colorByPoint: true,
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: json.series
            });
        }

        function setChart4(json) {
            $('#container4').highcharts({
                chart: {
                    type: 'line'
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
                colors: ['#009933'],
                tooltip: {
                    headerFormat: '<span style="font-size:9px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        colorByPoint: true,
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: json.series
            });
            //$("#loader").html('');                                
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>
<!-- MAIN CONTENT -->
<div class="mt-5"></div>
<div class="content mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/event_pharmacy_page">Events Monitor</a></li>
        <li class="breadcrumb-item active">Sync Analyzer</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <input name="facilityId" type="hidden" id="facilityId"/>

        <div class="row">
            <div class="col-md-12">
                <div class="card card-stats">
                    <div class="card-header">
                        <div class="card-category">Facility Activity Summary</div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-2">
                                <div class="statistics">
                                    <div class="info">
                                        <div class="icon icon-primary">
                                            <i class="now-ui-icons objects_support-17"></i>
                                        </div>
                                        <h3 id="facilitySyncing" class="info-title"></h3>
                                        <h6 class="stats-title">No. of facilities Syncing</h6>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="statistics">
                                    <div class="info">
                                        <div class="icon icon-success">
                                            <i class="now-ui-icons users_single-02"></i>
                                        </div>
                                        <h3 id="facilityExpected" class="info-title"></h3>
                                        <h6 class="stats-title">No. of facilities Expected</h6>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="statistics">
                                    <div class="info">
                                        <div class="table-responsive">
                                            <table class="table">
                                                <tbody>
                                                <tr>
                                                    <td class="text-right text-success">No. of registrations entered
                                                    </td>
                                                    <td class="text-right text-success" id="newRecPatient"></td>
                                                </tr>
                                                <tr>
                                                    <td class="text-right text-warning">Newly enrolled</td>
                                                    <td class="text-right text-warning" id="newEnrolled"></td>
                                                </tr>
                                                <tr>
                                                    <td class="text-right text-success">No. of clinic records entered
                                                    </td>
                                                    <td class="text-right text-success" id="newRecClinic"></td>
                                                </tr>
                                                <tr>
                                                    <td class="text-right text-warning">Ever enrolled</td>
                                                    <td class="text-right text-warning" id="everEnrolled"></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="statistics">
                                    <div class="info">
                                        <div class="table-responsive">
                                            <table class="table">
                                                <tbody>
                                                <tr>
                                                    <td class="text-right text-info">No. of pharmacy records entered
                                                    </td>
                                                    <td class="text-right text-info" id="newRecPharm"></td>
                                                </tr>
                                                <tr>
                                                    <td class="text-right text-primary">Current on care</td>
                                                    <td class="text-right text-primary" id="currentCare"></td>
                                                </tr>
                                                <tr>
                                                    <td class="text-right text-info">No. of lab records entered</td>
                                                    <td class="text-right text-info" id="newRecLab"></td>
                                                </tr>
                                                <tr>
                                                    <td class="text-right text-primary">Current on ART</td>
                                                    <td class="text-right text-primary" id="currentART"></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="grid" class="table table-hover table-striped"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-12">
                <div id="loader"></div>
                <br clear="both"/>
            </div>
        </div>
        <!--                <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-category">
                        Facility Activity Trend
                    </div>
                </div>
            </div>
        </div>-->
        <div class="row">
            <div class="col-lg-6 col-md-6 col-sm-12">
                <div class="card card-chart">
                    <div class="card-header">
                        <h5 class="card-category">Sync Patient</h5>
                    </div>
                    <div class="card-body">
                        <div class="chart-area">
                            <div id="container1"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-12">
                <div class="card card-chart">
                    <div class="card-header">
                        <h5 class="card-category">Sync Clinic</h5>
                    </div>
                    <div class="card-body">
                        <div class="chart-area">
                            <div id="container2"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-12">
                <div class="card card-chart">
                    <div class="card-header">
                        <h5 class="card-category">Sync Laboratory</h5>
                    </div>
                    <div class="card-body">
                        <div class="chart-area">
                            <div id="container3"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-12">
                <div class="card card-chart">
                    <div class="card-header">
                        <h5 class="card-category">Sync Pharmacy</h5>
                    </div>
                    <div class="card-body">
                        <div class="chart-area">
                            <div id="container4"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <div id="userGroup" style="display: none"></div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>


