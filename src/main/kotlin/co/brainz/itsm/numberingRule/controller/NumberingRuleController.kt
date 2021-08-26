/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.controller

import co.brainz.itsm.numberingPattern.service.NumberingPatternService
import co.brainz.itsm.numberingRule.dto.NumberingPatternMapDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleSearchCondition
import co.brainz.itsm.numberingRule.service.NumberingRuleService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/numberingRules")
@Controller
class NumberingRuleController(
    private val numberingRuleService: NumberingRuleService,
    private val numberingPatternService: NumberingPatternService
) {
    private val numberingRuleSearchPage = "numbering-rule/numberingRuleSearch"
    private val numberingRuleListPage: String = "numbering-rule/numberingRuleList"
    private val numberingRuleEditPage: String = "numbering-rule/numberingRuleEdit"
    private val numberingRuleViewPage: String = "numbering-rule/numberingRuleView"

    /**
     * 문서번호 검색화면
     */
    @GetMapping("/search")
    fun getNumberingRuleSearch(request: HttpServletRequest, model: Model): String {
        return numberingRuleSearchPage
    }

    /**
     * 문서번호 리스트 화면
     */
    @GetMapping("")
    fun getNumberingRuleList(numberingRuleSearchCondition: NumberingRuleSearchCondition, model: Model): String {
        val result = numberingRuleService.getNumberingRuleList(numberingRuleSearchCondition)
        model.addAttribute("ruleList", result.data)
        model.addAttribute("paging", result.paging)
        return numberingRuleListPage
    }

    /**
     * 문서번호 등록 화면
     */
    @GetMapping("/new")
    fun getNumberingRuleNew(request: HttpServletRequest, model: Model): String {
        model.addAttribute("patternList", numberingPatternService.getPatternNameList())
        return numberingRuleEditPage
    }

    /**
     * 문서번호 보기 화면
     */
    @GetMapping("/{numberingRuleId}/view")
    fun getNumberingRuleView(@PathVariable numberingRuleId: String, model: Model): String {
        model.addAttribute("rule", numberingRuleService.getNumberingRuleDetail(numberingRuleId))
        return numberingRuleViewPage
    }

    /**
     * 문서번호 편집 화면
     */
    @GetMapping("/{numberingRuleId}/edit")
    fun getNumberingRuleEdit(@PathVariable numberingRuleId: String, model: Model): String {
        model.addAttribute("patternList", numberingPatternService.getPatternNameList())
        model.addAttribute("rule", numberingRuleService.getNumberingRuleDetail(numberingRuleId))
        model.addAttribute("byPatternOrder", Comparator.comparing(NumberingPatternMapDto::patternOrder))
        return numberingRuleEditPage
    }
}
