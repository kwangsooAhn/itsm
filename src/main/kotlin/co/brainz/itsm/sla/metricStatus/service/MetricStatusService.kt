/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.service

import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartCondition
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearDetailDto
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Service

@Service
class MetricStatusService(
    private val metricYearRepository: MetricYearRepository
) {

    fun getMetricList(): List<MetricLoadDto> {
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
            tag = metricDto.metricName,
            chartConfig = chartConfig,
            zqlString = metricDto.zqlString
        )
    }

    private fun initChartConfig(year: String): ChartConfig {
        val range = ChartRange(
            type = ChartConstants.Range.BETWEEN.code,
            fromDate = LocalDate.of(year.toInt(),1,1),
            toDate = LocalDate.of(year.toInt(),12,31)
        )
        return ChartConfig(
            range = range,
            periodUnit = ChartConstants.Unit.MONTH.code,
            operation = ChartConstants.Operation.COUNT.code
        )
    }
}
