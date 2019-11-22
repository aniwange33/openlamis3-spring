<%-- 
    Document   : casemanager_data
    Created on : Oct 20, 2017, 4:00:54 PM
    Author     : DURUANYANWU IFEANYI
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
        var obj = {};
        var updateRecord = false;
        var lastSelected = -99;
        $(document).ready(function () {
            resetPage();

            $("#grid").jqGrid({
                url: "case-manager/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["Full Name", "Address", "Phone", "Age", "Sex", "Facility Id"],
                colModel: [
                    {name: "fullname", index: "fullname", width: "240"},
                    {name: "address", index: "address", width: "140"},
                    {name: "phone", index: "phone", width: "140"},
                    //{name: "religion", index: "religion", width: "100"},
                    {name: "age", index: "age", width: "120"},
                    {name: "sex", index: "sex", width: "120"},
                    {name: "facilityId", index: "facilityId", width: "0", sortable: false, hidden: true},
                ],
                rowNum: -1,
                sortname: "casemanagerId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 300,
                jsonReader: {
                    root: "caseManagerList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "casemanagerId"
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    $("#fullname").val(data.fullname);
                    $("#address").val(data.address);
                    $("#phoneNumber").val(data.phone);
                    $("#sex").val(data.sex);
                    $("#age").val(data.age);
                    $("#religion").val(data.religion);
                    $("#casemanagerId").val(id);
                    $("#id").val(data.facilityId);

                    updateRecord = true;
                    lastSelected = id;
                    initButtonsForModify();
                }
            });

            $("#save_button").bind("click", function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    //if(validateForm()) {
                    $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                    if (updateRecord) {
                        $("#lamisform").attr("action", "Casemanager_update");
                    } else {
                        $("#lamisform").attr("action", "Casemanager_save");
                    }
                    $("#loader").html('');
                    return true;
                    //}
                    //else {
                    // return false;
                    //}
                }
            });

            $("#delete_button").bind("click", function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                } else {
                    $("#lamisform").attr("action", "Casemanager_delete");
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

        function validateForm() {
            var validate = true;
            // check for valid input is entered
            if ($("#stateId").val().length === 0) {
                $("#stateHelp").html(" *");
                validate = false;
            } else {
                $("#stateHelp").html("");
            }

            if ($("#lgaId").val().length === 0) {
                $("#lgaHelp").html(" *");
                validate = false;
            } else {
                $("#lgaHelp").html("");
            }

            if ($("#pharmacy").val().length === 0) {
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
    <li class="breadcrumb-item"><a href="/">Home</a></li>
    <li class="breadcrumb-item"><a href="/setup">Setup</a></li>
    <li class="breadcrumb-item active">Case Manager Details</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
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
                            <div class="form-group">
                                <label>Full Name:</label>
                                <input name="fullname" type="text" class="form-control" id="fullname"/>
                                <span id="nameHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Address:</label>
                                <input name="address" type="text" class="form-control" id="address"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Telephone:</label>
                                <input name="phoneNumber" type="text" class="form-control" id="phoneNumber"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Age: </label>
                                <input name="age" type="text" class="form-control" id="age"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Sex: </label>
                                <select name="sex" class="form-control select2" style="width: 100%;" id="sex">
                                    <option value="">Select</option>
                                    <option value="male">Male</option>
                                    <option value="female">Female</option>
                                </select>
                                <span id="sexHelp" class="errorspan"></span>
                                <input name="religion" value="" type="text" class="form-control" id="religion" hidden/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12 ml-auto mr-auto">
                            <div class="table-responsive">
                                <label> Search Result</label>
                                <table id="grid" class="table table-bordered table-striped"></table>
                            </div>
                        </div>
                    </div>
                    <div id="user_group" style="display: none">Administrator</div>
                    <div class="pull-right">
                        <button id="save_button" type="submit" class="btn btn-fill btn-info">Save</button>
                        <!--                                        <button id="delete_button" disabled="true" type="submit" class="btn btn-fill btn-danger">Delete</button>-->
                        <button id="close_button" type="reset" class="btn btn-fill btn-default">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>

<!-- END MAIN CONTENT-->
<div id="user_group" style="display: none">Administrator</div>
<div id="userGroup" style="display: none"><s:property value="#session.userGroup"/></div>
</body>
</html>

