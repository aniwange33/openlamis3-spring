<%-- 
    Document   : Contact
    Created on : Feb 8, 2012, 1:15:46 PM
    Author     : AALOZIE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>LAMIS 3.0</title>
    <!-- <link type="text/css" rel="stylesheet" href="css/lamis.css" /> -->
    <jsp:include page="/WEB-INF/views/template/css.jsp"/>
    <jsp:include page="/WEB-INF/views/template/javascript.jsp"/>
    <script type="text/JavaScript">
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

            $(".search").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#grid tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });

            $("#dialog").dialog({
                title: "Client Status Update",
                autoOpen: false,
                width: 500,
                resizable: false,
                buttons: [{text: "Yes", click: updateStatus},
                    {
                        text: "Cancel", click: function () {
                            $(this).dialog("close")
                        }
                    }]
            });

            var lastSelected = -99;
            $("#grid").jqGrid({
                url: "Defaulter_grid.action",
                datatype: "json",
                mtype: "GET",
                colNames: ["Hospital No", "Name", "Phone", "Date Tracked", "Outcome", "Agreed Date", "Action"],
                colModel: [
                    {name: "hospitalNum", index: "hospitalNum", width: "150"},
                    {name: "name", index: "name", width: "200"},
                    {name: "phone", index: "phone", width: "150"},
                    {
                        name: "dateTracked",
                        index: "dateTracked",
                        width: "150",
                        sortable: false,
                        editable: true,
                        editoptions: {
                            dataInit: function (date_tracked) {
                                $(date_tracked).datepicker({dateFormat: 'mm/dd/yy'})
                            }
                        }
                    },
                    {
                        name: "outcome",
                        index: "outcome",
                        width: "160",
                        sortable: false,
                        editable: true,
                        edittype: "select",
                        editoptions: {value: " : ;ART Transfer Out:ART Transfer Out;Pre-ART Transfer Out:Pre-ART Transfer Out;Lost to Follow Up:Lost to Follow Up;Stopped Treatment:Stopped Treatment;Known Death:Known Death"}
                    },
                    {
                        name: "agreedDate",
                        index: "agreedDate",
                        width: "150",
                        sortable: false,
                        editable: true,
                        editoptions: {
                            dataInit: function (agreed_date) {
                                $(agreed_date).datepicker({dateFormat: 'mm/dd/yy'})
                            }
                        }
                    },
                    {
                        name: "Action", index: "Action", width: "120", formatter: function () {
                            return "<button onclick='updateButton()' class='btn btn-sm btn-info'>Update</button>";

                        }
                    },
                ],
                pager: $('#pager'),
                rowNum: 100,
                sortname: "hospitalNum",
                sortorder: "desc",
                viewrecords: true,
                imgpath: "themes/basic/images",
                resizable: false,
                height: 350,
                jsonReader: {
                    root: "defaulterList",
                    page: "currpage",
                    total: "totalpages",
                    records: "totalrecords",
                    repeatitems: false,
                    id: "patientId"
                },
                onSelectRow: function (id) {
                    if (id && id != lastSelected) {
                        $("#grid").jqGrid('saveRow', lastSelected,
                            {
                                successfunc: function (response) {
                                    return true;
                                },
                                url: "Defaulter_update.action"
                            })
                        $("#update_button").attr("disabled", false);
                        lastSelected = id;
                    }
                    $("#id").val(id);
                    $("#grid").jqGrid('editRow', id);
                } //end of onSelectRow
            }); //end of jqGrid                 

//            $("#update_button").click(function(event){
//                console.log("update button clicked");
//                $("#messageBar").hide();
//                if($("#userGroup").html() != "Administrator") {
//                $("#lamisform").attr("action", "Error_message");
//                return true;                        
//                }
//                else {
//                $("#dialog").dialog("open");
//                return false;                        
//                }
//            });

        });

        function updateButton() {
            console.log("update button clicked");
            $("#messageBar").hide();
            // $("#dialog").dialog("open");
            //return false;
            $.confirm({
                title: 'Confirm!',
                content: 'Do you want to continue with client status update?<br/> Click Yes to continue or No to cancel',
                buttons: {
                    confirm: function () {
                        alert('Confirm');
                    },
                    cancel: function () {
                        aler("cancel");
                    }
                }
            });
        }

        function updateButton4() {
            console.log("update button clicked");
            $("#messageBar").hide();
            if ($("#userGroup").html() != "Administrator") {
                $("#lamisform").attr("action", "Error_message");
                return true;
            } else {
                $("#dialog").dialog("open");
            }
        }

        function updateStatus() {
            $("#dialog").dialog("close");
            $("#update_button").attr("disabled", true);
            $("#lamisform").attr("action", "Status_update_defaulter");
            $("#lamisform").trigger("submit");
            $("#update_button").attr("disabled", false);
        }

    </script>
    <style>
        .inline-edit-cell.form-control {
            border-width: 1px;
            border-color: #000;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/template/header.jsp"/>
<jsp:include page="/WEB-INF/views/template/nav_casemanagement.jsp"/>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="Home_page.action">Home</a></li>
        <li class="breadcrumb-item"><a href="Casemanagement_page">Case Management</a></li>
        <li class="breadcrumb-item active" aria-current="page">Client Tracking</li>
    </ol>
</nav>
<div class="row">
    <div class="col-md-12 ml-auto mr-auto">
        <div class="card">
            <form id="lamisform" theme="css_xhtml">
                <div class="card-body">
                    <div class="row">
                        <div class="col-12 ml-auto mr-auto">
                            <div class="input-group no-border col-md-3 pull-right">
                                <input type="text" class="form-control search" placeholder="search...">
                                <div class="input-group-append">
                                    <div class="input-group-text">
                                        <i class="now-ui-icons ui-1_zoom-bold"></i>
                                    </div>
                                </div>
                            </div>

                            <!--</div>-->
                            <div class="table-responsive">
                                <table id="grid" class="table table-striped table-bordered center"></table>
                                <div id="pager" style="text-align:center;"></div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12 ml-auto mr-auto">
                            <div id="dialog">
                                <div class="table-responsive">
                                    <table class="table">
                                        <tr>
                                            <td><label>Do you want to continue with client status update?</label></td>
                                        </tr>
                                        <tr>
                                            <td width="20%"><label>Click Yes to continue or No to cancel:</label></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
    </div>
</div>
</form>
<jsp:include page="/WEB-INF/views/template/footer.jsp"/>
</body>
</html>
