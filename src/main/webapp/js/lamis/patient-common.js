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

    $("#date5").mask("99/99/9999");
    $("#date5").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#date6").mask("99/99/9999");
    $("#date6").datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        constrainInput: true,
        buttonImageOnly: true,
        buttonImage: "/images/calendar.gif"
    });

    $("#gravida").mask("999", {
        placeholder: " "
    });
    $("#parity").mask("999", {
        placeholder: " "
    });

    $("#age").mask("999", {placeholder: " "});
    $("#ancNo").hide();
    $("#pmtct").hide();
    $("#lm").hide();
    $("#gravid").hide();
    $("#tdAncNo").hide();
    $("#tdAncNo1").hide();

    $("#date1").bind("change", function (event) {
        var dated = $("#date1").val();
        if (dated.length !== 0) {
            var todayFormatted = formatDate(new Date());
            if (parseInt(compare($("#date1").val(), todayFormatted)) === -1) {
                var message = "Date of birth cannot be later than today please correct!";
                $("#messageBar").html(message).slideDown('slow');
                $("#date1").val("");
            } else {
                $("#messageBar").slideUp('slow');
                findPatientByNames();
                calculateAge();
            }
        }
    });

    $("#date3").bind("change", function (event) {
        //TODO: Check
        var dated = $("#date3").val();
        if (dated.length !== 0) {
            var todayFormatted = formatDate(new Date());
            if (parseInt(compare($("#date3").val(), todayFormatted)) === -1) {
                var message = "Date confirmed HIV test cannot be later than today please correct!";
                $("#messageBar").html(message).slideDown('slow');
                $("#date3").val("");
            } else {
                if ($("#date2").val().length !== 0) {
                    if (parseInt(compare($("#date3").val(), $("#date2").val())) === -1) {
                        var message = "Date confirmed HIV test cannot be later than Date of registration please correct!";
                        $("#messageBar").html(message).slideDown('slow');
                        $("#date3").val("");
                    } else {
                        $("#messageBar").slideUp('slow');
                    }
                }
            }
        }
    });

    $("#date4").bind("change", function (event) {
        //TODO: Check
        var dated = $("#date4").val();
        if (dated.length !== 0) {
            var todayFormatted = formatDate(new Date());
            if (parseInt(compare($("#date4").val(), todayFormatted)) === -1) {
                var message = "Date enrolled into PMTCT cannot be later than today please correct!";
                $("#messageBar").html(message).slideDown('slow');
                $("#date4").val("");
            } else {
                $("#messageBar").slideUp('slow');
            }
        }
    });

    $("#date5").bind("change", function (event) {
        //TODO: Check
        var dated = $("#date5").val();
        if (dated.length !== 0) {
            var todayFormatted = formatDate(new Date());
            if (parseInt(compare($("#date5").val(), todayFormatted)) === -1) {
                var message = "L.M.P date cannot be later than today please correct!";
                $("#messageBar").html(message).slideDown('slow');
                $("#date5").val("");
            } else {
                $("#messageBar").slideUp('slow');
            }
        }
    });

    $("#date2").bind("change", function (event) {
        //Check that the age is proper.
        var dated = $("#date2").val();
        if (dated.length !== 0) {
            var todayFormatted = formatDate(new Date());
            if (parseInt(compare($("#date2").val(), todayFormatted)) === -1) {
                var message = "Date of Registration/Transfer In cannot be later than today please correct!";
                $("#messageBar").html(message).slideDown('slow');
                $("#date2").val("");
            } else {
                var age = $("#age").val();
                var ageUnit = $("#ageUnit").val();
                if (age !== '' && ageUnit !== '') {
                    var date = $("#date1").val();
                    if (date === '') {
                        if (ageUnit === 'day(s)') ageUnit = 'days';
                        else if (ageUnit === 'month(s)') ageUnit = 'months';
                        else if (ageUnit === 'year(s)') ageUnit = 'years';
                        calculateDateOfBirth(age, ageUnit);
                        var date = $("#date1").val();
                    } else {
                        calculateAge();
                    }
                } else {
                    var date = $("#date1").val();
                    if (date !== '')
                        calculateAge();
                }
            }
        }
    });

    $("#age").bind("change", function (event) {
        var date = $("#date2").val();
        var age = $("#age").val();
        var ageUnit = $("#ageUnit").val();
        if (date !== '' && ageUnit !== '') {
            if (ageUnit === 'day(s)') ageUnit = 'days';
            else if (ageUnit === 'month(s)') ageUnit = 'months';
            else if (ageUnit === 'year(s)') ageUnit = 'years';
            if (age < 10) {
//                var message = "You cannot place child under 10 years of age on PMTCT!";
//                $("#messageBar").html(message).slideDown('slow');
                checkPmtctRequirements(true);
            } else {
                $("#messageBar").slideUp('slow');
                calculateDateOfBirth(age, ageUnit);
            }
        }
    });

    $("#ageUnit").bind("change", function (event) {
        var date = $("#date2").val();
        var age = $("#age").val();
        var ageUnit = $("#ageUnit").val();
        if (date !== '' && age !== '') {
            if (ageUnit === 'day(s)') ageUnit = 'days';
            else if (ageUnit === 'month(s)') ageUnit = 'months';
            else if (ageUnit === 'year(s)') ageUnit = 'years';
            calculateDateOfBirth(age, ageUnit);
        }
    });

    $("#changeNumberLink").bind("click", function () {
        $("#dialog").dialog("open");
        return false;
    });

    $("#close_button").click(function (event) {

        window.location.href = "/patient";
    });

    $("#gender").change(function (event) {

        if ($("#gender").val() === "Female") {
            $("#pregnancyStatus").removeAttr("disabled");
        } else {
            $("#pregnancyStatus").val("");
            $("#pregnancyStatus").attr("disabled", "disabled");
        }

        checkPmtctRequirements(false);
    });

    $.ajax({
        url: "/api/state",
        dataType: "json",
        success: function (stateMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(stateMap, function (idx, obj) {
                options += "<option value = '" + obj.name + "'>" + obj.name + "</option>";
            })
            $("#state").html(options);
        }
    }); //end of ajax call


    $("#state").change(function (event) {
        let name = $("#state").val();
        $.ajax({
            url: "/api/state/lga/name/" +name,
            dataType: "json",
            success: function (lgaMap) {
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(lgaMap, function (idx, obj) {
                    options += "<option value = '" + obj.name + "'>" + obj.name + "</option>";
                })
                $("#lga").html(options);
            }
        }); //end of ajax call
    });

    $("#surname").change(function (event) {
        $("#surname").val($(this).val().toUpperCase());
        findPatientByNames();
    });

    $("#otherNames").change(function (event) {
        $("#otherNames").val($(this).val().capitalise());
        findPatientByNames();
    });

    $("#entryPoint").change(function (event) {
        checkPmtctRequirements(false);
    });


    $("#hospitalNum").bind("keypress", function (event) {
        if ($("#hospitalNum").val().length !== 0) {
            if (event.which === 13 || event.keyCode === 13 || event.which === 9 || event.keyCode === 9) {
                event.preventDefault();
                var hospitalNumber = $("#hospitalNum").val();
                $("#hospitalNum").val(zerorize(hospitalNumber));
                $("#uniqueId").focus();
                findPatientByNumber();
            }
        }
    });

    $("#hospitalNum").bind("blur", function (event) {
        if ($("#hospitalNum").val().length !== 0) {
            var hospitalNumber = $("#hospitalNum").val();
            $("#hospitalNum").val(zerorize(hospitalNumber));
            $("#uniqueId").focus();
            findPatientByNumber();
        }
    });


    $("#save_button").bind("click", function (event) {
        var inputs = document.getElementById("lamisform").elements;
        var data = {};
        for (i = 0; i < inputs.length; i++) {
            data[inputs[i].name] = inputs[i].value;
        }
        if (updateRecord) {
            fetch('/api/patient', {
                headers: new Headers({
                    'Content-Type': 'application/json'
                }), method: 'PUT', body: JSON.stringify(data)
            }).then(function (response) {
                if (response.ok) {
                    window.location.href = "/patient";
                }
            }).catch(function (error) {
                console.log('Error', error)
            });
        } else {
            fetch('/api/patient', {
                headers: new Headers({
                    'Content-Type': 'application/json'
                }), method: 'POST', body: JSON.stringify(data)
            }).then(function (response) {
                if (response.ok) {
                    window.location.href = "/patient";
                }

            });
        }

    });


    $("#delete_button1").bind("click", function (event) {
        if ($("#userGroup").html() === "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
        } else {
            $("#lamisform").attr("action", "Patient_delete");
        }
        return true;
    });

    $("#date5").bind("change", function (event) {
        if ($("#date5").val().length != 0) {
            var dateString = $("#date5").val().slice(3, 5) + "/" + $("#date5").val().slice(0, 2) + "/" + $("#date5").val().slice(6);
            var date = new Date(dateString);
            date.setMonth(date.getMonth() + 9); // add nine months 
            date.setDate(date.getDate() + 7); // add seven days
            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1);
            $("#date6").val("" + day + "/" + month + "/" + date.getFullYear());

            var second = 1000, minute = second * 60, hour = minute * 60, day = hour * 24, week = day * 7;
            var weeks = (new Date().getTime() - new Date(dateString).getTime()) / week;
            var weekString = weeks.toString().substring(0, 2);
            $("#gestationalAge").val(weekString);
        }
    });

    $("#parity").blur(function (event) {
        if ($("#parity").val().length !== 0) {
            if (parseInt($("#parity").val()) < 0 || parseInt($("#parity").val()) > 20) {
                $("#parity").val("");
                var message = "Parity ranges from 0 to 20";
                $("#messageBar").html(message).slideDown('slow');
            } else {
                $("#messageBar").slideUp('fast');
            }
        }
    });

    $("#gravida").blur(function (event) {
        if ($("#gravida").val().lenth !== 0) {
            if (parseInt($("#gravida").val()) < 1 || parseInt($("#gravida").val()) > 40) {
                $("#gravida").val("");
                var message = "Gravida ranges from 1 to 40";
                $("#messageBar").html(message).slideDown('slow');
            } else {
                $("#messageBar").slideUp('fast');
            }
        }
    });
}

