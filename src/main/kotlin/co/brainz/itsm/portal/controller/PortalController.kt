package co.brainz.itsm.portal.controller

import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.service.PortalService
import javax.servlet.http.HttpServletRequest
import kotlin.math.ceil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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
    fun getPortalList(portalSearchDto: PortalSearchDto, model: Model, @PageableDefault pageableValue: Pageable): String {
        val totalCount = portalService.findTotalCount(portalSearchDto)
        val totalPages = ceil(totalCount * 1.0 / 10).toInt()
        val pageNumber = pageableValue.pageNumber
        val pageSize = pageableValue.pageSize
        val hasPrevious = pageableValue.hasPrevious()
        val hasBefore = (totalCount.toDouble() / pageSize) > pageNumber + 1

        model.addAttribute("hasPrevious", hasPrevious)
        model.addAttribute("hasBefore", hasBefore)
        model.addAttribute("pageNumber", pageNumber)
        model.addAttribute("totalPages", totalPages)
        model.addAttribute("totalCount", totalCount)
        model.addAttribute("portalList", portalService.findPortalListOrSearchList(portalSearchDto, pageableValue))

        return portalListPage
    }

    @GetMapping("/browserGuide")
    fun getPortalBrowserGuide(): String {
        return portalBrowserGuidePage
    }
}