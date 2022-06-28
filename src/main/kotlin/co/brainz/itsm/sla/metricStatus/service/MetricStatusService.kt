/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.service

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.sla.metricManual.service.MetricManualService
import co.brainz.itsm.sla.metricPool.constants.MetricPoolConst
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusCondition
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusDto
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
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Service

@Service
class MetricStatusService(
    private val metricYearRepository: MetricYearRepository,
    private val zql: Zql,
    private val metricManualService: MetricManualService,
    private val metricPoolRepository: MetricPoolRepository
) {

    /**
     * 현재 년도에 저징된 지표 조회
     */
    fun getMetricList(year: String): List<MetricYearSimpleDto> {
        return metricYearRepository.findMetricYearListByLoadCondition(year)
    }

    fun getMetricStatusChartData(metricStatusCondition: MetricStatusCondition): MetricStatusDto? {
        val metricDto =
            metricYearRepository.findMetricYear(metricStatusCondition.metricId, metricStatusCondition.year) ?: return null

        val tag = mutableListOf<AliceTagDto>()
        tag.add(AliceTagDto(tagId = "", tagValue = metricDto.metricName))

        return MetricStatusDto(
            metricYears = metricStatusCondition.year,
            metricId = metricStatusCondition.metricId,
            chartType = metricStatusCondition.chartType,
            metricName = metricDto.metricName,
            metricDesc = metricDto.comment,
            tags = tag,
            chartConfig = this.initChartConfig(metricStatusCondition.year),
            chartData = this.initZqlCalculatedData(metricStatusCondition),
            zqlString = metricDto.zqlString
        )
    }

    /**
     * 차트 구성 세팅
     */
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

    /**
     *  차트 계산
     */
    private fun initZqlCalculatedData(metricStatusCondition: MetricStatusCondition): MutableList<ChartData> {
        val metric =
            metricYearRepository.findMetricYear(metricStatusCondition.metricId, metricStatusCondition.year)
        val from = LocalDateTime.of(metricStatusCondition.year.toInt(), 1, 1, 0, 0, 0)
        val to = LocalDateTime.of(metricStatusCondition.year.toInt(), 12, 31, 23, 59, 59)
        val chartData = mutableListOf<ChartData>()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        if (metric!!.metricType == MetricPoolConst.Type.MANUAL.code) {
            for (i in 1..12) {
                val month = LocalDate.of(metricStatusCondition.year.toInt(), i, 1)
                val point = metricManualService.getManualPointSum(
                    metric.metricId,
                    month,
                    LocalDate.of(metricStatusCondition.year.toInt(), i, month.lengthOfMonth())
                ).toString()

                chartData.add(
                    ChartData(
                        id = "",
                        category = month.atStartOfDay().format(formatter),
                        value = point,
                        series = metric.metricName
                    )
                )
            }
        } else {
            zql.setExpression(metric.zqlString)
                .setFrom(from)
                .setTo(to)
                .setPeriod(ZqlPeriodType.MONTH)
                .setInstanceStatus(InstanceStatus.FINISH)
                .setCriteria(ZqlInstanceDateCriteria.END)

            val calculatedData: List<ZqlCalculatedData> = when (metric.calculationType) {
                MetricPoolConst.CalculationType.SUM.code -> zql.sum()
                MetricPoolConst.CalculationType.PERCENTAGE.code -> zql.percentage()
                MetricPoolConst.CalculationType.AVERAGE.code -> zql.average()
                else -> listOf()
            }

            calculatedData.forEach {
                chartData.add(
                    ChartData(
                        id = "",
                        category = it.categoryDT.format(formatter),
                        value = it.value.toString(),
                        series = metric.metricName
                    )
                )
            }
        }

        return chartData
    }
}
