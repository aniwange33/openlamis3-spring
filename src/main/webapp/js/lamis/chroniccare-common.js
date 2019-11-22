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
    $("#date1").change(function () {
        if ($(this).val() != lastSelectDate) {
            findChroniccareByDate();
        }
        lastSelectDate = $(this).val();
    });

    $("#date2").mask("99/99/9999");
    $("#date2").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+20",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date3").mask("99/99/9999");
    $("#date3").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+5",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date4").mask("99/99/9999");
    $("#date4").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#bodyWeight").keypress(function (key) {
        if ((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
            return false;
        }
        return true;
    })
    $("#height").keypress(function (key) {
        if ((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
            return false;
        }
        return true;
    })
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
                fetch('/api/chronic-care', {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    }), method: method, body: JSON.stringify(data)
                }).then(function (response) {
                    window.location = '/event-page/patient/' + $('#patientId').val();
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
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
        } else {
            fetch('/api/chronic-care/' + $('#chroniccareId').val(), {
                method: 'DELETE',
                headers: new Headers({
                    'Content-Type': 'application/json'
                })
            }).then(function (re) {
                window.location.href = "/event-page/patient/" + $('#patientId').val();
            })
        }
        return true;
    });
}

function findChroniccareByDate() {
    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $.ajax({
        url: "Chroniccare_find_date.action",
        dataType: "json",
        data: {patientId: $("#id").val(), hospitalNum: $("#hospitalNum").val(), dateVisit: $("#dateVisit").val()},
        success: function (chroniccareList) {
            populateForm(chroniccareList);
        }
    });
}

