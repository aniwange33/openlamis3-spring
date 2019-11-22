<%-- 
    Document   : Facility
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
    <title>LAMIS 2.5</title>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/javascript" src="js/highcharts.js"></script>
    <script type="text/javascript" src="js/modules/exporting.js"></script>
    <script type="text/JavaScript">
        $(document).ready(function () {
            resetPage();
            reports();

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                    $.getJSON("Retention_chart.action", {
                        reportingMonthBegin: $('#reportingMonthBegin').val(),
                        reportingYearBegin: $('#reportingYearBegin').val(),
                        reportingMonthEnd: $('#reportingMonthEnd').val(),
                        reportingYearEnd: $('#reportingYearEnd').val(),
                        indicatorId: $('#indicatorId').val()
                    }, function (json) {
                        $("#loader").html('');
                        $("#container").css({minWidth: '400px', height: '400px', margin: '0 auto'});
                        setChart(json);
                    });
                }
                return false;
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Visualizer_page");
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
            if ($("#reportingMonthBegin").val().length == 0 || $("#reportingYearBegin").val().length == 0) {
                $("#periodHelp1").html(" *");
                validate = false;
            } else {
                $("#periodHelp1").html("");
            }
            if ($("#reportingMonthEnd").val().length == 0 || $("#reportingYearEnd").val().length == 0) {
                $("#periodHelp2").html(" *");
                validate = false;
            } else {
                $("#periodHelp2").html("");
            }
            return validate;
        }
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">
        <jsp:include page="/WEB-INF/views/template/nav_visualizer.jsp"/>

        <div id="rightPanel">
            <s:form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                            <img src="images/report_header.jpg" width="773" height="28" class="top" alt=""/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Retention Chart</td>
                    </tr>
                </table>
                <p></p>
                <p></p>
                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td></td>
                        <td>From:&nbsp;
                            <select name="reportingMonthBegin" style="width: 100px;" class="inputboxes"
                                    id="reportingMonthBegin"/>
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
                            <select name="reportingYearBegin" style="width: 75px;" class="inputboxes"
                                    id="reportingYearBegin"/>
                            <option></option>
                            </select><span id="periodHelp1" style="color:red"></span>
                            &nbsp; to:&nbsp;
                            <select name="reportingMonthEnd" style="width: 100px;" class="inputboxes"
                                    id="reportingMonthEnd"/>
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
                            <select name="reportingYearEnd" style="width: 75px;" class="inputboxes"
                                    id="reportingYearEnd"/>
                            <option></option>
                            </select><span id="periodHelp2" style="color:red"></span>
                        </td>
                    </tr>
                </table>
                <p></p>
                <hr></hr>
                <div>
                    <div id="container"></div>
                </div>
                <div id="buttons" style="width: 200px">
                    <button id="ok_button">Generate</button> &nbsp;<button id="cancel_button">Close</button>
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
