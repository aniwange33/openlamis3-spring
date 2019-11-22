<%-- 
    Document   : Data Export
    Created on : Aug 15, 2012, 6:53:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/JavaScript">
        var facilityIds = [];
        var fac_ids;
        $(document).ready(function () {
            initialize();
            reports();

            //                $("#date1").mask("99/99/9999");
            //                $("#date1").datepicker({
            //                    dateFormat: "dd/mm/yy",
            //                    changeMonth: true,
            //                    changeYear: true,
            //                    yearRange: "-100:+0",
            //                    constrainInput: true,
            //                    buttonImageOnly: true,
            //                    buttonImage: "/images/calendar.gif"
            //                });
            //
            //                $("#date2").mask("99/99/9999");
            //                $("#date2").datepicker({
            //                    dateFormat: "dd/mm/yy",
            //                    changeMonth: true,
            //                    changeYear: true,
            //                    yearRange: "-100:+0",
            //                    constrainInput: true,
            //                    buttonImageOnly: true,
            //                    buttonImage: "/images/calendar.gif"
            //                });

            $("body").ajaxStart(function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            });

            $("body").ajaxStop(function (event) {
                $("#loader").html('');
            });
            $("#messageBar").hide();

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

            $.ajax({
                url: "Labtest_retrieve_map.action",
                dataType: "json",
                success: function (labtestMap) {
                    var options = "";
                    $.each(labtestMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#labtestId").html(options);

                }
            }); //end of ajax call    

            $("#grid").jqGrid({
                //url: "Facility_grid.action?stateId='0'",
                datatype: "json",
                mtype: "GET",
                colNames: ["Facility"],
                colModel: [
                    {name: "name", index: "name", width: "500"}
                    //{name: "sel", index: "sel", width: "50", align: "center", formatter:"checkbox", editoptions:{value:"1:0"}, formatoptions:{disabled:false}}
                ],
                rowNum: -1,
                sortname: "facilityId",
                sortorder: "desc",
                multiselect: true,
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 250,
                jsonReader: {
                    root: "facilityList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "facilityId"
                },
                onSelectAll: function (aRowids) {
                    for (var i = aRowids.length - 1; i >= 0; i--)
                        $(this).jqGrid('editRow', aRowids[i], true);
                    console.log(aRowids[i]);
                }
            }); //end of jqGrid    

            $("#recordType").change(function (event) {
                $("#messageBar").slideUp('fast');

                //select lab test to convert
                if ($("#recordType").val() == 4) {
                    $("#labsel").show("slow");
                } else {
                    $("#labsel").hide("slow");
                }

                //convert all EID record
                if ($("#recordType").val() == 8) {
                    $("#stateId").attr("disabled", true);
                } else {
                    $("#stateId").attr("disabled", false);
                }

                //View Identifiers
                if ($("#recordType").val() == 1) {
                    if ($("#viewIdentifiers").text() == "true") {
                        $("#view_id").show("slow");
                        $("#view_id").attr("disabled", false);
                    }
                } else {
                    $("#view_id").hide("slow");
                    $("#view_id").attr("disabled", true);
                }

                //select viral load period to convert
                if ($("#recordType").val() == 9) {
                    $("#datesel").show("slow");
                } else {
                    $("#datesel").hide("slow");
                }

                //convert all Patient Monthly record//Load in the dates


                if ($("#recordType").val() == 11) {
                    $("#yearsel").show("slow");
                    //$("#fac").hide("slow");
                } else {
                    $("#yearsel").hide("slow");
                    //$("#fac").val("");
                    //$("#fac").show("slow");
                }

                if ($("#recordType").val() == 12) {
                    $("#yearsel").hide("slow");
                    //$("#fac").hide("slow");
                } else {
                    //$("#yearsel").show("slow");
                    //$("#fac").val("");
                    //$("#fac").show("slow");
                }

                return false;
            });

            $("#stateId").change(function (event) {
                $("#messageBar").slideUp('fast');
                $("#grid").setGridParam({
                    url: "Facilitysel_grid.action?q=1&stateId=" + $("#stateId").val(),
                    page: 1
                }).trigger("reloadGrid");
            });

            $("#ok_button").click(function (event) {
                //console.log($("#stateId").val().length);
                fac_ids = jQuery("#grid").jqGrid('getGridParam', 'selarrrow');
                //console.log(fac_ids.toString());
                facilityIds = [];
                //                    if($("#stateId").val().length == 0 ){
                //                        if($("#recordType").val() != 8){
                //                            $("#stateHelp").html(" *");
                //                        }else if($("#recordType").val() == 11){
                //                            convertData();
                //                        }
                //                    }
                //                    else {
                $("#stateHelp").html("");
                var ids = $("#grid").getDataIDs();
                for (var i = 0; i < ids.length; i++) {
                    var data = $("#grid").getRowData(ids[i]);
                    if (data.sel == 1) {
                        facilityIds.push(ids[i]);
                    }
                }
                if ($("#recordType").val() == 9) {
                    if (validateForm()) {
                        //Call the conversion function...;
                        convertData();
                    }
                } else {
                    //Call the conversion function...;
                    convertData();
                }
                //}
                return false;
            });

            $("#close_button").click(function (event) {
                $("#lamisform").attr("action", "Conversion_page");
                return true;
            });

        });

        var url = "";
        var x = function wait() {
            window.open(url);
        }

        function convertData() {
            $("#messageBar").hide();
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: "Converter_dispatch.action",
                dataType: "json",
                data: {
                    recordType: $("#recordType").val(),
                    year: $("#yearId option:selected").text(),
                    state: $("#stateId option:selected").text(),
                    labtestId: $("#labtestId").val(),
                    reportingDateBegin: $("#reportingDateBegin").val(),
                    reportingDateEnd: $("#reportingDateEnd").val(),
                    facilityIds: fac_ids.toString(),
                    viewIdentifier: $("#viewIdentifier").prop("checked")
                },
                beforeSend: function () {
                    //console.log($("#recordType").val(), $("#yearId option:selected").text(), $("#stateId option:selected").text(), $("#labtestId").val(), $("#reportingDateBegin").val(), $("#reportingDateEnd").val(), fac_ids.toString());
                },
                success: function (fileName) {
                    console.log(fileName);
                    $("#messageBar").html("Conversion Completed").slideDown('slow');
                    $("#ok_button").attr("disabled", false);
                    url = fileName;
                    window.setTimeout(x, 3000);
                },
                error: function (e) {
                    console.log("Error: " + JSON.stringify(e));
                    alert("There was an error in conversion!");
                    $("#ok_button").attr("disabled", false);
                }
            });
        }

        function validateForm() {
            var regex = /^\d{2}\/\d{2}\/\d{4}$/;
            var validate = true;

            $("#date1").datepicker("option", "altField", "#reportingDateBegin");
            $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
            $("#date2").datepicker("option", "altField", "#reportingDateEnd");
            $("#date2").datepicker("option", "altFormat", "mm/dd/yy");

            // check if beginning date is entered
            if ($("#reportingDateBegin").val().length == 0 || !regex.test($("#reportingDateBegin").val())) {
                $("#dateHelp").html(" *");
                validate = false;
            } else {
                $("#dateHelp").html("");
            }
            // check if beginning date is entered
            if ($("#reportingDateEnd").val().length == 0 || !regex.test($("#reportingDateEnd").val())) {
                $("#dateHelp").html(" *");
                validate = false;
            } else {
                $("#dateHelp").html("");
            }
            return validate;
        }


    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>