function populateForm(chroniccareList) {
    if ($.isEmptyObject(chroniccareList)) {
        updateRecord = false;
        resetButtons();

        $("#chroniccareId").val("");
        $("#dateVisit").val("");
        $("#clientType").val("");
        $("#currentStatus").val("");
        $("#pregnancyStatus").val("");
        $("#clinicStage").val("");
        $("#lastCd4").val("");
        $("#dateLastCd4").val("");
        $("#date2").val("");
        $("#lastViralLoad").val("");
        $("#dateLastViralLoad").val("");
        $("#date3").val("");
        $("#eligibleCd4").val("");
        $("#eligibleViralLoad").val("");
        $("#cotrimEligibility1").attr("checked", false);
        $("#cotrimEligibility2").attr("checked", false);
        $("#cotrimEligibility3").attr("checked", false);
        $("#cotrimEligibility4").attr("checked", false);
        $("#cotrimEligibility5").attr("checked", false);
        $("#ipt").val("");
        $("#inh").val("");
        $("#tbTreatment").val("");
        $("#dateStartedTbTreatment").val("");
        $("#date4").val("");
        $("#tbReferred").val("");
        $("#eligibleIpt").val("");
        $("#bodyWeight").val("");
        $("#height").val("");
        $("#bmi").val("");
        $("#muac").val("");
        $("#muacPediatrics").val("");
        $("#muacPregnant").val("");
        $("#supplementaryFood").val("");
        $("#nutritionalStatusReferred").val("");
        $("#gbv1").val("");
        $("#gbv1Referred").val("");
        $("#gbv2").val("");
        $("#gbv2Referred").val("");
        $("#hypertensive").val("");
        $("#firstHypertensive").val("");
        $("#bpAbove").val("");
        $("#bpReferred").val("");
        $("#diabetic").val("");
        $("#firstDiabetic").val("");
        $("#phdp1").val("");
        $("#phdp1ServicesProvided").val("");
        $("#phdp2").val("");
        //$("#phdp2ServicesProvided").val("");
        $("#phdp3").val("");
        //$("#phdp3ServicesProvided").val("");
        $("#phdp4").val("");
        $("#phdp4ServicesProvided").val("");
        $("#phdp5").val("");
        //$("#phdp5ServicesProvided").val("");
        $("#phdp6").val("");
        //$("#phdp6ServicesProvided").val("");
        $("#phdp7").val("");
        $("#phdp7ServicesProvided").val("");
        //$("#phdp8").val("");
        $("#phdp8ServicesProvided").val("");
        $("#reproductiveIntentions1").val("");
        $("#reproductiveIntentions1Referred").val("");
        $("#reproductiveIntentions2").val("");
        $("#reproductiveIntentions2Referred").val("");
        $("#reproductiveIntentions3").val("");
        $("#reproductiveIntentions3Referred").val("");
        $("#malariaPrevention1").val("");
        $("#malariaPrevention1Referred").val("");
        $("#malariaPrevention2").val("");
        $("#malariaPrevention2Referred").val("");
        $("#dmReferred").val("");
        $("#tbValues").val("");
        $("#dmValues").val("");
    } else {
        updateRecord = true;
        initButtonsForModify();

        $("#chroniccareId").val(chroniccareList[0].chroniccareId);
        $("#dateVisit").val(chroniccareList[0].dateVisit);
        date = chroniccareList[0].dateVisit;
        $("#date1").val(dateSlice(date));
        $("#clientType").val(chroniccareList[0].clientType);
        $("#currentStatus").val(chroniccareList[0].currentStatus);
        $("#pregnancyStatus").val(chroniccareList[0].pregnancyStatus);
        $("#clinicStage").val(chroniccareList[0].clinicStage);
        $("#lastCd4").val(chroniccareList[0].lastCd4);
        date = chroniccareList[0].dateLastCd4;
        $("#date2").val(dateSlice(date));
        $("#lastViralLoad").val(chroniccareList[0].lastViralLoad);
        date = chroniccareList[0].dateLastViralLoad;
        $("#date3").val(dateSlice(date));
        $("#eligibleCd4").val(chroniccareList[0].eligibleCd4);
        $("#eligibleViralLoad").val(chroniccareList[0].eligibleViralLoad);
        if (chroniccareList[0].cotrimEligibility1 == "1") {
            $("#cotrimEligibility1").attr("checked", true);
        }
        if (chroniccareList[0].cotrimEligibility2 == "1") {
            $("#cotrimEligibility2").attr("checked", true);
        }
        if (chroniccareList[0].cotrimEligibility3 == "1") {
            $("#cotrimEligibility3").attr("checked", true);
        }
        if (chroniccareList[0].cotrimEligibility4 == "1") {
            $("#cotrimEligibility4").attr("checked", true);
        }
        if (chroniccareList[0].cotrimEligibility5 == "1") {
            $("#cotrimEligibility5").attr("checked", true);
        }
        $("#ipt").val(chroniccareList[0].ipt);
        $("#inh").val(chroniccareList[0].inh);
        date = chroniccareList[0].dateStartedTbTreatment;
        $("#date4").val(dateSlice(date));
        $("#tbReferred").val(chroniccareList[0].tbReferred);
        $("#eligibleIpt").val(chroniccareList[0].eligibleIpt);
        $("#tbTreatment").val(chroniccareList[0].tbTreatment);
        if ($("#tbTreatment").val() == "No") {
            $("#tbscreen_button").attr("disabled", false);
            $("#date4").attr("disabled", "disabled");
            $("#tbReferred").attr("disabled", false);
            $("#ipt").attr("disabled", false);
            $("#inh").attr("disabled", false);
            $("#eligibleIpt").attr("disabled", false);
        } else if ($("#tbTreatment").val() == "Yes") {
            $("#tbscreen_button").attr("disabled", "disabled");
            $("#date4").attr("disabled", false);
            $("#tbReferred").attr("disabled", true);
            $("#ipt").attr("disabled", true);
            $("#inh").attr("disabled", true);
            $("#eligibleIpt").attr("disabled", true);
        } else {
            $("#tbscreen_button").attr("disabled", true);
            $("#date4").attr("disabled", true);
            $("#tbReferred").attr("disabled", true);
            $("#ipt").attr("disabled", true);
            $("#inh").attr("disabled", true);
            $("#eligibleIpt").attr("disabled", true);
        }
        $("#bodyWeight").val(chroniccareList[0].bodyWeight);
        $("#height").val(chroniccareList[0].height);
        if ($("#bodyWeight").val().length != 0 && $("#height").val().length != 0) {
            var w = $("#bodyWeight").val();
            var h = $("#height").val();
            var bmi = w / (h * h);
            bmi = Math.round(bmi * 100) / 100;
            $("#bmiValue").html(bmi);
        }
        $("#bmi").val(chroniccareList[0].bmi);
        $("#muac").val(chroniccareList[0].muac);
        $("#muacPediatrics").val(chroniccareList[0].muacPediatrics);
        $("#muacPregnant").val(chroniccareList[0].muacPregnant);
        $("#supplementaryFood").val(chroniccareList[0].supplementaryFood);
        $("#nutritionalStatusReferred").val(chroniccareList[0].nutritionalStatusReferred);
        $("#gbv1").val(chroniccareList[0].gbv1);
        $("#gbv1Referred").val(chroniccareList[0].gbv1Referred);
        $("#gbv2").val(chroniccareList[0].gbv2);
        $("#gbv2Referred").val(chroniccareList[0].gbv2Referred);
        $("#hypertensive").val(chroniccareList[0].hypertensive);
        if ($("#hypertensive").val() == "No") {
            $("#firstHypertensive").val("");
            $("#firstHypertensive").attr("disabled", true);
        } else if ($("#hypertensive").val() == "Yes") {
            $("#firstHypertensive").attr("disabled", false);
        } else {
            $("#firstHypertensive").val("");
            $("#firstHypertensive").attr("disabled", true);
        }
        $("#firstHypertensive").val(chroniccareList[0].firstHypertensive);
        $("#bpAbove").val(chroniccareList[0].bpAbove);
        $("#bpReferred").val(chroniccareList[0].bpReferred);
        $("#diabetic").val(chroniccareList[0].diabetic);
        if ($("#diabetic").val() == "No") {
            $("#dmscreen_button").attr("disabled", false);
            $("#firstDiabetic").val("");
            $("#firstDiabetic").attr("disabled", true);
        } else if ($("#diabetic").val() == "Yes") {
            $("#firstDiabetic").attr("disabled", false);
        } else {
            $("#dmscreen_button").attr("disabled", "disabled");
            $("#firstDiabetic").val("");
            $("#firstDiabetic").attr("disabled", true);
        }
        $("#firstDiabetic").val(chroniccareList[0].firstDiabetic);
        $("#phdp1").val(chroniccareList[0].phdp1);
        $("#phdp1ServicesProvided").val(chroniccareList[0].phdp1ServicesProvided);
        $("#phdp2").val(chroniccareList[0].phdp2);
        //$("#phdp2ServicesProvided").val(chroniccareList[0].phdp2ServicesProvided);
        $("#phdp3").val(chroniccareList[0].phdp3);
        //$("#phdp3ServicesProvided").val(chroniccareList[0].phdp3ServicesProvided);
        $("#phdp4").val(chroniccareList[0].phdp4);
        $("#phdp4ServicesProvided").val(chroniccareList[0].phdp4ServicesProvided);
        $("#phdp5").val(chroniccareList[0].phdp5);
        //$("#phdp5ServicesProvided").val(chroniccareList[0].phdp5ServicesProvided);
        $("#phdp6").val(chroniccareList[0].phdp6);
        //$("#phdp6ServicesProvided").val(chroniccareList[0].phdp6ServicesProvided);
        $("#phdp7").val(chroniccareList[0].phdp7);
        $("#phdp7ServicesProvided").val(chroniccareList[0].phdp7ServicesProvided);
        //$("#phdp8").val(chroniccareList[0].phdp8);
        $("#phdp8ServicesProvided").val(chroniccareList[0].phdp8ServicesProvided);

        var additionalPhdpServices = chroniccareList[0].phdp9ServicesProvided;
        if (additionalPhdpServices.indexOf("Insecticide treated nets") != -1) $("#phdp91").attr("checked", true);
        if (additionalPhdpServices.indexOf("Intermittent prophylactic treatment") != -1) $("#phdp92").attr("checked", true);
        if (additionalPhdpServices.indexOf("Cervical Cancer Screening") != -1) $("#phdp93").attr("checked", true);
        if (additionalPhdpServices.indexOf("Active member of SG") != -1) $("#phdp94").attr("checked", true);

        if (additionalPhdpServices.indexOf("Family Planning") != -1) $("#phdp95").attr("checked", true);
        if (additionalPhdpServices.indexOf("Basic care kits") != -1) $("#phdp96").attr("checked", true);
        if (additionalPhdpServices.indexOf("Disclosure counseling") != -1) $("#phdp97").attr("checked", true);
        if (additionalPhdpServices.indexOf("Social Services") != -1) $("#phdp98").attr("checked", true);

        if (additionalPhdpServices.indexOf("Linkage to IGAs") != -1) $("#phdp99").attr("checked", true);
        if (additionalPhdpServices.indexOf("Legal Services") != -1) $("#phdp910").attr("checked", true);
        if (additionalPhdpServices.indexOf("Others") != -1) $("#phdp911").attr("checked", true);

        $("#reproductiveIntentions1").val(chroniccareList[0].reproductiveIntentions1);
        $("#reproductiveIntentions1Referred").val(chroniccareList[0].reproductiveIntentions1Referred);
        $("#reproductiveIntentions2").val(chroniccareList[0].reproductiveIntentions2);
        $("#reproductiveIntentions2Referred").val(chroniccareList[0].reproductiveIntentions2Referred);
        $("#reproductiveIntentions3").val(chroniccareList[0].reproductiveIntentions3);
        $("#reproductiveIntentions3Referred").val(chroniccareList[0].reproductiveIntentions3Referred);
        $("#malariaPrevention1").val(chroniccareList[0].malariaPrevention1);
        $("#malariaPrevention1Referred").val(chroniccareList[0].malariaPrevention1Referred);
        $("#malariaPrevention2").val(chroniccareList[0].malariaPrevention2);
        $("#malariaPrevention2Referred").val(chroniccareList[0].malariaPrevention2Referred);
        $("#dmReferred").val(chroniccareList[0].dmReferred);
        $("#tbValues").val(chroniccareList[0].tbValues);
        $("#dmValues").val(chroniccareList[0].dmValues);

        var values = chroniccareList[0].tbValues;
        tbscreenIds = values.split("#");
        values = chroniccareList[0].dmValues;
        dmscreenIds = values.split("#");
    }

    $.ajax({
        url: "Patient_retrieve.action",
        dataType: "json",
        success: function (patientList) {
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#dateBirth").val(patientList[0].dateBirth);
            $("#gender").val(patientList[0].gender);
            //$("#currentStatus").val(patientList[0].currentStatus);                        
            //$("#dateCurrentStatus").val(patientList[0].dateCurrentStatus);                        
            //$("#dateLastClinic").val(patientList[0].dateLastClinic); 
            //$("#dateNextClinic").val(patientList[0].dateNextClinic); 
            //$("#dateLastCd4").val(patientList[0].dateLastCd4);
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            if (patientList[0].gender == "Female") {
                $("#pregnancyStatus").removeAttr("disabled");
                $("#reproductiveIntentions1").removeAttr("disabled");
                $("#reproductiveIntentions1Referred").removeAttr("disabled");
                $("#reproductiveIntentions2").removeAttr("disabled");
                $("#reproductiveIntentions2Referred").removeAttr("disabled");
                $("#malariaPrevention2").removeAttr("disabled");
                $("#malariaPrevention2Referred").removeAttr("disabled");
            } else {
                $("#pregnancyStatus").attr("disabled", "disabled");
                $("#reproductiveIntentions1").attr("disabled", "disabled");
                $("#reproductiveIntentions1Referred").attr("disabled", "disabled");
                $("#reproductiveIntentions2").attr("disabled", "disabled");
                $("#reproductiveIntentions2Referred").attr("disabled", "disabled");
                $("#reproductiveIntentions3").attr("disabled", "disabled");
                $("#reproductiveIntentions3Referred").attr("disabled", "disabled");
                $("#malariaPrevention2").attr("disabled", "disabled");
                $("#malariaPrevention2Referred").attr("disabled", "disabled");
            }
        }
    }); //end of ajax call
}