function checkPmtctRequirements(forAge) {
    if ($("#entryPoint").val() === "PMTCT") {
        if ($("#gender").val() === "Female") {

            if (forAge) {
                if ($("#age").val() !== "" && $("#age").val() > 10) {
                    $("#messageBar").slideUp('slow');
                    $("#ancNo").slideDown('fast');
                    $("#pmtct").slideDown('fast');
                    $("#lm").slideDown('fast');
                    $("#gravid").slideDown('fast');
                    $("#tdAncNo").slideDown('fast');
                    $("#tdAncNo1").slideDown('fast');
                } else {
                    var message = "Cannot place patient with unknown age or a minor on PMTCT";
                    $("#messageBar").html(message).slideDown('slow');
                    $("#ancNo").slideUp('fast');
                    $("#tdAncNo").slideUp('fast');
                    $("#tdAncNo1").slideUp('fast');
                    $("#pmtct").slideUp('fast');
                    $("#lm").slideUp('fast');
                    $("#gravid").slideUp('fast');
                    $("#entryPoint").val("");
                    $("#age").val("");
                    $("#ageUnit").val("");
                    $("#date1").val("");
                }
            } else {
                $("#messageBar").slideUp('slow');
                $("#ancNo").slideDown('fast');
                $("#pmtct").slideDown('fast');
                $("#lm").slideDown('fast');
                $("#gravid").slideDown('fast');
                $("#tdAncNo").slideDown('fast');
                $("#tdAncNo1").slideDown('fast');
            }
        } else {
            var message = "Please specify a female gender for PMTCT";
            $("#messageBar").html(message).slideDown('slow');
            $("#ancNo").slideUp('fast');
            $("#pmtct").slideUp('fast');
            $("#lm").slideUp('fast');
            $("#gravid").slideUp('fast');
            $("#entryPoint").val("");
            $("#tdAncNo").slideUp('fast');
            $("#tdAncNo1").slideUp('fast');
        }
    } else {
        $("#messageBar").slideUp('slow');
        $("#ancNo").slideUp('fast');
        $("#pmtct").slideUp('fast');
        $("#lm").slideUp('fast');
        $("#gravid").slideUp('fast');
        $("#tdAncNo").slideUp('fast');
        $("#tdAncNo1").slideUp('fast');
    }
}

