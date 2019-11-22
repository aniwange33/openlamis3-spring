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
    <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/basic/grid.css"/>
    <link type="text/css" rel="stylesheet" href="themes/jqModal.css"/>

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="js/grid.locale-en.js"></script>
    <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="js/jqDnR.js"></script>
    <script type="text/javascript" src="js/jqModal.js"></script>
    <script type="text/javascript" src="js/jquery.timer.js"></script>
    <script type="text/JavaScript">
        var url = "";
        $(document).ready(function () {
            resetPage();

            $("#grid").jqGrid({
                url: "Participant_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Participant", "", "", "", "", "", "", ""],
                colModel: [
                    {name: "message", index: "message", width: "200"},
                    {name: "participantId", index: "participantId", width: "100", hidden: true},
                    {name: "age", index: "name", width: "5", hidden: true},
                    {name: "gender", index: "gender", width: "10", hidden: true},
                    {name: "location", index: "location", width: "100", hidden: true},
                    {name: "phone", index: "phone", width: "15", hidden: true},
                    {name: "dateMessage", index: "dateMessage", width: "100", hidden: true},
                    {name: "unread", index: "unread", width: "1", hidden: true},
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "Name",
                sortorder: "asc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 460,
                jsonReader: {
                    root: "participantList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "phone"
                },
                afterInsertRow: function (id, data) {
                    if (data.unread == 1) {
                        $(this).jqGrid('setRowData', id, false, {color: 'red'});
                    }
                },
                onSelectRow: function (id) {
                    if (id == null) {
                        id = 0;
                        if ($("#detail").getRecords() > 0) {
                            $("#detail").setGridParam({
                                url: "Conversation_grid.action?q=1&phone=" + id,
                                page: 1
                            }).trigger("reloadGrid");
                        }
                    } else {
                        $("#detail").setGridParam({
                            url: "Conversation_grid.action?q=1&phone=" + id,
                            page: 1
                        }).trigger("reloadGrid");
                    }
                    $("#phone").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#age").val(data.age);
                    $("#gender").val(data.gender);
                    $("#location").val(data.location);
                    $("#participantId").val(data.participantId);
                    $("#ok_button").attr("disabled", false);
                }
            }); //end of master jqGrid

            $("#detail").jqGrid({
                datatype: "json",
                mtype: "GET",
                colNames: ["Message", "Time", "", ""],
                colModel: [
                    {name: "message", index: "message", width: "300"},
                    {name: "timeMessage", index: "timeMessage", width: "90"},
                    {name: "originatorId", index: "originatorId", width: "100", hidden: true},
                    {name: "conversationId", index: "conversationId", width: "100", hidden: true},
                ],
                rowNum: -1,
                sortname: "conversationId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 300,
                jsonReader: {
                    root: "conversationList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "conversationId"
                },
                afterInsertRow: function (id, data) {
                    if (data.originatorId == 1) {
                        $(this).jqGrid('setRowData', id, false, {background: 'blue', color: 'white'});
                    }
                }
            }); //end of detail jqGrid

            $("#ok_button").bind("click", function (event) {
                event.preventDefault();
                event.stopPropagation();
                if ($("#message").val().length == 0) {
                    $("#progress").html("Please enter message to send...");
                } else {
                    $("#progress").html("");
                    url = "Sms_send.action?from=" + $("#phone").val() + "&text=" + $("#message").val();
                    sendSms();
                }
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Maintenance_page");
                return true;
            });

            var timer = $.timer(function () {
                $("#grid").setGridParam({url: "Participant_grid.action", page: 1}).trigger("reloadGrid");
                if ($("#phone").val().length != 0) $("#detail").setGridParam({
                    url: "Conversation_grid.action?q=1&phone=" + $("#phone").val(),
                    page: 1
                }).trigger("reloadGrid");
            });
            timer.set({time: 120000, autostart: true});

        });

        function sendSms() {
            $("#progress").html("Sending...");
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: url,
                dataType: "json",
                success: function (status) {
                    $("#message").val("");
                    if ($("#phone").val().length != 0) $("#detail").setGridParam({
                        url: "Conversation_grid.action?q=1&phone=" + $("#phone").val(),
                        page: 1
                    }).trigger("reloadGrid");
                    $("#progress").html("");
                    $("#ok_button").attr("disabled", false);
                }
            });
        }
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">
        <div id="leftPanel">
            <div id="navigation">
                <div style="float: left" class="navMenu">
                    <div class="navTitle">Inbox</div>
                    <div style="margin-bottom: 20px;">
                        <table width="99%" height="90" border="0" class="space">
                            <tr>
                                <td>
                                    <table id="grid"></table>
                                    <div id="pager" class="scroll" style="text-align:center;"></div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td class="topheaders">Conversation</td>
                    </tr>
                </table>
                <table width="99%" border="0" class="space" cellpadding="3">
                    <tr>
                        <td>
                            <table id="detail"></table>
                        </td>
                    </tr>
                    <tr>
                        <td><span id="progress" style="color:#CBC7B8"></span></td>
                    </tr>
                    <tr>
                        <td>
                            <textarea name="message" style="width: 400px;" class="inputboxes" id="message"
                                      rows="5"></textarea><span id="messageToSendHelp" style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="buttons" style="width: 170px;">
                                <button id="ok_button" disabled="true">Send</button> &nbsp;<button id="close_button">
                                Close
                            </button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td width="20%"><input name="phone" type="hidden" id="phone"/></td>
                        <td width="30%"></td>
                        <td width="20%"></td>
                        <td width="30%"></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
