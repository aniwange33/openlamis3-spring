<%-- 
    Document   : Facility
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
    <script type="text/JavaScript">
        var url = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $("body").bind('ajaxStart', function (event) {
                $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
            });

            $("body").bind('ajaxStop', function (event) {
                $("#loader").html('');
            });
            $("#messageBar").hide();

            $("#date1").mask("99/99/9999");
            $("#date1").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                yearRange: "-70:+0",
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });
            $("#date2").mask("99/99/9999");
            $("#date2").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                yearRange: "-70:+0",
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $("#date3").mask("99/99/9999");
            $("#date3").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                yearRange: "-70:+0",
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });
            $("#date4").mask("99/99/9999");
            $("#date4").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                yearRange: "-70:+0",
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $("#date5").mask("99/99/9999");
            $("#date5").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                yearRange: "-70:+0",
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });
            $("#date6").mask("99/99/9999");
            $("#date6").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                yearRange: "-70:+0",
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $("#ageBegin").mask("9?99", {placeholder: " "});
            $("#ageEnd").mask("9?99", {placeholder: " "});

            $("#cd4Begin").keypress(function (key) {
                if ((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
                    return false;
                }
                return true;
            })
            $("#cd4End").keypress(function (key) {
                if ((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
                    return false;
                }
                return true;
            })

            $("#viralloadBegin").keypress(function (key) {
                if ((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
                    return false;
                }
                return true;
            })
            $("#viralloadEnd").keypress(function (key) {
                if ((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
                    return false;
                }
                return true;
            })

            $.ajax({
                url: "RegimenType_retrieve_name.action?commence",
                dataType: "json",

                success: function (regimenTypeMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(regimenTypeMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#regimentype").html(options);

                }
            }); //end of ajax call

            $.ajax({
                url: "State_retrieve.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#state").html(options);
                }
            }); //end of ajax call

            $("#state").change(function (event) {
                $.ajax({
                    url: "Lga_retrieve.action",
                    dataType: "json",
                    data: {state: $("#state").val()},
                    success: function (lgaMap) {
                        var options = "<option value = '" + '' + "'>" + '' + "</option>";
                        $.each(lgaMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }) //end each
                        $("#lga").html(options);
                    }
                }); //end of ajax call
            });

            $("#currentStatus").bind("change", function () {
                var currentStatus = $("#currentStatus").val();
                if (currentStatus == "HIV negative" || currentStatus == "HIV+ non ART" || currentStatus == "Pre-ART Transfer In" || currentStatus == "Pre-ART Transfer Out") {
                    $("#regimentype").val("");
                    $("#regimentype").attr("disabled", true);
                    $("#date3").val("");
                    $("#date3").attr("disabled", true);
                    $("#date4").val("");
                    $("#date4").attr("disabled", true);
                } else {
                    $("#regimentype").attr("disabled", false);
                    $("#date3").attr("disabled", false);
                    $("#date4").attr("disabled", false);
                }
            });

            $("#ok_button").bind("click", function (event) {
                formatDateFields();
                event.preventDefault();
                event.stopPropagation();
                url = "";
                url += "gender=" + $("#gender").val();
                url += "&ageBegin=" + $("#ageBegin").val();
                url += "&ageEnd=" + $("#ageEnd").val();
                url += "&state=" + $("#state").val();
                url += "&lga=" + $("#lga").val();
                url += "&currentStatus=" + $("#currentStatus").val();
                url += "&dateCurrentStatusBegin=" + $("#dateCurrentStatusBegin").val();
                url += "&dateCurrentStatusEnd=" + $("#dateCurrentStatusEnd").val();
                url += "&regimentype=" + $("#regimentype").val();
                url += "&dateRegistrationBegin=" + $("#dateRegistrationBegin").val();
                url += "&dateRegistrationEnd=" + $("#dateRegistrationEnd").val();
                url += "&artStartDateBegin=" + $("#artStartDateBegin").val();
                url += "&artStartDateEnd=" + $("#artStartDateEnd").val();
                url += "&clinicStage=" + $("#clinicStage").val();
                url += "&cd4Begin=" + $("#cd4Begin").val();
                url += "&cd4End=" + $("#cd4End").val();
                url += "&viralloadBegin=" + $("#viralloadBegin").val();
                url += "&viralloadEnd=" + $("#viralloadEnd").val();
                //url += "&tbStatus="+$("#tbStatus").val();
                if ($('[name="reportFormat"]:checked').val() == "pdf") {
                    url = "Patient_list.action?" + url;
                    window.open(url);
                    return false;
                } else {
                    url += "&recordType=1&viewIdentifier=" + $("#viewIdentifier").prop("checked");
                    url = "Converter_dispatch.action?" + url;
                    convertData();
                }
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Home_page");
                return true;
            });
        });

        var x = function wait() {
            window.open(url);
        }


        function convertData() {
            $("#messageBar").hide();
            $("#ok_button").attr("disabled", true);
            $.ajax({
                url: url,
                dataType: "json",
                success: function (fileName) {
                    $("#messageBar").html("Conversion Completed").slideDown('fast');
                    url = fileName;
                    window.setTimeout(x, 3000);
                }
            });
            $("#ok_button").attr("disabled", false);
        }

        function formatDateFields() {
            $("#date1").datepicker("option", "altField", "#dateRegistrationBegin");
            $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
            $("#date2").datepicker("option", "altField", "#dateRegistrationEnd");
            $("#date2").datepicker("option", "altFormat", "mm/dd/yy");

            $("#date3").datepicker("option", "altField", "#artStartDateBegin");
            $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
            $("#date4").datepicker("option", "altField", "#artStartDateEnd");
            $("#date4").datepicker("option", "altFormat", "mm/dd/yy");

            $("#date5").datepicker("option", "altField", "#dateCurrentStatusBegin");
            $("#date5").datepicker("option", "altFormat", "mm/dd/yy");
            $("#date6").datepicker("option", "altField", "#dateCurrentStatusEnd");
            $("#date6").datepicker("option", "altFormat", "mm/dd/yy");
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_report.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item"><a href="/patient/patient_query_criteria">Report</a></li>
        <li class="breadcrumb-item active" aria-current="page">Patient Information Query</li>
    </ol>
</nav>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-10 ml-auto mr-auto">
            <div class="card">

                <div id="loader"></div>
                <div id="messageBar"></div>
                <div class="card-body">
                    <caption><h5>Demographic filters</h5></caption>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Gender:</label>
                                <select name="gender" class="form-control select2" style="width: 100%" id="gender">
                                    <option>--All--</option>
                                    <option>Male</option>
                                    <option>Female</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">Age:</label>
                                <input name="ageBegin" type="text" class="form-control" id="ageBegin"/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">To</label>
                                <input name="ageEnd" type="text" class="form-control" id="ageEnd"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">State:</label>
                                <select name="state" class="form-control select2" style="width: 100%" id="state">
                                    <option></option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">LGA of residence:</label>
                                <select name="lga" class="form-control select2" style="width: 100%" id="lga">
                                    <option></option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <caption><h5>Clinical filters</h5></caption>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Current Status:</label>
                                <select name="currentStatus" class="form-control select2" style="width: 100%"
                                        id="currentStatus">
                                    <option>--All--</option>
                                    <option>HIV&plus; non ART</option>
                                    <option>ART Start</option>
                                    <option>ART Restart</option>
                                    <option>ART Transfer In</option>
                                    <option>ART Transfer Out</option>
                                    <option>Pre-ART Transfer In</option>
                                    <option>Pre-ART Transfer Out</option>
                                    <option>Lost to Follow Up</option>
                                    <option>Stopped Treatment</option>
                                    <option>Known Death</option>
                                    <option>Currently Active</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">Date of Current Status:</label>
                                <input name="date5" type="text" class="form-control" id="date5"/>
                                <input name="dateCurrentStatusBegin" type="hidden" id="dateCurrentStatusBegin"/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">To</label>
                                <input name="date6" type="text" class="form-control" id="date6"/><input
                                    name="dateCurrentStatusEnd" type="hidden" id="dateCurrentStatusEnd"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Regimen Line:</label>
                                <select name="regimentype" class="form-control select2" style="width: 100%"
                                        id="regimentype"/>
                                <option></option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">Date of Registration:</label>
                                <input name="date1" type="text" class="form-control" id="date1"/><input
                                    name="dateRegistrationBegin" type="hidden" id="dateRegistrationBegin"/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">To</label>
                                <input name="date2" type="text" class="form-control" id="date2"/><input
                                    name="dateRegistrationEnd" type="hidden" id="dateRegistrationEnd"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">ART Start Date:</label>
                                <input name="date3" type="text" class="form-control" id="date3"/><input
                                    name="artStartDateBegin" type="hidden" id="artStartDateBegin"/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">To</label>
                                <input name="date4" type="text" class="form-control" id="date4"/><input
                                    name="artStartDateEnd" type="hidden" id="artStartDateEnd"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Current Clinical Stage:</label>
                                <select name="clinicStage" class="form-control select2" style="width: 100%"
                                        id="clinicStage">
                                    <option></option>
                                    <option>Stage I</option>
                                    <option>Stage II</option>
                                    <option>Stage III</option>
                                    <option>Stage IV</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">Current CD4 Count:</label>
                                <input name="cd4Begin" type="text" class="form-control" id="cd4Begin"/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">To</label>
                                <input name="cd4End" type="text" class="form-control" id="cd4End"/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">Current Viral Load:</label>
                                <input name="viralloadBegin" type="text" class="form-control" id="viralloadBegin"/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="form-label">To</label>
                                <input name="viralloadEnd" type="text" class="form-control" id="viralloadEnd"/>
                            </div>
                        </div>
                    </div>
                    <!--<td><label>TB Status:</label></td>
                    <td>
                        <select name="tbStatus" style="width: 170px;" class="inputboxes" id="tbStatus">
                            <option></option>
                            <option>No sign or symptoms of TB</option>
                            <option>TB suspected and referred for evaluation</option>
                            <option>Currently on INH prophylaxis</option>
                            <option>Currently on TB treatment</option>
                            <option>TB positive not on TB drugs</option>
                        </select>
                    </td>-->

                    <caption><h5>Report format</h5></caption>
                    <div class="row">
                        <div class="col-md-3">
                            <div class="form-group">
                                <div class="form-check form-check-radio">
                                    <label class="form-check-label">
                                        <input type="radio" name="reportFormat" value="pdf" checked
                                               class="form-check-input"/>
                                        <span class="form-check-sign"></span>
                                        Genetate Report in PDF Format
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <div class="form-check form-check-radio">
                                    <label class="form-check-label">
                                        <input type="radio" name="reportFormat" value="cvs" class="form-check-input"/>
                                        <span class="form-check-sign"></span> Generate Report and convert report to MS
                                        Excel
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-check mt-6">
                                <div class="form-group">
                                    <label class="form-check-label">
                                        <input name="viewIdentifier" type="checkbox" value="1" class="form-check-input"
                                               id="viewIdentifier"/>
                                        <span class="form-check-sign"></span>Unscramble patient identifiers like names,
                                        addresses and phone numbers</label>
                                </div>
                            </div>
                            <div class="pull-right">
                                <button id="ok_button" type="submit" class="btn btn-fill btn-info">Ok</button>
                                <button id="cancel_button" type="reset" class="btn btn-fill btn-default">Close</button>
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
</html>
