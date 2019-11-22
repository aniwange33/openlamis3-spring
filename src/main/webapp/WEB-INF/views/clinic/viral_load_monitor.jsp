<%-- 
    Document   : viral_load_monitor
    Created on : Aug 22, 2017, 8:58:25 AM
    Author     : user10
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css"/>
    <link type="text/css" rel="stylesheet" href="css/zebra_dialog.css"/>

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
    <script type="text/javascript" src="js/zebra_dialog.js"></script>
    <script type="text/JavaScript">
        var enablePadding = true;
        $(document).ready(function () {
            $.ajax({
                url: "Padding_status.action",
                dataType: "json",
                success: function (statusMap) {
                    enablePadding = statusMap.paddingStatus;
                }
            });
            resetPage();
            initialize();
            reports();

            $("#dialog").dialog({
                title: "Viral Load Update",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Yes", click: updateStatus},
                    {
                        text: "Cancel", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            var lastSelected = -99;
            $("#grid").jqGrid({
                url: "viral_load_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Phone", "ART Start Date", "Baseline VL", "Date of Baseline VL", "Current VL", "Date of Current VL", "Status", "EAC", "Repeat VL"],
                //colNames: ["S/N","Hospital No", "Name", "Phone", "ART Start Date", "VL Baseline", "Current Viral Load", "Status", "EAC Conducted", "Repeat Viral Load"],
                colModel: [
//                        {name: "sn", index: "sn", width: "30"},
                    {name: "hospitalNum", index: "hospitalNum", width: "130"},
                    {name: "name", index: "name", width: "120"},
                    {name: "phone", index: "phone", width: "100"},
                    {
                        name: "dateStarted",
                        index: "dateStarted",
                        width: "100",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
                    },
                    {name: "baselineVl", index: "baselineVl", width: "80"},
                    {name: "dateBaselineVl", index: "dateBaselineVl", width: "120"},
                    {name: "currentVl", index: "currentVl", width: "100"},
                    {name: "dateCurrentVl", index: "dateCurrentVl", width: "120"},
                    {name: "status", index: "status", width: "70"},
                    {
                        name: "eac",
                        index: "eac",
                        width: "50",
                        sortable: false,
                        editable: true,
                        edittype: "select",
                        editoptions: {value: " : ;Yes:Yes;No:No"}
                    },
                    {
                        name: "repeatVl",
                        index: "repeatVl",
                        width: "80",
                        sortable: false,
                        editable: true,
                        edittype: "select",
                        editoptions: {value: " : ;Yes:Yes;No:No"}
                    }
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "hospitalNum",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 350,
                width: 760,
                jsonReader: {
                    root: "viralLoadList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                },
                onSelectRow: function (id) {
                    if (id && id != lastSelected) {
                        $("#grid").jqGrid('saveRow', lastSelected,
                            {
                                successfunc: function (response) {
                                    return true;
                                },
                                url: "viral_load_update.action"
                            });
                        $("#update_button").attr("disabled", false);
                        lastSelected = id;
                    }
                    var data = $("#grid").getRowData(id);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#id").val(id);
                    $("#grid").jqGrid('editRow', id);
                } //end of onSelectRow
            }); //end of jqGrid

            $("#update_button").bind("click", function (event) {
                $("#messageBar").hide();
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    $("#dialog").dialog("open");
                    return false;
                }
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Clinic_page");
                return true;
            });
        });

        function updateStatus() {
            $("#dialog").dialog("close");
            $("#update_button").attr("disabled", true);
            $("#lamisform").attr("action", "Status_update_defaulter");
            $("#lamisform").trigger("submit");
            $("#update_button").attr("disabled", false);
        }

    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">
        <jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>

        <div id="rightPanel">
            <s:form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/clinic.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Clinic >> ART Clinic >> Viral Load Monitoring</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">
                            <table width="97%" border="0">
                                <tr>
                                    <td width="12%">Hospital No:</td>
                                    <td><input name="hospitalNum" type="text" class="inputboxes" id="hospitalNum"/></td>
                                    <td><input name="patientId" type="hidden" id="patientId"/></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>Name:</td>
                                    <td width="18%"><input name="name" type="text" class="inputboxes" id="name"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                    </tr>
                </table>
                <div id="messageBar"></div>

                <div>
                    <fieldset>
                        <legend> VIral Load Details</legend>
                        <table width="99%" height="90" border="0" class="space">
                            <tr>
                                <td>
                                    <table id="grid"></table>
                                    <div id="pager" style="text-align:center;"></div>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </div>
                <p></p>
                <div id="dialog">
                    <table width="99%" border="0" class="space" cellpadding="3">
                        <tr>
                            <td><label>Do you want to continue with client Viral Load Update?</label></td>
                        </tr>
                        <tr>
                            <td width="20%"><label>Click Yes to continue or No to cancel:</label></td>
                        </tr>
                    </table>
                </div>

                <div id="userGroup" style="display: none"><s:property value="#session.userGroup"/></div>
                <div id="buttons" style="width: 200px; float: right">
                    <button id="update_button" disabled="true">Update</button> &nbsp;<button id="close_button">Close
                </button>
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
