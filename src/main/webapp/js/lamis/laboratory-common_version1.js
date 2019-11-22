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
            findLaboratoryByDate();
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

    $("#save_button").bind("click", function(event){
        if($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;                        
        }
        else {
            if(validateForm()) {
                if(updateRecord) {
                    $("#lamisform").attr("action", "Laboratory_update");                
                } 
                else {
                    $("#lamisform").attr("action", "Laboratory_save");                
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
            $("#lamisform").attr("action", "Laboratory_delete");
        }
        return true;
    });
}

function findLaboratoryByDate() {
    $("#date2").datepicker("option", "altField", "#dateReported");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");    
    $.ajax({
        url: "Laboratory_finddate.action",
        dataType: "json",
        data: {patientId: $("#id").val(), hospitalNum: $("#hospitalNum").val(), dateReported: $("#dateReported").val()},
        success: function(laboratoryList) {
            populateForm(laboratoryList);            
        }        
    });
}

function populateForm(laboratoryList) {
    if($.isEmptyObject(laboratoryList)) {
        updateRecord = false;
        resetButtons();

        $.ajax({
            url: "Patient_retrieve.action",
            dataType: "json",                    
            success: function(patientList) {
                // set patient id and number for which infor is to be entered
                $("#id").val(patientList[0].patientId);
                $("#hospitalNum").val(patientList[0].hospitalNum);
                $("#dateLastCd4").val(patientList[0].dateLastCd4);                        
                $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            }                    
        }); //end of ajax call
        $("#laboratoryId").val("");
        $("#dateReported").val("");
        $("#date2").val("");
        $("#labno").val("");
    }
    else {
        updateRecord = true;
        initButtonsForModify();

        $("#id").val(laboratoryList[0].patientId);
        $("#hospitalNum").val(laboratoryList[0].hospitalNum);
        $("#dateLastCd4").val(laboratoryList[0].dateLastCd4);                        
        $("#laboratoryId").val(laboratoryList[0].laboratoryId);
        $("#dateCollected").val(laboratoryList[0].dateCollected);
        date = laboratoryList[0].dateCollected;
        $("#date1").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
        $("#dateReported").val(laboratoryList[0].dateReported);
        date = laboratoryList[0].dateReported;
        $("#date2").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
        $("#labno").val(laboratoryList[0].labno);
    }    
}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateCollected");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date2").datepicker("option", "altField", "#dateReported");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");    
    
    // check if date of result is entered
    if($("#dateReported").val().length == 0 || !regex.test($("#dateReported").val())){
        $("#dateHelp1").html(" *");
        validate = false;
    }
    else {
        $("#dateHelp1").html("");
    }
    return validate;
}                 
