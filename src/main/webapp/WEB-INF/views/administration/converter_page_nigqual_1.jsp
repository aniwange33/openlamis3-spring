<%-- 
    Document   : Facility
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 2.5</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css"/>

    <script type="text/javascript" src="js/lamis/maintenance-common.js"></script>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/JavaScript">
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $("#dialog").dialog({
                title: "Generate cohort",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Ok", click: generateCohort},
                    {
                        text: "Close", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            $("#grid").jqGrid({
                url: "Nigqual_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Review Period", "Review Period ID", "Thermatic Area", "Population", "Sample Size", "Portal Id", "", "", ""],
                colModel: [
                    {name: "reviewPeriod", index: "reviewPeriod", width: "200"},
                    {name: "reviewPeriodId", index: "reviewPeriodId", width: "100"},
                    {name: "ta", index: "ta", width: "100"},
                    {name: "population", index: "population", width: "80"},
                    {name: "sampleSize", index: "sampleSize", width: "80"},
                    {name: "portalId", index: "portalId", width: "100"},
                    {name: "reportingDateBegin", index: "reportingDateBegin", width: "100", hidden: true},
                    {name: "reportingDateEnd", index: "reportingDateEnd", width: "100", hidden: true},
                    {name: "thermaticArea", index: "thermaticArea", width: "100", hidden: true},
                ],
                rowNum: -1,
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 100,
                jsonReader: {
                    root: "nigqualList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "reviewPeriodId"
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    $("#reviewPeriodId").val(data.reviewPeriodId);
                    $("#portalId").val(data.portalId);
                    $("#reportingDateBegin").val(data.reportingDateBegin);
                    $("#reportingDateEnd").val(data.reportingDateEnd);

                    $("#detail").setGridParam({
                        url: "Nigqual_grid.action?q=1&detail&reviewPeriodId=" + data.reviewPeriodId + "&portalId=" + data.portalId,
                        page: 1
                    }).trigger("reloadGrid");
                    $("#ok_button").attr("disabled", false);
                }
            }); //end of master jqGrid

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "Age", "Date of Registration"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "160"},
                    {name: "name", index: "name", width: "220"},
                    {name: "gender", index: "gender", width: "70"},
                    {name: "age", index: "age", width: "80"},
                    {name: "dateRegistration", index: "dateRegistration", width: "135"},
                ],
                rowNum: -1,
                sortname: "hospitalNum",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "cohortList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false
                }
            }); //end of detail jqGrid

            $("#show_dialog").bind("click", function () {
                $("#dialog").dialog("open");
                return false;
            });

            $("#ok_button").bind("click", function (event) {
                if ($("#reviewPeriodId").val().length == 0 || $("#portalId").val().length == 0) {
                    $("#messageBar").html("Please, select the cohort/review period you want to generate").slideDown('slow');
                } else {
                    convertData();
                }
                return false;
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });
        });

        //var url = "";
        //var x = function wait() {window.open(url);}
        function convertData() {
            $("#messageBar").hide();
            $("#ok_button").attr("disabled", true);
            var recordType = $('[name="reportType"]:checked').val();
            $.ajax({
                url: "Converter_dispatch_nigqual.action",
                dataType: "json",
                data: {
                    recordType: recordType,
                    reviewPeriodId: $("#reviewPeriodId").val(),
                    portalId: $("#portalId").val(),
                    reportingDateBegin: $("#reportingDateBegin").val(),
                    reportingDateEnd: $("#reportingDateEnd").val()
                },
                success: function (fileName) {
                    $("#messageBar").html("Conversion completed").slideDown('slow');
                    //url = fileName;
                    //window.setTimeout(x, 3000);
                    $("#ok_button").attr("disabled", false);
                }
            });
        }

        function generateCohort() {
            var rnl = 0;
            if ($("#rnl").attr("checked")) {
                rnl = 1;
            }
            if (validateForm()) {
                $.ajax({
                    url: "Generate_cohort.action",
                    dataType: "json",
                    data: {
                        reportingMonthBegin: $("#reportingMonthBegin").val(),
                        reportingYearBegin: $("#reportingYearBegin").val(),
                        reportingMonthEnd: $("#reportingMonthEnd").val(),
                        reportingYearEnd: $("#reportingYearEnd").val(),
                        reviewPeriodId: $("#reviewPeriodId").val(),
                        portalId: $("#portalId").val(),
                        rnl: rnl
                    }
                });
                $("#dialog").dialog("close");
            }
            $("#grid").setGridParam({url: "Nigqual_grid.action?q=1", page: 1}).trigger("reloadGrid");
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
            if ($("#reviewPeriodId").val().length == 0) {
                $("#reviewHelp").html(" *");
                validate = false;
            } else {
                $("#reviewHelp").html("");
            }
            if ($("#portalId").val().length == 0) {
                $("#portalHelp").html(" *");
                validate = false;
            } else {
                $("#portalHelp").html("");
            }
            return validate;
        }


    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>
