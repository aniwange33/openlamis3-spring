<!-- DASHBOARD MENU-->
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

        if (userGroup === "Administrator") {
            $("#dash").html('Administrator');
            $("#breadcrumb").html('Administrator');
            $admin.show();
            disable($pharm);
            disable($lab);
            disable($clinic);
            disable($case);
            disable($home);
        } else if (userGroup === "Clinician") {
            $("#dash").html('Clinic');
            $("#breadcrumb").html('Clinic');
            $clinic.show();
            disable($pharm);
            disable($lab);
            disable($admin);
            disable($case);
            disable($home);
        } else if (userGroup === "Pharmacist") {
            $("#dash").html('Pharmacy');
            $("#breadcrumb").html('Pharmacy');
            $pharm.show();
            disable($clinic);
            disable($lab);
            disable($admin);
            disable($home);
        } else if (userGroup === "Laboratory Scientist") {
            $("#dash").html('Laboratory');
            $("#breadcrumb").html('Laboratory');
            $lab.show();
            disable($pharm);
            disable($clinic);
            disable($admin);
            disable($home);
        } else {
            $("#dash").html("Dashboard");
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

<div id="sidebar">
    <ul>
        <li class="active"><a href="#"><i class="icon icon-home"></i> <span>Dashboard</span></a></li>
        <li><a href="#"><i class="icon icon-signal"></i> <span>Patient Registration</span></a></li>
        <li><a href="#"><i class="icon icon-inbox"></i> <span>Clinic</span></a></li>
        <li><a href="#"><i class="icon icon-th"></i> <span>Laboratory</span></a></li>
        <li><a href="#"><i class="icon icon-fullscreen"></i> <span>Pharmacy</span></a></li>
        <li class="submenu"><a href="#"><i class="icon icon-th-list"></i> <span>Administrator</span> </a>
            <ul>
                <li><a href="#">Setup</a></li>
                <li><a href="#">Data Maintenance</a></li>
                <li><a href="#">Data Conversion</a></li>
            </ul>
        </li>
    </ul>
</div>
<div id="content">
    <div id="content-header">
        <div id="breadcrumbs"><a href="index.html" title="Go to Home" class="tip-bottom"><i class="icon-home"></i> Home</a>
        </div>
    </div>


    <!--<div id="sidebar">
        <ul>
            <li class="active"><a href="index.html"><i class="icon icon-home"></i> <span>Dashboard</span></a> </li>
            <li> <a href="charts.html"><i class="icon icon-signal"></i> <span>Charts &amp; graphs</span></a> </li>
            <li> <a href="widgets.html"><i class="icon icon-inbox"></i> <span>Widgets</span></a> </li>
            <li><a href="tables.html"><i class="icon icon-th"></i> <span>Tables</span></a></li>
            <li><a href="grid.html"><i class="icon icon-fullscreen"></i> <span>Full width</span></a></li>
            <li class="submenu"> <a href="#"><i class="icon icon-th-list"></i> <span>Forms</span> <span class="label label-important">3</span></a>
              <ul>
                <li><a href="form-common.html">Basic Form</a></li>
                <li><a href="form-validation.html">Form with Validation</a></li>
                <li><a href="form-wizard.html">Form with Wizard</a></li>
              </ul>
            </li>
            <li><a href="buttons.html"><i class="icon icon-tint"></i> <span>Buttons &amp; icons</span></a></li>
            <li><a href="interface.html"><i class="icon icon-pencil"></i> <span>Eelements</span></a></li>
            <li class="submenu"> <a href="#"><i class="icon icon-file"></i> <span>Addons</span> <span class="label label-important">4</span></a>
              <ul>
                <li><a href="index2.html">Dashboard2</a></li>
                <li><a href="gallery.html">Gallery</a></li>
                <li><a href="calendar.html">Calendar</a></li>
                <li><a href="chat.html">Chat option</a></li>
              </ul>
            </li>
        </ul>
    </div>-->
    <!--<nav class="navbar navbar-expand navbar-absolute bg-default">
        <div class="container-fluid">
            <div class="navbar-wrapper">
                <div class="navbar-toggle">
                    <button type="button" class="navbar-toggler">
                        <span class="navbar-toggler-bar bar1"></span>
                        <span class="navbar-toggler-bar bar2"></span>
                        <span class="navbar-toggler-bar bar3"></span>
                    </button>
                </div>
                <a class="navbar-brand" id="dash" href="#"></a>
            </div>

            <div class="collapse navbar-collapse">
                <div  id="hackit" style="display:none;">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a href="#" class="nav-link text-center">
                                <i class="now-ui-icons design_app"></i>
                                Hello! how did you get here???
                            </a>
                        </li>
                    </ul>
                </div>
                <div  id="pharmacy" style="display:none;">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a href="/Devolve_search.action" class="nav-link text-center">
                            <i class="now-ui-icons design_app"></i>
                            <span data-i18n="care-service">Differentiated Care Service</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/Pharmacy_search.action" class="nav-link text-center">
                            <i class="now-ui-icons business_badge"></i>
                            <span  data-i18n="drug-dispensing">Drug Dispensing</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/Prescription_search.action" class="nav-link text-center">
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
                            <a data-i18n="patient-on-first-line-regimen" class="dropdown-item" id="firstline" href="/Devolve_search.action">Patients on First line Regimen</a>
                            <a data-i18n="patient-on-second-line-regimen" class="dropdown-item" id="secondline" href="/Devolve_search.action">Patients on Second line Regimen</a>
                            <a data-i18n="patient-on-third-line-regimen-salvage" class="dropdown-item" id="thirdline" href="/Devolve_search.action">Patients on Third Line Regimen (Salvage)</a>
                            <a data-i18n="patient-per-regime-report" class="dropdown-item" id="regimensummary" href="/Devolve_search.action">Patient Per Regimen Report</a>
                            <a data-i18n="summary-of-drug-dispensed" class="dropdown-item" href="/Reporting_month_pharmacy.action?formId=1">Summary of Drugs Dispensed</a>
                            <a data-i18n="art-refill-devolvement-report" class="dropdown-item" id="devolvedsummary" href="/Devolve_search.action">ART Refill Devolvement Report</a>
                        </div>
                    </li>
                </ul>
                </div>
                <div id="clinic" style="display:none;">
                   <ul class="navbar-nav">
                    <li class="nav-item">
                        <a href="/Anc_search.action" class="nav-link text-center">
                            <i class="now-ui-icons design_app"></i>
                            ANC Registration
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/Delivery_search.action" class="nav-link text-center">
                            <i class="now-ui-icons files_single-copy-04"></i><br>
                            Labour/Delivery
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/Childfollowup_search.action" class="nav-link text-center">
                            <i class="now-ui-icons business_badge"></i>
                            Child Follow-Up
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/Status_search.action" class="nav-link text-center">
                            <i class="now-ui-icons business_chart-bar-32"></i>
                            Client Status Update
                        </a>
                    </li>
                    <li class="nav-item dropdown pull-right">
                        <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="now-ui-icons education_paper"></i>
                            Reports
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                            <a class="dropdown-item" id="cd4due" href="/Reporting_month_pmtct.action?formId=1">PMTCT Monthly Summary</a>
                            <a class="dropdown-item" id="cd4baseline" href="/Reporting_period_pmtct.action?formId=1">NIGERIA QUAL Indicators</a>
                        </div>
                    </li>
                </ul>
                </div>
                 End of Clinic Menu
                <div  id="lab" style="display:none;">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a href="/Laboratory_search.action" class="nav-link text-center">
                            <i class="now-ui-icons design_app"></i>
                            Laboratory Test
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/Labtest_prescription_search.action" class="nav-link text-center">
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
                            <a class="dropdown-item" href="/Lab_query_option.action?formId=1">Laboratory Result Query</a>
                            <a class="dropdown-item" id="cd4due" href="/Laboratory_page.action">Patients due for CD4 Count Test</a>
                            <a class="dropdown-item" id="cd4baseline" href="/Laboratory_page.action">Patients with current CD4 Count ? baseline value</a>
                            <a class="dropdown-item" href="/Viral_load.action">Patients due for Viral Load Test</a>
                            <a class="dropdown-item" id="viralloadsupressed" href="/Laboratory_page.action">Patients with current Viral Load &lt; 1000 copies/ml</a>
                            <a class="dropdown-item" id="viralloadunsupressed" href="/Laboratory_page.action">Patients with current Viral Load ? 1000 copies/ml</a>
                            <a class="dropdown-item" href="/Reporting_month_lab.action?formId=1">LAB Monthly Summary</a>
                        </div>
                    </li>
                </ul>
                </div>
                 End of Laboratory Menu
                <div  id="admin" style="display:none;">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a href="/Setup_page" class="nav-link text-center">
                            <i class="now-ui-icons design_app"></i>
                            Setup
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/Maintenance_page" class="nav-link text-center">
                            <i class="now-ui-icons business_badge"></i>
                            Data Maintenance
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/Event_page" class="nav-link text-center">
                            <i class="now-ui-icons design_app"></i>
                            Events Monitor
                        </a>
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
                            <a class="dropdown-item" id="cd4baseline" href="/Converter_page_retention">Generate Cohort Analysis File</a>
                            <a class="dropdown-item" href="/Converter_page_nigqual">Generate NIGQUAL Files</a>
                            <a class="dropdown-item" id="viralloadsupressed" href="/Converter_page_ndr">Generate NDR Files</a>
                        </div>
                    </li>
                </ul>
                </div>
                 End of Administrator Menu
            </div>
        </div>
    </nav>-->

    <!-- END DASHBOARD MENU -->
    <br clear="both">
                