function retrieveLga(obj) {
    $.ajax({
        url: "Lga_retrieve.action",
        dataType: "json",
        data: {state: obj.state},
        success: function (lgaMap) {
            var options = "<option value = '" + '' + "'>" + '' + "</option>";
            $.each(lgaMap, function (key, value) {
                options += "<option value = '" + key + "'>" + value + "</option>";
            }); //end each
            $("#lga").html(options);
            $("#lga").val(obj.lga);
            $("#state").val(obj.state);
        }
    }); //end of ajax call            
}

function findPatientByNumber() {
    $.ajax({
        url: "Patient_find_number.action",
        dataType: "json",
        data: {hospitalNum: $("#hospitalNum").val()},

        success: function (patientList) {
            populateForm(patientList);
        }
    }); //end of ajax call                        
}

function findPatientByNames() {
    if ($("#surname").val().length !== 0 && $("#otherNames").val().length !== 0 && $("#gender").val().length !== 0 && $("#date1").val().length !== 0) {
        $("#date1").datepicker("option", "altField", "#dateBirth");
        $("#date1").datepicker("option", "altFormat", "mm/dd/yy");

        $.ajax({
            url: "Patient_find_names.action",
            dataType: "json",
            data: {
                surname: $("#surname").val(),
                otherNames: $("#otherNames").val(),
                dateBirth: $("#dateBirth").val(),
                gender: $("#gender").val()
            },

            success: function (messageList) {
                if ($.isEmptyObject(messageList)) { // check if the return json object is empty ie no patient found
                    $("#messageBar").slideUp('slow');
                } else {
                    var message = "A " + messageList[0].gender.toLowerCase() + " patient named " + messageList[0].surname + " " + messageList[0].otherNames + " age " + messageList[0].age + " already registered - address: " + messageList[0].address;
                    $("#messageBar").html(message).slideDown('slow');
                }
            }
        }); //end of ajax call                            
    }
}