function checkDate(date1, date2, message) {
    if (parseInt(compare(date1, date2)) == 1) {
        $("#messageBar").html(message).slideDown('slow');
    } else {
        $("#messageBar").slideUp('slow');
    }
}

function iptEligible() {
    if ($("#ipt").val() == "No" && $("#inh").val() == "No") {
        $("#eligibleIpt").val("Yes");
    } else {
        $("#eligibleIpt").val("");
    }
}

function calBmi() {
    if ($("#bodyWeight").val().length != 0 && $("#height").val().length != 0) {
        var w = $("#bodyWeight").val();
        var h = $("#height").val();
        var bmi = w / (h * h);
        bmi = Math.round(bmi * 100) / 100;          //bmi = Math.ceil(bmi * 10) / 10;
        $("#bmiValue").html(bmi);
        if (bmi < 18.5) $("#bmi").val("<18.5 (Underweight)");
        if (bmi >= 18.5 && bmi <= 24.9) $("#bmi").val("18.5-24.9 (Healthy)");
        if (bmi >= 25.0 && bmi <= 29.9) $("#bmi").val("25.0-29.9 (Overweight)");
        if (bmi >= 30.0 && bmi <= 39.9) $("#bmi").val(">30 (Obesity)");
        if (bmi >= 40.0) $("#bmi").val(">40 (Morbid Obesity)");
    }
}

