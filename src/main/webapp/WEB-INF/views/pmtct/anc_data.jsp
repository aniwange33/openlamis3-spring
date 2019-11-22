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
    <script type="text/javascript" src="/js/lamis/anc-common.js"></script>
    <script type="text/JavaScript">
        var date = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            $.ajax({
                url: "/api/anc/last-visit/patient/" + $('#patientId').val(),
                dataType: "json",
                success: function (res) {
                    var ancLast = res.ancList;
                    populateForm(ancLast);
                },
                error: function (error) {
                    console.log("There was an error! " + error.getMessage());
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
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item"><a href="Anc_search">PMTCT</a></li>
    <li class="breadcrumb-item active">First ANC Visit</li>
</ol>
<form id="lamisform" method="post" theme="css_xhtml" autocomplete="off">
    <input name="patientId" type="hidden" value="${patientId}" class="form-control" id="patientId"/>
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
                        <div class="col-md-6 form-group">
                            <label class="form-label">Hospital Number <span style="color:red">*</span></label>
                            <input name="hospitalNum" type="text" class="form-control"
                                   id="hospitalNum" required/>
                            <span id="numHelp" style="color:red"></span>
                        </div>
                        <div class="col-md-6">
                            <span id="patientInfor"></span>
                            <input name="uniqueId" type="hidden" id="uniqueId"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">ANC Number</label>
                            <input name="ancNum" type="text" class="form-control"
                                   id="ancNum"/>
                            <span id="numHelp" class="errorspan"></span>
                        </div>
                        <div class="col-md-6 form-group">
                            <label class="form-label">Date of Visit</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <div class="input-group-text">
                                        <i class="fa fa-calendar"></i>
                                    </div>
                                </div>
                                <input name="date1" type="text" class="form-control" id="date1"/>
                            </div>
                            <input name="dateVisit" type="hidden" class="form-control" id="dateVisit"/>
                            <span id="datevisitHelp" class="errorspan"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">Gravida</label>
                            <input name="gravida" type="text" class="form-control" id="gravida"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <label class="form-label">Parity</label>
                            <input name="parity" type="text" class="form-control" id="parity"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">L.M.P.</label>
                            <input name="date3" type="text" class="form-control" id="date3"/><span id="lmpHelp"
                                                                                                   class="errorspan"></span>
                            <input name="lmp" type="hidden" class="form-control" id="lmp"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <label class="form-label">E.D.D</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date4" type="text" class="form-control" id="date4"/>
                                    </div>
                                </div>
                                <div class="col-md-6 form-group">
                                    <label class="form-label">Gestational Age (weeks)</label>
                                    <input name="gestationalAge" type="text" class="form-control" id="gestationalAge"/>
                                </div>
                            </div>
                            <input name="edd" type="hidden" class="form-control" id="edd"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">Time of HIV Diagnosis</label>
                            <select name="timeHivDiagnosis" style="width: 100%;" class="form-control"
                                    id="timeHivDiagnosis">
                                <option></option>
                                <option>Newly Tested HIV+ (ANC)</option>
                                <option>Previous known HIV+ (ANC)</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label class="form-label">Source of Referral</label>
                            <select name="sourceReferral" style="width: 100%;" class="form-control" id="sourceReferral">
                                <option value="">Select</option>
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
                            <label>Date of Next Appointment:</label>
                            <input name="date2" type="text" class="form-control" id="date2"/>
                            <input name="dateNextAppointment" type="hidden" class="form-control"
                                   id="dateNextAppointment"/>
                            <span id="dateNextAppointmentHelp" class="errorspan"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="card-title bg-light"><strong>Syphilis Information</strong></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Tested:</label>
                            <select name="syphilisTested" style="width: 100%;" class="form-control" id="syphilisTested">
                                <option></option>
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Test Result:</label>
                            <select name="syphilisTestResult" style="width: 100%;" class="form-control"
                                    id="syphilisTestResult">
                                <option></option>
                                <option>P - Positive</option>
                                <option>N - Negative</option>
                                <option>I - Indeterminate</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Treated:</label>
                            <select name="syphilisTreated" style="width: 100%;" class="form-control"
                                    id="syphilisTreated">
                                <option></option>
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="card-title bg-light"><strong>Hepatitis Information</strong></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Tested for Hepatitis B:</label>
                            <select name="hepatitisBTested" style="width: 100%;" class="form-control"
                                    id="hepatitisBTested">
                                <option></option>
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Hepatitis B Test Result:</label>
                            <select name="hepatitisBTestResult" style="width: 100%;" class="form-control"
                                    id="hepatitisBTestResult">
                                <option></option>
                                <option>P - Positive</option>
                                <option>N - Negative</option>
                                <option>I - Indeterminate</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Tested for Hepatitis C:</label>
                            <select name="hepatitisCTested" style="width: 100%;" class="form-control"
                                    id="hepatitisCTested">
                                <option></option>
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Hepatitis C Test Result:</label>
                            <select name="hepatitisCTestResult" style="width: 100%;" class="form-control"
                                    id="hepatitisCTestResult">
                                <option></option>
                                <option>P - Positive</option>
                                <option>N - Negative</option>
                                <option>I - Indeterminate</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 form-group">
                            <div class="card-title bg-light"><strong>Partner Information</strong></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Agreed to Partner Notification:</label>
                            <select name="partnerNotification" style="width: 100%;" class="form-control"
                                    id="partnerNotification">
                                <option></option>
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
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
                        <div class="col-md-12 form-group">
                            <label>Partner referred to:</label>
                            <div class="form-check-inline">
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="fp" type="checkbox" value="1" id="fp" class="form-check-input"/>
                                        <span class="form-check-sign"></span>FP
                                    </label>
                                </div>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="art" type="checkbox" value="1" id="art" class="form-check-input"/>
                                        <span class="form-check-sign"></span>ART
                                    </label>
                                </div>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input name="others" type="checkbox" value="1" id="others"
                                               class="form-check-input"/>
                                        <span class="form-check-sign"></span>Others
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <input name="date5" type="hidden" style="width: 100px;" class="form-control" id="date5"/>
                    <input name="dateConfirmedHiv" type="hidden" class="form-control" id="dateConfirmedHiv"/>
                    <input name="date6" type="hidden" style="width: 100px;" class="form-control" id="date6"/>
                    <input name="dateArvRegimenCurrent" type="hidden" class="form-control" id="dateArvRegimenCurrent"/>

                    <div id="userGroup" style="display: none">
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

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
