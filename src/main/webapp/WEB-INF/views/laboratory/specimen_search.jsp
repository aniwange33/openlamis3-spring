<%-- 
    Document   : Patient
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>LAMIS 3.0</title>
<jsp:include page="/WEB-INF/views/template/css.jsp"/>

<jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
<script type="text/javascript" src="js/lamis/specimen-common.js"></script>

<script type="text/JavaScript">
    var gridNum = 1;
    var enablePadding = true;
    $(document).ready(function () {
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
            url: "Specimen_grid.action",
            datatype: "json",
            mtype: "GET",
            colNames: ["Lab No", "Hospital No", "Name", "Date Received", "Sample", "Result", "Facility", "Action"],
            colModel: [
                {name: "labno", index: "labno", width: "150"},
                {name: "hospitalNum", index: "hospitalNum", width: "160"},
                {name: "name", index: "name", width: "300"},
                {
                    name: "dateReceived",
                    index: "dateReceived",
                    width: "150",
                    formatter: "date",
                    formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
                },
                {name: "specimenType", index: "specimenType", width: "150"},
                {name: "result", index: "result", width: "200"},
                {name: "facilityName", index: "facilityName", width: "150"},
                {
                    name: "specimenId",
                    index: "specimenId",
                    width: "100",
                    formatter: function (cellValue, options, rowObject) {
                        return "<button id='new_button' onclick='newAppointment(" + cellValue + ");'  class='btn btn-sm btn-info'>New</button>";

                    }
                },

            ],
            pager: $('#pager'),
            rowNum: 100,
            sortname: "specimenId",
            sortorder: "desc",
            viewrecords: true,
            imgpath: "themes/basic/images",
            resizable: false,
            height: 350,
            jsonReader: {
                root: "specimenList",
                page: "currpage",
                total: "totalpages",
                records: "totalrecords",
                repeatitems: false,
                id: "specimenId"
            },
            onSelectRow: function (id) {
                $("#specimenId").val(id);
                var data = $("#grid").getRowData(id);
                $("#labno").val(data.labno);
            },
            ondblClickRow: function (id) {
                $("#specimenId").val(id);
                var data = $("#grid").getRowData(id);
                $("#labno").val(data.labno);
                $("#lamisform").attr("action", "Specimen_find");
                $("#lamisform").submit();
            }
        }); //end of jqGrid                 

        $("#new_button").bind("click", function (event) {
            $("#lamisform").attr("action", "Specimen_new");
            return true;
        });
        $("#close_button").bind("click", function (event) {
            $("#lamisform").attr("action", "Specimen_page");
            return true;
        });
    });
</script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_specimen.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="/Specimen_page">Auto-Lab(PCR)</a></li>
        <li class="breadcrumb-item active" aria-current="page">Specimen List</li>
    </ol>
</nav>

<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
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
                        <div class="table-responsible">
                            <table id="grid" class="table table-striped table-bordered center"></table>
                            <div id="pager" style="text-align:center;"></div>
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
