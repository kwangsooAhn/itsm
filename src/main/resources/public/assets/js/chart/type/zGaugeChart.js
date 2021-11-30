/**
 * Gauge Chart
 *
 * @author woodajung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
const DEFAULT_CHART_PROPERTY = {
    chart: {
        type: 'solidgauge',
        alignTicks:false,
        spacingTop: 10,
        spacingBotton: 0,
        marginTop: 0,
        marginBottom: 0
    },
    pane: {
        center: ['50%', '85%'],
        size: '120%',
        startAngle: -90,
        endAngle: 90,
        background: {
            backgroundColor:'#EEEEEE',
            borderColor: '#CFD5D9',
            borderWidth: 1,
            innerRadius: '60%',
            outerRadius: '100%',
            shape: 'arc'
        }
    },
    yAxis: {
        min: 0,
        max: 100,
        lineWidth: 0,
        tickWidth: 0,
        minorTickInterval: null,
        tickAmount: 0,
        title: {
            y: -100,
            style: {
                fontFamily: 'Noto Sans KR, 맑은고딕, Malgun Gothic, dotum, gulim, sans-serif',
                fontSize: '16px',
                fontWeight: 'bold',
                color: '#222529'
            }
        }, // 시리즈명 위치 조정
        labels: {
            enabled: true,
            y: 16,  // min, max 라벨 위치 조정
            format: '{value} %'
        }
    },
    plotOptions: {
        solidgauge: {
            innerRadius: '60%',
            dataLabels: {}
        },
        gauge: {
            innerRadius: '60%',
            dial: { // 화살표
                baseLength: '0%', //화살표여부 100일수록 사각형임.
                baseWidth: 5,
                radius: '45%', // 각도
                rearLength: '0%', //뒷편으로도 선이 나옴
                backgroundColor: '#8B9094',
                topWidth: 1,
            },
            pivot: {
                borderWidth: 0,
                backgroundColor: '#8B9094',
                radius: 5
            }
        }
    },
    tooltip: { },
    series: [
        { type: 'solidgauge', data: [] }, // 게이지
        { type: 'gauge', dataLabels: { enabled: false }} // 다이얼
    ]
};
Object.freeze(DEFAULT_CHART_PROPERTY);

export const zGaugeChartMixin = {
    initProperty() {
        const defaultOptions = JSON.parse(JSON.stringify(DEFAULT_CHART_PROPERTY));
        // 차트 높이 설정
        this.setSize(defaultOptions);
        // 데이터 라벨 설정
        this.setDataLabelOption(defaultOptions);
        // 툴팁 설정
        this.setTooltipOption(defaultOptions);
        // 옵션 프로퍼티 초기화
        this._options = defaultOptions;
        // 멀티플 차트
        this.chart = [];
        this.setMultiple();
    },
    get options() {
        return this._options;
    },
    set options(value) {
        this._options = value;
    },
    setMultiple() {
        // 제목 표시
        const chartTitle = document.createElement('div');
        chartTitle.className = 'z-chart-content-title align-center mb-2';
        chartTitle.style.flexBasis = '100%';
        chartTitle.textContent = this.name;
        this.domElement.append(chartTitle);

        for (let i = 0; i < this.tags.length; i++) {
            const multipleContainer = document.createElement('div');
            multipleContainer.className = 'z-multiple';
            multipleContainer.style.flexBasis = this.getChartWidth();
            this.domElement.append(multipleContainer);

            // highcharts 초기화
            this.chart.push(Highcharts.chart(multipleContainer, this.options));
        }
    },
    /**
     * 데이터 갯수에 따른 차트 너비 조회
     * @returns 차트 너비
     */
    getChartWidth() {
        let chartWidth = '100%';
        if ((this.tags.length % 2) === 0) {
            chartWidth = '50%';
        } else if ((this.tags.length % 3) === 0) {
            chartWidth = '33%';
        }
        return chartWidth;
    },
    /**
     * 데이터 갯수에 따른 차트 높이 조회
     * @returns 차트 너비
     */
    getChartHeight() {
        let chartHeight = 400;
        if ((this.tags.length % 2) === 0) {
            chartHeight = 250;
        } else if ((this.tags.length % 3) === 0) {
            chartHeight = 200;
        }
        return chartHeight;
    },
    /**
     * 데이터 갯수에 따른 차트 높이 조회
     * @returns 차트 너비
     */
    getYAxisHeight() {
        let chartYAxis = -140;
        if ((this.tags.length % 2) === 0) {
            chartYAxis = -100;
        } else if ((this.tags.length % 3) === 0) {
            chartYAxis = -80;
        }
        return chartYAxis;
    },
    /**
     * 차트 크기 설정
     * @param option 하이차트 옵션
     */
    setSize(option) {
        const chart = this;
        option.yAxis.title.y = this.getYAxisHeight();
        Object.assign(option.chart, {
            height: chart.getChartHeight()
        });
    },
    /**
     * 데이터 라벨 설정
     * @param option 하이차트 옵션
     */
    setDataLabelOption(option) {
        const chart = this;
        Object.assign(option.plotOptions.solidgauge, {
            dataLabels: {
                enabled: true,
                y: 15, // category 데이터 표시
                borderWidth: 0,
                useHTML: true,
                formatter: function () {
                    return chart.getLabelFormat(this.y);
                }
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
                return `${this.series.name}: ${chart.getLabelFormat(this.y)}`;
            }
        });
    },
    /**
     * 업데이트
     * @param data 데이터
     */
    update(data) {
        if (data.length === 0) { return false; }

        for (let i = 0; i < this.tags.length; i++) {
            const tag = this.tags[i];
            let series = [];
            let seriesId = '';
            let category = '';
            for (let j = 0; j < data.length; j++) {
                const temp = data[j];
                if (tag === temp.series) {
                    seriesId = temp.id;
                    category = temp.category;
                    series.push({
                        name: temp.series,
                        color: Highcharts.getOptions().colors[i],
                        y: Number(temp.value)
                    });
                }
            }
            this.chart[i].yAxis[0].setTitle({ text: tag });
            const seriesName = Highcharts.dateFormat(this.getDateTimeFormat(), this.getStringToDateTime(category));
            this.chart[i].series[0].update({ name: seriesName, id: seriesId }, false);
            this.chart[i].series[1].update({ name: seriesName, id: seriesId }, false);
            this.chart[i].series[0].setData(series, false); // 게이지
            this.chart[i].series[1].setData(series, true); // 다이얼
        }
    },
    /**
     * 게이지 차트 삭제
     */
    destroyChart() {
        for (let i = 0; i < this.chart.length; i++) {
            this.chart[i].destroy();
        }
        this.chart = [];
        delete this.chart;
    }
};