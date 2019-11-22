<%-- 
    Document   : Patient
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 2.6</title>
    <link type="image/png" rel="icon" href="images/favicon.png"/>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <!--        <link type="text/css" rel="stylesheet" href="css/lamis.css" />-->
    <!--        <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.18.custom.css" />-->

    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="js/json.min.js"></script>
    <script type="text/javascript" src="assets/highchart/highcharts.js"></script>
    <script type="text/javascript"
            src="assets/highchart/modules/exporting.js"></script>
    <script type="text/javascript" src="assets/js/dashboard.js"></script>
    <script type="text/JavaScript">
        function newClinic() {
            $("#lamisform").attr("action", "Clinic_new");
            return true;
        }

        function pmtct(data) {
            console.log(data);
            if (data == 'Female') {
                $('#fel').show();
            } else {
                $('#fel').show();
            }
        }

        $(document).ready(function () {
            var queryString = decodeURIComponent(window.location.search);
            var hospitalNo = queryString.substring(5);
            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#grid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
            $.ajax({
                url: "Patient_find_number.action",
                dataType: "json",
                // data: {hospitalNum: $("#hospitalNum").val()},
                data: {hospitalNum: hospitalNo},
                success: function (patientList) {
                    console.log(patientList);
                    $('#hospitalNo').html(patientList[0].hospitalNum);
                    $('#hospitalNum').val(patientList[0].hospitalNum);
                    $('#pname').html(patientList[0].name);
                    $('#name').val(patientList[0].name);
                    $('#gender').html(patientList[0].gender);
                    $("#dateCurrentStatus").val(patientList[0].dateCurrentStatus);
                    $('#phone').html(patientList[0].phone);
                    $("#currentStatus").val(patientList[0].currentStatus);
                    $('#occupation').html(patientList[0].occupation);
                    $("#dateVisit").val(patientList[0].dateStarted);
                    $('#mstatus').html(patientList[0].maritalStatus);
                    $('#age').html(patientList[0].age);
                    $('#state').html(patientList[0].state);
                    $('#address').html(patientList[0].address);
                    $('#lga').html(patientList[0].lga);
                    $('#lastVisit').html(patientList[0].dateLastClinic);
                    $('#id').val(patientList[0].patientId);
                    clinicVisit(patientList[0].patientId);
                    artCommencement(patientList[0].patientId);
                    unSuppressed(patientList[0].patientId);
                    careSupport(patientList[0].patientId);
                    childFollowup(patientList[0].patientId);
                    ancRegistration(patientList[0].patientId);
                    labourDelivery(patientList[0].patientId);
                    laboratory(patientList[0].patientId);
                    pharmacy(patientList[0].patientId);
                    pmtct(patientList[0].gender);
                }
            }); //end of ajax call     
            $("#searchClinic").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#clinic tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
            $("#searchDispense").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#dispense tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
            //            $.getJSON("Pharmacy_grid.action?q=1&id=46712",
            //            {
            //            q: 1,
            //            id: 46712
            //            }, function (json) {
            //                console.log(json);
            //            });

            function clinicVisit(patientId) {
                $("#grid").jqGrid({
                    url: "Clinic_grid.action?q=1&id=" + patientId,
                    datatype: "json",
                    mtype: "GET",
                    colNames: ["Date of Visit", "Clinic Stage", "Functional Status", "TB Status", "Next Clinic Visit", "Action", "", ""],
                    colModel: [
                        {
                            name: "dateVisit",
                            index: "date1",
                            width: "120",
                            formatter: "date",
                            formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                        },
                        {name: "clinicStage", index: "clinicStage", width: "120"},
                        {name: "funcStatus", index: "funcStatus", width: "170"},
                        {name: "tbStatus", index: "tbStatus", width: "280"},
                        {
                            name: "nextAppointment",
                            index: "nextAppointment",
                            width: "150",
                            formatter: "date",
                            formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                        },
                        {
                            name: "patientId",
                            index: "patientId",
                            width: "280",
                            formatter: function (cellValue, options, rowObject) {
                                return "<button class='btn btn-info btn-sm' id='view_button' onClick='viewClinicVisit(" + cellValue + ");' data-toggle='tooltip' data-placement='left' title='View'>View</button><button class='btn btn-success btn-sm' data-toggle='tooltip' id='edit_button' onclick='editClinicVisit(" + cellValue + ")' data-placement='left' title='Edit'>Edit</button><button class='btn btn-danger btn-sm' data-toggle='tooltip' id='delete_button' onclick='deleteClinicVisit(" + cellValue + ")'data-placement='left' title='Delete'>Delete</button>";
                            }
                        },
                        {name: "dateVisit", index: "dateVisit", width: "100", hidden: true},
                        {name: "commence", index: "commence", width: "1", hidden: true},
                    ],
                    rowNum: -1,
                    sortname: "clinicId",
                    sortorder: "desc",
                    viewrecords: true,
                    imgpath: "themes/basic/images",
                    resizable: false,
                    height: 150,
                    pager: "#cpager",
                    jsonReader: {
                        root: "clinicList",
                        page: "currpage",
                        total: "totalpages",
                        records: "totalrecords",
                        repeatitems: false,
                        id: "clinicId"
                    }
                }); //end of detail jqGrid
            }

            //Pharmacy
            $("#dispense").jqGrid({
                url: 'Pharmacy_grid.action?q=1&id=46712',
                mtype: "GET",
                datatype: "json",
                colModel: [
                    {
                        label: 'Date of Refill', name: 'dateVisit', key: true, width: 160,
                        formatter: "date", formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {label: 'Drug Dispensed', name: 'description', width: 400},
                    {label: 'Refill Period', name: 'duration', width: 160},
                    {
                        label: 'Next Refill', name: 'nextAppointment', width: 160,
                        formatter: "date", formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    }
                ],
                viewrecords: true,
                //width: 780,
                height: 250,
                rowNum: 20,
                pager: "#dpager",
                jsonReader: {
                    root: "pharmacyList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "dispenseId"
                }
            });
            $("#care").jqGrid({
                url: 'Pharmacy_grid.action?q=1&id=46712',
                mtype: "GET",
                datatype: "json",
                colModel: [
                    {
                        label: 'Date of Devolved', name: 'dateDevolved', key: true, width: 150,
                        formatter: "date", formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {label: 'Viral Load', name: 'lastViralLoad', width: 120},
                    {label: 'CD4 Count', name: 'lastCd4', width: 120},
                    {label: 'Clinic Stage', name: 'lastClinicStage', width: 120},
                    {label: 'Regimen Dispensed', name: 'regimen', width: 220},
                    {label: 'Date of Next Visit', name: 'dateNextClinic', width: 150},
                ],
                viewrecords: true,
                //width: 780,
                height: 250,
                rowNum: 20,
                pager: "#carepager",
                jsonReader: {
                    root: "pharmacyList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "careId"
                }
            });

            function careSupport(patientId) {
                $("#support").jqGrid({
                    url: 'Eac_grid.action?q=1&id=' + patientId,
                    mtype: "GET",
                    datatype: "json",
                    colNames: ["Date of Visit", "ART Status", "Clinical Stage", "Last CD4 Result", "Last Viral Load Result", "Action", ""],
                    colModel: [
                        {
                            name: "dateVisit",
                            index: "date1",
                            width: "150",
                            formatter: "date",
                            formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                        },
                        {name: "currentStatus", index: "currentStatus", width: "150"},
                        {name: "clinicStage", index: "clinicStage", width: "150"},
                        {name: "lastCd4", index: "lastCd4", width: "170"},
                        {name: "lastViralLoad", index: "lastViralLoad", width: "230"},
                        {
                            name: "patientId",
                            index: "patientId",
                            width: "260",
                            formatter: function (cellValue, options, rowObject) {
                                return "<button class='btn btn-info btn-sm' id='view_button' onClick='viewButton(" + cellValue + ");' data-toggle='tooltip' data-placement='left' title='View'>View</button><button class='btn btn-success btn-sm' data-toggle='tooltip' id='edit_button' onclick='editButton(" + cellValue + ")' data-placement='left' title='Edit'>Edit</button><button class='btn btn-danger btn-sm' data-toggle='tooltip' id='delete_button' onclick='deleteButton(" + cellValue + ")'data-placement='left' title='Delete'>Delete</button>";
                            }
                        },
                        {name: "dateVisit", index: "dateVisit", width: "100", hidden: true},
                    ],
                    rowNum: -1,
                    sortname: "chroniccareId",
                    sortorder: "desc",
                    viewrecords: true,
                    imgpath: "themes/basic/images",
                    resizable: false,
                    height: 150,
                    jsonReader: {
                        root: "chroniccareList",
                        page: "currpage",
                        //total: "totalpages",
                        records: "totalrecords",
                        repeatitems: false,
                        id: "chroniccareId"
                    }
                }); //end of detail jqGrid
            }


            //Unsuppressed
            function unSuppressed(patientId) {
                $("#unsuppressed").jqGrid({
                    url: 'Eac_grid.action?q=1&id=' + patientId,
                    mtype: "GET",
                    datatype: "json",
                    colNames: ["Date of 1st EAC", "Date of 2nd EAC", "Date of 3rd EAC", "Date Repeat VL Sample collected", "Action", ""],
                    colModel: [
                        {
                            name: "dateVisit",
                            index: "date1",
                            width: "180",
                            formatter: "date",
                            formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                        },
                        {
                            name: "dateEac1",
                            index: "dateEac1",
                            width: "180",
                            formatter: "date",
                            formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                        },
                        {
                            name: "dateEac2",
                            index: "dateEac2",
                            width: "180",
                            formatter: "date",
                            formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                        },
                        {
                            name: "dateSampleCollected",
                            index: "dateSampleCollected",
                            width: "290",
                            formatter: "date",
                            formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                        },
                        {
                            name: "patientId",
                            index: "patientId",
                            width: "280",
                            formatter: function (cellValue, options, rowObject) {
                                return "<button class='btn btn-info btn-sm' id='view_button' onClick='viewButton(" + cellValue + ");' data-toggle='tooltip' data-placement='left' title='View'>View</button><button class='btn btn-success btn-sm' data-toggle='tooltip' id='edit_button' onclick='editButton(" + cellValue + ")' data-placement='left' title='Edit'>Edit</button><button class='btn btn-danger btn-sm' data-toggle='tooltip' id='delete_button' onclick='deleteButton(" + cellValue + ")'data-placement='left' title='Delete'>Delete</button>";
                            }
                        },
                        {name: "dateVisit", index: "dateVisit", width: "100", hidden: true}
                    ],
                    rowNum: -1,
                    sortname: "eacId",
                    sortorder: "desc",
                    viewrecords: true,
                    imgpath: "themes/basic/images",
                    resizable: false,
                    height: 150,
                    jsonReader: {
                        root: "eacList",
                        page: "currpage",
                        total: "totalpages",
                        records: "totalrecords",
                        repeatitems: false,
                        id: "eacId"
                    },
                    onSelectRow: function (id) {
                        if (id != null) {
                            var selectedRow = $("#detail").getGridParam('selrow');
                            if (selectedRow != null) {
                                var data = $("#detail").getRowData(selectedRow);
                                $("#dateVisit").val(data.dateVisit);
                            }
                            $("#eacId").val(id);
                            $("#new_button").html("View");
                        }
                    },
                    ondblClickRow: function (id) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            $("#dateVisit").val(data.dateVisit);
                        }
                        $("#eacId").val(id);
                        $("#lamisform").attr("action", "Eac_find");
                        $("#lamisform").submit();
                    }
                });
            }
        });

        function editArt() {
            //                data = $('#dateVisit').val();
            //                if(data == "") {
            //                    $("#lamisform").attr("action", "Commence_new");  
            //                } else {
            //                    $("#lamisform").attr("action", "Commence_find");                            
            //                }
            $("#lamisform").attr("action", "Commence_new");
            $("#lamisform").submit();
        }

        function artCommencement(jsonData) {
            $('#artStatus').html(jsonData.currentStatus);
            $('#artCD4').html(jsonData.lastCd4);
            $('#artStage').html(jsonData.lastClinicStage);
            $('#artRegStatus').html(jsonData.statusRegistration);
            $('#artLastVist').html(jsonData.dateLastClinic);
            $('#artStartDate').html(jsonData.dateStarted);
            $('#artRegimenType').html(jsonData.regimentype);
            $('#artRegimen').html(jsonData.regimen);
        }

        function viewChroniccare(data) {
            $("#id").val(data);
            $("#lamisform").attr("action", "Chroniccare_find");
            $("#lamisform").submit();
            return true;
        }

        function newChroniccare(data) {
            $("#id").val(data);
            $("#lamisform").attr("action", "Chroniccare_new");
            return true;
        }

        function viewClinicVisit(data) {
            $("#id").val(data);
            $("#lamisform").attr("action", "Clinic_find");
            $("#lamisform").submit();
            return true;
        }

        function newClinicVisit(data) {
            $("#id").val(data);
            $("#lamisform").attr("action", "Clinic_new");
            return true;
        }

        function newStatusUpdate(data) {
            $("#id").val(data);
            $("#lamisform").attr("action", "Status_new");
            return true;
        }

        function childFollowup(patientId) {
            $("#childfollowup").jqGrid({
                ul: "Childfollowup_grid.action?q=1&childId=" + patientId,
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Visit", "Feeding at Present", "Outcome", "Rapid Test Result", "On Cotrim", "Actions"],
                colModel: [
                    {
                        name: "dateVisit",
                        index: "dateVisit",
                        width: "170",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "feeding", index: "feeding", width: "200"},
                    {name: "childOutcome", index: "childOutcome", width: "160"},
                    {name: "rapidTestResult", index: "rapidTestResult", width: "170"},
                    {name: "cotrim", index: "cotrim", width: "130"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "290",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button class='btn btn-info btn-sm' onClick='viewButton(" + cellValue + ");' data-toggle='tooltip' data-placement='left' title='View'>View</button><button class='btn btn-success btn-sm' data-toggle='tooltip' onclick='editButton(" + cellValue + ")' data-placement='left' title='Edit'>Edit</button><button class='btn btn-danger btn-sm' data-toggle='tooltip' onclick='deleteButton(" + cellValue + ")'data-placement='left' title='Delete'>Delete</button>";
                        }
                    }
                ],
                rowNum: -1,
                sortname: "childfollowupId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "childfollowupList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "childfollowupId"
                },
                afterInsertRow: function (id) {
                    var content = $("#detail").getCell(id, "feeding");
                    $("#detail").setCell(id, "feeding", content.substring(content.indexOf("-") + 2), '', '');

                    content = $("#detail").getCell(id, "childOutcome");
                    $("#detail").setCell(id, "childOutcome", content.substring(content.indexOf("-") + 2), '', '');
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            var date = data.dateVisit;
                            $("#dateVisit").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                        }
                        $("#childfollowupId").val(id);
                        $("#new_button").html("View");
                    }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);
                        var date = data.dateVisit;
                        $("#dateVisit").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    }
                    $("#childfollowupId").val(id);
                    $("#lamisform").attr("action", "Childfollowup_find");
                    $("#lamisform").submit();
                }
            }); //end of detail jqGrid       
        }

        function labourDelivery(patientId) {
            $("#delivery").jqGrid({
                url: "Delivery_grid.action?q=1&id=" + patientId,
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Delivery", "Mode of Delivery", "Maternal Outcome", "Actions"],
                colModel: [
                    {
                        name: "dateDelivery",
                        index: "dateDelivery",
                        width: "205",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "modeDelivery", index: "modeDelivery", width: "330"},
                    {name: "maternalOutcome", index: "maternalOutcome", width: "300"},
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "280",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button class='btn btn-info btn-sm' onClick='viewButton(" + cellValue + ");' data-toggle='tooltip' data-placement='left' title='View'>View</button><button class='btn btn-success btn-sm' data-toggle='tooltip' onclick='editButton(" + cellValue + ")' data-placement='left' title='Edit'>Edit</button><button class='btn btn-danger btn-sm' data-toggle='tooltip' onclick='deleteButton(" + cellValue + ")'data-placement='left' title='Delete'>Delete</button>";
                        }
                    }
                ],
                rowNum: -1,
                sortname: "deliveryId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "deliveryList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "deliveryId"
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            var date = data.dateDelivery;
                            $("#dateDelivery").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                        }
                        $("#deliveryId").val(id);
                        // $("#new_button").html("View");
                    }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#delivery").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#delivery").getRowData(selectedRow);
                        var date = data.dateDelivery;
                        $("#dateDelivery").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    }
                    $("#deliveryId").val(id);
                    $("#lamisform").attr("action", "Delivery_find");
                    $("#lamisform").submit();
                }
            }); //end of detail jqGrid   
        }

        function ancRegistration(patientId) {
            $("#anc").jqGrid({
                url: "Anc_grid.action?q=1&id=" + patientId,
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Visit", "Source of Referral", "LMP", "EDD", "Actions"],
                colModel: [
                    {
                        name: "dateVisit",
                        index: "dateVisit",
                        width: "160",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "sourceReferral", index: "sourceReferral", width: "300"},
                    {
                        name: "lmp",
                        index: "lmp",
                        width: "193",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "edd",
                        index: "edd",
                        width: "193",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "patientId",
                        index: "patientId",
                        width: "280",
                        formatter: function (cellValue, options, rowObject) {
                            return "<button class='btn btn-info btn-sm' id='view_button' onClick='viewButton(" + cellValue + ");' data-toggle='tooltip' data-placement='left' title='View'>View</button><button class='btn btn-success btn-sm' data-toggle='tooltip' id='edit_button' onclick='editButton(" + cellValue + ")' data-placement='left' title='Edit'>Edit</button><button class='btn btn-danger btn-sm' data-toggle='tooltip' id='delete_button' onclick='deleteButton(" + cellValue + ")'data-placement='left' title='Delete'>Delete</button>";
                        }
                    }
                ],
                rowNum: -1,
                sortname: "ancId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "ancList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "ancId"
                },
                onSelectRow: function (id) {
                    if (id != null) {
                        var selectedRow = $("#detail").getGridParam('selrow');
                        if (selectedRow != null) {
                            var data = $("#detail").getRowData(selectedRow);
                            var date = data.dateVisit;
                            $("#dateVisit").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                        }
                        $("#ancId").val(id);
                        $("#new_button").html("View");
                    }
                },
                ondblClickRow: function (id) {
                    var selectedRow = $("#detail").getGridParam('selrow');
                    if (selectedRow != null) {
                        var data = $("#detail").getRowData(selectedRow);
                        var date = data.dateVisit;
                        $("#dateVisit").val(date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6));
                    }
                    $("#ancId").val(id);
                    $("#lamisform").attr("action", "Anc_find");
                    $("#lamisform").submit();
                }
            }); //end of detail jqGrid      
        }

        function laboratory(patientId) {
            $(".laboratory").jqGrid({
                url: "Laboratory_grid.action?q=1&id=" + patientId,
                datatype: "json",
                mtype: "GET",
                colNames: ["Sample Collected", "Result Reported", "Lab No", "Description", "Absolute", "Relative", ""],
                colModel: [
                    {
                        name: "dateCollected",
                        index: "dateCollected",
                        width: "170",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {
                        name: "dateReported",
                        index: "date1",
                        width: "170",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "labno", index: "labno", width: "140"},
                    {name: "description", index: "description", width: "200"},
                    {name: "resultab", index: "resultab", width: "110"},
                    {name: "resultpc", index: "resultpc", width: "110"},
                    {name: "dateReported", index: "dateReported", width: "100", hidden: true}
                ],
                rowNum: -1,
                sortname: "laboratoryId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "laboratoryList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "laboratoryId"
                }

            }); //end of detail jqGrid        
        }

        function pharmacy(patientId) {
            $(".pharmac").jqGrid({
                url: "Pharmacy_grid.action?q=1&id=" + patientId,
                datatype: "json",
                mtype: "GET",
                colNames: ["Date of Refill", "Drug Dispensed", "Refill Period", "Next Refill", ""],
                colModel: [
                    {
                        name: "dateVisit",
                        index: "date1",
                        width: "200",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "description", index: "description", width: "400"},
                    {name: "duration", index: "duration", width: "170"},
                    {
                        name: "nextAppointment",
                        index: "nextAppointment",
                        width: "170",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
                    },
                    {name: "dateVisit", index: "dateVisit", width: "100", hidden: true},
                ],
                rowNum: -1,
                sortname: "pharmacyId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "pharmacyList",
                    page: "currpage",
                    //total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "pharmacyId"
                }
            }); //end of detail jqGrid  
        }
    </script>
    <style class="cp-pen-styles">
        *, *::before, *::after {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        p:not(:last-child) {
            margin: 0 0 20px;
        }

        section {
            display: none;
            padding: 20px 0 0;
            border-top: 1px solid #abc;
        }

        input {
            display: none;
        }

        label {
            display: inline-block;
            margin: 0 0 -1px;
            padding: 15px 25px;
            font-weight: 600;
            text-align: center;
            color: #abc;
            border: 1px solid transparent;
        }

        label:before {
            font-family: fontawesome;
            font-weight: normal;
            margin-right: 10px;
        }

        label:hover {
            color: #789;
            cursor: pointer;
        }

        input:checked + label {
            color: #0af;
            border: 1px solid #abc;
            border-top: 2px solid #0af;
            border-bottom: 1px solid #fff;
        }

        #tab1:checked ~ #content1,
        #tab2:checked ~ #content2,
        #tab3:checked ~ #content3,
        #tab4:checked ~ #content4 {
            display: block;
        }

        @media screen and (max-width: 800px) {
            label {
                font-size: 0;
            }

            label:before {
                margin: 0;
                font-size: 18px;
            }
        }

        @media screen and (max-width: 500px) {
            label {
                padding: 15px;
            }
        }
    </style>