<div class="mt-5"></div>
<div class="content mr-auto ml-auto">
    <!-- MAIN CONTENT -->
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/converter">Data Conversion</a></li>
        <li class="breadcrumb-item active">Convert Data to Excel</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <div class="row">
            <div class="col-md-12 ml-auto mr-auto">
                <div class="card">
                    <div class="card-body">

                        <div id="loader"></div>
                        <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label>Record to Convert:</label></td>
                                            <select name="recordType" style="width: 100%;" class="form-control select2"
                                                    id="recordType">
                                                <option>Select</option>
                                                <option value=1>Patient Records</option>
                                                <option value=2>Clinic Records</option>
                                                <option value=3>Pharmacy Records</option>
                                                <option value=4>Laboratory Records</option>
                                                <option value=7>Care &amp; Support Records</option>
                                                <option value=10>Patient Treatment Outcome</option>
                                                <option value=5>Patient Status Summary</option>
                                                <option value=9>Viral Load Test Summary</option>
                                                <option value=6>Data Sync Audit Summary</option>
                                                <option value=8>EID Records</option>
                                                <option value=11>Patient Monthly Summary</option>
                                                <option value=12>Patient Encounter Summary</option>
                                                <option value=13>Treatment Retention Summary</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group" id="fac">
                                            <label>State/Facility:</label>
                                            <select name="stateId" style="width: 100%;" class="form-control select2"
                                                    id="stateId">
                                            </select><span id="stateHelp" class="errorspan"></span>
                                        </div>
                                    </div>
                                </div>
                                <div id="labsel" style="display: none">
                                    <select name="labtestId" style="width: 100%;" class="form-control select2"
                                            id="labtestId"/>
                                    <option></option>
                                    </select>
                                </div>
                                <div id="yearsel" style="display: none">
                                    <select name="yearId" style="width: 100%;" class="form-control select2"
                                            id="yearId"/>
                                    <option></option>
                                    <option value=2000>2000</option>
                                    <option value=2001>2001</option>
                                    <option value=2002>2002</option>
                                    <option value=2003>2003</option>
                                    <option value=2004>2004</option>
                                    <option value=2005>2005</option>
                                    <option value=2006>2006</option>
                                    <option value=2007>2007</option>
                                    <option value=2008>2008</option>
                                    <option value=2009>2009</option>
                                    <option value=2010>2010</option>
                                    <option value=2011>2011</option>
                                    <option value=2012>2012</option>
                                    <option value=2013>2013</option>
                                    <option value=2014>2014</option>
                                    <option value=2015>2015</option>
                                    <option value=2016>2016</option>
                                    <option value=2017>2017</option>
                                    </select>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="btn-group pull-right">
                                            <button class="btn btn-info" id="ok_button">Generate</button>
                                            <!--                                        <button class="btn btn-default" id="close_button">Close</button>-->
                                        </div>
                                    </div>
                                </div>
                                <div id="datesel" style="display: none">
                                    <input name="date1" type="text" style="width: 86px;" class="inputboxes" id="date1"/>
                                    <input name="reportingDateBegin" type="hidden" id="reportingDateBegin"/> to:
                                    <input name="date2" type="text" style="width: 87px;" class="inputboxes" id="date2"/>
                                    <input name="reportingDateEnd" type="hidden" id="reportingDateEnd"/>
                                    <span id="dateHelp" class="errorspan"></span>
                                </div>
                                <div id="view_id" style="display: none">
                                    <input name="viewIdentifier" type="checkbox" id="viewIdentifier"/>&nbsp;<label>Unscramble
                                    patient identifiers like names, addresses and phone numbers</label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-12">
                                        <table id="grid" class="table"></table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
