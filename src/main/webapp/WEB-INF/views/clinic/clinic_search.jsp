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
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
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
                url: "/api/patient/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Date Modified", "Action"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "200"},
                    {name: "name", index: "name", width: "300"},
                    {name: "gender", index: "gender", width: "200"},
                    {name: "currentStatus", index: "currentStatus", width: "200"},
                    {name: "address", index: "address", width: "200"},
                    {
                        name: "patientId", index: "patientId", width: "180", formatter: function () {
                            return "<button id='new_button'  onclick='newClinic();' class='btn btn-sm btn-info'>New</button>";
                        }
                    },
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "250", hidden: true},
                ],

                pager: $('#pager'),
                rowNum: 100,
                sortname: "timeStamp",
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
                                url: "/api/clinic/grid?page=1&id=" + id,
                                page: 1,
                                rows: 100
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "/api/clinic/grid?page=1&id=" + id,
                            page: 1,
                            rows: 100
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
                ondblClickRow: function (id) {
                    $("#lamisform").attr("action", "/clinic/new/"+id);
                    $("#lamisform").submit();
                }
            }); //end of master jqGrid

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Visit", "Clinic Stage", "Functional Status", "TB Status", "Next Clinic Visit", "Action", "", ""],
                colModel: [
                    {
                        name: "dateVisit",
                        index: "date1",
                        width: "150",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "clinicStage", index: "clinicStage", width: "200"},
                    {name: "funcStatus", index: "funcStatus", width: "250"},
                    {name: "tbStatus", index: "tbStatus", width: "300"},
                    {
                        name: "nextAppointment",
                        index: "nextAppointment",
                        width: "255",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "patientId", index: "patientId", width: "240", formatter: function () {
                            return "<button id='view_button' class='btn btn-sm btn-info'>View</button><button class='btn btn-success btn-sm' id='edit_button'>Edit</button><button id='delete_button' class='btn btn-danger btn-sm'>Delete</button>";
                        }
                    },
                    {name: "dateVisit", index: "dateVisit", width: "300", hidden: true},
                    {name: "commence", index: "commence", width: "1", hidden: true}
                ],
                rowNum: -1,
                sortname: "clinicId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "clinicList",
                    page: "currpage",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "clinicId"
                },
                afterInsertRow: function (id, data) {
                    if (data.commence == "1") {
                        $(this).jqGrid('setRowData', id, false, {color: 'red'});
                    }
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {

                            var data = $("#detail").getRowData(selectedRow);
                            $("#dateVisit").val(data.dateVisit);
                        }
                        $("#clinicId").val(id);
                        $("#new_button").html("View");
                    }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);
                        //$("#dateVisit").val(data.dateVisit);
                    }
                    //$("#clinicId").val(id);
                    $("#lamisform").attr("action", "/clinic/edit/" + id);
                    $("#lamisform").submit();
                }
            }); //end of detail jqGrid

            $.ajax({
                url: "/api/patient/grid",
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
                        url: "/api/clinic/grid?page=1&id=" + $("#id").val(),
                        page: 1,
                        rows: 100
                    }).trigger("reloadGrid");
                }
            });

            $("#new_button").bind("click", function (event) {
                if ($("#new_button").html() === "New") {
                    $("#lamisform").attr("action", "/clinic/new");
                    return true;
                } else if ($("#new_button").html() === "View") {
                    $("#lamisform").attr("action", "/api/clnic/find");
                    $("#lamisform").submit();
                    return true;
                }
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Event_page?hno=");
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
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/home">ART Clinic</a></li>
        <li class="breadcrumb-item active" aria-current="page">Client Visit</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <!--  <div class="card-header bg-info">
                 <h5 class="card-title text-white">Client Visit</h5>
             </div>  -->
            <form id="lamisform" theme="css_xhtml">
                <input name="patientId" id="patientId" type="hidden">
                <input name="clinicId" id="clinicId" type="hidden">
                <input name="hospitalNum" id="hospitalNum" type="hidden">
                <input name="currentStatus" id="currentstatus" type="hidden">
                <input name="dateCurrentStatus" id="dateCurrentStatus" type="hidden">
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
                        </div>
                    </div>
                    <div class="table-responsive">
                        <table id="grid" class="table table-striped table-bordered center"></table>
                        <div id="pager" style="text-align:center;"></div>
                        <p></p>
                        <div class="row">
                            <div class="col-12">
                                <div class="tables-responsive">
                                    <table id="detail" class="table table-stripped table-stripped"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

        </form>
    </div>
</div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>

</html>
