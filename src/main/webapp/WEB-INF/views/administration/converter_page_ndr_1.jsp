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
    <title>LAMIS 3.0</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
    <link type="text/css" rel="stylesheet" href="css/zebra_dialog.css"/>

    <script type="text/javascript" src="js/lamis/maintenance-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/jquery.timer.js"></script>
    <script type="text/javascript" src="js/zebra_dialog.js"></script>
    <script type="text/JavaScript">
        var messageOption = "1";
        $(document).ready(function () {
            initialize();

            $.ajax({
                url: "StateId_retrieve.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    })
                    $("#stateId").html(options);
                }
            }); //end of ajax call

            $("#stateId").change(function (event) {
                $.ajax({
                    url: "Facility_retrieve.action?active",
                    dataType: "json",
                    data: {stateId: $("#stateId").val()},

                    success: function (facilityMap) {
                        var options = "<option value = '" + '' + "'>" + '' + "</option>";
                        $.each(facilityMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }) //end each
                        $("#id").html(options);
                    }
                }); //end of ajax call
            });

            $("#ok_button").bind("click", function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    if ($('[name="messageOption"]:checked').val() == "2") {
                        messageOption = "2";
                        if (validateUpload()) {
                            $("#lamisform").attr("method", "post");
                            $("#lamisform").attr("enctype", "multipart/form-data");
                            $("#lamisform").attr("action", "Upload_ndrfile");
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        messageOption = "1";
                        convertData();
                        return false;
                    }
                }
            });

            if ($("#fileUploaded").html() == 1) {
                messageOption = "2";
                convertData();
            }

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Conversion_page");
                return true;
            });
        });

        var url = "";
        var x = function wait() {
            window.open(url);
        }

        function convertData() {
            $("#messageBar").hide();
            $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            $("#ok_button").attr("disabled", true);
            $("#attachment").attr("disabled", true);
            $.ajax({
                url: "Converter_dispatch_ndr.action",
                dataType: "json",
                data: {
                    messageOption: messageOption,
                    stateId: $("#stateId").val(),
                    facilityId: $("#id").val(),
                    facilityName: $("#id option:selected").text()
                },
                success: function (fileName) {
                    $("#loader").html('');
                    $("#messageBar").html("Conversion completed").slideDown('slow');
                    url = fileName;
                    window.setTimeout(x, 3000);
                    $("#ok_button").attr("disabled", false);
                    $("#attachment").attr("disabled", false);
                }
            });
        }

        function validateUpload() {
            var validate = true;

            // check for valid input is entered
            if ($("#id").val().length == 0) {
                $("#facilityHelp").html(" *");
                validate = false;
            } else {
                $("#facilityHelp").html("");
            }
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

        <jsp:include page="/WEB-INF/views/template/nav_conversion.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/report.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Administration >> Data Conversion >> Generate NDR Files</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Generate NDR Messages</td>
                    </tr>
                </table>
                <div id="loader"></div>
                <div id="messageBar"></div>
                <table width="99%" height="120" border="0" class="space" cellpadding="3">
                    <tr>
                        <td>
                            <input type="radio" name="messageOption" value="1" checked="checked"/> <label>Generate NDR
                            message of all clients</label>
                        </td>
                    </tr>
                    <tr>
                        <td><span style="margin-left:25px"></span><label>State: &nbsp;&nbsp;</label>
                            <select name="stateId" style="width: 130px;" class="inputboxes" id="stateId">
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><span style="margin-left:25px"></span><label>Facility:</label>
                            <select name="facilityId" style="width: 300px;" class="inputboxes" id="facilityId">
                            </select><span id="facilityHelp" class="errorspan"></span>
                        </td>
                    </tr>
                    <p></p>
                    <tr></tr>
                    <tr>
                        <td>
                            <input type="radio" name="messageOption" value="2"/> <label>Generate NDR message for clients
                            IDs in CSV file &nbsp;</label><input type="file" name="attachment" class="inputboxes"
                                                                 id="attachment"/><span id="fileHelp"
                                                                                        class="errorspan"></span>
                        </td>
                    </tr>
                </table>
                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                </table>
                <hr></hr>

                <div id="userGroup" style="display: none"><s:property value="#session.userGroup"/></div>
                <div id="fileUploaded" style="display: none"><s:property value="#session.fileUploaded"/></div>
                <div id="buttons" style="width: 200px">
                    <button id="ok_button">Convert</button> &nbsp;<button id="close_button">Close</button>
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
