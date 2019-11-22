<%-- 
    Document   : User
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
    <!--        <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />
            <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css" />
            <link type="text/css" rel="stylesheet" href="themes/basic/grid.css" />
            <link type="text/css" rel="stylesheet" href="themes/jqModal.css" />-->

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <!--        <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
            <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
            <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
            <script type="text/javascript" src="js/grid.locale-en.js"></script>
            <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
            <script type="text/javascript" src="js/jqDnR.js"></script>
            <script type="text/javascript" src="js/jqModal.js"></script>-->
    <script type="text/JavaScript">
        var updateRecord = false;
        var selected_ids, loaded_state_ids = "";

        $(document).ready(function () {
            resetPage();

            $("#grid").jqGrid({
                url: "User_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["User Name", "Full Name", "User Group", "Facility Accessed", "Time Login", "State", "", ""],
                colModel: [
                    {name: "username", index: "username", width: "100"},
                    {name: "fullName", index: "fullName", width: "150"},
                    {name: "userGroup", index: "userGroup", width: "120"},
                    {name: "name", index: "name", width: "288"},
                    {name: "timeLogin", index: "timeLogin", width: "100"},
                    {name: "stateIds", index: "stateIds", width: "0", sortable: false, hidden: true},
                    {name: "password", index: "password", width: "0", sortable: false, hidden: true},
                    {name: "viewIdentifier", index: "viewIdentifier", width: "0", sortable: false, hidden: true},
                ],
                rowNum: -1,
                sortname: "userId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 300,
                jsonReader: {
                    root: "userList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "userId"
                },
                onSelectRow: function (id) {
                    $("#states").hide();
                    var data = $("#grid").getRowData(id);
                    //                        console.log(data);
                    $("#fullname").val(data.fullName);
                    $("#username").val(data.username);
                    $("#password").val(data.password);
                    $("#passwordConfirm").val(data.password);
                    $("#userGroup").val(data.userGroup);
                    if (data.userGroup === "State Administrator") {
                        $("#states").show();
                    }
                    $("#state_ids").val(data.stateIds);
                    loaded_state_ids = data.stateIds;
                    $("#viewIdentifier").val(data.viewIdentifier);
                    if (data.viewIdentifier == "1") {
                        $("#viewIdentifier").attr("checked", "checked");
                    } else {
                        $("#viewIdentifier").removeAttr("checked");
                    }
                    $("#userId").val(id);

                    //Reload the states Grid and set selected
                    $("#statesgrid").setGridParam({url: "State_user_retrieve.action", page: 1}).trigger("reloadGrid");

                    updateRecord = true;
                    lastSelected = id;
                    initButtonsForModify();
                }
            }); //end of jqGrid 

            $("#statesgrid").jqGrid({
                url: "State_user_retrieve.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["", "State Name"],
                colModel: [
                    {name: "stateId", index: "stateId", width: "5", hidden: true},
                    {name: "name", index: "name", width: "200"}
                ],
                sortname: "stateId",
                sortorder: "desc",
                viewrecords: true,
                rowNum: 100,
                imgpath: "themes/basic/images",
                multiselect: true,
                resizable: false,
                height: 300,
                jsonReader: {
                    root: "statesList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "stateId"
                },
                loadComplete: function (data) {
                    if (loaded_state_ids.length === 1) {
                        jQuery('#statesgrid').jqGrid('setSelection', loaded_state_ids);
                    } else if (loaded_state_ids.length > 1) {
                        var s_data = loaded_state_ids.split(",");
                        for (var i = 0; i < s_data.length; i++) {
                            jQuery('#statesgrid').jqGrid('setSelection', s_data[i]);
                        }
                    }
                },
                onSelectRow: function (id) {

                }
            });
            $("#save_button").bind("click", function (event) {
                //Pull staes selected...
                selected_ids = jQuery("#statesgrid").jqGrid('getGridParam', 'selarrrow');
                $("#stateIds").val(selected_ids)
                if ($("#userGroupLogin").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    if (validateForm()) {
                        if (updateRecord) {
                            $("#lamisform").attr("action", "User_update");
                        } else {
                            $("#lamisform").attr("action", "User_save");
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            $("#delete_button").bind("click", function (event) {
                if ($("#userGroupLogin").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                } else {
                    $("#lamisform").attr("action", "User_delete");
                }
                return true;
            });

            $("#close_button").click(function (event) {
                $("#lamisform").attr("action", "Setup_page");
                return true;
            });

            var showing = false;
            $("#userGroup").change(function (event) {
                var userGroup = $("#userGroup").val();
                if (userGroup === "State Administrator") {
                    $("#states").show('slow');
                } else {
                    $("#states").hide('slow');
                }
            });
        });

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            //For the full namw
            if ($("#fullname").val().length == 0) {
                $("#nameHelp").html(" *");
                validate = false;
            } else {
                $("#nameHelp").html("");
            }

            //For the Username
            if ($("#username").val().length == 0) {
                $("#userHelp").html(" *");
                validate = false;
            } else {
                $("#userHelp").html("");
            }

            //For the Password
            if ($("#password").val().length == 0) {
                $("#passHelp").html(" *");
                validate = false;
            } else {
                $("#passHelp").html("");
            }

            if ($("#passwordConfirm").val().length == 0 || $("#password").val() != $("#passwordConfirm").val()) {
                $("#passHelp1").html(" *");
                validate = false;
            } else {
                $("#passHelp1").html("");
            }

            if ($("#userGroup").val().length == 0) {
                $("#userGroupHelp").html(" *");
                validate = false;
            } else {
                $("#userGroupHelp").html("");
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
    <li class="breadcrumb-item active">User Setup</li>
</ol>
<form id="lamisform" method="post" theme="css_xhtml">
    <input name="name" type="hidden" id="name"/>
    <input name="patientId" type="hidden" id="patientId"/>
    <input name="devolveId" type="hidden" id="devolveId"/>
    <input name="dateDevolved" type="hidden" id="dateDevolved"/>
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
                        <div class="form-group col-md-6">
                            <label>Full Name</label>
                            <input name="fullname" type="text" class="form-control" id="fullname"/>
                            <span id="nameHelp" class="errorspan"></span>
                        </div>
                        <div class="form-group col-md-6">
                            <label>User Name</label>
                            <input name="username" type="text" class="form-control" id="username"/>
                            <span id="usernameHelp" class="errorspan"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Password</label>
                            <input name="password" type="password" class="form-control" id="password"/>
                            <span id="passHelp" class="errorspan"></span>
                        </div>
                        <div class="form-group col-md-6">
                            <label>Confirm Password</label>
                            <input name="passwordConfirm" type="password" class="form-control" id="passwordConfirm"/>
                            <span id="passHelp1" class="errorspan"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>User Group</label>
                            <select name="userGroup" class="form-control select2" style="width:100%;" id="userGroup">
                                <option value="">Select</option>
                                <option value="Administrator">Administrator</option>
                                <option value="State Administrator">State Administrator</option>
                                <option value="Clinician">Clinician</option>
                                <option value="Pharmacist">Pharmacist</option>
                                <option value="Laboratory Scientist">Laboratory Scientist</option>
                                <option value="Data Analyst">Data Analyst</option>
                                <option value="Data Clerk">Data Clerk</option>
                            </select>
                            <span id="userGroupHelp" class="errorspan"></span>
                        </div>
                        <div class="col-md-6">
                            <div class="form-check">
                                <label class="form-check-label">
                                    <input name="viewIdentifier" type="checkbox" class="form-check-input" value="1"
                                           id="viewIdentifier"/>
                                    <span class="form-check-sign"></span>View patient identifiers
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div id="states" class="form-group col-4" style="display:none;">
                            <label>Assign State</label>
                            <table id="statesgrid" class="table-bordered table-striped"></table>
                            <input name="stateIds" type="text" hidden id="stateIds"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-12">
                            <div class="table-responsive ml-auto mr-auto">
                                <label> Search Result</label>
                                <table id="grid" class="table-bordered table-striped"></table>
                            </div>
                        </div>
                    </div>

                    <div id="userGroupLogin" style="display: none"><s:property value="#session.userGroup"/></div>
                    <div class="pull-right">
                        <button id="save_button" class="btn btn-info">Save</button>
                        <button id="close_button" class="btn btn-default"/>
                        Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<div id="user_group" style="display: none">Administrator</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
