/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function initialize() {
    addDatepicker("#date1");
    addDatepicker("#date2");
    addDatepicker("#date3");
    addDatepicker("#date4");
    addDatepicker("#date5");
    addDatepicker("#date6");
    addDatepicker("#date7");

    $("#rapidTestResult").attr("disabled", true);

    $("#rapidTest").change(function(event) {
    if ($("#rapidTest").val() == "Yes") {
                $("#rapidTestResult").attr("disabled", false);
            }
    else {
        $("#rapidTestResult").attr("disabled", true);
    }		
});
	
$("#date1").bind("change", function(event){
    if ($("#date1").val().length != 0 && $("#dateBirth").val().length != 0) {
        var dateString = $("#date1").val().slice(3,5)+"/"+$("#date1").val().slice(0,2)+"/"+$("#date1").val().slice(6);

        var second = 1000, minute = second*60, hour = minute*60, day = hour*24, week = day*7;
        var weeks = Math.round((new Date(dateString).getTime() - new Date($("#dateBirth").val()).getTime()) / week); 
        //if (weeks == 0) weeks = 1;
        $("#ageVisit").val(weeks); 
    }		
})
	
$("#save_button").bind("click", function(event){
    if($("#userGroup").html() == "Data Analyst") {
        $("#lamisform").attr("action", "Error_message");
        return true;                        
    }
    else {
        if(validateForm()) {
            if(updateRecord) {
                $("#lamisform").attr("action", "Childfollowup_update");                
            } 
            else {
                $("#lamisform").attr("action", "Childfollowup_save");                
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
            $("#lamisform").attr("action", "Childfollowup_delete");
        }
        return true;
    });

    $("#close_button").bind("click", function(event){
        $("#lamisform").attr("action", "Childfollowup_search");
        return true;
    });  	
}

function addDatepicker(id) {
    $(id).mask("99/99/9999");
    $(id).datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
}

function populateForm(childfollowupList) {
    if($.isEmptyObject(childfollowupList)) {
        updateRecord = false;
        resetButtons();

        $("#childfollowupId").val("");
        $("#date1").val("");
        $("#ageVisit").val("");
        $("#date2").val("");
        $("#ageNvpInitiated").val("");
        $("#date3").val("");
        $("#ageCotrimInitiated").val("");
        $("#bodyWeight").val("");
        $("#height").val("");
        $("#feeding").val("");
        $("#arv").val("");
        $("#cotrim").val("");
        $("#date4").val("");
        $("#reasonPcr").val("");
        $("#date5").val("");
        $("#date6").val("");
        $("#pcrResult").val("");
        $("#rapidTest").val("");
        $("#rapidTestResult").val("");
        $("#caregiverGivenResult").val("");
        $("#childOutcome").val("");
        $("#referred").val("");
        $("#date7").val("");
    }
    else {
        updateRecord = true;
        initButtonsForModify();

        $("#childId").val(childfollowupList[0].childId);
        $("#childfollowupId").val(childfollowupList[0].childfollowupId);
        date = childfollowupList[0].dateVisit;
	$("#date1").val(dateSlice(date));
        $("#ageVisit").val(childfollowupList[0].ageVisit);
        date = childfollowupList[0].dateNvpInitiated;
	$("#date2").val(dateSlice(date));		
        $("#ageNvpInitiated").val(childfollowupList[0].ageNvpInitiated);
        date = childfollowupList[0].dateCotrimInitiated;
        $("#date3").val(dateSlice(date));	
        $("#ageCotrimInitiated").val(childfollowupList[0].ageCotrimInitiated);
        $("#bodyWeight").val(childfollowupList[0].bodyWeight);	
        $("#height").val(childfollowupList[0].height);
        $("#feeding").val(childfollowupList[0].feeding);
        $("#arv").val(childfollowupList[0].arv);
        $("#cotrim").val(childfollowupList[0].cotrim);
        date = childfollowupList[0].dateSampleCollected;
        $("#date4").val(dateSlice(date));
        $("#reasonPcr").val(childfollowupList[0].reasonPcr);
        date = childfollowupList[0].dateSampleSent;
        $("#date5").val(dateSlice(date));
        date = childfollowupList[0].datePcrResult;
        $("#date6").val(dateSlice(date));
        $("#pcrResult").val(childfollowupList[0].pcrResult);
        $("#rapidTest").val(childfollowupList[0].rapidTest);
        $("#rapidTestResult").val(childfollowupList[0].rapidTestResult);
        if ($("#rapidTestResult").val().length != 0) {
            $("#rapidTestResult").attr("disabled", false);
        }
        $("#caregiverGivenResult").val(childfollowupList[0].caregiverGivenResult);
        $("#childOutcome").val(childfollowupList[0].childOutcome);
        $("#referred").val(childfollowupList[0].referred);
        date = childfollowupList[0].dateNextVisit;
        $("#date7").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
    } 

    $.ajax({
        url: "Child_retrieve.action",
        dataType: "json",                    
        success: function(childList) {
            // set child id and hospital number
            $("#childId").val(childList[0].childId);
            $("#hospitalNum").val(childList[0].hospitalNum);
            $("#dateBirth").val(childList[0].dateBirth);
	}	
    }); //end of ajax call 	

    $.ajax({
        url: "Patient_retrieve.action",
        dataType: "json",                    
        success: function(patientList) {
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames + " (Mother)"); 
        }                    
    }); //end of ajax call 
}

function getMonthDisplayName(month) {
    switch(month) {
        case "01" :return "Jan";
        case "02" :return "Feb";
        case "03" :return "Mar";
        case "04" :return "Apr";
        case "05" :return "May";
        case "06" :return "Jun";
        case "07" :return "Jul";
        case "08" :return "Aug";
        case "09" :return "Sep";
        case "10" :return "Oct";
        case "11" :return "Nov";
        case "12" :return "Dec";
        default :return undefined;
    }   
} 

function validateForm() {
    var validate = true;
    
    $("#date1").datepicker("option", "altField", "#dateVisit");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#dateNvpInitiated");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date3").datepicker("option", "altField", "#dateCotrimInitiated");    
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date4").datepicker("option", "altField", "#dateSampleCollected");    
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date5").datepicker("option", "altField", "#dateSampleSent");    
    $("#date5").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date6").datepicker("option", "altField", "#datePcrResult");    
    $("#date6").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date7").datepicker("option", "altField", "#dateNextVisit");    
    $("#date7").datepicker("option", "altFormat", "mm/dd/yy");    
    return validate;	
}
