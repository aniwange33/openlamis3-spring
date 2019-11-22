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
    <script type="text/JavaScript">

    </script>
</head>

<body>
<!-- Navbar -->
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_clinic.jsp"/>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
    <li class="breadcrumb-item active">Clinic</li>
</ol>

<!-- Summary Section -->
<s:form id="lamisform" theme="css_xhtml">
<input name="patientId" type="hidden" id="patientId"/>
<input name="hospitalNum" type="hidden" class="inputboxes" id="hospitalNum"/>
<div id="messageBar"></div>
<div class="row">
    <div class="col-md-4">
        <div class="card">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-12">
                        <h4>Teddy Thanhimoh</h4>
                        <span>Male</span>,<span>28 years old</span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <span>Clinic tab</span>
                        <span>Pharmacy tab</span>
                        <span>Laboratory tab</span>
                    </div>
                </div>
            </div>
        </div>
        <!-- Classic tabs -->
        <!--                    <div class="classic-tabs mx-2">
                                <ul class="nav tabs-cyan" id="myClassicTabShadow" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link  waves-light active show" id="profile-tab-classic-shadow" data-toggle="tab" href="#profile-classic-shadow"
                                           role="tab" aria-controls="profile-classic-shadow" aria-selected="true">Clinical Information</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link waves-light" id="follow-tab-classic-shadow" data-toggle="tab" href="#follow-classic-shadow"
                                           role="tab" aria-controls="follow-classic-shadow" aria-selected="false">Laboratory</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link waves-light" id="contact-tab-classic-shadow" data-toggle="tab" href="#contact-classic-shadow"
                                           role="tab" aria-controls="contact-classic-shadow" aria-selected="false">Pharmacy</a>
                                    </li>
                                </ul>

                                <div class="tab-content card" id="myClassicTabContentShadow">
                                    <div class="tab-pane fade active show" id="profile-classic-shadow" role="tabpanel" aria-labelledby="profile-tab-classic-shadow">
                                        <p>Clinical Info</p>
                                    </div>
                                    <div class="tab-pane fade" id="follow-classic-shadow" role="tabpanel" aria-labelledby="follow-tab-classic-shadow">
                                        <p>Lab Info</p>
                                    </div>
                                    <div class="tab-pane fade" id="contact-classic-shadow" role="tabpanel" aria-labelledby="contact-tab-classic-shadow">
                                        <p>Pharmacy Info</p>
                                    </div>
                                </div>
                            </div>
                             Classic tabs -->
    </div>
    <div class="col-md-8">
        <div class="row">
            <div class="card">
                <div class="card-header bg-light">
                    <div class="card-title">Medical Encounter note</div>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-12">
                            <h4>Medical History</h4>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <p>hello</p>
                        </div>
                    </div>
                </div>
            </div>
            <!--                     Classic tabs
                                <div class="classic-tabs mx-2">
                                    <ul class="nav tabs-cyan" id="myClassicTabShadow" role="tablist">
                                        <li class="nav-item">
                                            <a class="nav-link  waves-light active show" id="profile-tab-classic-shadow" data-toggle="tab" href="#profile-classic-shadow"
                                               role="tab" aria-controls="profile-classic-shadow" aria-selected="true">Clinical Information</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link waves-light" id="follow-tab-classic-shadow" data-toggle="tab" href="#follow-classic-shadow"
                                               role="tab" aria-controls="follow-classic-shadow" aria-selected="false">Laboratory</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link waves-light" id="contact-tab-classic-shadow" data-toggle="tab" href="#contact-classic-shadow"
                                               role="tab" aria-controls="contact-classic-shadow" aria-selected="false">Pharmacy</a>
                                        </li>
                                    </ul>

                                    <div class="tab-content card" id="myClassicTabContentShadow">
                                        <div class="tab-pane fade active show" id="profile-classic-shadow" role="tabpanel" aria-labelledby="profile-tab-classic-shadow">
                                            <p>Clinical Info</p>
                                        </div>
                                        <div class="tab-pane fade" id="follow-classic-shadow" role="tabpanel" aria-labelledby="follow-tab-classic-shadow">
                                            <p>Lab Info</p>
                                        </div>
                                        <div class="tab-pane fade" id="contact-classic-shadow" role="tabpanel" aria-labelledby="contact-tab-classic-shadow">
                                            <p>Pharmacy Info</p>
                                        </div>
                                    </div>
                                </div>
                                 Classic tabs -->
        </div>
    </div>
    </s:form>
    <div id="user_group1" style="display: none">
    </div>
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>