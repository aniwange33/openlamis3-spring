function getSum(total, num) {
    return total + num;
}

function generate() {
    //  $('#loader-container').show();
    //  $("#loader").html('<img id="loader_image" src="assets/img/loading.gif" />');

    var data = "ipId=" + $("#ipId").val() + "&stateId=" + $("#stateId").val() + "&lgaId=" + $("#lgaId").val() + "&id=" + $("#id").val() + "&reportingDateBegin=" + $("#reportingDateBegin").val() + "&reportingDateEnd=" + $("#reportingDateEnd").val();

    var url = "Summary_chart.action?" + data;
    $.getJSON(url, function (json) {
        $("#femaleTX").html(json.indicator[1].female);
        $("#maleTX").html(json.indicator[1].male);
        $("#femaleEnrolled").html(json.indicator[0].female);
        $("#maleEnrolled").html(json.indicator[0].male);
        $("#femaleCurrent").html(json.indicator[1].female);
        $("#maleCurrent").html(json.indicator[1].male);
        $("#femaleViralLoad").html(json.indicator[2].female);
        $("#maleViralLoad").html(json.indicator[2].male);
    });

    url = "EligibleViralload_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart1(json);
    });

    url = "ViralSuppressed_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart2(json);
    });

    url = "UnsuppressedEac_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart3(json);
    });

    url = "TbStatus_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart4(json);
    });

    url = "EverCurrent_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart5(json);
    });

    url = "NewlyEnrolled_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart6(json);
    });

    url = "CurrentOnArt_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart7(json);
    });

    url = "EnrolledStarted_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart8(json);
    });


    url = "MissedAppointment_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart9(json);
    });

    url = "DefaulterReturned_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart10(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });


    url = "CurrentOnArtRegimen_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart11(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });

    url = "CurrentOnArtRegimenAge_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart12(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });

    url = "CurrentOnArtDmoc_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart13(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });

    url = "CurrentOnArtDmocType_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart14(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });
}

function setChart1(json) {
    var highchart = new Highcharts.Chart({
        chart: {
            renderTo: 'container1',
            type: 'column',
            margin: 75,
            zoomType: 'x',
            panning: true,
            panKey: 'shift',
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 2,
                depth: 100,
                viewDistance: 25
            }
        },
        title: {
            text: json.title
        },
        xAxis: {
            categories: json.categories,
            //tickInterval: 1
        },
        yAxis: {
            title: {
                text: null
            },
            // tickInterval: 1
        },
        series: json.series
    });

}

function setChart2(json) {
    var v1 = json.series[0].data;
    var v2 = json.series[1].data;
    var v3 = json.series[2].data;


    var chart = {
        renderTo: 'container2',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
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
        }
    };

    var xAxis = {
        categories: json.categories,
    };
    var yAxis = {
        min: 0,
        title: {
            text: null
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
        }, {
            type: 'column',
            name: json.series[1].name,
            data: v2
        }, {
            type: 'spline',
            name: json.series[2].name,
            data: v3
        }];


    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}

