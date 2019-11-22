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
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/lamis/indexcontact-common.js"></script>
    <script type="text/JavaScript">
        var gridNum = 4;
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
                url: "Hts_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Client Code", "Client Name", "Gender", "Date Tested", "HIV Test Result", "Referred to ART", "Referred to TB", "Referred to STI", "Action", ""],
                colModel: [
                    {name: "clientCode", index: "clientCode", width: "100"},
                    {name: "name", index: "name", width: "150"},
                    {name: "gender", index: "gender", width: "100"},
                    {
                        name: "dateVisit",
                        index: "date1",
                        width: "100",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "hivTestResult", index: "hivTestResult", width: "120"},
                    {name: "artReferred", index: "artReferred", width: "120"},
                    {name: "tbReferred", index: "tbReferred", width: "120"},
                    {name: "stiReferred", index: "stiReferred", width: "120"},
                    {
                        name: "htsId",
                        index: "htsId",
                        width: "150",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button class='btn btn-info btn-sm' onclick='newButton(" + cellValue + ")' data-toggle='tooltip' data-placement='left' title='New'>New </button>";
                        }
                    },
                    {name: "dateVisit", index: "dateVisit", width: "100", hidden: true},
                ],

                pager: $('#pager'),
                rowNum: 100,
                sortname: "htsId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 200,
                jsonReader: {
                    root: "htsList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "htsId"
                },
                onSelectRow: function (id) {
                    if (id == null) {
                        id = 0;

                        if ($("#detail").getRecords() > 0) {
                            $("#detail").setGridParam({
                                url: "Indexcontact_grid.action?q=1&htsId=" + id,
                                page: 1
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "Indexcontact_grid.action?q=1&htsId=" + id,
                            page: 1
                        }).trigger("reloadGrid");
                    }
                    $("#htsId").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#new_button").removeAttr("disabled");
                    $("#new_button").html("New");
                },
                ondblClickRow: function (id) {
                    $("#lamisform").attr("action", "Indexcontact_new");
                    $("#lamisform").submit();
                }
            }); //end of master jqGrid

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Name", "Age", "Gender", "Relationship", "Phone", "Action", ""],
                colModel: [
                    {name: "name", index: "name", width: "200"},
                    {name: "age", index: "age", width: "100"},
                    {name: "gender", index: "gender", width: "120"},
                    {name: "relationship", index: "relationship", width: "220"},
                    {name: "phone", index: "phone", width: "170"},
                    {
                        name: "indexcontactId",
                        index: "indexcontactId",
                        width: "280",
                        formatter: function (cellValue, options, rowObject) {
                            return '<div id="table-btn" class="dropdown" style="postion: absolute; color: #000;"><button  class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action</button><div class="dropdown-menu dropdown-menu-right"><a class="dropdown-item" href="#" onclick="editButton(' + cellValue + ');">Edit</a><a class="dropdown-item" href="#" onclick="deleteButton(' + cellValue + ');">Delete</a></div></div>';
                        }
                    },
                    {name: "htsId", index: "htsId", width: "150", hidden: true},
                ],
                rowNum: -1,
                sortname: "indexcontactId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "indexcontactList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "indexcontactId"
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            $("#htsId").val(data.htsId);

                        }
                        $("#indexcontactId").val(id);
                        $("#new_button").html("View");
                    }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);

                        $("#htsId").val(data.htsId);

                    }
                    $("#indexcontactId").val(id);
                    $("#lamisform").attr("action", "Indexcontact_find");
                    $("#lamisform").submit();
                }
            }); //end of detail jqGrid

            $("#new_button").bind("click", function (event) {
                if ($("#new_button").html() === "New") {
                    $("#lamisform").attr("action", "Indexcontact_new");
                    return true;
                } else if ($("#new_button").html() === "View") {
                    $("#lamisform").attr("action", "Indexcontact_find");
                    $("#lamisform").submit();
                    return true;
                }
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Indexcontact_search");
                return true;
            });
        });

        function newButton(data) {
            $("#id").val(data);
            $("#lamisform").attr("action", "Indexcontact_new");
            return true;
        }

        function editButton(data) {
            $("#id").val(data);
            $("#lamisform").attr("action", "Indexcontac_find");
            $("#lamisform").submit();
            return true;
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_hts.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item active" aria-current="page">Indexcontact Tracing</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <input name="clientCode" id="clientCode" type="hidden">
                <input name="htsId" id="htsId" type="hidden">
                <input name="indexcontactId" id="indexcontactId" type="hidden">
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
                    </div>
                    <div class="table-responsive">
                        <table id="detail" class="table table-striped table-bordered center"></table>
                        <div id="detail" style="text-align:center;"></div>
                        <p></p>
                    </div>
                    <!--                         <div id="buttons" style="width: 200px">
                                                <button id="new_button" disabled ="true">New</button>
                                                &nbsp;<button id="close_button">Close</button>-->
                </div>
        </div>

        </form>
    </div>
</div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
