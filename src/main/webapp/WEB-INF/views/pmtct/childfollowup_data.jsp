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

    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/childfollowup-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <!--        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
            <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>       -->
    <script type="text/JavaScript">
        var date = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            $.ajax({
                url: "Childfollowup_retrieve.action",
                dataType: "json",
                success: function (childfollowupList) {
                    populateForm(childfollowupList);
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
    <li class="breadcrumb-item active">Child Follow-up</li>
</ol>
<form id="lamisform" method="post" theme="css_xhtml">
    <input name="childId" type="hidden" class="form-control" id="childId"/>
    <input name="childfollowupId" type="hidden" id="childfollowupId"/>
    <input name="dateBirth" type="hidden" id="dateBirth"/>
    <input name="patientId" type="hidden" class="form-control"
           id="patientId"/>
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div class="card-title">
                        <span id="patientInfor"></span>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Child's Hospital Number <span
                                        style="color:red">*</span></label>
                                <input name="hospitalNum" type="text" class="form-control" id="hospitalNum"
                                       required/>
                                <span id="numHelp" style="color:red"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
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
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Age (weeks)</label>
                                <input name="ageVisit" type="text" class="form-control" id="ageVisit"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Daily NVP Initiation Date</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date2" type="text" class="form-control" id="date2"/>
                                </div>
                                <input name="dateNvpInitiated" type="hidden" class="form-control"
                                       id="dateNvpInitiated"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Age at Initiation (weeks)</label>
                                <input name="ageNvpInitiated" type="text" class="form-control" id="ageNvpInitiated"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">CTX Initiation Date</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date3" type="text" class="form-control" id="date3"/>
                                </div>
                                <input name="dateCotrimInitiated" type="hidden" class="form-control"
                                       id="dateCotrimInitiated"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Age at Initiation (weeks)</label>
                                <input name="ageCotrimInitiated" type="text" class="form-control"
                                       id="ageCotrimInitiated"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Body Weight (kg)</label>
                                <input name="bodyWeight" type="text" class="form-control" id="bodyWeight"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Height (cm)</label>
                                <input name="height" type="text" class="form-control" id="height"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Feeding at Present</label>
                                <select name="feeding" class="form-control select2" style="width:100%;" id="feeding"/>
                                <option></option>
                                <option>1 - Exclusive Breast Feeding (EBF)</option>
                                <option>2 - Commercial Infant Formula/Exclusive
                                    Replacement Feeding
                                </option>
                                <option>3 - Mixed Feeding (MF)</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Covered with ARVs</label>
                                <select name="arv" class="form-control select2" style="width:100%;" id="arv">
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
                                <label class="form-label">On Cotrimoxazole</label>
                                <select name="cotrim" class="form-control select2" style="width:100%;" id="cotrim">
                                    <option></option>
                                    <option>Yes</option>
                                    <option>No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of PCR Sample Collection</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date4" type="text" class="form-control" id="date4"/>
                                </div>
                                <input name="dateSampleCollected" type="hidden" class="form-control"
                                       id="dateSampleCollected"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Reason for PCR</label>
                                <select name="reasonPcr" style="width: 100%;" class="form-control select2"
                                        id="reasonPcr"/>
                                <option></option>
                                <option>0 - 1st PCR</option>
                                <option>1 - Confirm Positive PCR</option>
                                <option>2 - Infant with Signs and Symptoms of HIV</option>
                                <option>3 - Follow-Up for Breastfed Child</option>
                                <option>4 - Previous Indeterminate Test</option>
                                <option>5 - Test +ve at 9 mths with rapid test kit</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date PCR Sample Sent</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date5" type="text" class="form-control" id="date5"/>
                                </div>
                                <input name="dateSampleSent" type="hidden" class="form-control" id="dateSampleSent"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date PCR Result Received</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date6" type="text" class="form-control" id="date6"/>
                                </div>
                                <input name="datePcrResult" type="hidden" class="form-control" id="datePcrResult"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">PCR Result</label>
                                <select name="pcrResult" class="form-control select2" style="width:100%;"
                                        id="pcrResult">
                                    <option></option>
                                    <option>P - Positive</option>
                                    <option>N - Negative</option>
                                    <option>I - Indeterminate</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Rapid Test Done</label>
                                <select name="rapidTest" class="form-control select2" style="width:100%;"
                                        id="rapidTest">
                                    <option></option>
                                    <option>Yes</option>
                                    <option>No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Rapid Test Result</label>
                                <select name="rapidTestResult" class="form-control select2" style="width:100%;"
                                        id="rapidTestResult">
                                    <option></option>
                                    <option>Positive</option>
                                    <option>Negative</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Caregiver given Result</label>
                                <select name="caregiverGivenResult" class="form-control select2" style="width:100%;"
                                        id="caregiverGivenResult">
                                    <option></option>
                                    <option>Yes</option>
                                    <option>No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Child Outcome</label>
                                <select name="childOutcome" class="form-control select2" style="width:100%;"
                                        id="childOutcome">
                                    <option></option>
                                    <option>1 - Well</option>
                                    <option>2 - Sick</option>
                                    <option>3 - Dead</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Referred to</label>
                                <select name="referred" class="form-control select2" style="width:100%;" id="referred">
                                    <option></option>
                                    <option>ART</option>
                                    <option>Nutrition</option>
                                    <option>Other</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Next Appointment</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date7" type="text" class="form-control" id="date7"/>
                                </div>
                                <input name="dateNextVisit" type="hidden" class="form-control" id="dateNextVisit"/>
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

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</body>
</html>
