/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.controller

import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.service.PortalService
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/portal")
class PortalController(private val portalService: PortalService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val portalMainPage: String = "portal/portalMain"
    private val portalListPage: String = "portal/portalList"
    private val portalBrowserGuidePage: String = "portal/portalBrowserGuide"
    private val portalFaqPage: String = "portal/portalFaq"
    private val portalFaqListPage: String = "portal/portalFaqList"

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

    @GetMapping("/faqs")
    fun getPortalSearch(@RequestParam(value = "id") id: String?, model: Model): String {
        model.addAttribute("faqs", portalService.getFaqCategories(id))
        return portalFaqPage
    }

    @GetMapping("/faqs/list")
    fun getPortalFaqList(
        @RequestParam(value = "category", defaultValue = "") category: String,
        @RequestParam(value = "id", defaultValue = "") id: String,
        model: Model
    ): String {
        model.addAttribute("faqs", portalService.getFaqList(category, id))
        model.addAttribute("faqItem", portalService.getFaqItem(id))
        return portalFaqListPage
    }
}
