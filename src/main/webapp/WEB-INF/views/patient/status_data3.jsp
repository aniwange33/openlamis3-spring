<%-- 
    Document   : Status
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 2.6</title>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css"/>
    <link type="text/css" rel="stylesheet" href="css/zebra_dialog.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/status-common.js"></script>
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
        var lastSelectDate = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            $.ajax({
                url: "Patient_retrieve.action",
                dataType: "json",
                success: function (patientList) {
                    // set patient id and number for which infor is to be entered
                    $("#id").val(patientList[0].patientId);
                    $("#hospitalNum").val(patientList[0].hospitalNum);
                    $("#previousStatus").html(patientList[0].currentStatus);
                    date = patientList[0].dateCurrentStatus;
                    $("#datePreviousStatus").html(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
                }
            }); //end of ajax call

            var lastSelected = -99;
            $("#grid").jqGrid({
                url: "Status_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["ART Status", "Date of Status", "", "", "", ""],
                colModel: [
                    {name: "currentStatus", index: "currentStatus", width: "450"},
                    {
                        name: "dateCurrentStatus",
                        index: "date",
                        width: "120",
                        align: 'center',
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "status", index: "status", width: "70"},
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "100", hidden: true},
                    {name: "historyId", index: "historyId", width: "100", hidden: true},
                    {name: "deletable", index: "deletable", width: "5", hidden: true},
                ],
                rowNum: -1,
                sortname: "dateCurrentStatus",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 200,
                jsonReader: {
                    root: "statusList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    //id: "historyId"
                },
                loadComplete: function () {
                    var gridIds = $("#grid").getDataIDs();
                    for (i = 0; i < gridIds.length; i++) {
                        var data = $("#grid").getRowData(gridIds[i]);
                        if (data.deletable == "0") {
                            $("#grid").jqGrid('setCell', i + 1, 'status', '', {background: '#ff9933'});
                        } else {
                            $("#grid").jqGrid('setCell', i + 1, 'status', '', {background: '#ccff33'});
                        }
                    }
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id);
                    $("#currentStatus").val(data.currentStatus);
                    $("#dateCurrentStatus").val(data.dateCurrentStatus);
                    $("#historyId").val(data.historyId);
                    date = data.dateCurrentStatus;
                    $("#date").val(dateSlice(date));
                    updateRecord = true;
                    lastSelected = id;
                    initButtonsForModify()
                    if (data.deletable == "1") {
                        $("#delete_button").removeAttr("disabled");
                        $("#save_button").removeAttr("disabled");
                    } else {
                        $("#delete_button").attr("disabled", "disabled");
                        $("#save_button").attr("disabled", "disabled");
                        $("#currentStatus").val("");
                        $("#dateCurrentStatus").val("");
                        $("#date").val("");
                    }
                }
            }); //end of jqGrid

            $("#currentStatus").change(function (event) {
                //select lab test to convert
                if ($("#currentStatus").val() == "Lost to Follow Up" || $("#currentStatus").val() == "Stopped Treatment" || $("#currentStatus").val() == "Died (Confirmed)") {
                    $("#trackedDateTr").show("slow");
                    if ($("#currentStatus").val() == "Stopped Treatment") {
                        $("#interruptTr").show("slow");
                        $("#deathTr").hide("slow");
                    }
                    if ($("#currentStatus").val() == "Died (Confirmed)") {
                        $("#deathTr").show("slow");
                        $("#interruptTr").hide("slow");
                    }
                    $("#date_tr").mask("99/99/9999");
                    $("#date_tr").datepicker({
                        dateFormat: "dd/mm/yy",
                        changeMonth: true,
                        changeYear: true,
                        yearRange: "-100:+0",
                        constrainInput: true,
                        buttonImageOnly: true,
                        buttonImage: "/images/calendar.gif"
                    });
                } else {
                    $("#trackedDateTr").hide("slow");
                    $("#interruptTr").hide("slow");
                    $("#deathTr").hide("slow");
                }
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Status_search");
                return true;
            });

        });

    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">

        <jsp:include page="/WEB-INF/views/template/nav_home.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/appointment.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Clinic >> ART Clinic >> Client Status Update</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Details</td>
                    </tr>
                </table>
                <div id="messageBar"></div>

                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td width="20%"><label>Hospital No:</label></td>
                        <td width="20%"><input name="hospitalNum" type="text" class="inputboxes" id="hospitalNum"
                                               readonly="readonly"/></td>
                        <td width="25%"><span id="patientInfor"</span></td>
                        <td width="20%"><input name="patientId" type="hidden" id="patientId"/><input name="historyId"
                                                                                                     type="hidden"
                                                                                                     id="historyId"/>
                        </td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td><label>Date of New Status:</label></td>
                        <td><input name="date" type="text" style="width: 100px;" class="inputboxes" id="date"/><input
                                name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/><span id="dateHelp"
                                                                                                     class="errorspan"></span>
                        </td>
                        <td colspan="2"><label>Date of Current Status: </label><span id="datePreviousStatus"
                                                                                     style="color:blue"></span></td>
                    </tr>
                    <tr>
                        <td><label>New Status:</label></td>
                        <td>
                            <select name="currentStatus" style="width: 230px;" class="inputboxes" id="currentStatus">
                                <option></option>
                                <option>ART Restart</option>
                                <option>ART Transfer Out</option>
                                <option>Pre-ART Transfer Out</option>
                                <option>Lost to Follow Up</option>
                                <option>Stopped Treatment</option>
                                <option>Died (Confirmed)</option>
                                <option>Previously Undocumented Patient Transfer (Confirmed)</option>
                                <option>Traced Patient (Unable to locate)</option>
                                <option>Did Not Attempt to Trace Patient</option>
                            </select><span id="statusregHelp" class="errorspan"></span>
                        </td>
                        <td colspan="2"><label>Current Status: </label><span id="previousStatus"
                                                                             style="color:blue"></span></td>
                    </tr>
                    <tr id="interruptTr" style="display: none">
                        <td><label>Reason for Interruption::</label></td>
                        <td>
                            <select name="reasonInterrupt" style="width: 230px;" class="inputboxes"
                                    id="reasonInterrupt">
                                <option></option>
                                <option>Toxicity/side effect</option>
                                <option>Pregnancy</option>
                                <option>Treatment failure</option>
                                <option>Poor adherence</option>
                                <option>Illness, hospitalization</option>
                                <option>Drug out of stock</option>
                                <option>Patient lacks finances</option>
                                <option>Other patient decision</option>
                                <option>Planned Rx interruption</option>
                                <option>Other</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="deathTr" style="display: none">
                        <td><label>Cause of Death:</label></td>
                        <td>
                            <select name="causeDeath" style="width: 230px;" class="inputboxes" id="causeDeath">
                                <option></option>
                                <option>HIV disease resulting in TB</option>
                                <option>HIV disease resulting in cancer</option>
                                <option>HIV disease resulting in other infectious and parasitic disease</option>
                                <option>Other HIV disease resulting in other disease or conditions leading to death
                                </option>
                                <option>Other natural causes</option>
                                <option>Non-natural causes</option>
                                <option>Unknown cause</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="trackedDateTr" style="display: none">
                        <td><label>Date of Tracked:</label></td>
                        <td><input name="date1" type="text" style="width: 100px;" class="inputboxes" id="date1"/><input
                                name="dateTracked" type="hidden" id="dateTracked"/><span id="dateHelpTracked"
                                                                                         class="errorspan"></span></td>
                    </tr>
                    <tr>
                        <td><input name="gender" type="hidden" id="gender"/><input name="dateBirth" type="hidden"
                                                                                   id="dateBirth"/></td>
                    </tr>
                </table>
                <p></p>
                <div>
                    <fieldset>
                        <legend> Status History</legend>
                        <p></p>
                        <table width="99%" height="90" border="0" class="space">
                            <tr>
                                <td width="2%"></td>
                                <td width="85%">
                                    <table id="grid"></table>
                                </td>
                            </tr>
                        </table>
                        <p></p>
                    </fieldset>
                </div>
                <p></p>
                <div id="userGroup" style="display: none">
                </div>
                <div id="buttons" style="width: 300px">
                    <button id="save_button">Save</button> &nbsp;<button id="delete_button" disabled="true"/>
                    Delete</button> &nbsp;<button id="close_button"/>
                    Close</button>
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
