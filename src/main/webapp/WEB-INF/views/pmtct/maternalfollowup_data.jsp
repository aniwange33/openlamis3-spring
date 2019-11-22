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
    <script type="text/javascript" src="/js/lamis/maternalfollowup-common.js"></script>
    <script type="text/JavaScript">
        var date = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            if (!$('#patientId').val()) {
                fetch('/api/maternal-followup/' + $('#maternalfollowupId').val(), {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(function (res) {
                    return res.json()
                }).then(function (res) {
                    var patientList = res.maternalinformationList;
                    $('#patientId').val(patientList[0].patientId)
                })
            }
            fetch('/api/patient/' + $('#patientId').val(),{
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function (res) {
                return res.json()
            }).then(function (res) {
                var patientList = res.patientList;
                $('#facilityId').val(patientList[0].facilityId);
                $('#hospitalNum').val(patientList[0].hospitalNum);
            });

            $.ajax({
                url: "/api/maternal-followup/" + $('#maternalfollowupId').val(),
                dataType: "json",
                success: function (res) {
                    var maternalfollowupList = res.maternalfollowupList;
                    populateForm(maternalfollowupList);
                }
            }); //end of ajax call

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
    <li class="breadcrumb-item active">ANC /PNC Visit</li>
</ol>
<form id="lamisform" method="post" theme="css_xhtml" autocomplete="off">
    <input name="patientId" value="${patientId}" type="hidden" class="form-control" id="patientId"/>
    <input name="maternalfollowupId" value="${maternalfollowupId}" type="hidden" id="maternalfollowupId"/>
    <input name="ancId" value="${ancId}" type="hidden" id="ancId"/>
    <input name="facilityId" type="hidden" id="facilityId"/>
    <input name="partnerinformationId" type="hidden" id="partnerinformationId"/>

    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Hospital No</label>
                                <input name="hospitalNum" type="text" class="form-control bg-light" id="hospitalNum"
                                       readonly="readonly"/>
                                <span id="numHelp" style="color:red"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <p><br/>
                                    <span id="patientInfor"></span>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Date of Visit</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date1" type="text" class="form-control" id="date1"/>
                                </div>
                            </div>
                            <input name="dateVisit" type="hidden" class="form-control" id="dateVisit"/>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Body Weight (kg)</label>
                                <input name="bodyWeight" type="text" class="form-control" id="bodyWeight"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Blood Pressure (mmHg)</label>
                                <div class="row">
                                    <div class="col-md-6">
                                        <input name="bp1" type="text" class="form-control" id="bp1"/>
                                    </div>
                                    <div class="col-md-6">
                                        <input name="bp2" type="text" class="form-control" id="bp2"/>
                                    </div>
                                </div>
                                <input name="bp" type="hidden" class="form-control" id="bp"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Fundal Height (cm)</label>
                                <input name="fundalHeight" type="text" class="form-control" id="fundalHeight"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Fetal Presentation</label>
                            <select name="fetalPresentation" style="width:100%;" class="form-control"
                                    id="fetalPresentation">
                                <option>Select</option>
                                <option value="Cephalic">Cephalic</option>
                                <option value="Breech">Breech</option>
                                <option value="Face">Face</option>
                                <option value="Brow">Brow</option>
                                <option value="Footling">Footling</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Gestational Age (weeks)</label>
                            <input name="gestationalAge" type="text" class="form-control bg-white" id="gestationalAge"
                                   readOnly="readOnly"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Type of visit:</label>
                            <select name="typeOfVisit" style="width: 100%;" class="form-control" id="typeOfVisit">
                                <option></option>
                                <option>ANC</option>
                                <option>PNC</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Visit Status:</label>
                            <select name="visitStatus" style="width: 100%;" class="form-control" id="visitStatus">
                                <option></option>
                                <option>Active in PMTCT cohort</option>
                                <option>Transfer In</option>
                                <option>Transfer Out</option>
                                <option>Transfered to another PMTCT cohort (New Pregnancy)</option>
                                <option>Transitioned to ART clinic</option>
                                <option>Missed Appointment/Defaulted</option>
                                <option>Lost to Follow Up</option>
                                <option>Dead</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Viral Load sample collected:</label>
                            <select name="viralLoadCollected" class="form-control" id="viralLoadCollected">
                                <option></option>
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Date sample collected:</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <div class="input-group-text">
                                        <i class="fa fa-calendar"></i>
                                    </div>
                                </div>
                                <input name="date2" type="text" style="width: 100px;" class="form-control" id="date2"/>
                            </div>
                            <input name="dateSampleCollected" type="hidden" class="form-control"
                                   id="dateSampleCollected"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>TB Status</label>
                            <select name="tbStatus" style="width: 100%;" class="form-control" id="tbStatus">
                                <option></option>
                                <option>No sign or symptoms of TB</option>
                                <option>TB suspected and referred for evaluation</option>
                                <option>Currently on INH prophylaxis</option>
                                <option>Currently on TB treatment</option>
                                <option>TB positive not on TB drugs</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Date of Next Appointment:</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <div class="input-group-text">
                                        <i class="fa fa-calendar"></i>
                                    </div>
                                </div>
                                <input name="date4" type="text" class="form-control" id="date4"/>
                            </div>
                            <input name="dateNextVisit" type="hidden" class="form-control" id="dateNextVisit"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="card-title"><strong>Counseling / Others</strong></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 form-group">
                            <div class="form-check-inline">
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="counselNutrition" type="checkbox" value="1" id="counselNutrition"/>
                                        <span class="form-check-sign"></span>Nutritional Support
                                    </label>
                                </div>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="counselFeeding" type="checkbox" value="1" id="counselFeeding"/>
                                        <span class="form-check-sign"></span>Infant Feeding
                                    </label>
                                </div>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="counselFamilyPlanning" type="checkbox" value="1"
                                               id="counselFamilyPlanning"/>
                                        <span class="form-check-sign"></span>Family Planning
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Family Planning Method Used:</label>
                            <select name="familyPlanningMethod" style="width: 100%;" class="form-control"
                                    id="familyPlanningMethod">
                                <option></option>
                                <option>1 - No FP Method</option>
                                <option>2 - Condom</option>
                                <option>3 - Oral Pills</option>
                                <option>4 - Injectable</option>
                                <option>5 - Implant</option>
                                <option>6 - IUD</option>
                                <option>7 - Other</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label>Referred to:</label>
                            <select name="referred" style="width: 100%;" class="form-control" id="referred">
                                <option></option>
                                <option>ART</option>
                                <option>Pap Smear</option>
                                <option>Other</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="card-title"><strong>Partner Information</strong></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Agreed to Partner Notification:</label>
                            <select name="partnerNotification" style="width: 100%;" class="form-control"
                                    id="partnerNotification">
                                <option></option>
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label>Partner's HIV Status:</label>
                            <select name="partnerHivStatus" style="width: 100%;" class="form-control"
                                    id="partnerHivStatus">
                                <option></option>
                                <option>Positive</option>
                                <option>Negative</option>
                                <option>Unknown</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Partner referred to:</label>
                            <div class="form-check-inline">
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="fp" type="checkbox" value="1" id="fp"/>
                                        <span class="form-check-sign"></span>FP
                                    </label>
                                </div>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="art" type="checkbox" value="1" id="art"/>
                                        <span class="form-check-sign"></span>ART
                                    </label>
                                </div>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="others" type="checkbox" value="1" id="others"/>
                                        <span class="form-check-sign"></span>Others
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="save_button" type="button" class="btn btn-info">Save</button>
                        <button id="close_button" type="button" class="btn btn-default">Close</button>
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