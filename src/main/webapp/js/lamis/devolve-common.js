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
    $("#date3").mask("99/99/9999");
    $("#date3").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
    $("#date4").mask("99/99/9999");
    $("#date4").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
    $("#date5").mask("99/99/9999");
    $("#date5").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
    $("#date6").mask("99/99/9999");
    $("#date6").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
    
    $.ajax({
        url: "StateId_retrieve.action",
        dataType: "json",
        success: function(stateMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(stateMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }); //end each
            $("#stateId").html(options);                        
        }                    
    }); //end of ajax call

    $("#date1").change(function(){
        if($(this).val() != lastSelectDate) {
            lastSelectDate = $(this).val();
            findDevolveByDate();
        }                    
    }); 

    $("#stateId").change(function(event){
        $.ajax({
            url: "LgaId_retrieve.action",
            dataType: "json",
            data: {stateId: $("#stateId").val()},
            success: function(lgaMap) {
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(lgaMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }); //end each
                $("#lgaId").html(options);                        
            }                    
        }); //end of ajax call
    }); 

    $("#lgaId").change(function(event){
        $.ajax({
            url: "Communitypharm_retrieve.action",
            dataType: "json",
            data: {stateId: $("#stateId").val(), lgaId: $("#lgaId").val()},
            success: function(pharmMap) {
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(pharmMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }); //end each
                $("#communitypharmId").html(options);                        
            }                    
        }); //end of ajax call
    }); 

    $("#communitypharmId").change(function(event){
        $.ajax({
            url: "CommunitypharmId_retrieve.action",
            dataType: "json",
            data: {communitypharmId: $("#communitypharmId").val()},
            success: function(pharmList) {
                $("#address").val(pharmList[0].address);
                $("#phone").val(pharmList[0].phone);
            }                    
        }); //end of ajax call
    }); 

//    $("#typeDmoc").change(function (event) {
//        if ($("#typeDmoc").val() === "MMS") {           
//            $("#dmocForm").hide("slow");
//        } else {
//            $("#dmocForm").show("slow");
//        }
//    });

    $("#save_button").bind("click", function(event){
        if($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;                        
        }
        else {
            if(validateForm()) {
                if(updateRecord) {
                    $("#lamisform").attr("action", "Devolve_update");                
                } 
                else {
                    $("#lamisform").attr("action", "Devolve_save");                
                }
                return true;                        
            } 
            else {
                return false;
            }
        }
        window.location.replace("/Devolve_search");
    });      
    
//    $("#delete_button").bind("click", function(event){
//        if($("#userGroup").html() == "Data Analyst") {
//            $("#lamisform").attr("action", "Error_message");
//        }
//        else {
//            $("#lamisform").attr("action", "Devolve_delete");
//        }
//        return true;
//    });

}    

function retrieveRegimen(obj) {
    $.ajax({
        url: "Regimen_retrieve_name.action",
        dataType: "json",
        data: {regimentype: obj.regimentype},
        success: function(regimenMap) {
            var options = "<option value = '" + '' + "'>" + 'Select' + "</option>";
            $.each(regimenMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }); //end each
            $("#regimen").html(options);                        
            $("#regimen").val(obj.regimen);
            
            console.log(regimenMap);
        }                    
    }); //end of ajax call
}    

function retrieveLga(obj) {
    $.ajax({
        url: "LgaId_retrieve.action",
        dataType: "json",
        data: {stateId: obj.stateId},
        success: function(lgaMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(lgaMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }); //end each
            $("#lgaId").html(options); 
            $("#lgaId").val(obj.lgaId);
        }                    
    }); //end of ajax call
}

function retrieveCommunityPharm(obj) {
    $.ajax({
        url: "Communitypharm_retrieve.action",
        dataType: "json",
        data: {stateId: obj.stateId, lgaId: obj.lgaId},
        success: function(pharmMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(pharmMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }); //end each
            $("#communitypharmId").html(options); 
            $("#communitypharmId").val(obj.communitypharmId); 
        }                    
    }); //end of ajax call            
    
}

function findDevolveByDate() {
    $("#date1").datepicker("option", "altField", "#dateDevolved");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $.ajax({
        url: "Devolve_find_date.action",
        dataType: "json",
        data: {patientId: $("#id").val(), dateDevolved: $("#dateDevolved").val()},
        success: function(devolveList) {
            populateForm(devolveList);            
        }        
    });
}


