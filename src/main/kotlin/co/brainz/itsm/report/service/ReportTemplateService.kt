/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.service

import co.brainz.cmdb.dto.RestTemplateReturnDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.mapper.ChartMapper
import co.brainz.itsm.chart.respository.ChartRepository
import co.brainz.itsm.chart.service.ChartService
import co.brainz.itsm.report.constants.ReportConstants
import co.brainz.itsm.report.dto.ReportTemplateCondition
import co.brainz.itsm.report.dto.ReportTemplateDetailDto
import co.brainz.itsm.report.dto.ReportTemplateDto
import co.brainz.itsm.report.dto.ReportTemplateListDto
import co.brainz.itsm.report.dto.ReportTemplateListReturnDto
import co.brainz.itsm.report.dto.ReportTemplateMapDto
import co.brainz.itsm.report.entity.ReportTemplateEntity
import co.brainz.itsm.report.entity.ReportTemplateMapEntity
import co.brainz.itsm.report.repository.ReportTemplateMapRepository
import co.brainz.itsm.report.repository.ReportTemplateRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.lang.Math.ceil
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ReportTemplateService(
    private val aliceUserRepository: AliceUserRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val reportTemplateRepository: ReportTemplateRepository,
    private val reportTemplateMapRepository: ReportTemplateMapRepository,
    private val chartRepository: ChartRepository,
    private val chartService: ChartService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val chartMapper: ChartMapper = Mappers.getMapper(ChartMapper::class.java)

    /**
     * 템플릿 목록 조회
     */
    fun getReportTemplateList(reportTemplateCondition: ReportTemplateCondition): ReportTemplateListReturnDto {
        val queryResult = reportTemplateRepository.getReportTemplateList(reportTemplateCondition)
        val reportTemplateList = mutableListOf<ReportTemplateListDto>()
        queryResult.results.forEach { template ->
            val chartList = mutableListOf<ChartDto>()
            template.charts?.forEach { it ->
                val chartEntity = chartRepository.findChartEntityByChartId(it.chartId)
                if (chartEntity != null) {
                    chartList.add(chartMapper.toChartDto(chartEntity))
                }
            }
            reportTemplateList.add(
                ReportTemplateListDto(
                    templateId = template.templateId,
                    templateName = template.templateName,
                    createDt = template.createDt,
                    automatic = template.automatic,
                    templateDesc = template.templateDesc,
                    charts = chartList
                )
            )
        }

        return ReportTemplateListReturnDto(
            data = reportTemplateList,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = reportTemplateRepository.count(),
                currentPageNum = reportTemplateCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * 템플릿 조회
     */
    fun getReportTemplateDetail(templateId: String): ReportTemplateDetailDto {
        val templateEntity = reportTemplateRepository.getReportTemplateDetail(templateId)
        val reportTemplateDto = ReportTemplateDetailDto(
            templateId = templateEntity.templateId,
            templateName = templateEntity.templateName,
            templateDesc = templateEntity.templateDesc,
            reportName = templateEntity.reportName,
            automatic = templateEntity.automatic
        )
        val chartList = mutableListOf<ChartDto>()
        val templateMapList = templateEntity.charts?.sortedBy { data -> data.displayOrder }
        templateMapList?.forEach { map ->
            val chartEntity = chartRepository.findChartEntityByChartId(map.chartId)
            if (chartEntity != null) {
                chartList.add(
                    ChartDto(
                        chartId = chartEntity.chartId,
                        chartName = chartEntity.chartName
                    )
                )
            }
        }
        reportTemplateDto.charts = chartList
        return reportTemplateDto
    }

    /**
     * 템플릿 저장
     */
    @Transactional
    fun saveReportTemplate(templateData: String): RestTemplateReturnDto {
        val templateDto = this.makeReportTemplateDto(templateData)
        val existCount =
            reportTemplateRepository.findDuplicationTemplateName(templateDto.templateName, templateDto.templateId)
        val restTemplateReturnDto = RestTemplateReturnDto()
        when (existCount) {
            0L -> {
                var templateEntity = ReportTemplateEntity(
                    templateId = "",
                    templateName = templateDto.templateName,
                    templateDesc = templateDto.templateDesc,
                    reportName = templateDto.reportName,
                    automatic = templateDto.automatic,
                    createDt = LocalDateTime.now(),
                    createUser = aliceUserRepository.findAliceUserEntityByUserKey(currentSessionUser.getUserKey())
                )
                templateEntity = reportTemplateRepository.save(templateEntity)

                // map
                templateDto.charts?.forEach { chart ->
                    val templateMapEntity = ReportTemplateMapEntity(
                        chartId = chart.chartId,
                        template = templateEntity,
                        displayOrder = chart.displayOrder
                    )
                    reportTemplateMapRepository.save(templateMapEntity)
                }
            }
            else -> {
                restTemplateReturnDto.code = ReportConstants.Template.EditStatus.STATUS_ERROR_DUPLICATION.code
                restTemplateReturnDto.status = false
            }
        }
        return restTemplateReturnDto
    }

    /**
     * 템플릿 수정
     */
    @Transactional
    fun updateReportTemplate(templateData: String): RestTemplateReturnDto {
        val templateDto = this.makeReportTemplateDto(templateData)
        val templateEntity = reportTemplateRepository.findByTemplateId(templateDto.templateId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[Report Template Entity]"
        )
        val existCount =
            reportTemplateRepository.findDuplicationTemplateName(templateDto.templateName, templateDto.templateId)
        val restTemplateReturnDto = RestTemplateReturnDto()
        when (existCount) {
            0L -> {
                templateEntity.templateName = templateDto.templateName
                templateEntity.templateDesc = templateDto.templateDesc
                templateEntity.reportName = templateDto.reportName
                templateEntity.automatic = templateDto.automatic
                templateEntity.updateDt = LocalDateTime.now()
                templateEntity.updateUser =
                    aliceUserRepository.findAliceUserEntityByUserKey(currentSessionUser.getUserKey())
                reportTemplateRepository.save(templateEntity)

                // map
                reportTemplateMapRepository.deleteReportTemplateMapEntityByTemplate(templateEntity)
                templateDto.charts?.forEach { chart ->
                    val templateMapEntity = ReportTemplateMapEntity(
                        chartId = chart.chartId,
                        template = templateEntity,
                        displayOrder = chart.displayOrder
                    )
                    reportTemplateMapRepository.save(templateMapEntity)
                }
            }
            else -> {
                restTemplateReturnDto.code = ReportConstants.Template.EditStatus.STATUS_ERROR_DUPLICATION.code
                restTemplateReturnDto.status = false
            }
        }
        return restTemplateReturnDto
    }

    /**
     * Template 데이터 파싱
     */
    private fun makeReportTemplateDto(templateData: String): ReportTemplateDto {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val map = mapper.readValue(templateData, LinkedHashMap::class.java)
        val reportTemplateDto = ReportTemplateDto(
            templateId = map["templateId"] as String,
            templateName = map["templateName"] as String,
            templateDesc = map["templateDesc"] as String,
            reportName = map["reportName"] as String,
            automatic = map["automatic"] as Boolean
        )

        if (map["charts"] != null) {
            val chartList: List<Map<String, Any>> = mapper.convertValue(map["charts"], object : TypeReference<List<Map<String, Any>>>() {})
            val templateMapList = mutableListOf<ReportTemplateMapDto>()
            chartList.forEach { chart ->
                templateMapList.add(
                    ReportTemplateMapDto(
                        templateId = reportTemplateDto.templateId,
                        chartId = chart["id"] as String,
                        displayOrder = chart["order"] as Int
                    )
                )
            }
            reportTemplateDto.charts = templateMapList
        }
        return reportTemplateDto
    }

    /**
     * Template 삭제
     */
    @Transactional
    fun deleteReportTemplate(templateId: String): RestTemplateReturnDto {
        val templateEntity = reportTemplateRepository.findByTemplateId(templateId)
        val restTemplateReturnDto = RestTemplateReturnDto()
        when (templateEntity) {
            null -> {
                restTemplateReturnDto.status = false
                restTemplateReturnDto.code = ReportConstants.Template.EditStatus.STATUS_ERROR_NOT_EXIST.code
            }
            else -> {
                reportTemplateMapRepository.deleteReportTemplateMapEntityByTemplate(templateEntity)
                reportTemplateRepository.delete(templateEntity)
            }
        }
        return restTemplateReturnDto
    }

    fun getReportTemplateChart(chartIds: Array<String>?): List<ChartDto> {
        val chartDataList = mutableListOf<ChartDto>()
        chartIds?.forEach { chartId ->
            chartDataList.add(chartService.getChartDetail(chartId))
        }
        return chartDataList
    }
}
