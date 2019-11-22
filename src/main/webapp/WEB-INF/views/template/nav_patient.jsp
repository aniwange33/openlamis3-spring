<ul class="navbar-nav">
    <li class="nav-item">
        <a href="Patient_search" class="nav-link text-center">
            <i class="now-ui-icons design_app"></i>
            <span data-i18n="Patient_search">Patients Registration</span>
        </a>
    </li>
    <li class="nav-item">
        <a href="Status_search.action" class="nav-link text-center">
            <i class="now-ui-icons business_badge"></i>
            <span data-i18n="drug-dispensing">Client Status Update</span>
        </a>
    </li>
    <li class="nav-item">
        <a href="Client_tracker.action" class="nav-link text-center">
            <i class="now-ui-icons business_globe"></i>
            <span data-i18n="drug-prescription">Client Tracking</span>
        </a>
    </li>
    <li class="nav-item">
        <a href="Appointment_search.action" class="nav-link text-center">
            <i class="now-ui-icons business_globe"></i>
            <span data-i18n="drug-prescription">Appointment Scheduling</span>
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
               href="Patient_query_criteria.action">Patients Information Query</a>
            <a data-i18n="patient-on-second-line-regimen" class="dropdown-item" id="secondline" href="Current_care">Currently
                on Care(ART & Pre-ART)</a>
            <a data-i18n="patient-on-third-line-regimen-salvage" class="dropdown-item" id="thirdline"
               href="Current_treatment">Currently On Treatment(ART)</a>
            <a data-i18n="patient-per-regime-report" class="dropdown-item" id="regimensummary" href="Lost_unconfirmed">Lost
                to Fellow Up Unconfirmed</a>
            <a data-i18n="summary-of-drug-dispensed" class="dropdown-item" href="Appointment_option.action">Clinic &
                Refill Appointment/Visits</a>
            <a data-i18n="summary-of-drug-dispensed" class="dropdown-item" href="Defaulter_refill">Defaulters for ARV
                Appointment</a>
            <a data-i18n="summary-of-drug-dispensed" class="dropdown-item"
               href="Reporting_month_patient.action?formId=1">ART Monthly Summary Form</a>
            <a data-i18n="art-refill-devolvement-report" class="dropdown-item" id="devolvedsummary"
               href="Reporting_month_patient.action?formId=2">Performance Indicator</a>
        </div>
    </li>
</ul>