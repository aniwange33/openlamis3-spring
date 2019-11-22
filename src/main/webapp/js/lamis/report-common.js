/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function reports() {
    for(i = new Date().getFullYear(); i > 1900; i--) {
        $("#reportingYear").append($("<option/>").val(i).html(i));
        $("#reportingYearBegin").append($("<option/>").val(i).html(i));
        $("#reportingYearEnd").append($("<option/>").val(i).html(i));
    }
    $("#patientlist").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Patient_list");
    });    
    $("#currentcare").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Current_care");
    });
    $("#currenttreatment").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Current_treatment");
    });
    $("#defaulterlist").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Defaulter_list");
    });
    $("#defaulterRefill").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Defaulter_refill");
    });
    $("#lostUnconfirmed").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Lost_unconfirmed");
    });
    $("#coinfected").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Co_infection");
    });
    $("#cd4due").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Cd4_due");
    });
    $("#cd4baseline").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Cd4_baseline");
    });
    $("#Unassigned_list").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Unassigned_list");
    });
    $("#viralloaddue").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Viralload_due");
    });    
    $("#viralloadsupressed").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Viralload_supressed");
    });    
    $("#viralloadunsupressed").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Viralload_unsupressed");
    });    
    $("#eligibleart").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Eligible_art");
    });
    $("#firstline").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("First_line");
    });
    $("#secondline").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Second_line");
    });
    $("#thirdline").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Third");
    });
    $("#regimensummary").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Regimen_summary");
    });
    $("#devolvedsummary").click(function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.open("Devolved_summary");
    });
}
