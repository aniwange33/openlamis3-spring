<%-- 
    Document   : Commence
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
        var gridNum = 0;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            $("#grid").jqGrid({
                url: "Patient_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "Age", "Current CD4", "Last Clinic Stage", "ART Start Date"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "100"},
                    {name: "name", index: "name", width: "220"},
                    {name: "gender", index: "gender", width: "50"},
                    {name: "age", index: "age", width: "50"},
                    {name: "lastCd4", index: "lastCd4", width: "100"},
                    {name: "lastClinicStage", index: "lastClinicStage", width: "100"},
                    {name: "dateStarted", index: "dateStarted", width: "100"}
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "patientId",
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
                    $("#ok_button").removeAttr("disabled");
                    $("#ok_button").attr("src", "images/ok_button.jpg");
                },
                ondblClickRow: function (id) {
                    $("#id").val(id);
                    $("#lamisform").attr("action", "Adr_report");
                    $("#lamisform").submit();
                }
            }); //end of jqGrid

            $("#ok_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Adr_report");
                return true;
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Clinic_page");
                return true;
            });
        });
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">

        <jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/search.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Clinic >> ART Clinic >> Care & Support Assessment</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Patients reporting ADR with Severity grade 2 and above</td>
                    </tr>
                </table>
                <p></p>
                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td colspan="4">Month/Year:&nbsp;
                            <select name="reportingMonth" style="width: 100px;" class="inputboxes" id="reportingMonth"/>
                            <option></option>
                            <option>January</option>
                            <option>February</option>
                            <option>March</option>
                            <option>April</option>
                            <option>May</option>
                            <option>June</option>
                            <option>July</option>
                            <option>August</option>
                            <option>September</option>
                            <option>October</option>
                            <option>November</option>
                            <option>December</option>
                            </select>
                            &nbsp;<select name="reportingYear" style="width: 75px;" class="inputboxes"
                                          id="reportingYear"/>
                            <option></option>
                            <option>2005</option>
                            <option>2006</option>
                            <option>2007</option>
                            <option>2008</option>
                            <option>2009</option>
                            <option>2010</option>
                            <option>2011</option>
                            <option>2012</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <p></p>

                <div>
                    <fieldset>
                        <legend> Patient List</legend>
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
                    <button id="ok_button" disabled="true">New</button> &nbsp;<button id="cancel_button">Cancel</button>
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
