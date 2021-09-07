/**
 * @projectDescription 사용자 정의 차트를 위한 Highcharts wrapper class.
 *
 * @author Jung Hee Chan
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
class ZChart {
    constructor() {
        // highcharts 기본 옵션
        this.options = {
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

        this.charTypeList = {
            PIE_CHART: 'chart.pie',
            STACKED_COLUMN_CHART: 'chart.stackedColumn',
            STACKED_BAR_CHART: 'chart.stackedBar',
            LINE_AND_COLUMN_CHART: 'chart.lineAndColumn',
            BASIC_LINE_CHART: 'chart.basicLine',
            ACTIVITY_GAUGE_CHART: 'chart.activityGauge'
        };
    }

    /**
     * 차트 종류별 초기화
     */
    init(chartType) {
        if (!charTypeList.contains(chartType)) {
            return;
        }

    }
}

export const zChart = new ZChart();
export const zChartType = charType;

