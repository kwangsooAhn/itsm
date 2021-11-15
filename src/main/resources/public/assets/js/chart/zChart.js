/**
 * 사용자 정의 차트를 위한 Highcharts wrapper.
 *
 * @author Jung Hee Chan
 * @version 1.0
 * @description
 *
 * 1) #11038 일감을 통해서 모듈화를 하려고 했으나 현재 상태가 너무 복잡해서 파일만 분리
 * 2) 사용자 정의차트, 보고서에서 동일한 로직을 이용해서 차트를 그리기 위해 차트를 그리는 부분만 분리했음.
 * 3) 향후 이 파일과 차트별 파일로 분리하여 리팩토링이 절실히 필요.
 * 4) 아직 chartSearch.html 파일에 차트 종류에 따른 처리가 많이 남아 있음 (설정관련). 이것도 정리 필요.
 *
 * @todo
 *
 * 1) 차트별 기본 설정, 처리 내용을 담은 파일로 분리. 1개씩이라도...
 * 2) 차트에 따라 설정화면도 변경이 되는데 이에 대한 부분도 chartSearch.html 에서 빼서 차트별 파일로 분리
 * 3) 추가적으로 서버와 주고받는 데이터도 재검토 및 정리 필요.
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
function ZChart(container, chartType, chartProperty) {
    // highcharts 기본 옵션
    let options = {
        global: { useUTC: false }, // 로컬 시간대를 보여주기 위한 설정
        chart: {type: 'line'},
        title: {
            text: '',
            style: {
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap'
            }
        },
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
                if (chartType === LINE_AND_COLUMN_CHART) {
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
            };
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
            };
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
                        x: (chart.chartWidth - labelWidth) / 2,
                        y: (chart.plotHeight / 2) + 15
                    };
                }
            };
            options.yAxis = {
                min: 0,
                max: 100,
                lineWidth: 0,
                tickPositions: []
            };
            options.plotOptions = {
                solidgauge: {
                    borderWidth: '2px',
                    dataLabels: {
                        enabled: false
                    },
                    linecap: 'round',
                    stickyTracking: false
                }
            };
            options.pane = {
                startAngle: 0,
                endAngle: 360,
                background: []
            };

            let docKeyList = [];
            let propertyJson = JSON.parse(chartProperty);
            for (let key in propertyJson[1].instanceList) {
                docKeyList.push(key);
            }
            for (let i = 0; i < docKeyList.length - 1; i++) {
                options.series.push({data: []});
            }
    }
    this.chart = Highcharts.chart(container, options);
}

Object.assign(ZChart.prototype, {
    /** chartConfig 파싱 진행 **/
    parsedPropertyJson: function (chartType, chartConfig, property) {
        let chartConfigJson = JSON.parse(chartConfig);
        chartConfigJson.type = chartType;
        let propertyJson = JSON.parse(property);

        /** Get propertyJson Data **/
        let countArray = [];
        let docKeyList = [];
        for (let key in propertyJson[0].operation) {
            // 문서에 대한 카운트 개수
            countArray.push(propertyJson[0].operation[key].count);
        }
        for (let key in propertyJson[1].instanceList) {
            // 문서의 각 나눈 연도의 개수
            docKeyList.push(key);
        }
        const getYear = Number(docKeyList[0].substring(0, 4));
        const getMonth = Number(docKeyList[0].substring(4, 6)) - 1;
        const getDay = Number(docKeyList[0].substring(6, 8));
        const getHour = Number(docKeyList[0].substring(8, 10));

        // 라인차트, 컬럼차트용
        let pointInterval = 1; // time
        let pointIntervalUnit = undefined; //  highcharts 기본값
        let dateTimeFormat = '%Y-%m-%d %H:%M:%S'; // YYYY-MM-DD HH:MI:SS
        if (typeof chartConfigJson.periodUnit !== 'undefined') {
            if (chartConfigJson.periodUnit === CHART_YEAR) {
                pointIntervalUnit = 'year';
                dateTimeFormat = '%Y'; // YYYY
            } else if (chartConfigJson.periodUnit === CHART_MONTH) {
                pointIntervalUnit = 'month';
                dateTimeFormat = '%Y-%m'; // YYYY-MM
            } else if (chartConfigJson.periodUnit === CHART_DAY) {
                pointIntervalUnit = 'day';
                dateTimeFormat = '%Y-%m-%d'; // YYYY-MM-DD
            } else {  // time
                pointInterval = 3600 * 1000;
                dateTimeFormat = '%Y-%m-%d %H'; // YYYY-MM-DD HH
            }
        }
        // 데이터상 현재 시리즈는 1개만 전달됨
        const seriesName = i18n.msg('chart.label.doc');

        // 차트 업데이트
        this.chart.setTitle({text: propertyJson[0].title});

        let seriesData = [];
        const categoryData = Object.keys(propertyJson[1].instanceList);
        switch (chartConfigJson.type) {
            case PIE_CHART:
                this.chart.series[0].update({
                    tooltip: {
                        pointFormatter: function () {
                            if (chartConfigJson.operation === 'percent') {
                                return i18n.msg('chart.label.docRatio') + ': <b>' + Highcharts.numberFormat(this.percentage, 2) + '%</b>';
                            } else {
                                return i18n.msg('chart.label.docCases') + ': <b>' + i18n.msg('common.label.count', Highcharts.numberFormat(this.y, 0)) + '</b>';
                            }
                        }
                    },
                    dataLabels: {
                        formatter: function () {
                            if (chartConfigJson.operation === 'percent') {
                                return '<b>' + this.key + ': <b>' + Highcharts.numberFormat(this.percentage, 2) + '%</b>';
                            } else {
                                return '<b>' + this.key + ': <b>' + i18n.msg('common.label.count', Highcharts.numberFormat(this.y, 0)) + '</b>';
                            }
                        }
                    }
                });
                for (let i = 0; i < categoryData.length; i++) {
                    seriesData.push({
                        name: docKeyList[i],
                        y: countArray[i],
                        sliced: (i === 0),
                        selected: (i === 0)
                    });
                }
                this.chart.series[0].setData(seriesData, true);
                break;
            case BASIC_LINE_CHART:
                this.chart.yAxis[0].setTitle({text: i18n.msg('chart.option.label.yAxisTitle')});
                // 시간 포맷
                this.chart.xAxis[0].update({
                    labels: {
                        formatter: function() {
                            return Highcharts.dateFormat(dateTimeFormat, this.value);
                        }
                    }
                }, false);
                this.chart.series[0].update({
                    pointStart: Date.UTC(getYear, getMonth, getDay, getHour), // TODO: UTC 시간을 사용하는게 맞는지 재확인 필요.
                    pointInterval: pointInterval,
                    pointIntervalUnit: pointIntervalUnit,
                    tooltip: {
                        pointFormatter: function () {
                            if (chartConfigJson.operation === 'percent') {
                                const sum = countArray.reduce((a, b) => (a + b));
                                const percent = (this.y / sum) * 100;
                                return i18n.msg('chart.label.docRatio') + ': <b>' + Highcharts.numberFormat(percent, 2) + '%</b>';
                            } else {
                                return i18n.msg('chart.label.docCases') + ': <b>' + i18n.msg('common.label.count', Highcharts.numberFormat(this.y, 0)) + '</b>';
                            }
                        }
                    },
                    dataLabels: {
                        formatter: function () {
                            if (chartConfigJson.operation === 'percent') {
                                const sum = countArray.reduce((a, b) => (a + b));
                                const percent = (this.y / sum) * 100;
                                return '<b>' + Highcharts.numberFormat(percent, 2) + '%</b>';
                            } else {
                                return '<b>' + Highcharts.numberFormat(this.y, 0) + '</b>';
                            }
                        }
                    }
                });

                for (let i = 0; i < categoryData.length; i++) {
                    const getYear = Number(categoryData[i].substring(0, 4));
                    const getMonth = Number(categoryData[i].substring(4, 6) - 1);
                    const getDay = Number(categoryData[i].substring(6, 8));
                    const getHour = Number(categoryData[i].substring(8, 10));
                    seriesData.push({
                        name: seriesName,
                        x: Date.UTC(getYear, getMonth, getDay, getHour),
                        y: countArray[i]
                    });
                }
                this.chart.series[0].update({name: seriesName, showInLegend: true}, false);
                this.chart.series[0].setData(seriesData, true);
                break;
            case STACKED_COLUMN_CHART:
            case STACKED_BAR_CHART:
            case LINE_AND_COLUMN_CHART:
                this.chart.yAxis[0].setTitle({text: i18n.msg('chart.option.label.yAxisTitle')});
                // 시간 포맷
                this.chart.xAxis[0].update({
                    labels: {
                        formatter: function() {
                            return Highcharts.dateFormat(dateTimeFormat, this.value);
                        }
                    }
                }, false);
                this.chart.series[0].update({
                    pointStart: Date.UTC(getYear, getMonth, getDay, getHour),
                    pointInterval: pointInterval,
                    pointIntervalUnit: pointIntervalUnit,
                    tooltip: {
                        pointFormatter: function () {
                            if (chartConfigJson.operation === 'percent') {
                                const sum = countArray.reduce((a, b) => (a + b));
                                const percent = (this.y / sum) * 100;
                                return i18n.msg('chart.label.docRatio') + ': <b>' + Highcharts.numberFormat(percent, 2) + '%</b>';
                            } else {
                                return i18n.msg('chart.label.docCases') + ': <b>' + i18n.msg('common.label.count', Highcharts.numberFormat(this.y, 0)) + '</b>';
                            }
                        }
                    },
                    dataLabels: {
                        formatter: function () {
                            if (chartConfigJson.operation === 'percent') {
                                const sum = countArray.reduce((a, b) => (a + b));
                                const percent = (this.y / sum) * 100;
                                return '<b>' + Highcharts.numberFormat(percent, 2) + '%</b>';
                            } else {
                                return '<b>' + Highcharts.numberFormat(this.y, 0) + '</b>';
                            }
                        }
                    }
                });

                for (let i = 0; i < categoryData.length; i++) {
                    const getYear = Number(categoryData[i].substring(0, 4));
                    const getMonth = Number(categoryData[i].substring(4, 6) - 1);
                    const getDay = Number(categoryData[i].substring(6, 8));
                    const getHour = Number(categoryData[i].substring(8, 10));
                    seriesData.push({
                        name: seriesName,
                        x: Date.UTC(getYear, getMonth, getDay, getHour),
                        y: countArray[i]
                    });
                }
                this.chart.series[0].update({name: seriesName, showInLegend: true}, false);
                this.chart.series[0].setData(seriesData, true);
                if (chartConfigJson.type === LINE_AND_COLUMN_CHART) {
                    this.chart.series[1].update({
                        name: seriesName,
                        showInLegend: false,
                        type: 'spline',
                        pointStart: Date.UTC(getYear, getMonth, getDay, getHour),
                        pointInterval: pointInterval,
                        pointIntervalUnit: pointIntervalUnit,
                        tooltip: {
                            pointFormatter: function () {
                                if (chartConfigJson.operation === 'percent') {
                                    const sum = countArray.reduce((a, b) => (a + b));
                                    const percent = (this.y / sum) * 100;
                                    return i18n.msg('chart.label.docRatio') + ': <b>' + Highcharts.numberFormat(percent, 2) + '%</b>';
                                } else {
                                    return i18n.msg('chart.label.docCases') + ': <b>' + i18n.msg('common.label.count', Highcharts.numberFormat(this.y, 0)) + '</b>';
                                }
                            }
                        },
                        dataLabels: {
                            formatter: function () {
                                if (chartConfigJson.operation === 'percent') {
                                    const sum = countArray.reduce((a, b) => (a + b));
                                    const percent = (this.y / sum) * 100;
                                    return '<b>' + Highcharts.numberFormat(percent, 2) + '%</b>';
                                } else {
                                    return '<b>' + Highcharts.numberFormat(this.y, 0) + '</b>';
                                }
                            }
                        }
                    }, false);
                    this.chart.series[1].setData(seriesData, true);
                }
                break;
            case ACTIVITY_GAUGE_CHART:
                let innerRadius = 40;
                let radius = 50;
                const sum = countArray.reduce((a, b) => (a + b));
                for (let k = 0; k < categoryData.length; k++) {
                    innerRadius = innerRadius + 10;
                    radius = radius + 10;
                    this.chart.series[k].update({
                        name: categoryData[k],
                        borderColor: Highcharts.getOptions().colors[k]
                    }, false);
                    this.chart.series[k].setData([{
                        color: Highcharts.getOptions().colors[k],
                        radius: radius,
                        innerRadius: innerRadius,
                        y: Number(Highcharts.numberFormat((countArray[k] / sum) * 100))
                    }], true);
                }
                break;
        }
    }
});
