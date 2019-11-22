<%-- 
    Document   : Appointment
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
    <script type="text/javascript" src="/js/lamis/anc-common.js"></script>
    <script type="text/javascript" src="/js/lamis/clinic-common.js"></script>

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
            if (!$('#patientId').val()) {
                fetch('/api/clinic/' + $('#clinicId').val(), {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    })
                }).then(function (response) {
                    return response.json()
                }).then(function (json) {
                    $('#patientId').val(json.clinicList[0].patientId);
                    $('#facilityId').val(json.clinicList[0].facilityId);
                })
            }
            $.ajax({
                url: "/api/clinic/" + $('#clinicId').val() || 0,
                dataType: "json",
                success: function (response) {
                    var clinicList = response.clinicList;
                    if (clinicList.length > 0) {
                        dateOfVisit = clinicList[0].dateVisit;
                        patientId = clinicList[0].patientId;
                        findDrugsByVisitDate(patientId, dateOfVisit);
                        findLabTestsByVisitDate(patientId, dateOfVisit);
                    }
                    populateForm(clinicList);
                }
            }); //end of ajax call


            showDrugPrescription();
            var lastSelected = -99;
            $("#adrgrid").jqGrid({
                url: "/api/adr/grid",
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
                                url: "/api/adr/update"
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
                url: "/api/oi/grid",
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
                url: "/api/adhere/grid",
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
                url: "/api/regimen/grid",
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
                    console.log(data);
                    if (isViewing === false) {
                        var populateMaps = new Map();
                        populateMaps = regimenMap.get(selectedType);
                        if (typeof populateMaps !== 'undefined') {
                            for (var [key, value] of populateMaps) {
                                if (oldRecord === true) {
                                    if (old_regimen_ids.has(key)) {
                                        if (old_regimen_ids.get(key) === "2") {
                                            jQuery('#drugGrid').jqGrid('setRowData', key, false, {background: 'yellow'});
                                            $("#drugGrid").jqGrid('setSelection', key);
                                            $("#" + key + "_duration").val(value);
                                        } else if (old_regimen_ids.get(key) === "1") {
                                            jQuery('#drugGrid').jqGrid('setRowData', key, false, {background: 'lime'});
                                            $("#drugGrid").jqGrid('setSelection', key);
                                            $("#" + key + "_duration").val(value);
                                        } else if (old_regimen_ids.get(key) === "0") {
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
                url: "/api/labtest/grid",
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
                if (checked === false) {
                    var populateMaps = new Map();
                    if (regimenMap != null && typeof regimenMap !== 'undefined') {
                        populateMaps = regimenMap.get(selectedType);
                        if (populateMaps != null && typeof populateMaps !== 'undefined') {
                            if (populateMaps.has(rowId)) {
                                $("#" + rowId + "_duration").val("");
                                //remove this from the map and all that...
                                populateMaps.delete(rowId);
                                if (populateMaps.size === 0)
                                    regimenMap.delete(selectedType);
                                var internal_regimen_ids = [];
                                for (var [key, value] of regimenMap) {
                                    for (var [k, v] of value) {
                                        internal_regimen_ids.push(k);
                                    }
                                }
                                $("#drugGrid").setGridParam({
                                    url: "/api/regimen/grid?q=1&regimen_ids=" + internal_regimen_ids.toString(),
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
                                    url: "/api/regimen/grid?q=1&regimen_ids=" + internal_regimen_ids.toString(),
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
                        url: "/api/regimen/grid?q=1&regimen_ids=" + internal_regimen_ids.toString(),
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
                window.location.href = "/event-page/patient/" + id;

            });

            $("#drugTypeId").change(function (event) {
                //Get all the selected data and prescriptions also here...
                getSelectedPrescriptions();
                selectedType = $("#drugTypeId").val();
                $("#drugGrid").setGridParam({
                    url: "/api/regimen/grid?page=1&regimentypeId=" + $("#drugTypeId").val(),
                    page: 1,
                    rows: 100
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
                        url: "/api/regimen/grid?q=1&regimen_ids=" + internal_regimen_ids.toString(),
                        page: 1
                    }).trigger("reloadGrid");
                    isViewing = true;
                } else {
                    var eID = document.getElementById("drugTypeId")
                    eID.options[selectedType].selected = "true";
                    if (selectedType != "0" && selectedType != "")
                        $("#drugGrid").setGridParam({
                            url: "/api/regimen/grid?q=1&regimentypeId=" + selectedType,
                            page: 1
                        }).trigger("reloadGrid");
                    else
                        $("#drugGrid").trigger("reloadGrid");
                    isViewing = false;
                }
            });

            $("#save_button").bind("click", function (event) {
                if ($("#userGroup").html() === "Data Analyst") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    if (validateForm()) {
                        var data = formMap();
                        var method = 'POST';
                        if (updateRecord) {
                            method = 'PUT';
                        }
                        fetch('/api/clinic', {
                            headers: new Headers({
                                'Content-Type': 'application/json'
                            }), method: method, body: JSON.stringify(data)
                        }).then(function (response) {
                            window.location = '/event-page/patient/' + $('#patientId').val();
                        }).catch(function (error) {
                        });
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        });

        function findDrugsByVisitDate(patientId, dateOfVisit) {
            if (dateOfVisit !== '') {
                $.ajax({
                    url: "/api/prescription/drugs-by-date/" + patientId + "?dateVisit=" + dateOfVisit,
                    type: "GET",
                    dataType: "json",
                    success: function (response) {
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
            if (dateOfVisit !== '') {
                $.ajax({
                    url: "/api/prescription/lab-tests-by-date/" + patientId + "?dateVisit=" + dateOfVisit,
                    dataType: "json",
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
                "patient_id": $("#patientId").val(),
                "regimen_map": regimen_ids.toString(),
                "dateVisit": myDate,
                "labtest_ids": labtest_ids.toString()
            };
            if (regimen_ids.length % 3 == 0) {
                $.ajax({
                    async: false,
                    url: "/api/prescription",
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
                url: "/api/regimen/types",
                dataType: "json",
                success: function (regimenTypeMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(regimenTypeMap, function (idx, obj) {
                        options += "<option value = '" + obj.id + "'>" + obj.description + "</option>";
                    })
                    $("#drugTypeId").html(options);
                }
            }); //end of ajax call
        }

        function checkDate() {
            if ($("#date1").val().length !== 0 && $("#date3").val().length !== 0) {
                if (parseInt(compare($("#date1").val(), $("#date3").val())) === -1) {
                    var message = "Date of next appointment cannot be earlier than date of visit";
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


<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>
<!-- MAIN CONTENT -->
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/clinic">Clinic</a></li>
        <li class="breadcrumb-item active" aria-current="page">New Clinic Visit</li>
    </ol>
</nav>
<form id="lamisform" theme="css_xhtml">
    <input name="deliveryId" type="hidden" id="deliveryId">
    <input name="ancId" type="hidden" id="ancId">
    <input name="partnerinformationId" type="hidden" id="partnerinformationId">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div id="messageBar"></div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Hospital No</label>
                            <input name="hospitalNum" type="text" class="form-control" id="hospitalNum"
                                   readonly="readonly"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <br/>
                            <span id="patientInfor"></span>
                            <input name="patientId" value="${patientId}" type="hidden" id="patientId"/>
                            <input name="clinicId" value="${clinicId}" type="hidden" id="clinicId"/>
                            <input name="facilityId" value="${facilityId}" type="hidden" id="facilityId"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Date of Visit:</label>
                                <input name="date1" type="text" class="form-control" id="date1"/>
                                <input name="dateVisit" type="hidden" id="dateVisit"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Clinical Stage:</label>
                                <select name="clinicStage" class="form-control select2" style="width: 100%"
                                        id="clinicStage">
                                    <option></option>
                                    <option value="Stage I">Stage I</option>
                                    <option value="Stage II">Stage II</option>
                                    <option value="Stage III">Stage III</option>
                                    <option value="Stage IV">Stage IV</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Functional Status:</label>
                                <select name="funcStatus" class="form-control select2" style="width: 100%"
                                        id="funcStatus">
                                    <option></option>
                                    <option value="Working">Working</option>
                                    <option value="Ambulatory">Ambulatory</option>
                                    <option value="Bedridden">Bedridden</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>TB Status:</label>
                                <select name="tbStatus" class="form-control select2" style="width: 100%" id="tbStatus">
                                    <option></option>
                                    <option value="No sign or symptoms of TB">No sign
                                        or symptoms of TB
                                    </option>
                                    <option value="TB suspected and referred for evaluation">TB
                                        suspected and referred for evaluation
                                    </option>
                                    <option value="Currently on INH prophylaxis">Currently
                                        on INH prophylaxis
                                    </option>
                                    <option value="Currently on TB treatment">Currently
                                        on TB treatment
                                    </option>
                                    <option value="TB positive not on TB drugs">TB
                                        positive not on TB drugs
                                    </option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3">
                            <div class="form-group">
                                <label>Body Weight(kg):</label>
                                <input name="bodyWeight" type="text" class="form-control" id="bodyWeight"/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label>Height(m):</label>
                                <input name="height" type="text" class="form-control" id="height"/>
                                <span id="bmi" style="color:red"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Blood Pressure(mmHg):</label>
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <input name="bp1" type="text" class="form-control"
                                                   id="bp1"/>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <input name="bp2" type="text" class="form-control" id="bp2"/>
                                        </div>
                                    </div>
                                </div>
                                <input name="bp" type="hidden" id="bp"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Pregnancy Status</label>
                                <select name="pregnantStatus" style="width: 100%;" class="form-control select2"
                                        id="pregnantStatus">
                                    <option></option>
                                    <option value="1">Not Pregnant</option>
                                    <option value="2">Pregnant</option>
                                    <option value="3">Breastfeeding</option>
                                    <span id="pregHelp" class="errorspan"></span>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>L.M.P:</label>
                                <input name="date2" type="text" class="form-control" id="date2" disabled="true"/>
                                <input name="lmp" type="hidden" id="lmp"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Opportunistic Infections:</label>
                                <select name="oiScreened" class="form-control select2" style="width: 100%"
                                        id="oiScreened">
                                    <option></option>
                                    <option value="No">No</option>
                                    <option value="Yes">Yes</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Adverse Drug Reactions:</label>
                                <select name="adrScreened" class="form-control select2" style="width: 100%"
                                        id="adrScreened">
                                    <option></option>
                                    <option value="No">No</option>
                                    <option value="Yes">Yes</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <!-- oi and adr options -->
                    <div class="row">
                        <div class="col-md-6">
                            <div id="oitable" style="display:none;">
                                <table id="oigrid" class="table stable"></table>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div id="adrtable" style="display:none;">
                                <table id="adrgrid" class="table stable"></table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Level of Adherence:</label>
                                <select name="adherenceLevel" class="form-control select2" style="width: 100%"
                                        id="adherenceLevel">
                                    <option></option>
                                    <option value="Good">Good</option>
                                    <option value="Fair">Fair</option>
                                    <option value="Poor">Poor</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Date of Next Appointment:</label>
                                <input name="date3" type="text" class="form-control datepicker" id="date3"/>
                                <input name="nextAppointment" type="hidden" id="nextAppointment"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div id="adheretable" style="display: none">
                                <table id="adheregrid"></table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <button id="prescription_button" class="btn btn-info">Prescribe Drugs</button>
                        </div>
                        <div class="col-md-6">
                            <button id="labtest_button" class="btn btn-info">Order Lab Tests</button>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="btn-group">
                                <input name="description" type="hidden" id="description"/>
                                <input name="screener" type="hidden" value="1" id="screener"/>
                                <input name="oiIds" type="hidden" id="oiIds"/><input name="adrId" type="hidden"
                                                                                     id="adrId"/>
                                <input name="adhereIds" type="hidden" id="adhereIds"/>

                                <input name="gender" type="hidden" id="gender"/>
                                <input name="dateBirth" type="hidden" id="dateBirth"/>
                                <input name="currentStatus" type="hidden" id="currentStatus"/>
                                <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/>
                                <input name="dateLastCd4" type="hidden" id="dateLastCd4"/>
                                <input name="dateLastClinic" type="hidden" id="dateLastClinic"/>
                                <input name="dateNextClinic" type="hidden" id="dateNextClinic"/>
                                <input name="commence" type="hidden" id="commence"/>
                            </div>
                        </div>

                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group" id="prescription" style="display: none">
                                <select name="drugTypeId" class="form-control" id="drugTypeId">
                                    <option></option>
                                </select>
                                <span id="drugIdHelp" class="errorspan"></span>

                                <div class="form-check mt-3">
                                    <label class="form-check-label">
                                        <input name="showSelected" type="checkbox" id="showSelected"
                                               class="form-check-input"/>
                                        <span class="form-check-sign"></span> Show selected prescriptions?
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--End of prescription-->
                    <div class="row">
                        <div class="col-md-6">
                            <div id="regimentable" style="display:none;">
                                <table id="drugGrid"></table>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <br/>
                            <div id="labtest" style="display: none;">
                                <div id="labtesttable">
                                    <table id="labtestGrid"></table>
                                </div>
                            </div>
                        </div>
                        <!-- End Lab Test-->
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div id="seldrug" style="display:none;">
                                <table id="selecteddrug"></table>
                            </div>
                        </div>
                        <!-- End Lab Test-->
                    </div>
                    <div id="userGroup" style="display: none">

                    </div>
                    <div class="pull-right">
                        <button id="save_button" type="button" class="btn btn-fill btn-info">Save</button>
                        <!--<button id="delete_button" disabled="true" type="submit" class="btn btn-fill btn-danger">Delete</button>-->
                        <button id="close_button" type="reset" class="btn btn-fill btn-default">Close</button>
                    </div>
                </div>
                <!-- END FORM CARD BODY -->
            </div>
            <!-- END FORM CARD -->
        </div>
        <!-- END FORM COL-->
    </div>
</form>
<!-- END MAIN CONTENT-->
<div id="user_group" style="display: none;">Clinician</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>

</html>