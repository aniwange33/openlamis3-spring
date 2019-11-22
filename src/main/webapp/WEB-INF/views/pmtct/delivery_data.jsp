<%-- 
    Document   : Patient
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
    <script type="text/javascript" src="/js/lamis/delivery-common.js"></script>
    <script type="text/javascript" src="/js/grid.celledit.js"></script>
    <style>
        table {
            font-size: 12px;
        }
    </style>
    <script type="text/JavaScript">
        var date = "";
        var updateRecord = false;
        var lastSelected = -99;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            $.ajax({
                url: "/api/patient/" + $('#patientId').val(),
                dataType: "json",
                success: function (res) {
                    var patientList = res.patientList;
                    // set patient id and number for which infor is to be entered
                    $("#id").val(patientList[0].patientId);
                    $("#patientId").val(patientList[0].patientId);
                    $("#facilityId").val(patientList[0].facilityId);
                    $("#hospitalNumMother").val(patientList[0].hospitalNum);
                    $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
                    if (!updateRecord) getAncLastVisit();
                    date = patientList[0].dateConfirmedHiv;
                    if (date !== "") {
                        $("#date2").val(dateSlice(date));
                        $("#date2").attr("disabled", true);
                    } else {
                        $("#date2").attr("disabled", false);
                    }
                    date = patientList[0].dateStarted;
                    if (date !== "") {
                        $("#date3").val(dateSlice(date));
                        $("#date3").attr("disabled", true);
                    } else {
                        $("#date3").attr("disabled", false);
                    }
                    dueViralLoad(patientList[0].dueViralLoad, patientList[0].viralLoadType);
                    if (newDelivery) retrieveCurrentRegimen(patientList[0].patientId);
                    createChildGrid();
                }
            });
            if (!!$('#deliveryId').val()) {
                $.ajax({
                    url: "/api/delivery/" + $('#deliveryId').val(),
                    dataType: "json",
                    success: function (res) {
                        var deliveryList = res.deliveryList;
                        populateForm(deliveryList);
                    }
                }); //end of ajax call
            }
            $.jgrid.defaults.responsive = true;
            $.jgrid.defaults.styleUI = 'jQueryUI';
        });

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_pmtct.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item"><a href="/pmct">PMTCT</a></li>
    <li class="breadcrumb-item active">Labour & Delivery</li>
