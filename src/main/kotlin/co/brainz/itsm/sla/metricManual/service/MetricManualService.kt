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
import co.brainz.itsm.sla.metricManual.dto.MetricManualDataDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualReturnDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.dto.MetricManualSimpleDto
import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import co.brainz.itsm.sla.metricManual.repository.MetricManualRepository
import co.brainz.itsm.sla.metricPool.constants.MetricPoolConstants
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearSimpleDto
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import java.time.LocalDate
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

    /**
     * 수동 지표 리스트 조회
     */
    fun findMetricManualSearch(manualSearchCondition: MetricManualSearchCondition): MetricManualReturnDto {
        val pagingResult = metricManualRepository.findMetricManualSearch(manualSearchCondition)

        return MetricManualReturnDto(
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

    /**
     * 수동지표 검색
     */
    fun getMetricsByManual(): List<MetricManualSimpleDto> {
        return metricManualRepository.findMetricByMetricType(MetricPoolConstants.MetricTypeCode.MANUAL.code)
    }

    /**
     * 수동 지표 등록
     */
    fun insertMetricManual(metricManualDataDto: MetricManualDataDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val metricEntity = metricPoolRepository.findById(metricManualDataDto.metricId).get()

        if (metricEntity.metricType == MetricPoolConstants.MetricTypeCode.MANUAL.code) {
            metricManualRepository.save(
                MetricManualEntity(
                    metric = metricEntity,
                    referenceDate = metricManualDataDto.formattedReferenceDate,
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

    /**
     * 수동지표 삭제
     */
    fun deleteMetricManual(metricManualId: String): ZResponse {
        val status = ZResponseConstants.STATUS.SUCCESS
        metricManualRepository.deleteById(metricManualId)
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 수동인 대상지표 목록 조회
     */
    fun getMetricManualData(): List<MetricYearSimpleDto> {
        val metricLoadCondition = MetricLoadCondition(
            source = Year.now().toString(),
            type = MetricPoolConstants.MetricTypeCode.MANUAL.code
        )
        return metricYearRepository.findMetricListByLoadCondition(metricLoadCondition)
    }

    /**
     * 기간 내 매뉴얼 지표 점수의 합
     */
    fun getManualPointSum(metricManualId: String, startDt: LocalDate, endDt: LocalDate): Float {
        return metricManualRepository.findManualPointSum(metricManualId, startDt, endDt) ?: 0f;
    }
}
