package co.brainz.itsm.notice.controller

import co.brainz.itsm.notice.dto.NoticeSearchDto
import co.brainz.itsm.notice.service.NoticeService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/notices")
class NoticeController(
    private val noticeService: NoticeService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val noticeSearchPage: String = "notice/noticeSearch"
    private val noticeListPage: String = "notice/noticeList"
    private val noticeEditPage: String = "notice/noticeEdit"
    private val noticeViewPage: String = "notice/noticeView"
    private val noticePopUpPage: String = "notice/noticePopUp"

    /**
     * 공지사항 검색 화면 호출 처리
     */
    @GetMapping("/search")
    fun getNoticeSearch(request: HttpServletRequest, model: Model): String {
        return noticeSearchPage
    }

    /**
     * 공지사항 검색 결과 리스트 화면 호출 처리
     */
    @GetMapping("/list")
    fun getNoticeList(noticeSearchDto: NoticeSearchDto, model: Model): String {
        val searchValue = noticeSearchDto.searchValue
        val fromDt = LocalDateTime.parse(noticeSearchDto.fromDt, DateTimeFormatter.ISO_DATE_TIME)
        val toDt = LocalDateTime.parse(noticeSearchDto.toDt, DateTimeFormatter.ISO_DATE_TIME)
        val offset = noticeSearchDto.offset
        val noticeResult = noticeService.findNoticeSearch(searchValue, fromDt, toDt, offset)
        model.addAttribute("noticeList", noticeResult)
        model.addAttribute("noticeCount", noticeResult[0].totalCount)
        model.addAttribute("topNoticeList", noticeService.findTopNoticeSearch(searchValue))
        return noticeListPage
    }

    /**
     * 공지사항 신규 등록 화면 호출 처리
     */
    @GetMapping("/new")
    fun getNoticeNew(request: HttpServletRequest, model: Model): String {
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
        model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeId))
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
