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

    $("#date7").mask("99/99/9999");
    $("#date7").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#age").mask("9?99", {placeholder: " "});

    $("#save_button").attr("disabled", true);

    $("#save_button").bind("click", function(event){
        formatDateFields();
        if(validateForm()) { 
            postData();
        }
        event.preventDefault();
        event.stopPropagation();
    });

   //Populate regimen combo for in ART commencement 
    $.ajax({
        url: "RegimenType_retrieve_name.action?commence",
        dataType: "json",

        success: function(regimenTypeMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(regimenTypeMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) //end each
            $("#regimentype").html(options);                        
        }                    
    }); //end of ajax call

    $("#regimentype").change(function(event){
        console.log("Clicked...1");
        $.ajax({
            url: "Regimen_retrieve_name.action",
            dataType: "json",
            data: {regimentype: $("#regimentype").val()},

            success: function(regimenMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(regimenMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }) //end each
                $("#regimen").html(options);                        
            }                    
        }); //end of ajax call
    });                    

    //Populate regimen combo for in dispensing 
    $.ajax({
        url: "RegimenType_retrieve_id.action?commence",
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
        console.log("Clicked");
        $.ajax({
            url: "Regimen_retrieve_map.action",
            dataType: "json",
            data: {regimentypeId: $("#regimentypeId").val()},

            success: function(regimenIdMap) {
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(regimenIdMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }) //end each
                $("#id").html(options);
            }                    
        }); //end of ajax call
    });
}

function retrieveRegimen(obj) {
    $.ajax({
        url: "Regimen_retrieve_name.action",
        dataType: "json",
        data: {regimentype: obj.regimentypeStart},
        success: function(regimenMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(regimenMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) 
            $("#regimen").html(options);                        
            $("#regimen").val(obj.regimenStart);
            $("#regimentype").val(obj.regimentypeStart);
        }                    
    }); //end of ajax call
}    

function retrieveRegimenId(obj) {
    $.ajax({
        url: "Regimen_retrieve_map.action",
        dataType: "json",
        data: {regimentypeId: obj.regimentypeId},
        success: function(regimenIdMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(regimenIdMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) 
            $("#id").html(options);
            $("#id").val(obj.regimenId);
            $("#regimentypeId").val(obj.regimentypeId);
        }                    
    }); //end of ajax call
}    

function formatDateFields() {
    $("#date1").datepicker("option", "altField", "#dateBirth");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy"); 

    $("#date2").datepicker("option", "altField", "#dateRegistration");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy"); 

    $("#date3").datepicker("option", "altField", "#dateCurrentStatus");    
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy"); 

    $("#date4").datepicker("option", "altField", "#dateStarted");    
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy"); 

    $("#date5").datepicker("option", "altField", "#dateLastRefill");    
    $("#date5").datepicker("option", "altFormat", "mm/dd/yy");     

    $("#date6").datepicker("option", "altField", "#dateCurrentViralLoad");    
    $("#date6").datepicker("option", "altFormat", "mm/dd/yy");     

    $("#date7").datepicker("option", "altField", "#dateCollected");    
    $("#date7").datepicker("option", "altFormat", "mm/dd/yy");     
}            

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;
    
    // check if patient number is entered
    if($("#hospitalNum").val().length == 0) {
        $("#numHelp").html(" *");
        validate = false;
    }
    else {
        $("#numHelp").html("");
    }

    // check is surname is entered
    if($("#surname").val().length == 0) {
        $("#surnameHelp").html(" *");
        validate = false;
    }
    else {
        $("#surnameHelp").html("");
    }

    // check is age is entered
    if($("#age").val().length == 0 || $("#ageUnit").val().length == 0) {
        $("#ageHelp").html(" *");
        validate = false;
    }
    else {
        $("#ageHelp").html("");
    }
    
    // check is gender is entered
    if($("#gender").val().length == 0){
        $("#genderHelp").html(" *");
        validate = false;
    }
    else {
        $("#genderHelp").html("");
    }

    // check is status at registration is entered
    if($("#statusRegistration").val().length == 0){
        $("#statusregHelp").html(" *");
        validate = false;
    }
    else {
        $("#statusregHelp").html("");
    }
    
    // check is date of registration is entered
    if($("#dateRegistration").val().length == 0){
        $("#dateregHelp").html(" *");
        validate = false;
    }
    else {
        $("#dateregHelp").html("");
    }
    
    // check is date started is entered
    if($("#dateStarted").val().length == 0 || !regex.test($("#dateStarted").val())){
        $("#dateHelp").html(" *");
        validate = false;
    }
    else {
        $("#dateHelp").html("");
    }

    // check if date last refill is entered, then regimen ID and duration must not be empty
    if($("#dateLastRefill").val().length != 0){
        if($("#id").val().length == 0){
            $("#regimenHelp").html(" *");
            validate = false;
        }
        else {
            $("#regimenHelp").html("");
        }
        if($("#duration").val().length == 0){
            $("#refillHelp").html(" *");
            validate = false;
        }
        else {
            $("#refillHelp").html("");
        }
    }

    // check if date last viral load is entered, then viralLoad must not be empty
    if($("#dateCurrentViralLoad").val().length != 0){
        if($("#viralLoad").val().length == 0){
            $("#vlHelp").html(" *");
            validate = false;
        }
        else {
            $("#vlHelp").html("");
        }
    }

    return validate;
}            

