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
        <script type="text/javascript" src="/js/fingerprint.js"></script>
        <script type="text/javascript" src="/js/zebra.js"></script>
        <link type="text/css" rel="stylesheet" href="/css/zebra.css"/>
        <script type="text/javascript" src="/js/lamis/patient-common.js"></script>

    </head>
    <body>
        <jsp:include page="/WEB-INF/views/template/header.jsp"/>
        <jsp:include page="/WEB-INF/views/template/nav_home.jsp"/>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/home">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">Patient View</li>
            </ol>
        </nav>
        <div class="row">
            <div class="col-md-12 ml-auto mr-auto">
                <div class="card">
                    <form id="lamisform" theme="css_xhtml">
                        <input type="hidden" id="hospitalNum"/>
                        <input type="hidden" id="name"/>
                        <div class="card-body">
                            <div id="messageBar"></div>
                            <div class="row">
                                <div class="col-12 ml-auto mr-auto">
                                    <div class="input-group no-border col-md-3 pull-right">
                                        <input type="text" class="form-control search" placeholder="search...">
                                        <div class="input-group-append">
                                            <div class="input-group-text">
                                                <i class="now-ui-icons ui-1_zoom-bold"></i>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <div class="pull-left">
                                            <a href="/patient/new" class="btn btn-info">New</a>
                                            <button id="identify_button" class="btn btn-info">Identify Client</button>
                                        </div>
                                    </div>
                                    <div class="table-responsive">
                                        <table id="grid" class="table table-striped table-bordered center"></table>
                                        <div id="pager" style="text-align:center;"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
        <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
    </body>
    <script type="text/JavaScript">
        var gridNum = 1;
        var enablePadding = true;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();
            createPatientGrid();
            enablePadding = padHospitalNum();

            $("#identify_button").bind("click", function (event) {
                var cmp = new FingerprintComponent();
                var result = function (response) {
                    if (!!res && res.patientId) {
                        if (response.inFacility) {
                            window.location = '/patient/' + response.patientId;
                            return true;
                        } else {
                            $('#messageBar').text(response.name + ' not in this facility!').show();
                            return false;
                        }
                    } else {
                        return true;
                    }
                };
                cmp.identify(result);
                return false;
            });
            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#grid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
        });

        function imageFormat(value, options, rowObject) {
            if (value === 'true') {
                return '<div style="text-align: center;"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACqUlEQVQ4T6WTW0iTYRjH/99pc9MN576yltoBYenMXJgijZT0opslZCDqnRddhBRosItCRlAwSCGJIKPu1IIKrCspOuBEDI2gVMwzzXma25rbt9O374tvh3INuumF5+Z9nv/vfZ8Tgf88RIZ+iNWBIi5CxBkQKIz7RfwAgTHExJdocTn3atIBz1hzFiPv1Ov0xYfZwrwceU6WCCAQ9odWtlfdc+vzC+FouBfNrtcpyB/AIGtWK9TWOsPZEhqUguf5tM/RNI2wEAl+nBmd9Yf8VrQmIAnAEKuTgxloqKivjoVjCkEQMjIbMT7F+S8tECgh+OHb6ESUiLZJ6SQAT7QdZcdKLfnZ+QWRSCRDfK/sNozqsvj9uU9NcHgcjnnnog3tO/cTgH7NYK2xrjG4yylT6mg0Cj/nh7XoOmo0lZBSuPH1Dka23kOlUXGLW4vDuOxpTQAe5NrrTzfUeL1ecrLhDYqen0QgGMApdTn6y3vi4scrA+hbfhQPVyqUwprLMY4rXlMC0Jdrr62qq2lmL5AmrhIUReHa6k08PH4XDM3gncuOru/dEMhEbWS0TNh0bIzjagrQox40VJxoXP+5oRzVvwLDMPFXJZvnlnBpph2Q7SlNVOQ8SzvD6PIlU7CpO7RHWAuZTxfwOzymjG9/AwxTJiA7va4hh98RXA/ZYPEli2hT6iiCGWCrDlRHs3iFuCViutIOw6QJ2J8uFvyxoO+ze0IUY22wcMk2SjHdSjOtpK0qk7aEUJEKcU0EDv0l9vHB3TH3rMAJVtzi9gxSKq5JbiaOEp3ykuximV6ZR2noLMkV8/ChyBznDs8GFsRlsRcvwhmjTAPQAmChow+iVDBhn2gATeribF5wYpuYxgxph5PfBLANwCV5UrtASu1NmgT714lJ+wWAAyD8AhbJDCA03+U6AAAAAElFTkSuQmCC"/></div>';
            }
            return '<div style="text-align: center;"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACrUlEQVQ4T21TbUhTYRR+3nv9aG7dud1NM6fDmZBKMjXsh30RLjKKICLICILAMCSlUgTpV0ahEFriIAoEQUiwAgVFJczqh5jOPrCiqYxp2dyW2/xgc943dmVXNzu/zjnPeZ7znvc9L0GU9es4NVmn5QAtAlAIgKXAOCjGBQbm0wvLi9spZHvQlyx/EKuQ16WcKAaXqYciIx2EIfDN2rE8a8f80Duse3xNpY6V2jBPEuhNVjzh87Irc66XgbhtEDyLELxOsY7ZzYPhNIA2A9/bu+EYmzSfWVi+EcJEgV4t18AX5tbnlJ1E4PMwBjqnYLq4L2K4wS4rTJeyEX/gKH50j+DPqKXprMNXS3p4WSrDcXNFd64g+O09Jq0KnOobRH+pCcZMrygyOc1F5GJzj2Cs5QWCTpeOvNRyDYZzJfWauAXQdT90Tz9KnUMiIQsJhm2u/CBITCyc/j2Y6Xlzn3RpOUvB5RIj654WaxLyjyGpuiX6ccTY0VyFVctb0Re0WRjvGJginRrOf+i8MU7wuiSSvOA4UmpaI0R+N1ViZWJYyjGKRIy+/hIgHRqlL//wXgVDBAm0raVFHDsEhMbRy+xSDSUMJkZ+BUi7RmnJzuONsvhNbJ4adpDDrJBIKpkRw0AA+PrJZSXPeGWDzsDXJ3FBEcgfskldZu5eE33DvedSzlKiF32Xj4Vt+m8rMfN8qkwWM7c/nQXDAInFJmQ9bMfPuqtY+rB5+9E5SoEpWxD+1WCuuEhtalWjimNr9Ensf28/Omlf3IDbE2ypcC9VS6v8mFe2qROYijSeBRPxQ7boAgXm3QKcKxvmmy7P1iqHS5rVqsY4ltRo5BTKXUB8zCYS2AA8a4BzlSAQFB5VuZduhzk7ejWrVOkAKihwAYD4ISgwSyh9RQXBfMvrtW4f6R9Xl/9Jy6U2GQAAAABJRU5ErkJggg=="/></div>';
        }

        function newButton() {
            window.location.href = '/patient/new';
        }
        function editButton(patientId) {
            window.location.href = '/patient/edit/' + patientId;
        }
        function deleteButton(patientId){
            $.confirm({
                title: 'Confirm!',
                content: 'Are you sure you want to delete?',
                buttons: {
                    confirm: function () {
                        $("#patientId").val(patientId);
                        var data = $("#grid").getRowData(patientId);
                        $("#hospitalNum").val(data.hospitalNum);
                        $("#name").val(data.name);
                        if($("#userGroup").html() === "Data Analyst") {
                            window.location.href = '/administration/error';
                        }
                        else {
                            $.ajax({
                                url : "api/patient",
                                method : "DELETE",
                                data : $("#lamisform").serialize(),
                                dataType: "json",
                                success : function(response) {
                                    window.location.href = "patient";
                                }
                            });
                        }
                        window.location.href = '/patient';
                        return true;
                    },
                    cancel: function () {
                        console.log("cancel");
                    }
                }
            });
        }
    </script>
</html>
