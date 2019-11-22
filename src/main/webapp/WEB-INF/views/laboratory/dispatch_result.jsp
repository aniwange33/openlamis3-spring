<%-- 
    Document   : Data Export
    Created on : Aug 15, 2012, 6:53:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 2.6</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/maintenance-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/JavaScript">
        $(document).ready(function () {
            initialize();

            $.ajax({
                url: "TreatmentUnit_retrieve.action",
                dataType: "json",
                success: function (facilityMap) {
                    var options = "<option value = '" + '0' + "'>" + 'All' + "</option>";
                    $.each(facilityMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    })
                    $("#treatmentUnitId").html(options);
                    $("#treatmentUnitId").val('0');
                }
            }); //end of ajax call

            $("#grid").jqGrid({
                url: "Result_dispatcher_grid.action?q=1&treatmentUnitId=0",
                datatype: "json",
                mtype: "GET",
                colNames: ["Lab No", "Hospital Num", "Test Result", "Date Recieved", ""],
                colModel: [
                    {name: "labno", index: "labno", width: "100"},
                    {name: "result", index: "result", width: "100"},
                    {
                        name: "dateReceived",
                        index: "dateReceived",
                        width: "110",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
                    },
                    {name: "facilityName", index: "facilityName", width: "360"},
                    {
                        name: "sel",
                        index: "sel",
                        width: "50",
                        align: "center",
                        formatter: "checkbox",
                        editoptions: {value: "1:0"},
                        formatoptions: {disabled: false}
                    }
                ],
                rowNum: -1,
                sortname: "dateReceived",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 320,
                jsonReader: {
                    root: "specimenList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "specimenId"
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    if (data.sel == "1") {
                        $(this).jqGrid('setRowData', id, false, {color: 'black'})
                    } else {
                        $(this).jqGrid('setRowData', id, false, {color: 'red'})
                    }
                }
            }); //end of jqGrid

            $("#send_button").bind("click", function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    dispatchResult();
                    return false;
                }
            });

            $("#treatmentUnitId").bind("change", function (event) {
                retrieveResult();
            });
            $('input[name="dispatched"]:radio').click(function (event) {
                retrieveResult();
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Specimen_page");
                return true;
            });
        });

        function retrieveResult() {
            var url = "Result_dispatcher_grid.action?q=1&treatmentUnitId=" + $("#treatmentUnitId").val();
            if ($('input[name="dispatched"]:checked').val() == 2) {
                url = "Result_dispatcher_grid.action?q=1&dispatched=1&treatmentUnitId=" + $("#treatmentUnitId").val()
            }
            $("#grid").setGridParam({url: url, page: 1}).trigger("reloadGrid");
        }

        function dispatchResult() {
            $("#send_button").attr("disabled", "disabled");
            var url = "Dispatch_result.action";
            if ($("#forwardSms").attr("checked")) {
                url = "Dispatch_result.action?forwardSms";
            }

            $.ajax({
                url: url,
                dataType: "json",
                error: function (jgXHR, status) {
                    status = "Error occured while dispatching result";
                    $("#messageBar").html(status).slideDown('fast');
                },
                success: function (status) {
                    status = "Result Dispatch Completed";
                    $("#messageBar").html(status).slideDown('fast');
                }
            });
            $("#send_button").removeAttr("disabled");
        }

    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">

        <jsp:include page="/WEB-INF/views/template/nav_specimen.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/searchicon.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Laboratory >> Auto Lab (PCR) >> Dispatch Result</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Lab Result Dispatcher</td>
                    </tr>
                </table>
                <div id="loader"></div>
                <div id="messageBar"></div>
                <p></p>

                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr></tr>
                    <tr>
                        <td><label>Treatment Unit/Facility: &nbsp;</label>
                            <select name="treatmentUnitId" style="width: 250px;" class="inputboxes"
                                    id="treatmentUnitId">
                                <option></option>
                            </select>
                        </td>
                    </tr>
                    <tr></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr></tr>
                    <tr>
                        <td><input type="radio" name="dispatched" value="1" checked/><label>Un-dispatched
                            results</label></td>
                        <td></td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td><input type="radio" name="dispatched" value="2"/><label>Dispatched results</label></td>
                        <td></td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td><input name="forwardSms" type="checkbox" id="forwardSms"/><label>Forward message to sender's
                            phone</label></td>
                    </tr>
                    <tr></tr>
                </table>
                <p></p>

                <div>
                    <fieldset>
                        <legend> Lab Test Result</legend>
                        <p></p>
                        <table width="99%" height="90" border="0" class="space">
                            <tr>
                                <td>
                                    <table id="grid"></table>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </div>
                <p></p>

                <div id="userGroup" style="display: none">
                </div>
                <div id="buttons" style="width: 200px">
                    <button id="send_button">Send</button> &nbsp;<button id="close_button"/>
                    Close</button>
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
