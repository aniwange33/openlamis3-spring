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


    $("#surname").change(function (event) {
        $("#surname").val($(this).val().toUpperCase());
        findHtsByNames();
    });
    $("#otherNames").change(function (event) {
        $("#otherNames").val($(this).val().capitalise())
        findHtsByNames();
    });

    $("#clientCode").bind("keypress", function (event) {
        if ($("#clientCode").val().length != 0) {
            if (event.which == 13 || event.keyCode == 13 || event.which == 9 || event.keyCode == 9) {
                event.preventDefault();
                findHtsByNumber();
            }
        }
    });

    $("#clientCode").bind("blur", function (event) {
        if ($("#clientCode").val().length != 0) {
            findHtsByNumber();
        }
    });

    $("#save_button").bind("click", function (event) {
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;
        } else {
            console.log("UPDATED 1")
            if (validateForm()) {
                console.log("UPDATED 2")
                if (updateRecord) {
                    console.log("UPDATED 3")
                    $("#lamisform").attr("action", "/api/index-contact");
                    console.log("UPDATED 4")
                } else {
                    $("#lamisform").attr("action", "/api/index-contact");
                }
                return true;
            } else {
                return false;
            }
        }
    });
    $("#delete_button").bind("click", function (event) {
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
        } else {
            fetch(apiUrl + "/api/indext-contact/delete/" + $("#htsId"), {
                method: 'DELETE'
            });

        }
        return true;
    });
}

function populateForm(indexcontactList) {

    if ($.isEmptyObject(indexcontactList)) { // check if the return json object is empty ie no patient found

        updateRecord = false;
        resetButtons();

        $("#indexcontactId").val("");
        $("#htsId").val("");
        $("#clientCode").val("");
        $("#contactType").val("");
        $("#indexContactCode").val("");
        $("#surname").val("");
        $("#otherNames").val("");
        $("#gender").val("");
        $("#age").val("");
        $("#address").val("");
        $("#phone").val("");
        $("#relationship").val("");
        $("#gbv").val("");
        $("#durationPartner").val("");
        $("#phoneTracking").val("");
        $("#homeTracking").val("");
        $("#outcome").val("");
        $("#dateHivTest").val("");
        $("#date1").val("");
        $("#hivStatus").val("");
        $("#linkCare").val("");
        $("#partnerNotification").val("");
        $("#modeNotification").val("");
        $("#serviceProvided").val("");
        $("#deviceconfigId").val("");
        $("#timeStamp").val("");
        $("#idUUID").val("");
    } else {
        updateRecord = true;
        initButtonsForModify();

        $("#indexcontactId").val(indexcontactList[0].indexcontactId);
        $("#htsId").val(indexcontactList[0].htsId);
        $("#clientCode").val(indexcontactList[0].clientCode);
        $("#contactType").val(indexcontactList[0].contactType);
        $("#indexContactCode").val(indexcontactList[0].indexContactCode);
        $("#surname").val(indexcontactList[0].surname);
        $("#otherNames").val(indexcontactList[0].otherNames);
        $("#gender").val(indexcontactList[0].gender);
        $("#age").val(indexcontactList[0].age);
        $("#address").val(indexcontactList[0].address);
        $("#phone").val(indexcontactList[0].phone);
        $("#relationship").val(indexcontactList[0].relationship);
        $("#gbv").val(indexcontactList[0].gbv);
        $("#durationPartner").val(indexcontactList[0].durationPartner);
        $("#phoneTracking").val(indexcontactList[0].phoneTracking);
        $("#homeTracking").val(indexcontactList[0].homeTracking);
        $("#outcome").val(indexcontactList[0].outcome);
        $("#dateHivTest").val(indexcontactList[0].dateHivTest);
        date = indexcontactList[0].dateHivTest;
        $("#date1").val(dateSlice(date));
        $("#hivStatus").val(indexcontactList[0].hivStatus);
        $("#linkCare").val(indexcontactList[0].linkCare);
        $("#partnerNotification").val(indexcontactList[0].partnerNotification);
        $("#modeNotification").val(indexcontactList[0].modeNotification);
        $("#serviceProvided").val(indexcontactList[0].serviceProvided);
        $("#deviceconfigId").val(indexcontactList[0].deviceconfigId);
        $("#timeStamp").val(indexcontactList[0].timeStamp);
        $("#idUUID").val(indexcontactList[0].idUUID);
    }

    $.ajax({
        url: "/api/hts/facility/" + $("#facilityId").val(),
        dataType: "json",
        success: function (htsList) {
            // set hts id and number for which infor is to be entered
            console.log("...." + htsList[0].surname);
            $("#htsId").val(htsList[0].htsId);
            $("#clientCode").val(htsList[0].clientCode);
            $("#patientInfor").html(htsList[0].surname + " " + htsList[0].otherNames);
        }
    });


}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateHivTest");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");

    if ($("#surname").val().length == 0) {
        $("#surnameHelp").html(" *");
        validate = false;
    } else {
        $("#surnameHelp").html("");
    }

    // check is age is entered
    if ($("#age").val().length == 0) {
        $("#ageHelp").html(" *");
        validate = false;
    } else {
        if ($("#age").val() > 120 || $("#age").val() <= 0) {
            $("#ageHelp").html(" *");
            validate = false;
        } else {
            $("#ageHelp").html("");
        }
    }

    // check is gender is entered
    if ($("#gender").val().length == 0) {
        $("#genderHelp").html(" *");
        validate = false;
    } else {
        $("#genderHelp").html("");
    }

    // check is date of registration is entered

    return validate;
}


