function initialize() {
    $("#date1").mask("99/99/9999");
    $("#date1").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+20",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });


//    $("#date1").change(function(){
//        if($(this).val() != lastSelectDate) {
//            findClinicByDate();
//            //findDrugsByVisitDate();
//            //findPrescriptionsByDate();
//        }
//        lastSelectDate = $(this).val();
//    });

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

//    $("#date4").mask("99/99/9999");
//    $("#date4").datepicker({
//        dateFormat: "dd/mm/yy",
//        changeMonth: true,
//        changeYear: true,
//        constrainInput: true,
//        buttonImageOnly: true,
//        buttonImage: "/images/calendar.gif"
//    });
//	
    $("#bodyWeight").keypress(function (key) {
        return !((key.charCode < 48 || key.charCode > 57) && (key.charCode !== 0) && (key.charCode !== 8) && (key.charCode !== 9) && (key.charCode !== 46));

    })
    $("#height").keypress(function (key) {
        return !((key.charCode < 48 || key.charCode > 57) && (key.charCode !== 0) && (key.charCode !== 8) && (key.charCode !== 9) && (key.charCode !== 46));

    })
    $("#bp1").mask("999", {placeholder: " "});
    $("#bp2").mask("999", {placeholder: " "});

    $("#bp1").blur(function (event) {
        $("#bp").val($("#bp1").val() + "/" + $("#bp2").val());
    })
    $("#bp2").blur(function (event) {
        $("#bp").val($("#bp1").val() + "/" + $("#bp2").val());
    })

    $("#delete_button").bind("click", function (event) {
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
        } else {
            fetch('/api/clinic/' + $("#clinicId").val(), {method: 'DELETE'});
        }
        return true;
    });
}

function findClinicByDate() {
    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $.ajax({
        url: "/api/clinic/" + $("#id").val(),
        dataType: "json",
        success: function (clinicList) {
            populateForm(clinicList);
        }
    });
}

function findDrugsByVisitDate() {
    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $.ajax({
        url: "/api/prescription/drugs-by-date/" + $("#id").val() + '?dateVisit=' + $("#dateVisit").val(),
        dataType: "json",
        success: function (response) {
            //console.log(response.drugList);
            var data = response.drugList;
            regimenMap = new Map();
            old_regimen_ids = new Map();
            for (var i = 0; i < data.length; i += 4) {
                var selectionMap = new Map();
                var regimenId = data[i + 1];
                var duration = data[i + 2];
                old_regimen_ids.set(data[i + 1], data[i + 3]);
                selectionMap.set(regimenId, duration);

                if (data[i] === "1" || data[i] === "2" || data[i] === "3" || data[i] === "4")
                    selectedType = data[i];
                if (data[i] != "" && selectionMap.size > 0)
                    regimenMap.set(data[i], selectionMap);

            }
            oldRecord = true;
        },
        complete: function () {

        }
    }); //end of ajax call
}

function populatePrescriptions(prescriptionMap) {

}

