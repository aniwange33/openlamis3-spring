<nav class="navbar navbar-expand-md navbar-dark bg-white">
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
                <a href="/case-manager" class="nav-link text-center">
                    <i class="now-ui-icons business_badge"></i>
                    <span data-i18n="Status_search">Client Status Update</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="/case-manager/appointment" class="nav-link text-center">
                    <i class="now-ui-icons business_globe"></i>
                    <span data-i18n=Appointment_search">Appointment Scheduling</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="/case-manager/assign-client" class="nav-link text-center">
                    <i class="now-ui-icons business_badge"></i>
                    Assign Case Managers
                </a>
            </li>
            <li class="nav-item">
                <a href="/case-manager/reassign-client" class="nav-link text-center">
                    <i class="now-ui-icons business_badge"></i>
                    Re-Assign Case Managers
                </a>
            </li>

            <li class="nav-item dropdown pull-right">
                <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="now-ui-icons education_paper"></i>
                    Reports
                </a>
                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                    <a class="dropdown-item" href="/case-manager/report/client-list">Case Managers Clients List</a>
                    <a class="dropdown-item" id="cd4due" href="/case-manager/report/unassigned-list">List of Clients not assigned to a Case
                        Manager</a>
                    <a class="dropdown-item" id="cd4baseline" href="/case-manager/report/client-appointment">Case Manager Client
                        Appointment List</a>
                    <a class="dropdown-item" href="/case-manager/report/client-defaulter">Case Managers Client Defaulters List</a>
                    <a class="dropdown-item" href="/case-manager/report/client-cd4">Case Managers Clients Due for CD4 Count Test </a>
                    <a class="dropdown-item" href="/case-manager/report/viral-load">Case Managers Clients due for Viral Load
                        Test</a>
                </div>
            </li>
        </ul>
    </div>
</nav>
<div class="mt-5"></div>
<div class="content col-12 mr-auto ml-auto">