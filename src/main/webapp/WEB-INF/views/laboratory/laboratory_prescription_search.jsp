<%-- 
    Document   : laboratory_prescription_search
    Created on : Jan 22, 2018, 4:50:59 PM
    Author     : user10
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/JavaScript">
        var gridNum = 6;
        var enablePadding = true;
        $(document).ready(function () {
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
                url: "/patient/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Action"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "150"},
                    {name: "name", index: "name", width: "200"},
                    {name: "gender", index: "gender", width: "150"},
                    {name: "currentStatus", index: "currentStatus", width: "200"},
                    {name: "address", index: "address", width: "250"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "100",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button class='btn btn-info btn-sm' onclick='newButton(" + cellValue + ")' title='New'>New </button>";
                        }
                    }
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "patientId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
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
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#new_button").removeAttr("disabled");
                },
                ondblClickRow: function (id) {
                    $("#lamisform").attr("action", "Laboratory_prescription_new");
                    $("#lamisform").submit();
                }
            }); //end of master jqGrid

            $.ajax({
                url: "Patient_retrieve_detail.action",
                dataType: "json",
                success: function (patientMap) {
//                        console.log(patientMap);
                    if (!$.isEmptyObject(patientMap)) {
                        $("#id").val(patientMap.patientId);
                        $("#hospitalNum").val(patientMap.hospitalNum);
                        $("#name").val(patientMap.name);
                        $("#new_button").removeAttr("disabled");
                    }
                },
                complete: function () {
                    //$("#detail").setGridParam({url: "Laboratory_grid.action?q=1&id="+$("#id").val(), page:1}).trigger("reloadGrid");
                }
            });

            $("#new_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Laboratory_prescription_new");
                return true;
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Laboratory_page");
                return true;
            });
        });

        function newButton(data) {
            $("#id").val(data);
            var data2 = $("#grid").getRowData(data);
            $("#hospitalNum").val(data2.hospitalNum);
            $("#lamisform").attr("action", "Laboratory_new");
            return true;
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_laboratory.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item active">Laboratory Test Prescription</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <input name="laboratoryId" type="hidden" id="laboratoryId"/>
    <input name="patientId" type="hidden" id="patientId"/>
    <input name="dateReported" type="hidden" id="dateReported"/>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="input-group no-border col-md-3 pull-right">
                                <input type="text" class="form-control  search" placeholder="Search...">
                                <div class="input-group-append">
                                    <div class="input-group-text">
                                        <i class="now-ui-icons ui-1_zoom-bold"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table id="grid"></table>
                                <div id="pager" style="text-align:center;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
