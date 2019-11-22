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
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/lamis/status-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
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
            sessionStorage.clear();

            $("#loader_client").html('<img id="loader_image" src="images/loader_small.gif" />');

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
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Action"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "170"},
                    {name: "name", index: "name", width: "250"},
                    {name: "gender", index: "gender", width: "120"},
                    {name: "currentStatus", index: "currentStatus", width: "200"},
                    {name: "address", index: "address", width: "350"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "100",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button onclick='newButton(" + cellValue + ");'  class='btn btn-sm btn-info'>New</button>";
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
                                url: "/api/status/grid?q=1&id=" + id,
                                page: 1
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "/api/status/grid?q=1&id=" + id,
                            page: 1
                        }).trigger("reloadGrid");
                    }

                    $("#id").val(id);
                    var data = $("#grid").getRowData(id);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#new_button").html("New");
                    $("#new_button").removeAttr("disabled");
                    sessionStorage.setItem("patientId", $("#id").val());
                },
                ondblClickRow: function (id) {
                    $("#lamisform").attr("action", "Status_new");
                    $("#lamisform").submit();
                }
            }); //end of jqGrid

            // status history
            $("#detail").jqGrid({
                url: "/api/status/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["ART Status", "Date of Status", "Tracking Outcome", "Date Tracked", "Action", "", "", "", ""],
                colModel: [
                    {name: "currentStatus", index: "currentStatus", width: "260"},
                    {
                        name: "dateCurrentStatus",
                        index: "date",
                        width: "200",
                        align: 'center',
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "outcome", index: "outcome", width: "250"},
                    {
                        name: "dateTracked",
                        index: "dateTracked",
                        width: "250",
                        align: 'center',
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "status", index: "status", width: "100"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "130",
                        classes: "table_dropdown",
                        formatter: function (cellValue, options, rowObject) {
                            return '<div id="table-btn" class="dropdown" style="postion: absolute; color: #000;"><button  class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action</button><div class="dropdown-menu dropdown-menu-right"><a class="dropdown-item" href="#" onclick="editButton(' + cellValue + ');">Edit</a><a class="dropdown-item" href="#" onclick="deleteButton(' + cellValue + ');">Delete</a></div></div>';
                        }
                    },
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "100", hidden: true},
                    {name: "historyId", index: "historyId", width: "100", hidden: true},
                    {name: "deletable", index: "deletable", width: "5", hidden: true},
                ],
                rowNum: -1,
                sortname: "dateCurrentStatus",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 100,
                jsonReader: {
                    root: "statusList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "historyId"
                },
                loadComplete: function () {
                    var gridIds = $("#detail").getDataIDs();
                    for (i = 0; i < gridIds.length; i++) {
                        var data = $("#detail").getRowData(gridIds[i]);
                        if (data.deletable == 0) {
                            $("#detail").jqGrid('setCell', i + 1, 'status', '', {background: '#ff9933'});
                        } else {
                            $("#detail").jqGrid('setCell', i + 1, 'status', '', {background: '#ccff33'});
                        }
                    }
                },
                onSelectRow: function (id) {
                    var data = $("#detail").getRowData(id);
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            $("#dateCurrentStatus").val(data.dateCurrentStatus);
                        }
                        $("#historyId").val(id);
                        $("#new_button").html("View");
                        sessionStorage.setItem("historyId", $("#historyId").val());
                        sessionStorage.setItem("dateCurrentStatus", $("#dateCurrentStatus").val());
                    }
                },
                ondblClickRow: function (id) {
                    $("#historyId").val(id);

                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);
                        $("#dateCurrentStatus").val(data.dateCurrentStatus);
                    }
                    sessionStorage.setItem("historyId", $("#historyId").val());
                    sessionStorage.setItem("dateCurrentStatus", $("#dateCurrentStatus").val());

                    $("#lamisform").attr("action", "Status_find");
                    $("#lamisform").submit();
                }
            }); //end of jqGrid

            $("#new_button").bind("click", function (event) {
                if ($("#new_button").html == 'View') {
                    $("#lamisform").attr("action", "Status_find");
                    return true;
                } else {
                    $("#lamisform").attr("action", "Status_new");
                    return true;
                }
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Clinic_page");
                return true;
            });
        });

        function deleteButton(patId) {
            $.confirm({
                title: 'Confirm!',
                content: 'Are you sure you want to delete?',
                buttons: {
                    confirm: function () {
                        $("#id").val(patId);
                        var data = $("#grid").getRowData(patId);
                        $("#hospitalNum").val(data.hospitalNum);
                        $("#name").val(data.name);
                        if ($("#userGroup").html() == "Data Analyst") {
                            $("#lamisform").attr("action", "Error_message");
                            $("#lamisform").submit();
                        } else {
                            $("#lamisform").attr("action", "Patient_delete");
                            $("#lamisform").submit();
                        }
                        window.location.replace("Patient_search");
                        return true;
                    },
                    cancel: function () {
                        console.log("cancel");
                    }
                }
            });
        }

        function editButton(patId) {
            $("#id").val(patId);
            var data = $("#grid").getRowData(patId);
            $("#hospitalNum").val(data.hospitalNum);
            $("#name").val(data.name);
            $("#lamisform").attr("action", "Patient_find");
            $("#lamisform").submit();
            //return true;
        }

        function newButton(patientId) {
            alert("..." + $("#id").val())
            $("#id").val(patientId);
            var data = $("#grid").getRowData(patientId);
            $("#hospitalNum").val(data.hospitalNum);
            $("#lamisform").attr("action", "Status_new");
            $("#lamisform").submit();
        }
    </script>
</head>

<body>
<div class="wrapper">
    <jsp:include page="/WEB-INF/views/template/header.jsp"/>
    <jsp:include page="/WEB-INF/views/template/nav_casemanagement.jsp"/>
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/home">Home</a></li>
            <li class="breadcrumb-item"><a href="/case-manager">Case Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Client Status Update</li>
        </ol>
    </nav>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <form id="lamisform" theme="css_xhtml">
                    <input name="hospitalNum" type="hidden" class="inputboxes" id="hospitalNum"/>
                    <input name="patientId" type="hidden" id="patientId"/>
                    <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/>
                    <input name="historyId" type="hidden" id="historyId"/>
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
                                </div>
                                <div class="table-responsive">
                                    <table id="detail" class="table table-striped table-bordered center"></table>
                                </div>
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
</div>
</body>
</html>
