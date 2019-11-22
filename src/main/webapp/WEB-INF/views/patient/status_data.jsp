<%-- 
    Document   : Status
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="/js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/javascript" src="/js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="/js/lamis/status-common.js"></script>
    <script type="text/javascript" src="/js/lamis/report-common.js"></script>
    <script type="text/JavaScript">
        var lastSelectDate = "";
        var updateRecord = false;

        function enableControl() {
            var outcome = $("#outcome").val();
            if (outcome == "ART Restart" || outcome == "ART Transfer Out" || outcome == "Pre-ART Transfer Out" || outcome == "Lost to Follow Up") {
                $("#date").removeAttr("disabled");

                $("#date1").attr("disabled", "disabled");
                $("#date2").attr("disabled", "disabled");
                $("#reasonInterrupt").attr("disabled", "disabled");
                $("#causeDeath").attr("disabled", "disabled");
            }
            if (outcome == "Stopped Treatment") {
                $("#date").removeAttr("disabled");
                $("#reasonInterrupt").removeAttr("disabled");

                $("#date1").attr("disabled", "disabled");
                $("#date2").attr("disabled", "disabled");
                $("#causeDeath").attr("disabled", "disabled");
            }
            if (outcome == "Died (Confirmed)") {
                $("#date").removeAttr("disabled");
                $("#causeDeath").removeAttr("disabled");

                $("#date1").attr("disabled", "disabled");
                $("#date2").attr("disabled", "disabled");
                $("#reasonInterrupt").attr("disabled", "disabled");
            }
            if (outcome == "Previously Undocumented Patient Transfer (Confirmed)" || outcome == "Traced Patient (Unable to locate)") {
                $("#date1").removeAttr("disabled");

                $("#date2").attr("disabled", "disabled");
                $("#date").attr("disabled", "disabled");
                $("#reasonInterrupt").attr("disabled", "disabled");
                $("#causeDeath").attr("disabled", "disabled");
            }
            if (outcome == "Traced Patient and agreed to return to care") {
                $("#date1").removeAttr("disabled");
                $("#date2").removeAttr("disabled");

                $("#date").attr("disabled", "disabled");
                $("#reasonInterrupt").attr("disabled", "disabled");
                $("#causeDeath").attr("disabled", "disabled");
            }
        }

        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            if ($("#date").val() == '')
                $("#save_button").attr("disabled", "disabled");
            $("#date").change(function () {
                if ($("#date").val() == '')
                    $("#save_button").attr("disabled", "disabled");
                else
                    $("#save_button").removeAttr("disabled");
            });

            if (!$('#patientId').val()) {
                fetch('/api/status-history/' + $('#historyId').val(), {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    })
                }).then(function (response) {
                    return response.json()
                }).then(function (json) {
                    $('#patientId').val(json.historyList[0].patientId);
                })
            }

            $.ajax({
                url: "/api/patient/" + $('#patientId').val(),
                dataType: "json",
                success: function (response) {
                    var patientList = response.patientList;
                    // set patient id and number for which infor is to be entered
                    $("#id").val(patientList[0].patientId);
                    $("#hospitalNum").val(patientList[0].hospitalNum);
                    $("#previousStatus").html(patientList[0].currentStatus);
                    $("#currentStatus").html(patientList[0].currentStatus);
                    $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);

                    date = patientList[0].dateCurrentStatus;
                    $("#currentStatus").val(patientList[0].currentStatus);
                    $("#datePreviousStatus").html(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    $("#lastStatusDate").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    $("#dateCurrentStatus").val(dateSlice(date));

                },
                error: function (err) {
                    console.log(err);
                }
            }); //end of ajax call

            $.ajax({
                url: "/api/status-history/" + ($('#historyId').val() || 0),
                dataType: "json",
                mtype: "get",
                success: function (statusList) {
                    console.log("Status History data", statusList);
                    populateForm(statusList);
                },
                error: function (err) {
                    console.log(err);
                }
            }); //end of ajax call


        });

        function populateForm(statusList) {
            if (statusList[0].currentStatus == "Known Death")
                $("#outcome").val("Died (Confirmed)");
            else
                $("#outcome").val(statusList[0].currentStatus);

            date = statusList[0].dateCurrentStatus;
            $("#outcomeDate").val(dateSlice(date));
            $("#historyId").val(statusList[0].historyId);
            $("#reasonInterrupt").val(statusList[0].reasonInterrupt);
            $("#causeDeath").val(statusList[0].causeDeath);
            $("#agreedDate").val(statusList[0].agreedDate);

            $("#date").val(dateSlice(date));
            updateRecord = true;
            lastSelected = statusList[0].historyId;
            initButtonsForModify();
            enableControl();

            if (statusList[0].deletable == "1") {
                $("#currentStatus").val(statusList[0].currentStatus);
                $("#dateCurrentStatus").val(dateSlice(date));

                $("#delete_button").removeAttr("disabled");
                $("#save_button").removeAttr("disabled");
            } else {
                $("#delete_button").attr("disabled", "disabled");
                $("#save_button").attr("disabled", "disabled");
//                                    $("#outcome").val("");
//                                    $("#outcomeDate").val("");                        
//                                    $("#date").val("");
            }

        }

    </script>