<!-- MAIN CONTENT -->
<div class="mt-5"></div>
<div class="content mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Converter_page">Data Conversion</a></li>
        <li class="breadcrumb-item active">NigQual Report</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <table width="100%" border="0">
            <tr>
                <td>
                    <img src="images/report_header.jpg" width="773" height="28" class="top" alt=""/>
                </td>
            </tr>
            <tr>
                <td class="topheaders" id="title">NigQual Report</td>
            </tr>
        </table>
        <div id="loader"></div>
        <div id="messageBar"></div>
        <div>
            <fieldset>
                <legend> Review Period/Cohort List</legend>
                <p></p>
                <table style="margin-left:10px" width="99%" height="90" border="0" class="space">
                    <tr>
                        <td>
                            <table id="grid"></table>
                            <p></p>
                            <table id="detail"></table>
                        </td>
                    </tr>
                </table>
                <p></p>
                <span style="margin-left:10px"><button id="show_dialog">New cohort...</button> &nbsp;<button
                        id="print_button">&nbsp;&nbsp;&nbsp;Print&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button></span>
                <p></p>
            </fieldset>
        </div>
        <p></p>

        <div id="dialog">
            <table width="99%" border="0" class="space" cellpadding="3">
                <tr>
                    <td><label>Review Period Start:</label></td>
                    <td>
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
                    </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td><label>Review Period End:</label></td>
                    <td>
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
                        <select name="reportingYearEnd" style="width: 75px;" class="inputboxes" id="reportingYearEnd"/>
                        <option></option>
                        </select><span id="periodHelp2" style="color:red"></span>
                    </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td><label>Review Period Id:</label></td>
                    <td><input name="reviewPeriodId" type="text" style="width: 50px;" class="inputboxes"
                               id="reviewPeriodId"/><span id="reviewHelp" class="errorspan"></span></td>
                    <td></td>
                </tr>
                <tr>
                    <td><label>Facility Code:</label></td>
                    <td><input name="portalId" type="text" style="width: 100px;" class="inputboxes" id="portalId"/><span
                            id="portalHelp" class="errorspan"></span></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td colspan="2"><input name="rnl" type="checkbox" value="1" id="rnl"/><label>Generate cohort using
                        random number list (RNL)</label></td>
                </tr>
                <tr></tr>
                <tr></tr>
                <tr>
                    <td colspan="4"><label><strong>The review period Id and web portal facility code are to be obtained
                        from the web portal</strong></label></td>
                </tr>
            </table>
        </div>
        <p></p>

        <div>
            <fieldset>
                <legend> Review Period/Cohort List</legend>
                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td></td>
                        <td colspan="2"><input type="radio" name="reportType" value="1" checked/><label>Patient
                            Demography </label></td>
                        <td></td>
                        <td colspan="2"><input type="radio" name="reportType" value="2"/><label>Adult ART </label></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td colspan="2"><input type="radio" name="reportType" value="3"/><label>Pediatrics ART </label>
                        </td>
                        <td></td>
                        <td colspan="2"><input type="radio" name="reportType" value="4"/><label>PMTCT </label></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input name="reportingDateBegin" type="hidden" id="reportingDateBegin"/></td>
                        <td><input name="reportingDateEnd" type="hidden" id="reportingDateEnd"/></td>
                    </tr>
                </table>
            </fieldset>
        </div>
        <p></p>

        <div id="buttons" style="width: 200px">
            <button id="ok_button" disabled="true">Generate</button> &nbsp;<button id="cancel_button">Close</button>
        </div>
    </form>
</div>
</div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
