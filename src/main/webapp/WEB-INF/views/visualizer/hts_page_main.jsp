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
    <link type="text/css" rel="stylesheet" href="chart-asset/bootstrap.min.css"/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="chart-asset/font-awesome/css/font-awesome.min.css">
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <!--        <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />-->
    <link type="text/css" rel="stylesheet" href="chart-asset/daterangepicker.css"/>

    <script type="text/javascript" src="chart-asset/jquery.min.js"></script>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <!--        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>       -->
    <!--        <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>       -->
    <!--        <script type="text/javascript" src="js/json.min.js"></script>-->
    <script type="text/javascript" src="chart-asset/moment.min.js"></script>
    <script type="text/javascript" src="chart-asset/daterangepicker.js"></script>

    <script type="text/javascript" src="js/highcharts.js"></script>
    <script type="text/javascript" src="js/modules/exporting.js"></script>
    <script type="text/javascript" src="chart-asset/hts-chart.js"></script>
    <style>
        .form-control {
            height: calc(1.5em + 0.5rem + 1px);
            line-height: inherit;
        }
    </style>
    <script type="text/JavaScript">
        $(document).ready(function () {

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
                event.preventDefault();
                generate();
                return false;
            });
        });


        $(function () {

            var start = moment().subtract(6, 'days');
            var end = moment();

            function cb(start, end) {
                $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
                $('#reportBeginDate').val(start.format('D/M/YYYY'));
                $('#reportEndDate').val(end.format('D/M/YYYY'));
            }


            $('#reportrange').daterangepicker({
                startDate: start,
                endDate: end,
                ranges: {
                    'Today': [moment(), moment()],
                    'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                    'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                    'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                    'This Month': [moment().startOf('month'), moment().endOf('month')],
                    'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                }
            }, cb);

            cb(start, end);

            generate();
        });
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">
        <jsp:include page="/WEB-INF/views/template/nav_visualizer.jsp"/>

        <div id="rightPanelScroll">
            <s:form id="lamisform" theme="css_xhtml">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/charts.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Visualizer >> HTS Chart</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">HTS Chart</td>
                    </tr>
                </table>
                <p></p>
                <input type="hidden" id="ipId" value="1"/>
                <input type="hidden" id="stateId" value="0"/>
                <input type="hidden" id="lgaId" value="0"/>
                <input type="hidden" id="facilityId" value="0"/>
                <div class="row">
                    <div class="col-md-4 form-group">
                        <label>IP</label>
                        <select name="ipId" id="ipId" class="form-control">
                            <option value="0">FHI360</option>
                            <option value="1">Global Fund</option>
                            <option value="2">Heartland Alliance</option>
                        </select>
                    </div>
                    <div class="col-md-4 form-group">
                        <label>State</label>
                        <select name="stateId" id="stateId" class="form-control">
                            <option value="0">Select</option>
                        </select>
                    </div>
                    <div class="col-md-4 form-group">
                        <label>LGA</label>
                        <select name="lgaId" id="lgaId" class="form-control">
                            <option value="0">Select</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 form-group">
                        <label>Facility</label>
                        <select name="facilityId" id="facilityId" class="form-control">
                            <option value="0">Select</option>
                        </select>
                    </div>
                    <div class="col-md-4 form-group">
                        <label>Date </label>
                        <div id="reportrange"
                             style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc; width: 100%">
                            <i class="fa fa-calendar-o"></i>&nbsp;
                            <span></span> <i class="fa fa-caret-down"></i>
                        </div>
                    </div>
                    <div class="col-md-4 form-group">
                        <br/>
                        <input type="hidden" name="reportBeginDate" id="reportBeginDate" class="form-control">
                        <input type="hidden" name="reportEndDate" id="reportEndDate" class="form-control">
                        <button id="ok_button" type="button" class="btn btn-sm btn-info">Generate</button>
                        <!--<button id="cancel_button" class="btn btn-sm btn-dark">Close</button>-->
                    </div>
                </div>

                <span id="loader"></span>
                <!-- Summary Section -->

                <div class="row">
                    <div class="card text-white bg-primary col-md-4 mb-3">
                        <div class="card-body">
                            <div class="col" id="tested">
                                <h5 class="text-light" id="tested"></h5>
                            </div>
                        </div>
                        <div class="card-footer">Total Tested</div>
                    </div>
                    <div class="card text-white bg-warning col-md-4 mb-3">
                        <div class="card-body">
                            <div class="col" id="positive">
                            </div>
                        </div>
                        <div class="card-footer">
                            Total Positive
                        </div>
                    </div>
                    <div class="card text-white bg-success col-md-4 mb-3">
                        <div class="card-body">
                            <div class="col" id="initiated">
                            </div>
                        </div>
                        <div class="card-footer">
                            Total Initiated on ART
                        </div>
                    </div>

                </div>

                <div class="row">
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
                </div>

            </s:form>
        </div>
    </div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>

</body>
</html>

