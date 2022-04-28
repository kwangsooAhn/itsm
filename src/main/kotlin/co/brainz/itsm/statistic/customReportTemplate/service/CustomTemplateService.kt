/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReportTemplate.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.respository.CustomChartRepository
import co.brainz.itsm.statistic.customChart.service.CustomChartService
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateCondition
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateDetailDto
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateDto
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateListDto
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateListReturnDto
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateMapDto
import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateEntity
import co.brainz.itsm.statistic.customReportTemplate.entity.CustomReportTemplateMapEntity
import co.brainz.itsm.statistic.customReportTemplate.repository.CustomReportTemplateMapRepository
import co.brainz.itsm.statistic.customReportTemplate.repository.CustomReportTemplateRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CustomTemplateService(
    private val aliceUserRepository: AliceUserRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val customReportTemplateRepository: CustomReportTemplateRepository,
    private val customReportTemplateMapRepository: CustomReportTemplateMapRepository,
    private val customChartRepository: CustomChartRepository,
    private val customChartService: CustomChartService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    init {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    /**
     * 템플릿 목록 조회
     */
    fun getReportTemplateList(customReportTemplateCondition: CustomReportTemplateCondition): CustomReportTemplateListReturnDto {
        val queryResult = customReportTemplateRepository.getReportTemplateList(customReportTemplateCondition)
        val reportTemplateList = mutableListOf<CustomReportTemplateListDto>()
        queryResult.results.forEach { template ->
            val chartList = mutableListOf<ChartDto>()
            val chartIds = mutableSetOf<String>()
            template.charts?.forEach {
                chartIds.add(it.chartId)
            }

            val chartNameList: MutableList<String> = mutableListOf()
            customChartRepository.findChartDataByChartIdsTemplateId(chartIds, template.templateId).forEach { chartData ->
                chartList.add(
                    ChartDto(
                        chartId = chartData.chartId,
                        chartName = chartData.chartName,
                        chartType = chartData.chartType,
                        chartDesc = chartData.chartDesc,
                        chartConfig = mapper.readValue(
                            chartData.chartConfig,
                            ChartConfig::class.java
                        )
                    )
                )
                chartNameList.add(chartData.chartName)
            }
            reportTemplateList.add(
                CustomReportTemplateListDto(
                    templateId = template.templateId,
                    templateName = template.templateName,
                    createDt = template.createDt,
                    createUserName = template.createUser?.userName,
                    automatic = template.automatic,
                    templateDesc = template.templateDesc,
                    chartNameList = chartNameList.joinToString(", ")
                )
            )
        }

        return CustomReportTemplateListReturnDto(
            data = reportTemplateList,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = customReportTemplateRepository.count(),
                currentPageNum = customReportTemplateCondition.pageNum,
                totalPageNum = kotlin.math.ceil(queryResult.total.toDouble() / customReportTemplateCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * 템플릿 조회
     */
    fun getReportTemplateDetail(templateId: String): CustomReportTemplateDetailDto {
        val templateEntity = customReportTemplateRepository.getReportTemplateDetail(templateId)
        val reportTemplateDto = CustomReportTemplateDetailDto(
            templateId = templateEntity.templateId,
            templateName = templateEntity.templateName,
            templateDesc = templateEntity.templateDesc,
            reportName = templateEntity.reportName,
            automatic = templateEntity.automatic
        )
        val chartList = mutableListOf<ChartDto>()
        val templateMapList = templateEntity.charts?.sortedBy { data -> data.displayOrder }
        val chartIds = mutableSetOf<String>()
        templateMapList?.forEach {
            chartIds.add(it.chartId)
        }
        customChartRepository.findChartDataByChartIdsTemplateId(chartIds, templateEntity.templateId).forEach { chartData ->
            chartList.add(
                ChartDto(
                    chartId = chartData.chartId,
                    chartName = chartData.chartName,
                    chartConfig = mapper.readValue(
                        chartData.chartConfig,
                        ChartConfig::class.java
                    )
                )
            )
        }
        reportTemplateDto.charts = chartList
        return reportTemplateDto
    }

    /**
     * 템플릿 저장
     */
    @Transactional
    fun saveReportTemplate(templateData: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val templateDto = this.makeReportTemplateDto(templateData)
        val existCount =
            customReportTemplateRepository.findDuplicationTemplateName(templateDto.templateName, templateDto.templateId)
        when (existCount) {
            0L -> {
                var templateEntity = CustomReportTemplateEntity(
                    templateId = "",
                    templateName = templateDto.templateName,
                    templateDesc = templateDto.templateDesc,
                    reportName = templateDto.reportName,
                    automatic = templateDto.automatic,
                    createDt = LocalDateTime.now(),
                    createUser = aliceUserRepository.findAliceUserEntityByUserKey(currentSessionUser.getUserKey())
                )
                templateEntity = customReportTemplateRepository.save(templateEntity)

                // map
                templateDto.charts?.forEach { chart ->
                    val templateMapEntity = CustomReportTemplateMapEntity(
                        chartId = chart.chartId,
                        template = templateEntity,
                        displayOrder = chart.displayOrder
                    )
                    customReportTemplateMapRepository.save(templateMapEntity)
                }
            }
            else -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 템플릿 수정
     */
    @Transactional
    fun updateReportTemplate(templateData: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val templateDto = this.makeReportTemplateDto(templateData)
        val templateEntity = customReportTemplateRepository.findByTemplateId(templateDto.templateId)
        if (templateEntity != null) {
            val existCount =
                customReportTemplateRepository.findDuplicationTemplateName(templateDto.templateName, templateDto.templateId)
            when (existCount) {
                0L -> {
                    templateEntity.templateName = templateDto.templateName
                    templateEntity.templateDesc = templateDto.templateDesc
                    templateEntity.reportName = templateDto.reportName
                    templateEntity.automatic = templateDto.automatic
                    templateEntity.updateDt = LocalDateTime.now()
                    templateEntity.updateUser =
                        aliceUserRepository.findAliceUserEntityByUserKey(currentSessionUser.getUserKey())
                    customReportTemplateRepository.save(templateEntity)

                    // map
                    customReportTemplateMapRepository.deleteReportTemplateMapEntityByTemplate(templateEntity)
                    templateDto.charts?.forEach { chart ->
                        val templateMapEntity = CustomReportTemplateMapEntity(
                            chartId = chart.chartId,
                            template = templateEntity,
                            displayOrder = chart.displayOrder
                        )
                        customReportTemplateMapRepository.save(templateMapEntity)
                    }
                }
                else -> {
                    status = ZResponseConstants.STATUS.ERROR_DUPLICATE
                }
            }
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * Template 데이터 파싱
     */
    private fun makeReportTemplateDto(templateData: String): CustomReportTemplateDto {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val map = mapper.readValue(templateData, LinkedHashMap::class.java)
        val customReportTemplateDto = CustomReportTemplateDto(
            templateId = map["templateId"] as String,
            templateName = map["templateName"] as String,
            templateDesc = map["templateDesc"] as String,
            reportName = map["reportName"] as String,
            automatic = map["automatic"] as Boolean
        )

        if (map["charts"] != null) {
            val chartList: List<Map<String, Any>> =
                mapper.convertValue(map["charts"], object : TypeReference<List<Map<String, Any>>>() {})
            val templateMapList = mutableListOf<CustomReportTemplateMapDto>()
            chartList.forEach { chart ->
                templateMapList.add(
                    CustomReportTemplateMapDto(
                        templateId = customReportTemplateDto.templateId,
                        chartId = chart["id"] as String,
                        displayOrder = chart["order"] as Int
                    )
                )
            }
            customReportTemplateDto.charts = templateMapList
        }
        return customReportTemplateDto
    }

    /**
     * Template 삭제
     */
    @Transactional
    fun deleteReportTemplate(templateId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (val templateEntity = customReportTemplateRepository.findByTemplateId(templateId)) {
            null -> {
                status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
            }
            else -> {
                customReportTemplateMapRepository.deleteReportTemplateMapEntityByTemplate(templateEntity)
                customReportTemplateRepository.delete(templateEntity)
            }
        }
        return ZResponse(
            status = status.code
        )
    }

    fun getReportTemplateChart(chartIds: Array<String>?): List<ChartDto> {
        val chartDataList = mutableListOf<ChartDto>()
        chartIds?.forEach { chartId ->
            chartDataList.add(customChartService.getChartDetail(chartId).data as ChartDto)
        }
        return chartDataList
    }
}
