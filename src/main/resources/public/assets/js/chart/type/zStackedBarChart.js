/**
 * Stacked Bar Chart
 *
 * @author woodajung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
const DEFAULT_CHART_PROPERTY = {
    chart: { type: 'bar' },
    xAxis: { labels: {} },
    yAxis : {
        endOnTick: false,
        maxPadding: 0.1,
        stackLabels: {}
    },
    plotOptions: {
        series: { stacking: 'normal' }
    },
    tooltip: {
        shared: true,
        crosshairs: false
    },
    series: []
};
Object.freeze(DEFAULT_CHART_PROPERTY);

export const zStackedBarChartMixin = {
    initProperty() {
        const defaultOptions = JSON.parse(JSON.stringify(DEFAULT_CHART_PROPERTY));
        // 데이터 라벨 설정
        this.setDataLabelOption(defaultOptions);
        // 툴팁 설정
        this.setTooltipOption(defaultOptions);
        // 시리즈 설정
        this.setSeries(defaultOptions);
        // 옵션 프로퍼티 초기화
        this._options = defaultOptions;
    },
    get options() {
        return this._options;
    },
    set options(value) {
        this._options = value;
    },
    /**
     * 데이터 라벨 설정
     * @param option 하이차트 옵션
     */
    setDataLabelOption(option) {
        const chart = this;
        Object.assign(option.plotOptions.series, {
            dataLabels: {
                enabled: true,
                //crop: false,
                //overflow:'none',
                //inside:false,
                formatter: function () {
                    return chart.getLabelFormat(this.y);
                }
            }
        });
        // Total 라벨
        Object.assign(option.yAxis.stackLabels, {
            enabled: true,
            formatter: function () {
                return chart.getLabelFormat(this.total);
            }
        });
    },
    /**
     * 툴팁 설정
     * @param option 하이차트 옵션
     */
    setTooltipOption(option) {
        const chart = this;
        Object.assign(option.tooltip, {
            formatter: function () {
                const points = this.points;
                let tooltipFormat = `<span style="font-size:inherit">${this.x}</span><br/>`;
                for (let i = 0; i < points.length; i++) {
                    const point = points[i];
                    tooltipFormat += `<br/><span style="color:${point.color}">\u25CF</span> ${point.series.name}: ` +
                        `${chart.getLabelFormat(point.y)}<br/>`;
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
                name: this.tags[i],
                index: this.tags.length - ( i + 1 ), // 범례와 데이터 순서 일치시킴
                legendIndex: i
            });
        }
    },
    /**
     * 업데이트
     * @param data 데이터
     */
    update(data) {
        if (data.length === 0) { return false; }

        const categories =  [...new Set(data.map(item => item.category))];
        // 날짜 데이터 변환
        const dateTimeCategories =  categories.map((category) =>
            Highcharts.dateFormat(this.getDateTimeFormat(), this.getStringToDateTime(category)));
        this.chart.xAxis[0].setCategories(dateTimeCategories, false);
        for (let i = 0; i < this.chart.series.length; i++) {
            const tag = this.chart.series[i];
            let series = [];
            let seriesId = '';
            for (let j = 0; j < data.length; j++) {
                const temp = data[j];
                if (tag.options.name === temp.series) {
                    seriesId = temp.id;
                    for (let z = 0; z < categories.length; z++) {
                        if (categories[z] === temp.category) {
                            series.push({ y: Number(temp.value) });
                            break;
                        }
                    }
                }
            }
            this.chart.series[i].update({ id: seriesId }, false);
            this.chart.series[i].setData(series, false);
        }
        // 모든 데이터를 넣은 후 한번 새로 그려줌
        this.chart.redraw(false);
    }
};