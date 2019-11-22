<%-- 
    Document   : Patient
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
    <script type="text/javascript" src="js/lamis/pharmacy-common.js"></script>
    <script type="text/javascript" src="js/lamis/prescribed_drugs.js"></script>
    <script type="text/JavaScript">
        var obj = {};
        var date = "";
        var regimenIds = [];
        var regex = /1|2|3|4|14/g;
        var adrIds = "", date = "", lastSelectDate = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();
            getPrescribedDrugs();

            $.ajax({
                url: "Pharmacy_retrieve.action",
                dataType: "json",
                success: function (pharmacyList) {
                    populateForm(pharmacyList);
                }
            }); //end of ajax call

            var lastSelected = -99;
            $("#grid").jqGrid({
                url: "Dispenser_grid_retrieve.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Description", "Morning", "Afternoon", "Evening", "Duration", "Quantity"],
                colModel: [
                    {name: "description", index: "description", width: "400"},
                    {name: "morning", index: "morning", width: "60", sortable: false, editable: true, edittype: "text"},
                    {
                        name: "afternoon",
                        index: "afternoon",
                        width: "60",
                        sortable: false,
                        editable: true,
                        edittype: "text"
                    },
                    {name: "evening", index: "evening", width: "60", sortable: false, editable: true, edittype: "text"},
                    {
                        name: "duration",
                        index: "duration",
                        width: "60",
                        sortable: false,
                        editable: true,
                        edittype: "text"
                    },
                    {name: "quantity", index: "quantity", width: "80"},
                ],
                sortname: "regimendrugId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "dispenserList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "regimendrugId"
                }, loadComplete: function (data) {
                    //console.log(data);
                },
                onSelectRow: function (id) {
                    var data = jQuery("#grid").jqGrid('getRowData', id);
                    //console.log(data);
                    if (id && id != lastSelected) {
                        $("#grid").jqGrid('saveRow', lastSelected,
                            {
                                successfunc: function (response) {
                                    return true;
                                },
                                url: "Dispenser_update.action"
                            })
                        lastSelected = id;
                    }
                    $("#regdrugId").val(id);
                    $("#grid").jqGrid('editRow', id);
                } //end of onSelectRow
            }); //end of jqGrid 

            var lastSelected = -99;
            $("#adrgrid").jqGrid({
                url: "Adr_grid_pharmacy.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Description", "Severity"],
                colModel: [
                    {name: "description", index: "description", width: "250"},
                    {
                        name: "severity",
                        index: "severity",
                        width: "70",
                        sortable: false,
                        editable: true,
                        edittype: "select",
                        editoptions: {value: " : ;Grade 1:Grade 1;Grade 2:Grade 2;Grade 3:Grade 3;Grade 4:Grade 4"}
                    },
                ],
                sortname: "adrId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 100,
                jsonReader: {
                    root: "adrList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "adrId"
                },
                onSelectRow: function (id) {
                    if (id && id != lastSelected) {
                        $("#adrgrid").jqGrid('saveRow', lastSelected,
                            {
                                successfunc: function (response) {
                                    return true;
                                },
                                url: "Adr_update.action"
                            })
                        lastSelected = id;
                    }
                    $("#adrgrid").jqGrid('editRow', id);
                    $("#adrId").val(id);
                    var data = $("#adrgrid").getRowData(id)
                    $("#description").val(data.description);
                }, //end of onSelectRow
                loadComplete: function (data) {
                    for (i = 0; i < adrIds.length; i++) {
                        var values = adrIds[i].split(",");
                        $("#adrgrid").jqGrid('setCell', values[0], 'severity', 'Grade ' + values[1]);
                    }
                }
            }); //end of jqGrid 

            var selectedId = -99;
            var selId;
            var comingFrom;
            $("#regimengrid").jqGrid({
                url: "Regimen_retrieve_id.action?q=1&regimentypeId=" + $("regimentypeId").val(),
                datatype: "json",
                mtype: "GET",
                colNames: ["", "", "Description", ""],
                colModel: [
                    {name: "regimenId", index: "regimenId", width: "5", hidden: true},
                    {name: "selectedRegimen", index: "selectedRegimen", width: "5", hidden: true},
                    {name: "description", index: "description", width: "275"},
                    {name: "regimentypeId", index: "regimentypeId", width: "5", hidden: true}
                ],
                sortname: "regimenId",
                sortorder: "desc",
                viewrecords: true,
                rowNum: 100,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 100,
                jsonReader: {
                    root: "regimenPharmList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "regimenId"
                },
                loadComplete: function (data) {
                    //console.log(data);
                    if (data.regimenPharmList.length > 0) {
                        for (var i = 0; i < data.regimenPharmList.length; i++) {
                            if (data.regimenPharmList[i].regimenId === data.regimenPharmList[i].selectedRegimen) {
                                var id = data.regimenPharmList[i].selectedRegimen;
                                selId = id;
                                comingFrom = "prescriptions";
                                $("#regimengrid").jqGrid('setSelection', id);
                                $("#grid").setGridParam({
                                    url: "Dispenser_grid_retrieve",
                                    page: 1
                                }).trigger("reloadGrid");
                                //$("#refill").blur();
                                //updateRefill();
                            }
                        }
                    } else {

                    }
                },
                onSelectRow: function (id) {
                    if (comingFrom === "prescriptions") {
                        if (selectedId !== selId) {
                            selectedId = id;
                        } else if (selectedId !== id) {
                            showDrugCompositionForRegimen(id);
                        } else if (selectedId === id) {
                            showDrugCompositionForRegimen(id);
                        }
                    } else {
                        showDrugCompositionForRegimen(id);
                    }
                    $("#id").val(id);
                }
            });
            $("#adrScreened").bind("click", function () {
                if ($("#adrScreened").val() == "Yes") {
                    $("#adr_button").removeAttr("disabled");
                } else {
                    $("#adr_button").attr("disabled", "disabled");
                }
            });

            $("#adr_button").bind("click", function (event) {
                $("#adrtable").toggle("slow");
                return false;
            }); //show and hide grid

            $("#date1").bind("change", function (event) {
                checkDate();
            });
            $("#date2").bind("change", function (event) {
                checkDate();
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Pharmacy_search");
                return true;
            });
        });

        function checkDate() {
            if ($("#date1").val().length != 0 && $("#date2").val().length != 0) {
                if (parseInt(compare($("#date1").val(), $("#date2").val())) == -1) {
                    var message = "Date of next appointment cannot be ealier than date of visit";
                    $("#messageBar").html(message).slideDown('slow');
                } else {
                    $("#messageBar").slideUp('slow');
                }
            }
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_pharmacy.jsp"/>
<div id="messageBar"></div>

<div id="accordion">
    <h3><a href="#">Patient Medical Records</a></h3>
    <div id="tabs">
        <ul>
            <li><a href="#tabs-1">Clinic Record</a></li>
            <li><a href="#tabs-2">Pharmacy Record</a></li>
            <li><a href="#tabs-3">Laboratory Record</a></li>
        </ul>
        <div id="tabs-1">
            <p>This Page will contain the Last Clinic Record of the client</p>
        </div>
        <div id="tabs-2">
            <p>This page will Contain the last Pharmacy Record of the client</p>
        </div>
        <div id="tabs-3">
            <p>This View Will contain the last Laboratory Record of the client</p>
            <p></p>
        </div>
    </div>
    <h3><a href="#">Patient Pharmacy Data Entry</a></h3>
    <div>
        <table width="99%" border="0" class="space" cellpadding="3">
            <tr>
                <td width="20%"><label>Hospital No:</label></td>
                <td width="20%"><input name="hospitalNum" type="text" class="inputboxes" id="hospitalNum"
                                       readonly="readonly"/></td>
                <td width="25%"><span id="patientInfor"></span><input name="name" type="hidden" id="name"/></td>
                <td width="20%"><input name="patientId" type="hidden" id="patientId"/></td>
            </tr>
            <tr>
                <td><label>Date of Dispensing:</label></td>
                <td><input name="date1" type="text" style="width: 100px;" class="inputboxes" id="date1"/><input
                        name="dateVisit" type="hidden" id="dateVisit"/><span id="dateHelp" class="errorspan"></span>
                </td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td><label>Regimen/Drugs:</label></td>
                <td colspan="3">
                    <select name="regimentypeId" style="width: 280px;" class="inputboxes" id="regimentypeId">
                        <option></option>
                    </select>
                    <input name="regimenId" type="hidden" id="regimenId"/></td>
                </td>
            </tr>
            <tr>
                <td></td>
                <td colspan="2">
                    <div id="regimentable">
                        <table id="regimengrid"></table>
                        <!--<input name="stateIds" type="text"  hidden id="stateIds"/>-->
                    </div>
                </td>
            </tr>

            <tr>
                <td><label>Refill Period (days):</label></td>
                <td><input name="refill" type="text" style="width: 50px;" class="inputboxes" id="refill"/><span
                        id="refillHelp" style="color:red"></span></td>
                <td><input name="prescripError" type="checkbox" value="0" id="prescripError"/>&nbsp;<label>Any
                    prescription error?</label></td>
                <td><input name="adherence" type="checkbox" value="0" id="adherence"/>&nbsp;<label>Adherence counseling
                    conducted?</label></td>
            </tr>
            <tr>
                <td><label>Adverse Drug Reactions:</label></td>
                <td>
                    <select name="adrScreened" style="width: 80px;" class="inputboxes" id="adrScreened">
                        <option></option>
                        <option>No</option>
                        <option>Yes</option>
                    </select>
                    <button id="adr_button" style="width: 20px;" disabled="true">...</button>
                </td>
                <td><label>Date of Next Appointment:</label></td>
                <td><input name="date2" type="text" style="width: 100px;" class="inputboxes" id="date2"/><input
                        name="nextAppointment" type="hidden" id="nextAppointment"/><span id="nextHelp"
                                                                                         style="color:red"></span></td>
            </tr>
            <tr>
                <td></td>
                <td colspan="2">
                    <div id="adrtable" style="display: none">
                        <table id="adrgrid"></table>
                    </div>
                </td>
            </tr>
            <tr>
                <td><input name="gender" type="hidden" id="gender"/><input name="dateBirth" type="hidden"
                                                                           id="dateBirth"/></td>
                <td><input name="currentStatus" type="hidden" id="currentStatus"/><input name="dateCurrentStatus"
                                                                                         type="hidden"
                                                                                         id="dateCurrentStatus"/><input
                        name="dateStarted" type="hidden" id="dateStarted"/></td>
                <td><input name="adrId" type="hidden" id="adrId"/><input name="screener" type="hidden" value="2"
                                                                         id="screener"/><input name="regdrugId"
                                                                                               type="hidden"
                                                                                               id="regdrugId"/><input
                        name="dateLastRefill" type="hidden" id="dateLastRefill"/></td>
                <td><input name="dateNextRefill" type="hidden" id="dateNextRefill"/><input name="regimentypePrevious"
                                                                                           type="hidden"
                                                                                           id="regimentypePrevious"/><input
                        name="regimenPrevious" type="hidden" id="regimenPrevious"/></td>
            </tr>
        </table>
        <div>
            <fieldset>
                <legend> Drug Dispensed</legend>
                <table width="99%" height="90" border="0" class="space">
                    <tr>
                        <td>
                            <table id="grid"></table>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </div>
        <p></p>
    </div>
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
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>

<script type="text/javascript" src="js/lamis/report-common.js"></script>
<script>
    $("#accordion").accordion({
        collapsible: true,
        heightStyle: "content"
    });
    $("#tabs").tabs();
</script>
</body>
</html>
