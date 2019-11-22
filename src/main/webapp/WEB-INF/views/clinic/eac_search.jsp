<%-- 
    Document   : Clini
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <!-- <link type="text/css" rel="stylesheet" href="css/lamis.css" /> -->
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!-- <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />

    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css" />
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css" />
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css" /> -->

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <!--<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>-->
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <!--<script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>-->
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/JavaScript">
        var gridNum = 4;
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
                url: "Patient_grid.action?unsuppressed",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", ""],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "200"},
                    {name: "name", index: "name", width: "280"},
                    {name: "gender", index: "gender", width: "250"},
                    {name: "currentStatus", index: "currentStatus", width: "280"},
                    {name: "address", index: "address", width: "350"},
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "250", hidden: true},
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "Name",
                sortorder: "asc",
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
                    if (id == null) {
                        id = 0;
                        if ($("#detail").getRecords() > 0) {
                            $("#detail").setGridParam({
                                url: "Eac_grid.action?q=1&id=" + id,
                                page: 1
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "Eac_grid.action?q=1&id=" + id,
                            page: 1
                        }).trigger("reloadGrid");
                    }
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#new_button").removeAttr("disabled");
                    $("#new_button").html("New");
                },
                ondblClickRow: function (id) {
                    $("#lamisform").attr("action", "Eac_new");
                    $("#lamisform").submit();
                }
            }); //end of master jqGrid

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of 1st EAC", "Date of 2nd EAC", "Date of 3rd EAC", "Date Repeat VL Sample collected", ""],
                colModel: [
                    {
                        name: "dateVisit",
                        index: "date1",
                        width: "350",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "dateEac1",
                        index: "dateEac1",
                        width: "280",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "dateEac2",
                        index: "dateEac2",
                        width: "350",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "dateSampleCollected",
                        index: "dateSampleCollected",
                        width: "350",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "dateVisit", index: "dateVisit", width: "100", hidden: true}
                ],
                rowNum: -1,
                sortname: "eacId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "eacList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "eacId"
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            $("#dateVisit").val(data.dateVisit);
                        }
                        $("#eacId").val(id);
                        $("#new_button").html("View");
                    }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);
                        $("#dateVisit").val(data.dateVisit);
                    }
                    $("#eacId").val(id);
                    $("#lamisform").attr("action", "Eac_find");
                    $("#lamisform").submit();
                }
            }); //end of detail jqGrid

            $.ajax({
                url: "Patient_retrieve_detail.action",
                dataType: "json",
                success: function (patientMap) {
                    if (!$.isEmptyObject(patientMap)) {
                        $("#id").val(patientMap.patientId);
                        $("#hospitalNum").val(patientMap.hospitalNum);
                        $("#name").val(patientMap.name);
                        $("#new_button").removeAttr("disabled");
                    }
                },
                complete: function () {
                    $("#detail").setGridParam({
                        url: "Eac_grid.action?q=1&id=" + $("#id").val(),
                        page: 1
                    }).trigger("reloadGrid");
                }
            });

            $("#new_button").bind("click", function (event) {
                if ($("#new_button").html() === "New") {
                    $("#lamisform").attr("action", "Eac_new");
                    return true;
                } else if ($("#new_button").html() === "View") {
                    $("#lamisform").attr("action", "Eac_find");
                    $("#lamisform").submit();
                    return true;
                }
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
        <li class="breadcrumb-item"><a href="Clinic_page">ART Clinic</a></li>
        <li class="breadcrumb-item active" aria-current="page">Unsuppressed Client Monitoring/EAC</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <!--  <div class="card-header bg-info">
            <h5 class="card-title text-white">Unsuppressed Client Monitoring/Enhanced Adherence Counseling Assessment</h5>
             </div>  -->
            <form id="lamisform" theme="css_xhtml">
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
                        <div id="messageBar"></div>
                        <div class="table-responsive">
                            <table id="grid" class="table table-striped tabled-border"></table>
                            <div id="pager" style="text-align:center;">
                            </div>
                        </div>
                    </div>
                    <table id="detail" class="table table-striped tabled-border"></table>
                    <div id="pager" style="text-align:center;">
                    </div>
                </div>
                </table>
                <div id="buttons" style="width: 200px">
                    <div class="pull-right">
                        <button id="new_button" class="btn btn-info" disabled="true">New</button>
                        <button id="close_button" class="btn btn-default">Close</button>
                    </div>
                </div>
        </div>
        </form>
        <div id="user_group" style="display: none;">Clinician</div>
        <div id="footer">
            <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
        </div>
</body>
</html>
