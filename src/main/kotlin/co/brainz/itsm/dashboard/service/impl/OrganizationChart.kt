/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl


import co.brainz.itsm.dashboard.dto.TemplateComponentConfig

import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartData
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import co.brainz.itsm.dashboard.dto.OrganizationChartDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants

class OrganizationChart(
    private val wfDocumentRepository: WfDocumentRepository,
    private val organizationRepository: OrganizationRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
) : co.brainz.itsm.dashboard.service.impl.TemplateComponent {
    override fun getResult(component: TemplateComponentConfig): OrganizationChartDto {
        val target: LinkedHashMap<String, List<String>> = component.target as LinkedHashMap<String, List<String>>
        val chartDataList: MutableList<ChartData> = mutableListOf()
        val tagList: MutableList<AliceTagDto> = mutableListOf()
        val documentList = wfDocumentRepository.findByDocumentIdList(target["documents"] as MutableList<String>)
        val organizationList = organizationRepository.findByOrganizationIdList(target["organizations"] as MutableList<String>)

        for (document in documentList!!) {
            //chart를 그릴 때 tag로 이름을 설정해야 입력해야 인식이 가능하다.
            tagList.add(
                AliceTagDto(
                    tagId = document.documentId,
                    tagValue = document.documentName
                )
            )
            for (organization in organizationList!!) {
                chartDataList.add(
                    ChartData(
                        id = document.documentId,
                        category = organization.organizationName!!,
                        value = dashboardTemplateRepository.countByDocumentIdAndOrganizationIdAndStatus(
                            document.documentId, organization.organizationId, WfInstanceConstants.Status.RUNNING.code
                        ).toString(),
                        series = document.documentName,
                        linkKey = organization.organizationId
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
                        type = ChartConstants.Range.NONE.code,
                        from = null,
                        to = null
                    )
                )
            ),
            chartData = chartDataList
        )
    }

}
