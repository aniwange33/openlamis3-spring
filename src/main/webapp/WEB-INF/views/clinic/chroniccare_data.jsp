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
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="/js/lamis/chroniccare-common.js"></script>
    <script type="text/JavaScript">

        var tbscreenIds = "", dmscreenIds = "";
        var date = "";
        var lastSelectDate = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            if (!!$('#patientId').val()) {
                fetch('/api/chronic-care/' + $('#chroniccareId').val(), {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    })
                }).then(function (response) {
                    return response.json()
                }).then(function (json) {
                    $('#patientId').val(json.chroniccareList[0].patientId);
                })
            }

            fetch('/api/patient/' + $('#patientId').val(), {
                headers: new Headers({
                    'Content-Type': 'application/json'
                })
            }).then(function (response) {
                return response.json()
            }).then(function (json) {
                $('#hospitalNum').val(json.patientList[0].hospitalNum)
            });

            $.ajax({
                url: "/api/chronic-care/" + $('#chroniccareId').val() || 0,
                dataType: "json",
                success: function (chroniccareList) {
                    populateForm(chroniccareList);
                }
            }); //end of ajax call

            var lastSelected = -99;
            $("#tbscreengrid").jqGrid({
                url: "/api/tbscreen/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["Description", "Value"],
                colModel: [
                    {name: "description", index: "description", width: "270"},
                    {
                        name: "tbValue",
                        index: "tbValue",
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
                    $("#tbscreenId").val(id);
                    var data = $("#tbscreengrid").getRowData(id)
                    $("#descriptionTb").val(data.description);
                }, //end of onSelectRow
                loadComplete: function (data) {
                    for (i = 0; i < tbscreenIds.length; i++) {
                        var values = tbscreenIds[i].split(",");
                        $("#tbscreengrid").jqGrid('setCell', values[0], 'tbValue', values[1]);
                    }
                }
            }); //end of jqGrid

            $("#dmscreengrid").jqGrid({
                url: "/api/dmscreen/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["Description", "Value"],
                colModel: [
                    {name: "description", index: "description", width: "220"},
                    {
                        name: "dmValue",
                        index: "dmValue",
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
                    $("#dmscreenId").val(id);
                    var data = $("#dmscreengrid").getRowData(id)
                    $("#descriptionDm").val(data.description);
                }, //end of onSelectRow
                loadComplete: function (data) {
                    for (i = 0; i < dmscreenIds.length; i++) {
                        var values = dmscreenIds[i].split(",");
                        $("#dmscreengrid").jqGrid('setCell', values[0], 'dmValue', values[1]);
                    }
                }
            }); //end of jqGrid				

            $("#tbTreatment").change(function () {
                if ($("#tbTreatment").val() == "No") {
                    //$("#tbscreen_button").attr("disabled",false);
                    $("#tbscreentable").show("slow");
                    $("#date4").val("");
                    $("#date4").attr("disabled", "disabled");
                    $("#tbReferred").val("");
                    $("#tbReferred").attr("disabled", false);
                    $("#ipt").val("");
                    $("#ipt").attr("disabled", false);
                    $("#inh").val("");
                    $("#inh").attr("disabled", false);
                    $("#eligibleIpt").val("");
                    $("#eligibleIpt").attr("disabled", false);
                } else if ($("#tbTreatment").val() == "Yes") {
                    //$("#tbscreen_button").attr("disabled", "disabled");
                    $("#tbscreentable").hide("slow");
                    $("#date4").attr("disabled", false);
                    $("#tbReferred").val("");
                    $("#tbReferred").attr("disabled", true);
                    $("#ipt").val("");
                    $("#ipt").attr("disabled", true);
                    $("#inh").val("");
                    $("#inh").attr("disabled", true);
                    $("#eligibleIpt").val("");
                    $("#eligibleIpt").attr("disabled", true);
                } else {
                    //$("#tbscreen_button").attr("disabled", true);
                    $("#tbscreentable").hide("slow");
                    $("#date4").attr("disabled", true);
                    $("#tbReferred").val("");
                    $("#tbReferred").attr("disabled", true);
                    $("#ipt").val("");
                    $("#ipt").attr("disabled", true);
                    $("#inh").val("");
                    $("#inh").attr("disabled", true);
                    $("#eligibleIpt").val("");
                    $("#eligibleIpt").attr("disabled", true);
                }
            });

            $("#hypertensive").bind("change", function () {
                if ($("#hypertensive").val() == "No") {
                    $("#bpAbove").attr("disabled", false);
                    $("#firstHypertensive").val("");
                    $("#firstHypertensive").attr("disabled", true);
                } else if ($("#hypertensive").val() == "Yes") {
                    $("#firstHypertensive").attr("disabled", false);
                } else {
                    $("#firstHypertensive").val("");
                    $("#firstHypertensive").attr("disabled", true);
                }
            });

            $("#diabetic").change(function () {
                if ($("#diabetic").val() == "No") {
                    $("#dmscreentable").show("slow");
                    //$("#dmscreen_button").attr("disabled",false);
                    $("#firstDiabetic").val("");
                    $("#firstDiabetic").attr("disabled", true);
                } else if ($("#diabetic").val() == "Yes") {
                    //$("#dmscreen_button").attr("disabled", "disabled");
                    $("#dmscreentable").hide("slow");
                    $("#firstDiabetic").attr("disabled", false);
                } else {
                    //$("#dmscreen_button").attr("disabled", "disabled");
                    $("#dmscreentable").hide("slow");
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


            $("#date1").bind("change", function (event) {
                var message = "";
                if ($("#date1").val() != 0) {
                    if ($("#date2").val() != 0) {
                        message = "Date of visit cannot be ealier than date of last Cd4";
                        checkDate($("#date1").val(), $("#date2").val(), message)
                    } else {
                        if ($("#date3").val() != 0) {
                            message = "Date of visit cannot be ealier than date of last viral load";
                            checkDate($("#date1").val(), $("#date2").val(), message)
                        } else {
                            if ($("#date4").val() != 0) {
                                message = "Date of visit cannot be ealier than date of TB start";
                                checkDate($("#date1").val(), $("#date2").val(), message)
                            }
                        }
                    }
                }
            });

            $("#date2").bind("change", function (event) {
                var message = "";
                if ($("#date1").val() != 0) {
                    if ($("#date2").val() != 0) {
                        message = "Date of visit cannot be ealier than date of last Cd4";
                        checkDate($("#date1").val(), $("#date2").val(), message)
                    }
                }
            });

            $("#date3").bind("change", function (event) {
                var message = "";
                if ($("#date1").val() != 0) {
                    if ($("#date3").val() != 0) {
                        message = "Date of visit cannot be ealier than date of last viral load";
                        checkDate($("#date1").val(), $("#date3").val(), message)
                    }
                }
            });

            $("#date4").bind("change", function (event) {
                var message = "";
                if ($("#date1").val() != 0) {
                    if ($("#date4").val() != 0) {
                        message = "Date of visit cannot be ealier than date of TB start";
                        checkDate($("#date1").val(), $("#date4").val(), message)
                    }
                }
            });


            $("#ipt").bind("change", function (event) {
                iptEligible();
            });
            $("#inh").bind("change", function (event) {
                iptEligible();
            });

            $("#bodyWeight").bind("change", function (event) {
                calBmi();
            });
            $("#height").bind("change", function (event) {
                calBmi();
            });

            $("#muac").bind("change", function (event) {
                calMuac();
            });

            $("#close_button").click(function (event) {
                window.location.href = "/event-page/patient/" + $('#patientId').val();
            });
        });


    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="/Clinic_page">Clinic</a></li>
        <li class="breadcrumb-item active" aria-current="page">Care and Support Assessment</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-4 ml-auto mr-auto">
                            <ul class="nav nav-pills nav-pills-primary flex-column" role="tablist">
                                <li class="nav-item">
                                    <a class="nav-link active show text-left" data-toggle="tab" href="#link1"
                                       role="tablist">
                                        Details
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link2" role="tablist">
                                        Co-trimoxazole Eligibility Assessment
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link3" role="tablist">
                                        TB Screening
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link4" role="tablist">
                                        Nutritional Status Assessment
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link5" role="tablist">
                                        Gender based Violence Screening
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link6" role="tablist">
                                        Screening for Chronic Conditions
                                    </a>
                                </li>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link7" role="tablist">
                                        Positive Health Dignity and Prevention (PHDP)
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link8" role="tablist">
                                        Reproductive Intentions/Malaria Prevention
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <div class="col-md-8">
                            <div class="tab-content tab-space">
                                <div class="tab-pane active show" id="link1">
                                    <h5><strong>Details</strong></h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Hospital No:</label>
                                                <input name="hospitalNum" type="text" class="form-control"
                                                       id="hospitalNum" readonly="readonly"/></div>
                                            <input name="patientId" value="${patientId}"
                                                   type="hidden" id="patientId"/>
                                            <input name="chroniccareId" value="${chroniccareId}" type="hidden" id="chroniccareId"/>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <br/>
                                                <span id="patientInfor"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Visit:</label>
                                                <input name="date1" type="text" class="form-control"
                                                       id="date1"/>
                                                <input name="dateVisit" type="hidden" class="form-control"
                                                       id="dateVisit"/><span id="datehelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row ">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Type of Client:</label>
                                                <select name="clinicType" style="width: 100%;"
                                                        class="form-control select2"
                                                        id="clinicType">
                                                    <option value=""></option>
                                                    <option>PLHIV newly enrolled into HIV Care & treatment</option>
                                                    <option>Registered PLHIV on first time visit this FY</option>
                                                    <option>Registered PLHIV on follow up/subsequent visit this FY
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">ART Status:</label>
                                                <select name="currentStatus" style="width: 100%;"
                                                        class="form-control select2"
                                                        id="currentStatus">
                                                    <option></option>
                                                    <option>Pre-ART</option>
                                                    <option>ART</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Pregnancy Status:</label>
                                                <select name="pregnancyStatus" style="width: 100%;"
                                                        class="form-control select2" id="pregnancyStatus" required/>
                                                <option></option>
                                                <option>Pregnant</option>
                                                <option>Non-Pregnant</option>
                                                <option>Post Partum</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Clinical Stage:</label>
                                                <select name="clinicStage" style="width: 100%;"
                                                        class="form-control select2" id="clinicStage"/>
                                                <option></option>
                                                <option>Stage I</option>
                                                <option>Stage II</option>
                                                <option>Stage III</option>
                                                <option>Stage IV</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Last CD4 Count:</label>
                                                <input name="lastCd4" type="text" class="form-control" id="lastCd4"
                                                       required/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Last CD4</label>
                                                <input name="date2" type="text" class="form-control" id="date2"/>
                                                <input name="dateLastCd4" type="hidden" class="form-control"
                                                       id="dateLastCd4"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row ">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Last Viral Load:</label>
                                                <input name="lastViralLoad" class="form-control" id="lastViralLoad">
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Last Viral Load:</label>
                                                <input name="date3" type="text" class="form-control" id="date3"/>
                                                <input name="dateLastViralLoad" type="hidden" class="form-control"
                                                       id="dateLastViralLoad"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Eligible for CD4:</label>
                                                <select name="eligibleCd4" style="width: 100%;"
                                                        class="form-control select2" id="eligibleCd4">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Eligible for Viral Load:</label>
                                                <select name="eligibleViralLoad" style="width: 100%;"
                                                        class="form-control select2" id="eligibleViralLoad">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- </div> -->
                                <div class="tab-pane" id="link2">
                                    <h6>
                                        <stong>Co-trimoxazole Eligibility Assessment</stong>
                                    </h6>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="form-check mt-3">
                                                <label class="form-check-label">
                                                    <input name="cotrimEligibility1" type="checkbox" value="1"
                                                           class="form-check-input" id="cotrimEligibility1"/>
                                                    <span class="form-check-sign"></span>PLHIV with symptomatic HIV
                                                </label>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-check mt-3">
                                                <label class="form-check-label">
                                                    <input name="cotrimEligibility2" type="checkbox" value="1"
                                                           class="form-check-input" id="cotrimEligibility2"/>
                                                    <span class="form-check-sign"></span>Asymptomatic PLHIV with CD4
                                                    count &lt;500 cells/mm<sup>3</sup>
                                                </label>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-check mt-3">
                                                <label class="form-check-label">
                                                    <input name="cotrimEligibility3" type="checkbox" value="1"
                                                           class="form-check-input" id="cotrimEligibility3"/><span
                                                        class="form-check-sign"></span>PLHIV with active TB
                                                </label>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-check mt-3">
                                                <label class="form-check-label">
                                                    <input name="cotrimEligibility4" type="checkbox" value="1"
                                                           class="form-check-input" id="cotrimEligibility4"/>
                                                    <span class="form-check-sign"></span>Pregnant PLHIV after the first
                                                    trimester
                                                </label>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-check mt-3">
                                                <label class="form-check-label">
                                                    <input name="cotrimEligibility5" type="checkbox" value="1"
                                                           class="form-check-input" id="cotrimEligibility5"/>
                                                    <span class="form-check-sign"></span>PLHIV &le; 5 years </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="link3">
                                    <h5><strong>TB Screening</strong></h5>
                                    <div class="row ">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Are you currently on TB Treatment?</label>
                                                <select name="tbTreatment" style="width: 100%;"
                                                        class="form-control select2"
                                                        id="tbTreatment">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                                <div class="row">
                                                    <div class="col-md-12 table-responsive">
                                                        <div id="tbscreentable" style="display: none;">
                                                            <table id="tbscreengrid"></table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date Started:</label>
                                                <input name="date4" type="text"
                                                       class="form-control" id="date4" disabled="disabled"/>
                                                <input name="dateStartedTbTreatment"
                                                       type="hidden" class="form-control"
                                                       id="dateStartedTbTreatment"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row ">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Referred for TB Diagnosis:</label>
                                                <select name="tbReferred" class="form-control select2"
                                                        style="width: 100%;" id="tbReferred" disabled="disabled">
                                                    <option value="">None</option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="divider">
                                        &nbsp; &nbsp; &nbsp;
                                        <h7><strong>Isoniazid Preventive Therapy (IPT)</strong></h7>
                                    </div>
                                    <div class="row ">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Are you currently on Isoniazid Preventive
                                                    Therapy (IPT)?</label>
                                                <select name="ipt" style="width: 100%;" class="form-control select2"
                                                        id="ipt" disabled="disabled">
                                                    <option value="">None</option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Have you received INH within the past 2
                                                    year?</label><select name="inh" class="form-control select2"
                                                                         style="width: 100%;" id="inh"
                                                                         disabled="disabled">
                                                <option>None</option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row ">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Eligible for IPT:</label>
                                                <select name="eligibleIpt" style="width: 100%;"
                                                        class="form-control select2"
                                                        id="eligibleIpt">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="link4">
                                    <h5><strong>Nutritional Status Assessment</strong></h5>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label">Body Weight(kg):</label><input
                                                    name="bodyWeight" type="text" class="form-control" id="bodyWeight"/>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label">Height(m):</label>
                                                <input name="height" type="text"
                                                       class="form-control" id="height" required/>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label">BMI (Adult):</label><select name="bmi"
                                                                                                      style="width:100%;"
                                                                                                      class="form-control select2"
                                                                                                      id="bmi">
                                                <option></option>
                                                <option>&lt;18.5 (Underweight)</option>
                                                <option>18.5-24.9 (Healthy)</option>
                                                <option>25.0-29.9 (Overweight)</option>
                                                <option>&gt;30 (Obesity)</option>
                                                <option>&gt;40 (Morbid Obesity)</option>
                                            </select>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label"> MUAC (under 5yrs):</label>
                                                <input name="muac" type="text"
                                                       class="form-control" id="muac">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label"></label>
                                                <select name="muacPediatrics" style="width: 100%;"
                                                        class="form-control select2"
                                                        id="muacpediatrics">
                                                    <option></option>
                                                    <option>&lt;11.5cm (Severe Acute Malnutrition)</option>
                                                    <option>11.5-12.5cm (Moderate Acute Malnutrition)</option>
                                                    <option>&gt;12.5cm (Well nourished)</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label">MUAC Pregnant:</label>
                                                <select name="muacPregnant" class="form-control select2"
                                                        style="width: 100%;"
                                                        id="muacPregnant">
                                                    <option></option>
                                                    <option>&lt;23cm (Underweight)</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label">Provided with Therapeutic/Supplementary
                                                    Food:</label><select name="supplementaryFood" style="width: 100%;"
                                                                         class="form-control select2"
                                                                         id="supplementaryFood">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label">Referred:<br></br></label>
                                                <select name="nutritionalStatusReferred" style="width: 100%;"
                                                        class="form-control select2" id="nutritionalStatusReferred">
                                                    <option>None</option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- </div> -->
                                <div class="tab-pane" id="link5">
                                    <h5><strong>Gender Based Violence Screening</strong>
                                        <h5>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="form-label">Have you been beaten, sexually
                                                            coerced, raped or threatened or any of these by your partner
                                                            or anyone else?</label>
                                                        <select name="gbv1" class="form-control select2"
                                                                style="width: 100%;" id="gbv1">
                                                            <option></option>
                                                            <option>Yes</option>
                                                            <option>No</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="form-label">Referred for Post GBV
                                                            Care:<br></br></label>
                                                        <select name="gbv1Referred"
                                                                class="form-control select2" style="width: 100%;"
                                                                id="gbv1Referred">
                                                            <option></option>
                                                            <option>Yes</option>
                                                            <option>No</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="form-label">Does your partner/family deny you
                                                            food, shelter, freedom of movement, livelihood or finance to
                                                            access health care?</label>
                                                        <select name="gbv2"
                                                                class="form-control select2" style="width: 100%;"
                                                                id="gbv2">
                                                            <option></option>
                                                            <option>Yes</option>
                                                            <option>No</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="form-label">Referred for Post GBV
                                                            Care:<br></br></label><select name="gbv2Referred"
                                                                                          class="form-control select2"
                                                                                          style="width: 100%;"
                                                                                          id="gbv2Referred">
                                                        <option></option>
                                                        <option>Yes</option>
                                                        <option>No</option>
                                                    </select>
                                                    </div>
                                                </div>
                                            </div>
                                </div>
                                <!-- </div>  -->
                                <div class="tab-pane" id="link6">
                                    <h5><strong>Screening for Chronic Conditions</strong></h5>
                                    <h5>Hypertension</h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Known Hypertensive?</label>
                                                <select name="hypertensive" class="form-control select2"
                                                        style="width: 100%;" id="hypertensive">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">First time identified within the
                                                    programme:</label>
                                                <select name="firstHypertensive" class="form-control select2"
                                                        id="firstHypertensive" style="width: 100%;" disabled="disabled">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">BP above 140/90mmHg:</label>
                                                <select name="bpAbove" class="form-control select2" style="width: 100%;"
                                                        id="bpAbove">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Referred for further care:</label>
                                                <select name="bpReferred" class="form-control select2"
                                                        style="width: 100%;" id="bpReferred">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="divider">
                                        &nbsp; &nbsp;
                                        <h5>Diabetes (DM)</h5>
                                    </div>
                                    <div class="new row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Known Diabetic?</label>
                                                <select name="firstDiabetic" class="form-control select2"
                                                        style="width: 100%;" id="firstDiabetic">
                                                    <option value=""></option>
                                                    <option value="Yes">Yes</option>
                                                    <option value="No">No</option>
                                                </select>
                                                <!-- <button id="dmscreen_button" disabled="true">..</button> -->
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">First Time Identified Within the
                                                    Programme?</label> <select name="firstDiabetic"
                                                                               class="form-control select2"
                                                                               style="width: 100%;" id="firsDdiabetic">
                                                <option value=""></option>
                                                <option value="Yes">Yes</option>
                                                <option value="No">No</option>
                                            </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6 table-responsive">
                                            <div id="dmscreentable" style="display: none;">
                                                <table id="dmscreengrid"></table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Referred for further care:</label>
                                                <select name="dmReferred" class="form-control select2"
                                                        style="width: 100%;" id="dmReferred">
                                                    <option>None</option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- </div> -->
                                <div class="tab-pane" id="link7">
                                    <h5><strong>Positive Health Dignity and Prevention</strong>(PHDP)</h5>
                                    <h5>A)Prevent HIV Transmission</h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">How many doses of ARVs have you missed since
                                                    the last appointment?(If on ART)</label>
                                                <select name="phdp1"
                                                        class="form-control select2" style="width: 100%;"
                                                        id="phdp1">
                                                    <option></option>
                                                    <option>&le;3</option>
                                                    <option>4-8</option>
                                                    <option>&ge;9</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Medication adherence counselling
                                                    done?<br></br></label>
                                                <select name="phdp1ServicesProvided"
                                                        class="form-control select2" style="width: 100%;"
                                                        id="phdp1ServicesProvided">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Have you disclosed your status to your
                                                    partner(s)?:</label>
                                                <select name="phdp2" style="width: 100%;"
                                                        class="form-control select2" id="phdp2">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Have you disclosed your status to your
                                                    partner(s)?</label>
                                                <select name="phdp2"
                                                        class="form-control select2" style="width: 100%;"
                                                        id="phdp2">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Do you know the status of your
                                                    partner(s)?</label>
                                                <select name="phdp3"
                                                        class="form-control select2"
                                                        id="phdp3" style="width: 100%;">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Do you use condoms during every sexual
                                                    encounter?</label>
                                                <select name="phdp4" class="form-control select2" style="width: 100%;"
                                                        id="phdp4"">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Condom use counselling done?</label><select
                                                    name="phdp4ServicesProvided" class="form-control select2"
                                                    style="width: 100%;" id="phdp4ServicesProvided"></select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="divider">
                                        &nbsp; &nbsp;
                                        <h5>B)Prevent Diseases/Opportunistic Infections<h5>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Do you/partner have genital
                                                    sores/rash/pain/discharge/bleeding?</label>
                                                <select name="phdp5" class="form-control select2" style="width: 100%;"
                                                        id="phdp5">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">How many doses of Co-trimoxazole have you
                                                    missed since the last appointment?</label><input
                                                    name="phdp6" type="text"
                                                    class="form-control"
                                                    id="phdp6">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="divider">
                                        <h7>C)Promote Healthy Living
                                            <h7>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">How regularly do you take alcohol in a
                                                    week?</label>
                                                <select name="phdp7"
                                                        class="form-control select2" style="width: 100%;" id="phdp7">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div
                                                class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Nutritional counseling done?</label>
                                                <select name="phdp7ServicesProvided" style="width: 100%;"
                                                        class="form-control select2" id="phdp7ServicesProvided">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">WASH counseling done?</label><input
                                                    name="phdp8ServicesProvided" type="text" class="form-control"
                                                    id="phdp8ServicesProvided"></div>
                                        </div>
                                    </div>
                                    <h7>Additional PHDP Services provided:
                                    </h7>
                                    <div class="row">
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label">
                                                <input name="phdp91" type="checkbox"
                                                       value="1" class="form-check-input" id="phdp91"/><span
                                                    class="form-check-sign"></span>Insecticide treated nets</label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label"><input
                                                    name="phdp92" type="checkbox"
                                                    value="1" class="form-check-input" id="phdp92"/>
                                                <span class="form-check-sign"></span>Intermittent prophylactic treatment
                                            </label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label"><input
                                                    name="phdp93" type="checkbox" value="1"
                                                    class="form-check-input" id="phdp93"/>
                                                <span class="form-check-sign"></span>Cervical Cancer Screening
                                            </label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label"><input
                                                    name="phdp94" type="checkbox"
                                                    value="1" class="form-check-input"
                                                    id="phdp94"/><span class="form-check-sign"></span>Active member of
                                                SG
                                            </label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label"><input
                                                    name="phdp95" type="checkbox"
                                                    value="1" class="form-check-input" id="phdp95"/><span
                                                    class="form-check-sign"></span>Family Planning
                                            </label>
                                        </div>
                                        <!--                                                                                </div>
                                                                                                                        <div class="row">-->
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label"><input
                                                    name="phdp96" type="checkbox"
                                                    value="1" class="form-check-input" id="phdp96"/>
                                                <span class="form-check-sign"></span>Basic care kits</label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label
                                                    class="form-check-label"><input name="phdp97"
                                                                                    type="checkbox" value="1"
                                                                                    class="form-check-input"
                                                                                    id="phdp97"/>
                                                <span class="form-check-sign"></span>Disclosure counseling
                                            </label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label
                                                    class="form-check-label"><input name="phdp98" type="checkbox"
                                                                                    value="1"
                                                                                    class="form-check-input"
                                                                                    id="phdp98"/>
                                                <span class="form-check-sign"></span>Social Services
                                            </label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label"><input
                                                    name="phdp99" type="checkbox"
                                                    value="1" class="form-check-input"
                                                    id="phdp98"/>
                                                <span class="form-check-sign"></span>Linkage to IGAs/label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label"><input
                                                    name="phdp10" type="checkbox"
                                                    value="1" class="form-check-input"
                                                    id="phdp910"/>
                                                <span class="form-check-sign"></span>Legal Services
                                            </label>
                                        </div>
                                        <div class="col-md-4 form-check mt-3">
                                            <label class="form-check-label"><input
                                                    name="phdp911" type="checkbox"
                                                    value="1" class="form-check-input"
                                                    id="phdp911"/>
                                                <span class="form-check-sign"></span>Others
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="link8">
                                    <h5><strong>Reproductive Intentions </strong></h5>
                                    <div class="row ">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Have you been screened for cervical cancer in
                                                    the last one year?</label>
                                                <select name="reproductiveIntentions1" class="form-control select2"
                                                        style="width: 100%;" id="reproductiveIntentions1">
                                                    <option value=""></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Referred for further care:</label><select
                                                    name="reproductiveIntentions1Referred" class="form-control select2"
                                                    style="width: 100%;" id="reproductiveIntentions1Referred">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Do you want a pregnancy in the next one
                                                    year? </label>
                                                <select name="reproductiveIntentions2"
                                                        class="form-control select2" style="width: 100%;"
                                                        id="reproductiveIntentions2">
                                                    <option value=""></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Referred for further care?</label>
                                                <select name="reproductiveIntension2Referred" style="width: 100%;"
                                                        class="form-control select2"
                                                        id="reproductiveIntension2Referred">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">If no, are you currently using a
                                                    contraceptive?</label><select name="reproductiveIntentions3"
                                                                                  class="form-control select2"
                                                                                  style="width: 100%;"
                                                                                  id="reproductiveIntension3">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Referred for further care?</label><select
                                                    name="reproductiveIntentions3Referred"
                                                    class="form-control select2" style="width: 100%;"
                                                    id="reproductiveIntentions3Referred">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="divider">
                                        <br>
                                        <h5><strong>Malaria Prevention </strong></h5>
                                        <div class="row ">
                                            <div class="col-md-6">
                                                <div class="form-group">
                                                    <label class="form-label">Do you use insecticide treated bed
                                                        net?</label>
                                                    <select name="malariaPrevention1" class="form-control select2"
                                                            style="width: 100%;" id="malariaPrevention1">
                                                        <option value=""></option>
                                                        <option>Yes</option>
                                                        <option>No</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div
                                                    class="col-md-6">
                                                <div class="form-group">
                                                    <label class="form-label">Referred:</label>
                                                    <select name="malariaPrevention1Referred"
                                                            class="form-control select2" style="width: 100%;"
                                                            id="malariaPrevention1Referred">
                                                        <option></option>
                                                        <option>Yes</option>
                                                        <option>No</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="form-group">
                                                    <label class="form-label">If pregnant: have you been on intermittent
                                                        preventive therapy?</label>
                                                    <select name="malariaPrevention2"
                                                            class="form-control select2" style="width: 100%;"
                                                            id="malariaPrevention2">
                                                        <option></option>
                                                        <option>Yes</option>
                                                        <option>No</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div
                                                    class="col-md-6">
                                                <div class="form-group">
                                                    <br/>
                                                    <label class="form-label">Referred:</label>
                                                    <select name="malariaPreventionReferred2Referred"
                                                            class="form-control select2" style="width: 100%;"
                                                            id="malariaPreventionReferred2Referred">
                                                        <option></option>
                                                        <option>Yes</option>
                                                        <option>No</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <input type="hidden" name="gender"><input type="hidden" name="dateBirth">
                                        </div>
                                    </div>
                                    <div class="pull-right">
                                        <button id="save_button" type="button" class="btn btn-fill btn-info">Save
                                        </button>
                                        <!-- <button id="delete_button" disabled="true" type="submit" class="btn btn-fill btn-danger">Delete</button> -->
                                        <button id="close_button" type="button" class="btn btn-fill btn-default"/>
                                        Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="descriptionTb" id="descriptionTb">
                    <input type="hidden" name="descriptionDm" id="descriptionDm"><input type="hidden"
                                                                                        name="tbscreengrId"
                                                                                        id="tbscreengrId">
                    <!-- </div> -->
                    <div id="userGroup" style="display: none">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>