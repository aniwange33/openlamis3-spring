function generate() {
    $("container1").html('');  $("container2").html('');  $("container3").html(''); $("container4").html('');
    $("totalTested").html('');  $("totalPositive").html('');  $("totalInitiated").html('');
    $("positive").html('');  $("initiated").html('');
    
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
        itemHoverStyle:{
        color: 'gray'
        }   
        }
        };

        // Apply the theme
        Highcharts.setOptions(Highcharts.theme);
    
    start = moment();
    end = moment();
    $('#reportingDateBegin').val(start.format('YYYY-M-D'));
    $('#reportingDateEnd').val(end.format('YYYY-M-D'));
    console.log("Date1 "+ start); console.log("Date2 " + end);
    var daily = "ipId="+$("#ipId").val() +"&stateId="+$("#stateId").val()+"&lgaId="+$("#lgaId").val()+"&id="+$("#id").val() + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val() ;
    
    $.getJSON("HtsTested_chart.action?"+daily, function (json) {
       //  $("#container1").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart1(json);
        $("#loader").html('');
    });
    
    $.getJSON("HtsArtInitiated_chart.action?"+daily, function (json) {
       //  $("#container2").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart2(json);
        $("#loader").html('');
    });

    $.getJSON("HtsPositive_chart.action?"+daily, function (json) {
       //  $("#container3").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart3(json);
    });

    $.getJSON("HtsGender_chart.action?"+daily, function (json) {
        // $("#container4").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart4(json);
        $("#loader").html('');
    });

    $.getJSON("HtsSummary_chart.action?"+daily, function (json) {
        console.log("Summary::: {} ", json);
        var tested = json.indicator[0].tested;
        var positive = json.indicator[0].positive;
        var initiated = json.indicator[0].initiated;
        var perPositive = 0;
        var perInitiated = 0;

        if (tested > 0) {
            perPositive = (positive / tested) * 100;
            perInitiated = (initiated / positive) * 100;
        } 
        
        var posdata =  '<div class="progress progress-sm mr-2">'
                           + '<div class="progress-bar progress-bar-animated bg-info" role="progressbar" style="width: ' + perPositive +'%" aria-valuenow="'+ positive + '" aria-valuemin="0" aria-valuemax="' + totalTested + '"></div>'
                           + '</div>';
        
        var initdata =  '<div class="progress progress-sm mr-2">'
                           + '<div class="progress-bar-animated bg-info" role="progressbar" style="width: ' + perInitiated +'%" aria-valuenow="'+ initiated + '" aria-valuemin="0" aria-valuemax="' + totalTested + '"></div>'
                           + '</div>';
                   
        $("#totalTested").html(tested);          
        $("#totalPositive").html(posdata);   
        $("#totalInitiated").html(initdata);    
        $("#positive").html(positive);   
        $("#initiated").html(initiated);          
    });
    
    
    //weekly
    start = moment().startOf('isoWeek');
    end = moment().endOf('isoWeek');
    $('#reportingDateBegin').val(start.format('YYYY-M-D'));
    $('#reportingDateEnd').val(end.format('YYYY-M-D'));
    console.log("Weekly1 "+ start); console.log("Weekly2 " + end);
    var weekly = "ipId="+$("#ipId").val() +"&stateId="+$("#stateId").val()+"&lgaId="+$("#lgaId").val()+"&id="+$("#id").val() + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val() ;
    
    $.getJSON("HtsTested_chart.action?"+weekly, function (json) {
    //    $("#container12").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart12(json);
    });
    
    $.getJSON("HtsArtInitiated_chart.action?"+weekly, function (json) {
    //    $("#container22").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart22(json);
    });

    $.getJSON("HtsPositive_chart.action?"+weekly, function (json) {
    //    $("#container32").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart32(json);
    });

    $.getJSON("HtsGender_chart.action?"+weekly, function (json) {
    //    $("#container42").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart42(json);
    });

    $.getJSON("HtsSummary_chart.action?"+weekly, function (json) {
        var tested2 = json.indicator[0].tested;
        var positive2 = json.indicator[0].positive;
        var initiated2 = json.indicator[0].initiated;
        var perPositive2 = 0;
        var perInitiated2 = 0;

        if (tested > 0) {
            perPositive2 = (positive2 / tested2) * 100;
            perInitiated2 = (initiated2 / positive2) * 100;
        } 
        
        var posdata2 =  '<div class="progress progress-sm mr-2">'
                           + '<div class="progress-bar progress-bar-animated bg-info" role="progressbar" style="width: ' + perPositive2 +'%" aria-valuenow="'+ positive2 + '" aria-valuemin="0" aria-valuemax="' + totalTested2 + '"></div>'
                           + '</div>';
        
        var initdata2 =  '<div class="progress progress-sm mr-2">'
                           + '<div class="progress-bar-animated bg-info" role="progressbar" style="width: ' + perInitiated2 +'%" aria-valuenow="'+ initiated2 + '" aria-valuemin="0" aria-valuemax="' + totalTested2 + '"></div>'
                           + '</div>';
                   
        $("#totalTested2").html(tested2);          
        $("#totalPositive2").html(posdata2);   
        $("#totalInitiated2").html(initdata2);    
        $("#positive2").html(positive2);   
        $("#initiated2").html(initiated2);          
    });
    
    //monthly

    start = moment().startOf('month');
    
    $('#reportingMonthBegin').val(start.format('M'));
     $('#reportingYearBegin').val(start.format('YYYY'));
    
    end = moment().subtract(6,'month').endOf('month');
    $('#reportingMonthEnd').val(end.format('M'));
    $('#reportingYearEnd').val(end.format('YYYY'));
    
    console.log("Year "+ $('#reportingYearEnd').val()); console.log("Monthly2 " + $('#reportingMonthEnd').val());
    
    var monthly = "ipId="+$("#ipId").val() +"&stateId="+$("#stateId").val()+"&lgaId="+$("#lgaId").val()+"&id="+$("#id").val() + "&reportingMonthBegin=" + $("#reportingMonthBegin").val() + "&reportingYearBegin=" + $("#reportingYearBegin").val() + "&reportingMonthEnd=" + $("#reportingMonthEnd").val() + "&reportingYearEnd=" + $("#reportingYearEnd").val();
 
    $.getJSON("HtsTestedMonth_chart.action?" + monthly, function (json) {
     //   $("#container13").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart13(json);
    });
    
    $.getJSON("HtsArtInitiatedMonth_chart.action?"+monthly, function (json) {
     //   $("#container23").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart23(json);
    });

    $.getJSON("HtsPositiveMonth_chart.action?"+monthly, function (json) {
     //   $("#container33").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart32(json);
    });

    $.getJSON("HtsGenderMonth_chart.action?"+monthly, function (json) {
    //    $("#container43").css({height: '100%', width: '100%', position: "absolute", margin: '0 auto'});
        setChart43(json);
    });

    $.getJSON("HtsSummaryMonth_chart.action?"+monthly, function (json) {
        var tested3 = json.indicator[0].tested;
        var positive3 = json.indicator[0].positive;
        var initiated3 = json.indicator[0].initiated;
        var perPositive3 = 0;
        var perInitiated3 = 0;

        if (tested3 > 0) {
            perPositive3 = (positive3 / tested3) * 100;
            perInitiated3 = (initiated3 / positive3) * 100;
        } 
        
        var posdata3 =  '<div class="progress progress-sm mr-2">'
                           + '<div class="progress-bar progress-bar-animated bg-info" role="progressbar" style="width: ' + perPositive3 +'%" aria-valuenow="'+ positive3 + '" aria-valuemin="0" aria-valuemax="' + totalTested + '"></div>'
                           + '</div>';
        
        var initdata3 =  '<div class="progress progress-sm mr-2">'
                           + '<div class="progress-bar-animated bg-info" role="progressbar" style="width: ' + perInitiated3 +'%" aria-valuenow="'+ initiated3 + '" aria-valuemin="0" aria-valuemax="' + totalTested + '"></div>'
                           + '</div>';
                   
        $("#totalTested3").html(tested3);          
        $("#totalPositive3").html(posdata3);   
        $("#totalInitiated3").html(initdata3);    
        $("#positive3").html(positive3);   
        $("#initiated3").html(initiated3);          
    });
    
}

//function getSum(total, num) {
//        return total + num;
//}

function setChart1(json) {
 $('#container1').highcharts({
        chart: {
            type: 'column'
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
            type: 'column'
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
            type: 'column'
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
            type: 'column'
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




function setChart12(json) {
 $('#container12').highcharts({
        chart: {
            type: 'column'
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

function setChart22(json) {
    $('#container22').highcharts({
        chart: {
            type: 'column'
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

function setChart32(json) {
    $('#container32').highcharts({
        chart: {
            type: 'column'
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

function setChart42(json) {
    $('#container42').highcharts({
        chart: {
            type: 'column'
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


function setChart13(json) {
 $('#container13').highcharts({
        chart: {
            type: 'column'
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

function setChart23(json) {
    $('#container23').highcharts({
        chart: {
            type: 'column'
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

function setChart33(json) {
    $('#container33').highcharts({
        chart: {
            type: 'column'
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

function setChart43(json) {
    $('#container43').highcharts({
        chart: {
            type: 'column'
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
