/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.dashboard.controller

import co.brainz.itsm.dashboard.dto.DashboardStatisticDto
import co.brainz.itsm.token.service.TokenService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/dashboard")
class DashboardRestController(
    private val tokenService: TokenService
) {
    @GetMapping("/statistic")
    fun getDashboardStatistic(): List<DashboardStatisticDto> {
        return tokenService.getDashboardStatistic()
    }
}
