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
    <script type="text/JavaScript">
        var obj = {};
        var updateRecord = false;
        var lastSelected = -99;
        $(document).ready(function () {
            //resetPage();
            for (i = 1; i < 31; i++) {
                $("#dayDqa").append($("<option/>").val(i).html(i));
            }
            $("#dayDqa").val("15");

            $.ajax({
                url: "StateId_retrieve.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#stateId").html(options);
                }
            }); //end of ajax call

            $("#grid").jqGrid({
                url: "Facility_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Facility", "Address", "Phone1", "Phone2", "Email", "Address1", "Address2", "FacilityType", "StateId", "LgaId", "PadHospitalNum", "DayDqa"],
                colModel: [
                    {name: "name", index: "name", width: "320"},
                    {name: "address", index: "address", width: "170"},
                    {name: "phone1", index: "phone1", width: "135"},
                    {name: "phone2", index: "phone2", width: "135"},
                    {name: "email", index: "email", width: "100", sortable: false, hidden: true},
                    {name: "address1", index: "address1", width: "0", sortable: false, hidden: true},
                    {name: "address2", index: "address2", width: "0", sortable: false, hidden: true},
                    {name: "facilityType", index: "facilityType", width: "0", sortable: false, hidden: true},
                    {name: "stateId", index: "stateId", width: "0", sortable: false, hidden: true},
                    {name: "lgaId", index: "lgaId", width: "0", sortable: false, hidden: true},
                    {name: "padHospitalNum", index: "padHospitalNum", width: "0", sortable: false, hidden: true},
                    {name: "dayDqa", index: "dayDqa", width: "0", sortable: false, hidden: true},
                ],
                rowNum: -1,
                sortname: "facilityId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 220,
                jsonReader: {
                    root: "facilityList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "facilityId"
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    $("#name").val(data.name);
                    $("#address1").val(data.address1)
                    $("#address2").val(data.address2)
                    $("#phone1").val(data.phone1)
                    $("#phone2").val(data.phone2)
                    $("#email").val(data.email)
                    $("#dayDqa").val(data.dayDqa)
                    $("#facilityType").val(data.facilityType)
                    if (data.padHospitalNum == "1") {
                        $("#padHospitalNum").attr("checked", "checked");
                    } else {
                        $("#padHospitalNum").removeAttr("checked");
                    }
                    $("#id").val(id);
                    obj.stateId = data.stateId;
                    obj.lgaId = data.lgaId;
                    retrieve(obj);

                    updateRecord = true;
                    lastSelected = id;
                    initButtonsForModify()
                }
            }); //end of jqGrid 

            $("#stateId").change(function (event) {
                $.ajax({
                    url: "LgaId_retrieve.action",
                    dataType: "json",
                    data: {stateId: $("#stateId").val()},

                    success: function (lgaMap) {
                        var options = "<option value = '" + '' + "'>" + '' + "</option>";
                        $.each(lgaMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }) //end each
                        $("#lgaId").html(options);
                    }
                }); //end of ajax call
                $("#grid").setGridParam({
                    url: "Facility_grid.action?q=1&stateId=" + $("#stateId").val(),
                    page: 1
                }).trigger("reloadGrid");
                reset();
            });

            $("#lgaId").change(function (event) {
                $("#grid").setGridParam({
                    url: "Facility_grid.action?q=1&stateId=" + $("#stateId").val() + "&lgaId=" + $("#lgaId").val(),
                    page: 1
                }).trigger("reloadGrid");
                reset();
            });

            $("#save_button").bind("click", function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    if (validateForm()) {
                        $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                        if (updateRecord) {
                            $("#lamisform").attr("action", "Facility_update");
                        } else {
                            $("#lamisform").attr("action", "Facility_save");
                        }
                        $("#loader").html('');
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            $("#delete_button").bind("click", function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                } else {
                    $("#lamisform").attr("action", "Facility_delete");
                }
                return true;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Setup_page");
                return true;
            });
            //checkConnection();
        });

        function reset() {
            updateRecord = false;
            lastSelected = -99;
            resetButtons();
        };

        function checkConnection() {
            $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            $.ajax({
                url: "Internet_connection.action",
                dataType: "json",
                error: function (jgXHR, status) {
                    alert(status);
                },
                success: function (status) {
                    if (status == 1) {
                        if (updateRecord) {
                            $("#save_button").removeAttr("disabled");
                            $("#delete_button").removeAttr("disabled");
                        } else {
                            $("#save_button").removeAttr("disabled");
                            $("#delete_button").attr("disabled", "disabled");
                        }
                        $("#messageBar").slideUp('slow');
                    } else {
                        $("#save_button").attr("disabled", "disabled");
                        $("#delete_button").attr("disabled", "disabled");
                        $("#messageBar").html("No connection established with the LAMIS server, please connect to the internet before proceeding").slideDown('slow');
                    }
                }
            });
            $("#loader").html('');
        };

        function retrieve(obj) {
            $.ajax({
                url: "LgaId_retrieve.action",
                dataType: "json",
                data: {stateId: obj.stateId},
                success: function (lgaMap) {
                    var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
                    $.each(lgaMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#lgaId").html(options);
                    $("#lgaId").val(obj.lgaId);
                    $("#stateId").val(obj.stateId);
                }
            }); //end of ajax call            
        };

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            if ($("#stateId").val().length == 0) {
                $("#stateHelp").html(" *");
                validate = false;
            } else {
                $("#stateHelp").html("");
            }

            if ($("#lgaId").val().length == 0) {
                $("#lgaHelp").html(" *");
                validate = false;
            } else {
                $("#lgaHelp").html("");
            }

            if ($("#name").val().length == 0) {
                $("#nameHelp").html(" *");
                validate = false;
            } else {
                $("#nameHelp").html("");
            }
            return validate;
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_setup.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item"><a href="Setup_page">Setup</a></li>
    <li class="breadcrumb-item active">Facility Setup</li>
</ol>
<form id="lamisform" method="post" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar"></div>

                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>State</label>
                            <select name="stateId" style="width: 100%;" class="form-control select2" id="stateId">
                            </select>
                            <span id="stateHelp" class="errorspan"></span>
                        </div>
                        <div class="form-group col-md-6">
                            <label>L.G.A</label>
                            <select name="lgaId" style="width: 100%;" class="form-control select2" id="lgaId">
                            </select>
                            <span id="lgaHelp" class="errorspan"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Facility Name</label>
                            <input name="name" type="text" class="form-control" id="name"/>
                            <span id="nameHelp" class="errorspan"></span>
                        </div>
                        <div class="form-group col-md-6">
                            <label>Type of Facility</label>
                            <select name="facilityType" style="width: 100%;" class="form-control select2"
                                    id="facilityType"/>
                            <option value="">Select</option>
                            <option>Primary</option>
                            <option>Secondary</option>
                            <option>Tertiary</option>
                            <option>Private</option>
                            </select>
                            <span id="typeHelp" class="errorspan"></span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Address</label>
                            <input name="address1" type="text" class="form-control" id="address1"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label>Telephone</label>
                            <input name="address2" type="text" class="form-control" id="address2"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Telephone</label>
                            <input name="phone1" type="text" class="form-control" id="phone1"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label>SMS Printer Phone</label>
                            <input name="phone2" type="text" class="form-control" id="phone2"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Email</label>
                            <input name="email" type="text" class="form-control" id="email"/>
                        </div>
                    </div>
                    <div class="divider">Settings</div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Day for DQA</label>
                            <select name="dayDqa" class="form-control select2" style="width:100%;" id="dayDqa"/>
                            <option value="">Select</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <div class="form-check">
                                <label class="form-check-label">
                                    <input class="form-check-input" name="padHospitalNum" type="checkbox" value="0"
                                           id="padHospitalNum"/>
                                    <span class="form-check-sign"></span> Enable padding on hospital number
                                </label>
                            </div>
                        </div>
                        <div class="form-group col-md-6">
                            <div class="form-check">
                                <label class="form-check-label">
                                    <input class="form-check-input" name="defaultFacility" type="checkbox" value="0"
                                           id="defaultFacility"/>
                                    <span class="form-check-sign"></span> Use as Default Facility?
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="table-responsive col-12">
                            <label>Search Result</label>
                            <table id="grid" class="table table-bordered table-striped"></table>
                        </div>
                    </div>
                    <div id="user_group" style="display: none">Administrator</div>

                    <div class="pull-right">
                        <button id="save_button" class="btn btn-info">Save</button>
                        <!--  <button id="delete_button" class="btn btn-danger" disabled="true"/>Delete</button>-->
                        <button id="close_button" class="btn btn-default"/>
                        Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>

<!-- END MAIN CONTENT-->
<div id="user_group" style="display: none">Administrator</div>
<div id="userGroup" style="display: none"></div>
</body>
</html>
