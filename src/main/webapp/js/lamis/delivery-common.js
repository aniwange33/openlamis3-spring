/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function initialize() {
    addDatepicker("#dateBirth");
    addDatepicker("#date1");
    addDatepicker("#date2");
    addDatepicker("#date3");

//    $("#timeHivDiagnosis").attr("disabled", true);
    $("#arvRegimenPast").attr("disabled", true);
    $("#arvRegimenCurrent").attr("disabled", true);
    $("#date2").attr("disabled", true);
    $("#date3").attr("disabled", true);
    $("#clinicStage").attr("disabled", true);
    $("#cd4Ordered").attr("disabled", true);
    $("#cd4").attr("disabled", true);

    $("#bookingStatus").change(function (event) {
        if ($("#bookingStatus").val() === "1") { //Booked
//            $("#timeHivDiagnosis").val(""); 
//            $("#timeHivDiagnosis").attr("disabled", true);
            $("#arvRegimenCurrent").val("");
            $("#arvRegimenCurrent").attr("disabled", true);
            $("#arvRegimenPast").val("");
            $("#arvRegimenPast").attr("disabled", true);
            //$("#date2").val("");
            $("#date2").attr("disabled", true);
            //$("#date3").val("");
            $("#date3").attr("disabled", true);
            $("#clinicStage").val("");
            $("#clinicStage").attr("disabled", true);
            $("#cd4Ordered").val("");
            $("#cd4Ordered").attr("disabled", true);
            $("#cd4").val("");
            $("#cd4").attr("disabled", true);
//            $("#partnerNotification").attr("disabled", true);
//            $("#partnerHivStatus").attr("disabled", true);
//            $("#fp").attr("disabled", true);
//            $("#art").attr("disabled", true);
//            $("#others").attr("disabled", true);
        } else {
            //$("#date2").val("");
            $("#date2").attr("disabled", false);
            //$("#timeHivDiagnosis").val(""); 
//            $("#timeHivDiagnosis").attr("disabled", true);
            $("#screenPostPartum").attr("checked", false);
            $("#arvRegimenCurrent").val("");
            $("#arvRegimenCurrent").attr("disabled", true);
            $("#arvRegimenPast").val("");
            $("#arvRegimenPast").attr("disabled", true);
            //$("#date3").val("");
            $("#date3").attr("disabled", true);
            $("#clinicStage").val("");
            $("#clinicStage").attr("disabled", true);
            $("#cd4Ordered").val("");
            $("#cd4Ordered").attr("disabled", true);
            $("#cd4").val("");
            $("#cd4").attr("disabled", true);
//            $("#partnerNotification").attr("disabled", false);
//            $("#partnerHivStatus").attr("disabled", false);
//            $("#fp").attr("disabled", false);
//            $("#art").attr("disabled", false);
//            $("#others").attr("disabled", false);
        }
    });

    $("#date2").change(function (event) {
        if ($("#date2").val().length != 0) {
            //$("#timeHivDiagnosis").attr("disabled", false);
            if ($("#bookingStatus").val() == "0") { //Unbooked
                $("#arvRegimenCurrent").attr("disabled", false);
                $("#arvRegimenPast").attr("disabled", false);
                $("#date3").attr("disabled", false);
                $("#clinicStage").attr("disabled", false);
                $("#cd4Ordered").attr("disabled", false);
                $("#cd4").attr("disabled", false);
            }
        } else {
//            $("#timeHivDiagnosis").val(""); 
//            $("#timeHivDiagnosis").attr("disabled", true);
            $("#screenPostPartum").attr("checked", false);
            $("#arvRegimenCurrent").val("");
            $("#arvRegimenCurrent").attr("disabled", true);
            $("#arvRegimenPast").val("");
            $("#arvRegimenPast").attr("disabled", true);
            $("#date3").val("");
            $("#date3").attr("disabled", true);
            $("#clinicStage").val("");
            $("#clinicStage").attr("disabled", true);
            $("#cd4Ordered").val("");
            $("#cd4Ordered").attr("disabled", true);
            $("#cd4").val("");
            $("#cd4").attr("disabled", true);
        }
    });

    $("#deliveryId").attr("data-row-id", "0");

    $("#save_button").bind("click", function (event) {
        event.preventDefault()
        if (validateForm()) {
            postData();
        }
    });

    $("#delete_button").bind("click", function (event) {
        fetch('/api/delivery/' + $('#deliveryId').val(), {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function () {
            window.history.back();
        });
        return true;
    });

    $("#close_button").bind("click", function (event) {
        window.history.back();
        return true;
    });

    $("#timeHivDiagnosis").bind("change", function (event) {
        //TODO: Check
        var dated = $("#date3").val() || '';
        if (dated.length !== 0) {
            var dateVisit = $("#date1").val();
            if ($("#timeHivDiagnosis").val().includes("Newly")) {
                if (dateVisit.length !== 0) {
                    if (parseInt(compare(dateVisit, dated)) === -1) {
                        var message = "ART commencement date " + dated + " cannot be before date newly tested positive which is " + dateVisit;
                        $("#timeHivDiagnosis").val("");
                        alert(message);
                    }
                }
            }
        }
    });

    $("#date1").bind("change", function (event) {
        //TODO: Check
        var dated = $("#date3").val() || "";
        if (dated.length !== 0) {
            var dateVisit = $("#date1").val();
            if ($("#timeHivDiagnosis").val().includes("Newly")) {
                if (parseInt(compare(dateVisit, dated)) === -1) {
                    var message = "ART commencement date " + dated + " cannot be before date newly tested positive";
                    $("#timeHivDiagnosis").val("");
                    alert(message);
                }
            }
        }
    });
}

function addDatepicker(id) {
    $(id).mask("99/99/9999");
    $(id).datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
}

var newDelivery = true;

function populateForm(deliveryList) {
    if ($.isEmptyObject(deliveryList)) {
        updateRecord = false;
        resetButtons();

        $("#deliveryId").val("");
        $("#bookingStatus").val("");
        $("#date1").val("");
        $("#romDeliveryInterval").val("");
        $("#modeDelivery").val("");
        $("#episiotomy").val("");
        $("#vaginalTear").val("");
        $("#maternalOutcome").val("");
//        $("#timeHivDiagnosis").val("");
        $("#screenPostPartum").attr("checked", false);
        $("#sourceReferral").val("");
        $("#gestationalAge").val("");
        $("#hepatitisBStatus").val("");
        $("#hepatitisCStatus").val("");
        $("#cd4").val("");
        $("#partnerHivStatus").val("");
        $("#partnerNotification").val("");
        $("#fp").attr("checked", false);
        $("#art").attr("checked", false);
        $("#others").attr("checked", false);
    } else {
        updateRecord = true;
        initButtonsForModify();
        newDelivery = false;

        $("#id").val(deliveryList[0].patientId);
        $("#deliveryId").val(deliveryList[0].deliveryId);
        $("#ancId").val(deliveryList[0].ancId);
        $("#bookingStatus").val(deliveryList[0].bookingStatus);
        date = deliveryList[0].dateDelivery;
        $("#date1").val(dateSlice(date));
        $("#romDeliveryInterval").val(deliveryList[0].romDeliveryInterval);
        $("#modeDelivery").val(deliveryList[0].modeDelivery);
        $("#episiotomy").val(deliveryList[0].episiotomy);
        $("#vaginalTear").val(deliveryList[0].vaginalTear);
        $("#maternalOutcome").val(deliveryList[0].maternalOutcome);
//        date = deliveryList[0].dateConfirmedHiv;
//        $("#date2").val(dateSlice(date));
        $("#timeHivDiagnosis").val(deliveryList[0].timeHivDiagnosis);
        if (deliveryList[0].screenPostPartum === "1") {
            $("#screenPostPartum").attr("checked", true);
        }
        $("#arvRegimenCurrent").val(deliveryList[0].arvRegimenCurrent);
        $("#sourceReferral").val(deliveryList[0].sourceReferral);
        $("#gestationalAge").val(deliveryList[0].gestationalAge);
        $("#hepatitisBStatus").val(deliveryList[0].hepatitisBStatus);
        $("#hepatitisCStatus").val(deliveryList[0].hepatitisCStatus);
        $("#arvRegimenPast").val(deliveryList[0].arvRegimenPast);
        date = deliveryList[0].dateArvRegimenCurrent;
        $("#date3").val(dateSlice(date));
        $("#clinicStage").val(deliveryList[0].clinicStage);
        $("#cd4Ordered").val(deliveryList[0].cd4Ordered);
        $("#cd4").val(deliveryList[0].cd4);

//        if ($("#bookingStatus").val() === "0") {
//            $("#arvRegimenCurrent").attr("disabled", false);
//            $("#arvRegimenPast").attr("disabled", false);
//            $("#date3").attr("disabled", false);
//            $("#clinicStage").attr("disabled", false);
//            $("#cd4Ordered").attr("disabled", false);
//            $("#cd4").attr("disabled", false);
//        }else if ($("#bookingStatus").val() === "1"){
//            $("#partnerNotification").attr("disabled", true);
//            $("#partnerHivStatus").attr("disabled", true);
//            $("#fp").attr("disabled", true);
//            $("#art").attr("disabled", true);
//            $("#others").attr("disabled", true);
        //}
        console.log("PATIENT ID ", $('#patientId').val());
    }

    $.ajax({
        url: "/api/patient/" + $('#patientId').val(),
        dataType: "json",
        success: function (res) {
            console.log("RES ", res);
            var patientList = res.patientList;
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#patientId").val(patientList[0].patientId);
            $("#facilityId").val(patientList[0].facilityId);
            $("#hospitalNumMother").val(patientList[0].hospitalNum);
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            if (!updateRecord) getAncLastVisit();
            date = patientList[0].dateConfirmedHiv;
            if (date !== "") {
                $("#date2").val(dateSlice(date));
                $("#date2").attr("disabled", true);
            } else {
                $("#date2").attr("disabled", false);
            }
            date = patientList[0].dateStarted;
            if (date !== "") {
                $("#date3").val(dateSlice(date));
                $("#date3").attr("disabled", true);
            } else {
                $("#date3").attr("disabled", false);
            }
            console.log("Got Here one!");
            dueViralLoad(patientList[0].dueViralLoad, patientList[0].viralLoadType);
            console.log("Got Here two!");
            if (newDelivery) retrieveCurrentRegimen(patientList[0].patientId);
            console.log("Got Here three!");
        }
    }); //end of ajax call 

    //create the child grid
    createChildGrid();

    $.ajax({
        url: "/api/partner-information",
        dataType: "json",
        success: function (res) {
            var partnerinformationList = res.partnerinformationList;
            if (!$.isEmptyObject(partnerinformationList)) {
                $("#partnerinformationId").val(partnerinformationList[0].partnerinformationId);
                $("#partnerNotification").val(partnerinformationList[0].partnerNotification);
                $("#partnerHivStatus").val(partnerinformationList[0].partnerHivStatus);
                var partnerReferred = partnerinformationList[0].partnerReferred;
                if (partnerReferred.indexOf("FP") != -1) $("#fp").attr("checked", true);
                if (partnerReferred.indexOf("ART") != -1) $("#art").attr("checked", true);
                if (partnerReferred.indexOf("OTHERS") != -1) $("#others").attr("checked", true);
            }
        }
    }); //end of ajax call 
}

function retrieveCurrentRegimen(patientId) {
    $.ajax({
        url: "/api/regimen/last-regimen/patient/" + patientId,
        dataType: "json",
        success: function (res) {
            console.log("lastRegimenList ", res);
            var lastRegimenList = res.regimenList;
            // set the ANC static values...
            if (typeof lastRegimenList !== "undefined") {
                if (lastRegimenList.length > 0) {
                    date = lastRegimenList[0].dateVisit;
//                        if(date !== ""){
//                            $("#date6").val(dateSlice(date));
//                            $("#date6").attr("disabled", true);
//                        }else{
//                            $("#date6").attr("disabled", false);
//                        }
                }
            }
        }
    });
}

function getAncLastVisit() {
    //Retrieve the last ANC visit ID for this client
    $.ajax({
        url: "/api/anc/last-visit/patient/" + $('#patientId').val(),
        dataType: "json",
        success: function (res) {
            var ancLast = res.ancList;
            if (!$.isEmptyObject(ancLast)) {
                $("#ancId").val(ancLast[0] ? ancLast[0].ancId : '');
            }
        }
    });
}

function getMonthDisplayName(month) {
    switch (month) {
        case "01" :
            return "Jan";
        case "02" :
            return "Feb";
        case "03" :
            return "Mar";
        case "04" :
            return "Apr";
        case "05" :
            return "May";
        case "06" :
            return "Jun";
        case "07" :
            return "Jul";
        case "08" :
            return "Aug";
        case "09" :
            return "Sep";
        case "10" :
            return "Oct";
        case "11" :
            return "Nov";
        case "12" :
            return "Dec";
        default :
            return undefined;
    }
}

function createChildGrid() {
    var date = $("#date1").val();
    date = date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6);
    var postData = updateRecord ? {deliveryId: $("#deliveryId").val()} : {deliveryId: 0};
    $("#grid").jqGrid({
        url: "/api/child/grid",
        datatype: "json",
        mtype: "GET",
        colNames: ["Hospital No", "Surname", "Other Names", "Gender", "Weight (kg)", "Apgar Score", "Child's Status", "Date of Birth", "ARV 72hrs", "HepB Ig 24hrs", "Non HBV - HBV vaccine 24hrs", "", "", ""],
        colModel: [
            {name: "hospitalNumber", index: "hospitalNumber", width: "110", editable: true},
            {name: "surname", index: "surname", width: "100", editable: true},
            {name: "otherNames", index: "otherNames", width: "100", editable: true},
            {
                name: "gender",
                index: "gender",
                width: "110",
                editable: true,
                edittype: 'select',
                editoptions: {value: "'':-- select --;Male:Male;Female:Female"}
            },
            {name: "bodyWeight", index: "bodyWeight", width: "110", editable: true},
            {
                name: "apgarScore",
                index: "apgarScore",
                width: "110",
                editable: true,
                edittype: 'select',
                editoptions: {value: "'':-- select --;0:0;1:1;2:2;3:3;4:4;5:5;6:6;7:7;8:8;9:9;10:10"}
            },
            {
                name: "status",
                index: "status",
                width: "170",
                editable: true,
                edittype: 'select',
                editoptions: {value: "'':-- select --;SB - Stillbirth:SB - Stillbirth;NND - Neonatal death:NND - Neonatal death;A - Alive:A - Alive"}
            },
            {
                name: "dateBirth",
                index: "dateBirth",
                width: "120",
                editable: true,
                formatter: "date",
                formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
            },
            {
                name: "arv",
                index: "arv",
                width: "120",
                editable: true,
                edittype: 'select',
                editoptions: {value: "'':-- select --;Yes:Yes;No:No"}
            },
            {
                name: "hepb",
                index: "hepb",
                width: "120",
                editable: true,
                edittype: 'select',
                editoptions: {value: "'':-- select --;Yes:Yes;No:No"}
            },
            {
                name: "hbv",
                index: "hbv",
                width: "210",
                editable: true,
                edittype: 'select',
                editoptions: {value: "'':-- select --;Yes:Yes;No:No"}
            },
            {name: "patientId", index: "patientId", width: "0", sortable: false, hidden: true},
            {name: "deliveryId", index: "deliveryId", width: "0", sortable: false, hidden: true},
            {name: "childId", index: "childId", width: "0", sortable: false, hidden: true}
        ],
        pager: $("#pager"),
        rowNum: 10,
        rowList: [10, 20],
        sortname: "childId",
        sortorder: "desc",
        viewrecords: true,
//        autowidth:true, 
//        shrinkToFit:false,
//        forceFit:true,
        imgpath: "themes/basic/images",
        resizable: false,
        height: 80,
        postData: postData,
        jsonReader: {
            root: "childList",
            page: "currpage",
            total: "totalpages",
            records: "totalrecords",
            repeatitems: false,
            id: "childId"
        },
        gridComplete: function () {
            $('#grid').jqGrid('setGridWidth', '1000'); // max width for grid
        },
        onSelectRow: function (id) {
            if (id && id !== lastSelected) {
                $("#grid").saveRow(lastSelected, false, 'clientArray');
                $("#grid").editRow(id, true);
                lastSelected = id;
                console.log("ID: ", id);

                // add a datepicker to the textfields on the cells
                addDatepicker("#" + id + "_dateBirth");
            }
        } //end of onSelectRow 
    }); //end of jqGrid

    // construct the navigation          
    $("#grid").navGrid('#pager', {
        edit: false, add: false, del: false, search: false

    }).navButtonAdd('#pager', {
        caption: "Add Child", buttonimg: "", title: "Add New Child", onClickButton: function () {
            // retrieve last selected child's id
            var selid = $("#grid").getGridParam("selrow");
            $("#grid").saveRow(selid, false, 'clientArray');

            var datarow = {
                hospitalNum: "",
                surname: "",
                otherNames: "",
                gender: "",
                bodyWeight: "",
                apgarScore: "",
                status: "",
                dateBirth: "",
                arv: "",
                hepb: "",
                hbv: "",
                patientId: $("#id").val(),
                deliveryId: "",
                childId: ""
            };

            var row_id = parseInt($("#deliveryId").attr("data-row-id")) - 1;
            $("#deliveryId").attr("data-row-id", row_id);

            var su = $("#grid").addRowData("" + row_id + "", datarow, "first");
            if (su) {
                $("#grid").editRow("" + row_id + "", true);
                // add a datepicker to the textfields on the cells
                addDatepicker("#" + row_id + "_dateBirth");
            }
            ;
            $("#grid").setGridParam({selrow: "" + row_id + ""});

        }, position: "last"

    }).navButtonAdd('#pager', {
        caption: "Remove Child", buttonimg: "", title: "Delete Child", onClickButton: function (id) {
            // retrieve selected child's id
            var selid = $("#grid").getGridParam("selrow");

            // a new entry
            if (parseInt(selid) < 0) {
                $("#grid").delRowData(selid);

                // an existing child
            } else {
                $("#grid").restoreRow(selid); // undo changes
                var child = $("#grid").getRowData(selid);
                if (child != null) {
                    fetch('/api/child/' + child.childId, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(function () {
                        $("#grid").delRowData(selid);
                    });
                }
            }

        }, position: "last"
    });
}

//function createMother(){
//    if($("#userGroup").html() === "Data Analyst") {
//        $("#lamisform").attr("action", "Error_message");
//        return true;                        
//    }
//    else {
//        if(validateChildForm()) {
//            $("#dialog").dialog("close");
//            
//            var motherRecord = {motherDTO : {}};
//
//            //Build the data...
//            motherRecord.motherDTO.hospitalNumber = $("#hospitalNumber").val();
//            motherRecord.motherDTO.uniqueId = $("#uniqueId").val();
//            motherRecord.motherDTO.dateEnrolledPmtct = $("#dateEnrolledPmtct").val();
//            motherRecord.motherDTO.surname = $("#surname").val();
//            motherRecord.motherDTO.otherNames = $("#otherNames").val();
//            motherRecord.motherDTO.dateStarted = $("#dateStarted").val();
//
//            //Save the data...
//            $.postJSON("Child_save.action", motherRecord, function(status) { 
//                window.location.href = "Delivery_search.action";
//            }); 
//        }
//    }    
//}

function postData() {
    var delivery = {deliveryDto: {}, childs: [], partnerInfoDto: {}};

    // fill the delivery object
    delivery.deliveryDto.deliveryId = $("#deliveryId").val();
    delivery.deliveryDto.patientId = $("#id").val();
    delivery.deliveryDto.ancId = $("#ancId").val();
    delivery.deliveryDto.bookingStatus = $("#bookingStatus").val();
    delivery.deliveryDto.dateDelivery = $("#dateDelivery").val();
    delivery.deliveryDto.romDeliveryInterval = $("#romDeliveryInterval").val();
    delivery.deliveryDto.modeDelivery = $("#modeDelivery").val();
    delivery.deliveryDto.hospitalNumMother = $("hospitalNumMother").val();
    delivery.deliveryDto.episiotomy = $("#episiotomy").val();
    delivery.deliveryDto.vaginalTear = $("#vaginalTear").val();
    delivery.deliveryDto.maternalOutcome = $("#maternalOutcome").val();
    delivery.deliveryDto.timeHivDiagnosis = $("#timeHivDiagnosis").val();
    delivery.deliveryDto.sourceReferral = $("#sourceReferral").val();
    delivery.deliveryDto.gestationalAge = $("#gestationalAge").val();
    delivery.deliveryDto.hepatitisBStatus = $("#hepatitisBStatus").val();
    delivery.deliveryDto.hepatitisCStatus = $("#hepatitisCStatus").val();

    // fill the partner information object
    delivery.partnerInfoDto.partnerinformationId = $("#partnerinformationId").val();
    delivery.partnerInfoDto.partnerNotification = $("#partnerNotification").val();
    delivery.partnerInfoDto.partnerHivStatus = $("#partnerHivStatus").val();
    delivery.partnerInfoDto.facilityId = $("#facilityId").val();
    delivery.partnerInfoDto.dateVisit = $("#dateDelivery").val();
    delivery.partnerInfoDto.patientId = $("#patientId").val();

    var partnerReferred = "";
    if ($("#fp").attr("checked")) partnerReferred += "FP,";
    if ($("#art").attr("checked")) partnerReferred += "ART,";
    if ($("#others").attr("checked")) partnerReferred += "OTHERS,";
    if (partnerReferred != "") partnerReferred = partnerReferred.substring(0, partnerReferred.lastIndexOf(","));

    delivery.partnerInfoDto.partnerReferred = partnerReferred;

    // retrieve last selected child's id
    var selid = $("#grid").getGridParam("selrow");
    $("#grid").saveRow(selid, false, 'clientArray');

    var ids = $("#grid").getDataIDs();

    // extract data from rows
    for (var i = 0; i < ids.length; i++) {
        var child = $("#grid").getRowData(ids[i]);
        child.facilityId = $("#facilityId").val();
        child.patientId = $("#patientId").val();
        var date = child.dateBirth;
        child.dateBirth = date.slice(3, 5) + "/" + date.slice(0, 2) + "/" + date.slice(6);
        if (child.dateBirth != '/ /')
            delivery.childs[i] = child;
    }
    var data = formMap();
    data.children = delivery.childs;
    data.partnerInformation = delivery.partnerInfoDto;
    fetch('/api/delivery', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(function () {
        window.history.back();
    });
}

function validateForm() {
    var validate = true;
    $("#date1").datepicker("option", "altField", "#dateDelivery");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#dateConfirmedHiv");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date3").datepicker("option", "altField", "#dateArvRegimenCurrent");
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
    return validate;
}
