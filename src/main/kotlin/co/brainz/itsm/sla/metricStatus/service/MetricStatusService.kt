/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.sla.metricStatus.dto.MetricYearlyDto
import co.brainz.itsm.sla.metricStatus.dto.MetricYearlyListReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import kotlin.math.ceil
import org.springframework.stereotype.Service

@Service
class MetricStatusService(
    private val metricYearRepository: MetricYearRepository
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun findMetricYearlySearch(metricYearSearchCondition: MetricYearSearchCondition): MetricYearlyListReturnDto {
        val pagingResult = metricYearRepository.findMetrics(metricYearSearchCondition)
        val dataList: List<MetricYearlyDto> = mapper.convertValue(pagingResult.dataList)
        dataList.forEach { it.metricYear = metricYearSearchCondition.year.toString() }
        val scoreCalculation = this.scoreCalculation(dataList)

        return MetricYearlyListReturnDto(
            data = scoreCalculation,
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = metricYearRepository.count(),
                currentPageNum = metricYearSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / metricYearSearchCondition.contentNumPerPage).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code

            )
        )
    }

    //zql 구현이 되면 instance의 건수를 계산하여 결과값이 나온다.
    /**
     * zql.setZqlExpression(zqlString)
    .setFromDateTime(from)
    .setToDateTime(to)
    .instanceStatus(InstanceStatus.RUNNING) // FINISH가 기본값. (SLA는 FINISH 사용)
    .criteria(ZqlInstanceDateCriteria.FROM) // TO가 기본값. (SLA는 TO 사용)
    .count() //.sum(), .average(), .percentage()
     */
    private fun scoreCalculation(metricYearList: List<MetricYearlyDto>): List<MetricYearlyDto> {
        metricYearList.forEach {
            it.score = 11.0
        }
        return metricYearList
    }
}
