/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.controller

import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metrics")
class MetricStatusController
