/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.sla.metricPool.constants.MetricPoolConstants
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricPool.service.MetricPoolService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metric-pool")
class MetricPoolController(
    private val metricPoolService: MetricPoolService,
    private val codeService: CodeService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val metricPoolSearchPage: String = "sla/metric-pool/metricPoolSearch"
    private val metricPoolListPage: String = "sla/metric-pool/metricPoolList"
    private val metricPoolPage: String = "sla/metric-pool/metricPool"
    private val metricPoolEditPage: String = "sla/metric-pool/metricPoolEdit"

    /**
     * SLA 지표 관리 - 검색 화면 호출
     */
    @GetMapping("/search")
    fun getMetricPoolSearch(): String {
        return metricPoolSearchPage
    }

    /**
     * SLA 지표 관리 - 리스트 화면 호출
     */
    @GetMapping("")
    fun getMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition, model: Model): String {
        val result = metricPoolService.getMetricPools(metricPoolSearchCondition)
        model.addAttribute("metricPoolList", result.data)
        model.addAttribute("paging", result.paging)
        return metricPoolListPage
    }

    /**
     * SLA 지표 관리 - 신규 등록 화면 호출
     */
    @GetMapping("/new")
    fun getMetricNew(model: Model): String {
        model.addAttribute("metricGroupList", metricPoolService.getMetricGroups())
        model.addAttribute("metricTypeList", codeService.selectCodeByParent(MetricPoolConstants.METRIC_TYPE_P_CODE))
        model.addAttribute("metricUnitList", codeService.selectCodeByParent(MetricPoolConstants.METRIC_UNIT_P_CODE))
        model.addAttribute("metricCalcTypeList", codeService.selectCodeByParent(MetricPoolConstants.METRIC_CALCULATION_TYPE_P_CODE))
        return metricPoolPage
    }

    /**
     * SLA 지표 관리 - 편집 화면 호출
     */
    @GetMapping("/{metricId}/edit")
    fun getMetricEdit(@PathVariable metricId: String, model: Model): String {
        model.addAttribute("metricGroupList", metricPoolService.getMetricGroups())
        model.addAttribute("metric", metricPoolService.getMetricDetail(metricId))
        model.addAttribute("useYn", metricPoolService.isExistMetricYearByMetric(metricId))
        model.addAttribute("view", false)
        return metricPoolEditPage
    }

    /**
     * SLA 지표 관리 - 조회 화면 호출
     */
    @GetMapping("/{metricId}/view")
    fun getMetricView(@PathVariable metricId: String, model: Model): String {
        model.addAttribute("metricGroupList", metricPoolService.getMetricGroups())
        model.addAttribute("metric", metricPoolService.getMetricDetail(metricId))
        model.addAttribute("view", true)
        return metricPoolEditPage
    }
}
