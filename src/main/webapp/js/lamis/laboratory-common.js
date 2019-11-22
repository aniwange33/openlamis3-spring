/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function initialize() {
    $("#date1").mask("99/99/9999");
    $("#date1").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date2").mask("99/99/9999");
    $("#date2").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date2").change(function () {
        if ($(this).val() != lastSelectDate) {
            findLaboratoryByDate();
        }
        lastSelectDate = $(this).val();
    });

    $("#save_button").click(function (event) {
        console.log("saved successfully.");
        if (emptyValues()) {
            $("#messageBar").html("One or more lab test has no result entered").slideDown('slow');
            return false;
        } else {
            $("#messageBar").slideUp('slow');
            if ($("#userGroup").html() == "Data Analyst") {
                $("#lamisform").attr("action", "Error_message");
                return true;
            } else {
                if (validateForm()) {
                    if (updateRecord) {
                        if ($("#fromLabTest").html() == "true") {
                            $("#lamisform").attr("action", "Laboratory_prescription_update");
                        } else {
                            $("#lamisform").attr("action", "Laboratory_update");
                        }
                    } else {
                        if ($("#fromLabTest").html() == "true") {
                            $("#lamisform").attr("action", "Laboratory_prescription_save");
                        } else {
                            $("#lamisform").attr("action", "Laboratory_save");
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
    });

//    $("#delete_button").click( function(event){
//        if($("#userGroup").html() == "Data Analyst") {
//            $("#lamisform").attr("action", "Error_message");
//        }
//        else {
//            $("#lamisform").attr("action", "Laboratory_delete");
//        }
//        return true;
//    });
}

function getLabTestComposition(labtestId) {
    $("#grid").setGridParam({url: "Labresult_grid.action?q=1&labtestId=" + labtestId, page: 1}).trigger("reloadGrid");
}

function getLabtest() {
    $("#labtestgrid").setGridParam({url: "Labtest_retrieve.action?q=1", page: 1}).trigger("reloadGrid");
}

function retrieveLabtest(labtestId) {
    $("#labtestId").val(labtestId);
    $("#labtestgrid").setGridParam({
        url: "Labtest_retrieve.action?q=1&selectedLabtest=" + $("#labtestId").val(),
        page: 1
    }).trigger("reloadGrid");
    //getLabTestComposition(labtestId);		
}

function findLaboratoryByDate() {
    $("#date2").datepicker("option", "altField", "#dateReported");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $.ajax({
        url: encodeURI('/api/laboratory/find?patientId=' + $('#patientId').val() + '&dateReported=' + $('#dateReported').val()),
        dataType: "json",
        success: function (res) {
            var laboratoryList = res.laboratoryList;
            populateForm(laboratoryList);
        }
    });
}

function retrievePrescriptions(patientId) {
    $.ajax({
        url: "Prescription_lab_retrieve_id.action",
        dataType: "json",
        data: {patientId: patientId},
        success: function (labtestPrescriptionList) {
            labtestId = labtestPrescriptionList[0];
            $("#grid").setGridParam({url: "Labresult_grid_retrieve.action?q=1", page: 1}).trigger("reloadGrid");
        },
        complete: function () {
            retrieveLabtest(labtestId);
        }
    }); //end of ajax call
}

var patientId = null;

function populateForm(laboratoryList) {

    $.ajax({
        url: "/api/patient/" + $('#patientId').val(),
        dataType: "json",
        success: function (res) {
            var patientList = res.patientList;
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#facilityId").val(patientList[0].facilityId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#dateBirth").val(patientList[0].dateBirth);
            $("#gender").val(patientList[0].gender);
            $("#currentStatus").val(patientList[0].currentStatus);
            $("#dateCurrentStatus").val(patientList[0].dateCurrentStatus);
            $("#dateLastCd4").val(patientList[0].dateLastCd4);
            $("#dateLastViralLoad").val(patientList[0].dateLastViralLoad);
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            $("#name").val(patientList[0].surname + " " + patientList[0].otherNames);

            patientId = patientList[0].patientId;
        },
        complete: function () {
            if ($.isEmptyObject(laboratoryList)) {
                updateRecord = false;
                resetButtons();

                $("#laboratoryId").val("");
                $("#labno").val("");

                if (patientId !== null) {
                    retrievePrescriptions(patientId);
                }
            } else {
                updateRecord = true;
                initButtonsForModify();

                $("#id").val(laboratoryList[0].patientId);
                $("#hospitalNum").val(laboratoryList[0].hospitalNum);
                $("#dateLastCd4").val(laboratoryList[0].dateLastCd4);
                $("#laboratoryId").val(laboratoryList[0].laboratoryId);
                $("#dateCollected").val(laboratoryList[0].dateCollected);
                date = laboratoryList[0].dateCollected;
                $("#date1").val(dateSlice(date));
                $("#dateReported").val(laboratoryList[0].dateReported);
                date = laboratoryList[0].dateReported;
                $("#date2").val(dateSlice(date));
                $("#labno").val(laboratoryList[0].labno);
                for (i = 0; i < laboratoryList.length; i++) {
                    var id = parseInt(laboratoryList[i].labtestId);
                    var idx = labtestIds.indexOf(id); //Find the index
                    if (idx == -1) {
                        labtestIds.push(id);
                    }
                }
                $("#labtestId").val(laboratoryList[0].labtestId);
                var labtestId = laboratoryList[0].labtestId;
                retrieveLabtest(labtestId);
                //$("#grid").setGridParam({url: "Labresult_grid_retrieve.action?q=1", page:1}).trigger("reloadGrid");                                    
            }
        }
    }); //end of ajax call

}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateCollected");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#dateReported");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");

    // check if date of result is entered
    if ($("#dateReported").val().length == 0 || !regex.test($("#dateReported").val())) {
        $("#dateHelp").html(" *");
        validate = false;
    } else {
        $("#dateHelp").html("");
    }

//    if($("#labtestId").val().length == 0){
//        $("#testHelp").html(" *");
//        validate = false;
//    }
//    else {
//        $("#testHelp").html("");
//    }

    return validate;
}

function emptyValues() {
    var empty = false;
    var gridIds = $("#grid").getDataIDs();
    for (i = 0; i < gridIds.length; i++) {
        var data = $("#grid").getRowData(gridIds[i]);
        var resultab = data.resultab;
        var resultpc = data.resultpc;
        if ((resultab == "" || resultab == null) && (resultpc == "" || resultpc == null)) {
            empty = true;
        }
    }
    return empty;
}

//$("#labtestId").dblclick(function(event){
//var id = parseInt($("#labtestId").val());
//var idx = labtestIds.indexOf(id); //Find the index
//if(idx != -1) {
//labtestIds.splice(idx, 1);  // Remove it if found
//}
//else {
//labtestIds.push(id);
//}
//$("#grid").setGridParam({url: "Labresult_grid.action?q=1&labtestIds="+labtestIds, page:1}).trigger("reloadGrid");
//});

