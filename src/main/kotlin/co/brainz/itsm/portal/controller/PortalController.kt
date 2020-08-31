/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.controller

import co.brainz.itsm.faq.dto.FaqSearchRequestDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.service.PortalService
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/portal")
class PortalController(private val portalService: PortalService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val portalMainPage: String = "portal/portalMain"
    private val portalListPage: String = "portal/portalList"
    private val portalBrowserGuidePage: String = "portal/portalBrowserGuide"
    private val portalFaqPage: String = "portal/portalFaq"

    /**
     * 포탈 검색 화면 호출 처리
     */
    @GetMapping("/portalMain")
    fun getPortalSearch(request: HttpServletRequest, model: Model): String {
        return portalMainPage
    }

    /**
     * 포탈 검색 리스트 호출 처리
     */
    @GetMapping("/list")
    fun getPortalList(portalSearchDto: PortalSearchDto, model: Model): String {
        model.addAttribute("portalSearchValue", portalSearchDto.searchValue)
        model.addAttribute(
            "totalCount",
            portalService.findPortalListOrSearchCount(portalSearchDto)[0].totalCount
        )
        model.addAttribute("portalList", portalService.findPortalListOrSearchList(portalSearchDto))
        return portalListPage
    }

    @GetMapping("/browserGuide")
    fun getPortalBrowserGuide(): String {
        return portalBrowserGuidePage
    }

    @GetMapping("/faq")
    fun getPortalFaq(faqSearchRequestDto: FaqSearchRequestDto, model: Model): String {
        val result = portalService.getFaqList(faqSearchRequestDto)
        model.addAttribute("faqs", result)
        return portalFaqPage
    }
}
