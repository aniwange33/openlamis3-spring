<%-- 
    Document   : Facility
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 2.6</title>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/JavaScript">
        var url = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $("body").bind('ajaxStart', function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            });

            $("body").bind('ajaxStop', function (event) {
                $("#loader").html('');
            });
            $("#messageBar").hide();

            $("#ok_button").bind("click", function (event) {
                event.preventDefault();
                event.stopPropagation();
                var outcome = $("#outcome").val();
                if ($('[name="reportFormat"]:checked').val() == "pdf") {
                    console.log(".....report txml");
                    url = "Txml_list.action?outcome=" + $("#outcome").val();
                    window.open(url);
                    return false;
                } else {
                    url += "Converter_dispatch.action?outcome=" + $("#outcome").val() + "&recordType=16&viewIdentifier=" + $("#viewIdentifier").prop("checked");
                    convertData();
                }
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Home_page");
                return true;
            });
        });

        var x = function wait() {
            window.open(url);
        }


        function convertData() {
            $("#messageBar").hide();
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: url,
                dataType: "json",
                success: function (fileName) {
                    $("#messageBar").html("Conversion Completed").slideDown('fast');
                    url = fileName;
                    window.setTimeout(x, 3000);
                }
            });
            $("#ok_button").attr("disabled", false);
        }

    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">
        <jsp:include page="/WEB-INF/views/template/nav_home.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/report.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Home >> Tracking Outcome Query</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Additional Query Filters</td>
                    </tr>
                </table>
                <div id="loader"></div>
                <div id="messageBar"></div>

                <div style="margin-right: 10px;">
                    <fieldset>
                        <legend> Query filters</legend>
                        <p></p>
                        <table width="99%" border="0" class="space" cellpadding="3">
                            <tr>
                                <td width="20%"><label>Tracking Outcome Status:</label></td>
                                <td>
                                    <select name="outcome" style="width: 230px;" class="inputboxes" id="outcome">
                                        <option>All</option>
                                        <option>Died (Confirmed)</option>
                                        <option>Previously Undocumented Patient Transfer (Confirmed)</option>
                                        <option>Traced Patient (Unable to locate)</option>
                                        <option>Did Not Attempt to Trace Patient</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <p></p>
                        <p></p>
                        <table width="99%" border="0" class="space" cellpadding="3">
                            <tr>
                                <td colspan="2"><input type="radio" name="reportFormat" value="pdf" checked/><label>Generate
                                    report in PDF format </label></td>
                                <td colspan="2"><input type="radio" name="reportFormat" value="cvs"/><label>Generate
                                    report and convert report to MS Excel </label></td>
                            </tr>
                            <tr>
                                <td colspan="2"></td>
                                <td colspan="2">
                                    <span style="margin-left:20px"><input name="viewIdentifier" type="checkbox"
                                                                          id="viewIdentifier"/>&nbsp;<label>Unscramble patient identifiers like names, addresses and phone numbers</label></span>
                                </td>
                            </tr>
                            <tr></tr>
                        </table>
                    </fieldset>
                </div>
                <p></p>

                <div id="buttons" style="width: 200px">
                    <button id="ok_button">Ok</button> &nbsp;<button id="cancel_button">Close</button>
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
