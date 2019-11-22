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
        yearRange: "-5:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date3").mask("99/99/9999");
    $("#date3").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-5:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date4").mask("99/99/9999");
    $("#date4").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-5:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
    $("#date5").mask("99/99/9999");
    $("#date5").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-5:+0",
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
            }) 
            $("#stateId").html(options);                        
        }                    
    }); //end of ajax call

    $("#stateId").change(function(event){
        $.ajax({
            url: "LgaId_retrieve.action",
            dataType: "json",
            data: {stateId: $("#stateId").val()},
            success: function(lgaMap) {
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(lgaMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }) //end each
                $("#lgaId").html(options);                        
            }                    
        }); //end of ajax call
    }); 

    $("#lgaId").change(function(event){
        $.ajax({
            url: "Facility_retrieve.action",
            dataType: "json",
            data: {stateId: $("#stateId").val(), lgaId: $("#lgaId").val()},

            success: function(facilityMap) {
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(facilityMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }) //end each
                $("#treatmentUnitId").html(options);                        
            }                    
        }); //end of ajax call
    }); 

    $("#labno").change(function(event) {
        $("#labno").val($(this).val().toUpperCase());
    });

    $("#labno").bind("keypress", function(event){
        if(gridNum == 1) {
            $("#grid").setGridParam({url: "Specimen_grid.action?q=1&labno="+$("#labno").val(), page:1}).trigger("reloadGrid");
            if($("#labno").val().length == 0) {
               $("#messageBar").slideUp('slow');
               $("#specimenId").val("");           
            }
            else {
                if(event.which == 13 || event.keyCode == 13 || event.which == 9 || event.keyCode == 9) {
                    event.preventDefault();
                    findSpecimenByNumber();
                }             
            }
        }
    });

    $("#save_button").bind("click", function(event){
        if($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;                        
        }
        else {
            if(validateForm()) {
                if(updateRecord) {
                    $("#lamisform").attr("action", "Specimen_update");                
                } 
                else {
                    $("#lamisform").attr("action", "Specimen_save");                
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
            $("#lamisform").attr("action", "Specimen_delete");
        }
        return true;
    });    
}

function findSpecimenByNumber() {
    $.ajax({
        url: "Specimen_find_number.action",
        dataType: "json",
        data: {labno: $("#labno").val()},
        success: function(specimenList) {
            if($.isEmptyObject(specimenList)) { // check if the return json object is empty ie no specimen found
               var message = "Laboratory number does not exist";
               $("#messageBar").html(message).slideDown('slow');     
               $("#specimenId").val("");
            }
            else {
               $("#messageBar").slideUp('slow');
               $("#labno").val(specimenList[0].labno);
               $("#specimenId").val(specimenList[0].specimenId);
            }            
        }                    
    }); //end of ajax call                        
}

function populateForm(specimenList) {
    if($.isEmptyObject(specimenList)) {
        updateRecord = false;
        resetButtons();

        $("#specimenId").val("");
        $("#stateId").val("");
        $("#treamentUnitId").val("");
        $("#specimenType").val("");
        $("#labno").val("");
        $("#barcode").val("");
        $("#qualityCntrl").removeAttr("checked");
        $("#hospitalNum").val("");
        $("#surname").val("");
        $("#otherNames").val("");
        $("#date1").val("");
        $("#dateBirth").val("");
        $("#age").val("");
        $("#ageUnit").val("");
        $("#gender").val("");
        $("#address").val("");
        $("#phone").val("");
        $("#date2").val("");
        $("#dateReceived").val("");
        $("#date3").val("");
        $("#dateCollected").val("");

        $("#eidId").val("");
        $("#motherName").val("");
        $("#motherAddress").val("");
        $("#motherPhone").val("");
        $("#senderName").val("");
        $("#senderAddress").val("");
        $("#senderDesignation").val("");
        $("#senderPhone").val("");
        $("#reasonPcr").val("");
        $("#rapidTestDone").val("");
        $("#dateRapidTest").val("");
        $("#rapidTestResult").val("");
        $("#motherArtReceived").val("");
        $("#motherProphylaxReceived").val("");
        $("#childProphylaxReceived").val("");
        $("#breastfedEver").val("");
        $("#feedingMethod").val("");
        $("#breastfedNow").val("");
        $("#feedingCessationAge").val("");
        $("#cotrim").val("");
        $("#nextAppointment").val("");
        
        $("#assignPrinterLink").html("");
        retrieveLastTreatmentUnit();         
    }
    else {
        updateRecord = true;
        initButtonsForModify();

        $("#specimenId").val(specimenList[0].specimenId);
        $("#specimenType").val(specimenList[0].specimenType);
        $("#labno").val(specimenList[0].labno);
        $("#barcode").val(specimenList[0].barcode);
        $("#qualityCntrl").removeAttr("checked");
        if(specimenList[0].qualityCntrl == "1") {
           $("#qualityCntrl").attr("checked", "checked"); 
        }
        $("#hospitalNum").val(specimenList[0].hospitalNum);
        $("#surname").val(specimenList[0].surname);
        $("#otherNames").val(specimenList[0].otherNames);
        $("#dateBirth").val(specimenList[0].dateBirth);
        date = specimenList[0].dateBirth;
        $("#date1").val(dateSlice(date));
        $("#age").val(specimenList[0].age);
        $("#ageUnit").val(specimenList[0].ageUnit);
        $("#gender").val(specimenList[0].gender);
        $("#address").val(specimenList[0].address);
        $("#phone").val(specimenList[0].phone);
        $("#dateReceived").val(specimenList[0].dateReceived);
        date = specimenList[0].dateReceived;
        $("#date2").val(dateSlice(date));
        $("#dateCollected").val(specimenList[0].dateCollected);
        date = specimenList[0].dateCollected;
        $("#date3").val(dateSlice(date));

        $("#eidId").val(specimenList[1].eidId);
        $("#motherName").val(specimenList[1].motherName);
        $("#motherAddress").val(specimenList[1].motherAddress);
        $("#motherPhone").val(specimenList[1].motherPhone);
        $("#senderName").val(specimenList[1].senderName);
        $("#senderAddress").val(specimenList[1].senderAddress);
        $("#senderDesignation").val(specimenList[1].senderDesignation);
        $("#senderPhone").val(specimenList[1].senderPhone);
        $("#reasonPcr").val(specimenList[1].reasonPcr);
        $("#rapidTestDone").val(specimenList[1].rapidTestDone);
        $("#dateRapidTest").val(specimenList[1].dateRapidTest);
        date = specimenList[1].dateRapidTest;
        $("#date4").val(dateSlice(date));
        $("#rapidTestResult").val(specimenList[1].rapidTestResult);
        $("#motherArtReceived").val(specimenList[1].motherArtReceived);
        $("#motherProphylaxReceived").val(specimenList[1].motherProphylaxReceived);
        $("#childProphylaxReceived").val(specimenList[1].childProphylaxReceived);
        $("#breastfedEver").val(specimenList[1].breastfedEver);
        $("#feedingMethod").val(specimenList[1].feedingMethod);
        $("#breastfedNow").val(specimenList[1].breastfedNow);
        $("#feedingCessationAge").val(specimenList[1].feedingCessationAge);
        $("#cotrim").val(specimenList[1].cotrim);
        $("#nextAppointment").val(specimenList[1].nextAppointment);
        date = specimenList[1].nextAppointment;
        $("#date5").val(dateSlice(date));

        if($("#specimenType").val() == "DBS") {
            $("#eid_button").removeAttr("disabled");                        
        }
        else {
            $("#eid_button").attr("disabled", "disabled");                                                
        }

        obj.stateId =  specimenList[0].stateId;
        obj.lgaId =  specimenList[0].lgaId;
        obj.treatmentUnitId =  specimenList[0].treatmentUnitId;
        retrieveTreatmentUnit(obj); 
        checkPrinter(obj.treatmentUnitId);
    }    
}

function retrieveTreatmentUnit(obj) {
    $.ajax({
        url: "CurrentTreatmentUnit_retrieve.action",
        dataType: "json",
        data: {stateId: obj.stateId, lgaId: obj.lgaId},
        success: function(treatmentUnitObj) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(treatmentUnitObj.lgaMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            })
            $("#lgaId").html(options);                        

            options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(treatmentUnitObj.facilityMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            })
            $("#treatmentUnitId").html(options); 
            
            $("#treatmentUnitId").val(obj.treatmentUnitId);
            $("#lgaId").val(obj.lgaId);
            $("#stateId").val(obj.stateId);
        }                    
    }); //end of ajax call    
}

