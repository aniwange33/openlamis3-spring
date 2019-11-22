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
    <title>LAMIS 2.6</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <!--
            <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
                    <script src="assets/js/core/jquery-3.2.1.min.js"></script>
            <script type="text/javascript" src="js/lamis/lamis-common.js"></script>               -->
    <script type="text/javascript" src="js/lamis/updater-common.js"></script>
    <!--
                    <script src="assets/js/jquery-1.9.min.js"></script>
            <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
            <script type="text/javascript" src="js/grid.locale-en.js"></script>
            <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
            <script type="text/javascript" src="js/jqDnR.js"></script>
            <script type="text/javascript" src="js/jqModal.js"></script>
            <script type="text/javascript" src="js/json.min.js"></script>   -->
    <script type="text/JavaScript">
        var obj = {};
        var url = "";
        $(document).ready(function () {
            resetPage();
            initialize();

            setTimeout(function () {
                $("#preloader").hide();
                $(".wrapper").show();
            }, 2000);

            $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');

            $("#grid").jqGrid({
                url: "Client_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No.", "Unique ID", "Name", "Gender", "ART Start", "Last Pickup", "Regimen (ART Start)", "Current Status", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "100"},
                    {name: "uniqueId", index: "uniqueId", width: "100"},
                    {name: "name", index: "name", width: "166"},
                    {name: "gender", index: "gender", width: "90"},
                    {
                        name: "dateStarted",
                        index: "date1",
                        width: "100",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "dateLastRefill",
                        index: "date2",
                        width: "100",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "regimenStart", index: "regimenStart", width: "200"},
                    {name: "currentStatus", index: "currentStatus", width: "120"},
                    {name: "patientId", index: "patientId", hidden: true},
                    {name: "dateRegistration", index: "dateRegistration", hidden: true},
                    {name: "regimentypeStart", index: "regimentypeStart", hidden: true},
                    {name: "regimentype", index: "regimentype", hidden: true},
                    {name: "regimen", index: "regimen", hidden: true},
                    {name: "regimenId", index: "regimenStart", hidden: true},
                    {name: "regimentypeId", index: "regimentypeStart", hidden: true},
                    {name: "duration", index: "duration", hidden: true},
                    {name: "surname", index: "surname", hidden: true},
                    {name: "otherNames", index: "otherNames", hidden: true},
                    {name: "dateBirth", index: "dateBirth", hidden: true},
                    {name: "age", index: "age", hidden: true},
                    {name: "ageUnit", index: "ageUnit", hidden: true},
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", hidden: true},
                    {name: "clinicStage", index: "clinicStage", hidden: true},
                    {name: "funcStatus", index: "funcStatus", hidden: true},
                    {name: "cd4", index: "cd4", hidden: true},
                    {name: "cd4p", index: "cd4p", hidden: true},
                    {name: "statusRegistration", index: "statusRegistration", hidden: true},
                    {name: "dateStarted", index: "dateStarted", hidden: true},
                    {name: "dateLastRefill", index: "dateLastRefill", hidden: true},
                    {name: "enrollmentSetting", index: "enrollmentSetting", hidden: true},
                    {name: "dateCurrentViralLoad", index: "dateCurrentViralLoad", hidden: true},
                    {name: "dateCollected", index: "dateCollected", hidden: true},
                    {name: "viralLoad", index: "viralLoad", hidden: true},
                    {name: "viralLoadIndication", index: "viralLoadIndication", hidden: true},
                    {
                        name: "category", index: "category", width: "60"
                        //formatter: function(cellvalue, options, rowObject){
//                     //$(this).parent().css('background', 'green');
//                     var test = $(this).jqGrid();
//                    
//            if(cellvalue == 1)
//                    return '<span style="background-color: red; display: block; width: 100%; height: 100%; ">' + cellvalue + '</span>';
//                else
//                    return cellvalue;
//            }
                    },
                ],
                rowNum: -1,
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 220,
                loadtext: "Retrieving records, please wait...",
                jsonReader: {
                    root: "clientList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false
                },
//            afterInsertRow: function(id, data) {
//            if(data.category == "1") {
//            $(this).jqGrid('setRowData', id, false, {background: 'khaki'});
//            }                        
//            if(data.category == "2") {
//            $(this).jqGrid('setRowData', id, false, {background: 'lightblue'});
//            }                        
//            if(data.category == "3") {
//            $(this).jqGrid('setRowData', id, false, {background: 'lime'});
//            }                        
//            if(data.category == "4") {
//            $(this).jqGrid('setRowData', id, false, {background: 'yellow'});
//            }                        
//            },
                loadComplete: function () {
                    $("#save_button").attr("disabled", true);
                    $("#loader").html('');

                    var ids = $(this).jqGrid("getDataIDs"), l = ids.length, i, rowid, status;
                    for (i = 0; i < l; i++) {
                        rowid = ids[i];
                        // get data from some column "ColumnName"
                        var category = $(this).jqGrid("getCell", rowid, "category");

                        if (category == "1") {
                            $('#' + $.jgrid.jqID(rowid)).css('background', 'khaki');
                        }
                        if (category == "2") {
                            $('#' + $.jgrid.jqID(rowid)).css('background', 'lightblue');
                        }
                        if (category == "3") {
                            $('#' + $.jgrid.jqID(rowid)).css('background', 'lime');
                        }
                        if (category == "4") {
                            $('#' + $.jgrid.jqID(rowid)).css('background', 'yellow');
                        }
                    }
                },
                onSelectRow: function (id) {
                    $("#save_button").attr("disabled", false);
                    $("#messageBar").hide();
                    var data = $("#grid").getRowData(id);
                    $("#id").val(data.patientId);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#uniqueId").val(data.uniqueId);
                    $("#currentStatus").val(data.currentStatus);
                    $("#surname").val(data.surname);
                    $("#otherNames").val(data.otherNames);
                    $("#age").val(data.age);
                    $("#ageUnit").val(data.ageUnit);
                    $("#gender").val(data.gender);
                    $("#statusRegistration").val(data.statusRegistration);
                    $("#statusRegistration").val(data.statusRegistration);
                    $("#enrollmentSetting").val(data.enrollmentSetting);
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
                    $("#dateCurrentViralLoad").val(data.dateCurrentViralLoad);
                    $("#date6").val(data.dateCurrentViralLoad);
                    $("#dateCollected").val(data.dateCollected);
                    $("#date7").val(data.dateCollected);
                    $("#viralLoad").val(data.viralLoad);
                    $("#viralLoadIndication").val(data.viralLoadIndication);
                    retrieveRegimen(data);
                    retrieveRegimenId(data);
                }
            }); //end of jqGrid 

            $("#radet_link1").bind("click", function (event) {
                var url = "Radet_report.action?reportType=1";
                event.preventDefault();
                event.stopPropagation();
                window.open(url);
                return false;
            });
            $("#radet_link2").bind("click", function (event) {
                var url = "Radet_report.action?reportType=2";
                event.preventDefault();
                event.stopPropagation();
                window.open(url);
                return false;
            });
            $("#radet_link3").bind("click", function (event) {
                var url = "Radet_report.action?reportType=3";
                event.preventDefault();
                event.stopPropagation();
                window.open(url);
                return false;
            });
            $("#radet_link4").bind("click", function (event) {
                var url = "Radet_report.action?reportType=4";
                event.preventDefault();
                event.stopPropagation();
                window.open(url);
                return false;
            });
        });

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item"><a href="/export">Data Maintenance</a></li>
    <li class="breadcrumb-item active" aria-current="page">RADET Analyzer & Data Update</li>
