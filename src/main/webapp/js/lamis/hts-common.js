/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function initialize() {
    var obj = {};
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


    $("#age").mask("9?99", {placeholder: " "});

    $("#grid").jqGrid({
        url: "/api/hts/grid",
        datatype: "json",
        mtype: "GET",
        colNames: ["Client Code", "Client Name", "Gender", "Date Tested", "HIV Test Result", "Referred to ART", "Referred to TB", "Referred to STI", "Action", ""],
        colModel: [
            {name: "clientCode", index: "clientCode", width: "100"},
            {name: "name", index: "name", width: "200"},
            {name: "gender", index: "gender", width: "100"},
            {
                name: "dateVisit",
                index: "date1",
                width: "100",
                formatter: "date",
                formatoptions: {srcformat: "m/d/Y", newformat: "d/m/Y"}
            },
            {name: "hivTestResult", index: "hivTestResult", width: "120"},
            {name: "artReferred", index: "artReferred", width: "125"},
            {name: "tbReferred", index: "tbReferred", width: "125"},
            {name: "stiReferred", index: "stiReferred", width: "138"},
            {
                name: "htsId",
                index: "htsId",
                width: "100",
                classes: "table_dropdown",
                formatter: function (cellValue, options, rowObject) {
                    return '<div id="table-btn" class="dropdown" style="postion: absolute; color: #000;"><button  class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action</button><div class="dropdown-menu dropdown-menu-right"><a class="dropdown-item" href="#" onclick="editButton(' + cellValue + ');">Edit</a><a class="dropdown-item" href="#" onclick="deleteButton(' + cellValue + ');">Delete</a></div></div>';
                }
            },
            {name: "dateVisit", index: "dateVisit", width: "138", hidden: true},
        ],

        pager: $('#pager'),
        rowNum: 100,
        sortname: "htsId",
        sortorder: "desc",
        viewrecords: true,
        imgpath: "themes/basic/images",
        resizable: false,
        height: 350,
        jsonReader: {
            root: "htsList",
            page: "currpage",
            total: "totalpages",
            records: "totalrecords",
            repeatitems: false,
            id: "htsId"
        },


        afterInsertRow: function (id, data) {
            //$(this).jqGrid('setRowData', id, false, {fontFamily: 'Arial, Helvetica, sans-serif'});
            if (data.hivTestResult == "Positive") {
                $(this).jqGrid('setRowData', id, false, {color: 'red'});
            }
        },
        onSelectRow: function (id) {
            $("#htsId").val(id);
            var data = $("#grid").getRowData(id);
            $("#clientCode").val(data.clientCode);
            $("#name").val(data.name);
        },
        ondblClickRow: function (id) {
            $("#htsId").val(id);
            var data = $("#grid").getRowData(id);
            $("#clientCode").val(data.clientCode);
            $("#name").val(data.name);
            $("#dateVisit").val(data.dateVisit);
            $("#lamisform").attr("action", "/api/hts/" + id);
            $("#lamisform").submit();
        }
    }); //end of jqGrid


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
            url: "/api/state/lga/name/" + name,
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


    $("#clientCode").bind("keypress", function (event) {
        if ($("#clientCode").val().length != 0) {
            if (event.which === 13 || event.keyCode === 13 || event.which === 9 || event.keyCode === 9) {
                event.preventDefault();
                //findHtsByNumber();
            }
        }
    });

    $("#clientCode").bind("blur", function (event) {
        if ($("#clientCode").val().length != 0) {
            //findHtsByNumber();
        }
    });

    $("#save_button").bind("click", function (event) {

        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
            return true;
        } else {
            if (validateForm()) {
                if (updateRecord) {
                    $("#lamisform").attr("action", "/api/hts");
                } else {
                    $("#lamisform").attr("action", "/api/hts");
                }

            } else {
                return false;
            }
        }
    });

    $("#delete_button").bind("click", function (event) {
        if ($("#userGroup").html() == "Data Analyst") {
            $("#lamisform").attr("action", "Error_message");
        } else {
            $("#lamisform").attr("action", "Hts_delete");
                fetch(apiUrl + "/api/hts/delete/" + $("#htsId"), {
                    method: 'DELETE'
                });

        }
        return true;
    });


}

function findHtsByNumber() {
    $.ajax({
        url: "/api/client-code/clienCode=" + $("#clientCode").val(),
        dataType: "json",
        success: function (htsList) {
            populateForm(htsList);
        }
    }); //end of ajax call                        
}

