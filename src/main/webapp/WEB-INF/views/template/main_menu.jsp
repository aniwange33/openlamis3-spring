<!-- MAIN MENU -->
<script type="text/javascript">
    $(document).ready(function () {

        //$("#headerText").html("Welcome "+$("#userId").val());
        var userGroup = $("#user_group").html();
        var $pharm = $("#pharmacy");
        var $lab = $("#lab");
        var $admin = $("#admin");
        var $Conversion = $("#Conversion");
        var $Setup = $("#Setup");
        var $Settings = $("#Settings");
        var $Event = $("#Event");
        var $Settings = $("#Settings");
        var $Visualizer = $("#Visualizer");
        var $clinic = $("#clinic");
        var $case = $("#case");
        var $home = $("#home");
        var $hackit = $("#hackit");
        if (userGroup === "Visualizer") {
            $("#dash").html('Visualizer');
            $("#breadcrumb").html('Visualizer');
            $Visualizer.show();
            disable($pharm);
            disable($lab);
            disable($clinic);
            disable($case);
            disable($home);
            disable($Event);
            disable($Settings);
            disable($Conversion)
            disable($Maintenance);
            disable($pharmacy);
            disable($Event);
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
<ul class="nav nav-pills nav-pills-primary nav-pills-icons flex-column">
    <div class="container-fluid">
        <div class="row">
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3 ml-auto mr-auto">
                <a class="nav-link" href="/patient">
                    <i class="fa fa-user-plus"></i>
                    Patient<br/>Registration
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/clinic">
                    <i class="fa fa-stethoscope"></i>
                    Clinic<br/>&nbsp;
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/pharmacy">
                    <i class="fa fa-medkit"></i>
                    Pharmacy<br/>&nbsp;
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/laboratory">
                    <i class="fa fa-flask"></i>
                    Laboratory<br/>&nbsp;
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/pmct">
                    <i class="fa fa-female"></i>
                    PMTCT<br/>&nbsp;
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/hts">
                    <i class="fa fa-heartbeat"></i>
                    HTS<br/>&nbsp;
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/case-manager">
                    <i class="fa fa-archive"></i>
                    Case<br/>Management
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/visualizer">
                    <i class="fa fa-newspaper-o"></i>
                    Report<br/>&nbsp;
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/converter">
                    <i class="fa fa-refresh"></i>
                    Data Conversion<br/>&nbsp;
                </a>
            </li>

            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/visualizer">
                    <i class="fa fa-line-chart"></i>
                    Visualizer<br/>&nbsp;
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/event">
                    <i class="fa fa-search"></i>
                    Event Monitor<br/>&nbsp;
                </a>
            </li>
            <li class="nav-item  col-sm-6 col-md-4 col-lg-3  ml-auto mr-auto">
                <a class="nav-link" href="/export">
                    <i class="fa fa-database"></i>
                    Data<br/>Maintenance
                </a>
            </li>
        </div>
    </div>
</ul>
<!-- END MAIN MENU -->