function retrieveLastTreatmentUnit() {
    $.ajax({
        url: "CurrentTreatmentUnit_retrieve.action",
        dataType: "json",
        success: function(treatmentUnitObj) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(treatmentUnitObj.lgaMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            })
            $("#lgaId").html(options);                        

            options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(treatmentUnitObj.facilityMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            })
            $("#treatmentUnitId").html(options); 
            
            $("#treatmentUnitId").val(treatmentUnitObj.treatmentUnitId);
            $("#lgaId").val(treatmentUnitObj.lgaId)
            $("#stateId").val(treatmentUnitObj.stateId);
        }                    
    }); //end of ajax call    
}

function barcode() {    
    var pressed = false; 
    var chars = []; 
    $(window).keypress(function(e) {
        if (e.which >= 48 && e.which <= 57) {
            chars.push(String.fromCharCode(e.which));
        }
        console.log(e.which + ":" + chars.join("|"));
        if (pressed == false) {
            setTimeout(function(){
                if (chars.length >= 10) {
                    var barcode = chars.join("");
                    console.log("Barcode Scanned: " + barcode);
                    // assign value to some input (or do whatever you want)
                    $("#barcode").val(barcode);
                }
                chars = [];
                pressed = false;
            },500);
        }
        pressed = true;
    });

    $("#barcode").keypress(function(e){
        if ( e.which === 13 ) {
            console.log("Prevent form submit.");
            e.preventDefault();
        }
    });    
}

