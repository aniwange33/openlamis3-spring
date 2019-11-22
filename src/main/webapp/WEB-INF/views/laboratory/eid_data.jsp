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
    <script type="text/javascript" src="js/lamis/laboratory-common.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_laboratory.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item"><a href="Laboratory_page">Laboratory</a></li>
    <li class="breadcrumb-item active">EID Data</li>
</ol>
<form id="lamisform" theme="css_xhtml">
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
                        <div class="col-md-6 form-group">
                            <div class="card-title">Mothers Details</div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Name:</label>
                            <input name="motherName" type="text" style="width: 200px;" class="form-control"
                                   id="motherName"/>
                            <input name="eidId" type="hidden" id="eidId"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Address:</label>
                            <input name="motherAddress" type="text" style="width: 200px;" class="form-control"
                                   id="motherAddress"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Phone:</label>
                            <input name="motherPhone" type="text" class="form-control" id="motherPhone"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <div class="card-title">Sender Details</div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Name:</label>
                            <input name="senderName" type="text" style="width: 200px;" class="form-control"
                                   id="senderName"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Designation:</label>
                            <input name="senderDesignation" type="text" style="width: 150px;" class="form-control"
                                   id="senderDesignation"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Address:</label>
                            <input name="senderAddress" type="text" style="width: 200px;" class="form-control"
                                   id="senderAddress"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Phone:</label>
                            <input name="senderPhone" type="text" class="form-control" id="senderPhone"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <div class="card-title">Clinical Information</div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Reason for PCR:</label>
                            <select name="reasonPcr" style="width: 100%;" class="form-control select2" id="reasonPcr">
                                <option>Select</option>
                                <option>1st test for health exposed baby</option>
                                <option>1st test for sick baby</option>
                                <option>Repeat test after cessation of breastfeeding</option>
                                <option>Repeat because of problem with 1st test</option>
                                <option>Repeat to confirm 1st result</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Rapid Test Done?:</label>
                            <select name="rapidTestDone" style="width: 100px;" class="form-control" id="rapidTestDone">
                                <option>Select</option>
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Date Rapid Test Done:</label>
                            <input name="date4" type="text" style="width: 100px;" class="form-control" id="date4"/>
                            <input name="dateRapidTest" type="hidden" id="dateRapidTest"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Result of Rapid Test:</label>
                            <select name="rapidTestResult" style="width: 100%;" class="form-control select2"
                                    id="rapidTestResult">
                                <option></option>
                                <option>Negative</option>
                                <option>Positive</option>
                                <option>Indeterminate</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <div class="card-title">Intervention received by Mother during Pregnancy</div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>ART Received?:</label>
                            <select name="motherArtReceived" style="width: 100%;" class="form-control select2"
                                    id="motherArtReceived">
                                <option>Select</option>
                                <option>No</option>
                                <option>ART started during pregnancy</option>
                                <option>ART started before pregnancy</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>ARV Prophylaxis &nbsp;received by mother:</label>
                            <select name="motherProphylaxReceived" style="width: 100%;" class="form-control select2"
                                    id="motherProphylaxReceived">
                                <option>Select</option>
                                <option>No</option>
                                <option>AZT + 3TC + sdNVP in labour</option>
                                <option>AZT + sdNVP in labour</option>
                                <option>Single dose NVP in labour</option>
                                <option>Triple Regimen</option>
                                <option>Unknown</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            Intervention received by baby
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>ARV Received?:</label>
                            <select name="childProphylaxReceived" style="width: 100%;" class="form-control select2"
                                    id="childProphylaxReceived">
                                <option></option>
                                <option>No</option>
                                <option>AZT + NVP</option>
                                <option>NVP</option>
                                <option>Unknown</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Was baby ever breastfed?:</label>
                            <select name="breastfedEver" style="width: 100%;" class="form-control select2"
                                    id="breastfedEver">
                                <option>Select</option>
                                <option>No</option>
                                <option>Yes</option>
                                <option>Unknown</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Feeding Method:</label>
                            <select name="feedingMethod" style="width: 100%;" class="form-control select2"
                                    id="feedingMethod">
                                <option>Select</option>
                                <option>Exclusive breast feeding</option>
                                <option>Exclusive replacement feeding</option>
                                <option>Mixed feeding</option>
                            </select>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Is baby breastfeeding now?:</label>
                            <select name="breastfedNow" style="width: 100%;" class="form-control" id="breastfedNow">
                                <option>Select</option>
                                <option>No</option>
                                <option>Yes</option>
                                <option>Unknown</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Age (months) at &nbsp;cessation of &nbsp;breastfeeding:</label>
                            <input name="feedingCessationAge" type="text" style="width: 50px;" class="form-control"
                                   id="feedingCessationAge"/>
                        </div>
                        <div class="col-md-6 form-group">
                            <label>Cotrimoxazole given to baby?:</label>
                            <select name="cotrim" style="width: 100%;" class="form-control select2" id="cotrim">
                                <option>Select</option>
                                <option>No</option>
                                <option>Yes, taking CTX daily</option>
                                <option>Starting CTX today</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Next Clinic Appointment</label>
                            <input name="date5" type="text" style="width: 100px;" class="form-control" id="date5"/>
                            <input name="nextAppointment" type="hidden" id="nextAppointment"/>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="save_button" type="submit" class="btn btn-info">Save</button>
                        <a href="/Laboratory_search" class="btn btn-default">Close</a>
                    </div>
                    <div id="userGroup" style="display: none">
                    </div>
                    <div id="fromLabTest" style="display: none">
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
