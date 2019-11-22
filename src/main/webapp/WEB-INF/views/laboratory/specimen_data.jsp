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
    <link type="text/css" rel="stylesheet" href="css/zebra_dialog.css"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/lamis/specimen-common.js"></script>
    <script type="text/javascript" src="js/zebra_dialog.js"></script>
    <script type="text/JavaScript">
        var obj = {};
        var date = "";
        var updateRecord = false;
        var gridNum = 0;
        $(document).ready(function () {
            $("#dialog").dialog({
                title: "Assign SMS printer number",
                autoOpen: false,
                width: 400,
                resizable: false,
                buttons: [{text: "Assign", click: assignPrinter},
                    {
                        text: "Cancel", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });
            resetPage();
            initialize();
            reports();
            barcode();

            $.ajax({
                url: "Specimen_retrieve.action",
                dataType: "json",
                success: function (specimenList) {
                    populateForm(specimenList);
                }
            }); //end of ajax call

            $("#specimenType").bind("change", function (event) {
                if ($("#labno").val().length == 0) {
                    $.ajax({
                        url: "Labno_generate.action",
                        dataType: "json",
                        success: function (json) {
                            //$("#labno").val(json.labno);
                        }
                    }); //end of ajax call
                }
            });

            $("#specimenType").bind("click", function () {
                if ($("#specimenType").val() == "DBS") {
                    $("#eid_button").removeAttr("disabled");
                } else {
                    $("#eid_button").attr("disabled", "disabled");
                }
            });

            $("#age").bind("change", function (event) {
                showDeatils()
            });
            $("#eid_button").bind("click", function (event) {
                showDeatils();
                return false;
            }); //show and hide details

            $("#print_button").bind("click", function (event) {
                var barcode = $("#barcode").val();
                if (barcode == "") {
                    barcode = $("#labno").val()
                    $("#barcode").val(barcode);
                    $("#barcode").html(barcode);
                }
                $.ajax({
                    url: "Print_barcode.action",
                    data: {"barcode": barcode},
                    dataType: "json",
                    success: function (json) {
                        printNotifier();
                        //$("#messageBar").html(json.status).slideDown('fast');
                    }
                });
                return false;
            });

            $("#date2").bind("change", function (event) {
                checkDate();
            });
            $("#date3").bind("change", function (event) {
                checkDate();
            });

            $("#treatmentUnitId").change(function (event) {
                var facilityId = $("#treatmentUnitId").val();
                checkPrinter(facilityId);
            });

            $("#assignPrinterLink").bind("click", function () {
                $("#dialog").dialog("open");
                return false;
            });

            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Specimen_search");
                return true;
            });
        });

        function checkDate() {
            if ($("#date2").val().length != 0 && $("#date3").val().length != 0) {
                if (parseInt(compare($("#date3").val(), $("#date2").val())) == -1) {
                    var message = "Date sample received cannot be ealier than date sample collected";
                    $("#messageBar").html(message).slideDown('slow');
                } else {
                    $("#messageBar").slideUp('slow');
                }
            }
        }

        function showDeatils() {
            if ($("#specimenType").val() == "DBS") {
                if ($("#age").val() <= 18) {
                    $("#eidScroll").toggle("slow");
                    $("#adultScroll").hide("slow");
                } else {
                    $("#eidScroll").hide("slow");
                    $("#adultScroll").toggle("slow");
                }
            }
        }

    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_specimen.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="/Specimen_page">General Lab</a></li>
        <li class="breadcrumb-item active" aria-current="page">Laboratory Test</li>
    </ol>
