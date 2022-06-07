/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.service

import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartCondition
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearSimpleDto
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartData
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import java.time.LocalDate
import java.time.Year
import org.springframework.stereotype.Service

@Service
class MetricStatusService(
    private val metricYearRepository: MetricYearRepository
) {

    fun getMetricList(): List<MetricYearSimpleDto> {
        val metricLoadCondition = MetricLoadCondition(
            source = Year.now().toString()
        )
        return metricYearRepository.findMetricListByLoadCondition(metricLoadCondition)
    }

    fun getMetricStatusChartData(metricStatusChartCondition: MetricStatusChartCondition): MetricStatusChartDto {
        val metricDto = metricYearRepository.findMetricYear(metricStatusChartCondition.metricId, metricStatusChartCondition.year)
        val chartConfig = this.initChartConfig(metricStatusChartCondition.year)
        return MetricStatusChartDto(
            metricYears = metricStatusChartCondition.year,
            metricId = metricStatusChartCondition.metricId,
            chartType = metricStatusChartCondition.chartType,
            metricName = metricDto.metricName,
            metricDesc = metricDto.comment,
            tag = mutableListOf(),
            chartConfig = chartConfig,
            chartData = this.initZqlCalculatedData(),
            zqlString = metricDto.zqlString
        )
    }

    private fun initChartConfig(year: String): ChartConfig {
        val range = ChartRange(
            type = ChartConstants.Range.BETWEEN.code,
            fromDate = LocalDate.of(year.toInt(), 1, 1),
            toDate = LocalDate.of(year.toInt(), 12, 31)
        )
        return ChartConfig(
            range = range,
            periodUnit = ChartConstants.Unit.MONTH.code,
            operation = ChartConstants.Operation.COUNT.code
        )
    }

    private fun initZqlCalculatedData(): MutableList<ChartData> {
        //TODO chartData 대신 ZqlCalculatedData DTO로 변경 해야함
        //임시가데이터 입력
        val dummyData: MutableList<ChartData> = mutableListOf()
        dummyData.add(ChartData(
            id = "",
            category = "2022-01-01 00:00:00",
            value = "3",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-02-01 00:00:00",
            value = "0",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-03-01 00:00:00",
            value = "8",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-04-01 00:00:00",
            value = "4",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-05-01 00:00:00",
            value = "3",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-06-01 00:00:00",
            value = "6",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-07-01 00:00:00",
            value = "1",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-08-01 00:00:00",
            value = "3",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-09-01 00:00:00",
            value = "8",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-10-01 00:00:00",
            value = "2",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-11-01 00:00:00",
            value = "5",
            series = "장애점수"
        ))
        dummyData.add(ChartData(
            id = "",
            category = "2022-12-01 00:00:00",
            value = "5",
            series = "장애점수"
        ))

        return dummyData
    }
}
