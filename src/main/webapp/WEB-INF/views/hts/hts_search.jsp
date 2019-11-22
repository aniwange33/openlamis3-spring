<%-- 
    Document   : Commence
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
    <script type="text/javascript" src="/js/jquery.maskedinput-1.3.min.js"></script>
<%--    <script type="text/javascript" src="/js/lamis/lamis-common.js"></script>--%>
    <script type="text/javascript" src="/js/lamis/search-common.js"></script>
    <script type="text/javascript" src="/js/lamis/report-common.js"></script>
    <script type="text/javascript" src="/js/lamis/hts-common.js"></script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_hts.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/home">Home</a></li>
        <li class="breadcrumb-item active" aria-current="page">Hiv Counseling and Testing</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <input name="clientCode" id="clientCode" type="hidden">
                <input name="htsId" id="htsId" type="hidden">
                <input name="dateVisit" id="dateVisit" type="hidden">
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
                                    <a href="/patient/new" class="btn btn-info">Enrol Client</a>
                                    <a href="/hts/assessment/new" class="btn btn-info">Risk Assessment</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="table-responsive">
                        <table id="grid" class="table table-striped table-bordered center"></table>
                        <div id="pager" style="text-align:center;"></div>
                        <p></p>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
