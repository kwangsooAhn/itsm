/**
 * Basic Column Chart
 *
 * @author jylim <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { CHART } from '../../lib/zConstants.js';

const DEFAULT_CHART_PROPERTY = {
    chart: {
        type: 'column'
    },
    xAxis: {
        labels: {},
        crosshair: true
    },
    yAxis : {
        title: { text: {} },
        gridLineWidth: 1,
        lineWidth: 0,
        stackLabels: {},
        labels: { autoRotation: false }
    },
    plotOptions: {
        column: {
            pointPadding: -0.1,
            borderWidth: 0
        },
        series: { events: {} }
    },
    tooltip: {
        shared: true,
        crosshairs: false
    },
    series: []
};
Object.freeze(DEFAULT_CHART_PROPERTY);

export const zBasicColumnChartMixin = {
    initProperty() {
        const defaultOptions = JSON.parse(JSON.stringify(DEFAULT_CHART_PROPERTY));
        // x 축 옵션
        this.setXAxisOptions(defaultOptions);
        // y 축 옵션
        this.setYAxisOptions(defaultOptions);
        // 데이터 라벨 설정
        this.setDataLabelOption(defaultOptions);
        // 툴팁 설정
        this.setTooltipOption(defaultOptions);
        // 시리즈 설정
        this.setSeries(defaultOptions);
        // 시리즈 이벤트 설정
        this.setSeriesEvent(defaultOptions);
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
     * X축 옵션 설정
     * @param option 하이차트 옵션
     */
    setXAxisOptions(option) {
        Object.assign(option.xAxis.labels, {
            rotation: (this.config.periodUnit === CHART.PERIOD.HOUR) ? -45 : 0
        });
    },
    /**
     * @param option 하이차트 옵션
     */
    setYAxisOptions(option) {
        Object.assign(option.yAxis.title, {
            text: (this.config.yAxisTitle !== '') ? this.config.yAxisTitle : ''
        });
    },
    /**
     * 데이터 라벨 설정
     * @param option 하이차트 옵션
     */
    setDataLabelOption(option) {
        const chart = this;
        Object.assign(option.plotOptions.series, {
            dataLabels: {
                enabled: false,
                formatter: function () {
                    return chart.getLabelFormat(this.y);
                }
            }
        });

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
                    tooltipFormat += `<br/><span style="color:${point.color}">${point.series.name}: </span> ` +
                        `<strong>${chart.getLabelFormat(point.y)}</strong><br/>`;
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
                name: this.tags[i].value,
                index: this.tags.length - ( i + 1 ), // 범례와 데이터 순서 일치시킴
                legendIndex: i
            });
        }
    },
    /**
     * 차트 시리즈 이벤트 설정
     * @param option 하이차트 옵션
     */
    setSeriesEvent(option) {
        // todo: #12334 차트 시리즈 클릭 이벤트
        Object.assign(option.plotOptions.series.events, {
            click: function(event){
                console.log(event.point);
                zAlert.warning(`${event.point.category} 요청현황 - 부서아이디 : ${event.point.id}`);
            }
        });
    },
    /**
     * 업데이트
     * @param data 데이터
     */
    update(data) {
        if (data.length === 0) { return false; }

        const categories =  [...new Set(data.map(item => item.category))];
        
        this.chart.xAxis[0].setCategories(categories, false);
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
                            // todo : linkKey 값 정리 
                            let testText = (temp.linkKey) ? temp.linkKey : '부서아이디';
                            series.push({ y: Number(temp.value), id: testText });
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
    },
    /**
     * 컬럼차트 삭제
     */
    destroyChart() {
        this.chart.destroy();
        this.chart = null;
        delete this.chart;
    }
};