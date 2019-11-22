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

            var queryString = decodeURIComponent(window.location.search);
            var action = queryString.substring(8);
            if (action == 'view') {
                $("#save_button").hide("slow");
                $("#close_button").hide("slow");
                $("#lamisform :input").prop('readonly', true);
                //$("#lamisform :select").prop('readonly', true);
            }

            getPrescribedDrugs();
            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#regimengrid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
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
                    {name: "description", index: "description", width: "280"},
                    {name: "morning", index: "morning", width: "95", sortable: false, editable: true, edittype: "text"},
                    {
                        name: "afternoon",
                        index: "afternoon",
                        width: "95",
                        sortable: false,
                        editable: true,
                        edittype: "text"
                    },
                    {name: "evening", index: "evening", width: "95", sortable: false, editable: true, edittype: "text"},
                    {
                        name: "duration",
                        index: "duration",
                        width: "95",
                        sortable: false,
                        editable: true,
                        edittype: "text"
                    },
                    {name: "quantity", index: "quantity", width: "97"},
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
                    {name: "description", index: "description", width: "200"},
                    {
                        name: "severity",
                        index: "severity",
                        width: "140",
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
                    {name: "description", index: "description", width: "345"},
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
            //Toggle Adverse Drug Reactions
            $("#adrScreened").change(function () {
                if ($("#adrScreened").val() == "Yes") {
                    $("#adrtable").show("slow");
                } else {
                    $("#adrtable").hide("slow");
                }
            });

            //Toggle Adverse Drug Reactions
            $("#regimentypeId").change(function () {
                // $('#adrtable').toggleClass('invisible visible');
                if ($("#regimentypeId").val() !== "") {
                    $("#regimentable").show("slow");
                } else {
                    $("#regimentable").hide("slow");
                }
            });

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
<!-- MAIN CONTENT -->
<form id="lamisform" theme="css_xhtml">
    <input name="regimenId" type="hidden" id="regimenId"/>
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/pharmacy">Pharmacy</a></li>
        <li class="breadcrumb-item"><a href="/pharmacy/view">Drug Dispensing</a></li>
        <li class="breadcrumb-item active">Data Entry</li>
    </ol>
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Hospital No:</label>
                                <input name="hospitalNum" type="text" class="form-control" id="hospitalNum"
                                       readonly="readonly"/>
                                <input name="name" type="hidden" id="name"/>
                                <input name="patientId" type="hidden" id="patientId"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <p><br clear="both">
                                    <span id="patientInfor" class="text-dark title"></span>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-6">
                            <div class="form-group">
                                <label>Date of Dispensing</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date1" type="text" class="form-control" id="date1"/>
                                </div>
                            </div>
                            <input name="dateVisit" type="hidden" id="dateVisit"/>
                            <span id="dateHelp" class="errorspan"></span>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Refill Period (days)</label>
                                <input name="refill" type="text" class="form-control" id="refill"/>
                                <span id="refillHelp" style="color:red"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Date of Next Appointment:</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date2" type="text" class="form-control" id="date2"/>
                                </div>
                                <input name="nextAppointment" type="hidden" id="nextAppointment"/>
                                <span id="nextHelp" style="color:red"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-check">
                                <br>
                                <label class="form-check-label">
                                    <input name="prescripError" type="checkbox" value="0" id="prescripError"
                                           class="form-check-input"/>
                                    <span class="form-check-sign"></span>Any prescription error?
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Adverse Drug Reactions:</label>
                                <select name="adrScreened" class="form-control select2" id="adrScreened"
                                        style="width: 100%;">
                                    <option>Select</option>
                                    <option value="No">No</option>
                                    <option value="Yes">Yes</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Regimen/Drugs</label>
                                <select name="regimentypeId" class="form-control select2" id="regimentypeId"
                                        style="width: 100%;">
                                    <option>Select</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div id="adrtable" style="display: none" class="table-responsive">
                                <table id="adrgrid"></table>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div id="regimentable" style="display: none">
                                <div class="row">
                                    <div class="col-12">
                                        <div class="input-group no-border col-6 pull-right">
                                            <input type="text" class="form-control search" placeholder="Search...">
                                            <div class="input-group-append">
                                                <div class="input-group-text">
                                                    <i class="now-ui-icons ui-1_zoom-bold"></i>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="table-responsive">
                                            <table id="regimengrid"></table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 table-responsive ">
                            <div class="title"> Drug Dispensed</div>
                            <table id="grid" class="table table-striped table-bordered">
                            </table>
                        </div>
                    </div>
                    <div class="row">
                        <input name="gender" type="hidden" id="gender"/>
                        <input name="dateBirth" type="hidden" id="dateBirth"/>
                        <input name="currentStatus" type="hidden" id="currentStatus"/>
                        <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/>
                        <input name="dateStarted" type="hidden" id="dateStarted"/>
                        <input name="adrId" type="hidden" id="adrId"/>
                        <input name="screener" type="hidden" value="2" id="screener"/>
                        <input name="regdrugId" type="hidden" id="regdrugId"/>
                        <input name="dateLastRefill" type="hidden" id="dateLastRefill"/></td>
                        <input name="dateNextRefill" type="hidden" id="dateNextRefill"/>
                        <input name="regimentypePrevious" type="hidden" id="regimentypePrevious"/>
                        <input name="regimenPrevious" type="hidden" id="regimenPrevious"/>
                    </div>

                    <div class="pull-right">
                        <button id="save_button" class="btn btn-info">Save</button>
                        <button id="close_button" class="btn btn-default">Close</button>
                    </div>

                    <div id="fromPrescription" style="display: none">

                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>