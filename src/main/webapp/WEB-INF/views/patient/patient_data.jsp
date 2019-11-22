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
        <script type="text/javascript" src="/js/fingerprint.js"></script>
        <script type="text/javascript" src="/js/zebra.js"></script>
        <link type="text/css" rel="stylesheet" href="/css/zebra.css"/>
        <script type="text/javascript" src="/js/lamis/patient-common.js"></script>

    </head>
    <body>
    <jsp:include page="/WEB-INF/views/template/header.jsp"/>
    <jsp:include page="/WEB-INF/views/template/nav_home.jsp"/>
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/home">Home</a></li>
            <li class="breadcrumb-item"><a href="/patient">Patient View</a></li>
            <li class="breadcrumb-item active" aria-current="page">New Registration</li>
        </ol>
    </nav>
    <form id="lamisform" autocomplete="off">
        <div class="row">
            <div class="col-md-10 ml-auto mr-auto">
                <div class="card">
                    <div class="card-body">
                        <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="row">
                            <div class="col-12">
                                <div class="pull-right">
                                    <button id="dup_button" type="button" class="btn btn-info">Record Duplicate</button>
                                    <button id="enrol_button" type="button" class="btn btn-info">Enrol Fingerprint</button>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Hospital Number <span style="color:red">*</span></label>
                                    <input name="hospitalNum" type="text" class="form-control" style="width: 100%" id="hospitalNum"/>
                                    <span id="numHelp" class="errorspan"></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">&nbsp;</label>
                                    <input name="patientId" value="${patientId}" type="hidden" class="inputboxes" id="patientId"/>
                                    <input name="facilityId"  type="hidden" class="inputboxes" id="facilityId"/>
                                    <div id="hyperlink"><span id="changeNumberLink" style="color:blue"></span></div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Unique ID</label>
                                    <input name="uniqueId" type="text" class="form-control" id="uniqueId">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Date of Registration/Transfer-In<span
                                            style="color:black"></span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date2" type="text" class="form-control date" id="date2"/>
                                    </div>
                                    <input name="dateRegistration" type="hidden" class="form-control"
                                           id="dateRegistration"/>
                                    <span id="dateregHelp" class="errorspan"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Surname <span style="color:red"></span></label>
                                    <input name="surname" type="text" class="form-control" id="surname"/>
                                    <span id="surnameHelp" class="errorspan"></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Other Names</label>
                                    <input name="otherNames" type="text" class="form-control"
                                           id="otherNames"/>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Date of Birth<span style="color:red"></span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date1" type="text" class="form-control" id="date1"/>
                                    </div>
                                    <input name="dateBirth" type="hidden" class="form-control" id="dateBirth"/>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Age at Registration</label>
                                <div class="row">
                                    <div class="col-6 form-group">
                                        <input name="age" type="text" class="form-control" id="age"/>
                                    </div>
                                    <div class="col-6 form-group">
                                        <select name="ageUnit" class="form-control" style="width: 100%;" id="ageUnit">
                                            <option value="year(s)">Year(s)</option>
                                            <option value="month(s)">Month(s)</option>
                                            <option value="day(s)">Day(s)</option>
                                        </select>
                                    </div>
                                </div>
                                <span id="numHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="row ">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Gender</label>
                                    <select name="gender" class="form-control select2" style="width: 100%;" id="gender">
                                        <option></option>
                                        <option>Male</option>
                                        <option>Female</option>
                                    </select><span id="genderHelp" class="errorspan"></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Marital Status</label>
                                    <select name="maritalStatus" class="form-control select2" style="width: 100%;"
                                            id="maritalStatus">
                                        <option></option>
                                        <option>Single</option>
                                        <option>Married</option>
                                        <option>Windowed</option>
                                        <option>Separated</option>
                                        <option>Divorced</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Job/Occupation Status</label>
                                    <select name="Occupation" class="form-control select2" style="width: 100%;"
                                            id="Occupation">
                                        <option></option>
                                        <option>Unemployed</option>
                                        <option>Employed</option>
                                        <option>Student</option>
                                        <option>Retired</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Educational Level</label>
                                    <select name="education" class="form-control select2" style="width: 100%;"
                                            id="education">
                                        <option></option>
                                        <option>None</option>
                                        <option>Primary</option>
                                        <option>Senior Secondary</option>
                                        <option>Quranic</option>
                                        <option>Junior Secondary</option>
                                        <option>Post Secondary</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">State of residence</label>
                                    <select name="state" class="form-control select2" style="width: 100%;" id="state">
                                        <option value=""></option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">LGA of residence</label>
                                    <select name="lga" class="form-control select2" style="width: 100%;" id="lga">
                                        <option value=""></option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Address<span style="color:red"></span></label>
                                    <input name="address" type="text" class="form-control" id="address">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Telephone</label>
                                    <input name="phone" type="text" class="form-control" id="phone"/>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Care Entry Point</label>
                                    <select name="entryPoint" class="form-control select2" style="width: 100%;"
                                            id="entryPoint">
                                        <option></option>
                                        <option>OPD</option>
                                        <option>In-patient</option>
                                        <option>HCT</option>
                                        <option>TB DOTS</option>
                                        <option>STI Clinic</option>
                                        <option>PMTCT</option>
                                        <option>Transfer-in</option>
                                        <option>Outreach</option>
                                        <option>CBO</option>
                                        <option>Others</option>
                                    </select><span id="entryHelp" class="errorspan"></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Date of Confirmed HIV Test<span
                                            style="color:black"></span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date3" type="text" class="form-control" id="date3"/>
                                    </div>
                                    <input name="dateConfiredHiv" type="hidden" class="form-control" id="dateConfiredHiv"/>
                                    <span id="ConfirmedHelp" class="errorspan"></span>
                                </div>
                            </div>
                        </div>
                        <div id="pmtct">
                            <div class="row" id="ancNo">
                                <div class="col-md-6 form-group">
                                    <label>Time of HIV Diagnosis:</label>
                                    <select name="timeHivDiagnosis" class="form-control select2" style="width: 100%"
                                            id="timeHivDiagnosis">
                                        <option></option>
                                        <option>Previous - Non pregnant</option>
                                        <option>Previous pregnancy (ANC)</option>
                                        <option>Previous pregnancy (L&AMP;D)</option>
                                        <option>Previous pregnancy (PP &lt;72hrs)</option>
                                        <option>ANC</option>
                                        <option>Labour</option>
                                        <option>Post Partum &lt;=72hrs</option>
                                        <option>Post Partum &gt;72hrs</option>
                                    </select>
                                </div>
                                <div class="col-md-6 form-group pull-right">
                                    <label>Date enrolled into PMTCT:</label><span id="datepmtctHelp"
                                                                                  class="errorspan"></span>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date4" type="text" class="form-control" id="date4"/>
                                    </div>
                                    <input name="dateEnrolledPmtct" type="hidden" class="form-control"
                                           id="dateEnrolledPmtct"/>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Source of Referral:</label>
                                        <select name="sourceReferral" style="width: 100%;" class="form-control select2"
                                                id="sourceReferral">
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
                                <div class="col-6">
                                    <div class="form-group">
                                        <label class="form-label">Enrollment Setting</label>
                                        <select name="enrollmentSetting" class="form-control select2" style="width: 100%;"
                                                id="enrollmentSetting">
                                            <option></option>
                                            <option>Facility</option>
                                            <option>Community</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">HIV Status at Registration</label>
                                        <select name="statusRegistration" class="form-control select2" style="width: 100%;"
                                                id="statusRegistration">
                                            <option></option>
                                            <option>HIV Exposed Status Unknown</option>
                                            <option>HIV+ Non ART</option>
                                            <option>ART Transfer In</option>
                                            <option>Pre-ART Transfer In</option>
                                        </select>
                                        <span id="statusregHelp" class="errorspan"></span>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">TB Status<span style="color:red"></span></label>
                                        <select name="tbStatus" class="form-control select2" style="width: 100%;"
                                                id="tbStatus">
                                            <option></option>
                                            <option>No sign or symptoms of TB</option>
                                            <option>TB suspected and referred for evaluation</option>
                                            <option>Currently on INH prophylaxis</option>
                                            <option>Currently on TB treatment</option>
                                            <option>TB positive not on TB drugs</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Pregnancy Status</label>
                                        <select name="pregnantStatus" style="width: 100%;" class="form-control select2"
                                                id="pregnantStatus">
                                            <option></option>
                                            <option value="1">Not Pregnant</option>
                                            <option value="2">Pregnant</option>
                                            <option value="3">Breastfeeding</option>
                                            <span id="pregHelp" class="errorspan"></span>
                                        </select>
                                        <input name="currentStatus" type="hidden" id="currentStatus"/>
                                        <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus">
                                    </div>
                                </div>
                            </div>
                            <hr/>
                            <div class="divider">
                                <Strong>Next of kin/Treatment Supporter</strong>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <div class="form-group">
                                            <label class="form-label">Name</label>
                                            <input name="nextKin" type="text" class="form-control"
                                                   id="nextKin"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">Relationship</label>
                                        <select name="relationKin" class="form-control select2" style="width: 100%;"
                                                id="relationKin">
                                            <option></option>
                                            <option>Aunt</option>
                                            <option>Brother</option>
                                            <option>Cousin</option>
                                            <option>Daughter</option>
                                            <option>Father</option>
                                            <option>Friend</option>
                                            <option>Grand parent</option>
                                            <option>Mother</option>
                                            <option>Son</option>
                                            <option>Spouse</option>
                                            <option>Treatment Supporter</option>
                                            <option>Uncle</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">Address<span style="color:red"></span></label>
                                        <input name="addressKin" type="text" class="form-control" id="addressKin"/>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">Telephone</label>
                                        <input name="phoneKin" type="text" class="form-control" id="phoneKin"/>
                                    </div>
                                </div>
                            </div>
                            <div id="dialog">
                                <div class="row">
                                    <div class="col-md-6">
                                        <label>New Hospital No</label>
                                        <input name="newHospitalNum" type="text" class="form-control" id="newHospitalNum"/>
                                        <span id="newHelp" style="color:red"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="pull-right">
                                        <div class="pull-right">
                                            <button id="save_button" type="button" class="btn btn-info">Save</button>
                                            <button id="close_button" type="button" class="btn btn-default">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
    </body>
    <script type="text/JavaScript">
        var obj = {};
        var date = "";
        var updateRecord = false;
        var enablePadding = true;

        $(document).ready(function () {
            resetPage();
            initialize();
            reports();
            enablePadding = padHospitalNum();
            anc();

            $("#enrol_button").bind("click", function (event) {
                var cmp = new FingerprintComponent();
                var result = function (response) {
                    return !(!!response && response.patientId);
                };
                cmp.enrol($("#id").val(), result);
                return false;
            });
            if (!$('#patientId').val()) {
                $('#enrol_button').hide();
            }
            $("#dup_button").bind("click", function (event) {
                var cmp = new FingerprintComponent();
                cmp.recordDuplicate($("#id").val());
                return false;
            }).hide();

            $("#dialog").dialog({
                title: "Change hospital number",
                autoOpen: false,
                width: 400,
                resizable: false,
                buttons: [{text: "Change", click: changeNumber},
                    {
                        text: "Cancel", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            $.ajax({
                url: "/api/patient/" + $("#patientId").val(),
                method : "GET",
                dataType: "json",
                success: function (response) {
                    populateForm(response.patientList);
                }
            });

            $("#save_button").bind("click", function(event){
                event.preventDefault();
                if(validateForm()) {
                    $.ajax({
                        url : "api/patient",
                        method : "POST",
                        data : $("#lamisform").serialize(),
                        dataType: "json",
                        success : function(response) {
                            window.location.href = "/patient";
                        }
                    });
                }
            });
            $("#close_button").bind("click", function (event) {
                event.preventDefault();
                window.location.href = "/patient";
            });
        });
    </script>
</html>
