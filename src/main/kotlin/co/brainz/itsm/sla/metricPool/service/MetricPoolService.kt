/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.sla.metricPool.dto.MetricPoolListReturnDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import kotlin.math.ceil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MetricPoolService(
    private val metricPoolRepository: MetricPoolRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * SLA 지표 목록 조회
     */
    fun getMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition): MetricPoolListReturnDto {
        val pagingResult = metricPoolRepository.findMetricPools(metricPoolSearchCondition)

        return MetricPoolListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = metricPoolRepository.count(),
                currentPageNum = metricPoolSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / metricPoolSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }
}
