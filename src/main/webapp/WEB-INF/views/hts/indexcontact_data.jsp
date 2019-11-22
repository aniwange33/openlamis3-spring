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
    <title>LAMIS 2.6</title>

    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/hts-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <!--        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
            <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
            <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
            <script type="text/javascript" src="js/moment-with-locales.js"></script> -->
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

                window.location.href = "Indexcontact_search";
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
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_hts.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/indexcontact">Index contact Page</a></li>
        <li class="breadcrumb-item active" aria-current="page">Indexing and Contact Tracking</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-10 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Type of Contact</label>
                                <select name="contactType" style="width: 100%;" class="form-control select2"
                                        id="contactType">
                                    <option></option>
                                    <option>Primary Contacts</option>
                                    <option>Secondary Contacts</option>
                                    <option>Tertiary Contacts</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Surname:</label>
                                <td><input name="surname" type="text" class="form-control" id="surname"/>
                                    <span id="surnameHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Other Names:</label>
                                <input name="otherNames" type="text" class="form-control" id="otherNames"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="">Gender:</label>
                                <select name="gender" style="width: 100%;" class="form-control select2" id="gender">
                                    <option></option>
                                    <option>Male</option>
                                    <option>Female</option>
                                </select><span id="genderHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Age</label>
                            <div class="row">
                                <div class="col-6 form-group">
                                    <input name="age" type="text" class="form-control" id="age"/>
                                </div>
                                <div class="col-6 form-group">
                                    <select name="ageUnit" class="form-control select2" style="width: 100%;"
                                            id="ageUnit">
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
                                <label class="">Address</label>
                                <input name="address" type="text" style="width: 100%;" class="form-control"
                                       id="address"/></td>
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
                                <label class="form-label">Type of Relationship:</label>
                                <select name="relationship" style="width: 100%;" class="form-control select2"
                                        id="relationship">
                                    <option></option>
                                    <option>Spouse</option>
                                    <option>Live-in Partners</option>
                                    <option>Boyfriend/Girlfriend</option>
                                    <option>Regular Casual Partner</option>
                                    <option>Infrequent Casual Partner</option>
                                    <option>Sex Worker</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Mode of Notification:</label>
                                <select name="notification" style="width: 100%;" class="form-control select2"
                                        id="education">
                                    <option></option>
                                    <option>Passive Notification/Self</option>
                                    <option>Provider Assisted</option>
                                    <option>Contracted</option>
                                    <option>Household referral/Clustering</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Duration of Partnership(Months):</label>
                                <input name="durationPartner" type="text" style="width: 100%;" class="form-control"
                                       id="durationPartner"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">History of GBV:</label>
                                <select name="gbv" style="width: 100%;" class="form-control select2" id="gbv">
                                    <option></option>
                                    <option>Yes</option>
                                    <option>No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Phone Tracking</label>
                                <select name="phoneTracking" style="width: 100%;" class="form-control select2"
                                        id="phoneTracking">
                                    <option></option>
                                    <option>1st</option>
                                    <option>2nd</option>
                                    <option>3rd</option>
                                    <option>4th</option>
                                    <option>5th</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Home Visit:</label>
                                <select name="homeTracking" style="width: 100%;" class="form-control select2"
                                        id="homeTracking">
                                    <option></option>
                                    <option>1st</option>
                                    <option>2nd</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Tracking Outcome:</label>
                                <select name="outcome" style="width: 100%;" class="form-control select2" id="outcome">
                                    <option></option>
                                    <option>Accept HTS</option>
                                    <option>Declines HTS</option>
                                </select><span id="counselingHelp" class="errorspan"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date Contact accepts HIV test</label>
                                <td><input name="dateHivTest" style="width: 100%;" type="text" class="form-control"
                                           id="dateHivTest"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">HIV Status:</label>
                                <select name="hivStatus" style="width: 100%;" class="form-control select2"
                                        id="hivStatus">
                                    <option></option>
                                    <option>Negative</option>
                                    <option>Positive</option>
                                    <option>Previously Known</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Linked to HIV Care:</label>
                                <select name="linkCare" style="width: 100%;" class="form-control select2" id="linkCare">
                                    <option></option>
                                    <option>Yes</option>
                                    <option>No</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Agree to partner notification:</label>
                                <select name="partnerNotification" style="width: 100%;" class="form-control select2"
                                        id="partnerNotification">
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
                                <label class="form-label">Service Provided:</label>
                                <input name="serviceProvided" type="text" class="form-control" id="serviceProvided"/>
                            </div>
                        </div>
                    </div>
                    <div id="userGroup" style="display: none">
                    </div>
                    <!--<div id="buttons" style="width: 300px">-->
                    <div class="row">
                        <div class="col-md-12">
                            <div class="pull-right">
                                <button id="save_button" type="submit" class="btn btn-info">Save</button>
                                <!-- <button id="delete_button" disabled="true" type="submit" class="btn btn-fill btn-danger">Delete</button> -->
                                <button id="close_button" type="button" class="btn btn-default">Close</button>
                                <!--<button id="save_button">Save</button> &nbsp;<button id="delete_button" disabled="true"/>Delete</button> &nbsp;<button id="close_button"/>Close</button>-->
                            </div>
                        </div>
                    </div>
                </div>
        </div>
        </form>
    </div>
</div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</div>
</div>
</html>
