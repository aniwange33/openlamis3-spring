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

    <title>LAMIS 3.0 Login</title>

    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no'
          name='viewport'/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="assets/font-awesome/css/font-awesome.min.css">

    <!-- CSS Files -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="assets/css/now-ui-dashboard.min.css" rel="stylesheet"/>
    <!-- Valid and Invalid Styles -->
    <style type="text/css">
        .forgot-password {
            color: rgb(104, 145, 162);
            text-transform: capitalize;
        }

        .forgot-password:hover,
        .forgot-password:active,
        .forgot-password:focus {
            color: rgb(14, 5, 117);
        }

        i.fa {
            font-size: 24px;
        }

        body {
            background-image: url("assets/img/bground1.jpeg");
            background-size: cover;
        }

        .content,
        .container {
            margin-top: 0px !important;
        }
    </style>

</head>

<body>
<div class="wrapper wrapper-full-page">
    <div class="full-page login-page" filter="rose">
        <div class="content">
            <div class="container">
                <div class="col-md-4 ml-auto mr-auto">
                    <form name="loginform" id="loginform">
                        <div class="card card-login">
                            <div class="card-header ">
                                <div class="logo-container mt-0 mb-0">
                                    <img src="assets/img/logo.jpg" alt=""/>
                                </div>
                                <h3 style="text-align: center">Welcome to LAMIS</h3>
                            </div>

                            <div class="card-body ">
                                <div class="input-group form-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-user-circle-o"></i>
                                        </div>
                                    </div>
                                    <input type="text" name="username" id="username" class="form-control"
                                           placeholder="Username...">
                                </div>

                                <div class="input-group form-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-lock"></i>
                                        </div>
                                    </div>
                                    <input type="password" name="password" id="password" placeholder="Password"
                                           class="form-control">
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <button type='button'
                                                class="btn btn-primary btn-lg btn-round btn-block btn-signin"
                                                name="login_button" id="login_button"> Login
                                        </button>
                                        <br/>
                                        <div class="text-center">
                                            <p class="title"><a href="#">Forgot Password?</a></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="text-center">
                            <!--                                <a href="#" class="text-white">LAMIS User Manual</a>-->
                            <div class="copyright text-white">
                                Copyright &copy; 2010 - <span id="copyright"></span> FHI360 Health Informatics Team
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        $("#login_button").bind("click", function (event) {
            var url = 'http://localhost:8080/api/authenticate';
            fetch(url, {
                    method: 'post',
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    }),
                    body: JSON.stringify(
                        {
                            username: $('#username').val(),
                            password: $('#password').val()
                        }
                    )
                }
            ).then(function (response) {
                if (response.ok) {
                    console.log('Response1', response.body);
                    var token = response.json()['id_token'];
                    console.log('Token1', token);
                    saveAuthorization(token);
                    window.location.href = "/home";
                }
            }).catch(function (error) {
                alert(error);
            });
        });
    });
</script>
</html>