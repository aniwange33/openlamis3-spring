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

        /*main {
          max-width: 800px;
          padding: 40px;
          border: 1px solid rgba(0, 0, 0, 0.2);
          background: #fff;
          box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }*/

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

        label[for*='1']:before {
            content: '\f1cb';
        }

        label[for*='2']:before {
            content: '\f17d';
        }

        label[for*='3']:before {
            content: '\f16c';
        }

        label[for*='4']:before {
            content: '\f171';
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
<!-- Navbar -->
<jsp:include page="/WEB-INF/views/template/header.jsp"/>

<div class="mt-5"></div>
<div class="content col-12 mr-auto ml-auto">
    <!--            <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="Dashboard_page">Visualizer</a></li>
                    <li class="breadcrumb-item active">Dashboard</li>
                </ol>-->

    <!-- Summary Section -->
    <form id="lamisform" theme="css_xhtml">
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
                        <label for="tab1">ART Clinic</label>

                        <input id="tab2" type="radio" name="tabs">
                        <label for="tab2">Dribbble</label>

                        <input id="tab3" type="radio" name="tabs">
                        <label for="tab3">Stack Overflow</label>

                        <input id="tab4" type="radio" name="tabs">
                        <label for="tab4">Bitbucket</label>

                        <section id="content1">
                            <!-- ART Clinic -->
                            <div class="row">
                                <div id="accordion" role="tablist" aria-multiselectable="true"
                                     class="card-collapse col-12">
                                    <div class="card card-plain">
                                        <div class="card-header" role="tab" id="careheading">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#clinictab"
                                               aria-expanded="false" aria-controls="clinictab" class="collapsed">
                                                Clinic Visit <i class="now-ui-icons arrows-1_minimal-down"></i>
                                            </a>
                                        </div>

                                        <div id="clinictab" class="collapse" role="tabpanel"
                                             aria-labelledby="clinicheading">
                                            <div class="card-body">
                                                <div class="row">
                                                    <div class="col-6">
                                                        <button onclick="newClinicVisit" class="btn btn-info">New
                                                        </button>
                                                    </div>
                                                    <div class="col-6">
                                                        <div class="input-group no-border col-6 pull-right">
                                                            <input id="searchCare" type="text" class="form-control"
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
                                                    <div class="col-12 mr-auto ml-auto">
                                                        <div class="table-responsive">
                                                            <table id="grid"
                                                                   class="table table-bordered table-striped table-hover"></table>
                                                            <div id="cpager" style="text-align:center;"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card card-plain">
                                        <div class="card-header" role="tab" id="artheading">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#arttab"
                                               aria-expanded="false" aria-controls="arttab" class="collapsed">
                                                ART Commencement <i class="now-ui-icons arrows-1_minimal-down"></i>
                                            </a>
                                        </div>

                                        <div id="arttab" class="collapse" role="tabpanel" aria-labelledby="artheading">
                                            <div class="card-body">

                                                <div class="row">
                                                    <div class="col-12 mr-auto ml-auto">
                                                        <div class="btn-group">
                                                            <button onClick="editArt();" class="btn btn-info">New
                                                            </button>
                                                        </div>
                                                        <div class="table-responsive">
                                                            <table class="table table-striped">
                                                                <tr>
                                                                    <th>ART Status</th>
                                                                    <td id="artStatus"></td>
                                                                    <th>Current CD4</th>
                                                                    <td id="artCD4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <th>Last Clinic Visit</th>
                                                                    <td id="artLastVisit"></td>
                                                                    <th>ART Start Date</th>
                                                                    <td id="artStartDate"></td>
                                                                </tr>
                                                                <tr>
                                                                    <th>Original Regimen Line</th>
                                                                    <td id="artRegimentType"></td>
                                                                    <th>Original Regimen</th>
                                                                    <td id="artRegimen"></td>
                                                                </tr>
                                                                <tr>
                                                                    <th>Status at Registration</th>
                                                                    <td id="artRegStatus"></td>
                                                                    <th>Clinic Stage</th>
                                                                    <td id="artStage"></td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card card-plain">
                                        <div class="card-header" role="tab" id="supportheading">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#supporttab"
                                               aria-expanded="false" aria-controls="supporttab" class="collapsed">
                                                Care and Support Assessment <i
                                                    class="now-ui-icons arrows-1_minimal-down"></i>
                                            </a>
                                        </div>

                                        <div id="supporttab" class="collapse" role="tabpanel"
                                             aria-labelledby="supportheading">
                                            <div class="card-body">
                                                <div class="row">
                                                    <div class="col-6">
                                                        <button onclick="newUnsuppressed" class="btn btn-info">New
                                                        </button>
                                                    </div>
                                                    <div class="col-6">
                                                        <div class="input-group no-border col-6 pull-right">
                                                            <input id="searchUnsuppressed" type="text"
                                                                   class="form-control" placeholder="Search...">
                                                            <div class="input-group-append">
                                                                <div class="input-group-text">
                                                                    <i class="now-ui-icons ui-1_zoom-bold"></i>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 mr-auto ml-auto">
                                                        <div class="table-responsive">
                                                            <table id="support"></table>
                                                            <div id="supportpager" style="text-align:center;"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card card-plain">
                                        <div class="card-header" role="tab" id="cstatusheading">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#cstatustab"
                                               aria-expanded="false" aria-controls="cstatustab" class="collapsed">
                                                Clinic Status Update <i class="now-ui-icons arrows-1_minimal-down"></i>
                                            </a>
                                        </div>

                                        <div id="cstatustab" class="collapse" role="tabpanel"
                                             aria-labelledby="cstatusheading">
                                            <div class="card-body">
                                                <div class="row">
                                                    <div class="col-6">
                                                        <button onclick="newUnsuppressed" class="btn btn-info">New
                                                        </button>
                                                    </div>
                                                    <div class="col-6">
                                                        <div class="input-group no-border col-6 pull-right">
                                                            <input id="searchUnsuppressed" type="text"
                                                                   class="form-control" placeholder="Search...">
                                                            <div class="input-group-append">
                                                                <div class="input-group-text">
                                                                    <i class="now-ui-icons ui-1_zoom-bold"></i>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 mr-auto ml-auto">
                                                        <div class="table-responsive">
                                                            <table id="cstatus"></table>
                                                            <div id="cstatuspager" style="text-align:center;"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card card-plain">
                                        <div class="card-header" role="tab" id="unsuppressedheading">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#unsuppressedtab"
                                               aria-expanded="false" aria-controls="unsuppressedtab" class="collapsed">
                                                Unsuppressed Client Monitoring/ Enhanced Adherence Counseling <i
                                                    class="now-ui-icons arrows-1_minimal-down"></i>
                                            </a>
                                        </div>

                                        <div id="unsuppressedtab" class="collapse" role="tabpanel"
                                             aria-labelledby="unsuppressedheading">
                                            <div class="card-body">
                                                <div class="row">
                                                    <div class="col-6">
                                                        <button onclick="newUnsuppressed()" class="btn btn-info">New
                                                        </button>
                                                    </div>
                                                    <div class="col-6">
                                                        <div class="input-group no-border col-6 pull-right">
                                                            <input id="searchUnsuppressed" type="text"
                                                                   class="form-control" placeholder="Search...">
                                                            <div class="input-group-append">
                                                                <div class="input-group-text">
                                                                    <i class="now-ui-icons ui-1_zoom-bold"></i>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 mr-auto ml-auto">
                                                        <div class="table-responsive">
                                                            <table id="unsuppressed"></table>
                                                            <div id="unsuppressedpager"
                                                                 style="text-align:center;"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section id="content2">
                            <p>
                                Bacon ipsum dolor sit amet landjaeger sausage brisket, jerky drumstick fatback boudin.
                            </p>
                            <p>
                                Jerky jowl pork chop tongue, kielbasa shank venison. Capicola shank pig ribeye leberkas
                                filet mignon brisket beef kevin tenderloin porchetta. Capicola fatback venison shank
                                kielbasa, drumstick ribeye landjaeger beef kevin tail meatball pastrami prosciutto
                                pancetta. Tail kevin spare ribs ground round ham ham hock brisket shoulder. Corned beef
                                tri-tip leberkas flank sausage ham hock filet mignon beef ribs pancetta turkey.
                            </p>
                        </section>

                        <section id="content3">
                            <p>
                                Jerky jowl pork chop tongue, kielbasa shank venison. Capicola shank pig ribeye leberkas
                                filet mignon brisket beef kevin tenderloin porchetta. Capicola fatback venison shank
                                kielbasa, drumstick ribeye landjaeger beef kevin tail meatball pastrami prosciutto
                                pancetta. Tail kevin spare ribs ground round ham ham hock brisket shoulder. Corned beef
                                tri-tip leberkas flank sausage ham hock filet mignon beef ribs pancetta turkey.
                            </p>
                            <p>
                                Bacon ipsum dolor sit amet landjaeger sausage brisket, jerky drumstick fatback boudin.
                            </p>
                        </section>

                        <section id="content4">
                            <p>
                                Bacon ipsum dolor sit amet landjaeger sausage brisket, jerky drumstick fatback boudin.
                            </p>
                            <p>
                                Jerky jowl pork chop tongue, kielbasa shank venison. Capicola shank pig ribeye leberkas
                                filet mignon brisket beef kevin tenderloin porchetta. Capicola fatback venison shank
                                kielbasa, drumstick ribeye landjaeger beef kevin tail meatball pastrami prosciutto
                                pancetta. Tail kevin spare ribs ground round ham ham hock brisket shoulder. Corned beef
                                tri-tip leberkas flank sausage ham hock filet mignon beef ribs pancetta turkey.
                            </p>
                        </section>
                    </div>
                </div>
            </div>
        </div>

        <!-- ART Clinic -->
        <div class="row">
            <div class="col-md-12">
                <div class="card ">
                    <div class="card-header">
                        <h4 class="card-title">
                            ART Clinic
                        </h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-12">
                                <div class="row">
                                    <div id="accordion" role="tablist" aria-multiselectable="true"
                                         class="card-collapse col-12">
                                        <div class="card card-plain">
                                            <div class="card-header" role="tab" id="careheading">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#clinictab"
                                                   aria-expanded="false" aria-controls="clinictab" class="collapsed">
                                                    Clinic Visit <i class="now-ui-icons arrows-1_minimal-down"></i>
                                                </a>
                                            </div>

                                            <div id="clinictab" class="collapse" role="tabpanel"
                                                 aria-labelledby="clinicheading">
                                                <div class="card-body">
                                                    <div class="row">
                                                        <div class="col-6">
                                                            <button onclick="newClinicVisit" class="btn btn-info">New
                                                            </button>
                                                        </div>
                                                        <div class="col-6">
                                                            <div class="input-group no-border col-6 pull-right">
                                                                <input id="searchCare" type="text" class="form-control"
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
                                                        <div class="col-12 mr-auto ml-auto">
                                                            <div class="table-responsive">
                                                                <table id="grid"
                                                                       class="table table-bordered table-striped table-hover"></table>
                                                                <div id="cpager" style="text-align:center;"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card card-plain">
                                            <div class="card-header" role="tab" id="artheading">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#arttab"
                                                   aria-expanded="false" aria-controls="arttab" class="collapsed">
                                                    ART Commencement <i class="now-ui-icons arrows-1_minimal-down"></i>
                                                </a>
                                            </div>

                                            <div id="arttab" class="collapse" role="tabpanel"
                                                 aria-labelledby="artheading">
                                                <div class="card-body">

                                                    <div class="row">
                                                        <div class="col-12 mr-auto ml-auto">
                                                            <div class="btn-group">
                                                                <button onClick="editArt();" class="btn btn-info">New
                                                                </button>
                                                            </div>
                                                            <div class="table-responsive">
                                                                <table class="table table-striped">
                                                                    <tr>
                                                                        <th>ART Status</th>
                                                                        <td id="artStatus"></td>
                                                                        <th>Current CD4</th>
                                                                        <td id="artCD4"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>Last Clinic Visit</th>
                                                                        <td id="artLastVisit"></td>
                                                                        <th>ART Start Date</th>
                                                                        <td id="artStartDate"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>Original Regimen Line</th>
                                                                        <td id="artRegimentType"></td>
                                                                        <th>Original Regimen</th>
                                                                        <td id="artRegimen"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <th>Status at Registration</th>
                                                                        <td id="artRegStatus"></td>
                                                                        <th>Clinic Stage</th>
                                                                        <td id="artStage"></td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card card-plain">
                                            <div class="card-header" role="tab" id="supportheading">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#supporttab"
                                                   aria-expanded="false" aria-controls="supporttab" class="collapsed">
                                                    Care and Support Assessment <i
                                                        class="now-ui-icons arrows-1_minimal-down"></i>
                                                </a>
                                            </div>

                                            <div id="supporttab" class="collapse" role="tabpanel"
                                                 aria-labelledby="supportheading">
                                                <div class="card-body">
                                                    <div class="row">
                                                        <div class="col-6">
                                                            <button onclick="newUnsuppressed" class="btn btn-info">New
                                                            </button>
                                                        </div>
                                                        <div class="col-6">
                                                            <div class="input-group no-border col-6 pull-right">
                                                                <input id="searchUnsuppressed" type="text"
                                                                       class="form-control" placeholder="Search...">
                                                                <div class="input-group-append">
                                                                    <div class="input-group-text">
                                                                        <i class="now-ui-icons ui-1_zoom-bold"></i>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-12 mr-auto ml-auto">
                                                            <div class="table-responsive">
                                                                <table id="support"></table>
                                                                <div id="supportpager" style="text-align:center;"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card card-plain">
                                            <div class="card-header" role="tab" id="cstatusheading">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#cstatustab"
                                                   aria-expanded="false" aria-controls="cstatustab" class="collapsed">
                                                    Clinic Status Update <i
                                                        class="now-ui-icons arrows-1_minimal-down"></i>
                                                </a>
                                            </div>

                                            <div id="cstatustab" class="collapse" role="tabpanel"
                                                 aria-labelledby="cstatusheading">
                                                <div class="card-body">
                                                    <div class="row">
                                                        <div class="col-6">
                                                            <button onclick="newUnsuppressed" class="btn btn-info">New
                                                            </button>
                                                        </div>
                                                        <div class="col-6">
                                                            <div class="input-group no-border col-6 pull-right">
                                                                <input id="searchUnsuppressed" type="text"
                                                                       class="form-control" placeholder="Search...">
                                                                <div class="input-group-append">
                                                                    <div class="input-group-text">
                                                                        <i class="now-ui-icons ui-1_zoom-bold"></i>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-12 mr-auto ml-auto">
                                                            <div class="table-responsive">
                                                                <table id="cstatus"></table>
                                                                <div id="cstatuspager" style="text-align:center;"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card card-plain">
                                            <div class="card-header" role="tab" id="unsuppressedheading">
                                                <a data-toggle="collapse" data-parent="#accordion"
                                                   href="#unsuppressedtab" aria-expanded="false"
                                                   aria-controls="unsuppressedtab" class="collapsed">
                                                    Unsuppressed Client Monitoring/ Enhanced Adherence Counseling <i
                                                        class="now-ui-icons arrows-1_minimal-down"></i>
                                                </a>
                                            </div>

                                            <div id="unsuppressedtab" class="collapse" role="tabpanel"
                                                 aria-labelledby="unsuppressedheading">
                                                <div class="card-body">
                                                    <div class="row">
                                                        <div class="col-6">
                                                            <button onclick="newUnsuppressed()" class="btn btn-info">
                                                                New
                                                            </button>
                                                        </div>
                                                        <div class="col-6">
                                                            <div class="input-group no-border col-6 pull-right">
                                                                <input id="searchUnsuppressed" type="text"
                                                                       class="form-control" placeholder="Search...">
                                                                <div class="input-group-append">
                                                                    <div class="input-group-text">
                                                                        <i class="now-ui-icons ui-1_zoom-bold"></i>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-12 mr-auto ml-auto">
                                                            <div class="table-responsive">
                                                                <table id="unsuppressed"></table>
                                                                <div id="unsuppressedpager"
                                                                     style="text-align:center;"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <div class="card ">
                                    <div class="card-header">
                                        <h4 class="card-title">
                                            PMTCT
                                        </h4>
                                    </div>
                                    <div class="card-body">
                                        <div class="row">
                                            <div id="accordion" role="tablist" aria-multiselectable="true"
                                                 class="card-collapse col-12">
                                                <div class="card card-plain">
                                                    <div class="card-header" role="tab" id="ancheading">
                                                        <a data-toggle="collapse" data-parent="#accordion"
                                                           href="#anctab" aria-expanded="false" aria-controls="anctab"
                                                           class="collapsed">
                                                            ANC Registration <i
                                                                class="now-ui-icons arrows-1_minimal-down"></i>
                                                        </a>
                                                    </div>

                                                    <div id="anctab" class="collapse" role="tabpanel"
                                                         aria-labelledby="careheading">
                                                        <div class="card-body">
                                                            <div class="row">
                                                                <div class="col-6">
                                                                    <button onclick="ancRegistration()"
                                                                            class="btn btn-info">New
                                                                    </button>
                                                                </div>
                                                                <div class="col-6">
                                                                    <div class="input-group no-border col-6 pull-right">
                                                                        <input id="searchUnsuppressed" type="text"
                                                                               class="form-control"
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
                                                                <div class="col-12 mr-auto ml-auto">

                                                                    <div class="table-responsive">
                                                                        <table id="anc"></table>
                                                                        <div id="ancpager"
                                                                             style="text-align:center;"></div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="card card-plain">
                                                    <div class="card-header" role="tab" id="labourheading">
                                                        <a data-toggle="collapse" data-parent="#accordion"
                                                           href="#labourtab" aria-expanded="false"
                                                           aria-controls="labourtab" class="collapsed">
                                                            Labour and Delivery <i
                                                                class="now-ui-icons arrows-1_minimal-down"></i>
                                                        </a>
                                                    </div>

                                                    <div id="labourtab" class="collapse" role="tabpanel"
                                                         aria-labelledby="labourheading">
                                                        <div class="card-body">
                                                            <div class="row">
                                                                <div class="col-6">
                                                                    <button onclick="newDelivery();"
                                                                            class="btn btn-info">New
                                                                    </button>
                                                                </div>
                                                                <div class="col-6">
                                                                    <div class="input-group no-border col-6 pull-right">
                                                                        <input id="searchUnsuppressed" type="text"
                                                                               class="form-control"
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
                                                                <div class="col-12 mr-auto ml-auto">

                                                                    <div class="table-responsive">
                                                                        <table id="delivery"></table>
                                                                        <div id="deliverypager"
                                                                             style="text-align:center;"></div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="card card-plain">
                                                    <div class="card-header" role="tab" id="cupdateheading">
                                                        <a data-toggle="collapse" data-parent="#accordion"
                                                           href="#cupdatetab" aria-expanded="false"
                                                           aria-controls="cupdatetab" class="collapsed">
                                                            ANC/PNC Visit<i
                                                                class="now-ui-icons arrows-1_minimal-down"></i>
                                                        </a>
                                                    </div>

                                                    <div id="cupdatetab" class="collapse" role="tabpanel"
                                                         aria-labelledby="cupdateheading">
                                                        <div class="card-body">
                                                            <div class="row">
                                                                <div class="col-6">
                                                                    <button onclick="newUnsuppressed"
                                                                            class="btn btn-info">New
                                                                    </button>
                                                                </div>
                                                                <div class="col-6">
                                                                    <div class="input-group no-border col-6 pull-right">
                                                                        <input id="searchUnsuppressed" type="text"
                                                                               class="form-control"
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
                                                                <div class="col-12 mr-auto ml-auto">
                                                                    <div class="table-responsive">
                                                                        <table id="cupdate"></table>
                                                                        <div id="cupdatepager"
                                                                             style="text-align:center;"></div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="card card-plain">
                                                    <div class="card-header" role="tab" id="followupheading">
                                                        <a class="collapsed" data-toggle="collapse"
                                                           data-parent="#accordion" href="#followuptab"
                                                           aria-expanded="false" aria-controls="followuptab">
                                                            Child Follow-up <i
                                                                class="now-ui-icons arrows-1_minimal-down"></i>
                                                        </a>
                                                    </div>
                                                    <div id="followuptab" class="collapse" role="tabpanel"
                                                         aria-labelledby="followupheading">
                                                        <div class="card-body">
                                                            <div class="row">
                                                                <div class="col-6">
                                                                    <button onclick="newChildFollowup();"
                                                                            class="btn btn-info">New
                                                                    </button>
                                                                </div>
                                                                <div class="col-6">
                                                                    <div class="input-group no-border col-6 pull-right">
                                                                        <input id="searchUnsuppressed" type="text"
                                                                               class="form-control"
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
                                                                <div class="col-12 mr-auto ml-auto">
                                                                    <div class="table-responsive">
                                                                        <table id="childfollowup"></table>
                                                                        <div id="cfpager"
                                                                             style="text-align:center;"></div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
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