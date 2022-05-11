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
import co.brainz.itsm.sla.metricPool.dto.MetricGroupDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolListReturnDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricPool.entity.MetricEntity
import co.brainz.itsm.sla.metricPool.entity.MetricGroupEntity
import co.brainz.itsm.sla.metricPool.repository.MetricGroupRepository
import co.brainz.itsm.sla.metricPool.repository.MetricPoolRepository
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
    private val metricGroupRepository: MetricGroupRepository,
    private val currentSessionUser: CurrentSessionUser
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

    /**
     * SLA 지표 그룹 목록 조회
     */
    fun getMetricGroups(): MutableList<HashMap<String, String>> {
        val mapList: MutableList<HashMap<String, String>> = mutableListOf()
        val metricGroupList = metricGroupRepository.findAll()

        metricGroupList.forEach { metricGroup ->
            val map = HashMap<String, String>()
            map["metricGroupId"] = metricGroup.metricGroupId
            map["metricGroupName"] = metricGroup.metricGroupName
            mapList.add(map)
        }
        return mapList
    }

    /**
     * SLA 지표 신규 등록
     */
    @Transactional
    fun createMetric(metricDto: MetricDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        // 지표 이름 충복 체크
        if (metricPoolRepository.existsByMetricName(metricDto.metricName.trim())) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        } else {
            metricPoolRepository.save(
                MetricEntity(
                    metricName = metricDto.metricName.trim(),
                    metricDesc = metricDto.metricDesc,
                    metricGroupId = metricDto.metricGroupId,
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
     * SLA 지표 그룹 신규 등록
     */
    @Transactional
    fun createMetricGroup(metricGroupDto: MetricGroupDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        // 지표 그룹 이름 충복 체크
        if (metricGroupRepository.existsByMetricGroupName(metricGroupDto.metricGroupName.trim())) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        } else {
            metricGroupRepository.save(
                MetricGroupEntity(
                    metricGroupName = metricGroupDto.metricGroupName.trim(),
                    createUserKey = currentSessionUser.getUserKey(),
                    createDt = LocalDateTime.now()
                )
            )
        }
        return ZResponse(
            status = status.code
        )
    }
}
