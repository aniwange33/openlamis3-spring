<%-- 
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
    <title>LAMIS 2.6</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/commence-common.js"></script>
    <script type="text/JavaScript">
        var obj = {};
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
                //$("#lamisform").attr("action", "Commence_search");
                if ('referrer' in document) {
                    window.location = document.referrer;
                } else {
                    window.history.back();
                }
            });
        });

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
    <li class="breadcrumb-item"><a href="/Clinic_page">Clinic</a></li>
    <li class="breadcrumb-item active" aria-current="page">ART Commencement</li>
</ol>
<s:form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label class="form-label">Hospital Number <span style="color:black">:</span></label>
                            <input name="hospitalNum" type="text" class="form-control" id="hospitalNum"
                                   readonly="readonly"/>
                            <input name="patientId" type="text" class="form-control" id="patientId">
                            <input name="clinicId" type="text" class="form-control" id="clinicId">
                        </div>
                        <div class="col-md-6 form-group">
                            <br/>
                            <span id="patientInfor"></span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label class="form-label">ART Start Date<span style="color:black">*</span></label>
                            <input name="date1" type="text" class="form-control" id="date1"/>
                            <input name="dateVisit" type="hidden" class="form-control" id="dataVisit"/>
                            <span id="dateHelp" class="errorspan"></span>
                        </div>
                        <div class="col-md-4 form-group">
                            <label class="form-label">CD4 at Start of ART<span style="color:black">:</span></label>
                            <input name="cd4" type="text" class="form-control" id="cd4"/>
                            <!--  <input name="cd4p" type="text" class="form-control" id="cd4p"/> -->
                        </div>
                        <div class="col-md-4 form-group">
                            <label class="form-label">CD4%<span style="color:black">:</span></label>
                            <input name="cd4p" type="text" class="form-control" id="cd4p"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label class="form-label">Original Regimen Line*</label>
                            <select name="regimentype" style="width: 100%;" class="form-control select2"
                                    id="regimentype"><span id="regimentypeHelp" class="errorspan"></span>
                                <!--  <option></option>
                                            <option>ART First Line Children</option>
                                                <option>ART First Line Adult</option>
                                                <option>ART Second Line Children</option>
                                                <option>ART Second Line Adult</option>
                                                <option>Third</option> -->
                            </select>
                        </div>
                        <div class="col-md-4 form-group">
                            <label class="form-label">Original Regimen*</label>
                            <select name="regimen" style="width: 100%;" class="form-control select2" id="regimen">
                                <span id="regimenHelp" class="errorspan"></span>
                            </select>
                        </div>
                        <div class="col-md-4 form-group">
                            <label class="form-label">Clinical Stage:</label>
                            <select name="clinicalStage" id="clinicalStage" style="width: 100%;"
                                    class="form-control select2"
                                    id="clinicalStage">
                                <option></option>
                                <option>Stage I</option>
                                <option>Stage II</option>
                                <option>Stage III</option>
                                <option>Stage IV</option>
                            </select>
                        </div>
                        <div class="col-md-4 form-group">
                            <label class="form-label">Functional Status:</label>
                            <select name="funcStatus" id="funcStatus" style="width: 100%;" class="form-control select2"
                                    id="funcStatus">
                                <option></option>
                                <option>Working</option>
                                <option>Ambulatory</option>
                                <option>Bedridden</option>
                            </select>
                        </div>
                        <div class="col-md-4 form-group">
                            <label class="form-label">TB Status</label>
                            <select name="tbStatus" style="width: 100%;" class=" form-control select2" id="tbStatus">
                                <option></option>
                                <option>No sign or symptoms of TB</option>
                                <option>TB suspected and referred for evaluation</option>
                                <option>Currently on INH Prophylaxis</option>
                                <option>Currently on TB treatment</option>
                                <option>TB positive not on TB drugs</option>
                            </select>
                        </div>
                        <div class="col-md-4 form-group">
                            <div class="form-check">
                                <label class="form-check-label">
                                    <input name="pregnant" type="checkbox" value="1" class="form-check-input"
                                           id="pregnant"/>
                                    <span class="form-check-sign"></span>Pregnant
                                </label>
                                <label class="form-check-label">
                                    <input name="breastfeeding" type="checkbox" class="form-check-input"
                                           id="breastfeeding"/>
                                    <span class="form-check-sign"></span>Breastfeeding
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label class="form-label">Bodyweight(kg)<span style="color:black">:</span></label>
                            <input name="BodyWeight" type="text" class="form-control" id="BodyWeight"/>
                            <span id="numHelp" style="color:black"></span>
                        </div>
                        <div class="col-md-4 form-group">
                            <label class="form-label">Height(m)<span style="color:black">:</span></label>
                            <input name="height" type="text" class="form-control" id="height"/><span id="bmi"
                                                                                                     style="color: red"></span>
                            <span id="numHelp" style="color:black"></span>
                        </div>
                        <div class="col-md-4 form-group">
                            <label class="form-label">Blood Pressure(mmHg)<span style="color:black">:</span></label>
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <input name="bp1" type="text" class="form-control" id="bp1"/>
                                    <span id="numHelp" style="color:black"></span>
                                </div>
                                <div class="col-md-6 form-group">
                                    <input name="bp2" type="text" class="form-control" id="bp2"/>
                                    <input type="hidden" name="bp" id="bp">
                                    <span id="numHelp" style="color:red"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label class="form-label">Date of Next Appointment*</label>
                            <input name="date2" type="text" class="form-control" id="date2">
                            <input name="nextAppointment" value="" type="hidden" id="nextAppointment">
                            <span id="nextHelp" style="color: red"></span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <label>Notes:</label>
                            <textarea name="notes" rows="7" cols="65" id="notes"></textarea>
                        </div>
                    </div>

                    <input name="oiIds" type="text" class="form-control" id="oiIds"/>
                    <input name="adrId" type="text" class="form-control" id="adrId"/>
                    <input name="adhereIds" type="text" class="form-control" id="adhereIds"/>
                    <input name="description" type="text" class="form-control" id="description"/>
                    <input name="screener" type="text" class="form-control" value="1" id="screener"/>
                    <input name="gender" type="text" class="form-control" id="gender"/>
                    <input name="dateBirth" type="text" class="form-control" id="dateBirth"/>
                    <input name="currentStatus" type="text" class="form-control" id="currentStatus"/>
                    <input name="dateCurrentStatus" type="text" class="form-control" id="dateCurrentStatus"/>
                    <input name="dateLastCd4" type="text" class="form-control" id="dateLastCd4"/>
                    <input name="dateLastClinic" type="text" class="form-control" id="dateLastClinic"/>
                    <input name="dateNextClinic" type="text" class="form-control" id="dateNextClinic"/>
                    <input name="commence" type="text" class="form-control" value="1" id="commence"/>
                    <input name="dateRegistration" type="text" class="form-control" id="dateRegistration"/>
                    <input name="dateConfirmedHiv" type="text" class="form-control" id="dateConfirmedHiv"/>

                    <div id="userGroup" style="display: none">
                        <s:property value="#session.userGroup"/>
                    </div>
                    <div class="pull-right">
                        <button id="save_button" type="submit" class="btn btn-fill btn-info">Save</button>
                        <button id="close_button" type="reset" class="btn btn-fill btn-default">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</s:form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>

</html>