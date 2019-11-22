<%-- 
    Document   : assessment_page
    Created on : Jun 19, 2019, 2:24:38 PM
    Author     : user10
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>

    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/assessment-common.js"></script>
    <script type="text/JavaScript">
        var obj = {};
        var date = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            $.ajax({
                url: "Assessment_retrieve.action",
                dataType: "json",
                success: function (assessmentList) {
                    populateForm(assessmentList);
                }
            });
        });
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_hts.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="Hts_search">Counseling and Testing Services</a></li>
        <li class="breadcrumb-item active" aria-current="page">HIV Risk Assessment Stratification</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-10 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <input name="riskScore" type="hidden" id="riskScore"/>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Client Code</label>
                                <input name="clientCode" type="text" class="form-control" id="clientCode"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of Assessment</label>
                                <input name="date1" type="text" style="width: 100%;" class="form-control"
                                       id="date1"/><input name="dateVisit" type="hidden" id="dateVisit"/><span
                                    id="dateHelp" class="errorspan"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">When was your most recent HIV test</label>
                                <select name="question1" style="width: 100%;" class="form-control select2"
                                        id="question1">
                                    <option></option>
                                    <option>Last 3 months</option>
                                    <option>Last 6 months</option>
                                    <option>More than 6 months</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">What was the result</label>
                                <select name="question2" style="width: 100%;" class="form-control select2"
                                        id="question2">
                                    <option></option>
                                    <option>Positive not on ART</option>
                                    <option>Positive on ART</option>
                                    <option>Negative</option>
                                    <option>Unknown</option>
                                    <option>Never Tested</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Have you had unprotected/condomless Penetrative
                                    sex(vaginal,anal,oral)in the last 6months</label>
                                <select name="question3" style="width: 100%;" class="form-control select2"
                                        id="question3">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Have you had more than one sexual partner known or unknown HIV
                                    positive status in the last 6months</label>
                                <select name="question4" style="width: 100%;" class="form-control select2"
                                        id="question4">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Have you been forced to have sex against your will(sexual
                                    abuse or rape)in the last 6months or since your last HIV test</label>
                                <select name="question5" style="width: 100%;" class="form-control select2"
                                        id="question5">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Have you paid for or received gratification/sold sex within
                                    the last 6months</label>
                                <select name="question6" style="width: 100%;" class="form-control select2"
                                        id="question6">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Do you currently have or have you been treated for any STI/STD
                                    in the last 6months</label>
                                <select name="question7" style="width: 100%;" class="form-control select2"
                                        id="question7">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                        <br>
                    </div>
                    <table>
                        <tr>
                            <td><input name="sti1" value="1" type="checkbox" id="sti1"/> Gonorrhea<span
                                    style="margin-left: 15px"></span></td>
                            <td><input name="sti2" value="1" type="checkbox" id="sti2"/> Chlamydia <span
                                    style="margin-left: 15px"></span></td>
                            <td><input name="sti3" value="1" type="checkbox" id="sti3"/> Syphilis <span
                                    style="margin-left: 15px"></span></td>
                            <td><input name="sti4" value="1" type="checkbox" id="sti4"/> Herpes<span
                                    style="margin-left: 15px"></span></td>

                            <td><input name="sti5" value="1" type="checkbox" id="sti5"/> Anal/Genital Warts of HPV <span
                                    style="margin-left: 15px"></span></td>
                            <td><input name="sti6" value="1" type="checkbox" id="sti6"/> Viral Hepatitis <span
                                    style="margin-left: 15px"></span></td>
                            <td><input name="sti7" value="1" type="checkbox" id="sti7"/> Tuberculosis (TB)<span
                                    style="margin-left: 15px"></span></td>
                            <td><input name="sti8" value="1" type="checkbox" id="sti8"/> Others <span
                                    style="margin-left: 15px"></span></td>
                        </tr>
                    </table>
                    <br>
                    <div class="row">
                        <div class="col-md-6">
                            <label class="form-group">Have you had the following symptoms in the last 6months?</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label"><br>(A) Persistent and unresolved cough >2 weeks, with weight
                                    loss, night sweats</label>
                                <select name="question8" style="width: 100%;" class="form-control select2"
                                        id="question8">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label"><br>(B) Prolonged and unresolved skin rash</label>
                                <select name="question9" style="width: 100%;" class="form-control select2"
                                        id="question9">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Have you injected drugs or Do you have a sexual or needle
                                    sharing partner with a known or Unknown HIV positive status? </label>
                                <select name="question10" style="width: 100%;" class="form-control select2"
                                        id="question10">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label"><br>Have you had a blood transfusion since your last HIV test?
                                </label>
                                <select name="question11" style="width: 100%;" class="form-control select2"
                                        id="question11">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Have you had Herpes zoster in the last 2 years</label>
                                <select name="question12" style="width: 100%;" class="form-control select2"
                                        id="question12">
                                    <option value=""></option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="save_button" disabled="true" class="btn btn-fill btn-info">HTS..</button>
                        <button id="close_button" class="btn btn-fill btn-default">Close</button>
                    </div>
                </div>
        </div>
    </div>
</div>
</form>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
