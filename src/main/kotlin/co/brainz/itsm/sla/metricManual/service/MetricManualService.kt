/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.sla.metricManual.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricManual.dto.MetricLoadDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualKeyDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualListReturnDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import co.brainz.itsm.sla.metricManual.repository.MetricManualRepository
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import java.time.LocalDateTime
import kotlin.math.ceil
import org.springframework.stereotype.Service

@Service
class MetricManualService(
    private val metricManualRepository: MetricManualRepository,
    private val metricPoolRepository: MetricPoolRepository,
    private val currentSessionUser: CurrentSessionUser
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

    fun getMetricYearList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        metricLoadCondition.metricType = "sla.metricType.manual"
        val metricReuslt = metricManualRepository.findMetricYearList(metricLoadCondition)
        return metricReuslt
    }

    fun insertMetricManual(metricManualKeyDto: MetricManualKeyDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val metricEntity = metricPoolRepository.findById(metricManualKeyDto.metricId).get()

        if (!metricManualRepository.existsByMetricIdAndReferenceDt(metricManualKeyDto)) {
            metricManualRepository.save(
                MetricManualEntity(
                    metric = metricEntity,
                    referenceDt = metricManualKeyDto.referenceDt,
                    metricValue = metricManualKeyDto.metricValue,
                    userKey = currentSessionUser.getUserKey(),
                    createDt = LocalDateTime.now()
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }
        return ZResponse(
            status = status.code
        )
    }
}
