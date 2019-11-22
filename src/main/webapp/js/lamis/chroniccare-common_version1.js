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

    $("#date3").mask("99/99/9999");
    $("#date3").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+5",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date4").mask("99/99/9999");
    $("#date4").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
	
    $("#date5").mask("99/99/9999");
    $("#date5").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
	
    $("#date6").mask("99/99/9999");
    $("#date6").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
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

    $("#save_button").bind("click", function(event){
        if($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;                        
        }
        else {
            if(validateForm()) {
                if(updateRecord) {
                    $("#lamisform").attr("action", "Clinic_update");                
                } 
                else {
                    $("#lamisform").attr("action", "Clinic_save");                
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
            $("#lamisform").attr("action", "Clinic_delete");
        }
        return true;
    });    
}

function findClinicByDate() {
    $("#date1").datepicker("option", "altField", "#dateVisit");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $.ajax({
        url: "Clinic_find_date.action",
        dataType: "json",
        data: {patientId: $("#id").val(), hospitalNum: $("#hospitalNum").val(), dateVisit: $("#dateVisit").val()},
        success: function(clinicList) {
            populateForm(clinicList);            
        }        
    });
}

function populateForm(clinicList) {
    if($.isEmptyObject(clinicList)) {
        updateRecord = false;
        resetButtons();

        $("#clinicId").val("");
        $("#clinicStage").val("");
        $("#funcStatus").val("");
        $("#tbStatus").val("");
        $("#bodyWeight").val("");
        $("#height").val("");
        $("#bp").val("");
        $("#bp1").val("");
        $("#bp2").val("");
        $("#pregnant").val("");
        $("#lmp").val("");
        $("#date2").val("");
        $("#oiScreened").val("");
        $("#adrScreened").val("");
        $("#adherenceLevel").val("");
        $("#nextAppointment").val("");
        $("#date3").val("");
        $("#pregnant").removeAttr("checked");
    }
    else {
        updateRecord = true;
        initButtonsForModify();

        $("#clinicId").val(clinicList[0].clinicId);
        $("#dateVisit").val(clinicList[0].dateVisit);
        date = clinicList[0].dateVisit;
        $("#date1").val(dateSlice(date));
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
        $("#lmp").val(clinicList[0].lmp);
        date = clinicList[0].lmp;
        $("#date2").val(dateSlice(date));                        
        $("#oiScreened").val(clinicList[0].oiScreened);
        $("#adrScreened").val(clinicList[0].adrScreened);
        $("#adherenceLevel").val(clinicList[0].adherenceLevel);
        $("#nextAppointment").val(clinicList[0].nextAppointment);
        date = clinicList[0].nextAppointment;
        $("#date3").val(dateSlice(date));                        
        if(clinicList[0].pregnant == "1") {
           $("#pregnant").attr("checked", "checked"); 
        }
        var ids = clinicList[0].oiIds;
        oiIds = ids.split(",");
        ids = clinicList[0].adrIds;
        adrIds = ids.split("#");
        ids = clinicList[0].adhereIds;
        adhereIds = ids.split(",");

        if($("#oiScreened").val() == "Yes") {
            $("#oi_button").removeAttr("disabled");                        
        }
        else {
            $("#oi_button").attr("disabled", "disabled");                                                
        }

        if($("#adrScreened").val() == "Yes") {
            $("#adr_button").removeAttr("disabled");                        
        }
        else {
            $("#adr_button").attr("disabled", "disabled");                                                
        }

        if($("#adherenceLevel").val() == "Fair" || $("#adherenceLevel").val() == "Poor") {
            $("#adhere_button").removeAttr("disabled");                        
        }
        else {
            $("#adhere_button").attr("disabled", "disabled");                                                
        }
    }    

    $.ajax({
        url: "Patient_retrieve.action",
        dataType: "json",                    
        success: function(patientList) {
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#dateBirth").val(patientList[0].dateBirth);
            $("#gender").val(patientList[0].gender);
            $("#currentStatus").val(patientList[0].currentStatus);                        
            $("#dateCurrentStatus").val(patientList[0].dateCurrentStatus);                        
            $("#dateLastClinic").val(patientList[0].dateLastClinic); 
            $("#dateNextClinic").val(patientList[0].dateNextClinic); 
            $("#dateLastCd4").val(patientList[0].dateLastCd4);
            if(patientList[0].gender == "Female") {
                $("#pregnant").removeAttr("disabled");
                $("#date2").removeAttr("disabled");                    
            }
            else {
                $("#pregnant").attr("disabled", "disabled");
                $("#date2").attr("disabled", "disabled");
            }
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            if((patientList[0].dateLastCd4).length != 0) {
                checkCd4(patientList[0].dateLastCd4);
            }
        }                    
    }); //end of ajax call
}

function populateHiddenForm(chroniccareList) {
    if($.isEmptyObject(chroniccareList)) {
        updateRecord = false;
        resetButtons();

        $("#chroniccareId").val("");
        $("#currentStatus").val("");
        $("#lastCd4").val("");
        $("#dateLastCd4").val("");
	$("#date4").val("");
        $("#lastViralLoad").val("");
        $("#dateLastViralLoad").val("");
	$("#date5").val("");
        $("#eligibleCd4").val("");
        $("#eligibleViralLoad").val("");
        $("#cotrimEligibility1").attr("checked", false);
        $("#cotrimEligibility2").attr("checked", false);
        $("#cotrimEligibility3").attr("checked", false);
        $("#cotrimEligibility4").attr("checked", false);
        $("#cotrimEligibility5").attr("checked", false);
        $("#ipt").val("");
        $("#tbTreatment").val("");
        $("#dateStartedTbTreatment").val("");
        $("#date6").val("");
        $("#tbReferred").val("");
        $("#eligibleIpt").val("");
        $("#bmi").val("");
        $("#muac").val("");
        $("#supplementaryFood").val("");
        $("#nutritionalStatusReferred").val("");
        $("#gbv1").val("");
        $("#gbv1Referred").val("");
        $("#gbv2").val("");
        $("#gbv2Referred").val("");
        $("#hypertensive").val("");
        $("#firstHypertensive").val("");
        $("#bpAbove").val("");
        $("#bpReferred").val("");
        $("#diabetic").val("");
        $("#firstDiabetic").val("");
        $("#phdp1").val("");
        $("#phdp1ServicesProvided").val("");
        $("#phdp2").val("");
        //$("#phdp2ServicesProvided").val("");
        $("#phdp3").val("");
        //$("#phdp3ServicesProvided").val("");
        $("#phdp4").val("");
        $("#phdp4ServicesProvided").val("");
        $("#phdp5").val("");
        //$("#phdp5ServicesProvided").val("");
        $("#phdp6").val("");
        //$("#phdp6ServicesProvided").val("");
        $("#phdp7").val("");
        $("#phdp7ServicesProvided").val("");
        //$("#phdp8").val("");
        $("#phdp8ServicesProvided").val("");
        $("#reproductiveIntentions1").val("");
        $("#reproductiveIntentions1Referred").val("");
        $("#reproductiveIntentions2").val("");
        $("#reproductiveIntentions2Referred").val("");
	$("#reproductiveIntentions3").val("");
        $("#reproductiveIntentions3Referred").val("");
        $("#malariaPrevention1").val("");
        $("#malariaPrevention1Referred").val("");
        $("#malariaPrevention2").val("");
        $("#malariaPrevention2Referred").val("");
        $("#dmReferred").val("");
	$("#tbValues").val("");
	$("#dmValues").val("");		
    }
    else {
        updateRecord = true;
        initButtonsForModify();

        $("#chroniccareId").val(chroniccareList[0].chroniccareId);
        $("#currentStatus").val(chroniccareList[0].currentStatus);
        $("#lastCd4").val(chroniccareList[0].lastCd4);
	date = chroniccareList[0].dateLastCd4;
        $("#date4").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
        $("#lastViralLoad").val(chroniccareList[0].lastViralLoad);
	date = chroniccareList[0].dateLastViralLoad;
        $("#date5").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
        $("#eligibleCd4").val(chroniccareList[0].eligibleCd4);
        $("#eligibleViralLoad").val(chroniccareList[0].eligibleViralLoad);
	if (chroniccareList[0].cotrimEligibility1 == "1") {
            $("#cotrimEligibility1").attr("checked", true);
	}
        if (chroniccareList[0].cotrimEligibility2 == "1") {
            $("#cotrimEligibility2").attr("checked", true);
	}
	if (chroniccareList[0].cotrimEligibility3 == "1") {
            $("#cotrimEligibility3").attr("checked", true);
	}
	if (chroniccareList[0].cotrimEligibility4 == "1") {
            $("#cotrimEligibility4").attr("checked", true);
	}
	if (chroniccareList[0].cotrimEligibility5 == "1") {
            $("#cotrimEligibility5").attr("checked", true);
	}
        $("#ipt").val(chroniccareList[0].ipt);
        $("#tbTreatment").val(chroniccareList[0].tbTreatment);
	if($("#tbTreatment").val() == "No") {
            $("#tbscreen_button").attr("disabled",false);
            $("#date6").val("");
            $("#date6").attr("disabled", "disabled");
        }
	else if ($("#tbTreatment").val() == "Yes") {
            $("#tbscreen_button").attr("disabled", "disabled"); 
            $("#date6").attr("disabled", false);
            $("#eligibleIpt").attr("disabled", true);
	}
        else 
        {
            $("#tbscreen_button").attr("disabled", "disabled");
            $("#date6").val("");
            $("#date6").attr("disabled", "disabled");
            $("#eligibleIpt").attr("disabled", false);			
        }
	ate = chroniccareList[0].dateStartedTbTreatment;
        $("#date6").val(date.slice(3,5)+"/"+date.slice(0,2)+"/"+date.slice(6));
	$("#tbReferred").val(chroniccareList[0].tbReferred);
	$("#eligibleIpt").val(chroniccareList[0].eligibleIpt);
        $("#bmi").val(chroniccareList[0].bmi);
        $("#muac").val(chroniccareList[0].muac);
        $("#supplementaryFood").val(chroniccareList[0].supplementaryFood);
        $("#nutritionalStatusReferred").val(chroniccareList[0].nutritionalStatusReferred);
	$("#gbv1").val(chroniccareList[0].gbv1);
        $("#gbv1Referred").val(chroniccareList[0].gbv1Referred);
        $("#gbv2").val(chroniccareList[0].gbv2);
        $("#gbv2Referred").val(chroniccareList[0].gbv2Referred);
        $("#hypertensive").val(chroniccareList[0].hypertensive);
        if ($("#hypertensive").val() == "No") {
            $("#bpAbove").attr("disabled", false);
            $("#firstHypertensive").val("");
            $("#firstHypertensive").attr("disabled", true);
        }
        else if ($("#hypertensive").val() == "Yes") {
            $("#firstHypertensive").attr("disabled", false);
        }
        else { 
            $("#bpAbove").val("");
            $("#bpAbove").attr("disabled", "disabled");	
            $("#firstHypertensive").val("");
            $("#firstHypertensive").attr("disabled", true);			
        }
	$("#firstHypertensive").val(chroniccareList[0].firstHypertensive);
        $("#bpAbove").val(chroniccareList[0].bpAbove);
        $("#bpReferred").val(chroniccareList[0].bpReferred);
	$("#diabetic").val(chroniccareList[0].diabetic);
	if($("#diabetic").val() == "No") {
            $("#dmscreen_button").attr("disabled",false);
            $("#firstDiabetic").val("");
            $("#firstDiabetic").attr("disabled", true);
        }
	else if ($("#diabetic").val() == "Yes") {
            $("#firstDiabetic").attr("disabled", false);
	}
        else {
            $("#dmscreen_button").attr("disabled", "disabled");
            $("#firstDiabetic").val("");
            $("#firstDiabetic").attr("disabled", true);				
        }
	$("#firstDiabetic").val(chroniccareList[0].firstDiabetic);
        $("#phdp1").val(chroniccareList[0].phdp1);
        $("#phdp1ServicesProvided").val(chroniccareList[0].phdp1ServicesProvided);
        $("#phdp2").val(chroniccareList[0].phdp2);
        //$("#phdp2ServicesProvided").val(chroniccareList[0].phdp2ServicesProvided);
        $("#phdp3").val(chroniccareList[0].phdp3);
        //$("#phdp3ServicesProvided").val(chroniccareList[0].phdp3ServicesProvided);
	$("#phdp4").val(chroniccareList[0].phdp4);
        $("#phdp4ServicesProvided").val(chroniccareList[0].phdp4ServicesProvided);
        $("#phdp5").val(chroniccareList[0].phdp5);
        //$("#phdp5ServicesProvided").val(chroniccareList[0].phdp5ServicesProvided);
        $("#phdp6").val(chroniccareList[0].phdp6);
        //$("#phdp6ServicesProvided").val(chroniccareList[0].phdp6ServicesProvided);
        $("#phdp7").val(chroniccareList[0].phdp7);
	$("#phdp7ServicesProvided").val(chroniccareList[0].phdp7ServicesProvided);
        //$("#phdp8").val(chroniccareList[0].phdp8);
        $("#phdp8ServicesProvided").val(chroniccareList[0].phdp8ServicesProvided);
		
        var additionalPhdpServices = chroniccareList[0].phdp9ServicesProvided;
        if (additionalPhdpServices.indexOf("Insecticide treated nets") != -1) $("#phdp91").attr("checked", true);
        if (additionalPhdpServices.indexOf("Intermittent prophylactic treatment") != -1) $("#phdp92").attr("checked", true);
        if (additionalPhdpServices.indexOf("Cervical Cancer Screening") != -1) $("#phdp93").attr("checked", true);
        if (additionalPhdpServices.indexOf("Active member of SG") != -1) $("#phdp94").attr("checked", true);

        if (additionalPhdpServices.indexOf("Family Planning") != -1) $("#phdp95").attr("checked", true);
        if (additionalPhdpServices.indexOf("Basic care kits") != -1) $("#phdp96").attr("checked", true);
        if (additionalPhdpServices.indexOf("Disclosure counseling") != -1) $("#phdp97").attr("checked", true);
        if (additionalPhdpServices.indexOf("Social Services") != -1) $("#phdp98").attr("checked", true);

        if (additionalPhdpServices.indexOf("Linkage to IGAs") != -1) $("#phdp99").attr("checked", true);
        if (additionalPhdpServices.indexOf("Leg") != -1) $("#phdp910").attr("checked", true);
        if (additionalPhdpServices.indexOf("Others") != -1) $("#phdp911").attr("checked", true);

        $("#reproductiveIntentions1").val(chroniccareList[0].reproductiveIntentions1);
        $("#reproductiveIntentions1Referred").val(chroniccareList[0].reproductiveIntentions1Referred);
        $("#reproductiveIntentions2").val(chroniccareList[0].reproductiveIntentions2);
        $("#reproductiveIntentions2Referred").val(chroniccareList[0].reproductiveIntentions2Referred);
        $("#reproductiveIntentions3").val(chroniccareList[0].reproductiveIntentions3);
        $("#reproductiveIntentions3Referred").val(chroniccareList[0].reproductiveIntentions3Referred);
        $("#malariaPrevention1").val(chroniccareList[0].malariaPrevention1);
        $("#malariaPrevention1Referred").val(chroniccareList[0].malariaPrevention1Referred);
        $("#malariaPrevention2").val(chroniccareList[0].malariaPrevention2);
        $("#malariaPrevention2Referred").val(chroniccareList[0].malariaPrevention2Referred);
        $("#dmReferred").val(chroniccareList[0].dmReferred);
        $("#tbValues").val(chroniccareList[0].tbValues);
        $("#dmValues").val(chroniccareList[0].dmValues);

        //fill the screening grids
        var tbValues = new Array();
        tbValues = $("#tbValues").val().split(",");
	
        var ids = $("#tbscreengrid").getDataIDs();	
        for (var i = 0; i < ids.length; i++) {                       
            $("#tbscreengrid").setCell(ids[i], "valueTb", tbValues[i], '', '');
        }
		
	var dmValues = new Array();
        dmValues = $("#dmValues").val().split(",");
	
	var ids2 = $("#dmscreengrid").getDataIDs();	
        for (var i = 0; i < ids2.length; i++) {                       
            $("#dmscreengrid").setCell(ids2[i], "valueDm", dmValues[i], '', '');
        }
		
    }    

}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;
    
    $("#date1").datepicker("option", "altField", "#dateVisit");    
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date2").datepicker("option", "altField", "#lmp");    
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");    
    $("#date3").datepicker("option", "altField", "#nextAppointment");    
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date4").datepicker("option", "altField", "#dateLastCd4");    
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date5").datepicker("option", "altField", "#dateLastViralLoad");    
    $("#date5").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date6").datepicker("option", "altField", "#dateStartedTbTreatment");    
    $("#date6").datepicker("option", "altFormat", "mm/dd/yy");
    
    // retrieve the selected oi and adhere ids from grids 
    $("#oiIds").val($("#oigrid").jqGrid('getGridParam', 'selarrrow'));
    $("#adhereIds").val($("#adheregrid").jqGrid('getGridParam', 'selarrrow'));

    // retrieve the selected tb and dm values from grids 
    var values = "";
    var ids = $("#tbscreengrid").getDataIDs();	
    for (var i = 0; i < ids.length; i++) { 
        $("#tbscreengrid").saveRow(ids[i], false, 'clientArray');	
        var data = $("#tbscreengrid").getRowData(ids[i]);
        values += data.valueTb + ",";
    }
    $("#tbValues").val(values.substring(0, values.lastIndexOf(",")));
	
    values = "";
    ids = $("#dmscreengrid").getDataIDs();	
    for (var i = 0; i < ids.length; i++) { 
        $("#dmscreengrid").saveRow(ids[i], false, 'clientArray');	
        var data = $("#dmscreengrid").getRowData(ids[i]);
        values += data.valueDm + ",";
    }
    $("#dmValues").val(values.substring(0, values.lastIndexOf(",")));
	

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

//to get the ids of selected rows in multiselect grid
// var selectedrows = $.extend([], $("#grid").jqGrid('getGridParam', 'selarrrow'));

//to select it again

// for(var i = 0; i < selectedrows.length; i++) {
//    $("#grid").jqGrid('setSelection', selectedrows[i]);
// }

//to get the selected value of a combobox in jqgrid
//varselectedValue = jQuery("#adrgrid").jqGrid('getCell', gridIds[i], 'severity');   