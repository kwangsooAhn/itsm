package co.brainz.itsm.numberingPattern.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.numberingPattern.constants.NumberingPatternConstants
import co.brainz.itsm.numberingPattern.service.NumberingPatternService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/numberingPatterns")
@Controller
class NumberingPatternController(
    private val numberingPatternService: NumberingPatternService,
    private val codeService: CodeService
) {
    private val logger = LoggerFactory.getLogger(NumberingPatternService::class.java)
    private val numberingPatternEditPage: String = "numbering-pattern/numberingPatternEdit"
    private val numberingPatternListPage: String = "numbering-pattern/numberingPatternList"

    /**
     * 패턴 편집 호출 화면.
     */
    @GetMapping("/edit")
    fun getNumberingPatternEdit(request: HttpServletRequest, model: Model): String {
        val dateList = codeService.selectCodeByParent(NumberingPatternConstants.PATTERN_TYPE_DATE_CODE)

        model.addAttribute("dateList", dateList)
        return numberingPatternEditPage
    }

    /**
     * 패턴 리스트 호출 화면.
     */
    @GetMapping("")
    fun getNumberingPatternList(search: String, model: Model): String {
        model.addAttribute("patternList", numberingPatternService.getNumberingPatternList(search))

        return numberingPatternListPage
    }
}
