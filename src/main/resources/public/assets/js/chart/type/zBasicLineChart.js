/**
 * Line Chart
 * X축 날짜 데이터 포맷은 YYYY-MM-DD HH:MI:SS 로 전달된다.
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
    chart: { type: 'areaspline' },
    xAxis: {
        type: 'datetime',
        labels: {},
        crosshair: {
            width: 1,
            color: '#ABB0B5'
        }
    },
    plotOptions: {
        series: {}
    },
    tooltip: {
        shared: true,
        split: false,
        crosshairs: true
    },
    legend: {
        y: 30
    },
    series: []
};
Object.freeze(DEFAULT_CHART_PROPERTY);

export const zBasicLineChartMixin = {
    initProperty() {
        const defaultOptions = JSON.parse(JSON.stringify(DEFAULT_CHART_PROPERTY));
        // 날짜 옵션 설정
        this.setDateTimeOption(defaultOptions);
        // 데이터 라벨 설정
        this.setDataLabelOption(defaultOptions);
        // 툴팁 설정
        this.setTooltipOption(defaultOptions);
        // 시리즈 설정
        this.setSeries(defaultOptions);
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
     * 날짜 옵션 설정
     * 라인차트는 x축 데이터가 'dateTime' 즉 시간 데이터가 전달되기 때문에 간격 설정을 처리해줘야 한다.
     * @param option 하이차트 옵션
     */
    setDateTimeOption(option) {
        // 날짜 데이터 사용자 포맷 변경
        const from = i18n.userDateTime(this.config.range.fromDateTime, CHART.DATETIME_FORMAT);
        // 시간 간격 설정
        const defaultDateTimeOptions = {
            pointStart: this.getStringToDateTime(from),
            pointIntervalUnit: this.getPointIntervalUnit()
        };
        Object.assign(option.plotOptions.series, defaultDateTimeOptions);

        // X축 날짜 포맷 설정
        let dateTimeFormat = this.getDateTimeFormat();
        Object.assign(option.xAxis.labels, {
            rotation: (this.config.periodUnit === CHART.PERIOD.HOUR) ? -45 : 0,
            formatter: function () {
                return Highcharts.dateFormat(dateTimeFormat, this.value);
            }
        });
    },
    /**
     * 데이터 라벨 설정
     * 라인차트 선 위에 표시되는 라벨의 포맷을 연산방법에 따라 포맷을 변환해서 표시한다.
     * @param option 하이차트 옵션
     */
    setDataLabelOption(option) {
        // 현재 모듈 클래스를 변수에 담아서 사용하는건 보기 좋은 소스는 아니지만 포인터 객체를 찾지 못하는 버그가 있어서 현재 구조로 구현함 (#11705 일감 확인)
        const chart = this;
        Object.assign(option.plotOptions.series, {
            dataLabels: {
                enabled: true,
                formatter: function () {
                    return chart.getLabelFormat(this.y);
                }
            }
        });
    },
    /**
     * 툴팁 설정
     * 라인차트 툴팁 포맷을 변경한다. X축 데이터는 날짜 포맷이 변환되어 표시되고
     * Y축 데이터는 연산방법에 따라 데이터 포맷이 변환되어 표시된다.
     * @param option 하이차트 옵션
     */
    setTooltipOption(option) {
        // 현재 모듈 클래스를 변수에 담아서 사용하는건 보기 좋은 소스는 아니지만 툴팁 객체를 찾지 못하는 버그가 있어서 현재 구조로 구현함 (#11705 일감 확인)
        const chart = this;
        Object.assign(option.tooltip, {
            formatter: function () {
                const points = this.points;
                let tooltipFormat = `<span style="font-size:inherit">` +
                    `${Highcharts.dateFormat(chart.getDateTimeFormat(), this.x)}` +
                    `</span></br>`;
                for (let i = 0; i < points.length; i++) {
                    const point = points[i];
                    tooltipFormat += `<br/><span style="color:${point.color}">\u25CF</span><b> ${point.series.name}: ` +
                        `${chart.getLabelFormat(point.y)}</b><br/>`;
                }
                return tooltipFormat;
            }
        });
    },
    /**
     * 차트 시리즈 설정
     * 전달된 시리즈 갯수만큼 초기 데이터를 넣어준다.
     * @param option 하이차트 옵션
     */
    setSeries(option) {
        for (let i = 0; i < this.tags.length; i++) {
            option.series.push({
                name: this.tags[i].value
            });
        }
    },
    /**
     * 업데이트
     * @param data 데이터
     */
    update(data) {
        if (!data.length) { return false; }

        for (let i = 0; i < this.chart.series.length; i++) {
            const tag = this.chart.series[i];
            let series = [];
            let seriesId = ''; // 태그는 저장된 후에 ID가 설정되므로 name 기준으로 비교하여 일치하면 태그 ID를 넣어준다.
            let seriesName = ''; // 차트의 기간 설정 데이터가 있을 경우, 날짜 데이터로 변환
            for (let j = 0; j < data.length; j++) {
                const temp = data[j];
                if (tag.options.name === temp.series) {
                    seriesId = temp.id;
                    seriesName = (this.config.range.type !== CHART.RANGE_TYPE_NONE) ?
                        this.getStringToDateTime(temp.category): temp.category;
                    series.push({
                        x: seriesName,
                        y: Number(temp.value)
                    });
                }
            }
            this.chart.series[i].update(
                {
                    fillColor: {
                        linearGradient: { x1: 0, x2: 0, y1: 0, y2: 1 },
                        stops: [
                            [0, aliceJs.hexToRgba(this.chart.options.colors[i], 0.4)],
                            [1, aliceJs.hexToRgba(this.chart.options.colors[i], 0.1)]
                        ]
                    }
                }
            );
            this.chart.series[i].update({ id: seriesId }, false);
            this.chart.series[i].setData(series, false);
        }
        // 모든 데이터를 넣은 후 한번 새로 그려줌
        this.chart.redraw(false);
    },
    /**
     * 라인차트 삭제
     */
    destroyChart() {
        this.chart.destroy();
        this.chart = null;
        delete this.chart;
    }
};
