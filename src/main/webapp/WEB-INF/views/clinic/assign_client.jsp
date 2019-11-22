<%-- 
    Document   : assign_casemanager
    Created on : Oct 13, 2017, 6:21:47 AM
    Author     : user10
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/javascript" src="/js/lamis/lamis-common.js"></script>
    <script type="text/javascript" src="/js/lamis/search-common.js"></script>
    <script type="text/javascript" src="/js/lamis/report-common.js"></script>
    <script type="text/JavaScript">
        var enablePadding = true;
        var patientIds = [];
        var selectedIds;
        var caseManagerId;
        var selectedValues = [];
        var assign = false;
        var done = false;
        function loadClients() {
            var url = "/api/case-manager/client-search";
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    categoryId: $("#categoryId").val(),
                    "state": $("#state").val(),
                    "lga": $("#lga").val(),
                    "gender": $("#gender").val(),
                    "ageGroup": $("#ageGroup").val(),
                    "showAssigned": $("#showAssigned").prop("checked"),
                    "pregnancyStatus": $("#pregnancyStatus").val()
                })
            }).then(function (res) {
                return res.json()
            }).then(function (res) {
                var data = {'clientSearchList': res};
                $("#grid").jqGrid("clearGridData", true).setGridParam({
                    datatype: 'jsonstring',
                    datastr: data,
                    page: 1,
                }).trigger("reloadGrid");
                $("#loader_client").html('');
                done = true;

                fetch('/api/case-manager/category-counts', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        categoryId: $("#categoryId").val(),
                        "state": $("#state").val(),
                        "lga": $("#lga").val(),
                        "gender": $("#gender").val(),
                        "ageGroup": $("#ageGroup").val(),
                        "showAssigned": $("#showAssigned").prop("checked"),
                        "pregnancyStatus": $("#pregnancyStatus").val()
                    })
                }).then(function (res) {
                    return res.json()
                }).then(function (clientsCategoryCountMap) {
                    $("#count_stable").html(clientsCategoryCountMap.stable);
                    $("#count_unstable_less").html(clientsCategoryCountMap.unstable_less);
                    $("#count_unstable_more").html(clientsCategoryCountMap.unstable_more);
                    $("#count_pre_art").html(clientsCategoryCountMap.preart);
                    $("#countstd").show();
                });
            });
        }
        $(document).ready(function () {
            resetPage();
            initializeClients();
            reports();
            checkOnlyAssigned();
            $("#detailstd").hide();
            $("#detailstd1").hide();

            $("#loader_client").html('<img id="loader_image" src="images/loader_small.gif" />');

            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#grid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });

            var lastSelected = -99;
            $("#grid").jqGrid({
                colNames: ["Hospital No", "Name", "Gender", "Date of Birth", "Address", "ART Status", "Assigned To"],
                colModel: [
                    //{name: "sel", index: "sel", width: "50", align: "center", formatter:"checkbox", editoptions:{value:"1:0"}, formatoptions:{disabled:false}},
                    {name: "hospitalNum", index: "hospitalNum", width: "100"},
                    {name: "name", index: "name", width: "150"},
                    {name: "gender", index: "gender", width: "80"},
                    {
                        name: "dateBirth",
                        index: "dateBirth",
                        width: "100",
                        formatter: "date",
                        formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
                    },
                    {name: "address", index: "address", width: "200"},
                    {name: "currentStatus", index: "currentStatus", width: "150"},
                    {name: "fullName", index: "fullName", width: "150"}
                ],
                pager: $('#pager'),
                rowNum: 100,
                viewrecords: true,
                multiselect: true,
                //rowList : [100, 200, 500],
                rownumbers: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 150,
                jsonReader: {
                    root: "clientSearchList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                },
                loadComplete: function () {
                    if (done == true)
                        getCategoryCounts();
                },
                onSelectRow: function (id) {
                    var data = $(this).getRowData(id);
                    if (selectedValues.includes(id)) {
                        var i = selectedValues.indexOf(id);
                        if (i !== -1) selectedValues.splice(i, 1);
                    } else {
                        selectedValues.push(id);
                    }
                }
            });

            fetch('/api/case-manager/init-client-search', {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function (res) {
                loadClients()
            });

            $("#dialog").dialog({
                title: "Client to Case Manager Assignment",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Yes", click: completeCaseAssignment},
                    {
                        text: "No", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            $("#dialog_deassign").dialog({
                title: "Client to Case Manager De-Assignment",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Yes", click: deAssignClients},
                    {
                        text: "No", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            $.ajax({
                url: "/api/state",
                dataType: "json",
                success: function (stateMap) {
                    var options = "<option value = '" + '' + "'>" + '' + "</option>";
                    $.each(stateMap, function (index, value) {
                        options += "<option value = '" + value.id + "'>" + value.name + "</option>";
                    }); //end each
                    $("#state").html(options);
                }
            }); //end of ajax call

            $("#state").change(function (event) {
                $.ajax({
                    url: "/api/state/lga/" + $('#state').val(),
                    dataType: "json",
                    success: function (lgaMap) {
                        var options = "<option value = '" + '' + "'>" + '' + "</option>";
                        $.each(lgaMap, function (key, value) {
                            options += "<option value = '" + value.id + "'>" + value.name + "</option>";
                        }); //end each
                        $("#lga").html(options);
                    }
                }); //end of ajax call
            });

            $.ajax({
                url: "/api/case-manager/retrieve",
                dataType: "json",
                success: function (caseManagerMap) {
                    //console.log(caseManagerMap);
                    var options = "<option value = '" + '0' + "'>" + '' + "</option>";
                    $.each(caseManagerMap, function (key, value) {
                        options += "<option value = '" + value.casemanagerId + "'>" + value.fullName + "</option>";
                    });//end each
                    $("#casemanagerId").html(options);
                }
            }); //end of ajax call

            $("#categoryId").bind("change", function (event) {
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                var lga = $("#lga").val();
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();
                var showAssigned = $("#showAssigned").prop("checked");
                var name = ($("#name").val() || '').toUpperCase();
                //console.log("Show Assigned is: "+ showAssigned);

                loadClients();
            });

            $("#state").bind("change", function (event) {
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                var lga = '';
                if (state == "") {
                    $("#lga").empty();
                    lga = '';
                } else {
                    lga = $("#lga").val();
                }
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();
                var showAssigned = $("#showAssigned").prop("checked");
                var name = ($("#name").val() || '').toUpperCase();
                //console.log("Show Assigned is: "+ showAssigned);
                loadClients();
            });

            $("#lga").bind("change", function (event) {
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                var lga = $("#lga").val();
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();
                var showAssigned = $("#showAssigned").prop("checked");
                var name = ($("#name").val() || '').toUpperCase();
                //console.log("Show Assigned is: "+ showAssigned);
                loadClients();
            });

            $("#gender").bind("change", function (event) {
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                var lga = $("#lga").val();
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();
                var showAssigned = $("#showAssigned").prop("checked");
                if (gender == "Female") {
                    $("#pregnancyStatus").attr('disabled', false);
                } else {
                    $("#pregnancyStatus").attr("disabled", true);
                    $("#pregnancyStatus").val("--All--");
                }
                //console.log("Gender: "+ gender);
                loadClients();
            });

            $("#ageGroup").bind("change", function (event) {
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                var lga = $("#lga").val();
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();
                var name = ($("#name").val() || '').toUpperCase();
                loadClients();
            });

            $("#pregnancyStatus").bind("change", function (event) {
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                var lga = $("#lga").val();
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();
                loadClients();
            });

            $("#showAssigned").bind("click", function (event) {
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                var lga = $("#lga").val();
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();
                var showAssigned = $("#showAssigned").prop("checked");
                var name = ($("#name").val() || '').toUpperCase();
                if (showAssigned == true) {
                    $("#assign_button").prop("disabled", false);
                    $("#assign_button").html('De-Assign')
                } else {
                    var casemanagerId = $("#casemanagerId").val();
                    if (casemanagerId != "0") $("#assign_button").prop("disabled", false);
                    else if (casemanagerId == "0") $("#assign_button").prop("disabled", true);
                    $("#assign_button").html('Assign')
                }
                loadClients();
            });

            $("#casemanagerId").bind("change", function (event) {
                var casemanagerId = $("#casemanagerId").val();
                $("#stable").html('0');
                $("#unstable_less").html('0');
                $("#unstable_more").html('0');
                $("#preart").html('0');
                $("#detailstd").hide();
                $("#detailstd1").hide();
                if (casemanagerId != "0") {
                    $("#assign_button").prop("disabled", false);
                    getCaseManagerDetails(casemanagerId);
                } else if (casemanagerId == "0") {
                    $("#assign_button").prop("disabled", true);
                    $("#detailstd").hide();
                    $("#detailstd1").hide();
                }
            });

            $("#hospitalNum").bind("keyup", function (event) {
                //TODO: Reset selections to empty
                var categoryId = $("#categoryId").val("--All--");
                var state = $("#state").val("--All--");
                $("#lga").empty();
                var lga = ''
                var gender = $("#gender").val("--All--");
                var ageGroup = $("#ageGroup").val("--All--");
                $("#countstd").hide();
            });

            $("#name").bind("keypress", function (event) {
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                $("#lga").empty();
                var lga = ''
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();
                $("#countstd").hide();
            });

            $("#assign_button").bind("click", function (event) {
                var buttonText = $("#assign_button").text();
                if (buttonText == 'Assign') {
                    selectedIds = jQuery("#grid").jqGrid('getGridParam', 'selarrrow');
                    console.log(selectedIds);
                    var casemanagerId = $("#casemanagerId").val();
                    if (selectedValues == "") {
                        if (selectedIds == "") {
                            alert("Please mark clients to assign");
                        } else {
                            assign = true;
                            patientIds = selectedIds;
                            caseManagerId = casemanagerId;
                            //console.log(selectedIds);
                            $("#dialog").dialog("open");
                        }
                    } else {
                        assign = true;
                        patientIds = selectedValues;
                        caseManagerId = casemanagerId;
                        //console.log(selectedValues);
                        $("#dialog").dialog("open");
                    }
                } else if (buttonText == 'De-Assign') {
                    selectedIds = jQuery("#grid").jqGrid('getGridParam', 'selarrrow');
                    if (selectedIds == "") {
                        alert("Please mark clients to de-assign");
                    } else {
                        assign = false;
                        patientIds = selectedIds;
                        $("#dialog_deassign").dialog("open");
                    }
                }
                event.preventDefault();
                return false;
            });

            $("#close_button").bind("click", function (event) {
                window.location= '/case-manager/assign-client';
                return true;
            });
        });

        function initClients() {
            $.ajax({
                url: "/api/case-manager/init-client-search",
                dataType: "json",
                success: function (clientsMap) {

                }
            });
        }

        function checkOnlyAssigned() {
            if (showAssigned == true) {
                $("#assign_button").prop("disabled", false);
                $("#assign_button").html('De-Assign')
            } else {
                var casemanagerId = $("#casemanagerId").val();
                if (casemanagerId != "0") $("#assign_button").prop("disabled", false);
                else if (casemanagerId == "0") $("#assign_button").prop("disabled", true);
                $("#assign_button").html('Assign')
            }
        }

        function getCaseManagerDetails(casemanagerId) {
            $.ajax({
                url: "/api/case-manager/" + casemanagerId,
                dataType: "json",
                success: function (caseManagerDetailsMap) {
                    //$("#caseManagerReligion").html(caseManagerDetailsMap.religion);
                    $("#caseManagerGender").html(caseManagerDetailsMap.sex.toUpperCase());
                    $("#caseManagerAssignments").html(caseManagerDetailsMap.clientCount);
                    getAssignedClientsTreatmentStatus(casemanagerId);
                }
            }); //end of ajax call
        }

        function getAssignedClientsTreatmentStatus(casemanagerId) {
            $.ajax({
                url: "/api/case-manager/client-status/" + casemanagerId,
                dataType: "json",
                success: function (clientsStatusCountMap) {
                    $("#stable").html(clientsStatusCountMap.stable);
                    $("#unstable_less").html(clientsStatusCountMap.unstable_less);
                    $("#unstable_more").html(clientsStatusCountMap.unstable_more);
                    $("#preart").html(clientsStatusCountMap.preart);
                    $("#detailstd").show();
                    $("#detailstd1").show();
                }
            }); //end of ajax call
        }

        function deAssignClients() {
            fetch('/api/case-manager/assignment?casemanagerId=' + $('#casemanagerId').val() + '&patientIds=' +
                selectedIds.toString() + '&type=deassign', {
                headers: {
                    'ContentType': 'application/json'
                }
            }).then(function (e) {
                $("#dialog_deassign").dialog("close");
                window.location= '/case-manager/assign-client';
            });
        }

        function completeCaseAssignment() {
            fetch('/api/case-manager/assignment?casemanagerId=' + $('#casemanagerId').val() + '&patientIds=' +
                selectedIds.toString() + '&type=assign', {
                headers: {
                    'ContentType': 'application/json'
                }
            }).then(function (e) {
                $("#dialog").dialog("close");
                window.location = '/case-manager/assign-client';
            });
        }


    </script>
</head>

<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_casemanagement.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="Casemanagement_page">Case Management</a></li>
        <li class="breadcrumb-item active" aria-current="page">Assign Clients To Case Managers</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <input name="patientIds" type="hidden" id="patientIds"/>
                <div id="loader_client"></div>
                <div id="messageBar"></div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Facility Case Manager:</label>
                                <select name="casemanagerId" style="width: 100%;" class="form-control select2"
                                        id="casemanagerId">
                                    <option value='0'></option>
                                </select><span id="caseManagerHelp" class="errorspan"></span>
                            </div>
                            <div id="detailstd1" style="display: none">
                                <label><strong id="caseManagerAssignments"></strong> Clients(s) Assigned : </label>
                                <label><strong id="stable">0</strong> Stable 1 Year,</label>
                                <label><strong id="unstable_less">0</strong> Unstable less than 1 Year,</label>
                                <label><strong id="unstable_more">0</strong> Unstable 1 Year or more,</label>
                                <label><strong id="preart">0</strong> Awaiting ART</label>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <div id="detailstd" style="display: hidden">
                                        <label>Case Manager Gender: <strong id="caseManagerGender"></strong></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <h6> Demographic/Clinic filters</h6>
                    <p></p>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Client Categorization:</label>
                                <select name="categoryId" style="width: 100%;" class="form-control select2"
                                        id="categoryId">
                                    <option value='0'>--All--</option>
                                    <option value='1'>Stable One Year</option>
                                    <option value='2'>Unstable less than One Year</option>
                                    <option value='3'>Unstable One Year or more</option>
                                    <option value='4'>Awaiting ART</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Age Group:</label>
                                <select name="ageGroup" style="width: 100%;" class="form-control select2" id="ageGroup">
                                    <option value='0'>--All--</option>
                                    <option value='0-9'>Children (0-9 Years)</option>
                                    <option value='10-14'>Younger Adolescent (10 to 14 Years)</option>
                                    <option value='15-19'>Older Adolescent (15 to 19 Years)</option>
                                    <option value='20-24'>Young People (20 to 24 Years)</option>
                                    <option value='25-1000'>Adults (25 and Above)</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Gender:</label>
                                <select name="gender" style="width: 100%;" class="form-control select2" id="gender">
                                    <option>--All--</option>
                                    <option>Male</option>
                                    <option>Female</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">Pregnancy Status</label>
                                <select name="pregnancyStatus" style="width: 100%;" class="form-control select2"
                                        id="pregnancyStatus" disabled>
                                    <option>--All--</option>
                                    <option value='1'>Pregnant</option>
                                    <option value='2'>Breastfeeding</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">State of residence:</label>
                                <select name="state" style="width: 100%;" class="form-control select2" id="state">
                                    <option></option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">LGA of residence:</label>
                                <select name="lga" style="width: 100%;" class="form-control select2" id="lga">
                                    <option></option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <table width="99%" border="0" class="space" cellpadding="3">
                        <tr>
                            <td id="countstd" style="display: none">
                                <label><strong id="count_stable">0</strong> Stable Clients (one year or more) : </label>
                                <label><strong id="count_unstable_less">0</strong> Unstable Clients (less than one year)
                                    : </label>
                                <label><strong id="count_unstable_more">0</strong> Unstable Clients (one year or more) :
                                </label>
                                <label><strong id="count_pre_art">0</strong> Awaiting ART </label>
                            </td>
                        </tr>
                    </table>
                    <div>
                        <div class="input-group no-border col-md-3 pull-right">
                            <input type="text" class="form-control search" placeholder="search...">
                            <div class="input-group-append">
                                <div class="input-group-text">
                                    <i class="now-ui-icons ui-1_zoom-bold"></i>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-1 col-sm-offset-11">
                                <button type='button' id='assign_button' class='btn btn-sm btn-info'>Assign</button>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table id="grid" class="table table-striped table-bordered center"></table>
                            <div id="pager" style="text-align:center;"></div>
                        </div>
                        <p></p>
                        <div id="dialog">
                            <table width="99%" border="0" class="space" cellpadding="3">
                                <tr>
                                    <td><label>Do you want to continue with case manager assignment for selected
                                        clients?</label></td>
                                </tr>
                                <tr>
                                    <td width="20%"><label>Click Yes to continue or No to cancel:</label></td>
                                </tr>
                            </table>
                        </div>
                        <div id="dialog_deassign">
                            <table width="99%" border="0" class="space" cellpadding="3">
                                <tr>
                                    <td><label>Do you want to continue with case manager de-assignment for selected
                                        clients?</label></td>
                                </tr>
                                <tr>
                                    <td width="20%"><label>Click Yes to continue or No to cancel:</label></td>
                                </tr>
                            </table>
                        </div>

                        <!--                                <div id="buttons" style="width: 200px">
                                                        <button id="assign_button" disabled>Assign</button> &nbsp;<button id="close_button">Close</button>-->
                    </div>
                    <div style="align: left">
                        <input name="showAssigned" type="checkbox" id="showAssigned"/>&nbsp;<label>Show only assigned
                        clients?</label>
                    </div>
                </div>
        </div>
        </form>
    </div>
</div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>