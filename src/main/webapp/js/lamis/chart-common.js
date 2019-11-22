/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function ColumnChart() {    
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