function populateForm(clinicList) {
    if ($.isEmptyObject(clinicList)) {
        updateRecord = false;
        resetButtons();
        $("#clinicId").val("");
        $("#dateVisit").val("");
        $("#date1").val("");
        $("#clinicStage").val("");
        $("#funcStatus").val("");
        $("#tbStatus").val("");
        $("#bodyWeight").val("");
        $("#height").val("");
        $("#bp").val("");
        $("#bp1").val("");
        $("#bp2").val("");
        $("#pregnant").val("");
        $("#breastfeeding").val("");
        $("#lmp").val("");
        $("#date2").val("");
        $("#oiScreened").val("");
        $("#adrScreened").val("");
        $("#adherenceLevel").val("");
        $("#nextAppointment").val("");
        $("#date3").val("");
        $("#notes").val("");
        $("#commence").val("");
        $("#pregnant").removeAttr("checked");
        $("#breastfeeding").removeAttr("checked");
        $("#commence").val("0");
    } else {
        updateRecord = true;
        initButtonsForModify();
        $("#clinicId").val(clinicList[0].clinicId);
        $("#id").val(clinicList[0].facilityId);
        $("#id").val(clinicList[0].patientId);
        $("#dateVisit").val(clinicList[0].dateVisit);
        date = clinicList[0].dateVisit;
        $("#date1").val(dateSlice(date));
        $("#clinicStage").val(clinicList[0].clinicStage);
        $("#funcStatus").val(clinicList[0].funcStatus);
        $("#tbStatus").val(clinicList[0].tbStatus);
        $("#bodyWeight").val(clinicList[0].bodyWeight);
        $("#height").val(clinicList[0].height);
        $("#bp").val(clinicList[0].bp);
        var bp = clinicList[0].bp || '';
        var bps = new Array();
        bps = bp.split("/");
        $("#bp1").val(bps[0]);
        $("#bp2").val(bps[1]);
        $("#pregnant").val(clinicList[0].pregnant);
        $("#breastfeeding").val(clinicList[0].breastfeeding);
        $("#lmp").val(clinicList[0].lmp);
        date = clinicList[0].lmp;
        $("#date2").val(dateSlice(date));
        $("#oiScreened").val(clinicList[0].oiScreened);
        $("#adrScreened").val(clinicList[0].adrScreened);
        $("#adherenceLevel").val(clinicList[0].adherenceLevel);
        $("#nextAppointment").val(clinicList[0].nextAppointment);
        date = clinicList[0].nextAppointment;
        $("#date3").val(dateSlice(date));
        if (clinicList[0].pregnant == "1") {
            $("#pregnant").attr("checked", "checked");
        }
        if (clinicList[0].breastfeeding == "1") {
            $("#breastfeeding").attr("checked", "checked");
        }
        $("#notes").val(clinicList[0].notes);
        $("#commence").val(clinicList[0].commence);

        var ids = clinicList[0].oiIds || '';
        oiIds = ids.split(",");
        ids = clinicList[0].adrIds || '';
        adrIds = ids.split("#");
        ids = clinicList[0].adhereIds || '';
        adhereIds = ids.split(",");

        if ($("#oiScreened").val() === "Yes") {
            $("#oi_button").removeAttr("disabled");
        } else {
            $("#oi_button").attr("disabled", "disabled");
        }

        if ($("#adrScreened").val() === "Yes") {
            $("#adr_button").removeAttr("disabled");
        } else {
            $("#adr_button").attr("disabled", "disabled");
        }

        if ($("#adherenceLevel").val() === "Fair" || $("#adherenceLevel").val() === "Poor") {
            $("#adhere_button").removeAttr("disabled");
        } else {
            $("#adhere_button").attr("disabled", "disabled");
        }
    }

    $.ajax({
        url: "/api/patient/grid",
        dataType: "json",
        success: function (response) {
            var patientList = response.patientList;
            // set patient id and number for which infor is to be entered
            //$("#id").val(patientList[0].id);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#dateBirth").val(patientList[0].dateBirth);
            $("#gender").val(patientList[0].gender);
            $("#currentStatus").val(patientList[0].currentStatus);
            if (!patientList[0].currentStatus.includes("Known Death") && !patientList[0].currentStatus.includes("ART Transfer Out")) {
                $("#prescriptions").removeAttr("hidden");
            }
            $("#dateCurrentStatus").val(patientList[0].dateCurrentStatus);
            $("#dateLastClinic").val(patientList[0].dateLastClinic);
            $("#dateNextClinic").val(patientList[0].dateNextClinic);
            $("#dateLastCd4").val(patientList[0].dateLastCd4);
            if (patientList[0].gender === "Female") {
                $("#pregnant").removeAttr("disabled");
                $("#breastfeeding").removeAttr("disabled");
                $("#date2").removeAttr("disabled");
            } else {
                $("#pregnant").attr("disabled", "disabled");
                $("#breastfeeding").attr("disabled", "disabled");
                $("#date2").attr("disabled", "disabled");
            }
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            $("#name").val(patientList[0].surname + " " + patientList[0].otherNames);
            dueViralLoad(patientList[0].dueViralLoad);
            //if((patientList[0].dateLastCd4).length != 0) {
            // checkCd4(patientList[0].dateLastCd4);
            //}
        }
    }); //end of ajax call
}


function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#lmp");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date3").datepicker("option", "altField", "#nextAppointment");
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date4").datepicker("option", "altField", "#dateLastCd4");
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy");

    // retrieve the selected oi and adhere ids from grids 
    $("#oiIds").val($("#oigrid").jqGrid('getGridParam', 'selarrrow'));
    $("#adhereIds").val($("#adheregrid").jqGrid('getGridParam', 'selarrrow'));

    // check if date of visit is entered
    if ($("#dateVisit").val().length == 0 || !regex.test($("#dateVisit").val())) {
        $("#dateHelp").html(" *");
        validate = false;
    } else {
        $("#dateHelp").html("");
    }

    if ($("#nextAppointment").val().length == 0 || !regex.test($("#nextAppointment").val())) {
        $("#nextHelp").html(" *");
        validate = false;
    } else {
        $("#nextHelp").html("");
    }
    return validate;

    $("#close_button").bind("click", function (event) {
        $("#lamisform").attr("action", "Event_page?hno=");
        return true;
    });


}

//to get the ids of selected rows in multiselect grid
// var selectedrows = $.extend([], $("#grid").jqGrid('getGridParam', 'selarrrow'));

//to select it again

// for(var i = 0; i < selectedrows.length; i++) {
//    $("#grid").jqGrid('setSelection', selectedrows[i]);
// }

//to get the selected value of a combobox in jqgrid
//varselectedValue = jQuery("#adrgrid").jqGrid('getCell', gridIds[i], 'severity');   