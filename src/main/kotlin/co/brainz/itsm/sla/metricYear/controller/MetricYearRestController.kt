/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.service.MetricYearService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla/metrics")
class MetricYearRestController(
    private val metricYearService: MetricYearService
) {

    /**
     * 년도 선택 시 해당년도에 저장된 지표목록 불러오기
     */
    @GetMapping("/", "")
    fun getMetricYearList(metricLoadCondition: MetricLoadCondition): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricYearService.getYearSaveMetricList(metricLoadCondition))
    }

}