function populateForm(devolveList) {
    if($.isEmptyObject(devolveList)) { // check if the return json object is empty ie no patient found
        updateRecord = false;
        resetButtons();
        
        $("#devolveId").val("");
        $("#viralLoadAssessed").val("");        
        $("#lastViralLoad").val("");
        $("#dateLastViralLoad").val("");
        $("#cd4Assessed").val("");        
        $("#lastCd4").val("");
        $("#dateLastCd4").val("");
        $("#lastClinicStage").val("");
        $("#dateLastClinicStage").val("");
        $("#arvDispensed").val("");        
        $("#regimentype").val("");
        $("#regimen").val("");
        $("#dateNextClinic").val("");
        $("#dateNextRefill").val("");
        $("#dateLastClinic").val("");
        $("#dateLastRefill").val("");
        $("#notes").val("");
        
        $("#dateStarted").val("");
        $("#clinicStageCommence").val("");
        $("#cd4Commence").val("");
        $("#regimentypeCommence").val("");
        $("#regimenCommence").val("");
        
        $("#communitypharmId").val("");
        $("#address").html("");
        $("#phone").html("");
        $("#stateId").val("");
        $("#lgaId").val("");
        
        $("#date2").val("");
        $("#date3").val("");
        $("#date4").val("");
        $("#date5").val("");
        $("#date6").val("");
    }
    else {
        updateRecord = true;
        initButtonsForModify();

        $("#id").val(devolveList[0].patientId);
        $("#hospitalNum").val(devolveList[0].hospitalNum);
        $("#uniqueId").val(devolveList[0].uniqueId);        
        $("#devolveId").val(devolveList[0].devolveId);
        $("#dateDevolved").val(devolveList[0].dateDevolved);
        $("#typeDmoc").val(devolveList[0].typeDmoc);
        $("#viralLoadAssessed").val(devolveList[0].viralLoadAssessed);        
        $("#lastViralLoad").val(devolveList[0].lastViralLoad);
        $("#dateLastViralLoad").val(devolveList[0].dateLastViralLoad);
        $("#cd4Assessed").val(devolveList[0].cd4Assessed);
        $("#lastCd4").val(devolveList[0].lastCd4);
        $("#dateLastCd4").val(devolveList[0].dateLastCd4);
        $("#lastClinicStage").val(devolveList[0].lastClinicStage);
        $("#dateLastClinicStage").val(devolveList[0].dateLastClinicStage);
        $("#regimentype").val(devolveList[0].regimentype);
        //$("#regimen").val(devolveList[0].regimen);
        $("#arvDispensed").val(devolveList[0].arvDispensed);
        $("#dateNextClinic").val(devolveList[0].dateNextClinic);
        $("#dateNextRefill").val(devolveList[0].dateNextRefill);
        $("#dateLastClinic").val(devolveList[0].dateLastClinic);
        $("#dateLastRefill").val(devolveList[0].dateLastRefill);
        $("#notes").val(devolveList[0].notes);
        
        $("#dateStarted").val(devolveList[0].dateStarted);
        $("#cd4Commence").val(devolveList[0].cd4Commence);
        $("#clinicStageCommence").val(devolveList[0].clinicStageCommence);
        $("#regimentypeCommence").val(devolveList[0].regimentypeCommence);
        $("#regimenCommence").val(devolveList[0].regimenCommence);

        if (devolveList[0].typeDmoc === "MMS") {
            $("#dmocForm").hide("slow");
        } else {
            $("#dmocForm").show("slow");
        }
        
        $("#communitypharmId").val(devolveList[0].communitypharmId);
        
        console.log(".....address: "+devolveList[0].address);
        $("#address").val(devolveList[0].address);
        $("#phone").val(devolveList[0].phone);
        $("#stateId").val(devolveList[0].stateId);
        $("#lgaId").val(devolveList[0].lgaId);
        
        date = devolveList[0].dateDevolved;
        $("#date1").val(dateSlice(date));
        date = devolveList[0].dateLastViralLoad;
        $("#date2").val(dateSlice(date));
        date = devolveList[0].dateLastCd4;
        $("#date3").val(dateSlice(date));
        date = devolveList[0].dateLastClinicStage;
        $("#date4").val(dateSlice(date));
        date = devolveList[0].dateNextClinic;
        $("#date5").val(dateSlice(date));
        date = devolveList[0].dateNextRefill;
        $("#date6").val(dateSlice(date));
        
        obj.regimentype = devolveList[0].regimentype;
        obj.regimen = devolveList[0].regimen;
        obj.communitypharmId = devolveList[0].communitypharmId;
        obj.stateId = devolveList[0].stateId;
        obj.lgaId =  devolveList[0].lgaId;
        retrieveRegimen(obj);
        retrieveLga(obj);
        retrieveCommunityPharm(obj);
    }

    $.ajax({
        url: "Patient_retrieve.action",
        dataType: "json",                    
        success: function(patientList) {
            $("#id").val(patientList[0].patientId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);

            if(!updateRecord) {
                $("#lastViralLoad").val(patientList[0].lastViralLoad);
                $("#dateLastViralLoad").val(patientList[0].dateLastViralLoad);
                $("#lastCd4").val(patientList[0].lastCd4);
                $("#dateLastCd4").val(patientList[0].dateLastCd4);
                $("#lastClinicStage").val(patientList[0].lastClinicStage);
                $("#dateLastClinicStage").val(patientList[0].dateLastClinicStage);
                $("#regimentype").val(patientList[0].regimentype);
                $("#regimen").val(patientList[0].regimen);
                
                date = patientList[0].dateLastViralLoad;
                $("#date2").val(dateSlice(date));
                date = patientList[0].dateLastCd4;
                $("#date3").val(dateSlice(date));
                date = patientList[0].dateLastClinicStage;
                $("#date4").val(dateSlice(date));

                obj.regimentype = patientList[0].regimentype;
                obj.regimen = patientList[0].regimen;
                retrieveRegimen(obj);
            }
        }                    
    }); 
}


function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateDevolved");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date2").datepicker("option", "altField", "#dateLastViralLoad");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");     
    $("#date3").datepicker("option", "altField", "#dateLastCd4");    
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date4").datepicker("option", "altField", "#dateLastClinicStage");    
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy"); 
    $("#date5").datepicker("option", "altField", "#dateNextClinic");    
    $("#date5").datepicker("option", "altFormat", "mm/dd/yy"); 
    $("#date6").datepicker("option", "altField", "#dateNextRefill");    
    $("#date6").datepicker("option", "altFormat", "mm/dd/yy"); 

    // check is date devolve is entered
    if($("#dateDevolved").val().length == 0 || !regex.test($("#dateDevolved").val())){
        $("#dateDevolveHelp").html(" *");
        validate = false;
    }
    else {
        $("#dateDevolveHelp").html("");
    }

    // check if dmoc type is entered
    if($("#typeDmoc").val().length == 0) {
        $("#typeHelp").html(" *");
        validate = false;
    }
    else {
        $("#typeHelp").html("");
    }     
    return validate;
}


