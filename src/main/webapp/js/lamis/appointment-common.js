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
        yearRange: "-0:+5",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date2").mask("99/99/9999");
    $("#date2").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-0:+5",
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
                $("#lamisform").attr("action", "Appointment_save");
                return true;                        
            } 
            else {
                return false;
            }
        }
    });                    
}


function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;
    
    $("#date1").datepicker("option", "altField", "#dateNextClinic");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date2").datepicker("option", "altField", "#dateNextRefill");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");    

    // check if date of visit is entered
    if($("#date1").val().length == 0 || !regex.test($("#date1").val())){
        $("#dateHelp1").html(" *");
        if($("#date2").val().length == 0 || !regex.test($("#date2").val())){
            $("#dateHelp2").html(" *");
            validate = false;
        }
        else {
            $("#dateHelp2").html("");
        }                    
    }
    else {
        $("#dateHelp1").html("");
    }                    
    return validate;
}                 
