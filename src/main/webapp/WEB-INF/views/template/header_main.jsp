<script type="text/javascript">
    $(document).ready(function () {
        $('#logout').on('click', function (e) {
            clearAuthorization();
        });
    });
</script>
<div id="preloader"></div>
<div class="wrapper">
    <div class="main-panel">
        <!-- Navbar -->

        <nav class="navbar topbar navbar-expand-lg" style="background: #2a5788">
            <a class="navbar-brand" href="#">
                <image src="assets/img/lamis_logo.png"/>
            </a>

            <button class="navbar-toggler text-white" type="button" data-toggle="collapse" data-target="#navigation"
                    aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-bar navbar-kebab"></span>
                <span class="navbar-toggler-bar navbar-kebab"></span>
                <span class="navbar-toggler-bar navbar-kebab"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navigation">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="#">
                            <p>
                                <span class="d-md-block">Welcome, </span>
                            </p>
                        </a>
                    </li>
                    <li class="nav-item dropdown switch-locale">
                        <a class="nav-link dropdown-toggle switch-locale" href="#" data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                            Help</a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdown09">
                            <a class="dropdown-item" data-locale="en">LAMIS User Manual </a>
                            <a class="dropdown-item" data-locale="fr">About LAMIS</a>
                        </div>
                    </li>
                    <li class="nav-item dropdown pull-right">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                            <i class="now-ui-icons users_single-02"></i>
                            <p>
                                <span class="d-lg-none d-md-block">Admin</span>
                            </p>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                            <a class="dropdown-item" href="#">Change Password</a>
                            <a class="dropdown-item" id="logout" href="http://localhost:8080">Logout</a>
                        </div>
                    </li>
                </ul>
            </div>
        </nav>
        <!-- End Navbar -->
        <!--        <div class="panel-header-sm my-1 py-1"></div>-->
        <!--<div class="my-2 py-2"></div>-->
        <div class="mt-5"></div>
        <div class="content col-10 mr-auto ml-auto">
