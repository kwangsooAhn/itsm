/**
 * Pie Chart
 *
 * @author woodajung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { CHART } from '../../lib/zConstants.js';

const DEFAULT_CHART_PROPERTY = {
    chart: { type: 'pie' },
    plotOptions: {
        pie: {
            size: 300,
            dataLabels: { enabled: false },
            cursor: 'pointer',
            slicedOffset: 10,
            startAngle: 0,
            showInLegend: true,
            states: {
                inactive: { enabled: true }
            },
        },
    },
    legend: {
        floating: false,
        layout: 'vertical',
        verticalAlign: 'middle'
    },
    tooltip: {},
    series: [{
        colorByPoint: true,
        innerSize: '60%',
        data: [],
    }]
};
// Total 카운트 위치 옵션
const COUNT_LOCATION_OPTION = {
    chart: {
        events: {
            redraw: function () {
                const chart = this,
                    offsetLeft = -80,
                    offsetTop = -24,
                    x = chart.plotLeft + chart.plotWidth / 2 + offsetLeft,
                    y = chart.plotTop + chart.plotHeight / 2 + offsetTop;
                let value = 0;

                // 기존 텍스트 엘리먼트가 있다면 제거
                const preTitleElements = chart.container.querySelectorAll('div.highcharts-total');
                preTitleElements.length ? preTitleElements[0].parentElement.remove() : '';

                // count number
                chart.series[0].yData.forEach(function (point) {
                    value += point;
                });
                chart.renderer.text(`<div class="highcharts-total">` +
                    `<span class="highcharts-total-count text-ellipsis" title="${value}">${value}</span>` +
                    `<br><span>Total</span></div>`, x, y, true).add().toFront();
            }
        }
    }
};
Object.freeze(DEFAULT_CHART_PROPERTY);
Object.freeze(COUNT_LOCATION_OPTION);

export const zPieChartMixin = {
    initProperty() {
        const defaultOptions = JSON.parse(JSON.stringify(DEFAULT_CHART_PROPERTY));
        // 차트 높이 설정
        this.setSize(defaultOptions);
        // 범례 설정
        this.setLegendOption(defaultOptions);
        // 툴팁 설정
        this.setTooltipOption(defaultOptions);
        // 옵션 프로퍼티 초기화
        this._options = aliceJs.mergeObject(defaultOptions, this.customOptions);
        // Total 카운트 위치 옵션 설정
        this._options = aliceJs.mergeObject(this._options, COUNT_LOCATION_OPTION);
        // highcharts 초기화
        this.chart = Highcharts.chart(this.container, this.options);
        // highcharts 이름 초기화
        this.chart.setTitle({ text: this.name }, false);
    },
    get options() {
        return this._options;
    },
    set options(value) {
        this._options = value;
    },
    /**
     * 데이터 갯수에 따른 차트 높이 조회
     * @returns 차트 너비
     */
    getChartHeight() {
        let chartHeight = 400;
        if (this.tags.length > 7) {
            chartHeight += 20 * (this.tags.length - 7);
        }
        return chartHeight;
    },
    /**
     * 차트 크기 설정
     * @param option 하이차트 옵션
     */
    setSize(option) {
        const chart = this;
        // 차트 높이
        Object.assign(option.chart, {
            height: chart.getChartHeight()
        });
    },
    /**
     * 범례 설정
     * 파이차트에 표시되는 범례의 포맷을 변환해서 표시한다.
     * @param option 하이차트 옵션
     */
    setLegendOption(option) {
        Object.assign(option.legend, {
            layout: 'vertical',
            verticalAlign: 'middle',
            x: -50,
            useHTML: true,
            labelFormatter: function () {
                if (this.name === 'title') {
                    return `<div class="highcharts-legend-row">`+
                        `<span style="float:left; color: ${this.color}"">` +
                            i18n.msg('statistic.label.columnName') +
                        `</span>` +
                        `<span style="float:right; color: ${this.color}"">` +
                            i18n.msg('statistic.label.currentSituation') +
                        `</span>` +
                        `</div>`;
                } else {
                    return `<div class="highcharts-legend-row">`+
                        `<span style="float:left; padding-right: 10px; color: ${this.color}">\u25A0</span>` +
                        `<span style="float:left;">${this.name}</span>`+
                        `<span style="float:right">${Highcharts.numberFormat(this.percentage, 2, '.', '')} %</span>`+
                        `</div>`;
                }
            }
        });
    },
    /**
     * 툴팁 설정
     * 파이차트 툴팁 포맷을 변경한다.
     * @param option 하이차트 옵션
     */
    setTooltipOption(option) {
        Object.assign(option.tooltip, {
            useHTML:false,
            formatter: function () {
                return `<span style="color: ${this.color}">\u25A0</span>
                <b> ${this.key}</b>&emsp;&emsp;<b>${Highcharts.numberFormat(this.percentage, 2, '.', '')} %</b>`;
            }
        });
    },
    /**
     * 업데이트
     * @param data 데이터
     */
    update(data) {
        if (!data.length) { return false; }

        let series = [];
        // 범례 헤더 구성
        series.push({ name: 'title', y: null, color: '#757575' });
        // 범례 데이터 구성
        for (let i = 0; i < data.length; i++) {
            const temp = data[i];
            series.push({
                name: temp.category,
                y: Number(temp.value),
                color: this.chart.options.colors[i],
                colorIndex: i
            });
        }

        // 차트의 기간 설정 데이터가 있을 경우, 날짜 데이터 변환
        if (this.config.range.type !== CHART.RANGE_TYPE_NONE) {
            // 날짜 데이터 사용자 포맷 변경
            const fromDt = Highcharts.dateFormat(
                this.getDateTimeFormat(), this.getStringToDateTime(this.config.range.fromDateTime));
            const toDt = Highcharts.dateFormat(
                this.getDateTimeFormat(), this.getStringToDateTime(this.config.range.toDateTime));
            this.chart.series[0].update({ name: (fromDt + ' ~ ' + toDt), id: data[0].id }, false);
        }
        this.chart.series[0].setData(series, true);
    },
    /**
     * 파이차트 삭제
     */
    destroyChart() {
        this.chart.destroy();
        this.chart = null;
        delete this.chart;
    }
};
