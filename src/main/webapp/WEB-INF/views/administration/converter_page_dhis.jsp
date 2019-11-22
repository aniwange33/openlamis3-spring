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
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/JavaScript">
        var facilityIds = [];
        $(document).ready(function () {
            initialize();
            reports();

            $("body").bind('ajaxStart', function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            });

            $("body").bind('ajaxStop', function (event) {
                $("#loader").html('');
            });
            $("#messageBar").hide();

            $.ajax({
                url: "StateId_retrieve.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#stateId").html(options);
                }
            }); //end of ajax call

            $("#grid").jqGrid({
                //url: "Facility_grid.action?stateId='0'",
                datatype: "json",
                mtype: "GET",
                colNames: ["Facility", ""],
                colModel: [
                    {name: "name", index: "name", width: "350"},
                    {
                        name: "sel",
                        index: "sel",
                        width: "50",
                        align: "center",
                        formatter: "checkbox",
                        editoptions: {value: "1:0"},
                        formatoptions: {disabled: false}
                    }
                ],
                rowNum: -1,
                sortname: "facilityId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 250,
                jsonReader: {
                    root: "facilityList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "facilityId"
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    if (data.sel == 1) {
                        $(this).jqGrid('setRowData', id, false, {color: 'red'})
                    } else {
                        $(this).jqGrid('setRowData', id, false, {color: 'black'})
                    }
                }
            }); //end of jqGrid

            $("#stateId").change(function (event) {
                $("#grid").setGridParam({
                    url: "Facilitysel_grid.action?q=1&stateId=" + $("#stateId").val(),
                    page: 1
                }).trigger("reloadGrid");
            });

            $("#ok_button").bind("click", function (event) {
                if ($("#stateId").val().length == 0) {
                    $("#stateHelp").html(" *");
                } else {
                    $("#stateHelp").html("");
                    var ids = $("#grid").getDataIDs();
                    for (var i = 0; i < ids.length; i++) {
                        var data = $("#grid").getRowData(ids[i]);
                        if (data.sel == 1) {
                            facilityIds.push(ids[i]);
                        }
                    }
                    convertData();
                }
                return false;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });

        });

        var url = "";
        var x = function wait() {
            window.open(url);
        }

        function convertData() {
            $("#messageBar").hide();
            $("#ok_button").attr("disabled", "disabled");
            $.ajax({
                url: "Converter_dispatch_dhis.action",
                dataType: "json",
                data: {
                    recordType: $("#recordType").val(),
                    state: $("#stateId option:selected").text(),
                    reportingMonth: $("#reportingMonth").val(),
                    reportingYear: $("#reportingYear").val(),
                    facilityIds: facilityIds.toString()
                },
                success: function (fileName) {
                    $("#messageBar").html("Conversion Completed").slideDown('fast');
                    //url = fileName;
                    //window.setTimeout(x, 3000);
                }
            });
            $("#ok_button").removeAttr("disabled");
        }
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">

        <jsp:include page="/WEB-INF/views/template/nav_conversion.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/report.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Administration >> Data Conversion >> Data Conversion to DHIS</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Data Conversion to DHIS</td>
                    </tr>
                </table>
                <p></p>
                <div id="loader"></div>
                <div id="messageBar"></div>

                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td width="15%"><label>Month/Year:</label></td>
                        <td width="85%">
                            <select name="reportingMonth" style="width: 100px;" class="inputboxes" id="reportingMonth"/>
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
                            &nbsp;<select name="reportingYear" style="width: 75px;" class="inputboxes"
                                          id="reportingYear"/>
                            <option></option>
                            </select><span id="periodHelp1" class="errorspan"></span>
                        </td>
                    </tr>
                    <tr>
                        <td width="15%"><label>Data to Convert:</label></td>
                        <td width="85%">
                            <select name="recordType" style="width: 200px;" class="inputboxes" id="recordType"/>
                            <option></option>
                            <option value=1>ART Summary</option>
                            <option value=2>LAB Summary</option>
                            <option value=3>Pharmacy Summary</option>
                            </select> &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td><label>State/Facility:</label></td>
                        <td>
                            <select name="stateId" style="width: 200px;" class="inputboxes" id="stateId">
                            </select><span id="stateHelp" class="errorspan"></span>
                        </td>
                    </tr>
                </table>

                <div>
                    <table width="99%" height="90" border="0" class="space">
                        <tr>
                            <td width="15%"></td>
                            <td width="85%">
                                <table id="grid"></table>
                            </td>
                        </tr>
                    </table>
                </div>
                <p></p>
                <hr></hr>

                <div id="buttons" style="width: 200px">
                    <button id="ok_button">Convert</button> &nbsp;<button id="close_button">Close</button>
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
