/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.controller

import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla/metrics/status")
class MetricStatusRestController(
    private val metricStatusService: MetricStatusService
) {

}