function populateForm(patientList) {
    if ($.isEmptyObject(patientList)) { // check if the return json object is empty ie no patient found
        updateRecord = false;
        resetButtons();
        $("#id").val("");
        $("#uniqueId").val("");
        $("#surname").val("");
        $("#otherNames").val("");
        $("#gender").val("");
        $("#dateBirth").val("");
        $("#date1").val("");
        $("#age").val("");
        $("#ageUnit").val("");
        $("#maritalStatus").val("");
        $("#address").val("");
        $("#phone").val("");
        $("#education").val("");
        $("#occupation").val("");
        $("#state").val("");
        $("#lga").val("");
        $("#statusRegistration").val("");
        $("#dateRegistration").val("");
        $("#date2").val("");
        $("#currentStatus").val("");
        $("#dateCurrentStatus").val("");
        $("#nextKin").val("");
        $("#relationKin").val("");
        $("#addressKin").val("");
        $("#phoneKin").val("");
        $("#entryPoint").val("");
        $("#dateConfirmedHiv").val("");
        $("#date3").val("");
        $("#pregnancyStatus").val();
//        $("#breastfeeding").removeAttr("checked");
        $("#tbStatus").val("");
        //Set the PMTCT fields as empty..
        $("#timeHivDiagnosis").val("");
        $("#gestationalAge").val("");
        $("#lmp").val("");
        $("#date4").val("");
        $("#edd").val("");
        $("#date5").val("");
        $("#dateEnrolledPmtct").val("");
        $("#date6").val("");
        $("#gravida").val("");
        $("#parity").val("");
        $("#ancNum").val("");
        $("#sourceReferral").val("");
        $("#changeNumberLink").html("");
    } else {
        updateRecord = true;
        initButtonsForModify();
        $("#id").val(patientList[0].patientId);
        $("#hospitalNum").val(patientList[0].hospitalNum);
        $("#uniqueId").val(patientList[0].uniqueId);
        $("#surname").val(patientList[0].surname);
        $("#otherNames").val(patientList[0].otherNames);
        $("#gender").val(patientList[0].gender);
        $("#dateBirth").val(patientList[0].dateBirth);
        $("#date1").val(dateSlice(patientList[0].dateBirth));
        $("#age").val(patientList[0].age);
        $("#ageUnit").val(patientList[0].ageUnit);
        $("#maritalStatus").val(patientList[0].maritalStatus);
        $("#address").val(patientList[0].address);
        $("#phone").val(patientList[0].phone);
        $("#education").val(patientList[0].education);
        $("#occupation").val(patientList[0].occupation);
        $("#state").val(patientList[0].state);
        $("#lga").val(patientList[0].lga);
        $("#statusRegistration").val(patientList[0].statusRegistration);
        $("#dateRegistration").val(patientList[0].dateRegistration);
        $("#date2").val(dateSlice(patientList[0].dateRegistration));
        date = patientList[0].dateConfirmedHiv;
        $("#currentStatus").val(patientList[0].currentStatus);
        $("#dateCurrentStatus").val(patientList[0].dateCurrentStatus);
        $("#tbStatus").val(patientList[0].tbStatus);
        $("#nextKin").val(patientList[0].nextKin);
        $("#relationKin").val(patientList[0].relationKin);
        $("#addressKin").val(patientList[0].addressKin);
        $("#phoneKin").val(patientList[0].phoneKin);
        $("#entryPoint").val(patientList[0].entryPoint);
        if (patientList[0].entryPoint === "PMTCT") {
            $("#ancNo").show();
            $("#pmtct").show();
            $("#timeHivDiagnosis").val(patientList[0].timeHivDiagnosis);
            $("#dateEnrolledPmtct").val(patientList[0].dateEnrolledPmtct);
            date = patientList[0].dateEnrolledPmtct;
            $("#date4").val(dateSlice(date));
            $("#sourceReferral").val(patientList[0].sourceReferral);
        }
        $("#dateConfirmedHiv").val(patientList[0].dateConfirmedHiv);
        date = patientList[0].dateConfirmedHiv;
        $("#date3").val(dateSlice(date));
        $("#tbStatus").val(patientList[0].tbStatus);
        if (patientList[0].gender === "Female") {
            if (patientList[0].pregnant === "1") {
                $("#pregnancyStatus").val("2");
            } else if (patientList[0].breastfeeding === "1") {
                $("#pregnancyStatus").val("3");
            } else if (patientList[0].pregnant === "0" && patientList[0].breastfeeding === "0") {
                $("#pregnancyStatus").val("1");
            }
            $("#pregnancyStatus").removeAttr("disabled");
        } else {
            $("#pregnancyStatus").attr("disabled", "disabled");
        }
        $("#changeNumberLink").html("Change hospital number");
        obj.state = patientList[0].state;
        obj.lga = patientList[0].lga;
        retrieveLga(obj);
    }
}

