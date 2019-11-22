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
    <!--        <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css" />
            <link type="text/css" rel="stylesheet" href="themes/basic/grid.css" />
            <link type="text/css" rel="stylesheet" href="themes/jqModal.css" />-->

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <!--        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
            <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
            <script type="text/javascript" src="js/grid.locale-en.js"></script>
            <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
            <script type="text/javascript" src="js/jqDnR.js"></script>
            <script type="text/javascript" src="js/jqModal.js"></script>-->

    <script type="text/JavaScript">
        var facilityIds;
        $(document).ready(function () {
            initialize();

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


            $("#grid").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["S/N", "Facility", "Patient Count"],
                colModel: [
                    {name: "sn", index: "sn", width: "70"},
                    {name: "name", index: "name", width: "825"},
                    {name: "count", index: "count", width: "200"}
                ],
                rowNum: -1,
                sortname: "facilityId",
                sortorder: "desc",
                multiselect: true,
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 350,
                jsonReader: {
                    root: "facilityList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "facilityId"
                }
            }); //end of jqGrid    


            $("#stateId").change(function (event) {
                $("#messageBar").slideUp('fast');
                $("#grid").setGridParam({
                    url: "Facilitysel_grid.action?q=1&stateId=" + $("#stateId").val(),
                    page: 1
                }).trigger("reloadGrid");
            });

            $("#ok_button").bind("click", function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    facilityIds = jQuery("#grid").jqGrid('getGridParam', 'selarrrow');
                    convertData();
                    return false;
                }
            });

            $("#close_button").bind("click", function (event) {
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
            $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: "Converter_dispatch_ndr.action",
                dataType: "json",
                data: {stateId: $("#stateId").val(), facilityIds: facilityIds.toString()},
                success: function (fileName) {
                    console.log(fileName);
                    $("#messageBar").html("Conversion Completed").slideDown('slow');
                    $("#ok_button").attr("disabled", false);
                    url = fileName;
                    //window.setTimeout(x, 3000);
                },
                error: function (e) {
                    console.log("Error: " + JSON.stringify(e));
                    alert("There was an error in conversion!");
                    $("#ok_button").attr("disabled", false);
                }
            });
        }

        function validateUpload() {
            var validate = true;

            // check for valid input is entered
            if ($("#id").val().length == 0) {
                $("#facilityHelp").html(" *");
                validate = false;
            } else {
                $("#facilityHelp").html("");
            }
            return validate;
        }

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/visualizer_menu.jsp"/>
<!-- MAIN CONTENT -->
<div class="mt-5"></div>
<div class="content mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item"><a href="Converter_page">Data Conversion</a></li>
        <li class="breadcrumb-item active">Generate NDR Files</li>
    </ol>
    <form id="lamisform" theme="css_xhtml">
        <div class="row">
            <div class="col-md-12 ml-auto mr-auto">
                <div class="card">
                    <div class="card-body">
                        <div id="loader"></div>
                        <div id="messageBar"></div>

                        <div class="row">
                            <div class="col-4">
                                <div class="form-group">
                                    <label>State/Facility:</label>
                                    <select name="stateId" style="width: 100%;" class="form-control select2"
                                            id="stateId">
                                    </select>
                                    <span id="stateHelp" class="errorspan"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-4">
                                <button class="btn btn-info pull-right" id="ok_button">Convert</button>
                                <!--   <button class="btn btn-default" id="close_button">Close</button>-->
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12">
                                <div class="table-responsive">
                                    <table id="grid" class="table table-bordered table-striped"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <div id="userGroup" style="display: none"><s:property value="#session.userGroup"/></div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>

