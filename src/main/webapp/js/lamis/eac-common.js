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
    $("#date2").mask("99/99/9999");
    $("#date2").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date3").mask("99/99/9999");
    $("#date3").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date4").mask("99/99/9999");
    $("#date4").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date1").change(function () {
        if ($(this).val() != lastSelectDate) {
            lastSelectDate = $(this).val();
            findEacByDate();
        }
    });

    $("#save_button").bind("click", function (event) {
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;
        } else {
            if (validateForm()) {
                var method = 'POST';
                if (updateRecord()) {
                    method = 'PUT'
                }
                fetch('/api/eac', {
                    headers: new Headers({
                        'Content-Type': 'application/json'
                    }), method: method, body: JSON.stringify(data)
                }).then(function (response) {
                    window.location = '/event-page/patient/' + $('#patientId').val();
                }).catch(function (error) {
                    console.log('Error', error)
                });
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
            fetch('/api/eac/' + $('#eacId'), {
                method: 'DELETE',
                headers: new Headers({
                    'Content-Type': 'application/json'
                })
            }).then(function (rs) {
                window.location = '/event-page/patient/' + $('#patientId').val()
            })
        }
        return true;
    });
}

function findEacByDate() {
    $("#date1").datepicker("option", "altField", "#dateEac1");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $.ajax({
        url: encodeURI('/api/eac/find?patientId=' + $('#patientId').val() + '&dateVisit=' + $("#dateEac1").val()),
        dataType: "json",
        success: function (eacList) {
            populateForm(eacList);
        }
    });
}

function populateForm(eacList) {
    if ($.isEmptyObject(eacList)) {
        updateRecord = false;
        resetButtons();

        $("#eacId").val("");
        $("#dateEac1").val("");
        $("#dateEac2").val("");
        $("#dateEac3").val("");
        $("#dateSampledCollected").val("");
    } else {
        updateRecord = true;
        initButtonsForModify();

        $("#eacId").val(eacList[0].eacId);
        $("#dateEac1").val(eacList[0].dateEac1);
        date = eacList[0].dateEac1;
        $("#date1").val(dateSlice(date));

        $("#dateEac2").val(eacList[0].dateEac2);
        date = eacList[0].dateEac2;
        $("#date2").val(dateSlice(date));

        $("#dateEac3").val(eacList[0].dateEac3);
        date = eacList[0].dateEac3;
        $("#date3").val(dateSlice(date));

        $("#dateSampleCollected").val(eacList[0].dateSampleCollected);
        date = eacList[0].dateSampleCollected;
        $("#date4").val(dateSlice(date));

        $("#dateLastViralLoad").val(eacList[0].dateLastViralLoad);
        $("#dateLastViralLoad_view").html(dateSlice(eacList[0].dateLastViralLoad));
        $("#lastViralLoad").val(eacList[0].lastViralLoad);
        $("#lastViralLoad_view").html(eacList[0].lastViralLoad);
    }

    $.ajax({
        url: "/api/patient/" + $('patientId').val(),
        dataType: "json",
        success: function (response) {
            var patientList = response.patientList;
            // set patient id and number for which infor is to be entered
            $("#id").val(patientList[0].patientId);
            $("#hospitalNum").val(patientList[0].hospitalNum);
            $("#patientInfor").html(patientList[0].surname + " " + patientList[0].otherNames);
            $("#name").val(patientList[0].surname + " " + patientList[0].otherNames);

            if (!updateRecord) {
                $("#dateLastViralLoad").val(patientList[0].dateLastViralLoad);
                $("#lastViralLoad").val(patientList[0].lastViralLoad);
                $("#dateLastViralLoad_view").html(dateSlice(patientList[0].dateLastViralLoad));
                $("#lastViralLoad_view").html(patientList[0].lastViralLoad);
            }
            //dueViralLoad(patientList[0].dueViralLoad);
        }
    }); //end of ajax call
}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateEac1");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");

    $("#date2").datepicker("option", "altField", "#dateEac2");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");

    $("#date3").datepicker("option", "altField", "#dateEac3");
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");

    $("#date4").datepicker("option", "altField", "#dateSampleCollected");
    $("#date4").datepicker("option", "altFormat", "mm/dd/yy");

    // check if date of visit is entered
    if ($("#date1").val().length == 0 || !regex.test($("#date1").val())) {
        $("#dateHelp").html(" *");
        validate = false;
    } else {
        $("#dateHelp").html("");
    }
    return validate;
}                 
