<%-- 
    Document   : Patient
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <link rel="apple-touch-icon" sizes="76x76" href="assets/img/apple-icon.png">
    <link rel="icon" type="image/png" href="assets/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <title>LAMIS 3.0</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <style>
        .nav-pills .nav-item i.fa {
            font-size: 36px;
            color: #FF6600;
        }

        .nav-pills .nav-item i.fa:hover {
            font-size: 36px;
            color: #ffffff;
        }
    </style>
    <script>
        //           //
        $(document).ready(function () {
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
            }
            toastr.options.onclick = function () {
                window.location.href = "Notifications_page.action";
            }

            $.ajax({
                url: "Enrolled_nonTX.action",
                dataType: "json",
                method: "POST",
                beforeSend: function () {
                    //console.log("here!");
                },
                success: function (response) {
                    //var i = 0;
                    //console.log(response.notificationListCount);
                    //if()
                    enrolled = response.notificationListCount[0].enrolled;
                    unconfirmed = response.notificationListCount[1].lostUnconfirmed;
                    //arv = response.notificationListCount[2].txNoArv;
                    vl = response.notificationListCount[2].txNoVl;
                    unsupressed = response.notificationListCount[3].vlUnsupressed;

                    var warning = "";
                    var i = 0;
                    var tracked = false;

                    if (typeof enrolled != "undefined" && enrolled != 0) {
                        i++;
                        warning += "<strong>" + enrolled + "</strong> clients enrolled but not on treatment <br/>";
                        tracked = true;
                    }
                    if (typeof unconfirmed != "undefined" && unconfirmed != 0) {
                        i++;
                        warning += "<strong>" + unconfirmed + "</strong> clients who are lost to follow-up unconfirmed<br/>";
                        tracked = true;
                    }
//                        if(typeof arv != "undefined" && arv != 0){
//                            i++;
//                            warning +="<strong>"+arv+"</strong> clients on treatment but no first ARV dispensed<br/>";
//                            tracked = true;
//                        }     
                    if (typeof vl != "undefined" && vl != 0) {
                        i++;
                        warning += "<strong>" + vl + "</strong> clients on treatment who are due for viral load test<br/>";
                        tracked = true;
                    }
                    if (typeof unsupressed != "undefined" && unsupressed != 0) {
                        i++;
                        warning += "<strong>" + unsupressed + "</strong> clients on treatment with viral load un-suppressed<br/>";
                        tracked = true;
                    }

                    if (tracked == true) {
                        toastr.warning(warning, "Notifications");
                    }
                }
            });
        });
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header_main.jsp"/>
<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="card-header">
                <h6 class="title">Home</h6>
            </div>
            <div class="card-body all-icons">
                <div class="container-fluid">
                    <jsp:include page="/WEB-INF/views/template/main_menu.jsp"/>
                </div>
                <br clear="both">
            </div>
            <!-- END CARD-BODY -->
        </div>
    </div>
    <!-- END CARD-->
</div>

<jsp:include page="/WEB-INF/views/template/footer.jsp"/>

</body>
</html>
