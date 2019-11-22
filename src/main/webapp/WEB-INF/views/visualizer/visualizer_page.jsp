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
    <link rel="apple-touch-icon" sizes="76x76" href="assets/img/apple-icon.png">
    <link rel="icon" type="image/png" href="assets/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <title>LAMIS</title>

    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no'
          name='viewport'/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>

    <script type="text/JavaScript">
        $(document).ready(function () {
            resetPage();
            reports();
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "newestOnTop": false,
                "progressBar": false,
                "positionClass": "toast-bottom-right",
                "preventDuplicates": false,
                "showDuration": "30000",
                "hideDuration": "1000",
                "timeOut": "30000",
                "extendedTimeOut": "3000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut",
                "onclick": function () {
                    window.location.href = "Labtest_prescription_search.action";
                }
            }

            $.ajax({
                url: "Labtests_prescribed.action",
                dataType: "json",
                method: "POST",
                beforeSend: function () {
                    //console.log("here!");
                },
                success: function (response) {
                    labtests = response.notificationListCount[0].labtests;

                    var warning = "";
                    var i = 0;
                    var tracked = false;

                    if (typeof labtests != "undefined" && labtests != 0) {
                        warning += "<strong>" + labtests + "</strong> clients with laboratory test order <br/>";
                        tracked = true;
                    }
                    if (tracked == true) {
                        toastr.warning(warning, "LabTest Order Notifications");
                    }
                }
            });

        });
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_report.jsp"/>
<!-- MAIN CONTENT -->
<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="card-header">
                <div class="card-category">
                    <strong>REPORTS</strong>
                </div>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-12 mr-auto ml-auto">
                        <div id="mainBackground"></div>
                    </div>
                </div>
            </div>
            <!-- END OF caRD BODY -->
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>

</body>
</html>

