<%-- 
    Document   : Contact
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
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/JavaScript">
        var enablePadding = true;
        var patientIds;
        $(document).ready(function () {

            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#grid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });

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

            $("#dialog").dialog({
                title: "Hospital Number Deduplication",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Yes", click: removeDuplicates},
                    {
                        text: "Cancel", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            var lastSelected = -99;

            loadGrid();

            $("#update_button").bind("click", function (event) {
                patientIds = [];
                var ids = $("#grid").getDataIDs();
                for (var i = 0; i < ids.length; i++) {
                    var data = $("#grid").getRowData(ids[i]);
                    if (data.sel == 1) {
                        patientIds.push(ids[i]);
                    }
                }
                $("#patientIds").val(patientIds);
                if ($("#patientIds").val().length == 0) {
                    $("#messageBar").html("No duplicate hospital number is selected").slideDown('slow');
                    return true;
                } else {
                    $("#messageBar").hide();
                    if ($("#userGroup").html() != "Administrator") {
                        $("#lamisform").attr("action", "Error_message");
                        return true;
                    } else {
                        $("#dialog").dialog("open");
                        return false;
                    }
                }

            });

            $("#export_button").bind("click", function () {
                $("#messageBar").hide();
                $("#messageBar").html('');
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');

                $.ajax({
                    url: "Export_duplicates.action",
                    dataType: "json",
                    success: function (fileName) {
                        console.log(fileName);
                        $("#loader").html('');
                        $("#messageBar").html("Export to excel completed").slideDown('slow');
                        url = fileName;
                        window.setTimeout(x, 3000);
                    },
                    error: function (e) {
                        console.log("Error: " + JSON.stringify(e));
                        $("#loader").html('');
                        alert("There was an error in exporting record");
                    }
                });
            });

            $("#import_button").bind("click", function (event) {
                var validate = true;
                $("#messageBar").hide();
                $("#messageBar").html('');

                // check if file name is entered
                if ($("#attachment").val().length == 0) {
                    $("#fileHelp").html(" *");
                    validate = false;
                } else {
                    $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
                    $("#fileHelp").html("");
                    if ($("#userGroup").html() != "Administrator") {
                        $("#lamisform").attr("action", "Error_message");
                        return true;
                    } else {
                        uploadFile();
                    }
                }

            });


            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });

        });

        function uploadFile() {
            var form = $('#uploadForm')[0];
            // Create an FormData object
            var data = new FormData(form);

            $.ajax({
                type: "POST",
                enctype: 'multipart/form-data',
                url: "uploadDuplicates",
                data: data,
                processData: false,
                contentType: false,
                cache: false,
                timeout: 600000,
                success: function (result) {
                    $("#grid").html();
                    $("#grid").setGridParam({url: "Duplicate_grid.action"})
                    $("#grid").trigger('reloadGrid');
                    $("#loader").html('');
                    $("#attachment").val('');
                    $("#messageBar").html("Duplicates remove successfully.").slideDown('slow');

                },
                error: function (e) {
                    console.log("ERROR : ", e);
                    $("#loader").html('');
                    $("#messageBar").html("Error in removing duplicates.").slideDown('slow');
                    $("#attachment").val('');
                }

            });
        }

        function removeDuplicates() {
            $("#dialog").dialog("close");
            $("#update_button").attr("disabled", true);
            $("#lamisform").attr("action", "Remove_duplicates");
            $("#lamisform").trigger("submit");
            $("#update_button").attr("disabled", false);
        }

        function loadGrid() {
            console.log("load grid initiated....");
            $("#grid").jqGrid({
                url: "Duplicate_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["", "Hospital No", "Unique ID", "Name", "Gender", "Date of Birth", "Address", "ART Status", "Encounters",],
                colModel: [
                    {
                        name: "sel",
                        index: "sel",
                        width: "50",
                        align: "center",
                        formatter: "checkbox",
                        editoptions: {value: "1:0"},
                        formatoptions: {disabled: false}
                    },
                    {name: "hospitalNum", index: "hospitalNum", width: "130"},
                    {name: "uniqueId", index: "uniqueId", width: "100"},
                    {name: "name", index: "name", width: "200"},
                    {name: "gender", index: "gender", width: "80"},
                    {
                        name: "dateBirth",
                        index: "dateBirth",
                        width: "130",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
                    },
                    {name: "address", index: "address", width: "280"},
                    {name: "currentStatus", index: "currentStatus", width: "150"},
                    {name: "count", index: "count", width: "100", align: 'center'},
                ],
                pager: $('#pager'),
                rowNum: -1,
                sortname: "hospitalNum",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 350,
                //width: 400,
                jsonReader: {
                    root: "duplicateList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                },
                onSelectRow: function (id) {
                    var data = $(this).getRowData(id);
                    if (data.sel == 1) {
                        $("#grid").jqGrid('setRowData', id, false, {background: 'khaki'});
                        //$("#grid").jqGrid('setRowData', id, false, {color: 'khaki'});
                    } else {
                        //console.log("Selected "+data.sel);
                        $("#grid").jqGrid('setRowData', id, false, {background: 'khaki'});
                    }
                }
            });
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item"><a href="/export">Data Maintenance</a></li>
    <li class="breadcrumb-item active">Remove Duplicate</li>
</ol>
<div id="loader"></div>
<div id="messageBar"></div>
<input name="patientIds" type="hidden" id="patientIds"/>
<form theme="css_xhtml" action="uploadDuplicates" id="uploadForm" method="post" enctype="multipart/form-data">
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>File</label>
                            <input type="file" class="form-control" name="attachment" id="attachment"/>
                            <span id="fileHelp" class="errorspan"></span>
                        </div>
                    </div>
                    <div class="col-md-6 form-group">
                        <div class="pull-right">
                            <button id="import_button" class="btn btn-info" name="import_button">Import Data</button>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <div id="buttons" style="width: 300px">
                                <a href="#" id="export_button" style="font-size:16px;">Export Duplicate record to
                                    Excel</a>
                            </div>
                        </div>
</form>
<form id="lamisform" theme="css_xhtml">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <div class="btn-group pull-left">
                        <button id="update_button" class="btn btn-info">Remove</button>
                    </div>
                </div>
                <div class="row">
                    <div class="table-responsive">
                        <table id="grid" class="table table-striped table-bordered center"></table>
                        <div id="pager" style="text-align:center;"></div>
                    </div>
                </div>
            </div>
            <p></p>
            <div id="dialog">
                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td><label>Do you want to continue with deletion of duplicate hospital numbers?</label></td>
                    </tr>
                    <tr>
                        <td width="20%"><label>Click Yes to continue or No to cancel:</label></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    </div>
    </div>

</form>
</div>
</div>
</div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</div>
</body>
</html>
