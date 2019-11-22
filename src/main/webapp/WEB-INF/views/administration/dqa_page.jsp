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
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/JavaScript">
        var lastSelected = -99;
        var facilityIds = [];
        var response = "";
        $(document).ready(function () {
            resetPage();

            $("#grid").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Data Element", "Total", "id"],
                colModel: [
                    {name: "element", index: "element", width: "640"},
                    {name: "total", index: "total", width: "110"},
                    {name: "elementId", index: "elementId", width: "0", sortable: false, hidden: true}
                ],
                rowNum: -1,
                sortname: "elementId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 350,
                loadtext: "Analyzing Data Quality, this may take some minutes...",
                jsonReader: {
                    root: "elementList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "elementId"
                },
                afterInsertRow: function (id, data) {
                    if (data.total == "0") {
                        $(this).jqGrid('setRowData', id, false, {background: 'lime'});
                    }
                },
                loadComplete: function () {
                    $("#loader").html('');
                    $("#showingData").html(response);
                },
                ondblClickRow: function (id) {
                    var data = $("#grid").getRowData(id);
                    if (data.total != 0 && lastSelected != id) {
                        lastSelected = id;
                        dqaReport(id);
                    }
                }
            }); //end of jqGrid  

            $.ajax({
                url: "StateId_retrieve_custom.action",
                dataType: "json",
                success: function (stateMapCustom) {
                    var options = "<option value = '" + 0 + "'>" + '' + "</option>";
                    $.each(stateMapCustom, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#stateId").html(options);
                }
            }); //end of ajax call

            $("#stateId").change(function (event) {
                facilityIds = [];
                $("#ok_button").removeAttr("disabled");
                $("#messageBar").slideUp('fast');
                if ($("#stateId").val() === "0") {
                    $("#id").html("");
                } else {
                    $.ajax({
                        url: "Facility_retrieve.action?stateId=" + $("#stateId").val() + "&active",
                        dataType: "json",
                        success: function (facilityMap) {
                            var options = "<option value = '" + 0 + "'>" + '' + "</option>";
                            $.each(facilityMap, function (key, value) {
                                facilityIds.push(key);
                                options += "<option value = '" + key + "'>" + value + "</option>";
                            }) //end each
                            $("#id").html(options);
                        }
                    }); //end of ajax call
                }
            });

            $("#id").change(function (event) {
                $("#ok_button").removeAttr("disabled");
            });

            $("#ok_button").bind("click", function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                dqaAnalysis();
                return false;
            });

            $("#print_button").bind("click", function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                printReport();
                return false;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });

            $('input[type="radio"]').bind("click", function (event) {
                $("#ok_button").removeAttr("disabled");
                $("#messageBar").slideUp('fast');
                //$('input[type="text"]').val("");
            });
        });

        function dqaAnalysis() {
            var hasFacility = false;
            var hasState = false;
            lastSelected = -99;
            $("#showingData").html('');
            var url = "Dqa_grid.action?q=1&analysisOption=" + $('[name="analysisOption"]:checked').val();
            if ($("#stateId").val() > 0) {
                if ($("#id").val() > 0) {
                    hasFacility = true;
                    url = "Dqa_grid.action?q=1&analysisOption=" + $('[name="analysisOption"]:checked').val() + "&facilityIds=" + $("#id").val();
                } else {
                    hasFacility = false;
                    url = "Dqa_grid.action?q=1&analysisOption=" + $('[name="analysisOption"]:checked').val() + "&facilityIds=" + facilityIds;
                }
                hasState = true;
            } else if ($("#stateId").val() == 0) {
                hasFacility = true;
                hasState = false;
                url = "Dqa_grid.action?q=1&analysisOption=" + $('[name="analysisOption"]:checked').val();
            }
            $("#grid").setGridParam({url: url, page: 1}).trigger("reloadGrid");
            var state = document.getElementById("stateId");
            var facility = document.getElementById("id");
            if (hasState == true) {
                if (hasFacility == false) {
                    response = "Showing Data Quality Records Analyzed for all facilities in <b>" + state.options[state.selectedIndex].text + " State</b>";
                } else {
                    response = "Showing Data Quality Records Analyzed for <b>" + facility.options[facility.selectedIndex].text + "</b> in <b>" + state.options[state.selectedIndex].text + " State</b>";
                }
            } else {
                response = "Showing Data Quality Records Analyzed for your facility <b>" + $("#facilityName").html() + "</b>";
            }
            $("#ok_button").attr("disabled", "disabled");
            $("#print_button").removeAttr("disabled");
        }

        function dqaReport(id) {
            if ($("#userGroup").html() != "Data Analyst") {
                url = "Dqa_report.action?elementId=" + id;
                window.open(url);
            }
        }

        var url = "";
        var x = function wait() {
            window.open(url);
        }

        function printReport() {

            $("#messageBar").hide();
            $("#print_button").attr("disabled", true);
            $.ajax({
                url: "Converter_dispatch.action",
                dataType: "json",
                data: {recordType: 14, exportDetail: response},
                beforeSend: function () {
                    //console.log($("#recordType").val(), $("#yearId option:selected").text(), $("#stateId option:selected").text(), $("#labtestId").val(), $("#reportingDateBegin").val(), $("#reportingDateEnd").val(), fac_ids.toString());
                },
                success: function (fileName) {
                    console.log(fileName);
                    $("#messageBar").html("Export to Excel Completed").slideDown('slow');
                    $("#print_button").attr("disabled", false);
                    url = fileName;
                    window.setTimeout(x, 3000);
                },
                error: function (e) {
                    console.log("Error: " + JSON.stringify(e));
                    alert("There was an error in conversion!");
                    $("print_button").attr("disabled", false);
                }
            });
        }

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item"><a href="/export">Data Maintenance</a></li>
    <li class="breadcrumb-item active">Data Quality Analysis</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-10 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <div id="loader"></div>

                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Select State:</label>
                            <select name="stateId" style="width: 100%;" class="form-control select2" id="stateId">
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Select Facility:</label>
                            <select name="facilityId" style="width: 100%;" class="form-control select2" id="facilityId">
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <div class="form-check form-check-radio">
                                <label class="form-check-label">
                                    <input type="radio" name="analysisOption" value="1" id="recordsCurrent"
                                           checked="checked"/>
                                    <span class="form-check-sign"></span>Current month transaction records
                                </label>
                            </div>
                            <div class="form-check form-check-radio">
                                <label class="form-check-label">
                                    <input type="radio" name="analysisOption" value="2" id="recordsAll"/>
                                    <span class="form-check-sign"></span>All transaction records
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="pull-right">
                                <!--<button id="print_button" disabled>Print</button> &nbsp;&nbsp;-->
                                <button id="ok_button" class="btn btn-info">Analyze</button>
                                <button id="close_button" class="btn btn-default">Close</button>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <label id="showingData"></label>
                            <div class="table-responsive">
                                <div class="card-title">Analysis Result</div>
                                <table id="grid"></table>
                            </div>
                        </div>
                    </div>
                    <div id="userGroup" style="display: none"></div>
                    <div id="facilityName" style="display: none"></div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
<div id="userGroup" style="display: none"></div>
</body>

</html>