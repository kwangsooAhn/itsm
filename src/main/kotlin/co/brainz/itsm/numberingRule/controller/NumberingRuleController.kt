package co.brainz.itsm.numberingRule.controller

import co.brainz.itsm.numberingRule.service.NumberingRuleService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@RequestMapping("/numberingRules")
@Controller
class NumberingRuleController(/*private val numberingRuleService: NumberingRuleService*/) {

    private val logger = LoggerFactory.getLogger(NumberingRuleController::class.java)
    private val numberingRuleEditPage: String = "numbering-rule/numberingRuleEdit"
    private val numberingRuleListPage: String = "numbering-rule/numberingRuleList"

    /**
     * 문서번호 편집 호출 화면.
     */
    @GetMapping("/edit")
    fun getNumberingRuleEdit(request: HttpServletRequest, model: Model): String {
        return numberingRuleEditPage
    }

    /**
     * 문서번호 리스트 호출 화면.
     */
    @GetMapping("")
    fun getNumberingRuleList(search: String, model: Model): String {
        /*model.addAttribute("ruleList", numberingRuleService.getNumberingRuleList(search))*/

        return numberingRuleListPage
    }

}
