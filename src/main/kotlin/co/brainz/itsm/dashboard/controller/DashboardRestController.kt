/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.dashboard.service.DashboardTemplateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/dashboard")
class DashboardRestController(
    private val dashboardTemplateService: DashboardTemplateService
) {

    @PostMapping("/template/{templateId}/component/{componentKey}")
    fun getOrganizationList(
        @PathVariable templateId: String,
        @PathVariable componentKey: String,
        @RequestBody options: Any?
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            dashboardTemplateService.getTemplateComponent(templateId, componentKey, options)
        )
    }
}
