/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.numberingPattern.constants.NumberingPatternConstants
import co.brainz.itsm.numberingPattern.dto.NumberingPatternSearchCondition
import co.brainz.itsm.numberingPattern.service.NumberingPatternService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/numberingPatterns")
@Controller
class NumberingPatternController(
    private val numberingPatternService: NumberingPatternService,
    private val codeService: CodeService
) {
    private val numberingPatternSearchPage: String = "numbering-pattern/numberingPatternSearch"
    private val numberingPatternListPage: String = "numbering-pattern/numberingPatternList"
    private val numberingPatternEditPage: String = "numbering-pattern/numberingPatternEdit"
    private val numberingPatternViewPage: String = "numbering-pattern/numberingPatternView"

    /**
     * 패턴 검색 화면
     */
    @GetMapping("/search")
    fun getNumberingPatternSearch(request: HttpServletRequest, model: Model): String {
        return numberingPatternSearchPage
    }

    /**
     * 패턴 리스트 화면
     */
    @GetMapping("")
    fun getNumberingPatternList(
        numberingPatternSearchCondition: NumberingPatternSearchCondition,
        model: Model
    ): String {
        val result = numberingPatternService.getNumberingPatternList(numberingPatternSearchCondition)
        model.addAttribute("patternList", result.data)
        model.addAttribute("paging", result.paging)
        return numberingPatternListPage
    }

    /**
     * 패턴 등록 화면
     */
    @GetMapping("/new")
    fun getNumberingPatternNew(request: HttpServletRequest, model: Model): String {
        val dateList = codeService.selectCodeByParent(NumberingPatternConstants.DEFAULT_DATE_FORMAT_PARENT_CODE)
        model.addAttribute("dateList", dateList)
        return numberingPatternEditPage
    }

    /**
     * 패턴 편집 화면
     */
    @GetMapping("/{numberingPatternId}/edit")
    fun getNoticeForm(@PathVariable numberingPatternId: String, model: Model): String {
        val dateList = codeService.selectCodeByParent(NumberingPatternConstants.DEFAULT_DATE_FORMAT_PARENT_CODE)
        model.addAttribute("dateList", dateList)
        model.addAttribute("pattern", numberingPatternService.getNumberingPatternsDetail(numberingPatternId))
        return numberingPatternEditPage
    }

    /**
     * 패턴 보기 화면
     */
    @GetMapping("/{numberingPatternId}/view")
    fun getNumberingPatternView(@PathVariable numberingPatternId: String, model: Model): String {
        model.addAttribute(
            "dateList",
            codeService.selectCodeByParent(NumberingPatternConstants.DEFAULT_DATE_FORMAT_PARENT_CODE)
        )
        model.addAttribute("pattern", numberingPatternService.getNumberingPatternsDetail(numberingPatternId))
        return numberingPatternViewPage
    }
}
