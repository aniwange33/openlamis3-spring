<%-- 
    Document   : devolve_data
    Created on : Feb 3, 2017, 2:12:29 PM
    Author     : user1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/devolve-common.js"></script>
    <!--               <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>  -->
    <script type="text/javascript" src="js/date.js"></script>
    <script type="text/JavaScript">
        var obj = {};
        var date = "", lastSelectDate = "";
        var updateRecord = false;
        $(document).ready(function () {
            resetPage();
            initialize();
            reports();

            var queryString = decodeURIComponent(window.location.search);
            var action = queryString.substring(8);
            if (action == 'view') {
                $("#save_button").hide("slow");
                $("#close_button").hide("slow");
                $("#lamisform :input").prop('readonly', true);
                //$("#lamisform :select").prop('readonly', true);
            }

            $("#typeDmoc").on('select2:select', function (e) {
                var data = e.params.data;
                console.log(data);
                if ($("#typeDmoc").val() === "MMS" || $("#typeDmoc").val() == "") {
                    $("#dmocForm").hide("slow");
                } else {
                    $("#dmocForm").show("slow");
                }
            });

            $.ajax({
                url: "Devolve_retrieve.action",
                dataType: "json",
                success: function (devolveList) {
                    populateForm(devolveList);
                }
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Devolve_search");
                return true;
            });
        });

        //date.slice(6), date.slice(0,2), date.slice(3,5)
        function checkDate() {
            if ($("#date1").val().length != 0) {
                var date = $("#date1").val();
                var dateLastCd4 = new Date();
                var revat = dateLastCd4.setMonth(6);
                $("#date5").val(formatDate(revat));
            }
        }

        function formatDate(date) {
            var monthNames = ["January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November",
                "December"];

            var day = date.getDate();
            var monthIndex = date.getMonth();
            var year = date.getFullYear();

            return day + ' ' + monthNames[monthIndex] + ' ' + year;
        }
    </script>

</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_pharmacy.jsp"/>
<div class="s2-event-log">
    <ul class="js-event-log"></ul>
</div>
<!-- MAIN CONTENT -->

<form id="lamisform" method="post" theme="css_xhtml">
    <input name="name" type="hidden" id="name"/>
    <input name="patientId" type="hidden" id="patientId"/>
    <input name="devolveId" type="hidden" id="devolveId"/>
    <input name="dateDevolved" type="hidden" id="dateDevolved"/>
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item"><a href="Pharmacy_page">Pharmacy</a></li>
        <li class="breadcrumb-item active">Differentiated Care</li>
    </ol>
    <div class="row">
        <div class="col-8 ml-auto mr-auto">
            <div class="card">
                <div class="card-body">
                    <div id="messageBar" class="alert alert-warning alert-dismissible fade show" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Hospital No</label> <input
                                    name="hospitalNum" type="text" class="form-control"
                                    id="hospitalNum" readonly="readonly"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <p>
                                    <br clear="both"> <span id="patientInfor"></span>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of Devolvement</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date1" type="text" class="form-control" id="date1"/>
                                </div>
                                <input name="dateDevolved" type="hidden" id="dateDevolved"/> <span
                                    id="dateDevolveHelp" class="errorspan"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Type of DMOC</label> <select
                                    name="typeDmoc" style="width: 100%;"
                                    class="form-control select2" id="typeDmoc">
                                <option value="">Select</option>
                                <option value="CPARP">CPARP</option>
                                <option value="CARC">CARC</option>
                                <option value="MMS">MMS</option>
                                <option value="MMD">MMD</option>
                            </select> <span id="typeHelp" class="errorspan"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Viral Load Assessment done?</label> <select
                                    name="viralLoadAssessed" style="width: 100%;"
                                    class="form-control select2" id="viralLoadAssessed">
                                <option value="">Select</option>
                                <option value="No">No</option>
                                <option value="Yes">Yes</option>
                            </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">CD4 count Assessment done?</label> <select
                                    name="cd4Assessed" style="width: 100%;"
                                    class="form-control select2" id="cd4Assessed">
                                <option value="">Select</option>
                                <option value="No">No</option>
                                <option value="Yes">Yes</option>
                            </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Current Viral Load</label> <input
                                    name="lastViralLoad" type="text" class="form-control"
                                    id="lastViralLoad"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of Viral Load</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date2" type="text" class="form-control" id="date2"/>
                                </div>
                                <input name="dateLastViralLoad" type="hidden"
                                       id="dateLastViralLoad"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Current CD4</label>
                                <input name="lastCd4" type="text" class="form-control" id="lastCd4"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of CD4</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date3" type="text" class="form-control"
                                           id="date3"/>
                                </div>
                                <input name="dateLastCd4" type="hidden"
                                       id="dateLastCd4"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Current Clinical Stage</label> <input
                                    name="lastClinicStage" type="text" class="form-control"
                                    id="lastClinicStage"/>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Date of Clinical Stage</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                    </div>
                                    <input name="date4" type="text" class="form-control"
                                           id="date4"/>
                                </div>
                                <input name="dateLastClinicStage" type="hidden"
                                       id="dateLastClinicStage"/>
                                <input name="regimentype" type="hidden" id="regimentype"/>
                            </div>
                        </div>
                    </div>

                    <div id="dmocForm" style="display: none;">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">ARV Regimen</label>
                                    <select name="regimen" style="width: 100%;"
                                            class="form-control select2" id="regimen">
                                        <option value="">Select</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Two months ARV refill
                                        dispensed?</label> <select name="arvDispensed" style="width: 100%;"
                                                                   class="form-control select2" id="arvDispensed">
                                    <option value="">Select</option>
                                    <option value="No">No</option>
                                    <option value="Yes">Yes</option>
                                </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Date of next Clinic/Lab
                                        re-evaluation</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date5" type="text" class="form-control" id="date5"/>
                                    </div>
                                    <input name="dateNextClinic" type="hidden" id="dateNextClinic"/> <span
                                        id="dateAppointHelp" class="errorspan"></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Date of next ARV Refill</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                        </div>
                                        <input name="date6" type="text" class="form-control"
                                               id="date6"/>
                                    </div>
                                    <input name="dateNextRefill" type="hidden"
                                           id="dateNextRefill"/>
                                    <span id="dateRefillHelp"
                                          class="errorspan"></span>
                                </div>
                            </div>
                        </div>
                        <hr/>
                        <div class="divider">
                            <h5>Details of Community Pharmacy/Club Providing ARV</h5>
                        </div>
                        <hr/>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">State</label>
                                    <select name="stateId" style="width: 100%;" class="form-control select2"
                                            id="stateId">
                                        <option value="">Select</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">LGA</label>
                                    <select name="lgaId" style="width: 100%;" class="form-control select2" id="lgaId">
                                        <option value="">Select</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Community Pharmacy/Club</label> <select
                                        name="communitypharmId" style="width: 100%;"
                                        class="form-control select2" id="communitypharmId">
                                    <option value="">Select
                                    <option>
                                </select> <span id="pharmHelp" class="errorspan"></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Address</label> <input
                                        name="address" type="text" class="form-control"
                                        id="address" readonly="readonly"/>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Phone</label>
                                    <input name="phone" type="text" class="form-control" id="phone"
                                           readonly="readonly"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button id="save_button" type="submit" class="btn btn-info">
                            Save
                        </button>
                        <button id="close_button" class="btn btn-default">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>

