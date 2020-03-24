package co.brainz.itsm.dashboard.controller

import co.brainz.itsm.dashboard.service.DashboardService
import co.brainz.itsm.token.service.TokenService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/dashboard")
class DashboardRestController(private val dashboardService: DashboardService,
                              private val tokenService: TokenService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

}
