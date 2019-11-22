function generateByMonth() {
    //  $('#loader-container').show();
     $("#loader").html('<img id="loader_image" src="images/loader_small.gif" />');
    
  var data = "ipId=" + $("#ipId").val() + "&stateId=" + $("#stateId").val() + "&lgaId=" + $("#lgaId").val() + "&id=" + $("#id").val() + "&reportingMonthBegin=" + $("#reportingMonthBegin").val() + "&reportingYearBegin=" + $("#reportingYearBegin").val() + "&reportingMonthEnd=" + $("#reportingMonthEnd").val() + "&reportingYearEnd=" + $("#reportingYearEnd").val();
 console.log(data);
 
    var url = "SummaryMonth_chart.action?" + data;
    $.getJSON(url, function (json) {
        $("#femaleEnrolled").html(json.indicator[0].female);
        $("#maleEnrolled").html(json.indicator[0].male);
        $("#femaleCurrent").html(json.indicator[1].female);
        $("#maleCurrent").html(json.indicator[1].male);
        $("#femaleViralLoad").html(json.indicator[2].female);
        $("#maleViralLoad").html(json.indicator[2].male);
    });

    url = "EligibleViralloadMonth_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart1(json);
    });

    url = "ViralSuppressed_chart.action?" + data;
    $.getJSON(url, function (json) {
        console.log(json);
       // setChart2(json);
    }).fail(function(err){
        console.log('Error', err);
    });

    url = "UnsuppressedEacMonth_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart3(json);
    });

    url = "TbStatusMonth_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart4(json);
    });

    url = "EverCurrentMonth_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart5(json);
    });

    url = "NewlyEnrolledMonth_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart6(json);
    });

    url = "CurrentOnArtMonth_chart.action?" + data;
    $.getJSON(url, function (json) {
        setChart7(json);
    });

    url = "EnrolledStartedMonth_chart.action?"+data;
    $.getJSON(url, function (json) {
        setChart8(json);
    });

    url = "MissedAppointmentMonth_chart.action?"+data;
    $.getJSON(url, function (json) {
        setChart9(json);
    });

    url = "DefaulterReturnedMonth_chart.action?"+data;
    $.getJSON(url, function (json) {
        setChart10(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });


    url = "CurrentOnArtRegimenMonth_chart.action?"+data;
    $.getJSON(url, function (json) {
        setChart11(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });

    url = "CurrentOnArtRegimenAgeMonth_chart.action?"+data;
    $.getJSON(url, function (json) {
        setChart12(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });

    url = "CurrentOnArtDmocMonth_chart.action?"+data;
    $.getJSON(url, function (json) {
        setChart13(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });

    url = "CurrentOnArtDmocTypeMonth_chart.action?"+data;
    $.getJSON(url, function (json) {
        setChart14(json);
        $("#loader").html('');
        $('#loader-container').hide();
    });
}

function isEmpty(obj) {
    for(var key in obj) {
        if(obj.hasOwnProperty(key))
            return false;
    }
    return true;
}

function setChart1(json) {
    var chart = {
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
        }
    };
    var series = json.series;
    var tooltip = {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    };

    var json = {};
    json.chart = chart;
    json.title = title;
    json.subtitle = subtitle;
    json.tooltip = tooltip;
    json.series = series;
    json.plotOptions = plotOptions;
    var highchart = new Highcharts.Chart(json);

    function showValues() {
        $('#R0-value').html(highchart.options.chart.options3d.alpha);
        $('#R1-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R0').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R1').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();

}

function setChart2(json) {
    console.log(json); 
    var v1 = 0; var v2 = 0; var v3 = 0;
    if (isEmpty(json)){
        v1 = json.series[0].data;
        v2 = json.series[1].data;
        v3 = json.series[2].data;
    }


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

    function showValues() {
        $('#R2-value').html(highchart.options.chart.options3d.alpha);
        $('#R3-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R2').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R3').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
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

    function showValues() {
        $('#R4-value').html(highchart.options.chart.options3d.alpha);
        $('#R5-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R4').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R5').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
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

    function showValues() {
        $('#R6-value').html(highchart.options.chart.options3d.alpha);
        $('#R7-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R6').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R7').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
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

    function showValues() {
        $('#R8-value').html(highchart.options.chart.options3d.alpha);
        $('#R9-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R8').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R9').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
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

    function showValues() {
        $('#R10-value').html(highchart.options.chart.options3d.alpha);
        $('#R11-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R10').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R11').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
}

function setChart7(json) {
    console.log(json);
    
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

    function showValues() {
        $('#R12-value').html(highchart.options.chart.options3d.alpha);
        $('#R13-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R12').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R13').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();

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
    var plotOptions =  {
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

    function showValues() {
        $('#R14-value').html(highchart.options.chart.options3d.alpha);
        $('#R15-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R14').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R15').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
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
    var plotOptions =  {
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

    function showValues() {
        $('#R16-value').html(highchart.options.chart.options3d.alpha);
        $('#R17-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R16').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R17').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
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
    var plotOptions =  {
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

    function showValues() {
        $('#R18-value').html(highchart.options.chart.options3d.alpha);
        $('#R19-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R18').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R19').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
}



function setChart11(json) {
    console.log(json);
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
           alpha:5, beta: 0
        },
        plotOptions: {
           pie: {
               allowPointSelect: true,
               cursor: 'pointer',
               depth:35,
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
        center: [720, 90],
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


function setChart12(json) {
    console.log(json);
    
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

    function showValues() {
        $('#R22-value').html(highchart.options.chart.options3d.alpha);
        $('#R23-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R22').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R23').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();

}

function setChart13(json) {
    console.log(json);
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
        name: 'Current & DMOC',
        options3d: {
           enabled: true,
           alpha:5, beta: 0
        },
        plotOptions: {
           pie: {
               allowPointSelect: true,
               cursor: 'pointer',
               depth:35,
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
        center: [720, 90],
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

    function showValues() {
        $('#R24-value').html(highchart.options.chart.options3d.alpha);
        $('#R25-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R24').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R25').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
}


    function setChart14(json) {
          var chart = {
              renderTo: 'container14',
       plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
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
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    formatter: function() {
                        return this.point.name + ':<b>' + Highcharts.numberFormat(this.percentage, 1) + '%</b>';
                    }
                }
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
    var tooltip =  {
               formatter: function() {
                   return this.point.name + ':<b>' + Highcharts.numberFormat(this.percentage, 1) + '%</b>';                
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

    function showValues() {
        $('#R26-value').html(highchart.options.chart.options3d.alpha);
        $('#R27-value').html(highchart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R26').on('change', function () {
        highchart.options.chart.options3d.alpha = this.value;
        showValues();
        highchart.redraw(false);
    });
    $('#R27').on('change', function () {
        highchart.options.chart.options3d.beta = this.value;
        showValues();
        highchart.redraw(false);
    });
    showValues();
    }



//            $('#container14').highcharts({
//            chart: {
//                plotBackgroundColor: null,
//                plotBorderWidth: null,
//                plotShadow: false
//            },
//            title: {
//                text: json.title
//            },
//            tooltip: {
//               formatter: function() {
//                   return this.point.name + ':<b>' + Highcharts.numberFormat(this.percentage, 1) + '%</b>';                
//               }
//            },
//            plotOptions: {
//                pie: {
//                    allowPointSelect: true,
//                    cursor: 'pointer',
//                    dataLabels: {
//                        enabled: true,
//                        color: '#000000',
//                        connectorColor: '#000000',
//                        formatter: function() {
//                            return this.point.name + ':<b>' + Highcharts.numberFormat(this.percentage, 1) + '%</b>';
//                        }
//                    }
//                }
//            },
//            series: json.series
//        });


//    $('#container5').highcharts({
//        chart: {
//            type: 'column'
//        },
//        title: {
//            text: json.title
//        },
//        subtitle: {
//            text: json.subtitle
//        },
//        xAxis: {
//            categories: json.categories
//        },
//        yAxis: {
//            min: 0,
//            title: {
//                text: json.titleForYAxis
//            }
//        },
//        tooltip: {
//            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
//            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y} </b></td></tr>',
//            footerFormat: '</table>',
//            shared: true,
//            useHTML: true
//        },
//        plotOptions: {
//            column: {
//                pointPadding: 0.2,
//                borderWidth: 0
//            }
//        },
//        series: [{
//            type: 'column',
//            name: 'Viral Load Result',
//            data: json.series
//          },{
//                type: 'spline',
//                name: 'Virally Suppressed',
//                data: result,
//                marker: {
//                    lineWidth: 2,
//                    lineColor: Highcharts.getOptions().colors[3],
//                    fillColor: 'white'
//                }
//            },
//        ]
//    });
