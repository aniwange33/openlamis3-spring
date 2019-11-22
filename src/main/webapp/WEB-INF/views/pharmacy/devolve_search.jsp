<%-- 
Document   : Differnetiated Care Service
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
    <script type="text/javascript" src="assets/jqueryui/jquery-ui.js"></script>
    <script type="text/javascript" src="assets/jqueryui/jqgrid/plugins/ui.multiselect.js"></script>
    <script type="text/javascript" src="js/lamis/prescribed_drugs.js"></script>
    <script type="text/JavaScript">
        var gridNum = 4;
        var enablePadding = true;
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

            $("#grid").jqGrid({
                url: "Patient_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Action", ""],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "130"},
                    {name: "name", index: "name", width: "287"},
                    {name: "gender", index: "gender", width: "100"},
                    {name: "currentStatus", index: "currentStatus", width: "190"},
                    {name: "address", index: "address", width: "345"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "100",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button class='btn btn-info btn-sm' onclick='newButton(" + cellValue + ")' data-toggle='tooltip' data-placement='left' title='New'>New </button>";
                        }
                    },
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "100", hidden: true},
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "Name",
                sortorder: "asc",
                viewrecords: true,
                imgpath: "assets/jqueryui/jqgrid/img/",
                resizable: false,
                height: 200,
                jsonReader: {
                    root: "patientList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                },
                onSelectRow: function (id) {
                    if (id == null) {
                        id = 0;
                        if ($("#detail").getRecords() > 0) {
                            $("#detail").setGridParam({
                                url: "Devolve_grid.action?q=1&id=" + id,
                                page: 1
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "Devolve_grid.action?q=1&id=" + id,
                            page: 1
                        }).trigger("reloadGrid");
                    }
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#currentStatus").val(data.currentStatus);
                    $("#dateCurrentStatus").val(data.dateCurrentStatus);
                    // $("#new_button").removeAttr("disabled");
                    // $("#new_button").html("New");
                },
//            ondblClickRow: function(id) {
//            $("#lamisform").attr("action", "Devolve_new");
//            $("#lamisform").submit();
//            }
            }); //end of master jqGrid                 

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Devolved", "Viral Load", "CD4 Count", "Clinic Stage", "Regimen Dispensed", "Date of Next Visit", "Actions", ""],
                colModel: [
                    {
                        name: "dateDevolved",
                        index: "date1",
                        width: "200",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "lastViralLoad", index: "lastViralLoad", width: "135"},
                    {name: "lastCd4", index: "lastCd4", width: "135"},
                    {name: "lastClinicStage", index: "lastClinicStage", width: "110"},
                    {name: "regimen", index: "regimen", width: "270"},
                    {
                        name: "dateNextClinic",
                        index: "dateNextClinic",
                        width: "180",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "devolveId",
                        index: "devolveId",
                        width: "120",
                        classes: "table_dropdown",
                        formatter: function (cellValue, options, rowObject) {
                            return '<div id="table-btn" class="dropdown" style="postion: absolute; color: #000;"><button  class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action</button><div class="dropdown-menu dropdown-menu-right"><a class="dropdown-item" href="#" onclick="viewButton(' + cellValue + ');">View</a><a class="dropdown-item" href="#" onclick="editButton(' + cellValue + ');">Edit</a><a class="dropdown-item" href="#" onclick="deleteButton(' + cellValue + ');">Delete</a></div></div>';
                        }
                    },
                    {name: "dateDevolved", index: "dateDevolved", width: "100", hidden: true},
                ],
                rowNum: -1,
                sortname: "devolveId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "devolveList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "devolveId"
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            $("#dateDevolved").val(data.dateDevolved);
                        }
                        $("#devolveId").val(id);
                        //$("#new_button").html("View");
                    }
                }
