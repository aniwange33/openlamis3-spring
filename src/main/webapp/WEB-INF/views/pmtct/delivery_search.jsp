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
            resetPage();
            initialize();
            reports();

            $("#grid").jqGrid({
                url: "/api/patient/grid?female=female",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Action", ""],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "130"},
                    {name: "name", index: "name", width: "290"},
                    {name: "gender", index: "gender", width: "100"},
                    {name: "currentStatus", index: "currentStatus", width: "190"},
                    {name: "address", index: "address", width: "290"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "100",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button type='button' class='btn btn-info btn-sm' onclick='newButton(" + cellValue + ")' data-toggle='tooltip' data-placement='left' title='New'>New </button>";
                        }
                    },
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "100", hidden: true},
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "hospitalNum",
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
                                url: "/api/delivery/patient/" + id,
                                page: 1
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "/api/delivery/patient/" + id,
                            page: 1
                        }).trigger("reloadGrid");
                    }

                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    // $("#new_button").removeAttr("disabled");
                    // $("#new_button").html("New");
                },
                ondblClickRow: function (id) {
                    window.location = '/pmtct/delivery/patient/' + id;
                }
            }); //end of master jqGrid                 

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Delivery", "Mode of Delivery", "Maternal Outcome", "Actions"],
                colModel: [
                    {
                        name: "dateDelivery",
                        index: "dateDelivery",
                        width: "205",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "modeDelivery", index: "modeDelivery", width: "344"},
                    {name: "maternalOutcome", index: "maternalOutcome", width: "280"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "280",
                        formatter: function (cellValue, options, rowObject) {
                            return '<div id="table-btn" class="dropdown" style="postion: absolute; color: #000;">' +
                                '<button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action</button><div class="dropdown-menu"><a class="dropdown-item" href="#">View</a><a class="dropdown-item" href="#">Edit</a><a class="dropdown-item" href="#">Delete</a></div></div>';
                        }
                    }
                ],
                rowNum: -1,
                sortname: "deliveryId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "deliveryList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "deliveryId"
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            var date = data.dateDelivery;
                            $("#dateDelivery").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                        }
                        $("#deliveryId").val(id);
                        // $("#new_button").html("View");
                    }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);
                        var date = data.dateDelivery;
                        $("#dateDelivery").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    }
                    $("#deliveryId").val(id);
                    window.location = '/pmtct/delivery/' + id;
                }
            }); //end of detail jqGrid                 

            $.ajax({
                url: "/api/patient/" + $('#patientId').val(),
                dataType: "json",
                success: function (res) {
                    var patientMap = res.patientMap[0];
                    if (!$.isEmptyObject(patientMap)) {
                        $("#id").val(patientMap.patientId);
                        $("#hospitalNum").val(patientMap.hospitalNum);
                        $("#name").val(patientMap.name);
                        //$("#new_button").removeAttr("disabled");
                    }
                },
                complete: function () {
                    $("#detail").setGridParam({
                        url: "/api/delivery/patient/" + $("#patientId").val(),
                        page: 1
                    }).trigger("reloadGrid");
                }
            });

            $("#close_button").click(function (event) {
                window.location = '/pmtct';
                return true;
            });

            $("#new_patient").bind("click", function (event) {
                window.location = '/pmtct/delivery/patient/' + $('#patientId').val();
                return true;
            });

        });

        function viewButton(data) {
            $("#id").val(data);
            window.location = '/pmtct/delivery/' + data;
            return true;
        }

        function newButton(data) {
            $("#id").val(data);
            window.location = '/pmtct/delivery/patient/' + data + '/new';
            return true;
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_pmtct.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item"><a href="/pmtct">PMTCT</a></li>
    <li class="breadcrumb-item active">Labour & Delivery</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <input name="deliveryId" value="${deliveryId}" type="hidden" id="deliveryId"/>
    <input name="patientId" type="hidden" value="${patientId}" id="patientId"/>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="card-title">
                                Cannot find patient record?
                                <button class="btn btn-info" id="new_patient">Register</button>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 mr-auto ml-auto">
                            <div class="input-group no-border col-3 pull-right">
                                <input type="text" class="form-control search" placeholder="Search...">
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

</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>