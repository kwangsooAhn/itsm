/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.util.AliceMessageSource
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
    private val dashboardTemplateRepository: DashboardTemplateRepository,
    private val aliceMessageSource: AliceMessageSource
) : TemplateComponent {

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    override fun init(option: Map<String, Any>) {}

    override fun getResult(component: TemplateComponentConfig): OrganizationChartDto {
        val target = mapper.convertValue(component.target, Map::class.java)
        val documents: List<String> = mapper.convertValue(target["documents"], object : TypeReference<List<String>>() {})
        val organizations: List<String> = mapper.convertValue(target["organizations"], object : TypeReference<List<String>>() {})
        val documentEntities = wfDocumentRepository.getDocumentListByIds(documents.toSet())
        val organizationEntities = organizationRepository.getOrganizationListByIds(organizations.toSet())

        val series = mutableListOf<AliceTagDto>()
        series.add(
            AliceTagDto(
                tagId = "all",
                tagValue = aliceMessageSource.getMessage("dashboard.label.all")
            )
        )
        val chartDataList = mutableListOf<ChartData>()
        documentEntities.forEach { documentEntity ->
            val organizationChartData = mutableListOf<ChartData>()
            series.add(
                AliceTagDto(
                    tagId = documentEntity.documentId,
                    tagValue = documentEntity.documentName
                )
            )
            var documentCount = 0L
            organizationEntities.forEach { organizationEntity ->
                val count = dashboardTemplateRepository
                    .countByOrganizationRunningDocument(documentEntity, organizationEntity)
                documentCount += count
                organizationChartData.add(
                    ChartData(
                        id = documentEntity.documentId,
                        category = organizationEntity.organizationName!!,
                        value = count.toString(),
                        series = documentEntity.documentName,
                        linkKey = organizationEntity.organizationId
                    )
                )
            }
            // ????????? ?????? ??????
            organizationChartData.add(0,
                ChartData(
                    id = documentEntity.documentId,
                    category = aliceMessageSource.getMessage("dashboard.label.all"),
                    value = documentCount.toString(),
                    series = documentEntity.documentName
                )
            )
            chartDataList.addAll(organizationChartData)
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
     * ?????? ?????? ?????? ?????? ??????
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
