/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearListReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import java.time.LocalDateTime
import kotlin.math.ceil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MetricYearService(
    private val metricYearRepository: MetricYearRepository,
    private val metricPoolRepository: MetricPoolRepository,
    private val currentSessionUser: CurrentSessionUser
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

    /**
     * 연도별 지표 신규 등록
     */
    @Transactional
    fun createMetricYear(metricYearDto: MetricYearDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val metricEntity = metricPoolRepository.findByMetricId(metricYearDto.metricId)

        if (metricYearRepository.existsByMetricAndMetricYear(metricYearDto.metricId, metricYearDto.metricYear)) {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        } else {
            metricYearRepository.save(
                MetricYearEntity(
                    metric = metricEntity,
                    metricYear = metricYearDto.metricYear,
                    minValue = metricYearDto.minValue,
                    maxValue = metricYearDto.maxValue,
                    weightValue = metricYearDto.weightValue,
                    owner = metricYearDto.owner,
                    comment = metricYearDto.comment,
                    createUserKey = currentSessionUser.getUserKey(),
                    createDt = LocalDateTime.now()
                )
            )
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 년도 선택 시 해당년도에 저장된 지표목록 불러오기
     */
    fun getYearSaveMetricList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        return metricYearRepository.findMetricListByLoadCondition(metricLoadCondition)
    }

}
