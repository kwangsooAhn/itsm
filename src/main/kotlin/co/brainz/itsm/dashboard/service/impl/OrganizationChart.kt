/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl


import co.brainz.itsm.dashboard.dto.TemplateComponentConfig

import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartData
import co.brainz.itsm.chart.dto.ChartRange
import co.brainz.itsm.dashboard.dto.OrganizationChartDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants
import java.time.LocalDateTime
import org.springframework.stereotype.Component

class OrganizationChart(
    private val wfDocumentRepository: WfDocumentRepository,
    private val organizationRepository: OrganizationRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
) : co.brainz.itsm.dashboard.service.impl.TemplateComponent {
    override fun getResult(component: TemplateComponentConfig): OrganizationChartDto {
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