function retrieveLastAnc(patientId) {
    $.ajax({
        url: "Anc_retrieve_last.action?id=" + patientId,
        dataType: "json",
        success: function (ancLast) {
            // set the ANC static values...
            console.log("Last Anc is:", ancLast);
            if (typeof ancLast !== "undefined") {
                if (ancLast.length > 0) {
                    //Use it to Populate the ANC fields...
                    $("#timeHivDiagnosis").val(ancLast[0].timeHivDiagnosis);
                    $("#gestationalAge").val(ancLast[0].gestationalAge);
                    $("#sourceReferral").val(ancLast[0].sourceReferral);
                    $("#gravida").val(ancLast[0].gravida);
                    $("#parity").val(ancLast[0].parity);
                    $("#ancNum").val(ancLast[0].ancNum);
                    $("#dateEnrolledPmtct").val(ancLast[0].dateEnrolledPmtct);
                    date = ancLast[0].dateEnrolledPmtct;
                    $("#date4").val(dateSlice(date));
                    $("#lmp").val(ancLast[0].lmp);
                    date = ancLast[0].lmp;
                    $("#date5").val(dateSlice(date));
                    $("#edd").val(ancLast[0].edd);
                    date = ancLast[0].edd;
                    $("#date6").val(dateSlice(date));
                }
            }
        }
    });
}

