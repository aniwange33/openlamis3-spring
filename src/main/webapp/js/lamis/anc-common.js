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

    $("#save_button").click(function (event) {
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;
        } else {
            if (validateForm()) {
                var method = 'POST';
                if (updateRecord()) {
                    method = 'PUT'
                }
                fetch('/api/anc', {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    }), method: method, body: JSON.stringify(data)
                }).then(function (response) {
                    window.location = '/pmtct';
                }).catch(function (error) {
                    console.log('Error', error)
                });
                return true;
            } else {
                return false;
            }
        }
    });

    $("#cd4").attr("disabled", true);

    $("#gravida").mask("9?9", {
        placeholder: " "
    });
    $("#parity").mask("9?9", {
        placeholder: " "
    });
    $("#cd4").mask("9?999", {
        placeholder: " "
    });

    $("#bodyWeight").mask("99?9", {
        placeholder: " "
    });

    $("#syphilisTested").change(function (event) {
        if ($("#syphilisTested").val() === "Yes") {
            $("#syphilisTestResult").attr("disabled", false);
            $("#syphilisTreated").attr("disabled", false);
        } else {
            $("#syphilisTestResult").val("");
            $("#syphilisTreated").val("");
            $("#syphilisTestResult").attr("disabled", true);
            $("#syphilisTreated").attr("disabled", true);
        }
    });

    $("#syphilisTestResult").change(function (event) {
        if ($("#syphilisTestResult").val() === "P - Positive") {
            $("#syphilisTreated").attr("disabled", false);
        } else {
            $("#syphilisTreated").attr("disabled", true);
        }
    });

    $("#hepatitisBTested").change(function (event) {
        if ($("#hepatitisBTested").val() === "Yes") {
            $("#hepatitisBTestResult").attr("disabled", false);
        } else {
            $("#hepatitisBTestResult").val("");
            $("#hepatitisBTestResult").attr("disabled", true);
        }
    });

    $("#hepatitisCTested").change(function (event) {
        if ($("#hepatitisCTested").val() === "Yes") {
            $("#hepatitisCTestResult").attr("disabled", false);
        } else {
            $("#hepatitisCTestResult").val("");
            $("#hepatitisCTestResult").attr("disabled", true);
        }
    });


    $("#gravida").blur(function (event) {
        if ($("#gravida").val().lenth != 0) {
            if (parseInt($("#gravida").val()) < 1 || parseInt($("#gravida").val()) > 40) {
                $("#gravida").val("");
                var message = "Gravida ranges from 1 to 40";
                $("#messageBar").html(message).slideDown('slow');
            } else {
                $("#messageBar").slideUp('fast');
            }
        }
    })

    $("#parity").blur(function (event) {
        if ($("#parity").val().length != 0) {
            if (parseInt($("#parity").val()) < 0 || parseInt($("#parity").val()) > 20) {
                $("#parity").val("");
                var message = "Parity ranges from 0 to 20";
                $("#messageBar").html(message).slideDown('slow');
            } else {
                $("#messageBar").slideUp('fast');
            }
        }
    })

    $("#date3").bind("change", function (event) {
        if ($("#date3").val().length != 0) {
            var dateString = $("#date3").val().slice(3, 5) + "/" + $("#date3").val().slice(0, 2) + "/" + $("#date3").val().slice(6);
            var date = new Date(dateString);
            date.setMonth(date.getMonth() + 9); // add nine months 
            date.setDate(date.getDate() + 7); // add seven days
            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1);
            $("#date4").val("" + day + "/" + month + "/" + date.getFullYear());

            var second = 1000, minute = second * 60, hour = minute * 60, day = hour * 24, week = day * 7;
            var weeks = (new Date().getTime() - new Date(dateString).getTime()) / week;
            var weekString = weeks.toString().substring(0, 2);
            if (!weekString.includes("."))
                $("#gestationalAge").val(weekString.substring(0, 2));
            else
                $("#gestationalAge").val(weekString.substring(0, 1));
        }
    });

    $("#arvRegimenCurrent").change(function (event) {
        if ($("#arvRegimenCurrent").val() == "ART") {
            $("#artEligibility").attr("disabled", false);
        } else {
            $("#artEligibility").val("");
            $("#artEligibility").attr("disabled", true);
        }
    });


    $("#timeHivDiagnosis").bind("change", function (event) {
        //TODO: Check
        var dated = $("#date6").val();
        if (dated.length !== 0) {
            var dateVisit = $("#date1").val();
            if ($("#timeHivDiagnosis").val().includes("Newly")) {
                if (dateVisit.length !== 0) {
                    if (parseInt(compare(dateVisit, dated)) === -1) {
                        var message = "ART commencement date " + dated + " cannot be before date newly tested positive which is " + dateVisit;
                        $("#timeHivDiagnosis").val("");
                        alert(message);
                    }
                }
            }
        }
    });

    $("#date1").bind("change", function (event) {
        //TODO: Check
        var dated = $("#date6").val();
        if (dated.length !== 0) {
            var dateVisit = $("#date1").val();
            if ($("#timeHivDiagnosis").val().includes("Newly")) {
                if (parseInt(compare(dateVisit, dated)) === -1) {
                    var message = "ART commencement date " + dated + " cannot be before date newly tested positive";
                    $("#timeHivDiagnosis").val("");
                    alert(message);
                }
            }
        }
    });

    $("#cd4Ordered").change(function (event) {
        if ($("#cd4Ordered").val() == "Yes") {
            $("#cd4").attr("disabled", false);
        } else {
            $("#cd4").attr("disabled", true);
        }
    });

    $("#delete_button").click(function (event) {
        fetch('/api/anc', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function () {
            window.history.back();
        })
        return true;
    });

    $("#close_button").click(function (event) {
        window.history.back();
        return true;
    });
}

