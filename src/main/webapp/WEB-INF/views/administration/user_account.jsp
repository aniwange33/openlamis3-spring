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
    <link type="text/css" rel="stylesheet" href="css/lamis.css"/>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css"/>

    <style type="text/css">
        #buttons {
            float: left;
            height: 30px;
            width: 230px;
            background-color: #FFF;
            padding-top: 10px;
            padding-bottom: 0px;
        }
    </style>
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.maskedinput-1.3.min.js"></script>
    <script type="text/JavaScript">
        $(document).ready(function () {
            $("#username")[0].focus();
            $("#createlink").bind("click", function (event) {
                $("#enterPassword").toggle("slow");
                $("#createlink").html("Back to login");
                return false;
            }); //show and hide grid

        })
    </script>
</head>

<body>
<div id="page">
    <div id="header">
        <img id="headerBanner" src="images/lamis_logo.png">
    </div>
    <div id="mainPanel">
        <div id="loginArea" align="center">
            <form action="User_login" method="login" name="loginform" id="loginform">
                <table width="300" border="0">
                    <tr>
                        <td height="195"></td>
                    </tr>
                    <tr>
                        <td>&nbsp;&nbsp;&nbsp;<strong>Sign in with your username and password</strong></td>
                    </tr>
                    <tr>
                        <td height="35">
                            &nbsp;&nbsp;&nbsp;<input name="username" type="text" class="inputboxes" id="username"
                                                     placeholder="username" size="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="35">
                            &nbsp;&nbsp;&nbsp;<input name="password" type="password" class="inputboxes" id="password"
                                                     placeholder="password" size="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="35">
                            <div id="enterPassword" style="display: none"><input name="passwordConfirm"
                                                                                 type="passwordConfirm"
                                                                                 class="inputboxes" id="passwordConfirm"
                                                                                 placeholder="confirm password"
                                                                                 size="30"/></div>
                            &nbsp;&nbsp;&nbsp;<div id="createlink" class="hyperlink"><span id="createUser"
                                                                                           style="margin-left:10px; color:blue">Create new account</span>
                        </div>
                        </td>
                    </tr>
                    <tr>
                        <td height="10">
                            <div id="buttons">
                                <button align="left">Sign in</button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td height="195"></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div id="footer">
</div>
</body>
</html>
