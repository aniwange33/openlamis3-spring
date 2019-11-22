<%-- 
    Document   : Administration
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
        $(document).ready(function () {
            resetPage();
            dqaNotifier();
        });
    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_maintenance.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="/home">Home</a></li>
    <li class="breadcrumb-item active">Data Maintenance</li>
</ol>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <!--                    <div class="card-header bg-light">
                                    <h5 class="card-title">Data Maintenance</h5>
                                </div>-->
            <div class="card-body">
                <div id="mainBackground"></div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