function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;

    $("#date1").datepicker("option", "altField", "#dateBirth");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#dateRegistration");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date3").datepicker("option", "altField", "#dateConfirmedHiv");
    $("#date3").datepicker("option", "altFormat", "mm/dd/yy");

    // check if patient number is entered
    if ($("#hospitalNum").val().length === 0) {
        $("#numHelp").html(" *");
        validate = false;
    } else {
        $("#numHelp").html("");
    }

    // check is surname is entered
    if ($("#surname").val().length === 0) {
        $("#surnameHelp").html(" *");
        validate = false;
    } else {
        $("#surnameHelp").html("");
    }

    // check is age is entered
    if ($("#age").val().length === 0 || $("#ageUnit").val().length === 0) {
        $("#ageHelp").html(" *");
        validate = false;
    } else {
        if (parseInt(compare($("#date1").val(), $("#date2").val())) === -1) {
            var message = "Date of registration cannot be ealier than patient's date of birth";
            $("#messageBar").html(message).slideDown('slow');
            $("#ageHelp").html(" *");
            validate = false;
        } else if ($("#age").val() > 120 || $("#age").val() <= 0) {
            var message = "Date of birth cannot be greater than 120 years or less than zero (0) years";
            $("#messageBar").html(message).slideDown('slow');
            $("#ageHelp").html(" *");
            validate = false;
        } else {
            $("#messageBar").slideUp('slow');
            $("#ageHelp").html("");
        }
    }

    // check is gender is entered
    if ($("#gender").val().length === 0) {
        $("#genderHelp").html(" *");
        validate = false;
    } else {
        $("#genderHelp").html("");
    }

    // check is status at registration is entered
    if ($("#statusRegistration").val().length === 0) {
        $("#statusregHelp").html(" *");
        validate = false;
    } else {
        $("#statusregHelp").html("");
        if (!updateRecord) {
            if ($("#currentStatus").val().length === 0) {
                var status = $("#statusRegistration").val();
                $("#currentStatus").val(status);
            }
        }
    }

    // check is date of registration is entered
    if ($("#dateRegistration").val().length === 0) {
        $("#dateregHelp").html(" *");
        validate = false;
    } else {
        $("#dateregHelp").html("");
        if (!updateRecord) {
            if ($("#dateCurrentStatus").val().length === 0) {
                var date = $("#dateRegistration").val();
                $("#dateCurrentStatus").val(date);
            }
        }
    }

    //Validate for PMTCT Values
    if ($("#entryPoint").val() === "PMTCT") {
        if ($("#gender").val() === "Female") {
            $("#date4").datepicker("option", "altField", "#dateEnrolledPmtct");
            $("#date4").datepicker("option", "altFormat", "mm/dd/yy");
            // check if date of enrollment into pmtct is entered
            if ($("#dateEnrolledPmtct").val().length === 0 || !regex.test($("#dateEnrolledPmtct").val())) {
                $("#datepmtctHelp").html(" *");
                validate = false;
            } else {
                $("#datepmtctHelp").html("");
            }
            //Check the Pregnancy Status
            if ($("#pregnancyStatus").val() === "" || $("#pregnancyStatus").val() === "1") {
                var message = "Female Enrolled into PMTCT must be either pregnant or breastfeeding";
                $("#messageBar").html(message).slideDown('slow');
                $("#pregHelp").html(" *");
                validate = false;
            } else {
                $("#messageBar").slideUp('slow');
                $("#pregHelp").html("");
            }
        } else {
            //Clear all PMTCT values...
            clearAllPmtctValues();
        }
    } else {
        //Clear all PMTCT Values
        clearAllPmtctValues();
    }

    return validate;
}

function clearAllPmtctValues() {
    $("#dateEnrolledPmtct").val("");
    $("#timeHivDiagnosis").val("");
    $("#sourceReferral").val("");
}

function calculateDateOfBirth(age, option) {
    startdate = $("#date2").val();
    var new_date = moment(startdate, "DD/MM/YYYY").subtract(age, option);
    var day = new_date.format('DD');
    var month = new_date.format('MM');
    var year = new_date.format('YYYY');
    var dateOfBirth = day + '/' + month + '/' + year;
    $("#date1").val(dateOfBirth);
}

function clearAge() {
    $("#date1").val("");
    $("#age").val("");
    $("#ageUnit").val("");
}

function calculateAge() {
    if ($("#date1").val().length !== 0 && $("#date2").val().length !== 0) {
        if (parseInt(compare($("#date1").val(), $("#date2").val())) === -1) {
            var message = "Date of registration cannot be ealier than patient's date of birth";
            $("#messageBar").html(message).slideDown('slow');
            clearAge();
        } else {
            $("#messageBar").slideUp('slow');
            var diff = dateDiff($("#date1").val(), $("#date2").val(), "years");
            if (parseInt(diff) < 1) {
                diff = dateDiff($("#date1").val(), $("#date2").val(), "months");
                if (diff < 1) diff = 1;
                $("#ageUnit").val("month(s)");
            } else if (parseInt(diff) === 1) {
                diff = dateDiff($("#date1").val(), $("#date2").val(), "months");
                if (diff === 12) {
                    diff = 1;
                    $("#ageUnit").val("year(s)");
                } else {
                    $("#ageUnit").val("month(s)");
                }
            } else {
                $("#ageUnit").val("year(s)");
            }
            $("#age").val(diff);
            checkPmtctRequirements(true);
        }
    }
}

