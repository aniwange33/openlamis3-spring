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
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>

    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/search-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>

    <script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/JavaScript">
        var gridNum = 1;
        var enablePadding = true;
        $(document).ready(function () {

            $("#grid").jqGrid({
                url: "Patient_grid_search.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address", "Date Modified"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "80"},
                    {name: "name", index: "name", width: "150"},
                    {name: "gender", index: "gender", width: "50"},
                    {name: "currentStatus", index: "currentStatus", width: "100"},
                    {name: "address", index: "address", width: "250"},
                    {
                        name: "timeStamp",
                        index: "timeStamp",
                        width: "100",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
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
                onSelectRow: function (id) {
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    $("#messageBar").html("").hide();
                    $("#ok_button").removeAttr("disabled");
                }
            }); //end of jqGrid

            $("#ok_button").bind("click", function (event) {
                generateBarcode();
                return false;
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Conversion_page");
                return true;
            });
        });

        function generateBarcode() {
            $("#ok_button").attr("disabled", "disabled");
            $.ajax({
                url: "Generate_barcode.action",
                dataType: "json",
                data: {patientId: $("#id").val()},
                success: function (fileName) {
                    $("#messageBar").html("QR code generation completed").slideDown('slow');
                    $("#ok_button").removeAttr("disabled");
                }
            });
        }
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">
        <jsp:include page="/WEB-INF/views/template/nav_conversion.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/search.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Maintenance >> Generate Quick Response Code</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">
                            <table width="97%" border="0">
                                <tr>
                                    <td width="12%">Hospital No:</td>
                                    <td><input name="hospitalNum" type="text" class="inputboxes" id="hospitalNum"/></td>
                                    <td><input name="patientId" type="hidden" id="patientId"/></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>Name:</td>
                                    <td width="18%"><input name="name" type="text" class="inputboxes" id="name"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <div id="messageBar"></div>
                <p></p>

                <div>
                    <fieldset>
                        <legend> Patient List</legend>
                        <p></p>
                        <table width="99%" height="90" border="0" class="space">
                            <tr>
                                <td>
                                    <table id="grid"></table>
                                    <div id="pager" style="text-align:center;"></div>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </div>
                <p></p>

                <div id="buttons" style="width: 200px">
                    <button id="ok_button" disabled="true">Generate</button> &nbsp;<button id="close_button">Close
                </button>
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
