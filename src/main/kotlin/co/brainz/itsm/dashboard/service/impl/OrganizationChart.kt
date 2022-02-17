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
import co.brainz.workflow.instance.constants.WfInstanceConstants

class OrganizationChart(
    private val wfDocumentRepository: WfDocumentRepository,
    private val organizationRepository: OrganizationRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
) : co.brainz.itsm.dashboard.service.impl.TemplateComponent {
    override fun getResult(component: TemplateComponentConfig): OrganizationChartDto {
        val target = component.target as LinkedHashMap<String, List<String>>
        val chartDataList = mutableListOf<ChartData>()
        val tagList = mutableListOf<AliceTagDto>()
        val documentList = wfDocumentRepository.findDocumentEntityList(target["documents"] as MutableList<String>)
        val organizationList =
            organizationRepository.findOrganizationEntityList(target["organizations"] as MutableList<String>)

        for (document in documentList) {
            //chart를 그릴 때 tag로 이름을 설정해야 입력해야 인식이 가능하다. -> component의 정보를 입력한다.
            tagList.add(
                AliceTagDto(
                    tagId = document.documentId,
                    tagValue = document.documentName
                )
            )
            for (organization in organizationList) {
                chartDataList.add(
                    ChartData(
                        id = document.documentId,
                        category = organization.organizationName!!,
                        value = dashboardTemplateRepository.countRunningDocumentByOrganizationId(
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
            tags = tagList,
            //chartConfig의 값은 출력할 때 필요하지만 현재 가져올 값이 없어 하드코딩하였다.
            chartConfig =
                ChartConfig(
                    operation = ChartConstants.Operation.COUNT.code,
                    periodUnit = ChartConstants.Unit.MONTH.code,
                    range = ChartRange(
                        type = ChartConstants.Range.NONE.code
                    )
            ),
            chartData = chartDataList
        )
    }
}
