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
        var oiIds = "", adrIds = "", adhereIds = "", date = "", lastSelectDate = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            $.ajax({
                url: "Clinic_retrieve.action",
                dataType: "json",
                success: function (clinicList) {
                    populateForm(clinicList);
                }
            }); //end of ajax call

            $.ajax({
                url: "Chroniccare_retrieve.action",
                dataType: "json",
                success: function (chroniccareList) {
                    populateHiddenForm(chroniccareList);
                }
            }); //end of ajax call

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

            $("#date1").bind("change", function (event) {
                checkDate();
            });
            $("#date3").bind("change", function (event) {
                checkDate();
            });

            $("#tbscreengrid").jqGrid({
                url: "Tbscreen_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Description", "Value"],
                colModel: [
                    {name: "description", index: "description", width: "270"},
                    {
                        name: "valueTb",
                        index: "valueTb",
                        width: "70",
                        sortable: false,
                        editable: true,
                        edittype: "select",
                        editoptions: {value: " : ;Yes:Yes;No:No"}
                    },
                ],
                sortname: "tbscreenId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 100,
                jsonReader: {
                    root: "tbscreenList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "tbscreenId"
                },
                onSelectRow: function (id) {
                    if (id && id != lastSelected) {
                        $("#tbscreengrid").jqGrid('saveRow', lastSelected,
                            {
                                successfunc: function (response) {
                                    return true;
                                },
                                url: "Tbscreen_update.action"
                            })
                        lastSelected = id;
                    }
                    $("#tbscreengrid").jqGrid('editRow', id);
                    var data = $("#tbscreengrid").getRowData(id)
                    $("#descriptionTb").val(data.description);
                } //end of onSelectRow
            }); //end of jqGrid

            $("#dmscreengrid").jqGrid({
                url: "Dmscreen_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Description", "Value"],
                colModel: [
                    {name: "description", index: "description", width: "220"},
                    {
                        name: "valueDm",
                        index: "valueDm",
                        width: "70",
                        sortable: false,
                        editable: true,
                        edittype: "select",
                        editoptions: {value: " : ;Yes:Yes;No:No"}
                    }
                ],
                sortname: "dmscreenId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 100,
                jsonReader: {
                    root: "dmscreenList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "dmscreenId"
                },
                onSelectRow: function (id) {
                    if (id && id != lastSelected) {
                        $("#dmscreengrid").jqGrid('saveRow', lastSelected,
                            {
                                successfunc: function (response) {
                                    return true;
                                },
                                url: "Dmscreen_update.action"
                            })
                        lastSelected = id;
                    }
                    $("#dmscreengrid").jqGrid('editRow', id);
                    var data = $("#dmscreengrid").getRowData(id)
                    $("#descriptionDm").val(data.description);
                } //end of onSelectRow
            }); //end of jqGrid

            $("#tbTreatment").bind("change", function () {
                if ($("#tbTreatment").val() == "No") {
                    $("#tbscreen_button").attr("disabled", false);
                    $("#date6").val("");
                    $("#date6").attr("disabled", "disabled");
                    $("#eligibleIpt").val("");
                    $("#eligibleIpt").attr("disabled", false);
                } else if ($("#tbTreatment").val() == "Yes") {
                    $("#tbscreen_button").attr("disabled", "disabled");
                    $("#date6").attr("disabled", false);
                    $("#eligibleIpt").val("");
                    $("#eligibleIpt").attr("disabled", true);
                } else {
                    $("#tbscreen_button").attr("disabled", "disabled");
                    $("#date6").val("");
                    $("#date6").attr("disabled", "disabled");
                    $("#eligibleIpt").val("");
                    $("#eligibleIpt").attr("disabled", false);
                }
            });

            $("#hypertensive").bind("change", function () {
                if ($("#hypertensive").val() == "No") {
                    $("#bpAbove").attr("disabled", false);
                    $("#firstHypertensive").val("");
                    $("#firstHypertensive").attr("disabled", true);
                } else if ($("#hypertensive").val() == "Yes") {
                    $("#firstHypertensive").attr("disabled", false);
                    $("#bpAbove").val("");
                    $("#bpAbove").attr("disabled", "disabled");
                } else {
                    $("#bpAbove").val("");
                    $("#bpAbove").attr("disabled", "disabled");
                    $("#firstHypertensive").val("");
                    $("#firstHypertensive").attr("disabled", true);
                }
            });

            $("#diabetic").bind("change", function () {
                if ($("#diabetic").val() == "No") {
                    $("#dmscreen_button").attr("disabled", false);
                    $("#firstDiabetic").val("");
                    $("#firstDiabetic").attr("disabled", true);
                } else if ($("#diabetic").val() == "Yes") {
                    $("#dmscreen_button").attr("disabled", "disabled");
                    $("#firstDiabetic").attr("disabled", false);
                } else {
                    $("#dmscreen_button").attr("disabled", "disabled");
                    $("#firstDiabetic").val("");
                    $("#firstDiabetic").attr("disabled", true);
                }
            });

            $("#tbscreen_button").bind("click", function (event) {
                $("#tbscreentable").toggle("slow");
                return false;
            }); //show and hide grid

            $("#dmscreen_button").bind("click", function (event) {
                $("#dmscreentable").toggle("slow");
                return false;
            }); //show and hide grid

            $("#chroniccare_button").bind("click", function (event) {
                event.preventDefault();
                $("#section1").toggle("slow");
                $("#section2").toggle("fast");
            });

            $("#done_button").bind("click", function (event) {
                event.preventDefault();
                $("#section2").toggle("slow");
                $("#section1").toggle("fast");
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Clinic_search");
                return true;
            });
        });

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
                            <img src="images/clinic_header.jpg" width="773" height="28" class="top" alt=""
                                 style="margin-bottom: 5px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Details</td>
                    </tr>
                </table>
                <div id="messageBar"></div>

                <div id="section1">
                    <table width="99%" border="0" class="space" cellpadding="3">
                        <tr>
                            <td width="20%"><label>Hospital No:</label></td>
                            <td width="20%"><input name="hospitalNum" type="text" class="inputboxes" id="hospitalNum"
                                                   readonly="readonly"/></td>
                            <td width="25%"><span id="patientInfor"></span></td>
                            <td width="20%"><input name="patientId" type="hidden" id="patientId"/><input name="clinicId"
                                                                                                         type="hidden"
                                                                                                         id="clinicId"/>
                            </td>
                        </tr>
                        <tr>
                            <td><label>Date of Visit:</label></td>
                            <td><input name="date1" type="text" style="width: 100px;" class="inputboxes"
                                       id="date1"/><input name="dateVisit" type="hidden" id="dateVisit"/><span
                                    id="dateHelp" class="errorspan"></span></td>
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
                            <td><input name="height" type="text" style="width: 50px;" class="inputboxes"
                                       id="height"/><span id="bmi" style="color:red"></span></td>
                        </tr>
                        <tr>
                            <td><label>Blood Pressure(mmHg):</label></td>
                            <td><input name="bp1" type="text" style="width: 25px;" class="inputboxes" id="bp1"/> <input
                                    name="bp2" type="text" style="width: 25px;" class="inputboxes" id="bp2"/><input
                                    name="bp" type="hidden" style="width: 50px;" class="inputboxes" id="bp"/></td>
                            <td><input name="pregnant" type="checkbox" value="1" id="pregnant"
                                       disabled="true"/>&nbsp;<label>P r e g n a n t</label></td>
                            <td><label>L.M.P:</label>&nbsp;<input name="date2" type="text" style="width: 100px;"
                                                                  class="inputboxes" id="date2" disabled="true"/><input
                                    name="lmp" type="hidden" id="lmp"/></td>
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
                                <select name="adherenceLevel" style="width: 80px;" class="inputboxes"
                                        id="adherenceLevel">
                                    <option></option>
                                    <option>Good</option>
                                    <option>Fair</option>
                                    <option>Poor</option>
                                </select>
                                <button id="adhere_button" style="width: 20px;" disabled="true">...</button>
                            </td>
                            <td><label>Date of Next Appointment:</label></td>
                            <td><input name="date3" type="text" style="width: 100px;" class="inputboxes"
                                       id="date3"/><input name="nextAppointment" value="" type="hidden"
                                                          id="nextAppointment"/><span id="nextHelp"
                                                                                      style="color:red"></span></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <button id="chroniccare_button" style="width: 190px;">Fill Chronic Care Checklist
                                </button>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <div id="adheretable" style="display: none">
                                    <table id="adheregrid"></table>
                                </div>
                            </td>
                            <td><input name="oiIds" type="hidden" id="oiIds"/><input name="adrId" type="hidden"
                                                                                     id="adrId"/><input name="adhereIds"
                                                                                                        type="hidden"
                                                                                                        id="adhereIds"/>
                            </td>
                            <td><input name="description" type="hidden" id="description"/><input name="screener"
                                                                                                 type="hidden" value="1"
                                                                                                 id="screener"/></td>
                        </tr>
                        <tr>
                            <td><input name="gender" type="hidden" id="gender"/><input name="dateBirth" type="hidden"
                                                                                       id="dateBirth"/></td>
                            <td><input name="currentStatus" type="hidden" id="currentStatus"/><input
                                    name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/></td>
                            <td><input name="dateLastCd4" type="hidden" id="dateLastCd4"/><input name="dateLastClinic"
                                                                                                 type="hidden"
                                                                                                 id="dateLastClinic"/>
                            </td>
                            <td><input name="dateNextClinic" type="hidden" id="dateNextClinic"/><input name="commence"
                                                                                                       type="hidden"
                                                                                                       value="0"
                                                                                                       id="commence"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4">&nbsp;</td>
                        </tr>
                    </table>
                </div>

                <!--Chronic care -->
                <div id="section2" style="display: none">
                    <div id="container" style="height: 540px; overflow-y: scroll; margin-top: 10px;">
                        <table width="99%" border="0" class="space" cellpadding="3">
                            <tr>
                                <td width="25%"><label>ART Status:</label></td>
                                <td width="30%">
                                    <select name="currentStatus" style="width: 100px;" class="inputboxes"
                                            id="currentStatus">
                                        <option></option>
                                        <option>Pre-ART</option>
                                        <option>ART</option>
                                    </select>
                                </td>
                                <td width="25%"><input name="chroniccareId" type="hidden" id="chroniccareId"/></td>
                                <td width="20%">&nbsp;</td>
                            </tr>
                            <tr>
                                <td><label>Last CD4 Count:</label></td>
                                <td><input name="lastCd4" type="text" style="width: 50px;" class="inputboxes"
                                           id="lastCd4"/></td>
                                <td><label>Date:</label></td>
                                <td><input name="date4" type="text" style="width: 100px;" class="inputboxes"
                                           id="date4"/><input name="dateLastCd4" type="hidden" class="inputboxes"
                                                              id="dateLastCd4"/></td>
                            </tr>
                            <tr>
                                <td><label>Last Viral Load:</label></td>
                                <td><input name="lastViralLoad" type="text" style="width: 50px;" class="inputboxes"
                                           id="lastViralLoad"/></td>
                                <td><label>Date:</label></td>
                                <td><input name="date5" type="text" style="width: 100px;" class="inputboxes"
                                           id="date5"/><input name="dateLastViralLoad" type="hidden" class="inputboxes"
                                                              id="dateLastViralLoad"/></td>
                            </tr>
                            <tr>
                                <td><label>Eligible for CD4:</label></td>
                                <td>
                                    <select name="eligibleCd4" style="width: 80px;" class="inputboxes" id="eligibleCd4">
                                        <option></option>
                                        <option>Yes</option>
                                        <option>No</option>
                                    </select>
                                </td>
                                <td><label>Eligible for Viral Load:</label></td>
                                <td>
                                    <select name="eligibleViralLoad" style="width: 80px;" class="inputboxes"
                                            id="eligibleViralLoad">
                                        <option></option>
                                        <option>Yes</option>
                                        <option>No</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <div style="margin-right: 10px;">
                            <fieldset>
                                <legend> Co-trimoxazole Eligibility Assessment</legend>
                                <p></p>
                                <table width="99%" border="0" class="space" cellpadding="3">
                                    <!--<tr>
                                        <td><input name="cotrimEligibility1" type="checkbox" value="PLHIV with symptomatic HIV" id="cotrimEligibility1"/>&nbsp;<label>PLHIV with symptomatic HIV</label></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                    </tr>
        <tr>
                                        <td><input name="cotrimEligibility2" type="checkbox" value="Asymptomatic PLHIV with CD4 count <350 cells/mm3" id="cotrimEligibility2"/>&nbsp;<label>Asymptomatic PLHIV with CD4 count &lt;350 cells/mm<sup>3</sup></label></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                    </tr>
        <tr>
                                        <td><input name="cotrimEligibility3" type="checkbox" value="PLHIV with active TB" id="cotrimEligibility3"/>&nbsp;<label>PLHIV with active TB</label></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                    </tr>
        <tr>
                                        <td><input name="cotrimEligibility4" type="checkbox" value="Pregnant PLHIV after the first trimester" id="cotrimEligibility4"/>&nbsp;<label>Pregnant PLHIV after the first trimester</label></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                    </tr>
        <tr>
                                        <td><input name="cotrimEligibility5" type="checkbox" value="PLHIV <= 5years" id="cotrimEligibility5"/>&nbsp;<label>PLHIV &lt;= 5 years</label></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                    </tr>-->
                                    <tr>
                                        <td width="50%"><input name="cotrimEligibility1" type="checkbox"
                                                               value="PLHIV with symptomatic HIV"
                                                               id="cotrimEligibility1"/>&nbsp;<label>PLHIV with
                                            symptomatic HIV</label></td>
                                        <td width="50%"><input name="cotrimEligibility2" type="checkbox"
                                                               value="Asymptomatic PLHIV with CD4 count <350 cells/mm3"
                                                               id="cotrimEligibility2"/>&nbsp;<label>Asymptomatic PLHIV
                                            with CD4 count &lt;350 cells/mm<sup>3</sup></label></td>
                                    </tr>
                                    <tr>
                                        <td><input name="cotrimEligibility3" type="checkbox"
                                                   value="PLHIV with active TB" id="cotrimEligibility3"/>&nbsp;<label>PLHIV
                                            with active TB</label></td>
                                        <td><input name="cotrimEligibility4" type="checkbox"
                                                   value="Pregnant PLHIV after the first trimester"
                                                   id="cotrimEligibility4"/>&nbsp;<label>Pregnant PLHIV after the first
                                            trimester</label></td>
                                    </tr>
                                    <tr>
                                        <td><input name="cotrimEligibility5" type="checkbox" value="PLHIV <= 5years"
                                                   id="cotrimEligibility5"/>&nbsp;<label>PLHIV &le; 5 years</label></td>
                                        <td></td>
                                    </tr>
                                </table>
                            </fieldset>
                        </div>
                        <p></p>
                        <div style="margin-right: 10px;">
                            <fieldset>
                                <legend> TB Screening</legend>
                                <p></p>
                                <table width="99%" border="0" class="space" cellpadding="3">
                                    <tr>
                                        <td width="55%" colspan="2"><label>Are you currently on Isoniazid Preventive
                                            Therapy(IPT)?</label></td>
                                        <td width="25%">
                                            <select name="ipt" style="width: 80px;" class="inputboxes" id="ipt">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                        <td width="20%"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><label>Are you currently on TB treatment?</label></td>
                                        <td colspan="2">
                                            <select name="tbTreatment" style="width: 80px;" class="inputboxes"
                                                    id="tbTreatment">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            <button id="tbscreen_button" style="width: 20px;" disabled="true">...
                                            </button>
                                            <span style="margin-left:11px"><label>Date started:</label>&nbsp;&nbsp;<input
                                                    name="date6" type="text" style="width: 100px;" class="inputboxes"
                                                    id="date6" disabled="disabled"/><input name="dateStartedTbTreatment"
                                                                                           type="hidden"
                                                                                           class="inputboxes"
                                                                                           id="dateStartedTbTreatment"/></span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"></td>
                                        <td colspan="2">
                                            <div id="tbscreentable" style="display: none">
                                                <table id="tbscreengrid"></table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><label>Referred for TB diagnosis:</label></td>
                                        <td colspan="2">
                                            <select name="tbReferred" style="width: 80px;" class="inputboxes"
                                                    id="tbReferred">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            <span style="margin-left:23px"><label>Eligible for IPT:</label>&nbsp;&nbsp;<select
                                                    name="eligibleIpt" style="width: 80px;" class="inputboxes"
                                                    id="eligibleIpt">
                                                            <option></option>
                                                            <option>Yes</option>
                                                            <option>No</option>
                                                        </select>
                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                        </div>
                        <div style="margin-right: 10px;">
                            <fieldset>
                                <legend> Nutritional Status Assessment</legend>
                                <p></p>
                                <table width="99%" border="0" class="space" cellpadding="3">
                                    <tr>
                                        <td width="40%"><label>BMI (Adult):</label></td>
                                        <td width="20%">
                                            <select name="bmi" style="width: 130px;" class="inputboxes" id="bmi">
                                                <option></option>
                                                <option>&lt;18.5 (Underweight)</option>
                                                <option>18.5-24.9 (Healthy)</option>
                                                <option>25.0-29.9 (Overweight)</option>
                                                <option>&gt;30 (Obesity)</option>
                                                <option>&gt;40 (Morbid Obesity)</option>
                                            </select>
                                        </td>
                                        <td width="20%"><label>MUAC (under 5yrs):</label></td>
                                        <td width="20%">
                                            <select name="muac" style="width: 170px;" class="inputboxes" id="muac">
                                                <option></option>
                                                <option>&lt;11.5cm (Severe Acute Malnutrition)</option>
                                                <option>11.5-12.5cm (Moderate Acute Malnutrition)</option>
                                                <option>&gt;12.5cm (Well nourished)</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label>Provided with therapeutic/supplementary food:</label></td>
                                        <td>
                                            <select name="supplementaryFood" style="width: 80px;" class="inputboxes"
                                                    id="supplementaryFood">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                        <td><label>Referred:</label></td>
                                        <td>
                                            <select name="nutritionalStatusReferred" style="width: 80px;"
                                                    class="inputboxes" id="nutritionalStatusReferred">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                        </div>
                        <div style="margin-right: 10px;">
                            <fieldset>
                                <legend> Gender Based Violence Screening</legend>
                                <p></p>
                                <table width="99%" border="0" class="space" cellpadding="3">
                                    <tr>
                                        <td width="48%" colspan="2"><label>Have you been beaten, sexually coerced, raped
                                            or threatened or any of these by your partner or anyone else?</label></td>
                                        <td width="45%" colspan="2">
                                            <select name="gbv1" style="width: 80px;" class="inputboxes" id="gbv1">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            <span style="margin-left:8px">
                                                        <label>Referred for post GBV care:</label>
                                                        <select name="gbv1Referred" style="width: 80px;"
                                                                class="inputboxes" id="gbv1Referred">
                                                            <option></option>
                                                            <option>Yes</option>
                                                            <option>No</option>
                                                        </select>
                                                    </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><label>Does your partner/family deny you food, shelter, freedom
                                            of movement, livelihood or finance to access health care?</label></td>
                                        <td colspan="2">
                                            <select name="gbv2" style="width: 80px;" class="inputboxes" id="gbv2">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            <span style="margin-left:8px">
                                                        <label>Referred for post GBV care:</label>
                                                        <select name="gbv2Referred" style="width: 80px;"
                                                                class="inputboxes" id="gbv2Referred">
                                                            <option></option>
                                                            <option>Yes</option>
                                                            <option>No</option>
                                                        </select>
                                                    </span>
                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                        </div>
                        <div style="margin-right: 10px;">
                            <fieldset>
                                <legend> Screening for Chronic Conditions</legend>
                                <p></p>
                                <table width="99%" border="0" class="space" cellpadding="3">
                                    <tr>
                                        <td width="27%"><strong>Hypertension</strong></td>
                                        <td width="22%">&nbsp;</td>
                                        <td width="37%">&nbsp;</td>
                                        <td width="14%">&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><label>Known hypertensive?</label></td>
                                        <td>
                                            <select name="hypertensive" style="width: 80px;" class="inputboxes"
                                                    id="hypertensive">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                        <td><label>First time identified within the programme:</label></td>
                                        <td>
                                            <select name="firstHypertensive" style="width: 80px;" class="inputboxes"
                                                    id="firstHypertensive" disabled="disabled">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label>BP above 140/90mmHg:</label></td>
                                        <td>
                                            <select name="bpAbove" style="width: 80px;" class="inputboxes" id="bpAbove"
                                                    disabled="disabled">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                        <td><label>Referred for further care:</label></td>
                                        <td>
                                            <select name="bpReferred" style="width: 80px;" class="inputboxes"
                                                    id="bpReferred">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><strong>Diabetes (DM)</strong></td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><label>Known diabetic?</label></td>
                                        <td>
                                            <select name="diabetic" style="width: 80px;" class="inputboxes"
                                                    id="diabetic">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            <button id="dmscreen_button" style="width: 20px;" disabled="true">...
                                            </button>
                                        </td>
                                        <td><label>First time identified within the programme:</label></td>
                                        <td>
                                            <select name="firstDiabetic" style="width: 80px;" class="inputboxes"
                                                    id="firstDiabetic" disabled="disabled">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td colspan="3">
                                            <div id="dmscreentable" style="display: none">
                                                <table id="dmscreengrid"></table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label>Referred for further care:</label></td>
                                        <td>
                                            <select name="dmReferred" style="width: 80px;" class="inputboxes"
                                                    id="dmReferred">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </table>
                            </fieldset>
                        </div>
                        <div style="margin-right: 10px;">
                            <fieldset>
                                <legend> Positive Health Dignity and Prevention (PHDP)</legend>
                                <p></p>
                                <table width="99%" border="0" class="space" cellpadding="3">
                                    <tr>
                                        <td width="48%"><strong>A)Prevent HIV Transmission</strong></td>
                                        <td width="12%">&nbsp;</td>
                                        <td width="17%">&nbsp;</td>
                                        <td width="13%">&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><label>How many doses of ARVs have you missed since the last appointment?(If
                                            on ART)</label></td>
                                        <td>
                                            <select name="phdp1" style="width: 80px;" class="inputboxes" id="phdp1">
                                                <option></option>
                                                <option>&le;3</option>
                                                <option>4-8</option>
                                                <option>&ge;9</option>
                                            </select>
                                        </td>
                                        <td><label>Medication adherence counselling done?</label></td>
                                        <td>
                                            <select name="phdp1ServicesProvided" style="width: 80px;" class="inputboxes"
                                                    id="phdp1ServicesProvided">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>


                                    </tr>
                                    <tr>
                                        <td><label>Have you disclosed your status to your partner(s)?</label></td>
                                        <td>
                                            <select name="phdp2" style="width: 80px;" class="inputboxes" id="phdp2">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <!--<td><label>Services Provided:</label></td>
                                            <td><select name="phdp2ServicesProvided" style="width: 80px;" class="inputboxes" id="phdp2ServicesProvided">
        <option></option>
        <option>Yes</option>
        <option>No</option>
    </select>
                                            </td>-->
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><label>Do you know the status of your partner(s)?</label></td>
                                        <td>
                                            <select name="phdp3" style="width: 80px;" class="inputboxes" id="phdp3">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <!--<td><label>Services Provided:</label></td>
                                            <td><select name="phdp3ServicesProvided" style="width: 80px;" class="inputboxes" id="phdp3ServicesProvided">
        <option></option>
        <option>Yes</option>
        <option>No</option>
    </select>

                                   </td>-->
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><label>Do you use condoms during every sexual encounter?</label></td>
                                        <td>
                                            <select name="phdp4" style="width: 80px;" class="inputboxes" id="phdp4">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <td><label>Condom use counselling done?</label></td>
                                        <td><select name="phdp4ServicesProvided" style="width: 80px;" class="inputboxes"
                                                    id="phdp4ServicesProvided">
                                            <option></option>
                                            <option>Yes</option>
                                            <option>No</option>
                                        </select>
                                        </td>


                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><strong>B)Prevent Diseases/Opportunistic Infections</strong></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><label>Do you/partner have genital
                                            sores/rash/pain/discharge/bleeding?</label></td>
                                        <td>
                                            <select name="phdp5" style="width: 80px;" class="inputboxes" id="phdp5">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <!--<td><label>Services Provided:</label></td>
                                            <td><select name="phdp5ServicesProvided" style="width: 80px;" class="inputboxes" id="phdp5ServicesProvided">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            </td>-->
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><label>How many doses of Co-trimoxazole have you missed since the last
                                            appointment?</label></td>
                                        <td>
                                            <input name="phdp6" type="text" style="width: 50px;" class="inputboxes"
                                                   id="phdp6"/>
                                        </td>

                                        <!--<td><label>Services Provided:</label></td>
                                            <td><select name="phdp6ServicesProvided" style="width: 80px;" class="inputboxes" id="phdp6ServicesProvided">
        <option></option>
        <option>Yes</option>
        <option>No</option>
    </select>
                                            </td>-->
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><strong>C)Promote Healthy Living</strong></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><label>How regularly do you take alcohol in a week?</label></td>
                                        <td>
                                            <input name="phdp7" type="text" style="width: 50px;" class="inputboxes"
                                                   id="phdp7"/>
                                        </td>

                                        <td><label>Nutritional counseling done?</label></td>
                                        <td><select name="phdp7ServicesProvided" style="width: 80px;" class="inputboxes"
                                                    id="phdp7ServicesProvided">
                                            <option></option>
                                            <option>Yes</option>
                                            <option>No</option>
                                        </select>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td><label>WASH counseling done?</label></td>
                                        <td><select name="phdp8ServicesProvided" style="width: 80px;" class="inputboxes"
                                                    id="phdp8ServicesProvided">
                                            <option></option>
                                            <option>Yes</option>
                                            <option>No</option>
                                        </select>
                                        </td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <!--<tr>
                                            <td></td>
                                            <td></td>
                                            <td></td>
            <td></td>
        </tr>
                                    <tr>
                                            <td></td>
                                            <td></td>
                                            <td></td>
            <td></td>
        </tr>
                                    <tr>
                                            <td></td>
                                            <td></td>
                                            <td></td>
            <td></td>
        </tr>-->
                                    <tr>
                                        <td><label>Additional PHDP Services provided:</label></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td colspan="4">
                                            <table width="100%" border="0" class="space" cellpadding="0">
                                                <tr>
                                                    <td>
                                                        <input name="phdp91" type="checkbox"
                                                               value="Insecticide treated nets" id="phdp91"/><label>Insecticide
                                                        treated nets</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                        <input name="phdp92" type="checkbox"
                                                               value="Intermittent prophylactic treatment" id="phdp92"/><label>Intermittent
                                                        prophylactic treatment</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                        <input name="phdp93" type="checkbox"
                                                               value="Cervical Cancer Screening" id="phdp93"/><label>Cervical
                                                        Cancer Screening</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                        <input name="phdp94" type="checkbox" value="Active member of SG"
                                                               id="phdp94"/><label>Active member of SG</label>&nbsp;&nbsp;
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input name="phdp95" type="checkbox" value="Family Planning"
                                                               id="phdp95"/><label>Family Planning</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                        <input name="phdp96" type="checkbox" value="Basic care kits"
                                                               id="phdp96"/><label>Basic care kits</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                        <input name="phdp97" type="checkbox"
                                                               value="Disclosure counseling" id="phdp97"/><label>Disclosure
                                                        counseling</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                        <input name="phdp98" type="checkbox" value="Social Services"
                                                               id="phdp98"/><label>Social Services</label>&nbsp;&nbsp;
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input name="phdp99" type="checkbox" value="Linkage to IGAs"
                                                               id="phdp99"/><label>Linkage to IGAs</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                        <input name="phdp910" type="checkbox" value="Leg" id="phdp910"/><label>Leg</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                        <input name="phdp911" type="checkbox" value="Others"
                                                               id="phdp911"/><label>Others</label>&nbsp;&nbsp;
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </fieldset>
                        </div>
                        <div style="margin-right: 10px;">
                            <fieldset>
                                <legend> Reproductive Intentions</legend>
                                <p></p>
                                <table width="99%" border="0" class="space" cellpadding="3">
                                    <tr>
                                        <td width="51%"><label>Have you been screened for cervical cancer in the last
                                            one year?</label></td>
                                        <td width="6%">
                                            <select name="reproductiveIntentions1" style="width: 80px;"
                                                    class="inputboxes" id="reproductiveIntentions1">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <td width="19%"><label>Referred for further care:</label></td>
                                        <td width="14%"><select name="reproductiveIntentions1Referred"
                                                                style="width: 80px;" class="inputboxes"
                                                                id="reproductiveIntentions1Referred">
                                            <option></option>
                                            <option>Yes</option>
                                            <option>No</option>
                                        </select>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td><label>Do you want a pregnancy in the next one year? </label></td>
                                        <td>
                                            <select name="reproductiveIntentions2" style="width: 80px;"
                                                    class="inputboxes" id="reproductiveIntentions2">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <td><label>Referred for further care:</label></td>
                                        <td><select name="reproductiveIntentions2Referred" style="width: 80px;"
                                                    class="inputboxes" id="reproductiveIntentions2Referred">
                                            <option></option>
                                            <option>Yes</option>
                                            <option>No</option>
                                        </select>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td><label>If no, are you currently using a contraceptive? </label></td>
                                        <td>
                                            <select name="reproductiveIntentions3" style="width: 80px;"
                                                    class="inputboxes" id="reproductiveIntentions3">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <td><label>Referred for further care:</label></td>
                                        <td><select name="reproductiveIntentions3Referred" style="width: 80px;"
                                                    class="inputboxes" id="reproductiveIntentions3Referred">
                                            <option></option>
                                            <option>Yes</option>
                                            <option>No</option>
                                        </select>
                                        </td>

                                    </tr>
                                </table>
                            </fieldset>
                        </div>
                        <div style="margin-right: 10px;">
                            <fieldset>
                                <legend> Malaria Prevention</legend>
                                <p></p>
                                <table width="99%" border="0" class="space" cellpadding="3">
                                    <tr>
                                        <td width="51%"><label>Do you use insecticide treated bed net?</label></td>
                                        <td width="6%">
                                            <select name="malariaPrevention1" style="width: 80px;" class="inputboxes"
                                                    id="malariaPrevention1">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <td width="19%"><label>Referred:</label></td>
                                        <td width="14%"><select name="malariaPrevention1Referred" style="width: 80px;"
                                                                class="inputboxes" id="malariaPrevention1Referred">
                                            <option></option>
                                            <option>Yes</option>
                                            <option>No</option>
                                        </select>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td><label>If pregnant: have you been on intermittent preventive
                                            therapy? </label></td>
                                        <td>
                                            <select name="malariaPrevention2" style="width: 80px;" class="inputboxes"
                                                    id="malariaPrevention2">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </td>

                                        <td><label>Referred:</label></td>
                                        <td><select name="malariaPrevention2Referred" style="width: 80px;"
                                                    class="inputboxes" id="malariaPrevention2Referred">
                                            <option></option>
                                            <option>Yes</option>
                                            <option>No</option>
                                        </select>
                                        </td>

                                    </tr>
                                </table>
                            </fieldset>
                        </div>

                        <p></p>
                        <button id="done_button" style="width: 190px;">Done</button>
                        <input name="descriptionTb" type="hidden" id="descriptionTb"/>
                        <input name="descriptionDm" type="hidden" id="descriptionDm"/>
                        <input name="tbValues" type="hidden" id="tbValues"/>
                        <input name="dmValues" type="hidden" id="dmValues"/>

                    </div>
                </div>
                <hr></hr>

                <div id="userGroup" style="display: none"><s:property value="#session.userGroup"/></div>
                <div id="buttons" style="width: 300px">
                    <button id="save_button">Save</button> &nbsp;<button id="delete_button" disabled="true"/>
                    Delete</button> &nbsp;<button id="close_button"/>
                    Close</button>
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
