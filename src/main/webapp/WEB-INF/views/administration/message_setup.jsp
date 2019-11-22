<%-- 
    Document   : SMS Service
    Created on : Aug 15, 2012, 6:53:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!--        <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />
            <link type="text/css" rel="stylesheet" href="css/ui.jqgrid.css" />
            <link type="text/css" rel="stylesheet" href="themes/basic/grid.css" />
            <link type="text/css" rel="stylesheet" href="themes/jqModal.css" />-->

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/maintenance-common.js"></script>
    <!--        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
          <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
            <script type="text/javascript" src="js/grid.locale-en.js"></script>
            <script type="text/javascript" src="js/jquery.jqGrid.src.js"></script>
            <script type="text/javascript" src="js/jqDnR.js"></script>
            <script type="text/javascript" src="js/jqModal.js"></script>-->
    <script type="text/JavaScript">
        var updateRecord = false;
        $(document).ready(function () {
            initialize();

            $("#dialog").dialog({
                title: "Modem setting",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Save", click: saveModem},
                    {
                        text: "Close", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            $("#dialog1").dialog({
                title: "Send Message",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Send", click: sendMessage},
                    {
                        text: "Close", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            $("#grid").jqGrid({
                url: "Message_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Message Type", "Date to Send", "Message", "", "", "", "", "", "", ""],
                colModel: [
                    {name: "msgType", index: "msgType", width: "200"},
                    {
                        name: "dateToSend",
                        index: "date",
                        width: "200",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "recipients", index: "recipients", width: "150"},
                    {name: "message1", index: "message1", width: "210"},
                    {name: "message2", index: "message2", width: "0", hidden: true},
                    {name: "message3", index: "message3", width: "0", hidden: true},
                    {name: "message4", index: "message4", width: "0", hidden: true},
                    {name: "messageType", index: "messageType", width: "0", hidden: true},
                    {name: "daysToAppointment", index: "daysToAppointment", width: "0", hidden: true},
                    {name: "dateToSend", index: "dateToSend", width: "0", hidden: true},
                ],
                rowNum: -1,
                sortname: "messageId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 200,
                jsonReader: {
                    root: "messageList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "messageId"
                },
                onSelectRow: function (id) {
                    var data = $("#grid").getRowData(id)
                    $("#messageType").val(data.messageType);
                    $("#daysToAppointment").val(data.daysToAppointment);
                    var date = data.dateToSend;
                    if (date != "" && date.length != 0) {
                        $("#date").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    } else {
                        $("#date").val("");
                    }
                    $("#dateToSend").val(data.dateToSend);
                    $("#recipients").val(data.recipients);
                    $("#message1").val(data.message1);
                    $("#message2").val(data.message2);
                    $("#message3").val(data.message3);
                    $("#message4").val(data.message4);
                    $("#messageId").val(id);

                    updateRecord = true;
                    lastSelected = id;
                    initButtonsForModify();
                    resetMsg();
                }
            }); //end of jqGrid 

            $.ajax({
                url: "Modem_retrieve.action",
                dataType: "json",
                success: function (modemList) {
                    populateForm(modemList);
                }
            });

            $("#date").attr("disabled", "disabled");
            $("#messageType").change(function () {
                resetMsg();
            });

            $("#show_dialog").click(function () {
                $("#dialog").dialog("open");
                return false;
            });
            $("#show_dialog1").click(function () {
                $("#dialog1").dialog("open");
                return false;
            });

            $("#save_button").click(function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                    return true;
                } else {
                    if (validateMessageForm()) {
                        if (updateRecord) {
                            $("#lamisform").attr("action", "Message_update");
                        } else {
                            $("#lamisform").attr("action", "Message_save");
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            $("#delete_button").click(function (event) {
                if ($("#userGroup").html() != "Administrator") {
                    $("#lamisform").attr("action", "Error_message");
                } else {
                    $("#lamisform").attr("action", "Message_delete");
                }
                return true;
            });
            $("#close_button").click(function (event) {
                $("#lamisform").attr("action", "Setup_page");
                return true;
            });
        });

        function saveModem() {
            if (validateModemForm()) {
                $.ajax({
                    url: "Modem_save.action",
                    dataType: "json",
                    data: {
                        comPort: $("#comPort").val(),
                        baudRate: $("#baudRate").val(),
                        manufacturer: $("#manufacturer").val(),
                        model: $("#model").val(),
                        countryCode: $("#countryCode").val()
                    }
                });
                $("#dialog").dialog("close");
            }
        }

        function sendMessage() {
            $.ajax({
                url: "TestMessage_send.action",
                dataType: "json",
                data: {
                    phone: $("#phone").val(),
                    message: $("#message").val(),
                }
            });
            $("#dialog1").dialog("close");
        }

        function resetMsg() {
            var msg = $("#messageType").val();
            if (msg == 1) {
                $("#daysToAppointment").removeAttr("disabled");
                $("#date").attr("disabled", "disabled");
                $("#date").val("");
            } else {
                if (msg == 3) {
                    $("#date").removeAttr("disabled");
                    $("#daysToAppointment").attr("disabled", "disabled");
                    $("#daysToAppointment").val("");
                } else {
                    $("#daysToAppointment").attr("disabled", "disabled");
                    $("#date").attr("disabled", "disabled");
                    $("#daysToAppointment").val("");
                    $("#date").val("");
                }
            }
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_setup.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item"><a href="Setup_page">Setup</a></li>
    <li class="breadcrumb-item active">SMS Setup</li>
</ol>
<s:form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Message Type</label>
                            <select name="messageType" style="width: 100%;" class="form-control select2"
                                    id="messageType">
                                <option>Select</option>
                                <option value=1>Appointment messages</option>
                                <option value=2>Daily messages</option>
                                <option value=3>Scheduled messages</option>
                            </select>
                        </div>
                        <div class="form-group col-md-6">
                            <label> days to appointment date </label>
                            <select name="daysToAppointment" style="width: 100%;" class="form-control select2"
                                    id="daysToAppointment">
                                <option>Select</option>
                                <option>1</option>
                                <option>2</option>
                                <option>3</option>
                            </select>
                            <span id="daysToAppointmentHelp" style="color:red"></span>
                            <input name="messageId" type="hidden" class="form-control" id="messageId"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Date to Send</label>
                            <input name="date" type="text" class="form-control datepicker text-lighten-1" id="date"/>
                            <input name="dateToSend" type="hidden" id="dateToSend"/>
                            <span id="dateHelp" style="color:red"></span>
                        </div>
                        <div class="form-group col-md-6">
                            <label>Message Recipients</label>
                            <select name="recipients" style="width: 100%;" class="form-control select2" id="recipients">
                                <option>Select</option>
                                <option>All</option>
                                <option>ART Only</option>
                                <option>Non ART Only</option>
                                <option>Defaulter Only</option>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Message (English)</label>
                            <input name="message1" type="text" class="form-control" id="message1"/>
                            <span id="messageHelp" style="color:red"></span>
                        </div>
                        <div class="form-group col-md-6">
                            <label>Message (Hausa)</label>
                            <input name="message2" type="text" class="form-control" id="message2"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Message (Igbo)</label>
                            <input name="message3" type="text" class="form-control" id="message3"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label>Message (Yoruba)</label>
                            <input name="message4" type="text" class="form-control" id="message4"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 pull-right">
                            <button id="show_dialog" class="btn btn-info">Modem settings...</button>
                            <button id="show_dialog1" class="btn btn-default">Send Message...</button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <label>Message List</label>
                            <div class="table-responsive">
                                <table id="grid" class="table table-bordered table-striped"></table>
                            </div>
                        </div>
                    </div>

                    <div id="dialog">
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label>COM Port</label>
                                <select name="comPort" style="width: 100%;" class="form-control select2" id="comPort">
                                    <option>Select</option>
                                    <option>COM1</option>
                                    <option>COM2</option>
                                    <option>COM3</option>
                                    <option>COM4</option>
                                    <option>COM5</option>
                                    <option>COM6</option>
                                    <option>COM7</option>
                                    <option>COM8</option>
                                    <option>COM9</option>
                                    <option>COM10</option>

                                    <option>COM11</option>
                                    <option>COM12</option>
                                    <option>COM13</option>
                                    <option>COM14</option>
                                    <option>COM15</option>
                                    <option>COM16</option>
                                    <option>COM17</option>
                                    <option>COM18</option>
                                    <option>COM19</option>
                                    <option>COM20</option>

                                    <option>COM21</option>
                                    <option>COM22</option>
                                    <option>COM23</option>
                                    <option>COM24</option>
                                    <option>COM25</option>
                                    <option>COM26</option>
                                    <option>COM27</option>
                                    <option>COM28</option>
                                    <option>COM29</option>
                                    <option>COM30</option>

                                    <option>COM31</option>
                                    <option>COM32</option>
                                    <option>COM33</option>
                                    <option>COM34</option>
                                    <option>COM35</option>
                                    <option>COM36</option>
                                    <option>COM37</option>
                                    <option>COM38</option>
                                    <option>COM39</option>
                                    <option>COM40</option>

                                    <option>COM41</option>
                                    <option>COM42</option>
                                    <option>COM43</option>
                                    <option>COM44</option>
                                    <option>COM45</option>
                                    <option>COM46</option>
                                    <option>COM47</option>
                                    <option>COM48</option>
                                    <option>COM49</option>
                                    <option>COM50</option>
                                </select>
                                <span id="comPortHelp" style="color:red"></span>
                            </div>
                            <div class="form-group col-md-6">
                                <label>Baud Rate</label>
                                <select name="baudRate" style="width: 100%;" class="form-control select2" id="baudRate">
                                    <option>Select</option>
                                    <option>9600</option>
                                    <option>19200</option>
                                    <option>38400</option>
                                    <option>56000</option>
                                    <option>57600</option>
                                    <option>115200</option>
                                    <option>230400</option>
                                    <option>460800</option>
                                </select>
                                <span id="baudRateHelp" style="color:red"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label>Manufacturer</label>
                                <select name="manufacturer" style="width: 100%;" class="form-control select2"
                                        id="manufacturer">
                                    <option>Select</option>
                                    <option>Billionton</option>
                                    <option>EagleTec</option>
                                    <option>ITengo</option>
                                    <option>Huawei</option>
                                    <option>ZTE</option>
                                    <option>Janus</option>
                                    <option>Nokia</option>
                                    <option>Multitech</option>
                                    <option>Sharp</option>
                                    <option>Siemens</option>

                                    <option>SIMCOM</option>
                                    <option>Sony Ericsson</option>
                                    <option>Ubinetics</option>
                                    <option>Wavecom</option>
                                    <option>Motorola</option>
                                    <option>Teltonika</option>
                                    <option>Samsung</option>
                                    <option>Samba</option>
                                    <option>Rogers</option>
                                    <option>Falcom</option>

                                    <option>Fargo Maestro 20</option>
                                    <option>BandLuxe</option>
                                    <option>SIM548C GSM module</option>
                                    <option>Karbonn</option>
                                    <option>D-Link</option>
                                </select><span id="manufacturerHelp" style="color:red"></span>
                            </div>
                            <div class="form-group col-md-6">
                                <label>Model</label>
                                <input name="model" type="text" class="form-control" id="model"/>
                                <span id="modelHelp" style="color:red"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label>Country Code</label>
                                <input name="countryCode" type="text" class="form-control" id="countryCode"/>
                                <span id="countryCodeHelp" style="color:red"></span>
                            </div>
                        </div>
                    </div>

                    <div id="dialog1">
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label>Message</label>
                                <input name="message" type="text" class="form-control" id="message"/>
                            </div>
                            <div class="form-group col-md-6">
                                <label>Phone Number</label>
                                <input name="phone" type="text" class="form-control" id="phone"/>
                            </div>
                        </div>
                    </div>

                    <div id="user_group" style="display: none">Administrator</div>
                    <div id="userGroup" style="display: none"></div>
                    <div class="pull-right">
                        <button id="save_button" type="submit" class="btn btn-fill btn-info">Save</button>
                        <button id="close_button" type="reset" class="btn btn-fill btn-default">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</s:form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>