function setChart3(json) {
    var chart = {
        renderTo: 'container3',
        type: 'bar',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = {
        categories: json.categories,
    };
    var yAxis = {
        min: 0,
        title: {
            text: null
        },
    };
    var tooltip = {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    };
    var plotOptions = {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}

function setChart4(json) {
    var chart = {
        renderTo: 'container4',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = {
        categories: json.categories,
        title: {
            text: null
        }
    };
    var yAxis = {
        min: 0,
        title: {
            text: null
        },
    };
    var tooltip = {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    };
    var plotOptions = {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);
    
}

function setChart5(json) {
    var chart = {
        renderTo: 'container5',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = {
        categories: json.categories,
        title: {
            text: null
        }
    };
    var yAxis = {
        min: 0,
        title: {
            text: null
        },
    };
    var tooltip = {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    };
    var plotOptions = {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}

function setChart6(json) {
    var chart = {
        renderTo: 'container6',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = {
        categories: json.categories,
        title: {
            text: null
        }
    };
    var yAxis = {
        min: 0,
        title: {
            text: null
        },
    };
    var tooltip = {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    };
    var plotOptions = {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}

function setChart7(json) {
    var chart = {
        renderTo: 'container7',
        type: 'bar',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = [{
            categories: json.categories,
            reversed: false,
            labels: {
                step: 1
            }
        }, {// mirror axis on right side
            opposite: true,
            reversed: false,
            categories: json.categories,
            linkedTo: 0,
            labels: {
                step: 1
            }
        }];
    var yAxis = {
        title: {
            text: null
        },
        labels: {
            formatter: function () {
                return Math.abs(this.value); // + '%';
            }
        }
    };
    var tooltip = {
        formatter: function () {
            return '<b>' + this.series.name + ', age ' + this.point.category + '</b><br/>' +
                    'Population: ' + Highcharts.numberFormat(Math.abs(this.point.y), 0);
        }
    };
    var plotOptions = {
        series: {
            stacking: 'normal'
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}

function setChart8(json) {
    var chart = {
        renderTo: 'container8',
        type: 'bar',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = [{
            categories: json.categories,
            title: {
                text: null
            },
            reversed: false,
            labels: {
                step: 1
            }
        }, {// mirror axis on right side
            opposite: true,
            reversed: false,
            categories: json.categories,
            linkedTo: 0,
            labels: {
                step: 1
            }
        }];
    var yAxis = {
        title: {
            text: null
        },
        labels: {
            formatter: function () {
                return Math.abs(this.value) + '%';
            }
        }
    };
    var tooltip = {
        formatter: function () {
            return '<b>' + this.series.name + ', age ' + this.point.category + '</b><br/>' +
                    'Population: ' + Highcharts.numberFormat(Math.abs(this.point.y), 0);
        }
    };
    var plotOptions = {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);
    
}


function setChart9(json) {
    var chart = {
        renderTo: 'container9',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = [{
            categories: json.categories,
            reversed: false,
            labels: {
                step: 1
            }
        }, {// mirror axis on right side
            opposite: true,
            reversed: false,
            categories: json.categories,
            linkedTo: 0,
            labels: {
                step: 1
            }
        }];
    var yAxis = {
        title: {
            text: null
        },
        labels: {
            formatter: function () {
                return Math.abs(this.value); // + '%';
            }
        }
    };
    var tooltip = {
        formatter: function () {
            return '<b>' + this.series.name + ', age ' + this.point.category + '</b><br/>' +
                    'Population: ' + Highcharts.numberFormat(Math.abs(this.point.y), 0);
        }
    };
    var plotOptions = {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}

function setChart10(json) {
    var chart = {
        renderTo: 'container10',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = [{
            categories: json.categories,
            reversed: false,
            labels: {
                step: 1
            }
        }, {// mirror axis on right side
            opposite: true,
            reversed: false,
            categories: json.categories,
            linkedTo: 0,
            labels: {
                step: 1
            }
        }];
    var yAxis = {
        title: {
            text: null
        },
        labels: {
            formatter: function () {
                return Math.abs(this.value);// + '%';
            }
        }
    };
    var tooltip = {
        formatter: function () {
            return '<b>' + this.series.name + ', age ' + this.point.category + '</b><br/>' +
                    'Population: ' + Highcharts.numberFormat(Math.abs(this.point.y), 0);
        }
    };
    var plotOptions = {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}



function setChart11(json) {
    var v1 = json.series[0].data;
    var v2 = json.series[1].data;
    var v3 = json.series[2].data;

    var chart = {
        renderTo: 'container11',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
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
        }
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
        }, {
            type: 'column',
            name: json.series[1].name,
            data: v2
        }, {
            type: 'spline',
            name: json.series[2].name,
            data: v3
        },

        {
            type: 'pie',
            name: 'Current & TLD',
            options3d: {
                enabled: true,
                alpha: 5, beta: 0
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    depth: 35,
                    dataLabels: {
                        enabled: true,
                        format: '{pont.name} <b>{point.percentage: .if}%</b>'
                    }
                }
            },
            data: [{
                    name: 'Current On ART',
                    y: 13,
                    color: Highcharts.getOptions().colors[0]
                }, {
                    name: 'TLD',
                    y: 23,
                    color: Highcharts.getOptions().colors[1]
                }],
            center: [100, 90],
            size: 120,
            showInLegend: false,
            dataLabels: {
                enabled: false
            }
        }]


    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}


function setChart12(json) {
    var chart = {
        renderTo: 'container12',
        type: 'bar',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
            viewDistance: 25
        }
    };
    var title = {
        text: json.title
    };
    var subtitle = {
        text: json.subtitle
    };
    var xAxis = [{
            categories: json.categories,
            reversed: false,
            title: {
                text: null
            },
            labels: {
                step: 1
            }
        }, {// mirror axis on right side
            opposite: true,
            reversed: false,
            categories: json.categories,
            linkedTo: 0,
            labels: {
                step: 1
            }
        }];
    var yAxis = {
        title: {
            text: null
        },
        labels: {
            formatter: function () {
                return Math.abs(this.value);// + '%';
            }
        }
    };
    var tooltip = {
        formatter: function () {
            return '<b>' + this.series.name + ', age ' + this.point.category + '</b><br/>' +
                    'Population: ' + Highcharts.numberFormat(Math.abs(this.point.y), 0);
        }
    };
    var plotOptions = {
        series: {
            stacking: 'normal'
        }
    };

    var series = json.series;

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}

function setChart13(json) {
    var v1 = json.series[0].data;
    var v2 = json.series[1].data;
    var v3 = json.series[2].data;

    var chart = {
        renderTo: 'container13',
        type: 'column',
        margin: 75,
        zoomType: 'x',
        panning: true,
        panKey: 'shift',
        options3d: {
            enabled: true,
            alpha: 15,
            beta: 2,
            depth: 100,
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
        }
    };

    var xAxis = {
        categories: json.categories
    };
    var yAxis = {
        min: 0,
        title: {
            text: null
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
        }, {
            type: 'column',
            name: json.series[1].name,
            data: v2
        }, {
            type: 'spline',
            name: json.series[2].name,
            data: v3
        },
        {
            type: 'pie',
            name: 'Current & DMOC',
            options3d: {
                enabled: true,
                alpha: 5, beta: 0
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    options3d: {
                        enabled: true,
                        alpha: 15,
                        beta: 2,
                        depth: 100,
                    },
                    dataLabels: {
                        enabled: true,
                        format: '{pont.name} <b>{point.percentage: .if}%</b>'
                    }
                }
            },
            data: [{
                    name: 'Current On ART',
                    y: 13,
                    color: Highcharts.getOptions().colors[0] // Jane's color
                }, {
                    name: 'DMOC',
                    y: 23,
                    color: Highcharts.getOptions().colors[1] // John's color
                }],
            center: [100, 90],
            size: 120,
            showInLegend: false,
            dataLabels: {
                enabled: false
            }
        }]


    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.xAxis = xAxis;
    json.yAxis = yAxis;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

}


function setChart14(json) {
    var highchart = Highcharts.chart('container14', {
        chart: {
            type: 'pie',
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 2,
                depth: 100,
            }
        },
        title: {
            text: json.title
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                depth: 35,
                dataLabels: {
                    enabled: true,
                    format: '{point.name} <b>{point.percentage:.1f}%</b>'
                }
            }
        },
        series: json.series
    });

}

