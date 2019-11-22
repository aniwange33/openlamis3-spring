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
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/JavaScript">
        var obj = {};
        $(document).ready(function () {
            resetPage();

            $.ajax({
                url: "StateId_retrieve.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#stateId").html(options);
                }
            }); //end of ajax call

            $("#grid").jqGrid({
                url: "Encounter_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No.", "Name", "Regimen Dispensed", "Quantity", "Date of Refill", "Community Pharmacy"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "120"},
                    {name: "name", index: "name", width: "250"},
                    {name: "regimen1", index: "regimen1", width: "250"},
                    {name: "duration1", index: "duration1", width: "100", align: "center"},
                    {
                        name: "dateVisit",
                        index: "dateVisit",
                        width: "150",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "pharmacy", index: "pharmacy", width: "265"},
                ],
                rowNum: -1,
                sortname: "encounterId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 450,
                jsonReader: {
                    root: "encounterList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "encounterId"
                }
            });

            $("#stateId").change(function (event) {
                $.ajax({
                    url: "LgaId_retrieve.action",
                    dataType: "json",
                    data: {stateId: $("#stateId").val()},

                    success: function (lgaMap) {
                        var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
                        $.each(lgaMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }) //end each
                        $("#lgaId").html(options);
                    }
                }); //end of ajax call
            });
            $("#lgaId").change(function (event) {
                $.ajax({
                    url: "Facility_retrieve.action",
                    dataType: "json",
                    data: {stateId: $("#stateId").val(), lgaId: $("#lgaId").val()},

                    success: function (facilityMap) {
                        var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
                        $.each(facilityMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }) //end each
                        $("#id").html(options);
                    }
                }); //end of ajax call
            });

            $("#id").change(function (event) {
                var name = $("select[name='id'] option:selected").text();
                $("#facilityName").val(name);
            });

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    $("#lamisform").attr("action", "Verify_group");
                    return true;
                } else {
                    return false;
                }
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Event_page");
                return true;
            });
        });

        function refreshGrid() {
        }

        var timer = $.timer(function () {
            $("#grid").setGridParam({url: "Encounter_grid.action?q=1", page: 1}).trigger("reloadGrid");
        });
        timer.set({time: 120000, autostart: true});

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>
<!-- MAIN CONTENT -->
<div class="mt-5"></div>
<div class="content mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/event">Events Monitor</a></li>
        <li class="breadcrumb-item active">Community Pharmacy Events</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <div class="row">
            <div class="col-12">
                <div class="card">

                    <div class="card-body">
                        <div id="messageBar"></div>
                        <input name="facilityName" type="hidden" id="facilityName"/>

                        <div class="row">
                            <div class="col-6 form-group">
                                <label class="form-label">State</label>
                                <select name="stateId" style="width: 100%;" class="form-control select2" id="stateId">
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-6 form-group">
                                <label class="form-label">L.G.A.</label>
                                <select name="lgaId" style="width: 100%;" class="form-control select2" id="lgaId">
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-6 form-group">
                                <label>Facility</label>
                                <select name="facilityId" style="width: 100%;" class="form-control select2"
                                        id="facilityId">
                                </select>
                                <span id="facilityHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12">
                                <div class="card-category"> Refill Events</div>
                                <div class="table-responsive">
                                    <table id="grid"></table>
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
