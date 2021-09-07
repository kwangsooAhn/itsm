/**
 * 사용자 정의 차트를 위한 Highcharts wrapper.
 *
 * @author Jung Hee Chan
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
const ZChart = function (container, chartType, chartProperty) {
    // highcharts 기본 옵션
    let options = {
        chart: {type: 'line'},
        title: {text: ''},
        subtitle: {text: ''},
        credits: {enabled: false},
        xAxis: {},
        yAxis: {},
        tooltip: {},
        pane: {},
        legend: {
            align: 'right',
            x: -30,
            verticalAlign: 'top',
            y: 25,
            floating: true,
            backgroundColor: 'white',
            borderColor: '#CCC',
            borderWidth: 1,
            shadow: false
        },
        plotOptions: {},
        series: [{data: []}]
    };

    switch (chartType) {
        case PIE_CHART:
            options.chart.type = 'pie';
            options.plotOptions.pie = {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {enabled: true}
            };
            options.series[0].type = 'pie';
            break;
        case BASIC_LINE_CHART:
            options.xAxis.type = 'datetime';
            options.plotOptions = {
                line: {
                    dataLabels: {
                        enabled: true
                    },
                },
                series: {
                    label: {
                        connectorAllowed: true
                    }
                }
            };
            options.responsive = {
                rules: [{
                    condition: {
                        maxWidth: 500,
                    },
                    chartOptions: {
                        legend: {
                            layout: 'horizontal',
                            align: 'center',
                            verticalAlign: 'bottom'
                        }
                    },
                }]
            };
            break;
        case STACKED_COLUMN_CHART:
        case STACKED_BAR_CHART:
        case LINE_AND_COLUMN_CHART:
            if (chartType === STACKED_COLUMN_CHART || chartType === LINE_AND_COLUMN_CHART) {
                if (chartType == LINE_AND_COLUMN_CHART) {
                    options.series.push({data: []});
                }
                options.chart.type = 'column';
            } else {
                options.chart.type = 'bar';
            }
            options.xAxis.type = 'datetime';
            options.yAxis = {
                min: 0,
                stackLabels: {enabled: false}
            }
            options.plotOptions = {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true
                    }
                }
            };
            break;
        case ACTIVITY_GAUGE_CHART:
            options.chart = {
                type: 'solidgauge',
                marginTop: 50
            }
            options.tooltip = {
                borderWidth: 0,
                backgroundColor: 'none',
                shadow: false,
                style: {
                    fontSize: '16px'
                },
                pointFormat: '{series.name}<br><span style="font-size:2em; color: {point.color}; font-weight: bold">{point.y}%</span>',
                positioner: function (labelWidth) {
                    return {
                        x: (this.chart.chartWidth - labelWidth) / 2,
                        y: (this.chart.plotHeight / 2) + 15
                    };
                }
            }
            options.yAxis = {
                min: 0,
                max: 100,
                lineWidth: 0,
                tickPositions: []
            }
            options.plotOptions = {
                solidgauge: {
                    borderWidth: '2px',
                    dataLabels: {
                        enabled: false
                    },
                    linecap: 'round',
                    stickyTracking: false
                }
            }
            options.pane = {
                startAngle: 0,
                endAngle: 360,
                background: []
            }

            let docKeyList = [];
            let propertyJson = JSON.parse(chartProperty);
            for (let key in propertyJson[1].documentList) {
                docKeyList.push(key);
            }
            for (let i = 0; i < docKeyList.length - 1; i++) {
                options.series.push({data: []});
            }
    }
    chart = Highcharts.chart(container, options);
}
