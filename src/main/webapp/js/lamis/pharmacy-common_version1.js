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
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
            
    $.ajax({
        url: "RegimenType_retrieve_id.action",
        dataType: "json",

        success: function(regimenTypeMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(regimenTypeMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) //end each
            $("#regimentypeId").html(options);                        

        }                    
    }); //end of ajax call

    $("#regimentypeId").change(function(event){
        $.ajax({
            url: "Regimen_retrieve_id.action",
            dataType: "json",
            data: {regimentypeId: $("#regimentypeId").val()},

            success: function(regimenMap) {
                var options = "";
                $.each(regimenMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }) //end each
                $("#id").html(options);
            }                    
        }); //end of ajax call
    });
    
    $("#id").dblclick(function(event){
        var id = parseInt($("#id").val());
        var idx = regimenIds.indexOf(id); //Find the index
        if(idx != -1) {
            regimenIds.splice(idx, 1);  // Remove it if found
        } 
        else {
            regimenIds.push(id);
        }                    
        $("#grid").setGridParam({url: "Dispenser_grid.action?q=1&regimenIds="+regimenIds, page:1}).trigger("reloadGrid");
        
        if($("#regimentypeId option:selected").text() != $("#regimentypePrevious").val()) {
            var message = "Alert: You have switched the patient to another line regimen";
            $("#messageBar").html(message).slideDown('slow');     
        } 
        else {
            $("#messageBar").slideUp('slow');                 
        }
    });                    

    $("#refill").mask("9?999", {placeholder: " "});
    $("#refill").blur(function(event) {
        var refill = $("#refill").val();
        $("#grid").setGridParam({url: "Refill_period.action?q=1&refill="+refill, page:1}).trigger("reloadGrid");                    
     });

    $("#save_button").bind("click", function(event){
        if($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;                        
        }
        else {
            if(validateForm()) {
                if(updateRecord) {
                    $("#lamisform").attr("action", "Pharmacy_update");                
                } 
                else {
                    $("#lamisform").attr("action", "Pharmacy_save");                
                }
                return true;                        
            } 
            else {
                return false;
            }
        }
    });      
    $("#delete_button").bind("click", function(event){
        if($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
        }
        else {
            $("#lamisform").attr("action", "Pharmacy_delete");
        }
        return true;
    });
}

function retrieveRegimen(obj) {
    $.ajax({
        url: "Regimen_retrieve_id.action",
        dataType: "json",
        data: {regimentypeId: obj.regimentypeId},
        success: function(regimenMap) {
            var options = "";
            $.each(regimenMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) //end each
            $("#id").html(options);
            $("#id").val(obj.regimenId);
            $("#regimentypeId").val(obj.regimentypeId);
        }                    
    }); //end of ajax call
}    

function findPharmacyByDate() {
    $("#date1").datepicker("option", "altField", "#dateVisit");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $.ajax({
        url: "Pharmacy_finddate.action",
        dataType: "json",
        data: {patientId: $("#id").val(), hospitalNum: $("#hospitalNum").val(), dateVisit: $("#dateVisit").val()},
        success: function(pharmacyList) {
            populateForm(pharmacyList);            
        }        
    });
}

function populateForm(pharmacyList) {
    if($.isEmptyObject(pharmacyList)) {
        updateRecord = false;
        resetButtons();

        $.ajax({
            url: "Patient_retrieve.action",
            dataType: "json",                    
            success: function(patientList) {
                // set patient id and number for which infor is to be entered
                $("#id").val(patientList[0].patientId);
                $("#hospitalNum").val(patientList[0].hospitalNum);
                $("#dateLastRefill").val(patientList[0].dateLastRefill); 
                $("#regimentypePrevious").val(patientList[0].regimentype);
                $("#regimenPrevious").val(patientList[0].regimen);
                $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            }                    
        }); //end of ajax call
        $("#pharmacyId").val("");
        $("#refill").val("");
        $("#adrScreened").val("");
        $("#prescripError").val("");
        $("#adherence").val("");
        $("#nextAppointment").val("");
        $("#date2").val("");
        $("#prescripError").removeAttr("checked"); 
        $("#adherence").removeAttr("checked"); 
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
        $("#date1").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
        $("#refill").val(pharmacyList[0].duration);
        $("#adrScreened").val(pharmacyList[0].adrScreened);
        $("#prescripError").val(pharmacyList[0].prescripError);
        $("#adherence").val(pharmacyList[0].adherence);
        $("#nextAppointment").val(pharmacyList[0].nextAppointment);
        date = pharmacyList[0].nextAppointment;
        $("#date2").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
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
                obj.regimentypeId = pharmacyList[i].regimentypeId;
                obj.regimenId = pharmacyList[i].regimenId;                                
            }
        }
        retrieveRegimen(obj);
    }                        
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
     return validate;
}                 