function createPatientGrid() {
    $("#grid").jqGrid({
        url: "/api/patient/grid",
        datatype: "json",
        mtype: "GET",
        colNames: ["Hospital No", "Biometric", "Name", "Gender", "ART Status", "Address", "Date Modified", "Action"],
        colModel: [
            {name: "hospitalNum", index: "hospitalNum", width: "150"},
            {name: "biometric", index: "biometric", width: "100", formatter: imageFormat},
            {name: "name", index: "name", width: "220"},
            {name: "gender", index: "gender", width: "100"},
            {name: "currentStatus", index: "currentStatus", width: "150"},
            {name: "address", index: "address", width: "250"},
            {
                name: "timeStamp",
                index: "timeStamp",
                width: "150",
                formatter: "date",
                formatoptions: {srcformat: "m/d/Y", newformat: "d-M-Y"}
            },
            {
                name: "patientId",
                index: "patientId",
                width: "100",
                classes: "table_dropdown",
                formatter: function (cellValue, options, rowObject) {
                    return '<div id="table-btn" class="dropdown" style="postion: absolute; color: #000;"><button  class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action</button><div class="dropdown-menu dropdown-menu-right"><a class="dropdown-item" href="#" onclick="editButton(' + cellValue + ');">Edit</a><a class="dropdown-item" href="#" onclick="deleteButton(' + cellValue + ');">Delete</a></div></div>';
                }
            },
        ],
        pager: $('#pager'),
        rowNum: 100,
        sortname: "timeStamp",
        sortorder: "desc",
        viewrecords: true,
        imgpath: "assets/jqueryui/jqgrid/img",
        resizable: false,
        height: 350,
        jsonReader: {
            root: "patientList",
            page: "currpage",
            total: "totalpages",
            records: "totalrecords",
            repeatitems: false,
            id: "patientId"
        },
        onSelectRow: function (id) {
            $("#id").val(id);
            var data = $("#grid").getRowData(id);
            $("#hospitalNum").val(data.hospitalNum);
        },
        ondblClickRow: function (id) {
            window.location.href = "/patient/" + id;
        }
    });
}

function anc() {
    $("#timeHivDiagnosis").change(function (event) {
        var timeHivDiagnosis = $("#timeHivDiagnosis").val();
        if (timeHivDiagnosis === "Labour") {
            $("#lm").slideUp('fast');
            $("#gravid").slideUp('fast');
            $("#tdAncNo").slideUp('fast');
            $("#tdAncNo1").slideUp('fast');
            nullifyValues();
        } else if (timeHivDiagnosis.includes("Post Partum")) {
            $("#lm").slideUp('fast');
            $("#gravid").slideUp('fast');
            $("#tdAncNo").slideUp('fast');
            $("#tdAncNo1").slideUp('fast');
            nullifyValues();
        } else {
            $("#lm").slideDown('fast');
            $("#gravid").slideDown('fast');
            $("#tdAncNo").slideDown('fast');
            $("#tdAncNo1").slideDown('fast');
        }
    });
}
function nullifyValues() {
    $("#lmp").val("");
    $("#edd").val("");
    $("#gravida").val("");
    $("#ancNum").val("");
    $("#date5").val("");
    $("#date6").val("");
    $("#parity").val("");
    $("#gestationalAge").val("");
}

function changeNumber() {
    if($("#newHospitalNum").val().length !== 0) {
        var newHospitalNum = zerorize($("#newHospitalNum").val());
        $.ajax({
            url: "/api/patient/update-hospital-number?hospitalNum=" + $("#hospitalNum").val() + "&newHospitalNum=" + newHospitalNum + "&facilityId=" + $('#facilityId').val(),
            method : "GET",
            dataType: "json",
            //data: {patientId: $("#patientId").val(), hospitalNum: $("#hospitalNum").val(), newHospitalNum: newHospitalNum},
            success: function(messageList) {
                if(messageList[0].message === "Updated") {
                    $("#messageBar").html("Hospital number changed").slideDown('slow');
                    $("#hospitalNum").val(newHospitalNum);
                }
                else {
                    $("#messageBar").html("Hospital number already exist").slideDown('slow');
                }
            }
        });
        $("#dialog").dialog("close");
    }
}

