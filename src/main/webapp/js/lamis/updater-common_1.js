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
    $("#age").mask("9?99", {placeholder: " "});

    $("#print_button").attr("disabled", true);
    $("#save_button").attr("disabled", true);

    $("#save_button").bind("click", function(event){
        $("#save_button").attr("disabled", true);
        event.preventDefault();
        event.stopPropagation();
        formatDateFields();
        if(validateForm()) {    
            postData();
         }
    });

    $("#print_button").bind("click", function(event){
        event.preventDefault();
        event.stopPropagation();
        window.open("Radet_report.action");
        return false;
    });              

    $.ajax({
        url: "State_retrieve.action",
        dataType: "json",
        success: function(stateMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(stateMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            })
            $("#state").html(options);                        
        }                    
    }); //end of ajax call

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
        $.ajax({
            url: "Regimen_retrieve_id.action",
            dataType: "json",
            data: {regimentypeId: $("#regimentypeId").val()},

            success: function(regimenMap) {
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(regimenMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }) //end each
                $("#id").html(options);
            }                    
        }); //end of ajax call
    });

    $("#state").change(function(event){
        $.ajax({
            url: "Lga_retrieve.action",
            dataType: "json",
            data: {state: $("#state").val()},
            success: function(lgaMap) {
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(lgaMap, function(key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }) //end each
                $("#lga").html(options);                        
            }                    
        }); //end of ajax call
    }); 

    $("#surname").change(function(event) {
        $("#surname").val($(this).val().toUpperCase());
        findPatientByNames();
    });
    $("#otherNames").change(function(event) {
        $("#otherNames").val($(this).val().capitalise())
        findPatientByNames();
    });
    $("#gender").change(function(event) {
        findPatientByNames();
    });

    $("#hospitalNum").bind("keypress", function(event){
        if($("#hospitalNum").val().length != 0  && $("#id").val().length == 0) {
            if(event.which == 13 || event.keyCode == 13 || event.which == 9 || event.keyCode == 9) {
                event.preventDefault();
                $("#uniqueId").focus();
                findPatientByNumber();
            }             
        }
    });

    $("#hospitalNum").bind("blur", function(event){
        if($("#hospitalNum").val().length != 0 && $("#id").val().length == 0) {
            $("#uniqueId").focus();
            findPatientByNumber();
        }
    });
}

function findPatientByNumber() {
    $.ajax({
        url: "Patient_find_number.action",
        dataType: "json",
        data: {hospitalNum: zerorize($("#hospitalNum").val())},

        success: function(patientList) {
            if($.isEmptyObject(patientList)) { // check if the return json object is empty ie no patient found
               $("#messageBar").slideUp('slow');                
            }
            else {
               var message = "A " + patientList[0].gender.toLowerCase() + " patient named " + patientList[0].surname + " " + patientList[0].otherNames + " age " + patientList[0].age + " already registered with this hospital number";
               $("#messageBar").html(message).slideDown('slow');                     
            }
        }                    
    }); //end of ajax call                        
}

function findPatientByNames() {
    if($("#surname").val().length != 0 && $("#otherNames").val().length != 0 && $("#gender").val().length != 0 && $("#date1").val().length != 0) {
        $("#date1").datepicker("option", "altField", "#dateBirth");    
        $("#date1").datepicker("option", "altFormat", "mm/dd/yy");            
        $.ajax({
            url: "Patient_find_names.action",
            dataType: "json",
            data: {surname: $("#surname").val(), otherNames: $("#otherNames").val(), dateBirth: $("#dateBirth").val(), gender: $("#gender").val()},

            success: function(messageList) {
                if($.isEmptyObject(messageList)) { // check if the return json object is empty ie no patient found
                   $("#messageBar").slideUp('slow');                
                }
                else {
                   var message = "A " + messageList[0].gender.toLowerCase() + " patient named " + messageList[0].surname + " " + messageList[0].otherNames + " age " + messageList[0].age + " already registered - address: " + messageList[0].address;
                   $("#messageBar").html(message).slideDown('slow');                     
                }
            }                    
        }); //end of ajax call                            
    }
    
}

function retrieveLga(obj) {
    $.ajax({
        url: "Lga_retrieve.action",
        dataType: "json",
        data: {state: obj.state},
        success: function(lgaMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(lgaMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) 
            $("#lga").html(options); 
            $("#lga").val(obj.lga);
            $("#state").val(obj.state);
        }                    
    }); //end of ajax call            
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
        url: "Regimen_retrieve_id.action",
        dataType: "json",
        data: {regimentypeId: obj.regimentypeId},
        success: function(regimenMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(regimenMap, function(key, value) {
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
    if($("#dateRegistration").val().length == 0){
        $("#dateregHelp").html(" *");
        validate = false;
    }
    else {
        $("#dateregHelp").html("");
    }
    if($("#dateStarted").val().length == 0 || !regex.test($("#dateStarted").val())){
        $("#dateHelp").html(" *");
        validate = false;
    }
    else {
        $("#dateHelp").html("");
    }    
    return validate;
}            

function postData() {
    var client = {clientDto: {}};

    client.clientDto.hospitalNum = $("#hospitalNum").val();
    client.clientDto.uniqueId = $("#uniqueId").val();
    client.clientDto.surname = $("#surname").val();
    client.clientDto.otherNames = $("#otherNames").val();
    client.clientDto.gender = $("#gender").val();
    client.clientDto.maritalStatus = $("#maritalStatus").val();
    client.clientDto.dateBirth = $("#dateBirth").val();
    client.clientDto.age = $("#age").val();
    client.clientDto.ageUnit = $("#ageUnit").val();
    client.clientDto.state = $("#state").val();
    client.clientDto.lga = $("#lga").val();
    client.clientDto.address = $("#address").val();
    client.clientDto.phone = $("#phone").val();
    client.clientDto.statusRegistration = $("#statusRegistration").val();
    client.clientDto.currentStatus = $("#currentStatus").val();
    client.clientDto.regimentypeStart = $("#regimentype").val();
    client.clientDto.regimenStart = $("#regimen").val();
    client.clientDto.regimentypeId = $("#regimentypeId").val();
    client.clientDto.regimenId = $("#id").val();
    client.clientDto.clinicStage = $("#clinicStage").val();
    client.clientDto.funcStatus = $("#funcStatus").val();
    client.clientDto.clinicStage = $("#clinicStage").val();
    client.clientDto.cd4 = $("#cd4").val();
    client.clientDto.cd4p = $("#cd4p").val();
    client.clientDto.dateRegistration= $("#dateRegistration").val();
    client.clientDto.dateCurrentStatus = $("#dateCurrentStatus").val();
    client.clientDto.dateStarted = $("#dateStarted").val();
    client.clientDto.dateLastRefill = $("#dateLastRefill").val();
    client.clientDto.duration = $("#duration").val();
    client.clientDto.patientId = $("#id").val();
    client.clientDto.rowcount = $("#rowcount").val();
    
    $.postJSON("ClientInfor_update.action", client, function(data) { 
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
    $("#maritalStatus").val("");
    $("#address").val("");
    $("#phone").val("");
    $("#state").val("");
    $("#lga").val("");
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
    $("#id").val("");
    $("#rowcount").val("");
    $("#save_button").attr("disabled", false);
}

function validateUpload() {
    var validate = true;

    // check if file name is entered
    if($("#attachment").val().length == 0) {
        $("#fileHelp").html(" *");
        validate = false;
    }
    else {
        $("#fileHelp").html("");
    }
    return validate;
}
