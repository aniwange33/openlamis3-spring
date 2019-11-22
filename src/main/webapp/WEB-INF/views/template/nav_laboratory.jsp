<nav class="navbar navbar-expand-md navbar-dark bg-white">
    <!--    <a class="navbar-brand" href="#" id="dash-label">Laboratory</a>-->
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
                <a href="/laboratory/new" class="nav-link text-center">
                    <i class="now-ui-icons design_app"></i>
                    Laboratory Test
                </a>
            </li>
            <li class="nav-item">
                <a href="/prescription" class="nav-link text-center">
                    <i class="now-ui-icons business_badge"></i>
                    Laboratory Test Orders
                </a>
            </li>
            <li class="nav-item dropdown pull-right">
                <a class="nav-link dropdown-toggle text-center" href="#" id="navbarDropdownMenuLink"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="now-ui-icons education_paper"></i>
                    Laboratory Reports
                </a>
                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                    <a href="Lab_query_option.action?formId=1" class="dropdown-item">Laboratory Result Query</a>
                    <a id="cd4due" href="Lab_query_option.action" class="dropdown-item">Patients due for CD4 Count
                        Test</a>
                    <a id="cd4baseline" href="Lab_query_option.action" class="dropdown-item">Patients with current CD4
                        Count &le; baseline value</a>
                    <a href="Viral_load.action" class="dropdown-item">Patients due for Viral Load Test</a>
                    <a id="viralloadsupressed" href="Lab_query_option.action" class="dropdown-item">Patients with
                        current Viral Load &lt; 1000 copies/ml</a>
                    <a id="viralloadunsupressed" href="Lab_query_option.action" class="dropdown-item">Patients with
                        current Viral Load &ge; 1000 copies/ml</a>
                    <a href="Reporting_month_lab.action?formId=1" class="dropdown-item">LAB Monthly Summary</a>
                </div>
            </li>
        </ul>
    </div>
</nav>
<div class="mt-5"></div>
<div class="content col-12 mr-auto ml-auto">