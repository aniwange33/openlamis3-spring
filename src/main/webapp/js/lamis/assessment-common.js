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


    $("#clientCode").bind("blur", function (event) {
        if ($("#clientCode").val().length != 0) {
            //findAssessmentByNumber();
        }
    });

    $("#save_button").bind("click", function (event) {
        var score = $("#riskScore").val();
        var question1 = $("#question1").val();
        var question2 = $("#question2").val();
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
             return true;
        } else {
            if (validateForm()) {
                if (score > 0 || question1 == 'More than 6 months' || question2 == 'Negative' || question2 == 'Unknown' || question2 == 'Never Tested' ) {
                   $("#lamisform").attr("action", "Hts_Assessment_save");
                } else {
                     $("#lamisform").attr("action", "Assessment_save");
                
                }
              
            }else{
                 return false; 
            }

        }
    });

    $("#close_button").bind("click", function (event) {
        $("#lamisform").attr("action", "Hts_search");
        return true;
    });


    $("#question1").change(function (event) {
        hts();
    });

    $("#question2").change(function (event) {
        hts();
    });

    $("#question3").change(function (event) {
        computeRiskScore();
    });

    $("#question4").change(function (event) {
        computeRiskScore();
    });

    $("#question5").change(function (event) {
        computeRiskScore();
    });

    $("#question6").change(function (event) {
        computeRiskScore();
    });

    $("#question7").change(function (event) {
        computeRiskScore();
    });
    $("#question8").change(function (event) {
        computeRiskScore();
    });
    $("#question9").change(function (event) {
        computeRiskScore();
    });
    $("#question10").change(function (event) {
        computeRiskScore();
    });
    $("#question11").change(function (event) {
        computeRiskScore();
    });
    $("#question12").change(function (event) {
        computeRiskScore();
    });
}


function hts() {
    var question1 = $("#question1").val();
    var question2 = $("#question2").val();

    var message = "Enroll client and initiate on ART";
    if (question2 == "Positive not on ART") {
        $("#messageBar").html(message).slideDown('slow');
    } else {
        $("#messageBar").slideUp('slow');
    }
    if ((question1 == "More than 6 months" && question2 == "Negative") || (question2 == "Unknown" || question2 == "Never Tested") || $("#riskScore").val() > 0 ) {
        $("#save_button").attr("disabled", false);
    } else {
        $("#save_button").attr("disabled", true);
    }
}

function findAssessmentByNumber() {
    $.ajax({
        url: "Assessment_find_number.action",
        dataType: "json",
        data: {clientCode: $("#clientCode").val()},
        success: function (assessmentList) {
            populateForm(assessmentList);
        }
    });
}

