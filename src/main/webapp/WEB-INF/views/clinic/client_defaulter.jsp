<%-- 
    Document   : client_defaulter
    Created on : Oct 27, 2017, 11:56:59 AM
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
                url = "Client_defaulter_report.action?" + url;
                window.open(url);
                return false;
            });

            $("#cancel_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Casemanagement_page");
                return true;
            });

        });
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_casemanagement.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="/Casemanagement_page">Reports</a></li>
        <li class="breadcrumb-item active" aria-current="page"> Case Manager Client Defaulter List</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-10 ml-auto mr-auto">
        <div class="card">
            <s:form id="lamisform" theme="css_xhtml">
            <div class="card-body">
                <div id="loader"></div>
                <div id="messageBar"></div>
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
                <div class="row">
                    <div class="col-md-6">
                        <div class="pull-right">
                            <button id="ok_button" type="submit" class="btn btn-fill btn-info" disabled>Ok</button>
                            <button id="cancel_button type=" reset
                            " class="btn btn-fill btn-default">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </s:form>
    </div>
</div>
<div id="user_group" style="display: none">Case management</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
