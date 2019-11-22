<%-- 
    Document   : newjsp
    Created on : Mar 29, 2014, 4:23:30 PM
    Author     : aalozie
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<div id="header">
    <img id="headerBanner" src="images/lamis_logo.png">
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
            if (userGroup === "Clinician") {
                disable($pharm);
                disable($lab);
                disable($admin);
                disable($case);
                disable($home);
            } else if (userGroup === "Pharmacist") {
                disable($clinic);
                disable($lab);
                disable($admin);
                disable($home);
            } else if (userGroup === "Laboratory Scientist") {
                disable($pharm);
                disable($clinic);
                disable($admin);
                disable($home);
            }

            function disable(obj) {
                obj.css("pointer-events", "none");
                obj.css("opacity", "0.6");
            }
        });
    </script>
    <span id="headerText" style="font-size: 1.1em">Welcome,</span>

    <div id="menuArea">

        <ul id="menu">
            <li id="home"><a href="/home">Home</a></li>
            <li id="clinic">
                <a href="" onclick="return false">Clinic</a>
                <ul>
                    <%--<li><a href="" onclick="return false">General Clinic - GOPD</a></li>--%>
                    <li><a href="/clinic">ART Clinic</a></li>
                    <li><a href="/pmct">Ante-Natal Clinic</a></li>
                    <li id="case"><a href="/case-manager">Case Management</a></li>
                </ul>
            </li>
            <li id="pharmacy">
                <a href="" onclick="return false">Pharmacy</a>
                <ul>
                    <li><a href="/pharmacy">General Pharmacy</a></li>
                </ul>
            </li>
            <li id="lab">
                <a href="" onclick="return false">Laboratory</a>
                <ul>
                    <li><a href="/laboratory">General Lab</a></li>
                    <li><a href="/specimen">Auto Lab (PCR)</a></li>
                </ul>
            </li>
            <li id="admin">
                <a href="" onclick="return false">Administration</a>
                <ul>
                    <li><a href="/set-up">Setup</a></li>
                    <li><a href="/maintenance">Data Maintenance</a></li>
                    <li><a href="/conversion">Data Conversion</a></li>
                    <li><a href="/event-page">Events Monitor</a></li>
                </ul>
            </li>
            <li>
                <a href="" onclick="return false">Visualizer</a>
                <ul>
                    <li><a href="/dashboard">Dashboard</a></li>
                    <li><a href="Chart_period2">Performance Indicator</a></li>
                </ul>
            </li>
            <li><a id="logout" href="/">Logout</a></li>
        </ul>
    </div>

</div> 
