<%-- 
    Document   : Appointment
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 2.6</title>
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
        var gridNum = 7;
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
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Action"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "170"},
                    {name: "name", index: "name", width: "250"},
                    {name: "gender", index: "gender", width: "180"},
                    {name: "currentStatus", index: "currentStatus", width: "300"},
                    {name: "address", index: "address", width: "350"},
                    // {name: "action", index: "hospitalNum", width: "130", formatter:function(cellvalue, options, rowObject){return"<button class='btn btn-info new_button'>new</button>";
                    // }},

                    {
                        name: "Action", index: "Action", width: "150", formatter: function () {
                            return "<button onclick='newAppointment();'  class='btn btn-sm btn-info'>New</button>";

                        }
                    },

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
                onSelectRow: function (id) {
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name)
                    $("#new_button").removeAttr("disabled");
                },
                ondblClickRow: function (id) {
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name)
                    $("#lamisform").attr("action", "Status_new");
                    $("#lamisform").submit();
                }
            }); //end of jqGrid

            $("#new_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Status_new");
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
<div class="wrapper">
    <jsp:include page="/WEB-INF/views/template/header.jsp"/>
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Client Status Update</li>
        </ol>
    </nav>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-header bg-info">
                    <h5 class="card-title text-white"></h5>
                </div>
                <form id="lamisform" theme="css_xhtml">
                    <div class="card-body">
                        <div id="messageBar"></div>
                        <div class="row">
                            <div class="col-12 ml-auto mr-auto">
                                <div class="input-group no-border col-md-3 pull-right">
                                    <input type="text" class="form-control search" placeholder="search...">
                                    <div class="input-group-append">
                                        <div class="input-group-text">
                                            <i class="now-ui-icons ui-1_zoom-bold"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="table-responsive">
                                    <table id="grid" class="table table-striped table-bordered center"></table>
                                    <div id="pager" style="text-align:center;"></div>
                                    <!--<div class="pull-right">
                                   <button id="new_button" class="btn btn-info" disabled="true">New</button>
                               <button id="close_button" class="btn btn-default">Close</button>-->
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
