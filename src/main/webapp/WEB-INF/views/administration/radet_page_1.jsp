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

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/updater-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/JavaScript">
        var obj = {};
        var url = "";
        $(document).ready(function () {
            resetPage();
            initialize();

            $("#grid").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No.", "Unique ID", "Name", "Gender", "ART Start", "Last Pickup", "Current Regimen", "Current Status", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "70"},
                    {name: "uniqueId", index: "uniqueId", width: "70"},
                    {name: "name", index: "name", width: "140"},
                    {name: "gender", index: "gender", width: "50"},
                    {
                        name: "dateStarted",
                        index: "date1",
                        width: "70",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "dateLastRefill",
                        index: "date2",
                        width: "70",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "regimen", index: "regimen", width: "155"},
                    {name: "currentStatus", index: "currentStatus", width: "100"},
                    {name: "dateRegistration", index: "dateRegistration", hidden: true},
                    {name: "regimenStart", index: "regimenStart", hidden: true},
                    {name: "regimentypeStart", index: "regimentypeStart", hidden: true},
                    {name: "regimentype", index: "regimentype", hidden: true},
                    {name: "regimenId", index: "regimenStart", hidden: true},
                    {name: "regimentypeId", index: "regimentypeStart", hidden: true},
                    {name: "duration", index: "duration", hidden: true},
                    {name: "surname", index: "surname", hidden: true},
                    {name: "otherNames", index: "otherNames", hidden: true},
                    {name: "dateBirth", index: "dateBirth", hidden: true},
                    {name: "age", index: "age", hidden: true},
                    {name: "ageUnit", index: "ageUnit", hidden: true},
                    {name: "maritalStatus", index: "maritalStatus", hidden: true},
                    {name: "state", index: "state", hidden: true},
                    {name: "lga", index: "lga", hidden: true},
                    {name: "address", index: "address", hidden: true},
                    {name: "phone", index: "phone", hidden: true},
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", hidden: true},
                    {name: "clinicStage", index: "clinicStage", hidden: true},
                    {name: "funcStatus", index: "funcStatus", hidden: true},
                    {name: "cd4", index: "cd4", hidden: true},
                    {name: "cd4p", index: "cd4p", hidden: true},
                    {name: "statusRegistration", index: "statusRegistration", hidden: true},
                    {name: "patientId", index: "patientId", hidden: true},
                    {name: "rowcount", index: "rowcount", hidden: true},
                    {name: "updated", index: "updated", hidden: true},
                    {name: "dateStarted", index: "dateStarted", hidden: true},
                    {name: "dateLastRefill", index: "dateLastRefill", hidden: true},
                ],
                rowNum: -1,
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 250,
                loadtext: "Retrieving record, please wait...",
                jsonReader: {
                    root: "clientList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false
                },
                afterInsertRow: function (id, data) {
                    if (data.updated == "1") {
                        $(this).jqGrid('setRowData', id, false, {background: 'khaki'});
                    }
                    if (data.updated == "4") {
                        $(this).jqGrid('setRowData', id, false, {background: 'lightblue'});
                    }
                    if (data.updated == "3") {
                        $(this).jqGrid('setRowData', id, false, {background: 'lime'});
                    }
                },
                onSelectRow: function (id) {
                    $("#save_button").attr("disabled", false);
                    $("#messageBar").hide();
                    var data = $("#grid").getRowData(id);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#uniqueId").val(data.uniqueId);
                    $("#currentStatus").val(data.currentStatus);
                    $("#surname").val(data.surname);
                    $("#otherNames").val(data.otherNames);
                    $("#age").val(data.age);
                    $("#ageUnit").val(data.ageUnit);
                    $("#gender").val(data.gender);
                    $("#maritalStatus").val(data.maritalStatus);
                    $("#address").val(data.address);
                    $("#phone").val(data.phone);
                    $("#statusRegistration").val(data.statusRegistration);
                    $("#clinicStage").val(data.clinicStage);
                    $("#funcStatus").val(data.funcStatus);
                    $("#clinicStage").val(data.clinicStage);
                    $("#cd4").val(data.cd4);
                    $("#cd4p").val(data.cd4p);
                    $("#dateBirth").val(data.dateBirth);
                    $("#date1").val(dateSlice(data.dateBirth));
                    $("#dateRegistration").val(data.dateRegistration);
                    $("#date2").val(dateSlice(data.dateRegistration));
                    $("#dateCurrentStatus").val(data.dateCurrentStatus);
                    $("#date3").val(dateSlice(data.dateCurrentStatus));
                    $("#dateStarted").val(data.dateStarted);
                    $("#date4").val(dateSlice(data.dateStarted));
                    $("#dateLastRefill").val(data.dateLastRefill);
                    $("#date5").val(dateSlice(data.dateLastRefill));
                    $("#duration").val(data.duration);
                    $("#id").val(data.patientId);
                    $("#rowcount").val(data.rowcount);
                    retrieveLga(data);
                    retrieveRegimen(data);
                    retrieveRegimenId(data);
                }
            }); //end of jqGrid

            $("#import_button").bind("click", function (event) {
                if (validateUpload()) {
                    $("#lamisform").attr("method", "post");
                    $("#lamisform").attr("enctype", "multipart/form-data");
                    $("#lamisform").attr("action", "Upload_radetfile");
                    return true;
                } else {
                    return false;
                }
            });

            if ($("#fileUploaded").html() == 1) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                $("#import_button").attr("disabled", true);
                $("#print_button").attr("disabled", true);
                $("#attachment").attr("disabled", true);
                $.ajax({
                    url: "Radet_service.action",
                    dataType: "json",
                    success: function (obj) {
                        $("#grid").setGridParam({url: "Radet_grid.action", page: 1}).trigger("reloadGrid");
                        $("#loader").html('');
                        $("#import_button").attr("disabled", false);
                        $("#print_button").attr("disabled", false);
                        $("#attachment").attr("disabled", false);
                    }
                }); //end of ajax call
                $("#fileUploaded").html(0);
            }
        });
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">
        <jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td class="topheaders">RADET Analysis</td>
                    </tr>
                </table>
                <div id="loader"></div>
                <div id="messageBar"></div>
                <div>
                    <table width="99%" height="90" border="0" class="space">
                        <tr></tr>
                        <tr>
                            <td><label>File Name: </label><input type="file" name="attachment" class="inputboxes"
                                                                 id="attachment"/><span id="fileHelp"
                                                                                        class="errorspan"></span><span
                                    style="margin-left:20px"><button id="import_button"
                                                                     style="width: 80px; font-size: 12px">Import</button></span><span
                                    style="margin-left:50px"><button id="print_button"
                                                                     style="width: 80px; font-size: 12px">Print...</button></span>
                            </td>
                        </tr>
                        <tr></tr>
                        <tr></tr>
                        <tr></tr>
                        <tr>
                            <td>
                                <table id="grid"></table>
                            </td>
                        </tr>
                    </table>
                </div>
                <table width="100%" border="0">
                    <tr>
                        <td class="topheaders">Client Information Update</td>
                    </tr>
                </table>
                <div id="section">
                    <div id="container" style="height: 300px; overflow-y: scroll; margin-top: 10px">
                        <div style="border: 1px solid #ccc">
                            <table width="99%" border="0" class="space" cellpadding="3">
                                <tr>
                                    <td width="20%"><label>Hospital No:</label></td>
                                    <td width="30%"><input name="hospitalNum" type="text" class="inputboxes"
                                                           id="hospitalNum"/><span id="numHelp"
                                                                                   class="errorspan"></span></td>
                                    <td width="20%"><label>Unique ID:</label></td>
                                    <td width="30%"><input name="uniqueId" type="text" class="inputboxes"
                                                           id="uniqueId"/></td>
                                </tr>
                                <tr>
                                    <td><label>Surname:</label></td>
                                    <td><input name="surname" type="text" class="inputboxes" id="surname"/><span
                                            id="surnameHelp" class="errorspan"></span></td>
                                    <td><label>Other Names:</label></td>
                                    <td><input name="otherNames" type="text" class="inputboxes" id="otherNames"/></td>
                                </tr>
                                <tr>
                                    <td><label>Date of Birth:</label></td>
                                    <td><input name="date1" type="text" style="width: 100px;" class="inputboxes"
                                               id="date1"/><input name="dateBirth" type="hidden" id="dateBirth"/></td>
                                    <td><label>Age at Registration:</label></td>
                                    <td><input name="age" type="text" style="width: 50px;" class="inputboxes" id="age"/>
                                        <select name="ageUnit" style="width: 75px;" class="inputboxes" id="ageUnit">
                                            <option></option>
                                            <option>year(s)</option>
                                            <option>month(s)</option>
                                            <option>day(s)</option>
                                        </select><span id="ageHelp" class="errorspan"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label>Gender:</label></td>
                                    <td>
                                        <select name="gender" style="width: 130px;" class="inputboxes" id="gender">
                                            <option></option>
                                            <option>Male</option>
                                            <option>Female</option>
                                        </select><span id="genderHelp" class="errorspan"></span>
                                    </td>
                                    <td><label>Marital Status:</label></td>
                                    <td>
                                        <select name="maritalStatus" style="width: 130px;" class="inputboxes"
                                                id="maritalStatus">
                                            <option></option>
                                            <option>Single</option>
                                            <option>Married</option>
                                            <option>Widowed</option>
                                            <option>Separated</option>
                                            <option>Divorced</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label>State of residence:</label></td>
                                    <td>
                                        <select name="state" style="width: 130px;" class="inputboxes" id="state">
                                            <option></option>
                                        </select>
                                    </td>
                                    <td><label>L.G.A. residence:</label></td>
                                    <td>
                                        <select name="lga" style="width: 130px;" class="inputboxes" id="lga">
                                            <option></option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label>Address:</label></td>
                                    <td><input name="address" type="text" style="width: 200px;" class="inputboxes"
                                               id="address"/></td>
                                    <td><label>Telephone:</label></td>
                                    <td><input name="phone" type="text" class="inputboxes" id="phone"/></td>
                                </tr>
                                <tr>
                                    <td><label>HIV Status at registration:</label></td>
                                    <td>
                                        <select name="statusRegistration" style="width: 200px;" class="inputboxes"
                                                id="statusRegistration"/>
                                        <option></option>
                                        <option>HIV exposed status unknown</option>
                                        <option>HIV+ non ART</option>
                                        <option>ART Transfer In</option>
                                        <option>Pre-ART Transfer In</option>
                                        </select><span id="statusregHelp" class="errorspan"></span>
                                    </td>
                                    <td><label>Date of registration/ &nbsp;Transfer-in:</label></td>
                                    <td><input name="date2" type="text" style="width: 100px;" class="inputboxes"
                                               id="date2"/><input name="dateRegistration" type="hidden"
                                                                  id="dateRegistration"/><span id="dateregHelp"
                                                                                               class="errorspan"></span>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <p></p>
                        <div style="border: 1px solid #ccc">
                            <table width="99%" border="0" class="space" cellpadding="3">
                                <tr>
                                    <td width="20%"><label>ART Start Date:</label></td>
                                    <td width="30%"><input name="date4" type="text" style="width: 100px;"
                                                           class="inputboxes" id="date4"/><input name="dateStarted"
                                                                                                 type="hidden"
                                                                                                 id="dateStarted"/><span
                                            id="dateHelp" class="errorspan"></span></td>
                                    <td width="20%"><label>CD4 at start of ART:</label></td>
                                    <td colspan="3"><input name="cd4" type="text" style="width: 50px;"
                                                           class="inputboxes" id="cd4"/>&nbsp;<input name="cd4p"
                                                                                                     type="text"
                                                                                                     style="width: 50px;"
                                                                                                     class="inputboxes"
                                                                                                     id="cd4p"/>&nbsp;CD4%
                                    </td>
                                </tr>
                                <tr>
                                    <td><label>Clinical Stage:</label></td>
                                    <td>
                                        <select name="clinicStage" style="width: 100px;" class="inputboxes"
                                                id="clinicStage">
                                            <option></option>
                                            <option>Stage I</option>
                                            <option>Stage II</option>
                                            <option>Stage III</option>
                                            <option>Stage IV</option>
                                        </select>
                                    </td>
                                    <td><label>Functional Status:</label></td>
                                    <td>
                                        <select name="funcStatus" style="width: 100px;" class="inputboxes"
                                                id="funcStatus">
                                            <option></option>
                                            <option>Working</option>
                                            <option>Ambulatory</option>
                                            <option>Bedridden</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label>Regimen Line (first):</label></td>
                                    <td>
                                        <select name="regimentype" style="width: 200px;" class="inputboxes"
                                                id="regimentype">
                                            <option></option>
                                        </select>
                                    </td>
                                    <td><label>Regimen (first):</label></td>
                                    <td>
                                        <select name="regimen" style="width: 200px;" class="inputboxes" id="regimen">
                                            <option></option>
                                        </select>
                                    </td>
                                </tr>

                            </table>
                        </div>
                        <p></p>
                        <div style="border: 1px solid #ccc; background-color: ghostwhite">
                            <table width="99%" border="0" class="space" cellpadding="3">
                                <tr>
                                    <td width="20%"><label>Date of last refill:</label></td>
                                    <td width="30%"><input name="date5" type="text" style="width: 100px;"
                                                           class="inputboxes" id="date5"/><input name="dateLastRefill"
                                                                                                 type="hidden"
                                                                                                 id="dateLastRefill"/><span
                                            id="dateHelp" class="errorspan"></span></td>
                                    <td width="20%"><label>Refill Period (days):</label></td>
                                    <td width="30%"><input name="duration" type="text" style="width: 50px;"
                                                           class="inputboxes" id="duration"/><span id="refillHelp"
                                                                                                   style="color:red"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label>Regimen Line (current):</label></td>
                                    <td>
                                        <select name="regimentypeId" style="width: 200px;" class="inputboxes"
                                                id="regimentypeId">
                                            <option></option>
                                        </select>
                                    </td>
                                    <td><label>Regimen (current):</label></td>
                                    <td>
                                        <select name="regimenId" style="width: 200px;" class="inputboxes"
                                                id="regimenId">
                                            <option></option>
                                        </select><span id="regimenHelp" class="errorspan"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label>Current Status:</label></td>
                                    <td>
                                        <select name="currentStatus" style="width: 200px;" class="inputboxes"
                                                id="currentStatus"/>
                                        <option></option>
                                        <option>HIV+ non ART</option>
                                        <option>ART Start</option>
                                        <option>ART Restart</option>
                                        <option>ART Transfer In</option>
                                        <option>Pre-ART Transfer In</option>
                                        <option>ART Transfer Out</option>
                                        <option>Pre-ART Transfer Out</option>
                                        <option>Lost to Follow Up</option>
                                        <option>Stopped Treatment</option>
                                        <option>Known Death</option>
                                        </select><span id="statusregHelp" class="errorspan"></span>
                                    </td>
                                    <td><label>Date of Current Status:</label></td>
                                    <td><input name="date3" type="text" style="width: 100px;" class="inputboxes"
                                               id="date3"/><input name="dateCurrentStatus" type="hidden"
                                                                  id="dateCurrentStatus"/><span id="dateHelp"
                                                                                                class="errorspan"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td><input name="patientId" type="hidden" class="inputboxes" id="patientId"/></td>
                                    <td><input name="rowcount" type="hidden" class="inputboxes" id="rowcount"/></td>
                                    <td colspan="4">&nbsp;</td>
                                </tr>
                            </table>
                        </div>
                        <p></p>
                        <div id="fileUploaded" style="display: none"></div>
                        <div>
                            <span style="margin-left:625px"><button id="save_button" style="width: 100px;">Save</button></span>
                        </div>
                    </div>
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
