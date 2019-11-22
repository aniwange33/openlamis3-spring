function generateByMonth() {

    $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
    $(function (H) {
        H.wrap(H.Chart.prototype, 'showResetZoom', function (proceed) {
        });
    }(Highcharts));

    Highcharts.theme = {

        colors: ['#FF6600', '#2a5788', '#FFCC00', '#DDDF00', '#24CBE5', '#64E572',
            '#FF9655', '#FFF263', '#6AF9C4'],
        chart: {
            backgroundColor: {
                linearGradient: [0, 0, 500, 500],
                stops: [
                    [0, 'rgb(255, 255, 255)'],
                    [1, 'rgb(240, 240, 255)']
                ]
            },
        },
        title: {
            style: {
                color: '#000',
                font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
            }
        },
        subtitle: {
            style: {
                color: '#666666',
                font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
            }
        },

        legend: {
            itemStyle: {
                font: '9pt Trebuchet MS, Verdana, sans-serif',
                color: 'black'
            },
            itemHoverStyle: {
                color: 'gray'
            }
        }
    };

    // Apply the theme
    Highcharts.setOptions(Highcharts.theme);

    var params = "ipId=" + $("#ipId").val() + "&stateId=" + $("#stateId").val() + "&lgaId=" + $("#lgaId").val() + "&id=" + $("#id").val() + "&reportingMonthBegin=" + $("#reportingMonthBegin").val() + "&reportingYearBegin=" + $("#reportingYearBegin").val() + "&reportingMonthEnd=" + $("#reportingMonthEnd").val() + "&reportingYearEnd=" + $("#reportingYearEnd").val();

    console.log("URL: ", params);
    url = "HtsTestedMonth_chart.action?" + params;
    $.getJSON(url, function (json) {
         $("#container1").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart1(json);
        $("#loader").html('');
    });

    $.getJSON("HtsArtInitiatedMonth_chart.action?" + params, function (json) {
         $("#container2").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart2(json);
        $("#loader").html('');
    });

    $.getJSON("HtsPositiveMonth_chart.action?" + params, function (json) {
         $("#container3").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart3(json);
        $("#loader").html('');
    });

    $.getJSON("HtsGenderMonth_chart.action?" + params, function (json) {
         $("#container4").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart4(json);
        $("#loader").html('');
    });
    
     $.getJSON("HtsSettingMonth_chart.action?" + params, function (json) {
          $("#container5").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
         console.log(json);
        setChart5(json);
        $("#loader").html('');
    }).fail(function(err){
        console.log(err);
    });

    $.getJSON("HtsReferralMonth_chart.action?" + params, function (json) {
         $("#container6").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
         console.log(json);
        setChart6(json);
        $("#loader").html('');
    });
    
    $.getJSON("HtsSummaryMonth_chart.action?" + params, function (json) {
        var tested = json.indicator[0].tested;
        var positive = json.indicator[0].positive;
        var initiated = json.indicator[0].initiated;
        var perPositive = 0;
        var perInitiated = 0;

        if (tested > 0) {
            perPositive = (positive / tested) * 100;
            perInitiated = (initiated / positive) * 100;
        }

        $("#totalTested").html(tested);
        $("#totalPositive").html(positive);
        $("#totalInitiated").html(initiated);

        var data = '<div class="progress" style="height: 4px;">'
                + '<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: ' + perPositive + '%;" aria-valuenow="' + perPositive + '" aria-valuemin="0" aria-valuemax="100"></div>'
                + '</div>';
        $("#positive").html(data);
        data = '<div class="progress" style="height: 4px;">'
                + '<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: ' + perInitiated + '%;" aria-valuenow="' + perInitiated + '" aria-valuemin="0" aria-valuemax="100"></div>'
                + '</div>';
        $("#initiated").html(data);

    });
//
   
}

function getSum(total, num) {
    return total + num;
}

function getFacility() {
    var url;

    if ($("#stateId").val().length > 0 && $("#stateId").val() !== "") {
        url = "FacilityId_retrieve.action?q=1&stateId=" + $("#stateId").val();

        if (($("#lgaId").val().length > 0 && $("#lgaId").val() !== "")) {
            url = "FacilityId_retrieve.action?q=1&stateId=" + $("#stateId").val() + "&lgaId=" + $("#lgaId").val();
        }
        $.ajax({
            url: url,
            //  dataType: "json",
            data: {state: $("#stateId").val(), lga: $("#lgaId").val()},
            success: function (facilityMap) {
                console.log("Facility", facilityMap);
                var options = "<option value = '" + '' + "'>" + '' + "</option>";
                $.each(facilityMap, function (key, value) {
                    options += "<option value = '" + key + "'>" + value + "</option>";
                }) //end each
                $("#id").html(options);
            },
            error: function (err) {
                console.warn(err);
            }
        }); //end of ajax call
    }
}
function setChart1(json) {
    $('#container1').highcharts({
        chart: {
            type: 'column',
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            }
        },
        title: {
            text: json.title
        },
        subtitle: {
            text: json.subtitle
        },
        xAxis: {
            categories: json.categories
        },
        yAxis: {
            min: 0,
            title: {
                text: json.titleForYAxis
            }
        },
        exporting: {
            enabled: true
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: json.series
    });

}

function setChart2(json) {
    $('#container2').highcharts({
        chart: {
            type: 'column',
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            }
        },
        title: {
            text: json.title
        },
        subtitle: {
            text: json.subtitle
        },
        xAxis: {
            categories: json.categories
        },
        yAxis: {
            min: 0,
            title: {
                text: json.titleForYAxis
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: json.series
    });
}

function setChart3(json) {
    $('#container3').highcharts({
        chart: {
            type: 'column',
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            }
        },
        title: {
            text: json.title
        },
        subtitle: {
            text: json.subtitle
        },
        xAxis: {
            categories: json.categories
        },
        yAxis: {
            min: 0,
            title: {
                text: json.titleForYAxis
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: json.series
    });
}

function setChart4(json) {
    $('#container4').highcharts({
        chart: {
            type: 'column',
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            }
        },
        title: {
            text: json.title
        },
        subtitle: {
            text: json.subtitle
        },
        xAxis: {
            categories: json.categories
        },
        yAxis: {
            min: 0,
            title: {
                text: json.titleForYAxis
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: json.series
    });

}

function setChart5(json) {
    $('#container5').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            }

        },
        title: {
            text: json.title
        },
        tooltip: {
            formatter: function () {
                return this.point.name + ':<b>' + Highcharts.numberFormat(this.percentage, 1) + '%</b>';
            }
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                innerSize: 100,
                depth: 45,
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    formatter: function () {
                        return this.point.name + ':<b>' + Highcharts.numberFormat(this.percentage, 1) + '%</b>';
                    }
                }
            },
        },
        series: json.series
    });

}

function setChart6(json) {
    $('#container6').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            }

        },
        title: {
            text: json.title
        },
        tooltip: {
            formatter: function () {
                return this.point.name + ':<b>' + Highcharts.numberFormat(this.percentage, 1) + '%</b>';
            }
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                innerSize: 100,
                depth: 45,
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    formatter: function () {
                        return this.point.name + ':<b>' + Highcharts.numberFormat(this.percentage, 1) + '%</b>';
                    }
                }
            }
        },
        series: json.series
    });
}
