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
    $("#date1").change(function(){
        if($(this).val() != lastSelectDate) {
            findClinicByDate();
        }
        lastSelectDate = $(this).val();
    });
    
//    $("#gestationalAge").attr("disabled", "true");
      $("#maternal").attr("hidden", true);
//    $("#maternalStatusArt").change( function(event){
//        if($("#maternalStatusArt").val() === "2") {
//            $("#gestationalAge").attr("disabled", false);
//        }
//        else {
//            $("#gestationalAge").val(""); 
//            $("#gestationalAge").attr("disabled", true);
//        } 
//    });
    
    $("#gestationalAge").attr("disabled", "disabled");
    $("#gestationalAge1").attr("hidden", true);
    $("#gestationalAge").attr("hidden", true);
    $("#maternalStatusArt").change(function(event){
        var status = $("#maternalStatusArt").val();
        if(status !== "2") {
            $("#gestationalAge").val("");
            $("#gestationalAge").attr("disabled", "disabled");
        }
        else {
            $("#gestationalAge").removeAttr("disabled");
        }
    }); 

    $("#date2").mask("99/99/9999");
    $("#date2").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+20",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
	
    $("#bodyWeight").keypress(function(key) {
        if((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
            return false;
        }
        return true;
    })
    $("#height").keypress(function(key) {
        if((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
            return false;
        }
        return true;
    })
    $("#bp1").mask("9?99", {placeholder:" "});
    $("#bp2").mask("9?99", {placeholder:" "});
    
    $("#bp1").blur(function(event){
        $("#bp").val($("#bp1").val()+"/"+$("#bp2").val());
    })
    $("#bp2").blur(function(event){
        $("#bp").val($("#bp1").val()+"/"+$("#bp2").val());
    })
    $("#cd4").keypress(function(key) {
        if((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
            return false;
        }
        return true;
    })
    $("#cd4p").keypress(function(key) {
        if((key.charCode < 48 || key.charCode > 57) && (key.charCode != 0) && (key.charCode != 8) && (key.charCode != 9) && (key.charCode != 46)) {
            return false;
        }
        return true;
    });
    
    $("#save_button").bind("click", function(event){
        if($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;                        
        }
        else {
            if(validateForm()) {
                var method = 'POST';
                if (updateRecord()) {
                    method = 'PUT'
                }
                fetch('/api/clinic', {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    }), method: method, body: JSON.stringify(data)
                }).then(function (response) {
                    window.location = '/event-page/patient/' + $('#patientId').val();
                }).catch(function (error) {
                    console.log('Error', error)
                });
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
            fetch('/api/clinic/' + $('#clinicId').val(), {
                method: 'DELETE',
                headers: new Headers({
                    'Content-Type': 'application/json'
                })
            }).then(function (re) {
                window.location.href = "/event-page/patient/" + $('#patientId').val();
            })
        }
        return true;
    });

    $.ajax({
        url: "/api/regimen/grid?regimenType=commence",
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
}

function findClinicByDate() {
    $("#date1").datepicker("option", "altField", "#dateVisit");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $.ajax({
        url: encodeURI('/api/clinic/find?patientId=' + $('#patientId').val() + '&dateVisit=' + $('#dataVisit').val()),
        dataType: "json",
        success: function(clinicList) {
            populateForm(clinicList);            
        }        
    });
}

function retrieveRegimen(obj) {
    $.ajax({
        url: "/api/regimen/grid?regimentypeId=" + obj.regimentype,
        dataType: "json",
        success: function(regimenMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(regimenMap, function(key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }) //end each
            $("#regimen").html(options);                        
            $("#regimen").val(obj.regimen);
            $("#regimentype").val(obj.regimentype);
        }                    
    }); //end of ajax call
}    

function populateForm(clinicList) {
    if($.isEmptyObject(clinicList)) {
        updateRecord = false;
        resetButtons();

        $("#clinicId").val("");
        $("#cd4").val("");
        $("#cd4p").val("");                        
        $("#clinicStage").val("");
        $("#funcStatus").val("");
        $("#tbStatus").val("");
        $("#bodyWeight").val("");
        $("#height").val("");
        $("#bp").val("");
        $("#bp1").val("");
        $("#bp2").val("");
        $("#oiScreened").val("");
        $("#adrScreened").val("");
        $("#adherenceLevel").val("");
        $("#nextAppointment").val("");
        $("#date2").val("");
        $("#maternalStatusArt").val();
        $("#gestationalAge").val();
    }
    else {
        updateRecord = true;
        initButtonsForModify();

        $("#clinicId").val(clinicList[0].clinicId);
        $("#dateVisit").val(clinicList[0].dateVisit);
        date = clinicList[0].dateVisit;
        $("#date1").val(dateSlice(date));
        $("#cd4").val(clinicList[0].cd4);
        $("#cd4p").val(clinicList[0].cd4p);                        
        $("#clinicStage").val(clinicList[0].clinicStage);
        $("#funcStatus").val(clinicList[0].funcStatus);
        $("#tbStatus").val(clinicList[0].tbStatus);
        $("#bodyWeight").val(clinicList[0].bodyWeight);
        $("#height").val(clinicList[0].height);
        $("#bp").val(clinicList[0].bp);
        var bp = clinicList[0].bp;
        var bps = new Array();
        bps = bp.split("/");
        $("#bp1").val(bps[0]);
        $("#bp2").val(bps[1]);
        $("#pregnant").val(clinicList[0].pregnant);
        $("#breastfeeding").val(clinicList[0].breastfeeding);
        $("#oiScreened").val(clinicList[0].oiScreened);
        $("#adrScreened").val(clinicList[0].adrScreened);
        $("#adherenceLevel").val(clinicList[0].adherenceLevel);
        $("#nextAppointment").val(clinicList[0].nextAppointment);
        date = clinicList[0].nextAppointment;
        $("#date2").val(dateSlice(date));   
        $("#notes").val(clinicList[0].notes);
        
        if(clinicList[0].pregnant === "1"){
            $("#maternalStatusArt").removeAttr("disabled");
            $("#maternalStatusArt").val("2");
        }
        else if(clinicList[0].breastfeeding === "1"){
            $("#maternalStatusArt").removeAttr("disabled");
            $("#maternalStatusArt").val("3");
        }
        else if(clinicList[0].pregnant === "0" && clinicList[0].breastfeeding === "0"){
            $("#maternalStatusArt").removeAttr("disabled");
            $("#maternalStatusArt").val("1");
        }
        //Gestational Age
        if($("#maternalStatusArt").val() === "2") {
            $("#gestationalAge").attr("disabled", false);
            $("#gestationalAge").val(clinicList[0].gestationalAge);
        }
        else {
            $("#gestationalAge").attr("disabled", true);
        }
        
        obj.regimentype = clinicList[0].regimentype;
        obj.regimen = clinicList[0].regimen;
        retrieveRegimen(obj);
    } 

    $.ajax({
        url: "/api/patient/" + $('#patientId').val(),
        dataType: "json",                    
        success: function(patientList) {
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#currentStatus").val(patientList[0].currentStatus);                        
            $("#dateCurrentStatus").val(patientList[0].dateCurrentStatus);                        
            $("#dateLastClinic").val(patientList[0].dateLastClinic);                        
            $("#dateNextClinic").val(patientList[0].dateNextClinic); 
            $("#dateLastCd4").val(patientList[0].dateLastCd4);                        
            if(patientList[0].gender === "Female") {
                $("#maternal").attr("hidden", false);
                $("#maternalStatusArt").attr("disabled", false);
            }
            else {
                $("#maternal").attr("hidden", true);
                $("#maternalStatusArt").attr("disabled", true);
            } 
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
        }                    
    }); //end of ajax call

    
//    $("#gender").change(function(event){
//        var gender = $("#gender").val();
//        if(gender === "Female") {
//            $("#maternalStatusArt").removeAttr("disabled");
//        }
//        else {
//            $("#maternalStatusArt").val("");
//            $("#maternalStatusArt").attr("disabled", "disabled");
//        }
//    }); 
  

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


/*
 * 
    if($("#cd4").val().length != 0 && isNaN($("#cd4").val())) {
        $("#cd4Help").html(" *");
        validate = false;        
    }
    else {
        $("#cd4Help").html("");
    }
    if($("#cd4p").val().length != 0 && isNaN($("#cd4p").val())) {
        $("#cd4Help").html(" *");
        validate = false;                
    }
    else {
        $("#cd4Help").html("");
    }

 */