function addDatepicker(id) {
    $(id).mask("99/99/9999");
    $(id).datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-10:+10",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });
}

var newAnc = true;

function populateForm(ancList) {
    if ($.isEmptyObject(ancList) || ancList[0] == null) {
        updateRecord = false;
        resetButtons();

        //end of ajax call
        $("#ancNum").val("");

        $("#ancId").val("");
        $("#date1").val("");

        $("#sourceReferral").val("");
        $("#timeHivDiagnosis").val("");
        $("#syphilisTested").val("");
        $("#syphilisTestResult").val("");
        $("#syphilisTreated").val("");
        $("#hepatitisBTestResult").val("");
        $("#hepatitisBTested").val("");
        $("#hepatitisCTestResult").val("");
        $("#hepatitisCTested").val("");
        $("#date2").val("");
        $("#date3").val("");
        $("#date4").val("");
        $("#gestationalAge").val("");
        $("#gravida").val("");
        $("#parity").val("");
    } else {
        updateRecord = true;
        initButtonsForModify();
        newAnc = false;
        $("#ancNum").val(ancList[0].ancNum);
        $("#uniqueId").val(ancList[0].uniqueId);
        $("#ancId").val(ancList[0].ancId);
        date = ancList[0].dateVisit;
        $("#date1").val(dateSlice(date));
        $("#sourceReferral").val(ancList[0].sourceReferral);
        $("#timeHivDiagnosis").val(ancList[0].timeHivDiagnosis);
        $("#syphilisTested").val(ancList[0].syphilisTested);
        if ($("#syphilisTested").val().length !== "Yes") {
            $("#syphilisTestResult").attr("disabled", true);
            $("#syphilisTreated").attr("disabled", true);
        } else {
            $("#syphilisTestResult").val(ancList[0].syphilisTestResult);
            $("#syphilisTestResult").attr("disabled", false);
            if ($("#syphilisTestResult").val().length !== "I - Indeterminate" &&
                $("#syphilisTestResult").val().length !== "N - Negative") {
                $("#syphilisTreated").val(ancList[0].syphilisTreated);
                $("#syphilisTreated").attr("disabled", false);
            } else {
                $("#syphilisTreated").attr("disabled", true);
            }
        }
        $("#hepatitisBTested").val(ancList[0].hepatitisBTested);
        if ($("#hepatitisBTested").val().length !== "Yes") {
            $("#hepatitisBTestResult").attr("disabled", true);
        } else {
            $("#hepatitisBTestResult").val(ancList[0].hepatitisBTestResult);
            $("#hepatitisBTestResult").attr("disabled", false);
        }
        $("#hepatitisCTested").val(ancList[0].hepatitisCTested);
        if ($("#hepatitisCTested").val().length !== "Yes") {
            $("#hepatitisCTestResult").attr("disabled", true);
        } else {
            $("#hepatitisCTestResult").val(ancList[0].hepatitisCTestResult);
            $("#hepatitisCTestResult").attr("disabled", false);
        }
        date = ancList[0].lmp;
        $("#date3").val(dateSlice(date));
        date = ancList[0].edd;
        $("#date4").val(dateSlice(date));
        date = ancList[0].dateNextAppointment;
        $("#date2").val(dateSlice(date));
        $("#gestationalAge").val(ancList[0].gestationalAge);
        $("#gravida").val(ancList[0].gravida);
        $("#parity").val(ancList[0].parity);
    }
    $.ajax({
        url: "/api/patient/" + $('#patientId').val(),
        dataType: "json",
        success: function (res) {
            var patientList = res.patientList;
            // set patient id and number for which infor is to be entered
//            $("#ancId").val(motherList[0].ancId);
            $("#id").val(patientList[0].patientId);
            $("#patientId").val(patientList[0].patientId);
            $("#facilityId").val(patientList[0].facilityId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            $("#uniqueId").val(patientList[0].uniqueId);
            date = patientList[0].dateConfirmedHiv;
            if (date !== "") {
                $("#date5").val(dateSlice(date));
                $("#date5").attr("disabled", true);
            } else {
                $("#date5").attr("disabled", false);
            }
//            $("#timeHivDiagnosis").val(patientList[0].timeHivDiagnosis);
            //Load the last regimenhistory of this client
            date = patientList[0].dateStarted;
            if (date !== "") {
                $("#date6").val(dateSlice(date));
                $("#date6").attr("disabled", true);
            } else {
                $("#date6").attr("disabled", false);
            }
            dueViralLoad(patientList[0].dueViralLoad, patientList[0].viralLoadType);

//            if(newAnc) retrieveLastAnc(patientList[0].id);
        }
    }); //end of ajax call 

    $.ajax({
        url: "/api/anc/partner-information",
        dataType: "json",
        success: function (res) {
            var partnerinformationList = res.partnerinformationList;
            if (!$.isEmptyObject(partnerinformationList[0])) {
                $("#partnerinformationId").val(partnerinformationList[0].partnerinformationId);
                $("#partnerNotification").val(partnerinformationList[0].partnerNotification);
                $("#partnerHivStatus").val(partnerinformationList[0].partnerHivStatus);
                var partnerReferred = partnerinformationList[0].partnerReferred;
                if (partnerReferred.indexOf("FP") !== -1) $("#fp").attr("checked", true);
                if (partnerReferred.indexOf("ART") !== -1) $("#art").attr("checked", true);
                if (partnerReferred.indexOf("OTHERS") !== -1) $("#others").attr("checked", true);
            }

        }
    }); //end of ajax call 
}

function retrieveLastAnc(patientId) {
    $.ajax({
        url: "/api/last-visit/patient/" + patientId,
        dataType: "json",
        success: function (res) {
            var ancLast = res.ancLast;
            // set the ANC static values...
            console.log("Last Anc is:", ancLast);
            if (typeof ancLast !== "undefined") {
                if (ancLast.length > 0) {
                    //Use it to Populate the ANC field and blur all pages if the wpman has not delivered...
//                        $("#sourceReferral").val(ancLast[0].sourceReferral);
//                        $("#sourceReferral").attr("disabled", true);
//                        date = ancLast[0].dateEnrolledPmtct;
//                        if(date !== ""){
//                            $("#date2").val(dateSlice(date));
//                            $("#date2").attr("disabled", true);
//                        }else{
//                            $("#date2").attr("disabled", false);
//                        }
//                        $("#timeHivDiagnosis").val(ancLast[0].timeHivDiagnosis);
//                        $("#timeHivDiagnosis").attr("disabled", true);
                    //$("#numAncVisit").html(ancLast[0].numberAncVisit);
                    //newNumAnc = ancLast[0].numberAncVisit === null ? 1 : ancLast[0].numberAncVisit++;
                    //$("#numberAncVisit").val(""+newNumAnc);
                }
            }
        }
    });
}

function getMonthDisplayName(month) {
    switch (month) {
        case "01" :
            return "Jan";
        case "02" :
            return "Feb";
        case "03" :
            return "Mar";
        case "04" :
            return "Apr";
        case "05" :
            return "May";
        case "06" :
            return "Jun";
        case "07" :
            return "Jul";
        case "08" :
            return "Aug";
        case "09" :
            return "Sep";
        case "10" :
            return "Oct";
        case "11" :
            return "Nov";
        case "12" :
            return "Dec";
        default :
            return undefined;
    }
}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#dateNextAppointment");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date3").datepicker("option", "altField", "#lmp");
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date4").datepicker("option", "altField", "#edd");
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date5").datepicker("option", "altField", "#dateConfirmedHiv");
    $("#date5").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date6").datepicker("option", "altField", "#dateArvRegimenCurrent");
    $("#date6").datepicker("option", "altFormat", "mm/dd/yy");
//    $("#date7").datepicker("option", "altField", "#dateCd4");    
//    $("#date7").datepicker("option", "altFormat", "mm/dd/yy");
//    $("#date8").datepicker("option", "altField", "#dateViralLoad");    
//    $("#date8").datepicker("option", "altFormat", "mm/dd/yy");

    // check if anc number is entered
    if ($("#ancNum").val().length === 0) {
        $("#numHelp").html(" *");
        validate = false;
    } else {
        $("#numHelp").html("");
    }

    // check if date of visit is entered
    if ($("#dateVisit").val().length === 0 || !regex.test($("#dateVisit").val())) {
        $("#datevisitHelp").html(" *");
        validate = false;
    } else {
        $("#datevisitHelp").html("");
    }

    // check if date of enrollment into pmtct is entered
    if ($("#dateNextAppointment").val().length == 0 || !regex.test($("#dateNextAppointment").val())) {
        $("#dateNextAppointmentHelp").html(" *");
        validate = false;
    } else {
        $("#dateNextAppointmentHelp").html("");
    }

    // check if lmp is entered
    if ($("#lmp").val().length == 0 || !regex.test($("#lmp").val())) {
        $("#lmpHelp").html(" *");
        validate = false;
    } else {
        $("#lmpHelp").html("");
    }

    return validate;
}	