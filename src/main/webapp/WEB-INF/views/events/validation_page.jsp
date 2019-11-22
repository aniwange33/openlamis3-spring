<%-- 
    Document   : validation_page
    Created on : Aug 7, 2017, 10:38:24 AM
    Author     : DURUANYANWU IFEANYI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>

    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/JavaScript">
        var selected_ids;
        var recordIds = [];
        var creators = [];
        var patientIds = [];
        var gridNum = 4;
        var enablePadding = true;
        $(document).ready(function () {
            //console.log("ready");
            $.ajax({
                url: "Padding_status.action",
                dataType: "json",
                success: function (statusMap) {
                    enablePadding = statusMap.paddingStatus;
                }
            });
            resetPage();
            initialize();
            reports();

            for (i = new Date().getFullYear(); i > 1900; i--) {
                $("#validationYear").append($("<option/>").val(i).html(i));
            }

            $("#validationMonth").val("08");
            $("#validationYear").val("2016");


            $("#grid").jqGrid({
                url: "validation_grid.action?year=2016&month=08",
                datatype: "json",
                mtype: "GET",
                colNames: ["ID", "Entity", "Count"],
                colModel: [
                    {name: "id", index: "id", width: "100"},
                    {name: "entity", index: "entity", width: "520"},
                    {name: "entityCount", index: "entityCount", width: "190"}
                ],
                sortname: "id",
                sortorder: "asc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "validationList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "id"
                },
                onSelectRow: function (id) {
                    var selectedRow = $("#grid").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#grid").getRowData(selectedRow);
                        var entity = data.id;
                        //console.log("Entity is: "+entity);
                        $("#detail").jqGrid("clearGridData", true).setGridParam({
                            url: "entity_grid.action?entity=" + entity,
                            page: 1
                        }).trigger("reloadGrid");
                        $("#validate_button").attr("disabled", false);
                    }

                }
            }); //end of master jqGrid                 

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["", "S/N", "Patient ID ", "Date", "Hospital Number", "Name", "Status", "", ""],
                colModel: [
                    {
                        name: "sel",
                        index: "sel",
                        width: "30",
                        align: "center",
                        formatter: "checkbox",
                        editoptions: {value: "1:0"},
                        formatoptions: {disabled: false}
                    },
                    {name: "sn", index: "sn", width: "50"},
                    {name: "patientId", index: "patientId", width: "130"},
                    {
                        name: "dateRegistration",
                        index: "dateRegistration",
                        width: "170",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "hospitalNum", index: "hospitalNum", width: "270"},
                    {name: "name", index: "name", width: "400"},
                    {name: "statusRegistration", index: "statusRegistration", width: "100"},
                    {name: "fullName", index: "fullName", width: "5", hidden: true},
                    {name: "validated", index: "validated", width: "5", hidden: true}
                ],
                pager: $('#pager'),
                rowNum: -1,
                sortname: "patientId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                loadtext: "Wait, while record loads...",
                resizable: false,
                //                    multiselect: true,
                height: 250,
                jsonReader: {
                    root: "entityList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                },
                afterInsertRow: function (id, data) {
                    if (data.validated == "true") {
                        $(this).jqGrid('setRowData', id, false, {background: 'lime'});
                    }
                },
                onSelectRow: function (id) {
                    var data = $(this).getRowData(id);
                    if (data.sel == 1) {
                        $(this).jqGrid('setRowData', id, true, {color: 'black'});
                    } else {
                        $(this).jqGrid('setRowData', id, false, {color: 'black'});
                    }

                }
            }); //end of detail jqGrid                 

            $("#validate_button").bind("click", function (event) {
                //selected_ids = jQuery("#detail").jqGrid('getGridParam','selarrrow');
                recordIds = [];
                creators = [];
                patientIds = [];
                var ids = $("#detail").getDataIDs();
                for (var i = 0; i < ids.length; i++) {
                    var data = $("#detail").getRowData(ids[i]);
                    if (data.sel == 1) {
                        var hospNum = data.hospitalNum;
                        var report_date = data.dateRegistration;
                        var entity_id = hospNum + "#" + report_date;
                        recordIds.push(entity_id);
                        creators.push(data.fullName);
                        patientIds.push(data.patientId);
                    }
                }
                if (recordIds == "") {
                    alert("Please mark records to validate");
                } else {
                    submitValidation(recordIds, creators, patientIds);
                }
                event.preventdefault();
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Event_page");
                return true;
            });
            $("#profile_button").bind("click", function (event) {
                //console.log("Profiler Clicked");
                buildProfiler();
                event.preventDefault();
            });
            $("#fetch_records").bind("click", function (event) {
                //console.log(" Record Fetcher Clicked");
                retrieveResult();
                event.preventDefault();
            });
        });

        function retrieveResult() {

            var url = "validation_grid.action?year=" + $("#validationYear").val() + "&month=" + $("#validationMonth").val();
            $("#detail").jqGrid("clearGridData", true);
            $("#validate_button").attr("disabled", true);
            $("#validationMonth").val($("#validationMonth").val());
            $("#validationYear").val($("#validationYear").val());
            $("#grid").jqGrid("clearGridData", true).setGridParam({url: url, page: 1}).trigger("reloadGrid");
        }

        var url = "";
        var x = function wait() {
            window.open(url);
        }

        function buildProfiler() {
            $("#messageBar").hide();
            //$("#ok_button").attr("disabled", true);
            $.ajax({
                url: "Profiler_download.action",
                dataType: "json",
                data: {recordType: 1, year: $("#validationYear").val(), month: $("#validationMonth").val()},
                beforeSend: function () {
                    //console.log($("#recordType").val(), $("#yearId option:selected").text(), $("#stateId option:selected").text(), $("#labtestId").val(), $("#reportingDateBegin").val(), $("#reportingDateEnd").val(), fac_ids.toString());
                },
                success: function (fileName) {
                    console.log(fileName);
                    $("#messageBar").html("Profiling Report Generated Successfully").slideDown('slow');
                    //$("#ok_button").attr("disabled", false);
                    url = fileName;
                    window.setTimeout(x, 3000);
                },
                error: function (e) {
                    console.log("Error: " + JSON.stringify(e));
                    alert("There was an error in conversion!");
                    //$("#ok_button").attr("disabled", false);
                }
            });
        }

        function submitValidation(recordIds, creators, patientIds) {

            var data = {
                "records_ids": recordIds.toString(),
                "creators": creators.toString(),
                "patient_ids": patientIds.toString()
            };
            console.log(data);
            $.ajax({
                url: "save_validation.action",
                data: data,
                dataType: "json",
                success: function (statusMap) {
                    enablePadding = statusMap.paddingStatus;
                }
            });
        }

    </script>
    <script>
        // $.jgrid.defaults.width = 780;
        $.jgrid.defaults.responsive = true;
        $.jgrid.defaults.styleUI = 'Bootstrap';
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>
<!-- MAIN CONTENT -->
<div class="mt-5"></div>
<div class="content mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item"><a href="Event_pharmacy_page.action">Events Monitor</a></li>
        <li class="breadcrumb-item active">Data Profiling and Validation</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <input name="clinicId" type="hidden" id="clinicId"/>
        <input name="patientId" type="hidden" id="patientId"/>
        <div class="row">
            <div class="col-md-12 ml-auto mr-auto">
                <div class="card">
                    <!-- <div class="card-header bg-light">
                        <h5 class="card-title">Data Profiling and Validation</h5>
                    </div> -->
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <label>Validation Period</label>
                                <div class="row">
                                    <div class="col-md-6">
                                        <select name="validationMonth" style="width: 100%;" class="form-control select2"
                                                id="validationMonth"/>
                                        <option>Select</option>
                                        <option value="01">January</option>
                                        <option value="02">February</option>
                                        <option value="03">March</option>
                                        <option value="04">April</option>
                                        <option value="05">May</option>
                                        <option value="06">June</option>
                                        <option value="07">July</option>
                                        <option value="08">August</option>
                                        <option value="09">September</option>
                                        <option value="10">October</option>
                                        <option value="11">November</option>
                                        <option value="12">December</option>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <select name="validationYear" style="width: 100%;" class="form-control select2"
                                                id="validationYear"/>
                                        <option>Select</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="pull-right">
                                    <button id="fetch_records" class="btn btn-info btn-sm">Fetch Records</button>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-10">
                                <div id="messageBar" class="alert alert-success alert-dismissible fade show"
                                     role="alert">
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header">
                                <div class="card-category">Records Listing</div>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="table-responsive">
                                            <table id="grid" class="table table-hover"></table>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-8">
                                        <div id="buttons" class="pull-right">
                                            <button id="validate_button" class="btn btn-info" disabled="true">Validate
                                            </button>
                                            <button id="profile_button" class="btn btn-default">Profile Data</button>
                                            <!-- <button id="close_button">Close</button> -->
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="table-responsive">
                                            <table id="detail" class="table table-hover"></table>
                                            <div id="pager" style="text-align:center;"></div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <!-- END MAIN CONTENT-->
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