//            ondblClickRow: function(id) {
//            var selectedRow = $("#detail").getGridParam('selrow');
//            if(selectedRow != null) {
//            var data = $("#detail").getRowData(selectedRow);
//            $("#dateDevolved").val(data.dateDevolved);
//            }
//            $("#devolveId").val(id);
//            $("#lamisform").attr("action", "Devolve_find");
//            $("#lamisform").submit();
//            }
            }); //end of detail jqGrid                 

            $.ajax({
                url: "Patient_retrieve_detail.action",
                dataType: "json",
                success: function (patientMap) {
                    if (!$.isEmptyObject(patientMap)) {
                        $("#id").val(patientMap.patientId);
                        $("#hospitalNum").val(patientMap.hospitalNum);
                        $("#name").val(patientMap.name);
                        //  $("#new_button").removeAttr("disabled");
                    }
                },
                complete: function () {
                    $("#detail").setGridParam({
                        url: "Devolve_grid.action?q=1&id=" + $("#id").val(),
                        page: 1
                    }).trigger("reloadGrid");
                }
            });

            $("#close_button").click(function (event) {
                $("#lamisform").attr("action", "Devolve_search");
                return true;
            });
        });

        function viewButton(devolveId) {
            $("#devolveId").val(devolveId);
            var data = $("#detail").getRowData(devolveId);
            $("#dateDevolved").val(data.dateDevolved);
            $("#lamisform").attr("action", "Devolve_find?ionact=view");
            $("#lamisform").submit();

            return true;
        }

        function editButton(devolveId) {
            $("#devolveId").val(devolveId);
            var data = $("#detail").getRowData(devolveId);
            $("#dateDevolved").val(data.dateDevolved);
            $("#lamisform").attr("action", "Devolve_find?ionact=edit");
            $("#lamisform").submit();

            return true;
        }

        function deleteButton(devolveId) {
            $("#devolveId").val(devolveId);
            var data = $("#detail").getRowData(devolveId);
            $("#dateDevolved").val(data.dateDevolved);
            $.confirm({
                title: 'Confirm!',
                content: 'Are you sure you want to delete?',
                buttons: {
                    confirm: function () {
                        if ($("#userGroup").html() == "Data Analyst") {
                            $("#lamisform").attr("action", "Error_message");
                            $("#lamisform").submit();
                        } else {
                            $("#lamisform").attr("action", "Devolve_delete");
                            $("#lamisform").submit();
                        }
                        window.location.replace("/Devolve_search");
                        return true;
                    },
                    cancel: function () {
                        console.log("cancel");
                    }
                }
            });
        }

        function newButton(id) {
            $("#id").val(id);
            var data = $("#grid").getRowData(id)
            $("#hospitalNum").val(data.hospitalNum);
            $("#name").val(data.name);
            $("#currentStatus").val(data.currentStatus);
            $("#dateCurrentStatus").val(data.dateCurrentStatus);
            $("#lamisform").attr("action", "Devolve_new");
            $("#lamisform").submit();
        }

    </script>

</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_pharmacy.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item"><a href="Pharmacy_page">Pharmacy</a></li>
    <li class="breadcrumb-item active">Differentiated Care</li>
</ol>
<!-- MAIN CONTENT -->
<form id="lamisform" theme="css_xhtml">
    <input name="hospitalNum" type="hidden" id="hospitalNum"/>
    <input name="devolveId" type="hidden" id="devolveId"/>
    <input name="patientId" type="hidden" id="patientId"/>
    <input name="currentStatus" type="hidden" id="currentStatus"/>
    <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-12 mr-auto ml-auto">
                            <div class="input-group no-border col-md-3 pull-right">
                                <input type="text" class="form-control  search" placeholder="Search...">
                                <div class="input-group-append">
                                    <div class="input-group-text">
                                        <i class="now-ui-icons ui-1_zoom-bold"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="table-responsive">
                                <table id="grid" class="table table-bordered table-striped table-hover"></table>
                                <div id="pager" style="text-align:center;"></div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 mr-auto ml-auto">
                            <div class="table-responsive">
                                <table id="detail" class="table table-bordered table-striped table-hover"></table>
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