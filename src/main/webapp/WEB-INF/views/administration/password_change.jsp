<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>

    <script type="text/JavaScript">
        $(document).ready(function () {
            resetPage(5);

            $(".form-control").bind("keypress", function (event) {
                if (event.which == 13) {
                    event.preventDefault();
                    return false;
                }
            });

            // initialize user form action and method attributes
            $("#ok_button").bind("click", function (event) {
                if (validateForm()) {
                    event.preventDefault();
                    event.stopPropagation();
                    postData();
                }
                return false;
            });

            $("#cancel_button").bind("click", function (event) {
                event.preventDefault();
                window.location.href = "administration";
            });

            $("#oldPassword").blur(function (event) {
                if ($("#oldPassword").val().length != 0) {
                    $.getJSON("password/checkPassword?password=" + $("#oldPassword").val(), function (map) {
                        if (map.result == "NO") {
                            $("#messageBar").html("Invalid password").slideDown('fast');
                            $("#oldPassword").val("");
                        } else {
                            $("#messageBar").hide();
                        }
                    });
                }
            });

            $("#confirmPassword").blur(function (event) {
                if ($("#confirmPassword").val().length != 0) {
                    if ($("#confirmPassword").val() != $("#newPassword").val()) {
                        $("#messageBar").html("Check your password entry").slideDown('fast');
                    } else {
                        $("#messageBar").hide();
                    }
                }
                ;
            });
        });

        function postData() {
            $.getJSON("password/changePassword?password=" + $("#oldPassword").val() + "&newPassword=" + $("#newPassword").val(), function (map) {
                if (map.result == "YES") {
                    $("#messageBar").html("Your password has been changed").slideDown('fast');
                    resetForm();
                } else {
                    $("#messageBar").html("Error encountered").slideDown('fast');
                }
            });
            return false;
        }

        function resetForm() {
            $("#password")[0].reset();
            $("#password").attr("action", "password");
            $("#messageBar").hide();
        }

        function validateForm() {
            var validate = true;

            // check for valid input is entered
            if ($("#oldPassword").val().length == 0) {
                $("oldPasswordHelp").html(" *");
                validate = false;
            } else {
                $("#oldPasswordHelp").html("");
            }
            if ($("#newPassword").val().length == 0) {
                $("#newPasswordHelp").html(" *");
                validate = false;
            } else {
                $("#newPasswordHelp").html("");
            }
            if ($("#confirmPassword").val().length == 0) {
                $("#confirmPasswordHelp").html(" *");
                validate = false;
            } else {
                $("#confirmPasswordHelp").html("");
            }
            return validate;
        }
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_pharmacy.jsp"/>
<!-- MAIN CONTENT -->

<form id="lamisform" theme="css_xhtml">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item active">Change Password</li>
    </ol>
    <div class="row">
        <div class="col-md-12 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>Current Password:</label>
                            <input name="oldPassword" type="password" class="form-control"
                                   id="oldPassword"/><span id="oldPasswordHelp" style="color:red"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label>New Password:</label>
                            <input name="newPassword" type="password" class="form-control"
                                   id="newPassword"/><span id="newPasswordHelp" style="color:red"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="row">
                            <label>Confirm New Password:</label>
                            <input name="confirmPassword" type="password" class="form-control"
                                   id="confirmPassword"/>
                            <span id="confirmPasswordHelp" style="color:red"></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="pull-right">
                                <button type="submit" class="btn btn-info" id="ok_button">Ok</button>
                                <button type="submit" class="btn btn-default" id="close_button">Close</button>
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
