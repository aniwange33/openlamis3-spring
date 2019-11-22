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
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $("#date1").mask("99/99/9999");
            $("#date1").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $("#date2").mask("99/99/9999");
            $("#date2").datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                constrainInput: true,
                buttonImageOnly: true,
                buttonImage: "/images/calendar.gif"
            });

            $.ajax({
                url: "Labtest_retrieve_map.action",
                dataType: "json",
                success: function (labtestMap) {
                    var options = "";
                    $.each(labtestMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#labtestId").html(options);
                }
            }); //end of ajax call    

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    url = "Laboratory_query.action?labtestId=" + $("#labtestId").val() + "&description=" + $("#labtestId option:selected").text() + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();
                    window.open(url);
                }
                return false;
            });
            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Laboratory_page");
                return true;
            });
        });

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            if ($("#date1").val().length == 0) {
                $("#dateHelp1").html(" *");
                validate = false;
            } else {
                $("#date1").datepicker("option", "altField", "#reportingDateBegin");
                $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
                $("#dateHelp1").html("");
            }
            if ($("#date2").val().length == 0) {
                $("#dateHelp2").html(" *");
                validate = false;
            } else {
                $("#date2").datepicker("option", "altField", "#reportingDateEnd");
                $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
                $("#dateHelp2").html("");
            }
            return validate;
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_report.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item"><a href="Visualizer_report">Report</a></li>
    <li class="breadcrumb-item active">Laboratory Result Query</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-10 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-6">
                            <label>Lab Test</label>
                            <select name="labtestId" style="width: 100%;" class="form-control select2" id="labtestId"/>
                            <option>Select</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-6">
                            <div class="row">
                                <div class="col-6 form-group">
                                    <label>From</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date1" type="text" class="form-control" id="date1"/>
                                    </div>
                                    <input name="reportingDateBegin" type="hidden" id="reportingDateBegin"/>
                                    <span id="dateHelp1" style="color:red"></span>
                                </div>
                                <div class="col-6 form-group">
                                    <label>To</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date2" type="text" class="form-control" id="date2"/>
                                    </div>
                                    <input name="reportingDateEnd" type="hidden" id="reportingDateEnd"/>
                                    <span id="dateHelp2" style="color:red"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-6">
                            <div class="pull-right">
                                <button id="ok_button" class="btn btn-info">Ok</button>
                                <button id="cancel_button" class="btn btn-default">Cancel</button>
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
