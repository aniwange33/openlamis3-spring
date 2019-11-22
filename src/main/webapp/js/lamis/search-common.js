/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor. 
 */
function initialize() {
    $("#hospitalNum").bind("keypress", function(event){
        $("#new_button").html("New");
        if($("#hospitalNum").val().length == 0) {
           $("#grid").setGridParam({url: "Patient_grid.action?q=1", page:1}).trigger("reloadGrid");
           $("#messageBar").slideUp('slow');
           $("#name").val("");           
        }
        else {
            $("#grid").setGridParam({url: "Patient_grid_number.action?q=1&hospitalNum="+$("#hospitalNum").val(), page:1}).trigger("reloadGrid");
            if(event.which == 13 || event.keyCode == 13 || event.which == 9 || event.keyCode == 9) {
                event.preventDefault();
                var hospitalNumber = $("#hospitalNum").val();
                $("#hospitalNum").val(zerorize(hospitalNumber));
                findPatientByNumber();
            }             
        }
        restDetailGrid();
    });
        
    $("#name").bind("keypress", function(event){
        $("#new_button").html("New");
        var name = $("#name").val().toUpperCase();
        $("#grid").setGridParam({url: "Patient_grid.action?q=1&name="+name, page:1}).trigger("reloadGrid");
        restDetailGrid();
    });        
}

function findPatientByNumber() {
    $.ajax({
        url: "Patient_find_number.action",
        dataType: "json",
        data: {hospitalNum: $("#hospitalNum").val()},

        success: function(patientList) {
            if($.isEmptyObject(patientList)) { // check if the return json object is empty ie no patient found
               var message = "Hospital number does not exist";
               $("#messageBar").html(message).slideDown('slow');     
               $("#name").val("");
               id = 0;
               if(gridNum != 1) { // appointment and status page
                   $("#new_button").attr("disabled", "true");
               }
            }
            else {
               $("#messageBar").slideUp('slow');
               $("#name").val(patientList[0].name);
               id = patientList[0].patientId;
               $("#id").val(id);
               $("#new_button").removeAttr("disabled");
               $("#new_button").focus();
            }
            
           //reload the detail grid with record of selected patient
           if(gridNum == 4) {
               $("#detail").setGridParam({url: "Clinic_grid.action?q=1&commence=0&id="+id, page:1}).trigger("reloadGrid");
           }
           if(gridNum == 5) {
               $("#detail").setGridParam({url: "Pharmacy_grid.action?q=1&id="+id, page:1}).trigger("reloadGrid");
           }
           if(gridNum == 6) {
               $("#detail").setGridParam({url: "Laboratory_grid.action?q=1&id="+id, page:1}).trigger("reloadGrid");
           }            
        }                    
    }); //end of ajax call                        

}


function restDetailGrid() {
   //reload the detail grid with record of selected patient
   if(gridNum == 4) {
       $("#detail").setGridParam({url: "Clinic_grid.action?q=1&commence=0&id=0", page:1}).trigger("reloadGrid");
   }
   if(gridNum == 5) {
       $("#detail").setGridParam({url: "Pharmacy_grid.action?q=1&id=0", page:1}).trigger("reloadGrid");
   }
   if(gridNum == 6) {
       $("#detail").setGridParam({url: "Laboratory_grid.action?q=1&id=0", page:1}).trigger("reloadGrid");
   }                
}


function initializeClients() {
    $("#hospitalNum").bind("keypress", function(event){
        //console.log("Length is: "+$("#hospitalNum").val().length+" and value is "+$("#hospitalNum").val());
        if($("#hospitalNum").val() == '') {
            if(event.which == 13 || event.keyCode == 13) {
                event.preventDefault();
                var categoryId = $("#categoryId").val();
                var state = $("#state").val();
                var lga = $("#lga").val();
                var gender = $("#gender").val();
                var ageGroup = $("#ageGroup").val();    
                var showAssigned = $("#showAssigned").prop("checked");
                var pregnacyStaus = $("#pregnancyStatus").val();
                $("#grid").setGridParam({url: "Client_grid.action?q=1&categoryId="+categoryId+"&state="+state+"&lga="+lga+"&gender="+gender+"&ageGroup="+ageGroup+"&showAssigned="+showAssigned+"&pregnancyStatus="+pregnacyStaus, page:1}).trigger("reloadGrid");
                $("#messageBar").slideUp('slow');
                $("#name").val("");           
            }             
        }
        else {
            $("#grid").setGridParam({url: "Client_grid_number.action?q=1&hospitalNum="+$("#hospitalNum").val(), page:1}).trigger("reloadGrid");
            if(event.which == 13 || event.keyCode == 13 || event.which == 9 || event.keyCode == 9) {
                event.preventDefault();
                var hospitalNumber = $("#hospitalNum").val();
                $("#hospitalNum").val(zerorize(hospitalNumber));
                findPatientByNumber();
            }             
        }
        //restDetailGrid();
    });
        
    $("#name").bind("keypress", function(event){
        var name = $("#name").val().toUpperCase();
        $("#grid").setGridParam({url: "Client_grid.action?q=1&name="+name, page:1}).trigger("reloadGrid");
        //restDetailGrid();
    });        
}
function getCategoryCounts() {

}