function calMuac() {
    if ($("#muac").val().length != 0) {
        var muac = $("#muac").val();
        if (muac < 11.5) $("#muacPediatrics").val("<11.5cm (Severe Acute Malnutrition)");
        if (muac >= 11.5 && muac <= 12.5) $("#muacPediatrics").val("11.5-12.5cm (Moderate Acute Malnutrition)");
        if (muac > 12.5) $("#muacPediatrics").val(">12.5cm (Well nourished)");
    }
}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#dateLastCd4");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date3").datepicker("option", "altField", "#dateLastViralLoad");
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date4").datepicker("option", "altField", "#dateStartedTbTreatment");
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy");

    // check if date of visit is entered
    if ($("#dateVisit").val().length == 0 || !regex.test($("#dateVisit").val())) {
        $("#dateHelp").html(" *");
        validate = false;
    } else {
        $("#dateHelp").html("");
    }
    return validate;
}

//to get the ids of selected rows in multiselect grid
// var selectedrows = $.extend([], $("#grid").jqGrid('getGridParam', 'selarrrow'));

//to select it again

// for(var i = 0; i < selectedrows.length; i++) {
//    $("#grid").jqGrid('setSelection', selectedrows[i]);
// }

//to get the selected value of a combobox in jqgrid
//varselectedValue = jQuery("#adrgrid").jqGrid('getCell', gridIds[i], 'severity');   