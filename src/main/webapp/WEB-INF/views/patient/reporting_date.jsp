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
    <title>LAMIS 3.0</title>
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/JavaScript">
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $("#date1").mask("99/99/9999");
            $("#date1").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $("#date2").mask("99/99/9999");
            $("#date2").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    if ($("#formId").html() == 1) {
                        url = "Refill_appointment.action?reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();
                    }
                    window.open(url);
                }
                return false;
            });
            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Home_page");
                return true;
            });
        });

        function validateForm() {
            var validate = true;

            $("#date1").datepicker("option", "altField", "#reportingDateBegin");
            $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
            $("#date2").datepicker("option", "altField", "#reportingDateEnd");
            $("#date2").datepicker("option", "altFormat", "mm/dd/yy");

            // check for valid input is entered
            if ($("#date1").val().length == 0) {
                $("#dateHelp1").html(" *");
                validate = false;
            } else {
                $("#dateHelp1").html("");
            }
            if ($("#date2").val().length == 0) {
                $("#dateHelp2").html(" *");
                validate = false;
            } else {
                $("#dateHelp2").html("");
            }
            return validate;
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
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong
                                                id="crumb">ART Monthly Summary</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">ART Monthly Summary</td>
                    </tr>
                </table>
                <p></p>
                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td></td>
                        <td colspan="3">From:&nbsp;<input name="date1" type="text" style="width: 100px;"
                                                          class="inputboxes" id="date1"/><input
                                name="reportingDateBegin" type="hidden" id="reportingDateBegin"/><span id="dateHelp1"
                                                                                                       class="errorspan"></span>
                            &nbsp;To:&nbsp;<input name="date2" type="text" style="width: 100px;" class="inputboxes"
                                                  id="date2"/><input name="reportingDateEnd" type="hidden"
                                                                     id="reportingDateEnd"/><span id="dateHelp2"
                                                                                                  class="errorspan"></span>
                        </td>
                    </tr>
                </table>
                <p></p>

                <div id="formId" style="display: none"></div>
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
