/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.controller

import co.brainz.itsm.archive.constants.ArchiveConstants
import co.brainz.itsm.archive.dto.ArchiveSearchCondition
import co.brainz.itsm.archive.service.ArchiveService
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.notice.dto.NoticeSearchCondition
import co.brainz.itsm.notice.service.NoticeService
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.service.PortalService
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/portals")
class PortalController(
    private val codeService: CodeService,
    private val archiveService: ArchiveService,
    private val noticeService: NoticeService,
    private val portalService: PortalService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val portalMainPage: String = "portal/portalMain"
    private val portalListPage: String = "portal/portalList"
    private val portalBrowserGuidePage: String = "portal/portalBrowserGuide"
    private val portalNoticeSearchPage: String = "portal/notice/noticeSearch"
    private val portalNoticeListPage: String = "portal/notice/noticeList"
    private val portalNoticeViewPage: String = "portal/notice/noticeView"
    private val portalFaqPage: String = "portal/faq/portalFaq"
    private val portalFaqListPage: String = "portal/faq/portalFaqList"
    private val portalArchiveSearchPage: String = "portal/archive/archiveSearch"
    private val portalArchiveListPage: String = "portal/archive/archiveList"
    private val portalArchiveViewPage: String = "portal/archive/archiveView"

    /**
     * ?????? ?????? ?????? ?????? ??????
     */
    @GetMapping("/main")
    fun getPortalSearch(request: HttpServletRequest, model: Model): String {
        return portalMainPage
    }

    /**
     * ?????? ?????? ????????? ?????? ??????
     */
    @GetMapping("")
    fun getPortalList(portalSearchDto: PortalSearchDto, model: Model): String {
        model.addAttribute("portalSearchValue", portalSearchDto.searchValue)
        val portalList = portalService.findPortalListOrSearchList(portalSearchDto)
        model.addAttribute("portalList", portalList.data)
        model.addAttribute("totalCount", portalList.totalCount)
        return portalListPage
    }

    @GetMapping("/browserguide")
    fun getPortalBrowserGuide(): String {
        return portalBrowserGuidePage
    }

    /**
     * ?????? ???????????? ????????????
     */
    @GetMapping("/notices/search")
    fun getNoticeSearch(request: HttpServletRequest, model: Model): String {
        return portalNoticeSearchPage
    }

    /**
     * ?????? ???????????? ????????? ??????
     */
    @GetMapping("/notices")
    fun getNoticeList(noticeSearchCondition: NoticeSearchCondition, model: Model): String {
        val topList = noticeService.findTopNotice()
        // ?????? ?????? ????????? ????????? ??????
        // ?????? ?????? = ????????? ??? ?????? ?????? - top ??????
        model.addAttribute("topNoticeList", topList)
        noticeSearchCondition.contentNumPerPage = (noticeSearchCondition.contentNumPerPage - topList.size.toLong())

        val result = noticeService.findNoticeSearch(noticeSearchCondition)
        model.addAttribute("noticeList", result.data)
        model.addAttribute("paging", result.paging)
        return portalNoticeListPage
    }

    /**
     * ?????? ???????????? ???????????? ??????
     */
    @GetMapping("/notices/{noticeId}/view")
    fun getNotice(@PathVariable noticeId: String, model: Model): String {
        model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeId))
        return portalNoticeViewPage
    }

    @GetMapping("/faqs/{faqId}/view")
    fun getPortalSearch(@PathVariable faqId: String, model: Model): String {
        model.addAttribute("faqs", portalService.getFaqCategories(faqId))
        return portalFaqPage
    }

    @GetMapping("/faqs")
    fun getPortalFaqList(
        @RequestParam(value = "category", defaultValue = "") category: String,
        @RequestParam(value = "id", defaultValue = "") id: String,
        model: Model
    ): String {
        model.addAttribute("faqs", portalService.getFaqList(category, id))
        return portalFaqListPage
    }

    /**
     * [model]??? ????????? ?????? ????????? ?????? ??????.
     */
    @GetMapping("/archives/search")
    fun getArchiveSearch(model: Model): String {
        model.addAttribute(
            "categoryList",
            codeService.selectCodeByParent(ArchiveConstants.ARCHIVE_CATEGORY_P_CODE)
        )
        return portalArchiveSearchPage
    }

    /**
     * [archiveSearchCondition], [model]??? ????????? ?????? ????????? ????????? ?????? ??????.
     */
    @GetMapping("/archives")
    fun getArchiveList(archiveSearchCondition: ArchiveSearchCondition, model: Model): String {
        val result = archiveService.getArchiveList(archiveSearchCondition)
        model.addAttribute("archiveList", result.data)
        model.addAttribute("paging", result.paging)
        return portalArchiveListPage
    }

    /**
     * [archiveId], [model]??? ????????? ?????? ????????? ?????? ?????? ???????????? ??????
     */
    @GetMapping("/archives/{archiveId}/view")
    fun getArchiveView(@PathVariable archiveId: String, model: Model): String {
        model.addAttribute("archive", archiveService.getArchiveDetail(archiveId, "view"))
        return portalArchiveViewPage
    }
}
