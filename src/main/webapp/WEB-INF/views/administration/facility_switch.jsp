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
        var obj = {};
        $(document).ready(function () {
            resetPage();

            $.ajax({
                url: "StateId_retrieve.action",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(stateMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#stateId").html(options);
                }
            }); //end of ajax call

            $("#stateId").change(function (event) {
                $.ajax({
                    url: "LgaId_retrieve.action",
                    dataType: "json",
                    data: {stateId: $("#stateId").val()},

                    success: function (lgaMap) {
                        var options = "<option value = '" + '' + "'>" + '' + "</option>";
                        $.each(lgaMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }) //end each
                        $("#lgaId").html(options);
                    }
                }); //end of ajax call
            });
            $("#lgaId").change(function (event) {
                $.ajax({
                    url: "Facility_retrieve.action",
                    dataType: "json",
                    data: {stateId: $("#stateId").val(), lgaId: $("#lgaId").val()},

                    success: function (facilityMap) {
                        var options = "<option value = '" + '' + "'>" + '' + "</option>";
                        $.each(facilityMap, function (key, value) {
                            options += "<option value = '" + key + "'>" + value + "</option>";
                        }) //end each
                        $("#id").html(options);
                    }
                }); //end of ajax call
            });

            $("#id").change(function (event) {
                var name = $("select[name='id'] option:selected").text();
                $("#facilityName").val(name);
            });

            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    $("#lamisform").attr("action", "Verify_group");
                    return true;
                } else {
                    return false;
                }
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Export_page");
                return true;
            });
        });

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            if ($("#id").val().length == 0) {
                $("#facilityHelp").html(" *");
                validate = false;
            } else {
                $("#facilityHelp").html("");
            }
            return validate;
        }

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>
<!-- MAIN CONTENT -->
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item"><a href="Export_page">Data Maintenance</a></li>
    <li class="breadcrumb-item active">Switch Facility</li>
</ol>
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar"></div>
                    <input name="facilityName" type="hidden" id="facilityName"/>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label for="" class="form-label">State</label>
                            <select name="stateId" style="width: 100%;" class="form-control select2" id="stateId">
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">L.G.A</label>
                            <select name="lgaId" style="width: 100%;" class="form-control select2" id="lgaId">
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-6">
                            <div class="form-group">
                                <label class="form-label">Facility</label>
                                <select name="facilityId" style="width: 100%;" class="form-control select2"
                                        id="facilityId">
                                </select>
                                <span id="facilityHelp" class="errorspan"></span>
                            </div>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="ok_button" class="btn btn-info">Switch</button>
                        <button id="close_button" class="btn btn-default">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
<div id="userGroup" style="display: none"></div>
</body>

</html>