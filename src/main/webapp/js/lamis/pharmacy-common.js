 /* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var regimen_durations = [];

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
    $("#date1").change(function(){
        if($(this).val() != lastSelectDate) {
            findPharmacyByDate();
        }
        lastSelectDate = $(this).val();
    });

    $("#date2").mask("99/99/9999");
    $("#date2").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+5",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
    
    $("#refill").prop('minLength',1);
    $("#refill").prop('maxLength',3);

	$.ajax({
        url: "RegimenType_retrieve_id.action",
        dataType: "json",

        success: function(regimenTypeMap) {
            
            var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
            $.each(regimenTypeMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) //end each
            $("#regimentypeId").html(options); 

        }                    
    }); //end of ajax call
    

    $("#regimentypeId").change(function(event){
        //Reload the grid with the URL above	
        if($("#currentStatus").val() === "HIV+ Non ART"){
            var message = "No ART Commencement record for this patient";
            $("#messageBar").html(message).slideDown('slow');
        }else{
            $("#messageBar").slideUp('slow');
        }
        $("#regimengrid").setGridParam({url: "Regimen_retrieve_id.action?q=1&regimentypeId="+$("#regimentypeId").val(), page:1}).trigger("reloadGrid"); 
    });                  

    $("#refill").blur(function() {
        $("#grid").setGridParam({url: "Refill_period.action?q=1&refill="+$("#refill").val(), page:1}).trigger("reloadGrid");                    
    });
    
    $("#date2").blur(function() { 
        updateRefill(regimen_durations);
    });
 
    $("#save_button").click( function(event){
        if(emptyValues()) {
            $("#messageBar").html("One or more drug dispensed has no duration/quantity entered").slideDown('slow'); 
            return false;
        } 
        else {
            $("#messageBar").slideUp('slow');  
            if($("#userGroup").html() == "Data Analyst") {
                $("#lamisform").attr("action", "Error_message");
                return true;                        
            }
            else {
                if(validateForm()) {
                    if(updateRecord) {
                        if($("#fromPrescription").html() == "true")
                            $("#lamisform").attr("action", "Pharmacy_update");
                        else
                            $("#lamisform").attr("action", "Pharmacy_update");
                    } 
                    else {
                        //console.log($("#fromPrescription").html());
                        if($("#fromPrescription").html() == "true"){
                            $("#lamisform").attr("action", "Pharmacy_prescription_save"); 
                        } else{
                            $("#lamisform").attr("action", "Pharmacy_save");  
                           // console.log("success");
                            
//                            $.ajax({
//                                type: 'POST',
//                                url: 'Pharmacy_save',
//                                data: $(this).serialize() // getting filed value in serialize form
//                            }).done(function(data){
//                                console.log(data);
//                                toastr.success('Record saved successfully.!');
//                            });
                        }
                        
                    }
                    return true;     
                    //window.location.replace("/Pharmacy_search");
                } 
                else {
                    return false;
                }
            }            
        }
    });      
//    $("#delete_button").click(function(event){
//        if($("#userGroup").html() == "Data Analyst") {
//            $("#lamisform").attr("action", "Error_message");
//        }
//        else {
//            $("#lamisform").attr("action", "Pharmacy_delete");
//        }
//        return true;
//    });
}

function updateRefill(regimen_durations){
//    console.log(regimen_durations);
    $("#grid").setGridParam({url: "Refill_period.action?q=1&regimen_durations="+regimen_durations, page:1}).trigger("reloadGrid");
    regimen_durations = [];
}

function showDrugCompositionForRegimen(regimenId){ //Dispenser_grid_retrieve
    var id = parseInt($("#regimentypeId").val());
    $("#grid").setGridParam({url: "Dispenser_grid.action?q=1&id="+regimenId+"&regimentypeId="+$("#regimentypeId").val(), page:1}).trigger("reloadGrid");
    
//    var age = dateDiff($("#dateBirth").val(), new Date(), "years");   
//    if(age <= 14 && (id == 1 || id == 2)) {
//        $("#messageBar").html("You are trying to dispense an adult line regimen to a child").slideDown('slow');     
//    } 
//    else {
//        if(age > 14 && (id == 3 || id == 4)) {
//            $("#messageBar").html("You are trying to dispense a child's line regimen to an adult").slideDown('slow');     
//        } 
//        else {
//            if($("#regimentypePrevious").val().length != 0) {
//                if((id > 0 && id <= 4) || id == 14 ) {
//                    if($("#regimentypeId option:selected").text() != $("#regimentypePrevious").val()) {
//                        $("#messageBar").html("You have switched the patient to another line regimen").slideDown('slow');     
//                    } 
//                    else {
//                        $("#messageBar").slideUp('slow');                 
//                    }            
//                }            
//            }            
//        }            
//    }
}

function showDrugComposition(){ //Dispenser_grid_retrieve
    //var id = parseInt($("#regimentypeId").val());
    $("#grid").setGridParam({url: "Dispenser_grid_retrieve", page:1}).trigger("reloadGrid");
    
//    var age = dateDiff($("#dateBirth").val(), new Date(), "years");   
//    if(age <= 14 && (id == 1 || id == 2)) {
//        $("#messageBar").html("You are trying to dispense an adult line regimen to a child").slideDown('slow');     
//    } 
//    else {
//        if(age > 14 && (id == 3 || id == 4)) {
//            $("#messageBar").html("You are trying to dispense a child's line regimen to an adult").slideDown('slow');     
//        } 
//        else {
//            if($("#regimentypePrevious").val().length != 0) {
//                if((id > 0 && id <= 4) || id == 14 ) {
//                    if($("#regimentypeId option:selected").text() != $("#regimentypePrevious").val()) {
//                        $("#messageBar").html("You have switched the patient to another line regimen").slideDown('slow');     
//                    } 
//                    else {
//                        $("#messageBar").slideUp('slow');                 
//                    }            
//                }            
//            }            
//        }            
//    }
}

function retrieveRegimen(obj, prescription) {
    $.ajax({
        url: "RegimenType_retrieve_id.action",
        dataType: "json",

        success: function(regimenTypeMap) {
            var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
            $.each(regimenTypeMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) //end each
            $("#regimentypeId").html(options);				   
            $("#regimentypeId").val(obj.regimentypeId);
            
            console.log("RegimenType = "+obj.regimentypeId);
            if(prescription === true){
                $("#regimengrid").setGridParam({url: "Regimen_retrieve_id.action?q=1&regimentypeId="+obj.regimentypeId+"&selectedRegimen="+obj.regimenId, page:1}).trigger("reloadGrid");
            }else{
                $("#regimengrid").setGridParam({url: "Regimen_retrieve_id.action?q=1&regimentypeId="+obj.regimentypeId, page:1}).trigger("reloadGrid");
            }

        },complete: function(){
//            if(prescription === true){
//                $("#regimengrid").setGridParam({url: "Regimen_retrieve_id.action?q=1&regimentypeId="+obj.regimentypeId+"&selectedRegimen="+obj.id, page:1}).trigger("reloadGrid");
//            }else{
//                $("#regimengrid").setGridParam({url: "Regimen_retrieve_id.action?q=1&regimentypeId="+obj.regimentypeId, page:1}).trigger("reloadGrid");
//            }
        }                    
    });
}  


function retrievePrescriptions(patientId) {
    var prescription = new Map();
    $.ajax({
        url: "Prescription_retrieve_id.action",
        dataType: "json",
        data: {patientId: patientId},
        success: function(prescriptionList) {
//            console.log(prescriptionList);
            regimen_durations = [];
            for(var i = 0; i < prescriptionList.length; i++){
                prescription = prescriptionList[i];
                regimen_durations.push(prescription.regimenTypeId);
                regimen_durations.push(prescription.regimenId);
                regimen_durations.push(prescription.duration);
                if(prescription.regimenTypeId === "1" || prescription.regimenTypeId === "2" 
                        || prescription.regimenTypeId === "3" || prescription.regimenTypeId === "4"){
                    obj.regimentypeId = prescription.regimenTypeId;
                    obj.regimenId = prescription.regimenId;
                    $("#refill").val(prescription.duration);
                }
            }
            retrieveRegimen(obj, true);
        },
        complete: function(){
//            retrieveRegimen(obj, true);
        }
    }); //end of ajax call
} 

function findPharmacyByDate() {
    $("#date1").datepicker("option", "altField", "#dateVisit");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $.ajax({
        url: "Pharmacy_find_date.action",
        dataType: "json",
        data: {patientId: $("#id").val(), hospitalNum: $("#hospitalNum").val(), dateVisit: $("#dateVisit").val()},
        success: function(pharmacyList) {
            populateForm(pharmacyList);            
        }        
    });
}

var patientId = null;
function populateForm(pharmacyList) {
	
    $.ajax({
        url: "Patient_retrieve.action",
        dataType: "json",                    
        success: function(patientList) {
           console.log(patientList);
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#dateBirth").val(patientList[0].dateBirth);
            $("#gender").val(patientList[0].gender);
            $("#currentStatus").val(patientList[0].currentStatus);
            $("#dateCurrentStatus").val(patientList[0].dateCurrentStatus);
            $("#dateStarted").val(patientList[0].dateStarted); 
            $("#dateLastRefill").val(patientList[0].dateLastRefill);                        
            $("#dateNextRefill").val(patientList[0].dateNextRefill); 
            $("#regimentypePrevious").val(patientList[0].regimentype);                        
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            $("#name").val(patientList[0].surname + " " + patientList[0].otherNames);
            
            patientId = patientList[0].patientId;  
            
            if($.isEmptyObject(pharmacyList)) {
                updateRecord = false;
                resetButtons();

                $("#pharmacyId").val("");
                $("#adrScreened").val("");
                $("#prescripError").val("");
                $("#adherence").val("");
                $("#nextAppointment").val("");
                $("#date2").val("");
                $("#prescripError").removeAttr("checked"); 
                $("#adherence").removeAttr("checked"); 

                if(patientId !== null)
                    retrievePrescriptions(patientId);                                   
            }
            else {
                updateRecord = true;
                initButtonsForModify();

                $("#id").val(pharmacyList[0].patientId);
                $("#hospitalNum").val(pharmacyList[0].hospitalNum);
                $("#dateLastRefill").val(pharmacyList[0].dateLastRefill);                        
                $("#pharmacyId").val(pharmacyList[0].pharmacyId);
                $("#dateVisit").val(pharmacyList[0].dateVisit);
                date = pharmacyList[0].dateVisit;
                $("#date1").val(dateSlice(date));
                $("#adrScreened").val(pharmacyList[0].adrScreened);
                $("#prescripError").val(pharmacyList[0].prescripError);
                $("#adherence").val(pharmacyList[0].adherence);
                $("#nextAppointment").val(pharmacyList[0].nextAppointment);
                date = pharmacyList[0].nextAppointment;
                $("#date2").val(dateSlice(date));
                if(pharmacyList[0].prescripError == "1") {
                   $("#prescripError").attr("checked", "checked"); 
                }
                if(pharmacyList[0].adherence == "1") {
                   $("#adherence").attr("checked", "checked"); 
                }
                for(i = 0; i < pharmacyList.length; i++) {
                    var id = parseInt(pharmacyList[i].regimenId);
                    var idx = regimenIds.indexOf(id); //Find the index
                    if(idx == -1) {
                        regimenIds.push(id);
                    }
                    if(regex.test(pharmacyList[i].regimentypeId)) {
                        $("#refill").val(pharmacyList[i].duration);
                        obj.regimentypeId = pharmacyList[i].regimentypeId;
                        obj.regimenId = pharmacyList[i].regimenId;                                
                    }
                }

                var ids = pharmacyList[0].adrIds;
                adrIds = ids.split("#");

                if($("#adrScreened").val() == "Yes") {
                    $("#adr_button").removeAttr("disabled");                        
                }
                else {
                    $("#adr_button").attr("disabled", "disabled");                                                
                }
                retrieveRegimen(obj, false);                                    
            }
        },
        complete: function(){
            console.log("completed!");
            
        }
    }); //end of ajax call
         
}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateVisit");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date2").datepicker("option", "altField", "#nextAppointment");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");   
    
    // check if date of visit is entered
    if($("#dateVisit").val().length == 0 || !regex.test($("#dateVisit").val())){
        $("#dateHelp").html(" *");
        validate = false;
    }
    else {
        $("#dateHelp").html("");
    }

    if($("#nextAppointment").val().length == 0 || !regex.test($("#nextAppointment").val())){
        $("#nextHelp").html(" *");
        validate = false;
    }
    else {
        $("#nextHelp").html("");
    }
    
//    TODO: Check for selected regimen...
//    if($("#id").val().length == 0){
//        $("#regimenHelp").html(" *");
//        validate = false;
//    }
//    else {
//        $("#regimenHelp").html("");
//    }    
    return validate;
}                 

// TODO: The empty vaue will be set to true if the last loop index evaluates to true efen if there has been a false

function emptyValues(){
    var empty = false;
    var gridIds = $("#grid").getDataIDs();
    for(i = 0; i < gridIds.length; i++) {
        var data = $("#grid").getRowData(gridIds[i]);
        var duration = data.duration;
        if((duration == "" || duration == null || duration == "0")) {
            empty = true;
        }                    
    }
    return empty;
}

//var pattern = "1,2,3,4,14";  //Check if ARV is dispensed
//if($("#regimentypePrevious").val().length != 0 && pattern.indexOf($("#regimentypeId").val()) > -1) {
