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
    <script type="text/javascript" src="/js/lamis/eac-common.js"></script>
    <script type="text/JavaScript">
        var obj = {};
        var lastSelectDate = "", date = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();
            if (!$('#patientId').val()) {
                fetch('/api/eac/' + $('#eacId').val(), {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    })
                }).then(function (response) {
                    return response.json()
                }).then(function (json) {
                    $('#patientId').val(json.eacList[0].patientId);
                })
            }
            $.ajax({
                url: "/api/patient/" + $('#patientId').val(),
                dataType: "json",
                success: function (response) {
                    var patientList = response.patientList;
                    $('#hospitalNum').val(patientList[0].hospitalNum)
                }
            })
            $.ajax({
                url: "/api/eac/" + ($('#eacId').val() || 0),
                dataType: "json",
                type: 'GET',
                success: function (response) {
                    populateForm(response.eacList);
                }
            }); //end of ajax call
            $("#save_button").bind("click", function (event) {
                var data = formMap();
                fetch('/api/eac', {
                    method: 'POST',
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    }),
                    body: JSON.stringify(data)
                }).then(function (res) {
                    window.location.href = "/event-page/patient/" + $('#patientId').val();
                })
                return true;
            });
            $("#close_button").bind("click", function (event) {
                window.location.href = "/event-page/patient/" + $('#patientId').val();
            });
        })
        ;
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Home</a></li>
        <li class="breadcrumb-item"><a href="/clinic">Clinic</a></li>
        <li class="breadcrumb-item active" aria-current="page">New EAC</li>
    </ol>
</nav>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Hospital Number <span style="color:black">:</span></label>
                                <input name="hospitalNum" type="text" class="form-control" id="hospitalNum"/>
                                <span id="patientifor"></span>
                                <input name="patientId" type="hidden" value="${patientId}" class="form-control"
                                       id="patientId"/>
                                <input name="eacId" type="hidden" value="${eacId}" id="eacId">
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of 1st EAC Session<span
                                        style="color:black">:</span></label>
                                <input name="date1" type="text" class="form-control" id="date1"/>
                                <input name="dateVisit" type="hidden" class="form-control" id="dateVisit"/><span
                                    id="dateHelp1" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group"><label>Last Viral Load: </label><span id="lastViralLoad_View"
                                                                                          style="color:blue"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of 2nd EAC Session<span
                                        style="color:black">:</span></label>
                                <input name="date2" type="text" class="form-control" id="date2"/>
                                <input name="dateEac1" type="hidden" class="form-control" id="dateEac1"/><span
                                    id="dateHelp2" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group"><label>Date of Last Viral Load: </label><span
                                    id="dateLastViral_view" style="color:blue"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of 3rd EAC Session</label>
                                <input name="date3" type="text" class="form-control" id="date3"/>
                                <input name="dateEac2" type="hidden" class="form-control" id="dateEac2"/><span
                                    id="dateHelp3" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Date of Repeat VL Sample Collection</label>
                                <input name="date4" type="text" class="form-control" id="date4"/>
                            </div>
                        </div>
                        <input name="dateSampleCollected" type="hidden" id="dateSampleCollected"/>
                    </div>
                    <div id="userGroup" style="display: none">
                        <s:property value="#session.userGroup"/>
                    </div>
                    <div class="pull-right">
                        <button id="save_button" class="btn btn-info">New</button>
                        <!--  <button id="delete_button" disabled="true" type="submit" class="btn btn-fill btn-danger">Delete</button> -->
                        <button id="close_button" type="button" class="btn btn-default">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>

</html>