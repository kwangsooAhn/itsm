/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.controller

import co.brainz.itsm.code.service.CodeService
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

@Controller
@RequestMapping("/portal")
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
    private val portalNoticeViewPage: String = "portal/notice/noticeView"
    private val portalDwnloadSearchPage: String = "portal/download/downloadSearch"
    private val portalDownloadListPage: String = "portal/download/downloadList"
    private val portalDownloadViewPage: String = "portal/download/downloadView"

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
    @GetMapping("/notices/list")
    fun getNoticeList(noticeSearchDto: NoticeSearchDto, model: Model): String {
        val searchValue = noticeSearchDto.searchValue
        val fromDt = LocalDateTime.parse(noticeSearchDto.fromDt, DateTimeFormatter.ISO_DATE_TIME)
        val toDt = LocalDateTime.parse(noticeSearchDto.toDt, DateTimeFormatter.ISO_DATE_TIME)
        val offset = noticeSearchDto.offset
        val result = noticeService.findNoticeSearch(searchValue, fromDt, toDt, offset)
        model.addAttribute("noticeList", result)
        model.addAttribute("noticeCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        model.addAttribute("topNoticeList", noticeService.findTopNoticeSearch(searchValue, fromDt, toDt))
        return portalNoticeListPage
    }

    /**
     * 포탈 공지사항 상세화면 호출
     */
    @GetMapping("/notices/{noticeId}/view")
    fun getNotice(@PathVariable noticeId: String, model: Model): String {
        model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeId))
        return portalNoticeViewPage
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
        return portalDwnloadSearchPage
    }

    /**
     * [downloadSearchDto], [model]를 받아서 포탈 자료실 리스트 화면 호출.
     */
    @GetMapping("/downloads/list")
    fun getDownloadList(downloadSearchDto: DownloadSearchDto, model: Model): String {
        val result = downloadService.getDownloadList(downloadSearchDto)
        model.addAttribute("downloadList", result)
        model.addAttribute("downloadCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return portalDownloadListPage
    }

    /**
     * [downloadId], [model]를 받아서 포탈 자료실 상세 조회 화면으로 이동
     */
    @GetMapping("/downloads/{downloadId}/view")
    fun getDownloadView(@PathVariable downloadId: String, model: Model): String {
        model.addAttribute("download", downloadService.getDownload(downloadId, "view"))
        return portalDownloadViewPage
    }
}
