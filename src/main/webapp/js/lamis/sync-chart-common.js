/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function generate() {
    $.getJSON("FacilitySync_chart.action?", function (json) {
    //    $("container1")
        setChart1(json);
        $("#loader").html('');
    });
    
}


function setChart1(json) {
    console.log(json);
    var v1 = json.series[0].data;
    var v2 = json.series[1].data;
    var v3 = json.series[2].data;


    var chart = {
        height: 600,
        renderTo: 'container1',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 15,
            depth: 50,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var plotOptions = {
            column: {
                depth: 25
            },
            series: {
                    borderWidth: 0,
                    dataLabels: {
                            enabled: true,
                            format: '{point.y}'
                    }
            }
    };

    var credit = {
            enabled: false
        };
		
    var xAxis = {
        categories: json.categories
    };
    var yAxis = {
        min: 0,
        title: {
            text: json.titleForYAxis
        },
    };
    var tooltip = {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    };
    var series = [{
            type: 'column',
            name: json.series[0].name,
            data: v1
        }, 
        {
            type: 'column',
            name: json.series[1].name,
            data: v2
        }, 
        {
            type: 'spline',
            name: json.series[2].name,
            data: v3
        }]

    var json = {};
    json.chart = chart;
	json.credit = credit;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

    function showValues() {
        $('#R20-value').html(highchart.options.chart.options3d.alpha);
        $('#R21-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R20').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R21').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
}
