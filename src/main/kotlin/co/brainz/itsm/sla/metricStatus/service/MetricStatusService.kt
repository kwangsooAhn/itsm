/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.service

import co.brainz.itsm.sla.metricManual.service.MetricManualService
import co.brainz.itsm.sla.metricPool.constants.MetricPoolConstants
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartCondition
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearSimpleDto
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartData
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import co.brainz.itsm.zql.const.ZqlInstanceDateCriteria
import co.brainz.itsm.zql.const.ZqlPeriodType
import co.brainz.itsm.zql.dto.ZqlCalculatedData
import co.brainz.itsm.zql.service.Zql
import co.brainz.workflow.instance.constants.InstanceStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import org.springframework.stereotype.Service

@Service
class MetricStatusService(
    private val metricYearRepository: MetricYearRepository,
    private val zql: Zql,
    private val metricManualService: MetricManualService
) {

    fun getMetricList(): List<MetricYearSimpleDto> {
        val metricLoadCondition = MetricLoadCondition(
            source = Year.now().toString()
        )
        return metricYearRepository.findMetricListByLoadCondition(metricLoadCondition)
    }

    fun getMetricStatusChartData(metricStatusChartCondition: MetricStatusChartCondition): MetricStatusChartDto {
        val metricDto =
            metricYearRepository.findMetricYear(metricStatusChartCondition.metricId, metricStatusChartCondition.year)
        val chartConfig = this.initChartConfig(metricStatusChartCondition.year)
        return MetricStatusChartDto(
            metricYears = metricStatusChartCondition.year,
            metricId = metricStatusChartCondition.metricId,
            chartType = metricStatusChartCondition.chartType,
            metricName = metricDto.metricName,
            metricDesc = metricDto.comment,
            tag = mutableListOf(),
            chartConfig = chartConfig,
            chartData = this.initZqlCalculatedData(metricStatusChartCondition),
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

    private fun initZqlCalculatedData(metricStatusChartCondition: MetricStatusChartCondition): MutableList<ChartData> {
        val metric =
            metricYearRepository.findMetricYear(metricStatusChartCondition.metricId, metricStatusChartCondition.year)
        val from = LocalDateTime.of(metricStatusChartCondition.year.toInt(), 1, 1, 0, 0, 0)
        val to = LocalDateTime.of(metricStatusChartCondition.year.toInt(), 12, 31, 23, 59, 59)
        val chartData = mutableListOf<ChartData>()

        if (metric.metricType == MetricPoolConstants.MetricTypeCode.MANUAL.code) {
            for (i in 1..12) {
                val month = LocalDate.of(metricStatusChartCondition.year.toInt(), i, 1)
                val point = metricManualService.getManualPointSum(
                    metric.metricId,
                    month,
                    LocalDate.of(metricStatusChartCondition.year.toInt(), i, month.lengthOfMonth())
                ).toString()

                chartData.add(
                    ChartData(
                        id = "",
                        category = month.toString(),
                        value = point,
                        series = metric.metricName
                    )
                )
            }
        } else {
            zql.setExpression(metric.zqlString)
                .setFrom(from)
                .setTo(to)
                .setPeriod(ZqlPeriodType.YEAR)
                .setInstanceStatus(InstanceStatus.FINISH)
                .setCriteria(ZqlInstanceDateCriteria.END)

            val calculatedData:  List<ZqlCalculatedData> = when (metric.calculationType) {
                MetricPoolConstants.MetricCalculationTypeCode.SUM.code -> zql.sum()
                MetricPoolConstants.MetricCalculationTypeCode.PERCENTAGE.code -> zql.percentage()
                MetricPoolConstants.MetricCalculationTypeCode.AVERAGE.code -> zql.average()
                else -> listOf()
            }

            calculatedData.forEach {
                chartData.add(ChartData(
                    id = "",
                    category = it.categoryDT.toString(),
                    value = it.value.toString(),
                    series = metric.metricName
                ))
            }
        }

        return chartData
    }
}
