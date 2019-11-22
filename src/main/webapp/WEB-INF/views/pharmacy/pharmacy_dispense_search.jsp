<%-- 
    Document   : pharmacy_dispense_search
    Created on : Jan 19, 2018, 5:02:32 PM
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
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/lamis/prescribed_drugs.js"></script>

    <script type="text/JavaScript">
        var gridNum = 5;
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
            getPrescribedDrugs();

            $("#grid").jqGrid({
                url: "Patient_drug_dispense_grid.action",
                datatype: "json",
                type: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Action"],
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
                            return "<button class='btn btn-info btn-sm' onclick='newButton(" + cellValue + ")'  title='Dispense'>Dispense</button>";
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
                }, onSelectRow: function (id) {
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#new_button").removeAttr("disabled");
                    $("#new_button").attr("src", "images/new_button.jpg");
                },
                ondblClickRow: function (id) {
                    $("#lamisform").attr("action", "Pharmacy_prescription_new");
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
                    //$("#detail").setGridParam({url: "Pharmacy_grid.action?q=1&id="+$("#id").val(), page:1}).trigger("reloadGrid");
                }
            });
            $("#close_button").click(function (event) {
                $("#lamisform").attr("action", "Pharmacy_page");
                return true;
            });
        });

        function newButton(patientId) {
            $("#id").val(patientId);
            var data = $("#grid").getRowData(patientId);
            $("#hospitalNum").val(data.hospitalNum);

            $("#lamisform").attr("action", "Pharmacy_prescription_new");
            $("#lamisform").submit();
            return true;
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_pharmacy.jsp"/>
<!-- MAIN CONTENT -->

<form id="lamisform" theme="css_xhtml">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item"><a href="Pharmacy_page">Pharmacy</a></li>
        <li class="breadcrumb-item active">Drug Prescriptions</li>
    </ol>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        Error messages notification!
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="input-group no-border col-3 pull-right">
                                <input type="text" class="form-control  search" placeholder="Search...">
                                <div class="input-group-append">
                                    <div class="input-group-text">
                                        <i class="now-ui-icons ui-1_zoom-bold"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--                                <div class="col-6">
                                                            <div class="btn-group pull-right">
                                                                <button class='btn btn-info' id='new_button' data-toggle='tooltip' data-placement='left'
                                                                        title='New Drug Prescription'>
                                                                    <i class='now-ui-icons ui-1_simple-add'></i> New
                                                                </button>
                                                            </div>
                                                        </div>-->
                    </div>
                    <div class="row">
                        <div class="col-12 mr-auto ml-auto">
                            <div class="table-responsive">
                                <table id="grid" class="table table-bordered table-striped table-hover"></table>
                                <div id="pager" style="text-align:center;"></div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 mr-auto ml-auto">
                            <div class="table-responsive">
                                <table id="detail"></table>
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
