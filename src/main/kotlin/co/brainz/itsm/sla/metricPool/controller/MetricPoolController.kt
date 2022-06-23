/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.sla.metricPool.constants.MetricPoolConst
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
@RequestMapping("/sla/metric-pools")
class MetricPoolController(
    private val metricPoolService: MetricPoolService,
    private val codeService: CodeService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val metricPoolSearchPage: String = "sla/metricPool/metricPoolSearch"
    private val metricPoolListPage: String = "sla/metricPool/metricPoolList"
    private val metricPoolPage: String = "sla/metricPool/metricPool"

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
        model.addAttribute("metricGroupList", codeService.selectCodeByParent(MetricPoolConst.GROUP_P_CODE))
        model.addAttribute("metricTypeList", codeService.selectCodeByParent(MetricPoolConst.TYPE_P_CODE))
        model.addAttribute("metricUnitList", codeService.selectCodeByParent(MetricPoolConst.UNIT_P_CODE))
        model.addAttribute(
            "metricCalcTypeList",
            codeService.selectCodeByParent(MetricPoolConst.CALCULATION_TYPE_P_CODE)
        )
        return metricPoolPage
    }

    /**
     * SLA 지표 관리 - 편집 화면 호출
     */
    @GetMapping("/{metricId}/edit")
    fun getMetricEdit(@PathVariable metricId: String, model: Model): String {
        model.addAttribute("metricGroupList", codeService.selectCodeByParent(MetricPoolConst.GROUP_P_CODE))
        model.addAttribute("metric", metricPoolService.getMetricDetail(metricId))
        model.addAttribute("edit", true)
        return metricPoolPage
    }

    /**
     * SLA 지표 관리 - 조회 화면 호출
     */
    @GetMapping("/{metricId}/view")
    fun getMetricView(@PathVariable metricId: String, model: Model): String {
        model.addAttribute("metric", metricPoolService.getMetricDetail(metricId))
        model.addAttribute("view", true)
        return metricPoolPage
    }
}
