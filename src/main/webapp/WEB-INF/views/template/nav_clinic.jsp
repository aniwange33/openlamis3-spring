<nav class="navbar navbar-expand-md navbar-dark bg-white">
    <!--    <a class="navbar-brand" href="/Clinic_page" id="dash-label">Clinic</a>-->
    <button class="navbar-toggler text-white" type="button"
            data-toggle="collapse" data-target="#navbarText"
            aria-controls="navbarSupportedContent" aria-expanded="false"
            aria-label="Toggle navigation">
        <span class="navbar-toggler-bar navbar-kebab"></span>
        <span class="navbar-toggler-bar navbar-kebab"></span>
        <span class="navbar-toggler-bar navbar-kebab"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav">
            <!--            <li class="nav-item">
                            <a href="/Clinic_search.action" class="nav-link text-center">
                                <i class="now-ui-icons files_single-copy-04"></i><br/>
                                Clinic Visit
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="/Commence_search.action" class="nav-link text-center">
                                <i class="now-ui-icons files_single-copy-04"></i><br/>
                                ART Commencement
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="/Chroniccare_search.action" class="nav-link text-center">
                                <i class="now-ui-icons business_badge"></i><br/>
                                Care and Suppport Assessment
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="/Status_search.action" class="nav-link text-center">
                                <i class="now-ui-icons business_chart-bar-32"></i><br/>
                                Client Status Update
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="Eac_search.action" class="nav-link text-center">
                                <i class="now-ui-icons business_chart-bar-32"></i><br/>
                                Unsuppressed Client Monitoring/EAC
                            </a>
                        </li>-->

            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="now-ui-icons education_paper"></i>
                    Clinic Reports
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                    <a class="dropdown-item" id="Viral_load.action" href="Viral_load.action">Patients Due for Viral Load
                        Test</a>
                    <a class="dropdown-item" id="Viralload_supressed" href="Viralload_supressed">Patient With Current
                        Viral Load &lt; 1000copies/ml</a>
                    <a class="dropdown-item" id="Viralload_unsupressed" href="Viralload_unsupressed">Patient with
                        Current Viral Load &ge; 1000copies/ml</a>
                    <a class="dropdown-item" id="Cd4_due" href="/cd4/due">Patients Due for CD4 Test</a>
                    <a class="dropdown-item" id="Cd4_baseline" href="/cd/baseline">Patients with Current CD4 < baseline
                        Value</a> <a data-i18n="Current_care" class="dropdown-item" id="Current_care"
                                     href="Current_care">Currently on Care(ART & Pre-ART)</a>
                    <a data-i18n="Current_treatment" class="dropdown-item" id="Current_treatment"
                       href="Current_treatment">Currently On Treatment(ART)</a>
                    <a data-i18n="Lost_unconfirmed" class="dropdown-item" id="Lost_unconfirmed" href="Lost_unconfirmed">Lost
                        to Fellow Up Unconfirmed</a>
                    <a data-i18n="Appointment_option.actiond" class="dropdown-item" href="Appointment_option.action">Clinic
                        & Refill Appointment/Visits</a>
                    <a data-i18n="Defaulter_refill" class="dropdown-item" href="Defaulter_refill">Defaulters for ARV
                        Appoinment</a>
                    <a class="dropdown-item" href="Reporting_month_patient.action?formId=1">ART Monthly Summary</a>
                    <a class="dropdown-item" id="Reporting_period_care.action?formId=1"
                       href="Reporting_period_care.action?formId=1">Care and Support Monthly Summary Form</a>
                </div>
            </li>
            <!-- <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="now-ui-icons education_paper"></i>
                    PMTCT Reports
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                    <a class="dropdown-item" id="cd4due" href="/Reporting_month_pmtct?formId=1">PMTCT Monthly Summary</a>
                    <a class="dropdown-item" id="cd4baseline" href="/Reporting_period_pmtct?formId=1">NIGERIA QUAL Indicators</a>
                </div>
            </li> -->
        </ul>
    </div>
</nav>
<div class="mt-5"></div>
<div class="content col-12 mr-auto ml-auto">