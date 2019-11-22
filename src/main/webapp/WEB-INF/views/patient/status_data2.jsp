<%-- 
    Document   : Appointment
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <link rel="apple-touch-icon" sizes="76x76" href="lamis/img/apple-icon.png">
    <link rel="icon" type="image/png" href="lamis/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <title>LAMIS 2.6</title>

    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no'
          name='viewport'/>
    <!--     Fonts and icons     -->
    <link href="lamis/css/googlefonts.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">
    <link href="fontawesome/releases/v5.0.6/css/all.css" rel="stylesheet">
    <link href="lamis/css/font-awesome.min.css" rel="stylesheet">
    <!-- CSS Files -->
    <link href="lamis/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="lamis/css/now-ui-dashboard.min.css" rel="stylesheet"/>
    <link href="lamis/css/select2.min.css" rel="stylesheet"/>
    <link href="lamis/css/ style2.css" rel="stylesheet"/>
    <style type="text/css">
        .scrollcell {
            overflow: auto;
            height: 100px;
        }
    </style>
    <!-- <link href="assets/css/bootstrap-datepicker.min.css" rel="stylesheet" />     -->
    <style type="text/css">

        div.font-icon-list:hover {
            cursor: hand;
            cursor: pointer;
            opacity: 0.8;
        }

        div.font-icon-list,
        p {
            font-size: large;
            font-weight: 300;
        }

        a.divLink {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            text-decoration: none;
            color: black;
            /* Makes sure the link doesn't get underlined */
            z-index: 10;
            /*workaround to make clickable in IE */
            opacity: 0;
            /*workaround to make clickable in IE */
            filter: alpha(opacity=0);
            /*workaround to make clickable in IE */
        }

        i.now-ui-icons {
            font-size: 72px;
        }

        .datepicker {
            display: block;
            z-index: 2147483647 !important;
        }

        .form-control {
            border-radius: 6px;
        }

        label.form-label, label.form-check-label {
            color: black;
        }
    </style>

    <script src="assets/js/core/jquery.min.js"></script>
    <script src="assets/js/core/popper.min.js"></script>
    <script type="text/javascript"></script>
    <script>
        $(document).ready(function () {
            $('#grid').DataTable();
        });
    </script>
    <script type="text/JavaScript">
        var gridNum = 7;
        var enablePadding = true;
        $(document).ready(function () {
            $.ajax({
                url: "Padding_status.action",
                dataType: "json",
                success: function (statusMap) {
                    enablePadding = statusMap.paddingStatus;
                }
            });
            resetPage();
            initialize();
            reports();

            $("#grid").jqGrid({
                url: "Patient_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Gender", "ART Status", "Address" "action"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "100"},
                    {name: "name", index: "name", width: "150"},
                    {name: "gender", index: "gender", width: "50"},
                    {name: "currentStatus", index: "currentStatus", width: "130"},
                    {name: "address", index: "address", width: "300"}
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "patientId",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 350,
                jsonReader: {
                    root: "patientList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                },
                onSelectRow: function (id) {
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name)
                    $("#new_button").removeAttr("disabled");
                },
                ondblClickRow: function (id) {
                    $("#id").val(id);
                    var data = $("#grid").getRowData(id)
                    $("#hospitalNum").val(data.hospitalNum);
                    $("#name").val(data.name)
                    $("#lamisform").attr("action", "Status_new");
                    $("#lamisform").submit();
                }
            }); //end of jqGrid

            $("#new_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Status_new");
                return true;
            });
            $("#close_button").bind("click", function (event) {
                $("#lamisform").attr("action", "Clinic_page");
                return true;
            });
        });
    </script>
</head>

<body>
<div id="page">
    <jsp:include page="/WEB-INF/views/template/menu.jsp"/>

    <div id="mainPanel">

        <jsp:include page="/WEB-INF/views/template/nav_home.jsp"/>

        <div id="rightPanel">
            <form id="lamisform">
                <table width="100%" border="0">
                    <tr>
                        <td>
                                    <span>
                                        <img src="images/search.png" style="margin-bottom: -5px;"/> &nbsp;
                                        <span class="top"
                                              style="margin-bottom: 1px; font-family: sans-serif; font-size: 1.1em;"><strong>Clinic >> ART Clinic >> Client Status Update</strong></span>
                                    </span>
                            <hr style="line-height: 2px"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="topheaders">
                            <table width="97%" border="0">
                                <tr>
                                    <td width="12%">Hospital No:</td>
                                    <td><input name="hospitalNum" type="text" class="inputboxes"
                                               id="hospitalNum"/></span></td>
                                    <td><input name="patientId" type="hidden" id="patientId"/></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>Name:</td>
                                    <td width="18%"><input name="name" type="text" class="inputboxes" id="name"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <div id="messageBar"></div>
                <p></p>
                <div>
                    <fieldset>
                        <legend> Patient List</legend>
                        <p></p>
                        <table width="99%" height="90" border="0" class="space">
                            <tr>
                                <td>
                                    <table id="grid"></table>
                                    <div id="pager" style="text-align:center;"></div>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </div>
                <p></p>

                <div id="buttons" style="width: 200px">
                    <button id="new_button" disabled="true">New</button> &nbsp;<button id="close_button">Close</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="footer">
    <jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</div>
</body>
</html>
