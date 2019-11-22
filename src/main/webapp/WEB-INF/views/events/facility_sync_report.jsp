<%-- 
    Document   : fingerprint_duplicate_page
    Created on : Mar 23, 2019, 7:09:30 PM
    Author     : User10
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>LAMIS 2.6</title>
<link type="text/css" rel="stylesheet" href="css/lamis.css"/>
<link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>
<!--<link type="text/css" rel="stylesheet" href="css/zebra_dialog.css" /> -->

<script type="text/javascript" src="js/lamis/lamis-common.js"></script>
<script type="text/javascript" src="js/lamis/report-common.js"></script>
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
<script type="text/javascript" src="js/moment-with-locales.js"></script>
<script type="text/javascript">
    var _this = this;
    $(document).ready(function (e) {
        $("#from, #to").mask("99/99/9999");
        $("#from, #to").datepicker({
            dateFormat: "dd/mm/yy",
            changeMonth: true,
            changeYear: true,
            yearRange: "-10:+0",
            constrainInput: true,
            buttonImageOnly: true,
            buttonImage: "/images/calendar.gif"
        });

        $("#lamisform").submit(function (e) {
            e.preventDefault(); // dont submit multiple times
            if (validateForm()) {
                $("#ok_button").attr("disabled", true);
                this.submit(); // use native js submit

                setTimeout(function () {
                    $("#ok_button").attr("disabled", false);
                });
            }
        });

        function validateForm() {
            return true;
        }
    });
</script>
<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">

        <jsp:include page="/WEB-INF/views/template/nav_event.jsp"/>

        <div id="rightPanel">
            <form id="lamisform" action="sync_download">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/monitor.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Administration >> Events Monitor >> Facility Sync Report</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Facility Sync Report</td>
                    </tr>
                </table>
                <div id="messageBar"></div>
                <table width="50%" border="0">
                    <tr>
                        <td style="width: 15%;"><label>From:</label></td>
                        <td style="width: 35%;"><input name="from" type="text" style="width: 100%;" class="inputboxes"
                                                       id="from"/></td>
                        <td style="width: 15%;"><label>To:</label></td>
                        <td style="width: 35%;"><input name="to" type="text" style="width: 100%;" class="inputboxes"
                                                       id="to"/></td>
                    </tr>
                </table>
                <p></p>
                <div id="buttons" style="width: 200px">
                    <button type="submit" id="ok_button">Generate</button>
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
