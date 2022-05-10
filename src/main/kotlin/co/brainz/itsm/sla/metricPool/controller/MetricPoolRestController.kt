/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.sla.metricPool.dto.MetricDto
import co.brainz.itsm.sla.metricPool.dto.MetricGroupDto
import co.brainz.itsm.sla.metricPool.service.MetricPoolService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla")
class MetricPoolRestController(
    private val metricPoolService: MetricPoolService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 신규 지표 등록 처리
     */
    @PostMapping("/metric-pool")
    fun insertMetric(@RequestBody metricDto: MetricDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricPoolService.createMetric(metricDto))
    }

    /**
     * 신규 지표 등록 처리
     */
    @PostMapping("/metric-group")
    fun insertMetricGroup(@RequestBody metricGroupDto: MetricGroupDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricPoolService.createMetricGroup(metricGroupDto))
    }
}
