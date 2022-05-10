/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.sla.metricManual.dto.MetricManualListReturnDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.repository.MetricManualRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import kotlin.math.ceil
import org.springframework.stereotype.Service

@Service
class SlaManualService(
    private val metricManualRepository: MetricManualRepository
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun findMetricManualSearch(manualSearchCondition: MetricManualSearchCondition): MetricManualListReturnDto {
        val pagingResult = metricManualRepository.findMetricManualSearch(manualSearchCondition)

        return MetricManualListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = metricManualRepository.count(),
                currentPageNum = manualSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / manualSearchCondition.contentNumPerPage).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }
}
