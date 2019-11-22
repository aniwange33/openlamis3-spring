<%-- 
    Document   : Appointment
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
    <script type="text/javascript" src="js/lamis/appointment-common.js"></script>
    <script type="text/JavaScript">
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();


            $.ajax({
                url: "Patient_retrieve.action",
                dataType: "json",
                success: function (patientList) {
                    // set patient id and number for which infor is to be entered
                    $("#id").val(patientList[0].patientId);
                    $("#hospitalNum").val(patientList[0].hospitalNum);
                    $("#dateNextClinic").val(patientList[0].dateNextClinic);
                    if ((patientList[0].dateLastClinic).length != 0) {
                        date = patientList[0].dateLastClinic;
                        $("#dateLastClinic").html(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    }
                    $("#dateNextRefill").val(patientList[0].dateNextRefill);
                    if ((patientList[0].dateLastRefill).length != 0) {
                        date = patientList[0].dateLastRefill;
                        $("#dateLastRefill").html(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    }
                    if ((patientList[0].dateLastCd4).length != 0) {
                        date = patientList[0].dateLastCd4;
                        $("#dateLastCd4").html(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    }
                    $("#sendMessage").val(patientList[0].sendMessage);
                    if (patientList[0].sendMessage != 0) {
                        $("#sendMessage").attr("disabled", false);
                        $("#enableSms").attr("checked", true);
                    } else {
                        $("#sendMessage").attr("disabled", true);
                        $("#enableSms").attr("checked", false);
                    }
                    $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
                    if ((patientList[0].dateLastCd4).length != 0) {
                        checkCd4(patientList[0].dateLastCd4);
                    }
                    if ((patientList[0].dateNextClinic).length != 0) {
                        date = patientList[0].dateNextClinic;
                        var dateNext = new Date(date.slice(6), date.slice(0, 2), date.slice(3, 5));
                        if (Date.today().isBefore(dateNext)) {
                            $("#date1").val(patientList[0].dateNextClinic);
                        }
                    }
                    if ((patientList[0].dateNextRefill).length != 0) {
                        date = patientList[0].dateNextRefill;
                        var dateNext = new Date(date.slice(6), date.slice(0, 2), date.slice(3, 5));
                        if (Date.today().isBefore(dateNext)) {
                            $("#date2").val(patientList[0].dateNextRefill);
                        }
                    }
                }
            }); //end of ajax call

            $("#enableSms").bind("click", function (event) {
                if ($("#enableSms").attr("checked")) {
                    $("#sendMessage").attr("disabled", false);
                } else {
                    $("#sendMessage").attr("disabled", true);
                }
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Appointment_search");
                return true;
            });
        });
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_casemanagement.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="Casemanagement_page">Case Management</a></li>
        <li class="breadcrumb-item active" aria-current="page">New Appointment</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-10 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <div id="messageBar"></div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">Hospital Number <span style="color:black">:</span></label>
                            <input name="hospitalNum" type="text" class="form-control" id="hospitalNum"/><span
                                id="patientifor"></span><input name="patientId" type="hidden" class="form-control"
                                                               id="patientId"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <div class="row">
                                <div class="col-md-4">
                                    <br/>
                                    <h7>Last Clinic visit:</h7>
                                </div>
                                <div class="col-md-6">
                                    <br/>
                                    <h6 id="dateLastClinic" style="color:blue"></h6>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">Next Clinic Appointment<span style="color:red">:</span></label>
                            <input name="date1" type="text" class="form-control" id="date1"/>
                            <input name="dateNextClinic" type="hidden" class="form-control" id="dateNextClinic"/><span
                                id="dateHelp1" class="errorspan"></span><span id="dateHelp1" class="errorspan"></span>
                        </div>
                        <div class="col-md-6 form-group">
                            <div class="row">
                                <div class="col-md-4">
                                    <br/>
                                    <h7>Last refill visit:</h7>
                                </div>
                                <div class="col-md-6">
                                    <br/>
                                    <h6 id="dateLastRefill" class="title" style="color:blue"></h6>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">Next Refill Appointment<span style="color:red">:</span></label>
                            <input name="date2" type="text" class="form-control" id="date2"/>
                            <input name="dateNextRefill" type="hidden" class="form-control" id="dateNextRefill"/><span
                                id="dateHelp2" class="errorspan"></span>
                        </div>
                        <div class="col-md-6 form-group">
                            <div class="row">
                                <div class="col-md-4">
                                    <br/>
                                    <h7>Last CD4 test:</h7>
                                </div>
                                <div class="col-md-6">
                                    <br/>
                                    <h6 id="dateLastCd4" style="color:blue"></h6>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <div class="row">
                                <div class="col-6">
                                    <div class="form-check mt-6">
                                        <label class="form-check-label">
                                            <input name="enableSms" type="checkbox" value="1" class="form-check-input"
                                                   id="enableSms"/><span class="form-check-sign"></span>Send
                                            SMS Reminder
                                        </label>
                                    </div>
                                </div>
                                <div class="col-6 form-group">
                                    <select name="sendMessage" class="form-control select2" style="width: 100%"
                                            id="sendMessage">
                                        <option value="">Languages</option>
                                        <option>English</option>
                                        <option>Yeruba</option>
                                        <option>Ibo</option>
                                        <option>Hausa</option>
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
                        <button id="save_button" class="btn btn-fill btn-info">Save</button>
                        <button id="close_button" class="btn btn-fill btn-default">Close</button>
                    </div>

                </div>

            </form>

        </div>
    </div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>