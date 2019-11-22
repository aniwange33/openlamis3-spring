<%-- 
    Document   : client_defaulter
    Created on : Oct 27, 2017, 11:56:59 AM
    Author     : user10
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

            $.ajax({
                url: "Casemanager_retrieve.action",
                dataType: "json",
                success: function (caseManagerMap) {
                    var options = "<option value = '" + '0' + "'>" + '' + "</option>";
                    $.each(caseManagerMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#casemanagerId").html(options);
                }
            }); //end of ajax call

            $("#casemanagerId").bind("change", function (event) {
                var casemanagerId = $("#casemanagerId").val();
                if (casemanagerId != "0") {
                    $("#ok_button").prop("disabled", false);
                } else if (casemanagerId == "0") {
                    $("#ok_button").prop("disabled", true);
                }
            });

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    var casemanagerId = $("#casemanagerId").val();
                    var reportType = $("input[name=reportType]:checked").val();
                    url = "Client_defaulter_report.action?casemanagerId=" + casemanagerId + "&reportType=" + reportType + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();

                    window.open(url);
                }
                return false;
            });

//                $("#ok_button").bind("click", function(event){
//                    var casemanagerId = $("#casemanagerId").val();
//                    event.preventDefault();
//                    event.stopPropagation();
//                    url = "";
//                    url += "casemanagerId="+casemanagerId;
//                    url = "Client_defaulter_report.action?"+url;                            
//                    window.open(url);
//                    return false;  
//                });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Casemanagement_page");
                return true;
            });

        });

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            if ($("#date1").val().length == 0) {
                $("#dateHelp1").html(" *");
                validate = false;
            } else {
                $("#date1").datepicker("option", "altField", "#reportingDateBegin");
                $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
                $("#dateHelp1").html("");
            }
            if ($("#date2").val().length == 0) {
                $("#dateHelp2").html(" *");
                validate = false;
            } else {
                $("#date2").datepicker("option", "altField", "#reportingDateEnd");
                $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
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
        <jsp:include page="/WEB-INF/views/template/nav_casemanagement.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/report.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Clinic >> Case Management >> Case Manager Client Defaulter List</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">Case Manager Detail</td>
                    </tr>
                </table>
                <p></p>

                <div style="margin-right: 10px;">
                    <table width="99%" border="0" class="space" cellpadding="3">
                        <tr>
                            <td width="20%"><label>Facility Case Manager:</label></td>
                            <td>
                                <select name="casemanagerId" style="width: 250px;" class="inputboxes"
                                        id="casemanagerId">
                                    <option value='0'></option>
                                </select><span id="caseManagerHelp" class="errorspan"></span>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="3"><input type="radio" name="reportType" value="6"/><label>List of missed
                                refill appointment (defaulters)</label></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="3"><input type="radio" name="reportType" value="7"/><label>List of missed
                                clinic appointment (defaulters)</label></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="3"><input type="radio" name="reportType" value="8"/><label>List of missed
                                tracking appointment (based on agreed date of return)</label></td>
                        </tr>
                        <tr></tr>
                        <p></p>
                        <tr>
                            <td></td>
                            <td colspan="3"><span style="margin-left:20px"></span> From: &nbsp;<input name="date1"
                                                                                                      type="text"
                                                                                                      style="width: 100px;"
                                                                                                      class="inputboxes"
                                                                                                      id="date1"/><input
                                    name="reportingDateBegin" type="hidden" id="reportingDateBegin"/><span
                                    id="dateHelp1" style="color:red"></span> &nbsp;To:&nbsp;<input name="date2"
                                                                                                   type="text"
                                                                                                   style="width: 100px;"
                                                                                                   class="inputboxes"
                                                                                                   id="date2"/><input
                                    name="reportingDateEnd" type="hidden" id="reportingDateEnd"/><span id="dateHelp2"
                                                                                                       style="color:red"></span>
                            </td>
                        </tr>
                    </table>
                </div>
                <p></p>
                <div id="buttons" style="width: 200px">
                    <button id="ok_button" disabled>Ok</button> &nbsp;<button id="cancel_button">Cancel</button>
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
