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
import { zValidation } from '../../lib/zValidation.js';

const DEFAULT_CHART_PROPERTY = {
    chart: { type: 'pie' },
    subtitle: {},
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
Object.freeze(DEFAULT_CHART_PROPERTY);

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
     * 데이터 갯수에 따른 차트 subtitle 좌표 조회
     * @returns subtitle 좌표
     */
    getSubtitleLocation() {
        let subtitleLocation = {
            x: 165,
            y: 180
        };
        // subtitle 의 x 좌표는 모달 여부로 계산
        subtitleLocation.x
            = !zValidation.isDOMElement(document.querySelector('.modal-content'))
                ? subtitleLocation.x : subtitleLocation.x - 30;
        // subtitle 의 y 좌표는 데이터(태그) 갯수로 계산
        subtitleLocation.y
            = (this.tags.length > 7) ? (subtitleLocation.y + 10 * (this.tags.length - 7)) : subtitleLocation.y;
        return subtitleLocation;
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
        // subtitle 위치
        Object.assign(option.subtitle, {
            align: 'left',
            verticalAlign: 'top',
            x: chart.getSubtitleLocation().x,
            y: chart.getSubtitleLocation().y
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
            x: -60,
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

        // total 카운트 구성
        let totalCount = 0;
        data.filter(it => totalCount += Number(it.value));
        this.chart.setSubtitle({
            useHTML: true,
            text: `<span class="highcharts-subtitle-count text-ellipsis" title="${totalCount}">${totalCount}</span>` +
                `</br>` +
                `<span>Total</span>`
        }, false);

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
