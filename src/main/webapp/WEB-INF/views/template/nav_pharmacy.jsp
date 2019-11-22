<nav class="navbar navbar-expand-md navbar-dark bg-white">
    <!--    <a class="navbar-brand" href="#" id="dash-label">Pharmacy</a>-->
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
            <li class="nav-item">
                <a href="/pharmacy" class="nav-link text-center">
                    <i class="now-ui-icons business_badge"></i>
                    <span data-i18n="drug-dispensing">Drug Dispensing</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="/devolve" class="nav-link text-center">
                    <i class="now-ui-icons design_app"></i>
                    <span data-i18n="care-service">Differentiated Care</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="/prescription" class="nav-link text-center">
                    <i class="now-ui-icons business_globe"></i>
                    <span data-i18n="drug-prescription">Drug Prescriptions</span>
                </a>
            </li>
            <li class="nav-item dropdown pull-right">
                <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="now-ui-icons education_paper"></i>
                    <span data-i18n="reports"> Pharmacy Reports</span>
                </a>
                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                    <s:a id="firstline" class="dropdown-item">Patients on First line Regimen</s:a>
                    <s:a id="secondline" class="dropdown-item">Patients on Second line Regimen</s:a>
                    <s:a id="thirdline" class="dropdown-item">Patients on Third Line Regimen (Salvage)</s:a>
                    <s:a id="regimensummary" class="dropdown-item">Patient Per Regimen Report</s:a>
                    <a data-i18n="Reporting_month_pharmacy.action?formId=1" class="dropdown-item"
                       href="Reporting_month_pharmacy.action?formId=1">ART Monthly Summary Form</a>
                    <s:a id="devolvedsummary" class="dropdown-item">ART Refill Devolvement Report</s:a>
                </div>
            </li>
        </ul>
    </div>
</nav>
<div class="mt-5"></div>
<div class="content col-12 mr-auto ml-auto">