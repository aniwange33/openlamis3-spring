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
    <script type="text/javascript" src="/js/lamis/laboratory-common.js"></script>
    <script type="text/JavaScript">
        var labtestIds = [];
        var date = "";
        var lastSelectDate = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();
            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#labtestgrid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
            $.ajax({
                url: "/api/laboratory/" + $('#laboratoryId').val(),
                dataType: "json",
                success: function (res) {
                    var laboratoryList = res.laboratoryList;
                    $('#patientId').val(laboratoryList[0].patientId);
                    $('#facilityId').val(laboratoryList[0].facilityId);
                    populateForm(laboratoryList);
                }
            }); //end of ajax call           


            var lastSelected = -99;
            $("#grid").jqGrid({
                url: "/api/lab-test/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["Test Description", "Absolute", "Unit", "Relative", "Unit", "Comment", "Indication"],
                colModel: [
                    {name: "description", index: "description", width: "200"},
                    {
                        name: "resultab",
                        index: "resultab",
                        width: "100",
                        sortable: false,
                        editable: true,
                        edittype: "text"
                    },
                    {name: "measureab", index: "measureab", width: "80"},
                    {
                        name: "resultpc",
                        index: "resultpc",
                        width: "100",
                        sortable: false,
                        editable: true,
                        edittype: "text"
                    },
                    {name: "measurepc", index: "measurepc", width: "80"},
                    {
                        name: "comment",
                        index: "comment",
                        width: "200",
                        sortable: false,
                        editable: true,
                        edittype: "text"
                    },
                    {
                        name: "indication",
                        index: "indication",
                        width: "100",
                        hidden: true,
                        sortable: false,
                        editable: true,
                        edittype: "select",
                        editoptions: {value: " : ;Routine Monitoring:Routine Monitoring;Targeted Monitoring:Targeted Monitoring"}
                    },
                ],
                sortname: "labtestId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 200,
                jsonReader: {
                    root: "labresultList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "labtestId"
                },
                onSelectRow: function (id) {
                    if (id && id != lastSelected) {
                        if (id == 16) {
                            $("#grid").hideCol("comment");
                            $("#grid").showCol("indication");
                        } else {
                            $("#grid").showCol("comment");
                            $("#grid").hideCol("indication");
                        }
                        $("#grid").jqGrid('saveRow', lastSelected,
                            {
                                successfunc: function (response) {
                                    return true;
                                },
                                url: "Labresult_update.action"
                            })
                        lastSelected = id;
                    }
                    $("#testId").val(id);
                    $("#grid").jqGrid('editRow', id);
                } //end of onSelectRow
            }); //end of jqGrid                                
            $("#labtestgrid").jqGrid({
                url: "/api/lab-test/grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["", "", "Description"],
                colModel: [
                    {name: "labtestId", index: "labtestId", width: "15", hidden: true},
                    {name: "selectedLabTest", index: "selectedLabTest", width: "15", hidden: true},
                    {name: "description", index: "description", width: "360"}
                ],
                sortname: "labtestId",
                sortorder: "desc",
                viewrecords: true,
                rowNum: 100,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 100,
                jsonReader: {
                    root: "labtestList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "labtestId"
                },
                loadComplete: function (data) {
                    console.log(data);

                },
                onSelectRow: function (id) {
                    getLabTestComposition(id);
                }
            });

            $("#date1").bind("change", function (event) {
                checkDate();
            });
            $("#date2").bind("change", function (event) {
                checkDate();
            });

            $("#close_button").bind("click", function (event) {
                window.location = '/laboratory';
                return true;
            });
        });

        function checkDate() {
            if ($("#date1").val().length != 0 && $("#date2").val().length != 0) {
                if (parseInt(compare($("#date1").val(), $("#date2").val())) == -1) {
                    var message = "Date of result reported cannot be ealier than date of sample collection";
                    $("#messageBar").html(message).slideDown('slow');
                } else {
                    $("#messageBar").slideUp('slow');
                }
            }
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_laboratory.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item"><a href="/laboratory">Laboratory</a></li>
    <li class="breadcrumb-item active">Laboratory Test</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Hospital No</label>
                                <input name="hospitalNum" type="text" class="form-control" id="hospitalNum"
                                       readonly="readonly"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <p><br clear="both">
                                    <span id="patientInfor"></span>
                                </p>
                                <input name="name" type="hidden" id="name"/>
                                <input name="patientId" value="${patientId}" type="hidden" id="patientId"/>
                                <input name="laboratoryId" value="${laboratoryId}" type="hidden" id="laboratoryId"/>
                                <input name="facilityId" type="hidden" id="facilityId"/>
                                <input name="testId" type="hidden" id="testId"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of Sample Collection:</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date1" type="text" class="form-control" id="date1"/>
                                </div>
                            </div>
                            <input name="dateCollected" type="hidden" id="dateCollected"/></span>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date Result Reported</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date2" type="text" class="form-control" id="date2"/>
                                </div>
                            </div>
                            <input name="dateReported" type="hidden" id="dateReported"/>
                            <span id="dateHelp" class="errorspan"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Laboratory No</label>
                                <input name="labno" type="text" class="form-control" id="labno"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="input-group no-border col-md-6 pull-right">
                                        <input type="text" class="form-control  search" placeholder="Search...">
                                        <div class="input-group-append">
                                            <div class="input-group-text">
                                                <i class="now-ui-icons ui-1_zoom-bold"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="labtesttable" class="table-responsive">
                                <table id="labtestgrid" class="table"></table>
                            </div>
                        </div>
                        <input name="dateLastCd4" type="hidden" id="dateLastCd4"/>
                        <input name="dateLastViralLoad" type="hidden" id="dateLastViralLoad"/>
                        <input name="gender" type="hidden" id="gender"/>
                        <input name="dateBirth" type="hidden" id="dateBirth"/>
                        <input name="currentStatus" type="hidden" id="currentStatus"/>
                        <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/>
                    </div>
                    <br clear="both">
                    <div class="row">
                        <div class="col-md-12">
                            <h6 class="divider">Selected Test</h6>
                            <div class="table-responsive">
                                <table id="grid"></table>
                            </div>
                        </div>
                    </div>

                    <div id="userGroup" style="display: none">
                    </div>
                    <div id="fromLabTest" style="display: none">
                    </div>
                    <div class="pull-right">
                        <button id="save_button" type="button" class="btn btn-info">Save</button>
                        <a href="/laboratory" class="btn btn-default">Close</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