</ol>
<form id="lamisform" method="post" theme="css_xhtml">
    <input name="patientId" type="hidden" value="${patientId}" class="form-control" id="patientId"/>
    <input name="deliveryId" type="hidden" value="${deliveryId}" id="deliveryId"/>
    <input name="ancId" value="${ancId}" type="hidden" id="ancId"/>
    <input name="facilityId" type="hidden" id="facilityId"/>
    <input name="partnerinformationId" type="hidden" id="partnerinformationId"/>

    <div class="row">
        <div class="col-md-10 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning"></div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Hospital No:</label>
                            <input name="hospitalNumMother" type="text" class="form-control bg-light"
                                   id="hospitalNumMother" readonly="readonly"/>
                            <span id="numHelp" style="color:red"></span>
                        </div>
                        <div class="col-md-6">
                            <p><br>
                                <span id="patientInfor"></span>
                            </p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Booking Status:</label>
                                <select name="bookingStatus" style="width: 100%;" class="form-control"
                                        id="bookingStatus">
                                    <option value="">Select</option>
                                    <option value="1">Booked</option>
                                    <option value="0">Unbooked</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Date of Delivery:</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date1" type="text" class="form-control" id="date1"/>
                                </div>
                                <input name="dateDelivery" type="hidden" class="form-control" id="dateDelivery"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>ROM to Delivery Interval:</label>
                                <select name="romDeliveryInterval" style="width: 100%;" class="form-control"
                                        id="romDeliveryInterval">
                                    <option></option>
                                    <option value="< 4hrs">&lt;4hrs</option>
                                    <option value="> 4hrs">&gt;4hrs</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Mode of Delivery:</label>
                                <select name="modeDelivery" style="width: 100%;" class="form-control" id="modeDelivery">
                                    <option>Select</option>
                                    <option value="Vaginal">Vaginal</option>
                                    <option value="Elective CS">Elective CS</option>
                                    <option value="Emergency CS">Emergency CS</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Episiotomy:</label>
                                <select name="episiotomy" style="width: 100%;" class="form-control" id="episiotomy">
                                    <option>Select</option>
                                    <option>Yes</option>
                                    <option>No</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Vaginal Tear:</label>
                                <select name="vaginalTear" style="width: 100%;" class="form-control" id="vaginalTear">
                                    <option>Select</option>
                                    <option>Yes</option>
                                    <option>No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Maternal Outcome:</label>
                                <select name="maternalOutcome" style="width: 100%;" class="form-control"
                                        id="maternalOutcome">
                                    <option value="">Select</option>
                                    <option value="Alive">Alive</option>
                                    <option value="Dead">Dead</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Time of HIV Diagnosis:</label>
                            <select name="timeHivDiagnosis" style="width: 100%;" class="form-control"
                                    id="timeHivDiagnosis">
                                <option></option>
                                <option>Previously known HIV+ (L&amp;D)</option>
                                <option>Newly tested HIV+ (L&amp;D))</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Source of Referral:</label>
                            <select name="sourceReferral" style="width: 100%;" class="form-control" id="sourceReferral">
                                <option></option>
                                <option>PMTCT outreach</option>
                                <option>Sex worker outreach</option>
                                <option>Medical outpatient</option>
                                <option>Youth/Adolescent outreach</option>
                                <option>Private/Commercial Health facility</option>
                                <option>Under-fives/Immunization clinic</option>
                                <option>External HCT centre</option>
                                <option>CBO</option>
                                <option>In-patients</option>
                                <option>Self-referral</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Gestational Age at Delivery:</label>
                            <input name="gestationalAge" type="text" class="form-control" id="gestationalAge"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Hepatitis B Status:</label>
                            <select name="hepatitisBStatus" style="width: 100%;" class="form-control"
                                    id="hepatitisBStatus">
                                <option></option>
                                <option>Positive</option>
                                <option>Negative</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Hepatitis C Status:</label>
                            <select name="hepatitisCStatus" style="width: 100%;" class="form-control"
                                    id="hepatitisCStatus">
                                <option></option>
                                <option>Positive</option>
                                <option>Negative</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="card-title bg-light"><strong>Child's Information</strong></div>
                        </div>
                        <div class="col-md-12 table-responsive">
                            <table id="grid" class="table table-bordered table-striped table-sm"></table>
                            <div id="pager" style="text-align:center;"></div>
                        </div>
                    </div>
                    <div class="row table-responsive">
                        <div class="col-12">
                            <div class="card-title bg-light"><strong>Partner Information</strong></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Agreed to Partner Notification:</label>
                                <select name="partnerNotification" style="width: 100%;" class="form-control"
                                        id="partnerNotification">
                                    <option value="">Select</option>
                                    <option value="Yes">Yes</option>
                                    <option value="No">No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Partner's HIV Status:</label>
                            <select name="partnerHivStatus" style="width: 100%;" class="form-control"
                                    id="partnerHivStatus">
                                <option value="">Select</option>
                                <option value="Positive">Positive</option>
                                <option value="Negative">Negative</option>
                                <option value="Unknown">Unknown</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Partner referred to:</label>
                            <div class="form-check-inline">
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="fp" type="checkbox" class="form-check-input" value="1" id="fp"/>
                                        <span class="form-check-sign"></span> FP
                                    </label>
                                </div>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="art" type="checkbox" class="form-check-input" value="1" id="art"/>
                                        <span class="form-check-sign"></span> ART
                                    </label>
                                </div>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="others" type="checkbox" class="form-check-input" value="1"
                                               id="others"/>
                                        <span class="form-check-sign"></span> Others
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="save_button" type="submit" class="btn btn-info">Save</button>
                        <button id="close_button" type="menu" class="btn btn-default">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
