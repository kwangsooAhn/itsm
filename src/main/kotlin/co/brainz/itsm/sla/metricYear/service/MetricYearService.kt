/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.sla.metricYear.dto.MetricYearListReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import kotlin.math.ceil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MetricYearService(
    private val metricYearRepository: MetricYearRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 연도별 SLA 지표 목록 조회
     */
    fun getMetrics(metricYearSearchCondition: MetricYearSearchCondition): MetricYearListReturnDto {
        val pagingResult = metricYearRepository.findMetrics(metricYearSearchCondition)

        return MetricYearListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = metricYearRepository.count(),
                currentPageNum = metricYearSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / metricYearSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }
}
