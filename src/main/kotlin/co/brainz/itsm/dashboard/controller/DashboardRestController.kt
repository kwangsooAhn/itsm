/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.dashboard.controller

import co.brainz.itsm.dashboard.dto.DashboardStatisticDto
import co.brainz.itsm.dashboard.service.DashboardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/dashboard")
class DashboardRestController(
    private val dashboardService: DashboardService
) {
    @GetMapping("/statistic")
    fun getDashboardStatistic(): List<DashboardStatisticDto> {
        return dashboardService.getDashboardStatistic()
    }
}