function populateForm(htsList) {

    if ($.isEmptyObject(htsList)) { // check if the return json object is empty ie no patient found
        updateRecord = false;
        resetButtons();
        $("#clientCode").val($("#clientSession").html());
        $("#assessmentId").val($("#assessmentIdSession").html());
        $("#htsId").val("");
        $("#dateVisit").val("");
        $("#date1").val("");
        $("#firstTimeVisit").val("");
        $("#testingSetting").val("");

        $("#referredFrom").val("");
        $("#surname").val("");
        $("#otherNames").val("");
        $("#gender").val("");
        $("#dateBirth").val("");
        $("#date2").val("");
        $("#age").val("");
        $("#ageUnit").val("");
        $("#maritalStatus").val("");
        $("#numChildren").val("");
        $("#numWives").val("");
        $("#address").val("");
        $("#phone").val("");

        $("#stateId").val("");
        $("#lgaId").val("");
        $("#state").val("");
        $("#lga").val("");
        $("#typeCounseling").val("");
        $("#indexClient").val("");
        $("#typeIndex").val("");
        $("#indexClientCode").val("");
        $("#knowledgeAssessment1").val("");
        $("#knowledgeAssessment2").val("");
        $("#knowledgeAssessment3").val("");
        $("#knowledgeAssessment4").val("");
        $("#knowledgeAssessment5").val("");
        $("#knowledgeAssessment6").val("");
        $("#knowledgeAssessment7").val("");

        $("#riskAssessment1").val("");
        $("#riskAssessment2").val("");
        $("#riskAssessment3").val("");
        $("#riskAssessment4").val("");
        $("#riskAssessment5").val("");
        $("#riskAssessment6").val("");

        $("#tbScreening1").val("");
        $("#tbScreening2").val("");
        $("#tbScreening3").val("");
        $("#tbScreening4").val("");

        $("#stiScreening1").val("");
        $("#stiScreening2").val("");
        $("#stiScreening3").val("");
        $("#stiScreening4").val("");
        $("#stiScreening5").val("");

        $("#hivTestResult").val("");
        $("#testedHiv").val("");
        $("#postTest1").val("");
        $("#postTest2").val("");
        $("#postTest3").val("");
        $("#postTest4").val("");
        $("#postTest5").val("");
        $("#postTest6").val("");
        $("#postTest7").val("");
        $("#postTest8").val("");
        $("#postTest9").val("");
        $("#postTest10").val("");
        $("#postTest11").val("");
        $("#postTest12").val("");
        $("#postTest13").val("");
        $("#postTest14").val("");


        $("#syphilisTestResult").val("");
        $("#hepatitisbTestResult").val("");
        $("#hepatitiscTestResult").val("");

    } else {
        updateRecord = true;
        initButtonsForModify();


        $("#htsId").val(htsList[0].htsId);
        $("#assessmentId").val(htsList[0].assessmentId);
        $("#clientCode").val(htsList[0].clientCode);
        $("#dateVisit").val(htsList[0].dateVisit);

        date = htsList[0].dateVisit;
        $("#date1").val(dateSlice(date));

        $("#firstTimeVisit").val(htsList[0].firstTimeVisit);
        $("#testingSetting").val(htsList[0].testingSetting);
        $("#referredFrom").val(htsList[0].referredFrom);
        $("#surname").val(htsList[0].surname);
        $("#otherNames").val(htsList[0].otherNames);
        $("#gender").val(htsList[0].gender);
        $("#dateBirth").val(htsList[0].dateBirth);
        date = htsList[0].dateBirth;

        $("#date2").val(dateSlice(date));
        $("#age").val(htsList[0].age);
        $("#ageUnit").val(htsList[0].ageUnit);
        $("#maritalStatus").val(htsList[0].maritalStatus);
        $("#numChildren").val(htsList[0].numChildren);
        $("#numWives").val(htsList[0].numWives);
        $("#address").val(htsList[0].address);
        $("#phone").val(htsList[0].phone);

        $("#stateId").val(htsList[0].stateId);
        $("#lgaId").val(htsList[0].lgaId);
        $("#state").val(htsList[0].state);
        $("#lga").val(htsList[0].lga);
        $("#typeCounseling").val(htsList[0].typeCounseling);
        $("#indexClient").val(htsList[0].indexClient);
        $("#typeIndex").val(htsList[0].typeIndex);

        $("#indexClientCode").val(htsList[0].indexClientCode);

        $("#knowledgeAssessment1").val(htsList[0].knowledgeAssessment1);
        $("#knowledgeAssessment2").val(htsList[0].knowledgeAssessment2);
        $("#knowledgeAssessment3").val(htsList[0].knowledgeAssessment3);
        $("#knowledgeAssessment4").val(htsList[0].knowledgeAssessment4);
        $("#knowledgeAssessment5").val(htsList[0].knowledgeAssessment5);
        $("#knowledgeAssessment6").val(htsList[0].knowledgeAssessment6);
        $("#knowledgeAssessment7").val(htsList[0].knowledgeAssessment7);

        $("#riskAssessment1").val(htsList[0].riskAssessment1);
        $("#riskAssessment2").val(htsList[0].riskAssessment2);
        $("#riskAssessment3").val(htsList[0].riskAssessment3);
        $("#riskAssessment4").val(htsList[0].riskAssessment4);
        $("#riskAssessment5").val(htsList[0].riskAssessment5);
        $("#riskAssessment6").val(htsList[0].riskAssessment6);

        $("#tbScreening1").val(htsList[0].tbScreening1);
        $("#tbScreening2").val(htsList[0].tbScreening2);
        $("#tbScreening3").val(htsList[0].tbScreening3);
        $("#tbScreening4").val(htsList[0].tbScreening4);

        $("#stiScreening1").val(htsList[0].stiScreening1);
        $("#stiScreening2").val(htsList[0].stiScreening2);
        $("#stiScreening3").val(htsList[0].stiScreening3);
        $("#stiScreening4").val(htsList[0].stiScreening4);
        $("#stiScreening5").val(htsList[0].stiScreening5);

        $("#hivTestResult").val(htsList[0].hivTestResult);
        $("#testedHiv").val(htsList[0].testedHiv);
        $("#postTest1").val(htsList[0].postTest1);
        $("#postTest2").val(htsList[0].postTest2);
        $("#postTest3").val(htsList[0].postTest3);
        $("#postTest4").val(htsList[0].postTest4);
        $("#postTest5").val(htsList[0].postTest5);
        $("#postTest6").val(htsList[0].postTest6);
        $("#postTest7").val(htsList[0].postTest7);
        $("#postTest8").val(htsList[0].postTest8);
        $("#postTest9").val(htsList[0].postTest9);
        $("#postTest10").val(htsList[0].postTest10);
        $("#postTest11").val(htsList[0].postTest11);
        $("#postTest12").val(htsList[0].postTest12);
        $("#postTest13").val(htsList[0].postTest13);
        $("#postTest14").val(htsList[0].postTest14);


        $("#syphilisTestResult").val(htsList[0].syphilisTestResult);
        $("#hepatitisbTestResult").val(htsList[0].hepatitisbTestResult);
        $("#hepatitiscTestResult").val(htsList[0].hepatitiscTestResult);

        obj.stateId = htsList[0].stateId;
        obj.lgaId = htsList[0].lgaId;
        retrieveLga(obj);

    }


}


