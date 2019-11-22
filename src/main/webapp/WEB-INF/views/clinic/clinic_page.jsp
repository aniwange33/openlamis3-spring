<%-- 
    Document   : Patient
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
        var gridNum = 1;
        var enablePadding = true;

        $(document).ready(function () {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "newestOnTop": false,
                "progressBar": false,
                "positionClass": "toast-bottom-right",
                "preventDuplicates": false,
                "showDuration": "30000",
                "hideDuration": "1000",
                "timeOut": "30000",
                "extendedTimeOut": "3000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut",
                "onclick": function () {
                    window.location.href = "Labtest_prescription_search.action";
                }
            }

            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#grid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });

            $.ajax({
                url: "Labtests_prescribed.action",
                dataType: "json",
                method: "POST",
                beforeSend: function () {
                    //console.log("here!");
                },
                success: function (response) {
                    labtests = response.notificationListCount[0].labtests;

                    var warning = "";
                    var i = 0;
                    var tracked = false;

                    if (typeof labtests != "undefined" && labtests != 0) {
                        warning += "<strong>" + labtests + "</strong> clients with laboratory test order <br/>";
                        tracked = true;
                    }
                    if (tracked == true) {
                        toastr.warning(warning, "LabTest Order Notifications");
                    }
                }
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

            $("#grid").jqGrid({
                url: "/api/patient/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Date Modified", "Action"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "150"},
                    {name: "name", index: "name", width: "230"},
                    {name: "gender", index: "gender", width: "80"},
                    {name: "currentStatus", index: "currentStatus", width: "180"},
                    {name: "address", index: "address", width: "270"},
                    {
                        name: "timeStamp",
                        index: "timeStamp",
                        width: "150",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
                    },
                    {
                        name: 'action', index: 'action', width: "95", sortable: false, jsonmap: "patientId",
                        formatter: function (cellvalue, options, rowObject) {
                            return "<input type='button' class='btn btn-info btn-sm' value='Details' onclick='history(\"" + cellvalue + "\")'\>";
                        }
                    }
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "timeStamp",
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
                //                    loadComplete: function() {
                //                        var grid = $("#grid"),
                //                        ids = grid.getDataIDs();
                //                        for (var i = 0; i < ids.length; i++) {
                //                            grid.setRowData(ids[i], false, { height : 35});
                //                        }
                //                        // grid.setGridHeight('auto');
                //                    },
//            onSelectRow: function(id) {
//            $("#id").val(id);
//            var data = $("#grid").getRowData(id);
//            $("#hospitalNum").val(data.hospitalNum);
//            $("#name").val(data.name);
//            $("#new_button").html("View");
//            },               
//            ondblClickRow: function(id) {
//            $("#id").val(id);
//            var data = $("#grid").getRowData(id);
//            $("#hospitalNum").val(data.hospitalNum);
//            $("#name").val(data.name); 
//            $("#lamisform").attr("action", "Patient_find");
//            $("#lamisform").submit();
//            }
            }); //end of jqGrid                 

//            $("#new_button").bind("click", function(event){
//            if($("#new_button").html() === "New"){
//            $("#lamisform").attr("action", "Patient_new");  
//            return true; 
//            }else if($("#new_button").html() === "View"){
//            $("#lamisform").attr("action", "Patient_find");
//            $("#lamisform").submit();
//            return true;
//            }		
//            });                
//            $("#close_button").bind("click", function(event){
//            $("#lamisform").attr("action", "Home_page");
//            return true;
//            });
        });

        function history(id) {
            //                console.log(id);
            //                var data = $("#grid").getRowData(id);
            //                $("#id").val(id);
            //                $("#hospitalNum").val(id);
            //                console.log(data);
            //                $("#lamisform").attr("action", "Patient_find");
            //                $("#lamisform").submit();
            //                return true;
            window.location.href = "/event-page/patient/" + id;

            // $("#lamisform").attr("action", "Patient_find?id=" + id);
            //  return true;
        }
    </script>
</head>

<body>
<!-- Navbar -->
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item active">Clinic</li>
</ol>

<!-- Summary Section -->
<form id="lamisform" theme="css_xhtml">
    <input name="patientId" type="hidden" id="patientId"/>
    <input name="hospitalNum" type="hidden" class="inputboxes" id="hospitalNum"/>
    <div id="messageBar"></div>
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="input-group no-border col-3 pull-right">
                                <input type="text" class="form-control  search" placeholder="Search...">
                                <div class="input-group-append">
                                    <div class="input-group-text">
                                        <i class="now-ui-icons ui-1_zoom-bold"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="table-responsive">
                                <table id="grid" class="table table-sm table-bordered table-striped"></table>
                                <div id="pager" style="text-align:center;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<div id="user_group1" style="display: none">
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>