<%-- 
    Document   : case_management_report
    Created on : Oct 27, 2017, 11:37:12 AM
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
    <script type="text/JavaScript">
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            reports();

            $.ajax({
                url: "Casemanager_retrieve.action",
                dataType: "json",
                success: function (caseManagerMap) {
                    var options = "<option value = '" + '0' + "'>" + '' + "</option>";
                    $.each(caseManagerMap, function (key, value) {
                        options += "<option value = '" + key + "'>" + value + "</option>";
                    }) //end each
                    $("#casemanagerId").html(options);
                }
            }); //end of ajax call

            $("#casemanagerId").bind("change", function (event) {
                var casemanagerId = $("#casemanagerId").val();
                if (casemanagerId != "0") {
                    $("#ok_button").prop("disabled", false);
                } else if (casemanagerId == "0") {
                    $("#ok_button").prop("disabled", true);
                }
            });

            $("#ok_button").bind("click", function (event) {
                var casemanagerId = $("#casemanagerId").val();
                event.preventDefault();
                event.stopPropagation();
                url = "";
                url += "casemanagerId=" + casemanagerId;
                if ($('[name="reportFormat"]:checked').val() == "pdf") {
                    url = "Client_list_report.action?" + url;
                    window.open(url);
                    return false;
                } else {
                    url += "&recordType=1&viewIdentifier=" + $("#viewIdentifier").prop("checked");
                    url = "Converter_dispatch.action?" + url;
                    convertData();
                }
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Casemanagement_page");
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

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_casemanagement.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="/Casemanagement_page">Case Management Page</a></li>
        <li class="breadcrumb-item active" aria-current="page">Reports</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-10 ml-auto mr-auto">
        <div class="card">
            <s:form id="lamisform" theme="css_xhtml">
                <div id="loader"></div>
                <div id="messageBar"></div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Facility Case Manager:</label>
                                <select name="casemanagerId" class="form-control" id="casemanagerId">
                                    <option value='0'></option>
                                </select><span id="caseManagerHelp" class="errorspan"></span>
                            </div>
                        </div>
                    </div>
                    <caption><h6>Report format </h6></caption>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <input type="radio" name="reportFormat" value="pdf" checked/><label>&nbsp; Generate
                                report in PDF format</label>
                            </div>
                            <input type="radio" name="reportFormat" value="cvs"/><label>&nbsp;Generate Report and
                            convert to MS Excel</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-check mt-3">
                            <label class="form-check-label">
                                <input name="viewIdentifier" type="checkbox" value="1" class="form-check-input"
                                       id="viewIdentifier" disabled="true"/>
                                <span class="form-check-sign"></span>Unscramble patient identifiers like name, addresses
                                and phone numbers
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="pull-right">
                                <button id="save_button" type="submit" class="btn btn-fill btn-info" disabled>Ok
                                </button>
                                <button id="cancel_button" type="reset" class="btn btn-fill btn-default">Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>
            </s:form>
        </div>
    </div>
</div>
<div id="user_group" style="display: none">Case management</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
