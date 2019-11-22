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
                url: "/api/patient/grid?female",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Action", ""],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "130"},
                    {name: "name", index: "name", width: "290"},
                    {name: "gender", index: "gender", width: "100"},
                    {name: "currentStatus", index: "currentStatus", width: "190"},
                    {name: "address", index: "address", width: "347"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "100",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button type='button' class='btn btn-info btn-sm' onclick='newButton(" + cellValue + ")' data-toggle='tooltip' data-placement='left' title='New'>New </button>";
                        }
                    },
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "100", hidden: true}
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "hospitalNum",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 200,
                postData: {gender: "Female"},
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
                                url: '/api/maternal-followup/patient/' + id,
                                page: 1
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "/api/maternal-followup/patient/" + id,
                            page: 1
                        }).trigger("reloadGrid");
                    }
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    //$("#new_button").removeAttr("disabled");
                    //$("#new_button").html("New")
                },
                ondblClickRow: function (id) {
                    window.location = '/pmtct/maternal-followup/patient/' + id + '/new';
                }
            }); //end of master jqGrid

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Visit", "CD4 Count", "Partner's HIV Status", "Syphilis Test Result", "Family Planning Method", "Action"],
                colModel: [
                    {
                        name: "dateVisit",
                        index: "dateVisit",
                        width: "130",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "cd4", index: "cd4", width: "125"},
                    {name: "partnerHivStatus", index: "partnerHivStatus", width: "200"},
                    {name: "syphilisTestResult", index: "syphilisTestResult", width: "210"},
                    {name: "familyPlanningMethod", index: "familyPlanningMethod", width: "240"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "250",
                        formatter: function (cellValue, options, rowObject) {
                            return '<div id="table-btn" class="dropdown" style="postion: absolute; color: #000;">' +
                                '<button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action</button>' +
                                '<div class="dropdown-menu"><a class="dropdown-item" href="#">View</a>' +
                                '<a class="dropdown-item" href="#">Edit</a><a class="dropdown-item" href="#">Delete</a></div></div>';
                        }
                    }
                ],
                rowNum: -1,
                sortname: "maternalfollowupId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "maternalfollowupList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "maternalfollowupId"
                },
                afterInsertRow: function (id) {
                    var content = $("#detail").getCell(id, "syphilisTestResult");
                    $("#detail").setCell(id, "syphilisTestResult", content.substring(content.indexOf("-") + 2), '', '');

                    content = $("#detail").getCell(id, "familyPlanningMethod");
                    $("#detail").setCell(id, "familyPlanningMethod", content.substring(content.indexOf("-") + 2), '', '');
//                    onSelectRow: function(id) {
//                        if(id != null) {
//                            var selectedRow = $("#detail").getGridParam('selrow');
//                            if(selectedRow != null) {
//                                var data = $("#detail").getRowData(selectedRow);
//                                var date = data.dateVisit;
//                                $("#dateVisit").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
//                            }
//                            $("#maternalfollowupId").val(id);
//                            $("#new_button").html("View");
//                        }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);
                        var date = data.dateVisit;
                        $("#dateVisit").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    }
                    window.location = '/pmtct/maternal-followup/' + id;
                }
            }); //end of detail jqGrid

            $.ajax({
                url: '/api/patient/' + $('#patientId').val(),
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
                        url: "/api/maternal-followup/patient/" + $("#id").val(),
                        page: 1
                    }).trigger("reloadGrid");
                }
            });
            $("#new_button").click(function (event) {
                window.location= '/pmtct/maternal-followup/patient/' + $('#patientId').val() + '/new';
                return true;
            });
            $("#view_button").click(function (event) {
                window.location= '/pmtct/maternal-followup/' + $('#maternalfollowupId').val();
                return true;
            });
            $("#close_button").click(function (event) {
                window.location= '/pmtct';
                return true;
            });
        });

        function viewButton(data) {
            $("#id").val(data);
            window.location= '/pmtct/maternal-followup/' + data;

            return true;
        }

        function newButton(data) {
            window.location = '/pmtct/maternal-followup/patient/' + data + '/new';
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
    <li class="breadcrumb-item"><a href="/pmct">PMTCT</a></li>
    <li class="breadcrumb-item active">ANC /PNC Visit</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <input name="dateVisit" type="hidden" id="dateVisit"/>
    <input name="maternalfollowupId" value="${maternalfollowupId}" type="hidden" id="maternalfollowupId"/>
    <input name="patientId" value="${patientId}" type="hidden" id="patientId"/>
    <input name="facilityId" id="facilityId" type="hidden"/>
    <input name="hospitalNum" type="hidden" class="form-control" id="hospitalNum"/>
    <input name="name" type="hidden" class="form-control" id="name"/>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <div class="row">
                        <div class="col-md-12 mr-auto ml-auto">
                            <div class="input-group no-border col-md-3 pull-right">
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

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>