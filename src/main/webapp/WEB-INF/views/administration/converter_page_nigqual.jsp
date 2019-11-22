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
    <title>LAMIS 3.0</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!--        <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />
            <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css" />
            <link type="text/css" rel="stylesheet" href="themes/basic/grid.css" />
            <link type="text/css" rel="stylesheet" href="themes/jqModal.css" />-->

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/maintenance-common.js"></script>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <!--        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
            <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
            <script type="text/javascript" src="js/grid.locale-en.js"></script>
            <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
            <script type="text/javascript" src="js/jqDnR.js"></script>
            <script type="text/javascript" src="js/jqModal.js"></script>-->
    <script type="text/JavaScript">
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $("#dialog").dialog({
                title: "Review Period/Sample Size",
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
                colNames: ["Review Period", "Review Period ID", "Thermatic Area", "Population", "Sample Size", "Portal Id", "", "", "", ""],
                colModel: [
                    {name: "reviewPeriod", index: "reviewPeriod", width: "200"},
                    {name: "reviewPeriodId", index: "reviewPeriodId", width: "200"},
                    {name: "description", index: "description", width: "320"},
                    {name: "population", index: "population", width: "140"},
                    {name: "sampleSize", index: "sampleSize", width: "140"},
                    {name: "portalId", index: "portalId", width: "140"},
                    {name: "reportingDateBegin", index: "reportingDateBegin", width: "100", hidden: true},
                    {name: "reportingDateEnd", index: "reportingDateEnd", width: "100", hidden: true},
                    {name: "thermaticArea", index: "thermaticArea", width: "100", hidden: true},
                    {name: "id", index: "id", hidden: true},
                ],
                rowNum: -1,
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "nigqualList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "id"
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    $("#reviewPeriodId").val(data.reviewPeriodId);
                    $("#portalId").val(data.portalId);
                    $("#reportingDateBegin").val(data.reportingDateBegin);
                    $("#reportingDateEnd").val(data.reportingDateEnd);
                    $("#thermaticArea").val(data.thermaticArea);

                    $("#detail").setGridParam({
                        url: "Nigqual_grid.action?q=1&thermaticArea=" + data.thermaticArea + "&detail&reviewPeriodId=" + data.reviewPeriodId + "&portalId=" + data.portalId,
                        page: 1
                    }).trigger("reloadGrid");
                    $("#ok_button").attr("disabled", false);
                    $("#print_button").attr("disabled", false);
                    $("#messageBar").html("").slideUp('slow');
                }
            }); //end of master jqGrid                 

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "Age", "Date of Registration"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "200"},
                    {name: "name", index: "name", width: "407"},
                    {name: "gender", index: "gender", width: "150"},
                    {name: "age", index: "age", width: "150"},
                    {name: "dateRegistration", index: "dateRegistration", width: "230"},
                ],
                rowNum: -1,
                sortname: "hospitalNum",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 200,
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

            $("#print_button").bind("click", function (event) {
                var url = "Nigqual_report.action?q=1&thermaticArea=" + $("#thermaticArea").val() + "&detail&reviewPeriodId=" + $("#reviewPeriodId").val() + "&portalId=" + $("#portalId").val() + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();
                event.preventDefault();
                event.stopPropagation();
                window.open(url);
                return false;
            });

            $("#ok_button").bind("click", function (event) {
                convertData();
                return false;
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Conversion_page");
                return true;
            });
        });

        //var url = "";
        //var x = function wait() {window.open(url);}
        function convertData() {
            $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: "Converter_dispatch_nigqual.action",
                dataType: "json",
                data: {
                    thermaticArea: $("#thermaticArea").val(),
                    reviewPeriodId: $("#reviewPeriodId").val(),
                    portalId: $("#portalId").val(),
                    reportingDateBegin: $("#reportingDateBegin").val(),
                    reportingDateEnd: $("#reportingDateEnd").val()
                },
                success: function (fileName) {
                    $("#loader").html('');
                    $("#messageBar").html("Conversion completed").slideDown('slow');
                    $("#ok_button").attr("disabled", false);
                    //url = fileName;
                    //window.setTimeout(x, 3000);
                }
            });
        }

        function generateCohort() {
            var rnl = 0;
            if ($("#rnl").attr("checked")) {
                rnl = 1;
            }
            if (validateForm()) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
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
                    },
                    success: function (status) {
                        $("#loader").html('');
                        $("#grid").setGridParam({url: "Nigqual_grid.action?q=1", page: 1}).trigger("reloadGrid");
                    }
                });
                $("#dialog").dialog("close");
            }
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
    <!-- MAIN CONTENT -->
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item"><a href="Converter_page">Data Conversion</a></li>
        <li class="breadcrumb-item active">NigQual Report</li>
    </ol>
    <s:form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar"></div>

                    <div class="col-12 ml-auto mr-auto">
                        <div class="card-category"> Review Period/Cohort List</div>
                        <div class="row">
                            <div class="col-12">
                                <div class="pull-right">
                                    <button id="show_dialog" class="btn btn-info">Review Period...</button>
                                    <button id="print_button" class="btn btn-default" disabled="true">Print</button>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12 table-responsive">
                                <table id="grid"></table>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12 table-responsive">
                                <table id="detail"></table>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="dialog">
                    <div class="row">
                        <div class="col-12">
                            <label>Review Period Start:</label>
                            <div class="row">
                                <div class="form-group col-6">
                                    <select name="reportingMonthBegin" style="width: 100%;" class="form-control select2"
                                            id="reportingMonthBegin"/>
                                    <option value="">Select</option>
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
                                </div>
                                <div class="form-group col-6">
                                    <select name="reportingYearBegin" style="width: 100%;" class="form-control select2"
                                            id="reportingYearBegin"/>
                                    <option value="">Select</option>
                                    </select>
                                </div>
                            </div>
                            <span id="periodHelp1" style="color:red"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <label">Review Period End:</label>
                            <div class="row">
                                <div class="form-group col-6">
                                    <select name="reportingMonthEnd" style="width: 100%;" class="form-control select2"
                                            id="reportingMonthEnd"/>
                                    <option value="">Select</option>
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
                                </div>
                                <div class="form-group col-6">
                                    <select name="reportingYearEnd" style="width: 100%;" class="form-control select2"
                                            id="reportingYearEnd"/>
                                    <option value="">Select</option>
                                    </select>
                                </div>
                            </div>
                            <span id="periodHelp2" style="color:red"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-6 form-group">
                            <label class="form-label">Review Period Id</label>
                            <input name="reviewPeriodId" type="text" class="form-control" id="reviewPeriodId"/>
                            <span id="reviewHelp" class="errorspan"></span>
                        </div>
                        <div class="col-6 form-group">
                            <label class="form-label">Facility Code</label>
                            <input name="portalId" type="text" class="form-control" id="portalId"/>
                            <span id="portalHelp" class="errorspan"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-10">
                            <div class="form-group">
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input class="form-check-input" name="rnl" type="checkbox" value="1"
                                               checked="checked" id="rnl"/>
                                        <span class="form-check-sign"></span> Generate cohort using random number list
                                        (RNL)
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-10 ml-3">
                            <strong>
                                The review period Id and web portal facility code are to be obtained from the web portal
                            </strong>
                        </div>
                    </div>
                    <input name="reportingDateEnd" type="hidden" id="reportingDateEnd"/>
                    <input name="reportingDateBegin" type="hidden" id="reportingDateBegin"/>
                    <input name="thermaticArea" type="hidden" id="thermaticArea"/>
                    <div class="btn-group pull-right">
                        <button class="btn btn-info" id="ok_button">Generate</button>
                        <!--                                    <button class="btn btn-default" id="close_button">Close</button>-->
                    </div>
                </div>
            </div>
        </div>
    </div>
    </s:form>
    <div id="userGroup" style="display: none"><s:property value="#session.userGroup"/></div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
