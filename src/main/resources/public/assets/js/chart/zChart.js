/**
 * Line Chart
 *
 * @author woodajung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { CHART } from '../lib/zConstants.js';
import { zLineChartMixin } from './type/zLineChart.js';
import { zStackedColumnChartMixin } from './type/zStackedColumnChart.js';
import { zStackedBarChartMixin } from './type/zStackedBarChart.js';
import { zPieChartMixin } from './type/zPieChart.js';
import { zGaugeChartMixin } from './type/zGaugeChart.js';
import { zValidation } from '../lib/zValidation.js';

const HIGHCHARTS_THEME = {
    global: { useUTC: false }, // 로컬 시간대를 보여주기 위한 설정
    chart: {
        backgroundColor: 'transparent',
        style: {
            fontFamily: '맑은고딕, Malgun Gothic, Noto Sans KR, dotum, gulim, sans-serif',
            fontSize: '14px',
            color: '#222529'
        },
        spacingTop: 20,
        marginTop: 120
    },
    title: {
        text:'',
        style : {
            fontFamily: 'Noto Sans KR, 맑은고딕, Malgun Gothic, dotum, gulim, sans-serif',
            fontSize: '18px',
            color: '#222529',
            fontWeight: 'bold',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap'
        }
    },
    colors: [ '#64BBF6', '#103F8B', '#FFAC47', '#A0D756', '#FBE961', '#C585F3'],
    xAxis: {
        gridLineWidth: 0,
        lineWidth: 1,
        lineColor: '#CFD5D9',
        tickColor: '#CFD5D9',
        maxPadding:0,
        minPadding:0
    },
    yAxis: {
        gridLineWidth: 0,
        lineWidth: 1,
        lineColor: '#CFD5D9',
        tickColor: '#CFD5D9',
        tickWidth: 1,
        title: { text: '' },
        maxPadding:0,
        minPadding:0
    },
    tooltip: {
        backgroundColor: 'rgba(250, 250, 250, 0.85)',
        borderColor: '#CFD5D9',
        shadow: false
    },
    plotOptions: {
        series: {
            turboThreshold : 0, // 속도 높임
            stickyTracking : false // 시리즈에서 마우스를 떼면 툴팁이 사라진다: false, 남아있다: true
        }
    },
    legend: {
        floating: true,
        align: 'right',
        verticalAlign: 'top',
        x: -30,
        y: 30,
        itemStyle: {
            fontFamily: '맑은고딕, Malgun Gothic, Noto Sans KR, dotum, gulim, sans-serif',
            fontSize: '14px',
            color: '#222529',
            fontWeight: 'normal'
        },
        itemHoverStyle: { color: '#1959AC' },
        itemDistance : 10,
        shadow: false,
    },
    exporting: { enabled: false }, // 하이차트 버튼(≡) 비표시
    credits: { enabled: false }, // 하이차트 하단 모서리 크레딧 레이블 삭제
    responsive: {
        rules: [{
            condition: {
                maxWidth: 500,
            },
            chartOptions: {
                legend: {
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom',
                },
            },
        }]
    }
};
Object.freeze(HIGHCHARTS_THEME);

export  default class ZChart {
    constructor(container, data = {}) {
        this.container = container;
        this.domElement = document.getElementById(container);
        this._type = data.chartType || CHART.TYPE.BASIC_LINE;
        this._id = data.chartId || '';
        this._name = data.chartName || '';
        this._desc = data.chartDesc || '';
        this._tags = data.tags || [];
        this._config = data.chartConfig;

        // 테마 적용
        Highcharts.setOptions(HIGHCHARTS_THEME);
        // 타입에 따른 Mixin import
        aliceJs.importMixin(this, this.getMixinByType(data.chartType));

        this.init();
    }

    /**
     * 초기화
     */
    init() {
        // 내부 property 초기화
        this.initProperty();
    }

    /**
     * 타입에 따른 믹스인 호출
     * @param type 타입
     * @returns mixin
     */
    getMixinByType(type) {
        switch(type) {
            case CHART.TYPE.BASIC_LINE:
                return zLineChartMixin;
            case CHART.TYPE.STACKED_COLUMN:
                return zStackedColumnChartMixin;
            case CHART.TYPE.STACKED_BAR:
                return zStackedBarChartMixin;
            case CHART.TYPE.PIE:
                return zPieChartMixin;
            case CHART.TYPE.GAUGE:
                return zGaugeChartMixin;
            default:
                break;
        }
    }

    get type() {
        return this._type;
    }

    set type(type) {
        this._type = type;
    }

    get id() {
        return this._id;
    }

    set id(id) {
        this._id = id;
    }

    get name() {
        return this._name;
    }

    set name(name) {
        this._name = name;
    }

    set desc(desc) {
        this._desc = desc;
    }

    get desc() {
        return this._desc;
    }

    set tags(tags) {
        this._tags = tags;
    }

    get tags() {
        return this._tags;
    }

    set config(config) {
        this._config = config;
    }

    get config() {
        return this._config;
    }

    /**
     * 차트 간격 설정에 따른 시간 데이터 포맷 조회
     * @returns 데이터 포맷
     */
    getDateTimeFormat() {
        switch (this.config.periodUnit) {
            case CHART.PERIOD.YEAR:
                return '%Y'; // YYYY
            case CHART.PERIOD.MONTH:
                return '%Y-%m'; // YYYY-MM
            case CHART.PERIOD.DAY:
                return '%Y-%m-%d'; // YYYY-MM-DD
            default: // 시간
                return '%Y-%m-%d %H'; // YYYY-MM-DD HH
        }
    }
    /**
     * 전달받은 데이터의 날짜 데이터를 Date 타입으로 변경
     * @param userDateTime 데이터 문자열
     * @returns 날짜 데이터
     */
    getStringToDateTime(userDateTime) {
        const matchDateTime = userDateTime.match(zValidation.regex.dateTime);
        return new Date(matchDateTime[1], matchDateTime[2] - 1, matchDateTime[3],
            matchDateTime[4], matchDateTime[5], matchDateTime[6]).getTime();
    }
    /**
     * 연산 방법 설정에 따른 라벨 포맷 조회
     * @param value 현재 값
     * @returns 변환된 값
     */
    getLabelFormat(value) {
        switch (this.config.operation) {
            case CHART.OPERATION.AVG: // 평균 = 소수점 2자리
                return Highcharts.numberFormat(value, 2, '.', ',');
            case CHART.OPERATION.COUNT: // 카운트 = 정수
                return Highcharts.numberFormat(value, 0, '.', ',');
            case CHART.OPERATION.PERCENT: // 퍼센트 = 소수점 2자리
                return Highcharts.numberFormat(value, 2, '.', '') + ' %';
            default:
                return value;
        }
    }
    /**
     * 차트 간격 설정에 따른 기간 단위 조회
     * @returns 기간 단위
     */
    getPointIntervalUnit() {
        switch (this.config.periodUnit) {
            case CHART.PERIOD.YEAR:
                return 'year';
            case CHART.PERIOD.MONTH:
                return 'month';
            case CHART.PERIOD.DAY:
                return 'day';
            default:
                return undefined;
        }
    }
    /**
     * 인스턴스 초기화
     */
    destroy() {
        // 하이차트 초기화
        this.destroyChart();
        // 엘리먼트 초기화
        this.domElement.innerHTML = '';
    }
}
