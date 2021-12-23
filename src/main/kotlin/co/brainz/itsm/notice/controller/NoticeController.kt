/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.controller

import co.brainz.itsm.notice.dto.NoticeSearchCondition
import co.brainz.itsm.notice.service.NoticeService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/notices")
class NoticeController(private val noticeService: NoticeService) {

    private val noticeSearchPage: String = "notice/noticeSearch"
    private val noticeListPage: String = "notice/noticeList"
    private val noticeEditPage: String = "notice/noticeEdit"
    private val noticeViewPage: String = "notice/noticeView"
    private val noticePopUpPage: String = "notice/noticePopUp"

    /**
     * 공지사항 검색 화면
     */
    @GetMapping("/search")
    fun getNoticeSearch(request: HttpServletRequest, model: Model): String {
        return noticeSearchPage
    }

    /**
     * 공지사항 검색 결과 리스트 화면
     */
    @GetMapping("")
    fun getNoticeList(noticeSearchCondition: NoticeSearchCondition, model: Model): String {
        val topList = noticeService.findTopNotice();
        // 화면 목록 스크롤 방지를 위해
        // 조회 갯수 = 페이지 당 조회 갯수 - top 갯수
        model.addAttribute("topNoticeList", topList)
        noticeSearchCondition.contentNumPerPage = (noticeSearchCondition.contentNumPerPage - topList.size.toLong())

        val result = noticeService.findNoticeSearch(noticeSearchCondition)
        model.addAttribute("noticeList", result.data)
        model.addAttribute("paging", result.paging)
        return noticeListPage
    }

    /**
     * 공지사항 신규 등록 화면
     */
    @GetMapping("/new")
    fun getNoticeNew(request: HttpServletRequest, model: Model): String {
        model.addAttribute("topNoticeCount", noticeService.findTopNotice().size.toLong())
        return noticeEditPage
    }

    /**
     * 공지사항 조회 화면
     */
    @GetMapping("/{noticeId}/view")
    fun getNotice(@PathVariable noticeId: String, model: Model): String {
        model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeId))
        return noticeViewPage
    }

    /**
     * 공지사항 편집 화면
     */
    @GetMapping("/{noticeId}/edit")
    fun getNoticeForm(@PathVariable noticeId: String, model: Model): String {
        val notice = noticeService.findNoticeByNoticeNo(noticeId)
        var topNoticeCount = noticeService.findTopNotice().size.toLong()
        if (notice.topNoticeYn) {
            topNoticeCount -= 1;
        }
        model.addAttribute("topNoticeCount", topNoticeCount)
        model.addAttribute("notice", notice)
        return noticeEditPage
    }

    /**
     * 공지사항 팝업 생성
     */
    @GetMapping("/{noticeId}/view-pop")
    fun getNoticePopUp(@PathVariable noticeId: String, model: Model): String {
        model.addAttribute("noticePopUp", noticeService.findPopupNoticeByNoticeNo(noticeId))
        return noticePopUpPage
    }
}
