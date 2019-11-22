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
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!--        <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />
            <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css" />
            <link type="text/css" rel="stylesheet" href="themes/basic/grid.css" />
            <link type="text/css" rel="stylesheet" href="themes/jqModal.css" />-->

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <!--        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
            <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
            <script type="text/javascript" src="js/grid.locale-en.js"></script>
            <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
            <script type="text/javascript" src="js/jqDnR.js"></script>
            <script type="text/javascript" src="js/jqModal.js"></script>
            <script type="text/javascript" src="js/jquery.timer.js"></script>-->
    <script type="text/JavaScript">
        var obj = {};
        var updateRecord = false;
        var lastSelected = -99;
        $(document).ready(function () {
            //resetPage();

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
                url: "Communitypharm_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Pharmacy", "Address", "Phone", "Email", "Pin", "StateId", "LgaId"],
                colModel: [
                    {name: "pharmacy", index: "pharmacy", width: "200"},
                    {name: "address", index: "address", width: "150"},
                    {name: "phone", index: "phone1", width: "140"},
                    {name: "email", index: "email", width: "200"},
                    {name: "pin", index: "pin", width: "70"},
                    {name: "stateId", index: "stateId", width: "0", sortable: false, hidden: true},
                    {name: "lgaId", index: "lgaId", width: "0", sortable: false, hidden: true},
                ],
                rowNum: -1,
                sortname: "communitypharmId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 300,
                jsonReader: {
                    root: "pharmList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "communitypharmId"
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    $("#pharmacy").val(data.pharmacy);
                    $("#address").val(data.address)
                    $("#phone").val(data.phone)
                    $("#email").val(data.email)
                    $("#pin").val(data.pin)
                    $("#communitypharmId").val(id);
                    obj.stateId = data.stateId;
                    obj.lgaId = data.lgaId;
                    retrieve(obj);

                    updateRecord = true;
                    lastSelected = id;
                    initButtonsForModify()
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
                        })
                        $("#lgaId").html(options);
                    }
                }); //end of ajax call
                $("#grid").setGridParam({
                    url: "Communitypharm_grid.action?q=1&stateId=" + $("#stateId").val(),
                    page: 1
                }).trigger("reloadGrid");
                reset();
            });

            $("#lgaId").change(function (event) {
                $("#grid").setGridParam({
                    url: "Communitypharm_grid.action?q=1&stateId=" + $("#stateId").val() + "&lgaId=" + $("#lgaId").val(),
                    page: 1
                }).trigger("reloadGrid");
                reset();
            });

            $("#save_button").click(function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    if (validateForm()) {
                        $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                        if (updateRecord) {
                            $("#lamisform").attr("action", "Communitypharm_update");
                        } else {
                            $("#lamisform").attr("action", "Communitypharm_save");
                        }
                        $("#loader").html('');
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            $("#delete_button").click(function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                } else {
                    $("#lamisform").attr("action", "Communitpharm_delete");
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
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
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

            if ($("#pharmacy").val().length == 0) {
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
    <li class="breadcrumb-item active">Community Pharmacy Setup</li>
</ol>
<!-- MAIN CONTENT -->
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">

                    <div id="loader"></div>
                    <div id="messageBar"></div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>State</label>
                                <select name="stateId" style="width: 100%;" class="form-control select2" id="stateId">
                                </select>
                                <span id="stateHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>L.G.A</label>
                                <select name="lgaId" style="width: 100%;" class="form-control select2" id="lgaId">
                                </select>
                                <span id="lgaHelp" class="errorspan"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Name of Pharmacy</label>
                                <input name="pharmacy" type="text" class="form-control" id="pharmacy"/>
                                <span id="nameHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Address</label>
                                <input name="address" type="text" class="form-control" id="address"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Telephone</label>
                                <input name="phone" type="tel" class="form-control" id="phone"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Alternate Telephone</label>
                                <input name="phone1" type="tel" class="form-control" id="phone1"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Email</label>
                                <input name="email" type="email" class="form-control" id="email"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Activation Code</label>
                                <input name="pin" type="text" class="form-control" id="pin"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="table-responsive">
                                <label> Search Result</label>
                                <table id="grid" class="table table-bordered table-striped table-hover"></table>
                            </div>
                        </div>
                    </div>

                    <div id="user_group" style="display: none">Administrator</div>
                    <div id="userGroup" style="display: none"><s:property value="#session.userGroup"/></div>
                    <div class="pull-right">
                        <button id="save_button" type="submit" class="btn btn-fill btn-info">Save</button>
                        <!--                                    <button id="delete_button" disabled="true" type="submit" class="btn btn-fill btn-danger">Delete</button>-->
                        <button id="close_button" type="reset" class="btn btn-fill btn-default">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
