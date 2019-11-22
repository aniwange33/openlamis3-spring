<%-- 
    Document   : validation_page
    Created on : Aug 7, 2017, 10:38:24 AM
    Author     : DURUANYANWU IFEANYI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/JavaScript">
        var url = "";
        var entity_global;
        $(document).ready(function () {
            $("body").bind('ajaxStart', function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            });

            $("body").bind('ajaxStop', function (event) {
                $("#loader").html('');
            });
            $("#messageBar").hide();


            $("#grid").jqGrid({
                url: "Enrolled_nonTX.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["", "Entity", "Count"],
                colModel: [
                    {name: "id", index: "id", hidden: true, width: "1"},
                    {name: "entity", index: "entity", width: "580"},
                    {name: "entityCount", index: "entityCount", width: "200"}
                ],
                sortname: "id",
                sortorder: "asc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "notificationList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "id"
                },
                onSelectRow: function (id) {
                    $("#messageBar").hide();
                    var selectedRow = $("#grid").getGridParam('selrow');
                    entity_global = null;
                    if (selectedRow != null) {
                        var data = $("#grid").getRowData(selectedRow);
                        var entity = data.id;
                        entity_global = entity;
                        //console.log("The ID is: "+id);
                        //if(data.entityCount > 0){
                        $("#detail").jqGrid("clearGridData", true).setGridParam({
                            url: "Enrolled_nonTXDetail.action?entity=" + entity,
                            page: 1
                        }).trigger("reloadGrid");
                        $("#generate_button").attr("disabled", false);
                        //}
                    }

                }
            }); //end of master jqGrid                 

            //Start for the details page...
            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["", "Registration Date", "Hospital Number", "Name", "Status"],
                colModel: [
                    {name: "patientId", index: "patientId", hidden: true, width: "30"},
                    {
                        name: "dateRegistration",
                        index: "dateRegistration",
                        width: "160",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "hospitalNum", index: "hospitalNum", width: "200"},
                    {name: "name", index: "name", width: "250"},
                    {name: "statusRegistration", index: "statusRegistration", width: "170"}
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
                height: 160,
                jsonReader: {
                    root: "notificationListReport",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                }
                //,
                //                    onSelectRow: function(id) {
                //                        var data = $(this).getRowData(id);
                //                        if(data.sel == 1) {
                //                            $(this).jqGrid('setRowData', id, true, {color: 'black'});
                //                        }
                //                        else {
                //                            $(this).jqGrid('setRowData', id, false, {color: 'black'});
                //                        }
                //
                //                    }
            }); //end of detail jqGrid                 

            $("#generate_button").bind("click", function (event) {
                //formatDateFields();
                event.preventDefault();
                event.stopPropagation();
                //console.log("Global Entity is: "+entity_global);
                url = "";
                url += "entity=" + entity_global;
                if ($('[name="reportFormat"]:checked').val() == "pdf") {
                    url = "Patient_Notification_list.action?" + url;
                    window.open(url);
                    return false;
                } else {
                    url += "&recordType=1&entity=" + entity_global;
                    url = "Converter_dispatch.action?" + url;
                    convertData();
                }
            });

            $("#close_button").bind("click", function (event) {
                window.location.href = "Home_page.action";
            });
        });

        var x = function wait() {
            window.open(url);
        }

        function convertData() {
            $("#messageBar").hide();
            $("#generate_button").attr("disabled", true);
            $.ajax({
                url: url,
                dataType: "json",
                success: function (fileName) {
                    $("#messageBar").html("Conversion Completed").slideDown('fast');
                    url = fileName;
                    window.setTimeout(x, 3000);
                    //$("#messageBar").html("Conversion Completed").fadeOut('slow');
                }
            });
            $("#generate_button").attr("disabled", false);
        }

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<div class="mt-5"></div>
<div class="content col-10 mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item active">Notifications</li>
    </ol>

    <div class="row">
        <div class="col-md-11 mr-auto ml-auto">
            <div class="card card-user">
                <div class="card-body">
                    <div id="loader"></div>
                    <div id="messageBar"></div>
                    <div class="card-title">Record Listing</div>
                    <div class="row">
                        <div class="col-md-12 mr-auto ml-auto">
                            <div class="table-responsive">
                                <table id="grid"></table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 mr-auto ml-auto">
                            <div class="table-responsive">
                                <table id="detail"></table>
                                <div id="pager" style="text-align:center;"></div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 mr-auto ml-auto">
                            <label><strong>Report format</strong></label>
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <div class="form-check-radio">
                                        <label class="form-check-label">
                                            <input type="radio" name="reportFormat" value="pdf" checked
                                                   class="form-check-input"/>
                                            <span class="form-check-sign"></span> <span
                                                style="margin-left: 20px"></span>Generate report in PDF format
                                        </label>
                                    </div>
                                </div>
                                <div class="col-md-6 form-group">
                                    <div class="form-check-radio">
                                        <label class="form-check-label">
                                            <input type="radio" name="reportFormat" value="cvs"
                                                   class="form-check-input"/>
                                            <span class="form-check-sign"></span><span style="margin-left: 20px"></span>Generate
                                            report and convert report to MS Excel
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <p><strong>This report format contains more client information than PDF</strong></p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <label class="pull-right">
                                <button id="generate_button" class="btn btn-info" disabled="true">Generate</button>
                                <button id="close_button" class="btn btn-defaultr">Close</button>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
