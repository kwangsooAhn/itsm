package co.brainz.itsm.notice.controller

import co.brainz.itsm.common.Constants
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.service.NoticeService
import co.brainz.itsm.utility.ConvertParam
import co.brainz.itsm.user.UserEntity
import co.brainz.itsm.user.UserService
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * ### FAQ 관련 뷰 화면 호출 처리용 클래스.
 *
 * FaqController에서 처리하는 모든 호출은 View 혹은 View + Data를 반환한다.
 * 즉, View가 포함되는 호출에 대한 처리이며, 순수하게 JSON 데이터만 반환하는 경우는 FaqRestController에서 담당한다.
 *
 * @see co.brainz.itsm.notice.controller.NoticeRestController
 */

@Controller
@RequestMapping("/notices")
class NoticeController(private val userService: UserService,
                       private val noticeService: NoticeService,
                       private val convertParam: ConvertParam) {
    
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 공지사항 검색 화면 호출 처리
     */
    @GetMapping("/search")
    fun getNoticeSearch(request: HttpServletRequest, model: Model) : String {
        model.addAttribute("currentDate", LocalDateTime.now())
        model.addAttribute("addCurrentDate", LocalDateTime.now().plusDays(Constants.SEARCH_RANGE_VALUE))
        return "notice/noticeSearch"
    }

    /**
     * 공지사항 검색 결과 리스트 화면 호출 처리
     */
    @GetMapping("/list")
    fun getNoticeList(request: HttpServletRequest, model: Model): String {
        val isNoticeTitle = request.getParameter("noticeTitle")!!.toBoolean()
        val isCreateUserid = request.getParameter("createUserid")!!.toBoolean()
        val keyWord = request.getParameter("keyWord")
        var noticeList = emptyList<NoticeEntity>()
        val fromDt: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("fromDt"), "fromDt")
        val toDt: LocalDateTime = convertParam.convertToLocalDateTime(request.getParameter("toDt"), "toDt")
        
        when (isNoticeTitle && isCreateUserid) {
            true -> {
                noticeList = noticeService.findAllCheck(keyWord, fromDt, toDt)
            }
            false -> {
                if (isNoticeTitle) {
                    noticeList = noticeService.findAllByTitle(keyWord, fromDt, toDt)
                }
                if (isCreateUserid) {
                    noticeList = noticeService.findAllByWriter(keyWord, fromDt, toDt)
                }
                if (!isNoticeTitle && !isCreateUserid) {
                    noticeList = noticeService.findNoticeList()
                }
            }
        }

        model.addAttribute("addCurrentDate", LocalDateTime.now().plusDays(Constants.SEARCH_RANGE_VALUE))
        model.addAttribute("noticeList", noticeList)
        model.addAttribute("topNoticeList", noticeService.findTopNoticeList())
        return "notice/noticeList"
    }

    /**
     * 공지사항 신규 등록 화면 호출 처리
     */
    @GetMapping("/new")
    fun getFaqNew(request: HttpServletRequest, model: Model): String {
        return "notice/noticeEdit"
    }

    /**
     * 공지사항 조회 화면
     */
    @GetMapping("/{noticeId}/view")
    fun getNotice(@PathVariable noticeId: String, model: Model): String {
        model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeId))
        return "notice/noticeView"
    }

    /**
     * 공지사항 편집 화면
     */
    @GetMapping("/{noticeId}/edit")
    fun getNoticeForm(@PathVariable noticeId: String, model: Model): String {
        
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: UserEntity = userService.selectUser(userId)
 
        model.addAttribute("addCurrentDate", LocalDateTime.now().plusDays(Constants.SEARCH_RANGE_VALUE))
        model.addAttribute("notice", noticeService.findNoticeByNoticeNo(noticeId))
        model.addAttribute("userName", userDto.userName)
        return "notice/noticeEdit"
    }

    /**
     * 공지사항 팝업 생성
     */
    @GetMapping("/{noticeId}/view-pop")
    fun getNoticePopUp(@PathVariable noticeId: String, model: Model): String {

        model.addAttribute("noticePopUp", noticeService.findNoticeByNoticeNo(noticeId))
        return "notice/noticePopUp"
    }
}