</nav>
<form id="lamisform">

    <div id="messageBar"></div>

    <table class="table table-responsive">
        <tr>
            <td width="21%"><label>State:</label></td>
            <td width="35%">
                <select name="stateId" style="width: 130px;" class="inputboxes" id="stateId">
                </select><label>&nbsp;&nbsp;LGA:</label>
                <select name="lgaId" style="width: 130px;" class="inputboxes" id="lgaId">
                </select>

            </td>
        </tr>
        <tr>
            <td width="21%"><label>Sending Facility/ &nbsp;Treatment Unit:</label></td>
            <td width="32%">
                <select name="treatmentUnitId" style="width: 300px;" class="inputboxes" id="treatmentUnitId">
                </select><span id="facilityHelp" class="errorspan"></span>
            </td>
            <td>
                <div id="hyperlink"><span id="assignPrinterLink" style="color:blue"></span></div>
            </td>
        </tr>
        <tr>
            <td><label>Hospital No:</label></td>
            <td><input name="hospitalNum" type="text" class="inputboxes" id="hospitalNum"/><span id="numHelp"
                                                                                                 class="errorspan"></span>
            </td>
            <td><input name="specimenId" type="hidden" class="inputboxes" id="specimenId"/></td>
        </tr>
        <tr>
            <td><label>Surname:</label></td>
            <td><input name="surname" type="text" class="inputboxes" id="surname"/><span id="surnameHelp"
                                                                                         class="errorspan"></span></td>
            <td><label>Other Names:</label></td>
            <td><input name="otherNames" type="text" class="inputboxes" id="otherNames"/></td>
        </tr>
        <tr>
            <td><label>Date of Birth:</label></td>
            <td><input name="date1" type="text" style="width: 100px;" class="inputboxes" id="date1"/><input
                    name="dateBirth" type="hidden" id="dateBirth"/></td>
            <td><label>Age:</label></td>
            <td><input name="age" type="text" style="width: 50px;" class="inputboxes" id="age"/>
                <select name="ageUnit" style="width: 75px;" class="inputboxes" id="ageUnit">
                    <option></option>
                    <option>year(s)</option>
                    <option>month(s)</option>
                    <option>day(s)</option>
                </select><span id="ageHelp" class="errorspan"></span>
            </td>
        </tr>
        <tr>
            <td><label>Gender:</label></td>
            <td>
                <select name="gender" style="width: 100px;" class="inputboxes" id="gender">
                    <option></option>
                    <option>Male</option>
                    <option>Female</option>
                </select><span id="genderHelp" class="errorspan"></span>
            </td>
        </tr>
        <tr>
            <%-- <td><label>Address:</label></td>--%>
            <td><input name="address" type="hidden" style="width: 250px;" class="inputboxes" id="address"/></td>
            <%-- <td><label>Phone:</label></td>--%>
            <td><input name="phone" type="hidden" style="width: 100px;" class="inputboxes" id="phone"/></td>
        </tr>
    </table>
    <table width="100%" border="0">
        <tr>
            <td class="topheaders">Clinic Infor</td>
        </tr>
    </table>
    <table width="99%" border="0" class="space" cellpadding="3">
        <tr>
            <td width="17%"><label>Date Sample &nbsp;Received:</label></td>
            <td width="34%"><input name="date2" type="text" style="width: 100px;" class="inputboxes" id="date2"/><input
                    name="dateReceived" type="hidden" id="dateReceived"/><span id="dateHelp" class="errorspan"></span>
            </td>
            <td width="20%"><label>Date Collected:</label></td>
            <td width="42%"><input name="date3" type="text" style="width: 100px;" class="inputboxes" id="date3"/><input
                    name="dateCollected" type="hidden" id="dateCollected"/></td>
        </tr>
        <tr>
            <td><label>Nature of Sample:</label></td>
            <td>
                <select name="specimenType" style="width: 120px;" class="inputboxes" id="specimenType"/>
                <option></option>
                <option>Whole Blood</option>
                <option>DBS</option>
                <option>Plasma</option>
                <option>Serum</option>
                <option>Sputum</option>
                <option>Urine</option>
                <option>Faeces</option>
                </select> &nbsp;<button id="eid_button" style="width: 20px;" disabled="true">...</button>
                <span id="specimenTypeHelp" class="errorspan"></span>
            </td>
            <td><label>Laboratory No:</label> &nbsp;</td>
            <td><input name="labno" style="width: 100px" type="text" class="inputboxes" id="labno"/><span id="labnoHelp"
                                                                                                          class="errorspan"></span>
            </td>
            <%-- <td><label>Lab No:</label> &nbsp;<input name="labno" style="width:75px;color:blue;font-size:14px" type="text" class="inputboxes" id="labno" readonly="readonly"/><span id="labnoHelp" class="errorspan"></span></td>--%>
        </tr>
        <tr>
            <td><label>Is Sample Testable?:</label></td>
            <td>
                <select name="reasonNoTest" style="width: 120px;" class="inputboxes" id="reasonNoTest"/>
                <option>Yes</option>
                <option>Technical problems</option>
                <option>Insufficient blood</option>
                <option>Improper package</option>
                <option>Labeled improperly</option>
                <option>Layered or clotted</option>
                </select>
            </td>
            <td colspan="2"><input name="qualityCntrl" type="checkbox" id="qualityCntrl"/><label>Quality Control
                Sample</label></td>
        </tr>
        <tr>
            <td><label>Barcode:</label></td>
            <td><input name="barcode" type="text" style="width: 120px;" class="inputboxes" id="barcode"/> &nbsp;<button
                    id="print_button" style="width: 50px;font-size:10px">Print...
            </button>
            </td>
        </tr>
    </table>
    <p></p>

    <div id="eidScroll" style="height:250px; width: 790px; margin-left: 1px; overflow-y: scroll; display: none">
        <jsp:include page="/WEB-INF/views/laboratory/eid_data.jsp"/>
    </div>
    <div id="adultScroll" style="display: none">
        <jsp:include page="/WEB-INF/views/laboratory/adult_data.jsp"/>
    </div>
    <div id="dialog">
        <table width="99%" border="0" class="space" cellpadding="3">
            <tr>
                <td><label>SMS Printer No:</label>&nbsp; <input name="smsPrinter" type="text" class="inputboxes"
                                                                id="smsPrinter"/><span id="smsHelp"
                                                                                       style="color:red"></span></td>
                <td></td>
            </tr>
        </table>
    </div>
    <hr></hr>
    <p></p>


    <div id="buttons" style="width: 300px">
        <button id="save_button">Save</button> &nbsp;<button id="delete_button" disabled="true"/>
        Delete</button> &nbsp;<button id="close_button"/>
        Close</button>
    </div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
