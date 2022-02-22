/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.dashboard.controller

import co.brainz.itsm.dashboard.dto.DashboardStatisticDto
import co.brainz.itsm.dashboard.dto.TemplateComponentData
import co.brainz.itsm.dashboard.service.DashboardService
import co.brainz.itsm.dashboard.service.DashboardTemplateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/dashboard")
class DashboardRestController(
    private val dashboardService: DashboardService,
    private val dashboardTemplateService: DashboardTemplateService
) {
    @GetMapping("/statistic")
    fun getDashboardStatistic(): List<DashboardStatisticDto> {
        return dashboardService.getDashboardStatistic()
    }

    //TODO: Highchart에서 부서 클릭시 부서별 데이터 조회 URL 추가

    @GetMapping("/organization/{organizationId}")
    fun getOrganizationList(
        @PathVariable organizationId: String,
        @RequestParam(value = "templateId") templateId: String,
        @RequestParam(value = "componentKey") componentKey: String
    ): TemplateComponentData {
        val option = LinkedHashMap<String, Any>()
        option["organizationId"] = organizationId
        return dashboardTemplateService.getTemplateComponent(templateId, componentKey, option)
    }
}