</head>

<body>
<div class="wrapper">
    <jsp:include page="/WEB-INF/views/template/header.jsp"/>
    <jsp:include page="/WEB-INF/views/template/nav_casemanagement.jsp"/>
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/home">Home</a></li>
            <li class="breadcrumb-item"><a href="/case-manager">Case Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Client Status Update</li>
        </ol>
    </nav>
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <form id="lamisform" theme="css_xhtml">
                    <div id="messageBar"></div>
                    <div class="card-body">
                        <input name="hospitalNum" type="hidden" id="hospitalNum" readonly="readonly"/>
                        <input name="patientInfor" type="hidden">
                        <input name="patientId" value="${patientId}" type="hidden" id="patientId"/>
                        <input name="historyId" value="${historyId}" type="hidden" id="historyId"/>
                        <input name="currentStatus" type="hidden" id="currentStatus"/>
                        <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/>
                        <input name="lastStatusDate" type="hidden" id="lastStatusDate"/>
                        <input name="gender" type="hidden" id="gender"/>
                        <input name="dateBirth" type="hidden" id="dateBirth"/>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Current Status: </label>
                            </div>
                            <div class="col-md-6 form-group">
                                <div class="col-md-4">
                                    <span id="previousStatus" style="color:blue"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Date of Current Status: </label>
                            </div>
                            <div class="col-md-6 form-group">
                                <div class="col-md-4">
                                    <span id="datePreviousStatus" style="color:blue"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form">New Status:</label>
                                    <select name="outcome" style="width: 100%" class="form-control select2"
                                            id="outcome">
                                        <option></option>
                                        <option>ART Restart</option>
                                        <option>ART Transfer Out</option>
                                        <option>Pre-ART Transfer Out</option>
                                        <option>Lost to Follow Up</option>
                                        <option>Stopped Treatment</option>
                                        <option>Died (Confirmed)</option>
                                        <option>Previously Undocumented Patient Transfer (Confirmed)</option>
                                        <option>Traced Patient (Unable to locate)</option>
                                        <option>Traced Patient and agreed to return to care</option>
                                        <option>Did Not Attempt to Trace Patient</option>
                                    </select><span id="statusregHelp" class="errorspan"></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <labe class="form-label">Date of New Status:</label>
                                        <div id="statusDateTd2"><input name="date" type="text" class="form-control"
                                                                       id="date"/>
                                            <input name="outcomeDate" type="hidden" id="outcomeDate"/><span
                                                    id="dateHelp" class="errorspan"></span>
                                        </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <div id="trackedDateTr">
                                        <label class="form-label">Date of Tracked:</label>
                                        <input name="date1" type="text" class="form-control" id="date1" disabled/>
                                        <input name="dateTracked" type="hidden" id="dateTracked"/><span id="dateHelp"
                                                                                                        class="errorspan"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Date Agreed to Return:</label>
                                    <input name="date2" type="text" class="form-control" id="date2" disabled/>
                                    <input name="agreedDate" type="hidden" id="agreedDate"/><span id="dateHelp"
                                                                                                  class="errorspan"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <div id="deathTr">
                                        <label class="form-label">Cause of Death:</label>
                                        <select name="causeDeath" style="width: 100%" class="form-control select2"
                                                id="causeDeath" disabled>
                                            <option></option>
                                            <option>HIV disease resulting in TB</option>
                                            <option>HIV disease resulting in cancer</option>
                                            <option>HIV disease resulting in other infectious and parasitic disease
                                            </option>
                                            <option>Other HIV disease resulting in other disease or conditions leading
                                                to death
                                            </option>
                                            <option>Other natural causes</option>
                                            <option>Non-natural causes</option>
                                            <option>Unknown cause</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Reason for Interruption:</label>
                                    <select name="reasonInterrupt" style="width: 100%" class="form-control select2"
                                            id="reasonInterrupt" disabled>
                                        <option></option>
                                        <option>Toxicity/side effect</option>
                                        <option>Pregnancy</option>
                                        <option>Treatment failure</option>
                                        <option>Poor adherence</option>
                                        <option>Illness, hospitalization</option>
                                        <option>Drug out of stock</option>
                                        <option>Patient lacks finances</option>
                                        <option>Other patient decision</option>
                                        <option>Planned Rx interruption</option>
                                        <option>Other</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="userGroup" style="display: none">

                    </div>
                    <div id="buttons" style="width: 200px">
                        <div id="userGroup" style="display: none">

                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="save_button" type="button" class="btn btn-fill btn-info">Save</button>
                        <button id="close_button" type="button" class="btn btn-fill btn-default">Close</button>
                    </div>

            </div>
        </div>
        </form>

    </div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>