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
        var processed = false;
        $(document).ready(function () {
            initialize();

            $("#grid").jqGrid({
                url: "Result_processor_grid",
                datatype: "json",
                mtype: "GET",
                colNames: ["Lab No", "Hospital Num", "Test Result", "Date Recieved", "Facility"],
                colModel: [
                    {name: "labno", index: "labno", width: "80"},
                    {name: "hospitalNum", index: "hospitalNum", width: "120"},
                    {name: "result", index: "result", width: "100"},
                    {
                        name: "dateReceived",
                        index: "dateReceived",
                        width: "110",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
                    },
                    {name: "facilityName", index: "facilityName", width: "310"},
                ],
                rowNum: -1,
                sortname: "specimenId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 350,
                jsonReader: {
                    root: "specimenList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "specimenId"
                }
            }); //end of jqGrid

            $("#import_button").bind("click", function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    if (processed) {
                        saveData();
                        return false;
                    } else {
                        if (validateUpload()) {
                            $("#lamisform").attr("method", "post");
                            $("#lamisform").attr("enctype", "multipart/form-data");
                            $("#lamisform").attr("action", "Upload_labfile");
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            });

            if ($("#fileUploaded").html() == 1 && !processed) {
                initButtonsForSave();
                $("#fileUploaded").html(0);
                processed = true;
            }

            $("#discard_button").bind("click", function (event) {
                var url = "Result_processor_grid?q=1&discard";
                $("#grid").setGridParam({url: url, page: 1}).trigger("reloadGrid");
                $("#read_button").removeAttr("disabled");
                $("#fileUploaded").html(0);
                processed = false;
                resetButtons();
                return false;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Specimen_page");
                return true;
            });
        });

        function saveData() {
            $("#import_button").attr("disabled", true);
            $.ajax({
                url: "Result_save.action",
                dataType: "json",
                error: function (jgXHR, status) {
                    status = "Error occured while saving";
                    $("#messageBar").html(status).slideDown('fast');
                },
                success: function (status) {
                    status = "Result processing completed";
                    $("#messageBar").html(status).slideDown('fast');
                }
            });
            $("#import_button").attr("disabled", false);
            $("#fileUploaded").html(0);
            processed = false;
            resetButtons();
        }

        function resetButtons() {
            $("#import_button").html("Import");
            $("#close_button").html("Close");
            $("#close_button").attr("data-button-state", "close");
            $("#discard_button").attr("disabled", true);
        }

        function initButtonsForSave() {
            $("#import_button").html("Save");
            $("#close_button").html("Close");
            $("#close_button").attr("data-button-state", "close");
            $("#discard_button").attr("disabled", false);
        }

        function validateUpload() {
            var validate = true;

            // check if file name is entered
            if ($("#attachment").val().length == 0) {
                $("#fileHelp").html(" *");
                validate = false;
            } else {
                $("#fileHelp").html("");
            }
            return validate;
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
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Laboratory >> Auto Lab (PCR) >> Result Processing</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Lab Result File</td>
                    </tr>
                </table>
                <div id="loader"></div>
                <div id="messageBar"></div>

                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td></td>
                        <td colspan="3"><label>File Name: </label><input type="file" name="attachment"
                                                                         class="inputboxes" id="attachment"/><span
                                id="fileHelp" class="errorspan"></span></td>
                    </tr>
                </table>
                <div>
                    <fieldset>
                        <legend> Laboratory Test Result</legend>
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
                <div id="fileUploaded" style="display: none">
                </div>
                <div id="buttons" style="width: 300px">
                    <button id="import_button">Import</button> &nbsp;<button id="discard_button" disabled="true"/>
                    Discard</button> &nbsp;<button id="close_button"/>
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
