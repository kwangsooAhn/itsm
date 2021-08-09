/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.download.constants.DownloadConstants
import co.brainz.itsm.download.dto.DownloadSearchDto
import co.brainz.itsm.download.service.DownloadService
import co.brainz.itsm.notice.dto.NoticeSearchDto
import co.brainz.itsm.notice.service.NoticeService
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.service.PortalService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    private val downloadService: DownloadService,
    private val noticeService: NoticeService,
    private val portalService: PortalService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val portalMainPage: String = "portal/portalMain"
    private val portalListPage: String = "portal/portalList"
    private val portalBrowserGuidePage: String = "portal/portalBrowserGuide"
    private val portalNoticeSearchPage: String = "portal/notice/noticeSearch"
    private val portalNoticeListPage: String = "portal/notice/noticeList"
    private val portalNoticeListFragment: String = "portal/notice/noticeList :: list"
    private val portalNoticeViewPage: String = "portal/notice/noticeView"
    private val portalFaqPage: String = "portal/faq/portalFaq"
    private val portalFaqListPage: String = "portal/faq/portalFaqList"
    private val portalDownloadSearchPage: String = "portal/download/downloadSearch"
    private val portalDownloadListPage: String = "portal/download/downloadList"
    private val portalDownloadListFragment: String = "portal/download/downloadList :: list"
    private val portalDownloadViewPage: String = "portal/download/downloadView"

    /**
     * 포탈 검색 화면 호출 처리
     */
    @GetMapping("/main")
    fun getPortalSearch(request: HttpServletRequest, model: Model): String {
        return portalMainPage
    }

    /**
     * 포탈 검색 리스트 호출 처리
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
     * 포탈 공지사항 검색화면
     */
    @GetMapping("/notices/search")
    fun getNoticeSearch(request: HttpServletRequest, model: Model): String {
        return portalNoticeSearchPage
    }

    /**
     * 포탈 공지사항 리스트 호출
     */
    @GetMapping("/notices")
    fun getNoticeList(noticeSearchDto: NoticeSearchDto, model: Model): String {
        val searchValue = noticeSearchDto.searchValue
        val fromDt = LocalDateTime.parse(noticeSearchDto.fromDt, DateTimeFormatter.ISO_DATE_TIME)
        val toDt = LocalDateTime.parse(noticeSearchDto.toDt, DateTimeFormatter.ISO_DATE_TIME)
        val offset = noticeSearchDto.offset
        val limit = ItsmConstants.SEARCH_DATA_COUNT
        val result = noticeService.findNoticeSearch(searchValue, fromDt, toDt, offset, limit)
        model.addAttribute("noticeList", result.data)
        model.addAttribute("noticeCount", result.totalCount)
        model.addAttribute("topNoticeList", noticeService.findTopNotice(fromDt, toDt))
        return if (noticeSearchDto.isScroll) portalNoticeListFragment else portalNoticeListPage
    }

    /**
     * 포탈 공지사항 상세화면 호출
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
     * [model]를 받아서 포탈 자료실 호출 화면.
     */
    @GetMapping("/downloads/search")
    fun getDownloadSearch(model: Model): String {
        model.addAttribute(
            "categoryList",
            codeService.selectCodeByParent(DownloadConstants.DOWNLOAD_CATEGORY_P_CODE)
        )
        return portalDownloadSearchPage
    }

    /**
     * [downloadSearchDto], [model]를 받아서 포탈 자료실 리스트 화면 호출.
     */
    @GetMapping("/downloads")
    fun getDownloadList(downloadSearchDto: DownloadSearchDto, model: Model): String {
        val result = downloadService.getDownloadList(downloadSearchDto)
        model.addAttribute("downloadList", result.data)
        model.addAttribute("downloadCount", result.totalCount)
        return if (downloadSearchDto.isScroll) portalDownloadListFragment else portalDownloadListPage
    }

    /**
     * [downloadId], [model]를 받아서 포탈 자료실 상세 조회 화면으로 이동
     */
    @GetMapping("/downloads/{downloadId}/view")
    fun getDownloadView(@PathVariable downloadId: String, model: Model): String {
        model.addAttribute("download", downloadService.getDownloadDetail(downloadId, "view"))
        return portalDownloadViewPage
    }
}
