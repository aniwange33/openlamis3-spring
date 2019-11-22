<script type="text/javascript">
    $(document).ready(function () {

        //$("#headerText").html("Welcome "+$("#userId").val());
        var userGroup = $("#user_group").html();
        var $pharm = $("#pharmacy");
        var $lab = $("#lab");
        var $admin = $("#admin");
        var $clinic = $("#clinic");
        var $case = $("#case");
        var $home = $("#home");
        var $hackit = $("#hackit");
        var $patients = $("#patients");
        var $Newpatients = $("#Newpatients");
        var $case = $("#case");

        if (userGroup === "Administrator") {
            $("#dash").html('Administrator');
            $("#dash-label").html('Administrator');
            $("#breadcrumb").html('Administrator');
            $admin.show();
            disable($pharm);
            disable($lab);
            disable($clinic);
            disable($case);
            disable($home);
        } else if (userGroup === "Clinician") {
            $("#dash").html('Clinic');
            $("#dash-label").html('Clinic');
            $("#breadcrumb").html('Clinic');
            $clinic.show();
            disable($pharm);
            disable($lab);
            disable($admin);
            disable($case);
            disable($home);
        } else if (userGroup === "Pharmacist") {
            $("#dash").html('Pharmacy');
            $("#dash-label").html('Pharmacy');
            $("#breadcrumb").html('Pharmacy');
            $pharm.show();
            disable($clinic);
            disable($lab);
            disable($admin);
            disable($home);
        } else if (userGroup === "Laboratory Scientist") {
            $("#dash").html('Laboratory');
            $("#dash-label").html('Laboratory');
            $("#breadcrumb").html('Laboratory');
            $lab.show();
            disable($pharm);
            disable($clinic);
            disable($admin);
            disable($home);
        } else if (userGroup === "Case management") {
            $("#dash").html('Case management');
            $("#dash-label").html('Case management');
            $("#breadcrumb").html('Case management');
            $case.show();
            disable($clinic);
            disable($lab);
            disable($admin);
            disable($home);
        } else if (userGroup === "Patients") {
            $("#dash").html('Patients');
            $("#dash-label").html('Patients');
            $("#breadcrumb").html('Patients');
            $patients.show();
            disable($clinic);
            disable($lab);
            disable($admin);
            disable($case);
            disable($home);
        } else if (userGroup === "NewPatients") {
            $("#dash").html('NewPatients');
            $("#dash-label").html('NewPatients');
            $("#breadcrumb").html('NewPatients');
            $newpatients.show();
            disable($Patients);
            disable($lab);
            disable($admin);
            disable($case);
            disable($home);
            disable($pharm);
            disable($clinic);
        } else {
            $("#dash").html("Dashboard");
            $("#dash-label").html('Dashboard');
            $("#breadcrumb").html("Dashboard");
            $hackit.show();
            disable($pharm);
            disable($clinic);
            disable($lab);
            disable($admin);
            disable($home);
        }

        function disable(obj) {
            obj.hide();
            obj.css("pointer-events", "none");
            obj.css("opacity", "0.6");
        }
    });
