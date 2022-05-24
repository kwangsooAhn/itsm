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
import co.brainz.itsm.sla.metricManual.constants.MetricManualConstants
import co.brainz.itsm.sla.metricManual.dto.MetricManualDataDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualListReturnDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import co.brainz.itsm.sla.metricManual.repository.MetricManualRepository
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import java.time.LocalDateTime
import java.time.Year
import kotlin.math.ceil
import org.springframework.stereotype.Service

@Service
class MetricManualService(
    private val metricManualRepository: MetricManualRepository,
    private val metricPoolRepository: MetricPoolRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val metricYearRepository: MetricYearRepository
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

    fun insertMetricManual(metricManualDataDto: MetricManualDataDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val metricEntity = metricPoolRepository.findById(metricManualDataDto.metricId).get()

        if (metricEntity.metricType == MetricManualConstants.MetricTypeCode.MANUAL.code) {
            metricManualRepository.save(
                MetricManualEntity(
                    metric = metricEntity,
                    referenceDate = metricManualDataDto.referenceDate,
                    metricValue = metricManualDataDto.metricValue,
                    userKey = currentSessionUser.getUserKey(),
                    createDt = LocalDateTime.now()
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    fun deleteMetricManual(metricManualId: String): ZResponse {
        val status = ZResponseConstants.STATUS.SUCCESS
        metricManualRepository.deleteById(metricManualId)
        return ZResponse(
            status = status.code
        )
    }

    fun getMetricManualData(): List<MetricLoadDto> {
        val metricLoadCondition = MetricLoadCondition(
            source = Year.now().toString(),
            type = MetricManualConstants.MetricTypeCode.MANUAL.code
        )
        return metricYearRepository.findMetricListByLoadCondition(metricLoadCondition)
    }
}