</head>

<body>
<span id="style"></span>
<!-- Navbar -->
<jsp:include page="/WEB-INF/views/template/header.jsp"/>

<div class="mt-5"></div>
<div class="content col-12 mr-auto ml-auto">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page">Home</a></li>
        <li class="breadcrumb-item"><a href="Pharmacy_page">Pharmacy</a></li>
        <li class="breadcrumb-item active">Patient Details</li>
    </ol>

    <!-- Summary Section -->
    <s:form id="lamisform" theme="css_xhtml">
        <input type="hidden" id="patientId">
        <input name="hospitalNum" type="hidden" id="hospitalNum"/>
        <input name="dateVisit" type="hidden" id="dateVisit"/>
        <input name="commence" type="hidden" value="1" id="commence"/>
        <input name="currentStatus" type="hidden" id="currentStatus"/>
        <input name="dateCurrentStatus" type="hidden" id="dateCurrentStatus"/>
        <div class="row">

            <div class="col-12">
                <div class="card card-user">
                    <div class="card-header">
                        <div class="title"><i class="fa fa-user" style="font-size:44px;"></i> Profile</div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-12">
                                <table class="table table-striped">
                                    <tr>
                                        <th>Hospital Number</th>
                                        <td id="hospitalNo"></td>
                                        <th>Name</th>
                                        <td id="pname"></td>
                                    </tr>
                                    <tr>
                                        <th>Gender</th>
                                        <td id="gender"></td>
                                        <th>Marital Status</th>
                                        <td id="mstatus"></td>
                                    </tr>
                                    <tr>
                                        <th>Phone</th>
                                        <td id="phone"></td>
                                        <th>Address</th>
                                        <td id="address"></td>
                                    </tr>
                                    <tr>
                                        <th>LGA</th>
                                        <td id="lga"></td>
                                        <th>State</th>
                                        <td id="state"></td>
                                    </tr>
                                    <tr>
                                        <th>Occupation</th>
                                        <td id="occupation"></td>
                                        <th>Last Clinic Visit</th>
                                        <td id="lastVisit"></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <input id="tab1" type="radio" name="tabs" checked>
                        <label for="tab1">Drug Dispensing</label>

                        <input id="tab2" type="radio" name="tabs">
                        <label for="tab2" id="label2">Differentiated Care </label>

                        <input id="tab3" type="radio" name="tabs">
                        <label for="tab3">Drug Prescription</label>
                        <section id="content1">
                            <!-- Drug Dispensing -->
                            <div class="row">
                                <div class="col-6">
                                    <button onclick="newDispensing();" class="btn btn-info">New</button>
                                </div>
                                <div class="col-6">
                                    <div class="input-group no-border col-6 pull-right">
                                        <input id="searchDispensing" type="text" class="form-control"
                                               placeholder="Search...">
                                        <div class="input-group-append">
                                            <div class="input-group-text">
                                                <i class="now-ui-icons ui-1_zoom-bold"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <div class="table-responsive">
                                        <table id="dispensing" class="table table-striped"></table>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section id="content2">
                            <!-- Differentiated Care -->
                            <div class="row">
                                <div class="col-6">
                                    <button onclick="newDifferentiated();" class="btn btn-info">New</button>
                                </div>
                                <div class="col-6">
                                    <div class="input-group no-border col-6 pull-right">
                                        <input id="searchDifferentiated" type="text" class="form-control"
                                               placeholder="Search...">
                                        <div class="input-group-append">
                                            <div class="input-group-text">
                                                <i class="now-ui-icons ui-1_zoom-bold"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <div class="table-responsive">
                                        <table id="differentiated" class="table table-striped"></table>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section id="content3">
                            <!-- Drug Prescription -->
                            <div class="row">
                                <div class="col-6">
                                    <button onclick="newPrescription();" class="btn btn-info">New</button>
                                </div>
                                <div class="col-6">
                                    <div class="input-group no-border col-6 pull-right">
                                        <input id="searchPrescription" type="text" class="form-control"
                                               placeholder="Search...">
                                        <div class="input-group-append">
                                            <div class="input-group-text">
                                                <i class="now-ui-icons ui-1_zoom-bold"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <div class="table-responsive">
                                        <table id="prescription" class="table table-striped"></table>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>

    </s:form>
</div>
<div id="user_group1" style="display: none">
    <s:property value="#session.userGroup"/>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>