/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.dashboard.dto.OrganizationChartDto
import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartData
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import co.brainz.workflow.document.repository.WfDocumentRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

class OrganizationChart(
    private val wfDocumentRepository: WfDocumentRepository,
    private val organizationRepository: OrganizationRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
) : TemplateComponent {

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    override fun getResult(component: TemplateComponentConfig): OrganizationChartDto {
        val target = mapper.convertValue(component.target, Map::class.java)
        val documents: List<String> = mapper.convertValue(target["documents"], object : TypeReference<List<String>>() {})
        val organizations: List<String> = mapper.convertValue(target["organizations"], object : TypeReference<List<String>>() {})
        val documentEntities = wfDocumentRepository.getDocumentListByIds(documents.toSet())
        val organizationEntities = organizationRepository.getOrganizationListByIds(organizations.toSet())

        val series = mutableListOf<AliceTagDto>()
        val chartDataList = mutableListOf<ChartData>()
        documentEntities.forEach { documentEntity ->
            series.add(
                AliceTagDto(
                    tagId = documentEntity.documentId,
                    tagValue = documentEntity.documentName
                )
            )

            organizationEntities.forEach { organizationEntity ->
                chartDataList.add(
                    ChartData(
                        id = documentEntity.documentId,
                        category = organizationEntity.organizationName!!,
                        value = dashboardTemplateRepository.countByOrganizationRunningDocument(
                            documentEntity,
                            organizationEntity
                        ).toString(),
                        series = documentEntity.documentName,
                        linkKey = organizationEntity.organizationId
                    )
                )
            }
        }

        return OrganizationChartDto(
            chartId = component.key,
            chartName = component.title,
            chartType = ChartConstants.Type.BASIC_COLUMN.code,
            tags = series,
            chartConfig = this.initChartConfig(),
            chartData = chartDataList
        )
    }

    /**
     * 차트 기본 설정 정보 셋팅
     */
    private fun initChartConfig(): ChartConfig {
        return ChartConfig(
            operation = ChartConstants.Operation.COUNT.code,
            periodUnit = ChartConstants.Unit.MONTH.code,
            documentStatus = ChartConstants.DocumentStatus.ONLY_RUNNING.code,
            range = ChartRange(
                type = ChartConstants.Range.NONE.code
            )
        )
    }
}
