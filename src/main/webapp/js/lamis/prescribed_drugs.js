/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function getPrescribedDrugs(){
    var prescriptions = 0; 
    toastr.options = { 
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-bottom-right",
        "preventDuplicates": false,
        "showDuration": "7000",
        "hideDuration": "6000",
        "timeOut": "7000",
        "extendedTimeOut": "7500",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut",
        "onclick": function () { 
            //Take me to the pharmacy List page with drug prescriptions...ie patient search...
            window.location.href = "Prescription_search.action";
        } 
      }

      $.ajax({
        url: "Drugs_prescribed.action",
        dataType: "json",
        method: "POST",
        beforeSend : function(){
            //console.log("here!");
        },
        success: function(response) {
            prescriptions = response.notificationListCount[0].prescriptions;

            var warning = "";
            var i = 0;
            var tracked = false; 

            if(typeof prescriptions != "undefined" && prescriptions != 0){
                i++;
                warning +="<strong>"+prescriptions+"</strong> clients with drug prescriptions <br/>";
                tracked = true;
            }
            if(tracked == true){
                toastr.warning(warning, "Drug Prescription Notification"); 
            }   
        }
    });
    setTimeout(getPrescribedDrugs, 10000);
}




