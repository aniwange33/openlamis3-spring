/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function initialize() {
    addDatepicker("#date1");
    addDatepicker("#date2");
    addDatepicker("#date3");
    addDatepicker("#date4");

    $("#fundalHeight").mask("9?9", {placeholder: " "});
    $("#bodyWeight").mask("99?9", {placeholder: " "});
    $("#bp1").mask("9?99", {placeholder: " "});
    $("#bp2").mask("9?99", {placeholder: " "});

    $("#bp1").blur(function (event) {
        $("#bp").val($("#bp1").val() + "/" + $("#bp2").val());
    })
    $("#bp2").blur(function (event) {
        $("#bp").val($("#bp1").val() + "/" + $("#bp2").val());
    })

    $("#cd4").attr("disabled", true);
    $("#syphilisTestResult").attr("disabled", true);

    $("#cd4Ordered").change(function (event) {
        if ($("#cd4Ordered").val() == "Yes") {
            $("#cd4").attr("disabled", false);
        } else {
            $("#cd4").attr("disabled", true);
        }
    });

    $("#syphilisTested").change(function (event) {
        if ($("#syphilisTested").val() == "Yes") {
            $("#syphilisTestResult").attr("disabled", false);
        } else {
            $("#syphilisTestResult").val("");
            $("#syphilisTestResult").attr("disabled", true);
        }
    });

    $("#save_button").bind("click", function (event) {
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;
        } else {
            if (validateForm()) {
                var method = 'POST';
                if (updateRecord()) {
                    method = 'PUT'
                }
                fetch('/api/maternal-followup', {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    }), method: method, body: JSON.stringify(data)
                }).then(function (response) {
                    window.history.back();
                }).catch(function (error) {
                    console.log('Error', error)
                });
                return true;
            } else {
                return false;
            }
        }
    });

    $("#delete_button").bind("click", function (event) {
        fetch('/api/maternal-followup/' + $('#maternalfollowupId').val(), {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function () {
            window.location.reload()
        });
        return true;
    });

    $("#close_button").bind("click", function (event) {
        window.history.back();
        return true;
    });
}

function addDatepicker(id) {
    $(id).mask("99/99/9999");
    $(id).datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+2",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
}

function populateForm(maternalfollowupList) {
    if ($.isEmptyObject(maternalfollowupList)) {
        updateRecord = false;
        resetButtons();

        $("#maternalfollowupId").val("");
        $("#date1").val("");
        $("#bodyWeight").val("");
        $("#fetalPresentation").val("");
        $("#fundalHeight").val("");
        $("#bp").val("");
        $("#bp1").val("");
        $("#bp2").val("");
        $("#date2").val("");
        $("#date3").val("");
        $("#timeHivDiagnosis").val("");
        $("#arvRegimenCurrent").val("");
        $("#arvRegimenPast").val("");
        $("#screenPostPartum").attr("checked", false);
        $("#syphilisScreened").val("");
        $("#syphilisTested").val("");
        $("#syphilisTestResult").val("");
        $("#syphilisTreated").val("");
        $("#cd4Ordered").val("");
        $("#cd4").val("");
        $("#counselNutrition").attr("checked", false);
        $("#counselFeeding").attr("checked", false);
        $("#counselFamilyPlanning").attr("checked", false);
        $("#familyPlanningMethod").val("");
        $("#referred").val("");
        $("#date4").val("");
        $("#partnerHivStatus").val("");
        $("#partnerNotification").val("");
        $("#fp").attr("checked", false);
        $("#art").attr("checked", false);
        $("#others").attr("checked", false);
    } else {
        updateRecord = true;
        initButtonsForModify();

        $("#id").val(maternalfollowupList[0].patientId);
        $("#maternalfollowupId").val(maternalfollowupList[0].maternalfollowupId);
        $("#ancId").val(maternalfollowupList[0].ancId);
        date = maternalfollowupList[0].dateVisit;
        $("#date1").val(dateSlice(date));
        $("#bodyWeight").val(maternalfollowupList[0].bodyWeight);
        $("#bp").val(maternalfollowupList[0].bp);
        var bp = maternalfollowupList[0].bp;
        var bps = new Array();
        bps = bp.split("/");
        $("#bp1").val(bps[0]);
        $("#bp2").val(bps[1]);
        $("#fetalPresentation").val(maternalfollowupList[0].fetalPresentation);
        $("#fundalHeight").val(maternalfollowupList[0].fundalHeight);
        date = maternalfollowupList[0].dateConfirmedHiv;
        $("#date2").val(dateSlice(date));
        $("#timeHivDiagnosis").val(maternalfollowupList[0].timeHivDiagnosis);
        $("#arvRegimenPast").val(maternalfollowupList[0].arvRegimenPast);
        $("#arvRegimenCurrent").val(maternalfollowupList[0].arvRegimenCurrent);
        date = maternalfollowupList[0].dateArvRegimenCurrent;
        $("#date3").val(dateSlice(date));
        if (maternalfollowupList[0].screenPostPartum == "1") {
            $("#screenPostPartum").attr("checked", true);
        }
        $("#syphilisTested").val(maternalfollowupList[0].syphilisTested);
        $("#syphilisTestResult").val(maternalfollowupList[0].syphilisTestResult);
        if ($("#syphilisTestResult").val().length != 0) {
            $("#syphilisTestResult").attr("disabled", false);
        }
        $("#syphilisTreated").val(maternalfollowupList[0].syphilisTreated);
        $("#cd4Ordered").val(maternalfollowupList[0].cd4Ordered);
        $("#cd4").val(maternalfollowupList[0].cd4);
        if ($("#cd4").val().length != 0) {
            $("#cd4").attr("disabled", false);
        }
        if (maternalfollowupList[0].counselNutrition == "1") {
            $("#counselNutrition").attr("checked", true);
        }
        if (maternalfollowupList[0].counselFeeding == "1") {
            $("#counselFeeding").attr("checked", true);
        }
        if (maternalfollowupList[0].counselFamilyPlanning == "1") {
            $("#counselFamilyPlanning").attr("checked", true);
        }

        $("#familyPlanningMethod").val(maternalfollowupList[0].familyPlanningMethod);
        $("#referred").val(maternalfollowupList[0].referred);
        date = maternalfollowupList[0].dateNextVisit;
        $("#date4").val(dateSlice(date));
    }

    $.ajax({
        url: "/api/patient/" + $('#patientId').val(),
        dataType: "json",
        success: function (res) {
            var patientList = res.patientList;
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            if (!updateRecord) getAncLastVisit();
        }
    }); //end of ajax call 

    $.ajax({
        url: "/api/partner-information/grid",
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

function getAncLastVisit() {
    //Retrieve the last ANC visit ID for this client
    $.ajax({
        url: "/api/anc/last-visit/patient/" + $('#patientId').val(),
        dataType: "json",
        data: {patientId: $("#id").val(), last: "last"},
        success: function (ancList) {
            if (!$.isEmptyObject(ancList)) {
                $("#ancId").val(ancList[0].ancId);
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

function validateForm() {
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#dateConfirmedHiv");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date3").datepicker("option", "altField", "#dateArvRegimenCurrent");
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date4").datepicker("option", "altField", "#dateNextVisit");
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy");

    return validate;
}	