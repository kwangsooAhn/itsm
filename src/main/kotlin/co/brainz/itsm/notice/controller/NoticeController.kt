package co.brainz.itsm.notice.controller  

import co.brainz.itsm.certification.UserStatus
import co.brainz.itsm.common.Constants
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.service.NoticeService
import co.brainz.itsm.utility.ConvertParam
import co.brainz.itsm.user.UserEntity
import co.brainz.itsm.user.UserService
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.security.core.context.SecurityContextHolder

@Controller
class NoticeController(private val userService: UserService,
                       private val noticeService: NoticeService,
                       private val convertParam: ConvertParam) {
    
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("notices/list")
    fun getNoticeList(request: HttpServletRequest, model: Model) : String {
        model.addAttribute("currentDate", LocalDateTime.now())
        model.addAttribute("addCurrentDate", LocalDateTime.now().plusDays(Constants.SEARCH_RANGE_VALUE))
        return "notice/list"
    }

    @GetMapping("/notices/ajaxList")
    fun getNoticeSearchList(request: HttpServletRequest, model: Model): String {
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
        return "notice/ajaxList"
    }
    
    //공지사항 조회 화면
    @GetMapping("/notices/notice")
    fun getNotice(request: HttpServletRequest, model: Model): String {
        model.addAttribute("notice", noticeService.findNoticeByNoticeNo(request.getParameter("id")))
        return "notice/detail"
    }

    //공지사항 편집 화면
    @GetMapping("/notices/form")
    fun getNoticeForm(@RequestParam(value = "id", defaultValue = "") id: String, model: Model): String {
        
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: UserEntity = userService.selectUser(userId)
 
        model.addAttribute("addCurrentDate", LocalDateTime.now().plusDays(Constants.SEARCH_RANGE_VALUE))
        model.addAttribute("notice", noticeService.findNoticeByNoticeNo(id))
        model.addAttribute("userName", userDto.userName)
        return "notice/form"
    }
    
    //공지사항 팝업 생성
    @GetMapping("/notices/noticePopUp/{id}")
    fun getNoticePopUp(@PathVariable id: String, @RequestParam(required=false) value: String?, model: Model): String {

        if (value!!.toBoolean()) {
            model.addAttribute("isPopUp", "true")
        }

        model.addAttribute("noticePopUp", noticeService.findNoticeByNoticeNo(id))
        return "notice/noticePopUp"
    }
    
    @GetMapping("/index2")
    fun noticePopUp(model: Model): String {

        model.addAttribute("noticePopUp", noticeService.findNoticePopUp())

        //사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: UserEntity = userService.selectUser(userId)
        
        if (userDto.status == UserStatus.SIGNUP.code) {
            return "redirect:/certification/status"
        }
        return "index2"
    }
}
