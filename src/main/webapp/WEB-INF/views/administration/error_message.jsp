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
    <script type="text/javascript" src="js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="js/lamis/report-common.js"></script>
    <script type="text/JavaScript">
        $(document).ready(function () {
            resetPage();
            reports();
        })
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item active">Error</li>
</ol>
<!-- MAIN CONTENT -->
<form id="lamisform" theme="css_xhtml">
    <div class="row">
        <div class="col-md-8 ml-auto mr-auto">
            <div class="card" style="height:50rem;">
                <div class="card-body">
                    <div id="errormessage"><span
                            style="font-family:arial; margin-left: 200px; font-size:20px; color:red">Access Denied, Contact Your Administrator</span>
                    </div>
                </div>
            </div>
        </div>
</form>
<!-- END MAIN CONTENT-->
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
<div id="user_group1" style="display: none"><s:property value="#session.userGroup"/></div>
</body>
</html>
