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
const DEFAULT_CHART_PROPERTY = {
    chart: { type: 'pie' },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            slicedOffset: 10,
            startAngle: 0,
            showInLegend: true
        },
    },
    legend: {
        floating: false,
        layout: 'vertical',
        verticalAlign: 'middle',
        itemMarginTop: 10,
        itemMarginBottom: 10
    },
    tooltip: {},
    series: [{ data: [] }]
};
Object.freeze(DEFAULT_CHART_PROPERTY);

export const zPieChartMixin = {
    initProperty() {
        const defaultOptions = JSON.parse(JSON.stringify(DEFAULT_CHART_PROPERTY));
        // 데이터 라벨 설정
        this.setDataLabelOption(defaultOptions);
        // 툴팁 설정
        this.setTooltipOption(defaultOptions);
        // 옵션 프로퍼티 초기화
        this._options = defaultOptions;
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
     * 데이터 라벨 설정
     * 파이차트 각 항목에 표시되는 라벨의 포맷을 변환해서 표시한다.
     * @param option 하이차트 옵션
     */
    setDataLabelOption(option) {
        Object.assign(option.plotOptions.pie, {
            dataLabels: {
                enabled: true,
                formatter: function () {
                    return `${this.key} : ${Highcharts.numberFormat(this.percentage, 2, '.', '')} %`;
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
            formatter: function () {
                return `${this.series.name}<br/>` +
                    `<span style="color:${this.color}">\u25CF</span> ${this.key} : ` +
                    `${Highcharts.numberFormat(this.percentage, 2, '.', '')} % (${this.y} / ${this.total})`;
            }
        });
    },
    /**
     * 업데이트
     * @param data 데이터
     */
    update(data) {
        if (data.length === 0) { return false; }

        let series = [];
        for (let i = 0; i < data.length; i++) {
            const temp = data[i];
            series.push({
                name: temp.category,
                y: Number(temp.value),
                sliced: (i === 0),
                selected: (i === 0)
            });
        }
        // 카테고리가 날짜 데이터로 전달됨
        const seriesName = Highcharts.dateFormat(this.getDateTimeFormat(), this.getStringToDateTime(data[0].series));
        this.chart.series[0].update({ name: seriesName, id: data[0].id }, false);
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