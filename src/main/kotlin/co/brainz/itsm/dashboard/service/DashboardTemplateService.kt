/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service

import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartData
import co.brainz.itsm.chart.dto.ChartRange
import co.brainz.itsm.dashboard.constants.DashboardConstants
import co.brainz.itsm.dashboard.dto.OrganizationChartDto
import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.dto.TemplateComponentData
import co.brainz.itsm.dashboard.dto.TemplateDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DashboardTemplateService(
    private val currentSessionUser: CurrentSessionUser,
    private val dashboardTemplateRepository: DashboardTemplateRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val organizationRepository: OrganizationRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 템플릿 정보 조회
     */
    fun getTemplate(): TemplateDto {
        val templateId = currentSessionUser.getUserTemplateId()
        val templateComponentList = this.getTemplateComponentConfigList(templateId)
        val result = this.getTemplateComponentResult(templateComponentList)
        // TODO: TemplateDto 에 화면에 전송할 추가 정보가 존재할 경우 추가
        return TemplateDto(
            templateId = templateId,
            result = result
        )
    }

    /**
     * 템플릿의 컴포넌트별 설정 정보 조회
     */
    private fun getTemplateComponentConfigList(templateId: String): List<TemplateComponentConfig> {
        val template = dashboardTemplateRepository.findById(templateId).get()
        val templateConfig = mapper.readValue(template.templateConfig, LinkedHashMap::class.java)

        return mapper.convertValue(
            templateConfig["components"],
            TypeFactory.defaultInstance().constructCollectionType(List::class.java, TemplateComponentConfig::class.java)
        )
    }

    /**
     * 템플릿 컴포넌트별 조회 (데이터 포함)
     */
    private fun getTemplateComponentResult(templateComponentList: List<TemplateComponentConfig>): List<TemplateComponentData> {
        val templateComponentResultList = mutableListOf<TemplateComponentData>()

        templateComponentList.forEach { component ->
            var result: Any? = null
            when (component.key) {
                DashboardConstants.TemplateComponent.ORGANIZATION_CHART.code -> {
                    result = this.getRequestStatusByOrganizationCharts(component)
                }
            }
            templateComponentResultList.add(
                TemplateComponentData(
                    key = component.key,
                    title = component.title,
                    result = result
                )
            )
        }
        return templateComponentResultList
    }

    /**
     *  부서별 요청현황 조회
     */
    private fun getRequestStatusByOrganizationCharts(component: TemplateComponentConfig): OrganizationChartDto {
        val target: LinkedHashMap<String, List<String>> = component.target as LinkedHashMap<String, List<String>>
        val organizationList: MutableList<String> = target["organizations"] as MutableList<String>
        val documentList: MutableList<String> = target["documents"] as MutableList<String>
        val chartDataList: MutableList<ChartData> = mutableListOf()
        val tagList: MutableList<AliceTagDto> = mutableListOf()

        for (document in documentList) {
            val documentName = wfDocumentRepository.findDocumentEntityByDocumentId(document).documentName
            //chart를 그릴 때 tag로 이름을 설정해야 입력해야 인식이 가능하다.
            tagList.add(
                AliceTagDto(
                    tagId = document,
                    tagValue = documentName
                )
            )
            for (organization in organizationList) {
                chartDataList.add(
                    ChartData(
                        id = document,
                        category = organizationRepository.findByOrganizationId(organization).organizationName!!,
                        value = dashboardTemplateRepository.countByDocumentIdAndOrganizationIdAndStatus(
                            document, organization, WfInstanceConstants.Status.RUNNING.code
                        ).toString(),
                        series = documentName
                    )
                )
            }
        }

        return OrganizationChartDto(
            chartId = component.key,
            chartName = component.title,
            chartType = ChartConstants.Type.BASIC_COLUMN.code,
            chartDesc = "",
            tags = tagList,
            //TODO 현재 출력할때 필요하지만 값이 없어 하드코딩하였다.
            chartConfig = listOf(
                ChartConfig(
                    operation = ChartConstants.Operation.COUNT.code,
                    periodUnit = ChartConstants.Unit.MONTH.code,
                    range = ChartRange(
                        type = ChartConstants.Range.BETWEEN.code,
                        from = LocalDateTime.of(2021,12,31,0,0,0),
                        to = LocalDateTime.of(2022,12,31,0,0,0)
                    )
                )
            ),
            chartData = chartDataList
        )
    }
}
