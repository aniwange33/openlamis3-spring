<%-- 
    Document   : Commence
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>=
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/JavaScript">
        var gridNum = 3;
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

            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#grid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });

            $("#grid").jqGrid({
                url: "Patient_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Current CD4", "Last Clinic Stage", "ART Start Date", "Action", "", ""],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "150"},
                    {name: "name", index: "name", width: "250"},
                    {name: "gender", index: "gender", width: "150"},
                    {name: "currentStatus", index: "currentStatus", width: "200"},
                    {name: "lastCd4", index: "lastCd4", width: "150"},
                    {name: "lastClinicStage", index: "lastClinicStage", width: "200"},
                    {
                        name: "dateStarted",
                        index: "date1",
                        width: "150",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "Action", index: "Action", width: "150", formatter: function () {
                            return "<button id='new_button' onclick='newcommence();'  class='btn btn-sm btn-info'>New</button>";
                        }
                    },
                    {name: "dateStarted", index: "dateStarted", width: "100", hidden: true},
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "100", hidden: true},
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "patientId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 350,
                jsonReader: {
                    root: "patientList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                },
                afterInsertRow: function (id, data) {
                    //$(this).jqGrid('setRowData', id, false, {fontFamily: 'Arial, Helvetica, sans-serif'});
                    if (data.dateStarted != "") {
                        $(this).jqGrid('setRowData', id, false, {color: 'red'});
                    }
                },
                onSelectRow: function (id) {
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#currentStatus").val(data.currentStatus);
                    $("#dateCurrentStatus").val(data.dateCurrentStatus);
                    //if ART start date is empty enable new button
                    if (data.dateStarted == "") {
                        $("#new_button").removeAttr("disabled");
                    } else {
                        $("#new_button").attr("disabled", "disabled");
                    }
                },
                ondblClickRow: function (id) {
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#currentStatus").val(data.currentStatus);
                    $("#dateCurrentStatus").val(data.dateCurrentStatus);
                    //if ART start date is empty open a new commencement page, else find clinic visit with date of ART start

                    if (data.dateStarted == "") {
                        $("#lamisform").attr("action", "Commence_new");
                    } else {
                        $("#dateVisit").val(data.dateStarted);
                        $("#lamisform").attr("action", "Commence_find");
                    }
                    $("#lamisform").submit();
                }
            }); //end of jqGrid

            $("#new_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Commence_new");
                return true;
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Clinic_page");
                return true;
            });
        });
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="Casemanagement_page.action">Case Management</a></li>
        <li class="breadcrumb-item active" aria-current="page">ART Commencement</li>
    </ol>
</nav>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <div class="col-12 ml-auto mr-auto">
                        <div class="input-group no-border col-md-3 pull-right">
                            <input type="text" class="form-control search" placeholder="search...">
                            <div class="input-group-append">
                                <div class="input-group-text">
                                    <i class="now-ui-icons ui-1_zoom-bold"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--<button id="close_button" class="btn btn-default">Close</button>-->
                    <div class="table-responsive">
                        <table id="grid" class="table table-striped table-bordered center"></table>
                        <div id="pager" style="text-align:center;"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>

</html>