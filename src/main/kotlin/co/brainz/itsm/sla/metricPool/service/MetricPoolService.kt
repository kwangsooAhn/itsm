/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.sla.metricPool.dto.MetricDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolListReturnDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricPool.entity.MetricEntity
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
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
class MetricPoolService(
    private val metricPoolRepository: MetricPoolRepository,
    private val metricYearRepository: MetricYearRepository,
    private val currentSessionUser: CurrentSessionUser
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 지표 전체 목록 조회
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

    /**
     * 지표 신규 등록
     */
    @Transactional
    fun createMetric(metricDto: MetricDto): ZResponse {
        val status = this.checkMetricName(metricDto.metricId, metricDto.metricName.trim())
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            metricPoolRepository.save(
                MetricEntity(
                    metricName = metricDto.metricName.trim(),
                    metricDesc = metricDto.metricDesc,
                    metricGroup = metricDto.metricGroup,
                    metricType = metricDto.metricType,
                    metricUnit = metricDto.metricUnit,
                    calculationType = metricDto.calculationType,
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
     * 지표 세부 정보 조회
     */
    fun getMetricDetail(metricId: String): MetricDto {
        return metricPoolRepository.findMetric(metricId)
    }

    /**
     * 지표 편집
     */
    @Transactional
    fun updateMetric(metricId: String, metricDto: MetricDto): ZResponse {
        val status = this.checkMetricName(metricDto.metricId, metricDto.metricName.trim())
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val metricEntity = metricPoolRepository.findByMetricId(metricId)
            metricEntity.metricName = metricDto.metricName
            metricEntity.metricDesc = metricDto.metricDesc
            metricEntity.metricGroup = metricDto.metricGroup
            metricEntity.updateUserKey = currentSessionUser.getUserKey()
            metricEntity.updateDt = LocalDateTime.now()

            metricPoolRepository.save(metricEntity)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 지표 삭제
     */
    @Transactional
    fun deleteMetric(metricId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        // 연도별 지표에 존재하는지 체크
        if (metricYearRepository.existsByMetric(metricId)) {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        } else {
            metricPoolRepository.deleteById(metricId)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 지표 등록/편집 시 지표 이름 중복 체크
     */
    @Transactional
    fun checkMetricName(metricId: String, metricName: String): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS

        if (metricId.isEmpty()) {
            if (metricPoolRepository.existsByMetricName(metricName)) {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
        } else {
            val metricEntity = metricPoolRepository.getOne(metricId)
            val isExistsMetricName = metricEntity.metricName == metricName
            if (!isExistsMetricName) {
                if (metricPoolRepository.existsByMetricName(metricName)) {
                    status = ZResponseConstants.STATUS.ERROR_DUPLICATE
                }
            }
        }
        return status
    }
}