function assignPrinter() {
    if($("#smsPrinter").val().length !=0) {
        $.ajax({
            url: "Assign_printer.action",
            dataType: "json",
            data: {facilityId: $("#treatmentUnitId").val(), smsPrinter: $("#smsPrinter").val()},
            success: function(status) {
                $("#messageBar").html("Sms printer assigned").slideDown('slow');
            }                    
        }); //end of ajax call
        $("#dialog").dialog("close");
    } 
}

function checkPrinter(facilityId) {
    $.ajax({
        url: "Check_printer.action",
        dataType: "json",
        data: {facilityId: facilityId},

        success: function(smsPrinter) {
            if(smsPrinter == "") {
                $("#assignPrinterLink").html("Assign SMS printer");            
            }
            else {
                $("#assignPrinterLink").html("");            
            }
        }                    
    }); //end of ajax call            
}


function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;
    
    $("#date1").datepicker("option", "altField", "#dateBirth");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date2").datepicker("option", "altField", "#dateReceived");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date3").datepicker("option", "altField", "#dateCollected");    
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date4").datepicker("option", "altField", "#dateRapidTest");    
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date5").datepicker("option", "altField", "#nextAppointment");    
    $("#date5").datepicker("option", "altFormat", "mm/dd/yy");
        
    if($("#treatmentUnitId").val().length == 0){
        $("#facilityHelp").html(" *");
        validate = false;
    }
    else {
        $("#facilityHelp").html("");
    }
    
    if($("#specimenType").val().length == 0){
        $("#specimenTypeHelp").html(" *");
        validate = false;
    }
    else {
        $("#specimenTypeHelp").html("");
    }  
    
    if($("#labno").val().length == 0){
        $("#labnoHelp").html(" *");
        validate = false;
    }
    else {
        $("#labnoHelp").html("");
    }  
    
    if($("#dateReceived").val().length == 0 || !regex.test($("#dateReceived").val())){
        $("#dateHelp").html(" *");
        validate = false;
    }
    else {
        $("#dateHelp").html("");
    }    
    return validate;
}