function validateForm() {
    var regex = /^\d{2}\/\d{2}\/\d{4}$/;
    var validate = true;
    $("#date1").datepicker("option", "altField", "#dateVisit");
    $("#date1").datepicker("option", "altFormat", "mm/dd/yy");
    $("#date2").datepicker("option", "altField", "#dateBirth");
    $("#date2").datepicker("option", "altFormat", "mm/dd/yy");
    $("#state").val($("#stateId option:selected").text());
    $("#lga").val($("#lgaId option:selected").text())
    // check if patient number is entered
    if ($("#clientCode").val().length == 0) {
        $("#numHelp").html(" *");
        validate = false;
    } else {
        $("#numHelp").html("");
    }

    // check is surname is entered
    if ($("#surname").val().length == 0) {
        $("#surnameHelp").html(" *");
        validate = false;
    } else {
        $("#surnameHelp").html("");
    }
//
    // check is age is entered
    if ($("#age").val().length == 0 || $("#ageUnit").val().length == 0) {
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

function calculateDateOfBirth(age, option) {

    startdate = $("#date1").val();
    var new_date = moment(startdate, "DD/MM/YYYY").subtract(age, option);

    var day = new_date.format('DD');
    var month = new_date.format('MM');
    var year = new_date.format('YYYY');

    var dateOfBirth = day + '/' + month + '/' + year;
    $("#date2").val(dateOfBirth);


    $(document).ready(function () {
        $(".search").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#grid tr").filter(function () {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });

        $("#new_button").bind("click", function (event) {
            $("#lamisform").attr("action", "/hts/assessment/new");
            return true;
        });

        $("#register_button").bind("click", function (event) {
            window.location = '/hts/enrol/' + $("#htsId").val();
            return true;
        });

        $("#close_button").bind("click", function (event) {
            $("#lamisform").attr("action", "/hts");
            return true;
        });
    });

    function deleteButton(htsId) {
        $.confirm({
            title: 'Confirm!',
            content: 'Are you sure you want to delete?',
            buttons: {
                confirm: function () {
                    $("#id").val(htsId);
                    var data = $("#grid").getRowData(patId);
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name);
                    if ($("#userGroup").html() == "Data Analyst") {
                        $("#lamisform").attr("action", "Error_message");
                        $("#lamisform").submit();
                    } else {
                        fetch(apiUrl + "/api/hts/delete/" + $("#htsId"), {
                            method: 'DELETE'
                        }).then(() => {
                        //    console.log('removed');
                        });
                    }
                    window.location.replace("/hts");
                    return true;
                },
                cancel: function () {
                    console.log("cancel");
                }
            }
        });
    }

    function editButton(htsId) {
        $("#id").val(htsId);
        var data = $("#grid").getRowData(htsId);
        $("#hospitalNum").val(data.hospitalNum);
        $("#name").val(data.name);
        $("#lamisform").attr("action", "/api/hts/" + htsId);
        $("#lamisform").submit();
        //return true;
    }

}