function populateForm(assessmentList) {
    console.log(assessmentList)
    if ($.isEmptyObject(assessmentList)) {
        updateRecord = false;
        resetButtons();

        $("#assessmentId").val("");
        $("#clientCode").val("");
        $("#dateVisit").val("");
        $("#date1").val("");
        $("#question1").val("");
        $("#question2").val("");
        $("#question3").val("");
        $("#question4").val("");
        $("#question5").val("");
        $("#question6").val("");
        $("#question7").val("");
        $("#question8").val("");
        $("#question9").val("");
        $("#question10").val("");
        $("#question11").val("");
        $("#question12").val("");
        $("#sti1").val("");
        $("#sti2").val("");
        $("#sti3").val("");
        $("#sti4").val("");
        $("#sti5").val("");
        $("#sti6").val("");
        $("#sti7").val("");
        $("#sti1").removeAttr("checked");
        $("#sti2").removeAttr("checked");
        $("#sti3").removeAttr("checked");
        $("#sti4").removeAttr("checked");
        $("#sti5").removeAttr("checked");
        $("#sti6").removeAttr("checked");
        $("#sti7").removeAttr("checked");
        $("#sti8").removeAttr("checked");
    } else {
        updateRecord = true;
        initButtonsForModify();

        $("#assessmentId").val(assessmentList[0].assessmentId);
        $("#clientCode").val(assessmentList[0].clientCode);
        $("#dateVisit").val(assessmentList[0].dateVisit);
        date = assessmentList[0].dateVisit;
        $("#date1").val(dateSlice(date));

        $("#question1").val(assessmentList[0].question1);
        $("#question2").val(assessmentList[0].question2);
        $("#question3").val(assessmentList[0].question3);
        $("#question4").val(assessmentList[0].question4);
        $("#question5").val(assessmentList[0].question5);
        $("#question6").val(assessmentList[0].question6);
        $("#question7").val(assessmentList[0].question7);
        $("#question8").val(assessmentList[0].question8);
        $("#question9").val(assessmentList[0].question9);
        $("#question10").val(assessmentList[0].question10);
        $("#question11").val(assessmentList[0].question11);
        $("#question12").vval(assessmentList[0].question12);
        $("#sti1").val(assessmentList[0].sti1);
        $("#sti2").val(assessmentList[0].sti2);
        $("#sti3").val(assessmentList[0].sti3);
        $("#sti4").val(assessmentList[0].sti4);
        $("#sti5").val(assessmentList[0].sti5);
        $("#sti6").val(assessmentList[0].sti6);
        $("#sti7").val(assessmentList[0].sti7);
        $("#sti8").val(assessmentList[0].sti8);

        if (assessmentList[0].sti1 == "1") {
            $("#sti1").attr("checked", "checked");
        }
        if (assessmentList[0].sti2 == "1") {
            $("#sti2").attr("checked", "checked");
        }
        if (assessmentList[0].sti3 == "1") {
            $("#sti3").attr("checked", "checked");
        }
        if (assessmentList[0].sti4 == "1") {
            $("#sti4").attr("checked", "checked");
        }
        if (assessmentList[0].sti5 == "1") {
            $("#sti5").attr("checked", "checked");
        }
        if (assessmentList[0].sti5 == "1") {
            $("#sti5").attr("checked", "checked");
        }
        if (assessmentList[0].sti6 == "1") {
            $("#sti6").attr("checked", "checked");
        }
        if (assessmentList[0].sti7 == "1") {
            $("#sti7").attr("checked", "checked");
        }
        if (assessmentList[0].sti8 == "1") {
            $("#sti8").attr("checked", "checked");
        }

    }
}
function computeRiskScore() {
    var risk = 0;
    if ($("#question3").val().trim().length !== 0)
        risk = risk + parseInt($("#question3").val());
    if ($("#question4").val().trim().length !== 0)
        risk = risk + parseInt($("#question4").val());
    if ($("#question5").val().trim().length !== 0)
        risk = risk + parseInt($("#question5").val());
    if ($("#question6").val().trim().length !== 0)
        risk = risk + parseInt($("#question6").val());
    if ($("#question7").val().trim().length !== 0)
        risk = risk + parseInt($("#question7").val());
    if ($("#question8").val().trim().length !== 0)
        risk = risk + parseInt($("#question8").val());
    if ($("#question9").val().trim().length !== 0)
        risk = risk + parseInt($("#question9").val());
    if ($("#question10").val().trim().length !== 0)
        risk = risk + parseInt($("#question10").val());
    if ($("#question11").val().trim().length !== 0)
        risk = risk + parseInt($("#question11").val());
    if ($("#question12").val().trim().length !== 0)
        risk = risk + parseInt($("#question12").val());
    $("#riskScore").val(risk);
     hts();
}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");

    // check if client number is entered
    if ($("#clientCode").val().length == 0) {
        $("#numHelp").html(" *");
        validate = false;
    } else {
        $("#numHelp").html("");
    }

    // check is date of visit is entered
    if ($("#dateVisit").val().length == 0) {
        $("#datevisitHelp").html(" *");
        validate = false;
    } else {
        $("#datevisitHelp").html("");
        if (!updateRecord) {
            if ($("#dateVisit").val().length == 0) {
                var date = $("#dateVisit").val();
                $("#dateVisit").val(date);
            }
        }
    }
    return validate;
}




