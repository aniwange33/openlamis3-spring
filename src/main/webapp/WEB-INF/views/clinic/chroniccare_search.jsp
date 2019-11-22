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
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
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
                url: "Patient_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Action", ""],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "200"},
                    {name: "name", index: "name", width: "250"},
                    {name: "gender", index: "gender", width: "250"},
                    {name: "currentStatus", index: "currentStatus", width: "220"},
                    {name: "address", index: "address", width: "300"},
                    {name: "dateCurrentStatus", index: "dateCurrentStatus", width: "200", hidden: true},

                    {
                        name: "Action", index: "Action", width: "150", formatter: function () {
                            return "<button id='new_button' onclick='newAppointment();'  class='btn btn-sm btn-info'>New</button>";

                        }
                    },
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
                                url: "Chroniccare_grid.action?q=1&id=" + id,
                                page: 1
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "Chroniccare_grid.action?q=1&id=" + id,
                            page: 1
                        }).trigger("reloadGrid");
                    }
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#currentStatus").val(data.currentStatus);
                    $("#dateCurrentStatus").val(data.dateCurrentStatus);
                    $("#new_button").removeAttr("disabled");
                    $("#new_button").html("New");
                },
                ondblClickRow: function (id) {
                    $("#lamisform").attr("action", "Chroniccare_new");
                    $("#lamisform").submit();
                }
            }); //end of master jqGrid

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Visit", "ART Status", "Clinical Stage", "Last CD4 Result", "Last Viral Load Result", "Action", ""],
                colModel: [
                    {
                        name: "dateVisit",
                        index: "date1",
                        width: "255",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "currentStatus", index: "currentStatus", width: "250"},
                    {name: "clinicStage", index: "clinicStage", width: "200"},
                    {name: "lastCd4", index: "lastCd4", width: "250"},
                    {name: "lastViralLoad", index: "lastViralLoad", width: "220"},

                    {
                        name: "Action", index: "Action", width: "200", formatter: function () {
                            return "<button id='view_button' class='btn btn-sm btn-info'>View</button><button class='btn btn-success btn-sm' id='edit_button'>Edit</button><button id='delete_button' class='btn btn-danger btn-sm'>Delete</button>";
                        }
                    },

                    {name: "dateVisit", index: "dateVisit", width: "250", hidden: true},


                ],
                rowNum: -1,
                sortname: "chroniccareId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "chroniccareList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "chroniccareId",
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            $("#dateVisit").val(data.dateVisit);
                        }
                        $("#chroniccareId").val(id);
                        $("#new_button").html("View");
                    }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);
                        $("#dateVisit").val(data.dateVisit);
                    }
                    $("#chroniccareId").val(id);
                    $("#lamisform").attr("action", "Chroniccare_find");
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
                        url: "Chroniccare_grid.action?q=1&id=" + $("#id").val(),
                        page: 1
                    }).trigger("reloadGrid");
                }
            });

            $("#new_button").bind("click", function (event) {
                if ($("#new_button").html() === "New") {
                    $("#lamisform").attr("action", "Chroniccare_new");
                    return true;
                } else if ($("#new_button").html() === "View") {
                    $("#lamisform").attr("action", "Chroniccare_find");
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
        <li class="breadcrumb-item"><a href="/Clinic_page">ART Clinic</a></li>
        <li class="breadcrumb-item active" aria-current="page">Clinic Data Page</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <!-- <div class="card-header bg-info">
            <h5 class="card-title text-white">Care & Support Assessment</h5>
            </div>  -->
            <s:form id="lamisform" theme="css_xhtml">
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
                    <!--<div class="btn-group pull-left">
                   <button id="new_button" class="btn btn-info" disabled="true">New</button>
                   <button id="close_button" class="btn btn-default">Close</button>-->
                    <div class="table-responsive">
                        <table id="grid" class="table table-striped table-bordered center"></table>
                    </div>
                    <div class="table-responsive">
                        <table id="detail" class="table table-striped table-bordered center"></table>
                    </div>
                    <div id="user_group" style="display: none">Clinician</div>
                    </s:form>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
