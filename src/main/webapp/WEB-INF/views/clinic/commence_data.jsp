<%-- 
    Document   : Clinic
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="/js/lamis/commence-common.js"></script>
    <script type="text/JavaScript">
        var obj = {};
        var oiIds = "", adrIds = "", adhereIds = "", date = "", lastSelectDate = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

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
                success: function (clinicList) {
                    populateForm(clinicList);
                }
            }); //end of ajax call

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

            $("#date2").bind("change", function (event) {
                var dated = $("#date1").val();
                if (dated.length != 0) {
                    if (parseInt(compare($("#date1").val(), $("#date2").val())) == -1) {
                        var message = "Date of next appointment cannot be ealier than date of visit";
                        $("#messageBar").html(message).slideDown('slow');
                        $("#date2").val("");
                    } else {
                        $("#messageBar").slideUp('slow');
                    }
                }
            });
            $("#close_button").bind("click", function (event) {
                window.location.href = "/event-page/patient/" + $('#patientId').val();
            });

        });

        function checkDate() {

        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/">Home</a></li>
    <li class="breadcrumb-item"><a href="/clinic">Clinic</a></li>
    <li class="breadcrumb-item active" aria-current="page">ART Commencement</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
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
                            <input name="clinicId" type="hidden" id="clinicId"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label>ART Start Date<span style="color: red;">*</span></label>
                            <input name="date1" type="text" class="form-control datepicker" id="date1"/>
                            <input name="dateVisit" type="hidden" id="dateVisit"/>
                        </div>
                        <div class="col-md-4 form-group">
                            <label>CD4 at start of ART</label>
                            <input name="cd4" type="text" class="form-control" id="cd4"/>
                        </div>
                        <div class="col-md-4 form-group">
                            <label>CD4%</label>
                            <input name="cd4p" type="text" class="form-control" id="cd4p"/>
                        </div>
                        <span id="cd4Help" class="errorspan"></span>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label>Original Regimen Line <span style="color: red;">*</span></label>
                            <select name="regimentype" style="width: 100%;" class="form-control select2"
                                    id="regimentype">
                            </select>
                            <span id="regimenTypeHelp" class="errorspan"></span>
                        </div>
                        <div class="col-md-4 form-group">
                            <label>Original Regimen <span style="color: red;">*</span></label>
                            <select name="regimen" style="width: 100%;" class="form-control select2" id="regimen">
                            </select>
                            <span id="regimenHelp" class="errorspan"></span>
                        </div>
                        <div class="col-md-4 form-group">
                            <label>Clinical Stage</label>
                            <select name="clinicStage" style="width: 100%;" class="form-control select2"
                                    id="clinicStage">
                                <option></option>
                                <option>Stage I</option>
                                <option>Stage II</option>
                                <option>Stage III</option>
                                <option>Stage IV</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label>Functional Status</label>
                            <select name="funcStatus" style="width: 100%;" class="form-control select2" id="funcStatus">
                                <option></option>
                                <option>Working</option>
                                <option>Ambulatory</option>
                                <option>Bedridden</option>
                            </select>
                        </div>
                        <div class="col-md-4 form-group">
                            <label>TB Status</label>
                            <select name="tbStatus" style="width: 100%;" class="form-control select2" id="tbStatus">
                                <option></option>
                                <option>No sign or symptoms of TB</option>
                                <option>TB suspected and referred for evaluation</option>
                                <option>Currently on INH prophylaxis</option>
                                <option>Currently on TB treatment</option>
                                <option>TB positive not on TB drugs</option>
                            </select>
                        </div>
                        <div class="col-md-4 form-group">
                            <label>Pregnancy Status</label>
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
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label>Body Weight(kg)</label>
                            <input name="bodyWeight" type="text" class="form-control" id="bodyWeight"/>
                        </div>
                        <div class="col-md-4 form-group">
                            <label>Height(cm):</label>
                            <input name="height" type="text" class="form-control" id="height"/>
                            <span id="bmi" style="color:red"></span>
                        </div>
                        <div class="col-md-4 form-group">
                            <label>Blood Pressure(mmHg)</label>
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <input name="bp1" type="text" class="form-control" id="bp1"/>
                                </div>
                                <div class="col-md-6 form-group">
                                    <input name="bp2" type="text" style="width: 25px;" class="form-control" id="bp2"/>
                                </div>
                            </div>
                            <input name="bp" type="hidden" style="width: 50px;" class="form-control" id="bp"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label>Date of Next Appointment <span style="color: red;">*</span></label>
                            <input name="date2" type="text" class="form-control" id="date2"/>
                        </div>
                        <div class="col-md-4 form-group">
                            <input name="nextAppointment" value="" type="hidden" id="nextAppointment"/>
                            <span id="nextHelp" style="color:red"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Notes:</label>
                            <textarea name="notes" rows="10" cols="70" id="notes"></textarea>
                        </div>
                    </div>
                    <input name="oiIds" type="hidden" id="oiIds"/><input name="adrId" type="hidden" id="adrId"/>
                    <input name="adhereIds" type="hidden" id="adhereIds"/>
                    <input name="description" type="hidden" id="description"/>
                    <input name="screener" type="hidden" value="1" id="screener"/>
                    <!--                                <input name="notes" type="hidden" id="notes"/>-->
                    <input name="gender" type="hidden" id="gender"/>
                    <input name="dateBirth" type="hidden" id="dateBirth"/>
                    <input name="currentStatus" type="hidden" id="currentStatus"/>
                    <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/>
                    <input name="dateLastCd4" type="hidden" id="dateLastCd4"/>
                    <input name="dateLastClinic" type="hidden" id="dateLastClinic"/>
                    <input name="dateNextClinic" type="hidden" id="dateNextClinic"/>
                    <input name="commence" type="hidden" value="1" id="commence"/>
                    <input name="dateRegistration" type="hidden" id="dateRegistration"/>
                    <input name="dateConfirmedHiv" type="hidden" id="dateConfirmedHiv"/>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="pull-right">
                                <button id="save_button" type="button" class="btn btn-info">Save</button>
                                <button id="close_button" type="button" class="btn btn-default"/>
                                Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<div id="userGroup" style="display: none"></div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