</ol>
<s:form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar"></div>
                    <!-- <span style="margin-left:650px"><button id="print_button" disabled="true">Convert to Excel</button></span>-->
                    <div class="row">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table id="grid"></table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                            <span id="khaki" style="height:10px; background-color: khaki">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <span id="radet_link1" style="color:blue"> No ARV Pickup</span>
                        </div>
                        <div class="col-md-3">
                            <span id="lightblue" style="height:10px; background-color: lightblue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <span id="radet_link2" style="color:blue"> No Regimen at ART Start</span>
                        </div>
                        <div class="col-md-4">
                            <span id="lime" style="height:10px; background-color: lime">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <span id="radet_link3" style="color:blue"> Lost to Follow Up Unconfirmed</span>&nbsp;&nbsp;&nbsp;&nbsp;
                        </div>
                        <div class="col-md-3">
                            <span id="yellow" style="height:10px; background-color: yellow">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <span id="radet_link4" style="color:blue"> Due for Viral Load Test</span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-12 ml-auto mr-auto">
                            <section>
                                <div class="row">
                                    <div class="col-md-10">
                                        <div class="card-title"></div>
                                    </div>
                                </div>
                                <div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-group">Hospital No:</label>
                                                <input name="hospitalNum" type="text" class="form-control"
                                                       id="hospitalNum" readonly="readonly"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Unique ID:</label>
                                                <input name="uniqueId" type="text" class="form-control" id="uniqueId"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Surname:</label>
                                                <input name="surname" type="text" class="form-control"
                                                       id="surname"/><span
                                                    id="surnameHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Other Names:</label>
                                                <input name="otherNames" type="text" class="form-control"
                                                       id="otherNames"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Birth:</label>
                                                <input name="date1" type="text" class="form-control"
                                                       id="date1"/><input name="dateBirth" type="hidden"
                                                                          id="dateBirth"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Age at Registration:</label>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <input name="age" type="text" class="form-control" id="age"/>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <select name="ageUnit" style="width: 100%"
                                                                class="form-control select2" id="ageUnit">
                                                            <option>year(s)</option>
                                                            <option>month(s)</option>
                                                            <option>day(s)</option>
                                                        </select><span id="ageHelp" class="errorspan"></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Gender:</label>
                                                <select name="gender" style="width: 100%;" class="form-control select2"
                                                        id="gender">
                                                    <option></option>
                                                    <option>Male</option>
                                                    <option>Female</option>
                                                </select><span id="genderHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">HIV Status at registration:</label>
                                                <select name="statusRegistration" style="width: 100%;"
                                                        class="form-control select2" id="statusRegistration"/>
                                                <option></option>
                                                <option>HIV exposed status unknown</option>
                                                <option>HIV+ non ART</option>
                                                <option>ART Transfer In</option>
                                                <option>Pre-ART Transfer In</option>
                                                </select><span id="statusregHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of registration/
                                                    &nbsp;Transfer-in:</label>
                                                <input name="date2" type="text" class="form-control" id="date2"/><input
                                                    name="dateRegistration" type="hidden" id="dateRegistration"/><span
                                                    id="dateregHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">ART enrollment setting:</label>
                                                <select name="enrollmentSetting" style="width: 100%;"
                                                        class="form-control select2" id="enrollmentSetting">
                                                    <option></option>
                                                    <option>Facility</option>
                                                    <option>Community</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <p></p>
                                <div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">ART Start Date:</label>
                                                <input name="date4" type="text" style="width: 100%;"
                                                       class="form-control"
                                                       id="date4"/><input name="dateStarted" type="hidden"
                                                                          id="dateStarted"/><span
                                                    id="dateHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="form-label">CD4 at start of ART:</label>
                                                <input name="cd4" type="text" class="form-control"
                                                       id="cd4"/>
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <div class="form-group">
                                                <label class="form-label">CD4%</label>
                                                <input name="cd4p" type="text" class="form-control" id="cd4p"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Clinical Stage:</label>
                                                <select name="clinicStage" style="width: 100%;"
                                                        class="form-control select2" id="clinicStage">
                                                    <option></option>
                                                    <option>Stage I</option>
                                                    <option>Stage II</option>
                                                    <option>Stage III</option>
                                                    <option>Stage IV</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Functional Status:</label>
                                                <select name="funcStatus" style="width: 100%;"
                                                        class="form-control select2" id="funcStatus">
                                                    <option></option>
                                                    <option>Working</option>
                                                    <option>Ambulatory</option>
                                                    <option>Bedridden</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Regimen Line (first):</label>
                                                <select name="regimentype" style="width: 100%;" class="form-control"
                                                        id="regimentype">
                                                    <option></option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Regimen (first):</label>
                                                <select name="regimen" style="width: 100%;" class="form-control"
                                                        id="regimen">
                                                    <option></option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <p></p>
                                <div style=" background-color: ghostwhite">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of last refill:</label>
                                                <input name="date5" type="text" style="width: 100%;"
                                                       class="form-control"
                                                       id="date5"/><input name="dateLastRefill" type="hidden"
                                                                          id="dateLastRefill"/><span id="dateHelp"
                                                                                                     class="errorspan"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Refill Period (days):</label>
                                                <input name="duration" type="text" class="form-control"
                                                       id="duration"/><span id="refillHelp"
                                                                            style="color:red"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="">Regimen Line (current):</label>
                                                <select name="regimentypeId" style="width: 100%;"
                                                        class="form-control select2"
                                                        id="regimentypeId">
                                                    <option></option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Regimen (current):</label>
                                                <select name="regimenId" style="width: 100%;"
                                                        class="form-control select2" id="regimenId">
                                                    <option></option>
                                                </select><span id="regimenHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Current Status:</label>
                                                <select name="currentStatus" style="width: 100%;"
                                                        class="form-control select2"
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
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Current Status:</label>
                                                <input name="date3" type="text" class="form-control"
                                                       id="date3"/><input name="dateCurrentStatus" type="hidden"
                                                                          id="dateCurrentStatus"/><span id="dateHelp"
                                                                                                        class="errorspan"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <p></p>
                                <div style=" background-color: ghostwhite">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Current Viral Load:</label>
                                                <input name="date6" type="text" class="form-control"
                                                       id="date6"/><input name="dateCurrentViralLoad" type="hidden"
                                                                          id="dateCurrentViralLoad"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Sample Collected:</label>
                                                <input name="date7" type="text" class="form-control"
                                                       id="date7"/><input name="dateCollected" type="hidden"
                                                                          id="dateCollected"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Current Viral Load (c/ml):</label>
                                                <input name="viralLoad" type="text" style="width: 100%;"
                                                       class="form-control" id="viralLoad"/><span id="vlHelp"
                                                                                                  style="color:red"></span></td>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Viral Load Indication:</label>
                                                <select name="viralLoadIndication" style="width: 100%;"
                                                        class="form-control select2"
                                                        id="viralLoadIndication">
                                                    <option></option>
                                                    <option>Routine Monitoring</option>
                                                    <option>Targeted Monitoring</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <input name="patientId" type="hidden" class="inputboxes" id="patientId"/>
                                </div>
                                <p></p>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="pull-right">
                                            <button id="save_button" class="btn btn-info">Save</button>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</s:form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