</script>
<nav class="navbar navbar-expand-md navbar-dark bg-white">
    <a class="navbar-brand" href="#" id="dash-label">Dashboard</a>
    <button class="navbar-toggler text-white" type="button"
            data-toggle="collapse" data-target="#navbarText"
            aria-controls="navbarSupportedContent" aria-expanded="false"
            aria-label="Toggle navigation">
        <span class="navbar-toggler-bar navbar-kebab"></span>
        <span class="navbar-toggler-bar navbar-kebab"></span>
        <span class="navbar-toggler-bar navbar-kebab"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <div id="hackit" style="display:none;">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a href="#" class="nav-link text-center">
                        <i class="now-ui-icons design_app"></i>
                        Hello! how did you get here???
                    </a>
                </li>
            </ul>
        </div>
        <div id="pharmacy" style="display:none;">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a href="/Pharmacy_search" class="nav-link text-center">
                        <i class="now-ui-icons business_badge"></i>
                        <span data-i18n="drug-dispensing">Drug Dispensing</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Devolve_search" class="nav-link text-center">
                        <i class="now-ui-icons design_app"></i>
                        <span data-i18n="care-service">Differentiated Care</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Prescription_search" class="nav-link text-center">
                        <i class="now-ui-icons business_globe"></i>
                        <span data-i18n="drug-prescription">Drug Prescriptions</span>
                    </a>
                </li>
                <li class="nav-item dropdown pull-right">
                    <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="now-ui-icons education_paper"></i>
                        <span data-i18n="reports">Reports</span>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                        <s:a id="firstline" class="dropdown-item">Patients on First line Regimen</s:a>
                        <s:a id="secondline" class="dropdown-item">Patients on Second line Regimen</s:a>
                        <s:a id="thirdline" class="dropdown-item">Patients on Third Line Regimen (Salvage)</s:a>
                        <s:a id="regimensummary" class="dropdown-item">Patient Per Regimen Report</s:a>
                        <s:a action="Reporting_month_pharmacy.action?formId=1"
                             class="dropdown-item">Summary of Drugs Dispensed</s:a>
                        <s:a id="devolvedsummary" class="dropdown-item">ART Refill Devolvement Report</s:a>
                    </div>
                </li>
            </ul>
        </div>
        <div id="clinic" style="display:none;">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a href="/Anc_search" class="nav-link text-center">
                        <i class="now-ui-icons design_app"></i>
                        ANC Registration
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Delivery_search" class="nav-link text-center">
                        <i class="now-ui-icons files_single-copy-04"></i>
                        Labour/Delivery
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Maternalfollowup_search" class="nav-link text-center">
                        <i class="now-ui-icons business_chart-bar-32"></i>
                        ANC/PNC Visit
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Childfollowup_search" class="nav-link text-center">
                        <i class="now-ui-icons business_badge"></i>
                        Child Follow-Up
                    </a>
                </li>
                <li class="nav-item dropdown pull-right">
                    <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="now-ui-icons education_paper"></i>
                        Reports
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item" id="cd4due" href="/Reporting_month_pmtct?formId=1">PMTCT Monthly
                            Summary</a>
                        <a class="dropdown-item" id="cd4baseline" href="/Reporting_period_pmtct?formId=1">NIGERIA QUAL
                            Indicators</a>
                    </div>
                </li>
            </ul>
        </div>
        <!-- End of Clinic Menu -->
        <div id="lab" style="display:none;">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a href="/Laboratory_search" class="nav-link text-center">
                        <i class="now-ui-icons design_app"></i>
                        Laboratory Test
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Labtest_prescription_search" class="nav-link text-center">
                        <i class="now-ui-icons business_badge"></i>
                        Laboratory Test Orders
                    </a>
                </li>
                <li class="nav-item dropdown pull-right">
                    <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="now-ui-icons education_paper"></i>
                        Reports
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                        <a href="/Lab_query_option.action?formId=1" class="dropdown-item">Laboratory Result Query</a>
                        <a id="cd4due" href="/Lab_query_option.action" class="dropdown-item">Patients due for CD4 Count
                            Test</a>
                        <a id="cd4baseline" href="/Lab_query_option.action" class="dropdown-item">Patients with current
                            CD4 Count &le; baseline value</a>
                        <a href="/Viral_load.action" class="dropdown-item">Patients due for Viral Load Test</a>
                        <a id="viralloadsupressed" href="/Lab_query_option.action" class="dropdown-item">Patients with
                            current Viral Load &lt; 1000 copies/ml</a>
                        <a id="viralloadunsupressed" href="/Lab_query_option.action" class="dropdown-item">Patients with
                            current Viral Load &ge; 1000 copies/ml</a>
                        <a href="/Reporting_month_lab.action?formId=1" class="dropdown-item">LAB Monthly Summary</a>
                    </div>


                </li>
            </ul>
        </div>
        <!-- End of Laboratory Menu -->
        <div id="patients" style="display:none;">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a href="/Patient_find" class="nav-link text-center">
                        <i class="now-ui-icons design_app"></i>
                        <span data-i18n="Patient_search">Patients Registration</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Status_search.action" class="nav-link text-center">
                        <i class="now-ui-icons business_badge"></i>
                        <span data-i18n="drug-dispensing">Client Status Update</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Client_tracker.action" class="nav-link text-center">
                        <i class="now-ui-icons business_globe"></i>
                        <span data-i18n="drug-prescription">Clients Tracking</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="/Appointment_search.action" class="nav-link text-center">
                        <i class="now-ui-icons business_globe"></i>
                        <span data-i18n="drug-prescription">Appointment Schedulling</span>
                    </a>
                </li>
                <li class="nav-item dropdown pull-right">
                    <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="now-ui-icons education_paper"></i>
                        <span data-i18n="reports">Reports</span>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                        <a data-i18n="patient-on-first-line-regimen" class="dropdown-item" id="firstline"
                           href="/Patient_query_criteria.action">Patients Information Query</a>
                        <a data-i18n="patient-on-second-line-regimen" class="dropdown-item" id="secondline"
                           href="/Current_care">Currently on Care(ART & Pre-ART)</a>
                        <a data-i18n="patient-on-third-line-regimen-salvage" class="dropdown-item" id="thirdline"
                           href="/Current_treatment">Currently On Treatment(ART)</a>
                        <a data-i18n="patient-per-regime-report" class="dropdown-item" id="regimensummary"
                           href="/Lost_unconfirmed">Lost to Fellow Up Unconfirmed</a>
                        <a data-i18n="summary-of-drug-dispensed" class="dropdown-item"
                           href="/Appointment_option.action">Clinic & Refill Appointment/Visits</a>
                        <a data-i18n="summary-of-drug-dispensed" class="dropdown-item" href="/Defaulter_refill">Defaulters
                            for ARV Appoinment</a>
                        <a data-i18n="summary-of-drug-dispensed" class="dropdown-item"
                           href="/Reporting_month_patient.action?formId=1">ART Monthly Summary Form</a>
                        <a data-i18n="art-refill-devolvement-report" class="dropdown-item" id="devolvedsummary"
                           href="/Reporting_month_patient.action?formId=2">Performance Indicator</a>
                    </div>
                </li>
            </ul>
        </div>
        <!-- End of Patient nav -->
        <div id="admin" style="display:none;">
            <ul class="navbar-nav">
                <li class="nav-item dropdown pull-right">
                    <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="now-ui-icons education_paper"></i>
                        Setup
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item" href="/User_new">User Setup</a>
                        <a class="dropdown-item" href="/Facility_new">Facility Setup</a>
                        <a class="dropdown-item" id="cd4due" href="/Communitypharm_new">Community Pharmacy Setup</a>
                        <a class="dropdown-item" id="cd4baseline" href="/Casemanager_new">Case Manager Setup</a>
                        <a class="dropdown-item" href="/Drug_new">Drug Setup</a>
                        <a class="dropdown-item" id="viralloadsupressed" href="/Message_setup">Message/Modem
                            Settings</a>
                    </div>
                </li>
                <li class="nav-item dropdown switch-locale">
                    <a class="nav-link dropdown-toggle switch-locale" href="#" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        Visualizer</a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdown09">
                        <a class="dropdown-item" href="Dashboard_page">Dashboard</a>
                        <a class="dropdown-item" href="Chart_period2.action">Performance Chart</a>
                    </div>
                </li>
                <li class="nav-item dropdown switch-locale">
                    <a class="nav-link dropdown-toggle switch-locale" href="#" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        Data Maintenance</a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdown09">
                        <a class="dropdown-item" href="Export_page">Export Data</a>
                        <a class="dropdown-item" href="Import_page">Import Data</a>
                        <a class="dropdown-item" href="Upload_page">Upload Data to the Server</a>
                        <a class="dropdown-item" href="Sync_page">Download &amp; Synchronize Data</a>
                        <a class="dropdown-item" href="Updates_page">Check &amp; Download Updates</a>
                        <a class="dropdown-item" href="Radet_page">RADET Analyzer &amp; Data Update</a>
                        <a class="dropdown-item" href="Dqa_page">Data Quality Analyzer</a>
                        <a class="dropdown-item" href="Cleanup_page">Cleanup Database Records</a>
                        <a class="dropdown-item" href="Deduplicator_page">Remove Duplicate Numbers</a>
                        <a class="dropdown-item" href="Facility_switch">Switch Facility</a>
                    </div>
                </li>
                <li class="nav-item dropdown switch-locale">
                    <a class="nav-link dropdown-toggle switch-locale" href="#" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        Events Monitor</a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdown09">
                        <a class="dropdown-item" href="Event_pharmacy_page">Community Pharmacy Events</a>
                        <a class="dropdown-item" href="Analyzer_page">Data Synchronization Events</a>
                        <a class="dropdown-item" href="Validate_page">Data Profiling and Validation</a>
                    </div>
                </li>
                <li class="nav-item dropdown pull-right">
                    <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="now-ui-icons education_paper"></i>
                        Data Conversion
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item" href="/Converter_page">Convert Data to Excel</a>
                        <a class="dropdown-item" id="cd4due" href="/Converter_page_radet">Generate RADET File</a>
                        <a class="dropdown-item" id="cd4baseline" href="/Converter_page_retention">Generate Cohort
                            Analysis File</a>
                        <a class="dropdown-item" href="/Converter_page_nigqual">Generate NIGQUAL Files</a>
                        <a class="dropdown-item" id="viralloadsupressed" href="/Converter_page_ndr">Generate NDR
                            Files</a>
                    </div>
                </li>
            </ul>
        </div>
        <!-- End of Administrator Menu -->
    </div>
</nav>