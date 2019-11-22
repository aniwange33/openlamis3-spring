<%-- 
    Document   : Clinic
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
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
    <script type="text/javascript" src="js/lamis/clinic-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/javascript" src="js/jquery.timer.js"></script>
    <script type="text/javascript" src="js/date.js"></script>
    <script type="text/javascript" src="js/zebra_dialog.js"></script>
    <script type="text/JavaScript">
        var oiIds = "", adrIds = "", adhereIds = "", date = "", lastSelectDate = "", selectedType = "",
            selctedOption = "", myDate = "", dateOfVisit = "", patientId = "";
        var updateRecord = false;
        var allFilled = true;
        var drugShowing = false;
        var testShowing = false;
        var regimenMap = new Map();
        var populateMap = new Map();
        var regimen_ids = [];
        var labtest_ids = [];
        var regimen_durations = [];
        var showSelected = false;
        var hasPrescriptions = false;
        var isViewing = false;
        var oldRecord = false;
        var old_regimen_ids = new Map();
        var old_drug_ids = new Map();
        var complete = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();
            var selectedValues = [];
            $.ajax({
                url: "Clinic_retrieve.action",
                dataType: "json",
                success: function (clinicList) {
                    if (clinicList.length > 0) {
                        dateOfVisit = clinicList[0].dateVisit;
                        patientId = clinicList[0].patientId;
                        findDrugsByVisitDate(patientId, dateOfVisit);
                        findLabTestsByVisitDate(patientId, dateOfVisit);
                    }
                    populateForm(clinicList);
                }
            }); //end of ajax call

            $("#date1").change(function () {
                if ($(this).val() != lastSelectDate) {
                    findClinicByDate();
                    var patientId = $("#id").val();
                    var dateVisit = $("#dateVisit").val();
                    findDrugsByVisitDate(patientId, dateVisit);
                    findLabTestsByVisitDate(patientId, dateVisit);

                    var internal_regimen_ids = [];
                    if (regimenMap.size > 0) {
                        for (var [key, value] of regimenMap) {
                            for (var [k, v] of value) {
                                internal_regimen_ids.push(k);
                            }
                        }
                        showSelected = $("#showSelected").prop("checked", true);
                        $("#drugGrid").setGridParam({
                            url: "Regimen_grid.action?q=1&regimen_ids=" + internal_regimen_ids.toString(),
                            page: 1
                        }).trigger("reloadGrid");
                        isViewing = true;
                    }
                }
                lastSelectDate = $(this).val();
            });
            showDrugPrescription();
            var lastSelected = -99;
            $("#adrgrid").jqGrid({
                url: "Adr_grid_clinic.action",
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

            $("#oigrid").jqGrid({
                url: "Oi_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Description"],
                colModel: [
                    {name: "description", index: "description", width: "250"},
                ],
                sortname: "oiId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                multiselect: true,
                height: 85,
                jsonReader: {
                    root: "oiList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "oiId"
                },
                loadComplete: function (data) {
                    $.each(oiIds, function (_, rowId) {
                        $("#oigrid").setSelection(rowId, true);
                    })
                }
            }); //end of jqGrid

            $("#adheregrid").jqGrid({
                url: "Adhere_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Description"],
                colModel: [
                    {name: "description", index: "description", width: "250"},
                ],
                sortname: "adhereId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                multiselect: true,
                height: 85,
                jsonReader: {
                    root: "adhereList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "adhereId"
                },
                loadComplete: function (data) {
                    $.each(adhereIds, function (_, rowId) {
                        $("#adheregrid").setSelection(rowId, true);
                    })
                }
            }); //end of jqGrid
            var lastSelected = -99;
            var lastSel;
            $("#drugGrid").jqGrid({
                url: "Regimen_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["", "Description", "Duration (in days)", "", ""],
                colModel: [
                    {name: "regimenId", index: "regimenId", width: "5", hidden: true},
                    {name: "description", index: "description", width: "260"},
                    {
                        name: "duration",
                        index: "duration",
                        width: "100",
                        sortable: false,
                        editable: true,
                        edittype: "text"
                    },
                    {name: "sel", index: "sel", width: "5", hidden: true},
                    {name: "regimentypeId", index: "regimentypeId", width: "5", hidden: true}
                ],
                sortname: "regimenId",
                sortorder: "desc",
                viewrecords: true,
                rowNum: 100,
                imgpath: "themes/basic/images",
                multiselect: true,
                resizable: false,
                height: 90,
                jsonReader: {
                    root: "regimenList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "regimenId"
                },
                loadComplete: function (data) {
                    //console.log(data);
                    if (isViewing == false) {
                        var populateMaps = new Map();
                        populateMaps = regimenMap.get(selectedType);
                        if (typeof populateMaps !== 'undefined') {
                            for (var [key, value] of populateMaps) {
                                if (oldRecord == true) {
                                    if (old_regimen_ids.has(key)) {
                                        if (old_regimen_ids.get(key) == "2") {
                                            jQuery('#drugGrid').jqGrid('setRowData', key, false, {background: 'yellow'});
                                            $("#drugGrid").jqGrid('setSelection', key);
                                            $("#" + key + "_duration").val(value);
                                        } else if (old_regimen_ids.get(key) == "1") {
                                            jQuery('#drugGrid').jqGrid('setRowData', key, false, {background: 'lime'});
                                            $("#drugGrid").jqGrid('setSelection', key);
                                            $("#" + key + "_duration").val(value);
                                        } else if (old_regimen_ids.get(key) == "0") {
                                            $("#drugGrid").jqGrid('setSelection', key);
                                            $("#" + key + "_duration").val(value);
                                        }
                                    } else {
                                        $("#drugGrid").jqGrid('setSelection', key);
                                        $("#" + key + "_duration").val(value);
                                    }
                                } else {
                                    jQuery('#drugGrid').jqGrid('setSelection', key);
                                    $("#" + key + "_duration").val(value);
                                }
                            }
                        }
                    } else {
                        for (var [key, value] of regimenMap) {
                            for (var [k, v] of value) {
                                if (oldRecord == true) {
                                    if (old_regimen_ids.has(k)) {
                                        if (old_regimen_ids.get(k) == "2") {
                                            jQuery('#drugGrid').jqGrid('setRowData', k, false, {background: 'yellow'});
                                            $("#drugGrid").jqGrid('setSelection', k);
                                            $("#" + k + "_duration").val(v);
                                        } else if (old_regimen_ids.get(k) == "1") {
                                            jQuery('#drugGrid').jqGrid('setRowData', k, false, {background: 'lime'});
                                            $("#drugGrid").jqGrid('setSelection', k);
                                            $("#" + k + "_duration").val(v);
                                        } else if (old_regimen_ids.get(k) == "0") {
                                            $("#drugGrid").jqGrid('setSelection', k);
                                            $("#" + k + "_duration").val(v);
                                        }
                                    } else {
                                        console.log("Here 1");
                                        $("#drugGrid").jqGrid('setSelection', k);
                                        $("#" + k + "_duration").val(v);
                                    }
                                } else {
                                    console.log("Here");
                                    jQuery('#drugGrid').jqGrid('setSelection', k);
                                    $("#" + k + "_duration").val(v);
                                }
                            }
                        }
                    }
                },
                onSelectRow: function (id) {
                    $("#drugGrid").jqGrid('editRow', id);
                }
            }); //end of jqGrid

            $("#labtestGrid").jqGrid({
                url: "Labtest_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Select Lab Tests"],
                colModel: [
                    {name: "description", index: "description", width: "370"}
                ],
                sortname: "labtestId",
                sortorder: "desc",
                rowNum: 100,
                viewrecords: true,
                imgpath: "themes/basic/images",
                multiselect: true,
                resizable: false,
                height: 130,
                jsonReader: {
                    root: "labtestList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "labtestId"
                }
            }); //end of Labtest jqGrid

            $(document).on("click", "td[aria-describedby='drugGrid_cb']", function (e) {
                var checked = $(e.target).is(":checked")
                var rowId = $(e.target).closest("tr").attr("id")
                if (checked == false) {
                    var populateMaps = new Map();
                    if (regimenMap != null && typeof regimenMap !== 'undefined') {
                        populateMaps = regimenMap.get(selectedType);
                        if (populateMaps != null && typeof populateMaps !== 'undefined') {
                            if (populateMaps.has(rowId)) {
                                $("#" + rowId + "_duration").val("");
                                //remove this from the map and all that...
                                populateMaps.delete(rowId);
                                if (populateMaps.size == 0)
                                    regimenMap.delete(selectedType);
                                var internal_regimen_ids = [];
                                for (var [key, value] of regimenMap) {
                                    for (var [k, v] of value) {
                                        internal_regimen_ids.push(k);
                                    }
                                }
                                $("#drugGrid").setGridParam({
                                    url: "Regimen_grid.action?q=1&regimen_ids=" + internal_regimen_ids.toString(),
                                    page: 1
                                }).trigger("reloadGrid");
                            } else {
                                for (var [key, value] of regimenMap) {
                                    for (var [k, v] of value) {
                                        if (value.has(rowId)) {
                                            $("#" + rowId + "_duration").val("");
                                            value.delete(rowId);
                                            if (value.size == 0) {
                                                regimenMap.delete(key);
                                            }
                                        }
                                    }
                                }
                                var internal_regimen_ids = [];
                                for (var [key, value] of regimenMap) {
                                    for (var [k, v] of value) {
                                        internal_regimen_ids.push(k);
                                    }
                                }
                                $("#drugGrid").setGridParam({
                                    url: "Regimen_grid.action?q=1&regimen_ids=" + internal_regimen_ids.toString(),
                                    page: 1
                                }).trigger("reloadGrid");
                            }
                        }
                    }
                    $("#" + rowId + "_duration").val("");
                }
            });

            $(document).on("blur", "td[aria-describedby='drugGrid_duration']", function (e) {
                var rowId = $(e.target).closest("tr").attr("id")

                if (isViewing == true) {
                    console.log(rowId);
                    for (var [key, value] of regimenMap) {
                        for (var [k, v] of value) {
                            if (value.has(rowId)) {
                                console.log("Old Value is: " + v);
                                var duration = getCellValue(rowId, 'duration');
                                value.set(k, duration);
                            }
                        }
                    }
                    var internal_regimen_ids = [];
                    console.log("New Regimen Map: " + regimenMap);
                    for (var [key, value] of regimenMap) {
                        for (var [k, v] of value) {
                            console.log("New Value is: " + v);
                            internal_regimen_ids.push(k);
                        }
                    }
                }
            });

            $("#adrScreened").bind("click", function () {
                if ($("#adrScreened").val() == "Yes") {
                    $("#adr_button").removeAttr("disabled");
                } else {
                    $("#adr_button").attr("disabled", "disabled");
                }
            });

            $("#oiScreened").bind("click", function () {
                if ($("#oiScreened").val() == "Yes") {
                    $("#oi_button").removeAttr("disabled");
                } else {
                    $("#oi_button").attr("disabled", "disabled");
                }
            });
            $("#adherenceLevel").bind("click", function () {
                if ($("#adherenceLevel").val() == "Fair" || $("#adherenceLevel").val() == "Poor") {
                    $("#adhere_button").removeAttr("disabled");
                } else {
                    $("#adhere_button").attr("disabled", "disabled");
                }
            });

            $("#adr_button").bind("click", function (event) {
                $("#adrtable").toggle("slow");
                return false;
            }); //show and hide grid

            $("#oi_button").bind("click", function (event) {
                $("#oitable").toggle("slow");
                return false;
            }); //show and hide grid

            $("#adhere_button").bind("click", function (event) {
                $("#adheretable").toggle("slow");
                return false;
            }); //show and hide grid

            $("#prescription_button").bind("click", function () {
                var internal_regimen_ids = [];
                if (regimenMap.size > 0) {
                    for (var [key, value] of regimenMap) {
                        for (var [k, v] of value) {
                            internal_regimen_ids.push(k);
                        }
                    }
                    showSelected = $("#showSelected").prop("checked", true);
                    $("#drugGrid").setGridParam({
                        url: "Regimen_grid.action?q=1&regimen_ids=" + internal_regimen_ids.toString(),
                        page: 1
                    }).trigger("reloadGrid");
                    isViewing = true;
                }
                if (testShowing) {
                    $("#labtest").toggle("slow");
                    testShowing = false;
                }
                if (drugShowing == false) {
                    $("#prescription").toggle("slow");
                    drugShowing = true;
                } else {
                    $("#prescription").toggle("slow");
                    drugShowing = false;
                }
                return false;
            });
            $("#labtest_button").bind("click", function () {
                if (drugShowing) {
                    $("#prescription").toggle("slow");
                    drugShowing = false;
                }
                if (testShowing == false) {
                    $("#labtest").toggle("slow");
                    testShowing = true;
                } else {
                    $("#labtest").toggle("slow");
                    testShowing = false;
                }
                return false;
            });

            $("#date1").bind("change", function (event) {
                checkDate();
            });
            $("#date3").bind("change", function (event) {
                checkDate();
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Clinic_search");
                return true;
            });
            $("#drugTypeId").change(function (event) {
                //Get all the selected data and prescriptions also here...
                getSelectedPrescriptions();
                selectedType = $("#drugTypeId").val();
                $("#drugGrid").setGridParam({
                    url: "Regimen_grid.action?q=1&regimentypeId=" + $("#drugTypeId").val(),
                    page: 1
                }).trigger("reloadGrid");
                //Uncheck the selected box
                showSelected = $("#showSelected").prop("checked", false);
                isViewing = false;
            });

            $("#showSelected").bind("click", function (event) {
                showSelected = $("#showSelected").prop("checked");
                //$("#drugTypeId").
                if (showSelected == true) {
                    getSelectedPrescriptions();
                    showDrugPrescription();
                    var internal_regimen_ids = [];
                    for (var [key, value] of regimenMap) {
                        for (var [k, v] of value) {
                            internal_regimen_ids.push(k);
                        }
                    }
                    $("#drugGrid").setGridParam({
                        url: "Regimen_grid.action?q=1&regimen_ids=" + internal_regimen_ids.toString(),
                        page: 1
                    }).trigger("reloadGrid");
                    isViewing = true;
                } else {
                    var eID = document.getElementById("drugTypeId")
                    eID.options[selectedType].selected = "true";
                    if (selectedType != "0" && selectedType != "")
                        $("#drugGrid").setGridParam({
                            url: "Regimen_grid.action?q=1&regimentypeId=" + selectedType,
                            page: 1
                        }).trigger("reloadGrid");
                    else
                        $("#drugGrid").trigger("reloadGrid");
                    isViewing = false;
                }
            });

            $("#save_button").bind("click", function (event) {
                if ($("#userGroup").html() == "Data Analyst") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    myDate = $("#dateVisit").val();
                    //Check for prescriptions
                    hasPrescriptions = checkForPrescriptions().size > 0;
                    if (validateForm()) {
                        if (updateRecord) {
                            if (hasPrescriptions) {
                                callSavePrescriptions(event);
                                if (complete == true) {
                                    $("#lamisform").attr("action", "Clinic_update");
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                $("#lamisform").attr("action", "Clinic_update");
                                return true;
                            }
                        } else {
                            if (hasPrescriptions) {
                                callSavePrescriptions(event);
                                if (complete == true) {
                                    $("#lamisform").attr("action", "Clinic_save");
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                $("#lamisform").attr("action", "Clinic_save");
                                return true;
                            }
                        }
                    } else {
                        return false;
                    }
                }
            });
        });

        function findDrugsByVisitDate(patientId, dateOfVisit) {
            if (dateOfVisit != '') {
                var data = {"patientId": patientId, "dateVisit": dateOfVisit};
                $.ajax({
                    url: "Get_drugs_by_date.action",
                    type: "POST",
                    dataType: "json",
                    data: data,
                    success: function (response) {
                        //console.log(response.drugList);
                        var data = response.drugList;
                        regimenMap = new Map();
                        old_regimen_ids = new Map();
                        for (var i = 0; i < data.length; i += 4) {
                            var selectionMap = new Map();
                            var regimenId = data[i + 1];
                            var duration = data[i + 2];
                            old_regimen_ids.set(data[i + 1], data[i + 3]);
                            selectionMap.set(regimenId, duration);

                            if (data[i] === "1" || data[i] === "2" || data[i] === "3" || data[i] === "4")
                                selectedType = data[i];
                            if (data[i] != "" && selectionMap.size > 0)
                                regimenMap.set(data[i], selectionMap);

                        }
                        oldRecord = true;
                    },
                    complete: function () {

                    }
                }); //end of ajax call
            }
        }

        function convertDate(fullDate) {
            var convertedDate = "";
            var twoDigitMonth = ((fullDate.getMonth().length + 1) === 1) ? (fullDate.getMonth() + 1) : '0' + (fullDate.getMonth() + 1);
            convertedDate = twoDigitMonth + "/" + fullDate.getDate() + "/" + fullDate.getFullYear();

            return convertedDate;
        }

        function findLabTestsByVisitDate(patientId, dateOfVisit) {
            if (dateOfVisit != '') {
                var data = {"patientId": patientId, "dateVisit": dateOfVisit};
                $.ajax({
                    url: "Get_tests_by_date.action",
                    type: "POST",
                    dataType: "json",
                    data: data,
                    success: function (response) {
                        var data = response.labtestList;
                        for (i = 0; i < data.length; i += 2) {
                            labtest_ids.push(data[i]);
                            jQuery('#labtestGrid').jqGrid('setSelection', data[i]);
                            if (data[i + 1] === "1")
                                jQuery('#labtestGrid').jqGrid('setRowData', data[i], false, {background: 'lime'});
                            else if (data[i + 1] === "2")
                                jQuery('#labtestGrid').jqGrid('setRowData', data[i], false, {background: 'yellow'});
                            //NB: i+1 is the status will be used if needed to determine the dispensed and colored...
                        }
                        oldRecord = true;
                    }
                }); //end of ajax call
            }
        }

        function checkForPrescriptions() {
            var mapToReturn = null;
            if (regimenMap.size == 0)
                mapToReturn = getSelectedPrescriptions();
            else
                mapToReturn = regimenMap;
            return mapToReturn;
        }

        function callSavePrescriptions(event) {
            //Save the prescriptions...
            if (isViewing == false)
                getSelectedPrescriptions();
            for (var [key, value] of regimenMap) {
                for (var [k, v] of value) {
                    regimen_ids.push(key);
                    regimen_ids.push(k);
                    if (v != '')
                        regimen_ids.push(v);
                }
            }
            labtest_ids = [];
            //console.log("Regimen Ids: "+regimen_ids);
            var labtestIds = jQuery("#labtestGrid").jqGrid('getGridParam', 'selarrrow');
            for (i = 0; i < labtestIds.length; i++) {
                labtest_ids.push(labtestIds[i]);
            }
            var data = {
                "patient_id": $("#id").val(),
                "regimen_map": regimen_ids.toString(),
                "dateVisit": myDate,
                "labtest_ids": labtest_ids.toString()
            };
            console.log(data);
            if (regimen_ids.length % 3 == 0) {
                $.ajax({
                    async: false,
                    url: "Selected_prescriptions_save.action",
                    type: "POST",
                    data: data,
                    success: function () {
                        complete = true;
                    }
                });//end of ajax call
            } else {
                //alert("No duration specified for one of the regimens selected!");
            }
        }

        function getSelectedPrescriptions() {
            var selectionMap = new Map();
            var regimenType = selectedType;
            var ids = jQuery("#drugGrid").jqGrid('getGridParam', 'selarrrow');
            //console.log(ids);
            for (var i = 0; i < ids.length; i++) {
                var data = jQuery("#drugGrid").jqGrid('getRowData', ids[i]);
                var regimenId = ids[i];
                var duration = getCellValue(ids[i], 'duration');
                selectionMap.set(regimenId, duration);
            }
            if (regimenType != "" && selectionMap.size > 0)
                regimenMap.set(regimenType, selectionMap);

            return regimenMap;
        }

        function showDrugPrescription() {
            //Retrieve all Regimen...
            $.ajax({
                url: "RegimenType_retrieve_id.action",
                dataType: "json",
                success: function (regimenTypeMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(regimenTypeMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    })
                    $("#drugTypeId").html(options);
                }
            }); //end of ajax call
        }

        function checkDate() {
            if ($("#date1").val().length != 0 && $("#date3").val().length != 0) {
                if (parseInt(compare($("#date1").val(), $("#date3").val())) == -1) {
                    var message = "Date of next appointment cannot be ealier than date of visit";
                    $("#messageBar").html(message).slideDown('slow');
                } else {
                    $("#messageBar").slideUp('slow');
                }
            }
        }

        function getCellValue(rowId, cellId) {
            var cell = jQuery('#' + rowId + '_' + cellId);
            var val = cell.val();
            return val;
        }

        function emptyCellValue(rowId, cellId) {
            var cell = jQuery('#' + rowId + '_' + cellId);
            var val = "";
            return val;
        }
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">

        <jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/clinic.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Clinic >> ART Clinic >> Clinic Visit</strong></span>
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
                        <td width="20%"><span id="patientInfor"></span><input name="name" type="hidden" id="name"/></td>
                        <td width="25%"><input name="patientId" type="hidden" id="patientId"/><input name="clinicId"
                                                                                                     type="hidden"
                                                                                                     id="clinicId"/>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Date of Visit:</label></td>
                        <td><input name="date1" type="text" style="width: 100px;" class="inputboxes" id="date1"/><input
                                name="dateVisit" type="hidden" id="dateVisit"/><span id="dateHelp"
                                                                                     class="errorspan"></span></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><label>Clinical Stage:</label></td>
                        <td>
                            <select name="clinicStage" style="width: 100px;" class="inputboxes" id="clinicStage">
                                <option></option>
                                <option>Stage I</option>
                                <option>Stage II</option>
                                <option>Stage III</option>
                                <option>Stage IV</option>
                            </select>
                        </td>
                        <td><label>Functional Status:</label></td>
                        <td>
                            <select name="funcStatus" style="width: 100px;" class="inputboxes" id="funcStatus">
                                <option></option>
                                <option>Working</option>
                                <option>Ambulatory</option>
                                <option>Bedridden</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><label>TB Status:</label></td>
                        <td>
                            <select name="tbStatus" style="width: 170px;" class="inputboxes" id="tbStatus">
                                <option></option>
                                <option>No sign or symptoms of TB</option>
                                <option>TB suspected and referred for evaluation</option>
                                <option>Currently on INH prophylaxis</option>
                                <option>Currently on TB treatment</option>
                                <option>TB positive not on TB drugs</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Body Weight(kg):</label></td>
                        <td><input name="bodyWeight" type="text" style="width: 50px;" class="inputboxes"
                                   id="bodyWeight"/></td>
                        <td><label>Height(cm):</label></td>
                        <td><input name="height" type="text" style="width: 50px;" class="inputboxes" id="height"/><span
                                id="bmi" style="color:red"></span></td>
                    </tr>
                    <tr>
                        <td><label>Blood Pressure(mmHg):</label></td>
                        <td><input name="bp1" type="text" style="width: 25px;" class="inputboxes" id="bp1"/> <input
                                name="bp2" type="text" style="width: 25px;" class="inputboxes" id="bp2"/><input
                                name="bp" type="hidden" style="width: 50px;" class="inputboxes" id="bp"/></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input name="pregnant" type="checkbox" value="1" id="pregnant"
                                   disabled="true"/>&nbsp;<label>P r e g n a n t</label></td>
                        <td><label>L.M.P:</label>&nbsp;<input name="date2" type="text" style="width: 100px;"
                                                              class="inputboxes" id="date2" disabled="true"/><input
                                name="lmp" type="hidden" id="lmp"/></td>
                        <td><input name="breastfeeding" type="checkbox" value="1" id="breastfeeding" disabled="true"/>&nbsp;<label>B
                            r e a s t f e e d i n g</label></td>
                    </tr>
                    <tr>
                        <td><label>Opportunistic Infections:</label></td>
                        <td>
                            <select name="oiScreened" style="width: 80px;" class="inputboxes" id="oiScreened">
                                <option></option>
                                <option>No</option>
                                <option>Yes</option>
                            </select>
                            <button id="oi_button" style="width: 20px;" disabled="true">...</button>
                        </td>
                        <td><label>Adverse Drug Reactions:</label></td>
                        <td>
                            <select name="adrScreened" style="width: 80px;" class="inputboxes" id="adrScreened">
                                <option></option>
                                <option>No</option>
                                <option>Yes</option>
                            </select>
                            <button id="adr_button" style="width: 20px;" disabled="true">...</button>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <div id="oitable" style="display: none">
                                <table id="oigrid"></table>
                            </div>
                        </td>
                        <td colspan="2">
                            <div id="adrtable" style="display: none">
                                <table id="adrgrid"></table>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Level of Adherence:</label></td>
                        <td>
                            <select name="adherenceLevel" style="width: 80px;" class="inputboxes" id="adherenceLevel">
                                <option></option>
                                <option>Good</option>
                                <option>Fair</option>
                                <option>Poor</option>
                            </select>
                            <button id="adhere_button" style="width: 20px;" disabled="true">...</button>
                        </td>
                        <td><label>Date of Next Appointment:</label></td>
                        <td><input name="date3" type="text" style="width: 100px;" class="inputboxes" id="date3"/><input
                                name="nextAppointment" value="" type="hidden" id="nextAppointment"/><span id="nextHelp"
                                                                                                          style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <div id="adheretable" style="display: none">
                                <table id="adheregrid"></table>
                            </div>
                        </td>
                        <td><input name="oiIds" type="hidden" id="oiIds"/><input name="adrId" type="hidden" id="adrId"/><input
                                name="adhereIds" type="hidden" id="adhereIds"/></td>
                        <td><input name="description" type="hidden" id="description"/><input name="screener"
                                                                                             type="hidden" value="1"
                                                                                             id="screener"/></td>
                    </tr>
                    <!--                           <tr>
                                                    <td><label>Notes:</label></td>




                                                    <td colspan="3"><textarea name="notes" rows="2" cols="40" id="notes"></textarea></td>
                                                    <td><input name="notes" type="hidden" id="notes"/></td>
                                                </tr>-->

                    <tr>
                        <td><input name="gender" type="hidden" id="gender"/><input name="dateBirth" type="hidden"
                                                                                   id="dateBirth"/></td>
                        <td><input name="currentStatus" type="hidden" id="currentStatus"/><input
                                name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/></td>
                        <td><input name="dateLastCd4" type="hidden" id="dateLastCd4"/><input name="dateLastClinic"
                                                                                             type="hidden"
                                                                                             id="dateLastClinic"/></td>
                        <td><input name="dateNextClinic" type="hidden" id="dateNextClinic"/><input name="commence"
                                                                                                   type="hidden"
                                                                                                   id="commence"/></td>
                    </tr>
                    <tr id="prescriptions" hidden="true">
                        <td>
                            <button id="prescription_button">Prescribe Drugs</button>
                        </td>
                        <td>
                            <button id="labtest_button">Order Lab Tests</button>
                        </td>
                    </tr>
                </table>
                <hr>
                <!--The prescription Page-->
                <div id="prescription" style="display: none">
                    <p></p>
                    <table width="99%" border="0" class="space" cellpadding="3">
                        <tr>
                            <td colspan="3">
                                <select name="drugTypeId" style="width: 395px;" class="inputboxes" id="drugTypeId">
                                    <option></option>
                                </select><span id="drugIdHelp" class="errorspan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="regimentable">
                                    <table id="drugGrid"></table>
                                </div>
                            </td>
                        </tr>
                        <tr>

                        </tr>
                    </table>
                    <div style="align: left">
                        <input name="showSelected" type="checkbox" id="showSelected"/>&nbsp;<label>Show selected
                        prescriptions?</label>
                    </div>
                </div>
                <!--End of prescription-->

                <!--The Lab Test Page-->
                <div id="labtest" style="display: none">
                    <p></p>
                    <table width="99%" border="0" class="space" cellpadding="1">
                        <tr>
                            <!--                                    <td><label>Laboratory Tests</label></td>-->
                            <td>
                                <div id="labtesttable">
                                    <table id="labtestGrid"></table>
                                </div>
                            </td>
                        </tr>
                        <tr>

                        </tr>
                    </table>
                </div>
                <!--End of LabTest-->

                <div id="userGroup" style="display: none"><s:property value="#session.userGroup"/></div>
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
