/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function initialize() {    
    //$("#date").mask("99/99/9999");
    $("#date").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-0:+5",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
   // $("#dateStart").mask("99/99/9999");
    $("#dateStart").datepicker({
        dateFormat: "dd/mm/yy",
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
   // $("#dateEnd").mask("99/99/9999");
    $("#dateEnd").datepicker({
        dateFormat: "dd/mm/yy",
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
    
    $("body").bind('ajaxStart', function(event){
        $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');	
    });

    $("body").bind('ajaxStop', function(event){
        $("#loader").html('');
    });
    $("#messageBar").hide();
}

function validateExportForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    // check if start and end dates are entered
    if($("#dateStart").val().length == 0 || !regex.test($("#dateStart").val())){
        $("#dateHelp").html(" *");
        validate = false;
    }
    else {
        $("#dateHelp").html("");
    }
    
    if($("#dateEnd").val().length == 0 || !regex.test($("#dateEnd").val())){
        $("#dateHelp1").html(" *");
        validate = false;
    }
    else {
        $("#dateHelp1").html("");
    }
    return validate;
}

function validateMessageForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date").datepicker("option", "altField", "#dateToSend");    
    $("#date").datepicker("option", "altFormat", "mm/dd/yy");    

    $("#daysToAppointmentHelp").html("");
    $("#dateHelp").html("");
    
    // check if message is entered
    if($("#messageType").val() == 1) {
        // check if days to appointment is entered
        if($("#daysToAppointment").val().length == 0) {
            $("#daysToAppointmentHelp").html(" *");
            validate = false;
        }
        else {
            $("#daysToAppointmentHelp").html("");
        }
    }
        
    // check if message type is entered
    if($("#messageType").val() == 3) {
        // check if date of visit is entered
        if($("#dateToSend").val().length == 0 || !regex.test($("#dateToSend").val())){
            $("#dateHelp").html(" *");
            validate = false;
        }
        else {
            $("#dateHelp").html("");
        }
    }
            
    // check if message is entered
    if($("#message1").val().length == 0) {
        $("#messageHelp").html(" *");
        validate = false;
    }
    else {
        $("#messageHelp").html("");
    }
    return validate;
}

function validateModemForm() {
    var regex = /^\+\d+/;
    var validate = true;
    
    // check if model is entered
    if($("#model").val().length == 0) {
        $("#modelHelp").html(" *");
        validate = false;
    }
    else {
        $("#modelHelp").html("");
    }
    
    // check if manufacturer is entered
    if($("#manufacturer").val().length == 0) {
        $("#manufacturerHelp").html(" *");
        validate = false;
    }
    else {
        $("#manufacturerHelp").html("");
    }

    // check if com port is entered
    if($("#comPort").val().length == 0) {
        $("#comPortHelp").html(" *");
        validate = false;
    }
    else {
        $("#comPortHelp").html("");
    }
    
    // check if baud rate is entered
    if($("#baudRate").val().length == 0) {
        $("#baudRateHelp").html(" *");
        validate = false;
    }
    else {
        $("#baudRateHelp").html("");
    }
        
    // check if country code is entered
    if($("#countryCode").val().length == 0 || !regex.test($("#countryCode").val())) {
        $("#countryCodeHelp").html(" *");
        validate = false;
    }
    else {
        $("#countryCodeHelp").html("");
    }
    return validate;
}

function validateSyncForm() {
    var regex = /^\+\d+/;
    var validate = true;
    
    // check if username is entered
    if($("#userName").val().length == 0) {
        $("#userHelp").html(" *");
        validate = false;
    }
    else {
        $("#userHelp").html("");
    }
    
    // check if password is entered
    if($("#password").val().length == 0) {
        $("#passwordHelp").html(" *");
        validate = false;
    }
    else {
        $("#passwordHelp").html("");
    }
    return validate;
}


function populateForm(modemList) {
    $("#model").val(modemList[0].model);
    $("#manufacturer").val(modemList[0].manufacturer);
    $("#comPort").val(modemList[0].comPort);
    $("#baudRate").val(modemList[0].baudRate);
    $("#countryCode").val(modemList[0].countryCode);
}
 
function resetButtons() {
    $("#save_button").html("Save");
    $("#close_button").html("Close");
    $("#close_button").attr("data-button-state", "close");
    $("#delete_button").attr("disabled", true);
}

function initButtonsForModify() {
    $("#save_button").html("Modify");
    $("#close_button").html("Close");
    $("#close_button").attr("data-button-state", "close");
    $("#delete_button").attr("disabled", false);
}
 
function multipart_sample () {
   $.each(context.prototype.fileData, function(i, obj) {
        data.append(i, obj.value.files[0]);
    });
    var data = new FormData();            
    $.each($("#file")[0].files, function(i, file) {
        data.append(i, file);
    });

    $.ajax({
        type: "POST",
        url: "Upload_file2.action",
        data: data,
        processData: false,
        contentType: false,
        error: function(jgXHR, status) {
            alert(status);
        },
        success: function(status) {
            status = "Import Completed";
            $("#messageBar").html(status).slideDown('fast');     
        }                    
    }); 
}

function processingStatusNotifier(status) {
     new $.Zebra_Dialog(
        status, {
        'type': 'information',
        /*'custom_class': 'notifierclass',*/
        'buttons':  false,
        'modal': false,
        'position': ['right - 20', 'top + 40'],
        'auto_close': 10000,
        'show_close_button': false
    });
}


