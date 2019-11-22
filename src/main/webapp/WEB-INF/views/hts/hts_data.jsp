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
    <script type="text/javascript" src="/js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="/js/lamis/hts-common.js"></script>
    <script type="text/javascript" src="/js/lamis/report-common.js"></script>
    <script type="text/JavaScript">
        var obj = {};
        var date = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            $.ajax({
                url: "Hts_retrieve.action",
                dataType: "json",
                success: function (htsList) {
                    populateForm(htsList);
                }
            }); //end of ajax call

            $("#date1").bind("change", function (event) {
                calculateAge();
            });
            $("#date2").bind("change", function (event) {
                //Check that the age is proper.
                var dated = $("#date2").val();
                if (dated.length != 0) {
                    var age = $("#age").val();
                    var ageUnit = $("#ageUnit").val();
                    if (age !== '' && ageUnit !== '') {
                        var date = $("#date1").val();
                        if (date === '') {
                            if (ageUnit === 'day(s)') ageUnit = 'days';
                            else if (ageUnit === 'month(s)') ageUnit = 'months';
                            else if (ageUnit === 'year(s)') ageUnit = 'years';
                            calculateDateOfBirth(age, ageUnit);
                            var date = $("#date1").val();
                        } else {
                            calculateAge();
                        }
                    } else {
                        var date = $("#date1").val();
                        if (date !== '')
                            calculateAge();
                    }
                }
            });
            $("#age").bind("change", function (event) {
                var date = $("#date2").val();
                var age = $("#age").val();
                var ageUnit = $("#ageUnit").val();
                if (date !== '' && ageUnit !== '') {
                    if (ageUnit === 'day(s)') ageUnit = 'days';
                    else if (ageUnit === 'month(s)') ageUnit = 'months';
                    else if (ageUnit === 'year(s)') ageUnit = 'years';
                    calculateDateOfBirth(age, ageUnit);
                }
            });

            $("#ageUnit").bind("change", function (event) {
                var date = $("#date2").val();
                var age = $("#age").val();
                var ageUnit = $("#ageUnit").val();
                if (date !== '' && age !== '') {
                    if (ageUnit === 'day(s)') ageUnit = 'days';
                    else if (ageUnit === 'month(s)') ageUnit = 'months';
                    else if (ageUnit === 'year(s)') ageUnit = 'years';
                    calculateDateOfBirth(age, ageUnit);
                }
            });

            $("#close_button").click(function (event) {

                window.location.href = "Hts_search";
            });

            $("#save_button").bind("click", function (event) {
                console.log('Click called');

                $("#lamisform").submit();
                return true;
            });
        });

        function calculateAge() {
            if ($("#date1").val().length != 0 && $("#date2").val().length != 0) {
                if (parseInt(compare($("#date1").val(), $("#date2").val())) == -1) {
                    var message = "Date of visit cannot be ealier than client's date of birth";
                    $("#messageBar").html(message).slideDown('slow');
                } else {
                    $("#messageBar").slideUp('slow');
                    var diff = dateDiff($("#date1").val(), $("#date2").val(), "years");
                    if (parseInt(diff) < 1) {
                        diff = dateDiff($("#date1").val(), $("#date2").val(), "months");
                        if (diff < 1) diff = 1;
                        $("#ageUnit").val("month(s)");
                    } else if (parseInt(diff) == 1) {
                        diff = dateDiff($("#date1").val(), $("#date2").val(), "months");
                        if (diff == 12) {
                            diff = 1;
                            $("#ageUnit").val("year(s)");
                        } else {
                            $("#ageUnit").val("month(s)");
                        }
                    } else {
                        $("#ageUnit").val("year(s)");
                    }
                    $("#age").val(diff);
                }
            }
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_hts.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/hts">Home</a></li>
        <li class="breadcrumb-item"><a href="/hts">Hts Data Page</a></li>
        <li class="breadcrumb-item active" aria-current="page">Counseling &AMP; Testing Services</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <div class="card-body">
                    <input type="hidden" name="htsId" id="htsId">
                    <div class="row">
                        <div class="col-md-4 ml-auto mr-auto">
                            <ul class="nav nav-pills nav-pills-primary flex-column" role="tablist">
                                <li class="nav-item">
                                    <a class="nav-link active show text-left" data-toggle="tab" href="#link1"
                                       role="tablist">
                                        Client Details
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link2" role="tablist">
                                        Knowledge Assessment
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link3" role="tablist">
                                        HIV Risk Assessment
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link4" role="tablist">
                                        Clinical TB Screening
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link5" role="tablist">
                                        Syndromic STI Screening
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link6" role="tablist">
                                        Post Test Counseling
                                    </a>
                                </li>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link text-left" data-toggle="tab" href="#link7" role="tablist">
                                        Syphilis
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <div class="col-md-8">
                            <div class="tab-content tab-space">
                                <div class="tab-pane active show" id="link1">
                                    <h5><strong>Client Details</strong></h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Client Code:</label>
                                                <input name="clientCode" type="text" class="form-control"
                                                       id="clientCode"/><span id="numHelp" class="errorspan"></span>
                                                <input name="htsId" type="hidden" class="form-control" id="htsId"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <br/>
                                                <span id="patientInfor"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Testing Setting:</label>
                                                <select name="testingsetting" style="width: 100%;"
                                                        class="form-control select2" id="testingsetting">
                                                    <option></option>
                                                    <option>CT</option>
                                                    <option>TB</option>
                                                    <option>STI</option>
                                                    <option>FP</option>
                                                    <option>OPD</option>
                                                    <option>Ward</option>
                                                    <option>Outreach</option>
                                                    <option>Standalone HTS</option>
                                                    <option>Others</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Referred From:</label>
                                                <select name="referred" style="width: 100%;"
                                                        class="form-control select2" id="referred">
                                                    <option></option>
                                                    <option>Self</option>
                                                    <option>TB</option>
                                                    <option>STI</option>
                                                    <option>FP</option>
                                                    <option>OPD</option>
                                                    <option>Ward</option>
                                                    <option>Blood Bank</option>
                                                    <option>Others</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> First Time Visit:</label>
                                                <select name="firstTimeVisit" style="width: 100%;"
                                                        class="form-control select2" id="firstTimeVisit">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Visit:</label>
                                                <input name="date1" type="text" style="width: 100%;"
                                                       class="form-control" id="date1"/>
                                                <input name="dateVisit" type="hidden" id="dateVisit"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> Surname:</label>
                                                <input name="surname" type="text" class="form-control"
                                                       id="surname"/><span id="surnameHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Other Names:</label>
                                                <input name="othernames" type="text" class="form-control"
                                                       id="othernames"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Date of Birth:</label>
                                                <input name="date2" type="text" style="width: 100%;"
                                                       class="form-control" id="date2"/>
                                                <input name="datebirth" type="hidden" id="datebirth"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Age</label>
                                            <div class="row">
                                                <div class="col-6 form-group">
                                                    <input name="age" type="text" class="form-control" id="age"/>
                                                </div>
                                                <div class="col-6 form-group">
                                                    <select name="ageUnit" class="form-control select2"
                                                            style="width: 100%;" id="ageUnit">
                                                        <option value="year(s)">Year(s)</option>
                                                        <option value="month(s)">Month(s)</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <span id="numHelp" class="errorspan"></span>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Gender:</label>
                                                <select name="gender" style="width: 100%;" class="form-control select2"
                                                        id="gender">
                                                    <option></option>
                                                    <option>Male</option>
                                                    <option>Female</option>
                                                </select><span id="genderHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Marital Status:</label>
                                                <select name="maritalStatus" style="width: 100%;"
                                                        class="form-control select2" id="maritalStatus">
                                                    <option></option>
                                                    <option>Single</option>
                                                    <option>Married</option>
                                                    <option>Widowed</option>
                                                    <option>Separated</option>
                                                    <option>Divorced</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">No. of Children < 5 years:</label>
                                                <input name="numChildren" type="text" style="width: 100%;"
                                                       class="form-control" id="numChildren"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">No. of Wives/Co-wives:</label>
                                                <input name="numWives" type="text" class="form-control" id="numWives"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Job/Occupational Status:</label>
                                                <select name="occupation" style="width: 100%;"
                                                        class="form-control select2" id="occupation">
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
                                                <label class="form-label">Educational Level:</label>
                                                <select name="education" style="width: 100%;"
                                                        class="form-control select2" id="education">
                                                    <option></option>
                                                    <option>None</option>
                                                    <option>Primary</option>
                                                    <option>Senior Secondary</option>
                                                    <option>Quaranic</option>
                                                    <option>Junior Secondary</option>
                                                    <option>Post Secondary</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">State of residence:</label>
                                                <select name="state" style="width: 100%;" class="form-control select2"
                                                        id="state">
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">LGA of residence:</label>
                                                <select name="lga" style="width: 100%;" class="form-control select2"
                                                        id="lga">
                                                    <option></option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> Address:</label>
                                                <input name="address" type="text" style="width: 100%;"
                                                       class="form-control" id="address"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> Telephone:</label>
                                                <input name="phone" type="text" class="form-control" id="phone"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Type of Session:</label>
                                                <select name="typeCounseling" style="width: 100%;"
                                                        class="form-control select2" id="typeCounseling">
                                                    <option></option>
                                                    <option>Individual</option>
                                                    <option>Couple</option>
                                                </select><span id="counselingHelp" class="errorspan"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Is client identified from an index
                                                    client:</label>
                                                <select name="indexClient" style="width: 100%;"
                                                        class="form-control select2" id="indexClient">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Index Testing:</label>
                                                <select name="typeIndex " style="width: 100%;"
                                                        class="form-control select2" id="typeIndex ">
                                                    <option></option>
                                                    <option>Biological</option>
                                                    <option>Sexual</option>
                                                    <option>Social</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Index Client ID</label>
                                                <input name="indexClientCode" type="text" class="form-control"
                                                       id="indexClientCode"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="link2">
                                    <h5><strong>Knowledge Assessment</strong></h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Previously tested HIV negative</label>
                                                <select name="knowledgeAssessment1" style="width: 100%;"
                                                        class="form-control select2" id="knowledgeAssessment1">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Client Pregnant (Test and ensure linkage to
                                                    PMTCT program)</label>
                                                <select name="knowledgeAssessment2" style="width: 100%;"
                                                        class="form-control select2" id="knowledgeAssessment2">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Client informed about HIV transmission
                                                    routes</label>
                                                <select name="knowledgeAssessment3" style="width: 100%;"
                                                        class="form-control select2" id="knowledgeAssessment3">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Client informed about risk factors for HIV
                                                    transmission</label>
                                                <select name="knowledgeAssessment4" style="width: 100%;"
                                                        class="form-control select2" id="knowledgeAssessment4">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Client informed on preventing HIV transmission
                                                    method</label>
                                                <select name="knowledgeAssessment5" style="width: 100%;"
                                                        class="form-control select2" id="knowledgeAssessment5">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> Client informed about possible test
                                                    results</label>
                                                <select name="knowledgeAssessment6" style="width: 100%;"
                                                        class="form-control select2" id="knowledgeAssessment6">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Informed consent for HIV test given</label>
                                                <select name="knowledgeAssessment7" style="width: 100%;"
                                                        class="form-control select2" id="knowledgeAssessment7">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="link3">
                                    <h5><strong>HIV Risk Assessment</strong></h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Ever had sexual intercourse</label>
                                                <select name="riskAssessment1" style="width: 100%;"
                                                        class="form-control select2" id="riskAssessment1">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> Blood transfusion in last 3 months</label>
                                                <select name="riskAssessment2" style="width: 100%;"
                                                        class="form-control select2" id="riskAssessment2">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Unprotected sex with casual partner in last 3
                                                    months</label>
                                                <select name="riskAssessment3" style="width: 100%;"
                                                        class="form-control select2" id="riskAssessment3">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-6">
                                            <div class="form-group">
                                                <label class="form-label"> Unprotected sex with regular partner in last
                                                    3 months</label>
                                                <select name="riskAssessment4" style="width: 100%;"
                                                        class="form-control select2" id="riskAssessment4">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> STI in last 3 months</label>
                                                <select name="riskAssessment5" style="width: 100%;"
                                                        class="form-control select2" id="riskAssessment5">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">More than 1 sex partner in last 3
                                                    months</label>
                                                <select name="riskAssessment6" style="width: 100%;"
                                                        class="form-control select2" id="riskAssessment6">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="tab-pane" id="link4">
                                    <h5><strong>Clinical TB Screening</strong></h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> Current Cough:</label>
                                                <select name="tbScreening1" style="width: 100%;"
                                                        class="form-control select2" id="tbScreening1">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label"> Weight Loss</label>
                                                <select name="tbScreening2" style="width: 100%;"
                                                        class="form-control select2" id="tbScreening2">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Fever</label>
                                                <select name="tbScreening3" style="width: 100%;"
                                                        class="form-control select2" id="tbScreening3">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Night Sweat</label>
                                                <select name="tbScreening4" style="width:100%;"
                                                        class="form-control select2" id="tbScreening4">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="link5">
                                    <h5><strong>Syndromic STI Screening</strong></h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Female: Vagina discharge</label>
                                                <select name="stiScreening2" style="width: 100%;"
                                                        class="form-control select2" id="stiScreening2">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Female: Level abdominal pains</label>
                                                <select name="stiScreening1" style="width: 100%;"
                                                        class="form-control select2" id="stiScreening1">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Male: Urethral discharge</label>
                                                <select name="stiScreening3" style="width: 100%;"
                                                        class="form-control select2" id="stiScreening3">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Complain of genital sore(s)</label>
                                                <select name="stiScreening5" style="width: 100%;"
                                                        class="form-control select2" id="stiScreening5">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Male: Scrotal swelling and pain</label>
                                                <select name="stiScreening4" style="width: 100%;"
                                                        class="form-control select2" id="stiScreening4">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="link6">
                                    <h5><strong>Post Test Counseling</strong></h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">HIV test result</label>
                                                <select name="hivTestResult" style="width: 100%;"
                                                        class="form-control select2" id="hivTestResult">
                                                    <option></option>
                                                    <option>Negative</option>
                                                    <option>Positive</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Tested for HIV before within the year</label>
                                                <select name="testedHiv" style="width: 100%;"
                                                        class="form-control select2" id="testedHiv">
                                                    <option></option>
                                                    <option>Not Previously Tested</option>
                                                    <option>Previously Tested Negative</option>
                                                    <option>Previously Tested Positive in HIV Care</option>
                                                    <option>Previously Tested Positive Not in HIV Care</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">HIV request and result form signed by
                                                    tester</label>
                                                <select name="postTest1" style="width: 100%;"
                                                        class="form-control select2" id="postTest1">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">HIV request and result form filled with CT
                                                    intake</label>
                                                <select name="postTest2" style="width: 100%;"
                                                        class="form-control select2" id="postTest2">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Client received HIV test result</label>
                                                <select name="postTest3" style="width: 100%;"
                                                        class="form-control select2" id="postTest3">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Post test counseling done</div>
                                            <select name="postTest4" style="width: 100%;" class="form-control select2"
                                                    id="postTest4">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Risk reduction plan developed</label>
                                                <select name="postTest5" style="width: 100%;"
                                                        class="form-control select2" id="postTest5">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Post test disclosure plan developed</label>
                                                <select name="postTest6" style="width: 100%;"
                                                        class="form-control select2" id="postTest6">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Will bring partner(s)for HIV testing</label>
                                                <select name="postTest7" style="width: 100%;"
                                                        class="form-control select2" id="postTest7">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Will bring own children < 5 years for HIV
                                                    testing</label>
                                                <select name="postTest8" style="width: 100%;"
                                                        class="form-control select2" id="postTest8">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Provided with information of FP and dual
                                                    contraception</label>
                                                <select name="postTest9" style="width: 100%;"
                                                        class="form-control select2" id="postTest9">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Client/partner use FP methods (other than
                                                condom)</label>
                                            <select name="postTest10" style="width: 100%;" class="form-control select2"
                                                    id="postTest10">
                                                <option></option>
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Client/partner use condom as (one) FP
                                                    methods</label>
                                                <select name="postTest11" style="width: 100%;"
                                                        class="form-control select2" id="postTest11">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Correct condom use demonstrated</label>
                                                <select name="postTest12" style="width: 100%;"
                                                        class="form-control select2" id="postTest12">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Condom provided to client</label>
                                                <select name="postTest13" style="width: 100%;"
                                                        class="form-control select2" id="postTest13">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Client referred for other services</label>
                                                <select name="postTest14" style="width: 100%;"
                                                        class="form-control select2" id="postTest14">
                                                    <option></option>
                                                    <option>Yes</option>
                                                    <option>No</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="link7">
                                    <h5><strong>Syphilis</strong></h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Syphilis Test</label>
                                                <select name="syphilisTestResult" style="width: 100%;"
                                                        class="form-control select2" id="syphilisTestResult">
                                                    <option></option>
                                                    <option>Non-Reactive</option>
                                                    <option>Reactive</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Hepatitis B</label>
                                                <select name="hepatitisbTestResult" style="width: 100%;"
                                                        class="form-control select2" id="hepatitisbTestResult">
                                                    <option></option>
                                                    <option>Negative</option>
                                                    <option>Positive</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="form-label">Hepatitis C</label>
                                                <select name="hepatitiscTestResult" style="width: 100%;"
                                                        class="form-control select2" id="hepatitiscTestResult">
                                                    <option></option>
                                                    <option>Negative</option>
                                                    <option>Positive</option>
                                                </select>
                                            </div>
                                        </div>
                                        <!--</div>-->
                                        <div class="pull-right">
                                            <button id="save_button" type="submit" class="btn btn-fill btn-info">Save
                                            </button>
                                            <button id="close_button" type="button" class="btn btn-fill btn-default"/>
                                            Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>


</html>