function postData() {
    var client = {clientDto: {}};

    client.clientDto.patientId = $("#id").val();
    client.clientDto.hospitalNum = $("#hospitalNum").val();
    client.clientDto.uniqueId = $("#uniqueId").val();
    client.clientDto.surname = $("#surname").val();
    client.clientDto.otherNames = $("#otherNames").val();
    client.clientDto.gender = $("#gender").val();
    client.clientDto.dateBirth = $("#dateBirth").val();
    client.clientDto.age = $("#age").val();
    client.clientDto.ageUnit = $("#ageUnit").val();
    client.clientDto.enrollmentSetting = $("#enrollmentSetting").val();
    client.clientDto.statusRegistration = $("#statusRegistration").val();
    client.clientDto.currentStatus = $("#currentStatus").val();
    client.clientDto.regimentypeStart = $("#regimentype").val();
    client.clientDto.regimenStart = $("#regimen").val();
    client.clientDto.regimentypeId = $("#regimentypeId").val();
    client.clientDto.regimenId = $("#id").val();
    client.clientDto.clinicStage = $("#clinicStage").val();
    client.clientDto.funcStatus = $("#funcStatus").val();
    client.clientDto.cd4 = $("#cd4").val();
    client.clientDto.cd4p = $("#cd4p").val();
    client.clientDto.dateRegistration= $("#dateRegistration").val();
    client.clientDto.dateCurrentStatus = $("#dateCurrentStatus").val();
    client.clientDto.dateStarted = $("#dateStarted").val();
    client.clientDto.dateLastRefill = $("#dateLastRefill").val();
    client.clientDto.duration = $("#duration").val();
    client.clientDto.dateCurrentViralLoad = $("#dateCurrentViralLoad").val();
    client.clientDto.dateCollected = $("#dateCollected").val();
    client.clientDto.viralLoad = $("#viralLoad").val();
    client.clientDto.viralLoadIndication = $("#viralLoadIndication").val();
    
    $.postJSON("ClientData_update.action", client, function(data) { 
        $("#grid").setGridParam({url: "Radet_grid.action", page:1}).trigger("reloadGrid");
        clearPage();
    });                                        
}

function clearPage() { 
    $("#hospitalNum").val("");
    $("#uniqueId").val("");
    $("#surname").val("");
    $("#otherNames").val("");
    $("#gender").val("");
    $("#dateBirth").val("");
    $("#date1").val("");
    $("#age").val("");
    $("#ageUnit").val("");
    $("#statusRegistration").val("");
    $("#dateRegistration").val("");
    $("#date2").val("");
    $("#currentStatus").val("");
    $("#dateCurrentStatus").val("");    
    $("#date3").val("");
    $("#regimentype").val("");
    $("#regimen").val("");
    $("#regimentypeId").val("");
    $("#id").val("");
    $("#clinicStage").val("");
    $("#funcStatus").val("");
    $("#clinicStage").val("");
    $("#cd4").val("");
    $("#cd4p").val("");
    $("#dateStarted").val("");
    $("#date4").val("");
    $("#dateLastRefill").val("");
    $("#date5").val("");
    $("#duration").val("");
    $("#dateCurrentViralLoad").val("");
    $("#date6").val("");
    $("#dateCollected").val("");
    $("#date7").val("");
    $("#id").val("");
    $("#category").val("");
    $("#viralLoad").val("");
    $("#viralLoadIndication").val("");
    $("#enrollmentSetting").val("")
    $("#save_button").attr("disabled", true);
}
